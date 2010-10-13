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

package jsdai.express_g.exp2.ui.command;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.TreeSet;
import java.util.Vector;
import java.util.Iterator;

import jsdai.SExpress_g_schema.APage;
import jsdai.SExpress_g_schema.APage_reference_to;
import jsdai.SExpress_g_schema.ARelation_placement;
import jsdai.SExpress_g_schema.EAttribute_placement;
import jsdai.SExpress_g_schema.EConstraint_relation_placement;
import jsdai.SExpress_g_schema.EData_type_placement;
import jsdai.SExpress_g_schema.EDefined_relation_placement;
import jsdai.SExpress_g_schema.EGraphics_diagram;
import jsdai.SExpress_g_schema.EObject_placement;
import jsdai.SExpress_g_schema.EPage;
import jsdai.SExpress_g_schema.EPage_reference_bundle;
import jsdai.SExpress_g_schema.EPage_reference_from;
import jsdai.SExpress_g_schema.EPage_reference_to;
import jsdai.SExpress_g_schema.EPage_relation;
import jsdai.SExpress_g_schema.EProperty;
import jsdai.SExpress_g_schema.ERelation_bundle;
import jsdai.SExpress_g_schema.ERelation_placement;
import jsdai.SExpress_g_schema.ESchema_relation_placement;
import jsdai.SExpress_g_schema.ESelect_relation_placement;
import jsdai.SExpress_g_schema.ESize;
import jsdai.SExpress_g_schema.ESupertype_placement;
import jsdai.SExtended_dictionary_schema.AAttribute;
import jsdai.SExtended_dictionary_schema.AEntity_definition;
import jsdai.SExtended_dictionary_schema.AEntity_or_view_definition;
import jsdai.SExtended_dictionary_schema.AEntity_or_view_or_subtype_expression;
import jsdai.SExtended_dictionary_schema.AGlobal_rule;
import jsdai.SExtended_dictionary_schema.AInterfaced_declaration;
import jsdai.SExtended_dictionary_schema.AInverse_attribute;
import jsdai.SExtended_dictionary_schema.ANamed_type;
import jsdai.SExtended_dictionary_schema.AUniqueness_rule;
import jsdai.SExtended_dictionary_schema.AWhere_rule;
import jsdai.SExtended_dictionary_schema.CAttribute;
import jsdai.SExtended_dictionary_schema.CInverse_attribute;
import jsdai.SExtended_dictionary_schema.EAggregation_type;
import jsdai.SExtended_dictionary_schema.EAlgorithm_definition;
import jsdai.SExtended_dictionary_schema.EAnd_subtype_expression;
import jsdai.SExtended_dictionary_schema.EAndor_subtype_expression;
import jsdai.SExtended_dictionary_schema.EAnnotation;
import jsdai.SExtended_dictionary_schema.EArray_type;
import jsdai.SExtended_dictionary_schema.EAttribute;
import jsdai.SExtended_dictionary_schema.EBag_type;
import jsdai.SExtended_dictionary_schema.EBinary_type;
import jsdai.SExtended_dictionary_schema.EBoolean_type;
import jsdai.SExtended_dictionary_schema.EConstant_definition;
import jsdai.SExtended_dictionary_schema.EData_type;
import jsdai.SExtended_dictionary_schema.EDeclaration;
import jsdai.SExtended_dictionary_schema.EDefined_type;
import jsdai.SExtended_dictionary_schema.EDerived_attribute;
import jsdai.SExtended_dictionary_schema.EEntity_definition;
import jsdai.SExtended_dictionary_schema.EEntity_or_view_definition;
import jsdai.SExtended_dictionary_schema.EEntity_select_type;
import jsdai.SExtended_dictionary_schema.EEnumeration_type;
import jsdai.SExtended_dictionary_schema.EExplicit_attribute;
import jsdai.SExtended_dictionary_schema.EExpress_code;
import jsdai.SExtended_dictionary_schema.EExtended_enumeration_type;
import jsdai.SExtended_dictionary_schema.EExtended_select_type;
import jsdai.SExtended_dictionary_schema.EExtensible_enumeration_type;
import jsdai.SExtended_dictionary_schema.EExtensible_select_type;
import jsdai.SExtended_dictionary_schema.EGeneric_schema_definition;
import jsdai.SExtended_dictionary_schema.EGlobal_rule;
import jsdai.SExtended_dictionary_schema.EImplicit_declaration;
import jsdai.SExtended_dictionary_schema.EInterface_specification;
import jsdai.SExtended_dictionary_schema.EInterfaced_declaration;
import jsdai.SExtended_dictionary_schema.EInverse_attribute;
import jsdai.SExtended_dictionary_schema.EList_type;
import jsdai.SExtended_dictionary_schema.ELocal_declaration;
import jsdai.SExtended_dictionary_schema.ELogical_type;
import jsdai.SExtended_dictionary_schema.EMap_definition;
import jsdai.SExtended_dictionary_schema.ENamed_type;
import jsdai.SExtended_dictionary_schema.ENon_extensible_select_type;
import jsdai.SExtended_dictionary_schema.ENumber_type;
import jsdai.SExtended_dictionary_schema.EOneof_subtype_expression;
import jsdai.SExtended_dictionary_schema.EReal_type;
import jsdai.SExtended_dictionary_schema.EReference_from_specification;
import jsdai.SExtended_dictionary_schema.EReferenced_declaration;
import jsdai.SExtended_dictionary_schema.ESchema_definition;
import jsdai.SExtended_dictionary_schema.ESelect_type;
import jsdai.SExtended_dictionary_schema.ESimple_type;
import jsdai.SExtended_dictionary_schema.EString_type;
import jsdai.SExtended_dictionary_schema.ESub_supertype_constraint;
import jsdai.SExtended_dictionary_schema.ESubtype_expression;
import jsdai.SExtended_dictionary_schema.EUniqueness_rule;
import jsdai.SExtended_dictionary_schema.EUse_from_specification;
import jsdai.SExtended_dictionary_schema.EUsed_declaration;
import jsdai.SExtended_dictionary_schema.EVariable_size_aggregation_type;
import jsdai.express_g.SdaieditPlugin;
import jsdai.express_g.editors.SdaiEditor;
import jsdai.express_g.exp2.AbstractDraw;
import jsdai.express_g.exp2.EGToolKit;
import jsdai.express_g.exp2.Paging;
import jsdai.express_g.exp2.eg.AbstractEGBox;
import jsdai.express_g.exp2.eg.AbstractEGObject;
import jsdai.express_g.exp2.eg.AbstractEGRelation;
import jsdai.express_g.exp2.eg.Agregate;
import jsdai.express_g.exp2.eg.EGConstraint;
import jsdai.express_g.exp2.eg.EGDefined;
import jsdai.express_g.exp2.eg.EGEntity;
import jsdai.express_g.exp2.eg.EGEntityRef;
import jsdai.express_g.exp2.eg.EGEnumerated;
import jsdai.express_g.exp2.eg.EGPageFrom;
import jsdai.express_g.exp2.eg.EGPageFromRef;
import jsdai.express_g.exp2.eg.EGPageRef;
import jsdai.express_g.exp2.eg.EGPageRefLocalFrom;
import jsdai.express_g.exp2.eg.EGPageRefLocalTo;
import jsdai.express_g.exp2.eg.EGPageTo;
import jsdai.express_g.exp2.eg.EGPageToRef;
import jsdai.express_g.exp2.eg.EGPageToSimple;
import jsdai.express_g.exp2.eg.EGRelationSimple;
import jsdai.express_g.exp2.eg.EGRelationTree;
import jsdai.express_g.exp2.eg.EGSchema;
import jsdai.express_g.exp2.eg.EGSelect;
import jsdai.express_g.exp2.eg.EGSimple;
import jsdai.express_g.exp2.eg.IHidenRef;
import jsdai.express_g.exp2.eg.SDAIdicSchema;
import jsdai.express_g.exp2.eg.Wire;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.VisualPage;
import jsdai.lang.AEntity;
import jsdai.lang.ASdaiModel;
import jsdai.lang.A_string;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiRepository;

/**
 * <p>
 * Title: JSDAI Express-G
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Mantas Balnys
 * @version 1.0
 */

public abstract class OpenJSDAICommand extends AbstractCommand {
	private static final String EENTITY_KEY = "JSDAI EXPRESS-G EDITOR KEY";

	/**
	 * for cleaning tempData purposes
	 */
	private HashSet tempSet = new HashSet();

	public boolean flag_mark_constrained_if_where_clause = true;
	
	protected void setTemp(EEntity entity, Object data) {
		entity.setTemp(EENTITY_KEY, data);
		tempSet.add(entity);
	}

	protected Object getTemp(EEntity entity) {
		if (entity == null)
			return null;
		return entity.getTemp(EENTITY_KEY);
	}

	protected void cleanTemp() {
		Iterator iter = tempSet.iterator();
		while (iter.hasNext()) {
			EEntity entity = (EEntity) iter.next();
			entity.setTemp(EENTITY_KEY, null);
		}
		tempSet.clear();

		schemaRefMap1.clear();
		schemaRefMap2.clear();
	}

	private boolean showInterfaced = false;

	/*
	 * protected Point objectPlacementMatrix = new Point(); protected Point
	 * objectPlacementMatrixMax = new Point(); protected boolean
	 * objectPlacementMatrixDirection = true; protected double
	 * objectPlacementMatrixWidth = AbstractEGBox.basicWidth * 4; protected
	 * double objectPlacementMatrixHeight = AbstractEGBox.basicWidth * 2;
	 */
	public OpenJSDAICommand(CommandInvoker invoker) {
		super(invoker);

		/** @regcheck */

	}

	protected EGRelationSimple createEG(EInverse_attribute entity)
			throws SdaiException {
		Object object = getTemp(entity);
		if (showInterfaced || entity.findEntityInstanceSdaiModel() == modelDict)
		// OpenJSDAICommand added to avoid recursive creation of inverse attribute
		if (!(object instanceof AbstractEGRelation) && !(object instanceof OpenJSDAICommand)) { 
			EExplicit_attribute ent = entity.getInverted_attr(null);
			EEntity domain = null;
			if (ent.testDomain(null)) domain = ent.getDomain(null);
			while (domain instanceof EAggregation_type) {
				if (((EAggregation_type)domain).testElement_type(null))
					domain = ((EAggregation_type)domain).getElement_type(null);
				else domain = null;
			} 
//System.err.println(" creating inverse:" + entity);
				// inverse attribute has parent as child and child as parent
				// (reversed)
			setTemp(entity, this);
			if (entity.testParent(null) && ent.testDomain(null) && entity.getParent(null) == domain) {
				object = createEG(ent);
//System.err.println(" use OLD explicit:" + object);
			} else {
//				Object object = getTemp(ent);
//				if (!(object instanceof AbstractEGRelation)) {
					AbstractEGBox parent = null;
					if (entity.testDomain(null))
						parent = createEG(entity.getDomain(null));
					AbstractEGBox child = null;
					if (entity.testParent(null))
						child = createEG(entity.getParent(null));
					String name = "";
					if (ent.testName(null))
						name = ent.getName(null);
					String redeclaring = null;
					if (ent.testRedeclaring(null)) {
						redeclaring = "";
						EExplicit_attribute red_ent = ent.getRedeclaring(null);
						if ((red_ent != null) && red_ent.testName(null))
							redeclaring = red_ent.getName(null);
					}
					if ((parent != null) && (child != null)) {
						boolean fRestricted = isAttrRestricted(entity);
						object = new EGRelationSimple(prop, name, parent, child,
								EGRelationSimple.TYPE_AGREGATION, ent
										.getOptional_flag(null), false, redeclaring, fRestricted); // RR - fRestricted
						((SDAIdicSchema) object).setDefinition(entity);
						prop.handler().drawableAddR((AbstractDraw) object);
					} else {
//						System.err.println("skipping relation: " + entity);
					}
//System.err.println(" created NEW explicit:" + object);
//				}
			}
			if (object != null) {
				int minBound = Agregate.BOUND_NONE;
				int maxBound = Agregate.BOUND_NONE;
				String name = "";
				if (entity.testMin_cardinality(null))
					minBound = entity.getMin_cardinality(null).getBound_value(
							null);
				if (entity.testMax_cardinality(null))
					maxBound = entity.getMax_cardinality(null).getBound_value(
							null);
				int type = Agregate.TYPE_SIMPLE;
				if (minBound != Agregate.BOUND_NONE) {
					if (entity.getDuplicates(null))
						type = Agregate.TYPE_BAG;
					else
						type = Agregate.TYPE_SET;
				}
				if (entity.testName(null))
					name = entity.getName(null);
				if (entity.getParent(null) != ent.getParent(null)) { // when
																		// declaring
																		// new
																		// inverse
																		// type
					AbstractEGBox box = createEG(entity.getParent(null));
					if (box != null)
						((EGRelationSimple) object).setChild(box);
//					else
//						System.err.println("declaration of new inverse failed: " + entity);
				}
				((EGRelationSimple) object).setInverse(new Agregate(null, null,
						name, type, minBound, maxBound, false, false));
				if (entity.testRedeclaring(null)) {
					String redeclaring = "";
					EInverse_attribute red_ent = entity.getRedeclaring(null);
					if ((red_ent != null) && red_ent.testName(null))
						redeclaring = red_ent.getName(null);
					((EGRelationSimple)object).setRedeclaringInverse(redeclaring);
				}

			} //else
//				System.err.println("skipping inverse: " + entity);
			setTemp(entity, object);
		} 
		if (!(object instanceof EGRelationSimple)) object = null;
		return (EGRelationSimple)object;
	}

	protected Agregate createAgregate(EAggregation_type entity)
			throws SdaiException {
		EData_type data_type = null;
		if (entity.testElement_type(null)) {
			data_type = entity.getElement_type(null);
			AbstractEGBox box;
			Agregate next = null;
			int type = Agregate.TYPE_SIMPLE;
			if (data_type instanceof EAggregation_type) {
				next = createAgregate((EAggregation_type) data_type);
				box = next.getAgregation_type();
			} else {
				box = createEG(data_type);
				next = new Agregate(box);
			}
			int minBound = Agregate.BOUND_NONE;
			int maxBound = Agregate.BOUND_NONE;
			boolean unique = false;
			boolean optional = false;
			String name = "";
			if (entity.testName(null))
				name = entity.getName(null);
			if (entity instanceof EVariable_size_aggregation_type) {
				EVariable_size_aggregation_type ent = (EVariable_size_aggregation_type) entity;
				if (ent.testLower_bound(null))
					minBound = ent.getLower_bound(null).getBound_value(null);
				if (ent.testUpper_bound(null))
					maxBound = ent.getUpper_bound(null).getBound_value(null);
				if (entity instanceof EList_type) {
					EList_type list = (EList_type) entity;
					type = Agregate.TYPE_LIST;
					if (list.testUnique_flag(null))
						unique = list.getUnique_flag(null);
				} else if (entity instanceof EBag_type) {
					type = Agregate.TYPE_BAG;
				} else
					type = Agregate.TYPE_SET;
			} else {
				EArray_type ent = (EArray_type) entity;
				type = Agregate.TYPE_ARRAY;
				if (ent.testLower_index(null))
					minBound = ent.getLower_index(null).getBound_value(null);
				if (ent.testUpper_index(null))
					maxBound = ent.getUpper_index(null).getBound_value(null);
				if (ent.testUnique_flag(null))
					unique = ent.getUnique_flag(null);
				if (ent.testOptional_flag(null))
					optional = ent.getOptional_flag(null);
			}
			Agregate agregate = new Agregate(box, next, name, type, minBound,
					maxBound, optional, unique);
			return agregate;
		} else {
//			System.err.println("null agregate: " + entity);
			return null;
		}
	}

	protected EGRelationSimple createEG(EDerived_attribute entity)
			throws SdaiException {
		Object object = entity.getTemp(EENTITY_KEY);
		if (!(object instanceof EGRelationSimple) && !(object instanceof OpenJSDAICommand)) {
			setTemp(entity, this);
			AbstractEGBox parent = createEG(entity.getParent(null));
			EEntity domain = null;
			if (entity.testDomain(null))
				domain = entity.getDomain(null);
			AbstractEGBox child = null;
			Agregate agregate = null;
			if (domain instanceof EAggregation_type) {
				agregate = createAgregate((EAggregation_type) domain);
				if (agregate != null)
					child = agregate.getAgregation_type();
			} else if (domain != null)
				child = createEG(domain);
			String name = "";
			if (entity.testName(null))
				name = entity.getName(null);
			String redeclaring = null;
			if (entity.testRedeclaring(null)) {
				redeclaring = "";
				EEntity red_ent = entity.getRedeclaring(null);
				if ((red_ent instanceof EAttribute)
						&& ((EAttribute) red_ent).testName(null))
					redeclaring = ((EAttribute) red_ent).getName(null);
			}
			if ((parent != null) && (child != null)) {
				boolean fRestricted = isAttrRestricted(entity);
				object = new EGRelationSimple(prop, name, parent, child,
						EGRelationSimple.TYPE_AGREGATION, false, true,
						redeclaring, fRestricted); // RR - fRestricted
				((SDAIdicSchema) object).setDefinition(entity);
				if (agregate != null)
					((EGRelationSimple) object).setAgregate(agregate);

				prop.handler().drawableAddR((AbstractDraw) object);
				// ((EGRelationSimple)object).update(2);
			} else {
//				System.err.println("skipping derived attribute: " + entity);
				object = null;
			}
			setTemp(entity, object);
		}
		if (!(object instanceof EGRelationSimple)) object = null;
		return (EGRelationSimple) object;
	}

	// RR - checks if the attribute is constrained by UNIQUE clause and by WHERE clause
	boolean isAttrRestricted(EAttribute attr) throws SdaiException {
		// two parts - 1. UNIQUE clause, 2. - WHERE clause
		// find parent entity, find its UNIQUE clause, check if this attribute is included
		EEntity_or_view_definition parent_eov = attr.getParent(null);
		EEntity_definition parent_entity = null;
		if (parent_eov instanceof EEntity_definition) {
			parent_entity = (EEntity_definition) parent_eov;
		}
		if (parent_entity == null) return false;
		
		// check if constrained by UNIQUE clause
		AUniqueness_rule uniqueness_rules = parent_entity.getUniqueness_rules(null,null);
    SdaiIterator iter_ur = uniqueness_rules.createIterator();
   	while (iter_ur.next()) {
	    EUniqueness_rule ur_inst = (EUniqueness_rule)uniqueness_rules.getCurrentMemberObject(iter_ur);
      AAttribute attributes = ur_inst.getAttributes(null);
      SdaiIterator iter_attr = attributes.createIterator();
      while (iter_attr.next()) {
      	EAttribute attr_inst = (EAttribute)attributes.getCurrentMemberObject(iter_attr);
      	if (attr_inst == attr) return true;
      }      
		}
		
		// check if constrained by WHERE clause
	  // this is more complicated - need to find the string with the expression and search for the attribute name there, it seems	
	
	  // TODO
	 
	
	  // and finally:
	  return false;
	}

	protected EGRelationSimple createEG(EExplicit_attribute entity)
			throws SdaiException {
		Object object = getTemp(entity);
		if (object instanceof OpenJSDAICommand) {
			object = null;
		} else
		if (!(object instanceof EGRelationSimple)) {
			setTemp(entity, this);
			AbstractEGBox parent = createEG(entity.getParent(null));
			EEntity domain = null;
			if (entity.testDomain(null))
				domain = entity.getDomain(null);
			AbstractEGBox child = null;
			Agregate agregate = null;
			if (domain instanceof EAggregation_type) {
				agregate = createAgregate((EAggregation_type) domain);
				if (agregate != null)
					child = agregate.getAgregation_type();
			} else if (domain != null)
				child = createEG(domain);
			String name = "";
			if (entity.testName(null))
				name = entity.getName(null);
			String redeclaring = null;
			if (entity.testRedeclaring(null)) {
				redeclaring = "";
				EExplicit_attribute red_ent = entity.getRedeclaring(null);
				if ((red_ent != null) && red_ent.testName(null))
					redeclaring = red_ent.getName(null);
			}
			if ((parent != null) && (child != null)) {
				boolean fRestricted = isAttrRestricted(entity);
				object = new EGRelationSimple(prop, name, parent, child,
						EGRelationSimple.TYPE_AGREGATION, entity
								.getOptional_flag(null), false, redeclaring, fRestricted);  // RR - fRestricted
				((SDAIdicSchema) object).setDefinition(entity);
				if (agregate != null)
					((EGRelationSimple) object).setAgregate(agregate);

				prop.handler().drawableAddR((AbstractDraw) object);
			} else {
				object = null;
//				System.err.println("skipping relation: " + entity);
			}
			setTemp(entity, object);
		}
		return (EGRelationSimple) object;
	}

	protected EGSchema createEG(ESchema_definition definition)
			throws SdaiException {
		Object object = definition.getTemp(EENTITY_KEY);
		if (!(object instanceof EGSchema)) {
			object = new EGSchema(prop, definition.getName(null));
			((SDAIdicSchema) object).setDefinition(definition);
			setTemp(definition, object);

			prop.handler().drawableAddR((AbstractDraw) object);
		}
		return (EGSchema) object;
	}

	protected EGSchema mainSchema = null;

	private HashMap schemaRefMap1 = new HashMap();

	private HashMap schemaRefMap2 = new HashMap();

	protected EGRelationSimple createSchemaRelation(EGSchema refSchema, int type) {
		boolean relType = false;
		HashMap schemaRefMap = null;
		switch (type) {
		case (EGEntityRef.TYPE_REFERENCED):
			relType = true;
			schemaRefMap = schemaRefMap1;
			break;
		case (EGEntityRef.TYPE_IMPLICIT):
		case (EGEntityRef.TYPE_USED):
		default:
			relType = false;
			schemaRefMap = schemaRefMap2;
			break;
		}
		EGRelationSimple rel = (EGRelationSimple) schemaRefMap.get(refSchema.getDefinition());
		if (rel == null) {
			rel = new EGRelationSimple(prop, mainSchema, refSchema,
					AbstractEGRelation.TYPE_AGREGATION, relType, false, null);
			rel.setDefinition(refSchema.getDefinition());
			schemaRefMap.put(refSchema.getDefinition(), rel);
			prop.handler().drawableAddR(rel);
		}
		return rel;
	}

	private String getNameForDeclarationType(EEntity ent) throws SdaiException {
		String name = null;
		if (ent instanceof ENamed_type)
			name = ((ENamed_type) ent).getName(null);
		else if (ent instanceof EGlobal_rule)
			name = ((EGlobal_rule) ent).getName(null);
		else if (ent instanceof EAlgorithm_definition)
			name = ((EAlgorithm_definition) ent).getName(null);
		else if (ent instanceof EConstant_definition)
			name = ((EConstant_definition) ent).getName(null);
		else if (ent instanceof EMap_definition)
			name = ((EMap_definition) ent).getName(null);
		else if (ent instanceof ESub_supertype_constraint) {
			if (((ESub_supertype_constraint) ent).testName(null))
				name = ((ESub_supertype_constraint) ent).getName(null);
		}
		return name;
	}

	/**
	 * creates schema objects
	 * 
	 * @param declaration
	 * @throws SdaiException
	 */

	protected void createEG(EInterface_specification use) throws SdaiException {
		String schema_name = "unknown_schema";
		EGeneric_schema_definition schDef = use.getForeign_schema(null);
		EGSchema refSchema = createEG((ESchema_definition) schDef);
		int type = EGEntityRef.TYPE_USED;
		if (use instanceof EUse_from_specification) {
			type = EGEntityRef.TYPE_USED;
		} else if (use instanceof EReference_from_specification) {
			type = EGEntityRef.TYPE_REFERENCED;
		}
		// creates interschema relations
		EGRelationSimple rel = createSchemaRelation(refSchema, type);

		// create name on schema relation
		if (use.testItems(null)) {
			String name = null;
			AInterfaced_declaration decls = use.getItems(null);
			SdaiIterator sid = decls.createIterator();
			while (sid.next()) {
				EInterfaced_declaration decl = decls.getCurrentMember(sid);
				EEntity def = decl.getDefinition(null);
				String declname = getNameForDeclarationType(def);
				if (declname != null) {
					if (name == null)
						name = declname;
					else
						name += "\n" + declname;
					if (decl.testAlias_name(null))
						name += " > " + decl.getAlias_name(null);
				}
			}
			if (name != null)
				rel.setName(name);
		}
		// FIX tmp }
	}

	/**
	 * creates schema objects FIX not used
	 * 
	 * @param declaration
	 * @throws SdaiException
	 */

	protected void createEG1(EDeclaration declaration) throws SdaiException {
		if (declaration instanceof EInterfaced_declaration) {
			EEntity entity = declaration.getDefinition(null);
			if (entity instanceof EDefined_type) { // special for defined type
													// interfaced declaration
				if (((EDefined_type) entity).testDomain(null)) {
					EEntity entityDef = ((EDefined_type) entity)
							.getDomain(null);
					if ((entityDef instanceof ESelect_type)
							|| (entityDef instanceof EEnumeration_type))
						entity = entityDef;
				}
			}
			if (getTemp(entity) != null) {
				SdaiModel modelEnt = entity.findEntityInstanceSdaiModel();
				// FIX tmp if (modelEnt != modelDict) {
				AEntity list = modelEnt.getInstances(ESchema_definition.class);
				SdaiIterator it = list.createIterator();
				EGSchema refSchema = null;
				if (it.next()) {
					ESchema_definition schDef = (ESchema_definition) list
							.getCurrentMemberEntity(it);
					refSchema = createEG((ESchema_definition) schDef);
				}
				int type = EGEntityRef.TYPE_USED;
				if (declaration instanceof EImplicit_declaration) {
					type = EGEntityRef.TYPE_IMPLICIT;
				} else if (declaration instanceof EUsed_declaration) {
					type = EGEntityRef.TYPE_USED;
				} else if (declaration instanceof EReferenced_declaration) {
					type = EGEntityRef.TYPE_REFERENCED;
				}
				// creates interschema relations
				createSchemaRelation(refSchema, type);
			}
			// FIX tmp }
		}
	}

	/**
	 * creates interschema references
	 * 
	 * @param declaration
	 * @return
	 * @throws SdaiException
	 */
	protected AbstractEGObject createEG(EDeclaration declaration)
			throws SdaiException {
//System.err.println("dcl:" + declaration);

		Object object = declaration.getTemp(EENTITY_KEY);
		if (!(object instanceof AbstractEGObject)) {
			EEntity defined = declaration.getDefinition(null);
//System.err.println("\tdef:" + defined);
			if (declaration instanceof EInterfaced_declaration) {
				EEntity entity = defined;
				boolean isUnderDefined = false;
				if (defined instanceof EDefined_type) { // special for defined
														// type interfaced
														// declaration
					if (((EDefined_type) entity).testDomain(null)) {
						EEntity entityDef = ((EDefined_type) entity)
								.getDomain(null);
						if ((entityDef instanceof ESelect_type)
								|| (entityDef instanceof EEnumeration_type)) {
							entity = entityDef;
							isUnderDefined = true;
						}
					}
				}
				// FIX tmp if (entity.findEntityInstanceSdaiModel() !=
				// modelDict) {
				AbstractEGBox box = createEG(entity);
				// System.err.println("Declaration declaration = " + declaration
				// + ", tmp = " + box);
				if (box == null) {
//					System.err.println("EInterfaced_declaration ("
//							+ declaration + ") contains not supported entity: "
//							+ entity);
				} else {
					if (isUnderDefined) {
						box.setName(((EDefined_type) defined).getName(null));
					}
					if (showInterfaced) {
						object = box;
					} else {
						String schema_name = "unknown_schema";
						SdaiModel modelEnt = entity
								.findEntityInstanceSdaiModel();
						AEntity list = modelEnt
								.getInstances(ESchema_definition.class);
						SdaiIterator it = list.createIterator();
						// EGSchema refSchema = null;
						if (it.next()) {
							ESchema_definition schDef = (ESchema_definition) list
									.getCurrentMemberEntity(it);
							schema_name = schDef.getName(null);
							// createEG((ESchema_definition)schDef);
						}
						int type = EGEntityRef.TYPE_USED;
						String alias = null;
						if (((EInterfaced_declaration) declaration)
								.testAlias_name(null)) {
							alias = ((EInterfaced_declaration) declaration)
									.getAlias_name(null);
						}
						if (declaration instanceof EImplicit_declaration) {
							type = EGEntityRef.TYPE_IMPLICIT;
						} else if (declaration instanceof EUsed_declaration) {
							type = EGEntityRef.TYPE_USED;
						} else if (declaration instanceof EReferenced_declaration) {
							type = EGEntityRef.TYPE_REFERENCED;
						}
						// creates interschema relations
						// createSchemaRelation(refSchema, type);

						box.setDefinition(entity);
						EGEntityRef ref = new EGEntityRef(prop, box,
								schema_name, type);
						ref.setDefinition(declaration);
						if (alias != null)
							ref.setRename(alias);

						setTemp(entity, ref);

						prop.handler().drawableRemove(box);
						prop.handler().drawableAddR(ref);
						object = ref;
					}
				}
			} else if (declaration instanceof ELocal_declaration) {
				object = createEG(defined);
			}
			setTemp(declaration, object);

			// FIX tmp }
		}
//System.err.println("result:" + object);		
		return (AbstractEGObject) object;
	}

	// RR - to mark entities, restricted by global rules
	boolean isRestricted(EEntity_definition entity) throws SdaiException {
		ASdaiModel domain = entity.findEntityInstanceSdaiModel().getRepository().getSchemas().getAssociatedModels();
		AGlobal_rule global_rules = entity.getGlobal_rules(null, domain); // may need to work on domain
//System.out.println("global_rules: " + global_rules.getMemberCount());
		if (global_rules.getMemberCount() > 0) return true;
		//else return false;
		
		IPreferenceStore prefs = SdaieditPlugin.getDefault().getPreferenceStore();
		boolean use_asterisk = prefs.getBoolean(SdaiEditor.USE_ASTERISK_FOR_ENTITY);
		flag_mark_constrained_if_where_clause = !use_asterisk;
		if (flag_mark_constrained_if_where_clause) {
		
	  // do the same if local WHERE clause is present in the entity, to be switched by a flag

			AWhere_rule where_rules = entity.getWhere_rules(null, null);	// here domain perhaps not needed, as inside the same entity and so inside the same schema
    	if (where_rules.getMemberCount() > 0) return true;
		}
	  return false;
	}

	// Create Entity
	protected EGEntity createEG(EEntity_definition entity) throws SdaiException {
		Object object = getTemp(entity);
		if (!(object instanceof EGEntity)) {
			boolean complex = false;
			if (entity.testComplex(null))
				complex = entity.getComplex(null);
			if (!complex) {
				boolean independent = false;
				boolean instantiable = false;
				boolean fAbstract = false;
				boolean fRestricted = isRestricted(entity); // RR
				if (entity.testIndependent(null))
					independent = entity.getIndependent(null);
				if (entity.testInstantiable(null))
					instantiable = entity.getInstantiable(null);
				if (entity.testAbstract_entity(null))
					fAbstract = entity.getAbstract_entity(null);

				Point location = new Point(0, 0);

				object = new EGEntity(prop, entity.getName(null), location,
						complex, independent, instantiable, fAbstract, fRestricted); // RR - added fRestricted
				((SDAIdicSchema) object).setDefinition(entity);
				setTemp(entity, object);

				if (showInterfaced
						|| entity.findEntityInstanceSdaiModel() == modelDict) {
					prop.handler().drawableAddR((AbstractDraw) object);
					if (entity.testGeneric_supertypes(null)) {
						AEntity_or_view_definition supertypes = entity
								.getGeneric_supertypes(null);
						SdaiIterator iter = supertypes.createIterator();
						while (iter.next()) {
							EEntity_or_view_definition ent = supertypes.getCurrentMember(iter);
							AbstractEGBox box = createEG(ent);
							/** @todo changed parent with child */
							EGRelationSimple rel = new EGRelationSimple(prop,
									box, (EGEntity) object,
									AbstractEGRelation.TYPE_INHERITANCE, false,
									false, null);
							rel.setDefinition(entity);
							prop.handler().drawableAdd(rel);
						}
					}
				}
				if (showInterfaced
						|| entity.findEntityInstanceSdaiModel() == modelDict) {
					AAttribute alist = new AAttribute();
					CAttribute.usedinParent(null, entity,
							prop.getRepositoryHandler().getRepository()
									.getModels(), alist);
					SdaiIterator it2 = alist.createIterator();
					while (it2.next()) {
						EAttribute attribute = alist.getCurrentMember(it2);
						createEG(attribute);
					}
					// inverse
					AInverse_attribute ilist = new AInverse_attribute();
					CInverse_attribute.usedinDomain(null, entity,
							prop.getRepositoryHandler().getRepository()
									.getModels(), ilist);
					it2 = ilist.createIterator();
					while (it2.next()) {
						EInverse_attribute attribute = ilist
								.getCurrentMember(it2);
						createEG(attribute);
					}
				}
			}
		}
		return (EGEntity) object;
	}

	/**
	 * brings all covered entities
	 * 
	 * @param sube
	 * @return
	 * @throws SdaiException
	 */
	private Collection getCovered(ESubtype_expression sube)
			throws SdaiException {
		Collection covered = new LinkedHashSet();
		AEntity_or_view_or_subtype_expression subexp = sube
				.getGeneric_operands(null);
		SdaiIterator it = subexp.createIterator();
		while (it.next()) {
			EEntity child = subexp.getCurrentMember(it);
			if (child instanceof EEntity_definition) {
				Object egc = createEG(child);
				if (egc instanceof EGEntity)
					covered.add(egc);
//				else
//					System.err.println("Leaf ESub_supertype_constraint ("
//							+ sube + ") contains invalid value: " + child);
			} else if (child instanceof ESubtype_expression) {
				covered.addAll(getCovered((ESubtype_expression) child));
			} //else
//				System.err.println("Leaf ESub_supertype_constraint (" + sube
//						+ ") contains invalid value: " + child);
		}
		return covered;
	}

	private void parseANDOR(EAndor_subtype_expression subao,
			Collection list_of_trees) throws SdaiException {
		AEntity_or_view_or_subtype_expression subexp = subao
				.getGeneric_operands(null);
		SdaiIterator it = subexp.createIterator();
		while (it.next()) {
			EEntity child = subexp.getCurrentMember(it);
			if (child instanceof EAndor_subtype_expression) {
				parseANDOR((EAndor_subtype_expression) child, list_of_trees);
			} else if (child instanceof ESubtype_expression) {
				list_of_trees.add(child);
			}
		}
	}

	protected EGConstraint createEG(ESub_supertype_constraint constraint)
			throws SdaiException {
		Object object = constraint.getTemp(EENTITY_KEY);
		if (!(object instanceof EGConstraint)) {
			if (constraint.testConstraint(null)) {
				AbstractEGBox parent = createEG(constraint
						.getGeneric_supertype(null));

				ESubtype_expression sube = constraint.getConstraint(null);
				Collection list_of_trees = new LinkedHashSet();
				if (sube instanceof EAndor_subtype_expression) {
					parseANDOR((EAndor_subtype_expression) sube, list_of_trees);
					if (list_of_trees.size() == 0)
						list_of_trees.add(sube);
				} else {
					list_of_trees.add(sube);
				}

				AbstractEGRelation biggestTree = null;
				int biggestTreeSize = 0;

				Vector wires = parent.getWires();
				Iterator lti = list_of_trees.iterator();
				while (lti.hasNext()) {
					sube = (ESubtype_expression) lti.next();
					Collection covered = getCovered(sube);
					EGRelationTree tree = null;
					Iterator it = covered.iterator();
					if (covered.size() > 1)
						while (it.hasNext()) {
							AbstractEGBox child = (AbstractEGBox) it.next();
							if (child != null) {
								Iterator itw = wires.iterator();
								EGRelationSimple rel = null;
								while ((itw.hasNext()) && (rel == null)) {
									Wire wire = ((Wire) itw.next());
									if ((wire.getRelation().getChild() == child)
											&& (wire.getRelation() instanceof EGRelationSimple))
										rel = (EGRelationSimple) wire
												.getRelation();
								}
								if (rel != null) {
									if (tree == null) {
										tree = new EGRelationTree(prop, rel);
									} else {
										tree.addChild(child);
									}
									rel.eliminate();
									prop.handler().drawableRemove(rel);
								} //else
//									System.err
//											.println("ESub_supertype_constraint ("
//													+ constraint
//													+ ") contains invalid value: "
//													+ child);
							} //else
//								System.err
//										.println("ESub_supertype_constraint has null value: "
//												+ constraint);
						}
					// System.out.println(" DEBUGING: tree = " + tree + " sube =
					// " + sube);
					if (tree != null) {
						if (sube instanceof EOneof_subtype_expression)
							tree
									.setSubtype_expression(EGRelationTree.SUBTYPE_EXPRESSION_ONEOF);
						else if (sube instanceof EAnd_subtype_expression)
							tree
									.setSubtype_expression(EGRelationTree.SUBTYPE_EXPRESSION_AND);
						else
							tree
									.setSubtype_expression(EGRelationTree.SUBTYPE_EXPRESSION_ANDOR);
						prop.handler().drawableAdd(tree);
						if (covered.size() > biggestTreeSize)
							biggestTree = tree;
					}
				}
			}

			// Express II constraint
			if (constraint.testName(null)
					&& constraint.testGeneric_supertype(null)) {
/*				System.out.println("CONSTRAINT ON "
						+ constraint.findEntityInstanceSdaiModel()
						+ " "
						+ constraint.getGeneric_supertype(null)
						+ " <"
						+ constraint.getGeneric_supertype(null)
								.findEntityInstanceSdaiModel() + ">");  simplify log 2005-12-13*/
				String name = constraint.getName(null);
				AbstractEGBox referenced = createEG(constraint
						.getGeneric_supertype(null));
				if (referenced != null) {
					boolean abs = false;
					if (constraint.testAbstract_supertype(null)) {
						abs = constraint.getAbstract_supertype(null);
					}
					EGConstraint egconst = new EGConstraint(prop, name,
							referenced, abs);
					setTemp(constraint, egconst);
					egconst.setDefinition(constraint);
					object = egconst;
					prop.handler().drawableAddR(egconst);
					EGRelationSimple relsup = new EGRelationSimple(prop,
							egconst, referenced,
							AbstractEGRelation.TYPE_AGREGATION, true, false,
							null);
					prop.handler().drawableAddR(relsup);
					if (constraint.testTotal_cover(null)) {
						AEntity_definition subtypes = constraint
								.getTotal_cover(null);
//System.out.println("<<RR-A total_over: " + subtypes);
						SdaiIterator sid = subtypes.createIterator();
						AbstractEGRelation rel = null;
						if (subtypes.getMemberCount() == 1) {
							if (sid.next()) {
								EEntity_definition subtype = subtypes
										.getCurrentMember(sid);
								AbstractEGBox child = createEG(subtype);
								if (child != null)
									rel = new EGRelationSimple(prop, egconst,
											child,
											AbstractEGRelation.TYPE_AGREGATION,
											false, false, null);
							}
						} else if (subtypes.getMemberCount() > 1) {
							while (sid.next()) {
								EEntity_definition subtype = subtypes
										.getCurrentMember(sid);
								AbstractEGBox child = createEG(subtype);
								if (child != null) {
									if (rel == null)
										rel = new EGRelationTree(
												prop,
												egconst,
												child,
												AbstractEGRelation.TYPE_AGREGATION);
									else
										rel.addChild(child);
								}
							}
						}
						if (rel != null) {
							prop.handler().drawableAddR(rel);
						}
					}
				} else {
//					System.err
//							.println("referenced object missing in constraint: "
//									+ constraint);
				}
			} else {
//				System.err.println("not optional data missing in constraint: "
//						+ constraint);
			}
			// End Express II constraint
		}

		return (EGConstraint) object;
	}

	// RR
	boolean isDTRestricted(EDefined_type dt) throws SdaiException {
		AWhere_rule wr = dt.getWhere_rules(null,null);
		if (wr.getMemberCount() > 0) return true;
		else return false;
	}

	protected AbstractEGBox createEG(EDefined_type entity) throws SdaiException {
		Object object = getTemp(entity);
		if (!(object instanceof AbstractEGBox)) {
			Point location = new Point(0, 0);
			EEntity ent = null;
			if (entity.testDomain(null))
				ent = entity.getDomain(null);
			if ((ent instanceof ESelect_type)
					|| (ent instanceof EEnumeration_type)) {
				if (ent instanceof ESelect_type) {
					// EGSelect sel = createEG((ESelect_type)ent);
					// sel.setDefinition(entity);
				}
			} else {
				boolean fRestricted = isDTRestricted(entity);
				object = new EGDefined(prop, entity.getName(null), location, fRestricted); // RR - added fRestricted
				((SDAIdicSchema) object).setDefinition(entity);

				setTemp(entity, object);
				if (showInterfaced
						|| entity.findEntityInstanceSdaiModel() == modelDict)
					prop.handler().drawableAddR((AbstractDraw) object);
			}

			if (ent != null) {
				AbstractEGBox box = null;// = createEG(ent);
				Agregate agregate = null;
				if (ent instanceof EAggregation_type) {
					agregate = createAgregate((EAggregation_type) ent);
					if (agregate != null)
						box = agregate.getAgregation_type();
				} else
					box = createEG(ent);
				if (box == null) {
//					System.err.println("Agregate null in defined type: "
//							+ object);
				} else {
					if ((ent instanceof ESelect_type)
							|| (ent instanceof EEnumeration_type)) {
						object = box;
						box.setName(entity.getName(null));
						setTemp(entity, object);
					} else {
						if (showInterfaced
								|| entity.findEntityInstanceSdaiModel() == modelDict) {
							EGRelationSimple rel = new EGRelationSimple(prop,
									(EGDefined) object, box,
									AbstractEGRelation.TYPE_AGREGATION, false,
									false, null);
							rel.setDefinition(entity);
							if (agregate != null)
								rel.setAgregate(agregate);
							prop.handler().drawableAdd(rel);
						}
					}
				}
			}
		}
		return (AbstractEGBox) object;
	}

	protected EGEnumerated createEG(EEnumeration_type entity)
			throws SdaiException {
		Object object = entity.getTemp(EENTITY_KEY);
		if (!(object instanceof EGEnumerated)) {
			Point location = new Point(0, 0);
			int type = 0;
			if (entity instanceof EExtensible_enumeration_type)
				type |= EGEnumerated.TYPE_EXTENSIBLE;
			if (entity instanceof EExtended_enumeration_type)
				type |= EGEnumerated.TYPE_EXTENDED;
			object = new EGEnumerated(prop, entity.getName(null), location,
					type, entity.getLocal_elements(null));
			((SDAIdicSchema) object).setDefinition(entity);
			((EGEnumerated) object).setHidenDefinition(entity);
			setTemp(entity, object);
			if ((entity instanceof EExtended_enumeration_type)
					&& ((EExtended_enumeration_type) entity)
							.testIs_based_on(null)) {
				EExtensible_enumeration_type extensible = (EExtensible_enumeration_type) ((EExtended_enumeration_type) entity)
						.getIs_based_on(null).getDomain(null);
				EGEnumerated enumext = createEG(extensible);
				EGRelationSimple rel = new EGRelationSimple(prop, enumext,
						(EGEnumerated) object,
						AbstractEGRelation.TYPE_INHERITANCE);
				// FIX ? setDefinition(), setTemp()
				prop.handler().drawableAddR(rel);
			}

			prop.handler().drawableAddR((AbstractDraw) object);
		}
		return (EGEnumerated) object;
	}

	protected EGSelect createEG(ESelect_type entity) throws SdaiException {
		Object object = getTemp(entity);
		if (!(object instanceof EGSelect)) {
			Point location = new Point(0, 0);
			int type = 0;
			if (entity instanceof EExtensible_select_type)
				type |= EGSelect.TYPE_EXTENSIBLE;
			if (entity instanceof EExtended_select_type)
				type |= EGSelect.TYPE_EXTENDED;
			if (entity instanceof EEntity_select_type)
				type |= EGSelect.TYPE_ENTITY;
			if (entity instanceof ENon_extensible_select_type)
				type |= EGSelect.TYPE_NON_EXTENSIBLE;
			object = new EGSelect(prop, entity.getName(null), location, type);
			((SDAIdicSchema) object).setDefinition(entity);
			((EGSelect) object).setHidenDefinition(entity);
			setTemp(entity, object);
			if (showInterfaced
					|| entity.findEntityInstanceSdaiModel() == modelDict) {
				if ((entity instanceof EExtended_select_type)
						&& ((EExtended_select_type) entity)
								.testIs_based_on(null)) {
					EExtensible_select_type extensible = (EExtensible_select_type) ((EExtended_select_type) entity)
							.getIs_based_on(null).getDomain(null);
					EGSelect selext = createEG(extensible);
					EGRelationSimple rel = new EGRelationSimple(prop, selext,
							(EGSelect) object,
							AbstractEGRelation.TYPE_INHERITANCE);
					// FIX ? setDefinition(), setTemp()
					prop.handler().drawableAddR(rel);
				}

				prop.handler().drawableAddR((AbstractDraw) object);
				if (entity.testLocal_selections(null)) {
					AbstractEGRelation rel = null;
					ANamed_type named = entity.getLocal_selections(null);
					SdaiIterator iter = named.createIterator();
					if (named.getMemberCount() > 0) {
						iter.next();
						ENamed_type ent = named.getCurrentMember(iter);
						AbstractEGBox box = createEG(ent);
						if (box != null) {
							if (named.getMemberCount() > 1) {
								rel = new EGRelationTree(prop,
										(EGSelect) object, box,
										AbstractEGRelation.TYPE_AGREGATION);
								while (iter.next()) {
									ent = named.getCurrentMember(iter);
									box = createEG(ent);
									if (box != null)
										rel.addChild(box);
								}
							} else {
								rel = new EGRelationSimple(prop,
										(EGSelect) object, box,
										AbstractEGRelation.TYPE_AGREGATION,
										false, false, null);
							}
							rel.setDefinition(entity);
							prop.handler().drawableAdd(rel);
						} //else
//							System.err.println("skipping relation in: "
//									+ entity);
					}
				}
			}
		}
		return (EGSelect) object;
	}

	protected EGSimple createEG(ESimple_type entity) throws SdaiException {
		Object object = entity.getTemp(EENTITY_KEY);
		if (!(object instanceof EGSimple)) {
			Point location = new Point(0, 0);
			int type = EGSimple.TYPE_INTEGER;
			int type_width = 8;
			boolean fixed = false;
			if (entity instanceof EBinary_type) {
				type = EGSimple.TYPE_BINARY;
				if (((EBinary_type) entity).testWidth(null))
					type_width = ((EBinary_type) entity).getWidth(null)
							.getBound_value(null);
				if (((EBinary_type) entity).testFixed_width(null))
					fixed = ((EBinary_type) entity).getFixed_width(null);
			} else if (entity instanceof EBoolean_type) {
				type = EGSimple.TYPE_BOOLEAN;
			} else if (entity instanceof ELogical_type) {
				type = EGSimple.TYPE_LOGICAL;
			} else if (entity instanceof ENumber_type) {
				type = EGSimple.TYPE_NUMBER;
			} else if (entity instanceof EReal_type) {
				type = EGSimple.TYPE_REAL;
				if (((EReal_type) entity).testPrecision(null))
					type_width = ((EReal_type) entity).getPrecision(null)
							.getBound_value(null);
			} else if (entity instanceof EString_type) {
				type = EGSimple.TYPE_STRING;
				if (((EString_type) entity).testWidth(null))
					type_width = ((EString_type) entity).getWidth(null)
							.getBound_value(null);
				if (((EString_type) entity).testFixed_width(null))
					fixed = ((EString_type) entity).getFixed_width(null);
			}

			object = new EGSimple(prop, location, type, type_width, fixed);
			((SDAIdicSchema) object).setDefinition(entity);
			setTemp(entity, object);

			prop.handler().drawableAddR((AbstractDraw) object);
		}
		return (EGSimple) object;
	}

	protected AbstractEGBox createEG(EEntity entity) throws SdaiException {
		Object object = entity.getTemp(EENTITY_KEY);
		if (!(object instanceof AbstractEGBox)) {
			// System.err.println(entity);
			if (entity instanceof ESimple_type)
				object = createEG((ESimple_type) entity);
			else if (entity instanceof EEnumeration_type)
				object = createEG((EEnumeration_type) entity);
			else if (entity instanceof EEntity_definition)
				object = createEG((EEntity_definition) entity);
			else if (entity instanceof EDefined_type)
				object = createEG((EDefined_type) entity);
			else if (entity instanceof ESelect_type)
				object = createEG((ESelect_type) entity);
			else if (entity instanceof ESchema_definition)
				object = createEG((ESchema_definition) entity);
			else if (entity instanceof EDeclaration)
				object = createEG((EDeclaration) entity);
		}
		return (AbstractEGBox) object;
	}

	protected AbstractEGRelation createEG(EAttribute entity)
			throws SdaiException {
		Object object = entity.getTemp(EENTITY_KEY);
		if (!(object instanceof AbstractEGRelation)) {
			// System.err.println(entity);
			if (entity instanceof EDerived_attribute)
				object = createEG((EDerived_attribute) entity);
			else if (entity instanceof EExplicit_attribute)
				object = createEG((EExplicit_attribute) entity);
			else if (entity instanceof EInverse_attribute)
				object = createEG((EInverse_attribute) entity);
		}
		return (AbstractEGRelation) object;
	}

	protected void setEG(EData_type_placement place) throws SdaiException {
		EEntity data_type = null;
		try {
			if (place.testRepresented_object(null))
				data_type = place.getRepresented_object(null);
		} catch (Exception sex) {
//			System.err.println(sex.getMessage());
			data_type = null;
		}
		Object object = getTemp(data_type);
		if (object != null) {
			AbstractEGBox box = (AbstractEGBox) object;
			setEG(box, place);
			setTemp(place, box);
			/**
			 * @todo if (box instanceof EGSimple) box = new
			 *       EGSimple((EGSimple)box);
			 */
		} //else
//			System.err
//					.println("OpenJSDAICommand.setEG(EData_type_placement: object not found)");
	}

	/*
	 * protected void setEG(EEntity_reference place) throws SdaiException {
	 * EEntity_definition data_type = place.getRepresented_object(null); Object
	 * object = data_type.getTemp(EENTITY_KEY); if (object != null) {
	 * AbstractEGBox box = (AbstractEGBox)object; Rectangle bounds = new
	 * Rectangle( place.getObject_location(null).getX(null),
	 * place.getObject_location(null).getY(null),
	 * place.getObject_size(null).getWidth(null),
	 * place.getObject_size(null).getHeight(null)); if ((bounds.width ==
	 * 0)&&(bounds.height == 0)) { box.setLabelNew(true); } else
	 * box.setLabelNew(false); box.setBounds(bounds);
	 * box.setPage(place.getPresented_on(null).getPage_number(null)); if
	 * (place.testRepresentation_color(null)) { EColor ecolor =
	 * place.getRepresentation_color(null); Color color = new
	 * Color(Display.getDefault(), ecolor.getRed(null), ecolor.getGreen(null),
	 * ecolor.getBlue(null)); box.simpleSchema.background = color;
	 * box.selectedSchema.background = EGToolKit.darker(color); }
	 * 
	 * EGEntityRef ref; if (box instanceof EGEntityRef) { ref =
	 * (EGEntityRef)box; } else { ref = new EGEntityRef((EGEntity)box);
	 * prop.handler().drawableRemove(box); prop.handler().drawableAddR(ref); } //
	 * setTemp(data_type, ref); ref.setSchemaName(place.getSchema_name(null));
	 * if (place.testVisible(null)) ref.setVisible(place.getVisible(null)); if
	 * (place.testRedeclared_name(null))
	 * ref.setRename(place.getRedeclared_name(null)); if
	 * (place.getReferenced(null)) ref.setType(EGEntityRef.TYPE_REFERENCED);
	 * else ref.setType(EGEntityRef.TYPE_USED); } else
	 * System.err.println("OpenJSDAICommand.setEG(EEntity_reference: object not
	 * found)"); }
	 */
	protected void setEG(EAttribute_placement place) throws SdaiException {
		EAttribute attr = null;
		try {
			if (place.testRepresented_object(null)) {
				attr = place.getRepresented_object(null);
			}
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			attr = null;
		}
		Object object = getTemp(attr);
		if (object != null) {
			EGRelationSimple rel = (EGRelationSimple) object;
			setEG(rel, place);
			setTemp(place, rel);
		} //else
//			System.err
//					.println("OpenJSDAICommand.setEG(EAttribute_placement: object not found)");
	}

	protected void setEG(ESchema_relation_placement place) throws SdaiException {
		// ESchema_definition parentSchema = place.getParent(null);
		ESchema_definition childSchema = null;
		ESchema_definition parentSchema = null;
		try {
			if (place.testChild(null))
				childSchema = place.getChild(null);
			if (place.testParent(null))
				parentSchema = place.getParent(null);
		} catch (SdaiException sex) {
			System.err.println(sex.getMessage());
			childSchema = null;
			parentSchema = null;
		}

		Object parentObj = getTemp(parentSchema);
		Object childObj = getTemp(childSchema);
		if ((parentObj instanceof EGSchema) && (childObj instanceof EGSchema)) {
			EGSchema parentBox = (EGSchema) parentObj;
			EGSchema childBox = (EGSchema) childObj;
			boolean type = place.testRelation_type(null)
					&& (place.getRelation_type(null) != 0);

			Iterator wit = parentBox.getWires().iterator();
			EGRelationSimple rel = null;
			while ((rel == null) && (wit.hasNext())) {
				Wire wire = (Wire) wit.next();
				if (wire.isAttribute()) {
					AbstractEGRelation arel = wire.getRelation();
					if (arel instanceof EGRelationSimple) {
						if ((((EGRelationSimple) arel).isOptional() == type)
								&& (arel.getChild() == childBox))
							rel = (EGRelationSimple) arel;
					}
				}
			}
			if (rel != null) {
				setEG(rel, place);
				setTemp(place, rel);
			} //else
//				System.err
//						.println("OpenJSDAICommand.setEG(ESchema_relation_placement: object not found)");
		} //else
//			System.err
//					.println("OpenJSDAICommand.setEG(ESchema_relation_placement: not optional attributes missing) "
//							+ place);
		/*
		 * HashMap schemaRefMap = null; if (place.getRelation_type(null) == 0)
		 * schemaRefMap = schemaRefMap2; else schemaRefMap = schemaRefMap1;
		 * Object childObject = schemaRefMap.get(childSchema); if (childObject !=
		 * null) { EGRelationSimple rel = (EGRelationSimple)childObject;
		 * setEG(rel, place); setTemp(place, rel); } else
		 * System.err.println("OpenJSDAICommand.setEG(ESchema_relation_placement:
		 * object not found)");
		 */
	}

	protected void setEG(EDefined_relation_placement place)
			throws SdaiException {
		EDefined_type defined = null;
		try {
			if (place.testParent(null))
				defined = place.getParent(null);
		} catch (Exception sex) {
			System.err.println(sex.getMessage());
			defined = null;
		}
		Object object = getTemp(defined);
		if (object instanceof EGDefined) {
			EGDefined edef = (EGDefined) object;
			Vector wires = edef.getWires();
			Wire wire = null;
			Iterator iter = wires.iterator();
			while ((iter.hasNext()) && (wire == null)) {
				wire = (Wire) iter.next();
				if (!wire.isAttribute())
					wire = null;
			}
			if (wire != null) {
				setEG(wire.getRelation(), place);
				setTemp(place, wire.getRelation());
			} //else
//				System.err
//						.println("OpenJSDAICommand.setEG(EDefined_relation_placement: relation not found)");
		} //else
//			System.err
//					.println("OpenJSDAICommand.setEG(EDefined_relation_placement: object type error = "
//							+ object + ")");
	}

	protected void setEG(ESelect_relation_placement place) throws SdaiException {
		ESelect_type select = null;
		try {
			if (place.testParent(null))
				select = place.getParent(null);
		} catch (Exception sex) {
			System.err.println(sex.getMessage());
			select = null;
		}
		Object object = getTemp(select);
		if (object instanceof EGSelect) {
			EGSelect esel = (EGSelect) object;
			Vector wires = esel.getWires();
			Wire wire = null;
			Iterator iter = wires.iterator();
			while ((iter.hasNext()) && (wire == null)) {
				wire = (Wire) iter.next();
				if (!wire.isAttribute())
					wire = null;
			}
			if (wire != null) {
				setEG(wire.getRelation(), place);
				setTemp(place, wire.getRelation());
			} //else
//				System.err
//						.println("OpenJSDAICommand.setEG(ESelect_relation_placement: relation not found)");
		} //else
//			System.err
//					.println("OpenJSDAICommand.setEG(ESelect_relation_placement: object not found)");
	}

	protected void setEG(ESupertype_placement place) throws SdaiException {
		EData_type parent = null;
		EData_type child = null;
		try {
			if (place.testParent(null))
				parent = place.getParent(null);
			if (place.testChild(null))
				child = place.getChild(null);
		} catch (SdaiException sex) {
			System.err.println(sex.getMessage());
			parent = null;
			child = null;
		}
		Object object = getTemp(parent);
		Object object2 = getTemp(child);
		if ((object instanceof AbstractEGBox)
				&& (object2 instanceof AbstractEGBox)) {
			AbstractEGBox ep = (AbstractEGBox) object;
			AbstractEGBox ec = (AbstractEGBox) object2;
			Vector wires = ep.getWires();
			Wire wire = null;
			Iterator iter = wires.iterator();
			while ((iter.hasNext()) && (wire == null)) {
				wire = (Wire) iter.next();
				if ((!wire.isAttribute())
						|| (wire.getRelation().getType() != AbstractEGRelation.TYPE_INHERITANCE)
						|| (!wire.getRelation().getChilds().contains(ec)))
					wire = null;
			}
			if (wire != null) {
				setEG(wire.getRelation(), place);
				setTemp(place, wire.getRelation());
			} //else
//				System.err
//						.println("OpenJSDAICommand.setEG(ESupertype_placement: relation not found)");
		} //else
//			System.err
//					.println("OpenJSDAICommand.setEG(ESupertype_placement: objects not found)");
	}

	protected void setEG(EConstraint_relation_placement place)
			throws SdaiException {
		ESub_supertype_constraint parent = null;
		try {
			if (place.testParent(null))
				parent = place.getParent(null);
		} catch (Exception sex) {
			System.err.println(sex.getMessage());
			parent = null;
		}
		EData_type child = null;
		try {
			if (place.testChild(null))
				child = place.getChild(null);
		} catch (Exception sex) {
			System.err.println(sex.getMessage());
			child = null;
		}
		Object object = getTemp(parent);
		Object object2 = getTemp(child);
		if ((object instanceof EGConstraint)
				&& (object2 instanceof AbstractEGBox)) {
			EGConstraint ep = (EGConstraint) object;
			AbstractEGBox ec = (AbstractEGBox) object2;
			Vector wires = ep.getWires();
			Wire wire = null;
			Iterator iter = wires.iterator();
			while ((iter.hasNext()) && (wire == null)) {
				wire = (Wire) iter.next();
				if ((!wire.isAttribute())
						|| (!wire.getRelation().getChilds().contains(ec)))
					wire = null;
			}
			if (wire != null) {
				setEG(wire.getRelation(), place);
				setTemp(place, wire.getRelation());
			} //else
//				System.err
//						.println("OpenJSDAICommand.setEG(EConstraint_relation_placement: relation not found)");
		} //else
//			System.err
//					.println("OpenJSDAICommand.setEG(EConstraint_relation_placement: objects not found)");
	}

	private Properties specificPageMask = new Properties();

	protected void setEG(EPage page) throws SdaiException {
		if (page.testPage_number(null)) {
			int pgNr = page.getPage_number(null);
			if (pgNr >= 0) {
				VisualPage vp = new VisualPage(prop, pgNr);
				if (page.testComment(null))
					vp.setName(page.getComment(null));
				if (page.testPage_size(null)) {
					ESize size = page.getPage_size(null);
					vp.setSize(new Point(size.getWidth(null), size
							.getHeight(null)));
				}
				String printMask = specificPageMask
						.getProperty(PropertySharing.PROP_SPECIFIC_PAGE_MASK
								+ pgNr);
				if (printMask != null)
					vp.setPrintMask(printMask);
				setTemp(page, vp);
				VisualPage old = prop.handler().setVisualPage(vp);
				if (old != null)
					old.eliminate();
			} //else
//				System.err
//						.println("OpenJSDAICommand.setEG(EPage): page number invalid:"
//								+ page);
		} //else
//			System.err.println("OpenJSDAICommand.setEG(EPage): no page number:"
//					+ page);
	}

	protected EGRelationSimple setEG(EPage_relation relation)
			throws SdaiException {
		Object object = getTemp(relation);
		if (!(object instanceof EGRelationSimple)) {
			EObject_placement pent = relation.getParent(null);
			EEntity cent = relation.getChild(null);
			int relation_type = EGRelationSimple.TYPE_AGREGATION;
/*			
			if (cent instanceof EEntity_definition) {
				EEntity_definition edef = (EEntity_definition)cent;
				if (edef.testGeneric_supertypes(null)) {
					AEntity_or_view_definition supertypes = edef.getGeneric_supertypes(null);
					SdaiIterator sit = supertypes.createIterator();
					boolean none = true;
					while (none && sit.next()) {
						if (pent == supertypes.getCurrentMember(sit)) none = false;
					}
					if (!none) relation_type = EGRelationSimple.TYPE_INHERITANCE;
				}
			}*/
			AbstractEGBox parent = null;
			if (pent instanceof EPage_reference_from) {
				parent = setEG((EPage_reference_from)pent);
			} else if (pent instanceof EPage_reference_bundle) {
				parent = setEG((EPage_reference_bundle)pent);
			}
			if (parent != null) {
				AbstractEGBox child = createEG(cent);
				EGRelationSimple rel = new EGRelationSimple(prop, "", parent,
						child, relation_type, false, false,
						null);
				object = rel;
				setTemp(relation, object);
				setEG(rel, relation);

				prop.handler().drawableAddR((AbstractDraw) object);
			} else {
//				System.err.println("setEG(EPage_relation): parent (" + pent
//						+ ") not found in " + relation);
			}
		}
		return (EGRelationSimple) object;
	}

	/**
	 * @deprecated
	 * @param entity
	 * @return
	 * @throws SdaiException
	 */
	protected EGPageTo setEG(EPage_reference_to entity) throws SdaiException {
//		System.err.println("found data from older version:" + entity);
		Object object = getTemp(entity);
		if (!(object instanceof EGPageTo)) {
			EEntity refObject = entity.getReference_to(null).getChild(null);
			if (entity.getReference_to(null).testExtended_relation(null)) {
				AbstractEGBox parent = createEG(entity.getParent(null));
				AbstractEGBox child = createEG(refObject);
				Iterator itw = parent.getWires().iterator();
				AbstractEGRelation rel = null;
				while ((itw.hasNext()) && (rel == null)) {
					Wire wire = (Wire) itw.next();
					if (wire.isAttribute()) {
						Iterator itc = wire.getRelation().getChilds()
								.iterator();
						while ((itc.hasNext()) && (rel == null)) {
							AbstractEGBox childT = (AbstractEGBox) itc.next();
							if (child == childT)
								rel = wire.getRelation();
						}
					}
				}

				if (rel != null) {
					EGPageFrom pageF = setEG(entity.getReference_to(null));
					EGPageTo pageT = EGToolKit.newPageTo(prop, pageF,
							refObject, modelDict);
					setTemp(entity, pageT);
					setEG(pageT, entity);

					rel.removeChild(child);
					rel.addChild(pageT);
					setEG(
							entity.getReference_to(null).getExtended_relation(
									null)).setType(rel.getType());

					object = pageT;
					prop.handler().drawableAddR((AbstractDraw) object);
					// pageT.update(1);
					// child.update(1);
				} else {
//					System.err
//							.println("Page reference to: relation not found: "
//									+ parent + " -o " + child + " - skipping");
				}
			} else {
//				System.err.println("Page reference to: relation not found: "
//						+ entity + " - skipping");
			}
		}
		return (EGPageTo) object;
	}

	/**
	 * @deprecated
	 * @param entity
	 * @return
	 * @throws SdaiException
	 */
	protected EGPageFrom setEG(EPage_reference_from entity)
			throws SdaiException {
//		System.err.println("found data from older version:" + entity);
		Object object = getTemp(entity);
		if (!(object instanceof EGPageFrom)) {
			APage_reference_to pageT = entity.getReference_from(null);
			if (pageT.getMemberCount() > 0) {
				AbstractEGBox child = (AbstractEGBox) entity.getChild(null)
						.getTemp(EENTITY_KEY);
				if (child != null) {
					EGPageFrom pageF = EGToolKit.newPageFrom(prop, child,
							pageT, modelDict);
					setTemp(entity, pageF);
					setEG(pageF, entity);

					if (entity.testExtended_relation(null)) {
						setEG(entity.getExtended_relation(null));
					} else { // creating relation if it wasn't defined in
								// schema
						EGRelationSimple rel = new EGRelationSimple(prop, "",
								pageF, child, EGRelationSimple.TYPE_AGREGATION,
								false, false, null);
						rel.setPage(pageF.getPage());
						prop.handler().drawableAddR(rel);
//						System.err
//								.println("Page reference from: relation failed. Restored from child "
//										+ rel);
					}

					SdaiIterator iter = pageT.createIterator();
					while (iter.next())
						setEG(pageT.getCurrentMember(iter));

					object = pageF;
					prop.handler().drawableAddR((AbstractDraw) object);
					// pageF.update(0);
				} else {
					object = null;
//					System.err
//							.println("Page reference from error: no refering object found ("
//									+ entity + ") - Deleted");
				}
			} else {
				object = null;
//				System.err
//						.println("Page reference from error: no refering links found. Deleted");
			}
		}
		return (EGPageFrom) object;
	}

	protected EGPageRef setEG(EPage_reference_bundle entity)
			throws SdaiException {
		Object object = getTemp(entity);
		if (!(object instanceof EGPageRef)) {
			EEntity referenced = null;
			try {
				if (entity.testReferenced(null))
					referenced = entity.getReferenced(null);
			} catch (Exception sex) {
				System.err.println(sex.getMessage());
				referenced = null;
			}
			Object refObj = getTemp(referenced);
			// System.err.println("referenced = " + referenced + ", tmp = " +
			// refObj);
			if (refObj instanceof AbstractEGBox) {
				EGPageRef ref = EGToolKit.PageRef.getRefForReferenced(prop,
						(AbstractEGBox) refObj, entity.testLink(null));
				setEG(ref, entity);
				setTemp(entity, ref);
				Vector relsEG = new Vector(1);
				ARelation_placement rels = entity.getRelation(null);
				try {
					SdaiIterator sit = rels.createIterator();
					while (sit.next()) {
						ERelation_placement rel = rels.getCurrentMember(sit);
						Object relObj;
						if (rel instanceof EPage_relation)
							relObj = setEG((EPage_relation) rel);
						else
							relObj = getTemp(rel);
						if (relObj instanceof AbstractEGRelation) {
							relsEG.add(relObj);
						} else {
//							System.err
//									.println("Page reference error: no refering relation found ("
//											+ entity
//											+ ", "
//											+ rel
//											+ ") - Skipped");
						}
					}
				} catch (SdaiException sex) {
					System.err.println(sex.toString());
				}
				if (relsEG.size() > 0) {
					object = ref;
					Iterator iter = relsEG.iterator();
					int type = EGRelationSimple.TYPE_AGREGATION;
					while (iter.hasNext()) {
						AbstractEGRelation rel = (AbstractEGRelation) iter
								.next();
						if (!ref.addReferencedRelation(rel)) {
//							System.err.println("Page reference (" + ref + " <"
//									+ ref.getClass()
//									+ ">) error: bad refering relation (" + rel
//									+ ") - Skipped");
						} else {
							type = rel.getType();
						}
					}
					if ((entity.testLink(null))	&& (ref instanceof EGPageRefLocalTo)) {
						EGPageRef refL = setEG(entity.getLink(null));
						if (refL instanceof EGPageRefLocalFrom) {
							((EGPageRefLocalFrom)refL).addLinkedRef((EGPageRefLocalTo)ref);

							Iterator wit = refL.getWires().iterator();
							while (wit.hasNext()) {
								((Wire)wit.next()).getRelation().setType(type);
							}
						}
					}
					prop.handler().drawableAddR((AbstractDraw) object);
				} else {
					object = null;
//					System.err
//							.println("Page reference error: no relations to reference ("
//									+ entity + ") - Deleted");
				}
			} else {
				object = null;
//				System.err
//						.println("Page reference error: no refering object found ("
//								+ entity + ") - Deleted");
			}
		}
		return (EGPageRef) object;
	}

	protected void setEG(EGraphics_diagram schema) throws SdaiException {
		prop.setNameEG(schema.getComment(null));
	}

	protected void setEG(ERelation_bundle bundle) throws SdaiException {
		if (bundle.testMember(null)) {
			ARelation_placement rels = bundle.getMember(null);
			try {
				SdaiIterator sit = rels.createIterator();
				AbstractEGRelation first = null;
				while (sit.next()) {
					ERelation_placement rel = rels.getCurrentMember(sit);
					Object egobj = getTemp(rel);
					if (egobj instanceof AbstractEGRelation) {
						if (first == null)
							first = (AbstractEGRelation) egobj;
						else
							((AbstractEGRelation) egobj).setGroup(first
									.getGroup());
					}
				}
				if (first != null) {
					first.getGroup().setDefinitionPlacement(bundle);
					setTemp(bundle, first.getGroup());
				}
			} catch (SdaiException sex) {
				SdaieditPlugin.log(sex);
			}
		}
	}

	/*
	 * protected void emptyEntityTemp(SdaiModel model) throws SdaiException {
	 * AEntity list; SdaiIterator it;
	 * 
	 * list = model.getInstances(); it = list.createIterator(); while
	 * (it.next()) list.getCurrentMemberEntity(it).setTemp(EENTITY_KEY, null); }
	 */
	private void setEG(AbstractEGObject object, EObject_placement place)
			throws SdaiException {
		Rectangle bounds = new Rectangle(0, 0, 0, 0);
		if (place.testObject_location(null)) {
			bounds.x = place.getObject_location(null).getX(null);
			bounds.y = place.getObject_location(null).getY(null);
		}
		if (place.testObject_size(null)) {
			bounds.width = place.getObject_size(null).getWidth(null);
			bounds.height = place.getObject_size(null).getHeight(null);
		}
		if ((bounds.width == 0) && (bounds.height == 0)) {
			object.setLabelNew(true);
		} else {
			object.setLabelNew(false);
		}
		if (object instanceof VisualPage)
			((VisualPage) object).setMargins(bounds);
		else
			object.setBounds(bounds);
		if (place.testPresented_on(null)) {
			object.setPage(place.getPresented_on(null).getPage_number(null));
		}
		/**
		 * from 2005-04-27 attribute presented_on is optional else { if
		 * (!(object instanceof VisualPage))
		 * System.err.println("setEG(EObject_placement):non OPTIONAL attribute
		 * missing (presented_on) using default(0):" + place); }
		 */
		/*
		 * FIX if (place.testRepresentation_color(null)) { EColor ecolor =
		 * place.getRepresentation_color(null); Color color = new
		 * Color(Display.getDefault(), ecolor.getRed(null),
		 * ecolor.getGreen(null), ecolor.getBlue(null));
		 * object.simpleSchema.background = color;
		 * object.selectedSchema.background = EGToolKit.darker(color); }
		 */
		
		object.setVisible(place.testVisible(null) ? 
				(place.getVisible(null) ? 
						AbstractEGObject.VISIBLE_TRUE : AbstractEGObject.VISIBLE_FALSE)
				: AbstractEGObject.VISIBLE_UNSET);
		
		object.setDefinitionPlacement(place);
//if (!place.testVisible(null)) System.err.println("UNSET VISIBILITY: " + place);
	}

	private void setEG(AbstractEGRelation relation, ERelation_placement place)
			throws SdaiException {
		if (place.testHint1(null)) {
			Point p = new Point(place.getHint1(null).getX(null), place
					.getHint1(null).getY(null));
			relation.setHint1(p);
		}
		if (place.testHint2(null)) {
			Point p = new Point(place.getHint2(null).getX(null), place
					.getHint2(null).getY(null));
			relation.setHint2(p);
		}

		setEG((AbstractEGObject) relation, (EObject_placement) place);

		relation.simpleSchemaOpt.background = relation.simpleSchema2.background = relation.simpleSchema2.background;
		relation.selectedSchemaOpt.background = relation.selectedSchema2.background = relation.selectedSchema.background;
	}
	
	protected void expressEG(EAnnotation annotation) throws SdaiException {
		if (annotation.testTarget(null)) {
			EEntity target = annotation.getTarget(null);
			
			if (annotation.testValues(null)) {
				A_string astring = annotation.getValues(null);
				int total = astring.getMemberCount();
				if (total > 0) {
					Object object = getTemp(target);
					// special redirections:
					// redirecting Derived attribute value to simple type
/*					if (target instanceof EDerived_attribute && object instanceof EGRelationSimple) {
System.err.println("SET CONCRETE:" + object + " --> " + ((EGRelationSimple)object).getChild());						
						object = ((EGRelationSimple)object).getChild();
						if (object instanceof EGPageRef) {
							object = ((EGPageRef)object).getReferenced();
						}
					}*/

					if (object instanceof AbstractEGObject) {
//System.err.println("EXPRESSION (" + total + ") OF " + object);
//System.err.println(target);
						String[] values = new String[total];
						SdaiIterator iter = astring.createIterator();
						for (int i = 0; iter.next() && i < total; i++) {
							values[i] = astring.getCurrentMember(iter);
//System.err.println(values[i]);
						}
						((AbstractEGObject)object).setConcreteValues(values);
//System.err.println();						
					}
					// etc.
				}
				// no values?
			}
			// values not set?
		}
	}

	protected void setEG(EProperty property) throws SdaiException {
		// System.out.println(property);
		if (property.testName(null)) {
			String name = property.getName(null);
			if (name.equalsIgnoreCase(PropertySharing.PROP_EDITING_MODE)) {
				try {
					int mode = Integer.parseInt(property.getData(null));
					prop.setEditMode(mode);
				} catch (NumberFormatException e) {
					prop.status().log(
							"editing mode property corrupted: "
									+ property.getData(null) + " - using default");
				}
			} else if (name.equalsIgnoreCase(PropertySharing.PROP_FONT1)) {
				try {
					FontData fd = new FontData(property.getData(null));
					prop.setFont1(new Font(Display.getDefault(), fd));
				} catch (IllegalArgumentException e) {
					prop.status().log(
							"setProperty error: Font (Entity) not found:"
									+ property.getData(null) + " - using default");
				}
			} else if (name.equalsIgnoreCase(PropertySharing.PROP_FONT2)) {
				try {
					FontData fd = new FontData(property.getData(null));
					prop.setFont2(new Font(Display.getDefault(), fd));
				} catch (IllegalArgumentException e) {
					prop.status().log(
							"setProperty error: Font (Attribute) not found:"
									+ property.getData(null) + " - using default");
				}
			} else if (name.equalsIgnoreCase(PropertySharing.PROP_PAGE_RENUMBER)) {
				try {
					int pg_ren = Integer.parseInt(property.getData(null));
					prop.setPageRenumber(pg_ren);
				} catch (NumberFormatException e) {
					prop.status().log(
							"page renumbering property corrupted: "
									+ property.getData(null) + " - using default");
				}
			} else if (name
					.equalsIgnoreCase(PropertySharing.PROP_PAGES_OF_SAME_SIZE)) {
				prop.setPagesSameSize(true);
			} else if (name.startsWith(PropertySharing.PROP_SPECIFIC_PAGE_MASK)) {
				specificPageMask.put(name, property.getData(null));
			} else
				prop.properties().put(name, property.getData(null));
		} else {
			String message = "EProperty " + prop + " not optional attribute missing";
			SdaieditPlugin.log(message, IStatus.ERROR);
		}
	}

	private SdaiModel modelDict;

	protected void createEGPopulation(SdaiModel modelDict, SdaiModel modelEG, SdaiModel modelExp)
			throws SdaiException {
		this.modelDict = modelDict;
		AEntity list;
		SdaiIterator it;

		// emptyEntityTemp(modelDict);
		// emptyEntityTemp(modelEG);

		if (modelEG != null) {
			fix_Page_numbers(modelEG); // XXX

			list = modelEG.getInstances(EProperty.class);
			it = list.createIterator();
			while (it.next())
				setEG((EProperty) list.getCurrentMemberEntity(it));
		}
		showInterfaced = (prop.getEditMode() & PropertySharing.MODE_LONGFORM_MASK) != 0;

		list = modelDict.getInstances(ESchema_definition.class);
		it = list.createIterator();
		if (it.next()) {
			mainSchema = createEG((ESchema_definition) list
					.getCurrentMemberEntity(it));
		}

		// COMMENT 1
		list = modelDict.getInstances(EDeclaration.class);
		it = list.createIterator();
		while (it.next())
			createEG((EDeclaration) list.getCurrentMemberEntity(it));

		 list = modelDict.getInstances(EInterface_specification.class); 
		 it = list.createIterator(); 
		 while (it.next()) createEG((EInterface_specification)list.getCurrentMemberEntity(it));
		 list = modelDict.getInstances(ESub_supertype_constraint.class); 
		 it = list.createIterator(); 
		 while (it.next()) createEG((ESub_supertype_constraint)list.getCurrentMemberEntity(it));
		
			
			/**
			 * parsing data from _EXPRESS_... model
			 */
			if (modelExp != null) {
				 list = modelExp.getInstances(EExpress_code.class); 
				 it = list.createIterator(); 
				 while (it.next()) expressEG((EExpress_code)list.getCurrentMemberEntity(it));
			}

			
		/*
		 * / // COMMENT 1 closed on 2005-08-26, calling only EDeclaration / list =
		 * modelDict.getInstances(ESimple_type.class); it =
		 * list.createIterator(); while (it.next())
		 * createEG((ESimple_type)list.getCurrentMemberEntity(it)); list =
		 * modelDict.getInstances(EEnumeration_type.class); it =
		 * list.createIterator(); while (it.next())
		 * createEG((EEnumeration_type)list.getCurrentMemberEntity(it)); list =
		 * modelDict.getInstances(EEntity_definition.class); it =
		 * list.createIterator(); while (it.next())
		 * createEG((EEntity_definition)list.getCurrentMemberEntity(it)); list =
		 * modelDict.getInstances(ESub_supertype_constraint.class); it =
		 * list.createIterator(); while (it.next())
		 * createEG((ESub_supertype_constraint)list.getCurrentMemberEntity(it));
		 * list = modelDict.getInstances(ESelect_type.class); it =
		 * list.createIterator(); while (it.next())
		 * createEG((ESelect_type)list.getCurrentMemberEntity(it)); list =
		 * modelDict.getInstances(EDefined_type.class); it =
		 * list.createIterator(); while (it.next())
		 * createEG((EDefined_type)list.getCurrentMemberEntity(it)); list =
		 * modelDict.getInstances(EExplicit_attribute.class); it =
		 * list.createIterator(); while (it.next())
		 * createEG((EExplicit_attribute)list.getCurrentMemberEntity(it)); list =
		 * modelDict.getInstances(EInverse_attribute.class); it =
		 * list.createIterator(); while (it.next())
		 * createEG((EInverse_attribute)list.getCurrentMemberEntity(it)); list =
		 * modelDict.getInstances(EDerived_attribute.class); it =
		 * list.createIterator(); while (it.next())
		 * createEG((EDerived_attribute)list.getCurrentMemberEntity(it));
		 * 
		 * 
		 * list = modelDict.getInstances(EInterface_specification.class); it =
		 * list.createIterator(); while (it.next())
		 * createEG((EInterface_specification)list.getCurrentMemberEntity(it)); /*
		 * list = modelDict.getInstances(EDeclaration.class); it =
		 * list.createIterator(); while (it.next())
		 * createEG1((EDeclaration)list.getCurrentMemberEntity(it));
		 */

		prop.setNameEG("unknown_layout");
		if (modelEG != null) {

			list = modelEG.getInstances(EGraphics_diagram.class);
			it = list.createIterator();
			while (it.next())
				setEG((EGraphics_diagram) list.getCurrentMemberEntity(it));
			list = modelEG.getInstances(EPage.class);
			it = list.createIterator();
			while (it.next())
				setEG((EPage) list.getCurrentMemberEntity(it));
			list = modelEG.getInstances(EData_type_placement.class);
			it = list.createIterator();
			while (it.next())
				setEG((EData_type_placement) list.getCurrentMemberEntity(it));
			list = modelEG.getInstances(EAttribute_placement.class);
			it = list.createIterator();
			while (it.next())
				setEG((EAttribute_placement) list.getCurrentMemberEntity(it));
			list = modelEG.getInstances(EDefined_relation_placement.class);
			it = list.createIterator();
			while (it.next())
				setEG((EDefined_relation_placement) list
						.getCurrentMemberEntity(it));
			list = modelEG.getInstances(ESelect_relation_placement.class);
			it = list.createIterator();
			while (it.next())
				setEG((ESelect_relation_placement) list
						.getCurrentMemberEntity(it));
			list = modelEG.getInstances(ESupertype_placement.class);
			it = list.createIterator();
			while (it.next())
				setEG((ESupertype_placement) list.getCurrentMemberEntity(it));
			list = modelEG.getInstances(EConstraint_relation_placement.class);
			it = list.createIterator();
			while (it.next())
				setEG((EConstraint_relation_placement) list
						.getCurrentMemberEntity(it));
			/*
			 * list = modelEG.getInstances(EEntity_reference.class); it =
			 * list.createIterator(); while (it.next())
			 * setEG((EEntity_reference)list.getCurrentMemberEntity(it));
			 */
			list = modelEG.getInstances(ESchema_relation_placement.class);
			it = list.createIterator();
			while (it.next())
				setEG((ESchema_relation_placement) list
						.getCurrentMemberEntity(it));
			// not schema level
			list = modelEG.getInstances(EPage_reference_from.class);
			it = list.createIterator();
			while (it.next())
				setEG((EPage_reference_from) list.getCurrentMemberEntity(it));
		}
		/*
		 * COMMENT 1 closed on 2005-08-26, calling only EDeclaration / // schema
		 * level diagrams extension if ((prop.getEditMode() &
		 * PropertySharing.MODE_LONGFORM_MASK) != 0) { SchemaInstance schema =
		 * prop.getRepositoryHandler().getRepository().
		 * findSchemaInstance(mainSchema.getName().toLowerCase()); if (schema ==
		 * null) { list = modelDict.getInstances(EDeclaration.class); it =
		 * list.createIterator(); while (it.next())
		 * createEG((EDeclaration)list.getCurrentMemberEntity(it)); } else {
		 * Hashtable declarationMap = new Hashtable(); ASdaiModel models =
		 * schema.getAssociatedModels(); SdaiIterator mit =
		 * models.createIterator(); //System.out.println("Extended mode, model
		 * count = " + models.getMemberCount()); while (mit.next()) { SdaiModel
		 * model = models.getCurrentMember(mit); list =
		 * model.getInstances(ESchema_definition.class); it =
		 * list.createIterator(); //System.out.println(list.getMemberCount() + "
		 * schema defs for " + model); while (it.next()) { ESchema_definition
		 * schema_def = (ESchema_definition)list.getCurrentMemberEntity(it);
		 * AEntity decls = new AEntity(); CDeclaration.usedinParent(null,
		 * schema_def, prop.getRepositoryHandler().getRepository().getModels(),
		 * decls); SdaiIterator it2 = decls.createIterator();
		 * //System.out.println(decls.getMemberCount() + " declarations for " +
		 * schema_def); while (it2.next()) { EDeclaration declaration =
		 * (EDeclaration)decls.getCurrentMemberEntity(it2); Object obj =
		 * declarationMap.get(declaration.getDefinition(null)); boolean replace =
		 * false; if (obj instanceof EDeclaration) { EDeclaration declaration2 =
		 * (EDeclaration)obj; if (declaration2 instanceof ELocal_declaration) { //
		 * do nothing } else if (declaration instanceof ELocal_declaration) { //
		 * local declarations replaces interfaced but not viceversa replace =
		 * true; } } else replace = true; if (replace)
		 * declarationMap.put(declaration.getDefinition(null), declaration); } } }
		 * Iterator iter = declarationMap.values().iterator(); while
		 * (iter.hasNext()) { createEG((EDeclaration)iter.next()); } } } else {
		 * list = modelDict.getInstances(EDeclaration.class); it =
		 * list.createIterator(); while (it.next())
		 * createEG((EDeclaration)list.getCurrentMemberEntity(it)); } // COMMENT
		 * 1
		 */
		// relation grouping
		if (modelEG != null) {
			// list = modelEG.getInstances(EPage_relation.class);
			// it = list.createIterator();
			// while (it.next())
			// setEG((EPage_relation)list.getCurrentMemberEntity(it));
			list = modelEG.getInstances(EPage_reference_bundle.class);
			it = list.createIterator();
			while (it.next())
				setEG((EPage_reference_bundle) list.getCurrentMemberEntity(it));
			list = modelEG.getInstances(ERelation_bundle.class);
			it = list.createIterator();
			while (it.next())
				setEG((ERelation_bundle) list.getCurrentMemberEntity(it));
/**/
			// upgrade page refs to interschema refs (closed, needed only for schemas older than about 2005-05-01)
			// upgradePageRefs();
			fixPageRefLocalFrom(modelEG);
		}
		
		

		cleanTemp();
		specificPageMask.clear();
	}
	
	protected void fixPageRefLocalFrom(SdaiModel modelEG) throws SdaiException {
		AEntity list = modelEG.getInstances(EPage_reference_bundle.class);
		SdaiIterator it = list.createIterator();
		while (it.next()) {
			Object obj = setEG((EPage_reference_bundle) list.getCurrentMemberEntity(it));
			if (obj instanceof EGPageRefLocalFrom) {
				EGPageRefLocalFrom from2 = null;
				AbstractEGRelation from2_rel = null;
				EGPageRefLocalFrom from = (EGPageRefLocalFrom)obj;
				AbstractEGRelation from_rel = from.getFirstRelation();
				int pgNr = from.getPage();
				if (from_rel != null) {
					int from_type = from_rel.getType();
					Iterator lit = from.getLinkedIterator();
					while (lit.hasNext()) {
						EGPageRefLocalTo to = (EGPageRefLocalTo)lit.next();
						AbstractEGRelation to_rel = to.getFirstRelation();
						if (to_rel != null && to_rel.getType() != from_type) {
							from.removeLinkedRef(to);
							if (from2 == null) {
								from2 = new EGPageRefLocalFrom(prop, from.getReferenced());
								from2.setPage(pgNr);
								from2.setLocation(from.getCenterPoint());
								prop.handler().drawableAddR(from2);
								from2_rel = new EGRelationSimple(prop, from2, from.getReferenced(), 
										from_type == AbstractEGRelation.TYPE_AGREGATION ? 
												AbstractEGRelation.TYPE_INHERITANCE : AbstractEGRelation.TYPE_AGREGATION);
								from2_rel.setPage(pgNr);
								prop.handler().drawableAddR(from2_rel);
							}
							from2.addLinkedRef(to);
						}
					}
				}
			}
		}
	}

	/**
	 * invoked multiple times - for each schema
	 * 
	 * @param modelDict
	 * @throws SdaiException
	 */
	protected void createEGPopulationSchema1(SdaiModel modelDict)
			throws SdaiException {
		this.modelDict = modelDict;
		AEntity list;
		SdaiIterator it;

		list = modelDict.getInstances(ESchema_definition.class);
		it = list.createIterator();
		if (it.next()) {
			mainSchema = createEG((ESchema_definition) list
					.getCurrentMemberEntity(it));
		}

		list = modelDict.getInstances(EInterface_specification.class);
		it = list.createIterator();
		while (it.next())
			createEG((EInterface_specification) list.getCurrentMemberEntity(it));
		schemaRefMap1.clear();
		schemaRefMap2.clear();
	}

	/**
	 * invoked once after all dictionary data is loaded
	 * 
	 * @param modelEG
	 * @throws SdaiException
	 */
	protected void createEGPopulationSchema2(SdaiModel modelEG)
			throws SdaiException {
		AEntity list;
		SdaiIterator it;

		prop.setNameEG("unknown_layout");
		if (modelEG != null) {
			fix_Page_numbers(modelEG); // XXX

			list = modelEG.getInstances(EProperty.class);
			it = list.createIterator();
			while (it.next())
				setEG((EProperty) list.getCurrentMemberEntity(it));

			list = modelEG.getInstances(EGraphics_diagram.class);
			it = list.createIterator();
			while (it.next())
				setEG((EGraphics_diagram) list.getCurrentMemberEntity(it));
			list = modelEG.getInstances(EPage.class);
			it = list.createIterator();
			while (it.next())
				setEG((EPage) list.getCurrentMemberEntity(it));
			list = modelEG.getInstances(EData_type_placement.class);
			it = list.createIterator();
			while (it.next())
				setEG((EData_type_placement) list.getCurrentMemberEntity(it));
			list = modelEG.getInstances(ESchema_relation_placement.class);
			it = list.createIterator();
			while (it.next())
				setEG((ESchema_relation_placement) list
						.getCurrentMemberEntity(it));
			list = modelEG.getInstances(EPage_reference_from.class);
			it = list.createIterator();
			while (it.next())
				setEG((EPage_reference_from) list.getCurrentMemberEntity(it));
			list = modelEG.getInstances(EPage_reference_bundle.class);
			it = list.createIterator();
			while (it.next())
				setEG((EPage_reference_bundle) list.getCurrentMemberEntity(it));
			list = modelEG.getInstances(ERelation_bundle.class);
			it = list.createIterator();
			while (it.next())
				setEG((ERelation_bundle) list.getCurrentMemberEntity(it));

			// upgrade page refs to interschema refs
			upgradePageRefs();
		}
		cleanTemp();
		specificPageMask.clear();
	}

	private void upgradePageRefs() {
		Hashtable map = new Hashtable();
		Iterator iter = prop.handler().drawableIterator();
		while (iter.hasNext()) {
			Object item = iter.next();
			if (!((Paging) item).isOnPage(Paging.INVISIBLE_PAGE)) {
				if (item instanceof IHidenRef) {
					AbstractEGBox box = (AbstractEGBox) item;
					if (box.getWires().size() > 0) {
						EGPageRef ref = EGToolKit.PageRef.getRefForReferenced(
								prop, box);
						ref.setPage(box.getPage());
						ref.setBounds(box.getBounds());
						ref.setLabelNew(box.isLabelNew());
						prop.handler().drawableAddR(ref);
						Iterator wit = box.getWires().iterator();
						while (wit.hasNext()) {
							Wire wire = (Wire) wit.next();
							ref.addReferencedRelation(wire.getRelation());
						}
						box.setPage(Paging.INVISIBLE_PAGE);
					}
				} else if (item instanceof EGPageFromRef) {
					EGPageFromRef box = (EGPageFromRef) item;
					EGEntityRef eref = box.getEGRef();
					if (eref != null) {
						EGPageRef ref = EGToolKit.PageRef.getRefForReferenced(
								prop, eref);
						AbstractEGBox child = box.getReferencedObject();
						AbstractEGRelation delrel = box.getRefRelation();
						ref.setPage(box.getPage());
						ref.setBounds(box.getBounds());
						ref.setLabelNew(box.isLabelNew());
						prop.handler().drawableAddR(ref);
						prop.handler().drawableRemove(delrel);
						delrel.removeChild(child);
						delrel.setParent(null);
						delrel.eliminate();

						Iterator ito = box.getReferences().iterator();
						while (ito.hasNext()) {
							EGPageTo pto = (EGPageTo) ito.next();
							AbstractEGRelation rel = pto.getRefRelation();
							rel.removeChild(pto);
							rel.addChild(child);
							rel.setParent(ref);
							rel.setHint1(delrel.getHint1());
							rel.setHint2(delrel.getHint2());
							rel.setPage(child.getPage());
							prop.handler().drawableRemove(pto);
							pto.eliminate();
						}
						prop.handler().drawableRemove(box);
						box.eliminate();
					}
				} else if (item instanceof EGPageToRef) {
					EGPageToRef box = (EGPageToRef) item;
					EGEntityRef eref = (EGEntityRef) box.getReferencedObject();
					EGPageRef ref = EGToolKit.PageRef.getRefForReferenced(prop,
							eref);
					AbstractEGRelation rel = box.getRefRelation();
					EGPageFrom delbox = box.getReferenced();
					AbstractEGRelation delrel = delbox.getRefRelation();
					ref.setPage(box.getPage());
					ref.setBounds(box.getBounds());
					ref.setLabelNew(box.isLabelNew());
					prop.handler().drawableAddR(ref);
					if (delrel != null) {
						delrel.removeChild(eref);
						delrel.setParent(null);
						delrel.eliminate();
						prop.handler().drawableRemove(delrel);
						prop.handler().drawableRemove(delbox);
						delbox.eliminate();
					}
					rel.removeChild(box);
					rel.addChild(ref);
					prop.handler().drawableRemove(box);
					box.eliminate();
				} else if (item instanceof EGPageToSimple) {
					EGPageToSimple box = (EGPageToSimple) item;
					EGSimple eref = (EGSimple) box.getReferencedObject();
					EGPageRef ref = EGToolKit.PageRef.getRefForReferenced(prop,
							eref);
					AbstractEGRelation rel = box.getRefRelation();
					EGPageFrom delbox = box.getReferenced();
					AbstractEGRelation delrel = delbox.getRefRelation();
					ref.setPage(box.getPage());
					ref.setBounds(box.getBounds());
					ref.setLabelNew(box.isLabelNew());
					prop.handler().drawableAddR(ref);
					if (delrel != null) {
						delrel.removeChild(eref);
						delrel.setParent(null);
						delrel.eliminate();
						prop.handler().drawableRemove(delrel);
						prop.handler().drawableRemove(delbox);
						delbox.eliminate();
					}
					rel.removeChild(box);
					rel.addChild(ref);
					prop.handler().drawableRemove(box);
					box.eliminate();
				}
			} // end "not on invisiple page"
			if (item instanceof EGPageToRef) {
			} else if (item instanceof EGPageToSimple) {
			} else if ((item instanceof EGPageTo)
					&& !(((EGPageTo) item).getRefRelation().getParent() instanceof EGSimple)) {
				EGPageTo pgref = (EGPageTo) item;
				AbstractEGRelation rel = pgref.getRefRelation();
				rel.removeChild(pgref);
				EGPageRef ref = EGToolKit.PageRef.getRefForReferenced(prop,
						pgref.getReferencedObject(), true);
				ref.setPage(pgref.getPage());
				ref.setBounds(pgref.getBounds());
				ref.setLabelNew(pgref.isLabelNew());
				prop.handler().drawableAddR(ref);
				rel.addChild(ref);
				prop.handler().drawableRemove(pgref);
				map.put(pgref, ref);
			}

		}
		iter = prop.handler().drawableIterator();
		while (iter.hasNext()) {
			Object item = iter.next();
			if ((item instanceof EGPageFrom)
					&& !(((EGPageFrom) item).getReferencedObject() instanceof EGSimple)) {
				EGPageFrom pgref = (EGPageFrom) item;
				EGPageRefLocalFrom ref = new EGPageRefLocalFrom(prop, pgref
						.getReferencedObject());
				ref.setPage(pgref.getPage());
				ref.setBounds(pgref.getBounds());
				ref.setLabelNew(pgref.isLabelNew());
				prop.handler().drawableAddR(ref);
				Iterator pit = pgref.getReferences().iterator();
				while (pit.hasNext()) {
					EGPageTo pgto = (EGPageTo) pit.next();
					EGPageRefLocalTo newto = (EGPageRefLocalTo) map.get(pgto);
					ref.addLinkedRef(newto);
					pgto.eliminate();
				}
				AbstractEGRelation rel = pgref.getRefRelation();
				rel.setParent(ref);
				prop.handler().drawableRemove(pgref);
				pgref.eliminate();
			}
		}

	}

	/**
	 * fixes erroneus page numbers in Express_G schema model
	 * 
	 * @param modelEG
	 */
	private void fix_Page_numbers(SdaiModel modelEG) {
		try {
			TreeSet numbers = new TreeSet();
			ArrayList errorPage = new ArrayList();
			APage list = (APage) modelEG.getInstances(EPage.class);
			SdaiIterator it = list.createIterator();
			while (it.next()) {
				EPage page = list.getCurrentMember(it);
				int pgNr = page.getPage_number(null);
				if (pgNr >= 0) {
					Integer pageNumber = new Integer(pgNr);
					if (numbers.contains(pageNumber)) {
						page.setPage_number(null, Paging.NO_PAGE);
						errorPage.add(page);
					} else {
						numbers.add(pageNumber);
					}
				} else {
					errorPage.add(page);
				}
			}
			Iterator iter = errorPage.iterator();
			Iterator numit = numbers.iterator();
			int last_page = 0;
			int next_page = numit.hasNext() ? ((Integer) numit.next())
					.intValue() : 0;
			while (iter.hasNext()) {
				while (last_page == next_page) {
					if (numit.hasNext()) {
						next_page = ((Integer) numit.next()).intValue();
					}
					last_page++;
				}

				EPage page = (EPage) iter.next();
				page.setPage_number(null, last_page);

				last_page++;
			}
		} catch (SdaiException sex) {
			SdaieditPlugin.log(sex);
		}
	}
}
