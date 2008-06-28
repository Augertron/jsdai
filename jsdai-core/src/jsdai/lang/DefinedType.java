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

/**
 * This class is a supertype of <code>CDefined_type</code> contained in the 
 * <code>jsdai.dictionary</code> package.
 * It was designed primarily to define some internal nonpublic fields.
 * The class is for internal JSDAI use only.
 */
public abstract class DefinedType extends DataType implements EDefined_type {

	protected DefinedType() { }

	protected boolean used;
	CWhere_rule where_rules_array [];
	SchemaData typeOf_schema;
	CDefined_type [] typeOf_types = null;
/*	String nameUpperCase;

	protected String getNameUpperCase(){
		if (nameUpperCase == null) {
			try {
				nameUpperCase = ((CDefined_type)this).getName(null).toUpperCase();
			} catch (Exception ex) {
				return null;
			}
		}
		return nameUpperCase;
	}*/

	CWhere_rule [] get_dt_where_rules(StaticFields staticFields, AWhere_rule w_rules) throws SdaiException {
		if (where_rules_array != null) {
			return where_rules_array;
		}
		int ln = ((AEntity)w_rules).myLength;
		where_rules_array = new CWhere_rule[ln];
		if (staticFields.w_rules_sorting == null) {
			if (ln <= CEntityDefinition.RULES_ARRAY_SIZE) {
				staticFields.w_rules_sorting = new CWhere_rule[CEntityDefinition.RULES_ARRAY_SIZE];
			} else {
				staticFields.w_rules_sorting = new CWhere_rule[ln];
			}
		} else if (ln > staticFields.w_rules_sorting.length) {
			staticFields.w_rules_sorting = new CWhere_rule[ln];
		}
		int count = 0;
		if (staticFields.it3 == null) {
			staticFields.it3 = w_rules.createIterator();
		} else {
			w_rules.attachIterator(staticFields.it3);
		}
		while(staticFields.it3.next()) {
			CWhere_rule wrule = (CWhere_rule)w_rules.getCurrentMember(staticFields.it3);
			((CEntity)wrule).instance_position = wrule.getOrder(null);
			staticFields.w_rules_sorting[count] = wrule;
			where_rules_array[count++] = wrule;
		}
		CEntityDefinition.sortWhereRules(staticFields.w_rules_sorting, where_rules_array, 0, ln);
		return where_rules_array;
	}


}
