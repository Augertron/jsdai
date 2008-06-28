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

import jsdai.dictionary.*;

abstract class A_basis extends SdaiCommon implements SdaiEventSource {

	SdaiCommon owner;


	final SdaiCommon getOwner() {
		if (owner instanceof SdaiListenerElement) {
			return owner.getOwner();
		}
		return owner;
	}


/*    Methods with listeners      */
	public void addSdaiListener(SdaiListener listener) {
//		synchronized (syncObject) {
			owner = new SdaiListenerElement(owner, listener);
//		} // syncObject
	}

	public void removeSdaiListener(SdaiListener listener) {
//		synchronized (syncObject) {
		SdaiCommon o = owner;
		SdaiListenerElement oeLast = null;
		while (o != null && o instanceof SdaiListenerElement) {
			SdaiListenerElement oe = (SdaiListenerElement) o;
			if (oe.listener == listener) { // remove oe
				if (oe == owner) { // if first element
					owner = oe.getOwner();
				} else {
					oeLast.owner = oe.owner;
				}
				o = null; // terminate the while loop
			} else {
				oeLast = oe;
				o = oe.owner;
			}
		}
//		} // syncObject
	}

	protected void fireSdaiEvent(int id, int item, Object argument) {
		SdaiCommon o = owner;
		SdaiEvent sdaiEvent = null;
		while (o != null && o instanceof SdaiListenerElement) {
			SdaiListenerElement oe = (SdaiListenerElement) o;
			if (sdaiEvent == null) {
				sdaiEvent = new SdaiEvent(this, id, item, argument);
			}
			oe.listener.actionPerformed(sdaiEvent);
			o = oe.owner;
		}
	}

	abstract CEntity getOwningInstance();

}
