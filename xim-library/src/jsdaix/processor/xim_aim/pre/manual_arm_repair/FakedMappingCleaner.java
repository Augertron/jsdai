/*
 * $Id$
 *
 * JSDAI(TM), a way to implement STEP, ISO 10303
 * Copyright (C) 1997-2009, LKSoftWare GmbH, Germany
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

package jsdaix.processor.xim_aim.pre.manual_arm_repair;


import java.util.Collection;
import java.util.Map;

import jsdai.dictionary.EEntity_definition;
import jsdai.lang.AEntity;
import jsdai.lang.ASdaiModel;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdaix.processor.xim_aim.pre.AutomaticXimPopulationCreator;

/**
 * @author Giedrius
 * 
 *	Cleans garbage from items list (those entities, which are there mainly because of mapping and not as items for geometry
 */
public class FakedMappingCleaner {
	/**
	 * 
	 * @param repo
	 * @throws SdaiException
	 */
	public static void run(ASdaiModel models, AutomaticXimPopulationCreator ximPopulationCreator) throws SdaiException {
		EEntity_definition[] types = AutomaticXimPopulationCreator.getSpecialTypes();
		Map map = ximPopulationCreator.getSpecialTypeInstanceMap();
		for(int t=0; t<types.length; t++){
			EEntity_definition type = types[t];
			// This may be expanded in the future
			AEntity instancesToClean = models.getExactInstances(type);
			Collection instancesToKeep = (Collection)map.get(type);
//			if(instancesToKeep == null){
//				System.err.println(type+" NO COLLECTION ");
//			}
			for(int index=1; index <= instancesToClean.getMemberCount();){
				EEntity instanceToClean = instancesToClean.getByIndexEntity(index);
				if((instancesToKeep == null)||(!(instancesToKeep.contains(instanceToClean)))){
					// System.err.println(" CLEANING REP "+instanceToClean+" "+instancesToKeep);					
					instanceToClean.deleteApplicationInstance();
				}else{
					index++;
				}
			}
		}
	}
}
