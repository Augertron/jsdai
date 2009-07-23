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

import jsdai.lang.AEntity;
import jsdai.lang.ASdaiModel;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.SBasic_curve_xim.CClosed_composite_curve;
import jsdai.SBasic_curve_xim.EClosed_composite_curve;
import jsdai.SConstructive_solid_geometry_2d_xim.CPath_area_with_parameters_armx;
import jsdai.SGeometry_schema.AComposite_curve_segment;
import jsdai.SGeometry_schema.CComposite_curve;
import jsdai.SGeometry_schema.EComposite_curve_segment;
import jsdai.SGeometry_schema.ETransition_code;
import jsdai.SLayered_interconnect_simple_template_xim.AClosed_path_area_with_parameters;
import jsdai.SLayered_interconnect_simple_template_xim.CClosed_path_area_with_parameters;
import jsdai.SLayered_interconnect_simple_template_xim.EClosed_path_area_with_parameters;
import jsdaix.processor.xim_aim.pre.Importer;

/**
 * @author Giedrius
 * 
 *	Check if closed_curve satisfies mapping constraint. Do it manually as mapping path uses DERIVED attribute which is not supported yet.
 */
public class Closed_curveFixer {
	/**
	 * 
	 * @param models
	 * @throws SdaiException
	 */
	public static void run(ASdaiModel models, Importer importer) throws SdaiException {
		// This may be expanded in the future
		AEntity instancesToRename = models.getExactInstances(CClosed_composite_curve.definition);
		for(int index=1; index <= instancesToRename.getMemberCount();){
			// basically check for constraint
			// {composite_curve
			// composite_curve.closed_curve = .TRUE.}
			// Where composite_curve.closed_curve is derived as follows:
			// closed_curve : LOGICAL :=  segments [ n_segments ] . transition <> discontinuous ;
			EClosed_composite_curve instanceToRename = (CClosed_composite_curve)instancesToRename.getByIndexEntity(index);
			AComposite_curve_segment segments = instanceToRename.getSegments(null);
			EComposite_curve_segment lastSegment = segments.getByIndex(segments.getMemberCount());
			if(lastSegment.getTransition(null) == ETransition_code.DISCONTINUOUS){
				AClosed_path_area_with_parameters acpawp = new AClosed_path_area_with_parameters();
				CClosed_path_area_with_parameters.usedinCentreline(null, instanceToRename, models, acpawp);
				instanceToRename.findEntityInstanceSdaiModel().substituteInstance(instanceToRename, CComposite_curve.definition);
				// Also downgrade its users if needed
				for(int j=1,count2=acpawp.getMemberCount(); j<=count2; j++){
					EClosed_path_area_with_parameters ecpawp = acpawp.getByIndex(j);
					String message = " Changed "+ecpawp; 
					EEntity instance = ecpawp.findEntityInstanceSdaiModel().substituteInstance(ecpawp, CPath_area_with_parameters_armx.definition);
					importer.logMessage(message+" to "+instance);
				}
			}else{
				index++;
			}
		}
	}
}
