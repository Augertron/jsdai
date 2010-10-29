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

package jsdai.express_g.util.repocopy;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.console.MessageConsoleStream;

import jsdai.SExpress_g_schema.AGraphics_diagram;
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
import jsdai.SExpress_g_schema.ESupertype_placement;
import jsdai.SExpress_g_schema.SExpress_g_schema;
import jsdai.SExtended_dictionary_schema.ADeclaration;
import jsdai.SExtended_dictionary_schema.EAttribute;
import jsdai.SExtended_dictionary_schema.EData_type;
import jsdai.SExtended_dictionary_schema.EDeclaration;
import jsdai.SExtended_dictionary_schema.EDefined_type;
import jsdai.SExtended_dictionary_schema.ESchema_definition;
import jsdai.SExtended_dictionary_schema.ESelect_type;
import jsdai.SExtended_dictionary_schema.ESub_supertype_constraint;
import jsdai.common.CommonPlugin;
import jsdai.express_g.SdaieditPlugin;
import jsdai.lang.AEntity;
import jsdai.lang.ASdaiModel;
import jsdai.lang.CEntity;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiRepository;

/**
 * @author Mantas Balnys
 *
 *	applies layout from oldLayout repository to newDictionary repository data
 */
public class RepositoryMerger extends Action implements Runnable, IRunnableWithProgress {
	private SdaiRepository repoEG;
	private SdaiRepository repoDict;
	
	private InstanceMap instanceMap;

	private boolean canceled = false;

  MessageConsoleStream stream;


	boolean flag_print_debug = false;

  long start_time, finish_time, elapsed_time;

	boolean flag_debug1 = false;

	public RepositoryMerger(SdaiRepository newDictionary, SdaiRepository oldLayout) throws SdaiException {
		repoDict = newDictionary;
		repoEG = oldLayout;
		instanceMap = new InstanceMap(repoDict);
		if (!repoDict.isActive()) repoDict.openRepository();
		if (!repoEG.isActive()) repoEG.openRepository();
	}
	// move entity and all references in entity
	
	void printDebug(StringBuffer message, Object msg) {
			// System.out.println("<DEBUG> " + msg);
		if (flag_print_debug) {
//			System.out.println("<DEBUG> " + msg);
			//message.append("\n\tDEBUG: " + msg);
		}
	}
	void printDebug(Object msg) {
		if (flag_print_debug) {
			// System.out.println("<DEBUG> " + msg);
		}
	}
	void printDebug1(Object msg) {
		//if (flag_print_debug) {
	    finish_time = System.currentTimeMillis();
		  elapsed_time = finish_time - start_time;
			if (flag_debug1) {
				System.out.println("<DEBUG> " + msg + ", time: " + elapsed_time);
	  	}
	    start_time = System.currentTimeMillis();
		//}
	}
	
	
	protected ESelect_relation_placement moveEG(SdaiModel model, ESelect_relation_placement place) {
		if (testModel(model, place)) return place;
		ESelect_relation_placement placeNew = null;
		try {
			ESelect_type parent = null;
			if (place.testParent(null)) parent = (ESelect_type)instanceMap.get(place.getParent(null));
			if (parent != null) {
				placeNew = (ESelect_relation_placement)moveEG(model, (ERelation_placement)place); // call super
				placeNew.setParent(null, parent);
			}
		} catch (SdaiException sex) {
			SdaieditPlugin.log(sex);
			placeNew = null;
		}
		return placeNew;
	}
	
	protected EDefined_relation_placement moveEG(SdaiModel model, EDefined_relation_placement place) {
		if (testModel(model, place)) return place;
		EDefined_relation_placement placeNew = null;
		try {
			EDefined_type parent = null;
			if (place.testParent(null)) parent = (EDefined_type)instanceMap.get(place.getParent(null));
			if (parent != null) {
				placeNew = (EDefined_relation_placement)moveEG(model, (ERelation_placement)place); // call super
				placeNew.setParent(null, parent);
			}
		} catch (SdaiException sex) {
			SdaieditPlugin.log(sex);
			placeNew = null;
		}
		return placeNew;
	}
	
	protected EAttribute_placement moveEG(SdaiModel model, EAttribute_placement place) {
		if (testModel(model, place)) return place;
		EAttribute_placement placeNew = null;
		try {
			EAttribute represented = null;
			if (place.testRepresented_object(null)) represented = (EAttribute)instanceMap.get(place.getRepresented_object(null));
/*			
if (   place.getRepresented_object(null).getName(null) == "datum_reference"
	|| place.getRepresented_object(null).getName(null) == "precedent_item"
	|| place.getRepresented_object(null).getName(null) == "subsequent_item") {
	System.err.println("\nREQUESTED NAME FOUND");
	System.err.println("old place:" + place);
	System.err.println(place.findEntityInstanceSdaiModel());
	System.err.println(instanceMap.getKey(place));
	System.err.println("old repr:" + place.getRepresented_object(null));
	System.err.println(place.getRepresented_object(null).findEntityInstanceSdaiModel());
	System.err.println(instanceMap.getKey(place.getRepresented_object(null)));
	System.err.println("represented:" + represented);
}*/
			if (represented != null) {
/*if (   represented.getName(null) == "datum_reference"
	|| represented.getName(null) == "precedent_item"
	|| represented.getName(null) == "subsequent_item"
		) {
	System.err.println("\nREQUESTED NAME FOUND2");
	System.err.println("old place:" + place);
	System.err.println(place.findEntityInstanceSdaiModel());
	System.err.println(instanceMap.getKey(place));
	System.err.println("old repr:" + place.getRepresented_object(null));
	System.err.println(place.getRepresented_object(null).findEntityInstanceSdaiModel());
	System.err.println(instanceMap.getKey(place.getRepresented_object(null)));
	System.err.println("new repr:" + represented);
	System.err.println(represented.findEntityInstanceSdaiModel());
	System.err.println(instanceMap.getKey(represented));
}*/
				placeNew = (EAttribute_placement)moveEG(model, (ERelation_placement)place); // call super
				placeNew.setRepresented_object(null, represented);
			}
		} catch (SdaiException sex) {
			SdaieditPlugin.log(sex);
			placeNew = null;
		}
		return placeNew;
	}
	
	protected ESupertype_placement moveEG(SdaiModel model, ESupertype_placement place) {
		if (testModel(model, place)) return place;
		ESupertype_placement relNew = null;
		try {
			EData_type child = null;
			if (place.testChild(null)) child = (EData_type)instanceMap.get(place.getChild(null));
			EData_type parent = null;
			if (place.testParent(null)) parent = (EData_type)instanceMap.get(place.getParent(null));
			if ((child != null)&&(parent != null)) {
				relNew = (ESupertype_placement)moveEG(model, (ERelation_placement)place); // call super
				relNew.setChild(null, child);
				relNew.setParent(null, parent);
			}
		} catch (SdaiException sex) {
			SdaieditPlugin.log(sex);
			relNew = null;
		}
		return relNew;
	}
	
	protected EConstraint_relation_placement moveEG(SdaiModel model, EConstraint_relation_placement place) {
		if (testModel(model, place)) return place;
		EConstraint_relation_placement relNew = null;
		try {
			EData_type child = null;
			if (place.testChild(null)) child = (EData_type)instanceMap.get(place.getChild(null));
			ESub_supertype_constraint parent = null;
			if (place.testParent(null)) parent = (ESub_supertype_constraint)instanceMap.get(place.getParent(null));
			if ((child != null)&&(parent != null)) {
				relNew = (EConstraint_relation_placement)moveEG(model, (ERelation_placement)place); // call super
				relNew.setChild(null, child);
				relNew.setParent(null, parent);
			}
		} catch (SdaiException sex) {
			SdaieditPlugin.log(sex);
			relNew = null;
		}
		return relNew;
	}

	protected ESchema_relation_placement moveEG(SdaiModel model, ESchema_relation_placement place) {
		if (testModel(model, place)) return place;
		ESchema_relation_placement relNew = null;
		try {
			ESchema_definition child = null;
			if (place.testChild(null)) child = (ESchema_definition)instanceMap.get(place.getChild(null));
			ESchema_definition parent = null;
			if (place.testParent(null)) parent = (ESchema_definition)instanceMap.get(place.getParent(null));
			if ((child != null)&&(parent != null)) {
				relNew = (ESchema_relation_placement)moveEG(model, (ERelation_placement)place); // call super
				relNew.setChild(null, child);
				relNew.setParent(null, parent);
			}
		} catch (SdaiException sex) {
			SdaieditPlugin.log(sex);
			relNew = null;
		}
		return relNew;
	}
	
	protected EEntity moveEGselect(SdaiModel model, EEntity data_type_extension) {
		if (testModel(model, data_type_extension)) return data_type_extension;
		EEntity ent = null;
		if (data_type_extension instanceof EPage)
			ent = moveEG(model, (EPage)data_type_extension);
		// etc with data_type_extension
		return ent;
	}
	
	protected EData_type_placement moveEG(SdaiModel model, EData_type_placement place) {
		if (testModel(model, place)) return place;
		EData_type_placement placeNew = null;
		try {
			EEntity represented = null;
			if (place.testRepresented_object(null))	{
				EEntity old_represented = place.getRepresented_object(null);
				if (old_represented.findEntityInstanceSdaiModel().getUnderlyingSchemaString().equalsIgnoreCase("EXPRESS_G_SCHEMA")) {
					represented = moveEGselect(model, old_represented);
//					System.out.println("finding old represented: " + represented + ", old represented: " + old_represented);
				}else {
					represented = instanceMap.get(old_represented);
//					System.out.println("represented from instanceMap: " + represented + ", old represented: " + old_represented + ", schema: " + old_represented.findEntityInstanceSdaiModel().getUnderlyingSchemaString());
				}	
/*				if (old_represented instanceof EPage)
					represented = moveEG(model, (EPage)placeNew.getPresented_on(null));
				else
					represented = instanceMap.get(old_represented);*/
			}
			if (represented != null) {
				placeNew = (EData_type_placement)moveEG(model, (EObject_placement)place); // call super
				placeNew.setRepresented_object(null, represented);
			}
		} catch (SdaiException sex) {
			SdaieditPlugin.log(sex);
			placeNew = null;
		}
		return placeNew;
	}
	
	protected EPage_reference_from moveEG(SdaiModel model, EPage_reference_from place) {
		if (testModel(model, place)) return place;
		EPage_reference_from pageNew = null;
		try {
			EEntity child = null;
			if (place.testChild(null)) child = instanceMap.get(place.getChild(null));
			if (child != null) {
				pageNew = (EPage_reference_from)moveEG(model, (EObject_placement)place); // call super
				if (pageNew.testExtended_relation(null)) moveEG(model, pageNew.getExtended_relation(null));
				if (pageNew.testReference_from(null)) moveEG(model, pageNew.getReference_from(null));
				pageNew.setChild(null, child);
			}
		} catch (SdaiException sex) {
			SdaieditPlugin.log(sex);
			pageNew = null;
		}
		return pageNew;
	}
	
	protected EPage_relation moveEG(SdaiModel model, EPage_relation place) {
		printDebug("<###> moveEG-page_relation: " + place);
		if (testModel(model, place)) return place;
		EPage_relation relNew = null;
		try {
			EEntity child = null;
			if (place.testChild(null)) child = instanceMap.get(place.getChild(null));
			printDebug("moveEG-page_relation - place.getChild: " + place.getChild(null));
			printDebug("moveEG-page_relation - instanceMap: " + instanceMap);

			if (child != null) {
				relNew = (EPage_relation)moveEG(model, (ERelation_placement)place); // call super
				if (relNew.testParent(null)) {
				    if (relNew.getParent(null) instanceof EPage_reference_bundle) {
				    	printDebug("moveEG-page_relation - page_reference_bundle, relNew: " + relNew + ", parent: " + relNew.getParent(null));
				        moveEG(model, (EPage_reference_bundle)relNew.getParent(null));
				    } else if (relNew.getParent(null) instanceof EPage_reference_from) {
				    	printDebug("moveEG-page_relation - page_reference_from, relNew: " + relNew + ", parent: " + relNew.getParent(null));
				      	moveEG(model, (EPage_reference_from)relNew.getParent(null));
						}
				} else {
					printDebug("moveEG-page_relation - parent is NULL");
				}
				relNew.setChild(null, child);
			} else {
				printDebug("moveEG-page_relation - child is NULL");
			}
		} catch (SdaiException sex) {
			SdaieditPlugin.log(sex);
			relNew = null;
		}
		return relNew;
	}
	
	protected APage_reference_to moveEG(SdaiModel model, APage_reference_to pages) {
		try {
			SdaiIterator sit = pages.createIterator();
			while (sit.next()) {
				EPage_reference_to page = pages.getCurrentMember(sit);
				moveEG(model, page);
			}
			return pages; // agregate is not movable itself so returns the same object
		} catch (SdaiException e) {
			SdaieditPlugin.log(e);
			return null;
		}
	}
	
	protected EPage_reference_to moveEG(SdaiModel model, EPage_reference_to place) {
		if (testModel(model, place)) return place;
		EPage_reference_to pageNew = null;
		try {
			EEntity parent = null;
			if (place.testParent(null)) parent = instanceMap.get(place.getParent(null));
			if (parent != null) {
				pageNew = (EPage_reference_to)moveEG(model, (EObject_placement)place); // call super
				if (pageNew.testReference_to(null)) moveEG(model, pageNew.getReference_to(null));
				pageNew.setParent(null, parent);
			}
		} catch (SdaiException sex) {
			SdaieditPlugin.log(sex);
			pageNew = null;
		}
		return pageNew;
	}

	protected ARelation_placement moveEG(SdaiModel model, ARelation_placement rels) {
		try {
			int count = 0;
			SdaiIterator sit = rels.createIterator();
			while (sit.next()) {
				ERelation_placement rel = rels.getCurrentMember(sit);
				ERelation_placement relNew = null; 
				if (rel instanceof ESchema_relation_placement) relNew = moveEG(model, (ESchema_relation_placement)rel); else 
				if (rel instanceof EAttribute_placement) relNew = moveEG(model, (EAttribute_placement)rel); else 
				if (rel instanceof EDefined_relation_placement) relNew = moveEG(model, (EDefined_relation_placement)rel); else 
				if (rel instanceof ESelect_relation_placement) relNew = moveEG(model, (ESelect_relation_placement)rel); else 
				if (rel instanceof ESupertype_placement) relNew = moveEG(model, (ESupertype_placement)rel); else 
				if (rel instanceof EConstraint_relation_placement) relNew = moveEG(model, (EConstraint_relation_placement)rel); else 
				if (rel instanceof EPage_relation) relNew = moveEG(model, (EPage_relation)rel); 
				else SdaieditPlugin.log("Unknown relation type - " + rel, IStatus.WARNING);
				if (relNew != null) count++;
			}
			if (count > 0) return rels; // agregate is not movable itself so returns the same object
			else return null;
		} catch (SdaiException e) {
			SdaieditPlugin.log(e);
			return null;
		}
	}
	
	protected ERelation_bundle moveEG(SdaiModel model, ERelation_bundle place) {
		if (testModel(model, place)) return place;
		ERelation_bundle bundle = null;
		try {
			bundle = (ERelation_bundle)moveEG(model, (EEntity)place); // call super
			if (bundle.testMember(null)) moveEG(model, bundle.getMember(null));
		} catch (SdaiException sex) {
			SdaieditPlugin.log(sex);
			bundle = null;
		}
		return bundle;
	}
	
	protected EPage_reference_bundle moveEG(SdaiModel model, EPage_reference_bundle place) {
		if (testModel(model, place)) return place;
		EPage_reference_bundle bundle = null;
		try {
			EEntity referenced = null;
			if (place.testReferenced(null)) {
				referenced = instanceMap.get(place.getReferenced(null));
			}
			if (referenced != null) {
				bundle = (EPage_reference_bundle)moveEG(model, (EObject_placement)place); // call super
/*				if (bundle.testRelation(null)) moveEG(model, bundle.getRelation(null));
	2005-12-13
	trying to disable moving bundles with no relations
				*/
				if (bundle.testRelation(null) && moveEG(model, bundle.getRelation(null)) != null) {
					if (bundle.testLink(null)) moveEG(model, bundle.getLink(null));
					bundle.setReferenced(null, referenced);
				} else {
					bundle.deleteApplicationInstance();
					bundle = null; 
				}
			}
		} catch (SdaiException sex) {
			SdaieditPlugin.log(sex);
			bundle = null;
		}
		return bundle;
	}
	
	protected EGraphics_diagram moveEG(SdaiModel model, EGraphics_diagram place) {
		if (testModel(model, place)) return place;
		EGraphics_diagram diagramNew = null;
		try {
			ESchema_definition schemaDef = null;
			if (place.testDic_schema(null)) schemaDef = (ESchema_definition)instanceMap.get(place.getDic_schema(null));
			if (!place.testDic_schema(null) || schemaDef != null) {
				diagramNew = (EGraphics_diagram)model.substituteInstance(place);
				if (diagramNew.testDefault_size(null)) moveEG(model, diagramNew.getDefault_size(null));
				if (schemaDef != null) diagramNew.setDic_schema(null, schemaDef);
			}
		} catch (SdaiException sex) {
			SdaieditPlugin.log(sex);
			diagramNew = null;
		}
		return diagramNew;
	}
	
	protected ERelation_placement moveEG(SdaiModel model, ERelation_placement place) {
		if (testModel(model, place)) return place;
		try {
			ERelation_placement placeNew = (ERelation_placement)moveEG(model, (EObject_placement)place); // call super
			if (placeNew.testHint1(null)) moveEG(model, placeNew.getHint1(null));
			if (placeNew.testHint2(null)) moveEG(model, placeNew.getHint2(null));
			return placeNew;
		} catch (SdaiException e) {
			SdaieditPlugin.log(e);
			return null;
		}
	}
	
	protected EObject_placement moveEG(SdaiModel model, EObject_placement place) {
		if (testModel(model, place)) return place;
		try {
			EObject_placement placeNew = (EObject_placement)model.substituteInstance(place);
//if (placeNew.toString().indexOf("177383") >= 0) {
//	System.err.println(placeNew);
//}
			if (placeNew.testPresented_on(null)) moveEG(model, placeNew.getPresented_on(null));
			if (placeNew.testObject_location(null)) moveEG(model, placeNew.getObject_location(null));
			if (placeNew.testObject_size(null)) moveEG(model, placeNew.getObject_size(null));
			if (placeNew.testRepresentation_color(null)) moveEG(model, placeNew.getRepresentation_color(null));
			return placeNew;
		} catch (SdaiException e) {
			SdaieditPlugin.log(e);
			return null;
		}
	}
	
	protected EPage moveEG(SdaiModel model, EPage place) {
		if (testModel(model, place)) return place;
		try {
			EPage pageNew = (EPage)model.substituteInstance(place);
			if (pageNew.testDiagram(null)) moveEG(model, pageNew.getDiagram(null));
			if (pageNew.testPage_size(null)) moveEG(model, pageNew.getPage_size(null));
			return pageNew;
		} catch (SdaiException e) {
			SdaieditPlugin.log(e);
			return null;
		}
	}
	
	protected EEntity moveEG(SdaiModel model, EEntity entity) {
		if (testModel(model, entity)) return entity;
		try {
			EEntity entityNew = model.substituteInstance(entity);
			return entityNew;
		} catch (SdaiException sex) {
			SdaieditPlugin.log(sex);
			return null;
		}
	}
	
	protected boolean testModel(SdaiModel model, EEntity entity) {
		SdaiModel modelE = null;
		try {
			modelE = entity.findEntityInstanceSdaiModel();
		} catch (SdaiException sex) {
			SdaieditPlugin.log(sex);
		}
		return modelE == model;
	}
	
	/**
	 * creates EGschema model in newDictionary and copies valid data to it
	 * @param modelEG
	 * @return
	 */
	protected SdaiModel copyEGData(SdaiModel modelEG, IProgressMonitor progress, String taskName, StringBuffer message) throws SdaiException {
System.out.println("<0>model: " + modelEG.getName());
String model_name = modelEG.getName();
if (model_name.equals("AP203-PARTIAL_EXPRESS_G_DATA") || 
	model_name.equals("AP210_ELECTRONIC_ASSEMBLY_INTERCONNECT_AND_PACKAGING_DESIGN_ARM_PARTIAL_LONG_LAYOUT_EXPRESS_G_DATA") || 
	model_name.equals("EXPRESSCOMPILERREPO_COMPLETE_LAYOUT_EXPRESS_G_DATA") || 
	model_name.equals("EXPRESSCOMPILERREPO_COMPLETE_SHORT_LAYOUT_EXPRESS_G_DATA")) {
 flag_debug1 = true;
} else {
	flag_debug1 = false;
}

printDebug(message, "<1>: " + modelEG.getInstances(EPage_reference_bundle.class));


//		SdaieditPlugin.console("copying");
		int countOld = 0;
		int countNew = 0;

		stream = CommonPlugin.getDefault().getConsole();

		
//		System.out.println("copying");
		SdaiModel modelNew = repoDict.createSdaiModel(modelEG.getName(), SExpress_g_schema.class);
		modelNew.startReadWriteAccess();
		AEntity list;
	    IndependentSdaiIterator it = new IndependentSdaiIterator();

	    EGraphics_diagram diagram = null;
	    list = modelEG.getInstances(EGraphics_diagram.class);

	    start_time = System.currentTimeMillis();
printDebug1("EGraphics_diagram: " + list.getMemberCount());
	    it.setEntities(list);
	    if (it.hasNext()) {
	    	diagram = (EGraphics_diagram)it.next();  // old
printDebug(message, "diagram: " + diagram);
	    	if (diagram.testComment(null)) {
	    		message.append(diagram.getComment(null));
//System.out.println("<>prev diagram: " + diagram + ", comment: " + diagram.getComment(null));
	    	}else {
	    		message.append("name missing!");
//System.out.println("<>prev diagram: " + diagram + ", comment: _NAME_MISSING_");
				}
	    	diagram = moveEG(modelNew, diagram);
	    }
//System.out.println("<>new diagram: " + diagram);
printDebug(message, "<2>: " + modelEG.getInstances(EPage_reference_bundle.class).getMemberCount());
//System.out.println("<2>: " + modelEG.getInstances(EPage_reference_bundle.class));
	    if (diagram != null) {

System.out.println("<<((SINGLE_LOADING))>>: " + !diagram.testDic_schema(null));
		    //instanceMap.SINGLE_LOADING = !diagram.testDic_schema(null);  // original
	
					// experimenting  - true - diagram correctly drawn after import, false - also drawn - hm?
					instanceMap.SINGLE_LOADING = false;

//System.out.println("SINGLE MODE:" + instanceMap.SINGLE_LOADING);		    
printDebug(message, "<3>: " + modelEG.getInstances(EPage_reference_bundle.class).getMemberCount());
		    if (progress != null) progress.subTask(taskName + " [0%]");
		    list = modelEG.getInstances(EPage_reference_to.class);
printDebug1("EPage_reference_to: " + list.getMemberCount());
		    it.setEntities(list);
		    while (it.hasNext()) {
		    	countOld++;
//System.out.println("<01> countOld: " + countOld);
//		    	if (moveEG(modelNew, (EPage_reference_to)it.next()) != null) countNew++;
					EPage_reference_to itnext = (EPage_reference_to)it.next();
		    	if (moveEG(modelNew, itnext) != null) {
		    		countNew++;
		    	} else {
		    		message.append("\nplacing failed: " + itnext);
		    	}
		    }
printDebug(message, "<4>: " + modelEG.getInstances(EPage_reference_bundle.class).getMemberCount());
		    if (progress != null) progress.subTask(taskName + " [3%]");
		    list = modelEG.getInstances(EPage_reference_from.class);
printDebug1("EPage_reference_from: " + list.getMemberCount());

		    it.setEntities(list);
		    while (it.hasNext()) {
		    	countOld++;
//System.out.println("<02> countOld: " + countOld);
//		    	if (moveEG(modelNew, (EPage_reference_from)it.next()) != null) countNew++;
					EPage_reference_from itnext = (EPage_reference_from)it.next();
		    	if (moveEG(modelNew, itnext) != null) {
		    		countNew++;
		    	} else {
		    		message.append("\nplacing failed: " + itnext);
		    	}	
		    }
printDebug(message, "<5>: " + modelEG.getInstances(EPage_reference_bundle.class).getMemberCount());
		    if (progress != null) progress.subTask(taskName + " [30%]");
		    list = modelEG.getInstances(EData_type_placement.class);
printDebug1("EData_type_placement: " + list.getMemberCount());
		    it.setEntities(list);
		    while (it.hasNext()) {
		    	countOld++;
//System.out.println("<03> countOld: " + countOld);
//		    	if (moveEG(modelNew, (EData_type_placement)it.next()) != null) countNew++;
					EData_type_placement itnext = (EData_type_placement)it.next();
		    	if (moveEG(modelNew, itnext) != null) {
		    		countNew++;
//						System.out.println("placing OK: " + itnext);
		    	} else {
		    		message.append("\nplacing failed: " + itnext);
//						System.out.println("placing FAILED: " + itnext);
		    	}	
		    }
printDebug(message, "<6>: " + modelEG.getInstances(EPage_reference_bundle.class).getMemberCount());
		    if (progress != null) progress.subTask(taskName + " [40%]");
		    list = modelEG.getInstances(ESchema_relation_placement.class);
printDebug1("ESchema_relation_placement: " + list.getMemberCount());
		    it.setEntities(list);
		    while (it.hasNext()) {
		    	countOld++;
//System.out.println("<04> countOld: " + countOld);
//		    	if (moveEG(modelNew, (ESchema_relation_placement)it.next()) != null) countNew++;
					ESchema_relation_placement itnext = (ESchema_relation_placement)it.next();
		    	if (moveEG(modelNew, itnext) != null) {
		    		countNew++;
		    	} else {
		    		message.append("\nplacing failed: " + itnext);
		    	}	
		    }
printDebug(message, "<7>: " + modelEG.getInstances(EPage_reference_bundle.class).getMemberCount());
		    if (progress != null) progress.subTask(taskName + " [45%]");
		    list = modelEG.getInstances(EAttribute_placement.class);
printDebug1("EAttribute_placement: " + list.getMemberCount());
		    it.setEntities(list);
		    while (it.hasNext()) {
		    	countOld++;
//System.out.println("<05> countOld: " + countOld);
//		    	if (moveEG(modelNew, (EAttribute_placement)it.next()) != null) countNew++;
					EAttribute_placement itnext = (EAttribute_placement)it.next();
		    	if (moveEG(modelNew, itnext) != null) {
		    		countNew++;
		    	} else {
		    		message.append("\nplacing failed: " + itnext);
		    	}	
		    }
printDebug(message, "<8>: " + modelEG.getInstances(EPage_reference_bundle.class).getMemberCount());
		    if (progress != null) progress.subTask(taskName + " [60%]");
		    list = modelEG.getInstances(EDefined_relation_placement.class);
printDebug1("EDefined_relation_placement: " + list.getMemberCount());
		    it.setEntities(list);
		    while (it.hasNext()) {
		    	countOld++;
//System.out.println("<06> countOld: " + countOld);
//		    	if (moveEG(modelNew, (EDefined_relation_placement)it.next()) != null) countNew++;
					EDefined_relation_placement itnext = (EDefined_relation_placement)it.next();
		    	if (moveEG(modelNew, itnext) != null) {
		    		countNew++;
		    	} else {
		    		message.append("\nplacing failed: " + itnext);
		    	}	
		    }
printDebug(message, "<9>: " + modelEG.getInstances(EPage_reference_bundle.class).getMemberCount());
		    if (progress != null) progress.subTask(taskName + " [65%]");
		    list = modelEG.getInstances(ESelect_relation_placement.class);
printDebug1("ESelect_relation_placement: " + list.getMemberCount());
		    it.setEntities(list);
		    while (it.hasNext()) {
		    	countOld++;
//System.out.println("<07> countOld: " + countOld);
//		    	if (moveEG(modelNew, (ESelect_relation_placement)it.next()) != null) countNew++;
					ESelect_relation_placement itnext = (ESelect_relation_placement)it.next();
		    	if (moveEG(modelNew, itnext) != null) {
		    		countNew++;
		    	} else {
		    		message.append("\nplacing failed: " + itnext);
		    	}
		    }
printDebug(message, "<10>: " + modelEG.getInstances(EPage_reference_bundle.class).getMemberCount());
		    if (progress != null) progress.subTask(taskName + " [70%]");
		    list = modelEG.getInstances(ESupertype_placement.class);
printDebug1("ESupertype_placement: " + list.getMemberCount());
		    it.setEntities(list);
		    while (it.hasNext()) {
		    	countOld++;
//System.out.println("<08> countOld: " + countOld);
//		    	if (moveEG(modelNew, (ESupertype_placement)it.next()) != null) countNew++;
					ESupertype_placement itnext = (ESupertype_placement)it.next();
		    	if (moveEG(modelNew, itnext) != null) {
		    		countNew++;
		    	} else {
		    		message.append("\nplacing failed: " + itnext);
		    	}
		    }
printDebug(message, "<11>: " + modelEG.getInstances(EPage_reference_bundle.class).getMemberCount());
		    if (progress != null) progress.subTask(taskName + " [77%]");
		    list = modelEG.getInstances(EConstraint_relation_placement.class);
printDebug1("EConstraint_relation_placement: " + list.getMemberCount());
		    it.setEntities(list);
		    while (it.hasNext()) {
		    	countOld++;
//System.out.println("<09> countOld: " + countOld);
//		    	if (moveEG(modelNew, (EConstraint_relation_placement)it.next()) != null) countNew++;
					EConstraint_relation_placement itnext = (EConstraint_relation_placement)it.next();
		    	if (moveEG(modelNew, itnext) != null) {
		    		countNew++;
		    	} else {
		    		message.append("\nplacing failed: " + itnext);
		    	}	
		    }
printDebug(message, "<12>: " + modelEG.getInstances(EPage_reference_bundle.class).getMemberCount());
		    if (progress != null) progress.subTask(taskName + " [80%]");
		    list = modelEG.getInstances(EPage_relation.class);
printDebug1("EPage_relation: " + list.getMemberCount());
		    it.setEntities(list);
		    while (it.hasNext()) {
		    	countOld++;
//System.out.println("<10> countOld: " + countOld);
//		    	if (moveEG(modelNew, (EPage_relation)it.next()) != null) countNew++;
					EPage_relation itnext = (EPage_relation)it.next();
printDebug(message, "page_relation: " + itnext);
		    	if (moveEG(modelNew, itnext) != null) {
		    		countNew++;
		    	} else {
		    		message.append("\nplacing failed: " + itnext);
		    	}	
printDebug(message, "<12->>: " + modelEG.getInstances(EPage_reference_bundle.class).getMemberCount());
		    }
printDebug(message, "<13>: " + modelEG.getInstances(EPage_reference_bundle.class).getMemberCount());
		    if (progress != null) progress.subTask(taskName + " [82%]");
		    list = modelEG.getInstances(ERelation_bundle.class);
printDebug1("ERelation_bundle: " + list.getMemberCount());
		    it.setEntities(list);
		    while (it.hasNext()) {
		    	countOld++;
//System.out.println("<11> countOld: " + countOld);
//		    	if (moveEG(modelNew, (ERelation_bundle)it.next()) != null) countNew++;
					ERelation_bundle itnext = (ERelation_bundle)it.next();
		    	if (moveEG(modelNew, itnext) != null) {
		    		countNew++;
		    	} else {
		    		message.append("\nplacing failed: " + itnext);
		    	}	
		    }
printDebug(message, "<14>: " + modelEG.getInstances(EPage_reference_bundle.class).getMemberCount());
		    if (progress != null) progress.subTask(taskName + " [85%]");

printDebug(modelEG.getInstances(EPage_reference_bundle.class));

		    list = modelEG.getInstances(EPage_reference_bundle.class);
printDebug1("EPage_reference_bundle: " + list.getMemberCount());
printDebug(message, "<X> count: " + list.getMemberCount());
		    it.setEntities(list);
		    while (it.hasNext()) {
		    	countOld++;
//System.out.println("<12> countOld: " + countOld);
//		    	if (moveEG(modelNew, (EPage_reference_bundle)it.next()) != null) countNew++;
					EPage_reference_bundle itnext = (EPage_reference_bundle)it.next();
printDebug(message, "<12B> instance: " + itnext);
		    	if (moveEG(modelNew, itnext) != null) {
		    		countNew++;
		    	} else {
		    		message.append("\nplacing failed: " + itnext);
		    	}
		    } 
printDebug(message, "<15>: " + modelEG.getInstances(EPage_reference_bundle.class).getMemberCount());
		    if (progress != null) progress.subTask(taskName + " [97%]");
		    list = modelEG.getInstances(EProperty.class);
printDebug1("EProperty: " + list.getMemberCount());
		    it.setEntities(list);
		    while (it.hasNext()) {
		    	countOld++;
//System.out.println("<13> countOld: " + countOld);
//		    	if (moveEG(modelNew, (EProperty)it.next()) != null) countNew++;
					EProperty itnext = (EProperty)it.next();
		    	if (moveEG(modelNew, itnext) != null) {
		    		countNew++;
		    	} else {
		    		message.append("\nplacing failed: " + itnext);
		    	}	
		    }
printDebug1("==DONE== ");

printDebug(message, "<16>: " + modelEG.getInstances(EPage_reference_bundle.class).getMemberCount());
		    if (progress != null) progress.subTask(taskName + " [100%]");
//		    message.append("\t - updated (" + countNew + " of " + countOld + ")");
		    message.append("\n\t - updated (" + countNew + " of " + countOld + ")");
	    } else {
	    	modelNew.deleteSdaiModel();
	    	modelNew = null;
//	    	SdaieditPlugin.console("associated dictionary model not found - skipping");
	    	message.append(" - deleted (associated schema missing)");
//			System.out.println("associated dictionary model not found - skipping");
	    }
	    return modelNew;
	}
	
	
    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IAction#run()
     */
    public void run() {
        run(null);
    }
    
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run(IProgressMonitor progress) {
		int modelOld = 0;
		int modelNew = 0;
		
		SdaieditPlugin.console("\n\nMERGING DATA:");
//	    System.out.println("\n\nMERGING REPOSITORY:");
 		try {
//try {
//	repoEG.exportClearTextEncoding(new FileOutputStream("G:\\_BUGS\\ap_old.step"));
//} catch (FileNotFoundException e) {
	// TODO Auto-generated catch block
//	e.printStackTrace();
//} 			
//		  repoEG.exportClearTextEncoding("G:\\_BUGS\\repoEGstarting.pf"); - exception
 			
			ASdaiModel models = repoEG.getModels();
			int totalWork = models.getMemberCount();
		    if (progress != null) progress.beginTask("merging repository...", totalWork);

/*/ 			
			SdaiIterator msit0 = models.createIterator();
			while (msit0.next()) {
				SdaiModel model = models.getCurrentMember(msit0);
				try {
					if (model.getMode() == SdaiModel.NO_ACCESS) model.startReadOnlyAccess();
				} catch (SdaiException sex) {
					sex.printStackTrace();
				}
			}
			ASdaiModel models2 = repoDict.getModels();
			msit0 = models2.createIterator();
			while (msit0.next()) {
				SdaiModel model = models2.getCurrentMember(msit0);
				try {
					if (model.getMode() == SdaiModel.NO_ACCESS) model.startReadOnlyAccess();
				} catch (SdaiException sex) {
					sex.printStackTrace();
				}
			}
// END  */			
			
		    int worked = 0;
			SdaiIterator msit = models.createIterator();
//			Runtime runtime = Runtime.getRuntime();
//			long freeMemory = runtime.freeMemory();
//			long freeMemory0 = freeMemory;
//			long totalMemory = runtime.totalMemory();
			
flag_print_debug = false;

			while (msit.next() && !canceled) {
			    worked++;
				SdaiModel model = models.getCurrentMember(msit);
			    
				if (model.getUnderlyingSchemaString().equalsIgnoreCase("EXPRESS_G_SCHEMA")) {
					modelOld++;
					
					
if (model.getName().equalsIgnoreCase("FAILUREMODEEFFECTSCAUSESANALYSISSCHEMA_COMPLETE_SHORT_LAYOUT_EXPRESS_G_DATA")) {
	flag_print_debug = true;
} else {
	flag_print_debug = false;
}
				
					
					StringBuffer message = new StringBuffer();
					try {
//						message.append("Diagram (" + worked + "/" + totalWork + ") ");
//						SdaieditPlugin.console("MODEL (" + worked + "/" + totalWork + "): " + model);
//						System.out.println("MODEL (" + worked + "/" + totalWork + "): " + model);
						if (model.getMode() == SdaiModel.NO_ACCESS) model.startReadWriteAccess();
					    
//						if (model.getName().equalsIgnoreCase("EXPRESSCOMPILERREPO_PARTIAL_SHORT_LAYOUT_EXPRESS_G_DATA")) {
//							continue;
//						}
						
						AGraphics_diagram list = (AGraphics_diagram)model.getInstances(EGraphics_diagram.class);
						String taskName = null;
					    SdaiIterator it = list.createIterator();
					    if (it.next()) {
					    	EGraphics_diagram diagram = list.getCurrentMember(it);
					    	if (diagram.testComment(null)) taskName = diagram.getComment(null); 
					    }
					    if (progress != null) {
					    	taskName = taskName + "\t(" + worked + "/" + totalWork + ")";
					    	progress.subTask(taskName);
					    }
						if (copyEGData(model, progress, taskName, message) != null) modelNew++;

// DEBUG memory usage						
/*System.out.println("MEMORY: free=" + runtime.freeMemory() + " max=" + runtime.maxMemory() + " total=" + runtime.totalMemory());						
						if (totalMemory < runtime.totalMemory()) {
System.out.println("RESET MEMORY");							
							freeMemory = freeMemory0;
							instanceMap.reset();
							totalMemory = runtime.totalMemory();
							freeMemory0 = runtime.freeMemory();
						} else {
							freeMemory0 = runtime.freeMemory();
							if (freeMemory > freeMemory0) {
System.out.println("RESET MEMORY");							
								instanceMap.reset();
							}
						} /* Memory usage control */
					} catch (SdaiException sex) {
						SdaieditPlugin.log(sex);
//						System.err.println(model + "\t - skipping");
						message.append("\t - error " + sex.getLocalizedMessage() + " (see log for more details)");
//						SdaieditPlugin.console(sex.toString());
//						SdaieditPlugin.console(model + " - skipping");
					}
				    SdaieditPlugin.console(message.toString());
				    if (progress != null) {
				    	progress.worked(1);
				    	
				    	if (progress.isCanceled()) {
				    		canceled = true;

				    		/*
				    		Display.getDefault().syncExec(new Runnable() {
				    			public void run() {
				    				canceled = MessageDialog.openQuestion(null, "Data import", "Cancel data importing?");				    			
				    			}
				    		});
				    		
				    		if (!canceled) {
				    			progress.setCanceled(false);
				    		}*/
				    	}
				    }
			    
				} else {
//						SdaieditPlugin.log("skipping");
//						System.out.println("skipping");

//RR - let's put here some debugging printing -----------------
/*
					if (model.getUnderlyingSchemaString().equalsIgnoreCase("EXTENDED_DICTIONARY_SCHEMA")) {
						if (model.getMode() == SdaiModel.NO_ACCESS) model.startReadWriteAccess();
					  ADeclaration declarations = (ADeclaration)model.getInstances(EDeclaration.class);
					  SdaiIterator it_decl = declarations.createIterator();
					  while(it_decl.next()) {
				      EDeclaration decl = declarations.getCurrentMember(it_decl);
				      if (!decl.testDefinition(null)) {
				      	System.out.println("DEFINITION IS UNSET!!!: " + decl);
				      } else {
				      	Object def_obj = decl.getDefinition(null);
				      	if (!(def_obj instanceof CEntity)) {
					      	System.out.println("DEFINITION IS of INVALID TYPE: " + def_obj + ", declaration: " + decl);
				      	} else {
					      	System.out.println("OK - definition: " + def_obj + ", declaration: " + decl);
				      	
				      	}
				      }
  	
					  }

					} else {
						// what else could be?
					}

*/				

//RR - end of debugging stuff --------------------------
				}
			}
		    if (progress != null) progress.done();
		    if (progress != null && progress.isCanceled()) {
				SdaieditPlugin.console("\nEND OF MERGING\nCanceled by user");
		    } else {
				SdaieditPlugin.console("\nEND OF MERGING\nUpdated " + modelNew + " of " + modelOld + " diagrams");
		    }
		    
//repoDict.exportClearTextEncoding(new FileOutputStream("/home/monte/eclipse/runtime-workspace/test2/ap_new.step")); 			
		} catch (SdaiException sex) {
			SdaieditPlugin.log(sex);
		    if (progress != null) progress.setCanceled(true);
		    SdaieditPlugin.console("DATA MERGING FAILED:\n" + sex.toString());
		} 
//catch (IOException e) { e.printStackTrace();}
//		System.out.println("\nEND OF REPOSITORY MERGING\n");
	}
	
	/** 
	 * @throws Throwable
	 * @see java.lang.Object#finalize()
	 */
	
	protected void finalize() throws Throwable {
    	if (instanceMap != null) instanceMap.reset();
    	instanceMap = null;
		super.finalize();
	}
}
