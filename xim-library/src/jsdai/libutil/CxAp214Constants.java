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

package jsdai.libutil;

/**
 * <p>
 * Contains constants used for Ap214 Cx creation.
 * </p>
 * 
 * @author Valdas Zigas
 * @version $Revision$
 */
public interface CxAp214Constants {
	public static String OLD_MAPPING_TARGET = "old_mapping_target";

	public final static String UOF_NAME_AP214_G1 = "wireframe_model_2d";
	public final static String UOF_NAME_AP214_G2 = "wireframe_model_3d";
	public final static String UOF_NAME_AP214_G3 = "connected_surface_model";
	public final static String UOF_NAME_AP214_G4 = "faceted_b_rep_model";
	public final static String UOF_NAME_AP214_G5 = "b_rep_model";
	public final static String UOF_NAME_AP214_G6 = "compound_model";
	public final static String UOF_NAME_AP214_G7 = "csg_model";
	public final static String UOF_NAME_AP214_E1 = "external_reference_mechanism";
	public final static String UOF_NAME_AP214_S2 = "element_structure";
	public final static String UOF_NAME_AP214_P1 = "geometric_presentation";
	public final static String UOF_NAME_AP214_P2 = "annotated_presentation";
	public final static String UOF_NAME_AP214_S7 = "specification_control";
	public final static String UOF_NAME_AP214_S4 = "effectivity";
	
	public final static String UOF_NAME_AP212_CF1 = "configuration_management";

	public static final String KEY_Transformation__is_tranformation_operator = "transformation__is_tranformation_operator";
	public static final String KEY_Transformation__mapping_target = "transformation__mapping_target";
	public static final String KEY_Transformation__mapping_origin = "transformation__mapping_origin";

	public static final String KEY_Detailed_element__part_of_geom_model = "detailed_element__part_of_geom_model";
	public static final String KEY_Detailed_element__part_of_topo_model = "detailed_element__part_of_topo_model";

	public static final String KEY_Axis2d_placement = "axis2d_placement";

	public static final String KEY_Product = "product";
	public static final String KEY_Product_definition = "product_definition";
	public static final String KEY_Product_related_product_category = "product_related_product_category";
	public static final String KEY_AGGREGATE_Product_definition_context_association = "product_definition_context_association";
	public static final String KEY_Class_usage_effectivity_context_assignment = "class_usage_effectivity_context_assignment";
	public static final String KEY_Applied_effectivity_assignment = "applied_effectivity_assignment";
	public static final String KEY_Drawing_sheet_revision_usage = "drawing_sheet_revision_usage";
	public static final String KEY_Presentation_size = "presentation_size";
	public static final String KEY_Context_dependent_shape_representation = "context_dependent_shape_representation";

	/**
	 * Key defined case of mapping target defined in mapping table. Value type
	 * Integer.
	 */
	public static final String KEY_MAPPING_TARGET_CASE = "mapping_target_case";

	public static final Object KEY_GENERATED = new Object();
}