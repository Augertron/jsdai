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

package jsdai.express_g.editors;

import org.eclipse.jface.dialogs.IInputValidator;

/**
 * @author Mantas Balnys
 *
 */
public class SchemaNameValidator implements IInputValidator {
	private String[] names = null;

	/**
	 * 
	 */
	public SchemaNameValidator(String[] names) {
		super();
		if (names != null) this.names = (String[])names.clone();
	}
	
	private boolean nameExists(String newName) {
		boolean exists = false;
		for (int i = 0; (i < names.length)&&(!exists); i++)
			exists = names[i].equalsIgnoreCase(newName); 
		return exists;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IInputValidator#isValid(java.lang.String)
	 */
	public String isValid(String newText) {
		String message = null;
		if (newText.length() == 0) message = "Enter more characters"; else
		if (newText.indexOf(" ") >= 0) message = "Spaces not allowed in model name"; else
		if (nameExists(newText)) message = "Model with same name already exists";
		return message;
	}

}
