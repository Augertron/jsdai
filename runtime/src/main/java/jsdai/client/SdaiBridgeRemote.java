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

/**
 * This interface represents a remote Server (bridge) through
 * which remote repositories and their contents can be accessed.
 * The whole transaction reponsibility and multi-user looking is
 * the task of the remote server.
 * An SdaiSession has at most one SdaiBridgeRemote.
 * All request for remote repositores are managed by this one SdaiBridgeRemote.
 */
public interface SdaiBridgeRemote {

  public void initBridge(String bridgeURL) throws SdaiException;

  /*
   * see SdaiTransaction.comitt().
   * returns null on success, otherwise some error description.
   * ... when update things ....
   */

  public SessionRemote initUser(String userName, String password) throws SdaiException;

}
