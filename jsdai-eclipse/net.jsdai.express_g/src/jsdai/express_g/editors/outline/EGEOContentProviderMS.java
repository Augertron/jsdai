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

package jsdai.express_g.editors.outline;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import jsdai.express_g.common.Resources;
import jsdai.express_g.common.TableTreeContentProvider;
import jsdai.express_g.common.TableTreeObject;
import jsdai.express_g.exp2.Paging;
import jsdai.express_g.exp2.eg.AbstractEGBox;
import jsdai.express_g.exp2.eg.AbstractEGRelation;
import jsdai.express_g.exp2.eg.EGConstraint;
import jsdai.express_g.exp2.eg.EGEntity;
import jsdai.express_g.exp2.eg.EGEntityRef;
import jsdai.express_g.exp2.eg.EGEnumerated;
import jsdai.express_g.exp2.eg.EGPage;
import jsdai.express_g.exp2.eg.EGPageRefInterschema;
import jsdai.express_g.exp2.eg.EGPageRefToSimple;
import jsdai.express_g.exp2.eg.EGSchema;
import jsdai.express_g.exp2.eg.EGSelect;
import jsdai.express_g.exp2.eg.EGSimple;
import jsdai.express_g.exp2.eg.IDefined_type;
import jsdai.express_g.exp2.eg.Wire;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.event.DrawListener;
import jsdai.express_g.exp2.ui.event.PageChangeEvent;
import jsdai.express_g.exp2.ui.event.PageListener;
import jsdai.express_g.exp2.ui.event.SelectionEvent;
import jsdai.express_g.exp2.ui.event.SelectionListener;
import jsdai.express_g.exp2.ui.event.VectorListener;

/**
 * @author Mantas Balnys
 *
 *	Express-G Editor Outline content provider with multiple schemas
 */
public class EGEOContentProviderMS extends TableTreeContentProvider implements VectorListener, PageListener, DrawListener, SelectionListener, ISelectionChangedListener {
	private PropertySharing prop;
	private AbstractTreeViewer tree;
	
	private TableTreeObject root;
	private TableTreeObject schema;
	private TableTreeObject defined;
	private TableTreeObject entity;
	private TableTreeObject constraint;
	
	private Map mapNodes = new Hashtable();
	private Map mapSchemas = new Hashtable();
	
	public static final String NAME_DEFINED = "Defined types";
	public static final String NAME_ENTITY = "Entity types";
	public static final String NAME_CONSTRAINT = "Constraint types";

	/**
	 * 
	 */
	public EGEOContentProviderMS(PropertySharing prop, AbstractTreeViewer tree) {
		super();
		setInvisibleRoot(true);
		this.prop = prop;
		this.tree = tree;
		
		if (tree instanceof TableTreeViewer) {
			Table table = ((TableTreeViewer)tree).getTableTree().getTable();
		    table.setLinesVisible(true);
		    String[] cols = EGEOTreeItem.COLUMNS;
		    int width = 0;
			TableLayout layout = new TableLayout();
		    for (int i = 0; i < cols.length; i++) {
		    	TableColumn column = new TableColumn(table, i<2?SWT.LEFT:SWT.CENTER);
		    	column.setText(cols[i]);
				ColumnLayoutData cLayout;
		    	if (i != 0) {
		    		cLayout = new ColumnWeightData(40, true);
//		    		column.setResizable(true);
//			        column.pack();
		    	} else {
		    		cLayout = new ColumnWeightData(20, false);
//		    		column.setResizable(false);
//		    		column.setWidth(table.getItemHeight() * 3 + 3);
		    	}
//		        width += column.getWidth();
				layout.addColumnData(cLayout);
		    }
		    table.getColumn(EGEOTreeItem.INDEX_NAME).setWidth(width);
		    table.setHeaderVisible(true);
			table.setLayout(layout);
		}
		
		root = new TableTreeObject();
		if (prop.getName() != null) {
			schema = new EGEOTreeItem(prop, root, "", "6", Resources.SCHEMA_CURRENT);
			mapSchemas.put(prop.getName(), schema);
			defined = new EGEOTreeItem(prop, schema, NAME_DEFINED, "2", Resources.DEFINED);
			entity = new EGEOTreeItem(prop, schema, NAME_ENTITY, "3", Resources.ENTITY);
			constraint = new EGEOTreeItem(prop, schema, NAME_CONSTRAINT, "4", Resources.CONSTRAINT);
		}
		setRoot(root);
		update();

		prop.handler().addVectorListener(this);
		prop.handler().addPageListener(this);
		prop.handler().addDrawListener(this);
		prop.getSelectionHandler().addSelectionListener(this);
		
		// external non ContentProvider operations:
		tree.setContentProvider(this);
		tree.setInput(prop);

		if (prop.getName() != null) {
			tree.setSelection(new StructuredSelection(entity), true);
//			tree.setExpandedState(schema, true);
		}
	}
	
	public void update() {
		Iterator iter = prop.handler().drawableIterator();
		while (iter.hasNext()) {
			Object item = iter.next();
			if (item instanceof AbstractEGBox)
				internalAdd(item);
		}	 
		iter = prop.handler().drawableIterator();
		while (iter.hasNext()) {
			Object item = iter.next();
			if (item instanceof AbstractEGRelation)
				internalAdd(item);
		}	 
	}
	
	public void dispose() {
		prop.handler().removeVectorListener(this);
		prop.handler().removePageListener(this);
		prop.handler().removeDrawListener(this);
	}
	
	protected TableTreeObject internalAdd(Object object) {
		TableTreeObject node = null;
	    if (object instanceof EGEntity) {
	        node = new EGEOTreeItem(prop, object);
	        entity.addChild(node);
	        mapNodes.put(object, node);
	    } else
	    if (object instanceof IDefined_type) {
	    	node = new EGEOTreeItem(prop, object);
	        defined.addChild(node);
	        mapNodes.put(object, node);
	    } else
	    if (object instanceof EGSchema) {
	    	String name = ((EGSchema)object).getName();
	    	EGEOTreeItem ref_schema = (EGEOTreeItem)mapSchemas.get(name);
	    	if (ref_schema == null) {
	    		ref_schema = new EGEOTreeItem(prop, object);
	    		ref_schema.setSortName("5");
			    root.addChild(ref_schema);
	    		mapSchemas.put(name, ref_schema);
	    	} else {
	    		ref_schema.setUserObject(object);
	    		root.addChild(ref_schema);
	    	}
	        mapNodes.put(object, ref_schema);
	    } else
	    if (object instanceof EGEntityRef) {
	    	node = new EGEOTreeItem(prop, object);
	    	String schema_name = ((EGEntityRef)object).getSchemaName();
	    	EGEOTreeItem parent_schema = (EGEOTreeItem)mapSchemas.get(schema_name);
	    	if (parent_schema == null) {
	    		parent_schema = new EGEOTreeItem(prop);
	    		parent_schema.setSortName("5");
//			    root.addChild(parent_schema);
	    		mapSchemas.put(schema_name, parent_schema);
	    	}
    		parent_schema.addChild(node);
    		mapNodes.put(object, node);
	    } else
	    if (object instanceof EGSimple) {

	    } else
	    if (object instanceof EGEnumerated) {

	    } else
	    if (object instanceof EGSelect) {

	    } else
	    if (object instanceof AbstractEGRelation) {
	    	AbstractEGRelation relation = (AbstractEGRelation)object;
	        AbstractEGBox parent = relation.getParent();
	        if ((relation.getType() == AbstractEGRelation.TYPE_AGREGATION)&&(parent instanceof EGEntity)) {
	        	TableTreeObject parentNode = (TableTreeObject)mapNodes.get(parent);
	        	if (parentNode != null) {
	        		node = new EGEOTreeItem(prop, object);
//			        node.setImage(Resources.getImageAttribute());
	        		parentNode.addChild(node);
	        		mapNodes.put(relation, node);
	        	} else {
//	        		System.err.println("node missing for " + parent);
	        	}
	        }
	    } else
	    if (object instanceof EGPage) {

	    } else
	    if (object instanceof EGConstraint) {
	        node = new EGEOTreeItem(prop, object);
	        constraint.addChild(node);
	        mapNodes.put(object, node);
	    }
	    return node;
	}
	
	public void vectorAdded(Object object) {
	    TableTreeObject node = internalAdd(object);
	    if ((node != null)&&(!prop.isMultiUpdate())) {
			repaint((TableTreeObject)node.getParent());
		}	    	
	}
	
	public void vectorRemoved(Object object) {
		TableTreeObject parent = null;
	    if ((object instanceof EGEntity)||(object instanceof IDefined_type)||(object instanceof EGEntityRef)||(object instanceof EGConstraint)) {
	    	TableTreeObject child = (TableTreeObject)mapNodes.remove(object);
	        if (child != null) {
	        	parent = (TableTreeObject)child.getParent();
	        	child.setParent(null);
	        }
	    } else
	    if (object instanceof EGSchema) {
	    	TableTreeObject child = (TableTreeObject)mapSchemas.remove(((EGSchema)object).getName());
		    mapNodes.remove(object);
		    if (child != null) {
		       	parent = (TableTreeObject)child.getParent();
		       	child.clearChildren();
		       	child.setParent(null);
		    }
		} else
	    if (object instanceof AbstractEGRelation) {
	    	AbstractEGRelation relation = (AbstractEGRelation)object;
	        AbstractEGBox parentBox = relation.getParent();
	        if ((relation.getType() == AbstractEGRelation.TYPE_AGREGATION)&&(parentBox instanceof EGEntity)) {
	        	TableTreeObject node = (TableTreeObject)mapNodes.remove(relation);
	        	if (node != null) {
	        		parent = (TableTreeObject)node.getParent();
	        		node.setParent(null);
	        	}
	        }
	 	} else
	    if (object == null) {
	      	entity.clearChildren();
	        defined.clearChildren();
	        mapNodes.clear();
	        mapSchemas.clear();
	        if (prop.getName() != null)
	        	mapSchemas.put(prop.getName(), schema);
	        parent = root;
	    }
	    
	    if (parent != null) {
	    	repaint(parent);
		}	    	
	}
	
	public void pageChanged(PageChangeEvent e) {
//		root.setText(prop.getName());
		repaint();
	}

	public Point getCenterLocation() {
		return new Point(0, 0);
	}
	
	public void repaint() {
		repaint(null);
	}
	
	public void repaint(final TableTreeObject parent) {
		if (!prop.isMultiUpdate()) { 
			if (!tree.getControl().isDisposed()) {
				if (parent == null) {
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							tree.refresh();
						}
					});
				} else {
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							tree.refresh(parent);
						}
					});
				}
			}
//			EGEPlugin.getDefault().getWorkbench().getDisplay().asyncExec(new TreeRefresh(parent));
		}
	}

	public void setCenterLocation(Point location) {
	}
	
	public void updateSize(boolean shrink) {
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionProvider#setSelection(org.eclipse.jface.viewers.ISelection)
	 */
	public void setSelection(ISelection selection) {
//System.out.println("set selection");
		if (selection instanceof StructuredSelection) {
			Iterator selit = ((StructuredSelection)selection).iterator();
			setSelection(selit);
//			EGEPlugin.getDefault().getWorkbench().getDisplay().syncExec(new TreeSetSelection(new StructuredSelection(list)));
		}
	}
	
	/**
	 * filtering what to add to tree selection
	 * @param list
	 * @param item
	 */
	private void addSelection(List list, Object item) {
		if (item instanceof EGPageRefInterschema)
			addSelection(list, ((EGPageRefInterschema)item).getReferenced());
		else if (item instanceof EGPageRefToSimple) {
			Iterator wit = ((EGPageRefToSimple)item).getWires().iterator();
			while (wit.hasNext()) {
				Wire wire = (Wire)wit.next();
				if (!wire.isAttribute())
					addSelection(list, wire.getRelation());
			}
		} else {
			Object object = mapNodes.get(item);
			if (object != null) {
				list.add(object);
			}
		}
	}
	
	private void addSelection(List list, Iterator selit) {
		while (selit.hasNext()) {
			Object item = selit.next();
			addSelection(list, item);
		}
	}
	
	private void setSelection(Iterator selit) {
		ArrayList list = new ArrayList();
		addSelection(list, selit);
		if (!tree.getControl().isDisposed()) {
			tree.getControl().getDisplay().syncExec(new SetTreeSelection(new StructuredSelection(list)));
			repaint();
		}
	}
	
	private class SetTreeSelection implements Runnable {
		private ISelection selection;
		
		/**
		 * @param selection
		 */
		public SetTreeSelection(ISelection selection) {
			this.selection = selection;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			tree.setSelection(selection, true);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
	 */
	public ISelection getSelection() {
		GetTreeSelection selection = new GetTreeSelection();
		tree.getControl().getDisplay().syncExec(selection);
//System.out.println("get selection");
		return new StructuredSelection(getSelection((StructuredSelection)selection.getSelection()).toArray());
	}
	
	private class GetTreeSelection implements Runnable {
		private ISelection selection;
		
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			selection = tree.getSelection();
		}
		
		public ISelection getSelection() {
			return selection;
		}
	}
	
	/**
	 * filtering what to add to Editor selection (real selection)
	 * @param selectionTree
	 * @return
	 */
	private Set getSelection(StructuredSelection selectionTree) {
		HashSet list = new HashSet(selectionTree.size());
		Iterator selit = selectionTree.iterator();
		while (selit.hasNext()) {
			Object object = ((TableTreeObject)selit.next()).getUserObject();
			if ((object instanceof EGEntity) || (object instanceof IDefined_type) || 
					(object instanceof AbstractEGRelation) || (object instanceof EGSchema) ||
					(object instanceof EGConstraint)) {
				list.add(object);
			}
		}
		return list;
	}

	private boolean selectingTree = false;
	
	/**
	 * invoked when selection on editor changes
	 */
	public void selectionChanged(SelectionEvent e) {
		if (!e.getSource().equals(tree)) {
//System.out.println("sel changed");
			selectingTree = true;
			setSelection(e.getNewSel().iterator());
			selectingTree = false;
		}
	}
	
	/**
	 * invoked when tree selection changes
	 */
	public void selectionChanged(SelectionChangedEvent event) {
		ISelection selection = event.getSelection();
		if ((!selectingTree)&&(selection instanceof StructuredSelection)) {
			Set list = getSelection((StructuredSelection)selection);
			prop.getSelectionHandler().setSelected(this, list);
			// change page to selected item page
			if (list.size() == 1) {
				Object obj = list.iterator().next();
				if (obj instanceof Paging /*&& obj != lastSelected*/) {
					int page = ((Paging)obj).getPage();
					if (page > 0)
						prop.handler().setPage(page);
				}
			}
			
			prop.getPainting().redraw();
				
		}
	}
	
	/*
	private class TreeSetSelection implements Runnable {
		private ISelection selection;
		
		public TreeSetSelection(ISelection selection) {
			this.selection = selection;	
		}
		
		public void run() {
			if (!tree.getControl().isDisposed()) {
				tree.setSelection(selection, true);
			}
		}
	}
	
	private class TreeRefresh implements Runnable {
		private Object element;
		
		public TreeRefresh(Object element) {
			this.element = element;	
		}
		
		public void run() {
			if (!tree.getControl().isDisposed()) {
				if (element == null) {
					tree.refresh();
				} else {
					tree.refresh(element);
				}
			}
		}
	}
	/**/
}
