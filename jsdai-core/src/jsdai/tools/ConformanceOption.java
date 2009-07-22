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

package jsdai.tools;

import java.util.ArrayList;
import java.util.List;

public class ConformanceOption {
//	/* Remove this comment is you want to use this class
	private String id;
	// stuff, which is needed because of possiblity to refer one CO from another CO 
	private List cos;
	private List armEntities; 
	private List mimEntities;
	private boolean fullyProcessed;

	public ConformanceOption(String coId) {
		this.id = coId;
		cos = new ArrayList();
		armEntities = new ArrayList();
		mimEntities = new ArrayList();
		setFullyProcessed(false);
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
	public String toString(){
		return "co with id "+id;
	}

	public void addCo(String co) {
		cos.add(co);
	}

	public List getCos() {
		return cos;
	}

	public void addArmEntity(String entity) {
		armEntities.add(entity);
	}

	public List getArmEntities() {
		return armEntities;
	}

	public void addMimEntity(String entity) {
		mimEntities.add(entity);
	}

	public List getMimEntities() {
		return mimEntities;
	}

	public boolean containsCOs(){
		return (cos.size() > 0);
	}

	public void setFullyProcessed(boolean fullyProcessed) {
		this.fullyProcessed = fullyProcessed;
	}

	public boolean isFullyProcessed() {
		return fullyProcessed;
	}

	public void processCoReference(ConformanceOption coRef) {
		// TODO - not sure if dublicates are correctly handled here.
		List coRefArmEntities = coRef.getArmEntities();
		armEntities.addAll(coRefArmEntities);
		List coRefMimEntities = coRef.getMimEntities();
		mimEntities.addAll(coRefMimEntities);
	}
//	*/
}
