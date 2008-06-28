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
 * This interface is for internal JSDAI use only.
 * Applications shall not use it.
 */
public interface EntityDefinition extends ENamed_type {


/**
 * Checks if the entity data type, specified by this entity definition,
 * is a subtype of another entity type. The latter is submitted
 * through the method's parameter. The answer is obtained solely
 * on the basis of "SDAI_dictionary_schema" information. If null is
 * passed to the method's parameter, then <code>SdaiException</code>
 * ED_NDEF is thrown.
 * <P><B>Example:</B>
 * <P><TT><pre>    EVector vect = ...;
 *    ERepresentation_item item = ...;
 *    EEntity_definition def_vect = vect.getInstanceType();
 *    EEntity_definition def_item = item.getInstanceType();
 *    boolean is_vect_subtype_of_item = def_vect.isSubtypeOf(def_item);</TT></pre>
 * <P>
 * @param compType the given type (instance of EEntity_definition).
 * @return <code>true</code> if this entity data type is a subtype of the
 * given type; <code>false</code>, otherwise.
 * @throws SdaiException ED_NDEF, entity definition not defined.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-22::10.9.2 Is subtype of"
 */
	boolean isSubtypeOf(EEntity_definition compType) throws SdaiException;

/**
 * Checks if the entity data type, specified by this entity definition,
 * is domain equivalent with another entity type. 
 * This method is not implemented in current JSDAI version.
 * <p>
 * SdaiException FN_NAVL will be thrown if this method is invoked.
 * @param compType the given type (instance of EEntity_definition).
 * @return <code>true</code> if this entity data type is domain equivalent with
 * the given type; <code>false</code>, otherwise.
 * @throws SdaiException FN_NAVL, function not available.
 * @see "ISO 10303-22::10.9.4 Is domain equivalent with"
 */
	boolean isDomainEquivalentWith(EEntity_definition compType) throws SdaiException;

/**
 * Returns a read-only aggregate containing all explicit attributes 
 * of the specified entity data type (but not of its subtypes).
 * Redeclaring explicit attributes are also included.
 * <P><B>Example:</B>
 * <P><TT><pre>    EVector vect = ...;
 *    EEntity_definition def_vect = vect.getInstanceType();
 *    AExplicit_attribute attributes = def_vect.getExplicit_attributes(null);</TT></pre>
 * <P>
 * @param type definition of the entity of which explicit attributes are required.
 * @return aggregate containing all explicit attributes of the specified
 * entity data type.
 * @throws SdaiException SY_ERR, underlying system error.
 */
	AExplicit_attribute getExplicit_attributes(EEntity_definition type) throws SdaiException;

/**
 * Returns <code>true</code> for each available entity data type. 
 * This method is included just to keep compatibility of applications written for 
 * an older version of JSDAI, in which this method was assigned to 
 * <code>EEntity_definition</code> class of <code>jsdai.dictionary</code> package.
 * @param type definition the aggregate of explicit attributes of which is tested.
 * @return <code>true</code> value always.
 */
	boolean testExplicit_attributes(EEntity_definition type) throws SdaiException;

/**
 * Checks if the entity data type, specified by this entity definition,
 * is within the context of the specified schema. Definition of the latter is submitted
 * through the method's parameter. If the answer is positive, then 
 * instances of the checked entity data type are allowed to be created 
 * in <code>SdaiModel</code>s whose underlying schema is the schema for 
 * which this method has been executed. If the answer is negative, then 
 * creation of such instances in such <code>SdaiModel</code>s will result in 
 * throwing <code>SdaiException</code> ED_NVLD.
 * <P><B>Example:</B>
 * <P><TT><pre>    	SdaiModel mod_xim = repo.createSdaiModel("mod_xim", 
 *    SAp210_electronic_assembly_interconnect_and_packaging_design_xim.class);
 *    SdaiModel mod_kin = repo.createSdaiModel("mod_kin", SKinematic_structure_schema.class);
 *    EEntity_definition edef = mod_kin.getUnderlyingSchema().getEntityDefinition("gear_pair");
 *    boolean valid = edef.isValidFor(mod_xim.getUnderlyingSchema());</TT></pre>
 * <P>
 * @param schema the given schema definition.
 * @return <code>true</code> if this entity data type is within the context of the
 * schema; <code>false</code>, otherwise.
 * @throws SdaiException SD_NDEF, schema definition not defined.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #isValidFor(Class) isValidFor(Class)
 * @since 4.0.0
 */
	boolean isValidFor(ESchema_definition schema) throws SdaiException;

/**
 * Checks if the entity data type, specified by this entity definition,
 * is within the context of the specified schema. Java class of the latter is submitted
 * through the method's parameter. If the answer is positive, then 
 * instances of the checked entity data type are allowed to be created 
 * in <code>SdaiModel</code>s whose underlying schema is the schema for 
 * which this method has been executed. If the answer is negative, then 
 * creation of such instances in such <code>SdaiModel</code>s will result in 
 * throwing <code>SdaiException</code> ED_NVLD.
 * <P><B>Example:</B>
 * <P><TT><pre>    	SdaiModel mod_kin = repo.createSdaiModel("mod_kin", SKinematic_structure_schema.class);
 *    EEntity_definition edef = mod_kin.getUnderlyingSchema().getEntityDefinition("gear_pair");
 *    boolean valid = edef.isValidFor(SAp210_electronic_assembly_interconnect_and_packaging_design_xim.class);</TT></pre>
 * <P>
 * @param schema the given schema.
 * @return <code>true</code> if this entity data type is within the context of the
 * schema; <code>false</code>, otherwise.
 * @throws SdaiException SD_NDEF, schema definition not defined.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #isValidFor(ESchema_definition) isValidFor(ESchema_definition)
 * @since 4.0.0
 */
	boolean isValidFor(Class schema) throws SdaiException;

}
