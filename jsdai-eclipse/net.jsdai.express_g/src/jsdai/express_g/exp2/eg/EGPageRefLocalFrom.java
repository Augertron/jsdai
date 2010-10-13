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

package jsdai.express_g.exp2.eg;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import jsdai.SExpress_g_schema.ARelation_placement;
import jsdai.SExpress_g_schema.EGraphics_diagram;
import jsdai.SExpress_g_schema.EPage;
import jsdai.SExpress_g_schema.EPage_reference_bundle;
import jsdai.express_g.editors.RepositoryHandler;
import jsdai.express_g.exp2.EGToolKit;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.event.PageChangeEvent;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;

/**
 * @author Mantas Balnys
 *
 */
public class EGPageRefLocalFrom extends EGPageRefLocal {
	protected Vector linkedRef = new Vector(5);

	/**
	 * @param prop
	 * @param referenced
	 */
	public EGPageRefLocalFrom(PropertySharing prop, AbstractEGBox referenced) {
		super(prop, referenced);
	}

	/**
	 * 
	 */
	protected EGPageRefLocalFrom() {
		super();
	}

	public String getText() {
		// how to get page entity in express_g_schema from here?
/*
    EEntity definition_placement0 = getDefinitionPlacement();
    EPage_reference_bundle page_reference_bundle0 = null;
    EPage a_page0 = null;
    String a_comment0 = "_NO_COMMENT_";
    int a_page_number0 = -1;
		EGraphics_diagram diagram0 = null;
//	    EData_type_extension referenced0 = null;
        EEntity referenced0 = null;
		ARelation_placement relation0 = null;
		EPage_reference_bundle link0 = null;
    
	try {	
    if (definition_placement0 instanceof EPage_reference_bundle) {
    	page_reference_bundle0 = (EPage_reference_bundle)definition_placement0;
			if (page_reference_bundle0.testPresented_on(null)) {
				a_page0 = page_reference_bundle0.getPresented_on(null);
				a_page_number0 = a_page0.getPage_number(null);
				if (a_page0.testComment(null)) {
					a_comment0 = a_page0.getComment(null);
				}
				diagram0 = a_page0.getDiagram(null);
			}
    	referenced0 = page_reference_bundle0.getReferenced(null);
    	relation0 = page_reference_bundle0.getRelation(null);
    	if (page_reference_bundle0.testLink(null)) {
    		link0 = page_reference_bundle0.getLink(null);
    	}
    } 
	} catch (SdaiException e) {
	}
	
*/	
//System.out.println("-----------------------START");
//System.out.println("<definition_placement>: " + definition_placement0);
//System.out.println("<page_reference_bundle>: " + page_reference_bundle0);
//System.out.println("<page>: " + a_page0);
//System.out.println("<page number>: " + a_page_number0);
//System.out.println("<page comment>: " + a_comment0);
//System.out.println("<diagram>: " + diagram0);
//System.out.println("<referenced>: " + referenced0);
//System.out.println("<relation>: " + relation0);
//System.out.println("<link>: " + link0);
//System.out.println("<PROP>: " + prop());
//System.out.println("<PROP-Name>: " + prop().getName());
//System.out.println("<PROP-NameEG>: " + prop().getNameEG());
//System.out.println("<PROP-PageRenumber>: " + prop().getPageRenumber());
//System.out.println("========================END");
//System.out.println("<PROP-handler>: " + prop().handler());
//System.out.println("<PROP-RepositoryHandler>: " + prop().getRepositoryHandler());
//System.out.println("<PROP-SelectionHandler>: " + prop().getSelectionHandler());
//RepositoryHandler rh00 = prop().getRepositoryHandler();
//try {
//System.out.println("model: " + rh00.getRepository().findSdaiModel(prop().getNameEG().toUpperCase() + "_EXPRESS_G_DATA").getName());;
//} catch (SdaiException e00) {
//	e00.printStackTrace();
//}
//Throwable thrw = new Throwable();
//thrw.printStackTrace();
//System.out.println("<0XO><09>getText");

//original		String text = EGToolKit.renumber(prop(), getPage()) + ", " + getReferencedID() + " (";
		String text = null;
		String text1 = "" + EGToolKit.renumber(prop(), getPage());
		String text2 = ", " + getReferencedID();
		String text3 = " (";
		if (linkedRef != null) {
			Iterator iter = linkedRef.iterator();
			while (iter.hasNext()) {
				EGPageRefLocalTo pgt = (EGPageRefLocalTo)iter.next();
				Iterator wit = pgt.getWires().iterator();
				while (wit.hasNext()) {

//System.out.println("<definition>: " + getDefinition());
//System.out.println("<definitionPlacement>: " + getDefinitionPlacement());

          Wire a_wire = (Wire)wit.next();
//System.out.println("<wire>: " + a_wire);
          AbstractEGRelation aegr = a_wire.getRelation();
//System.out.println("<AbstractEGRelation>: " + aegr);
					AbstractEGBox aegb = aegr.getParent();
//System.out.println("<AbstractEGBox>: " + aegb);
					int page_nr = aegb.getPage();
//System.out.println("<page>: " + page_nr);
//original			text +=  EGToolKit.convertPageNrToString(EGToolKit.renumber(prop(),page_nr));
					text3 +=  EGToolKit.convertPageNrToString(EGToolKit.renumber(prop(),page_nr));

//							EGToolKit.renumber(prop(), 
//							a_wire.getRelation().getParent().getPage()));

//					text += EGToolKit.convertPageNrToString(EGToolKit.renumber(prop(), 
//							((Wire)wit.next()).getRelation().getParent().getPage()));
//original					if (wit.hasNext()) text += ", ";
					if (wit.hasNext()) text3 += ", ";
				}
//original				if (iter.hasNext()) text += ", ";
				if (iter.hasNext()) text3 += ", ";
			}
		}
//original		text += ")";
		text3 += ")";

		text = text1 + text2 + text3;
		return text;
		//return "X2";
	}
	
	public void addLinkedRef(EGPageRefLocalTo linked) {
		int type = ((Wire)wires.firstElement()).getRelation().getType();
		linkedRef.add(linked);
		linked.setLinkedRef(this);
		updateWrapper();
		linked.addPageChangeListener(this);
		Vector wires = linked.getWires();
		if (wires.size() > 0) {
			Iterator wit = wires.iterator();
			while (wit.hasNext()) {
				Wire wire = (Wire)wit.next();
				if (wire.isAttribute()) 
					wire.getRelation().setType(type);
			}
		}
	}
	
	public void removeLinkedRef(EGPageRefLocalTo linked) {
		linkedRef.remove(linked);
		linked.removePageChangeListener(this);
		updateWrapper();
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.IXMLDefinition#getXMLDefinition(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String getXMLDefinition(Point startat, String schema_name, String schema_ext, String doc_file) {
		String text = null;
		if (linkedRef.size() == 1) {
			int pg = ((EGPageRefLocalTo)linkedRef.iterator().next()).getPage();
			if (pg > 0) {
				Rectangle bounds = getBounds();
				bounds.x -= startat.x;
				bounds.y -= startat.y;
				int dd = bounds.height / 3;
				int x1 = bounds.x;
				int x2 = bounds.x + dd;
				int x3 = bounds.x + bounds.width - dd;
				int x4 = bounds.x + bounds.width;
				int y1 = bounds.y;
				int y2 = bounds.y + bounds.height / 2;
				int y3 = bounds.y + bounds.height;
				text = "<img.area shape=\"poly\" coords=\"" 
					+ x1 + "," + y2 + "," + x2 + "," + y3 + "," + x3 + "," + y3 + "," + x4 + "," + y2 + "," 
						+ x3 + "," + y1 + "," + x2 + "," + y1 + "," + x1 + "," + y2  
					+ "\" href=\"./" + schema_ext.toLowerCase() + "expg" + pg + ".xml"
					+ "\" />";
			}
		}
		return text;
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.AbstractEGObject#eliminate()
	 */
	public void eliminate() {
		Iterator iter = linkedRef.iterator();
		while (iter.hasNext())
			((EGPageRefLocalTo)iter.next()).removePageChangeListener(this);
		super.eliminate();
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.event.PageListener#pageChanged(jsdai.express_g.exp2.ui.event.PageChangeEvent)
	 */
	public void pageChanged(PageChangeEvent e) {
		Object src = e.getSource();
		if (src instanceof EGPageRefLocalTo) {
			EGPageRefLocalTo ref = (EGPageRefLocalTo)src;
			eliminatePageReference(ref);
		}
//System.out.println("<0XO><13>pageChanged-src: " + src + ", event: " + e);
		super.pageChanged(e);
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.Paging#setPage(int)
	 */
	public void setPage(int pgNr) {
		super.setPage(pgNr);
		Iterator iter = ((Collection)linkedRef.clone()).iterator();
		while (iter.hasNext()) 
			eliminatePageReference((EGPageRefLocalTo)iter.next());
		updateWrapper();
	}

	/**
	 * removes page reference from drawable list, removes this if last reference removed
	 * @param ref
	 */
	public void eliminatePageReference(EGPageRefLocalTo ref) {
		if ((linkedRef.contains(ref))&&(ref.getPage() == getPage())) { // removing reference
				Iterator iter = ref.getWires().iterator();
				while (iter.hasNext()) { // changing wire child to referenced box
					Wire wire = (Wire)iter.next();
					if (!wire.isAttribute()) {
						AbstractEGRelation rel = wire.getRelation();
						rel.removeChild(ref);
						rel.addChild(referenced);
						linkedRef.remove(ref);
						ref.removePageChangeListener(this);
					}
				}
				if (ref.getWires().isEmpty()) { // if page connector has no more links - delete it
					prop().handler().drawableRemove(ref);
					ref.eliminate();
				}
				if (linkedRef.isEmpty()) { // if there are no more linking references - delete this
					deletePageReference();
				}
		}
	}

	private void deletePageReference() {
		Iterator iter = getWires().iterator();
		while (iter.hasNext()) {
			Wire wire = (Wire)iter.next();
			AbstractEGRelation rel = wire.getRelation();
			prop().handler().drawableRemove(rel);
			rel.setParent(null);
			rel.removeChild(referenced);
			rel.eliminate();
		}
		prop().handler().drawableRemove(this);
		eliminate();
	}
	
	public boolean addReferencedRelation(AbstractEGRelation relation) {
		AbstractEGBox box = relation.getParent();
		boolean ok = false;
		if ((box instanceof EGPageRefLocalFrom)&&(box != this)) {
			// add references
			EGPageRefLocalFrom ref = (EGPageRefLocalFrom)box;
			linkedRef.addAll(ref.linkedRef);
			Iterator iter = ref.linkedRef.iterator();
			while (iter.hasNext()) {
				((EGPageRefLocalTo)iter.next()).addPageChangeListener(this);
			}
			updateWrapper();
			// remove other pageFrom from drawable
			ref.deletePageReference();
			ok = true;
		}
		return ok;
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.SDAIdicSchema#updateModel(jsdai.lang.SdaiModel, jsdai.lang.SdaiModel)
	 */
	public void updateModel(SdaiModel modelDict, SdaiModel modelEG)	throws SdaiException {
		super.updateModel(modelDict, modelEG);
		EPage_reference_bundle bundle = (EPage_reference_bundle)getDefinition();
		Iterator iter = linkedRef.iterator();
		while (iter.hasNext()) {
			EGPageRefLocalTo pgt = (EGPageRefLocalTo)iter.next();
			EPage_reference_bundle page = (EPage_reference_bundle)pgt.getDefinitionPlacement(modelDict, modelEG);
			page.setLink(null, bundle);
		}
	}
	
	/**
	 * linked pageTo's iterator
	 * @return
	 */
	public Iterator getLinkedIterator() {
		return linkedRef.iterator();
	}
}
