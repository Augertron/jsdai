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

import java.util.EventObject;

/**
 * Used to inform SdaiListner about changes in SdaiEventSources.
 */

public class SdaiEvent extends EventObject {

	public static final int INVALID = 0;
	public static final int MODIFIED = 1;
/** Used to communicate with SdaiServer
 * source = SdaiRepository, id = SERVER_DATA_CHANGED, argument = null  */
	public static final int SERVER_DATA_CHANGED = 5;

	/**
	 * Remote <code>SdaiRepository</code>, <code>SchemaInstance</code>,
	 * or <code>SdaiModel</code> was locked in a shared mode on JSDAI-DB
	 * @since 4.1.0
	 */
	public static final int LOCKED_SHARED_MODE = 6;

	/**
	 * Remote <code>SdaiRepository</code>, <code>SchemaInstance</code>,
	 * or <code>SdaiModel</code> was locked in an exclusive mode on JSDAI-DB
	 * @since 4.1.0
	 */
	public static final int LOCKED_EXCLUSIVE_MODE = 7;

	/**
	 * Shared mode lock was removed from remote <code>SdaiRepository</code>,
	 * <code>SchemaInstance</code>, or <code>SdaiModel</code> on JSDAI-DB
	 * @since 4.1.0
	 */
	public static final int UNLOCKED_SHARED_MODE = 8;

	/**
	 * Exclusive mode lock was removed from remote <code>SdaiRepository</code>,
	 * <code>SchemaInstance</code>, or <code>SdaiModel</code> on JSDAI-DB
	 * @since 4.1.0
	 */
	public static final int UNLOCKED_EXCLUSIVE_MODE = 9;

	/**
	 * All locks was removed from remote <code>SdaiRepository</code>,
	 * <code>SchemaInstance</code>, or <code>SdaiModel</code> on JSDAI-DB
	 * @since 4.1.0
	 */
	public static final int UNLOCKED_ALL = 10;

	/**
	 * Event item value to indicate modification of remote
	 * <code>SdaiRepository</code>, <code>SchemaInstance</code>,
	 * or <code>SdaiModel</code> header on JSDAI-DB
	 * @since 4.1.0
	 */
	public static final int ITEM_HEADER = 1;

	/**
	 * Event item value to indicate modification of remote
	 * <code>SdaiRepository</code>, <code>SchemaInstance</code>,
	 * or <code>SdaiModel</code> contents on JSDAI-DB
	 * @since 4.1.0
	 */
	public static final int ITEM_CONTENTS = 2;

	/**
	 * Event item value to indicate modification of both remote
	 * <code>SdaiRepository</code>, <code>SchemaInstance</code>,
	 * or <code>SdaiModel</code> header and contents on JSDAI-DB
	 * @since 4.1.0
	 */
	public static final int ITEM_ALL = 3;

	// Object: source; -- already in EventObject
	
	private int id;
	private int item;
	private Object argument;

	public SdaiEvent(Object source, int id, int item, Object argument) {
		super(source);
		this.id = id;
		this.item = item;
		this.argument = argument;
	}

	public int getId() {
		return id;
	}

	public int getItem() {
		return item;
	}

	public Object getArgument() {
		return argument;
	}	  
}

