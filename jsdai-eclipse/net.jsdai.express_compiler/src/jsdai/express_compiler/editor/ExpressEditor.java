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

package jsdai.express_compiler.editor;

import jsdai.express_compiler.ExpressCompilerPlugin;
import jsdai.express_compiler.actions.ExpressCompilerEditorAction;
import jsdai.express_compiler.actions.GoToMatchingPeerAction;
import jsdai.express_compiler.actions.IExpressEditorActionDefinitionIds;
import jsdai.express_compiler.actions.SelectMatchingPeersAction;
import jsdai.express_compiler.help.IExpressCompilerHelpContextIds;
import jsdai.express_compiler.preferences.ExpressEditorPreferences;
import jsdai.express_compiler.utils.ExpressCompilerUtils;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewerExtension5;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.source.ICharacterPairMatcher;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.internal.editors.text.EditorsPlugin;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;

//public class ExpressEditor extends AbstractDecoratedTextEditor {
public class ExpressEditor extends TextEditor  {

	private ExpressColorProvider fColorProvider;

	public final static String EDITOR_MATCHING_BRACKETS= "matchingBrackets"; //$NON-NLS-1$

	public final static String EDITOR_MATCHING_BRACKETS_COLOR=  "matchingBracketsColor"; //$NON-NLS-1$

	
	protected static final char[] EXPRESSCHARPAIRS = 
		{ '{', '}', // not really express, but so what
			'(', ')', 
//			'*', ')',  //  (*    *)
			'[', ']', 
			'<', '>'  // not really express, but so what
		};

	protected static final String[] EXPRESCHARSWORDSPAIRS = 
		{ 
			// may include or not this part
//			"{", "}", // not really express, but so what
//			"(", ")", 
//			"[", "]", 
//			"<", ">",  // not really express, but so what
			// real words here
			"BEGIN", "END",
			"ALIAS", "END_ALIAS",
			"CASE", "END_CASE",
			"CONSTANT", "END_CONSTANT",
			"ENTITY", "END_ENTITY",
			"FUNCTION", "END_FUNCTION",
			"IF", "END_IF",
			"LOCAL", "END_LOCAL",
			"PROCEDURE", "END_PROCEDURE",
			"REPEAT", "END_REPEAT",
			"RULE", "END_RULE",
			"SCHEMA", "END_SCHEMA",
			"SUBTYPE_CONSTRAINT", "END_SUBTYPE_CONSTRAINT",
			"TYPE", "END_TYPE"
		};

	protected static final String[] EXPRESWORDSPAIRS = 
		{ 
			// may include or not this part
			"{", "}", // not really express, but so what
			"(", ")", 
			"[", "]", 
			"<", ">",  // not really express, but so what
			// real words here
			"BEGIN", "END",
			"ALIAS", "END_ALIAS",
			"CASE", "END_CASE",
			"CONSTANT", "END_CONSTANT",
			"ENTITY", "END_ENTITY",
			"FUNCTION", "END_FUNCTION",
			"IF", "END_IF",
			"LOCAL", "END_LOCAL",
			"PROCEDURE", "END_PROCEDURE",
			"REPEAT", "END_REPEAT",
			"RULE", "END_RULE",
			"SCHEMA", "END_SCHEMA",
			"SUBTYPE_CONSTRAINT", "END_SUBTYPE_CONSTRAINT",
			"TYPE", "END_TYPE"
		};

	protected ExpressPairMatcher fPairMatcher = new ExpressPairMatcher(EXPRESSCHARPAIRS, EXPRESWORDSPAIRS);


	public ExpressEditor() {
		super();

		// fColorProvider = new ExpressColorProvider();
		// setSourceViewerConfiguration(new ExpressViewerConfiguration(fColorProvider));
		// setDocumentProvider(new ExpressDocumentProvider());
	}
	public void dispose() {

		if (fPairMatcher != null) {
			fPairMatcher.dispose();
			fPairMatcher = null;
		}

		if (fColorProvider != null) // temp 
			fColorProvider.dispose();
		else
//			System.out.println("ERROR <001> - fColorProvider is NULL");		
			super.dispose();
	}	

    /**
     *  See in preferences if we want auto-compile upon saving the file, if yes, run Express Compiler
     *  
     */
    public void doSave(IProgressMonitor monitor) {
    	super.doSave(monitor);
    
    	if (EditorsPlugin.getDefault().getPreferenceStore().getBoolean(ExpressEditorPreferences.AUTO_COMPILE)) {
    		ExpressCompilerEditorAction compAction = new ExpressCompilerEditorAction();
    		compAction.selectionChanged(null, getSelectionProvider().getSelection());
    		compAction.run(null);
    	}
    }


//----------------------------- ----------------------------------------------------------------

	// no difference -------------------
	/* (non-ExpressDoc)
	 * Method declared on AbstractTextEditor
	 */
	protected void initializeEditor() {
		super.initializeEditor();
		setSourceViewerConfiguration(new ExpressViewerConfiguration(fColorProvider));
//		setEditorContextMenuId("#ExpressEditorContext"); //$NON-NLS-1$
//		setRulerContextMenuId("#ExpressRulerContext"); //$NON-NLS-1$

//	    WorkbenchHelp.setHelp(panel, SdaieditPlugin.ID_SDAIEDIT + ".ExpressG_EditorContextId");
	    
//	    IWorkbenchHelpSystem help = WorkbenchHelpSystem.getInstance();
//	    help.setHelp(this, ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID + ".Express_EditorContextId");
//		PlatformUI.getWorkbench().getHelpSystem().setHelp(getFieldEditorParent(), IExpressCompilerHelpContextIds.EXPRESS_DOC_PREFERENCE_PAGE);
//	    setHelpContextId(ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID + ".Express_EditorContextId");
	    setHelpContextId(IExpressCompilerHelpContextIds.EXPRESS_EDITOR);
			ExpressCompilerUtils.switchPerspective(getSite());
//			setPreferenceStore(EditorsPlugin.getDefault().getPreferenceStore());


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

/*
	protected void updatePropertyDependentActions() {
		super.updatePropertyDependentActions();
System.out.println("Are we here or not?");
// file has been changed, do you want to load changes
// handleEditorInputChanged();
		//reconsile();
	}
*/	


		protected void handlePreferenceStoreChanged(PropertyChangeEvent event) {
			super.handlePreferenceStoreChanged(event);

			
//System.out.println("EDITOR- property changed");			

	ISourceViewer viewer = getSourceViewer();
	ExpressViewerConfiguration sourceViewerConfiguration = (ExpressViewerConfiguration)getSourceViewerConfiguration();
	
//	IPresentationReconciler pr = sourceViewerConfiguration.getPresentationReconciler(viewer);
	// hm, it does it the second time
//	sourceViewerConfiguration.setDamagerRepairer((PresentationReconciler)pr);
//	pr.install(viewer);
//	pr.uninstall();
//	pr.install(viewer);
	
	ExpressCodeScanner exscanner = sourceViewerConfiguration.getExpressScanner();
	ExpressCommentScanner comscanner = sourceViewerConfiguration.getCommentScanner();
	ExpressSingleLineCommentScanner slcomscanner = sourceViewerConfiguration.getSingleLineCommentScanner();
	ExpressStringScanner stringscanner = sourceViewerConfiguration.getStringScanner();
	//	exscanner.setRules(null);  // parameter - IRule []
	ExpressColorProvider provider = ExpressCompilerPlugin.getDefault().getExpressColorProvider();
	exscanner.initRules(provider);
	comscanner.initRules(provider);
	slcomscanner.initRules(provider);
	stringscanner.initRules(provider);
	viewer.invalidateTextPresentation();

	
	
//	viewer.setDocument(viewer.getDocument());
//	pr.uninstall();
//	pr.install(viewer);
	////		if (affectsTextPresentation(event)) {
//			sourceViewerConfiguration.adaptToPreferenceChange(event);
////

//		sourceViewerConfiguration.changeConfiguration(event);
	}	



	public ISourceViewer getExpressSourceViewer() {
		return getSourceViewer();
	}

	protected void configureSourceViewerDecorationSupport(SourceViewerDecorationSupport support) {

		EditorsPlugin.getDefault().getPreferenceStore().setDefault(EDITOR_MATCHING_BRACKETS, true);
		PreferenceConverter.setDefault(EditorsPlugin.getDefault().getPreferenceStore(), EDITOR_MATCHING_BRACKETS_COLOR, new RGB(192, 192,192));
		
		
		//fPairMatcher.setSourceVersion(getPreferenceStore().getString(JavaCore.COMPILER_SOURCE));
		support.setCharacterPairMatcher(fPairMatcher);
		support.setMatchingCharacterPainterPreferenceKeys(EDITOR_MATCHING_BRACKETS, EDITOR_MATCHING_BRACKETS_COLOR);

		super.configureSourceViewerDecorationSupport(support);
	}

	protected void initializeKeyBindingScopes() {
		setKeyBindingScopes(new String[] { "net.jsdai.express_editor.expressEditorScope" });  //$NON-NLS-1$
	}


	public void selectMatchingPeers() {

// System.out.println("Select matching peers");
		
		ISourceViewer textViewer = getSourceViewer();

		int offset = textViewer.getSelectedRange().x;

		if (offset < 0)
			return;

		IDocument document = textViewer.getDocument();




		IRegion region = fPairMatcher.matchPairWhole(document, offset);
		if (region != null && region.getLength() >= 2) {
			textViewer.setSelectedRange(region.getOffset(), region.getLength() - 1);
		}

	}


	// jumps to the matching peer
	public void goToMatchingPeer() {

//System.out.println("JUMP to matching peer");

		ISourceViewer sourceViewer= getSourceViewer();
		IDocument document= sourceViewer.getDocument();
		if (document == null)
			return;

		IRegion selection = getSignedSelection(sourceViewer);

		int selectionLength= Math.abs(selection.getLength());
		if (selectionLength > 1) {
//			setStatusLineErrorMessage("invalid selection");
			sourceViewer.getTextWidget().getDisplay().beep();

//System.out.println("JUMP - BEEP 1");

			return;
		}

		// #26314
		int sourceCaretOffset= selection.getOffset() + selection.getLength();
//System.out.println("JUMP - 1 - sourceCaretOffset: "  + sourceCaretOffset);
		if (isSurroundedByBrackets(document, sourceCaretOffset))
			sourceCaretOffset -= selection.getLength();
//System.out.println("JUMP - 2 - sourceCaretOffset: "  + sourceCaretOffset);

		IRegion region= fPairMatcher.matchPairJump(document, sourceCaretOffset);


		if (region == null) {
			//setStatusLineErrorMessage("no matching peer");
			sourceViewer.getTextWidget().getDisplay().beep();
//System.out.println("JUMP - BEEP 2");
			return;
		}

		int offset= region.getOffset();
		int length= region.getLength();

		if (length < 1)
			return;

		int anchor= fPairMatcher.getAnchor();
		// http://dev.eclipse.org/bugs/show_bug.cgi?id=34195
		int targetOffset= (ICharacterPairMatcher.RIGHT == anchor) ? offset + 1: offset + length;

		boolean visible= false;
		if (sourceViewer instanceof ITextViewerExtension5) {
			ITextViewerExtension5 extension= (ITextViewerExtension5) sourceViewer;
			visible= (extension.modelOffset2WidgetOffset(targetOffset) > -1);
		} else {
			IRegion visibleRegion= sourceViewer.getVisibleRegion();
			// http://dev.eclipse.org/bugs/show_bug.cgi?id=34195
			visible= (targetOffset >= visibleRegion.getOffset() && targetOffset <= visibleRegion.getOffset() + visibleRegion.getLength());
		}

		if (!visible) {
//			setStatusLineErrorMessage("peer outside selected element");
			sourceViewer.getTextWidget().getDisplay().beep();
			return;
		}

		if (selection.getLength() < 0)
			targetOffset -= selection.getLength();

		sourceViewer.setSelectedRange(targetOffset, selection.getLength());
		sourceViewer.revealRange(targetOffset, selection.getLength());

	
	}

	protected IRegion getSignedSelection(ISourceViewer sourceViewer) {
		StyledText text= sourceViewer.getTextWidget();
		Point selection= text.getSelectionRange();

		if (text.getCaretOffset() == selection.x) {
			selection.x= selection.x + selection.y;
			selection.y= -selection.y;
		}

		selection.x= widgetOffset2ModelOffset(sourceViewer, selection.x);

		return new Region(selection.x, selection.y);
	}

	private static boolean isBracket(char character) {
		for (int i= 0; i != EXPRESSCHARPAIRS.length; ++i)
			if (character == EXPRESSCHARPAIRS[i])
				return true;
		return false;
	}

	private static boolean isSurroundedByBrackets(IDocument document, int offset) {
		if (offset == 0 || offset == document.getLength())
			return false;

		try {
			return
				isBracket(document.getChar(offset - 1)) &&
				isBracket(document.getChar(offset));

		} catch (BadLocationException e) {
			return false;
		}
	}


	protected void createActions() {
		super.createActions();

		Action action = new GoToMatchingPeerAction(this);
		action.setActionDefinitionId(IExpressEditorActionDefinitionIds.GOTO_MATCHING_PEER);
		setAction(GoToMatchingPeerAction.GOTO_MATCHING_PEER, action);

		action = new SelectMatchingPeersAction(this);
		action.setActionDefinitionId(IExpressEditorActionDefinitionIds.SELECT_MATCHING_PEERS);
		setAction(SelectMatchingPeersAction.SELECT_MATCHING_PEERS, action);

	}

}

