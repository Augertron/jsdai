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

package jsdai.express_compiler.utils;

import jsdai.express_compiler.ExpressCompilerPlugin;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPartitioningException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.ITypedRegion;

/*
  Here are methods that are different for new Eclipse 3.0 style API for IDocumentExtension3 rather than for old 2.0 IDocument
  The provided implementation here supports only 3.0, if also 2.0 support is needed, the else part has to be implemented in those methods

*/
public class PartitionUtils {

	public static ITypedRegion getPartition(String partitionerId, IDocument document, String partitionType, int offset, boolean preferOpenPartition) {
    ITypedRegion region = null;
    try {
        if (document instanceof IDocumentExtension3) {
            IDocumentExtension3 extension = (IDocumentExtension3) document;
            try {
                region = extension.getPartition(partitionerId, offset, true);
            } catch (BadPartitioningException e) {
				ExpressCompilerPlugin.log(e);
            }
        }
    } catch (BadLocationException e) {
		ExpressCompilerPlugin.log(e);
    }
    return region;
	}
  
	public static void setDocumentPartitioning(IDocument document, String partitionType, IDocumentPartitioner partitioner) {
    // Setting the partitioner will trigger a partitionChanged
    // listener that will attempt to use the partitioner to
    // initialize the document's partitions. Therefore, need
    // to connect first.
    // -RR: NOTE - other implementations often connect at the end instead
    partitioner.connect(document);
    if (document instanceof IDocumentExtension3) {
        IDocumentExtension3 extension3= (IDocumentExtension3) document;
        extension3.setDocumentPartitioner(partitionType, partitioner);
    } 
	}

	public static IDocumentPartitioner getPartitioner(IDocument document, String partitionType) {
    IDocumentPartitioner result = null;
    if (document instanceof IDocumentExtension3) {
        IDocumentExtension3 extension = (IDocumentExtension3) document;
        result = extension.getDocumentPartitioner(partitionType);
    } 
    return result;
	}
}