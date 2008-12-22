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

package jsdai.libutil;

/**
 * @author Giedrius Liutkus
 *
 */
import jsdai.lang.*;

/**
 * A common supertype for XIM entities which are NOT subtypes of MIM entities.
 * They have to be handeled differently therefore this interfaces is a 'flag' to distinguish them from regular entities
 *
 * @author <a href="mailto:giedrius.liutkus@lksoft.lt">Giedrius Liutkus</a>
 * @version $Revision$
 */
public interface XimEntityStandalone{

	/**
	 * Creates AIM instance if needed (which is in this case not instance itself), otherwise return existing instance
	 */
	public EEntity getAimInstance(SdaiContext context) throws SdaiException;

	/**
	 * Deletes link to AIM instance if needed
	 */
	public void unsetAimInstance(SdaiContext context) throws SdaiException;

}
