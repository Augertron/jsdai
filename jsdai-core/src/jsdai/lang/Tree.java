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

import java.util.*;

/**
 * Generic class for "tree" structure.
 */
class Tree {
	Object node;
	List children;
	
	/**
	 * Constructor - initializes node to null and creates empty list for children storage
	 */
	 public Tree() {
		node = null;
		children = new LinkedList();
	}
	
	/**
	 * Returns boolean value if there are any children added to children list
	 */
	boolean containsChildren() {
		return (!children.isEmpty());
	}
	
	/**
	 * Returns the size of children list - their count
	 */
	int childrenCount() {
		return children.size();
	}
	
	/**
	 * Getter for the child in children list (accessed by index)
	 */
	Tree child(int index) {
		return (Tree) children.get(index);
	}
	
	/**
	 * Creates new Tree node, links object to it, adds this new Tree object to children list and returns it from method.
	 */
	Tree addChild(Object childNode) {
		Tree child = new Tree();
		child.node = childNode;
		children.add(child);
		return child;
	}
	
	/**
	 * Returns the object value of Tree element (always empty for tree root)
	 */
	Object getNode() {
		return node;
	}
}


