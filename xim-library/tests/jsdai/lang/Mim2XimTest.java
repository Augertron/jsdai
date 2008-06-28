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

package jsdai.lang;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jsdai.dictionary.ANamed_type;
import jsdai.dictionary.EAttribute;
import jsdai.dictionary.EDefined_type;
import jsdai.dictionary.EEntity_definition;
import jsdai.dictionary.EExplicit_attribute;
import jsdai.libutil.CMappedXIMEntity;
import jsdai.mapping.AEntity_mapping;
import jsdai.mapping.AGeneric_attribute_mapping;
import jsdai.mapping.EEntity_mapping;
import jsdai.mapping.EGeneric_attribute_mapping;
import junit.framework.Test;


/**
 *
 * Created: Tue Oct 28 16:14:58 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public class Mim2XimTest extends FunctionalRepositoryTestSuite {

// 	private static String MIM_SUFFIX_LC = "_mim";
// 	private static String XIM_SUFFIX = "_XIM";
// 	private static int XIM_SUFFIX_LEN = XIM_SUFFIX.length();

	SdaiModel model;
	SdaiContext context;
	
	public Mim2XimTest(boolean remote) {
		super(remote);
	}
	
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}
	
	public static Test suite() {
		FunctionalRepositoryTestSuite mainSuite = new FunctionalRepositoryTestSuite(false);
		Mim2XimTest suite = new Mim2XimTest(false);
		suite.addTestSuite(InnerTestCase.class);
		mainSuite.addTest(suite);
		return mainSuite;
	}

	protected void setUp() throws Exception {
		super.setUp();
		purgeRepository();

		importData(getClass().getResource("test/preview.stp"));
// 		importData(new URL("file:///home/vaidas/jsdai/temp/Vaidas/preview.stp"));
		model = getRepository().findSdaiModel("default");
// 		model = getRepository().findSdaiModel("d:\\JOB\\_projects\\AP210Book\\modules\\jsdaix\\DATA\\MENTOR\\Surface_Mount_flasher\\surfaceMountFlasher.stp");

// 		ESchema_definition modelSchema = model.getUnderlyingSchema();
// 		String modelSchemaName = modelSchema.getName(null);
// 		if(modelSchemaName.endsWith(XIM_SUFFIX)) {
// 			modelSchema = model.getRepository().getSession()
// 				.findSchemaDefinition(modelSchemaName.substring(0, modelSchemaName.length() - XIM_SUFFIX_LEN).toLowerCase() +
// 									  MIM_SUFFIX_LC);
// 		} else {
// 			fail("Part21 file should be imported to XIM schema");
// 		}
// 		context = new SdaiContext(modelSchema, null, model);

		// This works only ifjsdai.properties has those lines:
		// mapping.schema.AP210_ELECTRONIC_ASSEMBLY_INTERCONNECT_AND_PACKAGING_DESIGN_XIM=\
		//     AP210_ELECTRONIC_ASSEMBLY_INTERCONNECT_AND_PACKAGING_DESIGN_XIM_MAPPING_DATA
		// jsdai.SAp210_electronic_assembly_interconnect_and_packaging_design_xim=\
		//     AP210_ELECTRONIC_ASSEMBLY_INTERCONNECT_AND_PACKAGING_DESIGN_MIM;

 		context = new SdaiContext(model);
	}
	
	public class InnerTestCase extends ExtendedTestCase {
		
		public InnerTestCase(String name) {
			super(name);
		}
		
		public String toString() {
			return getName()+"("+suite.getName()+")";
		}

		public void _testAttributeMappings() throws SdaiException {
			AGeneric_attribute_mapping attributeMappings = (AGeneric_attribute_mapping)
				context.mappingDomain.getInstances(EGeneric_attribute_mapping.class);
			SdaiIterator attrMappingIter = attributeMappings.createIterator();
			int countOfTypes = 0;
			while(attrMappingIter.next()) {
				EGeneric_attribute_mapping attrMapping =
					attributeMappings.getCurrentMember(attrMappingIter);
				if(attrMapping.testData_type(null) &&
// 				   (attrMapping.getData_type(null).getMemberCount() != 1)) {
				   (attrMapping.getData_type(null).getByIndex(1) instanceof EDefined_type)) {
					countOfTypes++;
				}
			}
			System.out.println("countOfTypes: " + countOfTypes);
		}

		public void _testTraverseMappings() throws SdaiException {
			getSession().setSdaiContext(context);
			System.gc();
			final MappingPopulationCreator briefPrintoutCreator = new MappingPopulationCreator() {
					public void createSourceInstance(EEntity_mapping type,
													 Collection targetInstances) throws SdaiException {
						EEntity_definition source = type.getSource(null);
						EEntity_definition target = (EEntity_definition)type.getTarget(null);
						System.out.println(source.getName(null) + " -> " + 
										   target.getName(null) +
										   "(" + type.getPersistentLabel() + ", " +
										   targetInstances.size() + "): ");
					}

					public void setSourceAttributeValues(EEntity_mapping type, EEntity targetInstance,
														 Map sourceValues) throws SdaiException {
					}

					public Set preprocessTargetInstances(EEntity_mapping type) throws SdaiException {
						return null;
					}
				};

			final MappingPopulationCreator verbosePrintoutCreator = new MappingPopulationCreator() {
					public void createSourceInstance(EEntity_mapping type,
													 Collection targetInstances) throws SdaiException {
						EEntity_definition source = type.getSource(null);
						EEntity_definition target = (EEntity_definition)type.getTarget(null);
						System.out.println(source.getName(null) + " -> " + 
										   target.getName(null) +
										   "(" + type.getPersistentLabel() + ", " +
										   targetInstances.size() + "): ");
						int num = 0;
						for(Iterator i = targetInstances.iterator(); i.hasNext(); ) {
							EEntity aimInstance = (EEntity)i.next();
							if(num % 16 == 0) {
								if(num > 0) {
									System.out.println();
								}
								//System.out.print("    ");
							} else {
								System.out.print(" ");
							}
							
							System.out.print(aimInstance.getPersistentLabel());
							num++;
						}
						if(num > 0) {
							System.out.println();
						}
						System.out.println();
					}

					public void setSourceAttributeValues(EEntity_mapping type, EEntity targetInstance,
														 Map sourceValues) throws SdaiException {
						System.out.println("\t" + targetInstance.getPersistentLabel() + ":");
						for(Iterator i = sourceValues.entrySet().iterator(); i.hasNext(); ) {
							Map.Entry entry = (Map.Entry)i.next();
							EGeneric_attribute_mapping attributeMapping =
								(EGeneric_attribute_mapping)entry.getKey();
							Object attributeValue = entry.getValue();
							System.out.println("\t\t" +
											   attributeMapping.getSource(null).getName(null) +
											   "(" + attributeMapping.getPersistentLabel() + "): " +
											   attributeValue + 
											   (attributeValue instanceof EEntity ?
												"" : "(" + attributeValue.getClass() + ")"));
						}
					}

					public Set preprocessTargetInstances(EEntity_mapping type) throws SdaiException {
						return null;
					}
				};


			long startTime = System.currentTimeMillis();
// 			MappingContext mappingContext = new MappingContext(context);
// 			MappingContext mappingContext = new MappingContext(context, briefPrintoutCreator);
			MappingContext mappingContext = new MappingContext(context, verbosePrintoutCreator);
			if(true) {
				SdaiSession.convertMapping(mappingContext);
			} else {
				AEntity_mapping entityMappings = (AEntity_mapping)
					context.mappingDomain.getInstances(EEntity_mapping.class);
				MappingConstraintMatcher.MatcherInstances allInstances = mappingContext
					.newAllInstances(MappingConstraintMatcher.MatcherInstances.STATUS_FORWARD);
				SdaiIterator entityMappingIter = entityMappings.createIterator();
				while(entityMappingIter.next()) {
					EEntity_mapping entityMapping = entityMappings.getCurrentMember(entityMappingIter);
// 					if(!entityMapping.getPersistentLabel().equals("#4503599627384521")) continue;
// 					if(entityMapping.getPersistentLabel().equals("#4222124650676526")) {
// 					System.out.println("Processing mapping: " + entityMapping);
					MappingConstraintMatcher.MatcherInstances armInstances =
						((MappingConstraintMatcher)entityMapping)
						.findForward(mappingContext, allInstances, false);
					Map armInstanceMap = armInstances.getInstanceMap();
					if(!armInstanceMap.isEmpty()) {
						System.out.print(entityMapping.getPersistentLabel() + "= ");
						System.out.println(entityMapping.getSource(null).getName(null) + " -> " +
										   ((EEntity_definition)entityMapping.getTarget(null))
										   .getName(null) + ": " + armInstanceMap.size());
					}
// 					} else {
// 						MappingConstraintMatcher.MatcherInstances armInstances =
// 							((MappingConstraintMatcher)entityMapping)
// 							.findForward(mappingContext, allInstances);
// 					}
				}
			}
			int instanceCount = model.getInstanceCount();
			long processingTime = System.currentTimeMillis() - startTime;
			System.out.println("testTraverseMappings time: " + processingTime +
							   "\ninstance count: " + instanceCount + "\nprocessing speed: " + 
							   (instanceCount / (processingTime / 1000.0)));
		}

		public void testCreateXim() throws SdaiException {
			getSession().setSdaiContext(context);

			long startTime = System.currentTimeMillis();
			SubstituteInstanceCreator substituteInstanceCreator = new SubstituteInstanceCreator(context);
			SdaiSession.convertMapping(substituteInstanceCreator.mappingContext);
			int instanceCount = model.getInstanceCount();
			long processingTime = System.currentTimeMillis() - startTime;
			System.out.println("testTraverseMappings time: " + processingTime +
							   "\ninstance count: " + instanceCount + "\nprocessing speed: " + 
							   (instanceCount / (processingTime / 1000.0)) +
							   "\ntotalSourceInstances: " + substituteInstanceCreator.totalSourceInstances + 
							   " totalAttributeValueInstances: " + substituteInstanceCreator.totalAttributeValueInstances);
			commitTransaction();
		}

	}

	private static class SubstituteInstanceCreator extends JsdaiLangAccessor implements MappingPopulationCreator {
		private MappingContext mappingContext;
		private Set unsuccessfullInstanceSet = new HashSet();
		private Collection deferredInstances = new ArrayList();

		// To store previous attribute mapping for debugging
		private EGeneric_attribute_mapping prevMapping = null;
		int totalSourceInstances = 0;
		int totalAttributeValueInstances = 0;
		int attributeValuesCatchCount = 0;

		private SubstituteInstanceCreator(SdaiContext context) throws SdaiException {
			mappingContext = new MappingContext(context, this);
			mappingContext.setInterleavedCreation(false);
		}

		public void createSourceInstance(EEntity_mapping type,
										 Collection targetInstances) throws SdaiException {
			EEntity_definition source = type.getSource(null);
			EEntity_definition target = (EEntity_definition)type.getTarget(null);
			totalSourceInstances += targetInstances.size();
			int num = 0;
			for(Iterator i = targetInstances.iterator(); i.hasNext(); ) {
				EEntity targetInstance = (EEntity)i.next();
				try {
					CMappedXIMEntity.buildMappedInstance(mappingContext, targetInstance, type, deferredInstances);
				} catch(SdaiException e) {
					String eString = e.getMessage(); //toString();

					if(num == 0) {
						System.out.println(source.getName(null) + " -> " + 
										   target.getName(null) +
										   "(" + type.getPersistentLabel() + ", " +
										   targetInstances.size() + "): ");
					}
					if(num % 16 == 0) {
						if(num > 0) {
							System.out.println();
						}
						System.out.print("    ");
					} else {
						System.out.print(" ");
					}
					System.out.print("#" + ((CEntity)targetInstance).instance_identifier);
					num++;
					if(eString.indexOf("substitute is not a subtype of the entity") < 0) {
						System.out.print(" " + eString);
// 						e.printStackTrace(System.out);
						num = 16;
					} else {
						System.out.print("!");
 						e.printStackTrace(System.out);
					}
// 					System.out.println();
// 					System.out.println("    " + e.toString());

					List instanceMappingPair = new ArrayList(2);
					instanceMappingPair.add(targetInstance);
					instanceMappingPair.add(type);
					unsuccessfullInstanceSet.add(instanceMappingPair);
				}
			}
			if(num > 0) {
				System.out.println();
			}
		}

		public void setSourceAttributeValues(EEntity_mapping type, EEntity targetInstance,
											 Map sourceValues) throws SdaiException {
			if(deferredInstances != null) {
				CMappedXIMEntity.buildDeferredMappedInstances(mappingContext, deferredInstances);
				deferredInstances = null;
			}
			totalAttributeValueInstances++;
			EEntity ximInstance = null;
			EEntity_definition ximType = type.getSource(null);
			Set allowedAttributes = new HashSet();
			for(Iterator i = sourceValues.entrySet().iterator(); i.hasNext(); ) {
				Map.Entry entry = (Map.Entry)i.next();
				EGeneric_attribute_mapping attributeMapping =
					(EGeneric_attribute_mapping)entry.getKey();
				if(ximInstance == null) {
					ximInstance = mappingContext.getMappedInstance(targetInstance);
					if(ximInstance == null && targetInstance.isKindOf(ximType)) {
						ximInstance = targetInstance;
					}
					if(ximInstance == null) {
						//System.out.println("ximInstance not found for " + targetInstance);
						return;
					}
					
					List instanceMappingPair = new ArrayList(2);
					instanceMappingPair.add(targetInstance);
					instanceMappingPair.add(type);
					if(unsuccessfullInstanceSet.contains(instanceMappingPair)) {
						return;
					}

					EEntity_definition concreteXimType = ximInstance.getInstanceType();
					EExplicit_attribute[] explicitAttributes = getEntityExplicitAttributes(concreteXimType);
					Field[] explicitAttributeFields = getEntityAttributeFields(concreteXimType);
					for (int j = 0; j < explicitAttributes.length; j++) {
						if(explicitAttributeFields[j] != null) {
							allowedAttributes.add(explicitAttributes[j]);
						}
					}
				}
				Object attrValue = entry.getValue();
				EAttribute attribute = attributeMapping.getSource(null);
				if(attribute instanceof EExplicit_attribute) {
					EExplicit_attribute baseAttribute = (EExplicit_attribute) attribute;
					while(baseAttribute.testRedeclaring(null)) {
						baseAttribute = baseAttribute.getRedeclaring(null);
					}
					boolean process = allowedAttributes.contains(baseAttribute);
// 					boolean process = true;
// 					if(attributeMapping instanceof EAttribute_mapping) {
// 						EAttribute_mapping attrAttrMapping =
// 							(EAttribute_mapping)attributeMapping;
// 						if(attrAttrMapping.testDomain(null) &&
// 						   attrAttrMapping.getDomain(null) instanceof EEntity_definition) {
// 							if(prevMapping != attributeMapping) {
// 								System.out.println("Can not handle attribute: " + attribute +
// 												   " in mapping: " + attributeMapping);
// 								prevMapping = attributeMapping;
// 							}
// 							process = false;
// 						}
// 					}
					if(process) {
						EExplicit_attribute explicitAttribute = (EExplicit_attribute)attribute;
						while(explicitAttribute.testRedeclaring(null)) {
							explicitAttribute = explicitAttribute.getRedeclaring(null);
						}
						try {
							CMappedXIMEntity.assignMappedValue(ximInstance, mappingContext, explicitAttribute, attrValue,
															   (attributeMapping.testData_type(null) ? 
																attributeMapping.getData_type(null) :
																(ANamed_type)null));
						} catch(SdaiException e) {
// 							System.out.print("!");
// 							System.out.println();
							String eString = e.getMessage();
							if(eString.startsWith("SY_ERR - Underlying system error. ")) {
								eString = eString.substring("SY_ERR - Underlying system error. ".length());
							}
// 							if(eString.indexOf("is not of type") < 0*/ /*&& eString.indexOf("Attribute invalid") < 0*/) {
// 							}
							if(eString.indexOf("Can not find mapped instance") >= 0) {
								eString = eString.substring(0, eString.indexOf('='));
							}
							attributeValuesCatchCount++;
							if(attributeValuesCatchCount > -1) {
								System.out.println(" " + "#" + ((CEntity)targetInstance).instance_identifier + "=" +
												   ximInstance.getInstanceType().getName(null) + " " + attribute + " " + eString);
							} else {
								System.out.print(" " + attrValue + " #" + ((CEntity)targetInstance).instance_identifier + "=" +
												 ximInstance.getInstanceType().getName(null) + " " + attribute + " ");
								e.printStackTrace(System.out);
							}
						}
					}
				} else {
					if(prevMapping != attributeMapping) {
						System.out.println("Unsupported attribute: " + attribute);
						prevMapping = attributeMapping;
					}
				}
			}
		}

		public Set preprocessTargetInstances(EEntity_mapping type) throws SdaiException {
			return null;
		}
	}
	
} // Mim2XimTest

/*
Local Variables:
compile-command: "ant -emacs -find build-xim-full.xml -Dtest.name=jsdai.lang.Mim2XimTest test.stepmod"
End:
*/
