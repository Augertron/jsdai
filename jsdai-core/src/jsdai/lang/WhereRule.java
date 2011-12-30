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

/** This is for internal JSDAI use only. Applications shall not use this */
public abstract class WhereRule extends CEntity implements EWhere_rule {

	CEntity viol_instances [];
	int aggr_size;
	boolean empty_aggr;
	static final int NUMBER_OF_INSTANCES = 16;

//	Exception exc;
	Throwable exc;

	protected WhereRule() {
		super();
		viol_instances = null;
	}


/**
 * Returns entity instances aggregate for which the Express expression 
 * SIZEOF(that entity instances aggregate) = d is violated, where d is 
 * a nonnegative integer value. If d=0, then the method's parameter shall 
 * be set to <code>true</code>, otherwise null is returned.
 * Also null is returned when the above equality is satisfied. 
 * @param zero_case <code>true</code> if SIZEOF value is required to be zero, 
 * <code>false</code> otherwise.
 * @return aggregate of entity instances.
 * @since 4.1.0
 */
 	public AEntity getInstancesSizeof(boolean zero_case) throws SdaiException {
 		int i;
 		AEntity aggr;
 		SdaiIterator it_insts;
 		if (zero_case) {
 			if (!empty_aggr || aggr_size == 0) {
 				return null;
 			}
 			aggr = new AEntity();
 			it_insts = aggr.createIterator();
			i = 0;
			while (i < aggr_size) {
				aggr.addAfter(it_insts, viol_instances[i]);
				it_insts.next();
				i++;
			}
			return aggr;
 		}
 		if (empty_aggr || aggr_size == 0) {
 			return null;
 		}
 		aggr = new AEntity();
 		it_insts = aggr.createIterator();
		i = 0;
		while (i < aggr_size) {
			aggr.addAfter(it_insts, viol_instances[i]);
			it_insts.next();
			i++;
		}
		return aggr;
 	}


	void store_to_array(CEntity [] aggr, int size, boolean vers) {
		if (viol_instances == null) {
			if (size <= NUMBER_OF_INSTANCES) {
				viol_instances = new CEntity[NUMBER_OF_INSTANCES];
			} else {
				viol_instances = new CEntity[size];
			}
		} else if (viol_instances.length < size) {
			enlarge_inst_array(size);
		}
		for (int i = 0; i < size; i++) {
			viol_instances[i] = aggr[i];
		}
		aggr_size = size;
		empty_aggr = vers;
	}


	boolean isRuleBypassed(String [] bp_rules_names, int br_count) throws SdaiException {
		int i;
		CWhere_rule wrule = (CWhere_rule)this;
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


	private void enlarge_inst_array(int demand) {
		int new_length = viol_instances.length * 2;
		if (new_length < demand) {
			new_length = demand;
		}
		viol_instances = new CEntity[new_length];
	}


/**
 * Returns <code>Throwable</code> if during validation of this where rule an 
 * error in Express expressions or in the population submitted for validation 
 * was found, and null otherwise.
 * @return <code>Throwable</code> in the case if error occurred; null otherwise.
 */
//	public Exception getError() {
   public Throwable getError() {
		return exc;
	}


}
