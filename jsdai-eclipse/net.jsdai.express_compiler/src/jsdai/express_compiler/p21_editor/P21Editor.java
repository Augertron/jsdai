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

package jsdai.express_compiler.p21_editor;

import jsdai.express_compiler.ExpressCompilerPlugin;
import jsdai.express_compiler.help.IExpressCompilerHelpContextIds;

import org.eclipse.jface.text.ITextViewerExtension5;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.internal.editors.text.EditorsPlugin;

//public class P21Editor extends AbstractDecoratedTextEditor {
public class P21Editor extends TextEditor {

	private P21ColorProvider fP21ColorProvider;
	ProjectionSupport fProjectionSupport;	
	
	public P21Editor() {
		super();
      //install the source configuration
//      setSourceViewerConfiguration(new P21Configuration());
      //install the document provider
//      setDocumentProvider(new P21DocumentProvider());
   }
   protected void createActions() {
      super.createActions();
      //... add other editor actions here
   }




	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		ProjectionViewer viewer= (ProjectionViewer) getSourceViewer();
		fProjectionSupport= new ProjectionSupport(viewer, getAnnotationAccess(), getSharedColors());
		fProjectionSupport.addSummarizableAnnotationType("org.eclipse.ui.workbench.texteditor.error"); //$NON-NLS-1$
		fProjectionSupport.addSummarizableAnnotationType("org.eclipse.ui.workbench.texteditor.warning"); //$NON-NLS-1$
		fProjectionSupport.install();
		viewer.doOperation(ProjectionViewer.TOGGLE);
	
		// for bug-workaround, but need to have document
		//if (document != null)
    	//P21DocumentSetupParticipant.setupPartitioner(document);

	}
	



	protected void initializeEditor() {
		super.initializeEditor();
//		setSourceViewerConfiguration(new P21SourceViewerConfiguration());
	  setSourceViewerConfiguration(new P21SourceViewerConfiguration(fP21ColorProvider));
		setPreferenceStore(EditorsPlugin.getDefault().getPreferenceStore());
	  setHelpContextId(IExpressCompilerHelpContextIds.P21_EDITOR);
}

	protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
		
		fAnnotationAccess= createAnnotationAccess();
		fOverviewRuler= createOverviewRuler(getSharedColors());
		
		ISourceViewer viewer= new ProjectionViewer(parent, ruler, getOverviewRuler(), isOverviewRulerVisible(), styles);
		// ensure decoration support has been created and configured.
		getSourceViewerDecorationSupport(viewer);
		
		return viewer;
	}
	
	protected void adjustHighlightRange(int offset, int length) {
		ISourceViewer viewer= getSourceViewer();
		if (viewer instanceof ITextViewerExtension5) {
			ITextViewerExtension5 extension= (ITextViewerExtension5) viewer;
			extension.exposeModelRange(new Region(offset, length));
		}
	}


	protected void handlePreferenceStoreChanged(PropertyChangeEvent event) {
			super.handlePreferenceStoreChanged(event);

			
//System.out.println("EDITOR- property changed");			

		ISourceViewer viewer = getSourceViewer();
		P21SourceViewerConfiguration sourceViewerConfiguration = (P21SourceViewerConfiguration)getSourceViewerConfiguration();
	
//	IPresentationReconciler pr = sourceViewerConfiguration.getPresentationReconciler(viewer);
	// hm, it does it the second time
//	sourceViewerConfiguration.setDamagerRepairer((PresentationReconciler)pr);
//	pr.install(viewer);
//	pr.uninstall();
//	pr.install(viewer);
	
//	P21CodeScanner p21scanner = ExpressCompilerPlugin.getDefault().getP21CodeScanner();
	P21CodeScanner p21scanner = sourceViewerConfiguration.getCodeScanner();
	P21ComplexInstanceScanner complexscanner = sourceViewerConfiguration.getComplexInstanceScanner();

	P21CommentScanner comscanner = sourceViewerConfiguration.getCommentScanner();

	//	exscanner.setRules(null);  // parameter - IRule []
	P21ColorProvider provider = ExpressCompilerPlugin.getDefault().getP21ColorProvider();
	p21scanner.initRules(provider);
	complexscanner.initRules(provider);
	comscanner.initRules(provider);
	viewer.invalidateTextPresentation();

	
	
//	viewer.setDocument(viewer.getDocument());
//	pr.uninstall();
//	pr.install(viewer);
	////		if (affectsTextPresentation(event)) {
//			sourceViewerConfiguration.adaptToPreferenceChange(event);
////

//		sourceViewerConfiguration.changeConfiguration(event);
	}	






}



/*
       document provider versus document setup participant
   
*/