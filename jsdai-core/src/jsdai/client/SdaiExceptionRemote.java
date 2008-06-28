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

package jsdai.client;

import jsdai.lang.SdaiException;
import jsdai.query.SerializableRef;


/**
 *
 * Created: Mon Oct 20 11:48:59 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public class SdaiExceptionRemote extends SdaiException {
	public static final int MO_CHG = 2000;
	public static final int MO_DEL = 2001;
	public static final int MO_ERR = 2002;
	public static final int SI_CHG = 2003;
	public static final int SI_DEL = 2004;
	public static final int EI_REF = 2005;

	private static final String[] errorDescriptions = {
		"Model was changed by another session: ",
		"Model was deleted by another session: ",
		"Model write error: ",
		"Schema instance was changed by another session: ",
		"Schema instance was deleted by another session: ",
		"Entity instance can not be deleted because it is referenced from another model in JSDAI-DB: ",
	};

	public SdaiExceptionRemote(int id, SerializableRef base, String errorMesage) {
		super(id, base, id >= 2000 ? errorDescriptions[id - 2000] + errorMesage : errorMesage, id < 2000);
	}

	protected SdaiExceptionRemote(int id, SerializableRef base, String text, boolean makeDescription) {
		super(id, base, text, makeDescription);
	}

	public final SerializableRef getErrorBaseRef() {
		return (SerializableRef)getErrorBase();
	}

} // SdaiExceptionRemote
