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

import jsdai.express_compiler.utils.PartitionUtils;

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;

public class P21DocumentSetupParticipant implements IDocumentSetupParticipant {

  private static P21PartitionScanner fScanner = new P21PartitionScanner();;

	/*
		required by IDocumentSetupParticipant. 
  */
  public void setup(IDocument document) {
    setupPartitioner(document);
  }

	/*
  	-RR:
  	  	
  	requered because I want to call it myself without bothering about instance dependencies
		NOTE - this is to work around a bug in Eclipse 3.0, when this bug is fixed (is it already fixed in 3.1?)
		this method may no longer be needed - to look into it
		THE BUG IN ECLIPSE 3.0 - in some cases (for some ways the file is opened), setup() may not be called at all
		
		Whe work-around is: 
		In the createPartControl() override in the editor class, adding the following lines:

		if (document != null)
    	P21DocumentSetupParticipant.setupPartitioner(document);
		
		possible side effects of this work-around:
		firing partitionChanged events, if we are listening to those events, it is better to make a special case for this iticialization.
		
		BTW, better to listen to documentChanged events - they report all changes, including but not limited to partitionChanged events		
	
	*/
  public static void setupPartitioner(IDocument document) {
    /*
    	check if a partitioner is already installed, because Eclipse may call setup() more than once for the same document
    */
    if (PartitionUtils.getPartitioner(document, P21Constants.P21_PARTITIONER) == null)
        PartitionUtils.setDocumentPartitioning(document, P21Constants.P21_PARTITIONER, createP21Partitioner());
  }

  private static IDocumentPartitioner createP21Partitioner() {
    // do we need our own P21Partitioner?
	//    return new P21Partitioner(fScanner, P21Constants.P21_PARTITION_TYPES);
	return new FastPartitioner(fScanner, P21Constants.P21_PARTITION_TYPES);
  }
}


