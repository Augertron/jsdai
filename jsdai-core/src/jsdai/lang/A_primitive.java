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

/** This is for internal JSDAI use only. 
Applications shall use A_integer or A_double.
*/

/* Implementation of Aggregate for primitive java types as a variable-size array or a list */
abstract class A_primitive extends A_basis implements Aggregate {

	Object myData;
	int myLength;
	AggregationType myType;



	abstract void setValue(AggregationType aggr_type, CEntity inst, Value val, 
			boolean mixed, boolean inverse) throws SdaiException;


	CEntity getOwningInstance() {
		SdaiCommon owner = this.owner;
		while (owner instanceof Aggregate || owner instanceof SdaiListenerElement) {
//System.out.println("  CAggregate  owner: " + owner.getClass().getName());
			owner = owner.getOwner();
		}
		return (owner instanceof CEntity) ? ((CEntity)owner) : null;
	}

	void modified() throws SdaiException {
		owner.modified();
	}


	protected A_primitive() {
	}

	protected A_primitive(AggregationType provided_type, CEntity agg_owner) throws SdaiException {
		myType = provided_type;
		owner = agg_owner;
		if (myType.express_type == DataType.ARRAY) {
			CArray_type arr_type = (CArray_type)myType;
			myLength = arr_type.getUpper_index(null).getBound_value(null) - 
				arr_type.getLower_index(null).getBound_value(null) + 1;
		}
	}




	void attach(EAggregation_type provided_type, CEntity instance) throws SdaiException {
		myType = (AggregationType)provided_type;
		owner = instance;
		if (myType.express_type == DataType.ARRAY) {
			CArray_type arr_type = (CArray_type)myType;
			myLength = arr_type.getUpper_index(null).getBound_value(null) - 
				arr_type.getLower_index(null).getBound_value(null) + 1;
		}
	}


	public int getMemberCount() throws SdaiException {
//		synchronized (syncObject) {
		if (myType != null && myType.shift != SdaiSession.PRIVATE_AGGR) {
			if (owner == null) {
				throw new SdaiException(SdaiException.AI_NEXS);
			}
			SdaiModel owning_model = getOwningInstance().owning_model;
			if (owning_model == null) {
				throw new SdaiException(SdaiException.EI_NEXS);
			}
			if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
				throw new SdaiException(SdaiException.MX_NDEF, owning_model);
			}
		}
		return myLength;
//		} // syncObject
	}


	public boolean isMember(Object value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}
	public boolean isMember(int value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}
	public boolean isMember(double value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}
	public boolean isMember(boolean value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}


	public SdaiIterator createIterator() throws SdaiException {
//		synchronized (syncObject) {
		 if (myType != null && myType.shift != SdaiSession.PRIVATE_AGGR) {
			if (owner == null) {
				throw new SdaiException(SdaiException.AI_NEXS);
			}
			SdaiModel owning_model = getOwningInstance().owning_model;
			if (owning_model == null) {
				throw new SdaiException(SdaiException.EI_NEXS);
			}
			if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
				throw new SdaiException(SdaiException.MX_NDEF, owning_model);
			}
		}
		return new SdaiIterator(this);
//		} // syncObject
	}


	public void attachIterator(SdaiIterator iter) throws SdaiException {
//		synchronized (syncObject) {
		if (iter == null) {
			throw new SdaiException(SdaiException.IR_NEXS);
		}
		iter.myAggregate = this;
		if (myType != null && myType.shift != SdaiSession.PRIVATE_AGGR) {
			if (owner == null) {
				throw new SdaiException(SdaiException.AI_NEXS);
			}
			SdaiModel owning_model = getOwningInstance().owning_model;
			if (owning_model == null) {
				throw new SdaiException(SdaiException.EI_NEXS);
			}
			if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
				throw new SdaiException(SdaiException.MX_NDEF, owning_model);
			}
		}
		iter.myIndex = -1;
		iter.behind = false;
		if (this instanceof A_integerPrimitive) {
			iter.AggregationType = SdaiIterator.INTEGER_AGGR;
		} else {
			iter.AggregationType = SdaiIterator.DOUBLE_AGGR;
		}
//		} // syncObject
	}


	public int getLowerBound() throws SdaiException {
		throw new SdaiException(SdaiException.EX_NSUP);
	}


	public int getUpperBound() throws SdaiException {
		throw new SdaiException(SdaiException.EX_NSUP);
	}


	public int query(String where, EEntity entity, AEntity result)
			throws SdaiException {
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

	public Object getByIndexObject(int index) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}
	public int getByIndexInt(int index) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}
	public double getByIndexDouble(int index) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}
	public boolean getByIndexBoolean(int index) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}


	public int getValueBoundByIndex(int index) throws SdaiException {
		throw new SdaiException(SdaiException.EX_NSUP);
	}


	public void setByIndex(int index, Object value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}
	public void setByIndex(int index, int value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}
	public void setByIndex(int index, double value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}
	public void setByIndex(int index, boolean value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}


	public Aggregate createAggregateByIndex(int index, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}


//	public int testByIndex(int index, EDefined_type select[]) throws SdaiException {
//		throw new SdaiException(SdaiException.VT_NVLD);
//	}


	public int getLowerIndex() throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


	public int getUpperIndex() throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


	public void reindexArray() throws SdaiException {
		throw new SdaiException(SdaiException.EX_NSUP);
	}


	public void resetArrayIndex(int lower, int upper) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


	public void addByIndex(int index, Object value, EDefined_type select[])
			throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}
	public void addByIndex(int index, int value, EDefined_type select[])
			throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}
	public void addByIndex(int index, double value, EDefined_type select[])
			throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}
	public void addByIndex(int index, boolean value, EDefined_type select[])
			throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}


	public Aggregate addAggregateByIndex(int index, EDefined_type select[])
		throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}


	public void addUnordered(Object value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}
	public void addUnordered(int value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}
	public void addUnordered(double value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}
	public void addUnordered(boolean value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}


	public Aggregate createAggregateUnordered(EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}


	public void removeUnordered(Object value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}
	public void removeUnordered(int value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}
	public void removeUnordered(double value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}
	public void removeUnordered(boolean value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}


//	public int testCurrentMember(SdaiIterator iter, EDefined_type select[]) throws SdaiException {
//		throw new SdaiException(SdaiException.VT_NVLD);
//	}


	public Object getCurrentMemberObject(SdaiIterator iter) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}
	public int getCurrentMemberInt(SdaiIterator iter) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}
	public double getCurrentMemberDouble(SdaiIterator iter) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}
	public boolean getCurrentMemberBoolean(SdaiIterator iter) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}


	public Aggregate createAggregateAsCurrentMember(SdaiIterator it,
			EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}


	public void setCurrentMember(SdaiIterator it, Object value, EDefined_type select[])
			throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}
	public void setCurrentMember(SdaiIterator it, int value, EDefined_type select[])
			throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}
	public void setCurrentMember(SdaiIterator it, double value, EDefined_type select[])
			throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}
	public void setCurrentMember(SdaiIterator it, boolean value, EDefined_type select[])
			throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}


	public void addBefore(SdaiIterator it, Object value, EDefined_type select[])
			throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}
	public void addBefore(SdaiIterator it, int value, EDefined_type select[])
			throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}
	public void addBefore(SdaiIterator it, double value, EDefined_type select[])
			throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}
	public void addBefore(SdaiIterator it, boolean value, EDefined_type select[])
			throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}


	public void addAfter(SdaiIterator it, Object value, EDefined_type select[])
			throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}
	public void addAfter(SdaiIterator it, int value, EDefined_type select[])
			throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}
	public void addAfter(SdaiIterator it, double value, EDefined_type select[])
			throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}
	public void addAfter(SdaiIterator it, boolean value, EDefined_type select[])
			throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}


	public Aggregate createAggregateBefore(SdaiIterator it, EDefined_type select[])
			throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}


	public Aggregate createAggregateAfter(SdaiIterator it, EDefined_type select[])
			throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}


	public EAggregation_type getAggregationType() throws SdaiException {
		return myType;
	}


	public void unsetAll() throws SdaiException {
	}


	void enlarge_instance_string(StaticFields staticFields, int str_length, int demand) {
		int new_length = staticFields.instance_as_string.length * 2;
		if (new_length < demand) {
			new_length = demand;
		}
		byte new_instance_as_string[] = new byte[new_length];
		System.arraycopy(staticFields.instance_as_string, 0, new_instance_as_string, 0, str_length);
		staticFields.instance_as_string = new_instance_as_string;
	}

}
