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

package jsdai.express_compiler.list_editor;

import java.util.Iterator;

import jsdai.express_compiler.ExpressCompilerPlugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.editors.text.TextEditor;

import org.eclipse.ui.views.navigator.LocalSelectionTransfer;

public class ExpressListEditor extends TextEditor implements DropTargetListener {

	private ExpressListColorProvider fColorProvider;

	public ExpressListEditor() {
		super();

//		 fColorProvider = new ExpressListColorProvider();
//		 setSourceViewerConfiguration(new ExpressListViewerConfiguration(fColorProvider));
//		 setSourceViewerConfiguration(new SourceViewerConfiguration());
//		 setDocumentProvider(new ExpressListDocumentProvider());
	}
	public void dispose() {
if (fColorProvider != null) // temp 
		fColorProvider.dispose();
else
//	System.out.println("ERROR <001> - fColorProvider is NULL");		
		super.dispose();
	}

    /**
     *  See in preferences if we want auto-compile upon saving the file, if yes, run Express Compiler
     *  
     */
    public void doSave(IProgressMonitor monitor) {
    	super.doSave(monitor);
    
    }


//----------------------------- ----------------------------------------------------------------

	// no difference -------------------
	/* (non-ExpressDoc)
	 * Method declared on AbstractTextEditor
	 */
	protected void initializeEditor() {
		super.initializeEditor();

						// drag'n'drop actions
//						layoutCommand = new LayoutCommand(ExpressGEditor.this);

//						Control my_control = this.getControl();	
//						Composite my_control = this.getSourceViewer();
//						DropTarget drop = new DropTarget(my_control, DND.DROP_NONE);

//						SourceViewer source_viewer = (SourceViewer)getSourceViewer();
//						if (source_viewer == null) {
//						   source_viewer = new SourceViewer(fParent, fVerticalRuler, fErrorCorrectionOnSave);
//						   source_viewer = new SourceViewer(this.getSite().getParent(), null, 0);
//						}
/*

				IWorkbenchPartSite site = this.getSite();
System.out.println("site: " + site);				
				IWorkbenchWindow window = site.getWorkbenchWindow();
				IWorkbenchPage page = window.getActivePage();
				IEditorPart part = page.getActiveEditor();
*/
//System.out.println("this: " + this);				
//System.out.println("part: " + part);				
				
//System.out.println("source viewer: " + source_viewer);						
//DropTarget drop = new DropTarget(((SourceViewer)this.getSourceViewer()).getControl(), DND.DROP_NONE);
//		DropTarget drop = new DropTarget(((SourceViewer)((ExpressListEditor)part).getSourceViewer()).getControl(), DND.DROP_NONE);

//		DropTarget drop = new DropTarget(fSourceViewer.getControl(), DND.DROP_COPY);

		
		//						DropTarget drop = new DropTarget(source_viewer.getControl(), DND.DROP_NONE);
//						DropTarget drop = new DropTarget(((SourceViewer) ((AbstractTextEditor) part).getSourceViewer()).getControl(), DND.DROP_NONE);
//						DropTarget drop = new DropTarget(, DND.DROP_NONE);

//		drop.setTransfer(new Transfer[]{LocalSelectionTransfer.getInstance()});
//						drop.addDropListener(ExpressListEditor.this);


  		setSourceViewerConfiguration(new ExpressListViewerConfiguration(fColorProvider));
//		setEditorContextMenuId("#ExpressEditorContext"); //$NON-NLS-1$
//		setRulerContextMenuId("#ExpressRulerContext"); //$NON-NLS-1$

//	    WorkbenchHelp.setHelp(panel, SdaieditPlugin.ID_SDAIEDIT + ".ExpressG_EditorContextId");
	    
//	    IWorkbenchHelpSystem help = WorkbenchHelpSystem.getInstance();
//	    help.setHelp(this, ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID + ".Express_EditorContextId");

//	    setHelpContextId(ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID + ".Express_EditorContextId");
//			ExpressCompilerUtils.switchPerspective(getSite());

	}




	/*
	 * @see org.eclipse.ui.texteditor.ExtendedTextEditor#createSourceViewer(org.eclipse.swt.widgets.Composite, org.eclipse.jface.text.source.IVerticalRuler, int)
	 */
/*
	protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
		
		fAnnotationAccess= createAnnotationAccess();
		fOverviewRuler= createOverviewRuler(getSharedColors());
		
		ISourceViewer viewer= new ProjectionViewer(parent, ruler, getOverviewRuler(), isOverviewRulerVisible(), styles);
		// ensure decoration support has been created and configured.
		getSourceViewerDecorationSupport(viewer);
		
		return viewer;
	}
*/
	/*
	 * @see org.eclipse.ui.texteditor.ExtendedTextEditor#createPartControl(org.eclipse.swt.widgets.Composite)
	 */


	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
//System.out.println("CreatePartControl - source viewer: " + fSourceViewer);
//      DropTarget drop = new DropTarget(fSourceViewer.getControl(), DND.DROP_COPY);


		// DropTarget drop = new DropTarget(parent, DND.DROP_NONE);
//		DropTarget drop = new DropTarget(parent, DND.DROP_LINK); - no
		DropTarget drop = new DropTarget(parent, DND.DROP_COPY);
//		DropTarget drop = new DropTarget(parent, DND.DROP_TARGET_MOVE); - no
	

		drop.setTransfer(new Transfer[] {FileTransfer.getInstance()});
//		drop.setTransfer(new Transfer[] {Transfer});
//		drop.setTransfer(new Transfer[]{LocalSelectionTransfer.getInstance()});
//		drop.setTransfer(new Transfer[] {TextTransfer.getInstance()});
		drop.addDropListener(ExpressListEditor.this);

	}


/*
	target.addDropListener (new DropTargetListener() {
 		public void dragEnter(DropTargetEvent event) {};
 		public void dragOver(DropTargetEvent event) {};
 		public void dragLeave(DropTargetEvent event) {};
 		public void dragOperationChanged(DropTargetEvent event) {};
 		public void dropAccept(DropTargetEvent event) {}
 		public void drop(DropTargetEvent event) {
 			// A drop has occurred, copy over the data
 			if (event.data == null) { // no data to copy, indicate failure in event.detail
 				event.detail = DND.DROP_NONE;
 				return;
 			}
 			label.setText ((String) event.data); // data copied to label text
 		}
  	});
*/


/*
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		ProjectionViewer viewer= (ProjectionViewer) getSourceViewer();
		fProjectionSupport= new ProjectionSupport(viewer, getAnnotationAccess(), getSharedColors());
		fProjectionSupport.addSummarizableAnnotationType("org.eclipse.ui.workbench.texteditor.error"); //$NON-NLS-1$
		fProjectionSupport.addSummarizableAnnotationType("org.eclipse.ui.workbench.texteditor.warning"); //$NON-NLS-1$
		fProjectionSupport.install();
		viewer.doOperation(ProjectionViewer.TOGGLE);
	}
*/	
	/*
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#adjustHighlightRange(int, int)
	 */
/*
	protected void adjustHighlightRange(int offset, int length) {
		ISourceViewer viewer= getSourceViewer();
		if (viewer instanceof ITextViewerExtension5) {
			ITextViewerExtension5 extension= (ITextViewerExtension5) viewer;
			extension.exposeModelRange(new Region(offset, length));
		}
	}
*/


	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragEnter(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragEnter(DropTargetEvent event) {
//		System.out.println("DRAG ENTERS: " + event);
//		ExpressCompilerPlugin.log("DRAG ENTERS", 1);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragLeave(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragLeave(DropTargetEvent event) {
//		System.out.println("DRAG LEAVES: " + event);
//		ExpressCompilerPlugin.log("DRAG LEAVES", 1);
	  drop(event);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragOperationChanged(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragOperationChanged(DropTargetEvent event) {
//		System.out.println("DRAG OPERATION CHANGES: " + event);
//		ExpressCompilerPlugin.log("DRAG OPERATION CHANGES", 1);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragOver(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragOver(DropTargetEvent event) {
//		System.out.println("DRAG OVER: " + event);
//		ExpressCompilerPlugin.log("DRAG OVER", 1);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DropTargetListener#drop(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void drop_old(DropTargetEvent event) {

//		System.out.println("Drop event: " + event); 

		ISelection selection = LocalSelectionTransfer.getInstance().getSelection();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (structuredSelection.size() == 1) {
				Object selectedResource = structuredSelection.getFirstElement();
				if (selectedResource instanceof IResource) {
					IResource the_selected_resource = (IResource) selectedResource;
					if (the_selected_resource instanceof IFile) {
						IFile the_selected_file = (IFile)the_selected_resource;
						String file_name = the_selected_file.getName();

						if (the_selected_file.getFileExtension().equalsIgnoreCase("exp")) {
							String filePath = the_selected_file.getLocation().toOSString();
//							System.out.println("dropped file: " + file_name); 
//							System.out.println("dropped file path: " + filePath); 

            
	            ISourceViewer viewer = getSourceViewer();
							IDocument document = viewer.getDocument();
							try {
							
								/* 
								   we may want to prevent duplicates of the same files
									 we may also want to sort the document, but that may cause a lack of obvious feedback to the user,
									 unless the inserted line is displayed in a different color, etc.
								*/
							
								int line_count = document.getNumberOfLines();
								boolean line_found = false;
								int end_line_number = -1;
								for (int i = 0; i < line_count; i++) {
									IRegion region = document.getLineInformation(i);
									String line = document.get(region.getOffset(), region.getLength());
//System.out.println(" line " + i + ": " + line);								
									if (line.equalsIgnoreCase(filePath)) {
										line_found = true;
										break;
									}
									if (line.startsWith("#### end of the list")) {
										end_line_number = i;
									}
								} 
								if (!line_found) {
									int put_it_here = 0;
									if (end_line_number > -1) {
//System.out.println("end_line: " + end_line_number);
										// put it before that line
										IRegion r;
										if (end_line_number - 1 >= 0) {
											r = document.getLineInformation(end_line_number-1);
											String s = document.get(r.getOffset(), r.getLength());
											if (s.equals("")) {
// System.out.println("EMPTY LINE");
												// empty line
												put_it_here = r.getOffset();
												document.replace(put_it_here,0, filePath + "\n");
											} else {
// System.out.println("NO EMPTY LINE");
												// insert an empty line
												put_it_here = document.getLineInformation(end_line_number).getOffset();
												// document.replace(put_it_here,0,"\n");
												// put_it_here += 1;
//												document.replace(put_it_here,0, "\n" + filePath);
												document.replace(put_it_here,0, filePath + "\n\n");
											}
										} else {
//System.out.println("end_line (the first): " + end_line_number);
//											put_it_here = document.getLineInformation(end_line_number).getOffset();
											document.replace(0,0, "\n" + filePath + "\n\n");
										}
									} else {
										// put it at the end
										// but, if the last line is new line, insert before it,
										// if not - add also a newline at the end
										if (line_count == 0) {
											document.replace(0,0, " \n" + filePath + "\n\n");
										} else {
											IRegion r = document.getLineInformation(line_count - 1);
											String s = document.get(r.getOffset(), r.getLength());
											if (s.equals("")) {
													document.replace(r.getOffset(),0, filePath + "\n");
											} else {
												document.replace(document.getLength(),0, "\n" + filePath + "\n\n");
											}
										}
									}
								}
							} catch (BadLocationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} // if exp 
					} else { // if not IFile
//						System.out.println("dropped not file: " + the_selected_resource); 
					}
				} else { // not IResource
				}
			} else { //  more than one	
//				System.out.println("dropped more than 1"); 

				Iterator i_selection = structuredSelection.iterator();
//				Object first_selection = null;
				while (i_selection.hasNext()) {
					Object selectedResource2 = i_selection.next();
					if (selectedResource2 instanceof IResource) {

						IResource the_selected_resource = (IResource) selectedResource2;
						if (the_selected_resource instanceof IFile) {
							IFile the_selected_file = (IFile)the_selected_resource;
//							String file_name = the_selected_file.getName();

							if (the_selected_file.getFileExtension().equalsIgnoreCase("exp")) {
								String filePath = the_selected_file.getLocation().toOSString();


		            ISourceViewer viewer = getSourceViewer();
								IDocument document = viewer.getDocument();
								try {
								
									/* 
									   we may want to prevent duplicates of the same files
										 we may also want to sort the document, but that may cause a lack of obvious feedback to the user,
										 unless the inserted line is displayed in a different color, etc.
									*/
							
									int line_count = document.getNumberOfLines();
									boolean line_found = false;
									for (int i = 0; i < line_count; i++) {
										IRegion region = document.getLineInformation(i);
										String line = document.get(region.getOffset(), region.getLength());
//System.out.println(" line " + i + ": " + line);								
										if (line.equalsIgnoreCase(filePath)) {
											line_found = true;
											break;
										}
									} 
									if (!line_found) {
										document.replace(document.getLength(),0, "\n" + filePath);
									}
							
							
								} catch (BadLocationException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} // exp
						} else { // not IFile (directories and projects are of interest to us
						}
					} else {// not IResource
					}
				} // while		
			} // more than 1
		} else {  // not structured selection
//			System.out.println("dropped NOT structured selection"); 
		}
	}

	public void drop(DropTargetEvent event) {

//		System.out.println("Drop event: " + event); 
//		ExpressCompilerPlugin.log("DROP EVENT", 1);

		ISelection selection = LocalSelectionTransfer.getInstance().getSelection();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (structuredSelection.size() == 1) {
				Object selectedResource = structuredSelection.getFirstElement();
				if (selectedResource instanceof IResource) {
					IResource the_selected_resource = (IResource) selectedResource;
					if (the_selected_resource instanceof IFile) {
						IFile the_selected_file = (IFile)the_selected_resource;
//						String file_name = the_selected_file.getName();

						if (the_selected_file.getFileExtension().equalsIgnoreCase("exp")) {
//							String filePath = the_selected_file.getLocation().toOSString();
							String filePath = the_selected_file.getFullPath().toOSString();
							if (filePath.startsWith("\\") || filePath.startsWith("/")) {
								filePath = filePath.substring(1);
							}
							// System.out.println("dropped file: " + file_name); 
							// System.out.println("dropped file path: " + filePath); 
							dropLine(filePath);
            
						} // if exp 
					} else { // if not IFile
//						System.out.println("dropped not file: " + the_selected_resource); 
					}
				} else { // not IResource
				}
			} else { //  more than one	
//				System.out.println("dropped more than 1"); 

				Iterator i_selection = structuredSelection.iterator();
//				Object first_selection = null;
				while (i_selection.hasNext()) {
					Object selectedResource2 = i_selection.next();
					if (selectedResource2 instanceof IResource) {

						IResource the_selected_resource = (IResource) selectedResource2;
						if (the_selected_resource instanceof IFile) {
							IFile the_selected_file = (IFile)the_selected_resource;
//							String file_name = the_selected_file.getName();

							if (the_selected_file.getFileExtension().equalsIgnoreCase("exp")) {
//								String filePath = the_selected_file.getLocation().toOSString();
									String filePath = the_selected_file.getFullPath().toOSString();
									if (filePath.startsWith("\\") || filePath.startsWith("/")) {
										filePath = filePath.substring(1);
									}
									dropLine(filePath);

							} // exp
						} else { // not IFile (directories and projects are of interest to us
						}
					} else {// not IResource
					}
				} // while		
			} // more than 1
		} else {  // not structured selection
//			System.out.println("dropped NOT structured selection"); 
		}
	}


	public void dropAccept(DropTargetEvent event) {
		// TODO Auto-generated method stub
//System.out.println("dropAccept");		
//ExpressCompilerPlugin.log("DROP ACCEPT", 1);
	}

  void dropLine(String filePath) {

	            ISourceViewer viewer = getSourceViewer();
							IDocument document = viewer.getDocument();
							try {
							
								/* 
								   we may want to prevent duplicates of the same files
									 we may also want to sort the document, but that may cause a lack of obvious feedback to the user,
									 unless the inserted line is displayed in a different color, etc.
								*/
							
								int line_count = document.getNumberOfLines();
								boolean line_found = false;
								int end_line_number = -1;
								for (int i = 0; i < line_count; i++) {
									IRegion region = document.getLineInformation(i);
									String line = document.get(region.getOffset(), region.getLength());
//System.out.println(" line " + i + ": " + line);								
									if (line.equalsIgnoreCase(filePath)) {
										line_found = true;
										break;
									}
									if (line.startsWith("#### end of the list")) {
										end_line_number = i;
									}
								} 
								if (!line_found) {
									int put_it_here = 0;
									if (end_line_number > -1) {
//System.out.println("end_line: " + end_line_number);
										// put it before that line
										IRegion r;
										if (end_line_number - 1 >= 0) {
											r = document.getLineInformation(end_line_number-1);
											String s = document.get(r.getOffset(), r.getLength());
											if (s.equals("")) {
// System.out.println("EMPTY LINE");
												// empty line
												put_it_here = r.getOffset();
												document.replace(put_it_here,0, filePath + "\n");
											} else {
// System.out.println("NO EMPTY LINE");
												// insert an empty line
												put_it_here = document.getLineInformation(end_line_number).getOffset();
												// document.replace(put_it_here,0,"\n");
												// put_it_here += 1;
//												document.replace(put_it_here,0, "\n" + filePath);
												document.replace(put_it_here,0, filePath + "\n\n");
											}
										} else {
//System.out.println("end_line (the first): " + end_line_number);
//											put_it_here = document.getLineInformation(end_line_number).getOffset();
											document.replace(0,0, "\n" + filePath + "\n\n");
										}
									} else {
										// put it at the end
										// but, if the last line is new line, insert before it,
										// if not - add also a newline at the end
										if (line_count == 0) {
											document.replace(0,0, " \n" + filePath + "\n\n");
										} else {
											IRegion r = document.getLineInformation(line_count - 1);
											String s = document.get(r.getOffset(), r.getLength());
											if (s.equals("")) {
													document.replace(r.getOffset(),0, filePath + "\n");
											} else {
												document.replace(document.getLength(),0, "\n" + filePath + "\n\n");
											}
										}
									}
								}
							} catch (BadLocationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}


							// ok, we did everything we needed, but now try to sort the lines
							sortLines();
  }


	// the complication here is caused by the header comment block, footer comment block, and possible commented out lines
	// commented out lines should be sorted as if they were not commented out, I think
	// However, the user may want to type his own comments related to some line above or below, that is the problem
	
	void sortLines() {
	}


}
