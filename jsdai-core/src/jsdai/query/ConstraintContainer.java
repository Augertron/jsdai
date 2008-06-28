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

import java.lang.StringBuffer;
import jsdai.lang.SdaiException;
import org.w3c.dom.Node;

/**
 *
 * Created: Thu May  8 15:32:13 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public abstract class ConstraintContainer {
	protected Constraint constraints;
	protected Constraint constraintsInv;

	protected void clearConstraints() {
		constraints = null;
		constraintsInv = null;
	}

	/**
	 * Describe <code>addConstraint</code> method here.
	 *
	 * @param constraint a <code>Constraint</code> value
	 */
	protected void addConstraint(Constraint constraint) {
		if(constraints == null) {
			constraints = constraint;
			constraintsInv = constraint;
			constraint.next = constraint.prev = null;
		} else {
			constraintsInv.next = constraint;
			constraint.prev = constraintsInv;
			constraint.next = null;
			constraintsInv = constraint;
		}
	}

	/**
	 * Describe <code>removeConstraint</code> method here.
	 *
	 * @param constraint a <code>Constraint</code> value
	 */
	protected void removeConstraint(Constraint constraint) {
		for(Constraint currConstraint = constraints;
			currConstraint != constraintsInv; currConstraint = currConstraint.next) {

			if(currConstraint == constraint) {
				if(constraint.prev != null) {
					constraint.prev.next = constraint.next;
				}
				if(constraint.next != null) {
					constraint.next.prev = constraint.prev;
				}
				if(constraints == constraint) {
					constraints = constraint.next;
				}
				if(constraintsInv == constraint) {
					constraintsInv = constraint.prev;
				}
				constraint.next = constraint.prev = null;
				return;
			}
		}
	}

	public abstract String getType();

	protected abstract void parse(Node containerNode, ConstraintContainer parent,
								  Context context) throws SdaiException;

	protected abstract void execute(Context context) throws SdaiException;

	/**
	 * Parse child constraints using constraint factory.
	 *
	 * @param containerNode a <code>Node</code> value
	 * @param parent if null then <code>this</code> is considered as parent
	 *               otherwise the specified <code>parent</code>
	 * @param context a <code>Context</code> value
	 * @param split a <code>boolean</code> value
	 * @param constraintFactory a <code>ConstraintFactory</code> value
	 * @return the number of child constraints added
	 * @exception SdaiException if an error occurs
	 */
	protected void parseFromConstraintList(Node containerNode, ConstraintContainer parent,
										   Context context, boolean split,
										   ConstraintFactory constraintFactory) throws SdaiException {
		//Object consList[][]
		Context childContext = context.dup(split, false);
		for(Node constraintChild = containerNode.getFirstChild(); constraintChild != null;
			constraintChild = constraintChild.getNextSibling()) {
			if(constraintChild.getNodeType() != Node.ELEMENT_NODE) continue;
			Constraint newConstraint = constraintFactory.newConstraint(constraintChild);
// 			int classIdx = Arrays.binarySearch(consList, constraintChild.getLocalName(), this);
// 			if(classIdx < 0) {
// 				throw new SdaiException(SdaiException.FN_NAVL,
// 										formatMessage(constraintChild,
// 													  "Unrecognized constraint", null));
// 			}
// 			Constraint newConstraint;
// 			try {
// 				newConstraint = (Constraint)((Class)consList[classIdx][1]).newInstance();
// 			} catch(IllegalAccessException e) {
// 				throw new SdaiException(SdaiException.SY_ERR, e);
// 			} catch(InstantiationException e) {
// 				throw new SdaiException(SdaiException.SY_ERR, e);
// 			}
			newConstraint.parse(constraintChild, parent != null ? parent : this, childContext);
			addConstraint(newConstraint);
		}
		context.addChildContext(childContext);
	}

	protected void executeChildrenOnContext(Context childContext) throws SdaiException {
		for(Constraint constraint = constraints; constraint != null; constraint = constraint.next) {
			childContext.contextConstraint = constraint;
			try {
				constraint.execute(childContext);
			} finally {
				childContext.contextConstraint = null;
			}
		}
	}

	protected void executeChildren(Context context, boolean split) throws SdaiException {
		Context childContext = context.dup(split, false);
		executeChildrenOnContext(childContext);
		context.addChildContext(childContext);
	}

	protected static void executeChildren(ConstraintContainer constraintContainer, Context context,
										  boolean split) throws SdaiException {
		constraintContainer.executeChildren(context, split);
	}

	protected void checkSiblings(Node containerNode) throws SdaiException { }

	protected static void checkSiblings(ConstraintContainer constraintContainer,
			Node containerNode) throws SdaiException {
		constraintContainer.checkSiblings(containerNode);
	}

	protected boolean isChildValueResult(Node childNode) throws SdaiException { return false; }

	public String toString() {
		StringBuffer asString = new StringBuffer();
		asString.append("(");
		asString.append(getClass().getName());
		asString.append(":");
		for(Constraint childConstraint = constraints; childConstraint != null;
			childConstraint = childConstraint.next) {
			asString.append(" ");
			asString.append(childConstraint.toString());
		}
		asString.append(" )");
		return asString.toString();
	}

	public Constraint getConstraints(){
		return constraints;
	}

} // ConstraintContainer
