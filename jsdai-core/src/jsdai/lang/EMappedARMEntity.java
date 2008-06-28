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

import jsdai.lang.*;
import jsdai.dictionary.*;
import jsdai.mapping.*;

/**
 * A common supertype for all ARM entity types that support mapping operations.
 * It contains methods to access assign ARM data from AIM population and
 * to create AIM data from ARM subpopulation.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */
public interface EMappedARMEntity extends EEntity {

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

	/**
	 * Returns the AIM instance which corresponds this ARM instance
	 *
	 * @return The AIM instance or null if there is no AIM instance linked
	 *         to this ARM instance.
	 */
	public EEntity getAimInstance();

	/**
	 * Sets AIM instance which corresponds this ARM instance. Invoking
	 * this methods establishes two directional link between ARM and AIM.
	 *
	 * @param aimInstance The AIM instance to be linked to this ARM instance
	 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
	 */
	public void setAimInstance(EEntity aimInstance);

	/**
	 * Returns next ARM instance which is linked to the same AIM instance as this one.
	 *
	 * @return The next ARM instance or null if there is no more ARM instance linked
	 *         to the same AIM instance.
     * @since 3.6.0
	 */
	public EMappedARMEntity getNextArmInstance();

	/**
	 * Assigns (sets) value of mapped attribute.
	 *
	 * @param context The context of this operation. The following fields
	 *                of <code>SdaiContext</code> have to be set:
	 *                <code>domain</code>, <code>mappingDomain</code>, 
	 *                <code>working_model</code>, and <code>mappedWorkingModel</code>
	 * @param attribute The attribute to assign value to
	 * @param attrValue The attribute value. This can be one of:<ul>
	 *                  <li>object corresponding simple type;</li>
	 *                  <li><code>EEntity</code> for entity instances;</li>
	 *                  <li>either <code>AEntity</code> or {@link java.util.Collection}
	 *                  for aggregates</li></ul>
	 *                  <code>EEntity</code> instances have to be target (AIM) instances
	 * @param genAttMapping The <code>EGeneric_attribute_mapping</code> which provides
	 *                      type information for the value
	 * @return true is value was assigned to the attribute and false otherwise.
	 * @exception SdaiException if an error occurs during attribute assignment
	 *                          from AIM data or in underlying JSDAI operations
	 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
     * @since 3.6.0
	 */
	public boolean assignMappedValue(SdaiContext context, EExplicit_attribute attribute, Object attrValue,
									 EGeneric_attribute_mapping genAttMapping) throws SdaiException;

	/**
	 * Assigns (sets) value of mapped attribute.
	 *
	 * @param context The context of this operation. The following fields
	 *                of <code>SdaiContext</code> have to be set:
	 *                <code>mappedWorkingModel</code>
	 * @param attribute The attribute to assign value to
	 * @param attrValue The attribute value. This can be one of:<ul>
	 *                  <li>object corresponding simple type;</li>
	 *                  <li><code>EEntity</code> for entity instances;</li>
	 *                  <li>either <code>AEntity</code> or {@link java.util.Collection}
	 *                  for aggregates</li></ul>
	 *                  <code>EEntity</code> instances have to be target (AIM) instances
	 * @param dataType The data type aggregate which provides select path information.
	 *                 This parameter has to be not null only when the attribute domain is select
	 *                 and attribute value is of simple type of aggregate of simple types
	 * @return true is value was assigned to the attribute and false otherwise
	 * @exception SdaiException if an error occurs during attribute assignment
	 *                          from AIM data or in underlying JSDAI operations
	 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
     * @since 3.6.0
	 */
	public boolean assignMappedValue(SdaiContext context, EExplicit_attribute attribute, Object attrValue,
									 ANamed_type dataType) throws SdaiException;

	/**
	 * Applies mapped (AIM) attribute values to this ARM instance.
	 * This instance has to be in ATTRIBUTES_UNKNOWN state for AIM
	 * attribute values to be applied. An <code>SdaiException</code>
	 * is thrown if this instance is in ATTRIBUTES_MODIFIED state.
	 * If the instance is in ATTRIBUTES_UNMODIFIED state then no actions
	 * are taken.
	 *
	 * @param context The context of this operation. The following fields
	 *                of <code>SdaiContext</code> have to be set:
	 *                <code>domain</code>, <code>mappingDomain</code>, 
	 *                <code>working_model</code>, and <code>mappedWorkingModel</code>
	 * @return True is attribute value were applied and false if no actions were taken
	 * @exception SdaiException if an error occurs during attribute assignment
	 *                           from AIM data or in underlying JSDAI operations
	 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
	 */
	public boolean applyMappedAttributes(SdaiContext context) throws SdaiException;

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
	public int getAttributeState();

	/**
	 * Sets this instance attribute state. Changing the attribute state by calling
	 * this method can lead to unpredictable results therefore this method is
	 * primarily for internal use.
	 *
	 * @param attributeState the new instance attribute state
	 * @see #ATTRIBUTES_UNKNOWN
	 * @see #ATTRIBUTES_UNMODIFIED
	 * @see #ATTRIBUTES_MODIFIED
     * @since 3.6.0
	 */
	public void setAttributeState(int attributeState);

	/**
	 * Returns the AIM entity type definition to which this instance maps.
	 *
	 * @param context The context of this operation. The following fields
	 *                of <code>SdaiContext</code> have to be set:
	 *                <code>domain</code> and <code>mappingDomain</code>, 
	 * @return the AIM entity type definition
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
	 */
	public EEntity_definition getMappingTarget(SdaiContext context) throws SdaiException;

	// set mappingTarget member inside - CMappedARMEntity unsets it in this method
    /**
	 * Unsets (clears) the AIM entity type definition to which this instance maps.
	 *
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
	 */
	public void unsetMappingTarget() throws SdaiException;

	/*
	  Do the following methods have to be defined in the interface ?
	*/
    /**
	 * Sets the AIM entity type definition to which this instance is
	 * supposed to map.
	 *
	 * @param edef  the AIM entity type definition
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
	 */
	public void setMappingTarget(EEntity_definition edef) throws SdaiException;

	/**
	 * Returns the attribute mapping of an attribute. This is a helper method.
	 *
	 * @param attr explicit attribute definition to get the mapping for
	 * @return the generic mapping of the attribute
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
	 */
	public EGeneric_attribute_mapping getAttributeMapping(EExplicit_attribute attr) throws SdaiException;

	/**
	 * Returns the entity_mappings alternatives for this instance type.
	 * This is a helper method.
	 *
	 * @param context The context of this operation. The following fields
	 *                of <code>SdaiContext</code> have to be set:
	 *                <code>mappingDomain</code>, 
	 * @param mappings the output parameter, <code>AEntity_mapping</code> aggregate which
	 *                 receives found entity mappings.
	 * @param mode currently not used and should be equal to {@link EEntity#NO_RESTRICTIONS}
	 * @return the number of found entity mappings
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
	 */
	public int findARMEntityMappings(SdaiContext context, AEntity_mapping mappings, int mode) 
		throws SdaiException;
}
