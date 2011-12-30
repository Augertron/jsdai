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

import jsdai.dictionary.*;
import jsdai.mapping.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import jsdai.query.SdaiModelRef;

/**
 * CEntity is a base class for all application and dictionary entity classes.
 * This class is for internal JSDAI use only.
 * Applications shall use EEntity instead.
 */
public abstract class CEntity extends InverseEntity implements EEntity, SdaiEventSource {
/**
	Identifier of the entity instance represented by this class.
*/
	protected long instance_identifier;

/**
	Model the entity instance represented by this class belongs to.
*/
	protected SdaiModel owning_model;
	// idea: to be replaced in future by
	// protected SdaiCommon owner;

/**
	The position(index) of the entity instance represented by this class in the
	corresponding row of the 'instances_sim' matrix in the owning model.
	This index is set and then written into the model's binary file during its
	creation when the instance is referenced by another instance within the
	same model. Afterwards, this information is efficiently used when references
	are resolved during loading of the binary file.
	Thus, the field is used only for a while, that is, when saving the data of
	the owning model to the binary file.
*/
	int instance_position;

/**
	The field left for user's disposition. An access to it is through
	<code>getTemp</code> and <code>setTemp</code> methods.
*/
//	private Map map = new HashMap();
	private Map map;

/**
	The special "default" key for get/setTemp methods without 'key' parameter
*/
	private static final Object SPECIAL_KEY = new Object();

/**
	An aggregate containing instances of <code>SdaiListener</code>.
*/
	private CAggregate listenrList;

	/**
     * @since 3.6.0
     */
	static final String WHERE_RULE_METHOD_NAME_PREFIX = "r";

	static final String ALL_WHERE_RULE_METHOD_NAME_PREFIX = "run";

	private static final int ROLES_ARRAY_SIZE = 16;

	private static final int TYPES_ARRAY_SIZE = 16;

	private static final int VALUE_ARRAY_SIZE = 8;

	private static final int ENTITY_TYPES_ARRAY_SIZE = 4;

	protected static final int FLG_MASK = 0xC0000000;
	protected static final int POS_MASK = 0x3FFFFFFF;
	protected static final int INS_MASK = 0x80000000;
	protected static final int UPD_MASK = 0x40000000;

//	static final boolean CATCH_EXCEPTIONS = false;
//	static boolean CATCH_EXCEPTIONS = true;
	static final boolean CATCH_EXCEPTIONS = true;

/**
	The constructor of this class. It is used indirectly in extensions of the
	class <code>SSuper</code> by the methods of the package <code>java.lang.reflect</code>.
	The statement "new CEntity()" in JSDAI lang package nowhere appears.
*/
	protected CEntity() {
	}



/*          Abstract methods       */

/**
	Returns a representation of values of the attributes of this entity instance.
	The values are represented using an instance of the class <code>ComplexEntityValue</code>.
	The method is implemented in JSDAI library classes.
*/
	protected abstract void getAll(ComplexEntityValue entity_values) throws SdaiException;


/**
	Assigns the values to the attributes of this entity instance according to their
	representation submitted to the method. The values are represented using an instance
	of the class <code>ComplexEntityValue</code>.
	The method is implemented in JSDAI library classes.
*/
	protected abstract void setAll(ComplexEntityValue entity_values) throws SdaiException;



/*              Special methods         */

/**
	Returns the model containing this entity instance.
*/
	SdaiCommon getOwner() {
		return owning_model;
	}


/**
	Notifies all listeners in the aggregate <code>listenrList</code> that
	this entity instance was modified.
	Also, sets the field 'modified' of the owning model with the value 'true'.
*/
	void modified() throws SdaiException {
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
		if (owning_model == null) {
			String base = SdaiSession.line_separator + AdditionalMessages.RD_INST + instance_identifier;
			throw new SdaiException(SdaiException.EI_NEXS, base);
		}
		MethodCallsCacheManager.clear(owning_model.repository.session);
		SdaiTransaction activeTransaction = owning_model.getRepository().getSession().active_transaction;
        if (activeTransaction != null &&
				activeTransaction.transactionStatus != SdaiTransaction.TRANSACTION_STATUS_REOPENING) {
			owning_model.modified = true;
			// Instance state tracking
			instance_position = CEntity.UPD_MASK | instance_position;
		}
	}
	void modified_sleeping() throws SdaiException {
		owning_model.modified_sleeping = true;
	}


/**
	Notifies all listeners in the aggregate <code>listenrList</code> that
	this entity instance was deleted.
	Also, sets the field 'modified' of the owning model with the value 'true'.
*/
	void deleted() throws SdaiException {
		fireSdaiEvent(SdaiEvent.INVALID, -1, null);
		owning_model.modified = true;
		owning_model.entityDeleted(this);
        //<--VV-- Instance state tracking
        if (owning_model.del_inst_threshold > 0) {
            if ((instance_position & CEntity.INS_MASK) == 0) {
                if (owning_model.n_del_inst_ids == owning_model.del_inst_ids.length) {
                    int new_length = owning_model.del_inst_ids.length * 2;
					if(new_length < SdaiModel.INSTANCE_ARRAY_INITIAL_SIZE) {
						new_length = SdaiModel.INSTANCE_ARRAY_INITIAL_SIZE;
					}
                    long [] new_array = new long[new_length];
                    System.arraycopy(owning_model.del_inst_ids, 0, new_array, 0, owning_model.del_inst_ids.length);
                    owning_model.del_inst_ids = new_array;
                }
                owning_model.del_inst_ids[owning_model.n_del_inst_ids++] = instance_identifier;   //--VV--
                owning_model.del_inst_threshold--;
                if (owning_model.del_inst_threshold == 0 && owning_model.n_del_inst_ids > 0) {
                    owning_model.n_del_inst_ids = 0;
                    owning_model.del_inst_ids = null;
                }
            }
        }
        //--VV--> Instance state tracking
	}

	void deletedWeak() throws SdaiException {
		owning_model.modified = true;
		//<--VV-- Instance state tracking
		if (owning_model.del_inst_threshold > 0) {
			if ((instance_position & CEntity.INS_MASK) == 0) {
				if (owning_model.n_del_inst_ids == owning_model.del_inst_ids.length) {
					int new_length = owning_model.del_inst_ids.length * 2;
					if(new_length < SdaiModel.INSTANCE_ARRAY_INITIAL_SIZE) {
						new_length = SdaiModel.INSTANCE_ARRAY_INITIAL_SIZE;
					}
					long [] new_array = new long[new_length];
					System.arraycopy(owning_model.del_inst_ids, 0, new_array, 0, owning_model.del_inst_ids.length);
					owning_model.del_inst_ids = new_array;
				}
				owning_model.del_inst_ids[owning_model.n_del_inst_ids++] = instance_identifier;   //--VV--
				owning_model.del_inst_threshold--;
				if (owning_model.del_inst_threshold == 0 && owning_model.n_del_inst_ids > 0) {
					owning_model.n_del_inst_ids = 0;
					owning_model.del_inst_ids = null;
				}
			}
		}
		//--VV--> Instance state tracking
	}

	void deletedObject() throws SdaiException {
		if(map != null) {
			CMappedARMEntity.removeArmInstances(this);
		}
	}

/*          SDAI operations        */

	/**
     * @since 3.6.0
     */
    public boolean isValid() {
		if (owning_model != null) {
			return true;
		}
		return false;
	}


	public AEntity get_inverse(EInverse_attribute attribute, ASdaiModel domain) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (attribute == null) {
			throw new SdaiException(SdaiException.AT_NDEF);
		}
		EAttribute saved_attr = attribute;
		while (attribute.testRedeclaring(null)) {
			attribute = attribute.getRedeclaring(null);
		}
		CEntityDefinition edef = (CEntityDefinition)getInstanceType();
		int length = 0;
		if (edef.attributesInverse != null) {
			length = edef.attributesInverse.length;
		}

		for (int i = 0; i < length; i++) {
			if (edef.attributesInverse[i] == attribute) {
				AEntity result;
				if (!attribute.testMin_cardinality(null)) {
					result = new AEntity();
				} else {
					AggregationType aggr_type = (AggregationType)((AttributeDefinition)attribute).getInverseAggregationType(this);
					try {
						result = (AEntity)aggr_type.getAggregateClass().newInstance();
					} catch (Exception ex) {
						throw new SdaiException(SdaiException.SY_ERR, ex);
					}
					result.attach(aggr_type, this);
				}

				CEntity_definition referencing_type = (CEntity_definition)attribute.getDomain(null);
				CExplicit_attribute role = (CExplicit_attribute)attribute.getInverted_attr(null);
				while (role.testRedeclaring(null)) {
					role = (CExplicit_attribute)role.getRedeclaring(null);
				}
				int k = 0;
				Object o = inverseList;
				CEntity inst;
				while (o != null) {
					if (o instanceof Inverse) {
						Inverse inv = (Inverse) o;
						inst = (CEntity)inv.value;
						k++;
						if ((k < 2 || (k > 1 && different(inst, k))) &&
								checkType(referencing_type, (CEntity_definition)inst.getInstanceType())) {
							makeUsedin2(inst, role, result, domain, true);
						}
						o = inv.next;
					} else {
						inst = (CEntity)o;
						k++;
						if ((k < 2 || (k > 1 && different(inst, k))) &&
								checkType(referencing_type, (CEntity_definition)inst.getInstanceType())) {
							makeUsedin2(inst, role, result, domain, true);
						}
						o = null;
					}
				}
				CEntityDefinition [] ent_types = null;
				CDerived_attribute [] der_attrs = null;
//				int count = findSubtypesWithDerived(referencing_type, role, ent_types, der_attrs);
//				if (count > 0) {
//					appendResult(count, result, domain, ent_types, der_attrs);
//				}
            //result = examineModelsUsers((CEntityDefinition)referencing_type, role, result, domain);
				boolean duplicates = attribute.getDuplicates(null);
				if (!duplicates && result.myLength > 1) {
					result.contract();
				}
				return result;
			}
		}

		if (attribute.testRedeclaring(null)) {
			EInverse_attribute redeclared_attr = attribute.getRedeclaring(null);
//if (Value.prnt)
//System.out.println("CEntity  RRRRRRRRRRRRRRRRRR    attribute: " + attribute +
//"   redeclared_attr: " + redeclared_attr +
//"   redeclared_attr_owner: " + redeclared_attr.getParent_entity(null).getName(null) +
//"   this edef: " + getInstanceType().getName(null));
			return get_inverse(redeclared_attr, domain);
		}

//System.out.println("CEntity  !!!!!    saved_attr: " + saved_attr +
//"   attr_owner: " + saved_attr.getParent_entity(null).getName(null) + "   this edef: " + edef.getName(null));

		printWarningToLogoAttribute(owning_model.repository.session, saved_attr, AdditionalMessages.AT_INEX);
		throw new SdaiException(SdaiException.AT_NVLD, saved_attr,
			"The supplied attribute is not inverse one for this entity type");
//		} // syncObject
	}


	int findSubtypesWithDerived(CEntity_definition referencing_type, CExplicit_attribute role, 
			CEntityDefinition [] ent_types, CDerived_attribute [] der_attrs) throws SdaiException {
		int count = 0;
		SchemaData sch_data = owning_model.underlying_schema.modelDictionary.schemaData;
		int ref_index = sch_data.find_entity(0, sch_data.sNames.length - 1, referencing_type);
		if (ref_index < 0) {
			return 0;
		}
		int subtypes [] = sch_data.schema.getSubtypes(ref_index);
		for (int i = 0; i < subtypes.length; i++) {
			CEntityDefinition edef = sch_data.entities[subtypes[i]];
			boolean expl_redecl = false;
			for (int j = 0; j < edef.attributes.length; j++) {
				if (edef.attributes[j] == role) {
					if (edef.attributeFields[j] == null) {
						expl_redecl = true;
					}
					break;
				}
			}
			if (expl_redecl) {
				EAttribute ex_der_attr = edef.getIfDerived(role);
				if (ex_der_attr != null) {
					if (ent_types == null) {
						ent_types = new CEntityDefinition[ENTITY_TYPES_ARRAY_SIZE];
						der_attrs = new CDerived_attribute[ENTITY_TYPES_ARRAY_SIZE];
					} else if (count >= ent_types.length) {
						int new_ln = 2 * ent_types.length;
						CEntityDefinition new_ent_types [] = new CEntityDefinition[new_ln];
						System.arraycopy(ent_types, 0, new_ent_types, 0, ent_types.length);
						ent_types = new_ent_types;
						CDerived_attribute new_der_attrs [] = new CDerived_attribute[new_ln];
						System.arraycopy(der_attrs, 0, new_der_attrs, 0, der_attrs.length);
						der_attrs = new_der_attrs;
					}
					ent_types[count] = edef;
					der_attrs[count] = (CDerived_attribute)ex_der_attr;
					count++;
				}
			}
		}
		return count;
	}


	void appendResult(int count, AEntity result, ASdaiModel domain, 
			CEntityDefinition [] ent_types, CDerived_attribute [] der_attrs) throws SdaiException {
		ASdaiModel models;
		if (domain == null) {
			models = new ASdaiModel(SdaiSession.setType0toN, this);
			models.addUnorderedRO(owning_model);
		} else if (domain.myLength == 0) {
			models = owning_model.repository.session.active_models;
		} else {
			models = domain;
		}
		StaticFields staticFields = StaticFields.get();
		if (staticFields.it == null) {
			staticFields.it = models.createIterator();
		} else {
			models.attachIterator(staticFields.it);
		}
		while (staticFields.it.next()) {
			SdaiModel model = (SdaiModel)models.getCurrentMemberObject(staticFields.it);
			if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS || model.instances_sim == null) {
				continue;
			}
			SchemaData sch_data = model.underlying_schema.modelDictionary.schemaData;
			for (int k = 0; k < count; k++) {
				CEntityDefinition edef = ent_types[k];
				if (edef.attributesDerived == null) {
					continue;
				}
				int ref_index = sch_data.find_entity(0, sch_data.sNames.length - 1, (CEntity_definition)edef);
				if (ref_index < 0) {
					continue;
				}
				if (model.instances_sim[ref_index] == null || model.lengths[ref_index] <= 0) {
					continue;
				}
				CDerived_attribute der_attr = der_attrs[k];
				int attr_index = -1;
				for (int i = 0; i < edef.attributesDerived.length; i++) {
					if (edef.attributesDerived[i] == der_attr) {
						attr_index = i;
						break;
					}
				}
				CEntity [] row_of_instances = model.instances_sim[ref_index];
				for (int j = 0; j < model.lengths[ref_index]; j++) {
					CEntity inst = row_of_instances[j];
					Object val_object;
					if (attr_index >= 0) {
						val_object = inst.get_derived(edef, attr_index);
					} else {
						val_object = inst.get_derived_redecl(edef, der_attr);
					}
					if (val_object == null || !(val_object instanceof CEntity)) {
						continue;
					}
					CEntity value_ent = (CEntity)val_object;
					if (value_ent != inst) {
						continue;
					}
					if (result.myType == null || result.myType.express_type == DataType.LIST) {
						result.addAtTheEnd(inst, null);
					} else {
						result.setForNonList(inst, result.myLength, null, null);
					}
				} // end j
			} // end k
		} // end while
	}


	private AEntity examineModelsUsers(CEntityDefinition referencing_type, CExplicit_attribute role, 
			AEntity result, ASdaiModel domain) throws SdaiException {
		StaticFields staticFields = StaticFields.get();
		if (domain == null || domain.myLength == 0) {
			return processPotentialUsers(owning_model, referencing_type, role, result, staticFields);
		}
		if (staticFields.it == null) {
			staticFields.it = domain.createIterator();
		} else {
			domain.attachIterator(staticFields.it);
		}
		while (staticFields.it.next()) {
			SdaiModel mod = (SdaiModel)domain.getCurrentMemberObject(staticFields.it);
			result = processPotentialUsers(mod, referencing_type, role, result, staticFields);
		}
		return result;
	}


	private AEntity processPotentialUsers(SdaiModel mod, CEntityDefinition referencing_type, CExplicit_attribute role, 
			AEntity result, StaticFields staticFields) throws SdaiException {
		SchemaData sch_data = mod.underlying_schema.owning_model.schemaData;
		int ref_index = sch_data.find_entity(0, sch_data.sNames.length - 1, referencing_type);
		if (ref_index < 0) {
			return result;
		}
		result = processInstances(mod, ref_index, role, result, staticFields);
		int subtypes [] = sch_data.schema.getSubtypes(ref_index);
		for (int i = 0; i < subtypes.length; i++) {
			result = processInstances(mod, subtypes[i], role, result, staticFields);
		}
		return result;
	}


	private AEntity processInstances(SdaiModel mod, int ref_index, CExplicit_attribute role, 
			AEntity result, StaticFields staticFields) throws SdaiException {
		int index_to_type_local;
		CEntityDefinition exact_type = mod.dictionary.schemaData.entities[ref_index];
		if ((mod.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY) {
			index_to_type_local = mod.find_entityRO(exact_type);
			if (index_to_type_local < 0) {
				throw new SdaiException(SdaiException.IX_NVLD, this);
			}
		} else {
			index_to_type_local = ref_index;
		}
		if (mod.lengths[index_to_type_local] <= 0) {
			return result;
		}
		EAttribute d_attr = exact_type.findLastDerived(role, staticFields);
		if (d_attr == null) {
			return result;
		}
		CDerived_attribute der_attr = (CDerived_attribute)d_attr;
		DataType type = analyse_attr_type((DataType)der_attr.getDomain(null));
		if (type == null) {
			return result;
		}
		CEntity_definition inv_owner_type = (CEntity_definition)getInstanceType();
		if (!check_type_compatibility(type, inv_owner_type, staticFields)) {
			return result;
		}
		int der_attr_index = -1;
		if (exact_type.attributesDerived != null) {
			for (int i = 0; i < exact_type.attributesDerived.length; i++) {
				if (exact_type.attributesDerived[i] == der_attr) {
					der_attr_index = i;
					break;
				}
			}
		}
		CEntity [] row_of_instances = mod.instances_sim[index_to_type_local];
		Object value;
		for (int j = 0; j < mod.lengths[index_to_type_local]; j++) {
			CEntity instance = row_of_instances[j];
			if (der_attr_index >= 0) {
				value = instance.get_derived(exact_type, der_attr_index);
			} else {
				value = instance.get_derived_redecl(exact_type, der_attr);
			}
			if (value == null) {
				continue;
			}
			if (value == this) {
				if (result.myType == null || result.myType.express_type == DataType.LIST) {
					result.addAtTheEnd(instance, null);
				} else {
					result.setForNonList(instance, result.myLength, null, null);
				}
			} else if (value instanceof CAggregate) {
				((CAggregate)value).usedin(this, instance, result);
			}
		}
		return result;
	}


	private DataType analyse_attr_type(DataType attr_type) throws SdaiException {
		if (attr_type.express_type >= DataType.LIST && attr_type.express_type <= DataType.AGGREGATE) {
			return attr_type;
		} else if (attr_type.express_type == DataType.ENTITY) {
			return attr_type;
		} else if (attr_type.express_type != DataType.DEFINED_TYPE) {
			return null;
		}
		DataType type = attr_type;
		while (type.express_type == DataType.DEFINED_TYPE) {
			type = (DataType)((CDefined_type)type).getDomain(null);
			if (type.express_type >= DataType.LIST && type.express_type <= DataType.AGGREGATE) {
				return type;
			}
			if (type.express_type >= DataType.SELECT && type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
				return type;
			} else if (type.express_type == DataType.DEFINED_TYPE) {
				continue;
			} else {
				return null;
			}
		}
		return null;
	}


	private boolean check_type_compatibility(DataType type, CEntity_definition inv_owner_type, 
				StaticFields staticFields) throws SdaiException {
		if (type.express_type == DataType.ENTITY) {
			return inv_owner_type.isSubtypeOf((CEntity_definition)type);
		} else if (type.express_type >= DataType.LIST && type.express_type <= DataType.AGGREGATE) {
			DataType next_type = analyse_attr_type((DataType)((EAggregation_type)type).getElement_type(null));
			if (next_type == null) {
				return false;
			}
			return check_type_compatibility(next_type, inv_owner_type, staticFields);
		} else {
			SdaiSession ss = owning_model.repository.session;
			if (type.express_type >= DataType.EXTENSIBLE_SELECT && ss.sdai_context == null) {
				staticFields.context_schema = owning_model.underlying_schema;
				if (!ss.sdai_context_missing) {
					ss.sdai_context_missing = true;
					ss.printWarningToLogoSdaiContext(AdditionalMessages.SS_SCMI);
				}
			}
			return ((SelectType)type).analyse_entity_in_select(inv_owner_type, ss.sdai_context);
		}
	}


	private Object get_derived(CEntityDefinition edef, int index) throws SdaiException {
		StaticFields staticFields = StaticFields.get();
		if (staticFields.args == null) {
			staticFields.args = new Object [1];
			staticFields.args[0] = null;
		}
		Method meth = edef.attributesDerivedMethod[index];
		if (meth == null) {
			throw new SdaiException(SdaiException.SY_ERR, "Expression for derived attribute is not provided");
		}
		Object value;
		try {
			value = meth.invoke(this, staticFields.args);
		} catch (Exception ex) {
			throw (SdaiException)new SdaiException(SdaiException.SY_ERR, ex.toString()).initCause(ex);
		}
		return value;
	}


	private Object get_derived_redecl(CEntityDefinition edef, CDerived_attribute der_attr) throws SdaiException {
		if (!der_attr.testRedeclaring(null)) {
			throw new SdaiException(SdaiException.SY_ERR, der_attr);
		}
		SchemaData sch_data = edef.owning_model.schemaData;
		int index = sch_data.findEntityExtentIndex(edef);
		if (index < 0) {
			throw new SdaiException(SdaiException.SY_ERR, "Entity not found in the table");
		}
		Class cl = sch_data.getEntityClassByIndex(index);
		EEntity redecl = der_attr.getRedeclaring(null);
		if (((AttributeDefinition)redecl).attr_tp == AttributeDefinition.DERIVED) {
			throw new SdaiException(SdaiException.FN_NAVL, redecl);
		}
		EExplicit_attribute red = (EExplicit_attribute)redecl;
		String red_name = red.getName(null);
		while (red != null) {
			if (red.testRedeclaring(null)) {
				EExplicit_attribute red_next = red.getRedeclaring(null);
				String red_name_next = red_next.getName(null);
				if (red_name_next.equals(red_name)) {
					red = red_next;
					red_name = red_name_next;
				} else {
					redecl = red;
					break;
				}
			} else {
				redecl = red;
				break;
			}
		}
		CEntity_definition super_type = (CEntity_definition)((CExplicit_attribute)redecl).getParent_entity(null);
		SchemaData type_sch_data = super_type.owning_model.schemaData;
		int indx = type_sch_data.findEntityExtentIndex(super_type);
		if (indx < 0) {
			throw new SdaiException(SdaiException.SY_ERR, "Entity not found in the table");
		}
		Class true_if_cl = type_sch_data.getEntityInterfaceByIndex(indx);
		String m_name = CEntityDefinition.GET_METHOD_PREFIX + normalise(der_attr.getName(null));

		StaticFields staticFields = StaticFields.get();
		Method meth;
		if (staticFields.param_ed == null) {
			staticFields.param_ed = new Class[1];
		}
		staticFields.param_ed[0] = true_if_cl;
		try {
			meth = cl.getDeclaredMethod(m_name, staticFields.param_ed);
		} catch (java.lang.NoSuchMethodException ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
		Object res;
		if (staticFields.args == null) {
			staticFields.args = new Object [1];
			staticFields.args[0] = null;
		}
		try {
			res = meth.invoke(this, staticFields.args);
		} catch (Exception ex) {
//ex.printStackTrace();
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
		return res;
	}


	public Object get_object(EAttribute attribute) throws SdaiException {
//System.out.println("***CEntity   attribute: " + attribute.getName(null));
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (attribute == null) {
			throw new SdaiException(SdaiException.AT_NDEF);
		}
		EAttribute saved_attr = attribute;
		if (((AttributeDefinition)attribute).attr_tp == AttributeDefinition.EXPLICIT &&
				((CExplicit_attribute)attribute).testRedeclaring(null)) {
			EExplicit_attribute expl_attr = (EExplicit_attribute)attribute;
			while (expl_attr.testRedeclaring(null)) {
				expl_attr = expl_attr.getRedeclaring(null);
			}
			attribute = expl_attr;
		}

		CEntityDefinition edef = (CEntityDefinition)getInstanceType();
		Object value;
// test for explicit attributes
		boolean expl_redecl = false;
		if (((AttributeDefinition)attribute).attr_tp == AttributeDefinition.EXPLICIT) {
			for (int i = 0; i < edef.attributes.length; i++) {
				if (edef.attributes[i] == attribute) {
					if (edef.attributeFields[i] != null) {
						Field field = edef.attributeFields[i];
						SSuper ssuper;
						if (owning_model.described_schema == null) {
							ssuper = edef.fieldOwners[i].ssuper;
						} else {
							ssuper = edef.ssuper;
						}
						try {
							value = ssuper.getObject(this, field);
						} catch (Exception ex) {
							throw new SdaiException(SdaiException.SY_ERR, ex);
						}
//System.out.println("***CEntity  value type: " + value.getClass().getName());
						if (value instanceof SdaiModel.Connector) {
							value = resolveAndReplaceConnector((SdaiModel.Connector)value, attribute);
						}
						if (value == null) {
							throw new SdaiException(SdaiException.VA_NSET);
						}
						return value;
					}
					expl_redecl = true;
				}
			}
		}

		if (expl_redecl) {
			EAttribute ex_der_attr = edef.getIfDerived((CExplicit_attribute)attribute);
			if (ex_der_attr != null) {
				attribute = ex_der_attr;
			}
		}

// test for derived attributes
		if (((AttributeDefinition)attribute).attr_tp == AttributeDefinition.DERIVED) {
			if (edef.attributesDerived != null) {
				for (int i = 0; i < edef.attributesDerived.length; i++) {
					if (edef.attributesDerived[i] == attribute) {
						value = get_derived(edef, i);
						if (value == null) {
							throw new SdaiException(SdaiException.VA_NSET, attribute);
						}
						return value;
					}
				}
			}
			value = get_derived_redecl(edef, (CDerived_attribute)attribute);
			if (value == null) {
				throw new SdaiException(SdaiException.VA_NSET, attribute);
			}
			return value;
		}

// test for inverse attributes
		if (((AttributeDefinition)attribute).attr_tp == AttributeDefinition.INVERSE) {
			if (edef.attributesInverse != null) {
				for (int i = 0; i < edef.attributesInverse.length; i++) {
					if (edef.attributesInverse[i] == attribute) {
						throw new SdaiException(SdaiException.FN_NAVL,
							"For inverse attributes method get_inverse should be invoked");
					}
				}
			}
		}

		throw new SdaiException(SdaiException.AT_NVLD, saved_attr);
//		} // syncObject
	}


	public int get_int(EAttribute attribute) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (attribute == null) {
			throw new SdaiException(SdaiException.AT_NDEF);
		}
		EAttribute saved_attr = attribute;
		if (((AttributeDefinition)attribute).attr_tp == AttributeDefinition.EXPLICIT &&
				((CExplicit_attribute)attribute).testRedeclaring(null)) {
			EExplicit_attribute expl_attr = (EExplicit_attribute)attribute;
			while (expl_attr.testRedeclaring(null)) {
				expl_attr = expl_attr.getRedeclaring(null);
			}
			attribute = expl_attr;
		}

		CEntityDefinition edef = (CEntityDefinition)getInstanceType();
		SelectType sel_type;
		Object val_object;
		int value;
// test for explicit attributes
		boolean expl_redecl = false;
		if (((AttributeDefinition)attribute).attr_tp == AttributeDefinition.EXPLICIT) {
			for (int i = 0; i < edef.attributes.length; i++) {
				if (edef.attributes[i] == attribute) {
					if (edef.attributeFields[i] != null) {
						Field field = edef.attributeFields[i];
						SSuper ssuper;
						if (owning_model.described_schema == null) {
							ssuper = edef.fieldOwners[i].ssuper;
						} else {
							ssuper = edef.ssuper;
						}
						try {
							sel_type = take_select(attribute);
							if (sel_type == null) {
								// looking for types: enumeration, logical, boolean (test_result = 2)
								// or integer (test_result = 4)
								switch(find_type(attribute)) {
								case 2:
								case 5:
									value = ssuper.getInt(this, field);
									if (value == 0) {
										throw new SdaiException(SdaiException.VA_NSET);
									}
									break;
								case 4:
									value = ssuper.getInt(this, field);
									if (value == Integer.MIN_VALUE) {
										throw new SdaiException(SdaiException.VA_NSET);
									}
									break;
								default:
									throw new SdaiException(SdaiException.VT_NVLD);
								}
								return value;
							} else if (sel_type.is_mixed == 0) {
								throw new SdaiException(SdaiException.VT_NVLD);
							} else {
								val_object = ssuper.getObject(this, field);
								if (val_object == null) {
									throw new SdaiException(SdaiException.VA_NSET);
								} else if (val_object instanceof Integer) {
									return ((Integer)val_object).intValue();
								} else {
									throw new SdaiException(SdaiException.VT_NVLD);
								}
							}
						} catch (java.lang.IllegalArgumentException ex) {
							throw new SdaiException(SdaiException.AT_NVLD, saved_attr);
						} catch (java.lang.IllegalAccessException ex) {
							throw new SdaiException(SdaiException.SY_ERR, ex);
						}
					}
					expl_redecl = true;
				}
			}
		}

		if (expl_redecl) {
			EAttribute ex_der_attr = edef.getIfDerived((CExplicit_attribute)attribute);
			if (ex_der_attr != null) {
				attribute = ex_der_attr;
			}
		}

// test for derived attributes
		if (((AttributeDefinition)attribute).attr_tp == AttributeDefinition.DERIVED) {
			boolean der_value_found = false;
			val_object = null;
			if (edef.attributesDerived != null) {
				for (int i = 0; i < edef.attributesDerived.length; i++) {
					if (edef.attributesDerived[i] == attribute) {
						val_object = get_derived(edef, i);
						der_value_found = true;
					}
				}
			}
			if (!der_value_found) {
				val_object = get_derived_redecl(edef, (CDerived_attribute)attribute);
			}
			if (val_object == null) {
				throw new SdaiException(SdaiException.VA_NSET);
			} else if (val_object instanceof Integer) {
				value = ((Integer)val_object).intValue();
			} else if (val_object instanceof Boolean) {
				String base = SdaiSession.line_separator + AdditionalMessages.EI_NCOM;
				throw new SdaiException(SdaiException.VT_NVLD, base);
			} else {
				throw new SdaiException(SdaiException.VT_NVLD);
			}
			sel_type = take_select(attribute);
			if (sel_type == null) {
				switch(find_type(attribute)) {
				case 2:
				case 5:
					if (value == 0) {
						throw new SdaiException(SdaiException.VA_NSET);
					}
					break;
				case 4:
					if (value == Integer.MIN_VALUE) {
						throw new SdaiException(SdaiException.VA_NSET);
					}
					break;
				default:
					throw new SdaiException(SdaiException.VT_NVLD);
				}
				return value;
			} else if (sel_type.is_mixed == 0) {
				throw new SdaiException(SdaiException.VT_NVLD);
			} else {
				return value;
			}
		}

/*		if (attribute instanceof CExplicit_attribute) {
			CExplicit_attribute expl_attr = (CExplicit_attribute)attribute;
			if (expl_attr.testRedeclaring(null)) {
				EExplicit_attribute redeclared_attr = expl_attr.getRedeclaring(null);
				return get_int(redeclared_attr);
			}
		}*/

		throw new SdaiException(SdaiException.AT_NVLD, saved_attr);
//		} // syncObject
	}


	public double get_double(EAttribute attribute) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (attribute == null) {
			throw new SdaiException(SdaiException.AT_NDEF, attribute);
		}
		EAttribute saved_attr = attribute;
		if (((AttributeDefinition)attribute).attr_tp == AttributeDefinition.EXPLICIT &&
				((CExplicit_attribute)attribute).testRedeclaring(null)) {
			EExplicit_attribute expl_attr = (EExplicit_attribute)attribute;
			while (expl_attr.testRedeclaring(null)) {
				expl_attr = expl_attr.getRedeclaring(null);
			}
			attribute = expl_attr;
		}

		CEntityDefinition edef = (CEntityDefinition)getInstanceType();
		SelectType sel_type;
		Object val_object;
		double value;
// test for explicit attributes
		boolean expl_redecl = false;
		if (((AttributeDefinition)attribute).attr_tp == AttributeDefinition.EXPLICIT) {
			for (int i = 0; i < edef.attributes.length; i++) {
				if (edef.attributes[i] == attribute) {
					if (edef.attributeFields[i] != null) {
						Field field = edef.attributeFields[i];
						SSuper ssuper;
						if (owning_model.described_schema == null) {
							ssuper = edef.fieldOwners[i].ssuper;
						} else {
							ssuper = edef.ssuper;
						}
						try {
							sel_type = take_select(attribute);
							if (sel_type == null) {
								if (find_type(attribute) != 3) {
									throw new SdaiException(SdaiException.VT_NVLD);
								}
								value = ssuper.getDouble(this, field);
								if (Double.isNaN(value)) {
									throw new SdaiException(SdaiException.VA_NSET);
								}
								return value;
							} else if (sel_type.is_mixed == 0) {
								throw new SdaiException(SdaiException.VT_NVLD);
							} else {
								val_object = ssuper.getObject(this, field);
								if (val_object == null) {
									throw new SdaiException(SdaiException.VA_NSET);
								} else if (val_object instanceof Double) {
									return ((Double)val_object).doubleValue();
								} else {
									throw new SdaiException(SdaiException.VT_NVLD);
								}
							}
						} catch (java.lang.IllegalArgumentException ex) {
							throw new SdaiException(SdaiException.AT_NVLD, saved_attr);
						} catch (java.lang.IllegalAccessException ex) {
							throw new SdaiException(SdaiException.SY_ERR, ex);
						}
					}
					expl_redecl = true;
				}
			}
		}

		if (expl_redecl) {
			EAttribute ex_der_attr = edef.getIfDerived((CExplicit_attribute)attribute);
			if (ex_der_attr != null) {
				attribute = ex_der_attr;
			}
		}

// test for derived attributes
		if (((AttributeDefinition)attribute).attr_tp == AttributeDefinition.DERIVED) {
			boolean der_value_found = false;
			val_object = null;
			if (edef.attributesDerived != null) {
				for (int i = 0; i < edef.attributesDerived.length; i++) {
					if (edef.attributesDerived[i] == attribute) {
						val_object = get_derived(edef, i);
						der_value_found = true;
					}
				}
			}
			if (!der_value_found) {
				val_object = get_derived_redecl(edef, (CDerived_attribute)attribute);
			}
			if (val_object == null) {
				throw new SdaiException(SdaiException.VA_NSET);
			} else if (val_object instanceof Double) {
				value = ((Double)val_object).doubleValue();
			} else {
				throw new SdaiException(SdaiException.VT_NVLD);
			}
			sel_type = take_select(attribute);
			if (sel_type == null) {
				if (find_type(attribute) != 3) {
					throw new SdaiException(SdaiException.VT_NVLD);
				}
				return value;
			} else if (sel_type.is_mixed == 0) {
				throw new SdaiException(SdaiException.VT_NVLD);
			} else {
				return value;
			}
		}

/*		if (attribute instanceof CExplicit_attribute) {
			CExplicit_attribute expl_attr = (CExplicit_attribute)attribute;
			if (expl_attr.testRedeclaring(null)) {
				EExplicit_attribute redeclared_attr = expl_attr.getRedeclaring(null);
				return get_double(redeclared_attr);
			}
		}*/

		throw new SdaiException(SdaiException.AT_NVLD, saved_attr);
//		} // syncObject
	}


	public boolean get_boolean(EAttribute attribute) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (attribute == null) {
			throw new SdaiException(SdaiException.AT_NDEF, attribute);
		}
		EAttribute saved_attr = attribute;
		if (((AttributeDefinition)attribute).attr_tp == AttributeDefinition.EXPLICIT &&
				((CExplicit_attribute)attribute).testRedeclaring(null)) {
			EExplicit_attribute expl_attr = (EExplicit_attribute)attribute;
			while (expl_attr.testRedeclaring(null)) {
				expl_attr = expl_attr.getRedeclaring(null);
			}
			attribute = expl_attr;
		}

		CEntityDefinition edef = (CEntityDefinition)getInstanceType();
		SelectType sel_type;
		Object val_object;
		int value;
// test for explicit attributes
		boolean expl_redecl = false;
		if (((AttributeDefinition)attribute).attr_tp == AttributeDefinition.EXPLICIT) {
			for (int i = 0; i < edef.attributes.length; i++) {
				if (edef.attributes[i] == attribute) {
					if (edef.attributeFields[i] != null) {
						Field field = edef.attributeFields[i];
						SSuper ssuper;
						if (owning_model.described_schema == null) {
							ssuper = edef.fieldOwners[i].ssuper;
						} else {
							ssuper = edef.ssuper;
						}
						try {
							sel_type = take_select(attribute);
							if (sel_type == null) {
								if (find_type(attribute) == 5) {
									value = ssuper.getInt(this, field);
									if (value == 0) {
										throw new SdaiException(SdaiException.VA_NSET);
									} else if (value == 1) {
										return false;
									} else if (value == 2) {
										return true;
									} else {
										throw new SdaiException(SdaiException.VA_NVLD);
									}
								} else {
									throw new SdaiException(SdaiException.VT_NVLD);
								}
							} else if (sel_type.is_mixed == 0) {
								throw new SdaiException(SdaiException.VT_NVLD);
							} else {
								val_object = ssuper.getObject(this, field);
								if (val_object == null) {
									throw new SdaiException(SdaiException.VA_NSET);
								} else if (val_object instanceof Integer) {
									value = ((Integer)val_object).intValue();
									if (value == 1) {
										return false;
									} else if (value == 2) {
										return true;
									} else {
										throw new SdaiException(SdaiException.VA_NVLD);
									}
								} else {
									throw new SdaiException(SdaiException.VT_NVLD);
								}
							}
						} catch (java.lang.IllegalArgumentException ex) {
							throw new SdaiException(SdaiException.AT_NVLD, saved_attr);
						} catch (java.lang.IllegalAccessException ex) {
							throw new SdaiException(SdaiException.SY_ERR, ex);
						}
					}
					expl_redecl = true;
				}
			}
		}

		if (expl_redecl) {
			EAttribute ex_der_attr = edef.getIfDerived((CExplicit_attribute)attribute);
			if (ex_der_attr != null) {
				attribute = ex_der_attr;
			}
		}

// test for derived attributes
		if (((AttributeDefinition)attribute).attr_tp == AttributeDefinition.DERIVED) {
			boolean der_value_found = false;
			val_object = null;
			if (edef.attributesDerived != null) {
				for (int i = 0; i < edef.attributesDerived.length; i++) {
					if (edef.attributesDerived[i] == attribute) {
						val_object = get_derived(edef, i);
						der_value_found = true;
					}
				}
			}
			if (!der_value_found) {
				val_object = get_derived_redecl(edef, (CDerived_attribute)attribute);
			}
			if (val_object == null) {
				throw new SdaiException(SdaiException.VA_NSET);
			} else if (val_object instanceof Integer) {
				value = ((Integer)val_object).intValue();
			} else if (val_object instanceof Boolean) {
				boolean bval = ((Boolean)val_object).booleanValue();
				if (bval) {
					value = 2;
				} else {
					value = 1;
				}
			} else {
				throw new SdaiException(SdaiException.VT_NVLD);
			}
			sel_type = take_select(attribute);
			if (sel_type == null) {
				if (find_type(attribute) != 5) {
					throw new SdaiException(SdaiException.VT_NVLD);
				}
			} else if (sel_type.is_mixed == 0) {
				throw new SdaiException(SdaiException.VT_NVLD);
			}
			if (value == 0) {
				throw new SdaiException(SdaiException.VA_NSET);
			} else if (value == 1) {
				return false;
			} else if (value == 2) {
				return true;
			} else {
				throw new SdaiException(SdaiException.VA_NVLD);
			}
		}

/*		if (attribute instanceof CExplicit_attribute) {
			CExplicit_attribute expl_attr = (CExplicit_attribute)attribute;
			if (expl_attr.testRedeclaring(null)) {
				EExplicit_attribute redeclared_attr = expl_attr.getRedeclaring(null);
				return get_boolean(redeclared_attr);
			}
		}*/

		throw new SdaiException(SdaiException.AT_NVLD, saved_attr);
//		} // syncObject
	}


	public int testAttribute(EAttribute attribute, EDefined_type[] select) throws SdaiException {
		int testResult = testAttributeFast(attribute, select);
		if(testResult >= 0) {
			return testResult;
		} else {
			throw new SdaiException(SdaiException.AT_NVLD, attribute);
		}
	}

	/**
	 * Checks if the specified attribute has a value and if so, then
	 * returns an indicator showing the type of the value. This method
	 * performs the same task as {@link #testAttribute(EAttribute attribute, EDefined_type[] select)}
	 * but in the case attribute does not belong to this entity value
	 * of <code>-1</code> is returned.
	 *
	 * @param attribute the attribute which value is tested.
	 * @param select the array of defined types used to store the select path
	 * corresponding to the actual value of the attribute.
	 * @return Type of the value (0 if the attribute has no value and
	 *         -1 if the attribute does not belong to this entity).
	 * @throws SdaiException RP_NOPN, repository is not open.
	 * @throws SdaiException TR_NAVL, transaction currently not available.
	 * @throws SdaiException TR_EAB, transaction ended abnormally.
	 * @throws SdaiException EI_NEXS, entity instance does not exist.
	 * @throws SdaiException AT_NDEF, attribute not defined.
	 * @throws SdaiException SY_ERR, underlying system error.
	 * @see #testAttribute(EAttribute attribute, EDefined_type[] select)
	 */
	public int testAttributeFast(EAttribute attribute, EDefined_type[] select) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (attribute == null) {
			throw new SdaiException(SdaiException.AT_NDEF);
		}
		EAttribute saved_attr = attribute;
		if (((AttributeDefinition)attribute).attr_tp == AttributeDefinition.EXPLICIT &&
				((CExplicit_attribute)attribute).testRedeclaring(null)) {
			EExplicit_attribute expl_attr = (EExplicit_attribute)attribute;
			while (expl_attr.testRedeclaring(null)) {
				expl_attr = expl_attr.getRedeclaring(null);
			}
			attribute = expl_attr;
		}

		CEntityDefinition edef = (CEntityDefinition)getInstanceType();
//for (int i = 0; i < edef.attributes.length; i++) {
//Field fie = edef.attributeFields[i];
//Class ccaa = fie.getDeclaringClass();
//System.out.println("  CEntity  i = " + i +
//"   field: " + fie.getName() + "   field_owner: " + ccaa.getName());
//}
		int val_int;
		Object val_object;
		boolean expl_redecl = false;
		for (int i = 0; i < edef.attributes.length; i++) {
			if (edef.attributes[i] == attribute) {
				if (edef.attributeFields[i] != null) {
					int test_result;
					SelectType sel_type = take_select(attribute);
					if (sel_type == null) {
						test_result = find_type(attribute);
					} else if (sel_type.is_mixed == 0) {
						test_result = 1;
					} else {
						test_result = -1;
					}
					Field field = edef.attributeFields[i];
					SSuper ssuper;
					if (owning_model.described_schema == null) {
						ssuper = edef.fieldOwners[i].ssuper;
					} else {
						ssuper = edef.ssuper;
					}
					switch (test_result) {
						case -1:
							try {
								val_object = ssuper.getObject(this, field);
							} catch (Exception ex) {
								throw new SdaiException(SdaiException.SY_ERR, ex);
							}
							if (val_object == null) {
								test_result = 0;
							} else if (val_object instanceof Integer) {
								test_result = 2;
							} else if (val_object instanceof Double) {
								test_result = 3;
							} else {
								if (val_object instanceof SdaiModel.Connector) {
									val_object =
										resolveAndReplaceConnector((SdaiModel.Connector)val_object, attribute);
								}
								test_result = val_object != null ? 1 : 0;
							}
							if (test_result > 0) {
								int sel_number;
								if (val_object instanceof CEntity) {
									sel_number = 1;
								} else {
									try {
										Field sel_field = edef.attributeFieldSelects[i];
										sel_number = ssuper.getInt(this, sel_field);
									} catch (Exception ex) {
										throw new SdaiException(SdaiException.SY_ERR, ex);
									}
								}
								if (sel_number <= 1) {
									if (select != null && select.length > 0) {
										select[0] = null;
									}
									break;
								}
								int result = sel_type.giveDefinedTypes(sel_number, select);
								if (result == -2) {
									String base = SdaiSession.line_separator + AdditionalMessages.SE_INSI;
									throw new SdaiException(SdaiException.SY_ERR, base);
								} else if (result == -1) {
									String base = SdaiSession.line_separator + AdditionalMessages.SE_ARTS;
									throw new SdaiException(SdaiException.SY_ERR, base);
								}
								if (test_result == 2) {
									int tag = sel_type.tags[sel_number - 2];
									if (tag == PhFileReader.BOOLEAN) {
										test_result = 4;
									}
								}
							}
							break;
						case  0:
							break;
						case  1:
//if (ssuper == null) System.out.println("  CEntity   ssuper is NULL" +
//"    entity name: " + edef.getName(null) + "   owning_model: " + edef.owning_model.name);
//else System.out.println("  CEntity   ssuper is POS" +
//"    entity name: " + edef.getName(null) + "   owning_model: " + edef.owning_model.name);
							try {
//CExplicit_attribute exat = (CExplicit_attribute)attribute;
//CEntityDefinition parent = (CEntityDefinition)exat.getParent_entity(null);
//SSuper true_ssuper = parent.ssuper;
//Class ccc = field.getDeclaringClass();
//System.out.println("  CEntity  this: " + this.getClass().getName() +
//"   field: " + field.getName() + "   ssuper: " + ssuper.getClass().getName() +
//"   field_owner: " + ccc.getName());
//val_object = true_ssuper.getObject(this, field);
								val_object = ssuper.getObject(this, field);
							} catch (Exception ex) {
								throw new SdaiException(SdaiException.SY_ERR, ex);
							}
							if (val_object instanceof SdaiModel.Connector) {
								val_object =
									resolveAndReplaceConnector((SdaiModel.Connector)val_object, attribute);
							}
							if (val_object == null) {
								test_result = 0;
							}
							break;
						case  2:
							try {
								val_int = ssuper.getInt(this, field);
							} catch (Exception ex) {
								throw new SdaiException(SdaiException.SY_ERR, ex);
							}
							if (val_int == 0) {
								test_result = 0;
							}
							break;
						case  5:
							try {
								val_int = ssuper.getInt(this, field);
							} catch (Exception ex) {
								throw new SdaiException(SdaiException.SY_ERR, ex);
							}
							if (val_int == 0) {
								test_result = 0;
							} else {
								test_result = 4;
							}
							break;
						case  3:
							double val_double;
							try {
								val_double = ssuper.getDouble(this, field);
							} catch (Exception ex) {
								throw new SdaiException(SdaiException.SY_ERR, ex);
							}
							if (Double.isNaN(val_double)) {
								test_result = 0;
							}
							break;
						case  4:
							try {
								val_int = ssuper.getInt(this, field);
							} catch (Exception ex) {
								throw new SdaiException(SdaiException.SY_ERR, ex);
							}
							if (val_int == Integer.MIN_VALUE) {
								test_result = 0;
							} else {
								test_result = 2;
							}
							break;
					}
					return test_result;
				}
			expl_redecl = true;
			}
		}

		if (expl_redecl) {
			EAttribute ex_der_attr = edef.getIfDerived((CExplicit_attribute)attribute);
			if (ex_der_attr != null) {
				attribute = ex_der_attr;
			}
		}

      // test for derived attributes
      if (((AttributeDefinition)attribute).attr_tp == AttributeDefinition.DERIVED) {
 	     boolean der_value_found = false;
			val_object = null;
			if (edef.attributesDerived != null) {
				for (int i = 0; i < edef.attributesDerived.length; i++) {
					if (edef.attributesDerived[i] == attribute) {
						val_object = get_derived(edef, i);
						der_value_found = true;
					}
				}
			}
			if (!der_value_found) {
				val_object = get_derived_redecl(edef, (CDerived_attribute)attribute);
			}
			if (val_object == null) {
				return 0;
			}
			SelectType sel_type = take_select(attribute);
			if (sel_type == null) {
				int type = find_type(attribute);
				if (type >= 4 || type == 2) {
					if (val_object instanceof Integer) {
						val_int = ((Integer)val_object).intValue();
						switch(type) {
							case 2:
								if (val_int == 0) {
									return 0;
								} else {
									return 2;
								}
							case 4:
								if (val_int == Integer.MIN_VALUE) {
									return 0;
								} else {
									return 2;
								}
							case 5:
								if (val_int == 0) {
									return 0;
								} else {
									return 4;
								}
						}
					} else {
						throw new SdaiException(SdaiException.VT_NVLD);
					}
				} else if (type == 3) {
					if (val_object instanceof Double) {
						double val_double = ((Double)val_object).doubleValue();
						if (Double.isNaN(val_double)) {
							return 0;
						} else {
							return 3;
						}
					} else {
						throw new SdaiException(SdaiException.VT_NVLD);
					}
				} else {
					return 1;
				}
			} else if (sel_type.is_mixed == 0) {
				return 1;
			} else {
				throw new SdaiException(SdaiException.FN_NAVL,
 					"Derived attributes of select type are not supported.");
			}
		}

/*		if (attribute instanceof CExplicit_attribute) {
			CExplicit_attribute expl_attr = (CExplicit_attribute)attribute;
			if (expl_attr.testRedeclaring(null)) {
				EExplicit_attribute redeclared_attr = expl_attr.getRedeclaring(null);
				return testAttribute(redeclared_attr, select);
			}
		}*/
		return -1;
//		} // syncObject
	}


// not in SDAI, similar to SdaiModel.getEntityDefinition(String entitName);
	public EAttribute getAttributeDefinition(String attributeName) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (attributeName == null) {
			throw new SdaiException(SdaiException.AT_NDEF);
		}
		CEntityDefinition edef = (CEntityDefinition)getInstanceType();
		EAttribute attr = edef.extract_attribute(attributeName);
		if (attr != null) {
			return attr;
		} else {
			throw new SdaiException(SdaiException.AT_NVLD);
		}
//		} // syncObject
	}


//	Returns entity attribute for a given entity definition and attribute name in
//	the case when the entity is complex. If an attribute with the submitted name is
//	not found, then <code>null</code> value is returned.
//	This method is invoked in <code>getAttributeDefinition</code>.
/*	private EAttribute extract_attribute_for_complex(CEntity_definition edef, String attributeName)
			throws SdaiException {
		CEntity_definition [] types = ((CEntityDefinition)edef).partialEntityTypes;
		for (int i = 0; i < types.length; i++) {
			EAttribute at = extract_attribute(types[i], attributeName, 1);
			if (at != null) {
				return at;
			}
		}
		return null;
	}*/


	public SdaiModel findEntityInstanceSdaiModel() throws SdaiException {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NVLD, "Missing owning model for instance " + toString());
		}
		return owning_model;
	}


	public boolean isInstanceOf(EEntity_definition type) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (type == null) {
			throw new SdaiException(SdaiException.ED_NDEF);
		}
		CEntity_definition def = (CEntity_definition)getInstanceType();
		if (def == null) {
			throw new SdaiException(SdaiException.SY_ERR);
		} else if (def == (CEntity_definition)type) {
			return true;
		} else {
			return false;
		}
//		} // syncObject
	}


	public boolean isInstanceOf(Class type) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (this.getClass() == type) {
			return true;
		} else {
			return false;
		}
//		} // syncObject
	}


	public boolean isKindOf(EEntity_definition type) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (type == null) {
			throw new SdaiException(SdaiException.ED_NDEF);
		}
		CEntity_definition my_def = (CEntity_definition)getInstanceType();
		if (((CEntity_definition)type).complex == 2) {
			return my_def.isSubtypeOf(type);
		}
		Class top_interface = ((CEntityDefinition)type).getEntityInterface();
		if (my_def.complex == 2) {
			return check_if_complex_is_subtype(top_interface, my_def);
		}
		Class bottom_interface = ((CEntityDefinition)my_def).getEntityInterface();
		return top_interface.isAssignableFrom(bottom_interface);
//		} // syncObject
	}


/**
	Checks if the type (described by CEntity_definition) of an entity
 	instance coincides with the given type (specified by the first parameter)
	or is a subtype of it in the case when the entity being checked is complex.
	This method is invoked in <code>isKindOf(EEntity_definition type)</code>.
*/
	private boolean check_if_complex_is_subtype(Class top_interface, CEntity_definition edef_complex)
			throws SdaiException {
		CEntity_definition [] types = ((CEntityDefinition)edef_complex).partialEntityTypes;
		for (int i = 0; i < types.length; i++) {
			Class bottom_interface = ((CEntityDefinition)types[i]).getEntityInterface();
			boolean is_subtype = top_interface.isAssignableFrom(bottom_interface);
			if (is_subtype) {
				return true;
			}
		}
		return false;
	}


	public boolean isKindOf(Class type) throws SdaiException {
//		synchronized (syncObject) {
		if (type == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		CEntity_definition def = owning_model.underlying_schema.modelDictionary.schemaData.findEntity(type);
		if (def == null) {
			def = SdaiSession.systemRepository.getEntityDefinition(type);
		}
		return isKindOf(def);
//		} // syncObject
	}


	public final void findEntityInstanceUsers(ASdaiModel modelDomain, AEntity result)
			throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (result == null) {
			throw new SdaiException(SdaiException.AI_NEXS);
		}
		if (result.myType != null) {
			throw new SdaiException(SdaiException.AI_NVLD, result);
		}
		if (owning_model.optimized) {
			findEntityInstanceUsersNoInverse(modelDomain, result);
			return;
		}
		EEntity_definition definition = getInstanceType();
		if(modelDomain == null) {
			owning_model.provideInstancesForUsersIfNeeded(definition, null, false);
		} else {
			boolean putToMap = modelDomain.getMemberCount() > 1;
			Map userAttribMap = null;
			for(SdaiIterator i = modelDomain.createIterator(); i.next(); ) {
				SdaiModel userModel = modelDomain.getCurrentMember(i);
				if((userModel.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.NO_ACCESS) {
					if(userModel.underlying_schema == null) {
						userModel.getUnderlyingSchema();
					}
					userAttribMap = userModel
						.provideInstancesForUsersIfNeeded(definition, userAttribMap, putToMap);
				}
			}
		}
		Object o = inverseList;
		while (o != null) {
			if (o instanceof Inverse) {
				Inverse inv = (Inverse) o;
				findEntityInstanceUsers2((CEntity) inv.value, result, modelDomain);
				o = inv.next;
			} else {
				findEntityInstanceUsers2((CEntity) o, result, modelDomain);
				o = null;
			}
		}
//		} // syncObject
	}


	public final int findEntityInstanceUserCount(ASdaiModel modelDomain)
			throws SdaiException {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (owning_model.optimized) {
			return findEntityInstanceUserCountNoInverse(modelDomain);
		}
		int count = 0;
		StaticFields staticFields = StaticFields.get();
		Object o = inverseList;
		while (o != null) {
			if (o instanceof Inverse) {
				Inverse inv = (Inverse) o;
				count = increaseInstanceUserCount(count, (CEntity)inv.value, modelDomain, staticFields);
				o = inv.next;
			} else {
				count = increaseInstanceUserCount(count, (CEntity)o, modelDomain, staticFields);
				o = null;
			}
		}
		return count;
	}


/**
	Adds the submitted instance to the resulting set provided its owning
	model either belongs to the specified set of models (if it is nonempty)
	or coincides with the owning model of this instance (if the set is empty).
	This method is invoked in <code>findEntityInstanceUsers</code>.
*/
	private final void findEntityInstanceUsers2(CEntity inst,
			AEntity result, ASdaiModel domain) throws SdaiException {
		SdaiModel model = inst.owning_model;
		if (domain == null) {
			if (owning_model == model) {
//				if (result.myType == null || result.myType instanceof CList_type) {
					result.addAtTheEnd(inst, null);
//				} else {
//					result.setForNonList(inst, result.myLength, null, null);
//				}
			}
			return;
		}
		if (domain.myLength == 0) {
			result.addAtTheEnd(inst, null);
			return;
		}
		StaticFields staticFields = StaticFields.get();
		if (staticFields.it == null) {
			staticFields.it = domain.createIterator();
		} else {
			domain.attachIterator(staticFields.it);
		}
		while (staticFields.it.next()) {
			if (domain.getCurrentMemberObject(staticFields.it) == model) {
//				if (result.myType == null || result.myType instanceof CList_type) {
					result.addAtTheEnd(inst, null);
//				} else {
//					result.setForNonList(inst, result.myLength, null, null);
//				}
				return;
			}
		}
	}


	private final int increaseInstanceUserCount(int count, CEntity inst,
			ASdaiModel domain, StaticFields staticFields) throws SdaiException {
		SdaiModel model = inst.owning_model;
		if (domain == null) {
			if (owning_model == model) {
					count++;
			}
			return count;
		}
		if (domain.myLength == 0) {
			count++;
			return count;
		}
		if (staticFields.it == null) {
			staticFields.it = domain.createIterator();
		} else {
			domain.attachIterator(staticFields.it);
		}
		while (staticFields.it.next()) {
			if (domain.getCurrentMemberObject(staticFields.it) == model) {
				count++;
				return count;
			}
		}
		return count;
	}


	private void findEntityInstanceUsersNoInverse(ASdaiModel modelDomain, AEntity result) 
			throws SdaiException {
		int i, j;
		SdaiModel model;
		CEntityDefinition entityDef;

		StaticFields staticFields = StaticFields.get();
		staticFields.context_schema = owning_model.underlying_schema;
		CEntity_definition this_def = (CEntity_definition)getInstanceType();
		ASdaiModel act_models = owning_model.repository.session.active_models;
		for (i = 0; i < act_models.myLength; i++) {
			model = (SdaiModel)act_models.myData[i];
			if (model.repository == SdaiSession.systemRepository) {
				continue;
			}
			if (modelDomain == null || modelDomain.myLength == 0) {
				if (modelDomain == null && owning_model != model) {
					continue;
				}
			} else {
				if (staticFields.it == null) {
					staticFields.it = modelDomain.createIterator();
				} else {
					modelDomain.attachIterator(staticFields.it);
				}
				boolean ignore_model = true;
				while (staticFields.it.next()) {
					if (modelDomain.getCurrentMemberObject(staticFields.it) == model) {
						ignore_model = false;
						break;
					}
				}
				if (ignore_model) {
					continue;
				}
			}
			SchemaData sch_data = model.underlying_schema.owning_model.schemaData;
			if (sch_data.involved == 0) {
				continue;
			}
			if (sch_data.involved > 0) {
				for (j = 0; j < sch_data.involved; j++) {
					entityDef = sch_data.entities[sch_data.aux[j]];
					model.scanForUsers(staticFields, this, entityDef, sch_data.aux[j], result);
				}
				continue;
			}
			sch_data.involved = 0;
			for (j = 0; j < sch_data.noOfEntityDataTypes; j++) {
				entityDef = sch_data.entities[j];
				CExplicit_attribute [] attributes = entityDef.attributes;
				if (attributes == null) {
					continue;
				}
				boolean found = false;
				for (int k = 0; k < attributes.length; k++) {
					if (entityDef.fieldOwners[k] == null) {
						continue;
					}
					found = ((AttributeDefinition)attributes[k]).search_attribute(this_def);
					if (found) {
						break;
					}
				}
//System.out.println("CEntity  checked entity: " + ((CEntity_definition)entityDef).getName(null) + "  found: " + found);
				if (found) {
					if (staticFields.entity_values == null) {
						staticFields.entity_values = new ComplexEntityValue();
					}
					sch_data.aux[sch_data.involved] = j;
					sch_data.involved++;
					model.scanForUsers(staticFields, this, entityDef, j, result);
				}
			}
		}

		for (i = 0; i < act_models.myLength; i++) {
			model = (SdaiModel)act_models.myData[i];
			if (model.repository == SdaiSession.systemRepository) {
				continue;
			}
			model.underlying_schema.owning_model.schemaData.involved = -1;
		}
		staticFields.context_schema = null;
	}


	private int findEntityInstanceUserCountNoInverse(ASdaiModel modelDomain) 
			throws SdaiException {
		int i, j;
		SdaiModel model;
		CEntityDefinition entityDef;
		int count = 0;

		StaticFields staticFields = StaticFields.get();
		staticFields.context_schema = owning_model.underlying_schema;
		CEntity_definition this_def = (CEntity_definition)getInstanceType();
		ASdaiModel act_models = owning_model.repository.session.active_models;
		for (i = 0; i < act_models.myLength; i++) {
			model = (SdaiModel)act_models.myData[i];
			if (model.repository == SdaiSession.systemRepository) {
				continue;
			}
			if (modelDomain == null || modelDomain.myLength == 0) {
				if (modelDomain == null && owning_model != model) {
					continue;
				}
			} else {
				if (staticFields.it == null) {
					staticFields.it = modelDomain.createIterator();
				} else {
					modelDomain.attachIterator(staticFields.it);
				}
				boolean ignore_model = true;
				while (staticFields.it.next()) {
					if (modelDomain.getCurrentMemberObject(staticFields.it) == model) {
						ignore_model = false;
						break;
					}
				}
				if (ignore_model) {
					continue;
				}
			}
			SchemaData sch_data = model.underlying_schema.owning_model.schemaData;
			if (sch_data.involved == 0) {
				continue;
			}
			if (sch_data.involved > 0) {
				for (j = 0; j < sch_data.involved; j++) {
					entityDef = sch_data.entities[sch_data.aux[j]];
					count = model.scanForUserCount(staticFields, this, entityDef, sch_data.aux[j], count);
				}
				continue;
			}
			sch_data.involved = 0;
			for (j = 0; j < sch_data.noOfEntityDataTypes; j++) {
				entityDef = sch_data.entities[j];
				CExplicit_attribute [] attributes = entityDef.attributes;
				if (attributes == null) {
					continue;
				}
				boolean found = false;
				for (int k = 0; k < attributes.length; k++) {
					if (entityDef.fieldOwners[k] == null) {
						continue;
					}
					found = ((AttributeDefinition)attributes[k]).search_attribute(this_def);
					if (found) {
						break;
					}
				}
//System.out.println("CEntity  checked entity: " + ((CEntity_definition)entityDef).getName(null) + "  found: " + found);
				if (found) {
					if (staticFields.entity_values == null) {
						staticFields.entity_values = new ComplexEntityValue();
					}
					sch_data.aux[sch_data.involved] = j;
					sch_data.involved++;
					count = model.scanForUserCount(staticFields, this, entityDef, j, count);
				}
			}
		}

		for (i = 0; i < act_models.myLength; i++) {
			model = (SdaiModel)act_models.myData[i];
			if (model.repository == SdaiSession.systemRepository) {
				continue;
			}
			model.underlying_schema.owning_model.schemaData.involved = -1;
		}
		staticFields.context_schema = null;
		return count;
	}


	void search_for_user(StaticFields staticFields, CEntity target, CEntityDefinition def, AEntity users)
			throws SdaiException {
		owning_model.prepareAll(staticFields.entity_values, (CEntity_definition)def);
		getAll(staticFields.entity_values);
		for (int i = 0; i < def.noOfPartialEntityTypes; i++) {
			EntityValue ent_val = staticFields.entity_values.entityValues[i];
			for (int j = 0; j < ent_val.count; j++) {
				ent_val.values[j].find_user(this, target, users);
			}
		}
	}


	int search_for_user_count(StaticFields staticFields, CEntity target, CEntityDefinition def, int count)
			throws SdaiException {
		owning_model.prepareAll(staticFields.entity_values, (CEntity_definition)def);
		getAll(staticFields.entity_values);
		for (int i = 0; i < def.noOfPartialEntityTypes; i++) {
			EntityValue ent_val = staticFields.entity_values.entityValues[i];
			for (int j = 0; j < ent_val.count; j++) {
				count = ent_val.values[j].find_user_count(this, target, count);
			}
		}
		return count;
	}


	void search_for_ref_change(StaticFields staticFields, CEntity old, CEntity newer, 
			CEntityDefinition def, boolean save4undo) throws SdaiException {

		owning_model.prepareAll(staticFields.entity_values2, (CEntity_definition)def);
		getAll(staticFields.entity_values2);
		boolean found = false;
		for (int i = 0; i < def.noOfPartialEntityTypes; i++) {
			EntityValue ent_val = staticFields.entity_values2.entityValues[i];
			for (int j = 0; j < ent_val.count; j++) {
				found = ent_val.values[j].user_exists(old);
				if (found) {
					break;
				}
			}
			if (found) {
				break;
			}
		}
		if (found) {
			if (save4undo) {
				owning_model.repository.session.undoRedoModifyPrepare(this);
			}
			if (owning_model != null && owning_model.mode == SdaiModel.READ_ONLY) {
				String base = SdaiSession.line_separator + AdditionalMessages.EI_ROMD + 
				SdaiSession.line_separator + AdditionalMessages.RD_MODL + owning_model.name + 
				SdaiSession.line_separator + AdditionalMessages.RD_INST + instance_identifier;
				throw new SdaiException(SdaiException.MX_NRW, base);
			}
			changeReferences(old, newer);
			modified();
		}
	}


	void if_referencing_mark_as_modified(StaticFields staticFields, CEntity target, 
			CEntityDefinition def) throws SdaiException {
		owning_model.prepareAll(staticFields.entity_values, (CEntity_definition)def);
		getAll(staticFields.entity_values);
		for (int i = 0; i < def.noOfPartialEntityTypes; i++) {
			EntityValue ent_val = staticFields.entity_values.entityValues[i];
			for (int j = 0; j < ent_val.count; j++) {
				if (ent_val.values[j].user_exists(target)) {
					modified();
					return;
				}
			}
		}
	}


	void search_for_unset(StaticFields staticFields, CEntity target, 
			CEntityDefinition def, boolean replace_by_connector) throws SdaiException {
		
		owning_model.prepareAll(staticFields.entity_values, (CEntity_definition)def);
		getAll(staticFields.entity_values);
		boolean found = false;
		for (int i = 0; i < def.noOfPartialEntityTypes; i++) {
			EntityValue ent_val = staticFields.entity_values.entityValues[i];
			for (int j = 0; j < ent_val.count; j++) {
				found = ent_val.values[j].user_exists(target);
				if (found) {
					break;
				}
			}
			if (found) {
				break;
			}
		}
		if (found) {
			if (replace_by_connector) {
				SdaiModel.Connector con = owning_model.newConnector(target.owning_model, 
					((CEntityDefinition)target.getInstanceType()).getCorrectName(), 
					target.instance_identifier, this);
				changeReferences(target, con);
			} else {
				changeReferences(target, null);
			}
		}
	}


	Value search_for_check(StaticFields staticFields, CEntity target, 
			CEntityDefinition owner_def, CEntity_definition new_def) throws SdaiException {
		owning_model.prepareAll(staticFields.entity_values, (CEntity_definition)owner_def);
		getAll(staticFields.entity_values);
		for (int i = 0; i < owner_def.noOfPartialEntityTypes; i++) {
			EntityValue ent_val = staticFields.entity_values.entityValues[i];
			if (ent_val == null) {
				continue;
			}
			CEntity_definition p_def = ent_val.def;
			CExplicit_attribute[] expl_attrs = ((CEntityDefinition)p_def).takeExplicit_attributes();
			for (int j = 0; j < p_def.noOfPartialAttributes; j++) {
				Value val = ent_val.values[j];
				Value res = val.examine_value(this, target, new_def, (DataType)expl_attrs[j].getDomain(null), expl_attrs[j]);
				if (res != null) {
					return res;
				} 
			}
		}
		return null;
	}


	void search_for_ref_move(StaticFields staticFields, CEntity old, CEntity newer, 
			CEntityDefinition def, boolean save4undo) throws SdaiException {
		
		owning_model.prepareAll(staticFields.entity_values, (CEntity_definition)def);
		getAll(staticFields.entity_values);
		boolean found = false;
		for (int i = 0; i < def.noOfPartialEntityTypes; i++) {
			EntityValue ent_val = staticFields.entity_values.entityValues[i];
			for (int j = 0; j < ent_val.count; j++) {
				found = ent_val.values[j].user_exists(old);
				if (found) {
					break;
				}
			}
			if (found) {
				break;
			}
		}
		if (found) {
			if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
				throw new SdaiException(SdaiException.MX_NRW, owning_model);
			}
			if (save4undo) {
				owning_model.repository.session.undoRedoModifyPrepare(this);
			}
			old.instance_identifier = -old.instance_identifier;
			newer.instance_identifier = -newer.instance_identifier;
			changeReferences(old, newer);
			newer.instance_identifier = -newer.instance_identifier;
			if (old.instance_identifier<0) {
				old.instance_identifier = -old.instance_identifier;
			}
		}
	}


	boolean search_if_referenced(StaticFields staticFields, CEntity role_value, 
			CEntityDefinition def) throws SdaiException {
		
		owning_model.prepareAll(staticFields.entity_values, (CEntity_definition)def);
		getAll(staticFields.entity_values);
		boolean found = false;
		for (int i = 0; i < def.noOfPartialEntityTypes; i++) {
			EntityValue ent_val = staticFields.entity_values.entityValues[i];
			for (int j = 0; j < ent_val.count; j++) {
				found = ent_val.values[j].user_exists(role_value);
				if (found) {
					return true;
				}
			}
		}
		return false;
	}


	public void findEntityInstanceUsedin(EAttribute role,
			ASdaiModel modelDomain, AEntity result) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (role == null) {
			throw new SdaiException(SdaiException.AT_NDEF);
		}
		if (result == null) {
			throw new SdaiException(SdaiException.AI_NEXS);
		}
		if (result.myType != null) {
			throw new SdaiException(SdaiException.AI_NVLD, result);
		}
		if (owning_model.optimized) {
			findEntityInstanceUsedinNoInverse(role, modelDomain, result);
			return;
		}
		provideInstancesInsideUsedin(role, modelDomain);
		Object o = inverseList;
		CEntity inst;
		while (o != null) {
			if (o instanceof Inverse) {
				Inverse inv = (Inverse) o;
				inst = (CEntity)inv.value;
				//if (inst.instance_position >= 0) {
                if ((inst.instance_position & POS_MASK) != POS_MASK) {    //--VV--
					makeUsedin2(inst, role, result, modelDomain, false);
					//inst.instance_position = -1;
                    inst.instance_position = (inst.instance_position & FLG_MASK) | POS_MASK;    //--VV--
				}
				o = inv.next;
			} else {
				inst = (CEntity)o;
				//if (inst.instance_position >= 0) {
                if ((inst.instance_position & POS_MASK) != POS_MASK) {  //--VV--
					makeUsedin2(inst, role, result, modelDomain, false);
				}
				o = null;
			}
		}
		o = inverseList;
		while (o != null) {
			if (o instanceof Inverse) {
				Inverse inv = (Inverse) o;
				//((CEntity)inv.value).instance_position = 0;
                ((CEntity)inv.value).instance_position = ((CEntity)inv.value).instance_position & FLG_MASK; //--VV--
				o = inv.next;
			} else {
				o = null;
			}
		}
//		} // syncObject
	}


	private void findEntityInstanceUsedinNoInverse(EAttribute role,
			ASdaiModel modelDomain, AEntity result) throws SdaiException {
		int i, j, k;
		SdaiModel model;
		CEntityDefinition entityDef;
		int [] aux2;

		ASdaiModel act_models = owning_model.repository.session.active_models;
		for (i = 0; i < act_models.myLength; i++) {
			model = (SdaiModel)act_models.myData[i];
			if (model.repository == SdaiSession.systemRepository) {
				continue;
			}
			if (modelDomain == null || modelDomain.myLength == 0) {
				if (modelDomain == null && owning_model != model) {
					continue;
				}
			} else {
				StaticFields staticFields = StaticFields.get();
				if (staticFields.it == null) {
					staticFields.it = modelDomain.createIterator();
				} else {
					modelDomain.attachIterator(staticFields.it);
				}
				boolean ignore_model = true;
				while (staticFields.it.next()) {
					if (modelDomain.getCurrentMemberObject(staticFields.it) == model) {
						ignore_model = false;
						break;
					}
				}
				if (ignore_model) {
					continue;
				}
			}
			SchemaData sch_data = model.underlying_schema.owning_model.schemaData;
			if (sch_data.involved == 0) {
				continue;
			}
			if (sch_data.involved > 0) {
				for (j = 0; j < sch_data.involved; j++) {
					entityDef = sch_data.entities[sch_data.aux[j]];
					model.scanEntityInstances(this, entityDef, sch_data.aux[j], sch_data.aux2[j], result);
				}
				continue;
			}
			sch_data.involved = 0;
			CEntityDefinition attr_owner = null;
			if (((AttributeDefinition)role).attr_tp == AttributeDefinition.EXPLICIT) {
				attr_owner = (CEntityDefinition)((CExplicit_attribute)role).getParent_entity(null);
			} else {
				continue;
			}
			int index = sch_data.find_entity(0, sch_data.noOfEntityDataTypes - 1, attr_owner);
			if (index < 0) {
				continue;
			}
			int subtypes [] = sch_data.schema.getSubtypes(index);
			int subt_ind;
			aux2 = sch_data.getAuxiliaryArray();
			for (j = -1; j < subtypes.length; j++) {
				if (j < 0) {
					subt_ind = index;
				} else {
					subt_ind = subtypes[j];
				}
				entityDef = sch_data.entities[subt_ind];
				CExplicit_attribute [] attributes = entityDef.attributes;
				if (attributes == null) {
					continue;
				}
				boolean found = false;
				for (k = 0; k < attributes.length; k++) {
					if (attributes[k] == role) {
						found = true;
						break;
					}
				}
				if (found) {
					sch_data.aux[sch_data.involved] = subt_ind;
					aux2[sch_data.involved] = k;
					sch_data.involved++;
					model.scanEntityInstances(this, entityDef, subt_ind, k, result);
					continue;
				}
				for (int m = 0; m < entityDef.noOfPartialEntityTypes; m++) {
					CEntityDefinition entity;
					if (entityDef.complex == 2) {
						entity = entityDef.partialEntityTypes[m];
					} else {
						entity = entityDef.partialEntityTypes[entityDef.externalMappingIndexing[m]];
					}
					boolean processed = false;
					for (k = 0; k < entity.attributesRedecl.length; k++) {
						EAttribute attrib = entity.attributesRedecl[k];
						if (attrib != role) {
							continue;
						}
						processed = true;
						int attr_index = GetHeadAttribute(entityDef, (CExplicit_attribute)attrib);
						if (attr_index < 0) {
							break;
						}
						sch_data.aux[sch_data.involved] = subt_ind;
						aux2[sch_data.involved] = attr_index;
						sch_data.involved++;
						model.scanEntityInstances(this, entityDef, subt_ind, attr_index, result);
						break;
					}
					if (processed) {
						break;
					}
				}
			}
		}

		for (i = 0; i < act_models.myLength; i++) {
			model = (SdaiModel)act_models.myData[i];
			if (model.repository == SdaiSession.systemRepository) {
				continue;
			}
			model.underlying_schema.owning_model.schemaData.involved = -1;
		}
	}


	public int getAttributeValueBound(EAttribute attribute) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


	public void findInstanceRoles(ASdaiModel modelDomain, AAttribute result)
			throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (result == null) {
			throw new SdaiException(SdaiException.AI_NEXS);
		}
		if (((AEntity)result).myType != null) {
			throw new SdaiException(SdaiException.AI_NVLD, result);
		}
		ListElement l_el = new ListElement(null);
		int f_index;
		if (owning_model.optimized) {
			f_index = findInstanceRolesNoInverse(modelDomain, result, l_el);
		} else {
			f_index = -1;
			CEntity inst;
			CEntityDefinition entityDef;
			Inverse inv;
			Object o = inverseList;
			while (o != null) {
				if (o instanceof Inverse) {
					inv = (Inverse) o;
					inst = (CEntity)inv.value;
					if ((inst.instance_position & POS_MASK) != POS_MASK) {  //--VV--
						entityDef = (CEntityDefinition)inst.getInstanceType();
						f_index = inst.findInstanceRoles2(this, entityDef, result, modelDomain, l_el, f_index);
						inst.instance_position = (inst.instance_position & FLG_MASK) | POS_MASK;    //--VV--
					}
					o = inv.next;
				} else {
					inst = (CEntity)o;
					if ((inst.instance_position & POS_MASK) != POS_MASK) {  //--VV--
						entityDef = (CEntityDefinition)inst.getInstanceType();
						f_index = inst.findInstanceRoles2(this, entityDef, result, modelDomain, l_el, f_index);
					}
					o = null;
				}
			}
			o = inverseList;
			while (o != null) {
				if (o instanceof Inverse) {
					inv = (Inverse) o;
					inst = (CEntity)inv.value;
				//inst.instance_position = 0;
					inst.instance_position = inst.instance_position & FLG_MASK; //--VV--
					o = inv.next;
				} else {
					o = null;
				}
			}
		}
		if (f_index < 0) {
			return;
		}
		f_index--;
		ListElement element;
		if (l_el.next != null) {
			element = l_el.next;
			while (element != null) {
				if (((AttributeDefinition)element.object).attr_tp == AttributeDefinition.EXPLICIT) {
					((AttributeDefinition)element.object).selected_as_role = false;
				}
				element = element.next;
			}
		} else {
			Object [] myDataA;
			AEntity res = (AEntity)result;
			if (res.myLength == 1) {
				if (((AttributeDefinition)res.myData).attr_tp == AttributeDefinition.EXPLICIT) {
					((AttributeDefinition)res.myData).selected_as_role = false;
				}
			} else if (res.myLength == 2) {
				myDataA = (Object [])res.myData;
				if (((AttributeDefinition)myDataA[f_index]).attr_tp == AttributeDefinition.EXPLICIT) {
					((AttributeDefinition)myDataA[f_index]).selected_as_role = false;
				}
				if (f_index == 0 && ((AttributeDefinition)myDataA[1]).attr_tp == AttributeDefinition.EXPLICIT) {
					((AttributeDefinition)myDataA[1]).selected_as_role = false;
				}
			} else {
				if (res.myLength <= CAggregate.SHORT_AGGR) {
					element = (ListElement)res.myData;
				} else {
					myDataA = (Object [])res.myData;
					element = (ListElement)myDataA[0];
				}
				while (f_index-- > 0) {
					element = element.next;
				}
				while (element != null) {
					if (((AttributeDefinition)element.object).attr_tp == AttributeDefinition.EXPLICIT) {
						((AttributeDefinition)element.object).selected_as_role = false;
					}
					element = element.next;
				}
			}
		}
//		} // syncObject
	}


	private int findInstanceRolesNoInverse(ASdaiModel modelDomain, AAttribute result, ListElement l_el)
			throws SdaiException {
		int i, j;
		SdaiModel model;
		CEntityDefinition entityDef;
		CEntity [] ref_instances = null;
		int ref_inst_count = 0;

		int [] param = new int[2];
		int f_index = -1;
		StaticFields staticFields = StaticFields.get();
		staticFields.context_schema = owning_model.underlying_schema;
		CEntity_definition this_def = (CEntity_definition)getInstanceType();
		ASdaiModel act_models = owning_model.repository.session.active_models;
		for (i = 0; i < act_models.myLength; i++) {
			model = (SdaiModel)act_models.myData[i];
			if (model.repository == SdaiSession.systemRepository) {
				continue;
			}
			if (modelDomain == null || modelDomain.myLength == 0) {
				if (modelDomain == null && owning_model != model) {
					continue;
				}
			} else {
				if (staticFields.it == null) {
					staticFields.it = modelDomain.createIterator();
				} else {
					modelDomain.attachIterator(staticFields.it);
				}
				boolean ignore_model = true;
				while (staticFields.it.next()) {
					if (modelDomain.getCurrentMemberObject(staticFields.it) == model) {
						ignore_model = false;
						break;
					}
				}
				if (ignore_model) {
					continue;
				}
			}
			SchemaData sch_data = model.underlying_schema.owning_model.schemaData;
			if (sch_data.involved == 0) {
				continue;
			}
			if (sch_data.involved > 0) {
				for (j = 0; j < sch_data.involved; j++) {
					entityDef = sch_data.entities[sch_data.aux[j]];
					ref_instances = model.scanForRoles(staticFields, this, sch_data.aux[j], entityDef, result, 
						modelDomain, l_el, f_index, ref_instances, param, ref_inst_count);
					f_index = param[0];
					ref_inst_count = param[1];
				}
				continue;
			}
			sch_data.involved = 0;
			for (j = 0; j < sch_data.noOfEntityDataTypes; j++) {
				entityDef = sch_data.entities[j];
				CExplicit_attribute [] attributes = entityDef.attributes;
				if (attributes == null) {
					continue;
				}
				boolean found = false;
				for (int k = 0; k < attributes.length; k++) {
					if (entityDef.fieldOwners[k] == null) {
						continue;
					}
					found = ((AttributeDefinition)attributes[k]).search_attribute(this_def);
					if (found) {
						break;
					}
				}
				if (found) {
					if (staticFields.entity_values == null) {
						staticFields.entity_values = new ComplexEntityValue();
					}
					sch_data.aux[sch_data.involved] = j;
					sch_data.involved++;
					ref_instances = model.scanForRoles(staticFields, this, j, entityDef, result, 
						modelDomain, l_el, f_index, ref_instances, param, ref_inst_count);
					f_index = param[0];
					ref_inst_count = param[1];
				}
			}
		}

		for (i = 0; i < act_models.myLength; i++) {
			model = (SdaiModel)act_models.myData[i];
			if (model.repository == SdaiSession.systemRepository) {
				continue;
			}
			model.underlying_schema.owning_model.schemaData.involved = -1;
		}
		staticFields.context_schema = null;

		for (i = 0; i < ref_inst_count; i++) {
			ref_instances[i].instance_position = ref_instances[i].instance_position & FLG_MASK;
		}
		return f_index;
	}


/**
	Searches for attributes by which the submitted instance references the
	current instance provided the owning model of the submitted instance
	either belongs to the specified set of models (if it is nonempty)
	or coincides with the owning model of this instance (if the set is empty).
	This method is invoked in <code>findInstanceRoles</code>.
*/
	int findInstanceRoles2(CEntity referenced, CEntityDefinition edef,
			AAttribute result, ASdaiModel domain, ListElement l_el, int f_index) throws SdaiException {
		SdaiModel model = referenced.owning_model;
		if (domain == null) {
			if (owning_model == model) {
				f_index = find_roles(edef, referenced, result, l_el, f_index);
			}
			return f_index;
		}
		StaticFields staticFields = StaticFields.get();
		if (staticFields.it == null) {
			staticFields.it = domain.createIterator();
		} else {
			domain.attachIterator(staticFields.it);
		}
		while (staticFields.it.next()) {
			if (domain.getCurrentMemberObject(staticFields.it) == owning_model) {
				f_index = find_roles(edef, referenced, result, l_el, f_index);
				break;
			}
		}
		return f_index;
	}
/*	private void findInstanceRoles2(CEntity inst, AAttribute result, ASdaiModel domain) throws SdaiException {
		SdaiModel model = inst.owning_model;
		if (domain == null) {
			if (owning_model == model) {
				inst.FindRoles(this, result);
			}
			return;
		}
		if (it == null) {
			it = domain.createIterator();
		} else {
			domain.attachIterator(it);
		}
		while (it.next()) {
			if (domain.getCurrentMemberObject(it) == model) {
//		for (int j = 0; j < domain.myLength; j++) {
//			if (domain.getByIndex(j + 1) == model) {
				inst.FindRoles(this, result);
				break;
			}
		}
	}*/


	int find_roles(CEntityDefinition edef, CEntity inst, AAttribute result, ListElement l_el, int f_index) throws SdaiException {
		int i;
		SSuper ssuper;
		Object value;
		for (i = 0; i < edef.attributes.length; i++) {
			AttributeDefinition attr = (AttributeDefinition)edef.attributes[i];
			if (attr.selected_as_role || edef.fieldOwners[i] == null) {
				continue;
			}
			if (owning_model.described_schema == null) {
				ssuper = edef.fieldOwners[i].ssuper;
			} else {
				ssuper = edef.ssuper;
			}
			try {
				value = ssuper.getObject(this, edef.attributeFields[i]);
			} catch (Exception ex) {
				throw new SdaiException(SdaiException.SY_ERR, ex);
			}
			if (value == inst || (value instanceof CAggregate && ((CAggregate)value).is_referencing(inst)) ) {
				((AEntity)result).addAtTheEnd(attr, null);
				attr.selected_as_role = true;
				if (f_index < 0) {
					f_index = ((AEntity)result).myLength;
					if (f_index > CAggregate.SHORT_AGGR) {
						Object [] myDataA = (Object [])((AEntity)result).myData;
						l_el.next = (ListElement)myDataA[1];
					}
				}
			}
		}

		CEntityDefinition entity;
		for (int j = 0; j < edef.noOfPartialEntityTypes; j++) {
			if (edef.complex == 2) {
				entity = edef.partialEntityTypes[j];
			} else {
				entity = edef.partialEntityTypes[edef.externalMappingIndexing[j]];
			}
			for (i = 0; i < entity.attributesRedecl.length; i++) {
				EAttribute attrib = entity.attributesRedecl[i];
				if (((AttributeDefinition)attrib).selected_as_role) {
					continue;
				}
				if (((AttributeDefinition)attrib).attr_tp != AttributeDefinition.EXPLICIT) {
					continue;
				}
				int ind = GetHeadAttribute(edef, (CExplicit_attribute)attrib);
				if (ind < 0) {
					continue;
				}
				if (owning_model.described_schema == null) {
					ssuper = edef.fieldOwners[ind].ssuper;
				} else {
					ssuper = edef.ssuper;
				}
				try {
					value = ssuper.getObject(this, edef.attributeFields[ind]);
				} catch (java.lang.IllegalAccessException ex) {
					throw new SdaiException(SdaiException.SY_ERR, ex);
				}
				if (value == inst || (value instanceof CAggregate && ((CAggregate)value).is_referencing(inst)) ) {
					((AEntity)result).addAtTheEnd(attrib, null);
					((AttributeDefinition)attrib).selected_as_role = true;
					if (f_index < 0) {
						f_index = ((AEntity)result).myLength;
						if (f_index > CAggregate.SHORT_AGGR) {
							Object [] myDataA = (Object [])((AEntity)result).myData;
							l_el.next = (ListElement)myDataA[1];
						}
					}
				}
			}
		}

		return f_index;
	}


	public void findInstanceDataTypes(ANamed_type result,  ESchema_definition schema)
			throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


	public EEntity copyApplicationInstance(SdaiModel target_model) throws SdaiException {
//		synchronized (syncObject) {
		if (target_model == null) {
			throw new SdaiException(SdaiException.MO_NEXS);
		}
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		SdaiSession session = target_model.repository.session;
		if (session.active_transaction.mode != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.TR_NRW, session.active_transaction);
		}
		if ((target_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, target_model);
		}
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		CEntity_definition def = (CEntity_definition)getInstanceType();
		SchemaData sch_data = target_model.underlying_schema.modelDictionary.schemaData;
		int index_to_entity = sch_data.find_entity(0, sch_data.noOfEntityDataTypes - 1,
//			def.getNameUpperCase());
			(CEntityDefinition)def);
		if (index_to_entity < 0) {
			throw new SdaiException(SdaiException.ED_NVLD, def);

		}
		boolean mod_state = target_model.modified;
		CEntity inst = target_model.makeEntityInstance(def, index_to_entity);
		inst.copy_values(StaticFields.get(), this, def, null, owning_model);
		session.undoRedoCreatePrepare(inst, mod_state);
		return inst;
//		} // syncObject
	}


	public void deleteApplicationInstance() throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.active_transaction.mode != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.TR_NRW, session.active_transaction);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		boolean save4undo = false;
		if (session.undo_redo_file != null && owning_model.undo_delete >= 0) {
			save4undo = true;
		}
		if (owning_model.optimized) {
			changeInverseReferencesNoInverse(this, null, save4undo);
		} else {
			changeInverseReferences(this, null, false, save4undo);
		}

		CEntity_definition edef = (CEntity_definition)getInstanceType();
		SchemaData sch_data = owning_model.underlying_schema.modelDictionary.schemaData;
//if (instance_identifier==31580) {
//for (int i = 0; i < sch_data.noOfEntityDataTypes; i++)
//System.out.println(" ind = " + i + "   NORMAL NAME: " + sch_data.sNames[i] +
//"   entity name: " + sch_data.entities[i].getNameUpperCase());
//}
		int index_to_entity = sch_data.findEntityExtentIndex(edef);
		if (index_to_entity >= 0) {
			int ind = -1;
            //if (owning_model.sorted[index_to_entity]) {
			if ((owning_model.sim_status[index_to_entity] & SdaiModel.SIM_SORTED) != 0) {
				ind = owning_model.find_instance(0, owning_model.lengths[index_to_entity] - 1,
				index_to_entity, instance_identifier);
			} else {
				for (int j = 0; j < owning_model.lengths[index_to_entity]; j++) {
					if (owning_model.instances_sim[index_to_entity][j] == this) {
						ind = j;
						break;
					}
				}
			}
			if (ind >= 0) {
				if(ind == 0 || ind == owning_model.lengths[index_to_entity] - 1) {
					owning_model.invalidate_quick_find();
				}
                //for (int k = ind; k < owning_model.lengths[index_to_entity] - 1; k++) {
				//	owning_model.instances_sim[index_to_entity][k] =
				//    	owning_model.instances_sim[index_to_entity][k+1];
                //}
                //--array copying operation above was replaced by System.arraycopy() to reduce time consumption -- VV
				if (save4undo) {
					session.undoRedoDeletePrepare(this);
				}
				System.arraycopy(owning_model.instances_sim[index_to_entity], ind+1, owning_model.instances_sim[index_to_entity], ind, owning_model.instances_sim[index_to_entity].length-ind-1);
				owning_model.lengths[index_to_entity]--;
				owning_model.bypass_setAll = true;
				try {
					setAll(null);
				} finally {
					owning_model.bypass_setAll = false;
				}
				deleted();
				deletedObject();
				owning_model.inst_deleted = true;
				owning_model = null;
				return;
			}
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
//		} // syncObject
	}


	void deleteInstanceWeak() throws SdaiException {
		SchemaData sch_data = owning_model.underlying_schema.modelDictionary.schemaData;
		int index_to_entity = sch_data.findEntityExtentIndex((CEntity_definition)getInstanceType());
// 7 lines right below were added to avoid problems due to substituteInstance() when substituted instance is in an R/O SdaiModel
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY) {
			index_to_entity = owning_model.find_entityRO(sch_data.entities[index_to_entity]);
		}
		if (index_to_entity < 0) {
			String base = SdaiSession.line_separator + AdditionalMessages.BF_EINC /*+ entity_name*/;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}

		if (index_to_entity >= 0) {
			int ind = -1;
			CEntity[] type_instances_sim = owning_model.instances_sim[index_to_entity];
			int type_length = owning_model.lengths[index_to_entity];
			//if (owning_model.sorted[index_to_entity]) {
            if ((owning_model.sim_status[index_to_entity] & SdaiModel.SIM_SORTED) != 0) {
				ind = owning_model.find_instance(0, type_length - 1,
					index_to_entity, instance_identifier);
			} else {
				for (int j = 0; j < type_length; j++) {
					if (type_instances_sim[j] == this) {
						ind = j;
						break;
					}
				}
			}
			if (ind >= 0) {
				if(ind == 0 || ind == type_length - 1) {
					owning_model.invalidate_quick_find();
				}
				if(ind < type_length - 1) {
					System.arraycopy(type_instances_sim, ind+1, type_instances_sim, ind,
									 type_length - ind - 1);
				}
				type_instances_sim[type_length - 1] = null;
				owning_model.lengths[index_to_entity]--;
				deletedWeak();
			}
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
	}


	public void set(EAttribute attribute, Object value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.active_transaction.mode != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.TR_NRW, session.active_transaction);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (value instanceof Aggregate) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		if (attribute == null) {
			throw new SdaiException(SdaiException.AT_NDEF);
		}
		if (value instanceof CEntity && (((CEntity)value).owning_model == null)) {
			throw new SdaiException(SdaiException.EI_NVLD, (CEntity)value);
		}
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		CEntityDefinition edef = (CEntityDefinition)getInstanceType();
//System.out.println("   CEntity      value type: " + value.getClass().getName());
//System.out.println("   CEntity      edef: " + edef.getName(null));
		EAttribute saved_attr = attribute;
		if (((AttributeDefinition)attribute).attr_tp == AttributeDefinition.EXPLICIT &&
				((EExplicit_attribute)attribute).testRedeclaring(null)) {
			attribute = getRedeclaredAttribute((EExplicit_attribute)attribute);
		}
		boolean found = false;
		for (int i = 0; i < edef.attributes.length; i++) {
//			if (edef.attributes[i] == attribute && edef.attributeFields[i] != null) {
			if (edef.attributes[i] == attribute) {
				found = true;
				if (edef.attributeFields[i] != null) {
					SelectType sel_type = take_select(attribute);
					int sel_number = 0;
					int tag = -1;
					if (sel_type != null) {
						if (sel_type.is_mixed == 0) {
							sel_number = 1;
							tag = PhFileReader.ENTITY_REFERENCE;
						} else {
							if (select == null || select.length == 0 || select[0] == null) {
								if (value instanceof CEntity) {
									sel_number = 1;
									tag = PhFileReader.ENTITY_REFERENCE;
								} else {
									String base = SdaiSession.line_separator + AdditionalMessages.SE_ATSM;
									throw new SdaiException(SdaiException.AT_NVLD, attribute, base);
								}
							} else {
								sel_number = sel_type.giveSelectNumber(select);
								if (sel_number == -1) {
									String base = SdaiSession.line_separator + AdditionalMessages.SE_NUNF;
									throw new SdaiException(SdaiException.VA_NVLD, base);
								} else if (sel_number == -2) {
									String base = SdaiSession.line_separator + AdditionalMessages.SE_INCP;
									throw new SdaiException(SdaiException.SY_ERR, base);
								}
								tag = sel_type.tags[sel_number - 2];
							}
						}
					}
					Field field = edef.attributeFields[i];
//					SSuper ssuper = owning_model.underlying_schema.modelDictionary.schemaData.super_inst;
					SSuper ssuper = edef.fieldOwners[i].ssuper;
					Object old_value;
					try {
						old_value = ssuper.getObject(this, field);
					} catch (Exception ex) {
						throw new SdaiException(SdaiException.SY_ERR, ex);
					}
					if (old_value == value) {
						return;
					}
					if (old_value instanceof CEntity) {
						CEntity old_ref = (CEntity)old_value;
						if (!old_ref.owning_model.optimized) {
							removeFromInverseList(old_ref);
						}
					} else if (old_value instanceof CAggregate) {
						((CAggregate)old_value).unsetAllByRef(this);
					}
					if (sel_type == null) {
						if ((value = analyse_value((CExplicit_attribute)attribute, value)) == null) {
							throw new SdaiException(SdaiException.VT_NVLD);
						}
					} else {
						if ((value = analyse_select_value(tag, sel_type, sel_number, value)) == null) {
							throw new SdaiException(SdaiException.VT_NVLD);
						}
					}
					session.undoRedoModifyPrepare(this);
					try {
						ssuper.setObject(this, field, value);
						if (sel_number > 1 || (sel_number == 1 && sel_type.is_mixed > 0)) {
							Field sel_field = edef.attributeFieldSelects[i];
							ssuper.setInt(this, sel_field, sel_number);
						}
					} catch (Exception ex) {
						throw new SdaiException(SdaiException.SY_ERR, ex);
					}
					if (value instanceof CEntity) {
						CEntity ref = (CEntity)value;
						if (!ref.owning_model.optimized) {
							addToInverseList(ref);
						}
					}
					modified();
					return;
				}
			}
		}
		if (found) {
			throw new SdaiException(SdaiException.AT_NVLD, saved_attr,
				"The supplied attribute is redeclared by the derived attribute");
		} else {
			throw new SdaiException(SdaiException.AT_NVLD, saved_attr,
				"The supplied attribute is not explicit one for this entity type");
		}
//		} // syncObject
	}


	private EExplicit_attribute getRedeclaredAttribute(EExplicit_attribute red_attr) throws SdaiException {
		EExplicit_attribute attr = red_attr.getRedeclaring(null);
		while (attr.testRedeclaring(null)) {
			attr = attr.getRedeclaring(null);
		}
		return attr;
	}


	public void set(EAttribute attribute, int value, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.active_transaction.mode != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.TR_NRW, session.active_transaction);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		if (attribute == null) {
			throw new SdaiException(SdaiException.AT_NDEF);
		}
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		CEntityDefinition edef = (CEntityDefinition)getInstanceType();
		EAttribute saved_attr = attribute;
		if (((AttributeDefinition)attribute).attr_tp == AttributeDefinition.EXPLICIT &&
				((EExplicit_attribute)attribute).testRedeclaring(null)) {
			attribute = getRedeclaredAttribute((EExplicit_attribute)attribute);
		}
		for (int i = 0; i < edef.attributes.length; i++) {
			if (edef.attributes[i] == attribute && edef.attributeFields[i] != null) {
				SelectType sel_type = take_select(attribute);
				int sel_number = 0;
				if (sel_type != null) {
					if (sel_type.is_mixed == 0) {
						throw new SdaiException(SdaiException.VT_NVLD);
					} else {
						if (select == null || select.length == 0 || select[0] == null) {
							String base = SdaiSession.line_separator + AdditionalMessages.SE_ARTS;
							throw new SdaiException(SdaiException.SY_ERR, base);
						}
						sel_number = sel_type.giveSelectNumber(select);
						if (sel_number == -1) {
							String base = SdaiSession.line_separator + AdditionalMessages.SE_NUNF;
							throw new SdaiException(SdaiException.VA_NVLD, base);
						} else if (sel_number == -2) {
							String base = SdaiSession.line_separator + AdditionalMessages.SE_INCP;
							throw new SdaiException(SdaiException.SY_ERR, base);
						}
						if (sel_number < 2) {
							throw new SdaiException(SdaiException.VT_NVLD);
						}
					}
				}
				Field field = edef.attributeFields[i];
//				SSuper ssuper = owning_model.underlying_schema.modelDictionary.schemaData.super_inst;
				SSuper ssuper = edef.fieldOwners[i].ssuper;
				try {
					if (sel_type != null) {
						int tag = sel_type.tags[sel_number - 2];
						if (tag != PhFileReader.INTEGER && tag != PhFileReader.ENUM &&
								tag != PhFileReader.LOGICAL && tag != PhFileReader.BOOLEAN &&
								tag != PhFileReader.REAL) {
							throw new SdaiException(SdaiException.VT_NVLD);
						}
						if (tag == PhFileReader.INTEGER || tag == PhFileReader.REAL) {
							if (value == Integer.MIN_VALUE) {
								throw new SdaiException(SdaiException.VA_NSET);
							}
						} else {
							if (value == 0) {
								throw new SdaiException(SdaiException.VA_NSET);
							}
						}
						Object old_value_obj = ssuper.getObject(this, field);
						if (old_value_obj instanceof CEntity) {
							CEntity old_ref = (CEntity)old_value_obj;
							if (!old_ref.owning_model.optimized) {
								removeFromInverseList(old_ref);
							}
						} else if (old_value_obj instanceof CAggregate) {
							((CAggregate)old_value_obj).unsetAllByRef(this);
						}
						session.undoRedoModifyPrepare(this);
						if (tag == PhFileReader.REAL) {
							ssuper.setObject(this, field, new Double(value));
						} else {
							ssuper.setObject(this, field, new Integer(value));
						}
					} else {
						switch(find_type(attribute)) {
						case 2:
						case 5:
							if (value == 0) {
								throw new SdaiException(SdaiException.VA_NSET);
							}
							session.undoRedoModifyPrepare(this);
							ssuper.setInt(this, field, value);
							break;
						case 4:
							if (value == Integer.MIN_VALUE) {
								throw new SdaiException(SdaiException.VA_NSET);
							}
							session.undoRedoModifyPrepare(this);
							ssuper.setInt(this, field, value);
							break;
						case 3:
							if (value == Integer.MIN_VALUE) {
								throw new SdaiException(SdaiException.VA_NSET);
							}
							session.undoRedoModifyPrepare(this);
							ssuper.setDouble(this, field, (double)value);
							break;
						default:
							throw new SdaiException(SdaiException.VT_NVLD);
						}
//						ssuper.setInt(this, field, value);
					}
					if (sel_number > 1) {
						Field sel_field = edef.attributeFieldSelects[i];
						ssuper.setInt(this, sel_field, sel_number);
					}
					modified();
					return;
				} catch (java.lang.IllegalAccessException ex) {
					throw new SdaiException(SdaiException.SY_ERR, ex);
				}
			}
		}
		throw new SdaiException(SdaiException.AT_NVLD, saved_attr,
			"The supplied attribute is not explicit one for this entity type");
//		} // syncObject
	}


	public void set(EAttribute attribute, double value, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.active_transaction.mode != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.TR_NRW, session.active_transaction);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		if (Double.isNaN(value)) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (attribute == null) {
			throw new SdaiException(SdaiException.AT_NDEF);
		}
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		CEntityDefinition edef = (CEntityDefinition)getInstanceType();
		EAttribute saved_attr = attribute;
		if (((AttributeDefinition)attribute).attr_tp == AttributeDefinition.EXPLICIT &&
				((EExplicit_attribute)attribute).testRedeclaring(null)) {
			attribute = getRedeclaredAttribute((EExplicit_attribute)attribute);
		}
		for (int i = 0; i < edef.attributes.length; i++) {
			if (edef.attributes[i] == attribute && edef.attributeFields[i] != null) {
				SelectType sel_type = take_select(attribute);
				int sel_number = 0;
				if (sel_type != null) {
					if (sel_type.is_mixed == 0) {
						throw new SdaiException(SdaiException.VT_NVLD);
					} else {
						if (select == null || select.length == 0 || select[0] == null) {
							String base = SdaiSession.line_separator + AdditionalMessages.SE_ARTS;
							throw new SdaiException(SdaiException.SY_ERR, base);
						}
						sel_number = sel_type.giveSelectNumber(select);
						if (sel_number == -1) {
							String base = SdaiSession.line_separator + AdditionalMessages.SE_NUNF;
							throw new SdaiException(SdaiException.VA_NVLD, base);
						} else if (sel_number == -2) {
							String base = SdaiSession.line_separator + AdditionalMessages.SE_INCP;
							throw new SdaiException(SdaiException.SY_ERR, base);
						}
						if (sel_number < 2) {
							throw new SdaiException(SdaiException.VT_NVLD);
						}
					}
				}
				Field field = edef.attributeFields[i];
//				SSuper ssuper = owning_model.underlying_schema.modelDictionary.schemaData.super_inst;
				SSuper ssuper = edef.fieldOwners[i].ssuper;
				try {
					if (sel_type != null) {
						int tag = sel_type.tags[sel_number - 2];
						if (tag != PhFileReader.REAL) {
							throw new SdaiException(SdaiException.VT_NVLD);
						}
						Object old_value_obj = ssuper.getObject(this, field);
						if (old_value_obj instanceof CEntity) {
							CEntity old_ref = (CEntity)old_value_obj;
							if (!old_ref.owning_model.optimized) {
								removeFromInverseList(old_ref);
							}
						} else if (old_value_obj instanceof CAggregate) {
							((CAggregate)old_value_obj).unsetAllByRef(this);
						}
						session.undoRedoModifyPrepare(this);
						ssuper.setObject(this, field, new Double(value));
					} else {
						if (find_type(attribute) != 3) {
							throw new SdaiException(SdaiException.VT_NVLD);
						}
						session.undoRedoModifyPrepare(this);
						ssuper.setDouble(this, field, value);
					}
					if (sel_number > 1) {
						Field sel_field = edef.attributeFieldSelects[i];
						ssuper.setInt(this, sel_field, sel_number);
					}
					modified();
					return;
				} catch (java.lang.IllegalAccessException ex) {
					throw new SdaiException(SdaiException.SY_ERR, ex);
				}
			}
		}
		throw new SdaiException(SdaiException.AT_NVLD, saved_attr,
			"The supplied attribute is not explicit one for this entity type");
//		} // syncObject
	}


	public void set(EAttribute attribute, boolean value, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.active_transaction.mode != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.TR_NRW, session.active_transaction);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		if (attribute == null) {
			throw new SdaiException(SdaiException.AT_NDEF);
		}
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		CEntityDefinition edef = (CEntityDefinition)getInstanceType();
		EAttribute saved_attr = attribute;
		if (((AttributeDefinition)attribute).attr_tp == AttributeDefinition.EXPLICIT &&
				((EExplicit_attribute)attribute).testRedeclaring(null)) {
			attribute = getRedeclaredAttribute((EExplicit_attribute)attribute);
		}
		for (int i = 0; i < edef.attributes.length; i++) {
			if (edef.attributes[i] == attribute && edef.attributeFields[i] != null) {
				SelectType sel_type = take_select(attribute);
				int sel_number = 0;
				if (sel_type != null) {
					if (sel_type.is_mixed == 0) {
						throw new SdaiException(SdaiException.VT_NVLD);
					} else {
						if (select == null || select.length == 0 || select[0] == null) {
							String base = SdaiSession.line_separator + AdditionalMessages.SE_ARTS;
							throw new SdaiException(SdaiException.SY_ERR, base);
						}
						sel_number = sel_type.giveSelectNumber(select);
						if (sel_number == -1) {
							String base = SdaiSession.line_separator + AdditionalMessages.SE_NUNF;
							throw new SdaiException(SdaiException.VA_NVLD, base);
						} else if (sel_number == -2) {
							String base = SdaiSession.line_separator + AdditionalMessages.SE_INCP;
							throw new SdaiException(SdaiException.SY_ERR, base);
						}
						if (sel_number < 2) {
							throw new SdaiException(SdaiException.VT_NVLD);
						}
					}
				}
				Field field = edef.attributeFields[i];
//				SSuper ssuper = owning_model.underlying_schema.modelDictionary.schemaData.super_inst;
				SSuper ssuper = edef.fieldOwners[i].ssuper;
				int value_int;
				try {
					if (sel_type != null) {
						int tag = sel_type.tags[sel_number - 2];
						if (tag != PhFileReader.BOOLEAN) {
							throw new SdaiException(SdaiException.VT_NVLD);
						}
						Object old_value_obj = ssuper.getObject(this, field);
						if (old_value_obj instanceof CEntity) {
							CEntity old_ref = (CEntity)old_value_obj;
							if (!old_ref.owning_model.optimized) {
								removeFromInverseList(old_ref);
							}
						} else if (old_value_obj instanceof CAggregate) {
							((CAggregate)old_value_obj).unsetAllByRef(this);
						}
						if (value) {
							value_int = 2;
						} else {
							value_int = 1;
						}
						session.undoRedoModifyPrepare(this);
						ssuper.setObject(this, field, new Integer(value_int));
					} else {
						if (find_type(attribute) != 5) {
							throw new SdaiException(SdaiException.VT_NVLD);
						}
						if (value) {
							value_int = 2;
						} else {
							value_int = 1;
						}
						session.undoRedoModifyPrepare(this);
						ssuper.setInt(this, field, value_int);
					}
					if (sel_number > 1) {
						Field sel_field = edef.attributeFieldSelects[i];
						ssuper.setInt(this, sel_field, sel_number);
					}
					modified();
					return;
				} catch (java.lang.IllegalAccessException ex) {
					throw new SdaiException(SdaiException.SY_ERR, ex);
				}
			}
		}
		throw new SdaiException(SdaiException.AT_NVLD, saved_attr,
			"The supplied attribute is not explicit one for this entity type");
//		} // syncObject
	}


	public void unsetAttributeValue(EAttribute attribute) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.active_transaction.mode != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.TR_NRW, session.active_transaction);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		if (attribute == null) {
			throw new SdaiException(SdaiException.AT_NDEF);
		}
		if (((AttributeDefinition)attribute).attr_tp != AttributeDefinition.EXPLICIT) {
			throw new SdaiException(SdaiException.AT_NVLD, attribute);
		}
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		CEntityDefinition edef = (CEntityDefinition)getInstanceType();
		EAttribute saved_attr = attribute;
		if (((EExplicit_attribute)attribute).testRedeclaring(null)) {
			attribute = getRedeclaredAttribute((EExplicit_attribute)attribute);
		}
		for (int i = 0; i < edef.attributes.length; i++) {
			if (edef.attributes[i] == attribute && edef.attributeFields[i] != null) {
				int type;
				SelectType sel_type = take_select(attribute);
				if (sel_type == null) {
					type = find_type(attribute);
				} else {
					type = 1;
				}
				Field field = edef.attributeFields[i];
				SSuper ssuper = edef.fieldOwners[i].ssuper;
				Object old_value_obj;
				try {
					switch (type) {
						case  0:
							break;
						case  1:
							old_value_obj = ssuper.getObject(this, field);
							if (old_value_obj != null) {
								if (old_value_obj instanceof CEntity) {
									CEntity old_ref = (CEntity)old_value_obj;
									if (!old_ref.owning_model.optimized) {
										removeFromInverseList(old_ref);
									}
								} else if (old_value_obj instanceof CAggregate) {
									((CAggregate)old_value_obj).unsetAllByRef(this);
								}
								session.undoRedoModifyPrepare(this);
								ssuper.setObject(this, field, null);
								if (sel_type != null && sel_type.is_mixed > 0) {
									try {
										Field sel_field = edef.attributeFieldSelects[i];
										ssuper.setInt(this, sel_field, 0);
									} catch (Exception ex) {
										throw new SdaiException(SdaiException.SY_ERR, ex);
									}
								}
								modified();
							}
							break;
						case  2:
						case  5:
							int old_value_int = ssuper.getInt(this, field);
							if (old_value_int != 0) {
								session.undoRedoModifyPrepare(this);
								ssuper.setInt(this, field, 0);
								modified();
							}
							break;
						case  3:
							double old_value_double = ssuper.getDouble(this, field);
							if (!(Double.isNaN(old_value_double))) {
								session.undoRedoModifyPrepare(this);
								ssuper.setDouble(this, field, Double.NaN);
								modified();
							}
							break;
						case  4:
							old_value_int = ssuper.getInt(this, field);
							if (old_value_int != Integer.MIN_VALUE) {
								session.undoRedoModifyPrepare(this);
								ssuper.setInt(this, field, Integer.MIN_VALUE);
								modified();
							}
							break;
					}
					return;
				} catch (java.lang.IllegalAccessException ex) {
					throw new SdaiException(SdaiException.SY_ERR, ex);
				}
			}
		}
		throw new SdaiException(SdaiException.AT_NVLD, saved_attr,
			"The supplied attribute is not explicit one for this entity type");
//		} // syncObject
	}


	public Aggregate createAggregate(EAttribute attribute, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
		if (attribute == null) {
			throw new SdaiException(SdaiException.AT_NDEF);
		}
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.active_transaction.mode != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.TR_NRW, session.active_transaction);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		if (((AttributeDefinition)attribute).attr_tp != AttributeDefinition.EXPLICIT) {
			throw new SdaiException(SdaiException.AT_NVLD, attribute);
		}
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		CEntityDefinition edef = (CEntityDefinition)getInstanceType();
		EAttribute saved_attr = attribute;
		if (((AttributeDefinition)attribute).attr_tp == AttributeDefinition.EXPLICIT &&
				((EExplicit_attribute)attribute).testRedeclaring(null)) {
			attribute = getRedeclaredAttribute((EExplicit_attribute)attribute);
		}
		AggregationType aggr_type;
		for (int i = 0; i < edef.attributes.length; i++) {
//System.out.println("  CEntity   edef: " + edef.getName(null) +
//"  param attribute: " + attribute.getName(null) +
//"  attribute for check: " + edef.attributes[i].getName(null));
			if (edef.attributes[i] == attribute && edef.attributeFields[i] != null) {
				SelectType sel_type = take_select(attribute);
				int sel_number;
				if (sel_type != null) {
					if (sel_type.is_mixed == 0) {
						throw new SdaiException(SdaiException.AT_NVLD, saved_attr);
					} else {
						if (select == null || select.length == 0 || select[0] == null) {
							String base = SdaiSession.line_separator + AdditionalMessages.SE_ARTS;
							throw new SdaiException(SdaiException.SY_ERR, base);
						}
						sel_number = sel_type.giveSelectNumber(select);
						if (sel_number == -1) {
							String base = SdaiSession.line_separator + AdditionalMessages.SE_NUNF;
							throw new SdaiException(SdaiException.VA_NVLD, base);
						} else if (sel_number == -2) {
							String base = SdaiSession.line_separator + AdditionalMessages.SE_INCP;
							throw new SdaiException(SdaiException.SY_ERR, base);
						}
						aggr_type = (AggregationType)sel_type.types[sel_number - 2];
						if (aggr_type == null) {
							throw new SdaiException(SdaiException.AT_NVLD, saved_attr);
						}
					}
				} else {
					sel_number = 0;
					aggr_type = find_aggr_type((CExplicit_attribute)attribute);
				}
				Field field = edef.attributeFields[i];
//				SSuper ssuper = owning_model.underlying_schema.modelDictionary.schemaData.super_inst;
				SSuper ssuper = edef.fieldOwners[i].ssuper;
				Object old_value;
				try {
					old_value = ssuper.getObject(this, field);
				} catch (Exception ex) {
					throw new SdaiException(SdaiException.SY_ERR, ex);
				}
				if (old_value instanceof CEntity) {
					CEntity old_ref = (CEntity)old_value;
					if (!old_ref.owning_model.optimized) {
						removeFromInverseList(old_ref);
					}
				} else if (old_value instanceof CAggregate) {
					((CAggregate)old_value).unsetAllByRef(this);
				}
				Class aggr_class = aggr_type.getAggregateClass();
				Aggregate aggr;
				try {
					aggr = (Aggregate)aggr_class.newInstance();
				} catch (Exception ex) {
					throw new SdaiException(SdaiException.SY_ERR, ex);
				}
				if (aggr instanceof CAggregate) {
					((CAggregate)aggr).attach(aggr_type, this);
				} else if (aggr instanceof A_primitive) {
					((A_primitive)aggr).attach(aggr_type, this);
				}
				session.undoRedoModifyPrepare(this);
				try {
					ssuper.setObject(this, field, aggr);
					if (sel_number > 1) {
						Field sel_field = edef.attributeFieldSelects[i];
						ssuper.setInt(this, sel_field, sel_number);
					}
				} catch (java.lang.IllegalAccessException ex) {
					throw new SdaiException(SdaiException.SY_ERR, ex);
				}
				modified();
				return aggr;
			}
		}
		throw new SdaiException(SdaiException.AT_NVLD, saved_attr,
			"The supplied attribute is not explicit one for this entity type");
//		} // syncObject
	}


/**
	For a given attribute, which should represent an aggregate, returns the
	aggregation type of its elements. If, however, the attribute is not
	an aggregate, then SdaiException AT_NVLD is thrown.
	This method is invoked in <code>createAggregate</code>.
*/
	private AggregationType find_aggr_type(CExplicit_attribute attribute)
			throws SdaiException {
		DataType type = (DataType)attribute.getDomain(null);
		if (type.express_type >= DataType.LIST && type.express_type <= DataType.AGGREGATE) {
			return (AggregationType)type;
		}
		while (type.express_type == DataType.DEFINED_TYPE) {
			type = (DataType)((CDefined_type)type).getDomain(null);
			if (type.express_type >= DataType.LIST && type.express_type <= DataType.AGGREGATE) {
				return (AggregationType)type;
			}
		}
		throw new SdaiException(SdaiException.AT_NVLD, attribute);
	}


	public String getPersistentLabel() throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		String label = Long.toString(instance_identifier, 10);
		return "#" + label;
//		} // syncObject
	}


	public String getDescription() throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		String name = Long.toString(instance_identifier, 10);
		return "#" + name + " " + owning_model.repository.physical_file_name;
//		} // syncObject
	}


	private boolean isBypassed(EWhere_rule wrule, String [] bp_rules_names, int br_count) throws SdaiException {
		int i;
		if (wrule.testLabel(null)) {
			String wr_name = wrule.getLabel(null);
			for (i = 0; i < br_count; i++) {
				if (bp_rules_names[i].equals(wr_name)) {
					return true;
				}
			}
		} else {
			for (i = 0; i < br_count; i++) {
				if (bp_rules_names[i].equals("")) {
					return true;
				}
			}
		}
		return false;
	}


	public int validateWhereRule(EWhere_rule rule, ASdaiModel domain) throws SdaiException {
//		synchronized (syncObject) {
		return validateWhereRule(StaticFields.get(), rule, domain, null);
//		} // syncObject
	}


	public int validateWhereRule(AWhere_rule viol_rules, ASdaiModel domain) throws SdaiException {
//		synchronized (syncObject) {
		return validateWhereRule(StaticFields.get(), null, domain, viol_rules);
//		} // syncObject
	}


	private int validateWhereRule(StaticFields staticFields, EWhere_rule rule, ASdaiModel domain, AWhere_rule viol_rules) throws SdaiException {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
//long time1=0, time2=0, time3, time4, time5, time6, time7;
		ASdaiModel dom;
		staticFields.inst_under_valid = this;
//String e_nm = getInstanceType().getName(null);
//boolean bl = false;
//if (e_nm.equals("stratum_technology_occurrence_link_armx")) bl = true;
		if (domain == null) {
			if (staticFields._domain == null) {
				staticFields._domain = new ASdaiModel();
			}
			staticFields._domain.addByIndex(1, owning_model);
			dom = staticFields._domain;
		} else {
			dom = domain;
		}
		CEntityDefinition def = (CEntityDefinition)getInstanceType();
		Class rule_class;
//String strg = ((CEntity_definition)def).getName(null);
//System.out.println("CEntity !!!!!   validated instance = " + strg + "  id: " + instance_identifier);
		int res;
		if (rule != null) {
			CEntity parent = (CEntity)rule.getParent_item(null);
			if (parent instanceof EDefined_type) {
				res = validateWhereRuleInDefTypes(staticFields, rule, (CDefined_type)parent, def, dom);
			} else if (parent instanceof CEntity_definition) {
				rule_class = def.getEntityClass();
				res = validateWhereRule(staticFields, rule, (CEntity_definition)parent, rule_class, def, dom, false);
			} else {
				throw new SdaiException(SdaiException.RU_NDEF);
			}
		} else {
			res = ELogical.TRUE;
			int check_res;

			rule_class = def.getEntityClass();
			String [] bp_rules_names = null;                   // added for Bug #2739
			SchemaData sch_data = def.owning_model.schemaData; // added for Bug #2739
			SdaiSession ss = owning_model.repository.session;  // added for Bug #2739
			for (int j = 0; j < def.noOfPartialEntityTypes; j++) {
				CEntityDefinition part_def = def.partialEntityTypes[j];
				int br_count = sch_data.getBypassedRulesCount(part_def.index, ss); // added for Bug #2739
				if (br_count > 0) {                                                // added for Bug #2739
					bp_rules_names = sch_data.getBypassedRules(part_def.index);      // added for Bug #2739
				}                                                                  // added for Bug #2739
				AWhere_rule w_rules = part_def.getWhere_rules(null, null);
				CWhere_rule [] w_rules_ord = part_def.get_where_rules(w_rules);
				int ln = ((AEntity)w_rules).myLength;
				if (ln > 0) {
					for (int i = 0; i < ln; i++) {
//					while(iter.next()) {
						EWhere_rule wrule = w_rules_ord[i];
						if (br_count > 0 && ((WhereRule)wrule).isRuleBypassed(bp_rules_names, br_count)) {  // added for Bug #2739
							continue;                                                                         // added for Bug #2739
						}                                                                                   // added for Bug #2739
//if (bl) {
//time1 = System.currentTimeMillis();
//if (instance_identifier==1004) Value.prnt=true;
//}
						check_res = validateWhereRule(staticFields, wrule, (CEntity_definition)wrule.getParent_item(null), rule_class,
							part_def, dom, true);
//Value.prnt=false;
//if (bl) {
//time2 = System.currentTimeMillis();
//time3=time2-time1;double tim = ((double)time3)/1000;
// The below 3 lines can be uncommented when investigating where rules in Validate tool
//EEntity parent = wrule.getParent_item(null);
//System.out.println("CEntity   after validateWhereRule ***** wrule: " + wrule.getLabel(null) +
//"   check_res: " + check_res + "  parent: " + ((CEntity_definition)parent).getName(null) + "  time: " + tim + " sec");
//}
						if (check_res == ELogical.FALSE) {
							res = check_res;
							if (viol_rules != null) {
								if (((AEntity)viol_rules).myType == null || ((AEntity)viol_rules).myType.express_type == DataType.LIST) {
									((AEntity)viol_rules).addAtTheEnd(wrule, null);
								} else {
									((AEntity)viol_rules).setForNonList(wrule, ((AEntity)viol_rules).myLength, null, null);
								}
							} else {
								break;
							}
						} else if (check_res == ELogical.UNKNOWN) {
							if (res == ELogical.TRUE) {
								res = check_res;
							}
						}
//System.out.println("CEntity  !!!!! res: " + res);
					}
				}
			}

			if (viol_rules != null || res != ELogical.FALSE) {
				check_res = validateWhereRuleInDefTypes(staticFields, def, dom, viol_rules);
//System.out.println("CEntity ^^^^^   check_res = " + check_res);
				if (check_res == ELogical.FALSE) {
					res = check_res;
				} else if (check_res == ELogical.UNKNOWN) {
					if (res == ELogical.TRUE) {
						res = check_res;
					}
				}
			}
		}
		if (dom == staticFields._domain) {
			staticFields._domain.clear();
		}
//System.out.println("CEntity  FINAL>>> res: " + res);
		staticFields.inst_under_valid = null;
		return res;
	}


	private int validateWhereRule(StaticFields staticFields, EWhere_rule rule, CEntity_definition rule_owner, Class rule_class, CEntityDefinition def,
			ASdaiModel domain, boolean all) throws SdaiException {
		((WhereRule)rule).exc = null;
		if (!all && !(def.isSubtypeOf(rule_owner))) {
			throw new SdaiException(SdaiException.RU_NDEF);
		}
		if (!rule.testLabel(null)) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
//if (instance_identifier==7873) Value.prnt=true;
		if (staticFields.param == null) {
			staticFields.param = new Class[1];
			staticFields.param[0] = SdaiContext.class;
			staticFields.arg = new Object [1];
		}
		SdaiSession ss = owning_model.repository.session;
		if (ss.sdai_context == null) {
			throw new SdaiException(SdaiException.SY_ERR, "SdaiContext shall be provided");
		}
		Method meth;
		String m_name = WHERE_RULE_METHOD_NAME_PREFIX + normalise(((CEntity_definition)def).getName(null)) +
			normalise(rule.getLabel(null));
		try {
			meth = rule_class.getMethod(m_name, staticFields.param);
		} catch (java.lang.NoSuchMethodException ex) {
			throw new SdaiException(SdaiException.SY_ERR, "Method " + m_name + " not found in class " + rule_class.getName());
		}
//if (instance_identifier == 16595 || instance_identifier == 16598 || instance_identifier == 16689) {
//System.out.println("CEntity *****   m_name: " + m_name + "  seeked in the class: " + rule_class.getName());
//}
		ASdaiModel saved_domain = ss.sdai_context.domain;
		ss.sdai_context.domain = domain;
		staticFields.arg[0] = ss.sdai_context;
		Object res;
		if (ss.sdai_context != null && ss.sdai_context.aggr_size != 0) {
			ss.sdai_context.aggr_size = 0;
		}
		try {
			res = meth.invoke(this, staticFields.arg);
		} catch (Exception ex) {
//if (instance_identifier==8034 && rule.getLabel(null).equals("wr3")) {CATCH_EXCEPTIONS=false;/*Value.prnt=true;*/}
			if (CATCH_EXCEPTIONS && ex instanceof java.lang.reflect.InvocationTargetException) {
//				Exception tex = (Exception)((java.lang.reflect.InvocationTargetException)ex).getTargetException();
				Object tex = ((java.lang.reflect.InvocationTargetException)ex).getTargetException();
				if (tex instanceof SdaiException) {
					((WhereRule)rule).exc = (SdaiException)tex;
					ss.sdai_context.domain = saved_domain;
					return ELogical.FALSE;
				} else if (tex instanceof StackOverflowError) {
					((WhereRule)rule).exc = new StackOverflowError("stack overflow");
					ss.sdai_context.domain = saved_domain;
					return ELogical.FALSE;
				} else {
//ex.printStackTrace();
					throw new SdaiException(SdaiException.SY_ERR, tex);
				}
			} else {
//CATCH_EXCEPTIONS=true;
//ex.printStackTrace();
				throw new SdaiException(SdaiException.SY_ERR, ex);
			}
		}
//Value.prnt=false;
		ss.sdai_context.domain = saved_domain;
		int ret_value = ((Integer)res).intValue();
		if (ret_value <= 1) {
			WhereRule wr = (WhereRule)rule;
			SdaiContext contxt = (SdaiContext)ss.sdai_context;
			if (contxt != null && contxt.aggr_size != 0) {
				wr.store_to_array(contxt.ent_instances, contxt.aggr_size, contxt.empty_aggr);
				contxt.aggr_size = 0;
			} else {
				wr.aggr_size = 0;
			}
		}

//		return ((Integer)res).intValue();
		return ret_value;
	}


	private int validateWhereRuleInDefTypes(StaticFields staticFields, CEntityDefinition def, ASdaiModel domain, AWhere_rule viol_rules) throws SdaiException {
		if (def.def_types == null) {
			staticFields.context_schema = owning_model.underlying_schema;
			def.extract_def_types(staticFields);
			staticFields.context_schema = null;
		}
//String strg = ((CEntity_definition)def).getName(null);
//System.out.println("CEntity +++++   def.def_types.length = " + def.def_types.length + "   def: " + strg);
		if (def.def_types.length <= 0) {
			return ELogical.TRUE;
		}
		int i, j, k;
		boolean found;
		if (staticFields.defined_types == null) {
			int ln;
			if (def.def_types.length > VALUE_ARRAY_SIZE) {
				ln = def.def_types.length;
			} else {
				ln = VALUE_ARRAY_SIZE;
			}
			staticFields.attr_values = new Value[ln];
			staticFields.defined_types = new CDefined_type[ln];
			staticFields.marks = new boolean[ln];
		} else if (def.def_types.length > staticFields.attr_values.length) {
			staticFields.attr_values = new Value[def.def_types.length];
			staticFields.defined_types = new CDefined_type[def.def_types.length];
			staticFields.marks = new boolean[def.def_types.length];
		}
		for (i = 0; i < def.def_types.length; i++) {
			staticFields.marks[i] = false;
		}
		int count = 0;
		int res;
		Value val, val_except;
		SdaiException sdaiex;
		for (i = 0; i < def.def_types.length; i++) {
			val = null;
			if (def.def_types_branch[i] == 0) {
				staticFields.defined_types[count] = def.def_types[i];
				try {
					staticFields.attr_values[count] = get(def.def_types_attr[i]);
				} catch (Throwable ex) {
					if (CATCH_EXCEPTIONS) {
						if (ex instanceof SdaiException) {
							sdaiex = (SdaiException)ex;
							if (sdaiex.getErrorId() == SdaiException.VA_NSET) {
								val_except = new Value();
								val_except.tag = Value.EXCPT;
								val_except.reference = ex;
								staticFields.attr_values[count] = val_except;
							} else if (((SdaiException)ex).getUnderlyingException() instanceof java.lang.reflect.InvocationTargetException) {
								val_except = new Value();
								val_except.tag = Value.EXCPT;
								val_except.reference = ex;
								staticFields.attr_values[count] = val_except;
							} else {
								throw sdaiex;
							}
						} else if (ex instanceof StackOverflowError) {
							val_except = new Value();
							val_except.tag = Value.EXCPT;
							val_except.reference = new StackOverflowError("stack overflow");
							staticFields.attr_values[count] = val_except;
						} else {
							throw new SdaiException(SdaiException.SY_ERR, ex);
						}
					} else {
						throw new SdaiException(SdaiException.SY_ERR, ex);
					}
				}
				count++;
				continue;
			}
			if (def.def_types_branch[i] == 1) {
				staticFields.defined_types[count] = def.def_types[i];
				try {
					val = get(def.def_types_attr[i]);
				} catch (Throwable ex) {
					if (CATCH_EXCEPTIONS) {
						if (ex instanceof SdaiException) {
							sdaiex = (SdaiException)ex;
							if (sdaiex.getErrorId() == SdaiException.VA_NSET) {
								val = new Value();
								val.tag = Value.EXCPT;
								val.reference = ex;
							} else {
								throw sdaiex;
							}
						} else if (ex instanceof StackOverflowError) {
							val = new Value();
							val.tag = Value.EXCPT;
							val.reference = new StackOverflowError("stack overflow");
						} else {
							throw new SdaiException(SdaiException.SY_ERR, ex);
						}
					} else {
						throw new SdaiException(SdaiException.SY_ERR, ex);
					}
				}
				staticFields.attr_values[count++] = val;
				found = false;
				for (j = i + 1; j < def.def_types.length; j++) {
					if (def.def_types_attr[j] == def.def_types_attr[i]) {
						found = true;
						break;
					}
				}
				if (found) {
					if (staticFields.df_types == null) {
						staticFields.df_types = new EDefined_type[TYPES_ARRAY_SIZE];
					}
					res = testAttribute(def.def_types_attr[i], staticFields.df_types);
					j = 0;
					while (staticFields.df_types[j] != null) {
						for (k = i + 1; k < def.def_types.length; k++) {
							if (def.def_types_attr[k] == def.def_types_attr[i]) {
								if (def.def_types[k] == staticFields.df_types[j]) {
									staticFields.defined_types[count] = def.def_types[k];
									staticFields.attr_values[count++] = val;
								}
								staticFields.marks[k] = true;
							}
						}
						j++;
					}
				}
			} else if (def.def_types_branch[i] == 2) {
				if (staticFields.marks[i]) {
					continue;
				}
				if (staticFields.df_types == null) {
					staticFields.df_types = new EDefined_type[TYPES_ARRAY_SIZE];
				}
				res = testAttribute(def.def_types_attr[i], staticFields.df_types);
				j = 0;
				while (staticFields.df_types[j] != null) {
					for (k = i; k < def.def_types.length; k++) {
						if (def.def_types_attr[k] == def.def_types_attr[i]) {
							if (def.def_types[k] == staticFields.df_types[j]) {
								staticFields.defined_types[count] = def.def_types[k];
								if (val == null) {
									try {
										val = get(def.def_types_attr[i]);
									} catch (Throwable ex) {
										if (CATCH_EXCEPTIONS) {
											if (ex instanceof SdaiException) {
												sdaiex = (SdaiException)ex;
												if (sdaiex.getErrorId() == SdaiException.VA_NSET) {
													val = new Value();
													val.tag = Value.EXCPT;
													val.reference = ex;
												} else {
													throw sdaiex;
												}
											} else if (ex instanceof StackOverflowError) {
												val = new Value();
												val.tag = Value.EXCPT;
												val.reference = new StackOverflowError("stack overflow");
											} else {
												throw new SdaiException(SdaiException.SY_ERR, ex);
											}
										} else {
											throw new SdaiException(SdaiException.SY_ERR, ex);
										}
									}
								}
								staticFields.attr_values[count++] = val;
							}
							staticFields.marks[k] = true;
						}
					}
					j++;
				}
			}
		}
		if (count <= 0) {
			return ELogical.TRUE;
		}
		int valid_res = ELogical.TRUE;
		Class cl;
		if (staticFields.paramwa == null) {
			staticFields.paramwa = new Class[3];
			staticFields.paramwa[0] = SdaiContext.class;
			staticFields.paramwa[1] = Value.class;
			staticFields.paramwa[2] = A_string.class;
			staticFields.argwa = new Object [3];
		}
//		A_string strings = new A_string(SdaiSession.setType0toN, this);
		A_string strings = new A_string(ExpressTypes.SET_STRING_TYPE, this);
		SdaiSession ss = owning_model.repository.session;
		if (ss.sdai_context == null) {
			throw new SdaiException(SdaiException.SY_ERR, "SdaiContext shall be provided");
		}
		ASdaiModel saved_domain = ss.sdai_context.domain;
		ss.sdai_context.domain = domain;
		staticFields.argwa[0] = ss.sdai_context;
		for (i = 0; i < count; i++) {
			if (staticFields.attr_values[i].tag == Value.EXCPT) {
				Throwable excpt = (Throwable)staticFields.attr_values[i].reference;
				collect_where_rules(staticFields, staticFields.defined_types[i], viol_rules, excpt);
				valid_res = ELogical.FALSE;
				continue;
			}
			SchemaData sch_data = ((CEntity)staticFields.defined_types[i]).owning_model.schemaData;
			String sch_name = sch_data.schema.getName(null);
			String normalized_sch_name = sch_name.substring(0,1).toUpperCase() + sch_name.substring(1).toLowerCase();
			String str = SdaiSession.SCHEMA_PREFIX + normalized_sch_name + ".S" + normalized_sch_name;
			try {
				cl = Class.forName(str, true, SdaiClassLoaderProvider.getDefault().getClassLoader());
			} catch (ClassNotFoundException ex) {
				throw new SdaiException(SdaiException.SY_ERR, "Special class for Express schema not found: " + str);
			}
			String method_name = ALL_WHERE_RULE_METHOD_NAME_PREFIX + normalise(staticFields.defined_types[i].getName(null));
			Method meth;
			try {
				meth = cl.getDeclaredMethod(method_name, staticFields.paramwa);
			} catch (java.lang.NoSuchMethodException ex) {
				throw new SdaiException(SdaiException.SY_ERR, "Method " + method_name + " not found in class " + cl.getName());
			}
			SSuper sp =  sch_data.super_inst;
			staticFields.argwa[1] = staticFields.attr_values[i];
			strings.clear();
			staticFields.argwa[2] = strings;
			Object v_res;
			try {
				v_res = meth.invoke(sp, staticFields.argwa);
			} catch (Exception ex) {
//				throw new SdaiException(SdaiException.SY_ERR, ex);
				if (CATCH_EXCEPTIONS && ex instanceof java.lang.reflect.InvocationTargetException) {
//					Exception tex = (Exception)((java.lang.reflect.InvocationTargetException)ex).getTargetException();
					Object tex = ((java.lang.reflect.InvocationTargetException)ex).getTargetException();
					if (tex instanceof SdaiException) {
						collect_where_rules(staticFields, staticFields.defined_types[i], viol_rules, (SdaiException)tex);
						valid_res = ELogical.FALSE;
						continue;
					} else if (tex instanceof StackOverflowError) {
						collect_where_rules(staticFields, staticFields.defined_types[i], viol_rules,
							new StackOverflowError("stack overflow"));
						valid_res = ELogical.FALSE;
						continue;
					} else {
						throw new SdaiException(SdaiException.SY_ERR, tex);
					}
				} else {
					throw new SdaiException(SdaiException.SY_ERR, ex);
				}
			}
			res = ((Integer)v_res).intValue();
//System.out.println("CEntity <<>>   str = " + str + "   res: " + res +
//"   method_name: " + method_name + "   def_type: " + defined_types[i].getName(null) +
//"   i: " + i + "   instance: #" + instance_identifier +
//"   attr_values[i]: " + attr_values[i]);
			if (res == ELogical.FALSE) {
				valid_res = res;
				if (viol_rules != null && strings.myLength > 0) {
					if (staticFields.wrules == null) {
						if (strings.myLength <= CEntityDefinition.RULES_ARRAY_SIZE) {
							staticFields.wrules = new CWhere_rule[CEntityDefinition.RULES_ARRAY_SIZE];
						} else {
							staticFields.wrules = new CWhere_rule[strings.myLength];
						}
					} else if (strings.myLength > staticFields.wrules.length) {
						staticFields.wrules = new CWhere_rule[strings.myLength];
					}
					Object [] myDataA = null;
					if (strings.myLength > 1) {
						myDataA = (Object [])strings.myData;
					}
					EWhere_rule wrule;
					for (j = 0; j < strings.myLength; j++) {
						CDefined_type def_type_rule;
						wrule = null;
						if (strings.myLength == 1) {
							str = (String)strings.myData;
						} else {
							str = (String)myDataA[j];
						}
						int dot = str.lastIndexOf(PhFileReader.DOT);
						String rule_name = str.substring(dot + 1);
						DataType dt = (DataType)SdaiSession.findDataType(str.substring(0, dot), cl);
						if (dt == null) {
							throw new SdaiException(SdaiException.SY_ERR);
						}
						if (dt.express_type == DataType.DEFINED_TYPE) {
							def_type_rule = (CDefined_type)dt;
						} else {
							throw new SdaiException(SdaiException.VT_NVLD);
						}
						AWhere_rule w_rules = def_type_rule.getWhere_rules(null, null);
						if (staticFields.it_refs == null) {
							staticFields.it_refs = w_rules.createIterator();
						} else {
							w_rules.attachIterator(staticFields.it_refs);
						}
						found = false;
						while(staticFields.it_refs.next()) {
							wrule = w_rules.getCurrentMember(staticFields.it_refs);
							if (wrule.getLabel(null).equals(rule_name)) {
								found = true;
								break;
							}
						}
						if (!found) {
							throw new SdaiException(SdaiException.SY_ERR);
						}
						staticFields.wrules[j] = (CWhere_rule)wrule;
					}
					for (j = 0; j < strings.myLength; j++) {
						int mind = -1, order_sel = 0;
						EEntity parent_sel = null;
						for (k = 0; k < strings.myLength; k++) {
							if (staticFields.wrules[k] == null) {
								continue;
							}
							int order = staticFields.wrules[k].getOrder(null);
							EEntity parent = staticFields.wrules[k].getParent_item(null);
							if (mind == -1 || (parent == parent_sel && order < order_sel)) {
								mind = k;
								parent_sel = parent;
								order_sel = order;
							}
						}
						if (((AEntity)viol_rules).myType == null || ((AEntity)viol_rules).myType.express_type == DataType.LIST) {
							((AEntity)viol_rules).addAtTheEnd(staticFields.wrules[mind], null);
						} else {
							((AEntity)viol_rules).setForNonList(staticFields.wrules[mind], ((AEntity)viol_rules).myLength, null, null);
						}
						staticFields.wrules[mind] = null;
					}
				}
			} else if (res == ELogical.UNKNOWN) {
				if (valid_res == ELogical.TRUE) {
					valid_res = res;
				}
			}
		}
		ss.sdai_context.domain = saved_domain;
		return valid_res;
	}


	private int validateWhereRuleInDefTypes(StaticFields staticFields, EWhere_rule rule, CDefined_type rule_owner, CEntityDefinition def,
			ASdaiModel domain) throws SdaiException {
		if (!rule.testLabel(null)) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		int count = verify_type(staticFields, def, rule_owner);
		if (count < 0) {
			throw new SdaiException(SdaiException.RU_NDEF);
		} else if (count == 0) {
			return ELogical.TRUE;
		}
		SchemaData sch_data = ((CEntity)rule_owner).owning_model.schemaData;
		String sch_name = sch_data.schema.getName(null);
		String normalized_sch_name = sch_name.substring(0,1).toUpperCase() + sch_name.substring(1).toLowerCase();
		String str = SdaiSession.SCHEMA_PREFIX + normalized_sch_name + ".S" + normalized_sch_name;
		Class cl;
		try {
			cl = Class.forName(str, true, SdaiClassLoaderProvider.getDefault().getClassLoader());
		} catch (ClassNotFoundException ex) {
			throw new SdaiException(SdaiException.SY_ERR, "Special class for Express schema not found: " + str);
		}
		String method_name = WHERE_RULE_METHOD_NAME_PREFIX + normalise(rule_owner.getName(null)) +
			normalise(rule.getLabel(null));
		if (staticFields.paramw == null) {
			staticFields.paramw = new Class[2];
			staticFields.paramw[0] = SdaiContext.class;
			staticFields.paramw[1] = Value.class;
			staticFields.argw = new Object [2];
		}
		Method meth;
		try {
			meth = cl.getDeclaredMethod(method_name, staticFields.paramw);
		} catch (java.lang.NoSuchMethodException ex) {
			throw new SdaiException(SdaiException.SY_ERR, "Method " + method_name + " not found in class " + cl.getName());
		}

		SSuper sp =  sch_data.super_inst;
		int valid_res = ELogical.TRUE;
		SdaiSession ss = owning_model.repository.session;
		if (ss.sdai_context == null) {
			throw new SdaiException(SdaiException.SY_ERR, "SdaiContext shall be provided");
		}
		ASdaiModel saved_domain = ss.sdai_context.domain;
		ss.sdai_context.domain = domain;
		staticFields.argw[0] = ss.sdai_context;
		for (int i = 0; i < count; i++) {
			if (staticFields.attr_values[i].tag == Value.EXCPT) {
				ss.sdai_context.domain = saved_domain;
				return ELogical.FALSE;
			}
			staticFields.argw[1] = staticFields.attr_values[i];
			Object res;
			try {
				res = meth.invoke(sp, staticFields.argw);
			} catch (Exception ex) {
				throw new SdaiException(SdaiException.SY_ERR, ex);
			}
			int resi = ((Integer)res).intValue();
			if (resi == ELogical.FALSE) {
				ss.sdai_context.domain = saved_domain;
				return resi;
			} else if (resi == ELogical.UNKNOWN) {
				valid_res = resi;
			}
		}
		ss.sdai_context.domain = saved_domain;
		return valid_res;
	}


	int collect_where_rules(StaticFields staticFields, CDefined_type type,
			AWhere_rule viol_rules, Throwable excpt) throws SdaiException {
		int count = 0;
		DataType tp = (DataType)type;
		while (tp.express_type == DataType.DEFINED_TYPE) {
			AWhere_rule w_rules = ((CDefined_type)tp).getWhere_rules(null, null);
			if (((AEntity)w_rules).myLength == 0) {
				continue;
			}
			CWhere_rule [] w_rules_ord = ((DefinedType)tp).get_dt_where_rules(staticFields, w_rules);
			int ln = ((AEntity)w_rules).myLength;
			for (int i = 0; i < ln; i++) {
				EWhere_rule wrule = w_rules_ord[i];
				((WhereRule)wrule).exc = excpt;
				if (((AEntity)viol_rules).myType == null || ((AEntity)viol_rules).myType.express_type == DataType.LIST) {
					((AEntity)viol_rules).addAtTheEnd(wrule, null);
				} else {
					((AEntity)viol_rules).setForNonList(wrule, ((AEntity)viol_rules).myLength, null, null);
				}
			}
			count += ln;
			tp = (DataType)((CDefined_type)tp).getDomain(null);
		}
		return count;
	}


	private int verify_type(StaticFields staticFields, CEntityDefinition def, CDefined_type type) throws SdaiException {
		int i, k;
		if (def.def_types == null) {
			staticFields.context_schema = owning_model.underlying_schema;
			def.extract_def_types(staticFields);
			staticFields.context_schema = null;
		}
		if (def.def_types.length == 0) {
			return -1;
		}
		boolean select = false;
		if (staticFields.attr_values == null) {
			int ln;
			if (def.def_types.length > VALUE_ARRAY_SIZE) {
				ln = def.def_types.length;
			} else {
				ln = VALUE_ARRAY_SIZE;
			}
			staticFields.attr_values = new Value[ln];
			staticFields.marks = new boolean[ln];
		} else if (def.def_types.length > staticFields.attr_values.length) {
			staticFields.attr_values = new Value[def.def_types.length];
			staticFields.marks = new boolean[def.def_types.length];
		}
		for (i = 0; i < def.def_types.length; i++) {
			staticFields.marks[i] = false;
		}
		SdaiException sdaiex;
		Value val_except;
		int count = 0;
		for (i = 0; i < def.def_types.length; i++) {
			if (def.def_types_branch[i] == 0) {
				if (verify_types_path(def.def_types[i], type)) {
					try {
						staticFields.attr_values[count] = get(def.def_types_attr[i]);
					} catch (Throwable ex) {
						if (CATCH_EXCEPTIONS) {
							if (ex instanceof SdaiException) {
								sdaiex = (SdaiException)ex;
								if (sdaiex.getErrorId() == SdaiException.VA_NSET) {
									val_except = new Value();
									val_except.tag = Value.EXCPT;
									val_except.reference = ex;
									staticFields.attr_values[count] = val_except;
								} else {
									throw sdaiex;
								}
							} else if (ex instanceof StackOverflowError) {
								val_except = new Value();
								val_except.tag = Value.EXCPT;
								val_except.reference = new StackOverflowError("stack overflow");
								staticFields.attr_values[count] = val_except;
							} else {
								throw new SdaiException(SdaiException.SY_ERR, ex);
							}
						} else {
							throw new SdaiException(SdaiException.SY_ERR, ex);
						}
					}
					count++;
				}
				continue;
			}
			if (def.def_types_branch[i] == 1) {
				if (verify_types_path(def.def_types[i], type)) {
					try {
						staticFields.attr_values[count] = get(def.def_types_attr[i]);
					} catch (Throwable ex) {
						if (CATCH_EXCEPTIONS) {
							if (ex instanceof SdaiException) {
								sdaiex = (SdaiException)ex;
								if (sdaiex.getErrorId() == SdaiException.VA_NSET) {
									val_except = new Value();
									val_except.tag = Value.EXCPT;
									val_except.reference = ex;
									staticFields.attr_values[count] = val_except;
								} else {
									throw sdaiex;
								}
							} else if (ex instanceof StackOverflowError) {
								val_except = new Value();
								val_except.tag = Value.EXCPT;
								val_except.reference = new StackOverflowError("stack overflow");
								staticFields.attr_values[count] = val_except;
							} else {
								throw new SdaiException(SdaiException.SY_ERR, ex);
							}
						} else {
							throw new SdaiException(SdaiException.SY_ERR, ex);
						}
					}
					count++;
					for (k = i + 1; k < def.def_types.length; k++) {
						if (def.def_types_attr[k] == def.def_types_attr[i]) {
							staticFields.marks[k] = true;
						}
					}
				}
				continue;
			}
			if (staticFields.marks[i]) {
				continue;
			}
			select = true;
			if (staticFields.df_types == null) {
				staticFields.df_types = new EDefined_type[TYPES_ARRAY_SIZE];
			}
			int res = testAttribute(def.def_types_attr[i], staticFields.df_types);
			int j = 0;
			boolean found = false;
			while (staticFields.df_types[j] != null) {
				for (k = i; k < def.def_types.length; k++) {
					if (def.def_types_attr[k] == def.def_types_attr[i]) {
						if (def.def_types[k] == staticFields.df_types[j] && verify_types_path((CDefined_type)staticFields.df_types[j], type)) {
							found = true;
						}
						staticFields.marks[k] = true;
					}
				}
				j++;
			}
			if (found) {
				try {
					staticFields.attr_values[count] = get(def.def_types_attr[i]);
				} catch (Throwable ex) {
					if (CATCH_EXCEPTIONS) {
						if (ex instanceof SdaiException) {
							sdaiex = (SdaiException)ex;
							if (sdaiex.getErrorId() == SdaiException.VA_NSET) {
								val_except = new Value();
								val_except.tag = Value.EXCPT;
								val_except.reference = ex;
								staticFields.attr_values[count] = val_except;
							} else {
								throw sdaiex;
							}
						} else if (ex instanceof StackOverflowError) {
							val_except = new Value();
							val_except.tag = Value.EXCPT;
							val_except.reference = new StackOverflowError("stack overflow");
							staticFields.attr_values[count] = val_except;
						} else {
							throw new SdaiException(SdaiException.SY_ERR, ex);
						}
					} else {
						throw new SdaiException(SdaiException.SY_ERR, ex);
					}
				}
				count++;
			}
		}
		if (count > 0) {
			return count;
		} else if (select) {
			return 0;
		} else {
			return -1;
		}
	}


	boolean verify_types_path(CDefined_type init_type, CDefined_type type) throws SdaiException {
		DataType tp = (DataType)init_type;
		while (tp.express_type == DataType.DEFINED_TYPE) {
			if (tp == type) {
				return true;
			}
			tp = (DataType)((CDefined_type)tp).getDomain(null);
		}
		return false;
	}


	public boolean validateRequiredExplicitAttributesAssigned(AAttribute nonConf)
			throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (nonConf == null) {
			throw new SdaiException(SdaiException.AI_NEXS);
		}
		CEntity_definition def = (CEntity_definition)getInstanceType();
		return validateRequiredExplicitAttributesAssigned(def, nonConf);
//		} // syncObject
	}


/**
	This method is invoked in the public method with the same name.
*/
	private boolean validateRequiredExplicitAttributesAssigned(CEntity_definition edef, AAttribute nonConf)
			throws SdaiException {
		boolean return_value = true;
		int i, j;
		CExplicit_attribute attr;
		CEntityDefinition entity;
		int count = -1;
		SSuper ssuper = null;
		if (owning_model.described_schema != null) {
			ssuper = edef.ssuper;
		}
		StaticFields staticFields = StaticFields.get();
		int ln = 0;
		for (j = 0; j < edef.noOfPartialEntityTypes; j++) {
			if (edef.complex == 2) {
				entity = edef.partialEntityTypes[j];
			} else {
				entity = edef.partialEntityTypes[edef.externalMappingIndexing[j]];
			}
			CExplicit_attribute [] expl_attr_arr = entity.takeExplicit_attributes();
			int test_result;
			for (i = 0; i < expl_attr_arr.length; i++) {
				attr = expl_attr_arr[i];
//System.out.println("    !!!!!!!!!!!!!!!!! attribute: " + attr.getName(null));
				count++;
				if (((CEntityDefinition)edef).checkIfDerived(attr)) {
					continue;
				}
				if (attr.getOptional_flag(null)) {
					continue;
				}
				test_result = validate_attribute(edef, attr, count, ssuper);
				if (test_result <= 0) {
					return_value = false;
					if (staticFields.roles == null) {
						staticFields.roles = new EAttribute[ROLES_ARRAY_SIZE];
					} else if (ln >= staticFields.roles.length) {
						ensureRolesCapacity(staticFields);
					}
					staticFields.roles[ln] = attr;
					ln++;
					if (((AEntity)nonConf).myType == null || ((AEntity)nonConf).myType.express_type == DataType.LIST) {
						((AEntity)nonConf).addAtTheEnd(attr, null);
					} else {
						((AEntity)nonConf).setForNonList(attr, ((AEntity)nonConf).myLength, null, null);
					}
				}
			}
		}
		for (j = 0; j < edef.noOfPartialEntityTypes; j++) {
			if (edef.complex == 2) {
				entity = edef.partialEntityTypes[j];
			} else {
				entity = edef.partialEntityTypes[edef.externalMappingIndexing[j]];
			}
			for (i = 0; i < entity.attributesRedecl.length; i++) {
				EAttribute attrib = entity.attributesRedecl[i];
				if (((AttributeDefinition)attrib).attr_tp != AttributeDefinition.EXPLICIT) {
					continue;
				}
				if (validateRedeclaringAttributesAssigned(staticFields, (CExplicit_attribute)attrib, ln) != null) {
//System.out.println("CEntity   +++++ this: " + this + "    attrib: " + attrib +
//"   its owner: " + ((CEntity)attrib).owning_model.name);
					if (((AEntity)nonConf).myType == null || ((AEntity)nonConf).myType.express_type == DataType.LIST) {
						((AEntity)nonConf).addAtTheEnd(attrib, null);
					} else {
						((AEntity)nonConf).setForNonList(attrib, ((AEntity)nonConf).myLength, null, null);
					}
				}
			}
		}
		return return_value;
	}


	private int validate_attribute(CEntity_definition edef, CExplicit_attribute attr, int index, SSuper ssuper)
			throws SdaiException {
		int test_result;
		SelectType sel_type = take_select(attr);
		if (sel_type == null) {
			test_result = find_type(attr);
		} else if (sel_type.is_mixed == 0) {
			test_result = 1;
		} else {
			test_result = -1;
		}
		Field field = ((CEntityDefinition)edef).attributeFields[index];
		if (owning_model.described_schema == null) {
			ssuper = ((CEntityDefinition)edef).fieldOwners[index].ssuper;
		}
		int val_int;
		try {
			switch (test_result) {
				case -1:
					Object val_object = ssuper.getObject(this, field);
					if (val_object == null) {
						test_result = 0;
					} else if (val_object instanceof Integer) {
						val_int = ((Integer)val_object).intValue();
						if (val_int == Integer.MIN_VALUE) {
							test_result = 0;
						} else {
							test_result = 1;
						}
					} else if (val_object instanceof Double) {
						if (((Double)val_object).isNaN()) {
							test_result = 0;
						} else {
							test_result = 1;
						}
					} else {
						test_result = 1;
					}
					break;
				case  0:
					break;
				case  1:
//System.out.println("  Entity: #" + instance_identifier + "   type: " +
//this.getInstanceType().getName(null) + "   field: " + field.getName() + "   CASE 2");
//Class cll = field.getDeclaringClass();
//System.out.println("  this: " + this.getClass().getName() +
//"  fields owner: " + cll.getName() + "  field pointer: " + field);
					val_object = ssuper.getObject(this, field);
					if (val_object == null) {
						test_result = 0;
					}
					break;
				case  2:
				case  5:
					val_int = ssuper.getInt(this, field);
					if (val_int == 0) {
						test_result = 0;
					}
					break;
				case  3:
					double val_double = ssuper.getDouble(this, field);
					if (Double.isNaN(val_double)) {
						test_result = 0;
					}
					break;
				case  4:
					val_int = ssuper.getInt(this, field);
					if (val_int == Integer.MIN_VALUE) {
						test_result = 0;
					}
					break;
			}
		} catch (java.lang.IllegalAccessException ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
		return test_result;
	}


	private CExplicit_attribute validateRedeclaringAttributesAssigned(StaticFields staticFields,
			CExplicit_attribute red_attr, int ln) throws SdaiException {
		if (red_attr.getOptional_flag(null)) {
			return null;
		}
		CExplicit_attribute attr = (CExplicit_attribute)red_attr.getRedeclaring(null);
		while (attr.testRedeclaring(null)) {
			attr = (CExplicit_attribute)attr.getRedeclaring(null);
		}
		for (int i = 0; i < ln; i++) {
			if (staticFields.roles[i] == attr) {
				return attr;
			}
		}
		return null;
	}


	public boolean validateInverseAttributes(AAttribute nonConf) throws SdaiException {
//		synchronized (syncObject) {
		return validateInverseAttributes(nonConf, null);
/*		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (nonConf == null) {
			throw new SdaiException(SdaiException.AI_NEXS);
		}
		CEntity_definition def = (CEntity_definition)getInstanceType();
		markInverseAttributes(def);
		return validateInverseAttributes(def, nonConf, null);*/
//		} // syncObject
	}


	public boolean validateInverseAttributes(AAttribute nonConf, ASdaiModel domain) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (nonConf == null) {
			throw new SdaiException(SdaiException.AI_NEXS);
		}
		CEntity_definition def = (CEntity_definition)getInstanceType();
		markInverseAttributes(def);
		return validateInverseAttributes(def, nonConf, domain);
//		} // syncObject
	}


/**
	This method is invoked in the public method with the same name.
*/
	private boolean validateInverseAttributes(CEntity_definition edef, AAttribute nonConf, ASdaiModel domain)
			throws SdaiException {
		boolean return_value = true;
		CEntityDefinition entity;
		for (int j = 0; j < edef.noOfPartialEntityTypes; j++) {
			if (edef.complex == 2) {
				entity = edef.partialEntityTypes[j];
			} else {
				entity = edef.partialEntityTypes[edef.externalMappingIndexing[j]];
			}
			CInverse_attribute [] inv_attr_arr = entity.takeInverse_attributesAll();
			if (inv_attr_arr == null) {
				continue;
			}
			for (int i = 0; i < inv_attr_arr.length; i++) {
				CInverse_attribute attri = inv_attr_arr[i];
            if (((((CEntity)attri).instance_position & POS_MASK) != POS_MASK) && ((((CEntity)attri).instance_position & POS_MASK) != 0)) {   //--VV--
					continue;
				}
				EBound lbound = null, ubound = null;
				if (attri.testMin_cardinality(null)) {
					lbound = attri.getMin_cardinality(null);
				}
				if (attri.testMax_cardinality(null)) {
					ubound = attri.getMax_cardinality(null);
				}
//				if (lbound == null && ubound == null) {
//					continue;
//				}
				Object value = get_inverse(attri, domain);
				if (!(value instanceof CAggregate)) {
					String base = SdaiSession.line_separator + AdditionalMessages.AT_EINV;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				int count = ((CAggregate)value).myLength;
/*				AggregationType type = ((CAggregate)value).myType;
			if (!(type instanceof CSet_type || type instanceof CBag_type)) {
				String base = SdaiSession.line_separator + AdditionalMessages.AT_EINV;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}*/
				boolean violated = false;
				if (lbound == null) {
//					String base = SdaiSession.line_separator + AdditionalMessages.AT_EINV;
//					throw new SdaiException(SdaiException.SY_ERR, base);
					if (count != 1) {
						violated = true;
					}
				} else {
					if (lbound.getBound_value(null) > count) {
						violated = true;
					}
					if (ubound != null) {
						if (ubound.getBound_value(null) < count) {
							violated = true;
						}
					}
				}
				if (violated) {
					return_value = false;
					if (((AEntity)nonConf).myType == null || ((AEntity)nonConf).myType.express_type == DataType.LIST) {
						((AEntity)nonConf).addAtTheEnd(attri, null);
					} else {
						((AEntity)nonConf).setForNonList(attri, ((AEntity)nonConf).myLength, null, null);
					}
				}
			}
		}
		return return_value;
	}


	private void markInverseAttributes(CEntity_definition edef) throws SdaiException {
		int i, j;
		CInverse_attribute attri;
		CInverse_attribute [] inv_attr_arr;
		CEntityDefinition entity;
		for (j = 0; j < edef.noOfPartialEntityTypes; j++) {
			if (edef.complex == 2) {
				entity = edef.partialEntityTypes[j];
			} else {
				entity = edef.partialEntityTypes[edef.externalMappingIndexing[j]];
			}
			inv_attr_arr = entity.takeInverse_attributesAll();
			if (inv_attr_arr == null) {
				continue;
			}
			for (i = 0; i < inv_attr_arr.length; i++) {
				attri = inv_attr_arr[i];
//				((CEntity)attri).instance_position = 0;
            ((CEntity)attri).instance_position = ((CEntity)attri).instance_position & FLG_MASK; //--VV--
			}
		}
		for (j = 0; j < edef.noOfPartialEntityTypes; j++) {
			if (edef.complex == 2) {
				entity = edef.partialEntityTypes[j];
			} else {
				entity = edef.partialEntityTypes[edef.externalMappingIndexing[j]];
			}
			inv_attr_arr = entity.takeInverse_attributesAll();
			if (inv_attr_arr == null) {
				continue;
			}
			for (i = 0; i < inv_attr_arr.length; i++) {
				attri = inv_attr_arr[i];
				if (attri.testRedeclaring(null)) {
					//((CEntity)attri.getRedeclaring(null)).instance_position = 1;
            	    ((CEntity)attri.getRedeclaring(null)).instance_position = (((CEntity)attri.getRedeclaring(null)).instance_position & FLG_MASK) | 1;    //--VV--
				}
			}
		}
	}


	public int validateExplicitAttributesReferences(AAttribute nonConf) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (nonConf == null) {
			throw new SdaiException(SdaiException.AI_NEXS);
		}
		CEntity_definition def = (CEntity_definition)getInstanceType();
		return validateExplicitAttributesReferences(def, nonConf);
//		} // syncObject
	}


/**
	This method is invoked in the public method with the same name.
*/
	private int validateExplicitAttributesReferences(CEntity_definition edef, AAttribute nonConf)
			throws SdaiException {
		int return_value = ELogical.TRUE;
		int i;
		int check_result;
		CExplicit_attribute attr;
		CEntityDefinition entity;
		int count = -1;
		SSuper ssuper = null;
		if (owning_model.described_schema != null) {
			ssuper = edef.ssuper;
		}
		StaticFields staticFields = StaticFields.get();
		staticFields.context_schema = owning_model.underlying_schema;
		for (int j = 0; j < edef.noOfPartialEntityTypes; j++) {
			if (edef.complex == 2) {
				entity = edef.partialEntityTypes[j];
			} else {
				entity = edef.partialEntityTypes[edef.externalMappingIndexing[j]];
			}
//if (instance_identifier == 381)
//System.out.println("CEntity  =================  entity: " + entity +
//"  its schema: " + ((CEntity)entity).owning_model.name);
			CExplicit_attribute [] expl_attr_arr = entity.takeExplicit_attributes();
			for (i = 0; i < expl_attr_arr.length; i++) {
				attr = expl_attr_arr[i];
				count++;
//if (instance_identifier == 381)
//System.out.println("CEntity  OOOOOOOOOOOOOOOO  attr: " + attr +
//"  its schema: " + ((CEntity)attr).owning_model.name + "   count: " + count);
				if (((CEntityDefinition)edef).checkIfDerived(attr)) {
					continue;
				}
				check_result = validate_attribute_for_references(edef, attr, attr, count, ssuper);
				if (check_result <= 0) {
					return_value = ELogical.FALSE;
					if (((AEntity)nonConf).myType == null || ((AEntity)nonConf).myType.express_type == DataType.LIST) {
						((AEntity)nonConf).addAtTheEnd(attr, null);
					} else {
						((AEntity)nonConf).setForNonList(attr, ((AEntity)nonConf).myLength, null, null);
					}
				} else if (check_result == 2) {
					if (return_value == ELogical.TRUE) {
						return_value = ELogical.UNKNOWN;
					}
				}
			}

			for (i = 0; i < entity.attributesRedecl.length; i++) {
				EAttribute attrib = entity.attributesRedecl[i];
//if (instance_identifier == 381)
//System.out.println("CEntity  RRRRRRRRRRRRRR  attrib: " + attrib +
//"  its schema: " + ((CEntity)attrib).owning_model.name + "   count: " + count);
				if (((AttributeDefinition)attrib).attr_tp != AttributeDefinition.EXPLICIT) {
					continue;
				}
				int r_attr_ind = validateRedeclaringAttributesSource(edef, (CExplicit_attribute)attrib);
				if (r_attr_ind < 0) {
					continue;
				}
				check_result = validate_attribute_for_references(edef, (CExplicit_attribute)attrib,
					((CEntityDefinition)edef).attributes[r_attr_ind], r_attr_ind, ssuper);
				if (check_result <= 0) {
					return_value = ELogical.FALSE;
					if (((AEntity)nonConf).myType == null || ((AEntity)nonConf).myType.express_type == DataType.LIST) {
						((AEntity)nonConf).addAtTheEnd(attrib, null);
					} else {
						((AEntity)nonConf).setForNonList(attrib, ((AEntity)nonConf).myLength, null, null);
					}
				} else if (check_result == 2) {
					if (return_value == ELogical.TRUE) {
						return_value = ELogical.UNKNOWN;
					}
				}
			}

		}
		staticFields.context_schema = null;
		return return_value;
	}


	private int validate_attribute_for_references(CEntity_definition edef, CExplicit_attribute attr,
			CExplicit_attribute redecl_attr, int index, SSuper ssuper) throws SdaiException {
		SelectType sel_type = take_select(attr);
		DataType type = (DataType)attr.getDomain(null);
		boolean entity_pos;
		if (sel_type == null) {
			entity_pos = type.allow_entity();
		} else {
			entity_pos = sel_type.allow_entity_select();
		}
		if (!entity_pos) {
			return 1;
		}
		Field field = ((CEntityDefinition)edef).attributeFields[index];
		if (owning_model.described_schema == null) {
			ssuper = ((CEntityDefinition)edef).fieldOwners[index].ssuper;
		}
		Object value;
		try {
			value = ssuper.getObject(this, field);
		} catch (Exception ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
		if (value instanceof SdaiModel.Connector) {
			value = resolveAndReplaceConnector((SdaiModel.Connector)value, redecl_attr);
		}
		if (value == null) {
			if (attr.getOptional_flag(null)) {
				return 1;
			} else {
				return 2;
			}
		} else if (value instanceof CEntity) {
			if (((CEntity)value).validate_entity(type, sel_type)) {
				return 1;
			} else {
				return 0;
			}
		} else if (value instanceof CAggregate) {
			return ((CAggregate)value).validate_instances_aggregate();
		} else {
			return 1;
		}
	}


	boolean validate_entity(DataType type, SelectType sel_type) throws SdaiException {
		CEntity_definition def_for_value = (CEntity_definition)getInstanceType();
		if (sel_type == null) {
			while (type.express_type == DataType.DEFINED_TYPE) 	{
				type = (DataType)((CDefined_type)type).getDomain(null);
			}
			if (type.express_type != DataType.ENTITY) {
				String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			if (def_for_value.isSubtypeOf((CEntity_definition)type)) {
				return true;
			} else {
				return false;
			}
		} else {
			return sel_type.check_instance_in_select(owning_model, def_for_value);
		}
	}


	private int validateRedeclaringAttributesSource(CEntityDefinition edef, CExplicit_attribute red_attr)
			throws SdaiException {
		if (edef.checkIfDerived(red_attr)) {
			return -1;
		}
		CExplicit_attribute attr = (CExplicit_attribute)red_attr.getRedeclaring(null);
		while (attr.testRedeclaring(null)) {
			attr = (CExplicit_attribute)attr.getRedeclaring(null);
		}
		for (int i = 0; i < edef.attributes.length; i++) {
			if (edef.attributes[i] == attr) {
				if (edef.fieldOwners[i] != null) {
					return i;
				} else {
					return -1;
				}
			}
		}
		return -1;
	}


	public int validateAggregatesSize(AAttribute nonConf) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (nonConf == null) {
			throw new SdaiException(SdaiException.AI_NEXS);
		}
		CEntity_definition def = (CEntity_definition)getInstanceType();
		return validateAggregateSize(def, nonConf);
//		} // syncObject
	}


/**
	This method is invoked in the public method with the same name.
*/
	private int validateAggregateSize(CEntity_definition def, AAttribute nonConf)
			throws SdaiException {
		int return_value = ELogical.TRUE;
		int i;
		int check_result;
		CExplicit_attribute attr;
		CEntityDefinition entity;
		int index = -1;
		SSuper ssuper = null;
		if (owning_model.described_schema != null) {
			ssuper = def.ssuper;
		}
		for (int j = 0; j < def.noOfPartialEntityTypes; j++) {
			if (def.complex == 2) {
				entity = def.partialEntityTypes[j];
			} else {
				entity = def.partialEntityTypes[def.externalMappingIndexing[j]];
			}
			CExplicit_attribute [] expl_attr_arr = entity.takeExplicit_attributes();
			for (i = 0; i < expl_attr_arr.length; i++) {
				attr = expl_attr_arr[i];
				index++;
				if (((CEntityDefinition)def).checkIfDerived(attr)) {
					continue;
				}
				check_result = validateAggregateSizeAttribute((CEntityDefinition)def, attr, attr, index, ssuper);
				if (check_result == 0) {
					return_value = ELogical.FALSE;
					if (((AEntity)nonConf).myType == null || ((AEntity)nonConf).myType.express_type == DataType.LIST) {
						((AEntity)nonConf).addAtTheEnd(attr, null);
					} else {
						((AEntity)nonConf).setForNonList(attr, ((AEntity)nonConf).myLength, null, null);
					}
				} else if (check_result == 2) {
					if (return_value == ELogical.TRUE) {
						return_value = ELogical.UNKNOWN;
					}
				}
			}

			for (i = 0; i < entity.attributesRedecl.length; i++) {
				EAttribute attrib = entity.attributesRedecl[i];
				if (((AttributeDefinition)attrib).attr_tp != AttributeDefinition.EXPLICIT) {
					continue;
				}
				int r_attr_ind = validateRedeclaringAttributesSource((CEntityDefinition)def, (CExplicit_attribute)attrib);
				if (r_attr_ind < 0) {
					continue;
				}
				check_result = validateAggregateSizeAttribute((CEntityDefinition)def, (CExplicit_attribute)attrib,
					((CEntityDefinition)def).attributes[r_attr_ind], r_attr_ind, ssuper);
				if (check_result == 0) {
					return_value = ELogical.FALSE;
					if (((AEntity)nonConf).myType == null || ((AEntity)nonConf).myType.express_type == DataType.LIST) {
						((AEntity)nonConf).addAtTheEnd(attrib, null);
					} else {
						((AEntity)nonConf).setForNonList(attrib, ((AEntity)nonConf).myLength, null, null);
					}
				} else if (check_result == 2) {
					if (return_value == ELogical.TRUE) {
						return_value = ELogical.UNKNOWN;
					}
				}
			}

		}
		return return_value;
	}


	private int validateAggregateSizeAttribute(CEntityDefinition def, CExplicit_attribute attr,
			CExplicit_attribute redecl_attr, int index, SSuper ssuper) throws SdaiException {
		int check_result;
		AggregationType aggr_type = null;
		SelectType sel_type = take_select(attr);
		if (sel_type == null) {
			aggr_type = find_aggr_type_for_validation(attr);
			if (aggr_type == null) {
				return 1;
			}
		}
		Field field = def.attributeFields[index];
		if (owning_model.described_schema == null) {
			ssuper = def.fieldOwners[index].ssuper;
		}
		Object value;
		try {
			value = ssuper.getObject(this, field);
		} catch (Exception ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
		if (value instanceof SdaiModel.Connector) {
			value = resolveAndReplaceConnector((SdaiModel.Connector)value, redecl_attr);
		}

		int count;
		if (value instanceof CAggregate) {
			if (sel_type != null) {
				aggr_type = ((CAggregate)value).myType;
			}
			count = ((CAggregate)value).myLength;
		} else if (value instanceof A_double3) {
			if (sel_type != null) {
				aggr_type = ((A_double3)value).myType;
			}
			count = ((A_double3)value).myLength;
		} else if (value instanceof A_double) {
			if (sel_type != null) {
				aggr_type = ((A_double)value).myType;
			}
			count = ((A_double)value).myLength;
		} else if (value instanceof A_integerPrimitive) {
			if (sel_type != null) {
				aggr_type = ((A_integerPrimitive)value).myType;
			}
			count = ((A_integerPrimitive)value).myLength;
		} else {
			return 1;
		}

		if (aggr_type.express_type == DataType.ARRAY) {
			EBound lindex, uindex;
			int differ;
			lindex = ((EArray_type)aggr_type).getLower_index(null);
			uindex = ((EArray_type)aggr_type).getUpper_index(null);
			if (lindex == null || uindex == null) {
				check_result = 2;
			} else {
				differ = uindex.getBound_value(null) - lindex.getBound_value(null);
				if (count > differ + 1) {
					check_result = 0;
				} else {
					check_result = 1;
				}
			}
		} else {
			check_result = 1;
			EBound lbound, ubound = null;
			lbound = ((EVariable_size_aggregation_type)aggr_type).getLower_bound(null);
			if (((EVariable_size_aggregation_type)aggr_type).testUpper_bound(null)) {
				ubound = ((EVariable_size_aggregation_type)aggr_type).getUpper_bound(null);
			}
			if (lbound == null) {
				String base = SdaiSession.line_separator + AdditionalMessages.AI_INCB;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			if (lbound.getBound_value(null) > count) {
				check_result = 0;
			}
			if (ubound != null) {
				if (ubound.getBound_value(null) < count) {
					check_result = 0;
				}
			}
		}
		return check_result;
	}


/**
	If a given attribute is an aggregate, then returns the aggregation type
	of its elements. If the type of the attribute is a select type, then
	SdaiException AT_NVLD is thrown. Otherwise, the return value is <code>null</code>.
	This method is invoked in <code>validateAggregateSize</code>,
	<code>validateAggregatesUniqueness</code> and <code>validateArrayNotOptional</code>
	methods.
*/
	private AggregationType find_aggr_type_for_validation(EAttribute attribute) throws SdaiException {
		if (((AttributeDefinition)attribute).attr_tp == AttributeDefinition.INVERSE ||
				((AttributeDefinition)attribute).attr_tp == AttributeDefinition.DERIVED) {
			return null;
		}
		DataType type = (DataType)((CExplicit_attribute)attribute).getDomain(null);
		if (type.express_type >= DataType.LIST && type.express_type <= DataType.AGGREGATE) {
			return (AggregationType)type;
		} else if (type.express_type == DataType.ENTITY ||
				(type.express_type >= DataType.NUMBER && type.express_type <= DataType.BINARY) ) {
			return null;
		} else if (type.express_type != DataType.DEFINED_TYPE) {
			String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		while (type.express_type == DataType.DEFINED_TYPE) {
			type = (DataType)((CDefined_type)type).getDomain(null);
			if (type.express_type >= DataType.SELECT && type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
				String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
				throw new SdaiException(SdaiException.SY_ERR, base);
			} else if (type.express_type >= DataType.LIST && type.express_type <= DataType.AGGREGATE) {
				return (AggregationType)type;
			}
		}
		return null;
	}


	public boolean validateAggregatesUniqueness(AAttribute nonConf) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (nonConf == null) {
			throw new SdaiException(SdaiException.AI_NEXS);
		}
		CEntity_definition def = (CEntity_definition)getInstanceType();
		return validateAggregatesUniqueness(def, nonConf);
//		} // syncObject
	}


/**
	This method is invoked in the public method with the same name.
*/
	private boolean validateAggregatesUniqueness(CEntity_definition def, AAttribute nonConf)
			throws SdaiException {
		int i;
		boolean return_value = true;
		CExplicit_attribute attr;
		CEntityDefinition entity;
		int count = -1;
		SSuper ssuper = null;
		if (owning_model.described_schema != null) {
			ssuper = def.ssuper;
		}
		boolean violated;
		for (int n = 0; n < def.noOfPartialEntityTypes; n++) {
			if (def.complex == 2) {
				entity = def.partialEntityTypes[n];
			} else {
				entity = def.partialEntityTypes[def.externalMappingIndexing[n]];
			}
			CExplicit_attribute [] expl_attr_arr = entity.takeExplicit_attributes();
			for (i = 0; i < expl_attr_arr.length; i++) {
				attr = expl_attr_arr[i];
				count++;
				if (((CEntityDefinition)def).checkIfDerived(attr)) {
					continue;
				}
				violated = validateAggregatesUniquenessAttribute((CEntityDefinition)def, attr, attr, count, ssuper);
				if (violated) {
					return_value = false;
					if (((AEntity)nonConf).myType == null || ((AEntity)nonConf).myType.express_type == DataType.LIST) {
						((AEntity)nonConf).addAtTheEnd(attr, null);
					} else {
						((AEntity)nonConf).setForNonList(attr, ((AEntity)nonConf).myLength, null, null);
					}
				}
			}

			for (i = 0; i < entity.attributesRedecl.length; i++) {
				EAttribute attrib = entity.attributesRedecl[i];
				if (((AttributeDefinition)attrib).attr_tp != AttributeDefinition.EXPLICIT) {
					continue;
				}
				int r_attr_ind = validateRedeclaringAttributesSource((CEntityDefinition)def, (CExplicit_attribute)attrib);
				if (r_attr_ind < 0) {
					continue;
				}
				violated = validateAggregatesUniquenessAttribute((CEntityDefinition)def, (CExplicit_attribute)attrib,
					((CEntityDefinition)def).attributes[r_attr_ind], r_attr_ind, ssuper);
				if (violated) {
					return_value = false;
					if (((AEntity)nonConf).myType == null || ((AEntity)nonConf).myType.express_type == DataType.LIST) {
						((AEntity)nonConf).addAtTheEnd(attrib, null);
					} else {
						((AEntity)nonConf).setForNonList(attrib, ((AEntity)nonConf).myLength, null, null);
					}
				}
			}

		}
		return return_value;
	}


	private boolean validateAggregatesUniquenessAttribute(CEntityDefinition def, CExplicit_attribute attr,
			CExplicit_attribute redecl_attr, int index, SSuper ssuper) throws SdaiException {
		AggregationType aggr_type = null;
		SelectType sel_type = take_select(attr);
		if (sel_type == null) {
			aggr_type = find_aggr_type_for_validation(attr);
			if (aggr_type == null) {
				return false;
			}
		}
		Field field = def.attributeFields[index];
		if (owning_model.described_schema == null) {
			ssuper = def.fieldOwners[index].ssuper;
		}
		Object value;
		try {
			value = ssuper.getObject(this, field);
		} catch (Exception ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
		if (value instanceof SdaiModel.Connector) {
			value = resolveAndReplaceConnector((SdaiModel.Connector)value, redecl_attr);
		}
		boolean violated;
		if (value instanceof CAggregate) {
			if (sel_type != null) {
				aggr_type = ((CAggregate)value).myType;
			}
			violated = ((CAggregate)value).checkUniquenessViolation(aggr_type);
		} else if (value instanceof A_double3) {
			if (sel_type != null) {
				aggr_type = ((A_double3)value).myType;
			}
			violated = ((A_double3)value).checkUniquenessViolation(aggr_type);
		} else if (value instanceof A_double) {
			if (sel_type != null) {
				aggr_type = ((A_double)value).myType;
			}
			violated = ((A_double)value).checkUniquenessViolation(aggr_type);
		} else if (value instanceof A_integerPrimitive) {
			if (sel_type != null) {
				aggr_type = ((A_integerPrimitive)value).myType;
			}
			violated = ((A_integerPrimitive)value).checkUniquenessViolation(aggr_type);
		} else {
			return false;
		}
		return violated;
	}


	public boolean validateArrayNotOptional(AAttribute nonConf) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (nonConf == null) {
			throw new SdaiException(SdaiException.AI_NEXS);
		}
		CEntity_definition def = (CEntity_definition)getInstanceType();
		return validateArrayNotOptional(def, nonConf);
//		} // syncObject
	}


/**
	This method is invoked in the public method with the same name.
*/
	private boolean validateArrayNotOptional(CEntity_definition def, AAttribute nonConf)
			throws SdaiException {
		int i;
		boolean return_value = true;
		CExplicit_attribute attr;
		CEntityDefinition entity;
		int count = -1;
		SSuper ssuper = null;
		if (owning_model.described_schema != null) {
			ssuper = def.ssuper;
		}
		boolean violated;
		for (int n = 0; n < def.noOfPartialEntityTypes; n++) {
			if (def.complex == 2) {
				entity = def.partialEntityTypes[n];
			} else {
				entity = def.partialEntityTypes[def.externalMappingIndexing[n]];
			}
			CExplicit_attribute [] expl_attr_arr = entity.takeExplicit_attributes();
			for (i = 0; i < expl_attr_arr.length; i++) {
				attr = expl_attr_arr[i];
				count++;
				if (((CEntityDefinition)def).checkIfDerived(attr)) {
					continue;
				}
				violated = validateArrayNotOptionalAttribute((CEntityDefinition)def, attr, attr, count, ssuper);
				if (violated) {
					return_value = false;
					if (((AEntity)nonConf).myType == null || ((AEntity)nonConf).myType.express_type == DataType.LIST) {
						((AEntity)nonConf).addAtTheEnd(attr, null);
					} else {
						((AEntity)nonConf).setForNonList(attr, ((AEntity)nonConf).myLength, null, null);
					}
				}
			}

			for (i = 0; i < entity.attributesRedecl.length; i++) {
				EAttribute attrib = entity.attributesRedecl[i];
				if (((AttributeDefinition)attrib).attr_tp != AttributeDefinition.EXPLICIT) {
					continue;
				}
				int r_attr_ind = validateRedeclaringAttributesSource((CEntityDefinition)def, (CExplicit_attribute)attrib);
				if (r_attr_ind < 0) {
					continue;
				}
				violated = validateArrayNotOptionalAttribute((CEntityDefinition)def, (CExplicit_attribute)attrib,
					((CEntityDefinition)def).attributes[r_attr_ind], r_attr_ind, ssuper);
				if (violated) {
					return_value = false;
					if (((AEntity)nonConf).myType == null || ((AEntity)nonConf).myType.express_type == DataType.LIST) {
						((AEntity)nonConf).addAtTheEnd(attrib, null);
					} else {
						((AEntity)nonConf).setForNonList(attrib, ((AEntity)nonConf).myLength, null, null);
					}
				}
			}

		}
		return return_value;
	}


	private boolean validateArrayNotOptionalAttribute(CEntityDefinition def, CExplicit_attribute attr,
			CExplicit_attribute redecl_attr, int index, SSuper ssuper) throws SdaiException {
		AggregationType aggr_type = null;
		SelectType sel_type = take_select(attr);
		if (sel_type == null) {
			aggr_type = find_aggr_type_for_validation(attr);
			if (aggr_type == null || aggr_type.express_type != DataType.ARRAY) {
				return false;
			}
		}
		Field field = def.attributeFields[index];
		if (owning_model.described_schema == null) {
			ssuper = def.fieldOwners[index].ssuper;
		}
		Object value;
		try {
			value = ssuper.getObject(this, field);
		} catch (Exception ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
		if (value instanceof SdaiModel.Connector) {
			value = resolveAndReplaceConnector((SdaiModel.Connector)value, redecl_attr);
		}
		boolean violated;
		if (value instanceof CAggregate) {
			if (sel_type != null) {
				aggr_type = ((CAggregate)value).myType;
				if (aggr_type.express_type != DataType.ARRAY) {
					return false;
				}
			}
			violated = ((CAggregate)value).checkOptionalMissing(aggr_type);
		} else if (value instanceof A_double3) {
			if (sel_type != null) {
				aggr_type = ((A_double3)value).myType;
				if (aggr_type.express_type != DataType.ARRAY) {
					return false;
				}
			}
			violated = ((A_double3)value).checkOptionalMissing(aggr_type);
		} else if (value instanceof A_double) {
			if (sel_type != null) {
				aggr_type = ((A_double)value).myType;
				if (aggr_type.express_type != DataType.ARRAY) {
					return false;
				}
			}
			violated = ((A_double)value).checkOptionalMissing(aggr_type);
		} else if (value instanceof A_integerPrimitive) {
			if (sel_type != null) {
				aggr_type = ((A_integerPrimitive)value).myType;
				if (aggr_type.express_type != DataType.ARRAY) {
					return false;
				}
			}
			violated = ((A_integerPrimitive)value).checkOptionalMissing(aggr_type);
		} else {
			return false;
		}
		return violated;
	}


	public int validateStringWidth(AAttribute nonConf) throws SdaiException {
		int i;
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (nonConf == null) {
			throw new SdaiException(SdaiException.AI_NEXS);
		}
		AEntity nonConf_agg = (AEntity)nonConf;

//		synchronized (syncObject) {
		CEntityDefinition def = (CEntityDefinition)getInstanceType();
		Object value;
		int ret_value = ELogical.TRUE;
		boolean attr_check_result;
		for (i = 0; i < def.attributes.length; i++) {
			if (def.attributeFields[i] == null) {
				continue;
			}
			CExplicit_attribute attribute = def.attributes[i];
			if (!look_for_string((EData_type)attribute.getDomain(null))) {
				continue;
			}
			Field field = def.attributeFields[i];
			SSuper ssuper;
			if (owning_model.described_schema == null) {
				ssuper = def.fieldOwners[i].ssuper;
			} else {
				ssuper = def.ssuper;
			}
			try {
				value = ssuper.getObject(this, field);
			} catch (Exception ex) {
				throw new SdaiException(SdaiException.SY_ERR, ex);
			}
			attr_check_result = true;
			if (value instanceof String) {
				attr_check_result = check_string_in_entity((String)value, attribute, def, i, ssuper);
			} else if (value instanceof A_string) {
				attr_check_result = ((A_string)value).check_A_string();
			} else if (value instanceof Aa_string) {
				attr_check_result = ((Aa_string)value).check_Aa_string();
			} else if (value instanceof Aaa_string) {
				attr_check_result = ((Aaa_string)value).check_Aaa_string();
			} else if (value instanceof CAggregate) {
				attr_check_result = ((CAggregate)value).check_string_in_aggregate();
			}
			if (!attr_check_result) {
				if (nonConf_agg.myType == null || nonConf_agg.myType.express_type == DataType.LIST) {
					nonConf_agg.addAtTheEnd(attribute, null);
				} else {
					nonConf_agg.setForNonList(attribute, nonConf_agg.myLength, null, null);
				}
				ret_value = ELogical.FALSE;
			}
		}

		if (def.attributesDerived != null) {
			for (i = 0; i < def.attributesDerived.length; i++) {
				value = get_derived(def, i);
				if (value == null) {
					if (ret_value == ELogical.TRUE) {
						ret_value = ELogical.UNKNOWN;
					}
					continue;
				}
				CDerived_attribute der_attribute = def.attributesDerived[i];
				attr_check_result = true;
				if (value instanceof String) {
					attr_check_result = check_string_in_entity_derived((String)value, der_attribute);
				} else if (value instanceof A_string) {
					attr_check_result = ((A_string)value).check_A_string();
				} else if (value instanceof Aa_string) {
					attr_check_result = ((Aa_string)value).check_Aa_string();
				} else if (value instanceof Aaa_string) {
					attr_check_result = ((Aaa_string)value).check_Aaa_string();
				} else if (value instanceof CAggregate) {
					attr_check_result = ((CAggregate)value).check_string_in_aggregate();
				}
				if (!attr_check_result) {
					if (nonConf_agg.myType == null || nonConf_agg.myType.express_type == DataType.LIST) {
						nonConf_agg.addAtTheEnd(der_attribute, null);
					} else {
						nonConf_agg.setForNonList(der_attribute, nonConf_agg.myLength, null, null);
					}
					ret_value = ELogical.FALSE;
				}
			}
		}

		return ret_value;
//		} // syncObject
	}


	private boolean look_for_string(EData_type type) throws SdaiException {
		DataType tp = (DataType)type;
		if (tp.express_type >= DataType.NUMBER && tp.express_type <= DataType.BINARY) {
			return look_for_string_simple(tp);
		} else if (tp.express_type >= DataType.LIST && tp.express_type <= DataType.AGGREGATE) {
			return look_for_string((EData_type)((EAggregation_type)tp).getElement_type(null));
		} else if (tp.express_type >= DataType.SELECT && tp.express_type <= DataType.ENT_EXT_EXT_SELECT) {
			AEntity sels = (AEntity)((ESelect_type)type).getSelections(null, owning_model.repository.session.sdai_context);
			if (sels.myLength < 0) {
				sels.resolveAllConnectors();
			}
			Object [] myDataA = null;
			if (sels.myLength > 1) {
				myDataA = (Object [])sels.myData;
			}
			for (int i = 0; i < sels.myLength; i++) {
				DataType sel_type;
				if (sels.myLength == 1) {
					sel_type = (DataType)sels.myData;
				} else {
					sel_type = (DataType)myDataA[i];
				}
				while (sel_type.express_type == DataType.DEFINED_TYPE) {
					sel_type = (DataType)((CDefined_type)sel_type).getDomain(null);
				}
				boolean res = look_for_string((EData_type)sel_type);
				if (res) {
					return res;
				}
			}
			return false;
		} else if (tp.express_type == DataType.DEFINED_TYPE) {
			while (tp.express_type == DataType.DEFINED_TYPE) {
				tp = (DataType)((CDefined_type)tp).getDomain(null);
			}
			return look_for_string((EData_type)tp);
		} else {
			return false;
		}
	}


	boolean look_for_string_simple(DataType type) throws SdaiException {
		if (type.express_type == DataType.STRING) {
			return ((CString_type)type).testWidth(null);
		}
		return false;
	}


	boolean check_string_in_entity(String value, CExplicit_attribute attribute, CEntityDefinition def,
			int index, SSuper ssuper) throws SdaiException {
		DataType type;
		SelectType sel_type = take_select(attribute);
		if (sel_type == null || sel_type.is_mixed == 0) {
			type = (DataType)attribute.getDomain(null);
			if (type.express_type == DataType.DEFINED_TYPE) {
				while (type.express_type == DataType.DEFINED_TYPE) {
					type = (DataType)((CDefined_type)type).getDomain(null);
				}
			}
			if (type.express_type != DataType.STRING) {
				throw new SdaiException(SdaiException.VT_NVLD);
			}
			return ((StringType)type).check_width(value);
		}
		int sel_number = 1;
		try {
			Field sel_field = def.attributeFieldSelects[index];
			sel_number = ssuper.getInt(this, sel_field);
		} catch (Exception ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
		if (sel_number <= 1) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		type = (DataType)sel_type.types[sel_number - 2];
		if (type.express_type != DataType.STRING) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		return ((StringType)type).check_width(value);
	}


	boolean check_string_in_entity_derived(String value, CDerived_attribute attribute) throws SdaiException {
		DataType	type = (DataType)attribute.getDomain(null);
		if (type.express_type == DataType.DEFINED_TYPE) {
			while (type.express_type == DataType.DEFINED_TYPE) {
				type = (DataType)((CDefined_type)type).getDomain(null);
			}
		}
		if (type.express_type != DataType.STRING) {
			return true;
		}
		return ((StringType)type).check_width(value);
	}


	public int validateBinaryWidth(AAttribute nonConf) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


	public int validateRealPrecision(AAttribute nonConf) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


	int validateInstance(AAttribute attrs_aggr, SdaiIterator it_rules, ASdaiModel models_dom) throws SdaiException {
		if ( (!validateRequiredExplicitAttributesAssigned(attrs_aggr))
				|| (!validateInverseAttributes(attrs_aggr))
				|| (!validateAggregatesUniqueness(attrs_aggr))
				|| (!validateArrayNotOptional(attrs_aggr)) ) {
			return ELogical.FALSE;
		}
		int valid_result = ELogical.TRUE;
		int res = validateExplicitAttributesReferences(attrs_aggr);
		if (res == ELogical.FALSE) {
			return ELogical.FALSE;
		} else if (res == ELogical.UNKNOWN) {
			valid_result = ELogical.UNKNOWN;
		}
		res = validateAggregatesSize(attrs_aggr);
		if (res == ELogical.FALSE) {
			return ELogical.FALSE;
		} else if (res == ELogical.UNKNOWN) {
			valid_result = ELogical.UNKNOWN;
		}
		res = validateStringWidth(attrs_aggr);
		if (res == ELogical.FALSE) {
			return ELogical.FALSE;
		} else if (res == ELogical.UNKNOWN) {
			valid_result = ELogical.UNKNOWN;
		}
//		res = validateBinaryWidth(attrs_aggr);
//		if (res == ELogical.FALSE) {
//			return ELogical.FALSE;
//		} else if (res == ELogical.UNKNOWN) {
//			valid_result = ELogical.UNKNOWN;
//		}
		CEntity_definition e_def = (CEntity_definition)getInstanceType();
		AWhere_rule w_rules = e_def.getWhere_rules(null, null);
		if (((AEntity)w_rules).myLength > 0) {
			if (it_rules == null) {
				it_rules = w_rules.createIterator();
			} else {
				w_rules.attachIterator(it_rules);
			}
			while(it_rules.next()) {
				EWhere_rule w_rule = w_rules.getCurrentMember(it_rules);
				res = validateWhereRule(w_rule, models_dom);
				if (res == ELogical.FALSE) {
					return ELogical.FALSE;
				} else if (res == ELogical.UNKNOWN) {
					valid_result = ELogical.UNKNOWN;
				}
			}
		}
		return valid_result;
	}



/*     Methods added to SDAI operations       */

	public String toString() {
//		synchronized (syncObject) {
		try {
			StaticFields staticFields = StaticFields.get();
			return getAsString(staticFields);
		} catch (SdaiException e) {
			SdaiSession.printStackTraceToLogWriter(e);
			return super.toString();
		}
//		} // syncObject
	}


/**
	This method is doing a job of the public method <code>toString</code>.
*/
	String getAsString(StaticFields staticFields) throws SdaiException {
		int i, j;
		int str_index = 0;
		int old_index;
		boolean first;
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (staticFields.ent_instance_as_string == null) {
			staticFields.ent_instance_as_string = new byte[PhFileWriter.NUMBER_OF_CHARACTERS_IN_INSTANCE];
		}
		boolean short_names = !owning_model.repository.suppress_short_names;
		staticFields.ent_instance_as_string[0] = PhFileReader.SPECIAL;
		str_index = instance_id_to_byte_array(staticFields);
		if (str_index + 1 >= staticFields.ent_instance_as_string.length) {
			enlarge_instance_string(staticFields, str_index + 1, str_index + 2);
		}
		staticFields.ent_instance_as_string[++str_index] = PhFileReader.EQUAL;
		CEntity_definition def = (CEntity_definition)getInstanceType();
		if (staticFields.entity_values == null) {
			staticFields.entity_values = new ComplexEntityValue();
		}
		SdaiModel derived_model_dict;
		if (this instanceof CEntity) {
			owning_model.prepareAll(staticFields.entity_values, def);
			derived_model_dict = ((CEntity)def).owning_model;
		} else {
			derived_model_dict = owning_model.underlying_schema.modelDictionary;
		}
		SchemaData schemaData;
//staticFields.entity_values.xim_special_substitute_instance = true;
		getAll(staticFields.entity_values);
//staticFields.entity_values.xim_special_substitute_instance = false;
		EntityValue pval;
		Value val;
		byte [] name;
		int index;
		int name_length;

		if (def.complex == 2) {
			if (str_index + 1 >= staticFields.ent_instance_as_string.length) {
				enlarge_instance_string(staticFields, str_index + 1, str_index + 2);
			}
			staticFields.ent_instance_as_string[++str_index] = PhFileReader.LEFT_PARENTHESIS;
//System.out.println(" entity: " + def.getName(null)
//+ "  noOfPartialEntityTypes = " + def.noOfPartialEntityTypes);
			for (j = 0; j < def.noOfPartialEntityTypes; j++) {
				pval = staticFields.entity_values.entityValues[j];
				if (pval == null /*|| pval.count <= 0 */) break;
				schemaData = pval.def.owning_model.schemaData;
//System.out.println(" partial entity: " + pval.def.getNameUpperCase());
//System.out.println("  CEntity schema for model: " + schemaData.model.name);
				index = schemaData.find_entity(0, schemaData.bNames.length - 1,
//					pval.def.getNameUpperCase());
					(CEntityDefinition)pval.def);
				if (index < 0 || index >= schemaData.bNames.length) {
					String base = SdaiSession.line_separator + AdditionalMessages.AI_ENNF;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				if (short_names) {
					index = schemaData.toShort[index];
					name =  schemaData.bShortNames[index];
					name_length = schemaData.bShortNames[index].length;
				} else {
					name =  schemaData.bNames[index];
					name_length = schemaData.bNames[index].length;
				}
				if (str_index + name_length + 2 >= staticFields.ent_instance_as_string.length) {
					enlarge_instance_string(staticFields, str_index + 1, str_index + name_length + 3);
				}
				for (i = 0; i < name_length; i++) {
					staticFields.ent_instance_as_string[++str_index] = name[i];
				}
				staticFields.ent_instance_as_string[++str_index] = PhFileReader.LEFT_PARENTHESIS;
				first = true;
				for (i = 0; i < pval.count; i++) {
					val = pval.values[i];
					old_index = str_index;
					str_index = get_value(staticFields, first, val, str_index);
					if (str_index > old_index) {
						first = false;
					}
				}
				if (str_index + 1 >= staticFields.ent_instance_as_string.length) {
					enlarge_instance_string(staticFields, str_index + 1, str_index + 2);
				}
				staticFields.ent_instance_as_string[++str_index] = PhFileReader.RIGHT_PARENTHESIS;
			}
		} else {
			if (this instanceof CEntity) {
				schemaData = ((CEntity)def).owning_model.schemaData;
				index = schemaData.find_entity(0, schemaData.bNames.length - 1,
					(CEntityDefinition)def);
				if (index < 0 || index >= schemaData.bNames.length) {
					String base = SdaiSession.line_separator + AdditionalMessages.AI_ENNF;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				if (short_names) {
					index = schemaData.toShort[index];
					name =  schemaData.bShortNames[index];
					name_length = schemaData.bShortNames[index].length;
				} else {
					name =  schemaData.bNames[index];
					name_length = schemaData.bNames[index].length;
				}
			} else {
				name = null;
				name_length = 0;
			}
			if (str_index + name_length + 2 >= staticFields.ent_instance_as_string.length) {
				enlarge_instance_string(staticFields, str_index + 1, str_index + name_length + 3);
			}
			for (i = 0; i < name_length; i++) {
				staticFields.ent_instance_as_string[++str_index] = name[i];
			}
			staticFields.ent_instance_as_string[++str_index] = PhFileReader.LEFT_PARENTHESIS;
			first = true;
			if (this instanceof CEntity) {
//if (Value.prnt) owning_model.print_entity_values(staticFields.entity_values, instance_identifier);
				for (i = 0; i < def.noOfPartialEntityTypes; i++) {
					int map_index = def.externalMappingIndexing[i];
					pval = staticFields.entity_values.entityValues[map_index];
					for (j = 0; j < pval.count; j++) {
						val = pval.values[j];
						old_index = str_index;
						str_index = get_value(staticFields, first, val, str_index);
						if (str_index > old_index) {
							first = false;
						}
					}
				}
			} else {
				pval = staticFields.entity_values.entityValues[0];
				for (j = 0; j < pval.count; j++) {
					val = pval.values[j];
					old_index = str_index;
					str_index = get_value(staticFields, first, val, str_index);
					if (str_index > old_index) {
						first = false;
					}
				}
			}
		}
		if (str_index + 2 >= staticFields.ent_instance_as_string.length) {
			enlarge_instance_string(staticFields, str_index + 1, str_index + 3);
		}
		staticFields.ent_instance_as_string[++str_index] = PhFileReader.RIGHT_PARENTHESIS;
		staticFields.ent_instance_as_string[++str_index] = PhFileReader.SEMICOLON_b;
		return new String(staticFields.ent_instance_as_string, 0, str_index + 1);
	}


/**
	Writes the integer number used in the construction of the entity
	name to a byte array. The count of digits in the byte representation of
	this number serves as a return value.
	This method is invoked in <code>getAsString</code>.
*/
	private int instance_id_to_byte_array(StaticFields staticFields) {
		long next_number;
		long lo = instance_identifier;
		int digit_index = 0;
		while (lo != 0) {
			next_number = lo / 10;
			staticFields.ent_instance_as_string[++digit_index] = PhFileWriter.DIGITS[(int)(lo - next_number * 10)];
			lo = next_number;
		}
		for (int i = 1; i <= digit_index / 2; i++) {
			byte sym = staticFields.ent_instance_as_string[i];
			staticFields.ent_instance_as_string[i] = staticFields.ent_instance_as_string[digit_index - i + 1];
			staticFields.ent_instance_as_string[digit_index - i + 1] = sym;
		}
		return digit_index;
	}


/**
	Writes a long number to a byte array. The last index value in this
	array used for writing a digit is returned.
	This method is invoked in <code>get_value</code>.
*/
	private int long_to_byte_array(StaticFields staticFields, long lo, int index) throws SdaiException {
		boolean neg;
		long number, next_number;
		if (lo < 0) {
			neg = true;
			number = -lo;
		} else if (lo > 0) {
			neg = false;
			number = lo;
		} else {
			if (index + 1 >= staticFields.ent_instance_as_string.length) {
				enlarge_instance_string(staticFields, index + 1, index + 2);
			}
			staticFields.ent_instance_as_string[++index] = PhFileWriter.DIGITS[0];
			return index;
		}
		int initial_index = index;
		while (number != 0) {
			next_number = number / 10;
			if (index + 1 >= staticFields.ent_instance_as_string.length) {
				enlarge_instance_string(staticFields, index + 1, index + 2);
			}
			staticFields.ent_instance_as_string[++index] = PhFileWriter.DIGITS[(int)(number - next_number * 10)];
			number = next_number;
		}
		if (neg) {
			if (index + 1 >= staticFields.ent_instance_as_string.length) {
				enlarge_instance_string(staticFields, index + 1, index + 2);
			}
			staticFields.ent_instance_as_string[++index] = PhFileReader.MINUS;
		}
		for (int i = initial_index + 1;
				i <= initial_index + (index - initial_index) / 2; i++) {
			byte sym = staticFields.ent_instance_as_string[i];
			staticFields.ent_instance_as_string[i] = staticFields.ent_instance_as_string[index - i + initial_index + 1];
			staticFields.ent_instance_as_string[index - i + initial_index + 1] = sym;
		}
		return index;
	}


/**
	Writes the value of an attribute to a byte array. The last index value in this
	array used for writing the attribute value is returned.
	This method is invoked in <code>getAsString</code>.
*/
	private int get_value(StaticFields staticFields, boolean first, Value val, int index) throws SdaiException {
		Value value_next;
		boolean first_next;
		SdaiSession ss = owning_model.repository.session;
		switch (val.tag) {
			case PhFileReader.MISSING:
				if (!first) {
					if (index + 1 >= staticFields.ent_instance_as_string.length) {
						enlarge_instance_string(staticFields, index + 1, index + 2);
					}
					staticFields.ent_instance_as_string[++index] = PhFileReader.COMMA_b;
				}
				if (index + 1 >= staticFields.ent_instance_as_string.length) {
					enlarge_instance_string(staticFields, index + 1, index + 2);
				}
				staticFields.ent_instance_as_string[++index] = PhFileReader.DOLLAR_SIGN;
				break;
			case PhFileReader.REDEFINE:
				if (!first) {
					if (index + 1 >= staticFields.ent_instance_as_string.length) {
						enlarge_instance_string(staticFields, index + 1, index + 2);
					}
					staticFields.ent_instance_as_string[++index] = PhFileReader.COMMA_b;
				}
				if (index + 1 >= staticFields.ent_instance_as_string.length) {
					enlarge_instance_string(staticFields, index + 1, index + 2);
				}
				staticFields.ent_instance_as_string[++index] = PhFileReader.ASTERISK;
				break;
			case PhFileReader.INTEGER:
				if (!first) {
					if (index + 1 >= staticFields.ent_instance_as_string.length) {
						enlarge_instance_string(staticFields, index + 1, index + 2);
					}
					staticFields.ent_instance_as_string[++index] = PhFileReader.COMMA_b;
				}
				if (val.integer == Integer.MIN_VALUE) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				index = long_to_byte_array(staticFields, val.integer, index);
				break;
			case PhFileReader.REAL:
				if (!first) {
					if (index + 1 >= staticFields.ent_instance_as_string.length) {
						enlarge_instance_string(staticFields, index + 1, index + 2);
					}
					staticFields.ent_instance_as_string[++index] = PhFileReader.COMMA_b;
				}
				String str = Double.toString(val.real);
				if (index + str.length() >= staticFields.ent_instance_as_string.length) {
					enlarge_instance_string(staticFields, index + 1, index + str.length() + 1);
				}
				for (int i = 0; i < str.length(); i++) {
					staticFields.ent_instance_as_string[++index] = (byte)str.charAt(i);
				}
				break;
			case PhFileReader.BOOLEAN:
				if (index + 4 >= staticFields.ent_instance_as_string.length) {
					enlarge_instance_string(staticFields, index + 1, index + 5);
				}
				if (!first) {
					staticFields.ent_instance_as_string[++index] = PhFileReader.COMMA_b;
				}
				if (val.integer == 0) {
					for (int i = 0; i < 3; i++) {
						staticFields.ent_instance_as_string[++index] = PhFileWriter.LOG_FALSE[i];
					}
				} else if (val.integer == 1) {
					for (int i = 0; i < 3; i++) {
						staticFields.ent_instance_as_string[++index] = PhFileWriter.LOG_TRUE[i];
					}
				}
				break;
			case PhFileReader.LOGICAL:
				if (index + 4 >= staticFields.ent_instance_as_string.length) {
					enlarge_instance_string(staticFields,index + 1, index + 5);
				}
				if (!first) {
					staticFields.ent_instance_as_string[++index] = PhFileReader.COMMA_b;
				}
				if (val.integer == 0) {
					for (int i = 0; i < 3; i++) {
						staticFields.ent_instance_as_string[++index] = PhFileWriter.LOG_FALSE[i];
					}
				} else if (val.integer == 1) {
					for (int i = 0; i < 3; i++) {
						staticFields.ent_instance_as_string[++index] = PhFileWriter.LOG_TRUE[i];
					}
				} else {
					for (int i = 0; i < 3; i++) {
						staticFields.ent_instance_as_string[++index] = PhFileWriter.LOG_UNKNOWN[i];
					}
				}
				break;
			case PhFileReader.ENUM:
				if (!first) {
					if (index + 1 >= staticFields.ent_instance_as_string.length) {
						enlarge_instance_string(staticFields, index + 1, index + 2);
					}
					staticFields.ent_instance_as_string[++index] = PhFileReader.COMMA_b;
				}
				ss.printer.fromStringBasicLatin(val.string);
				if (index + staticFields.string_length + 2 >= staticFields.ent_instance_as_string.length) {
					enlarge_instance_string(staticFields, index + 1, index + staticFields.string_length + 3);
				}
				staticFields.ent_instance_as_string[++index] = PhFileReader.DOT;
				for (int i = 0; i < staticFields.string_length; i++) {
					staticFields.ent_instance_as_string[++index] = staticFields.string[i];
				}
				staticFields.ent_instance_as_string[++index] = PhFileReader.DOT;
				break;
			case PhFileReader.STRING:
				if (!first) {
					if (index + 1 >= staticFields.ent_instance_as_string.length) {
						enlarge_instance_string(staticFields, index + 1, index + 2);
					}
					staticFields.ent_instance_as_string[++index] = PhFileReader.COMMA_b;
				}
				if (SdaiSession.isToStringUnicode()) {
					ss.printer.fromStringBasicLatin(val.string);
				} else {
					ss.printer.fromString(val.string);
				}
				int ln = 2 * staticFields.string_length;
				if (index + ln + 2 >= staticFields.ent_instance_as_string.length) {
					enlarge_instance_string(staticFields, index + 1, index + ln + 3);
				}
				staticFields.ent_instance_as_string[++index] = PhFileReader.APOSTROPHE;
				for (int i = 0; i < staticFields.string_length; i++) {
					staticFields.ent_instance_as_string[++index] = staticFields.string[i];
					if (staticFields.string[i] == PhFileReader.APOSTROPHE) {
						staticFields.ent_instance_as_string[++index] = PhFileReader.APOSTROPHE;
					}
				}
				staticFields.ent_instance_as_string[++index] = PhFileReader.APOSTROPHE;
				break;
			case PhFileReader.BINARY:
				if (!first) {
					if (index + 1 >= staticFields.ent_instance_as_string.length) {
						enlarge_instance_string(staticFields, index + 1, index + 2);
					}
					staticFields.ent_instance_as_string[++index] = PhFileReader.COMMA_b;
				}
				ss.printer.convertBinary((Binary)val.reference);
				if (index + staticFields.string_length + 2 >= staticFields.ent_instance_as_string.length) {
					enlarge_instance_string(staticFields, index + 1, index + staticFields.string_length + 3);
				}
				staticFields.ent_instance_as_string[++index] = PhFileReader.QUOTATION_MARK;
				for (int i = 0; i < staticFields.string_length; i++) {
					staticFields.ent_instance_as_string[++index] = staticFields.string[i];
				}
				staticFields.ent_instance_as_string[++index] = PhFileReader.QUOTATION_MARK;
				break;
			case PhFileReader.TYPED_PARAMETER:
				if (!first) {
					if (index + 1 >= staticFields.ent_instance_as_string.length) {
						enlarge_instance_string(staticFields, index + 1, index + 2);
					}
					staticFields.ent_instance_as_string[++index] = PhFileReader.COMMA_b;
				}
				ss.printer.fromStringBasicLatin(val.string);
				if (index + staticFields.string_length + 1 >= staticFields.ent_instance_as_string.length) {
					enlarge_instance_string(staticFields, index + 1, index + staticFields.string_length + 2);
				}
				for (int i = 0; i < staticFields.string_length; i++) {
					staticFields.ent_instance_as_string[++index] = staticFields.string[i];
				}
				staticFields.ent_instance_as_string[++index] = PhFileReader.LEFT_PARENTHESIS;
				value_next = val.nested_values[0];
				first_next = true;
				index = get_value(staticFields, first_next, value_next, index);
				if (index + 1 >= staticFields.ent_instance_as_string.length) {
					enlarge_instance_string(staticFields, index + 1, index + 2);
				}
				staticFields.ent_instance_as_string[++index] = PhFileReader.RIGHT_PARENTHESIS;
				break;
			case PhFileReader.ENTITY_REFERENCE:
				CEntity base_inst = null;
				if (!(val.reference instanceof SdaiModel.Connector)) {
					base_inst = (CEntity)val.reference;
				}
				if (!first) {
					if (index + 1 >= staticFields.ent_instance_as_string.length) {
						enlarge_instance_string(staticFields, index + 1, index + 2);
					}
					staticFields.ent_instance_as_string[++index] = PhFileReader.COMMA_b;
				}
				if (val.reference instanceof SdaiModel.Connector) {
					if (index + 1 >= staticFields.ent_instance_as_string.length) {
						enlarge_instance_string(staticFields, index + 1, index + 2);
					}
					staticFields.ent_instance_as_string[++index] = PhFileReader.QUESTION_MARK;
				} else {
					if (index + 1 >= staticFields.ent_instance_as_string.length) {
						enlarge_instance_string(staticFields, index + 1, index + 2);
					}
					staticFields.ent_instance_as_string[++index] = PhFileReader.SPECIAL;
					index = long_to_byte_array(staticFields, base_inst.instance_identifier, index);
				}
				break;
			case PhFileReader.EMBEDDED_LIST:
				if (!first) {
					if (index + 1 >= staticFields.ent_instance_as_string.length) {
						enlarge_instance_string(staticFields, index + 1, index + 2);
					}
					staticFields.ent_instance_as_string[++index] = PhFileReader.COMMA_b;
				}
				if (index + 1 >= staticFields.ent_instance_as_string.length) {
					enlarge_instance_string(staticFields, index + 1, index + 2);
				}
				staticFields.ent_instance_as_string[++index] = PhFileReader.LEFT_PARENTHESIS;
				first_next = true;
				for (int i = 0; i < val.length; i++) {
					value_next = val.nested_values[i];
					index = get_value(staticFields, first_next, value_next, index);
					first_next = false;
				}
				if (index + 1 >= staticFields.ent_instance_as_string.length) {
					enlarge_instance_string(staticFields, index + 1, index + 2);
				}
				staticFields.ent_instance_as_string[++index] = PhFileReader.RIGHT_PARENTHESIS;
				break;
		}
		return index;
	}


	public AEntity_definition typeOf() throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		CEntityDefinition edef = (CEntityDefinition)getInstanceType();
		return edef.getTypes();
//		} // syncObject
	}

	public void setTemp(Object key, Object value) {
		if (value == null && map != null) {
			map.remove(key);
		} else {
			if (map == null) {
				map = new HashMap();
			}
			map.put(key, value);
		}
	}

	public void setTemp(Object value) {
		if (map == null) {
			map = new HashMap();
		}
		map.put(SPECIAL_KEY, value);
	}

	public Object getTemp(Object key) {
		if (map != null) {
			return map.get(key);
		}
		return null;
	}

	public Object getTemp() {
		if (map != null) {
			return map.get(SPECIAL_KEY);
		}
		return null;
	}

	public boolean compareValuesBoolean(EEntity inst) throws SdaiException {
//		synchronized (syncObject) {
		if (inst == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		CEntity ins = (CEntity)inst;
		CEntity_definition edef_this = (CEntity_definition)getInstanceType();
		CEntity_definition edef = (CEntity_definition)ins.getInstanceType();
		if (edef_this != edef) {
			throw new SdaiException(SdaiException.ED_NDEQ);
		}
		StaticFields staticFields = StaticFields.get();
		if (staticFields.entity_values == null) {
			staticFields.entity_values = new ComplexEntityValue();
		}
		if (staticFields.entity_values2 == null) {
			staticFields.entity_values2 = new ComplexEntityValue();
		}
		if (this instanceof CEntity) {
			owning_model.prepareAll(staticFields.entity_values, edef_this);
		}
		getAll(staticFields.entity_values);
		if (ins instanceof CEntity) {
			owning_model.prepareAll(staticFields.entity_values2, edef);
		}
		ins.getAll(staticFields.entity_values2);
		for (int i = 0; i < edef_this.noOfPartialEntityTypes; i++) {
			EntityValue pval1 = staticFields.entity_values.entityValues[i];
			EntityValue pval2 = staticFields.entity_values2.entityValues[i];
			if (!compare_instances(pval1, pval2)) {
				return false;
			}
		}
		return true;
//		} // syncObject
	}


/**
	Compares values of all attributes of a partial entity type of two instances.
	The values are represented as objects of the class EntityValue. If all the
	corresponding values are equal, then <code>true</code> is returned.
	This method is invoked in <code>compareValuesBoolean</code>.
*/
	private boolean compare_instances(EntityValue pval1, EntityValue pval2) throws SdaiException {
		for (int i = 0; i < pval1.count; i++) {
			if (compare_values(pval1.values[i], pval2.values[i], false) == 0) {
				return false;
			}
		}
		return true;
	}


/**
	Compares a value of one instance with the corresponding value of another instance.
	The constant 0, 1 or 2 is returned depending on the result of
	comparison: not equal, equal, indeterminate, respectively.
	This method is invoked in <code>compare_instances</code>, <code>compare_instances_logical</code> and
	<code>find_in_aggregate</code> methods.
*/
	private int compare_values(Value val1, Value val2, boolean log_comp) throws SdaiException {
		Value value_next1, value_next2;
		if (log_comp) {
			if (val1.tag == PhFileReader.MISSING || val2.tag == PhFileReader.MISSING) {
				return 2;
			}
		}

		switch (val1.tag) {
			case PhFileReader.MISSING:
				if (val2.tag == PhFileReader.MISSING) {
					return 1;
				}
				return 0;
			case PhFileReader.REDEFINE:
				if (val2.tag == PhFileReader.REDEFINE) {
					return 1;
				}
				return 0;
			case PhFileReader.INTEGER:
				if (val2.tag == PhFileReader.INTEGER && val1.integer == val2.integer) {
					return 1;
				}
				return 0;
			case PhFileReader.REAL:
				if (val2.tag == PhFileReader.REAL && val1.real == val2.real) {
					return 1;
				}
				return 0;
			case PhFileReader.BOOLEAN:
				if (val2.tag == PhFileReader.BOOLEAN && val1.integer == val2.integer) {
					return 1;
				}
				return 0;
			case PhFileReader.LOGICAL:
				if (val2.tag == PhFileReader.LOGICAL && val1.integer == val2.integer) {
					return 1;
				}
				return 0;
			case PhFileReader.ENUM:
				if (val2.tag == PhFileReader.ENUM) {
					if (val1.string.equals(val2.string)) {
						return 1;
					} else {
						return 0;
					}
				}
				return 0;
			case PhFileReader.STRING:
				if (val2.tag == PhFileReader.STRING) {
					if (val1.string.equals(val2.string)) {
						return 1;
					} else {
						return 0;
					}
				}
				return 0;
			case PhFileReader.BINARY:
				if (val2.tag == PhFileReader.BINARY) {
					Binary bin = (Binary)val1.reference;
					if (bin.equals((Binary)val2.reference)) {
						return 1;
					} else {
						return 0;
					}
				}
				return 0;
			case PhFileReader.TYPED_PARAMETER:
				if (val2.tag != PhFileReader.TYPED_PARAMETER) {
					return 0;
				}
				if (!val1.string.equals(val2.string)) {
					return 0;
				}
				value_next1 = val1.nested_values[0];
				value_next2 = val2.nested_values[0];
				return compare_values(value_next1, value_next2, log_comp);
			case PhFileReader.ENTITY_REFERENCE:
				if (val2.tag != PhFileReader.ENTITY_REFERENCE) {
					return 0;
				}
				if (val1.reference instanceof SdaiModel.Connector || val2.reference instanceof SdaiModel.Connector) {
					long id1, id2;
					SdaiRepository rep1,rep2;
					CEntity app_inst;
					if (val1.reference instanceof SdaiModel.Connector) {
						id1 = ((SdaiModel.Connector)val1.reference).instance_identifier;
						SdaiModel mod1 = ((SdaiModel.Connector)val1.reference).resolveModelIn();
						rep1 = mod1 != null ? mod1.repository : null;
					} else {
						app_inst = (CEntity)val1.reference;
						id1 = app_inst.instance_identifier;
						rep1 = app_inst.owning_model.repository;
					}
					if (val2.reference instanceof SdaiModel.Connector) {
						id2 = ((SdaiModel.Connector)val2.reference).instance_identifier;
						SdaiModel mod2 = ((SdaiModel.Connector)val2.reference).resolveModelIn();
						rep2 = mod2 != null ? mod2.repository : null;
					} else {
						app_inst = (CEntity)val2.reference;
						id2 = app_inst.instance_identifier;
						rep2 = app_inst.owning_model.repository;
					}
					if (rep1 == rep2 && id1 == id2) {
						return 1;
					}
				} else {
					if (val1.reference == val2.reference) {
						return 1;
					}
				}
				return 0;
			case PhFileReader.EMBEDDED_LIST:
				if (val2.tag != PhFileReader.EMBEDDED_LIST) {
					return 0;
				}
				return compare_aggregates(val1, val2, log_comp);
			}
			return 0;
	}


/**
	Compares a value of one instance with the corresponding value of another instance
	under the condition that both values are aggregates.
	If any of the element comparisons evaluate to <code>false</code>, then 0 is returned.
	If one or more of the element comparisons evaluate to Express indeterminate and
	the remaining comparisons all evaluate to <code>true</code>, then the result is 2.
	Otherwise, the method returns the constant 1.
	This method is invoked in <code>compare_values</code>.
*/
	private int compare_aggregates(Value val1, Value val2, boolean log_comp) throws SdaiException {
		int i;
		if (val1.length != val2.length) {
			return 0;
		}
		if (!(val1.reference instanceof Aggregate) || !(val2.reference instanceof Aggregate)) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		AggregationType a_type1 = (AggregationType)((Aggregate)val1.reference).getAggregationType();
		AggregationType a_type2 = (AggregationType)((Aggregate)val2.reference).getAggregationType();
		if (a_type1 != a_type2) {
			return 0;
		}
		int var;
		if (a_type1.express_type == DataType.LIST || a_type1.express_type == DataType.ARRAY) {
			var = 1;
		} else if (a_type1.express_type == DataType.SET || a_type1.express_type == DataType.BAG) {
			var = 2;
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (var == 1) {
			boolean unknown = false;
			for (i = 0; i < val1.length; i++) {
				int res = compare_values(val1.nested_values[i], val2.nested_values[i], log_comp);
				if (res == 0) {
					return 0;
				} else if (log_comp && res == 2) {
					unknown = true;
				}
			}
			if (log_comp && unknown) {
				return 2;
			}
		} else {
			for (i = 0; i < val1.length; i++) {
				if (!find_in_aggregate(val1.nested_values[i], val2)) {
					return 0;
				}
			}
		}
		return 1;
	}


/**
	Returns <code>true</code> if the specified value belongs to the specified
	aggregate.
	This method is invoked in <code>compare_aggregates</code>.
*/
	private boolean find_in_aggregate(Value val, Value val2) throws SdaiException {
		for (int i = 0; i < val2.length; i++) {
			if (compare_values(val, val2.nested_values[i], false) == 1) {
				val2.nested_values[i].tag = -1;
				return true;
			}
		}
		return false;
	}


	public int compareValuesLogical(EEntity inst) throws SdaiException {
//		synchronized (syncObject) {
		if (inst == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		CEntity ins = (CEntity)inst;
		CEntity_definition edef_this = (CEntity_definition)getInstanceType();
		CEntity_definition edef = (CEntity_definition)ins.getInstanceType();
		if (edef_this != edef) {
			throw new SdaiException(SdaiException.ED_NDEQ);
		}
		StaticFields staticFields = StaticFields.get();
		if (staticFields.entity_values == null) {
			staticFields.entity_values = new ComplexEntityValue();
		}
		if (staticFields.entity_values2 == null) {
			staticFields.entity_values2 = new ComplexEntityValue();
		}
		if (this instanceof CEntity) {
			owning_model.prepareAll(staticFields.entity_values, edef_this);
		}
		getAll(staticFields.entity_values);
		if (ins instanceof CEntity) {
			owning_model.prepareAll(staticFields.entity_values2, edef);
		}
		ins.getAll(staticFields.entity_values2);
		boolean unknown = false;
		for (int i = 0; i < edef_this.noOfPartialEntityTypes; i++) {
			EntityValue pval1 = staticFields.entity_values.entityValues[i];
			EntityValue pval2 = staticFields.entity_values2.entityValues[i];
			int res = compare_instances_logical(pval1, pval2);
			if (res == 0) {
				return 1;
			} else if (res == 2) {
				unknown = true;
			}
		}
		if (unknown) {
			return 3;
		}
		return 2;
//		} // syncObject
	}


/**
	Compares values of all attributes of a partial entity type of two instances.
	The values are represented as objects of the class EntityValue. If all the
	corresponding values are equal, then <code>true</code> is returned.
	This method is invoked in <code>compareValuesBoolean</code>.
*/
	private int compare_instances_logical(EntityValue pval1, EntityValue pval2) throws SdaiException {
		boolean unknown = false;
		for (int i = 0; i < pval1.count; i++) {
			int res = compare_values(pval1.values[i], pval2.values[i], true);
			if (res == 0) {
				return 0;
			} else if (res == 2) {
				unknown = true;
			}
		}
		if (unknown) {
			return 2;
		}
		return 1;
	}



/*    Some nonpublic methods     */

/**
	Given an attribute returns either select type as a type of values of this
	attribute or <code>null</code> if the type of values is different than select type.
	This method is invoked in get/set/test/unset/create/validate methods in this class.
*/
	private SelectType take_select(EAttribute attribute) throws SdaiException {
		DataType type;
		if (((AttributeDefinition)attribute).attr_tp == AttributeDefinition.DERIVED) {
			type = (DataType)((CDerived_attribute)attribute).getDomain(null);
		} else if (((AttributeDefinition)attribute).attr_tp == AttributeDefinition.EXPLICIT) {
			type = (DataType)((CExplicit_attribute)attribute).getDomain(null);
		} else {
			String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		while (type.express_type == DataType.DEFINED_TYPE) {
			DataType underlying_type = (DataType)((CDefined_type)type).getDomain(null);
			if (underlying_type.express_type >= DataType.SELECT && underlying_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
				return (SelectType)underlying_type;
			} else if (underlying_type.express_type == DataType.DEFINED_TYPE) {
				type = underlying_type;
			} else {
				return null;
			}
		}
		return null;
	}


/**
	Given an attribute returns the type indicator (nonnegative integer) of values
	of this attribute. It is assumed that the attribute type is different than
	select type. If this requirement is violated, then SdaiException SY_ERR is thrown.
	The type indicator can get one of the following values:
		1 if the type is aggregation, entity, string or binary;
		2 if the type is logical or enumeration;
		3 if the type is real or number;
		4 if the type is integer;
		5 if the type is boolean;
		0 if failure in detection of the type.
	This method is invoked in get/set/test/unset/validate methods in this class.
*/
	private int find_type(EAttribute attribute) throws SdaiException {
		if (((AttributeDefinition)attribute).attr_tp == AttributeDefinition.INVERSE) {
			return 1;
		}
		DataType type;
		if (((AttributeDefinition)attribute).attr_tp == AttributeDefinition.DERIVED) {
			type = (DataType)((CDerived_attribute)attribute).getDomain(null);
		} else if (((AttributeDefinition)attribute).attr_tp == AttributeDefinition.EXPLICIT) {
			type = (DataType)((CExplicit_attribute)attribute).getDomain(null);
		} else {
			String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (type.express_type >= DataType.NUMBER && type.express_type <= DataType.BINARY) {
			return find_simple(type);
		} else if (type.express_type >= DataType.LIST && type.express_type <= DataType.AGGREGATE) {
			return 1;
		} else if (type.express_type == DataType.ENTITY) {
			return 1;
		} else if (type.express_type != DataType.DEFINED_TYPE) {
			String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		while (type.express_type == DataType.DEFINED_TYPE) {
			DataType underlying_type = (DataType)((CDefined_type)type).getDomain(null);
			if (underlying_type.express_type >= DataType.SELECT && underlying_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
				String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
				throw new SdaiException(SdaiException.SY_ERR, base);
			} else if (underlying_type.express_type >= DataType.ENUMERATION && underlying_type.express_type <= DataType.EXTENDED_EXTENSIBLE_ENUM) {
				return 2;
			} else if (underlying_type.express_type >= DataType.NUMBER && underlying_type.express_type <= DataType.BINARY) {
				return find_simple(underlying_type);
			} else if (underlying_type.express_type >= DataType.LIST && underlying_type.express_type <= DataType.AGGREGATE) {
				return 1;
			} else if (underlying_type.express_type == DataType.DEFINED_TYPE) {
				type = underlying_type;
			} else {
				String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
		return 0;
	}


/**
	Returns the type indicator (nonnegative integer) for the simple type submitted
	as a parameter. The type indicator can get one of the following values:
		1 if the type is string or binary;
		2 if the type is logical;
		3 if the type is real or number;
		4 if the type is integer;
		5 if the type is boolean;
	This method is invoked in <code>find_type</code>.
*/
	private int find_simple(DataType simple) throws SdaiException {
		if (simple.express_type == DataType.NUMBER) {
			return 3;
		} else if (simple.express_type == DataType.INTEGER) {
			return 4;
		} else if (simple.express_type == DataType.REAL) {
			return 3;
		} else if (simple.express_type == DataType.BOOLEAN) {
			return 5;
		} else if (simple.express_type == DataType.LOGICAL) {
			return 2;
		} else if (simple.express_type == DataType.BINARY) {
			return 1;
		} else if (simple.express_type == DataType.STRING) {
			return 1;
		} else {
			String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
	}


/**
	Returns the valid value for the assignment derived from the submitted value if it
	can be assigned to the specified attribute or null if the value is not valid
	for the attribute. It is assumed that the attribute type is different than
	select type. If this requirement is violated, then SdaiException SY_ERR is thrown.
	This method is invoked in <code>set</code> method for values of type
	<code>Object</code>.
*/
	private Object analyse_value(CExplicit_attribute attribute, Object value) throws SdaiException {
		DataType type = (DataType)attribute.getDomain(null);
		if (type.express_type >= DataType.NUMBER && type.express_type <= DataType.BINARY) {
			return analyse_simple_value(type, value);
		} else if (type.express_type >= DataType.LIST && type.express_type <= DataType.AGGREGATE) {
			return null;
		} else if (type.express_type == DataType.ENTITY) {
			if (!(value instanceof CEntity)) {
				return null;
			}
			CEntity_definition value_type = (CEntity_definition)((CEntity)value).getInstanceType();
			return value_type.isSubtypeOf((CEntity_definition)type) ? value : null;
		} else if (type.express_type != DataType.DEFINED_TYPE) {
			String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		while (type.express_type == DataType.DEFINED_TYPE) {
			DataType underlying_type = (DataType)((CDefined_type)type).getDomain(null);
			if (underlying_type.express_type >= DataType.SELECT && underlying_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
				String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
				throw new SdaiException(SdaiException.SY_ERR, base);
			} else if (underlying_type.express_type >= DataType.ENUMERATION && underlying_type.express_type <= DataType.EXTENDED_EXTENSIBLE_ENUM) {
				if (value instanceof Integer) {
					int int_value = ((Integer)value).intValue();
					A_string	ee;
					if (underlying_type.express_type == DataType.EXTENSIBLE_ENUM ||
							underlying_type.express_type == DataType.EXTENDED_EXTENSIBLE_ENUM) {
						ee = ((EExtensible_enumeration_type)underlying_type).getElements(null, owning_model.repository.session.sdai_context);
					} else {
						ee = ((EEnumeration_type)underlying_type).getElements(null);
					}
					if (int_value <= ee.myLength && int_value > 0) {
						return value;
					}
				}
				return null;
			} else if (underlying_type.express_type >= DataType.NUMBER && underlying_type.express_type <= DataType.BINARY) {
				return analyse_simple_value(underlying_type, value);
			} else if (underlying_type.express_type >= DataType.LIST && underlying_type.express_type <= DataType.AGGREGATE) {
				return null;
			} else if (underlying_type.express_type == DataType.DEFINED_TYPE) {
				type = underlying_type;
			} else {
				String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
		return null;
	}


/**
	Returns the valid value for the assignment derived from the submitted value if it
	can be assigned to the specified attribute of the simple type or null if
	the value is not valid for the attribute.
	This method is invoked in <code>analyse_value</code>.
*/
	private Object analyse_simple_value(DataType simple, Object value) throws SdaiException {
		int int_value;
		int width;
		EBound bound;
		if (simple.express_type == DataType.NUMBER) {
			if ((value instanceof Double && !((Double)value).isNaN()) ||
					(value instanceof Integer && ((Integer)value).intValue() != Integer.MIN_VALUE)) {
				return value;
			}
		} else if (simple.express_type == DataType.INTEGER) {
			if (value instanceof Integer && ((Integer)value).intValue() != Integer.MIN_VALUE) {
				return value;
			}
		} else if (simple.express_type == DataType.REAL) {
			if (value instanceof Double && !((Double)value).isNaN()) {
				return value;
			}
		} else if (simple.express_type == DataType.BOOLEAN) {
			if (value instanceof Integer) {
				int_value = ((Integer)value).intValue();
				if (int_value > 0 && int_value < 3) {
					return value;
				}
			} else if(value instanceof Boolean) {
				return new Integer(((Boolean)value).booleanValue() ? 2:1);
			}
		} else if (simple.express_type == DataType.LOGICAL) {
			if (value instanceof Integer) {
				int_value = ((Integer)value).intValue();
				if (int_value > 0 && int_value < 4) {
					return value;
				}
			}
		} else if (simple.express_type == DataType.BINARY) {
			if (value instanceof Binary) {
				CBinary_type bin_type = (CBinary_type)simple;
				if (bin_type.testWidth(null)) {
					int bit_count = ((Binary)value).getSize();
					bound = bin_type.getWidth(null);
					if (!(bound instanceof EInteger_bound)) {
						return value;
					}
					width = bound.getBound_value(null);
					if (bin_type.getFixed_width(null)) {
						if (bit_count == width) {
							return value;
						}
					} else {
						if (bit_count <= width) {
							return value;
						}
					}
				} else {
					return value;
				}
			}
		} else if (simple.express_type == DataType.STRING) {
			if (value instanceof String) {
				CString_type str_type = (CString_type)simple;
				if (str_type.testWidth(null)) {
					bound = str_type.getWidth(null);
					if (!(bound instanceof EInteger_bound)) {
						return value;
					}
					width = bound.getBound_value(null);
					if (str_type.getFixed_width(null)) {
						if (((String)value).length() == width) {
							return value;
						}
					} else {
						if (((String)value).length() <= width) {
							return value;
						}
					}
				} else {
					return value;
				}
			}
		}
		return null;
	}


/**
	Returns <code>true</code> if the submitted value can be assigned to the
	specified attribute whose type is a select type.
	This method is invoked in <code>set</code> method for values of type
	<code>Object</code>.
*/
	private Object analyse_select_value(int tag, SelectType sel_type, int sel_number, Object value)
			throws SdaiException {
		StaticFields staticFields = null;
		SdaiSession ss;
		int int_value;
		int width;
		EBound bound;
		switch(tag) {
			case PhFileReader.ENTITY_REFERENCE:
				if (value instanceof CEntity) {
					if (sel_type.express_type == DataType.ENT_EXT_SELECT) {
						return value;
					}
					CEntity_definition value_type = (CEntity_definition)((CEntity)value).getInstanceType();
					ss = owning_model.repository.session;
					if (sel_type.express_type >= DataType.EXTENSIBLE_SELECT && ss.sdai_context == null) {
						staticFields = StaticFields.get();
						staticFields.context_schema = owning_model.underlying_schema;
						if (!ss.sdai_context_missing) {
							ss.sdai_context_missing = true;
							ss.printWarningToLogoSdaiContext(AdditionalMessages.SS_SCMI);
						}
					}
					Object ret = sel_type.analyse_entity_in_select(value_type, ss.sdai_context) ? value : null;
					if (sel_type.express_type >= DataType.EXTENSIBLE_SELECT && ss.sdai_context == null) {
						staticFields.context_schema = null;
					}
					return ret;
				}
				break;
			case PhFileReader.INTEGER:
				if (value instanceof Integer && ((Integer)value).intValue() != Integer.MIN_VALUE) {
					return value;
				}
				break;
			case PhFileReader.REAL:
				if (value instanceof Double && !((Double)value).isNaN()) {
					return value;
				}
				break;
			case PhFileReader.LOGICAL:
				if (value instanceof Integer) {
					int_value = ((Integer)value).intValue();
					if (int_value > 0 && int_value < 4) {
						return value;
					}
				}
				break;
			case PhFileReader.BOOLEAN:
				if (value instanceof Integer) {
					int_value = ((Integer)value).intValue();
					if (int_value > 0 && int_value < 3) {
						return value;
					}
				} else if(value instanceof Boolean) {
					return new Integer(((Boolean)value).booleanValue() ? 2:1);
				}
				break;
			case PhFileReader.ENUM:
				if (value instanceof Integer) {
					int_value = ((Integer)value).intValue();
					EEnumeration_type enumer = (EEnumeration_type)sel_type.types[sel_number - 2];
					A_string	ee;
					DataType dt = (DataType)enumer;
					if (dt.express_type == DataType.EXTENSIBLE_ENUM || dt.express_type == DataType.EXTENDED_EXTENSIBLE_ENUM) {
						ss = owning_model.repository.session;
						if (ss.sdai_context == null) {
							staticFields = StaticFields.get();
							staticFields.context_schema = owning_model.underlying_schema;
							if (!ss.sdai_context_missing) {
								ss.sdai_context_missing = true;
								ss.printWarningToLogoSdaiContext(AdditionalMessages.SS_SCMI);
							}
						}
						ee = enumer.getElements(null, ss.sdai_context);
						if (ss.sdai_context == null) {
							staticFields.context_schema = null;
						}
					} else {
						ee = enumer.getElements(null);
					}
					if (int_value <= ee.myLength && int_value > 0) {
						return value;
					}
				}
				break;
			case PhFileReader.BINARY:
				if (value instanceof Binary) {
					CBinary_type bin_type = (CBinary_type)sel_type.types[sel_number - 2];
					if (bin_type.testWidth(null)) {
						int bit_count = ((Binary)value).getSize();
						bound = bin_type.getWidth(null);
						if (!(bound instanceof EInteger_bound)) {
							return value;
						}
						width = bound.getBound_value(null);
						if (bin_type.getFixed_width(null)) {
							if (bit_count == width) {
								return value;
							}
						} else {
							if (bit_count <= width) {
								return value;
							}
						}
					} else {
						return value;
					}
				}
				break;
			case PhFileReader.STRING:
				if (value instanceof String) {
					CString_type str_type = (CString_type)sel_type.types[sel_number - 2];
					if (str_type.testWidth(null)) {
						bound = str_type.getWidth(null);
						if (!(bound instanceof EInteger_bound)) {
							return value;
						}
						width = bound.getBound_value(null);
						if (str_type.getFixed_width(null)) {
							if (((String)value).length() == width) {
								return value;
							}
						} else {
							if (((String)value).length() <= width) {
								return value;
							}
						}
					} else {
						return value;
					}
				}
				break;
			default:
			}
		return null;
	}


/**
	Returns <code>true</code> if the submitted entity belongs to the selections set
	of the specified select type or some of its derivatives.
	This method is invoked in <code>analyse_select_value</code>.
*/
// Moved to SelectType class.
/*	private boolean analyse_entity_in_select(CSelect_type sel_type, CEntity_definition value_type)
			throws SdaiException {
		boolean res;
		ANamed_type sels = sel_type.getSelections(null);
		for (int i = 0; i < ((AEntity)sels).myLength; i++) {
			ENamed_type alternative = (ENamed_type)((AEntity)sels).myData[i];
			if (alternative instanceof CEntity_definition) {
				res = value_type.isSubtypeOf((CEntity_definition)alternative);
				if (res) {
					return true;
				}
				continue;
			}
			CEntity dom = (CEntity)alternative;
			while (dom instanceof CDefined_type) {
				dom = (CEntity)((CDefined_type)dom).getDomain(null);
			}
			if (dom instanceof CSelect_type) {
				res = analyse_entity_in_select((CSelect_type)dom, value_type);
				if (res) {
					return true;
				}
			}
		}
		return false;
	}*/


/**
	Returns entity instance obtained as a result of resolving a reference
	represented by a connector. The connector itself is eliminated whereas the
	reference is replaced by the instance obtained. The repository the
	connector points to should be open; otherwise, SdaiException RP_NOPN is thrown.
	The referenced model, however, if without access, is started automatically
	in read-only mode.
	This method is invoked in <code>get_object</code>, <code>testAttribute</code> and
	validation methods defined in this class.
*/
	private CEntity resolveAndReplaceConnector(SdaiModel.Connector value, EAttribute attribute
											   ) throws SdaiException {
		CEntity inst = value.resolveConnector(false, true, false);
		setInstance(attribute, inst);
// 		inst.inverseAdd(this);
		value.disconnect();
		return inst;
	}


/**
	Assigns the value of type entity to the specified attribute. The method
	can be deemed as a simplification for <code>resolveConnector</code> of a general
	method <code>set</code> defined in this class.
*/
	private void setInstance(EAttribute attribute, CEntity value) throws SdaiException {
		CEntityDefinition edef = (CEntityDefinition)getInstanceType();
		for (int i = 0; i < edef.attributes.length; i++) {
			if (edef.attributes[i] == attribute && edef.attributeFields[i] != null) {
				SelectType sel_type = take_select(attribute);
				int sel_number;
				if (sel_type == null) {
					sel_number = 0;
				} else {
					sel_number = 1;
				}
				SSuper ssuper;
				if (owning_model.described_schema == null) {
					ssuper = edef.fieldOwners[i].ssuper;
				} else {
					ssuper = edef.ssuper;
				}
				try {
					ssuper.setObject(this, edef.attributeFields[i], value);
					if (sel_number == 1 && sel_type.is_mixed > 0) {
						ssuper.setInt(this, edef.attributeFieldSelects[i], 1);
					}
					return;
				} catch (Exception ex) {
					throw new SdaiException(SdaiException.SY_ERR, ex);
				}
			}
		}
		throw new SdaiException(SdaiException.SY_ERR);
	}


/**
 * This method is for internal JSDAI use only. Applications shall not use it.
 * The method is invoked from the static early binding usedin methods
 * in the application entity classes.
 * Returns the count of instances added to the resulting list.
 */
	public int makeUsedin(EEntity_definition referent_def, EAttribute role,
			ASdaiModel modelDomain, AEntity result) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (role == null) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (result == null) {
			throw new SdaiException(SdaiException.AI_NEXS);
		}
//		if (result.myType != null) {
//			throw new SdaiException(SdaiException.AI_NVLD, result);
//		}
		if (owning_model.optimized) {
			return makeUsedinNoInverse(referent_def, role, modelDomain, result);
		}
		provideInstancesInsideUsedin(role, modelDomain);
		if (((AttributeDefinition)role).attr_tp == AttributeDefinition.EXPLICIT) {
			EExplicit_attribute userAttribute = (EExplicit_attribute)role;
			CEntityDefinition userEntity = (CEntityDefinition)userAttribute.getParent_entity(null);
			if(modelDomain == null) {
				int userEntityIndex = owning_model.underlying_schema.owning_model
					.schemaData.findEntityExtentIndex(userEntity);
				owning_model.provideInstancesForTypeIfNeeded(userEntityIndex);
			} else {
				SdaiModel savedDictModel = null;
				int userEntityIndex = 0;
				for(SdaiIterator i = modelDomain.createIterator(); i.next(); ) {
					SdaiModel userModel = modelDomain.getCurrentMember(i);
					if(userModel.underlying_schema == null) {
						userModel.getUnderlyingSchema();
					}
					if(savedDictModel != userModel.underlying_schema.owning_model) {
						savedDictModel = userModel.underlying_schema.owning_model;
						userEntityIndex = savedDictModel.schemaData.findEntityExtentIndex(userEntity);
					}
					userModel.provideInstancesForTypeIfNeeded(userEntityIndex);
				}
			}
		}
		int number = result.myLength;
if (SdaiSession.debug2) System.out.println("  domain member count = " + modelDomain.myLength);
if (SdaiSession.debug2) System.out.println("  referent_def ident = " +
((CEntity_definition)referent_def).instance_identifier +
"  referent_def: " + ((CEntity_definition)referent_def).getName(null) +
"   entity definition: " + ((CEntity_definition)referent_def).getCorrectName() +
"  owning model: " + ((CEntity_definition)referent_def).owning_model.name);
if (SdaiSession.debug2) System.out.println("  role ident = " +
((CExplicit_attribute)role).instance_identifier +
"   role: " + role.getName(null) +
"  owning model: " + ((CExplicit_attribute)role).owning_model.name);
if (SdaiSession.debug2) System.out.println("  this ident = " + instance_identifier +
"  owning model: " + owning_model.name);
//		int k = 0;
		Object o = inverseList;
		CEntity inst;
//if (o == null) System.out.println(" ========== Inverse list is empty");
		while (o != null) {
			if (o instanceof Inverse) {
				Inverse inv = (Inverse) o;
				inst = (CEntity)inv.value;
//				k++;
				if (inst.owning_model == null) {
//System.out.println("  CEntity  inst with null owning model: #" + inst.instance_identifier +
//"  this inst: #" + instance_identifier);
					throw new SdaiException(SdaiException.SY_ERR,
						"For an instance in inverse list owning model is null");
				}

//System.out.println("  inst 1 ident = " + inst.instance_identifier +
//"   inst: " + inst.getInstanceType().getName(null));
//if (inst.owning_model == null) System.out.println("  |||||||owning model is NULL ");
//else System.out.println("  +++owning model is POSITIVE");
//System.out.println("  owning model: " + inst.owning_model.name);
if (SdaiSession.debug2) {
System.out.println("  inst 1 ident = " + inst.instance_identifier +
"owning model: " + inst.owning_model.name + "  inst type: " +
((CEntity_definition)inst.getInstanceType()).getCorrectName() + "  referent def: " +
((CEntity_definition)referent_def).getCorrectName());
//boolean bb = checkType((CEntity_definition)referent_def, (CEntity_definition)inst.getInstanceType());
//System.out.println("   CEntity   bb = " + bb + "  inst def: " +
//((CEntity_definition)inst.getInstanceType()).getCorrectName());
}
//				if ((k < 2 || (k > 1 && different(inst, k))) &&
//					checkType((CEntity_definition)referent_def, (CEntity_definition)inst.getInstanceType())) {
//					makeUsedin2(inst, role, result, modelDomain);
//				}
				if (inst.instance_identifier >= 0 && checkType((CEntity_definition)referent_def,
						(CEntity_definition)inst.getInstanceType())) {
					makeUsedin2(inst, role, result, modelDomain, false);
					inst.instance_identifier = -inst.instance_identifier;
				}
				o = inv.next;
			} else {
				inst = (CEntity)o;
//				k++;
if (SdaiSession.debug2) System.out.println("  inst 2 ident = " + inst.instance_identifier +
"owning model: " + inst.owning_model.name + "  inst type: " +
((CEntity_definition)inst.getInstanceType()).getCorrectName() + "  referent def: " +
((CEntity_definition)referent_def).getCorrectName());
//				if ((k < 2 || (k > 1 && different(inst, k))) &&
//					checkType((CEntity_definition)referent_def, (CEntity_definition)inst.getInstanceType())) {
//					makeUsedin2(inst, role, result, modelDomain);
//				}
				if (inst.instance_identifier >= 0 && checkType((CEntity_definition)referent_def,
						(CEntity_definition)inst.getInstanceType())) {
					makeUsedin2(inst, role, result, modelDomain, false);
				}
				o = null;
			}
		}
		o = inverseList;
		while (o != null) {
			if (o instanceof Inverse) {
				Inverse inv = (Inverse) o;
				inst = (CEntity)inv.value;
				if (inst.instance_identifier < 0) {
					inst.instance_identifier = -inst.instance_identifier;
				}
				o = inv.next;
			} else {
				o = null;
			}
		}
if (SdaiSession.debug2) System.out.println(" ******* result.myLength = " + result.myLength);
		return result.myLength - number;
//		} // syncObject
	}


	private int makeUsedinNoInverse(EEntity_definition referent_def, EAttribute role,
			ASdaiModel modelDomain, AEntity result) throws SdaiException {
		int i, j, k;
		SdaiModel model;
		CEntityDefinition entityDef;
		int [] aux2;

		int number = result.myLength;
		ASdaiModel act_models = owning_model.repository.session.active_models;
		for (i = 0; i < act_models.myLength; i++) {
			model = (SdaiModel)act_models.myData[i];
			if (model.repository == SdaiSession.systemRepository) {
				continue;
			}
			if (modelDomain == null || modelDomain.myLength == 0) {
				if (modelDomain == null && owning_model != model) {
					continue;
				}
			} else {
				StaticFields staticFields = StaticFields.get();
				if (staticFields.it == null) {
					staticFields.it = modelDomain.createIterator();
				} else {
					modelDomain.attachIterator(staticFields.it);
				}
				boolean ignore_model = true;
				while (staticFields.it.next()) {
					if (modelDomain.getCurrentMemberObject(staticFields.it) == model) {
						ignore_model = false;
						break;
					}
				}
				if (ignore_model) {
					continue;
				}
			}
			SchemaData sch_data = model.underlying_schema.owning_model.schemaData;
			if (sch_data.involved == 0) {
				continue;
			}
			if (sch_data.involved > 0) {
				for (j = 0; j < sch_data.involved; j++) {
					entityDef = sch_data.entities[sch_data.aux[j]];
					model.scanEntityInstances(this, entityDef, sch_data.aux[j], sch_data.aux2[j], result);
				}
				continue;
			}
			sch_data.involved = 0;
			int index = sch_data.find_entity(0, sch_data.noOfEntityDataTypes - 1, (CEntityDefinition)referent_def);
			if (index < 0) {
				continue;
			}
			int subtypes [] = sch_data.schema.getSubtypes(index);
			int subt_ind;
			aux2 = sch_data.getAuxiliaryArray();
			for (j = -1; j < subtypes.length; j++) {
				if (j < 0) {
					subt_ind = index;
				} else {
					subt_ind = subtypes[j];
				}
				entityDef = sch_data.entities[subt_ind];
				CExplicit_attribute [] attributes = entityDef.attributes;
				if (attributes == null) {
					continue;
				}
				boolean found = false;
				for (k = 0; k < attributes.length; k++) {
					if (attributes[k] == role) {
						found = true;
						break;
					}
				}
				if (found) {
					sch_data.aux[sch_data.involved] = subt_ind;
					aux2[sch_data.involved] = k;
					sch_data.involved++;
					model.scanEntityInstances(this, entityDef, subt_ind, k, result);
					continue;
				}
				for (int m = 0; m < entityDef.noOfPartialEntityTypes; m++) {
					CEntityDefinition entity;
					if (entityDef.complex == 2) {
						entity = entityDef.partialEntityTypes[m];
					} else {
						entity = entityDef.partialEntityTypes[entityDef.externalMappingIndexing[m]];
					}
					boolean processed = false;
					for (k = 0; k < entity.attributesRedecl.length; k++) {
						EAttribute attrib = entity.attributesRedecl[k];
						if (attrib != role) {
							continue;
						}
						processed = true;
						int attr_index = GetHeadAttribute(entityDef, (CExplicit_attribute)attrib);
						if (attr_index < 0) {
							break;
						}
						sch_data.aux[sch_data.involved] = subt_ind;
						aux2[sch_data.involved] = attr_index;
						sch_data.involved++;
						model.scanEntityInstances(this, entityDef, subt_ind, attr_index, result);
						break;
					}
					if (processed) {
						break;
					}
				}
			}
		}

		for (i = 0; i < act_models.myLength; i++) {
			model = (SdaiModel)act_models.myData[i];
			if (model.repository == SdaiSession.systemRepository) {
				continue;
			}
			model.underlying_schema.owning_model.schemaData.involved = -1;
		}
		return result.myLength - number;
	}


	private void provideInstancesInsideUsedin(EAttribute role,
											 ASdaiModel modelDomain) throws SdaiException {
		if (((AttributeDefinition)role).attr_tp == AttributeDefinition.EXPLICIT) {
			EExplicit_attribute userAttribute = (EExplicit_attribute)role;
			CEntityDefinition userEntity = (CEntityDefinition)userAttribute.getParent_entity(null);
			if(modelDomain == null) {
				int userEntityIndex = owning_model.underlying_schema.owning_model
					.schemaData.findEntityExtentIndex(userEntity);
				owning_model.provideInstancesForTypeIfNeeded(userEntityIndex);
			} else {
				SdaiModel savedDictModel = null;
				int userEntityIndex = 0;
				for(SdaiIterator i = modelDomain.createIterator(); i.next(); ) {
					SdaiModel userModel = modelDomain.getCurrentMember(i);
					if((userModel.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.NO_ACCESS) {
					   if(savedDictModel != userModel.underlying_schema.owning_model) {
						   savedDictModel = userModel.underlying_schema.owning_model;
						   userEntityIndex =
							   savedDictModel.schemaData.findEntityExtentIndex(userEntity);
					   }
					   userModel.provideInstancesForTypeIfNeeded(userEntityIndex);
					}
				}
			}
		}
	}

/**
	Returns <code>true</code> if the submitted entity instance is found
	amongst the first <code>index-1</code> elements of the inverse list of this
	entity instance; here <code>index</code> is method's second parameter.
	This method is invoked in <code>get_inverse</code> and <code>makeUsedin</code>.
*/
	private boolean different(CEntity checked_inst, int index) throws SdaiException {
		int k = 0;
		Object o = inverseList;
		while (true) {
			k++;
			if (k >= index) {
				return true;
			}
			if (o instanceof Inverse) {
				Inverse inv = (Inverse)o;
				if (inv.value == checked_inst) {
					return false;
				}
				o = inv.next;
			}
		}
	}


/**
	Adds the submitted instance to the resulting set provided this instance references
	the current entity instance in the specified role (second parameter) and its owning
	model either belongs to the specified set of models (if it is nonempty)
	or coincides with the owning model of this instance (if the set is empty).
	This method is invoked in <code>findEntityInstanceUsedin</code>,
	<code>get_inverse</code> and <code>makeUsedin</code> methods.
*/
	private void makeUsedin2(CEntity inst, EAttribute role, AEntity result, ASdaiModel domain, boolean for_inverse)
			throws SdaiException {
		int i, j;
		CEntityDefinition entityDef = (CEntityDefinition)inst.getInstanceType();
		SSuper ssuper = null;
		if (owning_model.described_schema != null) {
			ssuper = entityDef.ssuper;
		}
		CEntityDefinition entity;
		CExplicit_attribute [] attributes = entityDef.attributes;
if (SdaiSession.debug2) System.out.println("  entityDef ident = " + ((CEntity_definition)entityDef).instance_identifier +
"  owning model: " + ((CEntity_definition)entityDef).owning_model.name);
if (SdaiSession.debug2) System.out.println("  inst ident = " + inst.instance_identifier +
"  owning model: " + inst.owning_model.name);
		SdaiModel model = inst.owning_model;
		Object o;
		if (domain == null || domain.myLength == 0) {
			if (domain == null && owning_model != model) {
				return;
			}
//			if (owning_model == model) {
			for (i = 0; i < attributes.length; i++) {
				if (attributes[i] == role) {
					try {
						if (model.described_schema == null) {
							if (entityDef.fieldOwners[i] == null) {
								if (for_inverse) {
									return;
								}
// Before printing a warning, check whether inst references 'this' through 'role' or another attribute.
// In the latter case, return with no warning.
// This check may have a negative impact on the performance.
								//boolean no_warn = SdaiSession.isNoUsedinWarnings();
								//if (!no_warn) {
									//printWarningToLogoRole(model.repository.session, inst, role);
								//}
								return;
//								String base = SdaiSession.line_separator + AdditionalMessages.AT_REDD +
//									SdaiSession.line_separator + "Role: " + role;
//								throw new SdaiException(SdaiException.AT_NVLD, role, base);
							}
							ssuper = entityDef.fieldOwners[i].ssuper;
						}
						o = ssuper.getObject(inst, entityDef.attributeFields[i]);
					} catch (java.lang.IllegalAccessException ex) {
						throw new SdaiException(SdaiException.SY_ERR);
					}
					if (o == this) {
						if (result.myType == null || result.myType.express_type == DataType.LIST) {
							result.addAtTheEnd(inst, null);
						} else {
							result.setForNonList(inst, result.myLength, null, null);
						}
					} else if (o instanceof CAggregate) {
						((CAggregate)o).usedin(this, inst, result);
					}
					return;
				}
			}

			for (j = 0; j < entityDef.noOfPartialEntityTypes; j++) {
				if (entityDef.complex == 2) {
					entity = entityDef.partialEntityTypes[j];
				} else {
					entity = entityDef.partialEntityTypes[entityDef.externalMappingIndexing[j]];
				}
				for (i = 0; i < entity.attributesRedecl.length; i++) {
					EAttribute attrib = entity.attributesRedecl[i];
					if (attrib != role) {
						continue;
					}
					int ind = GetHeadAttribute(entityDef, (CExplicit_attribute)attrib);
					if (ind < 0) {
						return;
					}
					try {
						if (model.described_schema == null) {
							ssuper = entityDef.fieldOwners[ind].ssuper;
						}
						o = ssuper.getObject(inst, entityDef.attributeFields[ind]);
					} catch (java.lang.IllegalAccessException ex) {
						throw new SdaiException(SdaiException.SY_ERR);
					}
					if (o == this) {
						if (result.myType == null || result.myType.express_type == DataType.LIST) {
							result.addAtTheEnd(inst, null);
						} else {
							result.setForNonList(inst, result.myLength, null, null);
						}
					} else if (o instanceof CAggregate) {
						((CAggregate)o).usedin(this, inst, result);
					}
					return;
				}
			}
//			}
			return;
		}
		StaticFields staticFields = StaticFields.get();
		if (staticFields.it == null) {
			staticFields.it = domain.createIterator();
		} else {
			domain.attachIterator(staticFields.it);
		}
		while (staticFields.it.next()) {
			if (domain.getCurrentMemberObject(staticFields.it) == model) {
				for (i = 0; i < attributes.length; i++) {
//System.out.println(" CEntity **** attr name = " + attributes[i].getName(null) + "   index: " + i);
					if (attributes[i] == role) {
						try {
							if (owning_model.described_schema == null) {
								if (entityDef.fieldOwners[i] == null) {
									if (for_inverse) {
										return;
									}
									//boolean no_warn = SdaiSession.isNoUsedinWarnings();
									//if (!no_warn) {
										//printWarningToLogoRole(owning_model.repository.session, inst, role);
									//}
									return;
//									String base = SdaiSession.line_separator + AdditionalMessages.AT_REDD +
//										SdaiSession.line_separator + "Role: " + role;
//									throw new SdaiException(SdaiException.AT_NVLD, role, base);
								}
								ssuper = entityDef.fieldOwners[i].ssuper;
							}
							o = ssuper.getObject(inst, entityDef.attributeFields[i]);
//if (o == null) System.out.println("&&&&&&&& o is NULL");
//else System.out.println("&&&&&&&& o is POSITIVE");
						} catch (java.lang.IllegalAccessException ex) {
							throw new SdaiException(SdaiException.SY_ERR);
						}
						if (o == this) {
							if (result.myType == null || result.myType.express_type == DataType.LIST) {
								result.addAtTheEnd(inst, null);
							} else {
								result.setForNonList(inst, result.myLength, null, null);
							}
						} else if (o instanceof CAggregate) {
							((CAggregate)o).usedin(this, inst, result);
						}
						return;
					}
				}

				for (j = 0; j < entityDef.noOfPartialEntityTypes; j++) {
					if (entityDef.complex == 2) {
						entity = entityDef.partialEntityTypes[j];
					} else {
						entity = entityDef.partialEntityTypes[entityDef.externalMappingIndexing[j]];
					}
					for (i = 0; i < entity.attributesRedecl.length; i++) {
						EAttribute attrib = entity.attributesRedecl[i];
						if (attrib != role) {
							continue;
						}
						int ind = GetHeadAttribute(entityDef, (CExplicit_attribute)attrib);
						if (ind < 0) {
							return;
						}
						try {
							if (owning_model.described_schema == null) {
								ssuper = entityDef.fieldOwners[ind].ssuper;
							}
							o = ssuper.getObject(inst, entityDef.attributeFields[ind]);
						} catch (java.lang.IllegalAccessException ex) {
							throw new SdaiException(SdaiException.SY_ERR);
						}
						if (o == this) {
							if (result.myType == null || result.myType.express_type == DataType.LIST) {
								result.addAtTheEnd(inst, null);
							} else {
								result.setForNonList(inst, result.myLength, null, null);
							}
						} else if (o instanceof CAggregate) {
							((CAggregate)o).usedin(this, inst, result);
						}
						return;
					}
				}

				/* TBD: double references needs to be eliminated */
				break;
			}
		}
	}


	private int GetHeadAttribute(CEntityDefinition edef, CExplicit_attribute red_attr) throws SdaiException {
		CExplicit_attribute attr = (CExplicit_attribute)red_attr.getRedeclaring(null);
		while (attr.testRedeclaring(null)) {
			attr = (CExplicit_attribute)attr.getRedeclaring(null);
		}
		for (int i = 0; i < edef.attributes.length; i++) {
			if (edef.attributes[i] == attr) {
				if (edef.fieldOwners[i] != null) {
					return i;
				} else {
					return -1;
				}
			}
		}
		return -1;
	}


	private void printWarningToLogoRole(SdaiSession session, CEntity inst, EAttribute role) throws SdaiException {
		String text =
			AdditionalMessages.AT_REDD +
			SdaiSession.line_separator + "   Role: " + role +
			SdaiSession.line_separator + "   From instance: " + inst +
			SdaiSession.line_separator + "   To instance: " + this;
		if (session != null && session.logWriterSession != null) {
			session.printlnSession(text);
		} else {
			SdaiSession.println(text);
		}
	}


	private void printWarningToLogoAttribute(SdaiSession session, EAttribute attr, String message) throws SdaiException {
		String text =
			message + 
			SdaiSession.line_separator + AdditionalMessages.RD_ENT + this.getInstanceType().getName(null) +
			SdaiSession.line_separator + AdditionalMessages.RD_INST + this.instance_identifier +
			SdaiSession.line_separator + AdditionalMessages.RD_ATTR + attr.getName(null);
		if (session != null && session.logWriterSession != null) {
			session.printlnSession(text);
		} else {
			SdaiSession.println(text);
		}
	}


/**
	Returns <code>true</code> if entity definition specified by the first
	parameter coincides with entity definition specified by the second
	parameter or belongs to the supertypes set, possibly indirectly, of it
	when this first definition is simple or if the second definition
	belongs to the set of partial entity types of the first definition
	when the latter is a complex one. This method is needed to check if
	a value of type entity whose definition is second in the pair of
	parameters fits to the attribute whose base type is entity represented by
	the first parameter.
	The method is invoked in <code>get_inverse</code> and <code>makeUsedin</code>
	methods.
*/
	private boolean checkType(CEntity_definition referent_def, CEntity_definition tested_def)
			throws SdaiException {
		if (referent_def.complex == 2) {
			if (referent_def == tested_def) {
				return true;
			}
			CEntity_definition [] stypes = ((CEntityDefinition)referent_def).partialEntityTypes;
			for (int i = 0; i < stypes.length; i++) {
				if (stypes[i] == tested_def) {
					return true;
				}
			}
			return false;
		} else {
			return checkSupertypeList(referent_def, tested_def);
		}
	}


/**
	Returns <code>true</code> if entity definition specified by the first
	parameter coincides with entity definition specified by the second
	parameter or belongs to the supertypes set, possibly indirectly, of it.
	The method is invoked in <code>checkType</code>.
*/
	private boolean checkSupertypeList(CEntity_definition def, CEntity_definition tested_def)
			throws SdaiException {
		if (def == tested_def) {
			return true;
		}
//		AEntity_definition list_of_definitions = tested_def.getSupertypes(null);
		AEntity list_of_definitions = tested_def.getSupertypes(null);
//		for (int i = 1; i <= ((AEntity)list_of_definitions).myLength; i++) {
		Object [] myDataA;
		if (list_of_definitions.myLength == 1) {
			return checkSupertypeList(def, (CEntity_definition)list_of_definitions.myData);
		} else if (list_of_definitions.myLength == 2) {
			myDataA = (Object [])list_of_definitions.myData;
			if (checkSupertypeList(def, (CEntity_definition)myDataA[0])) {
				return true;
			}
			if (checkSupertypeList(def, (CEntity_definition)myDataA[1])) {
				return true;
			}
		} else {
			ListElement element;
			if (list_of_definitions.myLength <= CAggregate.SHORT_AGGR) {
				element = (ListElement)list_of_definitions.myData;
			} else {
				myDataA = (Object [])list_of_definitions.myData;
				element = (ListElement)myDataA[0];
			}
			while (element != null) {
				if (checkSupertypeList(def, (CEntity_definition)element.object)) {
					return true;
				}
				element = element.next;
			}
		}
//			boolean found =
//				checkSupertypeList(def, (CEntity_definition)list_of_definitions.getByIndex(i));
//			if (found) {
//				return true;
//			}
//		}
		return false;
	}


/**
	Copies with some changes the values of the entity instance specified
	by the first parameter to the corresponding fields of this entity
	instance. The values being copied are changed in the following cases:
   	1) when a value is a reference to an instance, which belongs to the
	aggregate given by the third parameter. In this case assuming that the
	instance submitted through the first parameter also belongs to this
	aggregate the following rule is applied: references between instances of
	the given aggregate are mapped to references between their copies.
		2) when a value is an instance of the class <code>Connector</code>.
	In this case the value is replaced by a copy of it.
		3) when either case 1 or case 2 above appear in a value which is
	(possibly) nested aggregate.
	The method is invoked in <code>copyInstances</code> and
	<code>copyInstance</code> methods of SdaiModel class.
*/
	void copy_values(StaticFields staticFields, CEntity source, CEntity_definition def, AEntity inst_aggr, SdaiModel mod)
			throws SdaiException {
		if (staticFields.entity_values == null) {
			staticFields.entity_values = new ComplexEntityValue();
		}
		mod.prepareAll(staticFields.entity_values, def);
		source.getAll(staticFields.entity_values);
		for (int i = 0; i < def.noOfPartialEntityTypes; i++) {
			EntityValue ent_val = staticFields.entity_values.entityValues[i];
			for (int j = 0; j < ent_val.count; j++) {
				ent_val.values[j].check_references(staticFields, inst_aggr, mod, this);
			}
		}
		setAll(staticFields.entity_values);
	}


	boolean analyse_entity(CEntity ref_owner, CEntity_definition new_def) throws SdaiException {
		DataType type = (DataType)this;
		if (type.express_type == DataType.ENTITY) {
			return new_def.isSubtypeOf((CEntity_definition)type);
		} else if (type.express_type != DataType.DEFINED_TYPE) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		while (type.express_type == DataType.DEFINED_TYPE) {
			type = (DataType)((CDefined_type)type).getDomain(null);
		}
		if (type.express_type >= DataType.SELECT && type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
			StaticFields staticFields = null;
			SdaiSession ss = ref_owner.owning_model.repository.session;
			if (type.express_type >= DataType.EXTENSIBLE_SELECT && ss.sdai_context == null) {
				staticFields = StaticFields.get();
				staticFields.context_schema = ref_owner.owning_model.underlying_schema;
				if (!ss.sdai_context_missing) {
					ss.sdai_context_missing = true;
					ss.printWarningToLogoSdaiContext(AdditionalMessages.SS_SCMI);
				}
			}
			boolean res = ((SelectType)type).analyse_entity_in_select(new_def, ss.sdai_context);
			if (type.express_type >= DataType.EXTENSIBLE_SELECT && ss.sdai_context == null) {
				staticFields.context_schema = null;
			}
			return res;
		}
		throw new SdaiException(SdaiException.SY_ERR);
	}


/**
	Increases the size of the auxiliary array 'instance_as_string' either
	twice or to satisfy the demand requested whichever of the alternatives
	leads to a bigger array.
*/
	private void enlarge_instance_string(StaticFields staticFields, int str_length, int demand) {
		int new_length = staticFields.ent_instance_as_string.length * 2;
		if (new_length < demand) {
			new_length = demand;
		}
		byte new_instance_as_string[] = new byte[new_length];
		System.arraycopy(staticFields.ent_instance_as_string, 0, new_instance_as_string, 0, str_length);
		staticFields.ent_instance_as_string = new_instance_as_string;
	}


/**
	Puts an entity name into the form used in JSDAI. For example, for "point"
	the transformed name "Point" is returned.
*/
	static String normalise(String expressName) {
		return expressName.substring(0,1).toUpperCase() + expressName.substring(1).toLowerCase();
	}


/* special methods for attribute access, used in compiler generated early binding methods */

// test methods
/**
	Returns <code>true</code> if the submitted integer value is not equal to
	the unset value (which is Integer.MIN_VALUE).
	The method is invoked in compiler generated early binding methods.
*/
	final protected boolean test_integer(int value) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		return (value == Integer.MIN_VALUE) ? false : true;
//		} // syncObject
	}


/**
	Returns <code>true</code> if the submitted double value is not equal to
	the unset value (which is Double.NaN).
	The method is invoked in compiler generated early binding methods.
*/
	final protected boolean test_double(double value) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		return (Double.isNaN(value)) ? false : true;
//		} // syncObject
	}


/**
	Returns <code>true</code> if the submitted <code>String</code> value is
	nonnull.
	The method is invoked in compiler generated early binding methods.
*/
	final protected boolean test_string(String value) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		return (value == null) ? false : true;
//		} // syncObject
	}


/**
	Returns <code>true</code> if the submitted logical value is not equal to
	the unset value (which is 0).
	The method is invoked in compiler generated early binding methods.
*/
	final protected boolean test_logical(int value) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		return (value == 0) ? false : true;
//		} // syncObject
	}


/**
	Returns <code>true</code> if the submitted boolean value is not equal to
	the unset value (which is 0).
	The method is invoked in compiler generated early binding methods.
*/
	final protected boolean test_boolean(int value) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		return (value == 0) ? false : true;
//		} // syncObject
	}


/**
	Returns <code>true</code> if the submitted enumeration value is not equal to
	the unset value (which is 0).
	The method is invoked in compiler generated early binding methods.
*/
	final protected boolean test_enumeration(int value) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		return (value == 0) ? false : true;
//		} // syncObject
	}


/**
	Returns <code>true</code> if the submitted <code>Binary</code> value is
	nonnull.
	The method is invoked in compiler generated early binding methods.
*/
	final protected boolean test_binary(Binary value) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		return (value == null) ? false : true;
//		} // syncObject
	}


/**
	Returns <code>true</code> if the submitted value representing entity instance
	is nonnull. If the type of the value is <code>Connector</code>, then
	<code>resolveConnector</code> is applied.
	The method is invoked in compiler generated early binding methods.
*/
	final protected boolean test_instance(Object value) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (value instanceof SdaiModel.Connector) {
			value = ((SdaiModel.Connector)value).resolveConnector(false, true, false);
		}
		return (value == null) ? false : true;
//		} // syncObject
	}


/**
	Returns <code>true</code> if the submitted <code>Aggregate</code> value is
	nonnull.
	The method is invoked in compiler generated early binding methods.
*/
	final protected boolean test_aggregate(Aggregate value) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		return (value == null) ? false : true;
//		} // syncObject
	}


/**
	Returns one of the following integers:
	  0 if the submitted value is null;
	  1 if the type of the submitted value is entity;
	  the number specified by the second parameter, called select number, otherwise.
	The method is invoked in compiler generated early binding methods.
*/
	final protected int test_select(Object value, int sel_number) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (value == null) {
			return 0;
		} else if (value instanceof InverseEntity) {
			return 1;
		} else {
			return sel_number;
		}
//		} // syncObject
	}


// get methods
/**
	Checks if the submitted integer does not mean unset value and, if so,
	returns it.
	The method is invoked in compiler generated early binding methods.
*/
	final protected int get_integer(int value) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (value == Integer.MIN_VALUE) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		return value;
//		} // syncObject
	}


/**
	Checks if the submitted double does not mean unset value and, if so,
	returns it.
	The method is invoked in compiler generated early binding methods.
*/
	final protected double get_double(double value) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (Double.isNaN(value)) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		return value;
//		} // syncObject
	}


/**
	Checks if the submitted <code>String</code> value is nonnull and, if so,
	returns it.
	The method is invoked in compiler generated early binding methods.
*/
	final protected String get_string(String value) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		return value;
//		} // syncObject
	}


/**
	Checks if the submitted logical number does not mean unset value and, if so,
	returns it.
	The method is invoked in compiler generated early binding methods.
*/
	final protected int get_logical(int value) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (value == 0) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		return value;
//		} // syncObject
	}


/**
	Checks if the submitted boolean value does not mean unset value and, if so,
	returns it.
	The method is invoked in compiler generated early binding methods.
*/
	final protected boolean get_boolean(int value) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (value == 0) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		return (value==1)?false:true;
//		} // syncObject
	}


/**
	Checks if the submitted enumeration value does not mean unset value and,
	if so, returns it.
	The method is invoked in compiler generated early binding methods.
*/
	final protected int get_enumeration(int value) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (value == 0) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		return value;
//		} // syncObject
	}


/**
	Checks if the submitted <code>Binary</code> value is nonnull and, if so,
	returns it.
	The method is invoked in compiler generated early binding methods.
*/
	final protected Binary get_binary(Binary value) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		return value;
//		} // syncObject
	}


/**
	Checks if the submitted value of type entity instance is nonnull and,
	if so, returns it. If the type of the value is <code>Connector</code>,
	then <code>resolveConnector</code> is applied.
	The method is invoked in compiler generated early binding methods.
*/
	final protected CEntity get_instance(Object value) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (value instanceof SdaiModel.Connector) {
			value = ((SdaiModel.Connector)value).resolveConnector(true, true, false);
		}
		return (CEntity)value;
//		} // syncObject
	}


/**
	Checks if the submitted <code>Aggregate</code> value is nonnull and, if so,
	returns it.
	The method is invoked in compiler generated early binding methods.
*/
	final protected Aggregate get_aggregate(Aggregate value) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		return value;
//		} // syncObject
	}


/**
	Returns integer value for an attribute of select type.
	The method is invoked in compiler generated early binding methods.
*/
	final protected int get_integer_select(Object value, int sel_number, int case_number)
			throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (sel_number != case_number || !(value instanceof Integer)) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		int int_value = ((Integer)value).intValue();
		if (int_value == Integer.MIN_VALUE) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		return int_value;
//		} // syncObject
	}


/**
	Returns double value for an attribute of select type.
	The method is invoked in compiler generated early binding methods.
*/
	final protected double get_double_select(Object value, int sel_number, int case_number)
			throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (sel_number != case_number || !(value instanceof Double)) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		double double_value = ((Double)value).doubleValue();
		if (Double.isNaN(double_value)) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		return double_value;
//		} // syncObject
	}


/**
	Returns <code>String</code> value for an attribute of select type.
	The method is invoked in compiler generated early binding methods.
*/
	final protected String get_string_select(Object value, int sel_number, int case_number)
			throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (sel_number != case_number || !(value instanceof String)) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		return (String)value;
//		} // syncObject
	}


/**
	Returns logical value for an attribute of select type.
	The method is invoked in compiler generated early binding methods.
*/
	final protected int get_logical_select(Object value, int sel_number, int case_number)
			throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (sel_number != case_number || !(value instanceof Integer)) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		int int_value = ((Integer)value).intValue();
		if (int_value == 0) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		return int_value;
//		} // syncObject
	}


/**
	Returns boolean value for an attribute of select type.
	The method is invoked in compiler generated early binding methods.
*/
	final protected boolean get_boolean_select(Object value, int sel_number, int case_number)
			throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (sel_number != case_number || !(value instanceof Integer)) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		int int_value = ((Integer)value).intValue();
		if (int_value == 0) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		return (int_value==1)?false:true;
//		} // syncObject
	}


/**
	Returns enumeration value for an attribute of select type.
	The method is invoked in compiler generated early binding methods.
*/
	final protected int get_enumeration_select(Object value, int sel_number, int case_number)
			throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (sel_number != case_number || !(value instanceof Integer)) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		int int_value = ((Integer)value).intValue();
		if (int_value == 0) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		return int_value;
//		} // syncObject
	}


/**
	Returns <code>Binary</code> value for an attribute of select type.
	The method is invoked in compiler generated early binding methods.
*/
	final protected Binary get_binary_select(Object value, int sel_number, int case_number)
			throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (sel_number != case_number || !(value instanceof Binary)) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		return (Binary)value;
//		} // syncObject
	}


/**
	Returns value of type entity instance for an attribute of select type.
	The method is invoked in compiler generated early binding methods.
*/
	final protected CEntity get_instance_select(Object value)
			throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (value instanceof SdaiModel.Connector) {
			value = ((SdaiModel.Connector)value).resolveConnector(true, true, false);
		}
		if (!(value instanceof CEntity)) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		return (CEntity)value;
//		} // syncObject
	}


/**
	Returns <code>Aggregate</code> value for an attribute of select type.
	The method is invoked in compiler generated early binding methods.
*/
	final protected Aggregate get_aggregate_select(Object value, int sel_number, int case_number)
			throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (sel_number != case_number || !(value instanceof Aggregate)) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		return (Aggregate)value;
//		} // syncObject
	}


// set methods
/**
	Performs operations related with assigning integer value to an attribute.
	The method is invoked in compiler generated early binding methods.
*/
	final protected int set_integer(int value) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		if (value == Integer.MIN_VALUE) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		session.undoRedoModifyPrepare(this);
		modified();
		return value;
//		} // syncObject
	}


/**
	Performs operations related with assigning double value to an attribute.
	The method is invoked in compiler generated early binding methods.
*/
	final protected double set_double(double value) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		if (Double.isNaN(value)) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		session.undoRedoModifyPrepare(this);
		modified();
		return value;
//		} // syncObject
	}


/**
	Performs operations related with assigning <code>String</code> value
	to an attribute.
	The method is invoked in compiler generated early binding methods.
*/
	final protected String set_string(String value) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		session.undoRedoModifyPrepare(this);
		modified();
		return value;
//		} // syncObject
	}


/**
	Performs operations related with assigning logical value to an attribute.
	The method is invoked in compiler generated early binding methods.
*/
	final protected int set_logical(int value) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		if (value == 0) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (value < 1 || value > 3) {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		session.undoRedoModifyPrepare(this);
		modified();
		return value;
//		} // syncObject
	}


/**
	Performs operations related with assigning boolean value to an attribute.
	The method is invoked in compiler generated early binding methods.
*/
	final protected int set_boolean(boolean value) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		session.undoRedoModifyPrepare(this);
		modified();
		return value?2:1;
//		} // syncObject
	}


/**
	Performs operations related with assigning enumeration value to an attribute.
	The method is invoked in compiler generated early binding methods.
*/
	final protected int set_enumeration(int value, CExplicit_attribute attr) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		if (value == 0) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		DataType domain = (DataType)(CDefined_type)attr.getDomain(null);
		while (domain.express_type == DataType.DEFINED_TYPE) {
			domain = (DataType)((CDefined_type)domain).getDomain(null);
		}
		if (domain.express_type < DataType.ENUMERATION || domain.express_type > DataType.EXTENDED_EXTENSIBLE_ENUM) {
			throw new SdaiException(SdaiException.AT_NVLD, attr);
		}
		EEnumeration_type enum_type = (EEnumeration_type)domain;
		A_string	elements;
		DataType dt = (DataType)enum_type;
		if (dt.express_type == DataType.EXTENSIBLE_ENUM || dt.express_type == DataType.EXTENDED_EXTENSIBLE_ENUM) {
			elements = enum_type.getElements(null, owning_model.repository.session.sdai_context);
		} else {
			elements = enum_type.getElements(null);
		}
		if (value > ((CAggregate)elements).myLength || value < 1) {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
		session.undoRedoModifyPrepare(this);
		modified();
		return value;
//		} // syncObject
	}


/**
	Performs operations related with assigning <code>Binary</code> value
	to an attribute.
	The method is invoked in compiler generated early binding methods.
*/
	final protected Binary set_binary(Binary value) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		session.undoRedoModifyPrepare(this);
		modified();
		return value;
//		} // syncObject
	}


/**
	Performs operations related with assigning value of type entity
	instance to an attribute.
	The method is invoked in compiler generated early binding methods.
*/
	final protected EEntity set_instance(Object old_value, EEntity value)
			throws SdaiException {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		CEntity ref = (CEntity)value;
		if (ref.owning_model == null) {
			throw new SdaiException(SdaiException.EI_NVLD, ref);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (old_value instanceof CEntity) {
			CEntity old_ref = (CEntity)old_value;
			if (!old_ref.owning_model.optimized) {
				removeFromInverseList(old_ref);
			}
		}
/*try {
if (instance_identifier == 8693) {
System.out.println("  CEntity **** EARLY BINDING inst set: #" + instance_identifier +
"   with value: #" + ((CEntity)value).instance_identifier +
"   owning_model: " + owning_model.name);
System.out.println("CEntity ^^^^^^^^^ this: " + this);
CEntity inst7636 = (CEntity)owning_model.repository.getSessionIdentifier("#7636");
System.out.println("CEntity ^^^^^^^^^ inverse list of: #" + inst7636.instance_identifier);
inst7636.printInverses();
System.out.println("CEntity ^^^^^^^^^ inverse list of: #" + this.instance_identifier);
this.printInverses();
CEntity inst4 = (CEntity)owning_model.repository.getSessionIdentifier("#4");
System.out.println("CEntity ^^^^^^^^^ inverse list of: #" + inst4.instance_identifier);
inst4.printInverses();
CEntity inst7972 = (CEntity)owning_model.repository.getSessionIdentifier("#7972");
System.out.println("CEntity ^^^^^^^^^ inverse list of: #" + inst7972.instance_identifier);
inst7972.printInverses();
System.out.println("");System.out.println("");
if (((CEntity)value).instance_identifier == 8693) throw new SdaiException(SdaiException.SY_ERR);
}
} catch (SdaiException ex) {
ex.printStackTrace();
}*/
		if (!ref.owning_model.optimized) {
			addToInverseList(ref);
		}
		session.undoRedoModifyPrepare(this);
		modified();
		return value;
	}


/**
	Performs operations related with assigning value of type entity
	instance to an attribute that is redeclared as derived.
	The method is invoked in compiler generated early binding methods.
*/
	final protected EEntity set_instanceX(Object old_value, EEntity value)
			throws SdaiException {
/*		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (((CEntity)value).owning_model == null) {
			throw new SdaiException(SdaiException.EI_NVLD, (CEntity)value);
		}
if (instance_identifier == 49)
System.out.println("CEntity  XXXXXXX  this: #" + instance_identifier + 
"   old_value: " + old_value + "   value: " + value);
		if (old_value instanceof CEntity) {
			removeFromInverseList((CEntity)old_value);
		}
		modified();
if (instance_identifier == 49) System.out.println("CEntity  XXXXXXX  this: " + this);
		return value;*/
		return set_instance(old_value, value);
	}


/**
	Performs operations related with assigning integer value to an attribute
	of select type.
	The method is invoked in compiler generated early binding methods.
*/
	final protected Integer set_integer_select(int value) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		if (value == Integer.MIN_VALUE) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		session.undoRedoModifyPrepare(this);
		modified();
		return new Integer(value);
//		} // syncObject
	}


/**
	Performs operations related with assigning double value to an attribute
	of select type.
	The method is invoked in compiler generated early binding methods.
*/
	final protected Double set_double_select(double value) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		if (Double.isNaN(value)) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		session.undoRedoModifyPrepare(this);
		modified();
		return new Double(value);
//		} // syncObject
	}


/**
	Performs operations related with assigning logical value to an attribute
	of select type.
	The method is invoked in compiler generated early binding methods.
*/
	final protected Integer set_logical_select(int value) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		if (value == 0) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (value < 1 || value > 3) {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		session.undoRedoModifyPrepare(this);
		modified();
		return new Integer(value);
//		} // syncObject
	}


/**
	Performs operations related with assigning boolean value to an attribute
	of select type.
	The method is invoked in compiler generated early binding methods.
*/
	final protected Integer set_boolean_select(boolean value) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		session.undoRedoModifyPrepare(this);
		modified();
		return new Integer(value?2:1);
//		} // syncObject
	}


/**
	Performs operations related with assigning enumeration value to an attribute
	of select type.
	The method is invoked in compiler generated early binding methods.
*/
	final protected Integer set_enumeration_select(int value, CExplicit_attribute attr) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		if (value == 0) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		DataType dom = (DataType)attr.getDomain(null);
		if (dom.express_type != DataType.DEFINED_TYPE) {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
		if (!check_def_type(value, (CDefined_type)dom, false)) {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
		session.undoRedoModifyPrepare(this);
		modified();
		return new Integer(value);
//		} // syncObject
	}


/**
	Checks if the submitted enumeration value is a legal one for the specified
	defined type.
	The method is invoked in compiler generated early binding methods.
*/
	final private boolean check_def_type(int value, CDefined_type def_type, boolean inside)
			throws SdaiException {
		SdaiSession ss;
		boolean res;
		StaticFields staticFields = null;
		DataType dom = (DataType)def_type.getDomain(null);
		if (dom.express_type >= DataType.SELECT && dom.express_type <= DataType.ENT_EXT_EXT_SELECT) {
			ANamed_type	selections;
			if (dom.express_type >= DataType.EXTENSIBLE_SELECT) {
				ss = owning_model.repository.session;
				if (ss.sdai_context == null) {
					staticFields = StaticFields.get();
					staticFields.context_schema = owning_model.underlying_schema;
					if (!ss.sdai_context_missing) {
						ss.sdai_context_missing = true;
						ss.printWarningToLogoSdaiContext(AdditionalMessages.SS_SCMI);
					}
				}
				selections = ((EExtensible_select_type)dom).getSelections(null, ss.sdai_context);
				if (ss.sdai_context == null) {
					staticFields.context_schema = null;
				}
			} else {
				selections = ((ESelect_type)dom).getSelections(null);
			}
			AEntity sels = (AEntity)selections;
			if (sels.myLength < 0) {
				sels.resolveAllConnectors();
			}
			Object [] myDataA = null;
			if (sels.myLength > 1) {
				myDataA = (Object [])sels.myData;
			}
			for (int i = 0; i < sels.myLength; i++) {
				DataType element;
				if (sels.myLength == 1) {
					element = (DataType)sels.myData;
				} else {
					element = (DataType)myDataA[i];
				}
				if (element.express_type == DataType.DEFINED_TYPE) {
					res = check_def_type(value, (CDefined_type)element, true);
					if (res) {
						return res;
					}
				}
			}
			return false;
		} else if (dom.express_type == DataType.DEFINED_TYPE) {
			return check_def_type(value, (CDefined_type)dom, inside);
		} else if (inside && dom.express_type >= DataType.ENUMERATION && dom.express_type <= DataType.EXTENDED_EXTENSIBLE_ENUM) {
			A_string	elements;
			if (dom.express_type == DataType.EXTENSIBLE_ENUM || dom.express_type == DataType.EXTENDED_EXTENSIBLE_ENUM) {
				ss = owning_model.repository.session;
				if (ss.sdai_context == null) {
					staticFields = StaticFields.get();
					staticFields.context_schema = owning_model.underlying_schema;
					if (!ss.sdai_context_missing) {
						ss.sdai_context_missing = true;
						ss.printWarningToLogoSdaiContext(AdditionalMessages.SS_SCMI);
					}
				}
				elements = ((EExtensible_enumeration_type)dom).getElements(null, owning_model.repository.session.sdai_context);
				if (ss.sdai_context == null) {
					staticFields.context_schema = null;
				}
			} else {
				elements = ((EEnumeration_type)dom).getElements(null);
			}
			if (value <= ((CAggregate)elements).myLength && value > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}


// create methods
/**
	Creates an aggregate for values of type integer.
	The method is invoked in compiler generated early binding methods.
*/
	final protected A_integer create_aggregate_integer(Object old_value, CExplicit_attribute attr, int iSelect)
			throws SdaiException {
//		synchronized (syncObject) {
			return new A_integer(create_aggregate_integer_internal(old_value, attr, iSelect), this);
//		} // syncObject
	}


/**
	Creates an aggregate for values of type double.
	The method is invoked in compiler generated early binding methods.
*/
	final protected A_double create_aggregate_double(Object old_value, CExplicit_attribute attr, int iSelect)
			throws SdaiException {
//		synchronized (syncObject) {
			DataType domain = (DataType)attr.getDomain(null);
			if (domain.express_type >= DataType.LIST && domain.express_type <= DataType.ARRAY) {
				if (owning_model == null) {
					throw new SdaiException(SdaiException.EI_NEXS);
				}
				if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
					throw new SdaiException(SdaiException.MX_NRW, owning_model);
				}
				SdaiSession session = owning_model.repository.session;
				if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
					String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				session.undoRedoModifyPrepare(this);
				modified();
				if (((AggregationType)domain).check_aggregation_double()) {
					return new A_double3((EAggregation_type)domain, this);
				} else {
					return new A_double((EAggregation_type)domain, this);
				}
			} else {
				EAggregation_type aggr_tp = create_aggregate_double_internal(old_value, attr, iSelect);
				if (((AggregationType)aggr_tp).check_aggregation_double()) {
					return new A_double3(aggr_tp, this);
				} else {
					return new A_double(aggr_tp, this);
				}
			}
//		} // syncObject
	}


/**
	Creates an aggregate for values of type <code>String</code>.
	The method is invoked in compiler generated early binding methods.
*/
	final protected A_string create_aggregate_string(Object old_value, CExplicit_attribute attr, int iSelect)
			throws SdaiException {
//		synchronized (syncObject) {
			return new A_string(create_aggregate_string_internal(old_value, attr, iSelect), this);
//		} // syncObject
	}


/**
	Creates an aggregate for values of type boolean.
	The method is invoked in compiler generated early binding methods.
*/
	final protected A_boolean create_aggregate_boolean(Object old_value, CExplicit_attribute attr, int iSelect)
			throws SdaiException {
//		synchronized (syncObject) {
			return new A_boolean(create_aggregate_boolean_internal(old_value, attr, iSelect), this);
//		} // syncObject
	}


/**
	Creates an aggregate for values of type enumeration.
	The method is invoked in compiler generated early binding methods.
*/
	final protected A_enumeration create_aggregate_enumeration(Object old_value, CExplicit_attribute attr, int iSelect)
			throws SdaiException {
//		synchronized (syncObject) {
			return new A_enumeration(create_aggregate_enumeration_internal(old_value, attr, iSelect), this);
//		} // syncObject
	}


/**
	Creates an aggregate for values of type <code>Binary</code>.
	The method is invoked in compiler generated early binding methods.
*/
	final protected A_binary create_aggregate_binary(Object old_value, CExplicit_attribute attr, int iSelect)
			throws SdaiException {
//		synchronized (syncObject) {
			return new A_binary(create_aggregate_binary_internal(old_value, attr, iSelect), this);
//		} // syncObject
	}


/**
	Creates an aggregate for values of select type.
	The type of the aggregate is specified by the third parameter.
	For example, ATrimming_select.class.
	The method is invoked in compiler generated early binding methods.
*/
	final protected CAggregate create_aggregate_class(Object old_value, CExplicit_attribute attr, Class aggr_type, int iSelect)
			throws SdaiException {
//		synchronized (syncObject) {
		CAggregate aggr;
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (old_value instanceof CEntity) {
			CEntity old_ref = (CEntity)old_value;
			if (!old_ref.owning_model.optimized) {
				removeFromInverseList(old_ref);
			}
		} else if (old_value instanceof CAggregate) {
			((CAggregate)old_value).unsetAllByRef(this);
		}
		EAggregation_type provided_type;
if (SdaiSession.debug2) System.out.println("  iSelect = " + iSelect);
		DataType domain = (DataType)attr.getDomain(null);
		while (domain.express_type == DataType.DEFINED_TYPE) {
			domain = (DataType)((CDefined_type)domain).getDomain(null);
		}
//System.out.println(" !!!DOMAIN: " + dom.getClass().getName());
		if (iSelect == 0) {
			if (domain.express_type < DataType.LIST || domain.express_type > DataType.AGGREGATE) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			provided_type = (EAggregation_type)domain;
		} else {
			if (domain.express_type < DataType.SELECT || domain.express_type > DataType.ENT_EXT_EXT_SELECT) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			SelectType sel = (SelectType)domain;
			provided_type = (AggregationType)sel.types[iSelect - 2];
		}
if (SdaiSession.debug2) System.out.println("  provided_type = " + provided_type);
		try {
			aggr = (CAggregate)aggr_type.newInstance();
		} catch (Exception ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
		aggr.attach(provided_type, this);
		session.undoRedoModifyPrepare(this);
		modified();
if (SdaiSession.debug2) if (aggr.myType == null) System.out.println("   myType is NULL");
else System.out.println("   myType is POSITIVE");
		return aggr;
//		} // syncObject
	}


/**
	Currently, a dummy method.
	The method, possibly, is invoked in compiler generated early binding methods.
*/
	protected Object create_aggregate_select(Object attr_v, CExplicit_attribute attr,
			Class aggr_type, int iSelect , int x) throws SdaiException {
//		synchronized (syncObject) {
		CAggregate aggr;
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (attr_v instanceof CEntity) {
			CEntity old_ref = (CEntity)attr_v;
			if (!old_ref.owning_model.optimized) {
				removeFromInverseList(old_ref);
			}
		} else if (attr_v instanceof CAggregate) {
			((CAggregate)attr_v).unsetAllByRef(this);
		}
		EAggregation_type provided_type;
		DataType domain = (DataType)attr.getDomain(null);
		while (domain.express_type == DataType.DEFINED_TYPE) {
			domain = (DataType)((CDefined_type)domain).getDomain(null);
		}
		if (iSelect == 0) {
			if (domain.express_type < DataType.LIST || domain.express_type > DataType.AGGREGATE) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			provided_type = (EAggregation_type)domain;
		} else {
			if (domain.express_type < DataType.SELECT || domain.express_type > DataType.ENT_EXT_EXT_SELECT) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			SelectType sel = (SelectType)domain;
			provided_type = (AggregationType)sel.types[iSelect - 2];
		}
		try {
			aggr = (CAggregate)aggr_type.newInstance();
		} catch (Exception ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
		aggr.attach(provided_type, this);
		session.undoRedoModifyPrepare(this);
		modified();
		return aggr;
//		} // syncObject
	}


/**
	Creates a double nested aggregate for values of type integer.
	The method is invoked in compiler generated early binding methods.
*/
	final protected Aa_integer create_aggregate2_integer(Object old_value, CExplicit_attribute attr, int iSelect)
			throws SdaiException {
//		synchronized (syncObject) {
			return new Aa_integer(create_aggregate_integer_internal(old_value, attr, iSelect), this);
//		} // syncObject
	}


/**
	Creates a double nested aggregate for values of type double.
	The method is invoked in compiler generated early binding methods.
*/
	final protected Aa_double create_aggregate2_double(Object old_value, CExplicit_attribute attr, int iSelect)
			throws SdaiException {
//		synchronized (syncObject) {
			return new Aa_double(create_aggregate_double_internal(old_value, attr, iSelect), this);
//		} // syncObject
	}


/**
	Creates a double nested aggregate for values of type <code>String</code>.
	The method is invoked in compiler generated early binding methods.
*/
	final protected Aa_string create_aggregate2_string(Object old_value, CExplicit_attribute attr, int iSelect)
			throws SdaiException {
//		synchronized (syncObject) {
			return new Aa_string(create_aggregate_string_internal(old_value, attr, iSelect), this);
//		} // syncObject
	}


/**
	Creates a double nested aggregate for values of type boolean.
	The method is invoked in compiler generated early binding methods.
*/
	final protected Aa_boolean create_aggregate2_boolean(Object old_value, CExplicit_attribute attr, int iSelect)
			throws SdaiException {
//		synchronized (syncObject) {
			return new Aa_boolean(create_aggregate_boolean_internal(old_value, attr, iSelect), this);
//		} // syncObject
	}


/**
	Creates a double nested aggregate for values of type enumeration.
	The method is invoked in compiler generated early binding methods.
*/
	final protected Aa_enumeration create_aggregate2_enumeration(Object old_value, CExplicit_attribute attr, int iSelect)
			throws SdaiException {
//		synchronized (syncObject) {
			return new Aa_enumeration(create_aggregate_enumeration_internal(old_value, attr, iSelect), this);
//		} // syncObject
	}


/**
	Creates a double nested aggregate for values of type <code>Binary</code>.
	The method is invoked in compiler generated early binding methods.
*/
	final protected Aa_binary create_aggregate2_binary(Object old_value, CExplicit_attribute attr, int iSelect)
			throws SdaiException {
//		synchronized (syncObject) {
			return new Aa_binary(create_aggregate_binary_internal(old_value, attr, iSelect), this);
//		} // syncObject
	}


/**
	Creates a triple nested aggregate for values of type integer.
	The method is invoked in compiler generated early binding methods.
*/
	final protected Aaa_integer create_aggregate3_integer(Object old_value, CExplicit_attribute attr, int iSelect)
			throws SdaiException {
//		synchronized (syncObject) {
			return new Aaa_integer(create_aggregate_integer_internal(old_value, attr, iSelect), this);
//		} // syncObject
	}


/**
	Creates a triple nested aggregate for values of type double.
	The method is invoked in compiler generated early binding methods.
*/
	final protected Aaa_double create_aggregate3_double(Object old_value, CExplicit_attribute attr, int iSelect)
			throws SdaiException {
//		synchronized (syncObject) {
			return new Aaa_double(create_aggregate_double_internal(old_value, attr, iSelect), this);
//		} // syncObject
	}


/**
	Creates a triple nested aggregate for values of type <code>String</code>.
	The method is invoked in compiler generated early binding methods.
*/
	final protected Aaa_string create_aggregate3_string(Object old_value, CExplicit_attribute attr, int iSelect)
			throws SdaiException {
//		synchronized (syncObject) {
			return new Aaa_string(create_aggregate_string_internal(old_value, attr, iSelect), this);
//		} // syncObject
	}


/**
	Creates a triple nested aggregate for values of type boolean.
	The method is invoked in compiler generated early binding methods.
*/
	final protected Aaa_boolean create_aggregate3_boolean(Object old_value, CExplicit_attribute attr, int iSelect)
			throws SdaiException {
//		synchronized (syncObject) {
			return new Aaa_boolean(create_aggregate_boolean_internal(old_value, attr, iSelect), this);
//		} // syncObject
	}


/**
	Creates a triple nested aggregate for values of type enumeration.
	The method is invoked in compiler generated early binding methods.
*/
	final protected Aaa_enumeration create_aggregate3_enumeration(Object old_value, CExplicit_attribute attr, int iSelect)
			throws SdaiException {
//		synchronized (syncObject) {
			return new Aaa_enumeration(create_aggregate_enumeration_internal(old_value, attr, iSelect), this);
//		} // syncObject
	}


/**
	Creates a triple nested aggregate for values of type <code>Binary</code>.
	The method is invoked in compiler generated early binding methods.
*/
	final protected Aaa_binary create_aggregate3_binary(Object old_value, CExplicit_attribute attr, int iSelect)
			throws SdaiException {
//		synchronized (syncObject) {
			return new Aaa_binary(create_aggregate_binary_internal(old_value, attr, iSelect), this);
//		} // syncObject
	}


/**
	Prepares aggregation type and performs other operations related with
	creation of an aggregate for values of type integer.
	The method is invoked in <code>create_aggregate_integer</code>,
	<code>create_aggregate2_integer</code> and <code>create_aggregate3_integer</code>
	methods.
*/
	final private EAggregation_type create_aggregate_integer_internal(Object old_value,
			CExplicit_attribute attr, int iSelect) throws SdaiException {
		if (iSelect == 1) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (old_value instanceof CEntity) {
			CEntity old_ref = (CEntity)old_value;
			if (!old_ref.owning_model.optimized) {
				removeFromInverseList(old_ref);
			}
		} else if (old_value instanceof CAggregate) {
			((CAggregate)old_value).unsetAllByRef(this);
		}
		EAggregation_type provided_type;
		DataType domain = (DataType)attr.getDomain(null);
		while (domain.express_type == DataType.DEFINED_TYPE) {
			domain = (DataType)((CDefined_type)domain).getDomain(null);
		}
		if (iSelect == 0) {
			if (domain.express_type < DataType.LIST || domain.express_type > DataType.AGGREGATE) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			provided_type = (EAggregation_type)attr.getDomain(null);
		} else {
			if (domain.express_type < DataType.SELECT || domain.express_type > DataType.ENT_EXT_EXT_SELECT) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			provided_type = (AggregationType)((SelectType)domain).types[iSelect - 2];
		}
		session.undoRedoModifyPrepare(this);
		modified();
		return provided_type;
	}


/**
	Prepares aggregation type and performs other operations related with
	creation of an aggregate for values of type double.
	The method is invoked in <code>create_aggregate_double</code>,
	<code>create_aggregate2_double</code> and <code>create_aggregate3_double</code>
	methods.
*/
	final private EAggregation_type create_aggregate_double_internal(Object old_value,
			CExplicit_attribute attr, int iSelect) throws SdaiException {
		if (iSelect == 1) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		EAggregation_type provided_type;
		DataType domain = (DataType)attr.getDomain(null);
		while (domain.express_type == DataType.DEFINED_TYPE) {
			domain = (DataType)((CDefined_type)domain).getDomain(null);
		}
		if (iSelect == 0) {
			if (domain.express_type < DataType.LIST || domain.express_type > DataType.AGGREGATE) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			provided_type = (EAggregation_type)domain;
		} else {
			if (domain.express_type < DataType.SELECT || domain.express_type > DataType.ENT_EXT_EXT_SELECT) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			if (old_value instanceof CEntity) {
				CEntity old_ref = (CEntity)old_value;
				if (!old_ref.owning_model.optimized) {
					removeFromInverseList(old_ref);
				}
			} else if (old_value instanceof CAggregate) {
				((CAggregate)old_value).unsetAllByRef(this);
			}
			provided_type = (AggregationType)((SelectType)domain).types[iSelect - 2];
		}
		session.undoRedoModifyPrepare(this);
		modified();
		return provided_type;
	}


/**
	Prepares aggregation type and performs other operations related with
	creation of an aggregate for values of type <code>String</code>.
	The method is invoked in <code>create_aggregate_string</code>,
	<code>create_aggregate2_string</code> and <code>create_aggregate3_string</code>
	methods.
*/
	final private EAggregation_type create_aggregate_string_internal(Object old_value,
			CExplicit_attribute attr, int iSelect) throws SdaiException {
		if (iSelect == 1) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (old_value instanceof CEntity) {
			CEntity old_ref = (CEntity)old_value;
			if (!old_ref.owning_model.optimized) {
				removeFromInverseList(old_ref);
			}
		} else if (old_value instanceof CAggregate) {
			((CAggregate)old_value).unsetAllByRef(this);
		}
		EAggregation_type provided_type;
		DataType domain = (DataType)attr.getDomain(null);
		while (domain.express_type == DataType.DEFINED_TYPE) {
			domain = (DataType)((CDefined_type)domain).getDomain(null);
		}
		if (iSelect == 0) {
			if (domain.express_type < DataType.LIST || domain.express_type > DataType.AGGREGATE) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			provided_type = (EAggregation_type)attr.getDomain(null);
		} else {
			if (domain.express_type < DataType.SELECT || domain.express_type > DataType.ENT_EXT_EXT_SELECT) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			provided_type = (AggregationType)((SelectType)domain).types[iSelect - 2];
		}
		session.undoRedoModifyPrepare(this);
		modified();
		return provided_type;
	}


/**
	Prepares aggregation type and performs other operations related with
	creation of an aggregate for values of type boolean.
	The method is invoked in <code>create_aggregate_boolean</code>,
	<code>create_aggregate2_boolean</code> and <code>create_aggregate3_boolean</code>
	methods.
*/
	final private EAggregation_type create_aggregate_boolean_internal(Object old_value,
			CExplicit_attribute attr, int iSelect) throws SdaiException {
		if (iSelect == 1) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (old_value instanceof CEntity) {
			CEntity old_ref = (CEntity)old_value;
			if (!old_ref.owning_model.optimized) {
				removeFromInverseList(old_ref);
			}
		} else if (old_value instanceof CAggregate) {
			((CAggregate)old_value).unsetAllByRef(this);
		}
		EAggregation_type provided_type;
		DataType domain = (DataType)attr.getDomain(null);
		while (domain.express_type == DataType.DEFINED_TYPE) {
			domain = (DataType)((CDefined_type)domain).getDomain(null);
		}
		if (iSelect == 0) {
			if (domain.express_type < DataType.LIST || domain.express_type > DataType.AGGREGATE) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			provided_type = (EAggregation_type)attr.getDomain(null);
		} else {
			if (domain.express_type < DataType.SELECT || domain.express_type > DataType.ENT_EXT_EXT_SELECT) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			provided_type = (AggregationType)((SelectType)domain).types[iSelect - 2];
		}
		session.undoRedoModifyPrepare(this);
		modified();
		return provided_type;
	}


/**
	Prepares aggregation type and performs other operations related with
	creation of an aggregate for values of type enumeration.
	The method is invoked in <code>create_aggregate_enumeration</code>,
	<code>create_aggregate2_enumeration</code> and <code>create_aggregate3_enumeration</code>
	methods.
*/
	final private EAggregation_type create_aggregate_enumeration_internal(Object old_value,
			CExplicit_attribute attr, int iSelect) throws SdaiException {
		if (iSelect == 1) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (old_value instanceof CEntity) {
			CEntity old_ref = (CEntity)old_value;
			if (!old_ref.owning_model.optimized) {
				removeFromInverseList(old_ref);
			}
		} else if (old_value instanceof CAggregate) {
			((CAggregate)old_value).unsetAllByRef(this);
		}
		EAggregation_type provided_type;
		DataType domain = (DataType)attr.getDomain(null);
		while (domain.express_type == DataType.DEFINED_TYPE) {
			domain = (DataType)((CDefined_type)domain).getDomain(null);
		}
		if (iSelect == 0) {
			if (domain.express_type < DataType.LIST || domain.express_type > DataType.AGGREGATE) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			provided_type = (EAggregation_type)attr.getDomain(null);
		} else {
			if (domain.express_type < DataType.SELECT || domain.express_type > DataType.ENT_EXT_EXT_SELECT) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			provided_type = (AggregationType)((SelectType)domain).types[iSelect - 2];
		}
		session.undoRedoModifyPrepare(this);
		modified();
		return provided_type;
	}


/**
	Prepares aggregation type and performs other operations related with
	creation of an aggregate for values of type <code>Binary</code>.
	The method is invoked in <code>create_aggregate_binary</code>,
	<code>create_aggregate2_binary</code> and <code>create_aggregate3_binary</code>
	methods.
*/
	final private EAggregation_type create_aggregate_binary_internal(Object old_value,
			CExplicit_attribute attr, int iSelect)	throws SdaiException {
		if (iSelect == 1) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (old_value instanceof CEntity) {
			CEntity old_ref = (CEntity)old_value;
			if (!old_ref.owning_model.optimized) {
				removeFromInverseList(old_ref);
			}
		} else if (old_value instanceof CAggregate) {
			((CAggregate)old_value).unsetAllByRef(this);
		}
		EAggregation_type provided_type;
		DataType domain = (DataType)attr.getDomain(null);
		while (domain.express_type == DataType.DEFINED_TYPE) {
			domain = (DataType)((CDefined_type)domain).getDomain(null);
		}
		if (iSelect == 0) {
			if (domain.express_type < DataType.LIST || domain.express_type > DataType.AGGREGATE) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			provided_type = (EAggregation_type)attr.getDomain(null);
		} else {
			if (domain.express_type < DataType.SELECT || domain.express_type > DataType.ENT_EXT_EXT_SELECT) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			provided_type = (AggregationType)((SelectType)domain).types[iSelect - 2];
		}
		session.undoRedoModifyPrepare(this);
		modified();
		return provided_type;
	}



// unset methods
/**
	Performs actions related with unset operation applied to an attribute
	of integer type.
	The method is invoked in compiler generated early binding methods.
*/
	final protected int unset_integer() throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE && !owning_model.bypass_setAll) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		session.undoRedoModifyPrepare(this);
		modified();
		return Integer.MIN_VALUE;
//		} // syncObject
	}


/**
	Performs actions related with unset operation applied to an attribute
	of double type.
	The method is invoked in compiler generated early binding methods.
*/
	final protected double unset_double() throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE && !owning_model.bypass_setAll) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		session.undoRedoModifyPrepare(this);
		modified();
		return Double.NaN;
//		} // syncObject
	}


/**
	Performs actions related with unset operation applied to an attribute
	of <code>String</code> type.
	The method is invoked in compiler generated early binding methods.
*/
	final protected String unset_string() throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE && !owning_model.bypass_setAll) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		session.undoRedoModifyPrepare(this);
		modified();
		return null;
//		} // syncObject
	}


/**
	Performs actions related with unset operation applied to an attribute
	of boolean type.
	The method is invoked in compiler generated early binding methods.
*/
	final protected int unset_boolean() throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE && !owning_model.bypass_setAll) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		session.undoRedoModifyPrepare(this);
		modified();
		return 0;
//		} // syncObject
	}


/**
	Performs actions related with unset operation applied to an attribute
	of logical type.
	The method is invoked in compiler generated early binding methods.
*/
	final protected int unset_logical() throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE && !owning_model.bypass_setAll) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		session.undoRedoModifyPrepare(this);
		modified();
		return 0;
//		} // syncObject
	}


/**
	Performs actions related with unset operation applied to an attribute
	of enumeration type.
	The method is invoked in compiler generated early binding methods.
*/
	final protected int unset_enumeration() throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE && !owning_model.bypass_setAll) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		session.undoRedoModifyPrepare(this);
		modified();
		return 0;
//		} // syncObject
	}


/**
	Performs actions related with unset operation applied to an attribute
	of <code>Binary</code> type.
	The method is invoked in compiler generated early binding methods.
*/
	final protected Binary unset_binary() throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE && !owning_model.bypass_setAll) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		session.undoRedoModifyPrepare(this);
		modified();
		return null;
//		} // syncObject
	}


/**
	Performs actions related with unset operation applied to an attribute
	of type entity instance.
	The method is invoked in compiler generated early binding methods.
*/
	final protected Object unset_instance(Object old_value) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE && !owning_model.bypass_setAll) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (old_value instanceof CEntity) {
			CEntity to_ent = (CEntity)old_value;
			if (to_ent.owning_model != null && !to_ent.owning_model.closingAll && 
				!(owning_model.refresh_in_abort && to_ent.owning_model.modified) && !to_ent.owning_model.optimized) {
/*System.out.println("CEntity:  old_value: #" + to_ent.instance_identifier);
System.out.println("CEntity:  this instance being unset: #" + instance_identifier);*/
						removeFromInverseList(to_ent);
			}
		} else if (old_value instanceof SdaiModel.Connector) {
//System.out.println("CEntity:  owning_instance: #" + instance_identifier);
			((SdaiModel.Connector)old_value).disconnect();
		}
		if (session.undo_redo_file != null && !owning_model.bypass_setAll) {
			session.undoRedoModifyPrepare(this);
		}
		modified();
		return null;
//		} // syncObject
	}


/**
	Performs actions related with unset operation applied to an attribute
	of <code>Aggregate</code> type.
	The method is invoked in compiler generated early binding methods.
*/
	final protected Aggregate unset_aggregate(Aggregate old_value) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE  && !owning_model.bypass_setAll) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (old_value instanceof CAggregate) {
			((CAggregate)old_value).unsetAllByRef(this);
		}
		session.undoRedoModifyPrepare(this);
		modified();
		return null;
//		} // syncObject
	}


/**
	Performs actions related with unset operation applied to an attribute
	of select type.
	The method is invoked in compiler generated early binding methods.
*/
	final protected Object unset_select(Object old_value)  throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE && !owning_model.bypass_setAll) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (old_value instanceof CEntity) {
			CEntity to_ent = (CEntity)old_value;
			if (to_ent.owning_model != null && !to_ent.owning_model.closingAll) {
				if (!to_ent.owning_model.optimized) {
					removeFromInverseList(to_ent);
				}
			}
		} else if (old_value instanceof SdaiModel.Connector) {
			((SdaiModel.Connector)old_value).disconnect();
		} else if (old_value instanceof CAggregate) {
			((CAggregate)old_value).unsetAllByRef(this);
		}
		if (session.undo_redo_file != null && !owning_model.bypass_setAll) {
			session.undoRedoModifyPrepare(this);
		}
		modified();
		return null;
//		} // syncObject
	}


// other methods for early binding


	final protected AEntity get_inverse_aggregate(CInverse_attribute attribute) throws SdaiException {
//		synchronized (syncObject) {
			AEntity result;
			if (!attribute.testMin_cardinality(null)) {
				result = new AEntity();
			} else {
				AggregationType aggr_type = (AggregationType)((AttributeDefinition)attribute).getInverseAggregationType(this);
				try {
					result = (AEntity)aggr_type.getAggregateClass().newInstance();
				} catch (Exception ex) {
					throw new SdaiException(SdaiException.SY_ERR, ex);
				}
				result.attach(aggr_type, this);
			}
			return result;
//		} // syncObject
	}

	final protected boolean testParent_schemaInternal(EGeneric_schema_definition s_def) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (s_def instanceof ESchema_definition) {
			return (s_def == null) ? false : true;
		} else {
			return false;
		}
//		} // syncObject
	}

	final protected ESchema_definition getParent_schemaInternal(EGeneric_schema_definition s_def) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (s_def instanceof ESchema_definition) {
			if (s_def == null) {
				throw new SdaiException(SdaiException.VA_NSET);
			}
			return (ESchema_definition)s_def;
		} else {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
//		} // syncObject
	}

	final protected boolean testSuper_typeInternal(EEntity_or_view_definition def) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (((DataType)def).express_type == DataType.ENTITY) {
			return (def == null) ? false : true;
		} else {
			return false;
		}
//		} // syncObject
	}

	final protected EEntity_definition getSuper_typeInternal(EEntity_or_view_definition def) throws SdaiException {
//		synchronized (syncObject) {
		if (((DataType)def).express_type == DataType.ENTITY) {
			if (owning_model == null) {
				throw new SdaiException(SdaiException.EI_NEXS);
			}
			if (def == null) {
				throw new SdaiException(SdaiException.VA_NSET);
			}
			return (EEntity_definition)def;
		} else {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
//		} // syncObject
	}


/**
 * This method does the main job in getting the value of the derived attribute
 * 'operands' of entity 'subtype_expression' and its subtypes
 * 'andor_subtype_expression', 'and_subtype_expression', and 'oneof_subtype_expression'.
 * It extracts from the submitted aggregate instances of 'entity_definition' and
 * 'subtype_expression' while instances of 'view_definition' are bypassed.
 * The extracted instances are stored in a set returned by the method.
 * Method is invoked in dictionary package, in classes CSubtype_expression,
 * CAndor_subtype_expression, CAnd_subtype_expression, and COneof_subtype_expression.
 * @since 3.6.0
*/
	protected AEntity_or_subtype_expression getOperandsInternal(AEntity_or_view_or_subtype_expression aggr) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (aggr == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		AEntity_or_subtype_expression operands = new AEntity_or_subtype_expression();
		AEntitySelect agg = (AEntitySelect)aggr;
		SdaiIterator iter = agg.createIterator();
		int count = 0;
		if (agg.myLength <= 0) {
			return operands;
		}
		if (agg.myLength == 1) {
			process_expression(agg.myData, (CAggregate)operands, count);
		} else {
			Object [] myDataA = (Object [])agg.myData;
			for (int i = 0; i < agg.myLength; i++) {
				if (process_expression(myDataA[i], (CAggregate)operands, count)) {
					count++;
				}
			}
		}
		return operands;
//		} // syncObject
	}
	protected boolean process_expression(Object member, CAggregate operands, int count) throws SdaiException {
		CEntity inst;
		if (member instanceof SdaiModel.Connector) {
			inst = ((SdaiModel.Connector)member).resolveConnector(false, true, false);
		} else {
			inst = (CEntity)member;
		}
		if (inst instanceof ESubtype_expression) {
			operands.setForNonList(inst, count, null, null);
			return true;
		} else {
			if (((DataType)inst).express_type == DataType.ENTITY) {
				operands.setForNonList(inst, count, null, null);
				return true;
			}
		}
		return false;
	}

/**
	Changes the specified references in the submitted aggregate to point to the
	new entity instance (given as third parameter).
	The method is invoked in compiler generated early binding methods.
*/
	final protected void changeReferencesAggregate(Object a, InverseEntity old, InverseEntity newer)
			throws SdaiException {
//		synchronized (syncObject) {
		if (a != null) {
			if (a instanceof CAggregate) {
				((CAggregate)a).changeReferences(old, newer);
			}
		}
//		} // syncObject
	}


/**
	Changes the specified references in the submitted aggregate to point to the
	new entity instance (given as third parameter).
	The method is invoked in compiler generated early binding methods.
*/
	final protected void changeReferencesAggregate(CAggregate a, InverseEntity old, InverseEntity newer)
			throws SdaiException {
//		synchronized (syncObject) {
		if (a != null) {
			a.changeReferences(old, newer);
		}
//		} // syncObject
	}


/**
	Prints elements of the inverse list of this entity instance.
	The method may be invoked in Express compiler.
*/
	final protected void PrintInverseList() {
		CEntity the_inverse;
		System.out.println("The inverse list of " + instance_identifier + " instance:");
		printInverses();
	}


/*    Methods with listeners      */

/**
 * Adds <code>SdaiListener</code> extending <code>java.util.EventListener</code>
 * to a special aggregate defined for this entity instance.
 * @param listener a <code>SdaiListener</code> to be added.
 * @see #removeSdaiListener
 */
	public void addSdaiListener(SdaiListener listener) {
//		synchronized (syncObject) {
		try {
			if (listenrList == null) {
				listenrList = new CAggregate(SdaiSession.setTypeSpecial);
			}
			listenrList.addUnordered(listener, null);
		} catch (SdaiException ex) {
		}
//		} // syncObject
	}


/**
 * Removes <code>SdaiListener</code> extending <code>java.util.EventListener</code>
 * from the special aggregate defined for this entity instance.
 * @param listener <code>SdaiListener</code> to be removed.
 * @see #addSdaiListener
 */
	public void removeSdaiListener(SdaiListener listener) {
//		synchronized (syncObject) {
		try {
			if (listenrList != null) {
				listenrList.removeUnordered(listener, null);
			}
		} catch (SdaiException ex) {
		}
//		} // syncObject
	}


/**
	Notifies all listeners which have registered interest for notification
	on this event type. The event instance is lazily created using
	the parameters passed into the fire method.
*/
	protected void fireSdaiEvent(int id, int item, Object argument) {
//		synchronized (syncObject) {
		if (listenrList != null) {
			// Guaranteed to return a non-null array
			SdaiEvent sdaiEvent = null;

			// Process the listeners last to first, notifying
			// those that are interested in this event
			if (listenrList.myLength <= 0) {
				return;
			}
			if (listenrList.myLength == 1) {
				if (sdaiEvent == null) {
					sdaiEvent = new SdaiEvent(this, id, item, argument);
				}
				((SdaiListener)listenrList.myData).actionPerformed(sdaiEvent);
			} else {
				Object [] myDataA = (Object [])listenrList.myData;
				for (int i = 0; i < listenrList.myLength; i++) {
					// Lazily create the event:
					if (sdaiEvent == null) {
						sdaiEvent = new SdaiEvent(this, id, item, argument);
					}
					((SdaiListener)myDataA[i]).actionPerformed(sdaiEvent);
				}
			}
		}
//		} // syncObject
	}


/*                Mapping operations                 */

	public AEntity_mapping testMappedEntity(EEntity_definition sourceEntity, ASdaiModel targetDomain, ASdaiModel mappingDomain, int mode) throws SdaiException {
		if(Implementation.mappingSupport) {
			return Mapping.testSourceEntity(this, sourceEntity, targetDomain, mappingDomain, mode);
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}

	public boolean testMappedEntity(EEntity_mapping sourceEntity, ASdaiModel targetDomain, ASdaiModel mappingDomain, int mode) throws SdaiException {
		if(Implementation.mappingSupport) {
			return Mapping.testMappedEntity(this, sourceEntity, targetDomain, mappingDomain, mode);
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}

	public AGeneric_attribute_mapping testMappedAttribute(EAttribute sourceAttribute, ASdaiModel targetDomain, ASdaiModel mappingDomain, int mode) throws SdaiException {
		if(Implementation.mappingSupport) {
			return Mapping.testSourceAttribute(this, sourceAttribute, targetDomain, mappingDomain, mode);
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}

	public boolean testMappedAttribute(EGeneric_attribute_mapping sourceAttribute, ASdaiModel targetDomain, ASdaiModel mappingDomain, int mode) throws SdaiException {
		if(Implementation.mappingSupport) {
			return Mapping.testMappedAttribute(this, sourceAttribute, targetDomain, mappingDomain, mode);
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}

	public Object[] getMappedAttribute(EAttribute sourceAttribute, ASdaiModel targetDomain, ASdaiModel mappingDomain, int mode) throws SdaiException {
		if(Implementation.mappingSupport) {
			return Mapping.getSourceAttribute(this, sourceAttribute, targetDomain, mappingDomain, mode);
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}

	public Object getMappedAttribute(EGeneric_attribute_mapping sourceAttribute, ASdaiModel targetDomain, ASdaiModel mappingDomain, int mode) throws SdaiException {
		if(Implementation.mappingSupport) {
			return Mapping.getMappedAttribute(this, sourceAttribute, targetDomain, mappingDomain, mode);
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}

	public int findEntityMappings(EEntity_definition sourceEntity, ASdaiModel targetDomain, ASdaiModel mappingDomain, AEntity_mapping mappings, int mode) throws SdaiException {
		if(Implementation.mappingSupport) {
			return Mapping.findEntityMappings(this, sourceEntity, targetDomain, mappingDomain,
											  mappings, mode);
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}

	public int findEntityMappings(ASdaiModel targetDomain, ASdaiModel mappingDomain, AEntity_mapping mappings, int mode) throws SdaiException {
		if(Implementation.mappingSupport) {
			return Mapping.findEntityMappings(this, targetDomain, mappingDomain, mappings, mode);
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}

	/** Finds most specific ARM mappings for the instance.
	 * This is ARM (mapping) operations support method.
	 * Most specific mapping can be viewed as means to define
	 * of which ARM type the AIM entity really is.
	 * @return it returns an aggregate of entity_mappings that represent
	 * most specific mappings. Usualy resulting aggregate contains only one member.
	 * But in some cases instance can have more than one most specific mapping and
	 * then all mappings are returned.
	 * @param mappingDomain is domain for mapping constraints.
	 * @param dataDomain is domain which defines where to search instances that satisfy mapping constraints.
	 * 					In the case it is null owning model of target instance will be used as data domain.
	 * @param baseMappings is an aggregate of mapings which define where to start looking for most specific mappings.
	 * The mappings returned will be mappings for subtypes of source enitity of base mappings.
	 * @param mode is mode for mapping operations. It can be one of:
	 * EEntity.NO_RESTRICTIONS
	 * EEntity.MOST_SPECIFC_ENTITY
	 * EEntity.MANDATORY_ATTRIBUTES_SET
	 * @throws SdaiException All variety of SdaiExceptions can be thrown. */
	public AEntity_mapping findMostSpecificMappings(ASdaiModel dataDomain, ASdaiModel mappingDomain,
	AEntity_mapping baseMappings, int mode) throws SdaiException {
		if(Implementation.mappingSupport) {
			return Mapping.findMostSpecificMappings(this, dataDomain, mappingDomain, baseMappings, mode);
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}

	public EMappedARMEntity buildMappedInstance(SdaiContext context, EEntity_definition mappedInstanceType)
	throws SdaiException {
		if(Implementation.mappingSupport) {
			return Mapping.buildMappedInstance(this, context, mappedInstanceType);
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}

	public EMappedARMEntity findLinkedMappedInstance(EEntity_definition mappedInstanceType)
	throws SdaiException {
		if(Implementation.mappingSupport) {
			return CMappedARMEntity.findLinkedMappedInstance(this, mappedInstanceType);
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}

// 	public void finalyzeMapping(EEntity_mapping usedMapping, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) throws SdaiException {
// 		Mapping.finalyzeMapping(this, usedMapping, dataDomain, mappingDomain, mode);
// 	}

	public AEntity findMappedUsers(EEntity_mapping source_type, AAttribute_mapping attribute,
		   ASdaiModel data_domain, ASdaiModel mapping_domain, AAttribute_mapping users,
			int mode) throws SdaiException {
		if(Implementation.mappingSupport) {
			return Mapping.findMappedUsers(this, source_type, attribute, data_domain,
										   mapping_domain, users, mode);
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}

	public boolean hasMappedAttribute(EEntity_mapping entityMapping, EAttribute attribute, ASdaiModel mappingDomain) throws SdaiException {
		if(Implementation.mappingSupport) {
			return Mapping.hasMappedAttribute(entityMapping, attribute, mappingDomain);
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}


/**
	Returns entity definition for a given entity and the express schema
	containing it.
	The method is used to set static field <code>definition</code> in
	compiler generated entity-classes.
*/
	static protected CEntity_definition initEntityDefinition(Class cl, SSuper ss) {
		CEntity_definition def;

		try {
			if (ss.model.schemaData.noOfEntityDataTypes < 0) {
				ss.model.startReadOnlyAccess();
			}
			def = ss.model.schemaData.findEntity(cl);
			if (def != null) {
				((CEntityDefinition)def).entityClass = cl;
//System.out.println("CEntity before BBBBBB  setEarlyBinding" + cl.getName());
				((CEntityDefinition)def).setEarlyBinding(ss.model);
//System.out.println("CEntity after AAAAAA setEarlyBinding" + cl.getName());
			}
		} catch (SdaiException e) {
			SdaiSession.printStackTraceToLogWriter(e);
			return null;
		}
		return def;
	}


/**
	Returns explicit attribute for a given entity and attribute index.
	The method is used to set static fields like <code>ax$</code>, where
	<code>x</code> is a nonnegative integer, in compiler generated entity-classes.
*/
	static protected CExplicit_attribute initExplicitAttribute(CEntity_definition ed, int attribute_index) {
		if (ed == null) {
			return null;
		} else {
			return ((CEntityDefinition)ed).attributes[attribute_index];
		}
	}


	static protected CDerived_attribute initDerivedAttribute(CEntity_definition ed, int attribute_index) {
		if (ed == null) {
			return null;
		} else {
			return ((CEntityDefinition)ed).attributesDerived[attribute_index];
		}
	}


	static protected CInverse_attribute initInverseAttribute(CEntity_definition ed, int attribute_index) {
		if (ed == null) {
			return null;
		} else {
			return ((CEntityDefinition)ed).attributesInverse[attribute_index];
		}
	}


	public void set(EExplicit_attribute attribute, Value val) throws SdaiException {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.active_transaction.mode != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.TR_NRW, session.active_transaction);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		if (attribute == null) {
			throw new SdaiException(SdaiException.AT_NDEF);
		}
		if (val == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		int attr_ind = -1;
		Object obj_val;
		int int_val;
		EAttribute saved_attr = attribute;
		if (attribute.testRedeclaring(null)) {
			attribute = getRedeclaredAttribute(attribute);
		}
		CEntityDefinition edef = (CEntityDefinition)getInstanceType();
		for (int i = 0; i < edef.attributes.length; i++) {
//System.out.println("   CEntity      Attribute: " + edef.attributes[i]);
			if (edef.attributes[i] == attribute && edef.attributeFields[i] != null) {
				attr_ind = i;
				break;
			}
		}
		if (attr_ind < 0) {
			throw new SdaiException(SdaiException.AT_NVLD, saved_attr);
		}
		CExplicit_attribute attr = (CExplicit_attribute)attribute;
		SelectType sel_type = take_select(attr);
		Field field = edef.attributeFields[attr_ind];
		SSuper ssuper = edef.fieldOwners[attr_ind].ssuper;
//		if (val.check((EData_type)attr.getDomain(null)) == null) {
		if (val.check_for_set((EData_type)attr.getDomain(null), owning_model.repository.session.sdai_context) == null) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		if (sel_type != null) {
			remove_inverse(ssuper, field);
			StaticFields staticFields = StaticFields.get();
			staticFields.current_attribute = attr;
			obj_val = val.getMixedValue(attr, this);
			int sel_number = val.sel_number;
			try {
				ssuper.setObject(this, field, obj_val);
				if (sel_number > 1 || (sel_number == 1 && sel_type.is_mixed > 0)) {
					Field sel_field = edef.attributeFieldSelects[attr_ind];
					ssuper.setInt(this, sel_field, sel_number);
				}
			} catch (Exception ex) {
				throw new SdaiException(SdaiException.SY_ERR, ex);
			}
			return;
		}
//System.out.println(" CEntity    val.tag = " + val.tag  +
//"     val.aux = " + val.aux);
		try {
			if (val.tag == PhFileReader.MISSING) {
				switch (val.aux) {
					case PhFileReader.INTEGER:
						ssuper.setInt(this, field, Integer.MIN_VALUE);
						break;
					case PhFileReader.BOOLEAN:
					case PhFileReader.LOGICAL:
						ssuper.setInt(this, field, 0);
						break;
					case PhFileReader.REAL:
						ssuper.setDouble(this, field, Double.NaN);
						break;
					case PhFileReader.ENTITY_REFERENCE:
						remove_inverse(ssuper, field);
						ssuper.setObject(this, field, null);
						break;
					default:
						ssuper.setObject(this, field, null);
						break;
				}
				return;
			}
			switch (val.aux) {
				case PhFileReader.INTEGER:
					int_val = val.getInteger(owning_model.repository.session);
					ssuper.setInt(this, field, int_val);
					break;
				case PhFileReader.BOOLEAN:
					int_val = val.getBoolean(owning_model.repository.session);
					ssuper.setInt(this, field, int_val);
					break;
				case PhFileReader.LOGICAL:
					int_val = val.getLogical(owning_model.repository.session);
					ssuper.setInt(this, field, int_val);
					break;
				case PhFileReader.REAL:
					double doub_val = val.getDouble(owning_model.repository.session);
					ssuper.setDouble(this, field, doub_val);
					break;
				case PhFileReader.STRING:
					ssuper.setObject(this, field, val.string);
					break;
				case PhFileReader.ENUM:
					EEnumeration_type enum_tp;
					if (((DataType)val.v_type).express_type == DataType.DEFINED_TYPE) {
						enum_tp = (EEnumeration_type)((CDefined_type)val.v_type).getDomain(null);
					} else {
						enum_tp = (EEnumeration_type)val.v_type;
					}

					SdaiContext context;
//					int etp = ((DataType)val.v_type).express_type;
					int etp = ((DataType)enum_tp).express_type;
					if (etp == DataType.EXTENSIBLE_ENUM || etp == DataType.EXTENDED_EXTENSIBLE_ENUM) {
						context = owning_model.repository.session.sdai_context;
					} else {
						context = null;
					}
//					int_val = val.getEnumeration((EEnumeration_type)val.v_type, attr, context, owning_model.repository.session);
					int_val = val.getEnumeration(enum_tp, attr, context, owning_model.repository.session);
					ssuper.setInt(this, field, int_val);
					break;
				case PhFileReader.BINARY:
					ssuper.setObject(this, field, val.reference);
					break;
				case PhFileReader.ENTITY_REFERENCE:
					remove_inverse(ssuper, field);
					obj_val = val.getInstance(this, null);
					ssuper.setObject(this, field, obj_val);
					break;
				case Value.A_INTEGER:
					obj_val = val.getIntegerAggregate(attr, this);
					ssuper.setObject(this, field, obj_val);
					break;
				case Value.A_DOUBLE:
					obj_val = val.getDoubleAggregate(attr, this);
					ssuper.setObject(this, field, obj_val);
					break;
				case Value.A_STRING:
					obj_val = val.getStringAggregate(attr, this);
					ssuper.setObject(this, field, obj_val);
					break;
				case Value.A_LOGICAL:
					obj_val = val.getLogicalAggregate(attr, this);
					ssuper.setObject(this, field, obj_val);
					break;
				case Value.A_BOOLEAN:
					obj_val = val.getBooleanAggregate(attr, this);
					ssuper.setObject(this, field, obj_val);
					break;
				case Value.A_ENUMERATION:
					obj_val = val.getEnumerationAggregate(attr, this);
					ssuper.setObject(this, field, obj_val);
					break;
				case Value.A_BINARY:
					obj_val = val.getBinaryAggregate(attr, this);
					ssuper.setObject(this, field, obj_val);
					break;
				case Value.AENTITY:
					obj_val = val.getInstanceAggregate(attr, this);
					ssuper.setObject(this, field, obj_val);
					break;
				case Value.AMIXED:
					obj_val = val.getMixedAggregate(attr, this);
					ssuper.setObject(this, field, obj_val);
					break;
				case Value.AA_INTEGER:
					obj_val = val.getInteger2Aggregate(attr, this);
					ssuper.setObject(this, field, obj_val);
					break;
				case Value.AA_DOUBLE:
					obj_val = val.getDouble2Aggregate(attr, this);
					ssuper.setObject(this, field, obj_val);
					break;
				case Value.AA_STRING:
					obj_val = val.getString2Aggregate(attr, this);
					ssuper.setObject(this, field, obj_val);
					break;
				case Value.AA_LOGICAL:
					obj_val = val.getLogical2Aggregate(attr, this);
					ssuper.setObject(this, field, obj_val);
					break;
				case Value.AA_BOOLEAN:
					obj_val = val.getBoolean2Aggregate(attr, this);
					ssuper.setObject(this, field, obj_val);
					break;
				case Value.AA_ENUMERATION:
					obj_val = val.getEnumeration2Aggregate(attr, this);
					ssuper.setObject(this, field, obj_val);
					break;
				case Value.AAA_DOUBLE:
					obj_val = val.getDouble3Aggregate(attr, this);
					ssuper.setObject(this, field, obj_val);
					break;
				default:
// print error message
					break;
			}
		} catch (Exception ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
		modified();
	}


/**
*/
	private void remove_inverse(SSuper ssuper, Field field) throws SdaiException {
		Object old_value;
		try {
			old_value = ssuper.getObject(this, field);
		} catch (Exception ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
		if (old_value instanceof CEntity) {
			CEntity old_ref = (CEntity)old_value;
			if (!old_ref.owning_model.optimized) {
				removeFromInverseList(old_ref);
			}
		} else if (old_value instanceof CAggregate) {
			((CAggregate)old_value).unsetAllByRef(this);
		}
	}


	public Value get(EAttribute attribute) throws SdaiException {
		return get(attribute, null);
	}


	Value get(EAttribute attribute, SdaiContext context) throws SdaiException {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if (attribute == null) {
			throw new SdaiException(SdaiException.AT_NDEF);
		}
		int attr_ind = -1;
		Object obj_val;
		int int_val;
		EAttribute saved_attr = attribute;
		if (((AttributeDefinition)attribute).attr_tp == AttributeDefinition.EXPLICIT &&
				((CExplicit_attribute)attribute).testRedeclaring(null)) {
			EExplicit_attribute expl_attr = (EExplicit_attribute)attribute;
			while (expl_attr.testRedeclaring(null)) {
				expl_attr = expl_attr.getRedeclaring(null);
			}
			attribute = expl_attr;
		}

		CEntityDefinition edef = (CEntityDefinition)getInstanceType();
		boolean expl_redecl = false;
		for (int i = 0; i < edef.attributes.length; i++) {
//if (Value.prnt) {String attr_checked = edef.attributes[i].getParent_entity(null).getName(null);
//System.out.println("CEntity      attribute: " + attribute.getName(null) +
//"   edef.attributes[i]: " + edef.attributes[i].getName(null) +
//"   attr_owner: " + attr_owner + "   attr_checked: " + attr_checked);
//}
			if (edef.attributes[i] == attribute) {
				if (edef.attributeFields[i] != null) {
					attr_ind = i;
					break;
				}
				expl_redecl = true;
			}
		}
		if (attr_ind < 0 && expl_redecl) {
			EAttribute ex_der_attr = edef.getIfDerived((CExplicit_attribute)attribute);
			if (ex_der_attr != null) {
				attribute = ex_der_attr;
			}
		}
		Value v;
		if (attr_ind < 0) {
			if (edef.attributesDerived != null) {
				for (int i = 0; i < edef.attributesDerived.length; i++) {
					if (edef.attributesDerived[i] == attribute) {
/*System.out.println("CEntity      attribute: " + attribute.getName(null) +
"   edef.attributes[i]: " + edef.attributes[i].getName(null) +
"   attr_owner: " + attribute.getParent_entity(null).getName(null) + "   edef: " + edef.getName(null));*/
						v = get_derivedValue(edef, i, context);
						if (v == null/* || v.tag == PhFileReader.MISSING*/) {
							printWarningToLogoAttribute(owning_model.repository.session, saved_attr, AdditionalMessages.AT_VANX);
							throw new SdaiException(SdaiException.VA_NSET);
						}
						v.aux = 2;
						return v;
					}
				}
			}
			if (((AttributeDefinition)attribute).attr_tp == AttributeDefinition.DERIVED) {
				v = get_derivedValue_redecl(edef, (CDerived_attribute)attribute, context);
				if (v == null) {
					printWarningToLogoAttribute(owning_model.repository.session, saved_attr, AdditionalMessages.AT_VANX);
					throw new SdaiException(SdaiException.VA_NSET);
				}
				v.aux = 2;
				return v;
			}
			if (((AttributeDefinition)attribute).attr_tp == AttributeDefinition.INVERSE) {
				EInverse_attribute inv_attrib = (EInverse_attribute)attribute;
				AEntity inv_val = get_inverse(inv_attrib, null);
				Value v_inv = new Value();
				if (inv_attrib.testMin_cardinality(null)) {
					v_inv.v_type = v_inv.d_type = inv_val.myType;
					v_inv.tag = PhFileReader.EMBEDDED_LIST;
					v_inv.reference = inv_val;
					v_inv.aux = -1;
					v_inv.setAggrElements(inv_val);
				} else {
					EEntity_definition domain = inv_attrib.getDomain(null);
//					v_inv.v_type = v_inv.d_type = domain;
					v_inv.d_type = domain;
					if (inv_val.myLength > 0) {
//						v_inv.v_type = domain;
						v_inv.tag = PhFileReader.ENTITY_REFERENCE;
						v_inv.reference = inv_val.getByIndexEntity(1);
						v_inv.v_type = ((CEntity)v_inv.reference).getInstanceType();
					} else {
						v_inv.tag = PhFileReader.MISSING;
					}
				}
				v_inv.aux = 3;
				return v_inv;
			}
//			throw new SdaiException(SdaiException.AT_NVLD, saved_attr);
			return null;
		}
		CExplicit_attribute attr = (CExplicit_attribute)attribute;
		SelectType sel_type = take_select(attr);
		Field field = edef.attributeFields[attr_ind];
		SSuper ssuper;
		if (owning_model.described_schema == null) {
			ssuper = edef.fieldOwners[attr_ind].ssuper;
		} else {
			ssuper = edef.ssuper;
		}
		Value val = new Value();
		if (sel_type != null) {
			int sel_number;
			try {
				obj_val = ssuper.getObject(this, field);
				if (sel_type.is_mixed > 0) {
					if (obj_val instanceof CEntity) {
						sel_number = 1;
					} else {
						Field sel_field = edef.attributeFieldSelects[attr_ind];
						sel_number = ssuper.getInt(this, sel_field);
						if (sel_number == 0) {
							sel_number = 1;
						}
					}
				} else {
					sel_number = 1;
				}
			} catch (Exception ex) {
				throw new SdaiException(SdaiException.SY_ERR, ex);
			}
			val.d_type = (EData_type)attr.getDomain(null);
			val.setMixed(obj_val, sel_type, sel_number);
			val.aux = 1;
			return val;
		}

		if (!val.getValueType(attr, this)) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
//System.out.println("CEntity   Before switch  ==========  attribute: " + attribute.getName(null) +
//"   val.aux: " + val.aux + "  inst: #" + instance_identifier +
//"   instance: " + this);
		try {
			switch (val.aux) {
				case PhFileReader.INTEGER:
					int_val = ssuper.getInt(this, field);
					if (int_val != Integer.MIN_VALUE) {
						val.tag = PhFileReader.INTEGER;
						val.integer = int_val;
					} else {
						val.tag = PhFileReader.MISSING;
					}
					break;
				case PhFileReader.BOOLEAN:
					int_val = ssuper.getInt(this, field);
					if (int_val == 0) {
						val.tag = PhFileReader.MISSING;
						break;
					}
					val.tag = PhFileReader.BOOLEAN;
					if (int_val == 2) {
						val.integer = 1;
					} else {
						val.integer = 0;
					}
					val.length = 3;
					break;
				case PhFileReader.LOGICAL:
					int_val = ssuper.getInt(this, field);
					if (int_val == 0) {
						val.tag = PhFileReader.MISSING;
						break;
					}
					val.tag = PhFileReader.LOGICAL;
					if (int_val == 2) {
						val.integer = 1;
					} else if (int_val == 1) {
						val.integer = 0;
					} else {
						val.integer = 2;
					}
					break;
				case PhFileReader.REAL:
					double doub_val = ssuper.getDouble(this, field);
					if (!Double.isNaN(doub_val)) {
						val.tag = PhFileReader.REAL;
						val.real = doub_val;
					} else {
						val.tag = PhFileReader.MISSING;
					}
					break;
				case PhFileReader.STRING:
					obj_val = ssuper.getObject(this, field);
					if (obj_val != null) {
						val.tag = PhFileReader.STRING;
						val.string = (String)obj_val;
					} else {
						val.tag = PhFileReader.MISSING;
					}
					break;
				case PhFileReader.ENUM:
					int_val = ssuper.getInt(this, field);
					val.setEnumeration(int_val, attr);
					break;
				case PhFileReader.BINARY:
					obj_val = ssuper.getObject(this, field);
					if (obj_val != null) {
						val.tag = PhFileReader.BINARY;
						val.reference = obj_val;
					} else {
						val.tag = PhFileReader.MISSING;
					}
					break;
				case PhFileReader.ENTITY_REFERENCE:
					obj_val = ssuper.getObject(this, field);
					if (obj_val != null) {
						boolean bool = (obj_val instanceof CEntity);
//						if (!(obj_val instanceof CEntity || obj_val instanceof Connector)) {
						if (!(bool || obj_val instanceof SdaiModel.Connector)) {
							printWarningToLogoAttribute(owning_model.repository.session, saved_attr, AdditionalMessages.AT_VAWR);
							throw new SdaiException(SdaiException.SY_ERR);
						}
						val.tag = PhFileReader.ENTITY_REFERENCE;
						val.reference = obj_val;
						if (bool) {
							val.v_type = ((CEntity)obj_val).getInstanceType();
						}
					} else {
						val.tag = PhFileReader.MISSING;
					}
					break;
				case Value.A_INTEGER:
					obj_val = ssuper.getObject(this, field);
					if (obj_val != null) {
						if (!(obj_val instanceof A_integer)) {
							printWarningToLogoAttribute(owning_model.repository.session, saved_attr, AdditionalMessages.AT_VAWR);
							throw new SdaiException(SdaiException.SY_ERR);
						}
						val.tag = PhFileReader.EMBEDDED_LIST;
						val.reference = obj_val;
						val.agg_owner = this;
						val.aux = -1;
						val.setAggrInteger((A_integer)obj_val);
						val.aux = 0;
					} else {
						val.tag = PhFileReader.MISSING;
					}
					break;
				case Value.A_DOUBLE:
					obj_val = ssuper.getObject(this, field);
					if (obj_val != null) {
						if (!(obj_val instanceof A_double)) {
							printWarningToLogoAttribute(owning_model.repository.session, saved_attr, AdditionalMessages.AT_VAWR);
							throw new SdaiException(SdaiException.SY_ERR);
						}
						A_double aggr_double = (A_double)obj_val;
						val.tag = PhFileReader.EMBEDDED_LIST;
						val.reference = aggr_double;
						val.agg_owner = this;
						val.aux = -1;
//						if (aggr_double.myType != null && aggr_double.myType.check_aggregation_double()) {
						if (aggr_double.myType != null && !aggr_double.myType.check_aggregation_double()) {
							val.setAggrDouble(aggr_double);
						} else {
							if ((!owning_model.repository.session.a_double3_overflow &&
									!SdaiSession.getSession().a_double3_overflow) || aggr_double instanceof A_double3) {
								val.setAggrDouble((A_double3)aggr_double);
							} else {
								val.setAggrDouble(aggr_double);
							}
						}
						val.aux = 0;
					} else {
						val.tag = PhFileReader.MISSING;
					}
					break;
				case Value.A_STRING:
					obj_val = ssuper.getObject(this, field);
					if (obj_val != null) {
						if (!(obj_val instanceof A_string)) {
							printWarningToLogoAttribute(owning_model.repository.session, saved_attr, AdditionalMessages.AT_VAWR);
							throw new SdaiException(SdaiException.SY_ERR);
						}
						val.tag = PhFileReader.EMBEDDED_LIST;
						val.reference = obj_val;
						val.agg_owner = this;
						val.aux = -1;
						val.setAggrString((A_string)obj_val);
						val.aux = 0;
					} else {
						val.tag = PhFileReader.MISSING;
					}
					break;
				case Value.A_LOGICAL:
					obj_val = ssuper.getObject(this, field);
					if (obj_val != null) {
						if (!(obj_val instanceof A_enumeration)) {
							printWarningToLogoAttribute(owning_model.repository.session, saved_attr, AdditionalMessages.AT_VAWR);
							throw new SdaiException(SdaiException.SY_ERR);
						}
						val.tag = PhFileReader.EMBEDDED_LIST;
						val.reference = obj_val;
						val.agg_owner = this;
						val.aux = -1;
						val.setAggrLogical((A_enumeration)obj_val);
						val.aux = 0;
					} else {
						val.tag = PhFileReader.MISSING;
					}
					break;
				case Value.A_BOOLEAN:
					obj_val = ssuper.getObject(this, field);
					if (obj_val != null) {
						if (!(obj_val instanceof A_boolean)) {
							printWarningToLogoAttribute(owning_model.repository.session, saved_attr, AdditionalMessages.AT_VAWR);
							throw new SdaiException(SdaiException.SY_ERR);
						}
						val.tag = PhFileReader.EMBEDDED_LIST;
						val.reference = obj_val;
						val.agg_owner = this;
						val.aux = -1;
						val.setAggrBoolean((A_boolean)obj_val);
						val.aux = 0;
					} else {
						val.tag = PhFileReader.MISSING;
					}
					break;
				case Value.A_ENUMERATION:
					obj_val = ssuper.getObject(this, field);
					if (obj_val != null) {
						if (!(obj_val instanceof A_enumeration)) {
							printWarningToLogoAttribute(owning_model.repository.session, saved_attr, AdditionalMessages.AT_VAWR);
							throw new SdaiException(SdaiException.SY_ERR);
						}
						A_string elements = (A_string)val.reference;
						val.tag = PhFileReader.EMBEDDED_LIST;
						val.reference = obj_val;
						val.agg_owner = this;
						val.aux = -1;
						val.setAggrEnumeration((A_enumeration)obj_val, elements);
						val.aux = 0;
					} else {
						val.tag = PhFileReader.MISSING;
					}
					break;
				case Value.A_BINARY:
					obj_val = ssuper.getObject(this, field);
					if (obj_val != null) {
						if (!(obj_val instanceof A_binary)) {
							printWarningToLogoAttribute(owning_model.repository.session, saved_attr, AdditionalMessages.AT_VAWR);
							throw new SdaiException(SdaiException.SY_ERR);
						}
						val.tag = PhFileReader.EMBEDDED_LIST;
						val.reference = obj_val;
						val.agg_owner = this;
						val.aux = -1;
						val.setAggrBinary((A_binary)obj_val);
						val.aux = 0;
					} else {
						val.tag = PhFileReader.MISSING;
					}
					break;
				case Value.AENTITY:
					obj_val = ssuper.getObject(this, field);
					if (obj_val != null) {
						if (!(obj_val instanceof CAggregate)) {
							printWarningToLogoAttribute(owning_model.repository.session, saved_attr, AdditionalMessages.AT_VAWR);
							throw new SdaiException(SdaiException.SY_ERR);
						}
						val.tag = PhFileReader.EMBEDDED_LIST;
						val.reference = obj_val;
						val.agg_owner = this;
						val.aux = -1;
						val.setAggrElements((CAggregate)obj_val);
						val.aux = 0;
					} else {
						val.tag = PhFileReader.MISSING;
					}
					break;
				case Value.AMIXED:
					obj_val = ssuper.getObject(this, field);
					if (obj_val != null) {
						if (!(obj_val instanceof CAggregate)) {
							printWarningToLogoAttribute(owning_model.repository.session, saved_attr, AdditionalMessages.AT_VAWR);
							throw new SdaiException(SdaiException.SY_ERR);
						}
						val.tag = PhFileReader.EMBEDDED_LIST;
						val.reference = obj_val;
						val.agg_owner = this;
						val.aux = -1;
						val.setMixedAggrElements((CAggregate)obj_val);
						val.aux = 0;
					} else {
						val.tag = PhFileReader.MISSING;
					}
					break;
				case Value.AA_INTEGER:
					obj_val = ssuper.getObject(this, field);
					if (obj_val != null) {
						if (!(obj_val instanceof Aa_integer)) {
							printWarningToLogoAttribute(owning_model.repository.session, saved_attr, AdditionalMessages.AT_VAWR);
							throw new SdaiException(SdaiException.SY_ERR);
						}
						val.tag = PhFileReader.EMBEDDED_LIST;
						val.reference = obj_val;
						val.agg_owner = this;
						val.aux = -1;
						val.setAggrIntegerNested((Aa_integer)obj_val);
						val.aux = 0;
					} else {
						val.tag = PhFileReader.MISSING;
					}
					break;
				case Value.AA_DOUBLE:
					obj_val = ssuper.getObject(this, field);
					if (obj_val != null) {
						if (!(obj_val instanceof Aa_double)) {
							printWarningToLogoAttribute(owning_model.repository.session, saved_attr, AdditionalMessages.AT_VAWR);
							throw new SdaiException(SdaiException.SY_ERR);
						}
						val.tag = PhFileReader.EMBEDDED_LIST;
						val.reference = obj_val;
						val.agg_owner = this;
						val.aux = -1;
						val.setAggrDoubleNested((Aa_double)obj_val);
						val.aux = 0;
					} else {
						val.tag = PhFileReader.MISSING;
					}
					break;
				case Value.AA_STRING:
					obj_val = ssuper.getObject(this, field);
					if (obj_val != null) {
						if (!(obj_val instanceof Aa_string)) {
							printWarningToLogoAttribute(owning_model.repository.session, saved_attr, AdditionalMessages.AT_VAWR);
							throw new SdaiException(SdaiException.SY_ERR);
						}
						val.tag = PhFileReader.EMBEDDED_LIST;
						val.reference = obj_val;
						val.agg_owner = this;
						val.aux = -1;
						val.setAggrStringNested((Aa_string)obj_val);
						val.aux = 0;
					} else {
						val.tag = PhFileReader.MISSING;
					}
					break;
				case Value.AA_LOGICAL:
					obj_val = ssuper.getObject(this, field);
					if (obj_val != null) {
						if (!(obj_val instanceof Aa_enumeration)) {
							printWarningToLogoAttribute(owning_model.repository.session, saved_attr, AdditionalMessages.AT_VAWR);
							throw new SdaiException(SdaiException.SY_ERR);
						}
						val.tag = PhFileReader.EMBEDDED_LIST;
						val.reference = obj_val;
						val.agg_owner = this;
						val.aux = -1;
						val.setAggrLogicalNested((Aa_enumeration)obj_val);
						val.aux = 0;
					} else {
						val.tag = PhFileReader.MISSING;
					}
					break;
				case Value.AA_BOOLEAN:
					obj_val = ssuper.getObject(this, field);
					if (obj_val != null) {
						if (!(obj_val instanceof Aa_boolean)) {
							printWarningToLogoAttribute(owning_model.repository.session, saved_attr, AdditionalMessages.AT_VAWR);
							throw new SdaiException(SdaiException.SY_ERR);
						}
						val.tag = PhFileReader.EMBEDDED_LIST;
						val.reference = obj_val;
						val.agg_owner = this;
						val.aux = -1;
						val.setAggrBooleanNested((Aa_boolean)obj_val);
						val.aux = 0;
					} else {
						val.tag = PhFileReader.MISSING;
					}
					break;
				case Value.AA_ENUMERATION:
					obj_val = ssuper.getObject(this, field);
					if (obj_val != null) {
						if (!(obj_val instanceof Aa_enumeration)) {
							printWarningToLogoAttribute(owning_model.repository.session, saved_attr, AdditionalMessages.AT_VAWR);
							throw new SdaiException(SdaiException.SY_ERR);
						}
						val.tag = PhFileReader.EMBEDDED_LIST;
						val.reference = obj_val;
						val.agg_owner = this;
						val.aux = -1;
						val.setAggrEnumerationNested((Aa_enumeration)obj_val);
						val.aux = 0;
					} else {
						val.tag = PhFileReader.MISSING;
					}
					break;
				case Value.AAA_DOUBLE:
					obj_val = ssuper.getObject(this, field);
					if (obj_val != null) {
						if (!(obj_val instanceof Aaa_double)) {
							printWarningToLogoAttribute(owning_model.repository.session, saved_attr, AdditionalMessages.AT_VAWR);
							throw new SdaiException(SdaiException.SY_ERR);
						}
						val.tag = PhFileReader.EMBEDDED_LIST;
						val.reference = obj_val;
						val.agg_owner = this;
						val.aux = -1;
						val.setAggrDoubleNested2((Aaa_double)obj_val);
						val.aux = 0;
					} else {
						val.tag = PhFileReader.MISSING;
					}
					break;
				default:
// print error message
					break;
			}
		} catch (Exception ex) {
//System.out.println("CEntity   Before switch  ==========  attribute: " + attribute.getName(null) +
//"   val.aux: " + val.aux + "  inst: #" + instance_identifier +
//"   instance: " + this);
			printWarningToLogoAttribute(owning_model.repository.session, attribute, AdditionalMessages.AT_NEXS);
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
		val.aux = 1;
		return val;
	}


/**
	A method for debugging purposes.
*/
	private void print_value(Value val) {
		Value value_next;
		String str;
		switch (val.tag) {
			case PhFileReader.MISSING:
				System.out.print("$  ");
				break;
			case PhFileReader.REDEFINE:
				System.out.print("*  ");
				break;
			case PhFileReader.INTEGER:
				System.out.print(val.integer + "  ");
				break;
			case PhFileReader.REAL:
				System.out.print(val.real + "  ");
				break;
			case PhFileReader.LOGICAL:
				if (val.integer == 0) {
					System.out.print(".F.  ");
				} else if (val.integer == 1) {
					System.out.print(".T.  ");
				} else {
					System.out.print(".U.  ");
				}
				break;
			case PhFileReader.ENUM:
				System.out.print("." + val.string + ".  ");
				break;
			case PhFileReader.STRING:
				System.out.print("'" + val.string + "'  ");
				break;
			case PhFileReader.BINARY:
				System.out.print(" BINARY ");
				break;
			case PhFileReader.TYPED_PARAMETER:
				System.out.print(val.string + "(");
				value_next = val.nested_values[0];
				print_value(value_next);
				System.out.print(")  ");
				break;
			case PhFileReader.ENTITY_REFERENCE:
				Object ref = val.reference;
				String ref_class = ref.getClass().getName();
				System.out.print(ref_class + "  ");
//				System.out.print("REF  ");
				break;
			case PhFileReader.ENTITY_REFERENCE_SPECIAL:
				System.out.print("REF FORWARD  ");
				break;
			case PhFileReader.EMBEDDED_LIST:
				System.out.print("(");
				for (int i = 0; i < val.length; i++) {
					value_next = val.nested_values[i];
					print_value(value_next);
				}
				System.out.print(")  ");
				break;
			}
	}


	private Value get_derivedValue(CEntityDefinition edef, int index, SdaiContext context) throws SdaiException {
		StaticFields staticFields = StaticFields.get();
		if (staticFields.argsValue == null) {
			staticFields.argsValue = new Object [2];
			staticFields.argsValue[0] = null;
		}
		if (context != null) {
			staticFields.argsValue[1] = context;
		} else {
			staticFields.argsValue[1] = new SdaiContext(this);
		}
		Method meth;
		if (edef.attributesDerivedMethodValue[index] instanceof Method) {
			meth = (Method)edef.attributesDerivedMethodValue[index];
		} else {
			if (staticFields.paramValue == null) {
				staticFields.paramValue = new Class[2];
				staticFields.paramValue[1] = SdaiContext.class;
			}
			staticFields.paramValue[0] = (Class)edef.attributesDerivedMethodValue[index];
			Class cl = edef.attributesDerivedClass[index];
			try {
				meth = cl.getDeclaredMethod(edef.attributesDerivedMethodName[index], staticFields.paramValue);
			} catch (java.lang.NoSuchMethodException ex) {
//System.out.println("CEntity !!!!!!! this: " + this.getClass().getName() +
//"   edef: " + edef.getName(null) +
//"   method name: " + edef.attributesDerivedMethodName[index] +
//"    index: " + index +
//"    staticFields.paramValue [0]: " + staticFields.paramValue[0].getName() +
//"    cl: " + cl.getName());
				throw new SdaiException(SdaiException.SY_ERR, ex);
			}
			edef.attributesDerivedMethodValue[index] = meth;
		}
		Object res;
		try {
			res = meth.invoke(this, staticFields.argsValue);
		} catch (Exception ex) {
//ex.printStackTrace();
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
		return (Value) res;
	}


	private Value get_derivedValue_redecl(CEntityDefinition edef, CDerived_attribute der_attr, SdaiContext context) throws SdaiException {
		if (!der_attr.testRedeclaring(null)) {
			throw new SdaiException(SdaiException.SY_ERR, der_attr);
		}
		SchemaData sch_data = edef.owning_model.schemaData;
		int index = sch_data.findEntityExtentIndex(edef);
		if (index < 0) {
			throw new SdaiException(SdaiException.SY_ERR, "Entity not found in the table");
		}
		Class cl = sch_data.getEntityClassByIndex(index);
		EEntity redecl = der_attr.getRedeclaring(null);
		if (((AttributeDefinition)redecl).attr_tp == AttributeDefinition.DERIVED) {
			throw new SdaiException(SdaiException.FN_NAVL, redecl);
		}
		EExplicit_attribute red = (EExplicit_attribute)redecl;
		String red_name = red.getName(null);
		while (red != null) {
			if (red.testRedeclaring(null)) {
				EExplicit_attribute red_next = red.getRedeclaring(null);
				String red_name_next = red_next.getName(null);
				if (red_name_next.equals(red_name)) {
					red = red_next;
					red_name = red_name_next;
				} else {
					redecl = red;
					break;
				}
			} else {
				redecl = red;
				break;
			}
		}
		CEntity_definition super_type = (CEntity_definition)((CExplicit_attribute)redecl).getParent_entity(null);
		SchemaData type_sch_data = super_type.owning_model.schemaData;
		int indx = type_sch_data.findEntityExtentIndex(super_type);
		if (indx < 0) {
			throw new SdaiException(SdaiException.SY_ERR, "Entity not found in the table");
		}
		Class true_if_cl = type_sch_data.getEntityInterfaceByIndex(indx);
		String m_name = CEntityDefinition.GET_METHOD_PREFIX + normalise(der_attr.getName(null));

		StaticFields staticFields = StaticFields.get();
		if (staticFields.argsValue == null) {
			staticFields.argsValue = new Object [2];
			staticFields.argsValue[0] = null;
		}
		if (context != null) {
			staticFields.argsValue[1] = context;
		} else {
			staticFields.argsValue[1] = new SdaiContext(this);
		}
		Method meth;
		if (staticFields.paramValue == null) {
			staticFields.paramValue = new Class[2];
			staticFields.paramValue[1] = SdaiContext.class;
		}
		staticFields.paramValue[0] = true_if_cl;
		try {
			meth = cl.getDeclaredMethod(m_name, staticFields.paramValue);
		} catch (java.lang.NoSuchMethodException ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
		Object res;
		try {
			res = meth.invoke(this, staticFields.argsValue);
		} catch (Exception ex) {
//ex.printStackTrace();
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
		return (Value) res;
	}


	void makeUsedinExpress(EAttribute role, Value result, boolean all_refs, SdaiContext context) throws SdaiException {
		Object o = inverseList;
		CEntity inst;
		CEntity_definition entityDef;
		result.length = 0;
		while (o != null) {
			if (o instanceof Inverse) {
				Inverse inv = (Inverse) o;
				inst = (CEntity)inv.value;
				if (all_refs) {
					entityDef = (CEntity_definition)inst.getInstanceType();
					result.addNewMember(inst, entityDef);
				//} else if (inst.instance_position >= 0) {
                } else if ((inst.instance_position & POS_MASK) != POS_MASK) {   //--VV--
					makeUsedinExpress(inst, role, result, context);
					//inst.instance_position = -1;
                    inst.instance_position = (inst.instance_position & FLG_MASK) | POS_MASK;    //--VV--
				}
				o = inv.next;
			} else {
				inst = (CEntity)o;
				if (all_refs) {
					entityDef = (CEntity_definition)inst.getInstanceType();
					result.addNewMember(inst, entityDef);
				//} else if (inst.instance_position >= 0) {
                } else if ((inst.instance_position & POS_MASK) != POS_MASK) {   //--VV--
					makeUsedinExpress(inst, role, result, context);
				}
				o = null;
			}
		}
		if (all_refs) {
			return;
		}
		o = inverseList;
		while (o != null) {
			if (o instanceof Inverse) {
				Inverse inv = (Inverse) o;
				//((CEntity)inv.value).instance_position = 0;
                ((CEntity)inv.value).instance_position = ((CEntity)inv.value).instance_position & FLG_MASK; //--VV--
				o = inv.next;
			} else {
				o = null;
			}
		}
	}


	private void makeUsedinExpress(CEntity inst, EAttribute role, Value result, SdaiContext context) throws SdaiException {
		CEntityDefinition entityDef = (CEntityDefinition)inst.getInstanceType();
		if (!checkIfRoleBelongs(entityDef, role)) {
			return;
		}
		CExplicit_attribute [] attributes = entityDef.attributes;
		Object o;

		for (int i = 0; i < attributes.length; i++) {
//			if (attributes[i] == role) {
			if (checkIfRedefined(attributes[i], role)) {
				if (entityDef.fieldOwners[i] == null) {
					o = takeValueOfRedeclared(inst, entityDef, attributes[i], context);
				} else {
					try {
						SSuper ssuper;
						if (owning_model.described_schema == null) {
							ssuper = entityDef.fieldOwners[i].ssuper;
						} else {
							ssuper = entityDef.ssuper;
						}
						o = ssuper.getObject(inst, entityDef.attributeFields[i]);
					} catch (java.lang.IllegalAccessException ex) {
						throw new SdaiException(SdaiException.SY_ERR);
					}
				}
				if (o == this) {
					result.addNewMember(inst, (CEntity_definition)entityDef);
				} else if (o instanceof CAggregate) {
					((CAggregate)o).usedin(this, inst, (CEntity_definition)entityDef, result);
				}
			}
		}
		return;
	}


	private boolean checkIfRoleBelongs(CEntityDefinition entityDef, EAttribute role) throws SdaiException {
		CEntityDefinition role_part_def = (CEntityDefinition)role.getParent_entity(null);
		for (int i = 0; i < entityDef.noOfPartialEntityTypes; i++) {
			CEntityDefinition partial_edef = entityDef.partialEntityTypes[i];
			if (partial_edef == role_part_def) {
				return true;
			}
		}
		return false;
	}


	private boolean checkIfRedefined(CExplicit_attribute attr, EAttribute role) throws SdaiException {
		if (attr == role) {
			return true;
		}
		if (((AttributeDefinition)role).attr_tp == AttributeDefinition.EXPLICIT) {
			EExplicit_attribute e_attr = (EExplicit_attribute)role;
			while (e_attr.testRedeclaring(null)) {
				e_attr = e_attr.getRedeclaring(null);
				if (attr == e_attr) {
					return true;
				}
			}
		}
		return false;
	}


	private Object takeValueOfRedeclared(CEntity inst, CEntityDefinition entityDef, CExplicit_attribute attr,
			SdaiContext context) throws SdaiException {
		EAttribute attribute = entityDef.getIfDerived(attr);
		if (attribute == null) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (entityDef.attributesDerived != null) {
			for (int i = 0; i < entityDef.attributesDerived.length; i++) {
				if (entityDef.attributesDerived[i] == attribute) {
					Value v = inst.get_derivedValue(entityDef, i, context);
					if (v == null) {
						throw new SdaiException(SdaiException.VA_NSET);
					}
					if (v.tag == PhFileReader.ENTITY_REFERENCE || v.tag == PhFileReader.EMBEDDED_LIST) {
						return v.reference;
					} else {
						return null;
					}
				}
			}
		}
		throw new SdaiException(SdaiException.SY_ERR);
	}


	void get_roles(CEntityDefinition edef, CEntity inst, AAttribute result) throws SdaiException {
		Object value;
		for (int i = 0; i < edef.attributes.length; i++) {
			AttributeDefinition attr = (AttributeDefinition)edef.attributes[i];
			if (attr.selected_as_role) {
				continue;
			}
			Field field = edef.attributeFields[i];
			SSuper ssuper;
			if (owning_model.described_schema == null) {
				ssuper = edef.fieldOwners[i].ssuper;
			} else {
				ssuper = edef.ssuper;
			}
			try {
				value = ssuper.getObject(this, field);
			} catch (Exception ex) {
				throw new SdaiException(SdaiException.SY_ERR, ex);
			}
			if (value == inst) {
				((AEntity)result).addAtTheEnd(attr, null);
				attr.selected_as_role = true;
			} else if (value instanceof CAggregate && ((CAggregate)value).is_referencing(inst)) {
				((AEntity)result).addAtTheEnd(attr, null);
				attr.selected_as_role = true;
			}
		}
	}





	void rolesOfExpress(Value result) throws SdaiException {
		Object o = inverseList;
		CEntity inst;
		Inverse inv;
		StaticFields staticFields = StaticFields.get();
		while (o != null) {
			if (o instanceof Inverse) {
				inv = (Inverse) o;
				inst = (CEntity)inv.value;
				//if (inst.instance_position >= 0) {
                if ((inst.instance_position & POS_MASK) != POS_MASK) {  //--VV--
					inst.analyse_roles(staticFields, this, result);
					//inst.instance_position = -1;
                    inst.instance_position = (inst.instance_position & FLG_MASK) | POS_MASK;    //--VV--
				}
				o = inv.next;
			} else {
				inst = (CEntity)o;
				//if (inst.instance_position >= 0) {
                if ((inst.instance_position & POS_MASK) != POS_MASK) {  //--VV--
					inst.analyse_roles(staticFields, this, result);
				}
				o = null;
			}
		}
		o = inverseList;
		while (o != null) {
			if (o instanceof Inverse) {
				inv = (Inverse) o;
				//((CEntity)inv.value).instance_position = 0;
                ((CEntity)inv.value).instance_position = ((CEntity)inv.value).instance_position & FLG_MASK; //--VV--
				o = inv.next;
			} else {
				o = null;
			}
		}
	}


	private int get_roles(StaticFields staticFields, CEntityDefinition edef, CEntity inst) throws SdaiException {
		Object value;
		int roles_count = 0;
		boolean add;
		for (int i = 0; i < edef.attributes.length; i++) {
			CExplicit_attribute attr = edef.attributes[i];
			Field field = edef.attributeFields[i];
			SSuper ssuper;
			if (owning_model.described_schema == null) {
				ssuper = edef.fieldOwners[i].ssuper;
			} else {
				ssuper = edef.ssuper;
			}
			try {
				value = ssuper.getObject(this, field);
			} catch (Exception ex) {
				throw new SdaiException(SdaiException.SY_ERR, ex);
			}
			add = false;
			if (value == inst) {
				add = true;
			} else if (value instanceof CAggregate && ((CAggregate)value).is_referencing(inst)) {
				add = true;
			}
			if (add) {
				if (staticFields.roles == null) {
					staticFields.roles = new EAttribute[ROLES_ARRAY_SIZE];
				} else if (roles_count >= staticFields.roles.length) {
					ensureRolesCapacity(staticFields);
				}
				staticFields.roles[roles_count] = attr;
				roles_count++;
			}
		}
		return roles_count;
	}


	private void analyse_roles(StaticFields staticFields, CEntity reference, Value result) throws SdaiException {
		int i;
		CEntityDefinition edef = (CEntityDefinition)getInstanceType();
		CSchema_definition und_schema = owning_model.underlying_schema;
		SdaiModel dict = und_schema.modelDictionary;
		SchemaData schd = dict.schemaData;

		int r_count = get_roles(staticFields, edef, reference);
		SdaiModel ent_owner = ((CEntity)edef).owning_model;
		String ent_name = edef.getNameUpperCase();
		String used_schema = und_schema.getName(null).toUpperCase();
		String attr_name;
		String str;
		if (ent_owner == dict) {
			for (i = 0; i < r_count; i++) {
				attr_name = staticFields.roles[i].getName(null).toUpperCase();
				str = used_schema + "." + ent_name + "." + attr_name;
				result.add_qualified_name(str);
			}
		} else {
			if (ent_owner.described_schema == null) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			String ent_schema = ent_owner.described_schema.getName(null).toUpperCase();
			for (i = 0; i < r_count; i++) {
				attr_name = staticFields.roles[i].getName(null).toUpperCase();
				str = ent_schema + "." + ent_name + "." + attr_name;
				result.add_qualified_name(str);
			}
			int index = schd.find_entity(0, schd.sNames.length - 1, ent_name);
			if (index < 0) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			if (schd.e_aliases == null) {
				schd.take_aliases_of_entity_defs();
			}
			for (i = 0; i < r_count; i++) {
				attr_name = staticFields.roles[i].getName(null).toUpperCase();
				if (schd.e_aliases[index] != null) {
					str = used_schema + "." + schd.e_aliases[index] + "." + attr_name;
				} else {
					str = used_schema + "." + ent_name + "." + attr_name;
				}
				result.add_qualified_name(str);
			}
		}
	}


	/**
	 * Wraps this instance to ComplexEntityValue object and returns it.
	 *
	 * @return <code>ComplexEntityValue</code> which wraps this instance
	 * @exception SdaiException if an error occurs in underlying JSDAI operations
	 */
	public ComplexEntityValue toValue() throws SdaiException {
		StaticFields staticFields = StaticFields.get();
		if (staticFields.entity_values == null) {
			staticFields.entity_values = new ComplexEntityValue();
		}
		prepareData(staticFields.entity_values);
		getAll(staticFields.entity_values);
		for (int i = 0; i < staticFields.entity_values.def.noOfPartialEntityTypes; i++) {
			staticFields.entity_values.entityValues[i].owner = this;
		}
		return staticFields.entity_values;
	}


	void prepareData(ComplexEntityValue entity_values) throws SdaiException {
		int i;
		CEntity_definition def = (CEntity_definition)getInstanceType();
		entity_values.def = def;
		if (entity_values.entityValues.length < def.noOfPartialEntityTypes) {
			entity_values.enlarge(def.noOfPartialEntityTypes);
		} else {
			for (i = 0; i < def.noOfPartialEntityTypes; i++) {
				if (entity_values.entityValues[i] == null) {
					entity_values.entityValues[i] = new EntityValue(owning_model.repository.session);
				}
			}
		}
		for (i = 0; i < def.noOfPartialEntityTypes; i++) {
			EntityValue pval = entity_values.entityValues[i];
			CEntity_definition partial_def = def.partialEntityTypes[i];
			if (pval.values == null) {
				if (SdaiSession.NUMBER_OF_VALUES >= partial_def.noOfPartialAttributes) {
					pval.values = new Value[SdaiSession.NUMBER_OF_VALUES];
				} else {
					pval.values = new Value[partial_def.noOfPartialAttributes];
				}
			} else if (pval.values.length < partial_def.noOfPartialAttributes) {
				pval.enlarge(partial_def.noOfPartialAttributes);
			}
			for (int j = 0; j < partial_def.noOfPartialAttributes; j++) {
				if (pval.values[j] == null) {
					pval.values[j] = new Value();
				}
			}
			pval.count = partial_def.noOfPartialAttributes;
			pval.def = partial_def;
		}
	}


	public AEntity getAllReferences() throws SdaiException {
//		synchronized (syncObject) {
		int i, j;
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		StaticFields staticFields = StaticFields.get();
		if (staticFields.entity_values == null) {
			staticFields.entity_values = new ComplexEntityValue();
		}
		prepareData(staticFields.entity_values);
		getAll(staticFields.entity_values);
		CEntity_definition edef = (CEntity_definition)getInstanceType();
		AEntity refs = new AEntity();
		if (staticFields.it_refs == null) {
			staticFields.it_refs = refs.createIterator();
		} else {
			refs.attachIterator(staticFields.it_refs);
		}
		boolean resolved = false;
		for (i = 0; i < edef.noOfPartialEntityTypes; i++) {
			EntityValue pval = staticFields.entity_values.entityValues[i];
			if (pval == null) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			for (j = 0; j < pval.count; j++) {
				boolean res = pval.values[j].resolve_references(this);
				if (res) {
					resolved = true;
				}
			}
		}
		if (resolved) {
			getAll(staticFields.entity_values);
		}
		for (i = 0; i < edef.noOfPartialEntityTypes; i++) {
			EntityValue pval = staticFields.entity_values.entityValues[i];
			for (j = 0; j < pval.count; j++) {
				pval.values[j].find_references(refs, staticFields.it_refs);
			}
		}
		return refs;
//		} // syncObject
	}

    /**
     * @since 3.6.0
     */
	public EEntity moveUsersFrom(EEntity src) throws SdaiException {
//		synchronized (syncObject) {
		CEntity srce = (CEntity)src;
		if (owning_model == null || srce.owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.active_transaction.mode != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.TR_NRW, session.active_transaction);
		}
		if (!(isKindOf(srce.getInstanceType()))) {
//		if (getInstanceType() != srce.getInstanceType()) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		boolean save4undo = false;
		if (session.undo_redo_file != null) {
			save4undo = true;
		}
		if (srce.owning_model.optimized) {
			srce.moveReferencesNoInverse(srce, this, save4undo);
		} else {
			srce.moveReferences(srce, this, save4undo);
		}
		srce.inverseList = null;
		return this;
//		} // syncObject
	}


	/**
     * @since 3.6.0
     */
    public EEntity copyValuesFrom(EEntity src) throws SdaiException {
//		synchronized (syncObject) {
		CEntity srce = (CEntity)src;
		if (owning_model == null || srce.owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.active_transaction.mode != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.TR_NRW, session.active_transaction);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		CEntity_definition def = (CEntity_definition)srce.getInstanceType();
		if (getInstanceType() != def) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}

		StaticFields staticFields = StaticFields.get();
		if (staticFields.entity_values == null) {
			staticFields.entity_values = new ComplexEntityValue();
		}
		session.undoRedoModifyPrepare(this);
		srce.owning_model.prepareAll(staticFields.entity_values, def);
		srce.getAll(staticFields.entity_values);
		for (int i = 0; i < def.noOfPartialEntityTypes; i++) {
			EntityValue ent_val = staticFields.entity_values.entityValues[i];
			for (int j = 0; j < ent_val.count; j++) {
				ent_val.values[j].check_references(staticFields, null, null, this);
			}
		}
		if (owning_model.optimized) {
			changeInverseReferencesNoInverse(this, null, false);
		} else {
			changeInverseReferences(this, null, false, false);
		}
//owning_model.print_entity_values(staticFields.entity_values, 1106);
		setAll(staticFields.entity_values);
		return this;
//		} // syncObject
	}


	void load_values(RandomAccessFile ur_f, CEntity_definition def, boolean copied) throws java.io.IOException, SdaiException {
		int i;
		StaticFields staticFields = StaticFields.get();
		if (staticFields.entity_values2 == null) {
			staticFields.entity_values2 = new ComplexEntityValue();
		}
		staticFields.entity_values2.def = def;
		if (staticFields.entity_values2.entityValues.length < def.noOfPartialEntityTypes) {
			staticFields.entity_values2.enlarge(def.noOfPartialEntityTypes);
		} else {
			for (i = 0; i < def.noOfPartialEntityTypes; i++) {
				if (staticFields.entity_values2.entityValues[i] == null) {
					staticFields.entity_values2.entityValues[i] = new EntityValue(owning_model.repository.session);
				}
			}
		}
		SchemaData sch_data = owning_model.underlying_schema.modelDictionary.schemaData;
		for (i = 0; i < def.noOfPartialEntityTypes; i++) {
			CEntityDefinition partial_def = def.partialEntityTypes[i];
			staticFields.entity_values2.entityValues[i].def = (CEntity_definition)partial_def;
			int count_of_values = partial_def.noOfPartialAttributes;
			if (count_of_values > 0) {
				EntityValue pval = staticFields.entity_values2.entityValues[i];
				if (pval.values == null) {
					if (SdaiSession.NUMBER_OF_VALUES >= count_of_values) {
						pval.values = new Value[SdaiSession.NUMBER_OF_VALUES];
					} else {
						pval.values = new Value[count_of_values];
					}
				} else if (count_of_values > pval.values.length) {
					pval.enlarge(count_of_values);
				}
				int k = 0;
				for (int j = 0; j < count_of_values; j++) {
					if (pval.values[k] == null) {
						pval.values[k] = new Value();
					}
					Value val = pval.values[k];
					try {
						extract_value_for_undo(val, ur_f, true, (byte)' ', sch_data, copied);
					} catch (ArrayIndexOutOfBoundsException ex) {
						String base = SdaiSession.line_separator + AdditionalMessages.BF_ERR;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
					k++;
				}
				pval.count = k;
			} else {
				staticFields.entity_values2.entityValues[i].count = 0;
			}
		}
		setAll(staticFields.entity_values2);
	}


	private boolean extract_value_for_undo(Value val, RandomAccessFile ur_f, boolean byte_needed, byte sym,
				SchemaData sch_data, boolean copied) throws java.io.IOException, SdaiException, ArrayIndexOutOfBoundsException {
		SdaiSession se = owning_model.repository.session;
		byte token;

		long inst_id;
		int index2mod;
		SdaiModel ref_mod;
		SchemaData schd;
		String entity_name;
		int pop_index;
		int inst_index;

		if (byte_needed) {
			token = ur_f.readByte();
		} else {
			token = sym;
		}
		Object [] myDataA = null;
		switch (token) {
			case '$':
				val.tag = PhFileReader.MISSING;
				return false;
			case '*':
				val.tag = PhFileReader.REDEFINE;
				return false;
			case 'i':
				val.tag = PhFileReader.INTEGER;
				val.integer = ur_f.readInt();
				return false;
			case 'r':
				val.tag = PhFileReader.REAL;
				val.real = ur_f.readDouble();
				return false;
			case 'f':
				val.tag = PhFileReader.LOGICAL;
				val.integer = 0;
				return false;
			case 't':
				val.tag = PhFileReader.LOGICAL;
				val.integer = 1;
				return false;
			case 'u':
				val.tag = PhFileReader.LOGICAL;
				val.integer = 2;
				return false;
			case 'e':
				val.tag = PhFileReader.ENUM;
				val.string = ur_f.readUTF().intern();
				return false;
			case 's':
				val.tag = PhFileReader.STRING;
				val.string = ur_f.readUTF().intern();
				return false;
			case 'b':
				val.tag = PhFileReader.BINARY;
//				val.string = ur_f.readUTF();
//				val.reference = new Binary(val.string);
				Binary bnr = new Binary();
				long bt_count = ur_f.readLong();
				bnr.unused = ur_f.readByte();
				bt_count--;
				bnr.value = new byte[(int)bt_count];
				for (int i = 0; i < bt_count; i++) {
					bnr.value[i] = ur_f.readByte();
				}
				val.reference = bnr;
				return false;
			case 'p':
				val.tag = PhFileReader.TYPED_PARAMETER;
				int index2type = ur_f.readShort();
				if (index2type < 0 || index2type >= sch_data.defTypesCount) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				val.string = sch_data.dtNames[index2type];
				val.integer = 1;
				if (val.nested_values == null) {
					val.nested_values = new Value[SdaiSession.NUMBER_OF_VALUES];
				}
				if (val.nested_values[0] == null) {
					val.nested_values[0] = new Value();
				}
				extract_value_for_undo(val.nested_values[0], ur_f, true, (byte)' ', sch_data, copied);
				return false;
			case '1':
				val.tag = PhFileReader.ENTITY_REFERENCE;
				inst_id = ur_f.readLong();
				pop_index = ur_f.readShort();
				if (!copied) {
					val.reference = owning_model.instances_sim[pop_index][ur_f.readInt()];
					return false;
				}
				int instance_index = ur_f.readInt();
				if (inst_id <= instance_identifier) {
					val.reference = owning_model.instances_sim[pop_index][instance_index];
					return false;
				}
				int pos = owning_model.findInstancePositionRedo(0, owning_model.lengths[pop_index] - 1, pop_index, inst_id);
				if (pos < 0) {
					pos = -pos;
					if (pos >= owning_model.lengths[pop_index]) {
						pos = 0;
					}
					val.reference = owning_model.instances_sim[pop_index][pos];
					return false;
				}
				schd = owning_model.underlying_schema.modelDictionary.schemaData;
				CEntityDefinition edef = schd.entities[pop_index];
				SdaiModel dict_m = ((CSchema_definition)edef.owning_model.described_schema).modelDictionary;
				CEntity new_inst = dict_m.schemaData.super_inst.makeInstance(edef.getEntityClass(), owning_model, -1, 0);
				new_inst.instance_identifier = inst_id;
				CEntity[] pop_instances_sim = owning_model.instances_sim[pop_index];
				System.arraycopy(pop_instances_sim, pos, pop_instances_sim, pos + 1, owning_model.lengths[pop_index] - pos);
				pop_instances_sim[pos] = new_inst;
				owning_model.lengths[pop_index]++;
				val.reference = new_inst;
				return false;
			case '2':
				inst_id = ur_f.readLong();
				index2mod = ur_f.readShort();
				if (index2mod < 0 || index2mod >= se.n_mods_undo) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				ref_mod = se.mods_undo[index2mod];
				pop_index = ur_f.readShort();
				inst_index = ur_f.readInt();
				if (ref_mod.repository == null) {
					val.tag = PhFileReader.MISSING;
					return false;
				} else if ((ref_mod.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.NO_ACCESS) {
					val.tag = PhFileReader.ENTITY_REFERENCE;
					int true_index;
					if ((ref_mod.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY) {
						true_index = ref_mod.find_entityRO(ref_mod.dictionary.schemaData.entities[pop_index]);
					} else {
						true_index = pop_index;
					}
//					val.reference = ref_mod.instances_sim[pop_index][inst_index];
					val.reference = ref_mod.instances_sim[true_index][inst_index];
					return false;
				} else {
					schd = ref_mod.dictionary.schemaData;
					entity_name = schd.sNames[pop_index];
					val.tag = PhFileReader.ENTITY_REFERENCE;
					val.reference = owning_model.newConnector(ref_mod, entity_name, inst_id, this);
					val.string = entity_name;
					return true;
				}
			case '3':
				inst_id = ur_f.readLong();
				index2mod = ur_f.readShort();
				if (index2mod < 0 || index2mod >= se.n_mods_undo) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				ref_mod = se.mods_undo[index2mod];
				pop_index = ur_f.readShort();
				if (ref_mod.repository == null) {
					val.tag = PhFileReader.MISSING;
					return false;
				} else if ((ref_mod.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.NO_ACCESS) {
					if (pop_index >= 0) {
						inst_index = ref_mod.find_instance(0, ref_mod.lengths[pop_index] - 1, pop_index, inst_id);
						if (inst_index >= 0) {
							val.tag = PhFileReader.ENTITY_REFERENCE;
							val.reference = ref_mod.instances_sim[pop_index][inst_index];
							return false;
						} else {
							throw new SdaiException(SdaiException.SY_ERR);
						}
					} else {
						CEntity inst_rem = ref_mod.quick_find_instance(inst_id);
						if (inst_rem != null) {
							val.tag = PhFileReader.ENTITY_REFERENCE;
							val.reference = inst_rem;
							return false;
						} else {
							throw new SdaiException(SdaiException.SY_ERR);
						}
					}
				} else {
					if (pop_index >= 0) {
						schd = ref_mod.dictionary.schemaData;
						entity_name = schd.sNames[pop_index];
						val.tag = PhFileReader.ENTITY_REFERENCE;
						val.reference = owning_model.newConnector(ref_mod, entity_name, inst_id, this);
						val.string = entity_name;
					} else {
						val.tag = PhFileReader.ENTITY_REFERENCE;
						SdaiModelRef modelRef = (SdaiModelRef)ref_mod.getQuerySourceInstanceRef();
						val.reference = owning_model.newConnector(modelRef.getRepositoryId(), modelRef.getModelId(),
							inst_id, this);
						val.string = null;
					}
					return true;
				}
			case '4':
				inst_id = ur_f.readLong();
				index2mod = ur_f.readShort();
				if (index2mod < 0 || index2mod >= se.n_mods_undo) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				ref_mod = se.mods_undo[index2mod];
				int index2ent = ur_f.readShort();
				if (index2ent < 0 || index2ent >= se.n_ent_names_undo) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				entity_name = se.ent_names_undo[index2ent];
				if (ref_mod.repository == null) {
					val.tag = PhFileReader.MISSING;
					return false;
				} else if ((ref_mod.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.NO_ACCESS) {
         	   schd = ref_mod.underlying_schema.modelDictionary.schemaData;
            	pop_index = schd.find_entity(0, schd.sNames.length - 1, entity_name);
	            if (pop_index < 0) {
   	             String base = SdaiSession.line_separator + AdditionalMessages.BF_EINC + entity_name;
      	          throw new SdaiException(SdaiException.SY_ERR, base);
         	   }
					inst_index = ref_mod.find_instance(0, ref_mod.lengths[pop_index] - 1, pop_index, inst_id);
					if (inst_index >= 0) {
						val.tag = PhFileReader.ENTITY_REFERENCE;
						val.reference = ref_mod.instances_sim[pop_index][inst_index];
						return false;
					} else {
						throw new SdaiException(SdaiException.SY_ERR);
					}
				} else {
					val.tag = PhFileReader.ENTITY_REFERENCE;
					val.reference = owning_model.newConnector(ref_mod, entity_name, inst_id, this);
					val.string = entity_name;
					return true;
				}
			case '(':
				val.tag = PhFileReader.EMBEDDED_LIST;
				int index_in_list = 0;
				if (val.nested_values == null) {
					val.nested_values = new Value[SdaiSession.NUMBER_OF_VALUES];
				}
				byte bt = ur_f.readByte();
				boolean con_created = false;
				while (bt != ')') {
					if (index_in_list >= val.nested_values.length) {
						val.enlarge();
					}
					if (val.nested_values[index_in_list] == null) {
						val.nested_values[index_in_list] = new Value();
					}
					boolean res = extract_value_for_undo(val.nested_values[index_in_list], ur_f, false, bt, sch_data, copied);
					if (res) {
						con_created = true;
					}
					if (val.nested_values[index_in_list].tag != PhFileReader.REDEFINE) {
						index_in_list++;
					}
					bt = ur_f.readByte();
				}
				val.integer = index_in_list;
				val.length = index_in_list;
				if (con_created) {
					val.real = Double.NaN;
				} else {
					val.real = 0;
				}
				return false;
		}
		String base = SdaiSession.line_separator + AdditionalMessages.BF_WVAL;
		throw new SdaiException(SdaiException.SY_ERR, base);
	}


	int find_instance(int index) throws SdaiException {
		return owning_model.find_instance(0, owning_model.lengths[index] - 1, index, instance_identifier);
	}


	final protected void changeInverseReferencesNoInverse(CEntity old, CEntity newer, 
			boolean save4undo) throws SdaiException {
		int i, j;
		SdaiModel model;
		CEntityDefinition entityDef;

		StaticFields staticFields = StaticFields.get();
		staticFields.context_schema = owning_model.underlying_schema;
		CEntity_definition this_def = (CEntity_definition)getInstanceType();
		ASdaiModel act_models = owning_model.repository.session.active_models;
		for (i = 0; i < act_models.myLength; i++) {
			model = (SdaiModel)act_models.myData[i];
			if (model.repository == SdaiSession.systemRepository) {
				continue;
			}
			SchemaData sch_data = model.underlying_schema.owning_model.schemaData;
			if (sch_data.involved == 0) {
				continue;
			}
			if (sch_data.involved > 0) {
				for (j = 0; j < sch_data.involved; j++) {
					entityDef = sch_data.entities[sch_data.aux[j]];
					model.scanForRefChange(staticFields, old, newer, sch_data.aux[j], entityDef, save4undo);
				}
				continue;
			}
			sch_data.involved = 0;
			for (j = 0; j < sch_data.noOfEntityDataTypes; j++) {
				entityDef = sch_data.entities[j];
				CExplicit_attribute [] attributes = entityDef.attributes;
				if (attributes == null) {
					continue;
				}
				boolean found = false;
				for (int k = 0; k < attributes.length; k++) {
					if (entityDef.fieldOwners[k] == null) {
						continue;
					}
					found = ((AttributeDefinition)attributes[k]).search_attribute(this_def);
					if (found) {
						break;
					}
				}
				if (found) {
					if (staticFields.entity_values2 == null) {
						staticFields.entity_values2 = new ComplexEntityValue();
					}
					sch_data.aux[sch_data.involved] = j;
					sch_data.involved++;
					model.scanForRefChange(staticFields, old, newer, j, entityDef, save4undo);
				}
			}
		}

		for (i = 0; i < act_models.myLength; i++) {
			model = (SdaiModel)act_models.myData[i];
			if (model.repository == SdaiSession.systemRepository) {
				continue;
			}
			model.underlying_schema.owning_model.schemaData.involved = -1;
		}
		staticFields.context_schema = null;
	}


	final protected void setModifiedFlagNoInverse() throws SdaiException {
		int i, j;
		SdaiModel model;
		CEntityDefinition entityDef;

		StaticFields staticFields = StaticFields.get();
		staticFields.context_schema = owning_model.underlying_schema;
		CEntity_definition this_def = (CEntity_definition)getInstanceType();
		ASdaiModel act_models = owning_model.repository.session.active_models;
		for (i = 0; i < act_models.myLength; i++) {
			model = (SdaiModel)act_models.myData[i];
			if (model.repository == SdaiSession.systemRepository) {
				continue;
			}
			SchemaData sch_data = model.underlying_schema.owning_model.schemaData;
			if (sch_data.involved == 0) {
				continue;
			}
			if (sch_data.involved > 0) {
				for (j = 0; j < sch_data.involved; j++) {
					entityDef = sch_data.entities[sch_data.aux[j]];
					model.scanToSetModifiedFlag(staticFields, this, sch_data.aux[j], entityDef);
				}
				continue;
			}
			sch_data.involved = 0;
			for (j = 0; j < sch_data.noOfEntityDataTypes; j++) {
				entityDef = sch_data.entities[j];
				CExplicit_attribute [] attributes = entityDef.attributes;
				if (attributes == null) {
					continue;
				}
				boolean found = false;
				for (int k = 0; k < attributes.length; k++) {
					if (entityDef.fieldOwners[k] == null) {
						continue;
					}
					found = ((AttributeDefinition)attributes[k]).search_attribute(this_def);
					if (found) {
						break;
					}
				}
				if (found) {
					if (staticFields.entity_values == null) {
						staticFields.entity_values = new ComplexEntityValue();
					}
					sch_data.aux[sch_data.involved] = j;
					sch_data.involved++;
					model.scanToSetModifiedFlag(staticFields, this, j, entityDef);
				}
			}
		}

		for (i = 0; i < act_models.myLength; i++) {
			model = (SdaiModel)act_models.myData[i];
			if (model.repository == SdaiSession.systemRepository) {
				continue;
			}
			model.underlying_schema.owning_model.schemaData.involved = -1;
		}
		staticFields.context_schema = null;
	}


	final protected void unsetInverseReferencesNoInverse(boolean unset_if_true_delete_else, 
			boolean replace_by_connector, boolean delete_repo) throws SdaiException {
		int i, j;
		SdaiModel model;
		CEntityDefinition entityDef;

		StaticFields staticFields = StaticFields.get();
		staticFields.context_schema = owning_model.underlying_schema;
		CEntity_definition this_def = (CEntity_definition)getInstanceType();
		ASdaiModel act_models = owning_model.repository.session.active_models;
		for (i = 0; i < act_models.myLength; i++) {
			model = (SdaiModel)act_models.myData[i];
			if (model.repository == SdaiSession.systemRepository) {
				continue;
			}
			if (unset_if_true_delete_else) {
				if (owning_model == model || model.closingAll) {
					continue;
				}
			} else {
				if (model.repository == owning_model.repository) {
					continue;
				}
			}
			SchemaData sch_data = model.underlying_schema.owning_model.schemaData;
			if (sch_data.involved == 0) {
				continue;
			}
			if (sch_data.involved > 0) {
				for (j = 0; j < sch_data.involved; j++) {
					entityDef = sch_data.entities[sch_data.aux[j]];
					if (unset_if_true_delete_else) {
						model.scanForUnset(staticFields, this, sch_data.aux[j], entityDef, replace_by_connector);
					} else {
						model.scanForUnset(staticFields, this, sch_data.aux[j], entityDef, !delete_repo);
					}
				}
				continue;
			}
			sch_data.involved = 0;
			for (j = 0; j < sch_data.noOfEntityDataTypes; j++) {
				entityDef = sch_data.entities[j];
				CExplicit_attribute [] attributes = entityDef.attributes;
				if (attributes == null) {
					continue;
				}
				boolean found = false;
				for (int k = 0; k < attributes.length; k++) {
					if (entityDef.fieldOwners[k] == null) {
						continue;
					}
					found = ((AttributeDefinition)attributes[k]).search_attribute(this_def);
					if (found) {
						break;
					}
				}
				if (found) {
					if (staticFields.entity_values == null) {
						staticFields.entity_values = new ComplexEntityValue();
					}
					sch_data.aux[sch_data.involved] = j;
					sch_data.involved++;
					if (unset_if_true_delete_else) {
						model.scanForUnset(staticFields, this, j, entityDef, replace_by_connector);
					} else {
						model.scanForUnset(staticFields, this, j, entityDef, !delete_repo);
					}
				}
			}
		}

		for (i = 0; i < act_models.myLength; i++) {
			model = (SdaiModel)act_models.myData[i];
			if (model.repository == SdaiSession.systemRepository) {
				continue;
			}
			model.underlying_schema.owning_model.schemaData.involved = -1;
		}
		staticFields.context_schema = null;
	}


	final protected Value checkInverseReferencesNoInverse(CEntity_definition new_def) throws SdaiException {
		int i, j;
		SdaiModel model;
		CEntityDefinition entityDef;
		Value val = null;

		StaticFields staticFields = StaticFields.get();
		staticFields.context_schema = owning_model.underlying_schema;
		CEntity_definition this_def = (CEntity_definition)getInstanceType();
		ASdaiModel act_models = owning_model.repository.session.active_models;
		int m = act_models.myLength;
		for (i = 0; i < act_models.myLength; i++) {
			model = (SdaiModel)act_models.myData[i];
			if (model.repository == SdaiSession.systemRepository) {
				continue;
			}
			SchemaData sch_data = model.underlying_schema.owning_model.schemaData;
			if (sch_data.involved == 0) {
				continue;
			}
			if (sch_data.involved > 0) {
				for (j = 0; j < sch_data.involved; j++) {
					entityDef = sch_data.entities[sch_data.aux[j]];
					val = model.scanForCheck(staticFields, this, sch_data.aux[j], entityDef, new_def);
					if (val != null) {
						m = i + 1;
						break;
					}
				}
				if (val != null) {
					break;
				}
				continue;
			}
			sch_data.involved = 0;
			for (j = 0; j < sch_data.noOfEntityDataTypes; j++) {
				entityDef = sch_data.entities[j];
				CExplicit_attribute [] attributes = entityDef.attributes;
				if (attributes == null) {
					continue;
				}
				boolean found = false;
				for (int k = 0; k < attributes.length; k++) {
					if (entityDef.fieldOwners[k] == null) {
						continue;
					}
					found = ((AttributeDefinition)attributes[k]).search_attribute(this_def);
					if (found) {
						break;
					}
				}
				if (found) {
					if (staticFields.entity_values == null) {
						staticFields.entity_values = new ComplexEntityValue();
					}
					sch_data.aux[sch_data.involved] = j;
					sch_data.involved++;
					val = model.scanForCheck(staticFields, this, j, entityDef, new_def);
					if (val != null) {
						m = i + 1;
						break;
					}
				}
			}
			if (val != null) {
				break;
			}
		}

		for (i = 0; i < m; i++) {
			model = (SdaiModel)act_models.myData[i];
			if (model.repository == SdaiSession.systemRepository) {
				continue;
			}
			model.underlying_schema.owning_model.schemaData.involved = -1;
		}
		staticFields.context_schema = null;
		return val;
	}


	final protected void moveReferencesNoInverse(CEntity old, CEntity newer, 
			boolean save4undo) throws SdaiException {
		int i, j;
		SdaiModel model;
		CEntityDefinition entityDef;

		StaticFields staticFields = StaticFields.get();
		staticFields.context_schema = owning_model.underlying_schema;
		CEntity_definition this_def = (CEntity_definition)getInstanceType();
		ASdaiModel act_models = owning_model.repository.session.active_models;
		for (i = 0; i < act_models.myLength; i++) {
			model = (SdaiModel)act_models.myData[i];
			if (model.repository == SdaiSession.systemRepository) {
				continue;
			}
			SchemaData sch_data = model.underlying_schema.owning_model.schemaData;
			if (sch_data.involved == 0) {
				continue;
			}
			if (sch_data.involved > 0) {
				for (j = 0; j < sch_data.involved; j++) {
					entityDef = sch_data.entities[sch_data.aux[j]];
					model.scanForRefMove(staticFields, old, newer, sch_data.aux[j], entityDef, save4undo);
				}
				continue;
			}
			sch_data.involved = 0;
			for (j = 0; j < sch_data.noOfEntityDataTypes; j++) {
				entityDef = sch_data.entities[j];
				CExplicit_attribute [] attributes = entityDef.attributes;
				if (attributes == null) {
					continue;
				}
				boolean found = false;
				for (int k = 0; k < attributes.length; k++) {
					if (entityDef.fieldOwners[k] == null) {
						continue;
					}
					found = ((AttributeDefinition)attributes[k]).search_attribute(this_def);
					if (found) {
						break;
					}
				}
				if (found) {
					if (staticFields.entity_values == null) {
						staticFields.entity_values = new ComplexEntityValue();
					}
					sch_data.aux[sch_data.involved] = j;
					sch_data.involved++;
					model.scanForRefMove(staticFields, old, newer, j, entityDef, save4undo);
				}
			}
		}

		for (i = 0; i < act_models.myLength; i++) {
			model = (SdaiModel)act_models.myData[i];
			if (model.repository == SdaiSession.systemRepository) {
				continue;
			}
			model.underlying_schema.owning_model.schemaData.involved = -1;
		}
		staticFields.context_schema = null;
	}



	void ensureRolesCapacity(StaticFields staticFields) {
		int new_length = staticFields.roles.length * 2;
		EAttribute [] new_array = new EAttribute[new_length];
		System.arraycopy(staticFields.roles, 0, new_array, 0, staticFields.roles.length);
		staticFields.roles = new_array;
	}

	public ExternalData createExternalData() throws SdaiException {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		return owning_model.repository.createEntityExternalData(this);
	}

	public ExternalData getExternalData() throws SdaiException {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		return owning_model.repository.getEntityExternalData(this);
	}


	public boolean testExternalData() throws SdaiException {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		return owning_model.repository.testEntityExternalData(this);
	}


	public void removeExternalData() throws SdaiException {
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		owning_model.repository.removeEntityExternalData(this, false, true);
	}


}
