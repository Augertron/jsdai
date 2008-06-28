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

// Java interface for entity domain_equivalent_type

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public interface EDomain_equivalent_type extends EEntity {

	// attribute:external_type, base type: entity named_type
	public boolean testExternal_type(EDomain_equivalent_type type) throws SdaiException;
	public ENamed_type getExternal_type(EDomain_equivalent_type type) throws SdaiException;
	public void setExternal_type(EDomain_equivalent_type type, ENamed_type value) throws SdaiException;
	public void unsetExternal_type(EDomain_equivalent_type type) throws SdaiException;

	// attribute:native_type, base type: entity named_type
	public boolean testNative_type(EDomain_equivalent_type type) throws SdaiException;
	public ENamed_type getNative_type(EDomain_equivalent_type type) throws SdaiException;
	public void setNative_type(EDomain_equivalent_type type, ENamed_type value) throws SdaiException;
	public void unsetNative_type(EDomain_equivalent_type type) throws SdaiException;

	// attribute:owner, base type: entity external_schema
	public boolean testOwner(EDomain_equivalent_type type) throws SdaiException;
	public EExternal_schema getOwner(EDomain_equivalent_type type) throws SdaiException;
	public void setOwner(EDomain_equivalent_type type, EExternal_schema value) throws SdaiException;
	public void unsetOwner(EDomain_equivalent_type type) throws SdaiException;

}
