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

/** A supertype of each of CExtended_enumeration_type, CExtensible_enumeration_type 
 * and CExtended_enumeration_type$extensible_enumeration_type; for internal JSDAI use only. 
 * Needed to implement extensible enumerations. 
 */
public abstract class EnumerationType extends DataType {

	protected String em_wrong_method_used = SdaiSession.line_separator + AdditionalMessages.DI_WRME;

	EEnumeration_type [] enum_extensions;
	A_string elements;
	String [] elementsExt;
	CSchema_definition elements_schema;
	static final int NUMBER_OF_EXTENSIONS = 16;

	protected EnumerationType () {
		super();
	}

	protected A_string getElementsExtensible(CExplicit_attribute attr, SdaiContext context) throws SdaiException {
		CSchema_definition schema = null;
		if (express_type == DataType.EXTENSIBLE_ENUM || express_type == DataType.EXTENDED_EXTENSIBLE_ENUM) {
			if (context != null) {
				schema = (CSchema_definition)context.schema;
			} else {
				schema = owning_model.underlying_schema;
			}
		}
		if (elements != null && 
				(!(express_type == DataType.EXTENSIBLE_ENUM || express_type == DataType.EXTENDED_EXTENSIBLE_ENUM) || 
				elements_schema == schema)) {
			return elements;
		}
		int i;

		DataType domain = (DataType)attr.getDomain(null);
		while (domain.express_type == DataType.DEFINED_TYPE) {
			domain = (DataType)((CDefined_type)domain).getDomain(null);
		}
		if (domain.express_type < DataType.LIST || domain.express_type > DataType.AGGREGATE) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		A_string aggr = new A_string();
		aggr.attach((AggregationType)domain, this);

		StaticFields staticFields = StaticFields.get();
		if (staticFields.it_aggr == null) {
			staticFields.it_aggr = aggr.createIterator();
		} else {
			aggr.attachIterator(staticFields.it_aggr);
		}
		if (express_type == DataType.EXTENDED_ENUM || express_type == DataType.EXTENDED_EXTENSIBLE_ENUM) {
			String [] enumExt = getElementsExtended();
			for (i = 0; i < enumExt.length; i++) {
				aggr.addAfter(staticFields.it_aggr, enumExt[i]);
				staticFields.it_aggr.next();
			}
		} else {
			A_string self_elements = ((EEnumeration_type)this).getLocal_elements(null);
			if (staticFields.it_el == null) {
				staticFields.it_el = self_elements.createIterator();
			} else {
				self_elements.attachIterator(staticFields.it_el);
			}
			while (staticFields.it_el.next()) {
				aggr.addAfter(staticFields.it_aggr, (String)self_elements.getCurrentMemberObject(staticFields.it_el));
				staticFields.it_aggr.next();
			}
		}
		if (express_type == DataType.EXTENSIBLE_ENUM || express_type == DataType.EXTENDED_EXTENSIBLE_ENUM) {
			addExtensibleEnum(aggr, staticFields.it_aggr, schema, true);
			elements_schema = schema;
		}
		elements = aggr;
		return aggr;
	}


	private void addExtensibleEnum(A_string aggr, SdaiIterator it_aggr, CSchema_definition schema, boolean first) 
			throws SdaiException {
		if (!first) {
			A_string self_elements = ((EEnumeration_type)this).getLocal_elements(null);
			int count = aggr.myLength;
			StaticFields staticFields = StaticFields.get();
			if (staticFields.it_el == null) {
				staticFields.it_el = self_elements.createIterator();
			} else {
				self_elements.attachIterator(staticFields.it_el);
			}
			while (staticFields.it_el.next()) {
				String element = (String)self_elements.getCurrentMemberObject(staticFields.it_el);
				if (isDifferent(staticFields, aggr, element, count)) {
					aggr.addAfter(it_aggr, element);
					it_aggr.next();
				}
			}
			if (express_type != DataType.EXTENSIBLE_ENUM && express_type != DataType.EXTENDED_EXTENSIBLE_ENUM) {
				return;
			}
		}
		EEnumeration_type [] exts = getExtensionsEnum(schema);
		for (int i = 0; i < exts.length; i++) {
			((EnumerationType)exts[i]).addExtensibleEnum(aggr, it_aggr, schema, false);
		}
	}


	private EEnumeration_type [] getExtensionsEnum(CSchema_definition schema) throws SdaiException {
		int i;
		SdaiModel model = schema.modelDictionary;
		if (model.getMode() == SdaiModel.NO_ACCESS) {
			model.startReadOnlyAccess();
		}

		int [] ext_enum = new int[2];
		ext_enum[0] = SdaiSession.EXTENDED_ENUM_TYPE;
		ext_enum[1] = SdaiSession.EXTENDED_EXTENSIBLE_ENUM_TYPE;

		StaticFields staticFields = StaticFields.get();
		if (staticFields.aux_for_ext_enum == null) {
			staticFields.aux_for_ext_enum = new EEnumeration_type[NUMBER_OF_EXTENSIONS];
		}
		int count = 0;
		EExtended_enumeration_type extended_enum;
		DataType based_on;
		for (int j = 0; j < 2; j++) {
			CEntity [] enum_types = model.instances_sim[ext_enum[j]];
			for (i = 0; i < model.lengths[ext_enum[j]]; i++) {
				extended_enum = (EExtended_enumeration_type)enum_types[i];
				based_on = (DataType)extended_enum.getIs_based_on(null);
				while (based_on.express_type == DataType.DEFINED_TYPE) {
					based_on = (DataType)((CDefined_type)based_on).getDomain(null);
				}
				if (this == based_on) {
					if (count >= staticFields.aux_for_ext_enum.length) {
						enlargeExtensions(staticFields);
					}
					staticFields.aux_for_ext_enum[count++] = extended_enum;
				}
			}
		}

		CDefined_type def_type;
		DataType domain;
		CEntity [] d_types = model.instances_sim[SdaiSession.REFERENCED_DECL_TYPE_DECL];
		for (i = 0; i < model.lengths[SdaiSession.REFERENCED_DECL_TYPE_DECL]; i++) {
			CReferenced_declaration$type_declaration dec_ref = 
				(CReferenced_declaration$type_declaration)d_types[i];
			def_type = (CDefined_type)dec_ref.getDefinition(null);
			domain = (DataType)def_type.getDomain(null);
			if (domain.express_type != DataType.EXTENDED_ENUM && domain.express_type != DataType.EXTENDED_EXTENSIBLE_ENUM) {
				continue;
			}
			extended_enum = (EExtended_enumeration_type)domain;
			based_on = (DataType)extended_enum.getIs_based_on(null);
			while (based_on.express_type == DataType.DEFINED_TYPE) {
				based_on = (DataType)((CDefined_type)based_on).getDomain(null);
			}
			if (this == based_on) {
				if (count >= staticFields.aux_for_ext_enum.length) {
					enlargeExtensions(staticFields);
				}
				staticFields.aux_for_ext_enum[count++] = extended_enum;
			}
		}
		d_types = model.instances_sim[SdaiSession.TYPE_DECL_USED_DECL];
		for (i = 0; i < model.lengths[SdaiSession.TYPE_DECL_USED_DECL]; i++) {
			CType_declaration$used_declaration dec_used = 
				(CType_declaration$used_declaration)d_types[i];
			def_type = (CDefined_type)dec_used.getDefinition(null);
			domain = (DataType)def_type.getDomain(null);
			if (domain.express_type != DataType.EXTENDED_ENUM && domain.express_type != DataType.EXTENDED_EXTENSIBLE_ENUM) {
				continue;
			}
			extended_enum = (EExtended_enumeration_type)domain;
			based_on = (DataType)extended_enum.getIs_based_on(null);
			while (based_on.express_type == DataType.DEFINED_TYPE) {
				based_on = (DataType)((CDefined_type)based_on).getDomain(null);
			}
			if (this == based_on) {
				if (count >= staticFields.aux_for_ext_enum.length) {
					enlargeExtensions(staticFields);
				}
				staticFields.aux_for_ext_enum[count++] = extended_enum;
			}
		}
		d_types = model.instances_sim[SdaiSession.IMPLICIT_DECL_TYPE_DECL];
		for (i = 0; i < model.lengths[SdaiSession.IMPLICIT_DECL_TYPE_DECL]; i++) {
			CImplicit_declaration$type_declaration dec_imp = 
				(CImplicit_declaration$type_declaration)d_types[i];
			def_type = (CDefined_type)dec_imp.getDefinition(null);
			domain = (DataType)def_type.getDomain(null);
			if (domain.express_type != DataType.EXTENDED_ENUM && domain.express_type != DataType.EXTENDED_EXTENSIBLE_ENUM) {
				continue;
			}
			extended_enum = (EExtended_enumeration_type)domain;
			based_on = (DataType)extended_enum.getIs_based_on(null);
			while (based_on.express_type == DataType.DEFINED_TYPE) {
				based_on = (DataType)((CDefined_type)based_on).getDomain(null);
			}
			if (this == based_on) {
				if (count >= staticFields.aux_for_ext_enum.length) {
					enlargeExtensions(staticFields);
				}
				staticFields.aux_for_ext_enum[count++] = extended_enum;
			}
		}

		enum_extensions = new EEnumeration_type[count];
		System.arraycopy(staticFields.aux_for_ext_enum, 0, enum_extensions, 0, count);
		return enum_extensions;
	}


	private String [] getElementsExtended() throws SdaiException {
		if (elementsExt != null) {
			return elementsExt;
		}
		StaticFields staticFields = StaticFields.get();
		A_string self_elements = ((EEnumeration_type)this).getLocal_elements(null);
		if (express_type == DataType.EXTENDED_ENUM || express_type == DataType.EXTENDED_EXTENSIBLE_ENUM) {
			DataType based_on = (DataType)((EExtended_enumeration_type)this).getIs_based_on(null);
			while (based_on.express_type == DataType.DEFINED_TYPE) {
				based_on = (DataType)((CDefined_type)based_on).getDomain(null);
			}
			EExtensible_enumeration_type extensible_enum = (EExtensible_enumeration_type)based_on;
			String [] inherited_elements = ((EnumerationType)extensible_enum).getElementsExtended();
			int count = inherited_elements.length;
			if (staticFields.it_el == null) {
				staticFields.it_el = self_elements.createIterator();
			} else {
				self_elements.attachIterator(staticFields.it_el);
			}
			while (staticFields.it_el.next()) {
				if (isDifferent(inherited_elements, (String)self_elements.getCurrentMemberObject(staticFields.it_el), 
						inherited_elements.length)) {
					count++;
				}
			}
			elementsExt = new String[count];
			System.arraycopy(inherited_elements, 0, elementsExt, 0, inherited_elements.length);
//for (i = 0; i < inherited_elements.length; i++)
//System.out.println("EnumerationType  Case 0  added   extended: " + ((String)inherited_elements[i]).getName(null) + 
//"    i = " + i + "    elementsExt.length: " + elementsExt.length);
			count = inherited_elements.length;
			staticFields.it_el.beginning();
			while (staticFields.it_el.next()) {
				String element = (String)self_elements.getCurrentMemberObject(staticFields.it_el);
				if (isDifferent(inherited_elements, element, inherited_elements.length)) {
					elementsExt[count++] = element;
				}
			}
		} else {
			elementsExt = new String[self_elements.myLength];
			if (staticFields.it_el == null) {
				staticFields.it_el = self_elements.createIterator();
			} else {
				self_elements.attachIterator(staticFields.it_el);
			}
			int i = 0;
			while (staticFields.it_el.next()) {
				elementsExt[i++] = (String)self_elements.getCurrentMemberObject(staticFields.it_el);
			}
		}
		return elementsExt;
	}


	private boolean isDifferent(String [] elements, String element, int count) throws SdaiException {
		for (int i = 0; i < count; i++) {
			if (element == elements[i]) {
				return false;
			}
		}
		return true;
	}


	private boolean isDifferent(StaticFields staticFields, A_string elements, String element, int count) throws SdaiException {
		if (staticFields.it_checking == null) {
			staticFields.it_checking = elements.createIterator();
		} else {
			elements.attachIterator(staticFields.it_checking);
		}
		int i = 0;
		while (staticFields.it_checking.next() && i < count) {
			if (element == elements.getCurrentMemberObject(staticFields.it_checking)) {
				return false;
			}
			i++;
		}
		return true;
	}


	private static void enlargeExtensions(StaticFields staticFields) {
		int new_length = staticFields.aux_for_ext_enum.length * 2;
		EEnumeration_type [] new_aux_for_ext = new EEnumeration_type[new_length];
		System.arraycopy(staticFields.aux_for_ext_enum, 0, new_aux_for_ext, 0, staticFields.aux_for_ext_enum.length);
		staticFields.aux_for_ext_enum = new_aux_for_ext;
	}

}
