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

import jsdai.express_compiler.ExpressCompilerPlugin;

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
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.graphics.RGB;

public class ExpressListViewerConfiguration extends SourceViewerConfiguration {

    private ExpressListDoubleClickStrategy fDoubleClickStrategy;
    // private ExpressDocScanner fTagScanner;
    private ExpressListCodeScanner fScanner;
//TODO something with fColorProvider
    //    private ExpressColorProvider fColorProvider;

		/**
		 * Single token scanner.
		 */
		static class SingleTokenScanner extends BufferedRuleBasedScanner {
			public SingleTokenScanner(TextAttribute attribute) {
				setDefaultReturnToken(new Token(attribute));
			}
		}

    public ExpressListViewerConfiguration(ExpressListColorProvider color_provider) {
//TODO something with fColorProvider
    	//        fColorProvider = color_provider;
    }

    public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
//    	return new String[] { IDocument.DEFAULT_CONTENT_TYPE, ExpressListPartitionScanner.EXPRESS_SPEC_COMMENT, ExpressPartitionScanner.EXPRESS_MULTILINE_COMMENT };
    	return new String[] { IDocument.DEFAULT_CONTENT_TYPE};
    }


    public ITextDoubleClickStrategy getDoubleClickStrategy(
    	ISourceViewer sourceViewer, String contentType) {
      if (fDoubleClickStrategy == null) {
     		fDoubleClickStrategy = new ExpressListDoubleClickStrategy();
      }
      return fDoubleClickStrategy;
    }


		// alternative - provide the singletone factory method in the plugin class
    protected ExpressListCodeScanner getExpressListScanner() {
    	if (fScanner == null) {
//      	fScanner = new ExpressCodeScanner(fColorProvider);
      	fScanner = new ExpressListCodeScanner(ExpressCompilerPlugin.getDefault().getExpressListColorProvider());
      }
      return fScanner;
    }



	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {

		// ExpressColorProvider provider = ExpressCompilerPlugin.getDefault().getExpressColorProvider();
		PresentationReconciler reconciler = new PresentationReconciler();
		
		reconciler.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));
		
		// for default text
		// DefaultDamagerRepairer dr = new DefaultDamagerRepairer(ExpressCompilerPlugin.getDefault().getExpressCodeScanner());
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getExpressListScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
		
    // for multi-line comments
//		dr= new DefaultDamagerRepairer(new SingleTokenScanner(new TextAttribute(fColorProvider.getColor(ExpressColorProvider.MULTI_LINE_COMMENT))));
//		dr= new DefaultDamagerRepairer(new SingleTokenScanner(new TextAttribute(ExpressCompilerPlugin.getDefault().getExpressListColorProvider().getColor(ExpressColorProvider.MULTI_LINE_COMMENT))));
//		reconciler.setDamager(dr, ExpressPartitionScanner.EXPRESS_MULTILINE_COMMENT);
//		reconciler.setRepairer(dr, ExpressPartitionScanner.EXPRESS_MULTILINE_COMMENT);

		return reconciler;
	}

    /* (non-Javadoc)
     * Method declared on SourceViewerConfiguration
     */
    public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
    
    	ContentAssistant assistant = new ContentAssistant();
    	assistant.setContentAssistProcessor(new ExpressListCompletionProcessor(), IDocument.DEFAULT_CONTENT_TYPE);

    	assistant.enableAutoActivation(true);
    	assistant.setAutoActivationDelay(500);
    	assistant.setProposalPopupOrientation(IContentAssistant.PROPOSAL_OVERLAY);
    	assistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
//    	assistant.setContextInformationPopupBackground(fColorProvider.getColor(new RGB(150, 150, 0)));
    	assistant.setContextInformationPopupBackground(ExpressCompilerPlugin.getDefault().getExpressListColorProvider().getColor(new RGB(150, 150, 0)));
    
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


   
    



}


