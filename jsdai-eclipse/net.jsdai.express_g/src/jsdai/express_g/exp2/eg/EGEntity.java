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

import jsdai.SExtended_dictionary_schema.EEntity_definition;
import jsdai.express_g.exp2.Selectable;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.properties.DialogEntity;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

/**
 * @(#) EGEntity.java
 */

public class EGEntity extends AbstractEGBox {

	private static int ENTITY_COUNTING = 1;

	protected boolean complex = false;

	protected boolean independent = false;

	protected boolean instantiable = true;

	private boolean showABS = true;

	public EGEntity(PropertySharing prop) {
		super(prop);
		//    Dimension size = new Dimension();
		//    size.setSize(basicWidth, basicHeight);
		//    setSize(size);
		setName("Entity_" + (ENTITY_COUNTING++));
		textInset = 7;
	}

	public EGEntity(PropertySharing prop, String name, Rectangle bounds,
			boolean complex, boolean independent, boolean instantiable) {
		this(prop);
		setBounds(bounds);
		setName(name);
		setComplex(complex);
		setIndependent(independent);
		setInstantiable(instantiable);
	}

	public EGEntity(PropertySharing prop, String name, Point location,
			boolean complex, boolean independent, boolean instantiable) {
		this(prop);
		setLocation(location);
		setName(name);
		setComplex(complex);
		setIndependent(independent);
		setInstantiable(instantiable);
	}

	public void updateModel(SdaiModel modelDict, SdaiModel modelEG)	throws SdaiException {
		if ((modelDict != null) && !validDict) {
			EEntity_definition entity = null;
			EEntity ent = getDefinition();
			if (ent instanceof EEntity_definition) {
				entity = (EEntity_definition)ent;
			} else {
				entity = (EEntity_definition)modelDict.createEntityInstance(EEntity_definition.class);
			}
			entity.setName(null, getName());
			entity.setComplex(null, complex);
			entity.setIndependent(null, independent);
			entity.setInstantiable(null, instantiable);
			if (!"".equals(shortName)) entity.setShort_name(null, shortName);
			setDefinition(entity);
		}
		super.updateModel(modelDict, modelEG);
	}

	public void setShowABS(boolean show) {
		this.showABS = show;
		wrapper.setText(getText());
		fireLabelChanged();
	}

	public boolean createEditDialog(Composite parent) {
		dialog = new DialogEntity(parent, this, prop());
		return isEditDialogCreated();
	}

	public boolean isComplex() {
		return complex;
	}

	public void setComplex(boolean complex) {
		this.complex = complex;
		validDict = false;
	}

	public boolean isIndependent() {
		return independent;
	}

	public void setIndependent(boolean independent) {
		this.independent = independent;
		validDict = false;
	}

	public boolean isInstantiable() {
		return instantiable;
	}

	public void setInstantiable(boolean instantiable) {
		this.instantiable = instantiable;
		validDict = false;
		wrapper.setText(getText());
		fireLabelChanged();
	}

	public Selectable selectAsFirst(int type) {
		return this;
	}

	public Selectable selectSecond(int type, Selectable second) {
		boolean out = false;
		if (type == AbstractEGRelation.TYPE_INHERITANCE) {
			out = second instanceof EGEntity;
		} else {
			out = (second instanceof EGEntity) || (second instanceof EGDefined)
					|| (second instanceof EGSimple)
					|| (second instanceof EGEnumerated)
					|| (second instanceof EGSelect)
					|| (second instanceof EGEntityRef);
		}
		return out ? second : null;
	}

	/**
	 * getUIName
	 * 
	 * @return String
	 */
	public String getUIName() {
		return "Entity";
	}

	public String getText() {
		String text = super.getText();
		if (!isInstantiable())
			text = showABS ? "(Abs) " + text : text;
		return text;
	}

}