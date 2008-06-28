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

package jsdai.lang;

import jsdai.dictionary.*;

/**
 * Express types for use in expressions.
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */
public interface ExpressTypes { 

	/**
	 * Express <code>INTEGER</code> type.
	 */
	public static EInteger_type INTEGER_TYPE = 
		(EInteger_type)SdaiSession.findDataType("_INTEGER", SDictionary.class);

	/**
	 * Express <code>REAL</code> type.
	 */
	public static EReal_type REAL_TYPE = 
		(EReal_type)SdaiSession.findDataType("_REAL", SDictionary.class);

	/**
	 * Express <code>NUMBER</code> type.
	 */
	public static ENumber_type NUMBER_TYPE = 
		(ENumber_type)SdaiSession.findDataType("_NUMBER", SDictionary.class);

	/**
	 * Express <code>LOGICAL</code> type.
	 */
	public static ELogical_type LOGICAL_TYPE = 
		(ELogical_type)SdaiSession.findDataType("_LOGICAL", SDictionary.class);

	/**
	 * Express <code>BOOLEAN</code> type.
	 */
	public static EBoolean_type BOOLEAN_TYPE = 
		(EBoolean_type)SdaiSession.findDataType("_BOOLEAN", SDictionary.class);

	/**
	 * Express <code>STRING</code> type.
	 */
	public static EString_type STRING_TYPE = 
		(EString_type)SdaiSession.findDataType("_STRING", SDictionary.class);

	/**
	 * Express <code>BINARY</code> type.
	 */
	public static EBinary_type BINARY_TYPE = 
		(EBinary_type)SdaiSession.findDataType("_BINARY", SDictionary.class);

	/**
	 * Express <code>GENERIC_ENTITY</code> type.
	 */
	public static EData_type GENERIC_ENTITY_TYPE = 
		(EData_type)SdaiSession.findDataType("_ENTITY", SDictionary.class);

	/**
	 * Express <code>GENERIC</code> type.
	 */
	public static EData_type GENERIC_TYPE = 
		(EData_type)SdaiSession.findDataType("_GENERIC", SDictionary.class);

	/**
	 * Express <code>LIST_GENERIC</code> type.
	 */
	public static EList_type LIST_GENERIC_TYPE = 
		(EList_type)SdaiSession.findDataType("_GENERALLIST_0_GENERIC", SDictionary.class);

// Recently added

	/**
	 * Express <code>SET_GENERIC</code> type.
	 */
	public static ESet_type SET_GENERIC_TYPE = 
		(ESet_type)SdaiSession.findDataType("_GENERALSET_0_GENERIC", SDictionary.class);

	/**
	 * Express <code>SET_STRING</code> type.
	 */
	public static ESet_type SET_STRING_TYPE = 
		(ESet_type)SdaiSession.findDataType("_GENERALSET_0_STRING", SDictionary.class);

	/**
	 * Express <code>BAG_GENERIC</code> type.
	 */
	public static EBag_type BAG_GENERIC_TYPE = 
		(EBag_type)SdaiSession.findDataType("_GENERALBAG_0_GENERIC", SDictionary.class);

	/**
	 * Express <code>AGGREGATE_GENERIC</code> type.
	 */
	public static EAggregation_type AGGREGATE_GENERIC_TYPE = 
		(EAggregation_type)SdaiSession.findDataType("_AGGREGATE_GENERIC", SDictionary.class);

	/**
	 * Express <code>ARRAY_GENERIC</code> type.
     * @since 4.1.2
     */
	public static EArray_type ARRAY_GENERIC_TYPE = 
		(EArray_type)SdaiSession.findDataType("_GENERALARRAY_0_GENERIC", SDictionary.class);

}
