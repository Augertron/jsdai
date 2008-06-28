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

// Java interface for entity aggregate_target_parameter

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public interface EAggregate_target_parameter extends ETarget_parameter {

	// attribute:lower_bound, base type: entity bound
	public boolean testLower_bound(EAggregate_target_parameter type) throws SdaiException;
	public EBound getLower_bound(EAggregate_target_parameter type) throws SdaiException;
	public void setLower_bound(EAggregate_target_parameter type, EBound value) throws SdaiException;
	public void unsetLower_bound(EAggregate_target_parameter type) throws SdaiException;

	// attribute:upper_bound, base type: entity bound
	public boolean testUpper_bound(EAggregate_target_parameter type) throws SdaiException;
	public EBound getUpper_bound(EAggregate_target_parameter type) throws SdaiException;
	public void setUpper_bound(EAggregate_target_parameter type, EBound value) throws SdaiException;
	public void unsetUpper_bound(EAggregate_target_parameter type) throws SdaiException;

}
