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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jsdai.SDocument_schema.ADocument;
import jsdai.SDocument_schema.ADocument_representation_type;
import jsdai.SDocument_schema.CDocument;
import jsdai.SDocument_schema.CDocument_representation_type;
import jsdai.SDocument_schema.EDocument;
import jsdai.SDocument_schema.EDocument_representation_type;
import jsdai.SFile_identification_mim.CDocument_file;
import jsdai.SFile_identification_mim.EDocument_file;
import jsdai.lang.ASdaiModel;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;

/**
 * see bug #2317 for some details
 */
public class DocumentRepair {
	
	private DocumentRepair() { }
	
	private static final String TYPE_PHYSICAL = "physical";
	private static final String TYPE_DIGITAL = "digital";
	private static final Set REQUIRED_TYPES;
	
	static {
		Set requiredTypes = new HashSet();
		requiredTypes.add(TYPE_DIGITAL);
		requiredTypes.add(TYPE_PHYSICAL);
		
		REQUIRED_TYPES = Collections.unmodifiableSet(requiredTypes);
	}

	public static void run(ASdaiModel domain) throws SdaiException {
		ADocument aDoc = (ADocument) domain.getInstances(CDocument.definition);
		for (SdaiIterator i = aDoc.createIterator(); i.next();) {
			EDocument eDoc = aDoc.getCurrentMember(i);
			eDoc = ensureCorrectEntityType(eDoc);
			ensureCorrectRepresentationType(domain, eDoc);
		}		
	}

	private static EDocument ensureCorrectEntityType(EDocument eDoc)
		throws SdaiException {

		if (!(eDoc instanceof EDocument_file)) {
			// currently only Document_files can be mapped to something
			// in XIM, so we must convert all documents to document_files
			eDoc = (EDocument) eDoc.findEntityInstanceSdaiModel().substituteInstance(
				eDoc, CDocument_file.definition);
		}
		
		return eDoc;
	}
	
	private static void ensureCorrectRepresentationType(ASdaiModel domain, EDocument eDoc)
		throws SdaiException {

		if (!hasAppropriateType(domain, eDoc)) {
			EDocument_representation_type eDrt = (EDocument_representation_type)
			eDoc.findEntityInstanceSdaiModel().createEntityInstance(
				CDocument_representation_type.definition);
			eDrt.setName(null, TYPE_DIGITAL);
			eDrt.setRepresented_document(null, eDoc);
		}
	}

	private static boolean hasAppropriateType(ASdaiModel models, EDocument eDoc)
		throws SdaiException {
		
		ADocument_representation_type aDrt = new ADocument_representation_type();
		CDocument_representation_type.usedinRepresented_document(null, eDoc, models, aDrt);
		for (SdaiIterator i = aDrt.createIterator(); i.next();) {
			EDocument_representation_type eDrt = aDrt.getCurrentMember(i);
			if (eDrt.testName(null) && REQUIRED_TYPES.contains(eDrt.getName(null))) {
				return true;
			}
		}
		
		return false;
	}
}
