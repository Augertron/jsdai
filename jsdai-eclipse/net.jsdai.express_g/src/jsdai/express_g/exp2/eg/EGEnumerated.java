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
import jsdai.SExtended_dictionary_schema.EEnumeration_type;
import jsdai.SExtended_dictionary_schema.EExtended_enumeration_type;
import jsdai.SExtended_dictionary_schema.EExtensible_enumeration_type;
import jsdai.express_g.exp2.ColorSchema;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.properties.DialogEnumerated;
import jsdai.lang.A_string;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

/**
 * @(#) EGEnumerated.java
 */

public class EGEnumerated extends AbstractEGBox implements IDefined_type {

	public static final int TYPE_EXTENSIBLE = 1;

	public static final int TYPE_EXTENDED = 2;

	protected int type = TYPE_EXTENSIBLE;

	public Vector enumeration_items = new Vector();

	private static int ENUMERATION_TYPE_COUNTING = 1;

	public EGEnumerated(PropertySharing prop) {
		super(prop);
		simpleSchema.lineStyle = selectedSchema.lineStyle = ColorSchema.DASHED_LINE;
		setName("Enumeration_type_" + (ENUMERATION_TYPE_COUNTING++));
	}

	public EGEnumerated(PropertySharing prop, String name, Rectangle bounds,
			int type, A_string names) throws SdaiException {
		this(prop);
		setBounds(bounds);
		setName(name);
		setType(type);
		SdaiIterator it = names.createIterator();
		while (it.next())
			enumeration_items.add(names.getCurrentMember(it));
	}

	public EGEnumerated(PropertySharing prop, String name, Point location,
			int type, A_string names) throws SdaiException {
		this(prop);
		setLocation(location);
		setName(name);
		setType(type);
		SdaiIterator it = names.createIterator();
		while (it.next())
			enumeration_items.add(names.getCurrentMember(it));
	}

	/**
	 * draw
	 * 
	 * @param g2 Graphics2D
	 * @todo Implement this jsdai.paint.Drawable method
	 */
	public void draw(GC g) {
		super.draw(g);
		Rectangle bounds = getBounds();
		int x = bounds.x + bounds.width - bounds.width / 10;
		g.drawLine(x, bounds.y, x, bounds.y + bounds.height);
	}

	// select definition, standart definition contains defined type
	private EEntity hidenDefinition;

	public EEntity getHidenDefinition() {
		return hidenDefinition;
	}

	public void setHidenDefinition(EEntity en) {
		hidenDefinition = en;
	}

	public void updateModel(SdaiModel modelDict, SdaiModel modelEG)
			throws SdaiException {
		if (modelDict != null) {
			EEnumeration_type enum_type = null;
			EEntity ent = getDefinition();
			if (ent instanceof EEnumeration_type) {
				enum_type = (EEnumeration_type)ent;
			} else {
				Class clas = EExtensible_enumeration_type.class;
				if ((type & TYPE_EXTENDED) != 0)
					clas = EExtended_enumeration_type.class;
				if ((type & TYPE_EXTENSIBLE) != 0)
					clas = EExtensible_enumeration_type.class;
				enum_type = (EEnumeration_type)modelDict.createEntityInstance(clas);
			}
			enum_type.setName(null, "_ENUMERATION_" + getName());
			A_string astr = enum_type.createLocal_elements(null);
			Iterator iter = enumeration_items.iterator();
			while (iter.hasNext())
				astr.addUnordered((String)iter.next());
			setDefinition(enum_type);
			// hiding enumeration under defined type
			EDefined_type ed;
			ed = (EDefined_type)modelDict
					.createEntityInstance(EDefined_type.class);
			ed.setName(null, getName());
			ed.setDomain(null, enum_type);
			setDefinition(ed);
			if (!"".equals(shortName)) ed.setShort_name(null, shortName);
			hidenDefinition = enum_type;
		}
		super.updateModel(modelDict, modelEG);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
		validDict = false;
		fireLabelChanged();
	}

	/**
	 * getTextPlace
	 * 
	 * @return Rectangle
	 * @todo Implement this jsdai.paint.eg.AbstractEGBox method
	 */
	public Rectangle getTextInsets() {
		Rectangle place = super.getTextInsets();
		Rectangle bounds = getBounds();
		place.width += bounds.width / 10;
		return place;
	}

	public boolean createEditDialog(Composite parent) {
		dialog = new DialogEnumerated(parent, this, prop());
		return isEditDialogCreated();
	}

	public String getText() {
		String text = super.getText();
		if ((type & TYPE_EXTENSIBLE) != 0)
			text = "(EX) " + text;
		return text;
	}

	/**
	 * getUIName
	 * 
	 * @return String
	 */
	public String getUIName() {
		return "Enumeration type";
	}
}