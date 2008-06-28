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
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;

import jsdai.express_g.common.StaticTools;
import jsdai.express_g.exp2.AbstractDraw;
import jsdai.express_g.exp2.ColorSchema;
import jsdai.express_g.exp2.Editable;
import jsdai.express_g.exp2.Printable;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.UINaming;
import jsdai.express_g.exp2.ui.event.LabelListener;
import jsdai.express_g.exp2.ui.properties.DialogBasic;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;

/**
 * @(#) AbstractEGObject.java
 */

public abstract class AbstractEGObject extends AbstractDraw implements
		SDAIdicSchema, Editable, UINaming, Printable {

	protected DialogBasic dialog = null;

	protected String shortName = "";

	private boolean label_is_new = true;

	public static final short VISIBLE_TRUE = 1;
	public static final short VISIBLE_FALSE = 2;
	public static final short VISIBLE_UNSET = 3;
	private short visible = VISIBLE_TRUE;
	
	/**
	 * is Dictionary Entity valid compared to this item properties
	 */
	protected boolean validDict;

	protected ColorSchema nonVisibleSchema = new ColorSchema(
			ColorSchema.COLOR_LIGHT_GRAY, ColorSchema.COLOR_WHITE);

	/**
	 * listens for label changes
	 */
	private Collection labelListeners = new HashSet(2);

	public AbstractEGObject(PropertySharing prop) {
		super(prop);
		validDict = false;
		setLabelNew(true);
	}

	protected AbstractEGObject() {
		super();
		validDict = false;
		setLabelNew(true);
	}

	public void addLabelListener(LabelListener listener) {
		labelListeners.add(listener);
	}

	public void removeLabelListener(LabelListener listener) {
		labelListeners.remove(listener);
	}

	public void fireLabelChanged() {
		Iterator iter = labelListeners.iterator();
		while (iter.hasNext())
			((LabelListener)iter.next()).labelChanged(this);
	}

	public boolean createEditDialog(Composite parent) {
		dialog = new DialogBasic(parent, this, prop());
		return isEditDialogCreated();
	}

	public void showEditDialog() {
		if (dialog.open() == Dialog.OK) {
			if (prop() != null)
				prop().setModified(true);
		}
	}

	public boolean isEditDialogCreated() {
		return (dialog != null) && (!dialog.isDisposed());
	}

	public void setLabelNew(boolean isNew) {
		if (label_is_new != isNew) {
			label_is_new = isNew;
			if (prop() != null)
				prop().setModified(true);
		}
	}

	public boolean isLabelNew() {
		return label_is_new;
	}

	public void updateModel(SdaiModel modelDict, SdaiModel modelEG)
			throws SdaiException {}
/*
	public void setLocation(Point location) {
		Point size = getSize();
		Rectangle imageSize = prop().getPainting().getImageBounds();
		location.x = Math.max(0, Math.min(imageSize.width - size.x,
				location.x));
		location.y = Math.max(0, Math.min(imageSize.height - size.y,
				location.y));
		super.setLocation(location);
	}
*/
	
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		if (!StaticTools.equalStrings(this.shortName, shortName)) {
			this.shortName = shortName;
			validDict = false;
			if (prop() != null)
				prop().setModified(true);
		}
	}

	public String getText() {
		return getName();
	}

	protected EEntity entity = null;

	protected EEntity eplacement = null;

	public EEntity getDefinition(SdaiModel modelDict, SdaiModel modelEG)
			throws SdaiException {
		if (entity == null || !validDict)
			updateModel(modelDict, modelEG);
		return entity;
	}

	public EEntity getDefinition() {
		return entity;
	}

	public EEntity getDefinitionPlacement() {
		return eplacement;
	}

	public EEntity getDefinitionPlacement(SdaiModel modelDict, SdaiModel modelEG)
			throws SdaiException {
		//    if ((eplacement == null)&&(!isOnPage(INVISIBLE_PAGE)))
		// updateModel(modelDict, modelEG);
		if (eplacement == null)
			updateModel(modelDict, modelEG);
		return eplacement;
	}

	public void clearDefinitions() {
		entity = null;
		eplacement = null;
		validDict = false;
	}

	public void setDefinition(EEntity entity) {
		this.entity = entity;
		validDict = entity != null;
	}

	public void setDefinitionPlacement(EEntity placement) {
		this.eplacement = placement;
//		validDict = placement != null;
	}

	/**
	 * getUIName
	 * 
	 * @return String
	 */
	public String getUIName() {
		return "Object";
	}

	public void print(GC g) {
		if (isVisible()) {
			boolean sel = isSelected();
			setSelected(false);
			draw(g);
			setSelected(sel);
		}
	}
	
	/** 
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	
	public int hashCode() {
		return super.hashCode();
	}

	public boolean equals(Object other) {
		if (other instanceof AbstractEGObject)
			return super.equals(other);
		else
			return other.equals(this);
	}

	public boolean isVisible() {
		return (visible & VISIBLE_TRUE) != 0;
	}
	
	public short getVisible() {
		if (visible == VISIBLE_UNSET && (getConcreteValue() == null || getConcreteValue().length() == 0))
			return VISIBLE_TRUE;
		else
			return visible;
	}

	public void setVisible(short visible) {
		if (this.visible != visible) {
			this.visible = visible;
			if (prop() != null)
				prop().setModified(true);
		}
	}

	public boolean canBeInvisible() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jsdai.express_g.exp2.Named#setName(java.lang.String)
	 */
	public void setName(String name) {
		super.setName(name);
		validDict = false;
		fireLabelChanged();
	}

	private int uid = -1;

	public int getID() {
		if (uid < 0)
			uid = prop().getNewID();
		return uid;
	}

	/**
	 * some concrete value of object
	 * parsed from _EXPRESS_... schemas
	 */
	private String[] concreteValues = null;
	
	public String getConcreteValue() {
		if (isConcreteValuesSet()) return concreteValues[0];
		return null;
	}
	
	public String[] getConcreteValues() {
		if (concreteValues == null)
			return null;
		else
			return (String[])concreteValues.clone();
	}
	
	public void setConcreteValue(String value) {
		setConcreteValues(new String[]{value});
	}
	
	public void setConcreteValues(String[] values) {
		if (values == null)
			concreteValues = null;
		else
			concreteValues = (String[])values.clone();
		fireLabelChanged();
//System.err.println("VALUES (" + values.length + "):");		
//for (int i = 0;	i < values.length; i++) System.err.println(i + ":\t" + values[i]);	
	}
	
	public boolean isConcreteValuesSet() {
		return concreteValues != null && concreteValues.length > 0;
	}
	
}