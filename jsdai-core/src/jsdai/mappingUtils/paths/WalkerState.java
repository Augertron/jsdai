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
 *
 * @author  Vaidas Nargėlas
 * @version 
 */
public class WalkerState {

	protected LinkedList firstList;
	protected EntityAST first;
	public AST remaining;
	public AST mappedValue;
	public MappingSemanticException notReported;
	
	/** Creates new WalkerState */
    public WalkerState() {
		first = null;
		firstList = null;
		remaining = null;
		mappedValue = null;
		notReported = null;
	}

    public WalkerState(EntityAST id) {
		firstList = null;
		first = id;
		remaining = null;
		mappedValue = null;
		notReported = null;
	}

	public WalkerState dup() {
		WalkerState dupState = new WalkerState();
		dupState.first = first;
		dupState.firstList = firstList;
		dupState.remaining = remaining;
		dupState.mappedValue = mappedValue;
		dupState.notReported = notReported;
		return dupState;
	}
	
	public void synchronizeWith(WalkerState other) throws MappingSemanticException {
		if(mappedValue == null) {
			mappedValue = other.mappedValue;
		} else if(other.mappedValue != null) {
//			try {
//				throw new Exception();
//			}
//			catch(Exception e) {
//				e.printStackTrace();
//			}
			throw new MappingSemanticException(first != null ? first : mappedValue, 
				"Too many mapped values: " + mappedValue + " and " + other.mappedValue);
		}
	}

	static public WalkerState next(Iterator iterator) {
		return (WalkerState)iterator.next();
	}
	
	static public LinkedList createList(WalkerState state) {
		LinkedList list = new LinkedList();
		list.add(state);
		return list;
	}

	static public LinkedList createList(AST remaining) {
		WalkerState state = new WalkerState();
		state.remaining = remaining;
		return createList(state);
	}
	
	static public boolean equalsAST(AST first, AST second) {
//DEBUG		System.out.println("first " + first.toStringList() + " second " + second.toStringList());
		if(first.getType() != second.getType()) return false;
		if(first instanceof EntityAST) {
			if(!(second instanceof EntityAST)) return false;
			if(!((EntityAST)first).isEqualTo((EntityAST)second)) return false;
		} else {
			if(!first.getText().equals(second.getText())) return false;
		}
		if(first instanceof ParserAST) {
			if(!(second instanceof ParserAST)) {
				return false;
			}
			if(((ParserAST)first).getInPath() != ((ParserAST)second).getInPath()) {
				return false;
			}
		}
		AST firstChild, secondChild;
		for(firstChild = first.getFirstChild(), secondChild = second.getFirstChild();
			firstChild != null && secondChild != null;
			firstChild = firstChild.getNextSibling(), secondChild = secondChild.getNextSibling()) {
				if(!equalsAST(firstChild, secondChild)) return false;
		}
		if(firstChild != null || secondChild != null) return false;
		return true;
	}

	static public void removeDuplicates(MappingPathParser parser, List states
										) throws SdaiException, MappingSemanticException {
		for(ListIterator i = states.listIterator(); i.hasNext(); ) {
			WalkerState state = (WalkerState)i.next();
			if(state != null) {
				for(ListIterator j = states.listIterator(i.nextIndex()); j.hasNext(); ) {
					WalkerState otherState = (WalkerState)j.next();
					if(otherState != null && state.equals(parser, otherState)) {
						j.set(null);
					}
				}
			}
		}
		for(ListIterator i = states.listIterator(); i.hasNext(); ) {
			if(i.next() == null) {
				i.remove();
			}
		}
	}

	public EntityAST getFirst(MappingPathParser parser)
	throws SdaiException, MappingSemanticException {
		if(firstList == null) return first;
		first = EntityAST.construct(parser, firstList, true);
		firstList = null;
		return first;
	}
	
	public void setFirst(EntityAST first) {
		firstList = null;
		this.first = first;
	}
	
	public LinkedList getFirstList() {
		return firstList;
	}
	
	public void addFirstToList(EntityAST first) throws MappingSemanticException {
		if(first.declaration.type != DictionaryDeclaration.ENTITY) {
			throw new MappingSemanticException(first, 
				"Entity definition required but defined type found: " + first.getText());
		}
		if(firstList == null) firstList = new LinkedList();
		firstList.add(first);
	}

	public void addFirstToList(LinkedList otherFirstList) {
		if(firstList == null) firstList = otherFirstList;
		firstList.addAll(otherFirstList);
	}

	public String toString() {
		return "WalkerState(first: " + (firstList != null ? firstList.toString() : String.valueOf(first)) +
			" remaining : " + (remaining != null ? remaining.toStringTree() : "null") + ")";
	}

	public boolean equals(MappingPathParser parser, WalkerState otherState
						  ) throws SdaiException, MappingSemanticException {
		if(!getFirst(parser).isEqualTo(otherState.getFirst(parser))) {
			return false;
		}
		if(remaining != otherState.remaining && (remaining == null || otherState.remaining == null
												 || !equalsAST(remaining, otherState.remaining))) {
			return false;
		}
		if(mappedValue != otherState.mappedValue && (mappedValue == null || otherState.mappedValue == null
												 || !equalsAST(mappedValue, otherState.mappedValue))) {
			return false;
		}
		return true;
	}
}
