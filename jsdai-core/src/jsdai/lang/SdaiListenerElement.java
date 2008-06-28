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

package jsdai.lang; import jsdai.dictionary.*;

import java.util.EventListener;

/** keeps the reference to an SdaiListner in the owner queue */
class SdaiListenerElement extends SdaiCommon {

	SdaiCommon owner;
	SdaiListener listener;

	SdaiListenerElement(SdaiCommon o, SdaiListener l) {
//		SdaiCommon owner = o;
//		SdaiListener listener = l;
		owner = o;
		listener = l;
	}

	final SdaiCommon getOwner() {
		// loop recursively through SdaiListenerElements
		if (owner instanceof SdaiListenerElement) {
			return owner.getOwner();
		} else {
			return owner;
		}
	}

	final void modified() throws SdaiException {
		if (owner != null) {
			owner.modified();
		}
	}

}

