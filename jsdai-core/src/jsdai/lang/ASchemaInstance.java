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

import java.io.IOException;
import java.io.OutputStream;
import jsdai.client.*;
import jsdai.dictionary.*;
import jsdai.xml.InstanceReader;
import jsdai.xml.SdaiInputSource;

/**
 Specialized class implementing <code>Aggregate</code> for members of
 type <code>SchemaInstance</code>. See <a href="Aggregate.html">Aggregate</a> for detailed 
 description of methods whose specializations are given here.
 */
public class ASchemaInstance extends SessionAggregate
{

/**
 * Constructs a new object of aggregate <code>ASchemaInstance</code>
 * without aggregation type. The aggregates of such kind are used
 * as non-persistent lists.
 * @see Aggregate#getAggregationType
 */
	public ASchemaInstance() {
		super();
	}

	ASchemaInstance(EAggregation_type provided_type, SdaiCommon instance) {
		super(provided_type, instance);
	}


	public int getMemberCount() throws SdaiException {
//		synchronized (syncObject) {
		Object owning_obj = getOwner();
		if (owning_obj instanceof SdaiRepository) {
			SdaiRepository repo = (SdaiRepository)owning_obj;
			if (repo.isRemote()) {
				if (!SdaiSession.isSessionAvailable()) {
					throw new SdaiException(SdaiException.SS_NOPN);
				}
				SdaiTransaction trans = repo.session.active_transaction;
				if (trans == null) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				int del_count = 0;
				for (int i = 0; i < trans.stack_length_sch_insts; i++) {
					if (trans.stack_del_sch_insts_rep[i] == repo && !trans.stack_del_sch_insts[i].checkSchInstance(null)) {
						del_count++;
					}
				}
				return repo.getSchInstCount() - del_count + repo.unresolved_sch_count;
			}
		}
		return super.getMemberCount();
//		} // syncObject
	}


/**
 * It is a specialization of 
 * {@link Aggregate#isMember(Object, EDefined_type []) isMember(Object, EDefined_type [])}
 * method - the first parameter is of type <code>SchemaInstance</code> and the
 * second parameter is dropped.
 */
	public boolean isMember(SchemaInstance value) throws SdaiException {
		return isMember(value, null);
	}

/**
 * It is {@link Aggregate#getByIndexObject getByIndexObject} method
 * with return value of type <code>SchemaInstance</code> instead of <code>Object</code>.
 */
  public SchemaInstance getByIndex(int index) throws SdaiException {
//		synchronized (syncObject) {
		Object owning_obj = getOwner();
		if (owning_obj instanceof SdaiRepository) {
			SdaiRepository repo = (SdaiRepository)owning_obj;
			if (repo.isRemote()) {
				if (!SdaiSession.isSessionAvailable()) {
					throw new SdaiException(SdaiException.SS_NOPN);
				}
				SdaiIterator iter = createIterator();
				for (int i = 0; i < index; i++) {
					iter.next();
				}
				return getCurrentMember(iter);
			}
		}
		return (SchemaInstance)getByIndexObject(index);
//		} // syncObject
	}


	public void setByIndex(int index, Object value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (value instanceof SchemaInstance) {
			setByIndexCommon(index, value);
		} else {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
//		} // syncObject
	}


	public void addByIndex(int index, Object value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (value instanceof SchemaInstance) {
			addByIndexCommon(index, value);
		} else {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
//		} // syncObject
	}


/**
 * It is a specialization of 
 * {@link Aggregate#addByIndex(int, Object, EDefined_type []) addByIndex(int, Object, EDefined_type [])}
 * method - the second parameter is of type <code>SchemaInstance</code>
 * and the third parameter is dropped.
 */
	public void addByIndex(int index, SchemaInstance value) throws SdaiException {
		addByIndexCommon(index, value);
	}


	public void addUnordered(Object value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (value instanceof SchemaInstance) {
			addUnorderedCommon(value);
		} else {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
//		} // syncObject
	}


	public void setCurrentMember(SdaiIterator it, Object value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (value instanceof SchemaInstance) {
			setCurrentMemberCommon(it, value);
		} else {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
//		} // syncObject
	}


	public void addBefore(SdaiIterator it, Object value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (value instanceof SchemaInstance) {
			addBeforeCommon(it, value);
		} else {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
//		} // syncObject
	}


	public void addAfter(SdaiIterator it, Object value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (value instanceof SchemaInstance) {
			addAfterCommon(it, value);
		} else {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
//		} // syncObject
	}


/**
 * It is {@link Aggregate#getCurrentMemberObject getCurrentMemberObject} method
 * with return value of type <code>SchemaInstance</code> instead of <code>Object</code>.
 */
//	public SchemaInstance getCurrentMember(SdaiIterator iter) throws SdaiException {
//		return (SchemaInstance)getCurrentMemberObject(iter);
//	}

/**
 * Returns an aggregate which is a union of aggregates "associated_models"
 * taken over all schema instances contained in this <code>ASchemaInstance</code>.
 * The type of the produced aggregate is SET.
 * @return an aggregate containing models which are associated with at least
 * one schema instance in this <code>ASchemaInstance</code>.
 * <p> This method can be used to get an aggregate which can be submitted
 * to parameter with the name "domain" in several methods in class 
 * <code>CEntity</code>, for example 
 * {@link EEntity#findEntityInstanceUsedin findEntityInstanceUsedin} method.
 * <p> The method is an extension of JSDAI, which is 
 * not a part of the standard.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException SY_ERR, underlying system error.
 */
	public ASdaiModel getAssociatedModels() throws SdaiException {
//		synchronized (syncObject) {
		ASdaiModel models =
			new ASdaiModel(SdaiSession.setTypeSpecial, owner != null ? owner : GENERIC_OWNER);
		StaticFields staticFields = StaticFields.get();
		if (staticFields.it_sa == null) {
			staticFields.it_sa = createIterator();
		} else {
			attachIterator(staticFields.it_sa);
		}
		while (staticFields.it_sa.next()) {
			SchemaInstance si = getCurrentMember(staticFields.it_sa);
			if (si.associated_models == null) {
				if (si.isRemote()) {
					si.getAssociatedModelsPrivate();
				} else {
					continue;
				}
			}
			for (int j = 0; j < si.associated_models.myLength; j++) {
				models.addUnorderedRO((SdaiModel)si.associated_models.myData[j]);
			}
		}
		return models;
//		} // syncObject
	}


/**
 * Returns a description of this <code>ASchemaInstance</code> as a <code>String</code>.
 * It includes constant string "SchemaInstances: " and a list of the names
 * of all the schema instances contained in this aggregate.
 * <P><B>Example:</B>
 * <P><TT><pre>    ASchemaInstance sch_aggr = ...;
 *    System.out.println(sch_aggr);</TT></pre>
 * &nbsp;&nbsp;&nbsp;&nbsp;The string printed will be like this:
 * <pre>    SchemaInstances: sch_inst, sch_inst2, sch_inst3, sch_inst_special </pre>
 * @return the <code>String</code> representing this <code>ASchemaInstance</code>.
 */
	public String toString() {
//		synchronized (syncObject) {
		try {
			return getAsString();
		} catch (SdaiException e) {
			return super.toString();
		}
//		} // syncObject
	}
	String getAsString() throws SdaiException {
		int i;
		int str_index = -1;
		StaticFields staticFields = StaticFields.get();
		if (staticFields.instance_as_string == null) {
			staticFields.instance_as_string = new byte[PhFileWriter.NUMBER_OF_CHARACTERS_IN_INSTANCE];
		}
		for (i = 0; i < SCHEMA_INSTANCES_LENGTH; i++) {
			staticFields.instance_as_string[++str_index] = SCHEMA_INSTANCES[i];
		}
		staticFields.instance_as_string[++str_index] = PhFileReader.COLON;
		boolean first = true;
		SchemaInstance sch;
		int ln;
		if (myType == null || myType.express_type == DataType.LIST) {
			if (staticFields.it_sa == null) {
				staticFields.it_sa = createIterator();
			} else {
				attachIterator(staticFields.it_sa);
			}
			while (staticFields.it_sa.next()) {
				sch = (SchemaInstance)((ListElement)staticFields.it_sa.myElement).object;
				ln = sch.name.length();
				if (str_index + ln + 2 >= staticFields.instance_as_string.length) {
					enlarge_instance_string(str_index + 1, str_index + ln + 3);
				}
				if (!first) {
					staticFields.instance_as_string[++str_index] = PhFileReader.COMMA_b;
				}
				staticFields.instance_as_string[++str_index] = PhFileReader.SPACE;
				first = false;
				str_index = write_string(sch.name, str_index);
			}
		} else {
			for (i = 0; i < myLength; i++) {
				sch = (SchemaInstance)myData[i];
				ln = sch.name.length();
				if (str_index + ln + 2 >= staticFields.instance_as_string.length) {
					enlarge_instance_string(str_index + 1, str_index + ln + 3);
				}
				if (!first) {
					staticFields.instance_as_string[++str_index] = PhFileReader.COMMA_b;
				}
				staticFields.instance_as_string[++str_index] = PhFileReader.SPACE;
				first = false;
				str_index = write_string(sch.name, str_index);
			}
		}
		return new String(staticFields.instance_as_string, 0, str_index + 1);
	}


	void shrink(SdaiRepository repo) throws SdaiException {
		int count = 0;
		for (int i = 0; i < myLength; i++) {
			SchemaInstance sch = (SchemaInstance)myData[i];
			if (sch.repository != repo) {
				myData[count] = myData[i];
				count++;
			}
		}
		myLength = count;
	}


/**
 * It is {@link Aggregate#getCurrentMemberObject getCurrentMemberObject} method
 * with return value of type <code>SchemaInstance</code> instead of <code>Object</code>.
 */
	public SchemaInstance getCurrentMember(SdaiIterator it) throws SdaiException {
//		synchronized (syncObject) {
		Object owning_obj = getOwner();
		if (owning_obj instanceof SdaiRepository) {
			SdaiRepository repo = (SdaiRepository)owning_obj;
			if (repo.isRemote()) {
				if (it.myAggregate != this) {
					String base = SdaiSession.line_separator + AdditionalMessages.IR_IMPR;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				if (!SdaiSession.isSessionAvailable()) {
					throw new SdaiException(SdaiException.SS_NOPN);
				}
				if (it.myElement == null && (it.myIndex < 0 || it.behind)) {
					throw new SdaiException(SdaiException.IR_NSET, it);
				}
				if (it.myIndex >= 0) {
					return (SchemaInstance)repo.schemas.myData[it.myIndex];
				}
				for (int i = 0; i < repo.schemas.myLength; i++) {
					SchemaInstance sch_inst = (SchemaInstance)repo.schemas.myData[i];
					if (sch_inst.checkSchInstance(it.myElement)) {
						return sch_inst;
					}
				}
				SchemaInstanceHeader schInstHeader = repo.takeSchInstHeader(it.myElement);
				String dict_name = schInstHeader.nativeSchema.toUpperCase() + SdaiSession.DICTIONARY_NAME_SUFIX;
				SdaiModel dictionary = SdaiSession.systemRepository.findDictionarySdaiModel(dict_name);
				if (dictionary.getMode() == SdaiModel.NO_ACCESS) {
					dictionary.startReadOnlyAccess();
				}
//System.out.println("SchemaInstance   repo: " + repo.name + 
//"    schema name: " + schInstHeader.name);
				return repo.createNewSchemaInstance(schInstHeader, dictionary.described_schema, it.myElement);
			}
		}
		return (SchemaInstance)getCurrentMemberObject(it);
//		} // syncObject
	}


	void startAssociatedModelsAll() throws SdaiException {
		SchemaInstance si;
		if (myType == null || myType.express_type == DataType.LIST) {
			StaticFields staticFields = StaticFields.get();
			if (staticFields.it_sa == null) {
				staticFields.it_sa = createIterator();
			} else {
				attachIterator(staticFields.it_sa);
			}
			while (staticFields.it_sa.next()) {
				si = (SchemaInstance)((ListElement)staticFields.it_sa.myElement).object;
				si.startAssociatedModels(this);
			}
		} else {
			for (int i = 0; i < myLength; i++) {
				si = (SchemaInstance)myData[i];
				si.startAssociatedModels(this);
			}
		}
	}


	void refreshDomainAll(boolean read_write, ASdaiModel rwModels) throws SdaiException {
		SchemaInstance si;
		if (myType == null || myType.express_type == DataType.LIST) {
			StaticFields staticFields = StaticFields.get();
			if (staticFields.it_sa == null) {
				staticFields.it_sa = createIterator();
			} else {
				attachIterator(staticFields.it_sa);
			}
			while (staticFields.it_sa.next()) {
				si = (SchemaInstance)((ListElement)staticFields.it_sa.myElement).object;
				si.refreshDomain(read_write, rwModels);
			}
		} else {
			for (int i = 0; i < myLength; i++) {
				si = (SchemaInstance)myData[i];
				si.refreshDomain(read_write, rwModels);
			}
		}
	}


	void closeDomainAll() throws SdaiException {
		SchemaInstance si;
		if (myType == null || myType.express_type == DataType.LIST) {
			StaticFields staticFields = StaticFields.get();
			if (staticFields.it_sa == null) {
				staticFields.it_sa = createIterator();
			} else {
				attachIterator(staticFields.it_sa);
			}
			while (staticFields.it_sa.next()) {
				si = (SchemaInstance)((ListElement)staticFields.it_sa.myElement).object;
				si.closeDomain();
			}
		} else {
			for (int i = 0; i < myLength; i++) {
				si = (SchemaInstance)myData[i];
				si.closeDomain();
			}
		}
	}


	void removeSchemaUnorderedKeepSorted(Object value) throws SdaiException {
		int index =-1;
		for (int i = 0; i < myLength; i++) {
			if (myData[i] == value) {
				index = i;
				break;
			}
		}
		if (index < 0) {
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		System.arraycopy(myData, index + 1, myData, index, myLength-index-1);
		myData[myLength - 1] = null;
		myLength--;
	}


	/**
	 * Writes this aggregate of <code>SchemaInstances</code> in XML representation to specified stream.
	 * Output format is controlled by <code>instanceReader</code> parameter.
	 *
	 * @param location an <code>OutputStream</code> to write XML to
	 * @param instanceReader an {@link jsdai.xml.InstanceReader} describing output format
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 * @see jsdai.xml.InstanceReader jsdai.xml.InstanceReader
	 * @see jsdai.xml.LateBindingReader jsdai.xml.LateBindingReader
	 */
	public void exportXml(OutputStream location, InstanceReader instanceReader) throws SdaiException {
		try {
			instanceReader.serialize(location, new SdaiInputSource(this));
		} catch(SdaiException e) {
			throw e;
		} catch(Exception e) {
			throw new SdaiException(SdaiException.SY_ERR, e);
		}
	}

}
