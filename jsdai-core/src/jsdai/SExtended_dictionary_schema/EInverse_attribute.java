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

// Java interface for entity inverse_attribute

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public interface EInverse_attribute extends EAttribute {

	// attribute:domain, base type: entity entity_definition
	public boolean testDomain(EInverse_attribute type) throws SdaiException;
	public EEntity_definition getDomain(EInverse_attribute type) throws SdaiException;
	public void setDomain(EInverse_attribute type, EEntity_definition value) throws SdaiException;
	public void unsetDomain(EInverse_attribute type) throws SdaiException;

	// attribute:redeclaring, base type: entity inverse_attribute
	public boolean testRedeclaring(EInverse_attribute type) throws SdaiException;
	public EInverse_attribute getRedeclaring(EInverse_attribute type) throws SdaiException;
	public void setRedeclaring(EInverse_attribute type, EInverse_attribute value) throws SdaiException;
	public void unsetRedeclaring(EInverse_attribute type) throws SdaiException;

	// attribute:inverted_attr, base type: entity explicit_attribute
	public boolean testInverted_attr(EInverse_attribute type) throws SdaiException;
	public EExplicit_attribute getInverted_attr(EInverse_attribute type) throws SdaiException;
	public void setInverted_attr(EInverse_attribute type, EExplicit_attribute value) throws SdaiException;
	public void unsetInverted_attr(EInverse_attribute type) throws SdaiException;

	// attribute:min_cardinality, base type: entity bound
	public boolean testMin_cardinality(EInverse_attribute type) throws SdaiException;
	public EBound getMin_cardinality(EInverse_attribute type) throws SdaiException;
	public void setMin_cardinality(EInverse_attribute type, EBound value) throws SdaiException;
	public void unsetMin_cardinality(EInverse_attribute type) throws SdaiException;

	// attribute:max_cardinality, base type: entity bound
	public boolean testMax_cardinality(EInverse_attribute type) throws SdaiException;
	public EBound getMax_cardinality(EInverse_attribute type) throws SdaiException;
	public void setMax_cardinality(EInverse_attribute type, EBound value) throws SdaiException;
	public void unsetMax_cardinality(EInverse_attribute type) throws SdaiException;

	/// methods for attribute:duplicates, base type: BOOLEAN
	public boolean testDuplicates(EInverse_attribute type) throws SdaiException;
	public boolean getDuplicates(EInverse_attribute type) throws SdaiException;
	public void setDuplicates(EInverse_attribute type, boolean value) throws SdaiException;
	public void unsetDuplicates(EInverse_attribute type) throws SdaiException;

}
