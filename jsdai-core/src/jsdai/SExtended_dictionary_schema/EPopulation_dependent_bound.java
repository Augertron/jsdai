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

// Java interface for entity population_dependent_bound

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public interface EPopulation_dependent_bound extends EBound {

	/// methods for attribute:schema_name, base type: STRING
	public boolean testSchema_name(EPopulation_dependent_bound type) throws SdaiException;
	public String getSchema_name(EPopulation_dependent_bound type) throws SdaiException;
	public void setSchema_name(EPopulation_dependent_bound type, String value) throws SdaiException;
	public void unsetSchema_name(EPopulation_dependent_bound type) throws SdaiException;

	/// methods for attribute:entity_name, base type: STRING
	public boolean testEntity_name(EPopulation_dependent_bound type) throws SdaiException;
	public String getEntity_name(EPopulation_dependent_bound type) throws SdaiException;
	public void setEntity_name(EPopulation_dependent_bound type, String value) throws SdaiException;
	public void unsetEntity_name(EPopulation_dependent_bound type) throws SdaiException;

	/// methods for attribute:method_name, base type: STRING
	public boolean testMethod_name(EPopulation_dependent_bound type) throws SdaiException;
	public String getMethod_name(EPopulation_dependent_bound type) throws SdaiException;
	public void setMethod_name(EPopulation_dependent_bound type, String value) throws SdaiException;
	public void unsetMethod_name(EPopulation_dependent_bound type) throws SdaiException;

	public Value getBound_value(EBound type, SdaiContext _context) throws SdaiException;
}
