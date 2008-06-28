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

import jsdai.express_g.exp2.EGToolKit;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiException;
import jsdai.SExpress_g_schema.*;
import java.util.*;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * <p>Title: JSDAI Express-G</p>
 * <p>Description: </p>
 * @author Mantas Balnys
 * @version 1.0
 * @deprecated
 */

public class EGPageTo extends EGPage {

  protected EGPageFrom referenced = null;

  public EGPageTo(PropertySharing prop, EGPageFrom pageTo) {
    super(prop, pageTo.getReferencedObject());
    setReferenced(pageTo);
  }

  public void setReferenced(EGPageFrom referenced) {
    if (this.referenced != null) this.referenced.removeReference(this);
    this.referenced = referenced;
    referenced.addReference(this);
	updateWrapper();
  }

  public EGPageFrom getReferenced() {
    return referenced;
  }

  public String getText() {
    String text = super.getText();
    if (referenced != null)
    	text = EGToolKit.convertPageNrToString(referenced.getPage()) + ", " + getRefCount() + " " + text;
    return text;
  }

  public int getRefCount() {
    return referenced.getRefCount();
  }

  public void eliminate() {
    super.eliminate();
    referenced.removeReference(this);
  }


  public void updateModel(SdaiModel modelDict, SdaiModel modelEG) throws SdaiException {
    EPage_reference_to page = (EPage_reference_to)modelEG.createEntityInstance(EPage_reference_to.class);
    setDefinition(page);
    page.setReference_to(null, (EPage_reference_from)referenced.getDefinition(modelDict, modelEG));
    AbstractEGRelation rel = getRefRelation();
    page.setParent(null, rel.getParent().getDefinition(modelDict, modelEG));
    super.updateModel(modelDict, modelEG);
  }

  /**
   * getUIName
   *
   * @return String
   */
  public String getUIName() {
    return "Page reference to";
  }

  public AbstractEGRelation getRefRelation() {
    AbstractEGRelation rel = null;
    Iterator iter = getWires().iterator();
    while ((iter.hasNext())&&(rel == null)) {
      Wire wire = (Wire)iter.next();
      if (!wire.isAttribute()) rel = wire.getRelation();
    }
    return rel;
  }

  /* (non-Javadoc)
   * @see jsdai.express_g.exp.eg.AbstractEGObject#canBeInvisible()
   */
  public boolean canBeInvisible() {
    return getReferencedObject().canBeInvisible();
  }
 	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.Paging#setPage(int)
	 */
	public void setPage(int pgNr) {
		super.setPage(pgNr);
		updateWrapper();
		referenced.updateWrapper();
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.IXMLDefinition#getXMLDefinition(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String getXMLDefinition(Point startat, String schema_name, String schema_ext, String doc_file) {
		int pg = this.getReferencedObject().getPage();
		String text = null;
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
		return text;
	}
}
