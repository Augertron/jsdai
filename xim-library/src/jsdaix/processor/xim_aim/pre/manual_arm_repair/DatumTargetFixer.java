/*
 * $Id$
 *
 * JSDAI(TM), a way to implement STEP, ISO 10303
 * Copyright (C) 1997-2011, LKSoftWare GmbH, Germany
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

import jsdai.SGeometric_tolerance_xim.ADatum_defined_by_targets;
import jsdai.SGeometric_tolerance_xim.CDatum_defined_by_targets;
import jsdai.SGeometric_tolerance_xim.EDatum_defined_by_targets;
import jsdai.SShape_aspect_definition_schema.ADatum_target;
import jsdai.SShape_aspect_definition_schema.CDatum_target;
import jsdai.SShape_aspect_definition_schema.EDatum_target;
import jsdai.lang.ASdaiModel;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdaix.processor.xim_aim.pre.Importer;

public class DatumTargetFixer {

	private DatumTargetFixer() {}

	public static void run(ASdaiModel models, Importer importer) throws SdaiException {
		ADatum_target aTargets = (ADatum_target) models.getInstances(CDatum_target.definition);
		for (SdaiIterator i = aTargets.createIterator(); i.next();) {
			EDatum_target eTarget = aTargets.getCurrentMember(i);
			if (eTarget.testTarget_id(null)) {
				ADatum_defined_by_targets aDatums = new ADatum_defined_by_targets();
				CDatum_defined_by_targets.usedinDefined_by(null, eTarget, models, aDatums);
				if (aDatums.getMemberCount() == 1) {
					EDatum_defined_by_targets eDatum = aDatums.getByIndex(1);
					if (eDatum.testIdentification(null)) {
						String datumId = eDatum.getIdentification(null);
						String targetId = eTarget.getTarget_id(null);
						if (targetId.startsWith(datumId) && targetId.length() > datumId.length()) {
							eTarget.setTarget_id(null, targetId.substring(datumId.length()));
						}
					}
				} else {
					importer.logMessage("Unexpected number of datums using " + eTarget);
				}
			}
		}
	}
}
