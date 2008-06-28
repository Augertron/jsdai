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
 * This class is used to store a reference to a remote entity instance.
 * It contains methods to get a reference to a remote repository and a remote model.
 *
 * @author <a href="mailto:vytautas.vaitkevicius@lksoft.lt">Vytautas Vaitkevicius</a>
 * @version $Revision$
 */

public final class EEntityRef implements SerializableRef {

/**
 * A database id of the remote repository which contains the referenced entity instance.
 */
    public final long repositoryId;
/**
 * A database id of the remote model which contains the referenced entity instance.
 */
    public final long modelId;
/**
 * A persistent label of a remote entity instance
 */
    public final long persistentLabel;

    transient private final int typeIndex;

/**
 * <p>Creates a reference to a remote entity instance using specified database ids of a remote repository
 * and a remote model and a persistent label of a remote entity instance.</p>
 * <p>Note that users should not use this constructor, but may obtain <code>EEntityRef</code> object
 * using the {@link jsdai.lang.QueryResultSet#getItem(int) jsdai.lang.QueryResultSet.getItem(int)} method instead,
 * if the query result set contains references to entity instances.</p>
 */
	public EEntityRef(long repositoryId, long modelId, long persistentLabel, int typeIndex) {
        this.repositoryId = repositoryId;
        this.modelId = modelId;
        this.persistentLabel = persistentLabel;
		this.typeIndex = typeIndex;
	}

/**
 * <p>Creates a reference to a remote entity instance using specified database ids of a remote repository
 * and a remote model and a persistent label of a remote entity instance.</p>
 * <p>Note that users should not use this constructor, but may obtain <code>EEntityRef</code> object
 * using the {@link jsdai.lang.QueryResultSet#getItem(int) jsdai.lang.QueryResultSet.getItem(int)} method instead,
 * if the query result set contains references to entity instances.</p>
 */
	public EEntityRef(long repositoryId, long modelId, long persistentLabel) {
        this.repositoryId = repositoryId;
        this.modelId = modelId;
        this.persistentLabel = persistentLabel;
		this.typeIndex = -1;
    }
    
/**
 * <p>Creates a reference to a remote entity instance using specified references to a remote repository
 * and a remote model and a persistent label of a remote entity instance.</p>
 * <p>Note that users should not use this constructor, but may obtain <code>EEntityRef</code> object
 * using the {@link jsdai.lang.QueryResultSet#getItem(int) jsdai.lang.QueryResultSet.getItem(int)} method instead,
 * if the query result set contains references to entity instances.</p>
 */
    public EEntityRef(SdaiRepositoryRef repositoryRef, SdaiModelRef modelRef, long persistentLabel) {
        this.repositoryId = repositoryRef.repositoryId;
        this.modelId = modelRef.modelId;
        this.persistentLabel = persistentLabel;
		this.typeIndex = -1;
    }

/**
 * Returns a reference to the remote repository which contains the referenced remote entity instance.
 * @return <code>SdaiRepositoryRef</code> a reference to a remote repository.
 * @see SchemaInstanceRef#getRepositoryRef()
 * @see SdaiModelRef#getRepositoryRef()
 */
    public SdaiRepositoryRef getRepositoryRef() {
        return new SdaiRepositoryRef(repositoryId);
    }

/**
 * Returns a reference to the remote model which contains the referenced remote entity instance.
 * @return <code>SdaiModelRef</code> a reference to a remote model.
 * @see #getRepositoryRef()
 */
    public SdaiModelRef getSdaiModelRef() {
        return new SdaiModelRef(repositoryId, modelId);
    }

	public int getType() {
		return EENTITY_REF;
	}

	public long getRepositoryId() {
		return repositoryId;
	}

    public long getModelId() {
		return modelId;
	}

    public long getPersistentLabel() {
		return persistentLabel;
	}

    public int getTypeIndex() {
		return typeIndex;
	}

    public long getSchemaInstanceId() {
		return -1;
	}

	public SerializableRef[] getMembers() {
		return null;
	}

	public int hashCode() {
		return new HashCodeBuilder(3593, 9151)
			.append(repositoryId)
			.append(modelId)
			.append(persistentLabel)
			.toHashCode();
	}

	public boolean equals(Object obj) {
		if(!(obj instanceof EEntityRef)) {
			return false;
		}
		EEntityRef rhs = (EEntityRef)obj;
		return new EqualsBuilder()
                 .append(repositoryId, rhs.repositoryId)
                 .append(modelId, rhs.modelId)
                 .append(persistentLabel, rhs.persistentLabel)
                 .isEquals();
	}

	public String toString() {
		return new ToStringBuilder(this)
			.append("repositoryId", repositoryId)
			.append("modelId", modelId)
			.append("persistentLabel", persistentLabel)
			.toString();
	}

}
