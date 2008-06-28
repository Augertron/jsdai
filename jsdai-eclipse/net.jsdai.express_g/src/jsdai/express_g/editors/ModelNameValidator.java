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

import jsdai.express_g.SdaieditPlugin;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiRepository;

import org.eclipse.jface.dialogs.IInputValidator;

/**
 * @author Mantas Balnys
 *
 */
public class ModelNameValidator implements IInputValidator {
	private SdaiRepository repository;
	private String extension;

	/**
	 * checks if model exist in the repository
	 * @param repository SdaiRepository, must be opened
	 * @param extension model name extension (<real model name> = <entered name> + extension)
	 */
	public ModelNameValidator(SdaiRepository repository, String extension) {
		super();
		this.repository = repository;
		this.extension = extension;
	}
	
	private boolean nameExists(String newName) {
		SdaiModel model = null;
		try {
			model = repository.findSdaiModel((newName + extension).toUpperCase());
		} catch (SdaiException sex) {
			SdaieditPlugin.log(sex);
		}
		return model != null;
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
