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

package jsdai.express_g.editors;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import jsdai.SExpress_g_schema.APage;
import jsdai.SExpress_g_schema.EGraphics_diagram;
import jsdai.SExpress_g_schema.EPage;
import jsdai.express_g.SdaieditPlugin;
import jsdai.express_g.editors.outline.FakeWorkbenchPart;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;

/**
 * @author Mantas Balnys
 *
 */
public class EditorContainer extends Composite implements ISelectionChangedListener,
		IPropertyListener, IPageChangeListener {
	private StackLayout layout;
	
//	private ListenerList listeners = new ListenerList();
	
	private SdaiEditor sdaiEditor;
	
	private Hashtable internalEditors = new Hashtable();
	
	/**
	 * table of pages
	 * element (see Outline) <-> Composite
	 */
	private Hashtable pages = new Hashtable();

	/**
	 * @param parent
	 * @param style
	 */
	public EditorContainer(Composite parent, int style, SdaiEditor sdaiEditor) {
		super(parent, style);
		layout = new StackLayout();
		this.setLayout(layout);
		this.sdaiEditor = sdaiEditor;
	}
	
	/**
	 * can be invoked more than once, each time container must restart fully
	 *
	 */
	public void init() {
		clear();

	}
	
	public void clear() {
		Iterator iter = pages.values().iterator();
		while (iter.hasNext()) {
			Composite page = (Composite)iter.next();
			if (!page.isDisposed()) {
//System.out.println("NOT DISPOSED PAGE " + page);
				page.dispose();
			} 
		}
		pages.clear();
		internalEditors.clear();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	public void selectionChanged(SelectionChangedEvent event) {
		Object selection = ((IStructuredSelection)event.getSelection()).getFirstElement();
//System.out.println("<0XO><01>selection: " + selection);
// debugging block
/*
if (selection instanceof ModelHandler) {
  try {
	  ModelHandler debug_mh = (ModelHandler)selection;
	  SdaiModel debug_model = debug_mh.getModel();
	  System.out.println("<D00>model: " + debug_model.getName());
	  EGraphics_diagram debug_diagram = debug_mh.getDiagram_definition();
	  if (debug_diagram.testDic_schema(null)) {
		  System.out.println("<D00A>diagram schema OK: " + debug_diagram.getDic_schema(null).getName(null));
	  } else {
		  System.out.println("<D00B>diagram schema UNSET");
	  }
	  System.out.println("<D01>diagram comment: " + debug_diagram.getComment(null));
	  APage debug_pages = (APage)debug_model.getInstances(EPage.class);
	  SdaiIterator i_pages = debug_pages.createIterator();
	  while (i_pages.next()) {
		  EPage debug_page = (EPage)debug_pages.getCurrentMember(i_pages);
		  if (debug_page.testDiagram(null)) {
			  if (debug_page.getDiagram(null) != debug_diagram) {
				  System.out.println("<D02A>page diagram OK");
				  continue;
			  }	
		  } else {
			 // mandatory attribute unset 
			  System.out.println("<D02B>page diagram UNSET");
		  }
		  String debug_page_name = "_NO_NAME_";
		  int debug_page_number = -1;
		  if (debug_page.testComment(null)) {
			  debug_page_name = debug_page.getComment(null);
		  }
		  if (debug_page.testPage_number(null)) {
			  debug_page_number = debug_page.getPage_number(null);
		  }
		  System.out.println("<D03>page comment: " + debug_page_name + ", number: " + debug_page_number);
	  }
  } catch (SdaiException e) {
	  e.printStackTrace();
  }	  
}
*/		
// end of debugging block		
		select(selection);
	}
	
	public void select(Object element) {
		if (element == null) return;
		Composite page = (Composite)pages.get(element);
		if (page == null) {
			page = createPageFor(element);
			if (page != null) {
				pages.put(element, page);
			}
		}
		if (page != null) {
			layout.topControl = page;
				this.layout();
//System.out.println("<0XO><02>page: " + page);
			sdaiEditor.fireInternalPartChanged();
//System.out.println(page.setFocus());			
		}
	}
	
	public void delete(Object element) {
		if (element == null) return;
		Composite page = (Composite)pages.get(element);
		if (page != null) {
			page.setParent(null);
			page.dispose();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	public void dispose() {
		clear();
		super.dispose();
	}
	
	private SdaiEditorInternal createEditor(Composite page, RepositoryHandlerInput input) {
		SdaiEditorInternal editor = new SdaiEditorInternal(sdaiEditor);
		try {
			editor.init(sdaiEditor.getEditorSite(), input);
		} catch (PartInitException pie) {
			SdaieditPlugin.log(pie);
			SdaieditPlugin.console(pie.toString());
		}
		editor.createPartControl(page);
		editor.addPropertyListener(this);
		internalEditors.put(page, editor);
		editor.addPageChangeListener(this);
		return editor;
	}
	
	private Composite createPageFor(Object element) {
		Composite page = new Composite(this, SWT.NONE);
		page.setLayout(new FillLayout());
		if (element instanceof RepositoryHandler) {
			RepositoryHandler rh = (RepositoryHandler)element;
			createEditor(page, new RepositoryHandlerInput(rh, 
					((SdaiEditorInput)sdaiEditor.getEditorInput()).isReadonly()));
		} else 
		if (element instanceof ModelHandler) {
			ModelHandler mh = (ModelHandler)element;
			printStuffRR(mh);
			createEditor(page, new RepositoryHandlerInput(mh, 
					((SdaiEditorInput)sdaiEditor.getEditorInput()).isReadonly()));
		} else {
			Label label = new Label(page, SWT.NONE);
			label.setText(element.toString());
		}
		return page;
	}

	/**
	 * all elements in internal editors set must implement IEditorPart interface
	 * @return
	 */
	public Collection getInternalEditors() {
		return internalEditors.values();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPropertyListener#propertyChanged(java.lang.Object, int)
	 */
	public void propertyChanged(Object source, int propId) {
		if (propId == IEditorPart.PROP_DIRTY) {
			sdaiEditor.updateModifiedStatus();
		}
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.editors.IPageChangeListener#pageAdded(jsdai.express_g.editors.PageChangeEvent)
	 */
	public void pageAdded(PageChangeEvent e) {
		sdaiEditor.fireInternalPartChanged();
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.editors.IPageChangeListener#pageChanged(jsdai.express_g.editors.PageChangeEvent)
	 */
	public void pageChanged(PageChangeEvent e) {
		sdaiEditor.fireInternalPartChanged();
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.editors.IPageChangeListener#pageRemoved(jsdai.express_g.editors.PageChangeEvent)
	 */
	public void pageRemoved(PageChangeEvent e) {
		sdaiEditor.fireInternalPartChanged();
	}
	
	public IWorkbenchPart getActiveInternalPart() {
        if (layout.topControl == null) return null;
		SdaiEditorInternal internal = (SdaiEditorInternal)internalEditors.get(layout.topControl);
		IWorkbenchPart editor = internal.getActiveEditor();
		if (editor == null) return FakeWorkbenchPart.getPart();
			else return editor;
	}

			void printStuffRR(ModelHandler mh) {
			}


}
