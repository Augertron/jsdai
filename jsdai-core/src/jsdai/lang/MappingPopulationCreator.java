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

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import jsdai.mapping.EEntity_mapping;

/**
 * A <code>MappingPopulationCreator</code> is used as callbacks for
 * AIM2ARM conversion algorithm. Classes implementing this interface
 * ensure actual ARM data creation. AIM2ARM conversion algorithm just
 * calls methods of this interface with the collected information.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 * @see SdaiSession#convertMapping
 * @see MappingContext
 * @since 3.6.0
 */

public interface MappingPopulationCreator {
	
	/**
	 * Creates source (ARM) instances. This method is a call back method
	 * called as part of AIM2ARM conversion during <code>convertMapping</code> invocation.
	 * It is invoked by the algorithm only once per entity mapping. The conversion algorithm
	 * guarantees that each entity instance passed to <code>setSourceAttributeValues</code>
	 * through <code>sourceValues</code> parameter is previosly passed to
	 * <code>createSourceInstance</code> method through <code>targetInstances</code> parameter.
	 *
	 * @param type the entity mapping to source (ARM) type of the instances
	 * @param targetInstances the unique collection of instances in target (AIM) population
	 *        matching source (ARM) entiy type identified by <code>type</code> parameter
	 * @exception SdaiException if underlying JSDAI exception occurs
	 * @see #setSourceAttributeValues
	 */
	public void createSourceInstance(EEntity_mapping type,
									 Collection targetInstances) throws SdaiException;

	/**
	 * Assigns attribute values to source (ARM) instance. This method is a call back method
	 * called as part of AIM2ARM conversion during <code>convertMapping</code> invocation.
	 * It is invoked by the algorithm only once per <code>type</code>,
	 * <code>targetInstances</code> pair.
	 *
	 * @param type the entity mapping to source (ARM) type of the instance
	 * @param targetInstance the target (AIM) instance which matches source (ARM) entiy
	 *                       type identified by <code>type</code> parameter
	 * @param sourceValues the map of attribute values.<br>
	 *                     The keys of this map are <code>generic_attribute_mappings</code>
	 *                     for all attributes of this source (ARM) instance.<br>
	 *                     The values are either objects corresponding simple types,
	 *                     entity instance as target (AIM) or <code>Collection</code>
	 *                     of before mentioned objects for aggregates.
	 * @exception SdaiException if underlying JSDAI exception occurs
	 * @see #createSourceInstance
	 */
	public void setSourceAttributeValues(EEntity_mapping type, EEntity targetInstance,
										 Map sourceValues) throws SdaiException;

	public Set preprocessTargetInstances(EEntity_mapping type) throws SdaiException;

} // MappingPopulationCreator
