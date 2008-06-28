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
 * This class is used to store a reference to a remote schema instance.
 * It contains a method to get a reference to a remote repository.
 *
 * @author <a href="mailto:vytautas.vaitkevicius@lksoft.lt">Vytautas Vaitkevicius</a>
 * @version $Revision$
 */

public final class SchemaInstanceRef implements SerializableRef {

/**
 * A database id of the remote repository which contains the referenced remote schema instance.
 */
    public final long repositoryId;
/**
 * A database id of a remote schema instance.
 */
    public final long schemaInstanceId;
    
/**
 * <p>Creates a reference to a remote schema instance using specified database ids of a remote repository
 * and a remote schema instance.</p>
 * <p>Note that users should not use this constructor.</p>
 */
	public SchemaInstanceRef(long repositoryId, long schemaInstanceId) {
        this.repositoryId = repositoryId;
        this.schemaInstanceId = schemaInstanceId;
    }
    
/**
 * <p>Creates a reference to a remote schema instance using a specified reference to a remote repository
 * and a database id of a remote schema instance.</p>
 * <p>Note that users should not use this constructor.</p>
 */
    public SchemaInstanceRef(SdaiRepositoryRef repositoryRef, long schemaInstanceId) {
        this.repositoryId = repositoryRef.repositoryId;
        this.schemaInstanceId = schemaInstanceId;
    }
	
/**
 * Returns a reference to the remote repository which contains the referenced remote schema instance.
 * @return <code>SdaiRepositoryRef</code> a reference to a remote repository.
 * @see SdaiModelRef#getRepositoryRef()
 * @see EEntityRef#getRepositoryRef()
 */
    public SdaiRepositoryRef getRepositoryRef() {
        return new SdaiRepositoryRef(repositoryId);
    }

	public int getType() {
		return SCHEMA_INSTANCE_REF;
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
		return schemaInstanceId;
	}

	public SerializableRef[] getMembers() {
		return null;
	}

	public int hashCode() {
		return new HashCodeBuilder(3581, 9133).append(repositoryId).append(schemaInstanceId).toHashCode();
	}

	public boolean equals(Object obj) {
		if(!(obj instanceof SchemaInstanceRef)) {
			return false;
		}
		SchemaInstanceRef rhs = (SchemaInstanceRef)obj;
		return new EqualsBuilder()
                 .append(repositoryId, rhs.repositoryId)
                 .append(schemaInstanceId, rhs.schemaInstanceId)
                 .isEquals();
	}

	public String toString() {
		return new ToStringBuilder(this)
			.append("repositoryId", repositoryId)
			.append("schemaInstanceId", schemaInstanceId)
			.toString();
	}

}
