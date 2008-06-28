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

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class is used to store a reference to a remote repository.
 *
 * @author <a href="mailto:vytautas.vaitkevicius@lksoft.lt">Vytautas Vaitkevicius</a>
 * @version $Revision$
 */

public final class SdaiRepositoryRef implements SerializableRef {

/**
 * A database id of a remote repository.
 */
    public final long repositoryId;
    
/**
 * <p>Creates a reference to a remote repository using the specified database id of a remote repository.</p>
 * <p>Note that users should not use this constructor, but may obtain <code>SdaiRepositoryRef</code> object
 * using the {@link EEntityRef#getRepositoryRef() EEntityRef.getRepositoryRef()} method instead.</p>
 */
	public SdaiRepositoryRef(long repositoryId) {
        this.repositoryId = repositoryId;
    }

	public int getType() {
		return SDAI_REPOSITORY_REF;
	}

	public long getRepositoryId() {
		return repositoryId;
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
		return null;
	}

	public int hashCode() {
		return new HashCodeBuilder(3727, 9043).append(repositoryId).toHashCode();
	}

	public boolean equals(Object obj) {
		if(!(obj instanceof SdaiRepositoryRef)) {
			return false;
		}
		SdaiRepositoryRef rhs = (SdaiRepositoryRef)obj;
		return repositoryId == rhs.repositoryId;
	}

	public String toString() {
		return new ToStringBuilder(this).append("repositoryId", repositoryId).toString();
	}

}
