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

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.help.WorkbenchHelp;

import jsdai.express_g.SdaieditPlugin;
import jsdai.express_g.common.DialogSelectPageList;
import jsdai.express_g.exp2.AbstractDraw;
import jsdai.express_g.exp2.EGToolKit;
import jsdai.express_g.exp2.eg.AbstractEGBox;
import jsdai.express_g.exp2.eg.AbstractEGObject;
import jsdai.express_g.exp2.eg.AbstractEGRelation;
import jsdai.express_g.exp2.eg.EGPageRef;
import jsdai.express_g.exp2.eg.EGRelationSimple;
import jsdai.express_g.exp2.eg.EGRelationTree;
import jsdai.express_g.exp2.eg.EGSchema;
import jsdai.express_g.exp2.eg.RelationGroup;
import jsdai.express_g.exp2.eg.Wire;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.command.Command;
import jsdai.express_g.exp2.ui.command.CommandInvoker;
import jsdai.express_g.exp2.ui.command.LayoutCommand;
import jsdai.express_g.exp2.ui.command.SelectCommand;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * @author Mantas Balnys
 * @version 1.0
 */

public class ContextMenu extends MenuManager implements CommandInvoker, IMenuListener {
  public static final int MASK_NONE = 0;
  public static final int MASK_ALL = Integer.MAX_VALUE;
  protected Action menuProp;
  public static final int MASK_PROPERTIES = 1<<1;
  protected Action menuPage;
  public static final int MASK_MOVE_TO_PAGE = 1<<2;
  protected Action menuLayout;
  public static final int MASK_LAYOUT = 1<<3;
  protected Action menuDelete;
  public static final int MASK_DELETE = 1<<4;
  protected BoxSelectionMenu menuBring = new BoxSelectionMenu(this, "Bring to this page", BoxSelectionMenu.TYPE_REFERENCE);
  public static final int MASK_BRING_TO_THIS_PAGE = 1<<5;
  protected Action menuResetHint;
  public static final int MASK_RESET_PLACEMENT = 1<<6;
  protected MenuManager menuBringSome;
  protected BoxSelectionMenu menuBringSup = new BoxSelectionMenu(this, BoxSelectionMenu.TYPE_SUPERTYBE);
  public static final int MASK_BRING_SUPERTYPES = 1<<7;
  protected BoxSelectionMenu menuBringSub = new BoxSelectionMenu(this, BoxSelectionMenu.TYPE_SUBTYPE);
  public static final int MASK_BRING_SUBTYPES = 1<<8;
  protected BoxSelectionMenu menuBringRef = new BoxSelectionMenu(this, BoxSelectionMenu.TYPE_REFERENCE);
  public static final int MASK_BRING_REFERENCES = 1<<9;
  protected BoxSelectionMenu menuBringAttr = new BoxSelectionMenu(this, BoxSelectionMenu.TYPE_ATTRIBUTE);
  public static final int MASK_BRING_ATTRIBUTES = 1<<10;
  protected Action menuVisible;
  public static final int MASK_SET_VISIBILE = 1<<11;
  protected Action menuInvisible;
  public static final int MASK_SET_INVISIBILE = 1<<12;
  protected Action menuAutoSize;
  public static final int MASK_AUTO_SIZE = 1<<13;
  // Aligning:
  public static final int MASK_ALIGN = 1<<14;
  protected MenuManager menuJustify;
  protected MenuManager menuArrange;
  protected Action menuJustifyLeft;
  protected Action menuJustifyCenterH;
  protected Action menuJustifyRight;
  protected Action menuJustifyTop;
  protected Action menuJustifyCenterV;
  protected Action menuJustifyBottom;
  protected Action menuArrangeH;
  protected Action menuArrangeV;
  // Relation grouping
  protected MenuManager menuGrouping;
  protected Action menuBundle;
  public static final int MASK_BUNDLE = 1<<15;
  protected Action menuUnbundle;
  public static final int MASK_UNBUNDLE = 1<<16;
  // real value
  protected Action concreteValueOn;
  public static final int MASK_SHOW_CONCRETE = 1<<17;
  protected Action concreteValueOff;
  public static final int MASK_HIDE_CONCRETE = 1<<18;
  
  protected int mask = 0;
  protected PropertySharing prop;

  public ContextMenu(PropertySharing prop) {
  	super();
    this.prop = prop;
    try {
      init();
    }
    catch(Exception e) {
		prop.status().log(e);
    }
    addMenuListener(this);
  }
	
  private void init() throws Exception {
    menuProp = new Action("Properties...") {
        public void run() {
            menuProp_actionPerformed();
          }
    };
	WorkbenchHelp.setHelp(menuProp, SdaieditPlugin.ID_SDAIEDIT + ".MainContextMenuPropertiesContextId");

    menuPage = new Action("Move to page...") {
    	public void run() {
    		menuPage_actionPerformed();
    	}
    };
	WorkbenchHelp.setHelp(menuPage, SdaieditPlugin.ID_SDAIEDIT + ".MainContextMenuMoveToPageContextId");
    menuLayout = new Action("Layout on this page") {
        public void run() {
            menuLayout_actionPerformed();
        }
    };
	WorkbenchHelp.setHelp(menuLayout, SdaieditPlugin.ID_SDAIEDIT + ".MainContextMenuLayoutContextId");
    menuDelete = new Action("Delete") {
        public void run() {
            menuDelete_actionPerformed();
        }
    };
	WorkbenchHelp.setHelp(menuDelete, SdaieditPlugin.ID_SDAIEDIT + ".MainContextMenuDeleteContextId");
/*    menuBring.setText("bring to this page");
    menuBring.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        menuBring_actionPerformed(e);
      }
    });*/
    menuResetHint = new Action("Reset placement hints") {
        public void run() {
            menuResetHint_actionPerformed();
        }
    };
	WorkbenchHelp.setHelp(menuResetHint, SdaieditPlugin.ID_SDAIEDIT + ".MainContextMenuResetHintContextId");
    menuVisible = new Action("Make visible"){
        public void run() {
            menuVisibility_actionPerformed(true);
        }
    };
	WorkbenchHelp.setHelp(menuVisible, SdaieditPlugin.ID_SDAIEDIT + ".MainContextMenuVisibilityContextId");
    menuInvisible = new Action("Make invisible"){
        public void run() {
            menuVisibility_actionPerformed(false);
        }
    };
	WorkbenchHelp.setHelp(menuInvisible, SdaieditPlugin.ID_SDAIEDIT + ".MainContextMenuVisibilityContextId");
    menuAutoSize = new Action("Auto size"){
        public void run() {
            menuAutoSize_actionPerformed();
        }
    };
	WorkbenchHelp.setHelp(menuAutoSize, SdaieditPlugin.ID_SDAIEDIT + ".MainContextMenuAutoSizeContextId");
    menuBringSome = new MenuManager("Bring to this page");
    concreteValueOn = new Action("show expression") {
        public void run() {
            menuShowConcreteValue_actionPerformed(true);
        }
    };
    concreteValueOff = new Action("hide expression") {
        public void run() {
            menuShowConcreteValue_actionPerformed(false);
        }
    };
    this.add(menuProp);
    this.add(menuVisible);
    this.add(menuInvisible);
    this.add(concreteValueOn);
    this.add(concreteValueOff);
    this.add(menuResetHint);
    this.add(menuAutoSize);
    this.add(menuDelete);
    this.add(new Separator());
    this.add(menuPage);
    this.add(menuLayout);
    this.add(menuBring);
    this.add(menuBringSome);
    menuBringSome.add(menuBringSup);
    menuBringSome.add(menuBringSub);
    menuBringSome.add(menuBringRef);
    menuBringSome.add(menuBringAttr);
    // Aligning:
    menuJustify = new MenuManager("Justify");
    menuArrange = new MenuManager("Arrange");
    menuJustifyLeft = new Action("Left") {
        public void run() {
            menuJustifyLeft_actionPerformed();
        }
    };
	WorkbenchHelp.setHelp(menuJustifyLeft, SdaieditPlugin.ID_SDAIEDIT + ".MainContextMenuJustifyContextId");
    menuJustifyCenterH = new Action("Center") {
        public void run() {
            menuJustifyCenterH_actionPerformed();
        }
    };
	WorkbenchHelp.setHelp(menuJustifyCenterH, SdaieditPlugin.ID_SDAIEDIT + ".MainContextMenuJustifyContextId");
    menuJustifyRight = new Action("Right") {
        public void run() {
            menuJustifyRight_actionPerformed();
        }
    };
	WorkbenchHelp.setHelp(menuJustifyRight, SdaieditPlugin.ID_SDAIEDIT + ".MainContextMenuJustifyContextId");
    menuJustifyTop = new Action("Top") {
        public void run() {
            menuJustifyTop_actionPerformed();
        }
    };
	WorkbenchHelp.setHelp(menuJustifyTop, SdaieditPlugin.ID_SDAIEDIT + ".MainContextMenuJustifyContextId");
    menuJustifyCenterV = new Action("Center") {
        public void run() {
            menuJustifyCenterV_actionPerformed();
        }
    };
	WorkbenchHelp.setHelp(menuJustifyCenterV, SdaieditPlugin.ID_SDAIEDIT + ".MainContextMenuJustifyContextId");
    menuJustifyBottom = new Action("Bottom") {
        public void run() {
            menuJustifyBottom_actionPerformed();
        }
    };
	WorkbenchHelp.setHelp(menuJustifyBottom, SdaieditPlugin.ID_SDAIEDIT + ".MainContextMenuJustifyContextId");
    menuArrangeH = new Action("Horizontal") {
        public void run() {
            menuArrangeH_actionPerformed();
        }
    };
	WorkbenchHelp.setHelp(menuArrangeH, SdaieditPlugin.ID_SDAIEDIT + ".MainContextMenuArrangeContextId");
    menuArrangeV = new Action("Vertical") {
        public void run() {
            menuArrangeV_actionPerformed();
        }
    };
	WorkbenchHelp.setHelp(menuArrangeV, SdaieditPlugin.ID_SDAIEDIT + ".MainContextMenuArrangeContextId");
    this.add(menuJustify);
    menuJustify.add(menuJustifyLeft);
    menuJustify.add(menuJustifyCenterH);
    menuJustify.add(menuJustifyRight);
    menuJustify.add(new Separator());
    menuJustify.add(menuJustifyTop);
    menuJustify.add(menuJustifyCenterV);
    menuJustify.add(menuJustifyBottom);
    this.add(menuArrange);
    menuArrange.add(menuArrangeH);
    menuArrange.add(menuArrangeV);
    // bundle actions
    menuGrouping = new MenuManager("Grouping");
    menuBundle = new Action("Bundle") {
        public void run() {
            menuBundle_actionPerformed();
        }
    };
	WorkbenchHelp.setHelp(menuBundle, SdaieditPlugin.ID_SDAIEDIT + ".MainContextMenuGroupingContextId");
    menuUnbundle = new Action("Unbundle") {
        public void run() {
            menuUnbundle_actionPerformed();
        }
    };
	WorkbenchHelp.setHelp(menuUnbundle, SdaieditPlugin.ID_SDAIEDIT + ".MainContextMenuGroupingContextId");
    this.add(menuGrouping);
    menuGrouping.add(menuBundle);
    menuGrouping.add(menuUnbundle);
  }

  public Set getSelected() {
    return prop.getSelectionHandler().getSelected();
  }

  public void bringToThisPage(AbstractEGObject box) {
    HashSet items = new HashSet(1);
    items.add(box);
    bringToThisPage(items);
  }

  public void bringToThisPage(Set items) {
/*    Iterator iter = prop.getSelectionHandler().getSelected().iterator();
    Rectangle bounds = null;
    while (iter.hasNext()) {
      Object item = iter.next();
      if (item instanceof AbstractEGBox) {
        if (bounds == null) bounds = ((AbstractEGBox)item).getBounds();
        else bounds.add(((AbstractEGBox)item).getBounds());
      }
    }
    Point loc = new Point(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);*/
//    loc.translate(-(int)box.getSize().getWidth()/2, -(int)box.getSize().getHeight());
    prop.getSelectionHandler().setSelected(this, items);
    layoutCommand.setLocation(LayoutCommand.NO_LOCATION);
    prop.handler().startCommand(layoutCommand);
  }

  /**
   * commandDone
   *
   * @param command Command
   */
  public void commandDone(Command command) {
  }

  public int getMenuMask() {
    return mask;
  }

  public void setMenuMask(int mask) {
    this.mask = mask;
  }

  public int setMenuMask(int mask, boolean visible) {
    if (visible) {
      this.mask = this.mask | mask;
    } else {
      mask = MASK_ALL ^ mask;
      this.mask = this.mask & mask;
    }
    return this.mask;
  }

  public boolean hasProperties() {
    return (mask & MASK_PROPERTIES) != 0;
  }

  public boolean hasMove_to_page() {
    return (mask & MASK_MOVE_TO_PAGE) != 0;
  }

  public boolean hasLayout() {
    return (mask & MASK_LAYOUT) != 0;
  }

  public boolean hasDelete() {
    return (mask & MASK_DELETE) != 0;
  }

  public boolean hasBring_to_this_page() {
    return (mask & MASK_BRING_TO_THIS_PAGE) != 0;
  }

  public boolean hasReset_placement() {
    return (mask & MASK_RESET_PLACEMENT) != 0;
  }

  public boolean hasBring_attributes() {
    return (mask & MASK_BRING_ATTRIBUTES) != 0;
  }

  public boolean hasBring_references() {
    return (mask & MASK_BRING_REFERENCES) != 0;
  }

  public boolean hasBring_subtypes() {
    return (mask & MASK_BRING_SUBTYPES) != 0;
  }

  public boolean hasBring_supertypes() {
    return (mask & MASK_BRING_SUPERTYPES) != 0;
  }

  public boolean hasVisible() {
    return (mask & MASK_SET_VISIBILE) != 0;
  }

  public boolean hasInvisible() {
    return (mask & MASK_SET_INVISIBILE) != 0;
  }

  public boolean hasAutoSize() {
    return (mask & MASK_AUTO_SIZE) != 0;
  }

  public boolean hasAlign() {
    return (mask & MASK_ALIGN) != 0;
  }

  public boolean hasBundle() {
    return (mask & MASK_BUNDLE) != 0;
  }

  public boolean hasUnbundle() {
    return (mask & MASK_UNBUNDLE) != 0;
  }

  public boolean hasConcreteValueOn() {
    return (mask & MASK_SHOW_CONCRETE) != 0;
  }

  public boolean hasConcreteValueOff() {
    return (mask & MASK_HIDE_CONCRETE) != 0;
  }

  protected void updateMask() {
    menuProp.setEnabled(hasProperties());
    menuPage.setEnabled(hasMove_to_page());
    menuLayout.setEnabled(hasLayout());
    menuDelete.setEnabled(hasDelete());
    menuVisible.setEnabled(hasVisible());
    menuInvisible.setEnabled(hasInvisible());
    concreteValueOn.setEnabled(hasConcreteValueOn());
    concreteValueOff.setEnabled(hasConcreteValueOff());
    menuAutoSize.setEnabled(hasAutoSize());
    menuBring.trySetVisible(hasBring_to_this_page());
    menuResetHint.setEnabled(hasReset_placement());
    menuBringAttr.trySetVisible(hasBring_attributes());
    menuBringRef.trySetVisible(hasBring_references());
    menuBringSub.trySetVisible(hasBring_subtypes());
    menuBringSup.trySetVisible(hasBring_supertypes());
    menuBringSome.setVisible(menuBringAttr.isVisible() || menuBringRef.isVisible() ||
                             menuBringSup.isVisible() || menuBringSub.isVisible());
    menuJustify.setVisible(hasAlign());
    menuArrange.setVisible(hasAlign());
    menuBundle.setEnabled(hasBundle());
    menuUnbundle.setEnabled(hasUnbundle());
    menuGrouping.setVisible(menuBundle.isEnabled() || menuUnbundle.isEnabled());
    updateAll(true);
  }

  /** 
   * Displays the popup menu at the position x,y in the coordinate space of the
   * component invoker.
   *
   * @param invoker the component in whose space the popup menu is to appear
   * @param x the x coordinate in invoker's coordinate space at which the popup
   *   menu is to be displayed
   * @param y the y coordinate in invoker's coordinate space at which the popup
   *   menu is to be displayed
   *
  public void show(Component invoker, int x, int y) {
    menuBringAttr.clear();
    menuBringRef.clear();
    menuBringSub.clear();
    menuBringSup.clear();
    updateMask();
    super.show(invoker, x, y);
  }

  public void show(Component invoker, int x, int y, int menuMask) {
    setMenuMask(menuMask);
    show(invoker, x, y);
  }

  public void show(MouseEvent e, int menuMask) {
    show(e.getComponent(), e.getX(), e.getY(), menuMask);
  }
*/
  void menuProp_actionPerformed() {
    prop.getSelectionHandler().showPropertiesMenu();
  }

  void menuResetHint_actionPerformed() {
    Iterator it = prop.getSelectionHandler().getSelected().iterator();
    while (it.hasNext()) {
      Object obj = it.next();
      if (obj instanceof AbstractEGRelation) {
        AbstractEGRelation rel = (AbstractEGRelation)obj;
//        rel.setHintPoint(rel.getHint1());
//        rel.setNamePlace(rel.getHint2());
        if (obj instanceof EGRelationTree)
        	((EGRelationTree)obj).setSpecialDraw(false);
        rel.setHint1(new Point(0, 0));
        rel.setHint2(new Point(0, 0));
        rel.update(2);
        prop.handler().repaint(false);
      }
    }
//    prop.getSelectionHandler().setSelected(this, new ArrayList(0));
  }

  void menuDelete_actionPerformed() {
    prop.getSelectionHandler().deleteSelected();
  }

  protected DialogSelectPageList dialogSelectPage = null;

  void menuPage_actionPerformed() {
    Command command = prop.handler().getRunningCommand();
    if (command instanceof SelectCommand) {
      int oldPage = prop.handler().getPage();
      if (dialogSelectPage == null) dialogSelectPage = new DialogSelectPageList(null);
      int page = dialogSelectPage.open(oldPage, EGToolKit.resolvePageNames(prop, true));
      if (page != oldPage) {
        ((SelectCommand)command).changePageSelected(page);
      }
    }
  }

  protected LayoutCommand layoutCommand = new LayoutCommand(this);

  void menuLayout_actionPerformed() {
    prop.handler().startCommand(layoutCommand);
  }
  
  void menuVisibility_actionPerformed(boolean visible) {
    GC g = prop.getPainting().getLastGraphics();
    Iterator iter = getSelected().iterator();
    while (iter.hasNext()) {
      Object item = iter.next();
      if (item instanceof AbstractEGObject) {
        AbstractEGObject obj = (AbstractEGObject)item;
        if (obj.canBeInvisible()) {
          obj.setVisible(visible ? AbstractEGObject.VISIBLE_TRUE : AbstractEGObject.VISIBLE_FALSE);
          obj.draw(g);
          obj.update(2);
        }
      }
    }
//    prop.getSelectionHandler().setSelected(this, SelectCommand.EMPTY_SELECTION);
    prop.handler().update(prop.handler().getPage());
    prop.handler().repaint(true);
  }
  
  void menuShowConcreteValue_actionPerformed(boolean show) {
    GC g = prop.getPainting().getLastGraphics();
    Iterator iter = getSelected().iterator();
    while (iter.hasNext()) {
      Object item = iter.next();
      if (item instanceof AbstractEGObject) {
        AbstractEGObject obj = (AbstractEGObject)item;
        obj.setVisible(show ? AbstractEGObject.VISIBLE_UNSET : AbstractEGObject.VISIBLE_TRUE);
        obj.draw(g);
        obj.update(2);
      }
    }
    prop.getSelectionHandler().setSelected(this, SelectCommand.EMPTY_SELECTION);
    prop.handler().update(prop.handler().getPage());
    prop.handler().repaint(true);
  }
  
  void menuAutoSize_actionPerformed() {
    Iterator iter = getSelected().iterator();
    GC g = prop.getPainting().getLastGraphics();
    while (iter.hasNext()) {
      Object item = iter.next();
      if (item instanceof AbstractEGBox) {
        ((AbstractEGObject)item).setLabelNew(true);
        ((AbstractEGBox)item).setSize(new Point(0, 0));//AbstractEGBox.basicWidth, AbstractEGBox.basicHeight));
        ((AbstractEGBox)item).draw(g);
        ((AbstractEGBox)item).update(1);
      }
    }
//    prop.getSelectionHandler().setSelected(this, SelectCommand.EMPTY_SELECTION);
    prop.handler().update(prop.handler().getPage());
    prop.handler().repaint(true);
  }
/*
  void menuBring_actionPerformed(ActionEvent e) {
    try {
      EGPage egPage = (EGPage)prop.getSelectionHandler().getSelected().iterator().next();
      Vector items = new Vector(1);
      if (egPage instanceof EGPageTo) {
        Iterator iter = ((EGPageTo)egPage).getReferenced().getWires().iterator();
        while (iter.hasNext()) {
          Wire wire = (Wire)iter.next();
          if (wire.isAttribute()) items.addAll(wire.getRelation().getChilds());
        }
      } else {
        Iterator iter = ((EGPageFrom)egPage).getReferences().iterator();
        while (iter.hasNext()) {
          Iterator itw = ((EGPageTo)iter.next()).getWires().iterator();
          while (itw.hasNext()) {
            Wire wire = (Wire)itw.next();
            if (!wire.isAttribute()) items.add(wire.getRelation().getParent());
          }
        }
      }
      prop.getSelectionHandler().setSelected(e.getSource(), items);
      Point loc = egPage.getLocation();
      loc.translate(-(int)egPage.getSize().getWidth()/2, -(int)egPage.getSize().getHeight());
      layoutCommand.setInsets(loc);
      prop.handler().startCommand(layoutCommand);
    }
    catch (Exception ex) {
		ConsolePlugin.log(ex);
      ex.printStackTrace();
    }
  }
/**/
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
	 */

  
	public void menuAboutToShow(IMenuManager manager) {
//System.out.println("MENU about to show");	
		setMenuMask(prop.getSelectionHandler().getPopupMask());
		updateMask();
		prop.getSelectionHandler().mouseUp(null);
	}
	
	protected Rectangle topAligningRect(Collection selection) {
		Rectangle top = null;
		Iterator iter = selection.iterator();
		while (iter.hasNext()) {
			Object item = iter.next();
			if (item instanceof AbstractEGBox) {
				Rectangle bounds = ((AbstractEGBox)item).getBounds();
				if ((top == null)||(top.y > bounds.y)) top = bounds;
			}
		}
		return top;
	}
	
	protected Rectangle leftAligningRect(Collection selection) {
		Rectangle left = null;
		Iterator iter = selection.iterator();
		while (iter.hasNext()) {
			Object item = iter.next();
			if (item instanceof AbstractEGBox) {
				Rectangle bounds = ((AbstractEGBox)item).getBounds();
				if ((left == null)||(left.x > bounds.x)) left = bounds;
			}
		}
		return left;
	}
	
	public void menuJustifyLeft_actionPerformed() {
		Collection selection = getSelected();
		Rectangle top = topAligningRect(selection);
		Iterator iter = selection.iterator();
		while (iter.hasNext()) {
			Object item = iter.next();
			if (item instanceof AbstractEGBox) {
				Rectangle bounds = ((AbstractEGBox)item).getBounds(); 
				((AbstractEGBox)item).setLocation(new Point(top.x, bounds.y));
		        ((AbstractEGBox)item).update(2);
			}
		}
//	    prop.getSelectionHandler().setSelected(this, SelectCommand.EMPTY_SELECTION);
//	    prop.getSelectionHandler().setSelected(this, selection);
	    prop.handler().repaint(true);
	}
	
	public void menuJustifyCenterH_actionPerformed() {
		Collection selection = getSelected();
		Rectangle top = topAligningRect(selection);
		Iterator iter = selection.iterator();
		while (iter.hasNext()) {
			Object item = iter.next();
			if (item instanceof AbstractEGBox) {
				Rectangle bounds = ((AbstractEGBox)item).getBounds(); 
				((AbstractEGBox)item).setLocation(new Point(top.x + top.width / 2 - bounds.width / 2, bounds.y));
		        ((AbstractEGBox)item).update(2);
			}
		}
//	    prop.getSelectionHandler().setSelected(this, SelectCommand.EMPTY_SELECTION);
//	    prop.getSelectionHandler().setSelected(this, selection);
	    prop.handler().repaint(true);
	}
	
	public void menuJustifyRight_actionPerformed() {
		Collection selection = getSelected();
		Rectangle top = topAligningRect(selection);
		Iterator iter = selection.iterator();
		while (iter.hasNext()) {
			Object item = iter.next();
			if (item instanceof AbstractEGBox) {
				Rectangle bounds = ((AbstractEGBox)item).getBounds(); 
				((AbstractEGBox)item).setLocation(new Point(top.x + top.width - bounds.width, bounds.y));
		        ((AbstractEGBox)item).update(2);
			}
		}
//	    prop.getSelectionHandler().setSelected(this, SelectCommand.EMPTY_SELECTION);
//	    prop.getSelectionHandler().setSelected(this, selection);
	    prop.handler().repaint(true);
	}
	
	public void menuJustifyTop_actionPerformed() {
		Collection selection = getSelected();
		Rectangle left = leftAligningRect(selection);
		Iterator iter = selection.iterator();
		while (iter.hasNext()) {
			Object item = iter.next();
			if (item instanceof AbstractEGBox) {
				Rectangle bounds = ((AbstractEGBox)item).getBounds(); 
				((AbstractEGBox)item).setLocation(new Point(bounds.x, left.y));
		        ((AbstractEGBox)item).update(2);
			}
		}
//	    prop.getSelectionHandler().setSelected(this, SelectCommand.EMPTY_SELECTION);
//	    prop.getSelectionHandler().setSelected(this, selection);
	    prop.handler().repaint(true);
	}
	
	public void menuJustifyCenterV_actionPerformed() {
		Collection selection = getSelected();
		Rectangle left = leftAligningRect(selection);
		Iterator iter = selection.iterator();
		while (iter.hasNext()) {
			Object item = iter.next();
			if (item instanceof AbstractEGBox) {
				Rectangle bounds = ((AbstractEGBox)item).getBounds(); 
				((AbstractEGBox)item).setLocation(new Point(bounds.x, left.y + left.height / 2 - bounds.height / 2));
		        ((AbstractEGBox)item).update(2);
			}
		}
//	    prop.getSelectionHandler().setSelected(this, SelectCommand.EMPTY_SELECTION);
//	    prop.getSelectionHandler().setSelected(this, selection);
	    prop.handler().repaint(true);
	}
	
	public void menuJustifyBottom_actionPerformed() {
		Collection selection = getSelected();
		Rectangle left = leftAligningRect(selection);
		Iterator iter = selection.iterator();
		while (iter.hasNext()) {
			Object item = iter.next();
			if (item instanceof AbstractEGBox) {
				Rectangle bounds = ((AbstractEGBox)item).getBounds(); 
				((AbstractEGBox)item).setLocation(new Point(bounds.x, left.y + left.height - bounds.height));
		        ((AbstractEGBox)item).update(2);
			}
		}
//	    prop.getSelectionHandler().setSelected(this, SelectCommand.EMPTY_SELECTION);
//	    prop.getSelectionHandler().setSelected(this, selection);
	    prop.handler().repaint(true);
	}
	
	private static class ArrangeHComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			int x1 = ((AbstractEGBox)o1).getLocation().x;
			int x2 = ((AbstractEGBox)o2).getLocation().x;
			int out = 0;
			if (x1 < x2) out = -1;
			else out = 1;
			return out;
		}
	}

	private static class ArrangeVComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			int y1 = ((AbstractEGBox)o1).getLocation().y;
			int y2 = ((AbstractEGBox)o2).getLocation().y;
			int out = 0;
			if (y1 < y2) out = -1;
			else out = 1;
			return out;
		}
	}

	public void menuArrangeH_actionPerformed() {
		Set selection = getSelected();
		Iterator iter = selection.iterator();
		int size = 0;
		Collection boxes = new TreeSet(new ArrangeHComparator()); 
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		while (iter.hasNext()) {
			Object item = iter.next();
			if (item instanceof AbstractEGBox) {
				Rectangle bounds = ((AbstractEGBox)item).getBounds();
				size += bounds.width;
				boxes.add(item);
				if (bounds.x < min) min = bounds.x;
				if (bounds.x + bounds.width > max) max = bounds.x + bounds.width;
			}
		}
		double space = (double)(max - min - size) / (double)(boxes.size() - 1); // can be negative
		
		iter = boxes.iterator();
		int i = 0;
		size = min;
		while (iter.hasNext()) {
			AbstractEGBox box = (AbstractEGBox)iter.next();
			Rectangle bounds = box.getBounds();
			box.setLocation(new Point((int)(size + space * i), bounds.y));	
			box.update(2);
			size += bounds.width;
			i++;
		}
//	    prop.getSelectionHandler().setSelected(this, SelectCommand.EMPTY_SELECTION);
//	    prop.getSelectionHandler().setSelected(this, selection);
	    prop.handler().repaint(true);
	}
	
	public void menuArrangeV_actionPerformed() {
		Set selection = getSelected();
		Iterator iter = selection.iterator();
		int size = 0;
		Collection boxes = new TreeSet(new ArrangeVComparator()); 
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		while (iter.hasNext()) {
			Object item = iter.next();
			if (item instanceof AbstractEGBox) {
				Rectangle bounds = ((AbstractEGBox)item).getBounds();
				size += bounds.height;
				boxes.add(item);
				if (bounds.y < min) min = bounds.y;
				if (bounds.y + bounds.height > max) max = bounds.y + bounds.height;
			}
		}
		double space = (double)(max - min - size) / (double)(boxes.size() - 1); // can be negative
		
		iter = boxes.iterator();
		int i = 0;
		size = min;
		while (iter.hasNext()) {
			AbstractEGBox box = (AbstractEGBox)iter.next();
			Rectangle bounds = box.getBounds();
			box.setLocation(new Point(bounds.x, (int)(size + space * i)));	
			box.update(2);
			size += bounds.height;
			i++;
		}
//	    prop.getSelectionHandler().setSelected(this, SelectCommand.EMPTY_SELECTION);
//	    prop.getSelectionHandler().setSelected(this, selection);
	    prop.handler().repaint(true);
	}
	
	// Grouping actions:
	public void menuBundle_actionPerformed() {
/*		Iterator iter = getSelected().iterator();
		AbstractEGRelation first = null;
		while (iter.hasNext()) {
			Object obj = iter.next();
			if (obj instanceof AbstractEGRelation) {
				if (first == null) first = (AbstractEGRelation)obj;
				else {
					first.addGroup(((AbstractEGRelation)obj).getGroup());
					((AbstractEGRelation)obj).update();
				}
			}
		}*/
        menuBundleBox_actionPerformed();
//	    prop.getSelectionHandler().setSelected(this, SelectCommand.EMPTY_SELECTION);
	}

	public void menuUnbundle_actionPerformed() {
		Iterator iter = getSelected().iterator();
		while (iter.hasNext()) {
			Object obj = iter.next();
			if (obj instanceof AbstractEGRelation) {
				RelationGroup group = ((AbstractEGRelation)obj).getGroup();
				if (group.size() > 1) {
					((AbstractEGRelation)obj).setGroup(new RelationGroup(1));
					((AbstractEGRelation)obj).update();
				}
			}
		}
        menuUnbundleBox_actionPerformed();
//	    prop.getSelectionHandler().setSelected(this, SelectCommand.EMPTY_SELECTION);
	}

	public void menuBundleBox_actionPerformed() {
		Hashtable map = new Hashtable();
		HashSet delete = new HashSet();
		Iterator iter = getSelected().iterator();
		while (iter.hasNext()) {
			Object obj = iter.next();
			if (obj instanceof EGPageRef) {
				EGPageRef ref = (EGPageRef)obj;
				if (map.containsKey(ref.getBundleReferenceKey())) {
					EGPageRef ref1 = (EGPageRef)map.get(ref.getBundleReferenceKey());
					ref1.addReferencedBox(ref);
					delete.add(ref);
				} else {
					map.put(ref.getBundleReferenceKey(), ref);
				}
			} else if (obj instanceof AbstractEGRelation) {
				AbstractEGRelation rel = (AbstractEGRelation)obj;
				RelationBundleGroup bundle = new RelationBundleGroup(rel);
				if (map.containsKey(bundle)) {
					((AbstractEGRelation)map.get(bundle)).addGroup(rel.getGroup());
				} else {
					map.put(bundle, rel);
				}
			}
		}
		iter = delete.iterator();
		while (iter.hasNext()) {
			AbstractDraw item = (AbstractDraw)iter.next();
			prop.handler().drawableRemove(item);
			item.eliminate();
		}
	    GC g = prop.getPainting().getLastGraphics();
		iter = map.values().iterator();
		while (iter.hasNext()) {
			AbstractDraw item = ((AbstractDraw)iter.next());
			item.draw(g);
			item.update(2);
		}
//	    prop.getSelectionHandler().setSelected(this, SelectCommand.EMPTY_SELECTION);
	    prop.handler().update(prop.handler().getPage());
	    prop.handler().repaint(true);
	}
	
	private static class RelationBundleGroup {
		public AbstractEGRelation rel;
		private int hash;
		
		public RelationBundleGroup(AbstractEGRelation rel) {
			this.rel = rel;
			hash = rel.getParent().hashCode() + rel.getType();
		}
		
		public boolean equals(Object obj) {
//System.err.println("equals:" + this + " <--> " + obj);			
			if (obj instanceof RelationBundleGroup) {
				AbstractEGRelation rel = ((RelationBundleGroup)obj).rel;
				return (this.rel == rel) ||
					((this.rel.getParent() == rel.getParent()) &&
					(this.rel instanceof EGRelationSimple) &&
					(rel instanceof EGRelationSimple) &&
					(this.rel.getType() == rel.getType()) &&
					((rel.getParent() instanceof EGSchema) || 
							(rel.getType() == AbstractEGRelation.TYPE_INHERITANCE)));
			} else
				return super.equals(obj);
		}
		
		public int hashCode() {
//System.err.println("hash:" + this + " : " + hash);			
			return hash;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return rel + " | " + hash;
		}
	}

	public void menuUnbundleBox_actionPerformed() {
		Iterator iter = getSelected().iterator();
		Set selection = getSelected();
		while (iter.hasNext()) {
			Object obj = iter.next();
			if (obj instanceof EGPageRef) {
				EGPageRef ref = (EGPageRef)obj;
				Point location = ref.getCenterPoint();
				Iterator wit = ref.getWires().iterator();
				while (wit.hasNext()) {
					AbstractEGRelation rel = ((Wire)wit.next()).getRelation();
					EGPageRef ref1 = ref.getOneFor(rel);
					if (ref1 != ref) {
						ref1.setLocation(location);
						prop.handler().drawableAddR(ref1);
						location = ref1.getCenterPoint();
						selection.add(ref1);
					}
					rel.update(1);
				}
				ref.update(2);
			}
		}
//	    prop.getSelectionHandler().setSelected(this, SelectCommand.EMPTY_SELECTION);
		prop.getSelectionHandler().setSelected(this, selection);
	    prop.handler().repaint(true);
	}
	
	public Menu createContextMenu(Control parent) {
		Menu menu = super.createContextMenu(parent);
		WorkbenchHelp.setHelp(menu, SdaieditPlugin.ID_SDAIEDIT + ".MainContextMenuContextId");
		return menu;
	}
	
	public Menu createMenuBar(Decorations parent) {
		Menu menu = super.createMenuBar(parent);
		WorkbenchHelp.setHelp(menu, SdaieditPlugin.ID_SDAIEDIT + ".MainContextMenuContextId");
		return menu;
	}
}
