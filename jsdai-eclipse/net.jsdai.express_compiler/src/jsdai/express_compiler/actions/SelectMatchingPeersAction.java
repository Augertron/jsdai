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

import jsdai.express_compiler.editor.ExpressEditor;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.Assert;

public class SelectMatchingPeersAction extends Action {

  public final static String SELECT_MATCHING_PEERS = "SelectMatchingPeers"; //$NON-NLS-1$

  private final ExpressEditor fEditor;

  public SelectMatchingPeersAction(ExpressEditor editor) {

//    super(JavaEditorMessages.GotoMatchingBracket_label);
		super("Matching Pair Range");
//		System.out.println("Sel 2");
    Assert.isNotNull(editor);
    fEditor= editor;
    setEnabled(true);
//    PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IExpressHelpContextIds.GOTO_MATCHING_PEER_ACTION);
  }

  public void run() {
//System.out.println("Sel 3");
    fEditor.selectMatchingPeers();
  }
}
