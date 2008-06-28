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

import java.lang.*;
import java.io.*;
import java.util.*;
import java.awt.*;

import jsdai.lang.*;

public class AboutBean extends JPanel {
	JLabel image = new JLabel(new ImageIcon(getClass().getResource("images/lksoft.gif")));


	JTextArea usage = new JTextArea("This is a developer tool to manage the whole SDAI environment "
		+"(session, repositories, models, schema instances) and to view and edit AIM instances. "
		+"Furthermore AIM-Instances can be viewed and edited with ARM concepts. "
		+"Using this tool requires basic knowledge of EXPRESS.");

	JLabel version = new JLabel("version");
	/* FIXME: Uncomment this to get registration code back
	JLabel registered = new JLabel("registered");
 	JLabel serial = new JLabel("serial");
 	JLabel key = new JLabel("key");*/

	JTextArea license = new JTextArea();

	JLabel copyright = new JLabel("copyright");
   GridLayout gridLayout1 = new GridLayout();

	public AboutBean() {
      try {
         jbInit();
      }
      catch(Exception e) {
         e.printStackTrace();
      }
	}

	private void jbInit() throws Exception {
		setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		usage.setWrapStyleWord(true);
      usage.setText("This is a developer tool to manage the whole SDAI environment (session, " +
    "repositories, models, schema instances) and to view and edit AIM " +
    "instances. Furthermore AIM-Instances can be viewed and edited with " +
    "ARM concepts. Using this tool requires basic knowledge of EXPRESS.");
      copyright.setHorizontalAlignment(SwingConstants.CENTER);
      copyright.setHorizontalTextPosition(SwingConstants.CENTER);
      panel.setLayout(gridLayout1);
      gridLayout1.setRows(3);
      gridLayout1.setColumns(1);
      version.setHorizontalAlignment(SwingConstants.CENTER);
	  /* FIXME: Uncomment this to get registration code back
      registered.setHorizontalAlignment(SwingConstants.CENTER);
      serial.setHorizontalAlignment(SwingConstants.CENTER);
      key.setHorizontalAlignment(SwingConstants.CENTER);*/

		usage.setBackground(this.getBackground());
		usage.setForeground(this.getForeground());
		usage.setEditable(false);
		usage.setLineWrap(true);

	  /* FIXME: Uncomment this to get registration code back
		JPanel userData = new JPanel(new GridLayout(3, 1));*/



		String lic = "";
		try {
			InputStream istream = LicenceLoader.loadLicenceAsStream();
			DataInput stream = new DataInputStream(istream);
			String c;
			while ((c = stream.readLine()) != null) {
				lic += c+'\n';
			}
		}
		catch (IOException ex) {
		}
		license.setText(lic);
		license.setForeground(this.getForeground());
		license.setBackground(this.getBackground());
		license.setEditable(false);
		JScrollPane scroll = new JScrollPane(license);
		add(scroll, BorderLayout.CENTER);

		add(copyright, BorderLayout.SOUTH);
      this.add(panel, BorderLayout.NORTH);
      panel.add(image);
      panel.add(usage);
	  /* FIXME: Uncomment this and remove the next line to get registration code back
      panel.add(userData);
      userData.add(version);
      userData.add(registered);
      userData.add(serial);*/
	  panel.add(version);
//      userData.add(key);
	}

	public void setVersion(String v) {
		version.setText(v);
	}

    public void setCopyright(String c) {
        copyright.setText(c);
    }

	public void setRegistration(String sregistered, String sserial, String skey) {
		/* FIXME: Uncomment this to get registration code back
		registered.setText("Registered to: "+sregistered);
		serial.setText("Serial: "+sserial);
		key.setText(skey);*/
	}
}