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

import java.util.LinkedHashSet;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import jsdai.express_g.exp2.EGToolKit;
import jsdai.express_g.exp2.Paging;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiException;
import jsdai.SExpress_g_schema.*;

/**
 * <p>Title: JSDAI Express-G</p>
 * <p>Description: </p>
 * @author Mantas Balnys
 * @version 1.0
 * @deprecated
 */

public class EGPageFrom extends EGPage {
  protected LinkedHashSet references = new LinkedHashSet();
  protected int refCount;
  private static int REF_COUNT = 1;

  public EGPageFrom(PropertySharing prop, AbstractEGBox referencedObject) {
    super(prop, referencedObject);
    refCount = REF_COUNT++;
    setPage(referencedObject.getPage());
  }

  public boolean addReference(EGPageTo reference) {
    if (references.contains(reference)) {
      return false;
    } else {
      references.add(reference);
      updateWrapper();
      return true;
    }
  }

  public boolean removeReference(EGPageTo reference) {
  	boolean out = references.remove(reference);
    updateWrapper();
    return out;
  }

  public Collection getReferences() {
    return (Collection)references.clone();
  }

  public int getReferencesCount() {
    return references.size();
  }

  public String getText() {
    String text = super.getText();
    if (references != null) {
//    	text = EGToolKit.convertPageNrToString(getPage()) + ", " + getRefCount() + " (";
    	text = EGToolKit.convertPageNrToString(getPage()) + ", " + getRefCount() + " (";
    	Iterator iter = references.iterator();
    	while (iter.hasNext()) {
    		text += EGToolKit.convertPageNrToString(((EGPageTo)iter.next()).getPage());
    		if (iter.hasNext()) text += ", ";
    	}
    	text += ")";
    }
    return text;
  }

  public int getRefCount() {
    return refCount;
  }

  public void eliminate() {
    super.eliminate();
    references.clear();
  }

  public void updateModel(SdaiModel modelDict, SdaiModel modelEG) throws SdaiException {
    EPage_reference_from page = (EPage_reference_from)modelEG.createEntityInstance(EPage_reference_from.class);
    setDefinition(page);
    APage_reference_to pageT;
    if (page.testReference_from(null)) {
      pageT = page.getReference_from(null);
    } else {
      pageT = page.createReference_from(null);
    }
    Iterator itp = getReferences().iterator();
    while (itp.hasNext()) {
      EGPageTo item = (EGPageTo)itp.next();
      pageT.addUnordered(item.getDefinition(modelDict, modelEG));
    }

    page.setChild(null, getReferencedObject().getDefinition(modelDict, modelEG));
    super.updateModel(modelDict, modelEG);
  }

  /**
   * getUIName
   *
   * @return String
   */
  public String getUIName() {
    return "Page reference from";
  }

  public AbstractEGRelation getRefRelation() {
    AbstractEGRelation rel = null;
    Iterator iter = getWires().iterator();
    while ((iter.hasNext())&&(rel == null)) {
      Wire wire = (Wire)iter.next();
      if (wire.isAttribute()) rel = wire.getRelation();
    }
    return rel;
  }
  
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.Paging#setPage(int)
	 */
	public void setPage(int pgNr) {
		super.setPage(pgNr);
		updateWrapper();
		Iterator iter = references.iterator();
		while (iter.hasNext()) 
			((EGPageTo)iter.next()).updateWrapper();
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.IXMLDefinition#getXMLDefinition(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String getXMLDefinition(Point startat, String schema_name, String schema_ext, String doc_file) {
		String text = null;
		Collection refs = this.getReferences();
		if (refs.size() == 1) {
			int pg = ((Paging)refs.iterator().next()).getPage();
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
}
