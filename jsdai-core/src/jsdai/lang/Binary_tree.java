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

class Node {

/**
	An object represented by this node of the tree.
*/
	Object content;

/**
	The left son of this node.
*/
	Node left;

/**
	The right son of this node.
*/
	Node right;

/**
	Has value 1 (respectively, 2) if the left (respectively, right) subtree 
	rooted at this node has a larger height than the right (respectively, 
	left) subtree. If the heights of both subtrees are the same, then the 
	value is 0.
*/
	int node_state;


/**
	The constructor of this class. 
*/
	Node() {
	}

/**
	The constructor of this class that attaches a submitted object to the node.
*/
	Node(Object given_content)	{
		content = given_content;
	}

}




/** JSDAI(TM), An implementation of the Standard Data Access Interface
 * for Java
 * <p>
 * Copyright 1998-2003, LKSoftWare GmbH
 */
class Binary_tree {

/**
	The root of the tree.
*/
	private Node root;

/**
	The node at which a new object into the tree is inserted. In the case when 
	the new object is already represented by some node, then this node is 
	assigned to <code>insertion_node</code>.
*/
	private Node insertion_node;

/**
	The node representing an object currently considered. 
*/
	Node reference_node;

/**
	The side (if value 1 then left, if 2 then right) to which a new node 
   representing the new object is attached to <code>insertion_node</code> 
	in the tree. 
*/
	private int insertion_side;

/**
	An auxiliary array to which a sequence of tree nodes manipulated by tree 
	balancing operation is stored.
*/
	private Object [] path;

/**
	An auxiliary array in which for each node in <code>path</code> an edge 
	outcoming from it is indicated: value 1 for the left edge, value 2 for the 
	right one. The value specifies a (unique) outcoming edge which lies on the 
	path from the node in <code>path</code> to the new node representing a 
	new object.
*/
	private int [] choices;

/**
	The length of the arrays 'path' and 'choices' declared above.
*/
	private int path_length;

/**
	Value used to set an initial size for the internal arrays 'path' and 'choices'. 
*/
	private final int TREE_HEIGHT = 32;



/**
	The constructor of this class. Used in <code>PhFileReader</code>.
*/
	Binary_tree() throws SdaiException {
		root = new Node();
		path = new Object[TREE_HEIGHT];
		choices = new int[TREE_HEIGHT];
		path_length = 0;
	}



/**
	Returns <code>true</code> if the exchange structure contains a reference 
	to an instance whose encoding is missing. The method is invoked in 
	<code>PhFileReader</code> after the exchange structure has been read.
*/
	boolean check_unresolved() throws SdaiException {
		if (root != null) {
			boolean res = check_if_node_unresolved(root);
			root = null;
			path = null;
			return res;
		} else {
			return false;
		}
	}


/**
	Makes references to non-existing (with encoding not found) entity instances unset.
*/
	private boolean check_if_node_unresolved(Node node) throws SdaiException {
		boolean found = false;
		if (node.content instanceof CLateBindingEntity) {
			CLateBindingEntity forward = (CLateBindingEntity)node.content;
			forward.changeInverseReferences(forward, null, true, false);
			found = true;
		}
		boolean left = false, right = false;
		if (node.left != null) {
			left = check_if_node_unresolved(node.left);
		}
		if (node.right != null) {
			right = check_if_node_unresolved(node.right);
		}
		return found || left || right;
	}


/**
	Checks if the instance with the specified (by the second parameter) 
	identifier is already represented by some node in the tree. If so, then 
	this node is saved as <code>insertion_node</code> and <code>true</code> 
	is returned.
*/
	private boolean insert_internal(Node current_node, long index) throws SdaiException {
		long new_index;

		if (current_node == null) {
			insertion_node = current_node;
			insertion_side = 0;
			return false;
		} else {
			new_index =
					((CEntity)current_node.content).instance_identifier;
			if (index == new_index) {
				insertion_node = current_node;
				insertion_side = 0;
				return true;
			} else {
				if (path_length >= choices.length) {
					enlarge_choices();
				}
				if (index < new_index) {
					path[path_length] = current_node;
					choices[path_length] = 1;
					path_length++;
					if (current_node.left == null) {
						insertion_node = current_node;
						insertion_side = 1;
						return false;
					}	else {
						return insert_internal(current_node.left, index);
					}
				} else {
					path[path_length] = current_node;
					choices[path_length] = 2;
					path_length++;
					if (current_node.right == null) {
						insertion_node = current_node;
						insertion_side = 2;
						return false;
					} else {
						return insert_internal(current_node.right, index);
					}
				}
			}
		}
	}


/**
	Balances the binary tree.
*/
	private void balance(int index, int trace) throws SdaiException {
		Node temp_node1, temp_node2;

		int state = ((Node)path[index]).node_state;
//Node nd = (Node)path[index];
//System.out.println("Binary_tree index: " + index +  "   state: " + state +
//"   path[index]: " + ((CEntity)nd.content).instance_identifier);
		switch (state) { 
			case 0:
				if (choices[index] == 2) {
					((Node)path[index]).node_state = 2;
					if (index > 0) {
						balance(index - 1, 2);
					}
					return;
				} else if (choices[index] == 1) {
					((Node)path[index]).node_state = 1;
					if (index > 0) {
						balance(index - 1, 1);
					}
					return;
				} else {
					throw new SdaiException(SdaiException.SY_ERR);
				}
			case 1:
				if (choices[index] == 2) {
					((Node)path[index]).node_state = 0;
					return;
				}
				if (trace == 1) {
						/*    rotation   */
					if ((Node)path[index] == root) {
						root = (Node)path[index + 1];
					}
					temp_node1 = ((Node)path[index + 1]).right;
					((Node)path[index + 1]).right = (Node)path[index];
					((Node)path[index]).left = temp_node1;
					if (index > 0) {
						if (choices[index - 1] == 1) {
							((Node)path[index - 1]).left = (Node)path[index + 1];
						} else {
							((Node)path[index - 1]).right = (Node)path[index + 1];
						}
					}
					((Node)path[index]).node_state = 0;
					((Node)path[index + 1]).node_state = 0;
				} else {
						/*    double rotation   */
					if ( (Node)path[index] == root) {
						root = (Node)path[index + 2];
					}
					temp_node1 = ((Node)path[index + 2]).left;
					temp_node2 = ((Node)path[index + 2]).right;
					((Node)path[index + 2]).left = (Node)path[index + 1];
					((Node)path[index + 2]).right = (Node)path[index];
					((Node)path[index + 1]).right = temp_node1;
					((Node)path[index]).left = temp_node2;
					if (index > 0) {
						if (choices[index - 1] == 1) {
							((Node)path[index - 1]).left = (Node)path[index + 2];
						} else {
							((Node)path[index - 1]).right = (Node)path[index + 2];
						}
					}
					if (choices[index + 2] == 2) {
						((Node)path[index]).node_state = 0;
						((Node)path[index + 1]).node_state = 1;
					} else if (choices[index + 2] == 1) {
						((Node)path[index]).node_state = 2;
						((Node)path[index + 1]).node_state = 0;
					} else {
						((Node)path[index]).node_state = 0;
						((Node)path[index + 1]).node_state = 0;
					}
					((Node)path[index + 2]).node_state = 0;
				}
				return;
			case 2:
				if (choices[index] == 1) {
					((Node)path[index]).node_state = 0;
					return;
				}
				if (trace == 2) {
							/*    rotation   */
					if ( (Node)path[index] == root) {
						root = (Node)path[index + 1];
					}
					temp_node1 = ((Node)path[index + 1]).left;
					((Node)path[index + 1]).left = (Node)path[index];
					((Node)path[index]).right = temp_node1;
					if (index > 0) {
						if (choices[index - 1] == 1) {
							((Node)path[index - 1]).left = (Node)path[index + 1];
						} else {
							((Node)path[index - 1]).right = (Node)path[index + 1];
						}
					}
					((Node)path[index]).node_state = 0;
					((Node)path[index + 1]).node_state = 0;
				} else {
							/*    double rotation   */
					if ( (Node)path[index] == root) {
						root = (Node)path[index + 2];
					}
					temp_node1 = ((Node)path[index + 2]).left;
					temp_node2 = ((Node)path[index + 2]).right;
					((Node)path[index + 2]).left = (Node)path[index];
					((Node)path[index + 2]).right = (Node)path[index + 1];
					((Node)path[index]).right = temp_node1;
					((Node)path[index + 1]).left = temp_node2;
					if (index > 0) {
						if (choices[index - 1] == 1) {
							((Node)path[index - 1]).left = (Node)path[index + 2];
						} else {
							((Node)path[index - 1]).right = (Node)path[index + 2];
						}
					}
					if (choices[index + 2] == 1) {
						((Node)path[index]).node_state = 0;
						((Node)path[index + 1]).node_state = 2;
					} else if (choices[index + 2] == 2) {
						((Node)path[index]).node_state = 1;
						((Node)path[index + 1]).node_state = 0;
					} else {
						((Node)path[index]).node_state = 0;
						((Node)path[index + 1]).node_state = 0;
					}
					((Node)path[index + 2]).node_state = 0;
				}
				return;
		} /* end of switch */
	} /* end of balance */


/**
	Checks if the instance (first parameter) with the specified (by the second 
	parameter) identifier is already represented by some node in the tree. 
	If so, then <code>true</code> is returned. If not, then a new node for 
	this instance in the binary tree is created.
*/
	boolean insert(Object object, long given_index) throws SdaiException {
		long used_index = 0;
		boolean balanc = false;
		boolean exists = false;
		int exists_int;
		if (root.content == null) {
			exists_int = -1;
			if (given_index > 0) {
				used_index = given_index;
			}
		} else {
			if (given_index >= 0) {
				used_index = given_index;
			} else if (object instanceof CLateBindingEntity) {
				used_index = ((CLateBindingEntity)object).instance_identifier;
			} else { 
				balanc = true;
				used_index = ((CEntity)object).instance_identifier;
			}
			path_length = 0;
			exists = insert_internal(root, used_index);
			exists_int = exists ? 1 : 0;
		}
		if (exists_int <= 0 && object == null) {
			object = new CLateBindingEntity();
			((CLateBindingEntity)object).instance_identifier = used_index;
		}
		if (exists_int == -1) {
			root.content = object;
			insertion_node = root;
			reference_node = root;
		} else if (!exists) {
			Node new_node = new Node(object);
			reference_node = new_node;
			if (insertion_side == 1) {
				insertion_node.left = new_node;
			} else {
				insertion_node.right = new_node;
			}
			if (path_length >= choices.length) {
				enlarge_choices();
			}
			path[path_length] = new_node;
			choices[path_length] = 0;
			path_length++;
//for (int j = 0; j < path_length; j++) {
//Node nd = (Node)path[j];
//System.out.println(" Binary_tree   j: " + j + "   path[j]: " + ((CEntity)nd.content).instance_identifier);}
//for (int j = 0; j < path_length; j++) 
//System.out.println(" Binary_tree   j: " + j + "   choices[j]: " + choices[j]);
//print_tree();
			balance(path_length - 2,0);
//check_tree2();
//check_tree();
//System.out.println(" Binary_tree    check successfull for CREATED inst: #" + given_index);
		} else {
			reference_node = insertion_node;
		}
		if (exists_int == -1) {
			exists = false;
		}
		return exists;
	}


/**
	The object of <code>CLateBindingEntity</code> representing an entity 
	instance is replaced by the submitted (through the parameter) object of 
	<code>CEntity</code>. The latter is just created upon finding an encoding 
	of the instance in the exchange structure, whereas the former was introduced 
	because a reference to the instance was found earlier (before its creation).
*/
	CLateBindingEntity replace(CEntity objec) throws SdaiException {
		CLateBindingEntity forward;
		if (insertion_node.content instanceof CLateBindingEntity) {
			forward = (CLateBindingEntity)insertion_node.content;
		} else {
			return null;
		}
		insertion_node.content = objec;
		objec.inverseList = forward.inverseList;
		forward.changeInverseReferences(forward, objec, false, false);
		return forward;
	}


/**
	Increases the size of the auxiliary arrays 'path' and 'choices' twice. 
*/
	private void enlarge_choices() {
		int new_length = choices.length * 2;
		int new_choices[] = new int[new_length];
		System.arraycopy(choices, 0, new_choices, 0, choices.length);
		choices = new_choices;
		Object new_path[] = new Object[new_length];
		System.arraycopy(path, 0, new_path, 0, path.length);
		path = new_path;
	}


/**
	A method for debugging purposes.
*/
	private void check_tree() throws SdaiException {
		if (root != null) {
			long res = check_node(root);
		}
	}


/**
	A method for debugging purposes.
*/
	private long check_node(Node current_node) throws SdaiException {
		if (current_node == null) {
			return -1;
		}
		long ident = ((CEntity)current_node.content).instance_identifier;
		long left = check_node(current_node.left);
		long right = check_node(current_node.right);
		if (left >= ident || (right > 0 && right < ident)) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (right > 0) {
			return right;
		} else {
			return ident;
		}
	}


/**
	A method for debugging purposes.
*/
	private void check_tree2() throws SdaiException {
		if (root != null) {
			check_node2(root);
		}
	}


/**
	A method for debugging purposes.
*/
	private void check_node2(Node current_node) throws SdaiException {
		if (current_node == null) {
			return;
		}
		int left_path_ln = get_path_length(current_node.left);
		int right_path_ln = get_path_length(current_node.right);
		long ident = ((CEntity)current_node.content).instance_identifier;
		switch (current_node.node_state) { 
			case 0:
				if (left_path_ln != right_path_ln) {
					System.out.println("Binary_tree ----->   violation for ident: #" + ident);
					print_tree();
					throw new SdaiException(SdaiException.SY_ERR);
				}
				break;
			case 1:
				if (left_path_ln <= right_path_ln) {
					System.out.println("Binary_tree ----->   violation for ident: #" + ident);
					print_tree();
					throw new SdaiException(SdaiException.SY_ERR);
				}
				break;
			case 2:
				if (left_path_ln >= right_path_ln) {
					System.out.println("Binary_tree ----->   violation for ident: #" + ident);
					print_tree();
					throw new SdaiException(SdaiException.SY_ERR);
				}
				break;
		}
		check_node2(current_node.left);
		check_node2(current_node.right);
	}


/**
	A method for debugging purposes.
*/
	private int get_path_length(Node current_node) throws SdaiException {
		if (current_node == null) {
			return 0;
		}
		int left_path_ln = get_path_length(current_node.left);
		int right_path_ln = get_path_length(current_node.right);
		if (left_path_ln >= right_path_ln) {
			return left_path_ln + 1;
		} else {
			return right_path_ln + 1;
		}
	}


/**
	A method for debugging purposes.
*/
	private void print_tree() throws SdaiException {
		if (root != null) {
			print_node(root);
		}
	}


/**
	A method for debugging purposes.
*/
	private void print_node(Node current_node) throws SdaiException {
		if (current_node == null) {
			return;
		}
		long ident = ((CEntity)current_node.content).instance_identifier;
		System.out.println("Binary_tree ****   this node: #" + ident + "   node_state: " + current_node.node_state);
		if (current_node.left != null) {
			long left_ident = ((CEntity)current_node.left.content).instance_identifier;
			System.out.println("Binary_tree &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&   left node: #" + left_ident);
		}
		if (current_node.right != null) {
			long right_ident = ((CEntity)current_node.right.content).instance_identifier;
			System.out.println("Binary_tree &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&   right node: #" + right_ident);
		}
		System.out.println();
		System.out.println();
		print_node(current_node.left);
		print_node(current_node.right);
	}


}
