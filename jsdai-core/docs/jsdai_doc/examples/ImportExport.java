// ImportExport.java
// Copyright (c) LKSoftWare GmbH, 1999-2003. All Rights Reserved.
// This software is provided "AS IS," without any warranty

// Test program for JSDAI. Reads a physical file from input_file. 
// Writes it back to output_file and deletes the repository. 
//                                                                            
// USAGE: java importExport input_file output_file
                                                                             
// Author: Raimundas Raciunas
// Date: 1999-06-03

import java.io.*;
import jsdai.lang.*;

public class ImportExport {
	public static final void main(String argv[]) throws jsdai.lang.SdaiException {
		if (argv.length != 2) { 
			System.out.println("USAGE: java importExport imput_file output_file");
			return;
		}
		SdaiSession.setLogWriter(new PrintWriter(System.out, true));
		SdaiSession session = SdaiSession.openSession();
		SdaiTransaction trans = session.startTransactionReadWriteAccess();
		
		SdaiRepository repo = session.importClearTextEncoding(null, argv[0], null);
		
		//  repo.openRepository() -- already open after import
		
		repo.exportClearTextEncoding(argv[1]);
		trans.endTransactionAccessAbort();
		repo.closeRepository();
		repo.deleteRepository();
		session.closeSession();
	}
}