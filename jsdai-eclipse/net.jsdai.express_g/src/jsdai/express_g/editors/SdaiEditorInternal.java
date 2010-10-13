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

import jsdai.express_g.SdaieditPlugin;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.MultiPageEditorPart;

/**
 * @author Mantas Balnys
 *
 */
public class SdaiEditorInternal extends MultiPageEditorPart implements IPageChangeProvider, IPageControl {
	private SdaiEditor sdaiEditor;
	private RepositoryHandler repositoryHandler = null;
	private ModelHandler modelHandler = null;
	private ExpressGEditor egEditor = null;
//	private PageBrowsingControl pageBrowser = null;
	private ListenerList pageChangeListeners = new ListenerList();
	
	/**
	 * 
	 */
	public SdaiEditorInternal(SdaiEditor sdaiEditor) {
		super();
		this.sdaiEditor = sdaiEditor;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.MultiPageEditorPart#createPages()
	 */
	protected void createPages() {
		if (modelHandler == null) {
			createPageRepoInfo();
		} else {
			if (modelHandler.getDiagram_definition() != null) {
				Composite container = getContainer();
				if (container instanceof CTabFolder) {
					createPageEGEditor();
//					createPageDiagramInfo();
					CTabFolder tab = (CTabFolder)container;
					tab.setTabHeight(0);
				}
			} else 
			if (modelHandler.getSchema_definition() != null) {
				createPageSchemaInfo();
			} else {
				createPageModelInfo();
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void doSave(IProgressMonitor monitor) {
//System.out.println("<SdaiEditorInternal> doSave-0");
		for (int i = 0; i < getPageCount(); i++) {
			IEditorPart editor = getEditor(i);
			if ((editor != null) && (editor.isDirty())) {
				editor.doSave(monitor);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#doSaveAs()
	 */
	public void doSaveAs() {
	// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#setInput(org.eclipse.ui.IEditorInput)
	 */
	protected void setInput(IEditorInput input) {
		RepositoryHandlerInput rin = (RepositoryHandlerInput)input;
		repositoryHandler = rin.getRepositoryHandler();
		modelHandler = rin.getModelHandler();
		super.setInput(input);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if (input instanceof RepositoryHandlerInput) {
			super.init(site, input);
		} else {
			throw new PartInitException("Wrong input type, requeres jsdai.express_g.editors.RepositoryHandlerInput");
		}
	}

	/**
	 * Creates info page of the multi-page editor.
	 */
	private int createPageRepoInfo() {
		Composite composite = new Composite(getContainer(), SWT.NONE);
		composite.setLayout(new RowLayout(SWT.VERTICAL));
		Label path = new Label(composite, SWT.NONE);
		path.setText("PATH:  " + repositoryHandler.getRepoPath());
		Label repo = new Label(composite, SWT.NONE);
		repo.setText("NAME:  " + repositoryHandler.getRepository());
		int pgNr = addPage(composite);
		setPageText(pgNr, "Info");
		return pgNr;
	}

	private int createPageModelInfo() {
		Composite composite = new Composite(getContainer(), SWT.NONE);
		composite.setLayout(new RowLayout(SWT.VERTICAL));
		
		try {
			SdaiModel model = modelHandler.getModel();
			new Label(composite, SWT.NONE).setText("NAME:    " + modelHandler.getName());
			new Label(composite, SWT.NONE).setText("MODIFIED:  " + model.getChangeDate());
		} catch (SdaiException sex) {
			new Label(composite, SWT.NONE).setText("ERROR: " + sex.getMessage());
		}
		
		int pgNr = addPage(composite);
		setPageText(pgNr, "Info");
		return pgNr;
	}

	/* TODO XXX1 for later use 
	private int createPageDiagramInfo() {
		Composite composite = new Composite(getContainer(), SWT.NONE);
		composite.setLayout(new GridLayout());
		
		try {
			SdaiModel model = modelHandler.getModel();
			new Label(composite, SWT.NONE).setText("NAME:    " + modelHandler.getName());
			new Label(composite, SWT.NONE).setText("MODIFIED:  " + model.getChangeDate());
			pageBrowser = new PageBrowsingControl(composite, SWT.NONE);
			pageBrowser.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		} catch (SdaiException sex) {
			new Label(composite, SWT.NONE).setText("ERROR: " + sex.getMessage());
		}
		
		int pgNr = addPage(composite);
		setPageText(pgNr, "Info");
		return pgNr;
	}
	*/

	private int createPageSchemaInfo() {
		return createPageModelInfo();
	}
	
	private int createPageEGEditor() {
/*		
		Composite page = new Composite(getContainer(), SWT.NONE);
		page.setLayout(new FillLayout());
		ExpressGEditor editor = new ExpressGEditor();
		pageChangeListener.add(editor);
		try {
			editor.init(getEditorSite(), getEditorInput());
		} catch (PartInitException pie) {
			pie.printStackTrace();
		}
		editor.createPartControl(page);
		int pgNr = addPage(page);
		setPageText(pgNr, "Express-G");
		return pgNr;
*/
		egEditor = new ExpressGEditor();
//		getSite().getSelectionProvider().addSelectionChangedListener(editor);
		sdaiEditor.addInternalPartListener(3, egEditor);
		try {
			int pgNr = addPage(egEditor, getEditorInput());
			setPageText(pgNr, "Express-G");
			return pgNr;
		} catch (PartInitException pie) {
			SdaieditPlugin.log(pie);
			SdaieditPlugin.console(pie.toString());
			return -1;
		}
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.editors.IPageChangeProvider#addPageChangeListener(jsdai.express_g.editors.IPageChangeListener)
	 */
	public void addPageChangeListener(IPageChangeListener listener) {
		pageChangeListeners.add(listener);
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.editors.IPageChangeProvider#removePageChangeListener(jsdai.express_g.editors.IPageChangeListener)
	 */
	public void removePageChangeListener(IPageChangeListener listener) {
		pageChangeListeners.remove(listener);
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.editors.IPageChangeProvider#firePageChanged(jsdai.express_g.editors.PageChangeEvent)
	 */
	public void firePageChanged(PageChangeEvent e) {
		Object[] listener = pageChangeListeners.getListeners();
		for (int i = 0; i < listener.length; i++)
			((IPageChangeListener)listener[i]).pageChanged(e);
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.editors.IPageChangeProvider#firePageAdded(jsdai.express_g.editors.PageChangeEvent)
	 */
	public void firePageAdded(PageChangeEvent e) {
		Object[] listener = pageChangeListeners.getListeners();
		for (int i = 0; i < listener.length; i++)
			((IPageChangeListener)listener[i]).pageAdded(e);
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.editors.IPageChangeProvider#firePageRemoved(jsdai.express_g.editors.PageChangeEvent)
	 */
	public void firePageRemoved(PageChangeEvent e) {
		Object[] listener = pageChangeListeners.getListeners();
		for (int i = 0; i < listener.length; i++)
			((IPageChangeListener)listener[i]).pageRemoved(e);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.MultiPageEditorPart#addPage(org.eclipse.swt.widgets.Control)
	 */
	public int addPage(Control control) {
		int pgNr = super.addPage(control);
		firePageAdded(new PageChangeEvent(this, pgNr, null, control));
		return pgNr;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.MultiPageEditorPart#addPage(org.eclipse.ui.IEditorPart, org.eclipse.ui.IEditorInput)
	 */
	public int addPage(IEditorPart editor, IEditorInput input) throws PartInitException {
		int pgNr = super.addPage(editor, input);
		firePageAdded(new PageChangeEvent(this, pgNr, editor, getControl(pgNr)));
		return pgNr;
	}
	
	public ExpressGEditor getEGEditor() {
		return egEditor;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.MultiPageEditorPart#pageChange(int)
	 */
	protected void pageChange(int newPageIndex) {
        if (getSite().getSelectionProvider() == null) {
        	SdaieditPlugin.log("SdaiEditorInternal.pageChange() this site selectionProvider = null", IStatus.WARNING);
        } else {
            IEditorPart activeEditor = getEditor(newPageIndex);
            if (activeEditor != null && activeEditor.getSite().getSelectionProvider() == null) {
//            	SdaieditPlugin.log("SdaiEditorInternal.pageChange() active editor site selectionProvider = null", IStatus.WARNING);
            } else {
        		super.pageChange(newPageIndex); // 2005-11-28 fast upgrade to support eclipse 3.2
            }
        }
		
        /* TODO XXX1 		if ((egEditor != null) && (pageBrowser != null))
			pageBrowser.setInternalEditor(this, egEditorPage);*/
		firePageChanged(new PageChangeEvent(this, newPageIndex, getEditor(newPageIndex), getControl(newPageIndex)));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.MultiPageEditorPart#removePage(int)
	 */
	public void removePage(int pageIndex) {
		PageChangeEvent event = new PageChangeEvent(this, pageIndex, getEditor(pageIndex), getControl(pageIndex));
		super.removePage(pageIndex);
		firePageRemoved(event);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.MultiPageEditorPart#getActiveEditor()
	 */
	public IEditorPart getActiveEditor() {
		return super.getActiveEditor();
	}
	
	public void setActivePage(int pageIndex) { // increasing access level (was protected)
		super.setActivePage(pageIndex);
	}
	
	/** 
	 * @return
	 * @see jsdai.express_g.editors.IPageControl#getProperties()
	 */
	
	public PropertySharing getProperties() {
		if (egEditor != null) return egEditor.getProperties();
		return null;
	}
}
