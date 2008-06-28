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

package jsdai.express_g.exp2.ui;

import java.util.HashSet;
import java.util.Vector;
import java.util.Iterator;

import jsdai.express_g.SdaieditPlugin;
import jsdai.express_g.common.Resources;
import jsdai.express_g.exp2.Paging;
import jsdai.express_g.exp2.eg.AbstractEGBox;
import jsdai.express_g.exp2.eg.AbstractEGObject;
import jsdai.express_g.exp2.eg.AbstractEGRelation;
import jsdai.express_g.exp2.eg.EGPageRef;
import jsdai.express_g.exp2.eg.EGPageRefLocalFrom;
import jsdai.express_g.exp2.eg.EGPageRefLocalTo;
import jsdai.express_g.exp2.eg.IHidenRef;
import jsdai.express_g.exp2.eg.Wire;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.help.WorkbenchHelp;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * @author Mantas Balnys
 * @version 1.0
 */

public class BoxSelectionMenu extends MenuManager {
  protected Action menuBringAll;
  public static final int TYPE_ATTRIBUTE = 0;
  public static final int TYPE_REFERENCE = 1;
  public static final int TYPE_SUPERTYBE = 2;
  public static final int TYPE_SUBTYPE = 3;
  static final String[] menuText = {"attribute", "users", "supertype", "subtype"};
  protected int type = TYPE_REFERENCE;
  protected ContextMenu menu;

  protected Vector subMenus = new Vector();
  protected boolean subsValid = false;

  public BoxSelectionMenu(ContextMenu menu, int type) {
  	this(menu, menuText[type], type);
  }

  public BoxSelectionMenu(ContextMenu menu, String name, int type) {
  	super(name);
    this.menu = menu;
    this.type = type;
    init();
  }

  	private void init() {
  		menuBringAll = new Action("all") {
  			public void run() {
  				Iterator iter = subMenus.iterator();
  				HashSet items = new HashSet(subMenus.size());
  				while (iter.hasNext()) items.add(((BoxSelectionMenuItem)iter.next()).getObject());
  				menu.bringToThisPage(items);
  			}
  		};
  		add(menuBringAll);
  		add(new Separator());
  	}

  	public void clear() {
  		removeAll();
  		subMenus.clear();
  		subsValid = false;
  		add(menuBringAll);
  		add(new Separator());
  	}
/*
  protected void addSubMenu(AbstractEGBox object) {
    addSubMenu(object, object.getText());
  }
*/
  	protected void addSubMenu(AbstractEGObject object, String name) {
//System.out.println("ADD TO SUBMENU: " + object);  		
  		BoxSelectionMenuItem menuItem = new BoxSelectionMenuItem(menu, object, name);
  		add(menuItem);
  		subMenus.add(menuItem);
  		
  		if (object.isOnPage(Paging.INVISIBLE_PAGE)) {
  			menuItem.setImageDescriptor(Resources.getImageDescriptor(Resources.ERROR_OVERLAY));
  		}
  	}

  protected void updateSubMenus() {
    Iterator iter = menu.getSelected().iterator();
    while (iter.hasNext()) {
      Object item = iter.next();
      if (item instanceof AbstractEGBox) {
        int pgNr = ((AbstractEGBox)item).getPage();
        if (item instanceof EGPageRefLocalFrom) {
            if (type == TYPE_REFERENCE) {
            	Iterator itpf = ((EGPageRefLocalFrom)item).getLinkedIterator();
            	while (itpf.hasNext()) {
            		EGPageRefLocalTo pgt = (EGPageRefLocalTo)itpf.next();
            		Iterator wit = pgt.getWires().iterator();
            		while (wit.hasNext()) {
            			Wire wire = (Wire)wit.next();
            			if (!wire.isAttribute()) {
            				AbstractEGBox box = wire.getRelation().getParent();
            				if (!box.isOnPage(pgNr)) addSubMenu(box, box.getText());
            			}
            		}
            	}
            }
        } else if (item instanceof EGPageRefLocalTo) {
        	if (type == TYPE_REFERENCE) {
        		AbstractEGBox box = ((EGPageRefLocalTo)item).getReferenced();
                if (!box.isOnPage(pgNr)) addSubMenu(box, box.getText());
            }
/*        } else // items are no more available in visible view
        if (item instanceof EGPageFrom) {
          if (type == TYPE_REFERENCE) {
            Iterator itpf = ((EGPageFrom)item).getReferences().iterator();
            while (itpf.hasNext()) {
              EGPageTo pgt = (EGPageTo)itpf.next();
              AbstractEGBox box = pgt.getRefRelation().getParent();
              if (!box.isOnPage(pgNr)) addSubMenu(box, box.getText());
            }
          }
        } else if (item instanceof EGPageTo) {
            if (type == TYPE_REFERENCE) {
              AbstractEGBox box = ((EGPageTo)item).getReferencedObject();
              if (!box.isOnPage(pgNr)) addSubMenu(box, box.getText());
            }*/
        } else {
          Iterator itw = ((AbstractEGBox)item).getWires().iterator();
          switch (type) {
            case TYPE_ATTRIBUTE :
              while (itw.hasNext()) {
                Wire wire = (Wire)itw.next();
                AbstractEGRelation rel = wire.getRelation();
                if (wire.isAttribute() && (rel.getType() == AbstractEGRelation.TYPE_AGREGATION)) {
                        Iterator itchild = rel.getChilds().iterator();
                        while (itchild.hasNext()) {
                          AbstractEGObject box = (AbstractEGObject)itchild.next();
                          if (box instanceof EGPageRefLocalTo) box = ((EGPageRefLocalTo)box).getReferenced(); 
                          else if ((box instanceof EGPageRef) || (box instanceof IHidenRef)) box = rel;
                          if (rel.isOnPage(Paging.INVISIBLE_PAGE)) addSubMenu(rel, rel.getText() + " [" + box.getText() + "]");
                          else if (!box.isOnPage(pgNr)) addSubMenu(box, rel.getText() + " [" + box.getText() + "]");
                          else if (!rel.isOnPage(pgNr)) addSubMenu(rel, rel.getText() + " [" + box.getText() + "]");
                        }
                }
              }
              break;
            case TYPE_REFERENCE :
              while (itw.hasNext()) {
                Wire wire = (Wire)itw.next();
                AbstractEGRelation rel = wire.getRelation();
                if (!wire.isAttribute() && rel.getType() == AbstractEGRelation.TYPE_AGREGATION) {
                  AbstractEGObject box = rel.getParent();
                  if (box instanceof EGPageRefLocalFrom) {
                    Iterator itpf = ((EGPageRefLocalFrom)box).getLinkedIterator();
                    while (itpf.hasNext()) {
                      EGPageRefLocalTo pgt = (EGPageRefLocalTo)itpf.next();
                      Iterator wit2 = pgt.getWires().iterator();
                      while (wit2.hasNext()) {
                      	Wire wire2 = (Wire)wit2.next();
                      	if (!wire2.isAttribute()) {
                      		AbstractEGRelation rel2 = wire2.getRelation();
                            box = rel2.getParent();
                            if (!box.isOnPage(pgNr)) addSubMenu(box, box.getText() + "." + rel2.getText());
                      	}
                      }
                    }
                  } else {
//                    if ((box instanceof EGPageRef) || (box instanceof IHidenRef)) box = rel;
//                  	if (!box.isOnPage(pgNr)) addSubMenu(box, box.getText() + "." + rel.getText());
                  	if (!rel.isOnPage(pgNr)) addSubMenu(rel, box.getText() + "." + rel.getText());
                  }
                }
              }
              break;
            case TYPE_SUBTYPE :
              while (itw.hasNext()) {
                Wire wire = (Wire)itw.next();
                AbstractEGRelation rel = wire.getRelation();
                if ((wire.isAttribute())&&(rel.getType() == AbstractEGRelation.TYPE_INHERITANCE)) {
                  Iterator itchild = rel.getChilds().iterator();
                  while (itchild.hasNext()) {
                    AbstractEGObject box = (AbstractEGObject)itchild.next();
                    if (box instanceof EGPageRefLocalTo) {
                    	AbstractEGBox referenced = ((EGPageRefLocalTo)box).getReferenced();
                    	if (referenced.isOnPage(Paging.INVISIBLE_PAGE)) {
                    		addSubMenu(referenced, referenced.getText());
                    	} else
                    	if (box.isOnPage(Paging.INVISIBLE_PAGE)) {
                    		addSubMenu(box, referenced.getText());
                    	} else {
                            if (!referenced.isOnPage(pgNr)) addSubMenu(referenced, referenced.getText());
                            else if (!rel.isOnPage(pgNr)) addSubMenu(rel, rel.getText() + " [" + referenced.getText() + "]");
                    	}
                    }
                    else if ((box instanceof EGPageRef) || (box instanceof IHidenRef)) {
                        if (!rel.isOnPage(pgNr)) addSubMenu(rel, rel.getText() + " [" + box.getText() + "]");
                    } else {
                        if (!box.isOnPage(pgNr)) addSubMenu(box, box.getText());
                        else if (!rel.isOnPage(pgNr)) addSubMenu(rel, rel.getText() + " [" + box.getText() + "]");
                    }
                  }
                }
              }
              break;
            case TYPE_SUPERTYBE :
              while (itw.hasNext()) {
                Wire wire = (Wire)itw.next();
                AbstractEGRelation rel = wire.getRelation();
                if (!wire.isAttribute() && rel.getType() == AbstractEGRelation.TYPE_INHERITANCE) {
                  AbstractEGObject box = rel.getParent();
                  if (box instanceof EGPageRefLocalFrom) {
                    Iterator itpf = ((EGPageRefLocalFrom)box).getLinkedIterator();
                    while (itpf.hasNext()) {
                      EGPageRefLocalTo pgt = (EGPageRefLocalTo)itpf.next();
                      Iterator wit2 = pgt.getWires().iterator();
                      while (wit2.hasNext()) {
                      	Wire wire2 = (Wire)wit2.next();
                      	if (!wire2.isAttribute()) {
                      		AbstractEGRelation rel2 = wire2.getRelation();
                            box = rel2.getParent();
                            if (!box.isOnPage(pgNr)) addSubMenu(box, box.getText());
                      	}
                      }
                    }
                  } else {
                  	String name = box.getText() + "." + rel.getText();
                    if ((box instanceof EGPageRef) || (box instanceof IHidenRef)) box = rel;
                  	if (!box.isOnPage(pgNr)) addSubMenu(box, name);
                    else if (!rel.isOnPage(pgNr)) addSubMenu(rel, rel.getText() + " [" + box.getText() + "]");
                  }
                }
              }
              break;
          }
        }
      }
    }
    subsValid = true;
  }

  /**
   * Sets the visibility of the menu's popup.
   *
   * @param b a boolean value -- true to make the menu visible, false to hide it
   */
  public void trySetVisible(boolean visible) {
  	clear();
//System.out.println("MENU try set visibility to " + visible);  	
    if ((visible)&&(!subsValid)) {
    	updateSubMenus();
    	if (subMenus.size() == 0) { 
    		visible = false;
//System.out.println("false");    		
    	} else if (subMenus.size() == 1) {
    		menuBringAll.setEnabled(false);
//System.out.println("disable all");    		
    	} else { 
    		menuBringAll.setEnabled(true);
//System.out.println("enable all");    		
    	}
    }
    setVisible(visible);
  }
  
	
	public Menu createContextMenu(Control parent) {
		Menu menu = super.createContextMenu(parent);
		WorkbenchHelp.setHelp(menu, SdaieditPlugin.ID_SDAIEDIT + ".MainContextMenuBringObjectContextId");
		return menu;
	}
	
	public Menu createMenuBar(Decorations parent) {
		Menu menu = super.createMenuBar(parent);
		WorkbenchHelp.setHelp(menu, SdaieditPlugin.ID_SDAIEDIT + ".MainContextMenuBringObjectContextId");
		return menu;
	}
}

