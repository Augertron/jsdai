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

package jsdai.express_compiler.actions;

import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;

public interface IExpressEditorActionDefinitionIds extends ITextEditorActionDefinitionIds {

  public static final String GOTO_MATCHING_PEER = "net.jsdai.express_compiler.editor.goto.matching.peer"; //$NON-NLS-1$
  public static final String SELECT_MATCHING_PEERS = "net.jsdai.express_compiler.editor.select.matching.peers"; //$NON-NLS-1$

}
