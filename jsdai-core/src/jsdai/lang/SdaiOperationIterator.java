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

import jsdai.dictionary.EEntity_definition;

/**
 * <p>Implicit supertype for all classes aiding to run through entity
 * instances for which some changes (due to their modification, creation,
 * deletion or substitution) were recorded in the log file for
 * subsequent undo/redo operations. The records are divided into segments
 * called groups. The classes that implement this interface
 * are designed to iterate over instances described by records of one group.
 * </p>
 * <p>The interface is not intended to be implemented by the user.</p>
 * @author vaidas
 * @version $Revision$
 * @since 4.1.0
 */
public interface SdaiOperationIterator {

	int CREATE_OPERATION = 1;
	int DELETE_OPERATION = 2;
	int MODIFY_OPERATION = 3;
	int SUBSTITUTE_OPERATION = 4;
	/** @since 4.2.0 */
	int CREATE_EXTERNAL_DATA_OPERATION = 5;
	/** @since 4.2.0 */
	int REMOVE_EXTERNAL_DATA_OPERATION = 6;


/**
 * Positions this iterator to reference the next entity instance in the sequence
 * of instances for which records in undo/redo log file were created and
 * combined to form a separate group.
 * If the iterator currently references the last instance in this sequence,
 * or is at the end of it and thus has no current member, or this sequence is
 * empty, then the iterator is positioned to the end of the sequence
 * of affected instances without referencing any of them.
 * There are the following two cases:
 * <ul>
 * <li>   iterating records for undo operation; in this case, the records of
 * a group are searched in the backward direction, and the first instance in
 * the above defined sequence corresponds to the last record in the group;
 * <li>   iterating records for redo operation; in this case, the records of
 * a group are searched in the forward direction.
 * </ul>
 * @return <code>true</code> if iterator has a new current member (instance),
 * <code>false</code> otherwise.
 * @throws SdaiException SY_ERR, an underlying system error occurred.
 */
	boolean next() throws SdaiException;


/**
 * Returns an indicator specifying the type of changes performed
 * on the entity instance at hand. The specific constants indicating
 * the supported types are as follows:
 * <ul>
 * <li>   1 for CREATE_OPERATION;
 * <li>   2 for DELETE_OPERATION;
 * <li>   3 for MODIFY_OPERATION;
 * <li>   4 for SUBSTITUTE_OPERATION.
 * <li>   5 for CREATE_EXTERNAL_DATA_OPERATION.
 * <li>   6 for REMOVE_EXTERNAL_DATA_OPERATION.
 * </ul>
 * @return an integer constant specifying the type of operation.
 */
	int getOperationType() throws SdaiException;


/**
 * Returns <code>SdaiModel</code> that is the owner of the entity instance
 * on which an operation was performed.
 * In the case of the substitute instance operation, this <code>SdaiModel</code> owns
 * the new entity instance (substitute), not the old one (which is deleted).
 * @return the owner of the modified (or created, or deleted) entity instance.
 */
	SdaiModel getOperationModel() throws SdaiException;

/**
 * Returns <code>SdaiRepository</code> that contains the entity instance
 * on which an operation was performed.
 * The repository is available only for <code>CREATE_EXTERNAL_DATA_OPERATION</code>
 * and <code>REMOVE_EXTERNAL_DATA_OPERATION</code>.
 * @return the owning <code>SdaiRepository</code> of the entity instance or <code>null</code>
 *         if repository is not known.
 * @since 4.2.0
 */
	SdaiRepository getOperationRepository() throws SdaiException;

/**
 * Returns persistent label of the entity instance on which an operation was performed.
 * In the case of the substitute instance operation, this label stands for
 * the new instance (substitute), not for the old one (which is deleted).
 * If iterator has no current member, then empty string is returned.
 * @return persistent label of the modified (or created, or deleted) entity instance.
 */
	String getOperationInstanceLabel() throws SdaiException;


/**
 * Returns the identifier of the entity instance on which an operation was performed.
 * In the case of the substitute instance operation, this identifier stands for
 * the new instance (substitute), not for the old one (which is deleted).
 * @return ID of the modified (or created, or deleted) entity instance.
 */
	long getOperationInstanceId() throws SdaiException;


/**
 * Returns entity data type of the instance on which an operation was performed.
 * In the case of the substitute instance operation, this data type stands for
 * the new instance (substitute), not for the old one (which is deleted).
 * @return entity definition of the modified (or created, or deleted) application instance.
 */
	EEntity_definition getOperationInstanceType() throws SdaiException;


/**
 * Returns persistent label of the entity instance for which substitute instance
 * operation was performed (that is, this instance was substituted with
 * another one, possibly having different entity definition).
 * If iterator has no current member, then empty string is returned.
 * @return persistent label of the substituted entity instance.
 */
	String getOperationPrevInstanceLabel() throws SdaiException;


/**
 * Returns the identifier of the entity instance for which substitute instance
 * operation was performed (that is, this instance was substituted with
 * another one, possibly having different entity definition).
 * @return ID of the substituted entity instance.
 */
	long getOperationPrevInstanceId() throws SdaiException;


/**
 * Returns entity data type of the entity instance for which substitute instance
 * operation was performed (that is, this instance was substituted with another one).
 * @return entity definition of the substituted application instance.
 */
	EEntity_definition getOperationPrevInstanceType() throws SdaiException;


/**
 * Returns <code>SdaiModel</code> that is the owner of the entity instance
 * for which substitute instance operation was performed (that is,
 * this instance was substituted with another one).
 * @return the owner of the substituted entity instance.
 */
	SdaiModel getOperationPrevModel() throws SdaiException;
}
