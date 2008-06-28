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

import jsdai.lang.ASdaiRepository;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiRepository;
import jsdai.lang.SdaiSession;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * Created: Tue Jul 30 18:22:15 2002
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public class SdaiSessionReader extends SdaiXmlReader {

	private SdaiSession sdaiSession;

	public SdaiSessionReader(SdaiSession sdaiSession) {
		this.sdaiSession = sdaiSession;
	}
		
	public void parse (InputSource input)
		throws IOException, SAXException {
		try {
			contentHandler.startDocument();
			attributes.clear();
			contentHandler.startElement(null, "session", null, attributes);
			ASdaiRepository repositories = sdaiSession.getKnownServers();
			SdaiIterator repositoryIter = repositories.createIterator();
			while(repositoryIter.next()) {
				SdaiRepository repository = repositories.getCurrentMember(repositoryIter);
				attributes.clear();
				attributes.addAttribute(null, "name", "name", "NMTOKEN", 
										repository.getName());
				contentHandler.startElement(null, "repository", null, attributes);
				contentHandler.endElement(null, null, null);
			}
			contentHandler.endElement(null, null, null);
			contentHandler.endDocument();
		} catch(SdaiException e) {
			throw new SAXException(e);
		}
	}
}
