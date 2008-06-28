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

public class SdaiServerBean extends JDialog implements ActionListener, FocusListener, ItemListener {
	
	public static final String LINK_LINKING = "LINK_LINKING";
	public static final String LINK_LINKED = "LINK_LINKED";
	public static final String UNLINK_UNLINKING = "UNLINK_UNLINKING";
	public static final String UNLINK_UNLINKED = "UNLINK_UNLINKED";
	protected static final String LINK = "LINK";
	protected static final String CLOSE = "CLOSE";
	protected static final String LINK_BUTTON = "LINK_BUTTON";
	protected static final String UNLINK_BUTTON = "UNLINK_BUTTON";
	private final static int SERVER_ENTRY_MAX = 20;

	private JButton linkButton;
	private JButton unlinkButton;

	private JPanel parameterPanel;
	private JLabel serverLabel;
	private JComboBox serverInput;
	private JLabel userLabel;
	private JTextField userInput;
	private JLabel passwordLabel;
	private JPasswordField passwordInput;
	private JCheckBox storePassFlag;
	private JPanel buttonPanel;
	private JButton linkDialogButton;
	private JButton closeButton;

	private boolean changed;
	private Properties savedProperties;
	private int lastServerMax;
	private ServerEntry orderedServers[];
	private HashMap lastServers;
	private ServerEntry selectedServerEntry;

	private EventListenerList actionListenerList = new EventListenerList();
	
	public SdaiServerBean(JFrame parent) throws SdaiException {
		super(parent, "SQL Bridge connection", true);
		initComponents();
	}

	private void initComponents() {
		linkButton = null;
		unlinkButton = null;
		parameterPanel = new JPanel();
		serverLabel = new JLabel();
		serverInput = new JComboBox();
		userLabel = new JLabel();
		userInput = new JTextField();
		passwordLabel = new JLabel();
		passwordInput = new JPasswordField();
		storePassFlag = new JCheckBox();
		buttonPanel = new JPanel();
		linkDialogButton = new JButton();
		unlinkButton = new JButton();
		closeButton = new JButton();
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		parameterPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints;
		
		serverLabel.setText("Server:");
		serverLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(3, 5, 0, 5);
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		parameterPanel.add(serverLabel, gridBagConstraints);

		serverInput.getEditor().getEditorComponent().addFocusListener(this);
		serverInput.addItemListener(this);
		serverInput.setEditable(true);
		serverInput.setFont(userInput.getFont());

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(3, 5, 0, 5);
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		parameterPanel.add(serverInput, gridBagConstraints);
		
		userLabel.setText("Username:");
		userLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new Insets(3, 5, 0, 5);
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		parameterPanel.add(userLabel, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(3, 5, 0, 5);
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		parameterPanel.add(userInput, gridBagConstraints);
		
		passwordLabel.setText("Password:");
		passwordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy = 3;
		gridBagConstraints.insets = new Insets(3, 5, 0, 5);
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		parameterPanel.add(passwordLabel, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(3, 5, 0, 5);
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		parameterPanel.add(passwordInput, gridBagConstraints);
		
		storePassFlag.setText("Store password");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.insets = new Insets(3, 5, 10, 5);
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		parameterPanel.add(storePassFlag, gridBagConstraints);
		
		getContentPane().add(parameterPanel, BorderLayout.NORTH);
		
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
		
		linkDialogButton.addActionListener(this);
		linkDialogButton.setText("Link");
		linkDialogButton.setActionCommand("LINK");
		buttonPanel.add(linkDialogButton);
		
		closeButton.addActionListener(this);
		closeButton.setText("Cancel");
		closeButton.setActionCommand("CLOSE");
		buttonPanel.add(closeButton);
		
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		pack();
		//setResizable(false);
	}

	public boolean showDialog() throws SdaiException {
		setLocationRelativeTo(getParent());
		changed = false;
		loadData();
		show();
		if(changed) {
			saveData();
			enableButtons();
		}
		return changed;
	}

	public void attachLinkButton(JButton linkButton) throws SdaiException {
		this.linkButton = linkButton;
		linkButton.addActionListener(this);
		linkButton.setActionCommand("LINK_BUTTON");
		enableButtons();
	}
	
	public void attachUnlinkButton(JButton unlinkButton) throws SdaiException {
		this.unlinkButton = unlinkButton;
		unlinkButton.addActionListener(this);
		unlinkButton.setActionCommand("UNLINK_BUTTON");
		enableButtons();
	}

	public void enableButtons() throws SdaiException {
		boolean linked = SdaiSession.getSession().getDataBaseBridge() != null;
		if(linkButton != null) linkButton.setEnabled(!linked);
		if(unlinkButton != null) unlinkButton.setEnabled(linked);
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
		try {
			savedProperties = SdaiSession.getSession().loadApplicationProperties(getClass());
			Iterator propertyIter = savedProperties.entrySet().iterator();
			lastServerMax = Integer.parseInt(savedProperties.getProperty("lastServerMax", "0"));
			lastServers = new HashMap();
			orderedServers = new ServerEntry[SERVER_ENTRY_MAX];
			serverInput.removeAllItems();
			StringTokenizer lastServerOrderString =
				new StringTokenizer(savedProperties.getProperty("lastServerOrder", ""), ",");
			while(lastServerOrderString.hasMoreTokens() && lastServers.size() <= SERVER_ENTRY_MAX) {
				int order = Integer.parseInt(lastServerOrderString.nextToken());
				String server = savedProperties.getProperty("server." + order);
				String user = savedProperties.getProperty("user." + order);
				String passwordString = savedProperties.getProperty("password." + order);
				if(server == null || user == null) {
					throw new SdaiException(SdaiException.SY_ERR, 
						"Corrupted SdaiServerBean property file");
				}
				char password[] = passwordString != null ? passwordString.toCharArray() : null;
				int index = lastServers.size();
				ServerEntry serverEntry = new ServerEntry(server, user, password, order, index);
				lastServers.put(server, serverEntry);
				orderedServers[index] = serverEntry;
				serverInput.addItem(server);
			}
			if(lastServers.size() > 0) {
				selectedServerEntry = orderedServers[0];
				serverInput.setSelectedIndex(0);
				userInput.setText(selectedServerEntry.user);
				passwordInput.setText(selectedServerEntry.password != null ? 
					new String(selectedServerEntry.password) : "");
				storePassFlag.setSelected(selectedServerEntry.password != null);
			} else {
				selectedServerEntry = null;
				serverInput.setSelectedIndex(-1);
				userInput.setText("");
				passwordInput.setText("");
				storePassFlag.setSelected(false);
			}
		}
		catch(NumberFormatException e) {
			throw new SdaiException(SdaiException.SY_ERR, e);
		}
	}

	private void saveData() throws SdaiException {
		StringBuffer lastServerOrderString = new StringBuffer();
		if(selectedServerEntry == null) {
			String server = (String)serverInput.getEditor().getItem();
			if(server == null) return;
			lastServerMax++;
			if(lastServers.size() == SERVER_ENTRY_MAX) {
				int indexToRemove = orderedServers[SERVER_ENTRY_MAX - 1].order;
				savedProperties.remove("server." + indexToRemove);
				savedProperties.remove("user." + indexToRemove);
				savedProperties.remove("password." + indexToRemove);
				orderedServers[SERVER_ENTRY_MAX - 1] = null;
			}
			savedProperties.setProperty("server." + lastServerMax, server);
			savedProperties.setProperty("user." + lastServerMax, userInput.getText());
			if(storePassFlag.isSelected()) {
				char password[] = passwordInput.getPassword();
				savedProperties.setProperty("password." + lastServerMax, new String(password));
				Arrays.fill(password, '\0');
			}
			lastServerOrderString.append(Integer.toString(lastServerMax) + ",");
			savedProperties.setProperty("lastServerMax", Integer.toString(lastServerMax));
		} else {
			savedProperties.setProperty("user." + selectedServerEntry.order, userInput.getText());
			if(storePassFlag.isSelected()) {
				char password[] = passwordInput.getPassword();
				savedProperties.setProperty("password." + selectedServerEntry.order, new String(password));
				Arrays.fill(password, '\0');
			} else {
				savedProperties.remove("password." + selectedServerEntry.order);
			}
			orderedServers[selectedServerEntry.index] = null;
			lastServerOrderString.append(Integer.toString(selectedServerEntry.order) + ",");
		}
		int endIndex = selectedServerEntry != null ? selectedServerEntry.index : lastServers.size();
		for(int index = 0; index < endIndex; index++) {
			if(orderedServers[index] != null) {
				lastServerOrderString.append(Integer.toString(orderedServers[index].order) + ",");
			}
		}
		if(selectedServerEntry != null) {
			endIndex = lastServers.size();
			for(int index = selectedServerEntry.index + 1; index < endIndex; index++) {
				if(orderedServers[index] != null) {
					lastServerOrderString.append(Integer.toString(orderedServers[index].order) + ",");
				}
			}
		}
		if(lastServerOrderString.length() != 0) {
			lastServerOrderString.deleteCharAt(lastServerOrderString.length() - 1);
		}
		savedProperties.setProperty("lastServerOrder", lastServerOrderString.toString());
		SdaiSession.getSession().storeApplicationProperties(getClass(), savedProperties, 
			"Automatically generated data. DO NOT EDIT!");
	}
	
	// ActionListener
	public void actionPerformed(ActionEvent actionEvent) {
		String actionCommand = actionEvent.getActionCommand();
		if(actionCommand.equals(CLOSE)) {
			closeDialog();
		} else if (actionCommand.equals(LINK)) {
			String server = (String)serverInput.getEditor().getItem();
			if(server.indexOf("//") < 0) {
				server = "//" + server;
			}
			SdaiSession session = SdaiSession.getSession();
			try {
				session.linkDataBaseBridge(server, userInput.getText(), passwordInput.getPassword());
				changed = true;
				closeDialog();
			}
			catch (SdaiException ex) {
				JOptionPane.showMessageDialog(this,
					 "Failed to link to SQL Bridge:\n"+
					 ex.getMessage(), "SQL Bridge connection", JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}
		} else if (actionCommand.equals(LINK_BUTTON)) {
			SdaiSession session = SdaiSession.getSession();
			try {
				fireActionPerformed(LINK_LINKING);
				showDialog();
				fireActionPerformed(LINK_LINKED);
			}
			catch (SdaiException ex) {
				JOptionPane.showMessageDialog((JFrame)getParent(),
					 "SQL Bridge link dialog failed:\n"+
					 ex.getMessage(), "SQL Bridge connection", JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}
		} else if (actionCommand.equals(UNLINK_BUTTON)) {
			SdaiSession session = SdaiSession.getSession();
			try {
				fireActionPerformed(UNLINK_UNLINKING);
				try {
					session.unlinkDataBaseBridge();
				} finally {
					enableButtons();
				}
				fireActionPerformed(UNLINK_UNLINKED);
			}
			catch (SdaiException ex) {
				JOptionPane.showMessageDialog((JFrame)getParent(),
					 "Failed to unlink from SQL Bridge:\n"+
					 ex.getMessage(), "SQL Bridge connection", JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}
		}
	}

	private void loadServerEntry(String server) {
		Object serverEntryObject = lastServers.get(server);
		if(serverEntryObject instanceof ServerEntry) {
			ServerEntry serverEntry = (ServerEntry)serverEntryObject;
			if(serverEntry != selectedServerEntry) {
				selectedServerEntry = serverEntry;
				userInput.setText(serverEntry.user);
				passwordInput.setText(serverEntry.password != null ?
					new String(serverEntry.password) : "");
				storePassFlag.setSelected(serverEntry.password != null);
			}
		} else {
			selectedServerEntry = null;
		}
	}

	// FocusListener
	public void focusLost(FocusEvent focusEvent) {
		loadServerEntry((String)serverInput.getEditor().getItem());
	}
	
	public void focusGained(FocusEvent focusEvent) { }
	
	// ItemListener
	public void itemStateChanged(ItemEvent itemEvent) {
		if(itemEvent.getStateChange() == ItemEvent.SELECTED) {
			loadServerEntry((String)itemEvent.getItem());
		}
	}
	
	static private class ServerEntry {
		String server;
		String user;
		char password[];
		int order;
		int index;
		
		ServerEntry(String server, String user, char password[], int order, int index) {
			this.server = server;
			this.user = user;
			this.password = password;
			this.order = order;
			this.index = index;
		}
	}
	
}
