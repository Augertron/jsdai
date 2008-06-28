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

package jsdai.query;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * Created: Wed Jun 18 17:24:58 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

class TwoStrings {
	String string1;
	String string2;

	TwoStrings(String string1, String string2) {
		this.string1 = string1;
		this.string2 = string2;
	}

	public boolean equals(Object obj) {
		if(obj instanceof TwoStrings) {
			TwoStrings other = (TwoStrings)obj;
			return new EqualsBuilder()
				.append(string1, other.string1)
				.append(string2, other.string2)
				.isEquals();
		}
		return false;
	}

	public int hashCode() {
		return new HashCodeBuilder(3719, 1847)
			.append(string1)
			.append(string2)
			.toHashCode();
	}
} // TwoStrings
