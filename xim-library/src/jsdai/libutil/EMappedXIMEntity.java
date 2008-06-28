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

package jsdai.libutil;

/**
 * @author Giedrius Liutkus
 *
 */
import jsdai.lang.*;

/**
 * A common supertype for all ARM entity types that support mapping operations.
 * It contains methods to access assign ARM data from AIM population and
 * to create AIM data from ARM subpopulation.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */
public interface EMappedXIMEntity extends EEntity {

	/**
	 * Creates AIM data from ARM subpopulation starting from
	 * this ARM instance.
	 *
	 * @param context The context of this operation. The following fields
	 *                of <code>SdaiContext</code> have to be set:
	 *                <code>domain</code>, <code>mappingDomain</code>, and
	 *                <code>working_model</code>
	 * @exception SdaiException if an error occurs during AIM data creation
	 *                          or in underlying JSDAI operations
	 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
	 */
	public void createAimData(SdaiContext context) throws SdaiException;

	/**
	 * Removes AIM data which corresponds this ARM instance. This method
	 * works like {@link EEntity#deleteApplicationInstance} but works on
	 * AIM population and is mapping based.
	 *
	 * @param context The context of this operation. The following fields
	 *                of <code>SdaiContext</code> have to be set:
	 *                <code>domain</code>, <code>mappingDomain</code>, and
	 *                <code>working_model</code>
	 * @exception SdaiException if an error occurs during removal of AIM data
	 *                          or in underlying JSDAI operations
	 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
	 */
	public void removeAimData(SdaiContext context) throws SdaiException;

	/*
	  Attribute state flag is ATTRIBUTES_MODIFIED if ARM instance is not linked to AIM.
	  Otherwise instance can have any of the states below.
	 */
	/**
	 * The instance state when the attribute values are unknown. All attribute values
	 * are unset but no set operations on the attribute values are permitted.
	 * The instance is in this state when the instance is returned from
	 * {@link EEntity#buildMappedInstance} method. 
	 */
	public static final int ATTRIBUTES_UNKNOWN    = 0;
	/**
	 * The instance state when the attribute values are known and not modified.
	 * Attribute values can be accessed using get and test methods and modified using 
	 * set and unset methods.
	 * The instance is in this state after {@link #applyMappedAttributes} method
	 */
	public static final int ATTRIBUTES_UNMODIFIED = 1;
	/**
	 * The instance state when the attribute values are known and at least one
	 * attribute was modified. The changes to the values can be applied to AIM data
	 * by invoking {@link #createAimData} method.
	 * The instance changes to this state from ATTRIBUTES_UNMODIFIED when any change
	 * on any attribute value is made.
	 *
	 */
	public static final int ATTRIBUTES_MODIFIED   = 2;

	/**
	 * Returns this instance attribute state.
	 *
	 * @return the attribute state
	 * @see #ATTRIBUTES_UNKNOWN
	 * @see #ATTRIBUTES_UNMODIFIED
	 * @see #ATTRIBUTES_MODIFIED
	 */

}
