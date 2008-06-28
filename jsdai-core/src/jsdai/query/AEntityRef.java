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

package jsdai.query;

import java.util.Collection;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * Created: Tue Sep 30 15:12:54 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public final class AEntityRef implements SerializableRef {

    public final EEntityRef memberEntityRefs[];
	
	public AEntityRef(int size) {
		memberEntityRefs = new EEntityRef[size];
	}
	
	public AEntityRef(EEntityRef entityRefs[]) {
		memberEntityRefs = entityRefs;
	}
	
	public AEntityRef(Collection entityRefList) {
		memberEntityRefs = (EEntityRef[])entityRefList.toArray(new EEntityRef[entityRefList.size()]);
	}

	public int getType() {
		return AENTITY_REF;
	}

	public long getRepositoryId() {
		return -1;
	}

    public long getModelId() {
		return -1;
	}

    public long getPersistentLabel() {
		return -1;
	}

    public int getTypeIndex() {
		return -1;
	}

    public long getSchemaInstanceId() {
		return -1;
	}

	public SerializableRef[] getMembers() {
		return memberEntityRefs;
	}

	public int hashCode() {
		return new HashCodeBuilder(3613, 9161)
			.append(memberEntityRefs)
			.toHashCode();
	}

	public boolean equals(Object obj) {
		if(!(obj instanceof AEntityRef)) {
			return false;
		}
		AEntityRef rhs = (AEntityRef)obj;
		return new EqualsBuilder()
                 .append(memberEntityRefs, rhs.memberEntityRefs)
                 .isEquals();
	}

	public String toString() {
		return new ToStringBuilder(this)
			.append("memberEntityRefs", memberEntityRefs)
			.toString();
	}

} // AEntityRef
