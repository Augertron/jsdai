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

import jsdai.lang.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;

public class A_stringBean extends SdaiPanel {
	private A_string strings;
	private JTextArea text = new JTextArea();

	public A_stringBean() {
		setLayout(new BorderLayout());
		text.setBackground(getBackground());
		add(new JScrollPane(text), BorderLayout.CENTER);
	}

	public A_stringBean(A_string strings) throws SdaiException {
		this();
		setA_string(strings);
	}

	public void setA_string(A_string strings) throws SdaiException {
		this.strings = strings;
		if (strings == null) {
			text.setText("null");
			return;
		}
		text.setText("");
//		text.setToolTipText(String.valueOf(strings.getAggregationType()));
		SdaiIterator it = strings.createIterator();
		while (it.next()) {
			text.append(strings.getCurrentMember(it));
		}
	}

	public A_string getA_string() {
		return strings;
	}

   public void setEditable(boolean editable) {
   	text.setEditable(editable);
   	text.setBackground((editable)?Color.white:getBackground());
   }

	public int getMode()  throws SdaiException {
		int mode = mNOT_USE;
		return mode;
	}

	public void getSelected(Vector result) throws SdaiException {
	}

	public void setSelected(Vector agg) throws SdaiException {
	}

	public boolean isSelected() throws SdaiException {
		boolean result = false;
		return result;
	}

	public void sdaiEdit() throws SdaiException {
		super.sdaiEdit();
//		editFields.setVisible(true);
	};

	public void sdaiAccept() throws SdaiException {
		super.sdaiAccept();
		strings.clear();
		for (int i = 0; i < text.getLineCount(); i++) {
			try {
				strings.addByIndex(i+1, text.getText(text.getLineStartOffset(i), text.getLineEndOffset(i)-text.getLineStartOffset(i)));
			} catch (javax.swing.text.BadLocationException e) {
//				e.printStackTrace();
			}
		}
	};

	public void sdaiCancel() throws SdaiException {
		super.sdaiCancel();
//		editFields.setVisible(false);
	};

	public void sdaiNew() throws SdaiException {
//		session.createRepository((isNull.isSelected())?null:repoName.getText(), (location.getText().equals(""))?null:location.getText());
		paintAll(getGraphics());
	};

	public void sdaiDestroy() throws SdaiException {
	};

/* this is for testing
	public static final void main(String[] args) throws SdaiException {
		A_stringBean aa = new A_stringBean();
		aa.init();
	}

	public void init() throws SdaiException {
		JFrame frame = new JFrame("A_stringBean");
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.getContentPane().add(this);

   	frame.addWindowListener(new WindowAdapter() {
      	public void windowClosing(WindowEvent e) {System.exit(0);}
   	});
   	frame.setSize(500, 300);

		SdaiSession session = SdaiSession.openSession();
		SdaiTransaction tran = session.startTransactionReadWriteAccess();
		SdaiRepository repo = session.importClearTextEncoding("", "c:\\temp\\ap203w.pf", null);
		setA_string(repo.getDescription());

   	frame.setVisible(true);
   }*/
}