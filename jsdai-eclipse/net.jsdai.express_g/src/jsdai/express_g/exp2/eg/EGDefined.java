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
import java.util.Vector;

import jsdai.SExtended_dictionary_schema.EDefined_type;
import jsdai.express_g.exp2.ColorSchema;
import jsdai.express_g.exp2.Selectable;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * @(#) EGDefined.java
 */

public class EGDefined extends AbstractEGBox implements IDefined_type {

	private static int DEFINED_TYPE_COUNTING = 1;

	// RR
	protected boolean fRestricted = false;
	private boolean showRestricted = true;

	public EGDefined(PropertySharing prop) {
		super(prop);
		simpleSchema.lineStyle = selectedSchema.lineStyle = ColorSchema.DASHED_LINE;
		setName("Defined_type_" + (DEFINED_TYPE_COUNTING++));
	}

	public EGDefined(PropertySharing prop, String name, Rectangle bounds) {
		this(prop);
		setBounds(bounds);
		setName(name);
	}

	public EGDefined(PropertySharing prop, String name, Point location) {
		this(prop);
		setLocation(location);
		setName(name);
	}

	public EGDefined(PropertySharing prop, String name, Point location, boolean fRestricted) {
		this(prop);
		setLocation(location);
		setName(name);
		setRestricted(fRestricted);
	}




	// RR - when the defined type is restricted by a where clause
	public boolean isRestricted() {
		return fRestricted;
	}

	// RR
	public void setRestricted(boolean fRestricted) {
		this.fRestricted = fRestricted;
		validDict = false;
		wrapper.setText(getText());
		fireLabelChanged();
	}

	// RR
	public String getText() {
		String text = super.getText();
		if (isRestricted())
			text = showRestricted ? "*" + text : text; // defined type, restricted by a where clause
		
		return text;
		
	}


	public void updateModel(SdaiModel modelDict, SdaiModel modelEG)	throws SdaiException {
		if ((modelDict != null) && !validDict) {
			EDefined_type entity = null;
			EEntity ent = getDefinition();
			if (ent instanceof EDefined_type) {
				entity = (EDefined_type)ent;
			} else {
				entity = (EDefined_type)modelDict.createEntityInstance(EDefined_type.class);
			}
			entity.setName(null, getName());
			if (!"".equals(shortName)) entity.setShort_name(null, shortName);
			setDefinition(entity);
		}
		super.updateModel(modelDict, modelEG);
	}

	public Selectable selectAsFirst(int type) {
		boolean out = true;
		if (type == AbstractEGRelation.TYPE_AGREGATION) {
			Vector wires = getWires();
			Iterator iter = wires.iterator();
			while ((iter.hasNext()) && (out))
				out = !((Wire)iter.next()).isAttribute();
		} else
			out = false;
		return out ? this : null;
	}

	public Selectable selectSecond(int type, Selectable second) {
		boolean out = (second instanceof EGEntity)
				|| (second instanceof EGDefined)
				|| (second instanceof EGSimple)
				|| (second instanceof EGEnumerated)
				|| (second instanceof EGSelect);
		return out ? second : null;
	}

	/**
	 * getUIName
	 * 
	 * @return String
	 */
	public String getUIName() {
		return "Defined type";
	}

	/**
	 * returns defined type domain relation
	 * 
	 * @return
	 */
	public EGRelationSimple getAttribute() {
		Iterator iter = getWires().iterator();
		AbstractEGRelation rel = null;
		while ((iter.hasNext()) && (rel == null)) {
			Wire wire = (Wire)iter.next();
			if (wire.isAttribute())
				rel = wire.getRelation();
		}
		return (EGRelationSimple)rel;
	}

	/**
	 * returns defined type domain
	 * 
	 * @return
	 */
	public AbstractEGBox getDomain() {
		EGRelationSimple rel = getAttribute();
		AbstractEGBox box = null;
		if (rel != null)
			box = rel.getChild();
		return box;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jsdai.express_g.exp.eg.AbstractEGObject#canBeInvisible()
	 */
	public boolean canBeInvisible() {
		AbstractEGBox domain = getDomain();
		if (domain == null)
			return super.canBeInvisible();
		else
			return domain.canBeInvisible();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jsdai.express_g.exp.eg.AbstractEGObject#setVisible(boolean)
	 */
	public void setVisible(short visible) {
		super.setVisible(visible);
		EGRelationSimple rel = getAttribute();
		if (rel != null) {
			rel.getChild().setVisible(visible);
			rel.setVisible(visible);
		}
	}
}
