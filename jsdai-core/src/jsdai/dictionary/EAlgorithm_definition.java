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

// Java interface for entity algorithm_definition

package jsdai.dictionary;
import jsdai.lang.*;

public interface EAlgorithm_definition extends EEntity {

	/// methods for attribute:name, base type: STRING
	public boolean testName(EAlgorithm_definition type) throws SdaiException;
	public String getName(EAlgorithm_definition type) throws SdaiException;
	public void setName(EAlgorithm_definition type, String value) throws SdaiException;
	public void unsetName(EAlgorithm_definition type) throws SdaiException;

	// methods for attribute: parameters, base type: LIST OF ENTITY
	public boolean testParameters(EAlgorithm_definition type) throws SdaiException;
	public AParameter getParameters(EAlgorithm_definition type) throws SdaiException;
	public AParameter createParameters(EAlgorithm_definition type) throws SdaiException;
	public void unsetParameters(EAlgorithm_definition type) throws SdaiException;

}
