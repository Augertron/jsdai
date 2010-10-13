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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.operation.IRunnableWithProgress;

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
import jsdai.SExtended_dictionary_schema.EAttribute;
import jsdai.SExtended_dictionary_schema.EData_type;
import jsdai.SExtended_dictionary_schema.EDefined_type;
import jsdai.SExtended_dictionary_schema.ESchema_definition;
import jsdai.SExtended_dictionary_schema.ESelect_type;
import jsdai.SExtended_dictionary_schema.ESub_supertype_constraint;
import jsdai.express_g.SdaieditPlugin;
import jsdai.lang.AEntity;
import jsdai.lang.ASdaiModel;
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
public class DiagramInserter extends Action implements Runnable, IRunnableWithProgress {
	private SdaiRepository repoTarget;
	private SdaiRepository repoSource;
	private String diagramName;
	
	private InstanceMap instanceMap;

	private boolean canceled = false;

	public DiagramInserter(SdaiRepository sourceEXG, SdaiRepository targetEXG, String diagram_name) throws SdaiException {
		repoSource = sourceEXG;
		repoTarget = targetEXG;
		diagramName = diagram_name.toUpperCase() + "_EXPRESS_G_DATA";
		
//		instanceMap = new InstanceMap(repoSource);
		instanceMap = new InstanceMap(repoTarget);
		if (!repoSource.isActive()) repoSource.openRepository();
		if (!repoTarget.isActive()) repoTarget.openRepository();
	}
	// move entity and all references in entity
	
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
				if (old_represented.findEntityInstanceSdaiModel().getUnderlyingSchemaString().equalsIgnoreCase("EXPRESS_G_SCHEMA"))
					represented = moveEGselect(model, old_represented);
				else
					represented = instanceMap.get(old_represented);
					
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
		if (testModel(model, place)) return place;
		EPage_relation relNew = null;
		try {
			EEntity child = null;
			if (place.testChild(null)) child = instanceMap.get(place.getChild(null));
			if (child != null) {
				relNew = (EPage_relation)moveEG(model, (ERelation_placement)place); // call super
				if (relNew.testParent(null)) {
				    if (relNew.getParent(null) instanceof EPage_reference_bundle) 
				        moveEG(model, (EPage_reference_bundle)relNew.getParent(null));
				    else if (relNew.getParent(null) instanceof EPage_reference_from) 
				        moveEG(model, (EPage_reference_from)relNew.getParent(null));
				}
				relNew.setChild(null, child);
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
	protected SdaiModel copyEGData(SdaiModel modelSource, IProgressMonitor progress, String taskName, StringBuffer message) throws SdaiException {
//		SdaieditPlugin.console("copying");
		int countOld = 0;
		int countNew = 0;

//System.out.println("<01>");
		
//		System.out.println("copying");

 

//String target_model_name = "EG_THIRD_MY_DIAGRAM_EXPRESS_G_DATA";
		String target_model_name = modelSource.getName();

		deleteTargetModelIfPresent(target_model_name);
//		SdaiModel modelTarget = repoTarget.createSdaiModel(modelSource.getName(), SExpress_g_schema.class);
		SdaiModel modelTarget = repoTarget.createSdaiModel(target_model_name, SExpress_g_schema.class);
		modelTarget.startReadWriteAccess();
		AEntity list;
	    IndependentSdaiIterator it = new IndependentSdaiIterator();

	    EGraphics_diagram diagram = null;
	    list = modelSource.getInstances(EGraphics_diagram.class);
	    it.setEntities(list);
	    if (it.hasNext()) {
	    	diagram = (EGraphics_diagram)it.next();  // old
	    	if (diagram.testComment(null)) 
	    		message.append(diagram.getComment(null));
	    	else
	    		message.append("name missing!");
	    	diagram = moveEG(modelTarget, diagram);
	    }

//System.out.println("Diagram: " + diagram);

	    if (diagram != null) {
		    instanceMap.SINGLE_LOADING = !diagram.testDic_schema(null);
//System.out.println("SINGLE MODE:" + instanceMap.SINGLE_LOADING);		    
		    if (progress != null) progress.subTask(taskName + " [0%]");
		    list = modelSource.getInstances(EPage_reference_to.class);
		    it.setEntities(list);
		    while (it.hasNext()) {
		    	countOld++;
		    	if (moveEG(modelTarget, (EPage_reference_to)it.next()) != null) countNew++;
		    }
		    if (progress != null) progress.subTask(taskName + " [3%]");
		    list = modelSource.getInstances(EPage_reference_from.class);
		    it.setEntities(list);
		    while (it.hasNext()) {
		    	countOld++;
		    	if (moveEG(modelTarget, (EPage_reference_from)it.next()) != null) countNew++;
		    }
		    if (progress != null) progress.subTask(taskName + " [30%]");
		    list = modelSource.getInstances(EData_type_placement.class);
		    it.setEntities(list);
		    while (it.hasNext()) {
		    	countOld++;
		    	if (moveEG(modelTarget, (EData_type_placement)it.next()) != null) countNew++;
		    }
		    if (progress != null) progress.subTask(taskName + " [40%]");
		    list = modelSource.getInstances(ESchema_relation_placement.class);
		    it.setEntities(list);
		    while (it.hasNext()) {
		    	countOld++;
		    	if (moveEG(modelTarget, (ESchema_relation_placement)it.next()) != null) countNew++;
		    }
		    if (progress != null) progress.subTask(taskName + " [45%]");
		    list = modelSource.getInstances(EAttribute_placement.class);
		    it.setEntities(list);
		    while (it.hasNext()) {
		    	countOld++;
		    	if (moveEG(modelTarget, (EAttribute_placement)it.next()) != null) countNew++;
		    }
		    if (progress != null) progress.subTask(taskName + " [60%]");
		    list = modelSource.getInstances(EDefined_relation_placement.class);
		    it.setEntities(list);
		    while (it.hasNext()) {
		    	countOld++;
		    	if (moveEG(modelTarget, (EDefined_relation_placement)it.next()) != null) countNew++;
		    }
		    if (progress != null) progress.subTask(taskName + " [65%]");
		    list = modelSource.getInstances(ESelect_relation_placement.class);
		    it.setEntities(list);
		    while (it.hasNext()) {
		    	countOld++;
		    	if (moveEG(modelTarget, (ESelect_relation_placement)it.next()) != null) countNew++;
		    }
		    if (progress != null) progress.subTask(taskName + " [70%]");
		    list = modelSource.getInstances(ESupertype_placement.class);
		    it.setEntities(list);
		    while (it.hasNext()) {
		    	countOld++;
		    	if (moveEG(modelTarget, (ESupertype_placement)it.next()) != null) countNew++;
		    }
		    if (progress != null) progress.subTask(taskName + " [77%]");
		    list = modelSource.getInstances(EConstraint_relation_placement.class);
		    it.setEntities(list);
		    while (it.hasNext()) {
		    	countOld++;
		    	if (moveEG(modelTarget, (EConstraint_relation_placement)it.next()) != null) countNew++;
		    }
		    if (progress != null) progress.subTask(taskName + " [80%]");
		    list = modelSource.getInstances(EPage_relation.class);
		    it.setEntities(list);
		    while (it.hasNext()) {
		    	countOld++;
		    	if (moveEG(modelTarget, (EPage_relation)it.next()) != null) countNew++;
		    }
		    if (progress != null) progress.subTask(taskName + " [82%]");
		    list = modelSource.getInstances(ERelation_bundle.class);
		    it.setEntities(list);
		    while (it.hasNext()) {
		    	countOld++;
		    	if (moveEG(modelTarget, (ERelation_bundle)it.next()) != null) countNew++;
		    }
		    if (progress != null) progress.subTask(taskName + " [85%]");
		    list = modelSource.getInstances(EPage_reference_bundle.class);
		    it.setEntities(list);
		    while (it.hasNext()) {
		    	countOld++;
		    	if (moveEG(modelTarget, (EPage_reference_bundle)it.next()) != null) countNew++;
		    } 
		    if (progress != null) progress.subTask(taskName + " [97%]");
		    list = modelSource.getInstances(EProperty.class);
		    it.setEntities(list);
		    while (it.hasNext()) {
		    	countOld++;
		    	if (moveEG(modelTarget, (EProperty)it.next()) != null) countNew++;
		    }
		    if (progress != null) progress.subTask(taskName + " [100%]");
		    message.append("\t - updated (" + countNew + " of " + countOld + ")");
	    } else {
	    	modelTarget.deleteSdaiModel();
	    	modelTarget = null;
//	    	SdaieditPlugin.console("associated dictionary model not found - skipping");
	    	message.append(" - deleted (associated schema missing)");
//			System.out.println("associated dictionary model not found - skipping");
	    }
	    return modelTarget;
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

//System.out.println("<INSERTER-RUN-01> ");

		int modelOld = 1;
		int modelNew = 0;
		
		SdaieditPlugin.console("\n\nInserting a Diagram:");
//	    System.out.println("\n\nMERGING REPOSITORY:");
 		try {
//repoEG.exportClearTextEncoding(new FileOutputStream("/home/monte/eclipse/runtime-workspace/test2/ap_old.step")); 			
 			
			ASdaiModel models = repoSource.getModels();
			int totalWork = models.getMemberCount();
		    if (progress != null) progress.beginTask("inserting diagram...", totalWork);

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
			

			while (msit.next() && !canceled) {
			    worked++;
				SdaiModel model = models.getCurrentMember(msit);

//System.out.println("<INSERTER-RUN-02> model: " + model.getName());
//System.out.println("<INSERTER-RUN-02> underlying schema: " + model.getUnderlyingSchemaString());

			    
				if (model.getUnderlyingSchemaString().equalsIgnoreCase("EXPRESS_G_SCHEMA")) {
					// modelOld++;
					
					             
					// diagramName = "EG_FIRST_MY_DIAGRAM_EXPRESS_G_DATA";
					
					
					
					if (!model.getName().equalsIgnoreCase(diagramName)) continue;

//System.out.println("<INSERTER-RUN-03> NEEDED SOURCE MODEL FOUND: " + model.getName());

					
					StringBuffer message = new StringBuffer();
					try {
//						message.append("Diagram (" + worked + "/" + totalWork + ") ");
//						SdaieditPlugin.console("MODEL (" + worked + "/" + totalWork + "): " + model);
//						System.out.println("MODEL (" + worked + "/" + totalWork + "): " + model);
						if (model.getMode() == SdaiModel.NO_ACCESS) model.startReadWriteAccess();

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
//						boolean result = copyEGData(model, progress, taskName, message) != null);
            break;

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
				}
			}
		    if (progress != null) progress.done();
		    if (progress != null && progress.isCanceled()) {
					SdaieditPlugin.console("\nEND OF Diagram Insertion\nCanceled by user");
		    } else {
					SdaieditPlugin.console("\nEND OF Diagram Insertion\nUpdated " + modelNew + " of " + modelOld + " diagrams");
		    }
		    
//repoDict.exportClearTextEncoding(new FileOutputStream("/home/monte/eclipse/runtime-workspace/test2/ap_new.step")); 			
		} catch (SdaiException sex) {
			SdaieditPlugin.log(sex);
		    if (progress != null) progress.setCanceled(true);
		    SdaieditPlugin.console("DIAGRAM INSERTION FAILED:\n" + sex.toString());
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



  boolean deleteTargetModelIfPresent(String target_model_name) throws SdaiException {
        boolean result = false;
		ASdaiModel models = repoTarget.getModels();
		SdaiIterator msit = models.createIterator();
		while (msit.next()) {
			SdaiModel model = models.getCurrentMember(msit);
			if (model.getUnderlyingSchemaString().equalsIgnoreCase("EXPRESS_G_SCHEMA")) {
				if (model.getName().equalsIgnoreCase(target_model_name)) {
					// delete this model
					model.deleteSdaiModel();
					result = true;
					break;
				} // if
			} // if
		} // while
		return result;
  }


/*
  void DeleteDiagram() {

					RepositoryHandler rh = handler;
					ModelHandler mhEG = handler.getModelHandler(modelName);
					System.out.println("<ID>model handler : " + mhEG);

          // let's try to delete the diagram
          
					String schemaName = null;
					try {
						ESchema_definition schemaDef = mhEG.getSchema_definition();
						if (schemaDef != null)
							schemaName = schemaDef.getName(null);
					} catch (SdaiException sex) {
						SdaieditPlugin.log(sex);
						SdaieditPlugin.console(sex.toString());
					}
					RepositoryChanger changerR = null;
					RepositoryChanger changerW = new RepositoryChanger(new String[]{mhEG.getName()}, "Deleting diagram");
					String msg = null;
					if (schemaName != null) {
						changerR = new RepositoryChanger(new String[]{schemaName}, "Deleting diagram");
						msg = rh.startROChanger(changerR);
					}


					if (msg != null) {
						// MessageDialog.openWarning(getShell(), "Delete Diagram Action", msg);
						MessageDialog.openWarning(null, "Delete Diagram Action", msg);
					} else {
						msg = rh.startRWChanger(changerW);
						if (msg != null) {
							// MessageDialog.openWarning(getShell(), "Delete Diagram Action", msg);
							MessageDialog.openWarning(null, "Delete Diagram Action", msg);
						} else {
							rh.deleteModel(mhEG.getName());
							try {
								rh.update();
							} catch (SdaiException sex) {
								SdaieditPlugin.log(sex);
								SdaieditPlugin.console(sex.toString());
							}
//							refresh();
//							if (schemaName == null)
//								selectInView(rh);
//							else
//								selectInView(rh.getModelHandler(schemaName));
						}
						changerW.done();
						rh.endRWChanger(changerW);
					}
					if (changerR != null) {
						changerR.done();
						rh.endROChanger(changerR);
					}

	} // deleteDiagram
*/

}
