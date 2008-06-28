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

package jsdai.tools;

import java.util.*;

import jsdai.SExtended_dictionary_schema.*;
import jsdai.lang.*;

public final class CheckRedeclaredDerives {

	private CheckRedeclaredDerives() { }

	private static void checkAttributes(EEntity_definition eEd, ASdaiModel domain)
		throws SdaiException {

		// get redeclared attributes of entry entity
		AAttribute aAttr = eEd.getAttributes(null, domain);
		for (SdaiIterator i = aAttr.createIterator(); i.next();) {
			EAttribute eAttr = aAttr.getCurrentMember(i);
			if ((eAttr instanceof EDerived_attribute) && hasDerived(eAttr, domain)) {
				System.err.println(eEd.getName(null) + "." + eAttr.getName(null));
			}
		}
	}

	private static EAttribute getRootAttr(EAttribute eAttr)
		throws SdaiException {

		EAttribute eRoot = eAttr;
		while (eAttr != null) {
			EAttribute eRd = null;
			if (eAttr instanceof EDerived_attribute) {
				EDerived_attribute eDer = (EDerived_attribute) eAttr;
				if (eDer.testRedeclaring(null)) {
					eRd = (EAttribute) eDer.getRedeclaring(null);
				}
			} else if (eAttr instanceof EExplicit_attribute) {
				EExplicit_attribute eExp = (EExplicit_attribute) eAttr;
				if (eExp.testRedeclaring(null)) {
					eRd = eExp.getRedeclaring(null);
				}
			} else if (eAttr instanceof EInverse_attribute) {
				EInverse_attribute eInv = (EInverse_attribute) eAttr;
				if (eInv.testRedeclaring(null)) {
					eRd = eInv.getRedeclaring(null);
				}
			}

			eRoot = eAttr;
			eAttr = eRd;
		}

		return eRoot;
	}

	private static boolean hasDerived(EAttribute eAttr, ASdaiModel domain)
		throws SdaiException {

		EEntity_definition eParent = (EEntity_definition) eAttr.getParent(null);
		eAttr = getRootAttr(eAttr);

		Set supertypes = getSupertypes(eParent);
		while (supertypes.size() > 0) {
			EEntity_definition eSuper = (EEntity_definition) supertypes.iterator().next();
			supertypes.remove(eSuper);
			supertypes.addAll(getSupertypes(eSuper));

			AAttribute aAttr = eSuper.getAttributes(null, domain);
			for (SdaiIterator i = aAttr.createIterator(); i.next();) {
				EAttribute eA = aAttr.getCurrentMember(i);
				if ((eA instanceof EDerived_attribute) && getRootAttr(eA) == eAttr) {
					return true;
				}
			}
		}

		return false;
	}

	private static Set getSupertypes(EEntity_definition eEd)
		throws SdaiException {

		Set supertypes = new HashSet();
		AEntity_definition aSuper = CompareAttributes.getSupertypes(eEd);
		for (SdaiIterator i = aSuper.createIterator(); i.next();) {
			supertypes.add(aSuper.getCurrentMember(i));
		}

		return supertypes;
	}

	public static void main(String[] args)
		throws SdaiException {

		String repoName = "ExpressCompilerRepo";

		System.out.println("Initializing JSDAI...");
		SdaiRepository repo = CompareUsefroms.getRepository(repoName);
		if (repo == null) {
			System.err.println("Unable to open repo: " + repoName);
			return;
		}

		// activate all models
		ASdaiModel models = SearchForRedundantUsefroms.activateModels(repo);

		AEntity aEd = models.getInstances(CEntity_declaration$local_declaration.definition);
		for (SdaiIterator i = aEd.createIterator(); i.next();) {
			EEntity_declaration eDec = (EEntity_declaration) aEd.getCurrentMemberEntity(i);
			EEntity_definition eEd = (EEntity_definition) eDec.getDefinition(null);
//			System.out.println(eEd.getName(null));
			checkAttributes(eEd, models);
		}
	}
}
