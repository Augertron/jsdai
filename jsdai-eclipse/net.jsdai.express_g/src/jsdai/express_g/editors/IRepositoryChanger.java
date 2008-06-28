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

package jsdai.express_g.editors;

/**
 * @author Mantas Balnys
 *
 * Repository Changer
 * should use model definition names for data recognition
 * eg. constructor RC(String schemaName, String LayoutName);
 */
public interface IRepositoryChanger {
	/**
	 * Asks this changer to end work with repository
	 * if asking failes no action on repository should be taken.
	 * May use user dialog. Should return null if work hasn't started
	 * @return null if finished, error message otherwise
	 */
	public String askToEnd();
	
	/**
	 * Forces this changer to end work with repository
	 * should be invoked after failing to end by askToEnd();
	 * and only if this changer has lower priority
	 *
	 */
	public void forceToEnd();
	
	/**
	 * priority of changer
	 * can't stop changer with lower priority,
	 * asks to end if has the same priority
	 * @return
	 *
	public int getPriority();
	
	public final int PRIORITY_CRITICAL = 1; // may use save operation
	public final int PRIORITY_NORMAL = 3; // may use any standart operation
	public final int PRIORITY_LOW = 5;
	/**/
	
	/**
	 * Returns names of accesed models. Method result shouldn't change till end of job
	 * @return
	 */
	public String[] getModels();
}
