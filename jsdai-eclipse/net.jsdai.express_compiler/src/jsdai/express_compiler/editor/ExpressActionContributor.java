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

package jsdai.express_compiler.editor;

import jsdai.express_compiler.actions.GoToMatchingPeerAction;
import jsdai.express_compiler.actions.IExpressEditorActionDefinitionIds;
import jsdai.express_compiler.actions.SelectMatchingPeersAction;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.editors.text.TextEditorActionContributor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.RetargetTextEditorAction;

// public class ExpressActionContributor extends BasicTextEditorActionContributor {
public class ExpressActionContributor extends TextEditorActionContributor {

	protected RetargetTextEditorAction fContentAssistProposalAction;
//	protected ExpressCompilerEditorAction fCompilerAction;
	protected RetargetTextEditorAction fGotoMatchingPeer;
	protected RetargetTextEditorAction fSelectMatchingPeers;

	/**
	 * Default constructor.
	 */
	public ExpressActionContributor() {
		super();

		fContentAssistProposalAction = new RetargetTextEditorAction(ExpressEditorMessages.getResourceBundle(), "ContentAssistProposal."); //$NON-NLS-1$
//		fCompilerAction = ExpressCompilerEditorAction.getInstance();

		fGotoMatchingPeer = new RetargetTextEditorAction(ExpressEditorMessages.getResourceBundle(),  "GotoMatchingPeer."); //$NON-NLS-1$
		fGotoMatchingPeer.setActionDefinitionId(IExpressEditorActionDefinitionIds.GOTO_MATCHING_PEER);

		fSelectMatchingPeers = new RetargetTextEditorAction(ExpressEditorMessages.getResourceBundle(),  "SelectMatchingPeers."); //$NON-NLS-1$
		fSelectMatchingPeers.setActionDefinitionId(IExpressEditorActionDefinitionIds.SELECT_MATCHING_PEERS);

	}

	/*
	 * @see IEditorActionBarContributor#init(IActionBars)
	 */
	public void init(IActionBars bars) {
		super.init(bars);
		
		IMenuManager menuManager= bars.getMenuManager();
		IMenuManager editMenu= menuManager.findMenuUsingPath(IWorkbenchActionConstants.M_EDIT);
		if (editMenu != null) {
			editMenu.add(new Separator());
			editMenu.add(fContentAssistProposalAction);
		}	
		
	}

	private void doSetActiveEditor(IEditorPart part) {
		super.setActiveEditor(part);

		ITextEditor editor= null;
		if (part instanceof ITextEditor)
			editor= (ITextEditor) part;

		fGotoMatchingPeer.setAction(getAction(editor, GoToMatchingPeerAction.GOTO_MATCHING_PEER));
		fSelectMatchingPeers.setAction(getAction(editor, SelectMatchingPeersAction.SELECT_MATCHING_PEERS));
		fContentAssistProposalAction.setAction(getAction(editor, "ContentAssistProposal")); //$NON-NLS-1$
//		fCompilerAction.setEditor(editor);
//		fCompilerAction.update();
	}

	/*
	 * @see IEditorActionBarContributor#setActiveEditor(IEditorPart)
	 */
	public void setActiveEditor(IEditorPart part) {
		super.setActiveEditor(part);
		doSetActiveEditor(part);
	}
	
	/*
	 * @see IEditorActionBarContributor#dispose()
	 */
	public void dispose() {
		doSetActiveEditor(null);
		super.dispose();
	}


	public void contributeToMenu(IMenuManager menu) {

		super.contributeToMenu(menu);

		IMenuManager editMenu= menu.findMenuUsingPath(IWorkbenchActionConstants.M_EDIT);
		if (editMenu != null) {

			MenuManager structureSelection= new MenuManager("E&xpand Selection To", "expandSelection"); //$NON-NLS-1$
			editMenu.insertAfter(ActionFactory.SELECT_ALL.getId(), structureSelection);
			structureSelection.add(fSelectMatchingPeers);

//			editMenu.appendToGroup(IContextMenuConstants.GROUP_GENERATE, fRetargetShowJavaDoc);
		}

		IMenuManager gotoMenu= menu.findMenuUsingPath("navigate/goTo"); //$NON-NLS-1$
		if (gotoMenu != null) {
			gotoMenu.add(new Separator("additions2"));  //$NON-NLS-1$
			gotoMenu.appendToGroup("additions2", fGotoMatchingPeer); //$NON-NLS-1$
		}

	}
}
