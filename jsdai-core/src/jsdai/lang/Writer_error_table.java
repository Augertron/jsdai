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

import java.util.*;
 
class Writer_error_table
{
	Hashtable messages;

	Writer_error_table() throws SdaiException
	{
		Object val;
		messages = new Hashtable();


					/*   Writer errors   */
		val = messages.put(new Integer(PhFileWriter.INTERNAL_ERROR_WR),
			"  Writer error: Internal error.");
		if (val != null) {
			String base = "\n" + AdditionalMessages.WR_OUT +
				"\n     Unable to initialize table of messages";
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		val = messages.put(new Integer(PhFileWriter.REDEFINE_IN_HEADER),
			"  Writer error: Redefinition is not allowed in user-defined entity in header section.");
		if (val != null) {
			String base = "\n" + AdditionalMessages.WR_OUT +
				"\n     Unable to initialize table of messages";
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		val = messages.put(new Integer(PhFileWriter.REDEFINE_IN_USER_DEFINED),
			"  Writer error: Redefinition is not allowed in user-defined entity.");
		if (val != null) {
			String base = "\n" + AdditionalMessages.WR_OUT +
				"\n     Unable to initialize table of messages";
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		val = messages.put(new Integer(PhFileWriter.TYPED_PAR_IN_HEADER),
			"  Writer error: Typed parameter is not allowed in user_defined entity in header section.");
		if (val != null) {
			String base = "\n" + AdditionalMessages.WR_OUT +
				"\n     Unable to initialize table of messages";
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		val = messages.put(new Integer(PhFileWriter.REFERENCE_IN_HEADER),
			"  Writer error: Entity reference is not allowed in user_defined entity in header section.");
		if (val != null) {
			String base = "\n" + AdditionalMessages.WR_OUT +
				"\n     Unable to initialize table of messages";
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		val = messages.put(new Integer(PhFileWriter.ENTITY_NOT_FOUND_FOR_WRITER),
			"  Writer error: Entity not found in the table.");
		if (val != null) {
			String base = "\n" + AdditionalMessages.WR_OUT +
				"\n     Unable to initialize table of messages";
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
	}

}
