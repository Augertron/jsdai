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

package jsdai.tools;

import java.util.*;

import jsdai.lang.*;
import jsdai.SExtended_dictionary_schema.*;

/**
* This class contains some usefull mothods to supprot some simple operations on attributes of entity.
* It cann't be instatiated.
* This class is used only for exteded_dictionary_schema.
*/
public class DictionaryUtils {

/** It returns a domain of an attribute.*/
	public static EEntity getDomain(EAttribute attribute) throws SdaiException {
		EEntity result = null;
		if (attribute instanceof EExplicit_attribute) {
			EExplicit_attribute ea = (EExplicit_attribute)attribute;
			result = ea.getDomain(null);
		} else if (attribute instanceof EInverse_attribute) {
			EInverse_attribute ia = (EInverse_attribute)attribute;
			result = ia.getDomain(null);
		}
		else if (attribute instanceof EDerived_attribute) {
			EDerived_attribute da = (EDerived_attribute)attribute;
			result = da.getDomain(null);
		}
		return result;
	}
}
