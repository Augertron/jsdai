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

/** Implementation of SessionEntity */
abstract class SdaiCommon {

	static final int feature_level = 1; // 0 is for version 1.0, and 1 is for version 1.1
// level 0 features:
//  - only one repository is open at a time;
//  - only one model exists at a time;
//  - transaction level 1;
//  - only one application schema;
//  - no inter-model references;
//  - implementation class 1;

// level 1 features (perhaps feature_level should be removed on this level):
//  - many repositories open at a time;
//  - many models in a repository;
//  - inter-model references within repository;
//  - inter-model references between several repositories;
//  - several application schemas;
//  - domain equivalence;
//  - transaction level 3;
//  - implementation class 5;

	// this is the main global object for JSDAI synchronization
	static final Object syncObject = new Object();

	abstract SdaiCommon getOwner();
	abstract void modified() throws SdaiException;

}
