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

import jsdai.SExtended_dictionary_schema.EArray_type;
import jsdai.SExtended_dictionary_schema.EBag_type;
import jsdai.SExtended_dictionary_schema.EBound;
import jsdai.SExtended_dictionary_schema.EData_type;
import jsdai.SExtended_dictionary_schema.EInteger_bound;
import jsdai.SExtended_dictionary_schema.EList_type;
import jsdai.SExtended_dictionary_schema.ESet_type;
import jsdai.express_g.exp2.Named;
import jsdai.express_g.exp2.ui.event.LabelListener;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;

/**
 * <p>Title: JSDAI Express-G</p>
 * <p>Description: </p>
 * @author Mantas Balnys
 * @version 1.0
 */

public class Agregate implements Named, LabelListener {
  private AbstractEGBox entity_link;
  protected Agregate next = null;

  public static final int TYPE_SIMPLE = 0;
  public static final int TYPE_ARRAY = 1;
  public static final int TYPE_LIST = 2;
  public static final int TYPE_BAG = 3;
  public static final int TYPE_SET = 4;
  protected int type = TYPE_SIMPLE;

  protected String name = "";
  protected boolean unique = false;
  protected boolean optional = false;
  public static final int BOUND_NONE = Integer.MIN_VALUE;
  protected int minBound = BOUND_NONE;
  protected int maxBound = BOUND_NONE;

  public Agregate(AbstractEGBox agregation_type) {
    setAgregation_type(agregation_type);
  }
/*
  public void setEntity_link(AbstractEGBox link) {
    entity_link = link;
  }

  public AbstractEGBox getEntity_link() {
    return entity_link;
  }
*/
  public Agregate(AbstractEGBox agregation_type, Agregate next, String name, int type, int minBound, int maxBound, boolean optional, boolean unique) {
    this(agregation_type);
    setName(name);
    setType(type);
    setNext(next);
    setMinBound(minBound);
    setMaxBound(maxBound);
    setOptional(optional);
    setUnique(unique);
  }

  public String getText() {
    StringBuffer text = new StringBuffer();
    String namei = "";
    if (getAgregation_type() == null) namei = name;
    if (type == TYPE_SIMPLE) return namei;
    text.append(namei);
    if (!"".equals(namei)) text.append(" ");
    if (unique) text.append("*");
    switch (type) {
      case TYPE_ARRAY :
        text.append("A");
        break;
      case TYPE_BAG :
        text.append("B");
        break;
      case TYPE_LIST :
        text.append("L");
        break;
      case TYPE_SET :
        text.append("S");
        break;
    }
    text.append("[");
    if (minBound == BOUND_NONE) text.append("?"); else text.append(minBound);
    text.append(":");
    if (maxBound == BOUND_NONE) text.append("?"); else text.append(maxBound);
    text.append("]");
    if (next != null) {
      text.append(" ");
      text.append(next.getText());
    }
    return text.toString();
  }

  public void setAgregation_type(AbstractEGBox agregation_type) {
    if (agregation_type instanceof EGPageTo) entity_link = ((EGPageTo)agregation_type).getReferencedObject();
//    	else 
//    if (agregation_type instanceof EGEntityRef) entity_link = ((EGEntityRef)agregation_type).getReferenced();
       	else entity_link = agregation_type;
    if (next != null) next.setAgregation_type(agregation_type);
  }

  public AbstractEGBox getAgregation_type() {
    return entity_link;
  }

  public EData_type getSDAIObject(SdaiModel modelDict, SdaiModel modelEG) throws SdaiException {
    EData_type data_type = (EData_type)getAgregation_type().getDefinition(modelDict, modelEG);
    switch (type) {
      case TYPE_ARRAY :
        EArray_type array = (EArray_type)modelDict.createEntityInstance(EArray_type.class);
        array.setElement_type(null, next.getSDAIObject(modelDict, modelEG));
        array.setOptional_flag(null, optional);
        array.setUnique_flag(null, unique);
        if (minBound != BOUND_NONE) {
          EBound bound = (EBound)modelDict.createEntityInstance(EInteger_bound.class);
          bound.setBound_value(null, minBound);
          array.setLower_index(null, bound);
        }
        if (maxBound != BOUND_NONE) {
          EBound bound = (EBound)modelDict.createEntityInstance(EInteger_bound.class);
          bound.setBound_value(null, maxBound);
          array.setUpper_index(null, bound);
        }
        data_type = array;
        break;
      case TYPE_BAG :
        EBag_type bag = (EBag_type)modelDict.createEntityInstance(EBag_type.class);
        bag.setElement_type(null, next.getSDAIObject(modelDict, modelEG));
        if (minBound != BOUND_NONE) {
          EBound bound = (EBound)modelDict.createEntityInstance(EInteger_bound.class);
          bound.setBound_value(null, minBound);
          bag.setLower_bound(null, bound);
        }
        if (maxBound != BOUND_NONE) {
          EBound bound = (EBound)modelDict.createEntityInstance(EInteger_bound.class);
          bound.setBound_value(null, maxBound);
          bag.setUpper_bound(null, bound);
        }
        data_type = bag;
        break;
      case TYPE_LIST :
        EList_type list = (EList_type)modelDict.createEntityInstance(EList_type.class);
        list.setElement_type(null, next.getSDAIObject(modelDict, modelEG));
        list.setUnique_flag(null, unique);
        if (minBound != BOUND_NONE) {
          EBound bound = (EBound)modelDict.createEntityInstance(EInteger_bound.class);
          bound.setBound_value(null, minBound);
          list.setLower_bound(null, bound);
        }
        if (maxBound != BOUND_NONE) {
          EBound bound = (EBound)modelDict.createEntityInstance(EInteger_bound.class);
          bound.setBound_value(null, maxBound);
          list.setUpper_bound(null, bound);
        }
        data_type = list;
        break;
      case TYPE_SET :
        ESet_type set = (ESet_type)modelDict.createEntityInstance(ESet_type.class);
        set.setElement_type(null, next.getSDAIObject(modelDict, modelEG));
        if (minBound != BOUND_NONE) {
          EBound bound = (EBound)modelDict.createEntityInstance(EInteger_bound.class);
          bound.setBound_value(null, minBound);
          set.setLower_bound(null, bound);
        }
        if (maxBound != BOUND_NONE) {
          EBound bound = (EBound)modelDict.createEntityInstance(EInteger_bound.class);
          bound.setBound_value(null, maxBound);
          set.setUpper_bound(null, bound);
        }
        data_type = set;
        break;
    }
    if (type != TYPE_SIMPLE) data_type.setName(null, name);
    return data_type;
  }

  public Agregate getNext() {
    return next;
  }

  public void setNext(Agregate next) {
  	if (this.next != next) {
  		if (this.next != null) this.next.removeLabelListener(this);
  	    this.next = next;
  	    if (next != null) next.addLabelListener(this);
  	}
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
    if (type == TYPE_SIMPLE) {
      setNext(null);
    } else {
      if (next == null) next = new Agregate(getAgregation_type());
    }
    fireLabelChanged();
  }

  public boolean isUnique() {
    return unique;
  }
  public void setUnique(boolean unique) {
    this.unique = unique;
    fireLabelChanged();
  }
  public boolean isOptional() {
    return optional;
  }
  public void setOptional(boolean optional) {
    this.optional = optional;
    fireLabelChanged();
  }
  public int getMinBound() {
    return minBound;
  }
  public void setMinBound(int minBound) {
    this.minBound = minBound;
    fireLabelChanged();
  }
  public int getMaxBound() {
    return maxBound;
  }
  public void setMaxBound(int maxBound) {
    this.maxBound = maxBound;
    fireLabelChanged();
  }

  public String getName() {
    if (getAgregation_type() == null) return name;
    else {
      StringBuffer text = new StringBuffer();
      switch (type) {
        case TYPE_SIMPLE :
          text.append("_GENERIC");
          break;
        case TYPE_ARRAY :
          text.append("_ARRAY");
          break;
        case TYPE_LIST :
          text.append("_LIST");
          break;
        case TYPE_BAG :
          text.append("_BAG");
          break;
        case TYPE_SET :
          text.append("_SET");
          break;
      }
      if (minBound != BOUND_NONE) {
        text.append("_");
        text.append(minBound);
      }
      if (maxBound != BOUND_NONE) {
        text.append("_");
        text.append(maxBound);
      }
      if (unique) text.append("_UNIQUE");
      if (optional) text.append("_OPTIONAL");
      if (type == TYPE_SIMPLE) text.append("_" + getAgregation_type().getName());
      else text.append(next.getName());
      return text.toString();
    }
  }

  public void setName(String name) {
    this.name = name;
    fireLabelChanged();
  }

  /**
   * listens for label changes
   */
  private Collection labelListeners = new HashSet(2);
  
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
  
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.event.LabelListener#labelChanged(java.lang.Object)
	 */
	public void labelChanged(Object invoker) {
	    fireLabelChanged();
	}
}
