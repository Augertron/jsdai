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

package jsdai.mappingUtils.paths;

import jsdai.lang.*;
import jsdai.SExtended_dictionary_schema.*;
import jsdai.SMapping_schema.*;

import antlr.collections.AST;
import java.util.*;

/**
 * This class contains options which are passed to mapping path walker
 * methods. The design is not perfect here and this class also contains
 * return values which should be contained in WalkerState. But the difference
 * with WalkerState is that WalkerStates are always returned in a list and
 * contains states which are passed upwards. WalkerOptions return values
 * have only local effect.
 *
 * @author  Vaidas Nargėlas
 * @version 
 */
public class WalkerOptions {
	
//	static public final int POST_PROCESS = 0;
//	static public final int PARTIAL_CONSTRAINTS = 1;

	// Input values
	boolean postProcess;
	boolean partialConstraints;
	boolean negation;
	EntityAST previousId;

	// Output values
	LinkedList constraintStateList;
	AndEndAST reachedAndEnd;
	
    public WalkerOptions() {
		this.postProcess = true;
		this.partialConstraints = false;
		this.negation = false;
		this.constraintStateList = null;
		this.reachedAndEnd = null;
    }

    public WalkerOptions(WalkerOptions other) {
		this.postProcess = other.postProcess;
		this.partialConstraints = other.partialConstraints;
		this.negation = other.negation;
		this.constraintStateList = other.constraintStateList;
		this.reachedAndEnd = other.reachedAndEnd;
    }

//	/** Creates new WalkerOptions */
//    public WalkerOptions(int flagType, boolean flag) {
//		this();
//		switch(flagType) {
//			case POST_PROCESS:
//				postProcess = flag;
//				break;
//			case PARTIAL_CONSTRAINTS:
//				partialConstraints = flag;
//				break;
//		}
//    }
//
//    public WalkerOptions(EntityAST previousId) {
//		this();
//		this.previousId = previousId;
//	}
//
//    public WalkerOptions(boolean postProcess, boolean partialConstraints) {
//		this();
//		this.postProcess = postProcess;
//		this.partialConstraints = partialConstraints;
//    }
	
}
