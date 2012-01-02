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
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import jsdai.dictionary.EEntity_definition;
import jsdai.lang.AEntity;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;

public abstract class APInstancesProcessor {

	protected static Set types;
	
	public List findInconsistentEntities(AEntity instances)throws SdaiException{
		List inconsistentEntities = new ArrayList();
		SdaiIterator it = instances.createIterator();
		while (it.next()) {
			EEntity instance = instances.getCurrentMemberEntity(it);
			EEntity_definition eDef = instance.getInstanceType();
			// Need to decompose complex into leaves
			if(eDef.getComplex(null)){
				String name = eDef.getName(null);
				StringTokenizer st = new StringTokenizer(name,"+",false);
				while(st.hasMoreElements()){
					if (!types.contains(st.nextElement())) {
						inconsistentEntities.add(instance);
						break;
					}
				}
			}else{
				if (!types.contains(eDef.getName(null))) {
					inconsistentEntities.add(instance);
				}
			}
		}
		return inconsistentEntities;
	}
	
}
