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

import jsdai.client.SessionRemote;
import jsdai.dictionary.*;
import jsdai.query.SerializableRef;

/** Implementation of Aggregate in read-only mode as a variable-size array or a list */
abstract class SessionAggregate extends A_basis implements Aggregate {

	static final SdaiCommon GENERIC_OWNER =  new SdaiCommon() {
			SdaiCommon getOwner() {
				return null;
			}

			void modified() { }
		};

/**
	The count of elements in this aggregate.
*/
	protected int myLength;

/**
	The elements of this aggregate. They are represented in one of the following 
	two ways:
	 1) for EXPRESS SET, BAG and ARRAY types the aggregate members are stored in 
		java array;
	 2) for EXPRESS LIST type a chain of objects of class <code>ListElement</code> 
		is created; in the java array <code>myData</code> only the first (head) and 
		the last (tail) objects of this chain are stored.
*/
	protected Object [] myData;

/**
	The aggregation type of this aggregate.
*/
	protected AggregationType myType;

/**
	A constant representing a string used in <code>toString</code> method in 
	class <code>ASdaiModel</code> extending the current class. 
*/
	static final byte [] MODELS = {(byte)'S',(byte)'d',(byte)'a',(byte)'i',
		(byte)'M',(byte)'o',(byte)'d',(byte)'e',(byte)'l',(byte)'s'};

/**
	A constant representing a string used in <code>toString</code> method in 
	class <code>ASchemaInstance</code> extending the current class. 
*/
	static final byte [] SCHEMA_INSTANCES = {(byte)'S',(byte)'c',(byte)'h',(byte)'e',(byte)'m',(byte)'a',
		(byte)'I',(byte)'n',(byte)'s',(byte)'t',(byte)'a',(byte)'n',(byte)'c',(byte)'e',(byte)'s'};

/**
	A constant representing a string used in <code>toString</code> method in 
	class <code>ASdaiRepository</code> extending the current class. 
*/
	static final byte [] REPOSITORIES = {(byte)'S',(byte)'d',(byte)'a',(byte)'i',(byte)'R',(byte)'e',
		(byte)'p',(byte)'o',(byte)'s',(byte)'i',(byte)'t',(byte)'o',(byte)'r',(byte)'i',(byte)'e',(byte)'s'};

/**
	A constant representing a string used in <code>toString</code> method in 
	class <code>AEntityExtent/code> extending the current class. 
*/
	static final byte [] ENTITY_EXTENTS = {(byte)'E',(byte)'n',(byte)'t',(byte)'i',(byte)'t',(byte)'y',
		(byte)'E',(byte)'x',(byte)'t',(byte)'e',(byte)'n',(byte)'t',(byte)'s'};

/**
	The count of bytes in the constant <code>MODELS</code>.
*/
	static final int MODELS_LENGTH = MODELS.length;

/**
	The count of bytes in the constant <code>SCHEMA_INSTANCES</code>.
*/
	static final int SCHEMA_INSTANCES_LENGTH = SCHEMA_INSTANCES.length;

/**
	The count of bytes in the constant <code>REPOSITORIES</code>.
*/
	static final int REPOSITORIES_LENGTH = REPOSITORIES.length;

/**
	The count of bytes in the constant <code>ENTITY_EXTENTS</code>.
*/
	static final int ENTITY_EXTENTS_LENGTH = ENTITY_EXTENTS.length;

	static final int INIT_SIZE_SET_SESSION = 8;



/**
	Returns entity instance of which attribute this aggregate (possibly through 
	one or more other aggregates in the case of nesting) is a value. 
	If this aggregate is not created to be a value of an entity instance, then 
	<code>null</code> is returned.
*/
	CEntity getOwningInstance() {
		SdaiCommon owner = this.owner;
		while (owner instanceof Aggregate || owner instanceof SdaiListenerElement) {
			owner = owner.getOwner();
		}
		return (owner instanceof CEntity) ? ((CEntity)owner) : null;
	}


/**
	Informs the owner that some data in this aggregate were modified. 
*/
	void modified() throws SdaiException {
	}



/**
	The constructor of this class. It is used to create a non-persistent list 
	for an aggregate represented by a class extending this class, for example, 
	<code>ASdaiModel modelDomain = new ASdaiModel();</code>
*/
	protected SessionAggregate() {
		myData = new Object[2];
	}


/**
	The constructor of this class used internally in the package to create java 
	objects of classes <code>ASdaiModel</code>, <code>ASchemaInstance</code>, 
	<code>ASdaiRepository</code>, and <code>AEntityExtent</code>. 
*/
	protected SessionAggregate(EAggregation_type provided_type, SdaiCommon instance) {
		myType = (AggregationType)provided_type;
		if (myType != null && myType.express_type == DataType.LIST) {
			myData = new Object[2];
		}
		owner = instance;
	}


	public int getMemberCount() throws SdaiException {
//		synchronized (syncObject) {
		if (myType != null) {
			if (getOwner() == null) {
				throw new SdaiException(SdaiException.AI_NEXS);
			}
//			if (SdaiSession.session == null) {
			if (!isSessionAvailable()) {
				throw new SdaiException(SdaiException.SS_NOPN);
			}
		}
		if (this instanceof AEntityExtent) {
			if (myType == null || myType.shift != -1) {
				return myLength;
			} else {
				return myLength - 1;
			}
		} else if (myType != null && myType.express_type == DataType.ARRAY && myData == null) {
			CArray_type arr_type = (CArray_type)myType;
			return arr_type.getUpper_index(null).getBound_value(null) - 
				arr_type.getLower_index(null).getBound_value(null) + 1;
		} else {
			return myLength;
		}
//		} // syncObject
	}


	public boolean isMember(Object value, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
		if (myType != null) {
			if (getOwner() == null) {
				throw new SdaiException(SdaiException.AI_NEXS);
			}
//			if (SdaiSession.session == null) {
			if (!isSessionAvailable()) {
				throw new SdaiException(SdaiException.SS_NOPN);
			}
		}
		if (myLength <= 0) {
			return false;
		}
		if (myType == null || myType.express_type == DataType.LIST) {
			ListElement element = (ListElement)myData[0];
			while (element != null) {
				if (element.object == value) {
					return true;
				}
				element = element.next;
			}
			return false;
		} else {
			for (int i = 0; i < myLength; i++) {
				if (myData[i] == value) {
					return true;
				}
			}
			return false;
		}
//		} // syncObject
	}
	public boolean isMember(int value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}
	public boolean isMember(double value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}
	public boolean isMember(boolean value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


	public SdaiIterator createIterator() throws SdaiException {
//		synchronized (syncObject) {
		if (myType != null) {
			if (getOwner() == null) {
				throw new SdaiException(SdaiException.AI_NEXS);
			}
//			if (SdaiSession.session == null) {
			if (!isSessionAvailable()) {
				throw new SdaiException(SdaiException.SS_NOPN);
			}
		}
		return new SdaiIterator(this);
//		} // syncObject
	}


	public void attachIterator(SdaiIterator iter) throws SdaiException {
//		synchronized (syncObject) {
		if (myType != null) {
			if (getOwner() == null) {
				throw new SdaiException(SdaiException.AI_NEXS);
			}
//			if (SdaiSession.session == null) {
			if (!isSessionAvailable()) {
				throw new SdaiException(SdaiException.SS_NOPN);
			}
		}
		iter.myAggregate = this;
		iter.myIndex = -1;
		iter.behind = false;
		iter.myElement = null;
		if (this instanceof ASdaiModel) {
			Object owning_obj = ((ASdaiModel)this).getOwner();
			if (owning_obj instanceof SdaiRepository && 
					((SdaiRepository)owning_obj).isRemote()) {
				iter.AggregationType = SdaiIterator.SESSION_AGGR_MODELS;
			} else {
				iter.AggregationType = SdaiIterator.SESSION_AGGR;
			}
		} else if (this instanceof ASchemaInstance) {
			Object owning_obj = ((ASchemaInstance)this).getOwner();
			if (owning_obj instanceof SdaiRepository && 
					((SdaiRepository)owning_obj).isRemote()) {
				iter.AggregationType = SdaiIterator.SESSION_AGGR_SCHEMAS;
			} else {
				iter.AggregationType = SdaiIterator.SESSION_AGGR;
			}
		} else {
			iter.AggregationType = SdaiIterator.SESSION_AGGR;
		}
//		} // syncObject
	}


	public int getLowerBound() throws SdaiException {
		throw new SdaiException(SdaiException.EX_NSUP);
	}


	public int getUpperBound() throws SdaiException {
		throw new SdaiException(SdaiException.EX_NSUP);
	}


	public int query(String where, EEntity entity, AEntity result) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}

	public ASdaiModel getQuerySourceDomain() throws SdaiException{
		throw new SdaiException(SdaiException.FN_NAVL);
	}

	public AEntity getQuerySourceInstances() throws SdaiException{
		throw new SdaiException(SdaiException.FN_NAVL);
	}

	public SerializableRef getQuerySourceInstanceRef() throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}

	public SerializableRef getQuerySourceDomainRef() throws SdaiException{
		throw new SdaiException(SdaiException.FN_NAVL);
	}

	public Object getByIndexObject(int index) throws SdaiException	{
//		synchronized (syncObject) {
		if (myType != null) {
			if (getOwner() == null) {
				throw new SdaiException(SdaiException.AI_NEXS);
			}
//			if (SdaiSession.session == null) {
			if (!isSessionAvailable()) {
				throw new SdaiException(SdaiException.SS_NOPN);
			}
		}
		if (myType == null || myType.express_type == DataType.LIST) {
			index--;
			if (index < 0 || index >= myLength) {
				throw new SdaiException(SdaiException.IX_NVLD, this);
			}
			ListElement element = (ListElement)myData[0];
			while (index-- > 0) {
				element = element.next;
			}
			if (element.object == null) {
				throw new SdaiException(SdaiException.VA_NSET);
			}
			return element.object;
		} else {
			if (myType.express_type == DataType.ARRAY) {
				EBound bound = ((CArray_type)myType).getLower_index(null);
				int lower_index = bound.getBound_value(null);
				index -= lower_index;
				if (myData == null) {
					EBound upper_bound = ((CArray_type)myType).getUpper_index(null);
					initializeData(upper_bound.getBound_value(null) - lower_index + 1);
				}
			} else {
				index--;
			}
			if (index < 0 || index >= myLength) {
				throw new SdaiException(SdaiException.IX_NVLD, this);
			}
			if (myData[index] == null) {
				throw new SdaiException(SdaiException.VA_NSET);
			}
			return myData[index];
		}
//		} // syncObject
	}
	public int getByIndexInt(int index) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}
	public double getByIndexDouble(int index) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}
	public boolean getByIndexBoolean(int index) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


	public int getValueBoundByIndex(int index) throws SdaiException {
		throw new SdaiException(SdaiException.EX_NSUP);
	}


//	public void setByIndex(int index, Object value, EDefined_type select[])
//			throws SdaiException {
	void setByIndexCommon(int index, Object value) throws SdaiException {
//		synchronized (syncObject) {
		if (myType != null) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		Class bottom_class = value.getClass();
		if (CAggregate.class.isAssignableFrom(bottom_class) ||
				getClass().isAssignableFrom(bottom_class)) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		index--;
		if (index < 0 || index > myLength) {
			throw new SdaiException(SdaiException.IX_NVLD, this);
		}
		ListElement element = (ListElement)myData[0];
		while (index-- > 0) {
			element = element.next;
		}
		element.object = value;
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
//		} // syncObject
	}
	public void setByIndex(int index, int value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}
	public void setByIndex(int index, double value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}
	public void setByIndex(int index, boolean value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


	public Aggregate createAggregateByIndex(int index, EDefined_type select[])
			throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


	public int testByIndex(int index, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		if (getOwner() == null) {
			throw new SdaiException(SdaiException.AI_NEXS);
		}
//		if (SdaiSession.session == null) {
		if (!isSessionAvailable()) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		if (select != null) {
			select[0] = null;
		}
		if (myType.express_type == DataType.LIST) {
			index--;
			if (index < 0 || index >= myLength) {
				return 0;
			}
			return 1;
		} else {
			if (myType.express_type == DataType.ARRAY) {
				EBound bound = ((CArray_type)myType).getLower_index(null);
				int lower_index = bound.getBound_value(null);
				index -= lower_index;
				if (myData == null) {
					EBound upper_bound = ((CArray_type)myType).getUpper_index(null);
					initializeData(upper_bound.getBound_value(null) - lower_index + 1);
				}
			} else {
				index--;
			}
			if (index < 0 || index >= myLength) {
				throw new SdaiException(SdaiException.IX_NVLD, this);
			}
			if (myData[index] == null) {
				return 0;
			} else {
				return 1;
			}
		}
//		} // syncObject
	}


	public int testByIndex(int index) throws SdaiException {
		return testByIndex(index, null);
	}


	public int getLowerIndex() throws SdaiException {
		throw new SdaiException(SdaiException.AI_NVLD, this);
	}


	public int getUpperIndex() throws SdaiException {
		throw new SdaiException(SdaiException.AI_NVLD, this);
	}


	public void unsetValueByIndex(int index) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


	public void reindexArray() throws SdaiException {
		throw new SdaiException(SdaiException.EX_NSUP);
	}


	public void resetArrayIndex(int lower, int upper) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}

//	public void addByIndex(int index, Object value, EDefined_type select[])
//			throws SdaiException {
	void addByIndexCommon(int index, Object value) throws SdaiException {
//		synchronized (syncObject) {
		if (myType != null) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		Class bottom_class = value.getClass();
		if (CAggregate.class.isAssignableFrom(bottom_class) ||
				getClass().isAssignableFrom(bottom_class)) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		index--;
		if (index < 0 || index > myLength) {
			throw new SdaiException(SdaiException.IX_NVLD, this);
		}
		if (myData == null) {
			myData = new Object[2];
		}
		ListElement element;
		ListElement new_element = new ListElement(value);
		if (myLength == 0) {
			myData[0] = myData[1] = new_element;
		} else if (index == myLength) {
			((ListElement)myData[1]).next = new_element;
			myData[1] = new_element;
		} else if (index == 0) {
			new_element.next = (ListElement)myData[0];
			myData[0] = new_element;
		}	else {
			element = (ListElement)myData[0];
			while (--index > 0) {
				element = element.next;
			}
			if (element.next != null) {
				new_element.next = element.next;
			}
			element.next = new_element;
		}
		myLength++;
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);		
//		myLength++;
//		} // syncObject
	}
	public void addByIndex(int index, int value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}
	public void addByIndex(int index, double value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}
	public void addByIndex(int index, boolean value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


	public Aggregate addAggregateByIndex(int index, EDefined_type select[])
			throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


	public void removeByIndex(int index) throws SdaiException {
//		synchronized (syncObject) {
		if (myType != null) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		index--;
		if (index < 0 || index >= myLength) {
			throw new SdaiException(SdaiException.IX_NVLD, this);
		}
		ListElement element;
		if (myLength == 1) {
			myData[0] = myData[1] = null;
		} else if (index == 0) {
			myData[0] = ((ListElement)myData[0]).next;
		} else if (index == myLength - 1) {
			element = (ListElement)myData[0];
			while (--index > 0) {
				element = element.next;
			}
			element.next = null;
			myData[1] = element;
		} else {
			element = (ListElement)myData[0];
			while (--index > 0) {
				element = element.next;
			}
			element.next = element.next.next;
		}
		myLength--;
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);		
//		myLength--;
//		} // syncObject
	}


//	public void addUnordered(Object value, EDefined_type select[]) throws SdaiException {
	void addUnorderedCommon(Object value) throws SdaiException {
//		synchronized (syncObject) {
		if (myType != null) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		Class bottom_class = value.getClass();
		if (CAggregate.class.isAssignableFrom(bottom_class) ||
				getClass().isAssignableFrom(bottom_class)) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		if (myData == null) {
			myData = new Object[2];
		}
		ListElement element;
		ListElement new_element = new ListElement(value);
		if (myLength == 0) {
			myData[0] = myData[1] = new_element;
		} else {
			((ListElement)myData[1]).next = new_element;
			myData[1] = new_element;
		}
		myLength++;
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
//		myLength++;
//		} // syncObject
	}
	public void addUnordered(int value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}
	public void addUnordered(double value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}
	public void addUnordered(boolean value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


/**
	An implementation of <code>add unordered</code> operation for the internal 
	use in the package. The method is invoked in:
		- <code>getAssociatedModels</code> in <code>ASchemaInstance</code>;
		- <code>addSdaiModel</code> in <code>SchemaInstance</code>;
		- several methods in <code>SdaiModel</code>;
		- several methods in <code>SdaiRepository</code>;
		- the constructor and several methods in <code>SdaiSession</code>.
*/
	protected void addUnorderedRO(Object value) throws SdaiException {
		if (myType != null && (myType.express_type == DataType.ARRAY || myType.express_type == DataType.LIST) ) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		try {
			if (myType != null && myType.express_type == DataType.SET && isMember(value, null)) {
				return;
			}
		} catch (SdaiException ex) {
		}
		EBound upper_bound = null;
		EVariable_size_aggregation_type aggr_type = (EVariable_size_aggregation_type)myType;
		if (((AggregationType)aggr_type).shift != SdaiSession.PRIVATE_AGGR) {
			if (aggr_type.testUpper_bound(null)) {
				upper_bound = aggr_type.getUpper_bound(null);
			}
			if (upper_bound != null && myLength >= upper_bound.getBound_value(null)) {
				String base = SdaiSession.line_separator + AdditionalMessages.AI_BVL;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
		if (myData == null) {
			int ln;
			if (upper_bound == null) {
				ln = Integer.MAX_VALUE;
			} else {
				ln = upper_bound.getBound_value(null);
			}
			initializeData(ln);
		}
		if (myLength >= myData.length) {						
			ensureCapacity();
		}
		myData[myLength] = value;
		myLength++;
	}


	public Aggregate createAggregateUnordered(EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


	public void removeUnordered(Object value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}
	public void removeUnordered(int value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}
	public void removeUnordered(double value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}
	public void removeUnordered(boolean value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


/**
	An implementation of <code>remove unordered</code> operation for the 
	internal use in the package. The method is invoked in:
		- <code>delete</code> and <code>removeSdaiModel</code> in 
		  <code>SchemaInstance</code>;
		- several methods in <code>SdaiModel</code>;
		- several methods in <code>SdaiRepository</code>;
		- <code>closeSession</code> in <code>SdaiSession</code>.
*/
	void removeUnorderedRO(Object value) throws SdaiException {
		int index = -1;
		if (myType != null && (myType.express_type == DataType.ARRAY || myType.express_type == DataType.LIST) ) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		for (int i = 0; i < myLength; i++) {
			if (myData[i] == value) {
				index = i;
				break;
			}
		}
		if (index >= 0) {
			myData[index] = myData[myLength - 1];
			myData[myLength - 1] = null;
			myLength--;
		}
	}


	public int testCurrentMember(SdaiIterator it, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (it.myAggregate != this) {
			String base = SdaiSession.line_separator + AdditionalMessages.IR_IMPR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (myType != null) {
			if (getOwner() == null) {
				throw new SdaiException(SdaiException.AI_NEXS);
			}
//			if (SdaiSession.session == null) {
			if (!isSessionAvailable()) {
				throw new SdaiException(SdaiException.SS_NOPN);
			}
		}
		if (select != null) {
			select[0] = null;
		}
		if (myType == null || myType.express_type == DataType.LIST) {
			if (it.myElement == null) {
				return 0;
			}
			if (((ListElement)it.myElement).object == null) {
				return 0;
			}
			return 1;
		} else {
			if (it.myIndex < 0 || it.myIndex >= myLength) {
				throw new SdaiException(SdaiException.IR_NSET, it);
			}
			if (myData[it.myIndex] == null) {
				return 0;
			} else {
				return 1;
			}
		}
//		} // syncObject
	}


	public int testCurrentMember(SdaiIterator iter) throws SdaiException {
		return testCurrentMember(iter, null);
	}


	public Object getCurrentMemberObject(SdaiIterator it) throws SdaiException {
//		synchronized (syncObject) {
		if (it.myAggregate != this) {
			String base = SdaiSession.line_separator + AdditionalMessages.IR_IMPR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (myType != null) {
			SdaiCommon owning_obj = getOwner();
			if (owning_obj == null) {
				throw new SdaiException(SdaiException.AI_NEXS);
			}
			if (owning_obj instanceof SdaiRepository && ((SdaiRepository)owning_obj).isRemote()) {
				if (this instanceof ASdaiModel) {
					return ((ASdaiModel)this).getCurrentMember(it);
				} else if (this instanceof ASchemaInstance) {
					return ((ASchemaInstance)this).getCurrentMember(it);
				}
			}
//			if (SdaiSession.session == null) {
			if (!isSessionAvailable()) {
				throw new SdaiException(SdaiException.SS_NOPN);
			}
		}
		if (myType == null || myType.express_type == DataType.LIST) {
			if (it.myElement == null) {
				throw new SdaiException(SdaiException.IR_NSET, it);
			}
			if (((ListElement)it.myElement).object == null) {
				throw new SdaiException(SdaiException.VA_NSET);
			}
			return ((ListElement)it.myElement).object;
		} else {
			if (it.myIndex < 0 || it.myIndex >= myLength) {
				throw new SdaiException(SdaiException.IR_NSET, it);
			}
			if (myData[it.myIndex] == null) {
				throw new SdaiException(SdaiException.VA_NSET);
			}
			return myData[it.myIndex];
		}
//		} // syncObject
	}
	public int getCurrentMemberInt(SdaiIterator iter) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}
	public double getCurrentMemberDouble(SdaiIterator iter) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}
	public boolean getCurrentMemberBoolean(SdaiIterator iter) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


	public Aggregate createAggregateAsCurrentMember(SdaiIterator iter, EDefined_type select[])
			throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


//	public void setCurrentMember(SdaiIterator it, Object value, EDefined_type select[])
//			throws SdaiException {
	void setCurrentMemberCommon(SdaiIterator it, Object value) throws SdaiException {
//		synchronized (syncObject) {
		if (myType != null) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		Class bottom_class = value.getClass();
		if (CAggregate.class.isAssignableFrom(bottom_class) ||
				getClass().isAssignableFrom(bottom_class)) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		if (it.myAggregate != this) {
			String base = SdaiSession.line_separator + AdditionalMessages.IR_IMPR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (it.myElement == null) {
			throw new SdaiException(SdaiException.IR_NSET, it);
		}
		((ListElement)it.myElement).object = value;
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
//		} // syncObject
	}
	public void setCurrentMember(SdaiIterator iter, int value, EDefined_type select[])
			throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}
	public void setCurrentMember(SdaiIterator iter, double value, EDefined_type select[])
			throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}
	public void setCurrentMember(SdaiIterator iter, boolean value, EDefined_type select[])
			throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


//	public void addBefore(SdaiIterator it, Object value, EDefined_type select[])
//			throws SdaiException {
	void addBeforeCommon(SdaiIterator it, Object value) throws SdaiException {
//		synchronized (syncObject) {
		if (myType != null) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		Class bottom_class = value.getClass();
		if (CAggregate.class.isAssignableFrom(bottom_class) ||
				getClass().isAssignableFrom(bottom_class)) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		if (it.myAggregate != this) {
			String base = SdaiSession.line_separator + AdditionalMessages.IR_IMPR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (it.myIndex >= 0) {
			String base = SdaiSession.line_separator + AdditionalMessages.IR_IMPR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (myData == null) {
			myData = new Object[2];
		}
		ListElement element;
		ListElement new_element = new ListElement(value);
		if (myLength == 0) {
			myData[0] = myData[1] = new_element;
			it.myElement = null;
			it.behind = true;
		} else if (it.myElement == null && it.behind) {
			((ListElement)myData[1]).next = new_element;
			myData[1] = new_element;
		} else if (it.myElement == null) {
			new_element.next = (ListElement)myData[0];
			myData[0] = new_element;
			it.myElement = new_element;
		} else if (it.myElement == (ListElement)myData[0]) {
			new_element.next = (ListElement)myData[0];
			myData[0] = new_element;
		}	else {
			element = (ListElement)myData[0];
			while (element.next != it.myElement) {
				element=element.next;
			}
			new_element.next=element.next;
			element.next = new_element;
		}
		myLength++;
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);		
//		myLength++;
//		} // syncObject
	}
	public void addBefore(SdaiIterator iter, int value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}
	public void addBefore(SdaiIterator iter, double value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}
	public void addBefore(SdaiIterator iter, boolean value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


//	public void addAfter(SdaiIterator it, Object value, EDefined_type select[])
//			throws SdaiException {
	void addAfterCommon(SdaiIterator it, Object value) throws SdaiException {
//		synchronized (syncObject) {
		if (myType != null) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		Class bottom_class = value.getClass();
		if (CAggregate.class.isAssignableFrom(bottom_class) ||
				getClass().isAssignableFrom(bottom_class)) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		if (it.myAggregate != this) {
			String base = SdaiSession.line_separator + AdditionalMessages.IR_IMPR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (it.myIndex >= 0) {
			String base = SdaiSession.line_separator + AdditionalMessages.IR_IMPR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (myData == null) {
			myData = new Object[2];
		}
		ListElement element;
		ListElement new_element = new ListElement(value);
		if (myLength == 0) {
			myData[0] = myData[1] = new_element;
			it.myElement = null;
			it.behind = false;
		} else if (it.myElement == null && it.behind) {
			((ListElement)myData[1]).next = new_element;
			myData[1] = new_element;
			it.myElement = new_element;
		} else if (it.myElement == null) {
			new_element.next = (ListElement)myData[0];
			myData[0] = new_element;
		} else if (it.myElement == (ListElement)myData[1]) {
			((ListElement)myData[1]).next = new_element;
			myData[1] = new_element;
		}	else {
			new_element.next = ((ListElement)it.myElement).next;
			((ListElement)it.myElement).next = new_element;
		}
		myLength++;
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);		
//		myLength++;
//		} // syncObject
	}
	public void addAfter(SdaiIterator iter, int value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}
	public void addAfter(SdaiIterator iter, double value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}
	public void addAfter(SdaiIterator iter, boolean value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


	public Aggregate createAggregateBefore(SdaiIterator iter, EDefined_type select[])
			throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


	public Aggregate createAggregateAfter(SdaiIterator iter, EDefined_type select[])
			throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


	public void clear() throws SdaiException {
//		synchronized (syncObject) {
		if (myType != null) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		myLength = 0;
		if (myData != null) {
			ListElement element = (ListElement)myData[0];
			if (element != null) {
				element.object = null;
				element.next = null;
			}
		}
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
//		} // syncObject
	}


	public EAggregation_type getAggregationType() throws SdaiException {
		return myType;
	}


/**
	Writes a string to a byte array. The last index value in this array used 
	for writing the string is returned.
	This method is invoked in <code>getAsString</code> method in classes 
	extending the current class. 
*/
	int write_string(String str, int index) {
		StaticFields staticFields = StaticFields.get();
		for (int i = 0; i < str.length(); i++) {
			staticFields.instance_as_string[++index] = (byte)str.charAt(i);
		}
		return index;
	}


/**
	Initializes this aggregate: allocates memory for its elements and fixes 
	its length. Invoked in this class and in <code>SdaiIterator</code>.
*/
	void initializeData(int bound) {
		if (myData != null) {
			return;
		}
		myLength = 0;
		if (bound == Integer.MAX_VALUE) {
			myData = new Object[INIT_SIZE_SET_SESSION];
		} else {
			myData = new Object[bound];
			if (myType.express_type == DataType.ARRAY) {
				myLength = bound;
			}
		}
	}


/**
	Enlarges the memory allocated for aggregate members twice. 
*/
	private void ensureCapacity() {
		int new_length = myData.length * 2;
		Object[] new_array = new Object[new_length];
		System.arraycopy(myData, 0, new_array, 0, myLength);
		myData = new_array;
	}
	void ensureCapacity(int req) {
		int new_length = myData.length * 2;
		if (req > new_length) {
			new_length = req;
		}
		Object[] new_array = new Object[new_length];
		System.arraycopy(myData, 0, new_array, 0, myLength);
		myData = new_array;
	}


/**
	Increases the size of the auxiliary array 'instance_as_string' either 
	twice or to satisfy the demand requested whichever of the alternatives 
	leads to a bigger array. This method is used in extensions of this class: 
	<code>ASdaiModel</code>, <code>ASchemaInstance</code>, 
	<code>ASdaiRepository</code>, <code>AEntityExtent</code>.
*/
	protected void enlarge_instance_string(int str_length, int demand) {
		StaticFields staticFields = StaticFields.get();
		int new_length = staticFields.instance_as_string.length * 2;
		if (new_length < demand) {
			new_length = demand;
		}
		byte new_instance_as_string[] = new byte[new_length];
		System.arraycopy(staticFields.instance_as_string, 0, new_instance_as_string, 0, str_length);
		staticFields.instance_as_string = new_instance_as_string;
	}


	boolean isSessionAvailable() {
		Object owning_obj = getOwner();
		if (owning_obj == null) {
			return false;
		}
		if (owning_obj instanceof SdaiModel) {
			SdaiModel model = (SdaiModel)owning_obj;
			return model.repository != null ? model.repository.session != null : false;
		} else if (owning_obj instanceof SchemaInstance) {
			SchemaInstance schInst = (SchemaInstance)owning_obj;
			return schInst.repository != null ? schInst.repository.session != null : false;
		} else if (owning_obj instanceof SdaiRepository) {
			return ((SdaiRepository)owning_obj).session != null;
		} else if (owning_obj instanceof SdaiSession) {
			return ((SdaiSession)owning_obj).opened;
		} else if (owning_obj == GENERIC_OWNER) {
			return SdaiSession.isSessionAvailable();
		} else {
			return false;
		}
	}

// 	SdaiSession get_session() throws SdaiException {
// 		Object owning_obj = getOwner();
// 		if (owning_obj == null) {
// 			return null;
// 		}
// 		if (owning_obj instanceof SdaiModel) {
// 			return ((SdaiModel)owning_obj).repository.session;
// 		} else if (owning_obj instanceof SchemaInstance) {
// 			return ((SchemaInstance)owning_obj).repository.session;
// 		} else if (owning_obj instanceof SdaiRepository) {
// 			return ((SdaiRepository)owning_obj).session;
// 		} else if (owning_obj instanceof SdaiSession) {
// 			return (SdaiSession)owning_obj;
// 		}
// 		return null;
// 	}

}
