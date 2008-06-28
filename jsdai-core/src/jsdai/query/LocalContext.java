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

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import jsdai.dictionary.EEntity_definition;
import jsdai.lang.AEntity;
import jsdai.lang.CEntityDefinition;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.mapping.EEntity_mapping;
import org.w3c.dom.Node;

/**
 *
 * Created: Fri May  9 12:21:48 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

class LocalContext extends Context implements ConstraintFactory {
	//protected Set currentTypes;
	/** Contains Sets of EEntity_definition */
	protected List currentTypes;

	protected List values;

	protected LocalContext(LocalSdaiQuery query, boolean executeContext) throws SdaiException {
		super(query);
		currentTypes = new ArrayList();
		Set currentTypeSet = new HashSet();
		if(executeContext) {
			if(query.querySourceInstances != null) {
				SdaiIterator instIter = query.querySourceInstances.createIterator();
				while(instIter.next()) {
					EEntity instance = query.querySourceInstances.getCurrentMemberEntity(instIter);
					currentTypeSet.add(entityInstance(instance));
				}
			} else {
				SdaiIterator domainIter = query.domain.createIterator();
				while(domainIter.next()){
					AEntity allInstances = query.domain.getCurrentMember(domainIter).getInstances();
					SdaiIterator instIter = allInstances.createIterator();
					while(instIter.next()) {
						EEntity instance = allInstances.getCurrentMemberEntity(instIter);
						currentTypeSet.add(entityInstance(instance));
					}
				}
			}
		} else {
			currentTypeSet.add(LocalSdaiQuery.allTypesToken);
		}
		currentTypes.add(currentTypeSet);
		values = null;
	}

	protected LocalContext(LocalContext other, boolean split) {
		super(other);
		currentTypes = new ArrayList();
		Iterator ctsIter = other.currentTypes.iterator();
		if(split) {
			while(ctsIter.hasNext()) {
				Iterator tSetIter = ((Set)ctsIter.next()).iterator();
				while(tSetIter.hasNext()) {
					Set newSet = new HashSet();
					newSet.add(tSetIter.next());
					currentTypes.add(newSet);
				}
			}
		} else {
			while(ctsIter.hasNext()) {
				currentTypes.add(new HashSet((Collection)ctsIter.next()));
			}
		}
	}

	protected LocalContext(LocalContext other, List values) {
		super(other);
		this.values = new ArrayList(values);
	}

	protected LocalSdaiQuery getLocalQuery() {
		return (LocalSdaiQuery)query;
	}

// 	protected AEntity getResultInstances() throws SdaiException {
// 		AEntity resultInstances = new AEntity();
// 		int resInstIdx = 1;
// 		Iterator ctsIter = currentTypes.iterator();
// 		while(ctsIter.hasNext()) {
// 			Iterator tSetIter = ((Set)ctsIter.next()).iterator();
// 			while(tSetIter.hasNext()) {
// 				resultInstances.addByIndex(resInstIdx++, (EEntity)tSetIter.next());
// 			}
// 		}
// 		return resultInstances;
// 	}

	protected List getResultInstances() throws SdaiException {
		List resultInstances = new ArrayList();
		Iterator ctsIter = currentTypes.iterator();
		while(ctsIter.hasNext()) {
			Set tSet = (Set)ctsIter.next();
			resultInstances.addAll(tSet);
		}
		return resultInstances;
	}

	protected List getValueList() {
		return values;
	}

	protected void clearState() {
		Iterator ctsIter = currentTypes.iterator();
		while(ctsIter.hasNext()) {
			Set cType = (Set)ctsIter.next();
			cType.clear();
		}
	}


	protected void childUnion() {
		clearState();
		for(Context currContext = childContext; currContext != null; currContext = currContext.next) {
			addUnion(currContext);
		}
		childContext = null;
	}

	protected void childIntersect() {
		clearState();
		boolean firstChild = true;
		for(Context currContext = childContext; currContext != null; currContext = currContext.next) {
			if(firstChild) {
				addUnion(currContext);
				firstChild = false;
			} else {
				addIntersect(currContext);
			}
		}
		childContext = null;
	}

	protected void childAnd() throws SdaiException {
		BitSet childStatus = null;
		for(Context currContext = childContext; currContext != null; currContext = currContext.next) {
			int childIdx = 0;
			BitSet newChildStatus = new BitSet();
			Iterator ctsIter = ((LocalContext)currContext).currentTypes.iterator();
			while(ctsIter.hasNext()) {
				Set cType = (Set)ctsIter.next();
				if(!cType.isEmpty()) {
					newChildStatus.set(childIdx);
				}
				childIdx++;
			}
			if(childStatus == null) {
				childStatus = newChildStatus;
			} else {
				childStatus.and(newChildStatus);
			}
		}
		endLogical(childStatus);
		childContext = null;
	}

	protected void childOr() {
		BitSet childStatus = null;
		for(Context currContext = childContext; currContext != null; currContext = currContext.next) {
			int childIdx = 0;
			BitSet newChildStatus = new BitSet();
			Iterator ctsIter = ((LocalContext)currContext).currentTypes.iterator();
			while(ctsIter.hasNext()) {
				Set cType = (Set)ctsIter.next();
				if(!cType.isEmpty()) {
					newChildStatus.set(childIdx);
				}
				childIdx++;
			}
			if(childStatus == null) {
				childStatus = newChildStatus;
			} else {
				childStatus.or(newChildStatus);
			}
		}
		endLogical(childStatus);
		childContext = null;
	}

	protected void childNot() {
		if(childContext != null) {
			Iterator ctsChildIter = ((LocalContext)childContext).currentTypes.iterator();
			Iterator ctsIter = currentTypes.iterator();
			while(ctsIter.hasNext()) {
				Set cType = (Set)ctsIter.next();
				Iterator cTypeIter = cType.iterator();
				while(cTypeIter.hasNext()) {
					cTypeIter.next();
					Set cChildType = (Set)ctsChildIter.next();
					if(!cChildType.isEmpty()) {
						cTypeIter.remove();
					}
				}
			}
			childContext = null;
		} else {
			Iterator ctsIter = currentTypes.iterator();
			while(ctsIter.hasNext()) {
				Set cType = (Set)ctsIter.next();
				cType.clear();
			}
		}
	}

	protected void childProcess() {
		if(childContext != null) {
			Iterator ctsChildIter = ((LocalContext)childContext).currentTypes.iterator();
			Iterator ctsIter = currentTypes.iterator();
			while(ctsIter.hasNext()) {
				Set cType = (Set)ctsIter.next();
				Iterator cTypeIter = cType.iterator();
				while(cTypeIter.hasNext()) {
					cTypeIter.next();
					Set cChildType = (Set)ctsChildIter.next();
					if(cChildType.isEmpty()) {
						cTypeIter.remove();
					}
				}
			}
			childContext = null;
		}
	}

	protected void addUnion(Context childContext) {
		LocalContext childLocalContext = (LocalContext)childContext;
		Iterator ctsIter = currentTypes.iterator();
		Iterator ctsChildIter = childLocalContext.currentTypes.iterator();
		while(ctsIter.hasNext()) {
			Set cType = (Set)ctsIter.next();
			Set childCType = (Set)ctsChildIter.next();
			cType.addAll(childCType);
		}
	}

	protected void addIntersect(Context childContext) {
		LocalContext childLocalContext = (LocalContext)childContext;
		Iterator ctsIter = currentTypes.iterator();
		Iterator ctsChildIter = childLocalContext.currentTypes.iterator();
		while(ctsIter.hasNext()) {
			Set cType = (Set)ctsIter.next();
			Set childCType = (Set)ctsChildIter.next();
			cType.retainAll(childCType);
		}
	}

	protected void endLogical(BitSet childStatus) {
		int childIdx = 0;
		Iterator ctsIter = currentTypes.iterator();
		while(ctsIter.hasNext()) {
			Set cType = (Set)ctsIter.next();
			Iterator cTypeIter = cType.iterator();
			while(cTypeIter.hasNext()) {
				cTypeIter.next();
				if(!childStatus.get(childIdx++)) {
					cTypeIter.remove();
				}
			}
		}
	}

	protected ConstraintFactory getRegConstraintFactory() {
		return this;
	}

	protected Context dup(boolean split, boolean startItems) {
		return new LocalContext(this, split);
	}

	protected Context dupVal() {
		return new LocalContext(this, values);
	}

	protected void assign(Context other) throws SdaiException {
		super.init(other);
		LocalContext localOther = (LocalContext)other;
		currentTypes = new ArrayList();
		Iterator ctsIter = localOther.currentTypes.iterator();
		while(ctsIter.hasNext()) {
			currentTypes.add(new HashSet((Collection)ctsIter.next()));
		}
		values = localOther.values != null ? new ArrayList(localOther.values) : null;
	}

	protected boolean isNarrowedBy(Context other) throws SdaiException {
		LocalContext localOther = (LocalContext)other;
		Iterator ctsIter = currentTypes.iterator();
		while(ctsIter.hasNext()) {
			Set cType = (Set)ctsIter.next();
			Iterator currTypeIter = cType.iterator();
			while(currTypeIter.hasNext()) {
				Object currTypeObject = currTypeIter.next();
				if(currTypeObject == LocalSdaiQuery.allTypesToken) {
					return true;
				}
				EEntity_definition currType = LocalContext.getSchemaEntityType(currTypeObject);
				Iterator otherCtsIter = localOther.currentTypes.iterator();
				while(otherCtsIter.hasNext()) {
					Set otherCurrTypes = (Set)otherCtsIter.next();
					Iterator otherCurrTypeIter = otherCurrTypes.iterator();
					while(otherCurrTypeIter.hasNext()) {
						Object otherCurrTypeObject = otherCurrTypeIter.next();
						if(otherCurrTypeObject == LocalSdaiQuery.allTypesToken) {
							return false;
						}
						EEntity_definition otherCurrType = 
							LocalContext.getSchemaEntityType(otherCurrTypeObject);
						if(otherCurrType == currType 
						   || ((CEntityDefinition)otherCurrType).isSubtypeOf(currType)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public Constraint newConstraint(Node constraint) throws SdaiException {
		ConstraintFactory specificFactory = 
			(ConstraintFactory)query.getNamespaceHandler(constraint.getNamespaceURI());
		if(specificFactory == null) {
			throw new SdaiException(SdaiException.FN_NAVL,
									SdaiQueryEngine.formatMessage(constraint, "Unknown namespace: ",
																  constraint.getNamespaceURI()));
		}
		return specificFactory.newConstraint(constraint);
	}

	static Object entityType(EEntity_definition entityDefinition) throws SdaiException {
		return new EntityType(entityDefinition);
	}

	static Object entityType(EEntity_mapping entityMapping) throws SdaiException {
		return new EntityMappingType(entityMapping);
	}

	static EEntity_definition getEntityType(Object entityObject, boolean meta) throws SdaiException {
		if(entityObject instanceof EntityType) {
			return ((EntityType)entityObject).entityDefinition;
		} else if(entityObject instanceof EntityMappingType) {
			EntityMappingType entityMappingType = (EntityMappingType)entityObject;
			return meta 
				? entityMappingType.entityMapping.getSource(null) 
				: (EEntity_definition)entityMappingType.entityMapping.getTarget(null);
		}
		throw new SdaiException(SdaiException.SY_ERR, "Unknown object: " + entityObject);
	}

	static EEntity_definition getSchemaEntityType(Object entityObject) throws SdaiException {
		if(entityObject instanceof EntityType) {
			return ((EntityType)entityObject).entityDefinition;
		} else if(entityObject instanceof EntityMappingType) {
			return (EEntity_definition)((EntityMappingType)entityObject).entityMapping.getTarget(null);
		}
		throw new SdaiException(SdaiException.SY_ERR, "Unknown object: " + entityObject);
	}

	static EEntity_definition getMetaEntityType(Object entityObject) throws SdaiException {
		if(entityObject instanceof EntityType) {
			return ((EntityType)entityObject).entityDefinition;
		} else if(entityObject instanceof EntityMappingType) {
			return ((EntityMappingType)entityObject).entityMapping.getSource(null);
		}
		throw new SdaiException(SdaiException.SY_ERR, "Unknown object: " + entityObject);
	}

	static Object entityInstance(EEntity instance) throws SdaiException {
		return instance;
	}

	static EEntity getEntityInstance(Object entityObject) throws SdaiException {
		return (EEntity)entityObject;
	}

	public String toString() {
		return "LocalContext { currentTypes: " + currentTypes + " values: " + values + " }";
	}

// 	protected String debug() {
// 		return Integer.toString(currentTypes.size());
// 	}

	private static class EntityType {
		private EEntity_definition entityDefinition;

		private EntityType(EEntity_definition entityDefinition) {
			this.entityDefinition = entityDefinition;
		}
		
		public int hashCode() {
			return entityDefinition.hashCode();
		}

		public boolean equals(Object other) {
			try {
				if(other instanceof EntityType) {
					return entityDefinition == ((EntityType)other).entityDefinition;
				} else /*if(other instanceof EntityMappingType)*/ {
					return entityDefinition == ((EntityMappingType)other).entityMapping.getTarget(null);
				}
			} catch(SdaiException e) {
				IllegalStateException wrapper = new IllegalStateException();
				wrapper.initCause(e);
				throw wrapper;
			}
		}

		public String toString() {
			return "EntityType { " + entityDefinition + " }";
		}
	}

	private static class EntityMappingType {
		private EEntity_mapping entityMapping;

		private EntityMappingType(EEntity_mapping entityMapping) {
			this.entityMapping = entityMapping;
		}
		
		public int hashCode() {
			try {
				return entityMapping.getTarget(null).hashCode();
			} catch(SdaiException e) {
				IllegalStateException wrapper = new IllegalStateException();
				wrapper.initCause(e);
				throw wrapper;
			}
		}

		public boolean equals(Object other) {
			try {
				if(other instanceof EntityType) {
					return entityMapping.getTarget(null) == ((EntityType)other).entityDefinition;
				} else /*if(other instanceof EntityMappingType)*/ {
					return entityMapping == ((EntityMappingType)other).entityMapping;
				}
			} catch(SdaiException e) {
				IllegalStateException wrapper = new IllegalStateException();
				wrapper.initCause(e);
				throw wrapper;
			}
		}
	}

} // LocalContext
