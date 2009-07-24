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

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import jsdai.express_g.SdaieditPlugin;
import jsdai.express_g.editors.outline.FakeWorkbenchPart;
import jsdai.express_g.editors.outline.IInternalPartListener;
import jsdai.express_g.editors.outline.IInternalPartProvider;
import jsdai.express_g.editors.outline.SdaiEditorOutline;
import jsdai.express_g.perspectives.PerspectiveFactory;
import jsdai.lang.ASdaiModel;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiRepository;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

/**
 * @author Mantas Balnys
 *
 */
public class SdaiEditor extends EditorPart implements IInternalPartProvider {
	
	private RepositoryHandler repositoryHandler = null;
	private SdaiEditorOutline outline;
	
	public static final String HIDE_AUTO_PERSPECTIVE = "show_ExpressG_perspective_on_editor_open";
	public static final String READWRITE_EXD = "not_readonly_dictionary_data";
	public static final String HIDE_READWRITE = "show_not_readonly_dictionary_data";
	
	/**
	 * list of listener lists
	 */
	private ArrayList listeners = new ArrayList();
	private EditorContainer container = null;

	/**
	 * 
	 */
	public SdaiEditor() {
		super();
	}
	
	private void saveInternalEditors(IProgressMonitor monitor) {
		Iterator iter = container.getInternalEditors().iterator();
		while (iter.hasNext()) {
			SdaiEditorInternal editor = (SdaiEditorInternal)iter.next();
			if (editor.isDirty()) try {
				editor.doSave(monitor);
			} catch (Throwable t) {
				SdaieditPlugin.log(t);
				SdaieditPlugin.console(t.toString());
			}
		}
	}
	
	public void closeInternalEditors() {
		container.init();
		container.select(repositoryHandler);
		updateModifiedStatus();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void doSave(IProgressMonitor monitor) {
		if ((container != null) && (repositoryHandler != null)) {
			String file_extension = repositoryHandler.getRepoPath().getFileExtension();
			if (file_extension != null && file_extension.equalsIgnoreCase("exg")) {
				saveInternalEditors(monitor);
				try {
					repositoryHandler.saveAll();
					repositoryHandler.update();
				} catch (SdaiException sex) {
					SdaieditPlugin.log(sex);
					SdaieditPlugin.console(sex.toString());
				}
				updateModifiedStatus();
			} else {
				doSaveAs();
			}
		}
	}
	
	private void clearEGModels(SdaiRepository repository) throws SdaiException {
		HashSet egeModels = new HashSet();
		ASdaiModel models = repository.getModels();
		SdaiIterator sit = models.createIterator();
		while (sit.next()) {
			SdaiModel model = models.getCurrentMember(sit);
			if (model.getUnderlyingSchemaString().equalsIgnoreCase("EXPRESS_G_SCHEMA")) {
				egeModels.add(model);
			}
		}
		Iterator iter = egeModels.iterator();
		while (iter.hasNext()) {
			SdaiModel model = (SdaiModel)iter.next();
			model.deleteSdaiModel();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#doSaveAs()
	 */
	public void doSaveAs() {
		try{
		new ProgressMonitorDialog(getSite().getShell()).run(false, false, new IRunnableWithProgress() {
			public void run(IProgressMonitor progressMonitor) {
				FileDialog dialog = new FileDialog(getSite().getShell(), SWT.SAVE);
				dialog.setFilterExtensions(new String[]{"*.exg", "*.exd"});
				dialog.setFileName(repositoryHandler.getRepoPath().removeFileExtension().toOSString());
				String path = dialog.open();
				progressMonitor.beginTask("Saving file", IProgressMonitor.UNKNOWN);
				if (path != null) {
					saveInternalEditors(null);
					try {
						IPath ipath = new Path(path);
//System.err.println("FILE SAVE");						
//System.err.println(ipath);						
						String extension = ipath.getFileExtension();
//System.err.println(extension);						
						boolean canCreateFile = true;
						if ("exd".equalsIgnoreCase(extension)) { // inform that graphical data wouldnt be saved
							canCreateFile = MessageDialog.openQuestion(
									getSite().getShell(), "Saving file", "Saving dictionary data. This will loose all created graphical layouts, continue?");
						} else {
							if (!"exg".equalsIgnoreCase(extension)) {
								ipath = ipath.addFileExtension("exg");
//System.err.println("ADD exg");								
							}
						}
//System.err.println(ipath);						
						File newFile = ipath.toFile();
						if (canCreateFile && newFile.exists()) { // check for overwrite
							canCreateFile = MessageDialog.openQuestion(
									getSite().getShell(), "Saving file", "File exists, overwrite?");
						}
//System.err.println(canCreateFile + " - " + newFile);						
						if (canCreateFile) {
							if ("exd".equalsIgnoreCase(extension)) { // remove all graphical data
								clearEGModels(repositoryHandler.getRepository());
//System.err.println("clear exg data");								
							}
							repositoryHandler.getRepository().setLocation(newFile.getAbsolutePath());
							repositoryHandler.saveAll();
//System.err.println("saved");							
							
/*							File repoFile = repositoryHandler.getRepoPath().toFile();
							File tempFile = null;
							while (tempFile == null) try { // create temp file
								tempFile = File.createTempFile(repoFile.getName() + System.currentTimeMillis(), ".rtmp");
							} catch (IOException iox) {
								iox.printStackTrace();
							}
							EGToolKit.copyFile(repoFile, tempFile);
							if (extension.equalsIgnoreCase(".exd")) { // remove all graphical data
								clearEGModels(repositoryHandler.getRepository());
							}
							repositoryHandler.saveAll();
							// dispose all current data
							repositoryHandler.close();
							repositoryHandler = null;
							container.clear();
							// finish with file operations
							EGToolKit.copyFile(repoFile, newFile);
							EGToolKit.copyFile(tempFile, repoFile);
							if (!tempFile.delete()) tempFile.deleteOnExit(); /**/
							// update editor input
							IFile ifile= ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(ipath);
							setInput(new FileEditorInput(ifile));
						}
						
						
		/*/				
						// create new repository linked with new file
						SdaiSession session = SdaiSession.openSession();
						SdaiTransaction transaction;
						if (session.testActiveTransaction()) {
							transaction = session.getActiveTransaction();
						} else {
							transaction = session.startTransactionReadWriteAccess();
						}
						
						File file = new File(path);

						if (file.exists()) {
							// TODO ask to overwrite
						}
						File repoDir = new File(path);
						if (!repoDir.exists()) repoDir.mkdirs();
						System.setProperty("new.repository.format", "SDAI");
						SdaiRepository repository = session.createRepository("EXPRESS DICTIONARY REPO", path);
						System.out.println("created: " + repository);
						repository.openRepository();
						
						// copy all data to new repository
						repository.copyFrom(repositoryHandler.getRepository());
						
						if (extension.equalsIgnoreCase(".exd")) { // remove graphical data from new repository
							// TODO
						}
						// comit new repository
						transaction.commit();
						session.closeSession();
						IPath ipath = new Path(path);
						repositoryHandler.changePath(ipath);
						repositoryHandler.update();
						// update editor input
						IFile ifile= ResourcesPlugin.getWorkspace().getRoot().getFile(ipath);
						setInput(new FileEditorInput(ifile));/**/
					} catch (SdaiException sex) {
						SdaieditPlugin.log(sex);
						SdaieditPlugin.console(sex.toString());
					}
					updateModifiedStatus();
				}
				progressMonitor.done();
			}
		});
		} catch (InterruptedException ie) {
			SdaieditPlugin.log(ie);
			SdaieditPlugin.console(ie.toString());
		} catch (InvocationTargetException ie) {
			SdaieditPlugin.log(ie);
			SdaieditPlugin.console(ie.toString());
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if (!(input instanceof IFileEditorInput))
			throw new PartInitException("Invalid Input: Must be IFileEditorInput");
		setSite(site);
		setInput(input);
	}
	
	private void trySwitchPerspective() {
		try {
			IPreferenceStore prefs = SdaieditPlugin.getDefault().getPreferenceStore();
			boolean hide = prefs.getBoolean(HIDE_AUTO_PERSPECTIVE);
			IWorkbenchPartSite site = getSite();
			IWorkbenchWindow window = site.getWorkbenchWindow();
			IWorkbenchPage page = window.getActivePage();
			if (!hide) {
				IWorkbench workbench = window.getWorkbench();
				IPerspectiveRegistry registry = workbench.getPerspectiveRegistry();
				IPerspectiveDescriptor descriptor = registry.findPerspectiveWithId(PerspectiveFactory.ID);
				if ((page != null) && (page.getPerspective() != descriptor)) {
					MessageDialogWithToggle dialog = MessageDialogWithToggle.openYesNoQuestion(site.getShell(), "Confirm Perspective Switch",
							"This kind of editor is associated with the Express-G Perspective. Do you want to switch to this perspective now?",
							"Do not offer to switch perspective in the future", hide, prefs, HIDE_AUTO_PERSPECTIVE);
					if (dialog.getReturnCode() == 2)
						page.setPerspective(descriptor);
					hide = dialog.getToggleState();
					prefs.setValue(HIDE_AUTO_PERSPECTIVE, hide);
					SdaieditPlugin.getDefault().savePluginPreferences();
				}
			}
			if (page != null) page.showView("net.jsdai.express_g.editors.outline.InternalContentOutline");
		} catch (Throwable t) { 
			SdaieditPlugin.log(t);
			SdaieditPlugin.console("Warning: loading of Express-G perspective not completed");
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#isDirty()
	 */
	public boolean isDirty() {
		try {
			if ((repositoryHandler != null) && repositoryHandler.getRepository().isModified()) return true;
		} catch (SdaiException sex) {
			SdaieditPlugin.log(sex);
			SdaieditPlugin.console(sex.toString());
		}
		if (container == null) return false;
		Iterator iter = container.getInternalEditors().iterator();
		boolean dirty = false;
		while (!dirty && iter.hasNext()) 
			dirty = ((IEditorPart)iter.next()).isDirty(); 
		return dirty;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() {
		return (container != null) && (repositoryHandler != null);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		container = new EditorContainer(parent, SWT.NONE, this);
		container.init(); 
		if (repositoryHandler != null) {
			container.select(repositoryHandler);
		}
		if (outline != null) outline.addSelectionChangedListener(container);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	public void setFocus() {
		if (container != null) container.setFocus();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#setInput(org.eclipse.ui.IEditorInput)
	 */
	protected void setInput(IEditorInput input) {
		SdaiEditorInput sin;
		if (input instanceof SdaiEditorInput) 
			sin = (SdaiEditorInput)input;
		else {
			IPreferenceStore prefs = SdaieditPlugin.getDefault().getPreferenceStore();
			boolean readonly = true; //TODO !prefs.getBoolean(READWRITE_EXD);
//			try {
				boolean hide = true;//TODO prefs.getBoolean(HIDE_READWRITE);
				if (!hide) {
					MessageDialogWithToggle dialog = MessageDialogWithToggle.openYesNoQuestion(getSite().getShell(), 
							"Layout or Edit mode selection",
							"Layout schemas and don't modify them? (select \"No\" to edit schemas)",
							null, hide, prefs, HIDE_READWRITE);
					readonly = dialog.getReturnCode() == 2;
					hide = dialog.getToggleState();
					prefs.setValue(HIDE_READWRITE, hide);
					SdaieditPlugin.getDefault().savePluginPreferences();
				}
//			} catch (Throwable t) { t.printStackTrace(); }
			sin = new SdaiEditorInput((IFileEditorInput)input, readonly);
		}
		super.setInput(sin);
		BusyIndicator.showWhile(getSite().getShell().getDisplay(), new Runnable() {
			public void run() {
				if (repositoryHandler != null) try { repositoryHandler.close(); } catch (SdaiException sex) {}
				SdaiEditorInput sin = (SdaiEditorInput)getEditorInput();
				try {
					repositoryHandler = new RepositoryHandler(sin.getPath());
					if (container != null) {
						container.init(); 
						container.select(repositoryHandler);
					}
					
					setPartName(sin.getName() + " (" + (sin.isReadonly() ? "layout" : "edit") + ")");
//					setContentDescription("Express-G Editor - " + sin.getName());
					// refresh Outline
					refreshOutline();
					try {
						sin.getFile().getProject().refreshLocal(IProject.DEPTH_INFINITE, null);
					} catch (CoreException ce) { 
						SdaieditPlugin.log(ce);
						SdaieditPlugin.console(ce);
					}
					BasicNewResourceWizard.selectAndReveal(sin.getFile(), 
							SdaieditPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow());
				} catch (SdaiException sex) {
					SdaieditPlugin.log(sex);
					SdaieditPlugin.console(sex);
				}
			}
		});
		
		trySwitchPerspective();
	}
	
	public void refreshOutline() {
		if (outline != null) outline.refresh();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if ((repositoryHandler != null) && adapter.equals(IContentOutlinePage.class)) {
			if ((outline == null) || (repositoryHandler != outline.getRepositoryHandler())) {
				if ((container != null) && (outline != null))
					outline.removeSelectionChangedListener(container);
				outline = new SdaiEditorOutline(this);
				if (container != null) outline.addSelectionChangedListener(container);
			}
			return outline;
		} else 
		if (adapter.equals(IInternalPartProvider.class)) {
			return this;
		} else
			return super.getAdapter(adapter);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	public void dispose() {
		if (repositoryHandler != null) try { repositoryHandler.close(); } catch (SdaiException sex) {
			SdaieditPlugin.log(sex);
			SdaieditPlugin.console(sex);
		}
		super.dispose();
		if (container != null) container.dispose();
		fireInternalPartChanged();
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.editors.IExpressGEditor#updateModifiedStatus()
	 */
	public void updateModifiedStatus() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				firePropertyChange(PROP_DIRTY);
			}
		});
		try {
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(repositoryHandler.getRepoPath());
			file.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (Throwable t) {
			SdaieditPlugin.log(t);
			SdaieditPlugin.console(t.toString());
		}
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.editors.outline.IInternalPartProvider#addInternalPartListener(jsdai.express_g.editors.outline.IInternalPartListener)
	 */
	/**
	 * 3 - Express-G Editor listener
	 * 5 - internal outline listener, internal editor contributor
	 */
	public void addInternalPartListener(int groupIndex, IInternalPartListener listener) {
		while (groupIndex >= listeners.size()) listeners.add(new ListenerList());
		ListenerList list = (ListenerList)listeners.get(groupIndex);
		list.add(listener);
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.editors.outline.IInternalPartProvider#removeInternalPartListener(jsdai.express_g.editors.outline.IInternalPartListener)
	 */
	public void removeInternalPartListener(int groupIndex, IInternalPartListener listener) {
		if (groupIndex >= listeners.size()) return;
		ListenerList list = (ListenerList)listeners.get(groupIndex);
		list.remove(listener);
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.editors.outline.IInternalPartProvider#fireInternalPartChanged(org.eclipse.core.runtime.IAdaptable)
	 */
	public void fireInternalPartChanged() {
		Iterator iter = listeners.iterator();
		while (iter.hasNext()) {
			ListenerList list = (ListenerList)iter.next();
			Object[] listener = list.getListeners();
			IWorkbenchPart part = getActiveInternalPart();
			for (int i = 0; i < listener.length; i++) {
//System.out.println("<0XO><03>listener: " + listener[i] + ", part: " + part);
				((IInternalPartListener)listener[i]).internalPartChanged(part);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.editors.outline.IInternalPartProvider#getActiveInternalPart()
	 */
	public IWorkbenchPart getActiveInternalPart() {
		if (getSite().getShell().isDisposed() || (container == null) || container.isDisposed()) 
			return FakeWorkbenchPart.getPart();
		return container.getActiveInternalPart();
	}
	
	/**
	 * @return Returns the repositoryHandler.
	 */
	public RepositoryHandler getRepositoryHandler() {
		return repositoryHandler;
	}
}
