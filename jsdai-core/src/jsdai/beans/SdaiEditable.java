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

public interface SdaiEditable {
	/**Add SdaiEditableListener listener.*/
	public void addSdaiEditableListener(SdaiEditableListener l);
	/**Remove SdaiEditableListener listener.*/
	public void removeSdaiEditableListener(SdaiEditableListener l);
	/**Return is currently bean in edit mode.*/
	public boolean isEdit();
	/**SdaiEditableChangedis processed to all SdaiEditableListener listeners.*/
 	public void fireSdaiEditableChanged();

	/**Bean go to edit mode, where editing will be available.*/
	public void sdaiEdit() throws SdaiException ;
	/**All changes are accepted.*/
	public void sdaiAccept() throws SdaiException;
	/**All changes are descarded.*/
	public void sdaiCancel() throws SdaiException;
	/**Dependently on selected object three possible actions are proceded: create | set | add.*/
	public void sdaiNew() throws SdaiException;
	/**Dependently on selected object three possible actions are proceded: delete | unset | remove.*/
	public void sdaiDestroy() throws SdaiException;
}