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

import jsdai.SExpress_g_schema.EPage_reference_bundle;
import jsdai.express_g.exp2.EGToolKit;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.event.PageChangeEvent;
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
		String text = EGToolKit.renumber(prop(), getPage()) + ", " + getReferencedID() + " (";
		if (linkedRef != null) {
			Iterator iter = linkedRef.iterator();
			while (iter.hasNext()) {
				EGPageRefLocalTo pgt = (EGPageRefLocalTo)iter.next();
				Iterator wit = pgt.getWires().iterator();
				while (wit.hasNext()) {
					text += EGToolKit.convertPageNrToString(EGToolKit.renumber(prop(), 
							((Wire)wit.next()).getRelation().getParent().getPage()));
					if (wit.hasNext()) text += ", ";
				}
				if (iter.hasNext()) text += ", ";
			}
		}
		text += ")";
	    return text;
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
