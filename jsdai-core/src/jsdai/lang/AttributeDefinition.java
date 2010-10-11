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

package jsdai.lang; import jsdai.dictionary.*;

import java.lang.reflect.*;

/** This is for internal JSDAI use only. Applications shall not use this */
public abstract class AttributeDefinition extends CEntity implements EAttribute {

	static final String SET_PREFIX = "_SET_";
	static final String BAG_PREFIX = "_BAG_";
	static final int EXPLICIT = 1;
	static final int DERIVED = 2;
	static final int INVERSE = 3;

	boolean selected_as_role;
	int attr_tp;
	AggregationType invType;

	protected AttributeDefinition() {
		super();
	}


	final protected boolean testParent_entityInternal(EEntity_or_view_definition ev_def) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (((DataType)ev_def).express_type == DataType.ENTITY) {
			return (ev_def == null) ? false : true;
		} else {
			return false;
		}
//		} // syncObject
	}

	final protected EEntity_definition getParent_entityInternal(EEntity_or_view_definition ev_def) throws SdaiException {
//		synchronized (syncObject) {
		if (((DataType)ev_def).express_type == DataType.ENTITY) {
			if (owning_model == null) {
				throw new SdaiException(SdaiException.EI_NEXS);
			}
			if (ev_def == null) {
				throw new SdaiException(SdaiException.VA_NSET);
			}
			return (EEntity_definition)ev_def;
		} else {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
//		} // syncObject
	}


	EAggregation_type getInverseAggregationType(CEntity inst) throws SdaiException {
		if (invType == null) {
//			Class cl = inst.owning_model.underlying_schema.modelDictionary.schemaData.super_inst.getClass();
//			Class cl = ((CEntity)inst.getInstanceType()).owning_model.schemaData.super_inst.getClass();
			Class cl = ((CEntity)this).owning_model.schemaData.super_inst.getClass();
			DataType dt = (DataType)SdaiSession.findDataType(constructName(), cl);
			if (dt == null) {
				String base = SdaiSession.line_separator + AdditionalMessages.DI_NOTF + 
				SdaiSession.line_separator + AdditionalMessages.DI_NAME + constructName();
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			if (dt.express_type >= DataType.SET && dt.express_type <= DataType.BAG) {
				invType = (AggregationType)dt;
			} else {
				String base = SdaiSession.line_separator + AdditionalMessages.DI_WRDT + 
				SdaiSession.line_separator + AdditionalMessages.DI_NAME + constructName() + 
				SdaiSession.line_separator + AdditionalMessages.DI_CODE + dt.express_type;
				throw new SdaiException(SdaiException.VT_NVLD, base);
			}
		}
		return invType;
	}

	private String constructName() throws SdaiException {
		EInverse_attribute inv_at = (EInverse_attribute)this;
		String str;
		if (inv_at.getDuplicates(null)) {
			str = BAG_PREFIX;
		} else {
			str = SET_PREFIX;
		}
		EBound bound = inv_at.getMin_cardinality(null);
		int b = bound.getBound_value(null);
		str = str + b + "_";
		if (inv_at.testMax_cardinality(null)) {
			bound = inv_at.getMax_cardinality(null);
			b = bound.getBound_value(null);
			str = str + b + "_";
		}
		String ent_name = inv_at.getDomain(null).getName(null);
		str = str + ent_name;
		return str;
	}


	boolean search_attribute(CEntity_definition def_for_value) throws SdaiException {
		DataType attr_type = null;
		SelectType sel_type = null;
		if (attr_tp == AttributeDefinition.EXPLICIT) {
			attr_type = (DataType)((CExplicit_attribute)this).getDomain(null);
		} else if (attr_tp == AttributeDefinition.DERIVED) {
			return false;
		} else {
			String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		DataType type = attr_type;
		while (type.express_type == DataType.DEFINED_TYPE) {
			DataType underlying_type = (DataType)((CDefined_type)type).getDomain(null);
			if (underlying_type.express_type >= DataType.SELECT && underlying_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
				sel_type = (SelectType)underlying_type;
				break;
			} else if (underlying_type.express_type == DataType.DEFINED_TYPE) {
				type = underlying_type;
			} else {
				break;
			}
		}
		if (sel_type == null) {
			return attr_type.search_entity(def_for_value);
		} else {
			return sel_type.search_entity_select(def_for_value);
		}
	}

}
