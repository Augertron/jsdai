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

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

//public class P21SourceViewerConfiguration extends SourceViewerConfiguration {
public class P21SourceViewerConfiguration extends TextSourceViewerConfiguration {

	P21CommentScanner fCommentScanner;
	P21CodeScanner fCodeScanner;
	P21ComplexInstanceScanner fComplexScanner;

/*

Switching to Eclipse 3.x style API where a document may be shared by several editors
Here, more than one partitioner may be used on a document, and partitioners are identified by their IDs


adding new lines (one in each of two overriden methods:


getPresentationReconciler() {

	reconciler.setDocumentPartitioning(P21EditorConstants.P21_PARTITIONER);

}


getContentAssistant() {

	assistant.setDocumentPartitioning(P21EditorConstants.P21_PARTITIONER);

}

adding the following method:

public String getConfiguredDocumentPartitioning(ISourceViewer sourceViewer) {
    return P21EditorConstants.P21_PARTITIONER;
}


adding the participant to plugin.xml

making a work-around for Eclipse 3.0 bug - see in P21DocumentSetupParticipant.java

removing all 2.0 calls and replacing them with 3.0 calls.
That is, removing calls (explicit or implicit) to all the methods declared in 
IDocument and replacing them with methods declared in IDocumentExtension3

to replace all, for example:

getPartition() 
setDocumentPartitioning()
getPartitioner()

Perhaps it is convenient to isolate all the new api calls in a single class PartitionUtils

*/

   public P21SourceViewerConfiguration(P21ColorProvider color_provider) {
//TODO something with fColorProvider
    	//        fColorProvider = color_provider;
    }





		/**
		 * Single token scanner - for multi-line comments, instead, a special multi-line comment scanner may be implemented
		 */
		static class SingleTokenScanner extends BufferedRuleBasedScanner {
			public SingleTokenScanner(TextAttribute attribute) {
				setDefaultReturnToken(new Token(attribute));
			}
		}
		



	// not sure if will use
/*
	protected RuleBasedScanner getCodeScanner() {
		return fCodeScanner;
	}
*/

    protected P21CodeScanner getCodeScanner() {
    	if (fCodeScanner == null) {
//      	fScanner = new ExpressCodeScanner(fColorProvider);
      	fCodeScanner = new P21CodeScanner(ExpressCompilerPlugin.getDefault().getP21ColorProvider());
      }
      return fCodeScanner;
    }


    protected P21CommentScanner getCommentScanner() {
    	if (fCommentScanner == null) {
//      	fScanner = new ExpressCodeScanner(fColorProvider);
      	fCommentScanner = new P21CommentScanner(ExpressCompilerPlugin.getDefault().getP21ColorProvider());
      }
      return fCommentScanner;
    }

    protected P21ComplexInstanceScanner getComplexInstanceScanner() {
    	if (fComplexScanner == null) {
//      	fScanner = new ExpressCodeScanner(fColorProvider);
      	fComplexScanner = new P21ComplexInstanceScanner(ExpressCompilerPlugin.getDefault().getP21ColorProvider());
      }
      return fComplexScanner;
    }



	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {


		// may be our own P21PresentationReconsiler
		PresentationReconciler reconciler= new PresentationReconciler();

		// setting explicitly our own partitioner
		//	reconciler.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));
		reconciler.setDocumentPartitioning(P21Constants.P21_PARTITIONER);

	 setDamagerRepairer(reconciler);
	 
	 return reconciler;

	}
	
	public void setDamagerRepairer(PresentationReconciler reconciler) {

//		DefaultDamagerRepairer dr= new DefaultDamagerRepairer(getCodeScanner());
		// may go this way:
//		DefaultDamagerRepairer dr= new DefaultDamagerRepairer(ExpressCompilerPlugin.getDefault().getP21CodeScanner());
		DefaultDamagerRepairer dr= new DefaultDamagerRepairer(getCodeScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);


//		P21ColorProvider provider= ExpressCompilerPlugin.getDefault().getP21ColorProvider();




		dr = new DefaultDamagerRepairer(getCommentScanner());
		// dr= new DefaultDamagerRepairer(new SingleTokenScanner(new TextAttribute(provider.getColor(P21ColorProvider.MULTI_LINE_COMMENT))));
//		dr= new DefaultDamagerRepairer(getMultilineCommentScanner());
		reconciler.setDamager(dr, P21Constants.P21_MULTILINE_COMMENT);
		reconciler.setRepairer(dr, P21Constants.P21_MULTILINE_COMMENT);


		dr = new DefaultDamagerRepairer(getComplexInstanceScanner());
		reconciler.setDamager(dr, P21Constants.P21_COMPLEX_INSTANCE);
		reconciler.setRepairer(dr, P21Constants.P21_COMPLEX_INSTANCE);



//		return reconciler;
}


	/* (non-Javadoc)
	 * Method declared on SourceViewerConfiguration
	 */
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {

		// may check first if editor != null, if null, also return null
		// if (getEditor() != null) {
			ContentAssistant assistant= new ContentAssistant();

			// assistant.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));
			// more specific implementation:
			assistant.setDocumentPartitioning(P21Constants.P21_PARTITIONER);

			return assistant;
		// }
		// return null;

	}

	public String getConfiguredDocumentPartitioning(ISourceViewer sourceViewer) {
    return P21Constants.P21_PARTITIONER;
	}
	


}  