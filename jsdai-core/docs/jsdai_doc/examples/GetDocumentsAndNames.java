/*
 * $Id$
 *
 * Copyright © LKSoftWare GmbH, 2002-2003. All Rights Reserved.
 *
 */

import java.io.IOException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import jsdai.lang.EEntity;
import jsdai.lang.QueryResultSet;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiQuery;
import jsdai.lang.SdaiRepository;
import jsdai.lang.SdaiSession;
import jsdai.lang.SdaiTransaction;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * GetDocumentsAndNames.java
 *
 *
 * Created: Fri Jul  4 14:24:12 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */
public class GetDocumentsAndNames {

	public static void main(String[] args) throws SdaiException,
												  ParserConfigurationException,
												  IOException,
												  SAXException {
		if(args.length < 1) {
			System.out.println("java GetDocumentsAndNames ap214_file.stp");
			return;
		}
		getDocumentsAndNames(args[0]);
	}

	public static void getDocumentsAndNames(String stepFile)
		throws SdaiException, ParserConfigurationException,
			   IOException, SAXException {

        SdaiSession sdaiSession = SdaiSession.openSession();
        SdaiTransaction transaction = sdaiSession.startTransactionReadWriteAccess();
		SdaiRepository repository = sdaiSession.importClearTextEncoding("", stepFile, null);
		SdaiModel model = repository.findSdaiModel("default");
		long startMillis = System.currentTimeMillis();
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		documentBuilderFactory.setIgnoringComments(true);
		documentBuilderFactory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		queryDocumentsAndNames(sdaiSession, model, documentBuilder);
		System.out.println("Query time: " + (System.currentTimeMillis() - startMillis));
	}

	public static void queryDocumentsAndNames(SdaiSession sdaiSession, SdaiModel model, 
											  DocumentBuilder documentBuilder)
		throws SdaiException, ParserConfigurationException,
			   IOException, SAXException {

		URL queryLibUrl = GetDocumentsAndNames.class.getResource("arm214DocumentLib.xml");
		Document queryLibSpec = documentBuilder.parse(queryLibUrl.openStream(), queryLibUrl.toString());
		SdaiQuery queryLib = sdaiSession.newQuery(queryLibSpec);

		URL queryUrl = GetDocumentsAndNames.class.getResource("queryDocumentsAndNames.xml");
		Document querySpec = documentBuilder.parse(queryUrl.openStream(), queryUrl.toString());
		SdaiQuery query = sdaiSession.newQuery(querySpec);
		query.execute(model);
		QueryResultSet rSet = query.getResultSet("documents-and-names");
		while(rSet.next()) {
			EEntity document = (EEntity)rSet.getItem(1);
			String name = (String)rSet.getItem(2);
			System.out.println(document.getPersistentLabel() + "   \t" + name);
		}
	}

} // GetDocumentsAndNames

/*
  Local Variables:
  compile-command: "ant -emacs -find build.xml get.documents.and.names"
  End:
*/
