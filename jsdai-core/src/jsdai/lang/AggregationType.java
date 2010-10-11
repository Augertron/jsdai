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

/** Supertype of dictionary CAggregation_type, for internal JSDAI use only.
   needed to hold some non-public data fields */
public abstract class AggregationType extends DataType implements EAggregation_type {

	protected AggregationType() {
	}

//	Class aggregateClass;
	protected Class aggregateClass;

	// aggMemberImplClass is one level below aggregateClass; 
	// e.g. if aggregateClass is AaCartesian_point, then aggMemberImplClass is to ACartesian_point
	// at the lowest level aggMemberImplClass is null
	Class aggMemberImplClass; 
	protected int shift = 0;
	protected SelectType select;

	// set in the case of entity-aggregates
	CEntity base;
	int order; // 1==A, 2=Aa, 3=Aaa ..
	String name;

	final int ADOUBLE_AGGR      = 1;
	final int ADOUBLE3_AGGR     = 2;


	Class getAggregateClass() throws SdaiException {
		if (aggregateClass == null) {
			int etp = ((DataType)base).express_type;
			if (etp == DataType.ENTITY) {
				aggregateClass = ((CEntityDefinition)base).getEntityNestedAggregate(order);
			} else if (etp >= DataType.SELECT && etp <= DataType.ENT_EXT_EXT_SELECT) {
				aggregateClass = ((SelectType)base).getSelectNestedAggregate(name, order);
			}
		}
		return aggregateClass;
	}

	Class getAggMemberImplClass() throws SdaiException {
		if (aggMemberImplClass == null && order > 1) {
			int etp = ((DataType)base).express_type;
			if (etp == DataType.ENTITY) {
				aggMemberImplClass = ((CEntityDefinition)base).getEntityNestedAggregate(order - 1);
			} else if (etp >= DataType.SELECT && etp <= DataType.ENT_EXT_EXT_SELECT) {
				aggMemberImplClass = ((SelectType)base).getSelectNestedAggregate(name, order - 1);
			}
		}
		return aggMemberImplClass;
	}

	protected boolean check_aggregation_double() throws SdaiException {
		if (shift == ADOUBLE3_AGGR) {
			return true;
		} else if (shift == ADOUBLE_AGGR) {
			return false;
		}
		int u_bound = 100;
		if (express_type < ARRAY) {
			EVariable_size_aggregation_type var_aggr_type = (EVariable_size_aggregation_type)this;
			if (var_aggr_type.testUpper_bound(null)) {
				EBound bound = var_aggr_type.getUpper_bound(null);
				if (bound instanceof EInteger_bound) {
					u_bound = bound.getBound_value(null);
				}
			}
		} else {
			EBound u_index = ((CArray_type)this).getUpper_index(null);
			EBound l_index = ((CArray_type)this).getLower_index(null);
			if (u_index instanceof EInteger_bound && l_index instanceof EInteger_bound) {
				u_bound = u_index.getBound_value(null) - l_index.getBound_value(null) + 1;
			}
		}
		if (u_bound <= 3) {
			shift = ADOUBLE3_AGGR;
			return true;
		}
		shift = ADOUBLE_AGGR;
		return false;
	}


	boolean allow_entity_aggregate(EAggregation_type aggr_type) throws SdaiException {
		if (select == null) {
			DataType type = (DataType)aggr_type.getElement_type(null);
			return type.allow_entity();
		} else {
			return select.allow_entity_select();
		}
	}


	boolean search_entity_aggregate(CEntity_definition def_for_value) throws SdaiException {
		if (select == null) {
			DataType type = (DataType)((EAggregation_type)this).getElement_type(null);
			return type.search_entity(def_for_value);
		} else {
			return select.search_entity_select(def_for_value);
		}
	}


}
