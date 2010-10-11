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

import jsdai.query.QueryLibProvider;

/**
 * Transactions control operations performed during the session.
 * JSDAI conforms to transaction level 3, see
 * "ISO 10303-22::13.1.1 Levels of transaction" for its definition.
 * Therefore, an application always has to start either a read-only or read-write
 * transaction before the contents of any <code>SdaiModel</code> is accessed.
 * When a transaction is committed, the data which were changed are written
 * to the binary files in the appropriate directories. In the case when
 * a transaction is aborted, the previous state of all open
 * <code>SdaiRepositories</code> with <code>SdaiModel</code>s and their instances
 * inside them is restored. Entity instances created after the most recent
 * start transaction with read-write access or commit operation performed
 * become invalid.
 * <BR>For details please look at "ISO 10303-22::7.4.5 sdai_transaction".
 * <h2>Remote database transactions</h2>
 * <p align="right"><font size="-2">Last update 2006-01-25</font></p>
 * <h3>Introduction</h3>
 * <code>SdaiTransactions</code> have some differences from SDAI transactions in ISO 10303-22
 * when used with remote JSDAI-DB repositories. The reason for this is multiuser
 * user concurrent environment and underlying RDBMS specifics.
 * <h3>Transaction differences from SDAI</h3>
 * <ul>
 * <li>Remote repository operations is part of SDAI transaction. This includes 
 * {@link SdaiSession#createRepository}, {@link SdaiSession#importClearTextEncoding}
 * {@link SdaiRepository#openRepository}, and {@link SdaiRepository#closeRepository}.
 * This is related to the fact that all changes in RDBMS are transaction oriented. For backward
 * compatibility if transaction is not running while the above methods are invoked the appropriate
 * transaction is started and committed afterwards.</li>
 * <li>Invoking <code>SdaiTransaction</code> methods {@link #commit} and {@link #abort} implies
 * starting a new SDAI transaction associated with the same <code>SdaiTransaction</code> object.
 * Also open remote repository contents are reloaded to reflect the status at the moment
 * new transaction started. This includes aggregates of <code>SdaiModels</code> and
 * <code>SchemaInstances</code>, associated models of <code>SchemaInstances</code> and
 * entity instances and their values of <code>SdaiModels</code> that have their accesses started.
 * JSDAI guarantees that <code>SdaiModel</code>, <code>SchemaInstance</code>
 * and <code>EEntity</code> object instances are retained as long as they point to existing
 * object on the database throughout the lifetime of active <code>SdaiTransaction</code>.
 * Aggregate and <code>ExternalData</code> objects may become invalid after <code>commit</code>
 * and <code>abort</code> operation.</li>
 * <li><code>SdaiTransaction</code> methods {@link #endTransactionAccessCommit} and
 * {@link #endTransactionAccessAbort} end active transaction. Starting new <code>SdaiTransaction</code>
 * afterwards does not ensure that open repositories reflect actual status on JSDAI-DB. However
 * opening the repository with this new transaction following closing it will force refreshing
 * the repository</li>
 * <li><code>SdaiTransaction</code> methods {@link #commit} and {@link #endTransactionAccessCommit}
 * may fail if any of <code>SdaiModels</code> or <code>SchemaInstances</code> modified during
 * the transaction was concurrently changed by another <code>SdaiSession</code>. If this 
 * situation occurs the commit operation fails with <code>jsdai.client.SdaiExceptionRemote</code>.
 * Application can only abort transaction afterwards.</li>
 * <li>Each of the models that have been modified since the last <code>commit</code> or
 * <code>abort</code> operation is locked from changing within the <code>commit</code> and
 * <code>endTransactionAccessCommit</code> operation for the moment changes are written
 * to JSDAI-DB.</li>
 * </ul>
 * <h3>Concurrency support</h3>
 * <p>The above mentioned remote <code>SdaiTransaction</code> specifics ensure data consistency even
 * when many users are making changes on JSDAI-DB.</p>
 * <p>Bellow are the examples of some situations that may occur when several users are making
 * simultaneous changes to JSDAI-DB:</p>
 * <p>Suppose there are two <code>SdaiModels</code> A and B. There are two users working on JSDAI-DB
 * <code>user1</code> and <code>user2</code>. Model A has entity instance <code>#1</code> and model
 * B has instances <code>#2</code> and <code>#3</code>. Attribute <code>#1.a1</code> has string
 * value <code>'id1'</code> and attribute <code>#1.a2</code> has entity reference
 * value <code>#2</code>.</p>
 * <ol>
 * <li>
 * <i>timestamp01:</i> <code>user1</code> starts new transaction and starts R/W access for model A<br/>
 * <i>timestamp02:</i> <code>user2</code> starts new transaction and starts R/W access for model A<br/>
 * <i>timestamp03:</i> <code>user1</code> changes <code>#1.a1</code> to <code>'id2'</code><br/>
 * <i>timestamp04:</i> <code>user2</code> changes <code>#1.a1</code> to <code>'id2'</code><br/>
 * <i>timestamp05:</i> <code>user1</code> commits active transaction. Commit operation succeeds<br/>
 * <i>timestamp06:</i> <code>user2</code> commits active transaction. Commit operation fails<br/>
 * <i>timestamp07:</i> <code>user2</code> aborts active transaction<br/>
 * <i>timestamp08:</i> <code>user2</code> gets value of <code>#1.a1</code> which is
 *                     <code>'id2'</code> now<br/>
 * <i>timestamp09:</i> <code>user2</code> changes <code>#1.a1</code> to <code>'id3'</code><br/>
 * <i>timestamp10:</i> <code>user2</code> commits active transaction. Commit operation succeeds<br/><br/>
 * </li>
 * <li>
 * <i>timestamp01:</i> <code>user1</code> starts new transaction and starts R/W access for model A<br/>
 * <i>timestamp02:</i> <code>user2</code> starts new transaction and starts R/W access for model B<br/>
 * <i>timestamp03:</i> <code>user1</code> creates new instance <code>#4</code> and assigns
 *                     <code>'id2'</code> to <code>#4.a1</code><br/>
 * <i>timestamp04:</i> <code>user2</code> creates new instance <code>#5</code> and assigns
 *                     <code>'id2'</code> to <code>#5.a1</code><br/>
 * <i>timestamp05:</i> <code>user1</code> commits active transaction. Commit operation succeeds<br/>
 * <i>timestamp06:</i> <code>user2</code> commits active transaction. Commit operation fails<br/>
 * <i>timestamp07:</i> <code>user2</code> aborts active transaction<br/>
 * <i>timestamp08:</i> <code>user2</code> detects that <code>#4</code> with <code>#4.a1</code> value
 *                     <code>'id2'</code> was created<br/>
 * <i>timestamp09:</i> <code>user2</code> creates new instance <code>#6</code> and assigns
 *                     <code>'id3'</code> to <code>#6.a1</code><br/>
 * <i>timestamp10:</i> <code>user2</code> commits active transaction. Commit operation succeeds<br/><br/>
 * </li>
 * <li>
 * <i>timestamp01:</i> <code>user1</code> starts new transaction and starts access R/W for model A
 *                     and R/O for model B<br/>
 * <i>timestamp02:</i> <code>user2</code> starts new transaction and starts access R/W for model B<br/>
 * <i>timestamp03:</i> <code>user1</code> changes <code>#1.a2</code> to <code>#3</code><br/>
 * <i>timestamp04:</i> <code>user2</code> deletes instance <code>#3</code><br/>
 * <i>timestamp05:</i> <code>user1</code> commits active transaction. Commit operation succeeds<br/>
 * <i>timestamp06:</i> <code>user2</code> commits active transaction. Commit operation succeeds<br/>
 * <i>timestamp07:</i> <code>user1</code> aborts active transaction<br/>
 * <i>timestamp08:</i> <code>user1</code> detects that <code>#1.a2</code> value is unset<br/>
 * </li>
 * </ol>
 */
public final class SdaiTransaction extends SdaiCommon {

/**
 * When {@link #getMode() getMode} returns this value, all operations for accessing
 * entity instances within session are disallowed.
 */
	public static final int NO_ACCESS = 0;
/**
 * When {@link #getMode() getMode} returns this value, entity instances within session
 * are or may become available (after starting access to owning model) for read-only access.
 */
	public static final int READ_ONLY = 1;
/**
 * When {@link #getMode() getMode} returns this value, entity instances within session
 * are or may become available (after starting access to owning model) for read-write access.
 */
	public static final int READ_WRITE = 2;

	static final int TRANSACTION_STATUS_NORMAL = 0;
	static final int TRANSACTION_STATUS_COMITTING = 1;
	static final int TRANSACTION_STATUS_ABORTING = 2;
	static final int TRANSACTION_STATUS_REOPENING = 3;

/**
	EXPRESS attributes of SdaiTransaction, see ISO 10303-22::7.4.5 sdai_transaction.
*/
	int mode;
	SdaiSession owning_session;
	//V.N. old mode for special implicit transactions
	int oldMode;
	int transactionStatus;

/**
	An array consisting of identifiers of all models deleted since the last 
	execution of openSession, commit or abort operation, whichever of them 
	occurred most recently.
	Models to this array are added during deleteSdaiModel in SdaiModel class.
	The array is used by the commit method which deletes binary files of all 
   models whose identifiers are stored in it.
	The array is made empty in either commit or abort method.
*/
	SdaiModel [] stack_del_mods;

/**
	An array in which the i-th element is the mode of the model whose 
	identifier is stored as the i-th element of the array stack_del_mods.
	The array is filled in with values in deleteSdaiModel in SdaiModel class 
	and used in abort method.
*/

/*
	An array in which the i-th element is the location of the repository of 
	the model whose identifier is stored as the i-th element of the array 
	stack_del_mods. The array is filled in with values in deleteSdaiModel 
	in SdaiModel class and used in commit method and its auxiliary leaveIfNotDeleted.
*/
//	String [] stack_del_mods_rep_loc;

/**
	An array in which the i-th element is the repository of 
	the model whose identifier is stored as the i-th element of the array 
	stack_del_mods. The array is filled in with values in deleteSdaiModel 
	in SdaiModel class and used in commit and abort methods here.
*/
	SdaiRepository [] stack_del_mods_rep;

/**
	The count of elements in arrays stack_del_mods, stack_del_mods_mode, 
	and stack_del_mods_rep.
*/
	int stack_length;

	SchemaInstance [] stack_del_sch_insts;

	SdaiRepository [] stack_del_sch_insts_rep;

	int stack_length_sch_insts;

//	SchemaInstance domain;
	Object domain;

	/* V.N. */
	QueryLibProvider queryLibProvider = null;

/**
	The initial length of the internal arrays 'stack_del_mods' and 'stack_del_mods_rep'. 
*/
	private final int NUMBER_OF_DELETED_MODS = 16;

	private final int NUMBER_OF_DELETED_SCH_INSTS = 16;



/**
	Returns an owner of this SdaiTransaction.
*/
	SdaiCommon getOwner() {
		return owning_session;
	}


/**
	For SdaiTransaction it is a dummy method.
*/
	void modified() throws SdaiException {
		// skip
	}



/**
	Used to create an SdaiTransaction in SdaiSession class:
	 - temporary in SdaiSession constructor to allow lang operations in openSession method;
	 - in methods startTransactionReadOnlyAccess and startTransactionReadWriteAccess.
*/
	SdaiTransaction(int mode_set, SdaiSession session) throws SdaiException {
		mode = mode_set;
		owning_session = session;
// 		initRemoteTransaction();
		oldMode = -1;
		transactionStatus = TRANSACTION_STATUS_NORMAL;
	}

	//Transactions start implicitly on the bridge now V.N.
// 	final void initRemoteTransaction() throws SdaiException {
// 		if(owning_session.bridgeSession != null) {
// 			switch(mode) {
// 			case READ_ONLY:
// 				owning_session.bridgeSession.startReadOnlyTransaction();
// 				break;
// 			case READ_WRITE:
// 				owning_session.bridgeSession.startReadWriteTransaction();
// 				break;
// 			}
// 			throw new SdaiException(SdaiException.SY_ERR, e);
// 		}
// 	}

	final void stopRemoteTransaction() throws SdaiException {
		if(owning_session.bridgeSession != null) {
			owning_session.bridgeSession.commitStart();
			owning_session.bridgeSession.commitEnd("unlinkDataBaseBridge");
		}
	}

	private void reopenActiveRepositories() throws SdaiException {
		if(owning_session.bridgeSession != null) {
			SdaiIterator activeIter = owning_session.active_servers.createIterator();
			boolean completed = false;
			transactionStatus = TRANSACTION_STATUS_REOPENING;
			try {
				while(activeIter.next()) {
					SdaiRepository activeRepository = 
						owning_session.active_servers.getCurrentMember(activeIter);
					activeRepository.reopenRepository();
				}
				owning_session.active_models.attachIterator(activeIter);
				while(activeIter.next()) {
					SdaiModel activeModel = 
						owning_session.active_models.getCurrentMember(activeIter);
					activeModel.restartReopenedAccess(false);
				}
				completed = true;
			} finally {
				transactionStatus = TRANSACTION_STATUS_NORMAL;
				if(!completed) {
					owning_session.active_models.attachIterator(activeIter);
					while(activeIter.next()) {
						SdaiModel activeModel = 
							owning_session.active_models.getCurrentMember(activeIter);
						activeModel.restartReopenedAccess(true);
					}
				}
			}
		}
	}

	private void closeActiveRepositories() throws SdaiException {
		SdaiRepository activeRepositories[] = 
			new SdaiRepository[owning_session.active_servers.getMemberCount()];
		int activeRepositoryIdx = 0;
		SdaiIterator activeRepositoryIter = owning_session.active_servers.createIterator();
		while(activeRepositoryIter.next()) {
			SdaiRepository activeRepository = 
				owning_session.active_servers.getCurrentMember(activeRepositoryIter);
			activeRepositories[activeRepositoryIdx++] = activeRepository;
		}
		for(activeRepositoryIdx = 0;
			activeRepositoryIdx < activeRepositories.length; activeRepositoryIdx++) {
			
			activeRepositories[activeRepositoryIdx].closeRepository();
		}
	}

/**
 * Returns the current access mode provided by this <code>SdaiTransaction</code>
 * within <code>SdaiSession</code>. 
 * For data accessing, 3 modes are defined:
 * <p>NO_ACCESS: the data cannot be accessed;
 * <p>READ_ONLY: only read operations on the data are allowed;
 * <p>READ_WRITE: read-write operations are allowed.
 * @return the current access mode.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 */
	public int getMode() throws SdaiException {
		if (owning_session == null) {
			throw new SdaiException(SdaiException.TR_NEXS);
		}
//		if (owning_session.session == null) {
		if (!owning_session.opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		return mode;
	}


/**
 * Returns the only one <code>SdaiSession</code> object.
 * @return the current session.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 */
	public SdaiSession getOwningSession() throws SdaiException {
		if (owning_session == null) {
			throw new SdaiException(SdaiException.TR_NEXS);
		}
//		if (owning_session.session == null) {
		if (!owning_session.opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		return owning_session;
	}


/**
 * Makes persistent all changes in all open repositories made since the
 * most recent either start transaction with read-write access, commit,
 * or abort operation was performed.
 * The data of a repository to be stored persistently include:
 * <ul>
 * <li> repository data outside its contents, such as name, description,
 * author, organization, preprocessor version, language, context identifiers,
 * and so on, if at least one such attribute was updated. This information
 * is directed to the binary file named "header";
 * <li> general information about currently existing models and all
 * information about schema instances provided at least one model or schema
 * instance was created, deleted or modified during time interval considered
 * by this method. These data are written down to binary file "contents"; 
 * this file also contains the largest number used to construct the
 * persistent label for instances in the models of the repository;
 * <li><code>SdaiModel</code>s in which some data were modified and whose
 * access mode is set. For each model, a separate binary file with the name
 * of that model is created.
 * </ul>
 * No function is performed if access mode for this transaction is read-only.
 * The method does not change the current access mode.
 * <p> This method also sets the change date for any model or schema instance
 * that has been modified or created. Moreover, it sets the change date also
 * for repository itself if some data in the repository was changed.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #abort
 * @see SdaiModel#getChangeDate
 * @see SchemaInstance#getChangeDate
 * @see SdaiRepository#getChangeDate
 * @see "ISO 10303-22::10.4.8 Commit"
 * @see "ISO 10303-22::7.4.5 sdai_transaction"
 */
	public void commit() throws SdaiException {
//		synchronized (syncObject) {
			commitAction(null);
// 			if(owning_session.bridgeSession != null) {
// 				owning_session.bridgeSession.commit();
// 				//Transactions start implicitly on the bridge now V.N.
// // 				switch(mode) {
// // 				case READ_ONLY:
// // 					owning_session.bridgeSession.startReadOnlyTransaction();
// // 					break;
// // 				case READ_WRITE:
// // 					owning_session.bridgeSession.startReadWriteTransaction();
// // 					break;
// // 				}
// 			}
			reopenActiveRepositories();
//		} // syncObject
	}

	/**
	 * Makes persistent all changes in all open repositories made since the
	 * most recent either start transaction with read-write access, commit,
	 * or abort operation was performed.
	 * This is an extension of SDAI.
	 * 
	 * @param appComment application provided comment to be recorded in JSDAI-DB
	 *                   for history tracking. Application comment is ignored if
	 *                   JSDAI-DB changes did not occur for the  current transaction
	 *                   or if JSDAI-DB does not support recording commit operations
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 * @since 4.1.0
	 * @see #commit()
	 */
	public void commit(String appComment) throws SdaiException {
			commitAction(appComment);
			reopenActiveRepositories();
	}

	private void commitAction(String appComment) throws SdaiException {
//		if (owning_session == null || owning_session.session == null) {
		if (owning_session == null || !owning_session.opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		if (owning_session.active_transaction == null) {
			throw new SdaiException(SdaiException.TR_NEXS);
		}
		if (owning_session.active_transaction != this) {
			throw new SdaiException(SdaiException.TR_EAB, this);
		}
		if (mode == NO_ACCESS) {
			throw new SdaiException(SdaiException.TR_NAVL, this);
		}
		if(owning_session.bridgeSession != null) {
			owning_session.bridgeSession.commitStart();
		}
		transactionStatus = TRANSACTION_STATUS_COMITTING;
        try {
		if (mode == READ_WRITE) {
		int i;
		boolean commit_bridge = false;
        boolean successful = false;

			SdaiRepository rep;
        
			try {
				for (int l = 0; l < owning_session.active_servers.myLength; l++) {
					rep = (SdaiRepository)owning_session.active_servers.myData[l];
					if (rep == SdaiSession.systemRepository) {
						continue;
					}
					rep.preCommitting();
				}
            
				//int mo_count = repo.getModels().getMemberCount();
				for (i = 0; i < stack_length; i++) {
					SdaiRepository rep_for_del_mod = stack_del_mods_rep[i];
					//System.out.println("SdaiTransaction  ??????????? model in stack: " + stack_del_mods[i].name + 
					//"    its repo name: " + rep_for_del_mod.name + "   repo itself: " + rep_for_del_mod + 
					//"  session: " + rep_for_del_mod.getSession());
					if (owning_session.known_servers.isMember(rep_for_del_mod) && stack_del_mods[i].committed) {
						commit_bridge = stack_del_mods[i].deleting(commit_bridge, rep_for_del_mod);
					}
				}
				if(owning_session.bridgeSession != null) {
					owning_session.bridgeSession.commitPostModelDelete();
				}
				for (i = 0; i < stack_length; i++) {
					stack_del_mods[i] = null;
				}
				stack_length = 0;
				//int moq_count = repoq.getModels().getMemberCount();
				for (i = 0; i < stack_length_sch_insts; i++) {
					commit_bridge = stack_del_sch_insts[i].deleting(commit_bridge, stack_del_sch_insts_rep[i]);
					stack_del_sch_insts[i] = null;
				}
				stack_length_sch_insts = 0;

				SdaiModel model;
				SchemaInstance sch;
				for (int l = 0; l < owning_session.active_servers.myLength; l++) {
					rep = (SdaiRepository)owning_session.active_servers.myData[l];
					if (rep == SdaiSession.systemRepository) {
						continue;
					}
					for (i = 0; i < rep.models.myLength; i++) {
						model = (SdaiModel)rep.models.myData[i];
						commit_bridge = model.committingHeader(commit_bridge);
					}
				}
				for (int l = 0; l < owning_session.active_servers.myLength; l++) {
					rep = (SdaiRepository)owning_session.active_servers.myData[l];
					if (rep == SdaiSession.systemRepository) {
						continue;
					}
    
					boolean contents_modified;
					if (rep.model_deleted || rep.schema_instance_deleted) {
						contents_modified = true;
					} else {
						contents_modified = false;
					}
					boolean repo_stored_not_after_import = contents_modified;
					//int m_count = rep.getModels().getMemberCount();
					//System.out.println("SdaiTransaction  rep: " + rep.name + "    rep.models.myLength: " + rep.models.myLength + 
					//"    rep.schemas.myLength: " + rep.schemas.myLength + "    m_count: " + m_count + "   rep: " + rep +
					//"  session: " + rep.getSession());
					for (i = 0; i < rep.models.myLength; i++) {
						model = (SdaiModel)rep.models.myData[i];
						//System.out.println("SdaiTransaction  model: " + model.name);

						if (SdaiSession.debug) System.out.println("SdaiTransaction  contents_modified: " + contents_modified +
	                    "   model: " + model.name + 
	                    "   model.modified: " + model.modified + 
	                    "   model.modified_by_import: " + model.modified_by_import + 
	                    "   model.modified_outside_contents = " + model.modified_outside_contents +
	                    "   model.modified_outside_contents_by_import = " + model.modified_outside_contents_by_import +
	                    "   model.mode = " + model.mode);
						leaveIfNotDeleted(model);
						if (((model.modified || model.modified_by_import) && (model.mode & SdaiModel.MODE_MODE_MASK) != NO_ACCESS) ||
                            model.modified_outside_contents || model.modified_outside_contents_by_import) {

							model.change_date = System.currentTimeMillis();
							contents_modified = true;
							if (model.modified || model.modified_outside_contents) {
                            repo_stored_not_after_import = true;
							}
						}
					}
					for (i = 0; i < rep.schemas.myLength; i++) {
						sch = (SchemaInstance)rep.schemas.myData[i];
						if (sch.modified) {
							contents_modified = true;
							repo_stored_not_after_import = true;
						} else if (sch.modified_by_import) {
							contents_modified = true;
						}
						commit_bridge = sch.committing(commit_bridge);
					}
					boolean modified_ext = rep.modified || 
						(!rep.isRemote() && rep.created_or_imported && rep.models.myLength == 0 && rep.schemas.myLength == 0);
					if (modified_ext || contents_modified) {
						if (modified_ext) {
							repo_stored_not_after_import = true;
						}
						commit_bridge = rep.committing(commit_bridge);
						//System.out.println("SdaiTransaction  !!!!! rep committing: " + rep.name);
					}
					for (i = 0; i < rep.models.myLength; i++) {
						model = (SdaiModel)rep.models.myData[i];
						commit_bridge = model.committing(commit_bridge);
					}
					rep.commitSetFields(repo_stored_not_after_import);
				}
    
				for (int l = 0; l < owning_session.active_servers.myLength; l++) {
					rep = (SdaiRepository)owning_session.active_servers.myData[l];
					if (rep == SdaiSession.systemRepository) {
						continue;
					}
					for (i = 0; i < rep.schemas.myLength; i++) {
						sch = (SchemaInstance)rep.schemas.myData[i];
						if (sch.modified || sch.modified_by_import) {
							sch.committingAssocModels();
							sch.modified = false;
							sch.modified_by_import = false;
						}
					}
				}
    
				for (int l = 0; l < owning_session.active_servers.myLength; l++) {
					rep = (SdaiRepository)owning_session.active_servers.myData[l];
					if (rep == SdaiSession.systemRepository) {
						continue;
					}
					rep.postCommitting();
				}
				successful = true;
				owning_session.empty_undo_file();
			} finally {
					for (int l = 0; l < owning_session.active_servers.myLength; l++) {
						rep = (SdaiRepository)owning_session.active_servers.myData[l];
						if (rep == SdaiSession.systemRepository) {
							continue;
						}

						rep.postCommittingRelease(successful);
					}
			}
			if (commit_bridge) {
				owning_session.commitClosing();		
			}
			if (domain instanceof SchemaInstance) {
				((SchemaInstance)domain).closeDomain();
			} else if (domain != null) {
				((ASchemaInstance)domain).closeDomainAll();
			}
			domain = null;
		}
		if(owning_session.bridgeSession != null) {
			owning_session.bridgeSession.commitEnd(appComment);
		}
		} finally {
			transactionStatus = TRANSACTION_STATUS_NORMAL;
		}
	}


/**
	Checks if model, which belongs to the set 'models' of a repository, is 
	included into the array 'stack_del_mods'. If so, then the model is removed from 
	this array (and companion arrays).
*/
	private void leaveIfNotDeleted(SdaiModel model) throws SdaiException {
		int i;
		int position = -1;
		String loc;
		if (model.repository.location instanceof String) {
			loc = (String)model.repository.location;
		} else {
			return;
		}
		for (i = 0; i < stack_length; i++) {
			String del_mod_rep_loc;
			if (stack_del_mods_rep[i].location instanceof String) {
				del_mod_rep_loc = (String)stack_del_mods_rep[i].location;
			} else {
				continue;
			}
			if (model.identifier == stack_del_mods[i].identifier && 
//					loc.equals(stack_del_mods_rep_loc[i])) {
					loc.equals(del_mod_rep_loc)) {
				position = i;
				break;
			}
		}
		if (position < 0) {
			return;
		}
//		for (i = position; i < stack_length - 1; i++) {
//			stack_del_mods[i] = stack_del_mods[i + 1];
//			stack_del_mods_rep[i] = stack_del_mods_rep[i + 1];
//		}
		System.arraycopy(stack_del_mods, position + 1, stack_del_mods, position, stack_length-position-1);
		System.arraycopy(stack_del_mods_rep, position + 1, stack_del_mods_rep, position, stack_length-position-1);
		stack_length--;
	}


/**
 * Restores the state of all open repositories, which existed at the time when
 * the most recent start transaction with read-write access or commit
 * operation was performed. All deleted models, schema instances and
 * entity instances are restored, all created models, schema instances and
 * entity instances no longer exist, and all modifications to models including
 * instances within them, to schema instances and to repository attributes outside
 * its contents are lost. Deleted repositories, however, are not restored,
 * and created repositories survive during this operation.
 * <p> No function is performed if access mode for this transaction is read-only.
 * The method does not change the current access mode.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #commit
 * @see "ISO 10303-22::10.4.9 Abort"
 * @see "ISO 10303-22::7.4.5 sdai_transaction"
 */
	public void abort() throws SdaiException {
//		synchronized (syncObject) {
			if(owning_session.bridgeSession != null) {
				owning_session.bridgeSession.abort();
				//Transactions start implicitly on the bridge now V.N.
// 				switch(mode) {
// 				case READ_ONLY:
// 					owning_session.bridgeSession.startReadOnlyTransaction();
// 					break;
// 				case READ_WRITE:
// 					owning_session.bridgeSession.startReadWriteTransaction();
// 					break;
// 				}
			}
			abortAction(false);
			reopenActiveRepositories();
//		} // syncObject
	}

	private void abortAction(boolean endTransaction) throws SdaiException {

//		if (owning_session == null || owning_session.session == null) {
		if (owning_session == null || !owning_session.opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		if (owning_session.active_transaction == null) {
			throw new SdaiException(SdaiException.TR_NEXS);
		}
		if (owning_session.active_transaction != this) {
			throw new SdaiException(SdaiException.TR_EAB, this);
		}
		if (mode == NO_ACCESS) {
			throw new SdaiException(SdaiException.TR_NAVL, this);
		}
		if (mode != READ_WRITE) {
			return;
		}

		transactionStatus = TRANSACTION_STATUS_ABORTING;
		try {
			int i;
			for (int l = 0; l < owning_session.active_servers.myLength; l++) {
				SdaiRepository rep =
					(SdaiRepository)owning_session.active_servers.myData[l];
				if (rep == SdaiSession.systemRepository) {
					continue;
				}
				boolean contents_modified = false;
				SdaiModel model;
				SchemaInstance sch;
				for (i = 0; i < rep.models.myLength; i++) {
					model = (SdaiModel)rep.models.myData[i];
					if (!model.committed) {
						continue;
					}
					if ((model.mode & SdaiModel.MODE_MODE_MASK) != NO_ACCESS) {
						if (model.modified || model.modified_outside_contents) {
							contents_modified = true;
						}
					} else {
						if (model.modified_outside_contents) {
							contents_modified = true;
						}
					}
				}
				for (i = 0; i < rep.schemas.myLength; i++) {
					sch = (SchemaInstance)rep.schemas.myData[i];
					if (sch.committed && sch.modified) {
						contents_modified = true;
					}
				}

if (SdaiSession.debug) System.out.println("SdaiTransaction (((+++))) before loadContentsLocal  rep: " + 
rep.name + 
"   schemas count = " + rep.schemas.myLength +
"    rep.committed: " + rep.committed + "   contents_modified: " + contents_modified + 
"    rep.modified: " + rep.modified);

				rep.aborting(contents_modified);
				int count_of_models = rep.models.myLength;
				int deleted = 0;
				for (i = 0; i < count_of_models; i++) {
					model = (SdaiModel)rep.models.myData[i - deleted];
					if (model.aborting(this, endTransaction)) {
						deleted++;
					}
				}
				int count_of_schemas = rep.schemas.myLength;
				deleted = 0;
				for (i = 0; i < count_of_schemas; i++) {
					sch = (SchemaInstance)rep.schemas.myData[i - deleted];
					if (sch.aborting()) {
						deleted++;
					}
				}
				rep.restoring(this);
				rep.model_deleted = false;
				rep.schema_instance_deleted = false;
			}

			for (i = 0; i < stack_length; i++) {
				stack_del_mods[i] = null;
				stack_del_mods_rep[i] = null;
			}
			stack_length = 0;
			for (i = 0; i < stack_length_sch_insts; i++) {
				stack_del_sch_insts[i] = null;
				stack_del_sch_insts_rep[i] = null;
			}
			stack_length_sch_insts = 0;
			if (domain instanceof SchemaInstance) {
				((SchemaInstance)domain).closeDomain();
			} else if (domain != null) {
				((ASchemaInstance)domain).closeDomainAll();
			}
			domain = null;
			owning_session.empty_undo_file();
		} finally {
			transactionStatus = TRANSACTION_STATUS_NORMAL;
		}
	}


/**
 * Ends the sequence of operations started by startTransactionReadWriteAccess
 * or startTransactionReadOnlyAccess method. Before ending the transaction
 * access the commit operation is performed. Further access to entity instances
 * within the session is available only after the next invocation of either
 * startTransactionReadWriteAccess or startTransactionReadOnlyAccess
 * method.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #commit
 * @see SdaiSession#startTransactionReadWriteAccess
 * @see SdaiSession#startTransactionReadOnlyAccess
 * @see "ISO 10303-22::10.4.10 End transaction access and commit"
 * @see "ISO 10303-22::7.4.5 sdai_transaction"
 */
	public void endTransactionAccessCommit()  throws SdaiException {
//		synchronized (syncObject) {
//		if (owning_session == null || owning_session.session == null) {
		if (owning_session == null || !owning_session.opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		if (owning_session.active_transaction == null) {
			throw new SdaiException(SdaiException.TR_NEXS);
		}
		if (owning_session.active_transaction != this) {
			throw new SdaiException(SdaiException.TR_EAB, this);
		}
		if (mode == NO_ACCESS) {
			throw new SdaiException(SdaiException.TR_NAVL, this);
		}
		commitAction(null);
		if(Implementation.userConcurrencyCompatibility == 1) {
			closeActiveRepositories();
		}
		owning_session.active_transaction = null;
		owning_session = null;
//		} // syncObject
	}

	/**
	 * Ends the sequence of operations started by startTransactionReadWriteAccess
	 * or startTransactionReadOnlyAccess method.
	 * This is an extension of SDAI.
	 * 
	 * @param appComment application provided comment to be recorded in JSDAI-DB
	 *                   for history tracking. Application comment is ignored if
	 *                   JSDAI-DB changes did not occur for the  current transaction
	 *                   or if JSDAI-DB does not support recording commit operations
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 * @since 4.1.0
	 * @see #endTransactionAccessCommit()
	 */
	public void endTransactionAccessCommit(String appComment)  throws SdaiException {
		if (owning_session == null || !owning_session.opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		if (owning_session.active_transaction == null) {
			throw new SdaiException(SdaiException.TR_NEXS);
		}
		if (owning_session.active_transaction != this) {
			throw new SdaiException(SdaiException.TR_EAB, this);
		}
		if (mode == NO_ACCESS) {
			throw new SdaiException(SdaiException.TR_NAVL, this);
		}
		commitAction(appComment);
		if(Implementation.userConcurrencyCompatibility == 1) {
			closeActiveRepositories();
		}
		owning_session.active_transaction = null;
		owning_session = null;
	}

/**
 * Ends the sequence of operations started by startTransactionReadWriteAccess
 * or startTransactionReadOnlyAccess method. Before ending the transaction
 * access the abort operation is performed. Further access to entity instances
 * within the session is available only after the next invocation of either
 * startTransactionReadWriteAccess or startTransactionReadOnlyAccess
 * method.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException TR_NEXS, transaction does not exist.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #abort
 * @see SdaiSession#startTransactionReadWriteAccess
 * @see SdaiSession#startTransactionReadOnlyAccess
 * @see "ISO 10303-22::10.4.11 End transaction access and abort"
 * @see "ISO 10303-22::7.4.5 sdai_transaction"
 */
	public void endTransactionAccessAbort()  throws SdaiException {
//		synchronized (syncObject) {
//		if (owning_session == null || owning_session.session == null) {
		if (owning_session == null || !owning_session.opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		if (owning_session.active_transaction == null) {
			throw new SdaiException(SdaiException.TR_NEXS);
		}
		if (owning_session.active_transaction != this) {
			throw new SdaiException(SdaiException.TR_EAB, this);
		}
		if (mode == NO_ACCESS) {
			throw new SdaiException(SdaiException.TR_NAVL, this);
		}
		if(owning_session.bridgeSession != null) {
			owning_session.bridgeSession.abort();
		}
		abortAction(true);
		if(Implementation.userConcurrencyCompatibility == 1) {
			closeActiveRepositories();
		}
		owning_session.active_transaction = null;
		owning_session = null;
//		} // syncObject
	}

	/** 
	 * Establishes domain of <code>SdaiModels</code> for use
	 * in concurrent multiuser refresh and commit operations.
	 * <br>
	 * This method is used for access of remote multiuser JSDAI databases.
	 * It ensures data integrity in concurrent environments.
	 * Read only access for all <code>SdaiModels</code> associated with the <code>SchemaInstance</code>
	 * is started in an atomic operation.
	 * <br>
	 * Prior to this call <code>SdaiModels</code> of the <code>SchemaInstance</code> 
	 * have to be in no access mode.
	 * (This is going to be relaxed when <i>refresh</i> operations are implemented.)
	 * <br>
	 * The domain can be set for a running transaction only once. <i>(This needs to be checked.)</i>
	 * The domain becomes unset within an abort or a commit operation.
	 * <br>
	 * This method is an extension to ISO/TS 10303-27:2000.
	 * @param schemaInstance a schema instance which models are established as a domain.
	 * <i>Maybe schemaInstance can be null which means that domain is removed.</i>
	 * @throws SdaiException <br>
	 * <dl><dd>SS_NOPN, session is not open.</dd>
	 *     <dd>TR_NEXS, transaction does not exist.</dd>
	 *     <dd>TR_NAVL, transaction currently not available.</dd>
	 *     <dd>xxxx, domain has already been set.</dd>
	 *     <dd>yyyy, one or more models have RO or RW access.</dd>
	 *     <dd>SY_ERR, underlying system error.</dd></dl>
	 * @see #setDomainStartingROAccess(ASchemaInstance)
	 * @see #refreshDomainAndPromoteToRWAccess
	 */
	private void setDomainStartingROAccess(SchemaInstance schemaInstance)
	throws SdaiException {
		if (owning_session == null || !owning_session.opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		if (owning_session.active_transaction == null) {
			throw new SdaiException(SdaiException.TR_NEXS);
		}
		if (owning_session.active_transaction != this) {
			throw new SdaiException(SdaiException.TR_EAB, this);
		}
		if (mode == NO_ACCESS) {
			throw new SdaiException(SdaiException.TR_NAVL, this);
		}
		if (schemaInstance == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (domain != null) {
			throw new SdaiException(SdaiException.OP_NVLD);
		}
//		synchronized (syncObject) {
		schemaInstance.startAssociatedModels(schemaInstance);
		domain = schemaInstance;
//		} // syncObject
	}

	/**
	 * Establishes domain of <code>SdaiModels</code> for use
	 * in concurrent multiuser refresh and commit operations.
	 * <br>
	 * For more details see {@link #setDomainStartingROAccess(SchemaInstance schemaInstance)}.
	 * @param schemaInstances schema instances which models are established as a domain.
	 * @exception SdaiException if an error occurs, see
	 *                          {@link #setDomainStartingROAccess(SchemaInstance schemaInstance)}
	 * @see #setDomainStartingROAccess(SchemaInstance)
	 * @see #refreshDomainAndPromoteToRWAccess
	 */
	private void setDomainStartingROAccess(ASchemaInstance schemaInstances) throws SdaiException {
		if (owning_session == null || !owning_session.opened) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		if (owning_session.active_transaction == null) {
			throw new SdaiException(SdaiException.TR_NEXS);
		}
		if (owning_session.active_transaction != this) {
			throw new SdaiException(SdaiException.TR_EAB, this);
		}
		if (mode == NO_ACCESS) {
			throw new SdaiException(SdaiException.TR_NAVL, this);
		}
		if (schemaInstances == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (domain != null) {
			throw new SdaiException(SdaiException.OP_NVLD);
		}
//		synchronized (syncObject) {
		schemaInstances.startAssociatedModelsAll();
		domain = schemaInstances;
//		} // syncObject
	}

// This is a portion of obsolete code to support concurrency on JSDAI-DB
//	/**
//	 * Refreshes contents of <code>SdaiModels<code> in a domain and promotes to read write mode.
//	 *
//	 * This method works in conjunction with methods 
//	 * {@link #setDomainStartingROAccess(SchemaInstance)} and
//	 * {@link #setDomainStartingROAccess(ASchemaInstance)}
//	 * Domain models are locked in remote JSDAI database and their content is
//	 * refreshed. Then all models are promoted into read write access mode. Prior to
//	 * this call domain models have to be in either no access or read only mode.
//	 * @param rwModels <code>SdaiModels</code> that should be promoted to read write mode 
//	 *                 after refresh (models have to be already part of the domain defined
//	 *                 using {@link #setDomainStartingROAccess(SchemaInstance)} or
//	 *                 {@link #setDomainStartingROAccess(ASchemaInstance)}).
//	 *                 If <code>rwModels</code> is <code>null</code> then all models in the
//	 *                 domain are promoted to read write mode.
//	 * @exception SdaiException <br>
//	 * <dl><dd>SS_NOPN, session is not open.</dd>
//	 *     <dd>TR_NEXS, transaction does not exist.</dd>
//	 *     <dd>TR_NAVL, transaction currently not available.</dd>
//	 * @throws SdaiException TR_NRW, transaction not read-write.
//	 *     <dd>zzzz, domain has not been set.</dd>
//	 *     <dd>wwww, one or more models have RW access.</dd>
//	 *     <dd>SY_ERR, underlying system error.</dd></dl>
//	 * @see #setDomainStartingROAccess(SchemaInstance)
//	 * @see #setDomainStartingROAccess(ASchemaInstance)
//	 */
//	private void refreshDomainAndPromoteToRWAccess(ASdaiModel rwModels) throws SdaiException {
////1)lock all
////2)reload contents
////3)RW mode
//		if (owning_session == null || !owning_session.opened) {
//			throw new SdaiException(SdaiException.SS_NOPN);
//		}
//		if (owning_session.active_transaction == null) {
//			throw new SdaiException(SdaiException.TR_NEXS);
//		}
//		if (owning_session.active_transaction != this) {
//			throw new SdaiException(SdaiException.TR_EAB, this);
//		}
//		if (mode == NO_ACCESS) {
//			throw new SdaiException(SdaiException.TR_NAVL, this);
//		}
//		if (mode != READ_WRITE) {
//			throw new SdaiException(SdaiException.TR_NRW, this);
//		}
//		if (domain == null) {
//			throw new SdaiException(SdaiException.OP_NVLD);
//		}
////		synchronized (syncObject) {
//		if (domain instanceof SchemaInstance) {
//			((SchemaInstance)domain).refreshDomain(true, rwModels);
//		} else {
//			((ASchemaInstance)domain).refreshDomainAll(true, rwModels);
//		}
////		} // syncObject
//	}
//
//	/** 
//	 * Refreshes contents of <code>SdaiModels</code> in a domain.
//	 *
//	 * This method works in conjunction with methods 
//	 * {@link #setDomainStartingROAccess(SchemaInstance)} and
//	 * {@link #setDomainStartingROAccess(ASchemaInstance)}
//	 * Domain models are locked in remote JSDAI database and their content is
//	 * refreshed. Prior to this call domain models have to be in either
//	 * no access or read only mode.
//	 * If a (domain) model has no access, then it is started in read-only.
//	 * @throws SdaiException <br>
//	 * <dl><dd>SS_NOPN, session is not open.</dd>
//	 *     <dd>TR_NEXS, transaction does not exist.</dd>
//	 *     <dd>TR_NAVL, transaction currently not available.</dd>
//	 *     <dd>OP_NVLD, domain has not been set.</dd>
//	 *     <dd>MX_RW, one or more models have RW access.</dd>
//	 *     <dd>SY_ERR, underlying system error.</dd></dl>
//	 * @see #setDomainStartingROAccess(SchemaInstance)
//	 * @see #setDomainStartingROAccess(ASchemaInstance)
//	 */
//	private void refreshDomain() throws SdaiException {
//		if (owning_session == null || !owning_session.opened) {
//			throw new SdaiException(SdaiException.SS_NOPN);
//		}
//		if (owning_session.active_transaction == null) {
//			throw new SdaiException(SdaiException.TR_NEXS);
//		}
//		if (owning_session.active_transaction != this) {
//			throw new SdaiException(SdaiException.TR_EAB, this);
//		}
//		if (mode == NO_ACCESS) {
//			throw new SdaiException(SdaiException.TR_NAVL, this);
//		}
//		if (domain == null) {
//			throw new SdaiException(SdaiException.OP_NVLD);
//		}
////		synchronized (syncObject) {
//		if (domain instanceof SchemaInstance) {
//			((SchemaInstance)domain).refreshDomain(false, null);
//		} else {
//			((ASchemaInstance)domain).refreshDomainAll(false, null);
//		}
////		} // syncObject
//	}

/**
 * Returns a description of this transaction as a <code>String</code>.
 * It includes constant string "SdaiTransaction mode: " and the access mode
 * provided by this transaction.
 * @return a description of the transaction.
 */
	public String toString() {
		return "SdaiTransaction mode: " + mode;
	}


	void registerDeletedModel(SdaiModel mod) throws SdaiException {
		boolean found = false;
		String loc = null;
		SdaiRepository repo = mod.repository;
		if (repo.location instanceof String) {
			loc = (String)repo.location;
		}
		if (loc == null) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		for (int i = 0; i < stack_length; i++) {
			if (repo != stack_del_mods_rep[i] || mod.identifier != stack_del_mods[i].identifier) {
				continue;
			}
			if (stack_del_mods_rep[i].location instanceof String) {
				String del_mod_rep_loc = (String)stack_del_mods_rep[i].location;
				if (loc.equals(del_mod_rep_loc)) {
					found = true;
					break;
				}
			}
		}
		if (found) {
			return;
		}
		if (stack_del_mods == null) {
			stack_del_mods = new SdaiModel[NUMBER_OF_DELETED_MODS];
			stack_del_mods_rep = new SdaiRepository[NUMBER_OF_DELETED_MODS];
			stack_length = 0;
		} else if (stack_length >= stack_del_mods.length) {
			stack_del_mods = ensureModsCapacity(stack_del_mods);
			stack_del_mods_rep = ensureRepsCapacity(stack_del_mods_rep);
		}
		stack_del_mods[stack_length] = mod;
		stack_del_mods_rep[stack_length] = repo;
		mod.mode_before_deletion = mod.mode & SdaiModel.MODE_MODE_MASK;
		stack_length++;
	}


	SdaiModel restoreDeletedModel(String name, int identifier, SdaiRepository owner) throws SdaiException {
		for (int i = 0; i < stack_length; i++) {
			if (stack_del_mods_rep[i] != owner) {
				continue;
			}
			if (identifier == stack_del_mods[i].identifier && name.equals(stack_del_mods[i].name)) {
				return stack_del_mods[i];
			}
		}
		return null;
	}


	void registerDeletedSchemaInstance(SchemaInstance sch_inst) throws SdaiException {
		if (stack_del_sch_insts == null) {
			stack_del_sch_insts = new SchemaInstance[NUMBER_OF_DELETED_SCH_INSTS];
			stack_del_sch_insts_rep = new SdaiRepository[NUMBER_OF_DELETED_SCH_INSTS];
			stack_length_sch_insts = 0;
		} else if (stack_length_sch_insts >= stack_del_sch_insts.length) {
			stack_del_sch_insts = ensureSchInstsCapacity(stack_del_sch_insts);
			stack_del_sch_insts_rep = ensureRepsCapacity(stack_del_sch_insts_rep);
		}
		stack_del_sch_insts[stack_length_sch_insts] = sch_inst;
		stack_del_sch_insts_rep[stack_length_sch_insts] = sch_inst.repository;
		stack_length_sch_insts++;
	}


	SchemaInstance restoreDeletedSchemaInstance(String name, SdaiRepository owner) throws SdaiException {
		for (int i = 0; i < stack_length_sch_insts; i++) {
			if (stack_del_sch_insts_rep[i] == owner && name.equals(stack_del_sch_insts[i].name)) {
				return stack_del_sch_insts[i];
			}
		}
		return null;
	}



/**
*/
	SdaiModel [] ensureModsCapacity(SdaiModel [] mods) {
		int new_length = mods.length * 2;
		SdaiModel [] new_array = new SdaiModel[new_length];
		System.arraycopy(mods, 0, new_array, 0, mods.length);
		return new_array;
	}


/**
*/
	SchemaInstance [] ensureSchInstsCapacity(SchemaInstance [] sch_insts) {
		int new_length = sch_insts.length * 2;
		SchemaInstance [] new_array = new SchemaInstance[new_length];
		System.arraycopy(sch_insts, 0, new_array, 0, sch_insts.length);
		return new_array;
	}


/**
	Increases the size of the array of repositories submitted as a parameter twice.
	This method is used in deleteSdaiModel in SdaiModel and in delete in SchemaInstance.
*/
	SdaiRepository [] ensureRepsCapacity(SdaiRepository [] reps) {
		int new_length = reps.length * 2;
		SdaiRepository [] new_array = new SdaiRepository[new_length];
		System.arraycopy(reps, 0, new_array, 0, reps.length);
		return new_array;
	}


}
