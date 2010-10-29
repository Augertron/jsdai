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

import jsdai.SExtended_dictionary_schema.ESchema_definition;
import jsdai.express_g.SdaieditPlugin;
import jsdai.express_g.commands.OpenRepository;
import jsdai.express_g.commands.SaveRepository;
import jsdai.express_g.editors.outline.EGEditorOutlineTree;
import jsdai.express_g.editors.outline.IInternalPartListener;
import jsdai.express_g.exp2.EGToolKit;
import jsdai.express_g.exp2.ui.Application;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.VisualPage;
import jsdai.express_g.exp2.ui.command.Command;
import jsdai.express_g.exp2.ui.command.CommandInvoker;
import jsdai.express_g.exp2.ui.command.LayoutCommand;
import jsdai.express_g.exp2.ui.command.SelectCommand;
import jsdai.express_g.exp2.ui.command.UpdateCommand;
import jsdai.express_g.exp2.ui.event.PageChangeEvent;
import jsdai.express_g.exp2.ui.event.PageListener;
import jsdai.express_g.exp2.ui.panels.PaintPanel;
import jsdai.express_g.widgets.CTabFolderSingleControl;
import jsdai.express_g.widgets.CTabItemSingleControl;
import jsdai.express_g.widgets.PageBrowsingControl;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.help.WorkbenchHelp;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.navigator.LocalSelectionTransfer;

/**
 * @author Mantas Balnys
 *
 */
public class ExpressGEditor extends EditorPart implements IExpressGEditor, CommandInvoker, 
		IInternalPartListener, DropTargetListener {
	private RepositoryHandlerInput input = null;
	private Application application = null;
	private Composite infoPage = null;
	private PaintPanel panel = null;
	private ScrolledComposite scroll = null;
	private CTabFolderSingleControl tabs = null;
	private Label pageName = null;
	private LayoutCommand layoutCommand = null;
	
	private EGEditorOutlineTree outline = null;
	
	/**
	 * 
	 */
	public ExpressGEditor() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void doSave(IProgressMonitor monitor) {
//System.out.println("<ExpressGEditor> doSave");
		if ((application != null) && !application.isDisposed() && 
				application.handler().startCommand(new SaveRepository(this))) {
			application.getSelectionHandler().setSelected(this, SelectCommand.EMPTY_SELECTION);
			application.setModified(false);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#doSaveAs()
	 */
	public void doSaveAs() {
	// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
//System.out.println("<init(ExpressGEditor>");
		if (input instanceof RepositoryHandlerInput) {
			setSite(site);
			setInput(input);
		} else {
			throw new PartInitException("Wrong input type, requeres jsdai.express_g.editors.RepositoryHandlerInput");
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#isDirty()
	 */
	public boolean isDirty() {
		if ((application == null) || application.isDisposed()) return false;
		return application.isModified();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		tabs = new CTabFolderSingleControl(parent, SWT.BOTTOM);
		Composite topright = new Composite(tabs, SWT.NONE);
		topright.setLayout(new FillLayout());
		pageName = new Label(topright, SWT.NONE);
		pageName.addMouseListener(new MouseListener() {
			public void mouseDoubleClick(MouseEvent e) {
				if (application != null) {
					VisualPage vp = application.handler().getVisualPage(application.handler().getPage());
					if (!vp.isEditDialogCreated()) vp.createEditDialog((PaintPanel)application.getPainting());
					vp.showEditDialog();
					application.handler().setPage(application.handler().getPage());
				}
			}

			public void mouseDown(MouseEvent e) {
			}

			public void mouseUp(MouseEvent e) {
			}
		});
		
		tabs.setTopRight(topright, SWT.FILL);
	    WorkbenchHelp.setHelp(tabs, SdaieditPlugin.ID_SDAIEDIT + ".EditorPageSelectionContextId");
		// create scroller
		scroll = new ScrolledComposite(tabs, SWT.H_SCROLL | SWT.V_SCROLL);
		scroll.getHorizontalBar().setIncrement(20);
		scroll.getHorizontalBar().setPageIncrement(120);
		scroll.getVerticalBar().setIncrement(20);
		scroll.getVerticalBar().setPageIncrement(120);
		//
		panel = new PaintPanel(scroll, SWT.NONE);
		scroll.setContent(panel);
	    WorkbenchHelp.setHelp(panel, SdaieditPlugin.ID_SDAIEDIT + ".ExpressG_EditorContextId");

		// finish with tab page selector
	    // info page:
		infoPage = new Composite(tabs, SWT.NONE);
		infoPage.setLayout(new GridLayout());
		
		CTabItemSingleControl item = new CTabItemSingleControl(tabs, SWT.NONE);
		item.setControl(infoPage);
		item.setText("Info");
		// done with info page
		
		item = new CTabItemSingleControl(tabs, SWT.NONE);
		item.setControl(scroll);
		item.setText("+");
		tabs.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			
			public void widgetSelected(SelectionEvent e) {
				if (application != null) {
					int nr = tabs.getSelectionIndex();
					if (nr > 0)	application.handler().setPage(nr);
				}
			}
		});		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	public void setFocus() {
		panel.setFocus();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#setInput(org.eclipse.ui.IEditorInput)
	 */
	protected void setInput(IEditorInput input) {
		this.input = (RepositoryHandlerInput)input;
		if (application != null) application.setRepositoryHandler(this.input.getRepositoryHandler());
		super.setInput(input);
	}
	
	// RR - need input for orfan diagrams
	public RepositoryHandlerInput getInput() {
		return input;
	}
	
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.command.CommandInvoker#commandDone(jsdai.express_g.exp2.ui.command.Command)
	 */
	public void commandDone(Command command) {
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.editors.IMultiEditorPageChangeListener#multiEditorPageChanged(int)
	 */
	public void initEditor() {
//System.out.println("<initEditor>");
		if ((application == null) && (input != null)) { // Open diagram if not yet opened
//System.out.println("<ExpressGEditor> diagram NOT YET opened");		

			BusyIndicator.showWhile(null, new Runnable() {
				public void run() {
					ModelHandler mh = input.getModelHandler();
//					RepositoryHandler rh = mh.getRepositoryHandler();
					try { 
						ESchema_definition schemaDef = mh.getSchema_definition();
						String schemaName = null;
//System.out.println("<<A>> schemaDef: " + schemaDef + ", model handler: " + mh + ", input: " + input);
						if (schemaDef != null) 
							schemaName = schemaDef.getName(null); 
//						ModelHandler mhs = rh.getModelHandler(schemaName);

						// instantiate application
						application = new Application(ExpressGEditor.this);
						application.handler().addPageListener(new PageListener() {
							
							/* (non-Javadoc)
							 * @see jsdai.express_g.exp2.ui.event.PageListener#pageChanged(jsdai.express_g.exp2.ui.event.PageChangeEvent)
							 */
							public void pageChanged(PageChangeEvent e) {
								while (tabs.getItemCount() < application.handler().getMaxPage() + 2) {
									int pg = tabs.getItemCount() - 1;
									CTabItemSingleControl prev = tabs.getItem(pg);
									prev.setText(EGToolKit.renumberInTab(application, pg));
//									prev.setText(String.valueOf(tabs.getItemCount() - 1));
									CTabItemSingleControl item = new CTabItemSingleControl(tabs, SWT.NONE);
									item.setControl(scroll);
									item.setText("+");
								}
								while (tabs.getItemCount() > application.handler().getMaxPage() + 2) {
									CTabItemSingleControl item = tabs.getItem(tabs.getItemCount() - 1);
									item.dispose();
									CTabItemSingleControl prev = tabs.getItem(tabs.getItemCount() - 1);
									prev.setText("+");
								}
								
								tabs.setSelection(e.getNewPage());
								pageName.setText(" " + application.handler().getCurrentPage().getName());
							}
						});
						application.setRepositoryHandler(input.getRepositoryHandler());
						panel.setProperties(application);
						getSite().setSelectionProvider(new ExpressGSelectionProvider(application));

						// finish with GUI
						try {
							SdaiModel model = mh.getModel();
							new Label(infoPage, SWT.NONE).setText("NAME:     " + mh.getName());
							new Label(infoPage, SWT.NONE).setText("MODIFIED: " + model.getChangeDate());
							PageBrowsingControl pageBrowser = new PageBrowsingControl(infoPage, SWT.NONE);
							pageBrowser.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
							pageBrowser.setProperties(application);
						} catch (SdaiException sex) {
							new Label(infoPage, SWT.NONE).setText("ERROR: " + sex.getMessage());
						}

						
						// drag'n'drop actions
						layoutCommand = new LayoutCommand(ExpressGEditor.this);
						DropTarget drop = new DropTarget(panel, DND.DROP_MOVE);
						drop.setTransfer(new Transfer[]{LocalSelectionTransfer.getInstance()});
						drop.addDropListener(ExpressGEditor.this);
						
						// starting application
//System.out.println("<<A>>Setting schema name: " + schemaName);
						application.setName(schemaName);
						application.setNameEG(mh.getName());
						
						application.handler().startCommand(new OpenRepository(ExpressGEditor.this));
						application.handler().setPage(1);
//						tabs.setSelection(0);
						// update page references
//System.out.println("<RR-01-update-page-references>-1");
						application.handler().startCommand(new UpdateCommand(ExpressGEditor.this));
//System.out.println("<RR-01-update-page-references>-2");
						if (input.isReadonly()) {
							int emode = application.getEditMode();
							if ((emode & PropertySharing.MODE_EDIT) != 0)
								application.setEditMode(emode - PropertySharing.MODE_EDIT);
						} else {
							application.setEditMode(application.getEditMode() | PropertySharing.MODE_EDIT);
						}
						application.setModified(false);
						
						updateTabNames();
						// add context menu
						new PageHandlingContextMenu(tabs, application);
					} catch (SdaiException sex) {
						SdaieditPlugin.log(sex);
						SdaieditPlugin.console(sex.toString());
					}
				}
			});
		} else {
//System.out.println("<ExpressGEditor> diagram already opened");		
		}
	}

// RR experiment

// this is a radical approach

	public void reInitEditor() {
//System.out.println("<reInitEditor>");
    if (application != null) application = null;
		if ((application == null) && (input != null)) { // Open diagram if not yet opened
//System.out.println("<ExpressGEditor> diagram NOT YET opened");		

			BusyIndicator.showWhile(null, new Runnable() {
				public void run() {
					ModelHandler mh = input.getModelHandler();
//					RepositoryHandler rh = mh.getRepositoryHandler();
					try { 
						ESchema_definition schemaDef = mh.getSchema_definition();
						String schemaName = null;
//System.out.println("<<A>> schemaDef: " + schemaDef + ", model handler: " + mh + ", input: " + input);
						if (schemaDef != null) 
							schemaName = schemaDef.getName(null); 
//						ModelHandler mhs = rh.getModelHandler(schemaName);

						// instantiate application
						application = new Application(ExpressGEditor.this);
						application.handler().addPageListener(new PageListener() {
							
							/* (non-Javadoc)
							 * @see jsdai.express_g.exp2.ui.event.PageListener#pageChanged(jsdai.express_g.exp2.ui.event.PageChangeEvent)
							 */
							public void pageChanged(PageChangeEvent e) {
								while (tabs.getItemCount() < application.handler().getMaxPage() + 2) {
									int pg = tabs.getItemCount() - 1;
									CTabItemSingleControl prev = tabs.getItem(pg);
									prev.setText(EGToolKit.renumberInTab(application, pg));
//									prev.setText(String.valueOf(tabs.getItemCount() - 1));
									CTabItemSingleControl item = new CTabItemSingleControl(tabs, SWT.NONE);
									item.setControl(scroll);
									item.setText("+");
								}
								while (tabs.getItemCount() > application.handler().getMaxPage() + 2) {
									CTabItemSingleControl item = tabs.getItem(tabs.getItemCount() - 1);
									item.dispose();
									CTabItemSingleControl prev = tabs.getItem(tabs.getItemCount() - 1);
									prev.setText("+");
								}
								
								tabs.setSelection(e.getNewPage());
								pageName.setText(" " + application.handler().getCurrentPage().getName());
							}
						});
						application.setRepositoryHandler(input.getRepositoryHandler());
						panel.setProperties(application);
						getSite().setSelectionProvider(new ExpressGSelectionProvider(application));

						// finish with GUI
						try {
							SdaiModel model = mh.getModel();
							new Label(infoPage, SWT.NONE).setText("NAME:     " + mh.getName());
							new Label(infoPage, SWT.NONE).setText("MODIFIED: " + model.getChangeDate());
							PageBrowsingControl pageBrowser = new PageBrowsingControl(infoPage, SWT.NONE);
							pageBrowser.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
							pageBrowser.setProperties(application);
						} catch (SdaiException sex) {
							new Label(infoPage, SWT.NONE).setText("ERROR: " + sex.getMessage());
						}

						
						// drag'n'drop actions
						layoutCommand = new LayoutCommand(ExpressGEditor.this);
						DropTarget drop = new DropTarget(panel, DND.DROP_MOVE);
						drop.setTransfer(new Transfer[]{LocalSelectionTransfer.getInstance()});
						drop.addDropListener(ExpressGEditor.this);
						
						// starting application
//System.out.println("<<A>>Setting schema name: " + schemaName);
						application.setName(schemaName);
						application.setNameEG(mh.getName());
						
						application.handler().startCommand(new OpenRepository(ExpressGEditor.this));
						application.handler().setPage(1);
//						tabs.setSelection(0);
						// update page references
						application.handler().startCommand(new UpdateCommand(ExpressGEditor.this));
						if (input.isReadonly()) {
							int emode = application.getEditMode();
							if ((emode & PropertySharing.MODE_EDIT) != 0)
								application.setEditMode(emode - PropertySharing.MODE_EDIT);
						} else {
							application.setEditMode(application.getEditMode() | PropertySharing.MODE_EDIT);
						}
						application.setModified(false);
						
						updateTabNames();
						// add context menu
						new PageHandlingContextMenu(tabs, application);
					} catch (SdaiException sex) {
						SdaieditPlugin.log(sex);
						SdaieditPlugin.console(sex.toString());
					}
				}
			});
		} else {
//System.out.println("<ExpressGEditor> diagram already opened");		
		}
	}


// RR experiment - end


	
	// this is at the bottom, except 1st (Info) and last (+): Info here ... here +
	public void updateTabNames() {
		CTabItemSingleControl[] items = tabs.getItems();
		for (int i = 1; i < items.length - 1; i++) {
			items[i].setText(EGToolKit.renumberInTab(application, i));
		}
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.editors.IExpressGEditor#getProperties()
	 */
	public PropertySharing getProperties() {
		return application;
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.editors.IExpressGEditor#updateModifiedStatus()
	 */
	public void updateModifiedStatus() {
//System.out.println("<<>>setting editor to DIRTY");
		this.firePropertyChange(PROP_DIRTY);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if (adapter.equals(IContentOutlinePage.class)) {
			if (outline == null) outline = new EGEditorOutlineTree(this);
			return outline;
		} else 
			return super.getAdapter(adapter);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragEnter(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragEnter(DropTargetEvent event) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragLeave(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragLeave(DropTargetEvent event) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragOperationChanged(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragOperationChanged(DropTargetEvent event) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragOver(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragOver(DropTargetEvent event) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DropTargetListener#drop(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void drop(DropTargetEvent event) {
		ISelection selection = LocalSelectionTransfer.getInstance().getSelection();
		if (selection instanceof StructuredSelection) {
			if (layoutCommand == null)
				layoutCommand = new LayoutCommand(this);
			layoutCommand.setLocation(panel.toControl(event.x, event.y));
			outline.setSelection(selection);
  			application.handler().startCommand(layoutCommand);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DropTargetListener#dropAccept(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dropAccept(DropTargetEvent event) {
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.editors.outline.IInternalPartListener#internalPartChanged(org.eclipse.ui.IWorkbenchPart)
	 */
	public void internalPartChanged(IWorkbenchPart part) {
//System.out.println("<internalPartChanged>");
		if (part == this) {
//System.out.println("<0XO><04>part: " + part);
//System.out.println("<internalPartChanged>- initEditor");
			initEditor();
		}	
		else
			if (panel != null) panel.suspend();
	}
}
