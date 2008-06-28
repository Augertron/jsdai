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

package jsdai.xml;

import java.io.IOException;
import jsdai.lang.ASchemaInstance;
import jsdai.lang.ASdaiModel;
import jsdai.lang.SchemaInstance;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiRepository;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * Created: Tue Jul 30 18:28:23 2002
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public class RepositoryReader extends SdaiXmlReader {

	private SdaiRepository repository;

	public RepositoryReader(SdaiRepository repository) {
		this.repository = repository;
	}
		
	public void parse (InputSource input)
	throws IOException, SAXException {
		try {
			contentHandler.startDocument();
			attributes.clear();
			attributes.addAttribute(null, "name", "name", "NMTOKEN", 
									repository.getName());
			contentHandler.startElement(null, "repository", "repository", attributes);
			ASdaiModel models = repository.getModels();
			SdaiIterator modelIter = models.createIterator();
			while(modelIter.next()) {
				SdaiModel model = models.getCurrentMember(modelIter);
				attributes.clear();
				attributes.addAttribute(null, "name", "name", "NMTOKEN", 
										model.getName());
				contentHandler.startElement(null, "model", "model", attributes);
				contentHandler.endElement(null, null, null);
			}
			ASchemaInstance schemas = repository.getSchemas();
			SdaiIterator schemaIter = schemas.createIterator();
			while(schemaIter.next()) {
				SchemaInstance schema = schemas.getCurrentMember(schemaIter);
				attributes.clear();
				attributes.addAttribute(null, "name", "name", "NMTOKEN", 
										schema.getName());
				contentHandler.startElement(null, "schema_instance", "schema_instance", attributes);
				models = schema.getAssociatedModels();
				modelIter = models.createIterator();
				while(modelIter.next()) {
					SdaiModel model = models.getCurrentMember(modelIter);
					attributes.clear();
					attributes.addAttribute(null, "name", "name", "NMTOKEN", 
											model.getName());
					contentHandler.startElement(null, "model", "model", attributes);
					contentHandler.endElement(null, null, null);
				}
				contentHandler.endElement(null, null, null);
			}
			contentHandler.endElement(null, null, null);
			contentHandler.endDocument();
		} catch(SdaiException e) {
			throw new SAXException(e);
		}
	}
}
