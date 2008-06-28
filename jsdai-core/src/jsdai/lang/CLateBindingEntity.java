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

/** This is for internal JSDAI use only. Applications shall not use this */
final class CLateBindingEntity extends CEntity {
	
	EEntity_definition definition;


/*------------------------ Constructors ----------*/
	CLateBindingEntity() {
	}


	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		// not implemented today
	}

	public EEntity_definition getInstanceType() {
		return (EEntity_definition) definition;
	}

//	protected int getEntityExtentIndex() {
//		return -1;
//	}

	protected EEntity_definition getEntityDefinition() {
		return null;
	}


	protected void setAll(ComplexEntityValue av) throws SdaiException {
	}

	protected void getAll(ComplexEntityValue av) throws SdaiException {
	}

}
