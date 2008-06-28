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

import java.io.DataInput;
import java.io.IOException;
import java.io.UTFDataFormatException;

/**
 *
 * Created: Fri Apr 30 13:56:58 2004
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

final class StreamUTF {

	private StringBuffer strBuffer = new StringBuffer(64);
	private byte byteBuffer[] = new byte[64];

	StreamUTF() {
		
	}

    String readUTF(DataInput in) throws IOException {
        int utfLength = in.readUnsignedShort();
		if(utfLength == 0) {
			return "";
		} else {
			if(byteBuffer.length < utfLength) {
				if(byteBuffer.length * 2 > utfLength) {
					byteBuffer = new byte[byteBuffer.length * 2];
				} else {
					byteBuffer = new byte[utfLength];
				}
			}
			strBuffer.setLength(0);
			int c, char2, char3;
			int count = 0;

			in.readFully(byteBuffer, 0, utfLength);

			while (count < utfLength) {
				c = (int)byteBuffer[count] & 0xff;
				switch (c >> 4) {
				case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7:
					/* 0xxxxxxx*/
					count++;
					strBuffer.append((char)c);
					break;
				case 12: case 13:
					/* 110x xxxx   10xx xxxx*/
					count += 2;
					if (count > utfLength) {
						throw new UTFDataFormatException();
					}
					char2 = (int)byteBuffer[count-1];
					if((char2 & 0xC0) != 0x80) {
						throw new UTFDataFormatException(); 
					}
					strBuffer.append((char)(((c & 0x1F) << 6) | (char2 & 0x3F)));
					break;
				case 14:
					/* 1110 xxxx  10xx xxxx  10xx xxxx */
					count += 3;
					if (count > utfLength) {
						throw new UTFDataFormatException();
					}
					char2 = (int)byteBuffer[count-2];
					char3 = (int)byteBuffer[count-1];
					if(((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80)) {
						throw new UTFDataFormatException();	  
					}
					strBuffer.append((char)(((c     & 0x0F) << 12) |
											((char2 & 0x3F) << 6)  |
											((char3 & 0x3F) << 0)));
					break;
				default:
					/* 10xx xxxx,  1111 xxxx */
					throw new UTFDataFormatException();		  
				}
			}
			return strBuffer.substring(0).intern();
		}
	}
	
} // StreamUTF
