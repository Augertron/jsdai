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
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import jsdai.dictionary.ESchema_definition;
import jsdai.lang.ASchemaInstance;
import jsdai.lang.ASdaiModel;
import jsdai.lang.A_string;
import jsdai.lang.EEntity;
import jsdai.lang.SchemaInstance;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiRepository;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotSupportedException;

/**
 * This class is an abstract <code>org.xml.sax.XMLReader</code> implementation
 * which reads JSDAI data and presents it in ISO 10303-28 representation.
 * It allows extending classes to implement specific bindings. Objects
 * of classes extending <code>InstanceReader</code> are primarily useful
 * for exporting JSDAI populations to XML representation.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public abstract class InstanceReader extends SdaiXmlReader {

	protected static final String SCHEMA_ID_PREFIX = "schema";
	protected static final String MODEL_ID_PREFIX = "m";

	protected boolean featureSchemaPopulationNames;
	protected boolean featureMakeSchemaPopulations;

	protected SdaiInputSource sdaiInputSource;
	protected HashMap modelIds;
	protected HashMap schemaIds;
// 	private HashMap literalNames;

	protected String defaultNamespacePrefix;

	static protected ThreadLocal localTransformer = new ThreadLocal();

	protected InstanceReader() {
		this.modelIds = new HashMap();
		this.schemaIds = new HashMap();
// 		this.literalNames = new HashMap();
		featureSchemaPopulationNames = true;
		featureMakeSchemaPopulations = true;
	}

	/**
	 * Sets the state of the feature for this <code>InstanceReader</code>.
	 * The following features are recognized:
	 * <dl>
	 * <dt>schema-population-names</dt>
	 * <dd>If true, <code>name</code> attribute is added to
	 *     <code>&lt;schema-population&gt;</code> elements</dd> which is
	 *     the <code>name</code> attribute of <code>SchemaInstances</code>.
	 *     This is the extension of ISO 10303-28. The default value is <code>true</code>.</dd>
	 * <dt>make-schema-populations</dt>
	 * <dd>If true, <code>&lt;schema-population&gt;</code> element is added immediately after
	 *     the header otherwise this information is skipped. The default value is <code>true</code>.</dd>
	 * </dl>
	 *
	 * @param name The feature name, see the list above for recognized features.
	 * @param value The <code>true</code> or <code>false</code> value.
	 */
	public void setFeature(String name, boolean value) {
		if(name.equals("schema-population-names")) {
			featureSchemaPopulationNames = value;
		} else if(name.equals("make-schema-populations")) {
			featureMakeSchemaPopulations = value;
		}
	}

	/**
	 * Serializes JSDAI population as XML text to the output stream.
	 * This is a convenient method to get XML text output from JSDAI.
	 * It uses <a href="http://http://java.sun.com/xml/jaxp/index.jsp">JAXP Transformer API</a>
	 * for serialization.
	 *
	 * @param stream The output stream of the serialized output
	 * @param inputSource The SDAI input source which defines the population
	 * @exception IOException if an I/O error occurs
	 * @exception SAXException if an error occurs during the operation
	 *                         or in underlying JSDAI operations
	 */
	public void serialize(OutputStream stream, SdaiInputSource inputSource)
	throws TransformerConfigurationException, TransformerException {
		Transformer transformer = (Transformer)localTransformer.get();
		if(transformer == null) {
			transformer = TransformerFactory.newInstance().newTransformer();
			Properties outputProperties = new Properties();
			outputProperties.setProperty(OutputKeys.METHOD, "xml");
			outputProperties.setProperty(OutputKeys.INDENT, "yes");
			outputProperties.setProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			outputProperties.setProperty("{http://xml.apache.org/xalan}indent-amount", "2");
			transformer.setOutputProperties(outputProperties);
			localTransformer.set(transformer);
		}
		transformer.transform(new SAXSource(this, inputSource), new StreamResult(stream));
	}

	/**
	 * Parses XML from JSDAI population. This method
	 * contains the main logic in creating the XML.
	 *
	 * @param input The input source which has to be of class {@link SdaiInputSource}
	 * @exception IOException if an I/O error occurs
	 * @exception SAXException if an error occurs during the operation
	 *                         or in underlying JSDAI operations
	 */
	public void parse (InputSource input)
	throws IOException, SAXException {
		try {
			sdaiInputSource = (SdaiInputSource)input;
		} catch(ClassCastException e) {
			throw new SAXNotSupportedException("Input source shoud be SdaiInputSource");
		}
		try {
			SdaiRepository repository = sdaiInputSource.getRepository();
			SchemaInstance schInstance = sdaiInputSource.getSchemaInstance();
			ASchemaInstance schInstances = sdaiInputSource.getSchemaInstances();
			SdaiModel model = sdaiInputSource.getModel();
			ASdaiModel[] models = sdaiInputSource.getModels();
            //create model & schema ids
			modelIds.clear();
			schemaIds.clear();
			int schemaDefinitionNum = 1;
			if(models == null) {
				modelIds.put(model, MODEL_ID_PREFIX + '0');
				schemaIds.put(model.getUnderlyingSchema(), SCHEMA_ID_PREFIX + '1');
				schemaDefinitionNum++;
			} else {
				for(int i = 0; i < models.length; i++) {
					int modelNum = 0;
					for(SdaiIterator it = models[i].createIterator(); it.next();) {
						SdaiModel currModel = models[i].getCurrentMember(it);
						if(!modelIds.containsKey(currModel)) {
							modelIds.put(currModel, MODEL_ID_PREFIX + modelNum++);
						}
						ESchema_definition schema = currModel.getUnderlyingSchema();
						if(!schemaIds.containsKey(schema)) {
							schemaIds.put(schema, SCHEMA_ID_PREFIX + schemaDefinitionNum++);
						}
					}
				}
			}
			//create schema ids
			if(schInstances == null) {
				if(schInstance != null) {
					ESchema_definition schema = schInstance.getNativeSchema();
					if(!schemaIds.containsKey(schema)) {
						schemaIds.put(schema, SCHEMA_ID_PREFIX + schemaDefinitionNum++);
					}
					ASdaiModel schemaModels = schInstance.getAssociatedModels();
					for(SdaiIterator it = schemaModels.createIterator(); it.next();) {
						schema = schemaModels.getCurrentMember(it).getUnderlyingSchema();
						if(!schemaIds.containsKey(schema)) {
							schemaIds.put(schema, SCHEMA_ID_PREFIX + schemaDefinitionNum++);
						}
					}
				}
			} else {
				for (SdaiIterator schIter = schInstances.createIterator(); schIter.next();) {
					SchemaInstance schema_instance = schInstances.getCurrentMember(schIter);
					ESchema_definition schema = schema_instance.getNativeSchema();
                    if(!schemaIds.containsKey(schema)) {
                        schemaIds.put(schema, SCHEMA_ID_PREFIX + schemaDefinitionNum++);
					}
					ASdaiModel schemaModels = schema_instance.getAssociatedModels();
					for(SdaiIterator it = schemaModels.createIterator(); it.next();){
						schema = schemaModels.getCurrentMember(it).getUnderlyingSchema();
						if(!schemaIds.containsKey(schema)) {
							schemaIds.put(schema, SCHEMA_ID_PREFIX + schemaDefinitionNum++);
						}
					}
				}
			}

			defaultNamespacePrefix = "";
			contentHandler.startDocument();
			attributes.clear();

			//this should handle a subclass
			setRootAttributes(attributes);

			contentHandler.startElement(null, "iso_10303_28",
										defaultNamespacePrefix + "iso_10303_28", attributes);
            parseHeader(repository);

			for(Iterator it = schemaIds.entrySet().iterator(); it.hasNext();){
				Map.Entry entry = (Map.Entry)it.next();
				parseExpressSchema((ESchema_definition)entry.getKey(), (String)entry.getValue());
			}

			if(featureMakeSchemaPopulations) {
				if(schInstances == null) {
					if(schInstance != null) {
						parseSchInst(schInstance, modelIds, schemaIds);
					}
				} else {
					SdaiIterator schIter = schInstances.createIterator();
					while(schIter.next()) {
						parseSchInst(schInstances.getCurrentMember(schIter), modelIds, schemaIds);
					}
				}
			}

			if(models == null) {
				ESchema_definition schema_definition = model.getUnderlyingSchema();
				String schema_definition_id = (String) schemaIds.get(schema_definition);
				if(schema_definition_id == null)
					throw new SdaiException(SdaiException.VA_NVLD, "Schema " +
											schema_definition.getName(null) + " id not found.");
				parseModel(model, "m0", schema_definition_id);
			} else {
				Iterator modelIter = modelIds.entrySet().iterator();
				while(modelIter.hasNext()) {
					Map.Entry modelEntry = (Map.Entry)modelIter.next();
					SdaiModel currModel = (SdaiModel) modelEntry.getKey();
					ESchema_definition schema_definition = currModel.getUnderlyingSchema();
                    String schema_definition_id = (String) schemaIds.get(schema_definition);
					if(schema_definition_id == null)
						throw new SdaiException(SdaiException.VA_NVLD, "Schema " +
												schema_definition.getName(null) + " id not found.");
					parseModel(currModel,
							String.valueOf(modelEntry.getValue()),
							String.valueOf(schema_definition_id));
				}
			}
			contentHandler.endElement(null, "iso_10303_28", defaultNamespacePrefix + "iso_10303_28");
			contentHandler.endDocument();
		} catch(SdaiException e) {
			throw (SAXException)(new SAXException(e.toString())).initCause(e);
		} finally {
			modelIds.clear();
// 			literalNames.clear();
		}
	}

	/**
	 * sets root element attributes that specify version of p28 file
	 * @param attr target attribute set
	 */
	protected abstract void setRootAttributes(Attributes attr);

	/**
	 * sets nessery info in header
	 *
	 */
	private void parseHeader(SdaiRepository repository)
	throws SAXException, SdaiException
	{
		//************************** START of iso_10303_28_header ************************
		attributes.clear();
		contentHandler.startElement(null, "iso_10303_28_header",
									defaultNamespacePrefix + "iso_10303_28_header", attributes);

		contentHandler.startElement(null, "document_name",
									defaultNamespacePrefix + "document_name", attributes);
		putStrings(repository.getDescription());
		contentHandler.endElement(null,"document_name", defaultNamespacePrefix + "document_name");

		contentHandler.startElement(null, "time_stamp", defaultNamespacePrefix + "time_stamp", attributes);
		String string = repository.getChangeDate();
		if(string != null && string.length() > 0) {
			contentHandler.characters(string.toCharArray(), 0, string.length());
		}
		contentHandler.endElement(null, "time_stamp", defaultNamespacePrefix + "time_stamp");

		contentHandler.startElement(null, "author", defaultNamespacePrefix + "author", attributes);
		putStrings(repository.getAuthor());
		contentHandler.endElement(null, "author", defaultNamespacePrefix + "author");

		contentHandler.startElement(null, "originating_organization",
									defaultNamespacePrefix + "originating_organization", attributes);
		putStrings(repository.getOrganization());
		contentHandler.endElement(null, "originating_organization",
								  defaultNamespacePrefix + "originating_organization");

		contentHandler.startElement(null, "authorization",
									defaultNamespacePrefix + "authorization", attributes);
		string = repository.getAuthorization();
		if(string != null && string.length() > 0) {
			contentHandler.characters(string.toCharArray(), 0, string.length());
		}
		contentHandler.endElement(null, "authorization", defaultNamespacePrefix + "authorization");

		contentHandler.startElement(null, "originating_system",
									defaultNamespacePrefix + "originating_system", attributes);
		string = repository.getOriginatingSystem();
		if(string != null && string.length() > 0) {
			contentHandler.characters(string.toCharArray(), 0, string.length());
		}
		contentHandler.endElement(null, "originating_system",
								  defaultNamespacePrefix + "originating_system");

		contentHandler.startElement(null, "preprocessor_version", 
									defaultNamespacePrefix + "preprocessor_version", attributes);
		string = repository.getPreprocessorVersion();
		if(string != null && string.length() > 0) {
			contentHandler.characters(string.toCharArray(), 0, string.length());
		}
		contentHandler.endElement(null, "preprocessor_version",
								  defaultNamespacePrefix + "preprocessor_version");

		contentHandler.endElement(null, "iso_10303_28_header", 
								  defaultNamespacePrefix + "iso_10303_28_header");
		//************************** END of iso_10303_28_header ************************
	}

	protected abstract void parseExpressSchema(ESchema_definition schema, String schema_id)
	throws IOException, SAXException, SdaiException;

	protected abstract void parseSchInst(SchemaInstance currSchInst, Map mIds, Map sIds)
	throws IOException, SAXException, SdaiException;

	protected abstract void parseModel(SdaiModel currModel, String currModelId, String nativeSchemaId)
	throws IOException, SAXException, SdaiException;

	protected static String getDefinitionSchemaName(EEntity definition)
	throws SdaiException
	{
		String fullName = definition.findEntityInstanceSdaiModel().getName();
		return fullName.substring(0, fullName.length() - "_DICTIONARY_DATA".length());
	}

	private void putStrings(A_string strings)
	throws SdaiException, SAXException {
		SdaiIterator stringIter = strings.createIterator();
		boolean notFirst = false;
		while(stringIter.next()) {
			String string = strings.getCurrentMember(stringIter);
			if(notFirst) {
				contentHandler.characters("\n".toCharArray(), 0, 1);
			}
			if(string.length() > 0) {
				contentHandler.characters(string.toCharArray(), 0, string.length());
			}
			notFirst = true;
		}
	}

}
