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

package jsdai.mappingUtils.paths;

import antlr.*;
import antlr.collections.AST;
import java.util.List;

/**
 *
 * @author  Vaidas Nargėlas
 * @version 
 */
public class MappingSemanticException extends Exception {
	
	public AST token;
	public String message;
	private List locators;

    /**
	 * Constructs an <code>MappingSemanticException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public MappingSemanticException(AST token, String message) {
		this.token = token;
		this.message = message;
		this.locators = null;
    }

	public final void setLocators(List locators) {
		this.locators = locators;
	}

	public final List getLocators() {
		return locators;
	}
}
