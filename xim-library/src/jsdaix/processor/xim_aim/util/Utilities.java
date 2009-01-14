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

package jsdaix.processor.xim_aim.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jsdai.dictionary.ADeclaration;
import jsdai.dictionary.CEntity_declaration;
import jsdai.dictionary.EEntity_definition;
import jsdai.lang.AEntity;
import jsdai.lang.ASdaiModel;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiRepository;
import jsdai.lang.SdaiSession;

/**
 * @author Eva
 *
 */
public class Utilities {
	
	public static List findInconsistentEntities(AEntity instances, String schemaName) throws SdaiException {
		List inconsistentEntities = new ArrayList(); 
		ASdaiModel domain = new ASdaiModel();
		HashMap testedDefinitions = new HashMap();
		
		SdaiRepository systemRepo = SdaiSession.getSession().getSystemRepository();
		SdaiModel systemModel = systemRepo.findSdaiModel(String.valueOf(schemaName + "_DICTIONARY_DATA").toUpperCase());
		
		if (systemModel.getMode() == SdaiModel.NO_ACCESS) {
			systemModel.startReadOnlyAccess();
		}
		
		domain.addUnordered(systemModel, null);
		
		SdaiIterator it = instances.createIterator();
		while (it.next()) {
			EEntity instance = instances.getCurrentMemberEntity(it);
			
			EEntity_definition eDef = instance.getInstanceType();

			Boolean wasConsistent = (Boolean) testedDefinitions.get(eDef);
			if (wasConsistent == null) {
				// still not checked ..
				// check for availability of eDefinition

				ADeclaration usedBySchema = new ADeclaration();
				CEntity_declaration.usedinDefinition(null, eDef, domain, usedBySchema);

				if (usedBySchema.getMemberCount() > 0) {
					// add to map as compatible entity definition
					wasConsistent = new Boolean(true);
				}
				else {
					// add to map as not compatible entity definition
					wasConsistent = new Boolean(false);
				}
				testedDefinitions.put(eDef, wasConsistent);
			}

			if (!wasConsistent.booleanValue()) {
				inconsistentEntities.add(instance);
				continue;
			}
		}
		
		return inconsistentEntities;
	}
}
