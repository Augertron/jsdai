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

/** Supertype of CSelect_type, for internal JSDAI use only. 
  Needed to hold non-public data fields and methods */
public abstract class SelectType extends DataType {

	protected int is_mixed; // 0 - pure entities, 1 - pure defined types, 2 - mixed
	protected CDefined_type [] paths[];
	protected int count;
	protected int [] tags;
	protected CEntity [] types;
	protected static final String em_wrong_method_used = SdaiSession.line_separator + AdditionalMessages.DI_WRMS;

	static final int NUMBER_OF_PATHS = 8;

	CDefined_type [] route;
	int count_ent_leaves, count_other_leaves;


// Data for extensible selects
	ESelect_type [] extensions;
	ANamed_type selections;
	ENamed_type [] selectionsExt;
	CSchema_definition selections_schema;
	static final int NUMBER_OF_EXTENSIONS = 16;

	protected SelectType () {
		super();
	}



	void enumerateSelectPaths(SdaiContext context) throws SdaiException {
if (SdaiSession.debug2) System.out.println(" IN ENUM SEL " + instance_identifier + 
"   type: " + this.getClass().getName());
		count = 0;
// The following lines were added 2005-03-24; they restrict functionality of extensible selects
//		if (this instanceof EExtensible_select_type || this instanceof EExtended_select_type) {
//			is_mixed = 0;
//			return;
//		}
// Until here 2005-03-24
		count_ent_leaves = 0;
		count_other_leaves = 0;
		route = new CDefined_type[SdaiSession.NUMBER_OF_DEFINED_TYPES];
		paths = new CDefined_type[NUMBER_OF_PATHS][];
		tags = new int[NUMBER_OF_PATHS];
		types = new CEntity[NUMBER_OF_PATHS];
		ANamed_type sels;
//		ANamed_type sels = ((ESelect_type)this).getSelections(null, context);
		if (this instanceof EExtensible_select_type || this instanceof EExtended_select_type) {
			sels = ((ESelect_type)this).getLocal_selections(null);
		} else {
			sels = ((ESelect_type)this).getSelections(null, context);
		}
		if (((AEntity)sels).myLength < 0) {
			((AEntity)sels).resolveAllConnectors();
		}
		for (int i = 1; i <= ((AEntity)sels).myLength; i++) {
			ENamed_type alternative = (ENamed_type)sels.getByIndexObject(i);
if (SdaiSession.debug2) System.out.println(" alternative " + ((CEntity)alternative).instance_identifier + " name: " + alternative.getClass().getName());
			if (alternative instanceof CDefined_type) {
				searchPath((CDefined_type)alternative, 0, false, context);
			} else {
				count_ent_leaves++;
			}
		}
if (SdaiSession.debug2) System.out.println(" AFTER ENUM SEL  " + count);
		shrinkSelectPaths();
		if (count_ent_leaves > 0) {
			if (count_other_leaves == 0) {
				is_mixed = 0;
			} else {
				is_mixed = 2;
			}
		} else if (count_other_leaves > 0) {
			is_mixed = 1;
		}
if (SdaiSession.debug2) printPaths();
	}
	private void searchPath(CDefined_type def_type, int path_length, boolean last_is_def_type, 
			SdaiContext context) throws SdaiException {
		EEntity domain = def_type.getDomain(null);
		if (domain instanceof ESimple_type || domain instanceof EAggregation_type || 
				domain instanceof EEnumeration_type) {
			if (count >= paths.length) {
				enlargePaths();
				enlargeTags();
				enlargeTypes();
			}
			int final_path_length;
			if (last_is_def_type) {
				final_path_length = path_length;
			} else {
				final_path_length = path_length + 1;
			}
			paths[count] = new CDefined_type[final_path_length];
//			paths[count] = new CDefined_type[path_length + 1];
//int ttt = path_length + 1;
//System.out.println(" New path No. " + count + "   of length = " + ttt + "  sel type" + this);
			for (int i = 0; i < path_length; i++) {
if (SdaiSession.debug2) System.out.println(" route 1: " + route[i].getName(null));
				paths[count][i] = route[i];
			}
			if (!last_is_def_type) {
				paths[count][path_length] = def_type;
			}
if (SdaiSession.debug2) System.out.println(" route 2: " + def_type.getName(null) 
+ "   instance: " + def_type.instance_identifier);
			if (domain instanceof EInteger_type) {
				tags[count] = PhFileReader.INTEGER;
				types[count] = (CEntity)domain; // added
			} else if (domain instanceof EReal_type) {
				tags[count] = PhFileReader.REAL;
				types[count] = (CEntity)domain; // added
			} else if (domain instanceof EString_type) {
				tags[count] = PhFileReader.STRING;
				types[count] = (CEntity)domain;
			} else if (domain instanceof EBinary_type) {
				tags[count] = PhFileReader.BINARY;
				types[count] = (CEntity)domain;
			} else if (domain instanceof ELogical_type) {
				tags[count] = PhFileReader.LOGICAL;
				types[count] = (CEntity)domain; // added
			} else if (domain instanceof EBoolean_type) {
				tags[count] = PhFileReader.BOOLEAN;
				types[count] = (CEntity)domain; // added
			} else if (domain instanceof ENumber_type) {
				tags[count] = PhFileReader.REAL;
				types[count] = (CEntity)domain; // added
			} else if (domain instanceof EEnumeration_type) {
				tags[count] = PhFileReader.ENUM;
				types[count] = (CEntity)domain;
			} else if (domain instanceof EAggregation_type) {
				tags[count] = PhFileReader.EMBEDDED_LIST;
				types[count] = (CEntity)domain;
if (SdaiSession.debug2) {
Class ccc = ((AggregationType)domain).getAggregateClass();
System.out.println("   SelectType = " + "    aggr: " + ccc.getName()
+ "   domain: " + ((CEntity)domain).instance_identifier);}
			}
			count++;
if (SdaiSession.debug2) System.out.println("  SIMPLE type  " + count);
			count_other_leaves++;
		} else if (domain instanceof CEntity_definition) {
			count_ent_leaves++;
		} else if (domain instanceof ESelect_type) {
			ANamed_type sels;
			if (domain instanceof EExtensible_select_type || domain instanceof EExtended_select_type) {
				sels = ((ESelect_type)domain).getLocal_selections(null);
			} else {
				sels = ((ESelect_type)domain).getSelections(null, context);
			}
//			ANamed_type sels = ((ESelect_type)domain).getSelections(null, context);
			if (((AEntity)sels).myLength < 0) {
				((AEntity)sels).resolveAllConnectors();
			}
			ENamed_type alternative;
			int ln = ((AEntity)sels).myLength;
			if (ln == 1) {
				alternative = (ENamed_type)((AEntity)sels).myData;
				if (alternative instanceof CDefined_type) {
					searchPath((CDefined_type)alternative, path_length, false, context);
				} else {
					count_ent_leaves++;
				}
			} else {
				Object [] myDataA = (Object [])((AEntity)sels).myData;
				for (int i = 0; i < ln; i++) {
					alternative = (ENamed_type)myDataA[i];
					if (alternative instanceof CDefined_type) {
						searchPath((CDefined_type)alternative, path_length, false, context);
					} else {
						count_ent_leaves++;
					}
				}
			}
		} else if (domain instanceof CDefined_type) {
			if (!last_is_def_type) {
				if (path_length >= route.length) {
					enlargeRoute();
				}
				route[path_length] = def_type;
				path_length++;
			}
			searchPath((CDefined_type)domain, path_length, true, context);
		}
	}
	private void shrinkSelectPaths() throws SdaiException {
		int m = 0;
		boolean found;
		for (int i = 0; i < count; i++) {
			CDefined_type [] def = paths[i];
			found = false;
			for (int j = 0; j < m; j++) {
				if (def.length != paths[j].length) {
					continue;
				}
				found = true;
				for (int k = 0; k < def.length; k++) {
					if (def[k] != paths[j][k]) {
						found = false;
						break;
					}
				}
				if (found) {
					break;
				}
			}
			if (!found) {
				paths[m] = def;
				tags[m] = tags[i];
				types[m] = types[i];
				m++;
			}
		}
		count = m;
	}

	Class getSelectNestedAggregate(String name, int level) 
			throws SdaiException {
		SSuper sup = owning_model.schemaData.super_inst;
		String str = sup.getClass().getName();
		int index = str.lastIndexOf('.');
		String many_a = ".A";
		for (int i = 1; i < level; i++) {
			many_a += "a";
		}
		String s = str.substring(0, index) + many_a + CEntity.normalise(name);
		try {
			return Class.forName(s, true, SdaiClassLoaderProvider.getDefault().getClassLoader());
		} catch (ClassNotFoundException e) {
			throw new SdaiException(SdaiException.SY_ERR, "early binding entity-aggregate class not found");
		}
	}

	protected int giveSelectNumber(EDefined_type [] select) {
		int array_length = 0;
		for (int i = 0; i < select.length; i++) {
			if (select[i] == null) {
				break;
			}
			array_length++;
		}
		return giveSelectNumber(select, array_length);
	}


	protected int giveSelectNumber(EDefined_type [] select, int array_length) {
		CDefined_type [] row;
		boolean found;
		for (int i = 0; i < count; i++) {
			row = paths[i];
			if (row.length != array_length) {
				continue;
			}
			found = true;
			for (int j = 0; j < row.length; j++) {
				if (select[j] != row[j]) {
					found = false;
					break;
				}
			}
			if (found) {
				return i + 2;
			}
		}
//for (int i = 0; i < count; i++) {
//row = paths[i];
//int ii=i+2;
//System.out.println("*************  No. " + ii);
//for (int j = 0; j < row.length; j++) {
//CDefined_type def = row[j];
//try {
//System.out.println("  def: " + def.getName(null));
//} catch (Exception ex) {
//}
//}
//System.out.println();
//}
		return -1;
	}

	protected int giveDefinedTypes(int select_number, EDefined_type [] select) throws  SdaiException {
		if (select_number <= 1 || select_number > count + 1) {
			return -2;
		}
		CDefined_type [] row = paths[select_number - 2];
		if (select == null || select.length < row.length) {
			return -1;
		}
		for (int i = 0; i < row.length; i++) {
			select[i] = row[i];
		}
		if (select.length > row.length) {
			select[row.length] = null;
		}
		return 0;
	}

/*	protected int giveDefinedTypesArrayLength(int select_number) throws  SdaiException {
		if (select_number <= 1 || select_number > count + 1) {
			return -1;
		}
		CDefined_type [] row = paths[select_number - 2];
		return row.length;
	}*/

	protected CDefined_type [] getSelectArray(int select_number) throws  SdaiException {
//System.out.println("## in getSelectArray - paths lenght: " + paths.length + ", select_number: " + select_number);
		if (select_number <= 1) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_IVP;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		return paths[select_number - 2];
	}


	protected int getType(EDefined_type [] select) {
		if (select == null) {
			return -1;
		}
		int i;
		CDefined_type [] row;
		boolean found;
		int array_length = 0;
		for (i = 0; i < select.length; i++) {
			if (select[i] == null) {
				break;
			}
			array_length++;
		}
		for (i = 0; i < count; i++) {
			row = paths[i];
			if (row.length != array_length) {
				continue;
			}
			found = true;
			for (int j = 0; j < row.length; j++) {
				if (select[j] != row[j]) {
					found = false;
					break;
				}
			}
			if (found) {
				return tags[i];
			}
		}
		return -1;
	}


	boolean analyse_entity_in_select(CEntity_definition value_type, SdaiContext context) throws SdaiException {
		boolean res;
		if (express_type == DataType.ENT_EXT_SELECT) {
			return true;
		}
		AEntity sels = ((ESelect_type)this).getSelections(null, context);
		if (sels.myLength < 0) {
			sels.resolveAllConnectors();
		}
		Object [] myDataA = null;
		if (sels.myLength > 1) {
			myDataA = (Object [])sels.myData;
		}
		for (int i = 0; i < ((AEntity)sels).myLength; i++) {
			DataType dom;
			if (sels.myLength == 1) {
				dom = (DataType)sels.myData;
			} else {
				dom = (DataType)myDataA[i];
			}
			if (dom.express_type == DataType.ENTITY) {
				res = value_type.isSubtypeOf((CEntity_definition)dom);
				if (res) {
					return true;
				}
				continue;
			}
			while (dom.express_type == DataType.DEFINED_TYPE) {
				dom = (DataType)((CDefined_type)dom).getDomain(null);
			}
			if (dom.express_type >= DataType.SELECT && dom.express_type <= DataType.ENT_EXT_EXT_SELECT) {
				res = ((SelectType)dom).analyse_entity_in_select(value_type, context);
				if (res) {
					return true;
				}
			}
		}
		return false;
	}


	boolean allow_entity_select() throws SdaiException {
		AEntity sels;
		if (express_type >= DataType.EXTENSIBLE_SELECT && express_type <= DataType.ENT_EXT_EXT_SELECT) {
			sels = ((EExtensible_select_type)this).getSelections(null, null);
		} else {
			sels = ((ESelect_type)this).getSelections(null);
		}

		if (sels.myLength < 0) {
			sels.resolveAllConnectors();
		}
		Object [] myDataA = null;
		if (sels.myLength > 1) {
			myDataA = (Object [])sels.myData;
		}
		boolean res = false;
		for (int i = 0; i < ((AEntity)sels).myLength; i++) {
			DataType dom;
			if (sels.myLength == 1) {
				dom = (DataType)sels.myData;
			} else {
				dom = (DataType)myDataA[i];
			}
			if (dom.express_type == DataType.ENTITY) {
				return true;
			} else if (dom.express_type >= DataType.LIST && dom.express_type <= DataType.AGGREGATE) {
				res = ((AggregationType)dom).allow_entity_aggregate((EAggregation_type)dom);
				if (res) {
					return true;
				}
			} else if (dom.express_type >= DataType.SELECT && dom.express_type <= DataType.ENT_EXT_EXT_SELECT) {
				res = ((SelectType)dom).allow_entity_select();
				if (res) {
					return true;
				}
			} else if (dom.express_type != DataType.DEFINED_TYPE) {
				continue;
			}
			while (dom.express_type == DataType.DEFINED_TYPE) {
				dom = (DataType)((CDefined_type)dom).getDomain(null);
			}
			if (dom.express_type >= DataType.SELECT && dom.express_type <= DataType.ENT_EXT_EXT_SELECT) {
				res = ((SelectType)dom).allow_entity_select();
				if (res) {
					return true;
				}
			} else {
				res = dom.allow_entity();
				if (res) {
					return true;
				}
			}
		}
		return res;
	}


	boolean search_entity_select(CEntity_definition def_for_value) throws SdaiException {
		AEntity sels;
		if (express_type >= DataType.EXTENSIBLE_SELECT && express_type <= DataType.ENT_EXT_EXT_SELECT) {
			sels = ((EExtensible_select_type)this).getSelections(null, null);
		} else {
			sels = ((ESelect_type)this).getSelections(null);
		}

		if (sels.myLength < 0) {
			sels.resolveAllConnectors();
		}
		Object [] myDataA = null;
		if (sels.myLength > 1) {
			myDataA = (Object [])sels.myData;
		}
		boolean res = false;
		for (int i = 0; i < sels.myLength; i++) {
			DataType dom;
			if (sels.myLength == 1) {
				dom = (DataType)sels.myData;
			} else {
				dom = (DataType)myDataA[i];
			}
			if (dom.express_type == DataType.ENTITY) {
				if (def_for_value.isSubtypeOf((CEntity_definition)dom)) {
					return true;
				}
			} else if (dom.express_type >= DataType.LIST && dom.express_type <= DataType.AGGREGATE) {
				res = ((AggregationType)dom).search_entity_aggregate(def_for_value);
				if (res) {
					return true;
				}
			} else if (dom.express_type >= DataType.SELECT && dom.express_type <= DataType.ENT_EXT_EXT_SELECT) {
				res = ((SelectType)dom).search_entity_select(def_for_value);
				if (res) {
					return true;
				}
			} else if (dom.express_type != DataType.DEFINED_TYPE) {
				continue;
			}
			while (dom.express_type == DataType.DEFINED_TYPE) {
				dom = (DataType)((CDefined_type)dom).getDomain(null);
			}
			if (dom.express_type >= DataType.SELECT && dom.express_type <= DataType.ENT_EXT_EXT_SELECT) {
				res = ((SelectType)dom).search_entity_select(def_for_value);
				if (res) {
					return true;
				}
			} else {
				res = dom.search_entity(def_for_value);
				if (res) {
					return true;
				}
			}
		}
		return res;
	}


/**
	Checks if the specified entity is valid for the specified select type,
	that is, belongs to its (or of a select type derived from it) selections list.
	If so, then value 1 is returned. If not, then the return value is 0, and if
	the answer is not found, then value is 2.
	This method is invoked in <code>validateExplicitAttributesReferences</code>.
*/
	boolean check_instance_in_select(SdaiModel model, CEntity_definition def) throws SdaiException {
		boolean res;
		ANamed_type	selections;
		if (express_type >= DataType.EXTENSIBLE_SELECT && express_type <= DataType.ENT_EXT_EXT_SELECT) {
			SdaiSession ss = model.repository.session;
			if (ss.sdai_context == null) {
				if (!ss.sdai_context_missing) {
					ss.sdai_context_missing = true;
					ss.printWarningToLogoSdaiContext(AdditionalMessages.SS_SCMI);
				}
			}
			selections = ((EExtensible_select_type)this).getSelections(null, ss.sdai_context);
		} else {
			selections = ((ESelect_type)this).getSelections(null);
		}
		AEntity sels = (AEntity)selections;
		if (sels.myLength < 0) {
			sels.resolveAllConnectors();
		}
		Object [] myDataA = null;
		if (sels.myLength > 1) {
			myDataA = (Object [])sels.myData;
		}
		for (int i = 0; i < sels.myLength; i++) {
			DataType element;
			if (sels.myLength == 1) {
				element = (DataType)sels.myData;
			} else {
				element = (DataType)myDataA[i];
			}
			if (element.express_type == DataType.ENTITY) {
				if (def.isSubtypeOf((CEntity_definition)element)) {
					return true;
				}
			} else if (element.express_type >= DataType.SELECT && element.express_type <= DataType.ENT_EXT_EXT_SELECT) {
				res = ((SelectType)element).check_instance_in_select(model, def);
				if (res) {
					return res;
				}
			} else if (element.express_type == DataType.DEFINED_TYPE) {
				DataType type = element;
				while (type.express_type == DataType.DEFINED_TYPE) 	{
					type = (DataType)((CDefined_type)type).getDomain(null);
				}
				if (type.express_type == DataType.ENTITY) {
					if (def.isSubtypeOf((CEntity_definition)type)) {
						return true;
					}
				} else if (type.express_type >= DataType.SELECT && type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
					res = ((SelectType)type).check_instance_in_select(model, def);
					if (res) {
						return res;
					}
				}
			}
		}
		return false;
	}


	private void enlargeRoute() {
		int new_length = route.length * 2;
		CDefined_type [] new_route = new CDefined_type[new_length];
		System.arraycopy(route, 0, new_route, 0, route.length);
		route = new_route;
	}

	private void enlargePaths() {
		int new_length = paths.length * 2;
		CDefined_type [] new_paths [] = new CDefined_type[new_length][];
		for (int i = 0; i < paths.length; i++) {
			new_paths[i] = new CDefined_type[paths[i].length];
			System.arraycopy(paths[i], 0, new_paths[i], 0, paths[i].length);
		}
		paths = new_paths;
	}

	private void enlargeTags() {
		int new_length = tags.length * 2;
		int [] new_tags = new int[new_length];
		System.arraycopy(tags, 0, new_tags, 0, tags.length);
		tags = new_tags;
	}

	private void enlargeTypes() {
		int new_length = types.length * 2;
		CEntity [] new_types = new CEntity[new_length];
		System.arraycopy(types, 0, new_types, 0, types.length);
		types = new_types;
	}


	private void printPaths() throws  SdaiException {
		int i, j, k;
		CDefined_type [] path;
		for (i = 0; i < count; i++) {
			k = i + 2;
			System.out.println("Path for select number " + k + " :");
			path = paths[i];
			for (j = 0; j < path.length; j++) {
				System.out.print("   " + path[j].getName(null));
			}
			System.out.println();
		}
	}


	protected ANamed_type getSelectionsExtensible(CExplicit_attribute attr, SdaiContext context) throws SdaiException {
		CSchema_definition schema = null;
		if (express_type >= DataType.EXTENSIBLE_SELECT && express_type <= DataType.ENT_EXT_EXT_SELECT) {
			if (context != null) {
				schema = (CSchema_definition)context.schema;
			} else {
				StaticFields staticFields = StaticFields.get();
				schema = staticFields.context_schema;
				if (schema == null) {
					schema = owning_model.underlying_schema;
				}
			}
		}
		if (selections != null && (express_type < DataType.EXTENSIBLE_SELECT || selections_schema == schema)) {
			return selections;
		}
		int i;
		DataType domain = (DataType)attr.getDomain(null);
		while (domain.express_type == DataType.DEFINED_TYPE) {
			domain = (DataType)((CDefined_type)domain).getDomain(null);
		}
		if (domain.express_type < DataType.LIST || domain.express_type > DataType.AGGREGATE) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		ANamed_type aggr = new ANamed_type();
		((AEntity)aggr).attach((AggregationType)domain, this);

		if ( (express_type >= DataType.EXTENDED_SELECT && express_type <= DataType.ENT_EXT_NON_EXT_SELECT) || 
				express_type == DataType.EXT_EXT_SELECT || express_type == DataType.ENT_EXT_EXT_SELECT) {
			ENamed_type [] selExt = getSelectionsExtended();
			for (i = 0; i < selExt.length; i++) {
				aggr.addUnordered(selExt[i]);
			}
		} else {
			ANamed_type self_selections = ((ESelect_type)this).getLocal_selections(null);
			if (((AEntity)self_selections).myLength < 0) {
				((AEntity)self_selections).resolveAllConnectors();
			}
			for (i = 0; i < ((AEntity)self_selections).myLength; i++) {
				aggr.addUnordered(self_selections.getByIndex(i + 1));
			}
		}
		if (express_type >= DataType.EXTENSIBLE_SELECT && express_type <= DataType.ENT_EXT_EXT_SELECT) {
			addExtensible(aggr, schema, true);
			selections_schema = schema;
		}
		selections = aggr;
		return aggr;
	}


	private void addExtensible(ANamed_type aggr, CSchema_definition schema, boolean first) throws SdaiException {
		int i;
		if (!first) {
			ANamed_type self_selections = ((ESelect_type)this).getLocal_selections(null);
			if (((AEntity)self_selections).myLength < 0) {
				((AEntity)self_selections).resolveAllConnectors();
			}
			int count = ((AEntity)aggr).myLength;
			for (i = 1; i <= ((AEntity)self_selections).myLength; i++) {
				ENamed_type selection = self_selections.getByIndex(i);
				if (isDifferent(aggr, selection, count)) {
					aggr.addUnordered(selection);
				}
			}
			if (express_type < DataType.EXTENSIBLE_SELECT) {
				return;
			}
		}
		ESelect_type [] exts = getExtensions(schema);
		for (i = 0; i < exts.length; i++) {
			((SelectType)exts[i]).addExtensible(aggr, schema, false);
		}
	}


	private ESelect_type [] getExtensions(CSchema_definition schema) throws SdaiException {
		int i;
		SdaiModel model = schema.modelDictionary;
		if (model.getMode() == SdaiModel.NO_ACCESS) {
			model.startReadOnlyAccess();
		}

		int [] ext_sel = new int[5];
		ext_sel[0] = SdaiSession.ENT_EXT_EXT_SELECT_TYPE;
		ext_sel[1] = SdaiSession.ENT_EXT_NON_EXT_SELECT_TYPE;
		ext_sel[2] = SdaiSession.EXTENDED_SELECT_TYPE;
		ext_sel[3] = SdaiSession.EXT_EXT_SELECT_TYPE;
		ext_sel[4] = SdaiSession.EXT_NON_EXT_SELECT_TYPE;
		StaticFields staticFields = StaticFields.get();
		if (staticFields.aux_for_ext_sel == null) {
			staticFields.aux_for_ext_sel = new ESelect_type[NUMBER_OF_EXTENSIONS];
		}
		int count = 0;
		EExtended_select_type extended;
		DataType based_on;
		for (int j = 0; j < 5; j++) {
			CEntity [] select_types = model.instances_sim[ext_sel[j]];
			for (i = 0; i < model.lengths[ext_sel[j]]; i++) {
				extended = (EExtended_select_type)select_types[i];
				based_on = (DataType)extended.getIs_based_on(null);
				while (based_on.express_type == DataType.DEFINED_TYPE) {
					based_on = (DataType)((CDefined_type)based_on).getDomain(null);
				}
				if (this == based_on) {
					if (count >= staticFields.aux_for_ext_sel.length) {
						enlargeExtensions(staticFields);
					}
					staticFields.aux_for_ext_sel[count++] = extended;
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
			if (domain.express_type < DataType.EXTENDED_SELECT || (domain.express_type > DataType.ENT_EXT_NON_EXT_SELECT && 
				domain.express_type != DataType.EXT_EXT_SELECT && domain.express_type != DataType.ENT_EXT_EXT_SELECT)) {
				continue;
			}
			extended = (EExtended_select_type)domain;
			based_on = (DataType)extended.getIs_based_on(null);
			while (based_on.express_type == DataType.DEFINED_TYPE) {
				based_on = (DataType)((CDefined_type)based_on).getDomain(null);
			}
			if (this == based_on) {
				if (count >= staticFields.aux_for_ext_sel.length) {
					enlargeExtensions(staticFields);
				}
				staticFields.aux_for_ext_sel[count++] = extended;
			}
		}
		d_types = model.instances_sim[SdaiSession.TYPE_DECL_USED_DECL];
		for (i = 0; i < model.lengths[SdaiSession.TYPE_DECL_USED_DECL]; i++) {
			CType_declaration$used_declaration dec_used = 
				(CType_declaration$used_declaration)d_types[i];
			def_type = (CDefined_type)dec_used.getDefinition(null);
			domain = (DataType)def_type.getDomain(null);
			if (domain.express_type < DataType.EXTENDED_SELECT || (domain.express_type > DataType.ENT_EXT_NON_EXT_SELECT && 
				domain.express_type != DataType.EXT_EXT_SELECT && domain.express_type != DataType.ENT_EXT_EXT_SELECT)) {
				continue;
			}
			extended = (EExtended_select_type)domain;
			based_on = (DataType)extended.getIs_based_on(null);
			while (based_on.express_type == DataType.DEFINED_TYPE) {
				based_on = (DataType)((CDefined_type)based_on).getDomain(null);
			}
			if (this == based_on) {
				if (count >= staticFields.aux_for_ext_sel.length) {
					enlargeExtensions(staticFields);
				}
				staticFields.aux_for_ext_sel[count++] = extended;
			}
		}
		d_types = model.instances_sim[SdaiSession.IMPLICIT_DECL_TYPE_DECL];
		for (i = 0; i < model.lengths[SdaiSession.IMPLICIT_DECL_TYPE_DECL]; i++) {
			CImplicit_declaration$type_declaration dec_imp = 
				(CImplicit_declaration$type_declaration)d_types[i];
			def_type = (CDefined_type)dec_imp.getDefinition(null);
			domain = (DataType)def_type.getDomain(null);
			if (domain.express_type < DataType.EXTENDED_SELECT || (domain.express_type > DataType.ENT_EXT_NON_EXT_SELECT && 
				domain.express_type != DataType.EXT_EXT_SELECT && domain.express_type != DataType.ENT_EXT_EXT_SELECT)) {
				continue;
			}
			extended = (EExtended_select_type)domain;
			based_on = (DataType)extended.getIs_based_on(null);
			while (based_on.express_type == DataType.DEFINED_TYPE) {
				based_on = (DataType)((CDefined_type)based_on).getDomain(null);
			}
			if (this == based_on) {
				if (count >= staticFields.aux_for_ext_sel.length) {
					enlargeExtensions(staticFields);
				}
				staticFields.aux_for_ext_sel[count++] = extended;
			}
		}

		extensions = new ESelect_type[count];
		System.arraycopy(staticFields.aux_for_ext_sel, 0, extensions, 0, count);
		return extensions;
	}


	private ENamed_type [] getSelectionsExtended() throws SdaiException {
		int i;
		if (selectionsExt != null) {
			return selectionsExt;
		}
		ANamed_type self_selections = ((ESelect_type)this).getLocal_selections(null);
		if (((AEntity)self_selections).myLength < 0) {
			((AEntity)self_selections).resolveAllConnectors();
		}
		if ( (express_type >= DataType.EXTENDED_SELECT && express_type <= DataType.ENT_EXT_NON_EXT_SELECT) || 
				express_type == DataType.EXT_EXT_SELECT || express_type == DataType.ENT_EXT_EXT_SELECT) {
			DataType based_on = (DataType)((EExtended_select_type)this).getIs_based_on(null);
			while (based_on.express_type == DataType.DEFINED_TYPE) {
//			while (based_on instanceof CDefined_type) {
				based_on = (DataType)((CDefined_type)based_on).getDomain(null);
			}
			EExtensible_select_type extensible = (EExtensible_select_type)based_on;
			ENamed_type [] inherited_sels = ((SelectType)extensible).getSelectionsExtended();
			int count = inherited_sels.length;
			for (i = 1; i <= ((AEntity)self_selections).myLength; i++) {
				if (isDifferent(inherited_sels, self_selections.getByIndex(i), inherited_sels.length)) {
					count++;
				}
			}
			selectionsExt = new ENamed_type[count];
			System.arraycopy(inherited_sels, 0, selectionsExt, 0, inherited_sels.length);
//for (i = 0; i < inherited_sels.length; i++)
//System.out.println("SelectType  Case 0  added   extended: " + ((ENamed_type)inherited_sels[i]).getName(null) + 
//"    i = " + i + "    selectionsExt.length: " + selectionsExt.length);
			count = inherited_sels.length;
			for (i = 1; i <= ((AEntity)self_selections).myLength; i++) {
				ENamed_type selection = self_selections.getByIndex(i);
				if (isDifferent(inherited_sels, selection, inherited_sels.length)) {
					selectionsExt[count++] = selection;
				}
			}
		} else {
			selectionsExt = new ENamed_type[((AEntity)self_selections).myLength];
			for (i = 0; i < ((AEntity)self_selections).myLength; i++) {
				selectionsExt[i] = self_selections.getByIndex(i + 1);
			}
		}
		return selectionsExt;
	}


	private boolean isDifferent(ENamed_type [] sels, ENamed_type selection, int count) throws SdaiException {
		for (int i = 0; i < count; i++) {
			if (selection == sels[i]) {
				return false;
			}
		}
		return true;
	}


	private boolean isDifferent(ANamed_type sels, ENamed_type selection, int count) throws SdaiException {
		for (int i = 1; i <= count; i++) {
			if (selection == sels.getByIndex(i)) {
				return false;
			}
		}
		return true;
	}


	private static void enlargeExtensions(StaticFields staticFields) {
		int new_length = staticFields.aux_for_ext_sel.length * 2;
		ESelect_type [] new_aux_for_ext = new ESelect_type[new_length];
		System.arraycopy(staticFields.aux_for_ext_sel, 0, new_aux_for_ext, 0, staticFields.aux_for_ext_sel.length);
		staticFields.aux_for_ext_sel = new_aux_for_ext;
	}

}
