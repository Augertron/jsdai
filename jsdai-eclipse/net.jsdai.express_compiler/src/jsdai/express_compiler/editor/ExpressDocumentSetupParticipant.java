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

import jsdai.express_compiler.utils.PartitionUtils;

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;

/**
 * 
 */
public class ExpressDocumentSetupParticipant implements IDocumentSetupParticipant {

  private static ExpressPartitionScanner fScanner = new ExpressPartitionScanner();;
	
	/*
		required by IDocumentSetupParticipant. 
  */
  public void setup(IDocument document) {
    setupPartitioner(document);
  }

  public static void setupPartitioner(IDocument document) {
    /*
    	check if a partitioner is already installed, because Eclipse may call setup() more than once for the same document
    */
    if (PartitionUtils.getPartitioner(document, ExpressConstants.EXPRESS_PARTITIONER) == null)
        PartitionUtils.setDocumentPartitioning(document, ExpressConstants.EXPRESS_PARTITIONER, createExpressPartitioner());
  }

  private static IDocumentPartitioner createExpressPartitioner() {
    // do we need our own P21Partitioner?
		//    return new P21Partitioner(fScanner, P21Constants.P21_PARTITION_TYPES);
		return new FastPartitioner(fScanner, ExpressConstants.EXPRESS_PARTITION_TYPES);
//		return new FastPartitioner(fScanner, ExpressPartitionScanner.EXPRESS_PARTITION_TYPES);
  }


	/**
	 */
/*
	public ExpressDocumentSetupParticipant() {
	}
*/

	/*
	 * @see org.eclipse.core.filebuffers.IDocumentSetupParticipant#setup(org.eclipse.jface.text.IDocument)
	 */
/*
	public void setup(IDocument document) {
		if (document instanceof IDocumentExtension3) {
			IDocumentExtension3 extension3 = (IDocumentExtension3) document;
			IDocumentPartitioner partitioner= new FastPartitioner(ExpressCompilerPlugin.getDefault().getExpressPartitionScanner(), ExpressPartitionScanner.EXPRESS_PARTITION_TYPES);
			extension3.setDocumentPartitioner(ExpressCompilerPlugin.EXPRESS_PARTITIONING, partitioner);
			partitioner.connect(document);
		}
	}
*/
}
