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

import java.util.Hashtable;
//import jsdai.lang.*;
import jsdai.dictionary.*;
import jsdai.mapping.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
*	This class is a superclass of all mapping early binding
*	classes known as M classes.
*	<BR>Here is an example how derived classes can be used:
*	<BR><pre>
* import jsdai.MAp214.*;
* import jsdai.lang.*;
* import jsdai.dictionary.*;
* import jsdai.mapping.*;
*	
* public static final void main (String args[])  throws SdaiException, Exception {
*		System.out.println("Test application of Item");
*		//Session is opened already by initializing static members.
*		SdaiSession session = SdaiSession.getSession();
*		//Transaction is already started by initializing static members.
*		//SdaiTransaction trx = session.startTransactionReadWriteAccess();
*		SdaiRepository repo = session.linkRepository(args[0],null);
*		repo.openRepository();
*		ASdaiModel domain = repo.getModels();
*		SdaiModel model = domain.getByIndex(1);
*
*		model.startReadWriteAccess();
*
*		AEntity instances = MItem.findInstances(domain, null);
*
*		int no_of_instances = instances.getMemberCount();
*		System.out.println("Number of received instances is "+no_of_instances);
*		System.out.println(instances);
*		int expectedRetType = -1;
*		for (int i=1;i<=no_of_instances;i++) {
*			EEntity instance = (EEntity) instances.getByIndexEntity(i);
*			System.out.println("");
*			System.out.println("instance is "+instance);
*			expectedRetType = MItem.testId(domain, instance);
*			switch (expectedRetType) {
*				case MItem.ID__STRING:
*					//put appropriate call to getXX method here.
*				break;
*			default:
*				System.out.println("Value not set!");
*			}
*			expectedRetType = MItem.testName(domain, instance);
*			switch (expectedRetType) {
*				case MItem.NAME__STRING:
*					//put appropriate call to getXX method here.
*					String result = MItem.getName(domain, instance, (String) null);
*					System.out.println("result for name is"+result);
*					if (result.equalsIgnoreCase("nba")) {
*						result = "LKL";
*						System.out.println("setting new value for name");
*						MItem.setName(domain, instance, result, (String) null);
*						session.getActiveTransaction().commit();
*					}
*				break;
*			default:
*				System.out.println("Value not set!");
*			}
*			expectedRetType = MItem.testDescription(domain, instance);
*			switch (expectedRetType) {
*				case MItem.DESCRIPTION__STRING:
*					//put appropriate call to getXX method here.
*				break;
*			default:
*				System.out.println("Value not set!");
*			}
*		}
*	}
*
*</pre>
*/
public class MappingUtil {

	protected MappingUtil() { }
	
	/**
	*	The error string to be used to inform user about attempt to use setXX method
	*	improperly. Original string says: "Attempt to determine which attribute_mapping
	*	to use failed: type of given instance should not be used with this overload of 
	*	setXX method."
	*/
	public static final String setErrorString1 = "Attempt to determine which attribute_mapping"+
												" to use failed: type of given instance should"+
												" not be used with this overload of setXX method.";
	/**
	*	The error string to be used to construct SdaiException, when early binding
	*	class detects that type of actually returned value is unexpected and 
	*	there is no static constant to be returned from testXX method.
	*	Original string says: "Attempt to determine  type of returned value failed:
	*	you may need to regenerate your classes with jsdai.mappingUtils.ClassesGenerator."
	*/
	public static final String testErrorString1 = "Attempt to determine  type of returned"+
						     " value failed: you may need to regenerate your classes with"+
							 " jsdai.mappingUtils.ClassesGenerator.";
	/**
	*	This error string is used to create and throw SdaiException and indicates
	*	that user called a getXX method that accepts EEntity instance as parameter,
	*	but user has passed to this method an instance, which was of incorrect type.
	*	'Incorrect' type means that attribute value can not be read from instances
	*	of 'incorrect' type. Or, attribute value can be read, but its type will be
	*	incompatible with return type of getXX method.
	*	Original string says: "Type of given instance is not compatible with required
	*	types!"
	*	If desired, a processor of this exception can invoke getErrorBase() : Object
	*	to obtain the instance, on which this 'get' operation has failed.
	*	The output of getErrorString1 is followed by invalid value itself, thus giving
	*	the processor full information on all circumstances, under which the exception
	*	has occurred.
	*/							 
	public static final String getErrorString1 = "Type of given instance is not "+
							"compatible with required types!";
	
	/**
	*	Current mapping mode to be used throughout all derived classes in all
	*	getXX and testXX methods. Default value is EEntity.NO_RESTRICTIONS.
	*/						 
	protected static	int	currentMappingMode = EEntity.NO_RESTRICTIONS;
	
	/**
	*	Value used to return from testXX methods to indicate:
	*	a) attribute is not set
	*	b) call to getXX would result in SdaiException.
	*/
	public	static	final int VA_NSET = 0;

	protected static ArrayList buildArrayList(Object value) throws SdaiException {
		if (value instanceof ArrayList)
			return (ArrayList) value;
		else {
			ArrayList retValue = new ArrayList();
			retValue.add(value);
			return retValue;
		}
	}

	/**
	*
	*
	*/	
	protected static AEntity buildAggregate(Object value, Class aggregateType) throws SdaiException {
		AEntity retValue = null;
		try {
			retValue = (AEntity) aggregateType.newInstance();
		}
		catch (InstantiationException e) {
			throw new SdaiException(SdaiException.SY_ERR, e);
		}
		catch (IllegalAccessException e) {
			throw new SdaiException(SdaiException.SY_ERR, e);
		}
		if (value instanceof ArrayList) {
			ArrayList aList = (ArrayList) value;
			int size = aList.size();
			for (int i=0;i<size;i++) {
				retValue.addUnordered((EEntity)aList.get(i));
			}
			return retValue;
		}
		if (value instanceof Aggregate) {
			Aggregate ag = (Aggregate) value;
			SdaiIterator it = ag.createIterator();
			while (it.next()) {
				EEntity agValue = (EEntity) ag.getCurrentMemberObject(it);
				retValue.addUnordered(agValue);
			}
			return retValue;
		}
		else {
			retValue.addUnordered((EEntity)value);
		}
		return retValue;
	}

	/**
	*	@param parseAggregate	This parameter is used in cases, when given
	*									value for evaluation contains aggregate.
	*									If parseAggregate is set to true, method
	*									will attempt to go inside of aggregate.
	*									Otherwise it will not.
	*/
	protected static boolean compatibleObjectValue(Object value,
									Class conformanceClass, boolean parseAggregate)
																throws SdaiException {
		if (parseAggregate) {
			if (value instanceof ArrayList) {
				ArrayList aValue = (ArrayList) value;
				int valueSize = aValue.size();
				for (int j=0;j<valueSize;j++) {
					Object itValue = aValue.get(j);
					if (!conformanceClass.isInstance(itValue)) {
						return false;
					}	
				}
				return true;
			}
		}
		if (parseAggregate) {
			if (value instanceof Aggregate) {
				Aggregate ag = (Aggregate) value;
				SdaiIterator it = ag.createIterator();
				while (it.next()) {
					EEntity agValue = (EEntity) ag.getCurrentMemberObject(it);
					if (!conformanceClass.isInstance(agValue)) {
						return false;
					}
				}
				return true;
			}
		}
		
		if (!conformanceClass.isAssignableFrom(value.getClass())) {
				return false;
		}
		return true;
	}

	/**
	*	
	*
	*/
	protected static boolean compatibleObjectValue(Object value, Class conformanceClass)
																throws SdaiException {
		if (value instanceof ArrayList) {
			ArrayList aValue = (ArrayList) value;
			int valueSize = aValue.size();
			for (int j=0;j<valueSize;j++) {
				Object itValue = aValue.get(j);
				if (!conformanceClass.isInstance(itValue)) {
					return false;
				}
			}
			return true;
		}
		if (value instanceof Aggregate) {
			Aggregate ag = (Aggregate) value;
			SdaiIterator it = ag.createIterator();
			while (it.next()) {
				EEntity agValue = (EEntity) ag.getCurrentMemberObject(it);
				if (!conformanceClass.isInstance(agValue)) {
					return false;
				}
			}
			return true;
		}
		
		if (!conformanceClass.isAssignableFrom(value.getClass())) {
				return false;
		}
		return true;
	}

	protected static Object getAllValuesForAttribute(ASdaiModel domain, ASdaiModel metaDomain,
													EExplicit_attribute atr, EEntity instance)
													 throws SdaiException {
		if (instance == null)
			return null;
		Object[] result = instance.getMappedAttribute(atr, domain, metaDomain, currentMappingMode);
		
		// no values:
		if (result.length == 0)
			return null;
		// one value:
		if (result.length == 1)
			return result[0];
		// more than one value. convert to arraylist.
		ArrayList list = new ArrayList();
		for (int i=0;i<result.length;i++) {
			list.add(result[i]);
		}
		return list;
	}

	/**
	*
	*/
	protected static Object getAnyValueForAttribute(ASdaiModel domain, ASdaiModel metaDomain,
													EExplicit_attribute atr, EEntity instance)
													 throws SdaiException {
		Object o = null;
		if (instance == null)
			return o; // same as null
		AGeneric_attribute_mapping result = new AGeneric_attribute_mapping();
		
		CGeneric_attribute_mapping.usedinSource(null, atr, metaDomain, result) ;
		SdaiIterator it = result.createIterator();
		while (it.next()) {
			EGeneric_attribute_mapping atrMap = (EGeneric_attribute_mapping) result.getCurrentMemberObject(it);
			o = instance.getMappedAttribute(atrMap, domain, metaDomain, currentMappingMode);
			if (o != null) {
				break;
			}
		}
		return o;
	}

	/**
	*	Method is used to find out whether attribute value is set. If there is any
	*	attribute value set, then the value of attribute is returned and the data_type
	* 	property of attribute_mapping is returned as well. Combination of these two
	*	allows to identify which static constant should be returned from testXX method.
	*
	*	@param	atr						The arm attribute to be tested.
	*	@param	linkedAttrMappingName	The array of String. Its length is 1(one). Array
	*									is used to return value of data_type property of
	*									attribute_mapping, that was used to read return
	*									value of this method.
	*	@return Object					The value of attribute, if attribute is not set,
	*									null is returned.
	*/
	protected static Object getAnyValueForAttribute(ASdaiModel domain, ASdaiModel metaDomain,
													EExplicit_attribute atr, EEntity instance,
													String linkedAttrMappingName[]) throws SdaiException {
		Object o = null;
		if (instance == null)
			return o; // same as null
		AGeneric_attribute_mapping result = new AGeneric_attribute_mapping();
		
		CGeneric_attribute_mapping.usedinSource(null, atr, metaDomain, result) ;
		SdaiIterator it = result.createIterator();
		while (it.next()) {
			EGeneric_attribute_mapping atrMap = (EGeneric_attribute_mapping) result.getCurrentMemberObject(it);
			o = instance.getMappedAttribute(atrMap, domain, metaDomain, currentMappingMode);

			if (o != null) {
				linkedAttrMappingName[0] = "";
				if (atrMap.testData_type(null)) { 
					ANamed_type namedTypes = atrMap.getData_type(null);
					SdaiIterator mIt = namedTypes.createIterator();
					// take only first value from this list.
					if (mIt.next()) {
						ENamed_type namedType = (ENamed_type) namedTypes.getCurrentMemberObject(mIt);
						linkedAttrMappingName[0] = namedType.getName(null);
					}
				}
				break;
			}
		}
		return o;
	}
	
	/**
	*	Method allows to change value of protected static member, which is taken
	*	when derived classes needs to access mapping operation, and mapping mode
	*	(or severity level) is required for passing to mapping operation.
	*
	*	@param newMappingMode	new mapping mode, can be one of values, defined in
	*							EEntity class (NO_RESTRICTIONS, etc.)
	*/							
	public static void changeMappingModeTo(int newMappingMode) {
		currentMappingMode = newMappingMode;
	}
	
	public static SdaiModel findSystemModel(String name) {
		try {
			if (SdaiSession.getSession() == null) {
				SdaiSession.openSession();
				SdaiSession.getSession().startTransactionReadWriteAccess();
			}
			ASdaiModel models = SdaiSession.getSession().getDataDictionary().getRepository().getModels();
			SdaiIterator iterator = models.createIterator();
			while (iterator.next()) {
				SdaiModel model = models.getCurrentMember(iterator);
				if (model.getName().equals(name)) {
				    if (model.getMode() == SdaiModel.NO_ACCESS) {
         			model.startReadOnlyAccess();
      			 }
					return model;
				}
			}
			return null;
		} catch (Exception e) {
			SdaiSession.printStackTraceToLogWriter(e);
			return null;
		}
	}
	

	public static ESchema_mapping findSchemaMapping(SdaiModel modelMAP) {
		try {
			ASchema_mapping mappings = (ASchema_mapping) modelMAP.getInstances(ESchema_mapping.class);
			return mappings.getByIndex(1);
		} catch (Exception e) {
			SdaiSession.printStackTraceToLogWriter(e);
			return null;
		}
	}
	
	public static ASdaiModel findMetaDomain(SdaiModel mp) {
		try {
			return mp.getRepository().getModels();
		} catch (Exception e) {
			SdaiSession.printStackTraceToLogWriter(e);
			return null;
		}
	}
	
	public static ESchema_definition getSource(ESchema_mapping sm) {
		try {
			return sm.getSource(null);
		} catch (Exception e) {
			SdaiSession.printStackTraceToLogWriter(e);
			return null;
		}
	}
	
	public static ESchema_definition getTarget(ESchema_mapping sm) {
		try {
			return sm.getTarget(null);
		} catch (Exception e) {
			SdaiSession.printStackTraceToLogWriter(e);
			return null;
		}
	}
	
	public static ANamed_type getNamedTypes(ESchema_definition schema, ASdaiModel metaDomain) {
		try {
			ANamed_type types = new ANamed_type();
			ADeclaration declarations = new ADeclaration();
			CDeclaration.usedinParent_schema(null, schema, SdaiSession.getSession().getActiveModels(), declarations);
			SdaiIterator i = declarations.createIterator();
			while (i.next()) {
				EDeclaration declaration = declarations.getCurrentMember(i);
				EEntity e = declaration.getDefinition(null);
				if (e instanceof ENamed_type) {
					types.addUnordered((ENamed_type) e, null);
				}
			}
			return types;
		} catch (Exception e) {
			SdaiSession.printStackTraceToLogWriter(e);
			return null;
		}
	}
	
	public static ENamed_type findType(String name, ANamed_type types) {
		try {
			SdaiIterator i = types.createIterator();
			while (i.next()) {
				ENamed_type type = types.getCurrentMember(i);
				if (type.getName(null).equals(name)) {
					return type;
				}
			}
			return null;
		} catch (Exception e) {
			SdaiSession.printStackTraceToLogWriter(e);
			return null;
		}
	}

	protected static boolean isCompatible(Class entityType, EEntity target,
							Hashtable entities2Class) {
		if (entities2Class.containsKey(target)) {
			Class type = (Class) entities2Class.get(target);
			if (entityType.isInterface()) {
				Class[] interfaces = type.getInterfaces();
				for (int i=0;i<interfaces.length;i++) {
					Class iFace = interfaces[i];
					if (!entityType.isAssignableFrom(iFace))
						return false;
				}
				return true;
			}			
			else {
				Class[] superTypes = entityType.getInterfaces();
				for (int j=0;j<superTypes.length;j++) {
					if (!superTypes[j].isAssignableFrom(type))
							return false;
				}
				return true;
			}
		}
		return false;
	}

	protected static Aggregate findEntityMappings(EEntity_definition mappedEntity,
							ASdaiModel mappingDomain, Hashtable entities2Class) {
		AEntity_mapping agMappings = new AEntity_mapping();								
		try {
			CEntity_mapping.usedinSource(null, mappedEntity, mappingDomain, agMappings);
			SdaiIterator it = agMappings.createIterator();
			while (it.next()) {
				EEntity_mapping mapping = agMappings.getCurrentMember(it);
				EEntity target = mapping.getTarget(null);
				if (target instanceof EEntity_definition) {
					EEntity_definition eDef = (EEntity_definition) target;
					SdaiModel model = eDef.findEntityInstanceSdaiModel();
					String modelDesc = model.getName();
					int index = modelDesc.indexOf("_DICTIONARY_DATA");
					modelDesc = modelDesc.substring(0, index);
					modelDesc = modelDesc.toLowerCase();
					modelDesc = "jsdai.S" + capitalize(modelDesc);
					String fullName = modelDesc + ".C" + makeValidName(capitalize(eDef.getName(null)));
					Class exactClass =
						Class.forName(fullName, true, SdaiClassLoaderProvider.getDefault().getClassLoader());
					entities2Class.put(target, exactClass);
				}
			}
		} catch (Exception e) {
			SdaiSession.printStackTraceToLogWriter(e);
		}
		return agMappings;			
	}

	private static String capitalize(String name) {
		StringBuffer nameBuffer = new StringBuffer(name);
		nameBuffer.setCharAt(0, nameBuffer.substring(0, 1).toUpperCase().charAt(0));
		String retName = nameBuffer.toString();
		return retName;
	}

	private static String makeValidName(String name) {
		String retValue = new String(name);
		retValue = retValue.replace('+','$');
		return retValue;
	}

	public static EEntity_mapping findEntityMapping(EEntity_definition mappedEntity, String aimName, ASdaiModel mappingDomain) {
		try {
			AEntity_mapping aema = new AEntity_mapping();
			CEntity_mapping.usedinSource(null, mappedEntity, mappingDomain, aema);
			SdaiIterator i = aema.createIterator();
			while(i.next()) {
				EEntity_mapping em = aema.getCurrentMember(i);
				EEntity_definition ed = (EEntity_definition) em.getTarget(null);
				if (ed.getName(null).equals(aimName)) {
					return em;
				}
			}
			return null;
		} catch (Exception e) {
			SdaiSession.printStackTraceToLogWriter(e);
			return null;
		}
	}
	
	public static EAttribute findArmAttribute(EEntity_definition entity, String attributeName) {
		try {
			AAttribute attributes = new AAttribute();
			CAttribute.usedinParent_entity(null, entity, null, attributes);
			SdaiIterator i = attributes.createIterator();
			while(i.next()) {
				EAttribute att = (EAttribute) attributes.getCurrentMember(i);
				if (att.getName(null).equals(attributeName)) {
					return att;
				}
			}
			
			return null;
		} catch (Exception e) {
			SdaiSession.printStackTraceToLogWriter(e);
			return null;
		}
	}

	protected static Aggregate findAttrMappings(Aggregate entMappings,
									EExplicit_attribute attr, ASdaiModel mappingDomain) {
		AGeneric_attribute_mapping validated = new AGeneric_attribute_mapping();
		try {
			AGeneric_attribute_mapping candidates = new AGeneric_attribute_mapping();	
			CGeneric_attribute_mapping.usedinSource(null, attr, mappingDomain, candidates);
				
			SdaiIterator it= candidates.createIterator();
			while (it.next()) {
				EGeneric_attribute_mapping atrMapping = candidates.getCurrentMember(it);
				EEntity_mapping entMapping = atrMapping.getParent_entity(null);
				if (!acceptableMapping(entMapping, entMappings))
					continue;
				validated.addUnordered(atrMapping);
			} 
		}
		catch (Exception e) {
			SdaiSession.printStackTraceToLogWriter(e);
		}
		return validated;
	}

	/**
	*	As generic_attribute_mapping may lead to value, not necessarily to
	*	another entity instance, a method giving only those attribute mappings which
	*	leads to entity instances, is required.
	*/
	protected static AAttribute_mapping findAttrMappingsAsRef(Aggregate entMappings,
									EExplicit_attribute attr, ASdaiModel mappingDomain) {
		AAttribute_mapping validated = new AAttribute_mapping();
		try {
			AAttribute_mapping candidates = new AAttribute_mapping();	
			CAttribute_mapping.usedinSource(null, attr, mappingDomain, candidates);
				
			SdaiIterator it= candidates.createIterator();
			while (it.next()) {
				// because source attribute is in supertype, not only attribute_mappings
				// are returned!
				EGeneric_attribute_mapping atrMapping = candidates.getCurrentMember(it);
				if (!(atrMapping instanceof EAttribute_mapping))
					continue;
				EEntity_mapping entMapping = atrMapping.getParent_entity(null);
				if (!acceptableMapping(entMapping, entMappings))
					continue;
				
				validated.addUnordered((EAttribute_mapping) atrMapping);
			} 
		}
		catch (Exception e) {
			SdaiSession.printStackTraceToLogWriter(e);
		}
		return validated;
	}
	
	
	private static boolean acceptableMapping(EEntity_mapping entMap,
								Aggregate validMappings) throws SdaiException {
		SdaiIterator it = validMappings.createIterator();
		while (it.next()) {
			EEntity_mapping mapping = (EEntity_mapping) validMappings.getCurrentMemberObject(it);
			if (mapping == entMap) 
				return true;
		}
		return false;
	}

	/**
	*	@param	em				Entity mapping, that is related with desired to find 
	*							attribute mapping.
	*	@param	attribute 		Attribute, whose mapping is searched.
	*	@param	dataTypeName	In some cases entity_mapping can have several
	*							alternatives for mapping given attribute. Then,
	*							each attribute_mapping contains a string value that
	*							allows to distinguish attribute_mappings between
	*							each other. This parameter indicates what string
	*							value is contained in desired attribute_mapping.
	*	@param	mappingDomain	Model(s), where desired attribute_mapping should be
	*							searched.
	*/
	public static EGeneric_attribute_mapping findAttributeMapping(EEntity_mapping em,
										EAttribute attribute,
										String dataTypeName,
										ASdaiModel mappingDomain) {
		try {
			AGeneric_attribute_mapping agam = new AGeneric_attribute_mapping();
			CGeneric_attribute_mapping.usedinParent_entity(null, em, mappingDomain, agam);
			SdaiIterator i = agam.createIterator();
			while(i.next()) {
				EGeneric_attribute_mapping gam = agam.getCurrentMember(i);
				if (gam.getSource(null)==attribute) {
					// now check whether this mapping is the one we desired:
					if (MappingUtil.getDataTypeName(gam).equalsIgnoreCase(dataTypeName)) {
						return gam;
					}
				}
			}
		} catch (Exception e) {
			SdaiSession.printStackTraceToLogWriter(e);
			return null;
		}
		return null;
	}

	protected static AEntity findMappingInstances(EEntity_mapping entityMapping, 
		ASdaiModel targetDomain, ASdaiModel mappingDomain, AEntity targetAgg, 
		int mode) throws SdaiException {
		return targetAgg.findMappingInstances(entityMapping,
							targetDomain, mappingDomain, mode);
	}
										
	protected static AEntity findMappingInstances(EEntity_mapping entityMapping, 
		ASdaiModel targetDomain, ASdaiModel mappingDomain, int mode) throws SdaiException {
		return targetDomain.findMappingInstances(entityMapping,
                             targetDomain, mappingDomain, mode);
	}
	
	private static String getDataTypeName(EGeneric_attribute_mapping atrMapping) throws SdaiException, Exception {
		String dataTypesName = "";
		if (atrMapping.testData_type(null)) {
			ANamed_type namedTypes = atrMapping.getData_type(null);
			SdaiIterator it = namedTypes.createIterator();
			if (it.next()) {
				ENamed_type namedType = (ENamed_type) namedTypes.getCurrentMemberObject(it);
				dataTypesName = namedType.getName(null);
				// check if there are several named_types set:
				if (namedTypes.getMemberCount() > 1)
					throw new Exception ("Can not select desired data type name: "+
								"several named data types in data_types list detected!");
			}
		}
		return dataTypesName;
	}

	/**
	*	A helper method for findInstances method in generated classes.
	*	Is used to copy instances from one aggregate to another. Ensures, that
	*	resulting aggregate does not contain repeated entries.
	*	Notice, that we switched over to using HashSet, as this appeared to be
	*	many times faster than using isMember() function- probably due to
	*	bug in that function. See bugzilla #631 for report on this.
	*/
	public static void copyEntities(AEntity to, AEntity from) throws SdaiException {
		//long t0 = System.currentTimeMillis();
		HashSet filter = new HashSet();
		// first add to filter target aggregate
		SdaiIterator it = to.createIterator();
		while (it.next()) {
			filter.add(to.getCurrentMemberObject(it));
		}
		// now do the same for source aggregate.
		it = from.createIterator();
		while (it.next()) {
			filter.add(from.getCurrentMemberObject(it));
		}
		// now cleanup the target aggregate and add everything from scratch from filter:
		to.clear();
		Iterator sIt = filter.iterator();
		while (sIt.hasNext()) {
			to.addUnordered((EEntity) sIt.next());
		}
		//long t1 = System.currentTimeMillis();
		//System.out.println("duration: "+(t1-t0));
		/*
		SdaiIterator it = from.createIterator();
		while (it.next()) {
			EEntity ent = (EEntity) from.getCurrentMemberObject(it);
			if (!to.isMember(ent))
				to.addUnordered(ent);
			// otherwise simply skip this entry.
		}
		*/
	}


}
