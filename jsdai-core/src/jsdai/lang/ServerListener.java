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

package jsdai.lang;

import java.io.*;
import java.net.*;

// Used to listen response from server, when one of server thread make commit
// 

class ServerListener implements Runnable {
	OutputStream os;
	InputStream is;
	SdaiRepository repo;
	static final String EVENT_STRING = "! EVENT";	
	ServerListener(Socket sock, SdaiRepository repository) throws java.io.IOException {
		is = sock.getInputStream();
		os = sock.getOutputStream();
		repo = repository;
	}
	
	public void run(){
		try {
			DataInputStream istream = new DataInputStream((InputStream)is);
			DataOutputStream ostream = new DataOutputStream((OutputStream)os);
			while(true) {
				byte br = istream.readByte();
				if( br == 100) { 				//ping client from server
					ostream.writeByte(1);
				}	
				if (br == 1 && repo.listenrList != null) { //user making commit
					// Guaranteed to return a non-null array
					SdaiEvent sdaiEvent = null;
					if (repo.listenrList.myLength <= 0) {
						return;
					}
					// Process the listeners last to first, notifying
					// those that are interested in this event
					if (repo.listenrList.myLength == 1) {
						if (sdaiEvent == null) {
							sdaiEvent = new SdaiEvent(repo, SdaiEvent.SERVER_DATA_CHANGED, -1, null);
						}
						((SdaiListener)repo.listenrList.myData).actionPerformed(sdaiEvent);
					} else {
						Object [] myDataA = (Object [])repo.listenrList.myData;
						for (int i = 0; i < repo.listenrList.myLength; i++) {
							if (sdaiEvent == null) {
								sdaiEvent = new SdaiEvent(repo, SdaiEvent.SERVER_DATA_CHANGED, -1, null);
							}
							((SdaiListener)myDataA[i]).actionPerformed(sdaiEvent);
						}
					}
				}
			}	
		} catch (java.io.IOException e) {};
	}	
}
