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
 * This class is used to store a reference to a remote model.
 * It contains a method to get a reference to a remote repository.
 *
 * @author <a href="mailto:vytautas.vaitkevicius@lksoft.lt">Vytautas Vaitkevicius</a>
 * @version $Revision$
 */

public final class SdaiModelRef implements SerializableRef {

/**
 * A database id of the remote repository which contains the referenced remote model.
 */
    public final long repositoryId;
/**
 * A database id of a remote model.
 */
    public final long modelId;

/**
 * <p>Creates a reference to a remote model using specified database ids of a remote repository
 * and a remote model.</p>
 * <p>Note that users should not use this constructor, but may obtain <code>SdaiModelRef</code> object
 * using the {@link EEntityRef#getSdaiModelRef() EEntityRef.getSdaiModelRef()} method instead.</p>
 */
	public SdaiModelRef(long repositoryId, long modelId) {
        this.repositoryId = repositoryId;
        this.modelId = modelId;
    }

/**
 * <p>Creates a reference to a remote model using a specified reference to a remote repository
 * and a database id of a remote model.</p>
 * <p>Note that users should not use this constructor, but may obtain <code>SdaiModelRef</code> object
 * using the {@link EEntityRef#getSdaiModelRef() EEntityRef.getSdaiModelRef()} method instead.</p>
 */
    public SdaiModelRef(SdaiRepositoryRef repositoryRef, long modelId) {
        this.repositoryId = repositoryRef.repositoryId;
        this.modelId = modelId;
    }
	
    
/**
 * Returns a reference to the remote repository which contains the referenced remote model.
 * @return <code>SdaiRepositoryRef</code> a reference to a remote repository.
 * @see SchemaInstanceRef#getRepositoryRef()
 * @see EEntityRef#getRepositoryRef()
 */
    public SdaiRepositoryRef getRepositoryRef() {
        return new SdaiRepositoryRef(repositoryId);
    }
    
	public int getType() {
		return SDAI_MODEL_REF;
	}

	public long getRepositoryId() {
		return repositoryId;
	}

    public long getModelId() {
		return modelId;
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
		return new HashCodeBuilder(3559, 9091).append(repositoryId).append(modelId).toHashCode();
	}

	public boolean equals(Object obj) {
		if(!(obj instanceof SdaiModelRef)) {
			return false;
		}
		SdaiModelRef rhs = (SdaiModelRef)obj;
		return new EqualsBuilder()
                 .append(repositoryId, rhs.repositoryId)
                 .append(modelId, rhs.modelId)
                 .isEquals();
	}

	public String toString() {
		return new ToStringBuilder(this)
			.append("repositoryId", repositoryId)
			.append("modelId", modelId)
			.toString();
	}

}
