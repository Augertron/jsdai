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

package jsdai.express_g.exp2.eg;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import jsdai.SExpress_g_schema.ARelation_placement;
import jsdai.SExpress_g_schema.ERelation_bundle;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;

/**
 * @author Mantas Balnys
 *
 */
public class RelationGroup extends HashSet implements SDAIdicSchema {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2418881895098354357L;
	protected EEntity entity = null;
	protected EEntity place = null;

	/**
	 * 
	 */
	public RelationGroup() {
		super();
	}

	/**
	 * @param arg0
	 */
	public RelationGroup(int arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public RelationGroup(int arg0, float arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public RelationGroup(Collection arg0) {
		super(arg0);
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.SDAIdicSchema#updateModel(jsdai.lang.SdaiModel, jsdai.lang.SdaiModel)
	 */
	public void updateModel(SdaiModel modelDict, SdaiModel modelEG)	throws SdaiException {
		if (size() > 1) {
			ERelation_bundle bundle = (ERelation_bundle)modelEG.createEntityInstance(ERelation_bundle.class);
			ARelation_placement rels = bundle.createMember(null);
			Iterator iter = iterator();
			while (iter.hasNext()) {
				EEntity place = ((AbstractEGRelation)iter.next()).getDefinitionPlacement(modelDict, modelEG);
				rels.addUnordered(place);
			}
			setDefinitionPlacement(bundle);
			setDefinition(bundle);
		}
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.SDAIdicSchema#getDefinition(jsdai.lang.SdaiModel, jsdai.lang.SdaiModel)
	 */
	public EEntity getDefinition(SdaiModel modelDict, SdaiModel modelEG) throws SdaiException {
		//if (entity == null) updateModel(modelDict, modelEG);
	    return entity;
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.SDAIdicSchema#getDefinitionPlacement(jsdai.lang.SdaiModel, jsdai.lang.SdaiModel)
	 */
	public EEntity getDefinitionPlacement(SdaiModel modelDict, SdaiModel modelEG) throws SdaiException {
	    if (place == null) updateModel(modelDict, modelEG);
	    return place;
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.SDAIdicSchema#clearDefinitions()
	 */
	public void clearDefinitions() {
		entity = null;
		place = null;
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.SDAIdicSchema#setDefinition(jsdai.lang.EEntity)
	 */
	public void setDefinition(EEntity entity) {
		this.entity = entity;
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.SDAIdicSchema#setDefinitionPlacement(jsdai.lang.EEntity)
	 */
	public void setDefinitionPlacement(EEntity placement) {
		place = placement;
	}

}
