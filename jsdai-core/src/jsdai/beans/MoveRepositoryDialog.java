/*
 * $Id$
 *
 * JSDAI(TM), a way to implement STEP, ISO 10303
 * Copyright (C) 1997-2008, LKSoftWare GmbH, Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License
 * version 3 as published by the Free Software Foundation (AGPL v3).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * JSDAI is a registered trademark of LKSoftWare GmbH, Germany
 * This software is also available under commercial licenses.
 * See also http://www.jsdai.net/
 */

package jsdai.beans;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

import java.util.*;

import jsdai.lang.*;
import jsdai.util.*;

public class MoveRepositoryDialog extends JDialog implements ActionListener, ItemListener, KeyListener {
	
	public static final String MOVING = "MOVING";
	public static final String MOVED = "MOVED";
	protected static final String MOVE = "MOVE";
	protected static final String CLOSE = "CLOSE";

	private JTextField dummyInput;
	private JPanel parameterPanel;
	private JLabel repositoryLabel;
	private JComboBox repositoryInput;
	private JLabel messageLabel;
	private JPanel buttonPanel;
	private JButton okButton;
	private JButton cancelButton;

	private boolean changed;
	private Map repositories;
	private SchemaInstance schemaInstance;

	private EventListenerList actionListenerList = new EventListenerList();
	
	public MoveRepositoryDialog(JFrame parent) throws SdaiException {
		super(parent, "Target repostitory", true);
		initComponents();
	}

	private void initComponents() {
		dummyInput = new JTextField();

		parameterPanel = new JPanel();
		repositoryLabel = new JLabel();
		repositoryInput = new JComboBox();
		messageLabel = new JLabel();
		buttonPanel = new JPanel();
		okButton = new JButton();
		cancelButton = new JButton();
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		parameterPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints;
		
		repositoryLabel.setText("Repository:");
		repositoryLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(3, 5, 0, 5);
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		parameterPanel.add(repositoryLabel, gridBagConstraints);
		
		repositoryInput.setMaximumRowCount(30);
		repositoryInput.setToolTipText(
			"Select repository or enter name of new local repository which will be created");
		repositoryInput.setEditable(true);
		repositoryInput.setFont(dummyInput.getFont());
		repositoryInput.getEditor().getEditorComponent().addKeyListener(this);
		repositoryInput.addItemListener(this);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.insets = new Insets(3, 5, 0, 5);
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		parameterPanel.add(repositoryInput, gridBagConstraints);
		
		messageLabel.setFont(dummyInput.getFont());
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		parameterPanel.add(messageLabel, gridBagConstraints);

		getContentPane().add(parameterPanel, BorderLayout.NORTH);
		
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
		
		okButton.setText("OK");
		okButton.setActionCommand(MOVE);
		okButton.addActionListener(this);
		
		buttonPanel.add(okButton);
		
		cancelButton.setText("Cancel");
		cancelButton.setActionCommand(CLOSE);
		cancelButton.addActionListener(this);
		buttonPanel.add(cancelButton);
		
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		outputMessage();
		pack();
	}

	public boolean moveSchemaInstance(SchemaInstance schemaInstance) throws SdaiException {
		this.schemaInstance = schemaInstance;
		setLocationRelativeTo(getParent());
		changed = false;
		loadData();
		show();
		schemaInstance = null;
		return changed;
	}

	private void performMove() throws SdaiException {
		String repositoryName = (String)repositoryInput.getEditor().getItem();
		SdaiRepository repository;
		Object repositoryObj = repositories.get(repositoryName);
		if(repositoryObj instanceof SdaiRepository) {
			repository = (SdaiRepository)repositoryObj;
		} else {
			repository = SdaiSession.getSession().createRepository(repositoryName, null);
		}
		fireActionPerformed(MOVING);
		try {
			jsdai.util.Move.moveSchemaInstance(schemaInstance, repository);
		}
		catch(SdaiException exception) {
			if(!(repositoryObj instanceof SdaiRepository)) {
				repository.deleteRepository();
			}
			throw exception;
		}
		fireActionPerformed(MOVED);
	}

    private void closeDialog() {
        setVisible(false);
        dispose();
    }
	
	public void addActionListener(ActionListener l) {
		actionListenerList.add(ActionListener.class, l);
	}
	
	public void removeActionListener(ActionListener l) {
		actionListenerList.remove(ActionListener.class, l);
	}
	
	protected void fireActionPerformed(String action) {
		ActionEvent actionEvent = null;
		Object[] listeners = actionListenerList.getListenerList();
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==ActionListener.class) {
				// Lazily create the event:
				if (actionEvent == null)
					actionEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, action);
				((ActionListener)listeners[i+1]).actionPerformed(actionEvent);
			}
		}
	}

	private void loadData() throws SdaiException {
		repositories = new TreeMap();
		repositoryInput.removeAllItems();
		ASdaiRepository repositoryAggreg = SdaiSession.getSession().getKnownServers();
		SdaiIterator repositorySdaiIter = repositoryAggreg.createIterator();
		while(repositorySdaiIter.next()) {
			SdaiRepository repository = repositoryAggreg.getCurrentMember(repositorySdaiIter);
			repositories.put(repository.getName(), repository);
		}
		Iterator repositoryIter = repositories.keySet().iterator();
		while(repositoryIter.hasNext()) {
			repositoryInput.addItem(repositoryIter.next());
		}
		repositoryInput.setSelectedIndex(-1);
		outputMessage();
	}
	
	private void outputMessage() {
		String repositoryName = (String)repositoryInput.getEditor().getItem();
		if(repositoryName.length() == 0) {
			messageLabel.setText("New temp repository will be created");
		} else if(!repositories.containsKey(repositoryName)) {
			messageLabel.setText("New repository will be created");
		} else if(repositoryName.startsWith("//")) {
			messageLabel.setText("Remote repository");
		} else {
			messageLabel.setText("Local repository");
		}
	}

	// ActionListener
	public void actionPerformed(ActionEvent actionEvent) {
		String actionCommand = actionEvent.getActionCommand();
		if(actionCommand.equals(CLOSE)) {
			closeDialog();
		} else if (actionCommand.equals(MOVE)) {
			SdaiSession session = SdaiSession.getSession();
			try {
				performMove();
				changed = true;
				closeDialog();
			}
			catch (SdaiException ex) {
				JOptionPane.showMessageDialog(this,
					 "Failed to move schema instance:\n"+
					 ex.getMessage(), "Schema instance move", JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}
		}
	}

	// KeyListener
	public void keyPressed(KeyEvent keyEvent) {	}
	
	public void keyReleased(KeyEvent keyEvent) {
		outputMessage();
	}
	
	public void keyTyped(KeyEvent keyEvent) {
	}

	// ItemListener
	public void itemStateChanged(ItemEvent itemEvent) {
		if(itemEvent.getStateChange() == ItemEvent.SELECTED) {
			outputMessage();
		}
	}
	
}