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

import jsdai.dictionary.*;

/** This is for internal JSDAI use only. Applications shall not use this */
public abstract class GlobalRule extends CEntity implements EGlobal_rule {

	Class rule_class;
	Object rule_object;

	CWhere_rule w_rules_array [];
	String bypass_rules [];

	protected GlobalRule() {
		super();
	}


	Class getRuleClass() throws SdaiException {
		if (rule_class != null) {
			return rule_class;
		}
		SdaiModel owner = ((CEntity)this).owning_model;
		SSuper sup = owner.schemaData.super_inst;
		String str = sup.getClass().getName();
		int index = str.lastIndexOf('.');
		String s = str.substring(0, index) + ".R" + normalise(((CGlobal_rule)this).getName(null));
		try {
			rule_class =
				Class.forName(s, true, SdaiClassLoaderProvider.getDefault().getClassLoader());
			return rule_class;
		} catch (ClassNotFoundException e) {
			throw new SdaiException(SdaiException.SY_ERR, "Rule-class not found: " + s);
		}
	}


	Object getRuleObject() throws SdaiException {
		if (rule_object != null) {
			return rule_object;
		}
		try {
			rule_object = rule_class.newInstance();
			return rule_object;
		} catch (java.lang.InstantiationException ex1) {
			throw new SdaiException(SdaiException.SY_ERR, ex1);
		} catch (java.lang.IllegalAccessException ex2) {
			throw new SdaiException(SdaiException.SY_ERR, ex2);
		}
	}


	CWhere_rule [] getWhereRules(AWhere_rule w_rules) throws SdaiException {
		if (w_rules_array != null) {
			return w_rules_array;
		}
		int ln = ((AEntity)w_rules).myLength;
		w_rules_array = new CWhere_rule[ln];
		StaticFields staticFields = StaticFields.get();
		if (staticFields.w_rules_sorting_gr == null) {
			if (ln <= CEntityDefinition.RULES_ARRAY_SIZE) {
				staticFields.w_rules_sorting_gr = new CWhere_rule[CEntityDefinition.RULES_ARRAY_SIZE];
			} else {
				staticFields.w_rules_sorting_gr = new CWhere_rule[ln];
			}
		} else if (ln > staticFields.w_rules_sorting_gr.length) {
			staticFields.w_rules_sorting_gr = new CWhere_rule[ln];
		}
		int count = 0;
		if (staticFields.iter_gr == null) {
			staticFields.iter_gr = w_rules.createIterator();
		} else {
			w_rules.attachIterator(staticFields.iter_gr);
		}
		while(staticFields.iter_gr.next()) {
			CWhere_rule wrule = (CWhere_rule)w_rules.getCurrentMember(staticFields.iter_gr);
			((CEntity)wrule).instance_position = wrule.getOrder(null);
			staticFields.w_rules_sorting_gr[count] = wrule;
			w_rules_array[count++] = wrule;
		}
		sortWhereRules(staticFields.w_rules_sorting_gr, w_rules_array, 0, ln);
		return w_rules_array;
	}


	private void sortWhereRules(CWhere_rule [] s_rules, CWhere_rule [] rules, int start_index, int end_index) {
		int i;
		int gap = end_index - start_index;
		if (gap < 7) {
			for (i = start_index; i < end_index; i++) {
				for (int j=i; j>start_index &&
						((CEntity)rules[j-1]).instance_position > ((CEntity)rules[j]).instance_position; j--) {
					CWhere_rule r = rules[j-1];
					rules[j-1] = rules[j];
					rules[j] = r;
				}
			}
			return;
		}
		int middle = (start_index + end_index)/2;
		sortWhereRules(rules, s_rules, start_index, middle);
		sortWhereRules(rules, s_rules, middle, end_index);
		if (((CEntity)s_rules[middle-1]).instance_position <= ((CEntity)s_rules[middle]).instance_position) {
			System.arraycopy(s_rules, start_index, rules, start_index, gap);
			return;
		}
		int m, n;
		for(i = start_index, m = start_index, n = middle; i < end_index; i++) {
			if (n>=end_index || m<middle &&
					((CEntity)s_rules[m]).instance_position <= ((CEntity)s_rules[n]).instance_position) {
				rules[i] = s_rules[m++];
			} else {
				rules[i] = s_rules[n++];
			}
		}
	}


	int getBypassedRulesCount(SdaiSession ss) throws SdaiException {
		if (ss.byp_rules_count < 0) {
			return 0;
		}
		String [][] byp_rules;
		if (ss.byp_rules_count == 0) {
			byp_rules = ss.getBlackList();
			if (ss.byp_rules_count < 0) {
				return 0;
			}
			getBypassedRulesInit(byp_rules, ss.byp_rules_count);
		}
		if (bypass_rules == null) {
			return 0;
		}
		return bypass_rules.length;
	}


	String [] getBypassedRules() throws SdaiException {
		return bypass_rules;
	}


	private void getBypassedRulesInit(String [][] byp_rules, int byp_rules_count) throws SdaiException {
		String glob_name = ((CGlobal_rule)this).getName(null).toUpperCase();
		int count = 0;
		for (int j = 0; j < byp_rules_count; j++) {
			if (byp_rules[0][j] == null) {
				if (!(byp_rules[1][j].toUpperCase().equals(glob_name))) {
					continue;
				}
				bypass_rules = new String[1];
				bypass_rules[0] = ""; // all where rules in this global rule should be bypassed
				break;
			} else {
				if (!(byp_rules[0][j].equals(glob_name))) {
					continue;
				}
				if (bypass_rules == null) {
					bypass_rules = new String[1];
				} else {
					enlarge_bypass_rules();
				}
				bypass_rules[count] = byp_rules[1][j];
				count++;
			}
		}
	}


	private void enlarge_bypass_rules() throws SdaiException {
		int new_length = bypass_rules.length + 1;
		String [] new_rules_file = new String[new_length];
		System.arraycopy(bypass_rules, 0, new_rules_file, 0, bypass_rules.length);
		bypass_rules = new_rules_file;
	}


}
