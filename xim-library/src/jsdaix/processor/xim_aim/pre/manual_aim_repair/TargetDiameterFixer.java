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

package jsdaix.processor.xim_aim.pre.manual_aim_repair;

import jsdai.SMeasure_schema.ELength_measure_with_unit;
import jsdai.SMixed_complex_types.CLength_measure_with_unit$measure_representation_item;
import jsdai.SQualified_measure_schema.AMeasure_representation_item;
import jsdai.SQualified_measure_schema.CMeasure_representation_item;
import jsdai.SRepresentation_schema.ERepresentation_item;
import jsdai.lang.ASdaiModel;
import jsdai.lang.SdaiException;
import jsdaix.processor.xim_aim.pre.Importer;

/**
 * @author Giedrius Liutkus
 * Target_circle.diameter has to be of type length_measure_with_unit, but in all Cax-if files it is of type measure_representation_item
 * This has to be fixed
 *
 */
public class TargetDiameterFixer {

	public static void run(ASdaiModel models, Importer importer) throws SdaiException {

		AMeasure_representation_item repItemList = (AMeasure_representation_item) models.getExactInstances(CMeasure_representation_item.definition);
		for (int i=1;i<=repItemList.getMemberCount(); ) {
			ERepresentation_item eri = repItemList.getByIndex(i);
			if((eri.testName(null))&&(eri.getName(null).equals("target diameter"))){
				if(eri instanceof ELength_measure_with_unit){
					i++;
					continue;
				}
				importer.logMessage(" Target_circle.diameter and Target_circular_curve.diameter must be of type length_meausre_with_unit. So changing "+eri);
				eri.findEntityInstanceSdaiModel().substituteInstance(eri, CLength_measure_with_unit$measure_representation_item.definition);
			}else{
				i++;
			}
		}
	}
}
