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

package jsdai.beans;

import jsdai.lang.*;
import java.util.*;

public interface SdaiSelectable {

	public static int mNOT_USE = 0;
	public static int mSET_UNSET = 1;
	public static int mCREATE_DELETE = 2;
	public static int mADD_REMOVE = 3;
	
	/**Return string representation of this element in session instances tree.*/
	public String getTreeLeave() throws SdaiException;
	/**Return value of selected object.*/
	public int getMode() throws SdaiException;
	/**Return currently selected value(s).*/
	public void getSelected(Vector result) throws SdaiException;
	/**Set selected object value(s).*/
	public void setSelected(Vector agg) throws SdaiException;
	/**Return is currently something selected.*/
	public boolean isSelected() throws SdaiException;
	
	/**Add SdaiSelectableListener listener.*/
	public void addSdaiSelectableListener(SdaiSelectableListener l);
	/**Remove SdaiSelectableListener listener.*/
	public void removeSdaiSelectableListener(SdaiSelectableListener l);
	/**SdaiSelectionChanged is processed to all SdaiSelectableListener listeners.*/
	public void fireSdaiSelectionChanged();
	/**SdaiSelectionProcessed is processed to all SdaiSelectableListener listeners.*/
	public void fireSdaiSelectionProcessed();
}