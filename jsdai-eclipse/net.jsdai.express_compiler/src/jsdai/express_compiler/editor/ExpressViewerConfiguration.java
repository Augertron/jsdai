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
import jsdai.express_compiler.preferences.ExpressEditorPreferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;
import org.eclipse.ui.internal.editors.text.EditorsPlugin;

public class ExpressViewerConfiguration extends TextSourceViewerConfiguration {
//public class ExpressViewerConfiguration extends SourceViewerConfiguration {

    private ExpressDoubleClickStrategy fDoubleClickStrategy;
    // private ExpressDocScanner fTagScanner;
    private ExpressCodeScanner fScanner;
    private ExpressCommentScanner fCommentScanner;
    private ExpressSingleLineCommentScanner fSingleLineCommentScanner;
    private ExpressStringScanner fStringScanner;
//TODO something with fColorProvider
    //    private ExpressColorProvider fColorProvider;


		/**
		 * Single token scanner - for multi-line comments, instead, a special multi-line comment scanner may be implemented
		 */
		static class SingleTokenScanner extends BufferedRuleBasedScanner {
			public SingleTokenScanner(TextAttribute attribute) {
				setDefaultReturnToken(new Token(attribute));
			}
		}


    public ExpressViewerConfiguration(ExpressColorProvider color_provider) {
//TODO something with fColorProvider
    	//        fColorProvider = color_provider;
    }

    public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
//    	return new String[] { IDocument.DEFAULT_CONTENT_TYPE, ExpressPartitionScanner.EXPRESS_SPEC_COMMENT, ExpressPartitionScanner.EXPRESS_MULTILINE_COMMENT };
//    	return new String[] { IDocument.DEFAULT_CONTENT_TYPE, ExpressPartitionScanner.EXPRESS_MULTILINE_COMMENT };
    	return new String[] { IDocument.DEFAULT_CONTENT_TYPE, ExpressConstants.EXPRESS_MULTILINE_COMMENT, ExpressConstants.EXPRESS_SINGLELINE_COMMENT, ExpressConstants.EXPRESS_STRING};
    }


    public ITextDoubleClickStrategy getDoubleClickStrategy(
    	ISourceViewer sourceViewer, String contentType) {
      if (fDoubleClickStrategy == null) {
     		fDoubleClickStrategy = new ExpressDoubleClickStrategy();
      }
      return fDoubleClickStrategy;
    }


		// alternative - provide the singletone factory method in the plugin class
    protected ExpressCodeScanner getExpressScanner() {
    	if (fScanner == null) {
//      	fScanner = new ExpressCodeScanner(fColorProvider);
      	fScanner = new ExpressCodeScanner(ExpressCompilerPlugin.getDefault().getExpressColorProvider());
      }
      return fScanner;
    }

    protected ExpressCommentScanner getCommentScanner() {
    	if (fCommentScanner == null) {
//      	fScanner = new ExpressCodeScanner(fColorProvider);
      	fCommentScanner = new ExpressCommentScanner(ExpressCompilerPlugin.getDefault().getExpressColorProvider());
      }
      return fCommentScanner;
    }

    protected ExpressSingleLineCommentScanner getSingleLineCommentScanner() {
    	if (fSingleLineCommentScanner == null) {
//      	fScanner = new ExpressCodeScanner(fColorProvider);
      	fSingleLineCommentScanner = new ExpressSingleLineCommentScanner(ExpressCompilerPlugin.getDefault().getExpressColorProvider());
      }
      return fSingleLineCommentScanner;
    }

    protected ExpressStringScanner getStringScanner() {
    	if (fStringScanner == null) {
//      	fScanner = new ExpressCodeScanner(fColorProvider);
      	fStringScanner = new ExpressStringScanner(ExpressCompilerPlugin.getDefault().getExpressColorProvider());
      }
      return fStringScanner;
    }


	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {

/*

		// ExpressColorProvider provider = ExpressCompilerPlugin.getDefault().getExpressColorProvider();
		PresentationReconciler reconciler = new PresentationReconciler();
		
		reconciler.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));
		
		// for default text
		// DefaultDamagerRepairer dr = new DefaultDamagerRepairer(ExpressCompilerPlugin.getDefault().getExpressCodeScanner());
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getExpressScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
		
    // for multi-line comments
//		dr= new DefaultDamagerRepairer(new SingleTokenScanner(new TextAttribute(fColorProvider.getColor(ExpressColorProvider.MULTI_LINE_COMMENT))));
		dr= new DefaultDamagerRepairer(new SingleTokenScanner(new TextAttribute(ExpressCompilerPlugin.getDefault().getExpressColorProvider().getColor(ExpressCompilerPlugin.getDefault().getExpressColorProvider().getColorPreference(ExpressPreferencePage.EXPRESS_MULTI_COLOR)))));
		reconciler.setDamager(dr, ExpressPartitionScanner.EXPRESS_MULTILINE_COMMENT);
		reconciler.setRepairer(dr, ExpressPartitionScanner.EXPRESS_MULTILINE_COMMENT);

*/

//	ExpressColorProvider provider = ExpressCompilerPlugin.getDefault().getExpressColorProvider();

	PresentationReconciler reconciler = new PresentationReconciler();
	// new:
	reconciler.setDocumentPartitioning(ExpressConstants.EXPRESS_PARTITIONER);

	 setDamagerRepairer(reconciler);
	 
	 return reconciler;

	}
	
	public void setDamagerRepairer(PresentationReconciler reconciler) {
		
//	DefaultDamagerRepairer dr = new DefaultDamagerRepairer(ExpressCompilerPlugin.getDefault().getExpressCodeScanner());
	DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getExpressScanner());
	reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
	reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

	// express spec comment, but we remove it, move to tags instead
//	dr= new DefaultDamagerRepairer(new SingleTokenScanner(new TextAttribute(provider.getColor(ExpressColorProvider.EXPRESS_SPEC_COMMENT))));
//	reconciler.setDamager(dr, ExpressPartitionScanner.EXPRESS_SP	EC_COMMENT);
//	reconciler.setRepairer(dr, ExpressPartitionScanner.EXPRESS_SPEC_COMMENT);



		IPreferenceStore store = EditorsPlugin.getDefault().getPreferenceStore();

		boolean multi_bold = store.getBoolean(ExpressEditorPreferences.EXPRESS_MULTI_BOLD);
		boolean multi_italic = store.getBoolean(ExpressEditorPreferences.EXPRESS_MULTI_ITALIC); 
		boolean multi_strike = store.getBoolean(ExpressEditorPreferences.EXPRESS_MULTI_STRIKE);
		boolean multi_underline = store.getBoolean(ExpressEditorPreferences.EXPRESS_MULTI_UNDERLINE);
		int multi_flags = 0; 
		if (multi_bold) multi_flags |= SWT.BOLD;
		if (multi_italic) multi_flags |= SWT.ITALIC;
		if (multi_strike) multi_flags |= TextAttribute.STRIKETHROUGH;
		if (multi_underline) multi_flags |= TextAttribute.UNDERLINE;

//		ExpressColorProvider provider = ExpressCompilerPlugin.getDefault().getExpressColorProvider();









	dr = new DefaultDamagerRepairer(getCommentScanner());
//	dr= new DefaultDamagerRepairer(new SingleTokenScanner(new TextAttribute(provider.getColor(provider.getColorPreference(ExpressEditorPreferences.EXPRESS_MULTI_COLOR)), provider.getColor(provider.getColorPreference(ExpressEditorPreferences.EXPRESS_MULTI_BG_COLOR)), multi_flags)));

//	dr= new DefaultDamagerRepairer(new SingleTokenScanner(new TextAttribute(provider.getColor(provider.getColorPreference(ExpressEditorPreferences.EXPRESS_MULTI_COLOR)))));
//	reconciler.setDamager(dr, ExpressPartitionScanner.EXPRESS_MULTILINE_COMMENT);
	reconciler.setDamager(dr, ExpressConstants.EXPRESS_MULTILINE_COMMENT);
//	reconciler.setRepairer(dr, ExpressPartitionScanner.EXPRESS_MULTILINE_COMMENT);
	reconciler.setRepairer(dr, ExpressConstants.EXPRESS_MULTILINE_COMMENT);

	dr = new DefaultDamagerRepairer(getSingleLineCommentScanner());
	reconciler.setDamager(dr, ExpressConstants.EXPRESS_SINGLELINE_COMMENT);
	reconciler.setRepairer(dr, ExpressConstants.EXPRESS_SINGLELINE_COMMENT);

	dr = new DefaultDamagerRepairer(getStringScanner());
	reconciler.setDamager(dr, ExpressConstants.EXPRESS_STRING);
	reconciler.setRepairer(dr, ExpressConstants.EXPRESS_STRING);


//		return reconciler;
	}




    /* (non-Javadoc)
     * Method declared on SourceViewerConfiguration
     */
    public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
    
    	ContentAssistant assistant = new ContentAssistant();
  		// new:
  		assistant.setDocumentPartitioning(ExpressConstants.EXPRESS_PARTITIONER);
  		
  		assistant.setContentAssistProcessor(new ExpressCompletionProcessor(), IDocument.DEFAULT_CONTENT_TYPE);

    	assistant.enableAutoActivation(true);
    	assistant.setAutoActivationDelay(500);
    	assistant.setProposalPopupOrientation(IContentAssistant.PROPOSAL_OVERLAY);
    	assistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
//    	assistant.setContextInformationPopupBackground(fColorProvider.getColor(new RGB(150, 150, 0)));
    	assistant.setContextInformationPopupBackground(ExpressCompilerPlugin.getDefault().getExpressColorProvider().getColor(new RGB(150, 150, 0)));
    
    	return assistant;
    }

    /* (non-Javadoc)
     * Method declared on SourceViewerConfiguration
     */
    public String getDefaultPrefix(ISourceViewer sourceViewer, String contentType) {
    	return (IDocument.DEFAULT_CONTENT_TYPE.equals(contentType) ? "//" : null); //$NON-NLS-1$
    }

    /* (non-Javadoc)
     * Method declared on SourceViewerConfiguration
     */
    public String[] getIndentPrefixes(ISourceViewer sourceViewer, String contentType) {
    	return new String[] { "\t", "    " }; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /* (non-Javadoc)
     * Method declared on SourceViewerConfiguration
     */
    public int getTabWidth(ISourceViewer sourceViewer) {
    	return 4;
    }


	public String getConfiguredDocumentPartitioning(ISourceViewer sourceViewer) {
		return ExpressConstants.EXPRESS_PARTITIONER;
	}

    



}


