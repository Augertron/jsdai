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

package jsdai.express_g.util.repocopy;

import jsdai.lang.AEntity;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;

/**
 * @author Mantas Balnys
 *
 */
public class IndependentSdaiIterator {
	private EEntity[] entities = {};
	int index = 0;

	public IndependentSdaiIterator() {
	}

	public IndependentSdaiIterator(AEntity entities) throws SdaiException {
		this();
		setEntities(entities);
	}
	
	public void setEntities(AEntity entities) throws SdaiException {
		this.entities = new EEntity[entities.getMemberCount()];
		SdaiIterator sit = entities.createIterator();
		int i = 0;
		while ((i < this.entities.length)&&(sit.next())) {
			this.entities[i] = entities.getCurrentMemberEntity(sit);
			i++;
		}
		reset();
	}
	
	public EEntity[] getEntities() {
		if (entities == null)
			return new EEntity[0];
		else
			return (EEntity[])entities.clone();
	}
	
	/**
	 * iterator is set to the starting item
	 *
	 */
	public void reset() {
		index = 0;
	}

	/**
	 * 
	 */
	public boolean hasNext() {
		return index < entities.length;
	}

	/**
	 * 
	 */
	public EEntity next() {
		if (hasNext()) {
			EEntity current = entities[index];
			index++;
			return current;
		} else {
			return null;
		}
	}

}
