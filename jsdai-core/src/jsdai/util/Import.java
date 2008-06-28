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

package jsdai.util;

import jsdai.lang.*;
import jsdai.dictionary.*;
import java.io.*;


public class Import {

	public static void main(String argv[]) throws SdaiException {
   	 if (argv.length < 2) { 
      	System.out.println("USAGE: java Import exchange_structure_name repository_name");
	      return;
   	 }
		int i;
		SdaiSession session = SdaiSession.openSession();
		SdaiTransaction trans = session.startTransactionReadWriteAccess();
		SdaiRepository repoSource = session.importClearTextEncoding(null, argv[0], null);
		SdaiRepository repoTarget = session.linkRepository(argv[1], null);
		moveRepoContents(repoSource, repoTarget);
		trans.endTransactionAccessCommit();
		repoSource.deleteRepository();
		session.closeSession();
	}

/**
Moving all models and schema instances from one repository to another. 
*/
	public static void moveRepoContents(SdaiRepository repoSource, SdaiRepository repoTarget) 
			throws SdaiException {
		int i, j;
		boolean found;
		String str;
		ESchema_definition s_schema;
		SdaiSession.setLogWriter(new PrintWriter(System.out, true));
		String line_separator = System.getProperty("line.separator");
		if (!repoSource.isActive()) {
			repoSource.openRepository();
		}
		if (!repoTarget.isActive()) {
			repoTarget.openRepository();
		}
		ASdaiModel modsSource = repoSource.getModels();
		ASdaiModel modsTarget = repoTarget.getModels();
		int ms_count = modsSource.getMemberCount();
		int mt_count = modsTarget.getMemberCount();
		SdaiModel s_mod;
		SdaiModel t_mod;
		SdaiModel [] mods_arr = new SdaiModel[ms_count];
		for (i = 0; i < ms_count; i++) {
			s_mod = modsSource.getByIndex(i + 1);
			String s_name = s_mod.getName();
			found = true;
			while (found) {
				found = false;
				for (j = 1; j <= mt_count; j++) {
					t_mod = modsTarget.getByIndex(j);
					if (s_name.equals(t_mod.getName())) {
						s_name = s_name + "x";
						found = true;
					}
				}
			}
			s_schema = s_mod.getUnderlyingSchema();
			t_mod = repoTarget.createSdaiModel(s_name, s_schema);
			mods_arr[i] = t_mod;
			if (s_mod.getMode() == SdaiModel.NO_ACCESS) {
				s_mod.startReadOnlyAccess();
			}
			t_mod.startReadWriteAccess();
			AEntity_declaration decls = s_schema.getEntity_declarations(null, null);
			SdaiIterator it_decls = decls.createIterator();
			while (it_decls.next()) {
				EEntity_declaration decl = decls.getCurrentMember(it_decls);
				EEntity_definition def = (EEntity_definition)decl.getDefinition(null);
				AEntity insts = s_mod.getExactInstances(def);
				int init_count = insts.getMemberCount();
				for (j = 1; j <= init_count; j++) {
					EEntity t_inst = t_mod.substituteInstance(insts.getByIndexEntity(1));
				}
			}
		}
		ASchemaInstance schInstsSource = repoSource.getSchemas();
		ASchemaInstance schInstsTarget = repoTarget.getSchemas();
		int st_count = schInstsTarget.getMemberCount();
		SchemaInstance t_schInst;
		for (i = 1; i <= schInstsSource.getMemberCount(); i++) {
			SchemaInstance s_schInst = schInstsSource.getByIndex(i);
			String si_name = s_schInst.getName();
			found = true;
			while (found) {
				found = false;
				for (j = 1; j <= st_count; j++) {
					t_schInst = schInstsTarget.getByIndex(j);
					if (si_name.equals(t_schInst.getName())) {
						si_name = si_name + "x";
						found = true;
					}
				}
			}
			s_schema = s_schInst.getNativeSchema();
			t_schInst = repoTarget.createSchemaInstance(si_name, s_schema);
			A_string s_descr = s_schInst.getDescription();
			A_string t_descr = t_schInst.getDescription();
			for (j = 1; j <= s_descr.getMemberCount(); j++) {
				t_descr.addByIndex(j, s_descr.getByIndex(j));
			}
			A_string s_auth = s_schInst.getAuthor();
			A_string t_auth = t_schInst.getAuthor();
			for (j = 1; j <= s_auth.getMemberCount(); j++) {
				t_auth.addByIndex(j, s_auth.getByIndex(j));
			}
			A_string s_org = s_schInst.getOrganization();
			A_string t_org = t_schInst.getOrganization();
			for (j = 1; j <= s_org.getMemberCount(); j++) {
				t_org.addByIndex(j, s_org.getByIndex(j));
			}
			A_string s_con = s_schInst.getContextIdentifiers();
			A_string t_con = t_schInst.getContextIdentifiers();
			for (j = 1; j <= s_con.getMemberCount(); j++) {
				t_con.addByIndex(j, s_con.getByIndex(j));
			}
			str = s_schInst.getPreprocessorVersion();
			if (str != null) {
				t_schInst.setPreprocessorVersion(str);
			}
			str = s_schInst.getOriginatingSystem();
			if (str != null) {
				t_schInst.setOriginatingSystem(str);
			}
			str = s_schInst.getAuthorization();
			if (str != null) {
				t_schInst.setAuthorization(str);
			}
			str = s_schInst.getDefaultLanguage();
			if (str != null) {
				t_schInst.setDefaultLanguage(str);
			}
			ASdaiModel s_assocMods = s_schInst.getAssociatedModels();
			for (j = 1; j <= s_assocMods.getMemberCount(); j++) {
				SdaiModel mod = s_assocMods.getByIndex(j);
				found = false;
				for (int k = 0; k < ms_count; k++) {
					s_mod = modsSource.getByIndex(k + 1);
					if (s_mod == mod) {
						t_schInst.addSdaiModel(mods_arr[k]);
						found = true;
						break;
					}
				}
				if (!found) {
					SdaiSession.println("Associated model '" + mod.getName() + 
						"' not found." + line_separator);
				}
			}
		}
	}


}