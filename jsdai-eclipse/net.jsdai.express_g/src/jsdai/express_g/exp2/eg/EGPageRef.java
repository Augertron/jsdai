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

import java.util.Iterator;

import org.eclipse.swt.graphics.Point;

import jsdai.SExpress_g_schema.ARelation_placement;
import jsdai.SExpress_g_schema.ELocation;
import jsdai.SExpress_g_schema.EPage_reference_bundle;
import jsdai.SExpress_g_schema.ESize;
import jsdai.express_g.SdaieditPlugin;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;

/**
 * @author Mantas Balnys
 *
 */
public abstract class EGPageRef extends AbstractEGBox {
	protected AbstractEGBox referenced = null;
	
	/**
	 * @param prop
	 */
	public EGPageRef(PropertySharing prop, AbstractEGBox referenced) {
		super(prop);
//System.err.println("NEW PAGE REF " + this + " FOR " + referenced);		
		setReferenced(referenced);
	}

	protected EGPageRef() {
//System.err.println("NEW PAGE REF");		
	}
	
	/**
	 * @return Returns the referenced.
	 */
	public AbstractEGBox getReferenced() {
		return referenced;
	}
	
	public void setReferenced(AbstractEGBox referenced) {
		this.referenced = referenced;
		updateWrapper();
	}

	public void updateWrapper() {
//System.err.println("\tUPDATE WRAPPER:" + this);
//System.err.println(getText());
  		if (wrapper != null) wrapper.setText(getText());
	}
	
	protected void setDataFrom(EGPageRef other) {
		setProp(other.prop());
		setBounds(other.getBounds());
		setVisible(other.getVisible());
		setConcreteValues(other.getConcreteValues());
		setLabelNew(other.isLabelNew());
		setPage(other.getPage());
		setReferenced(other.referenced);
	}
	
	/**
	 * finds new instance of EGPageRef for specified relation (relation must have link to this box)
	 * returned EGPageRef (can be the same) contains only one relation
	 * @param relation
	 * @return
	 */
	public EGPageRef getOneFor(AbstractEGRelation relation) {
		if (getWires().size() > 1) {
			EGPageRef ref = this;
			if (relation != null) {
				try {
					ref = (EGPageRef)getClass().newInstance();
					ref.setDataFrom(this);
					Wire wire = getWire(relation, relation.getParent() == relation.getChild());
					if (wire.isAttribute()) {
						relation.setParent(ref);
					} else {
						relation.removeChild(this);
						relation.addChild(ref);
					}
				} catch (InstantiationException e) {
					SdaieditPlugin.log(e);
				} catch (IllegalAccessException e) {
					SdaieditPlugin.log(e);
				}
			} else {
				SdaieditPlugin.log(new Exception("relation == null"));
			}
			return ref;
		} else {
			return this;
		}
	}
	
	abstract protected boolean can_be_replaced_by_this(AbstractEGBox box, int rel_type);
	
	abstract public Object getBundleReferenceKey();
	
	public boolean addReferencedRelation(AbstractEGRelation relation) {
		AbstractEGBox box = relation.getParent();
		boolean ok = false;
		if (can_be_replaced_by_this(box, relation.getType())) {
			relation.setParent(this);
			ok = true;
		} else {
			Iterator childs = relation.getChilds().iterator();
			while (!ok && (childs.hasNext())) {
				box = (AbstractEGBox)childs.next();
				if (can_be_replaced_by_this(box, relation.getType())) {
					relation.removeChild(box);
					relation.addChild(this);
					ok = true;
				}
			}
		}
//		if (ok) setVisible(VISIBLE_TRUE);
		return ok;
	}
	
	public boolean addReferencedBox(EGPageRef other) {
		boolean ok = false;
		if ((other != this)&&(other.getBundleReferenceKey().equals(getBundleReferenceKey()))) {
			Iterator wit = other.getWires().iterator();
			while (wit.hasNext()) {
				addReferencedRelation(((Wire)wit.next()).getRelation());
			}
			ok = true;
		}
		return ok;
	}
	
	protected EEntity getReferencedDefinition(SdaiModel modelDict, SdaiModel modelEG) throws SdaiException {
		return referenced.getDefinition(modelDict, modelEG);
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.SDAIdicSchema#updateModel(jsdai.lang.SdaiModel, jsdai.lang.SdaiModel)
	 */
	public void updateModel(SdaiModel modelDict, SdaiModel modelEG)	throws SdaiException {
	        EPage_reference_bundle entity_placement = (EPage_reference_bundle)modelEG.createEntityInstance(EPage_reference_bundle.class);
	        setDefinitionPlacement(entity_placement);
	        setDefinition(entity_placement);

	        Point cloc = getLocation();
	        if (cloc.x != 0 || cloc.y != 0) {
	        	ELocation loc = (ELocation)modelEG.createEntityInstance(ELocation.class);
	        	loc.setX(null, cloc.x);
	        	loc.setY(null, cloc.y);
	        	entity_placement.setObject_location(null, loc);
	        }

	        Point csize = getSize();
	        if (!isLabelNew() && (csize.x != 0 || csize.y != 0)) {
	        	ESize size = (ESize)modelEG.createEntityInstance(ESize.class);
	        	size.setWidth(null, csize.x);
	        	size.setHeight(null, csize.y);
	        	entity_placement.setObject_size(null, size);
	        }

	        if (getVisible() != VISIBLE_UNSET)
	      	  entity_placement.setVisible(null, isVisible());

	        entity_placement.setReferenced(null, getReferencedDefinition(modelDict, modelEG));
	        ARelation_placement rels = entity_placement.createRelation(null);
	        Iterator wit = getWires().iterator();
	        while (wit.hasNext()) {
	        	AbstractEGRelation rel = ((Wire)wit.next()).getRelation();
	        	EEntity reldef = rel.getDefinitionPlacement(modelDict, modelEG);
	        	rels.addUnordered(reldef);
	        }
	        
	        /** TODO Colors disabled
	        EColor color = (EColor)modelEG.createEntityInstance(EColor.class);
	        color.setRed(null, simpleSchema.background.getRed());
	        color.setGreen(null, simpleSchema.background.getGreen());
	        color.setBlue(null, simpleSchema.background.getBlue());
	        entity_placement.setRepresentation_color(null, color);
	        */
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.SDAIdicSchema#getDefinition(jsdai.lang.SdaiModel, jsdai.lang.SdaiModel)
	 */
	public EEntity getDefinition(SdaiModel modelDict, SdaiModel modelEG) throws SdaiException {
		validDict = true;
	    return referenced.getDefinition(modelDict, modelEG);
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.AbstractEGObject#canBeInvisible()
	 */
	public boolean canBeInvisible() {
		return referenced.canBeInvisible();
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.AbstractEGObject#setConcreteValues(java.lang.String[])
	 */
	public void setConcreteValues(String[] values) {
		super.setConcreteValues(values);
		updateWrapper();
	}
	
	public AbstractEGRelation getFirstRelation() {
		if (wires.isEmpty()) return null;
		else return ((Wire)wires.firstElement()).getRelation();
	}
	
}
