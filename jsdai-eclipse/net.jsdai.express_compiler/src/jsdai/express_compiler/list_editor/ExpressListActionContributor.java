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

package jsdai.express_compiler.list_editor;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.editors.text.TextEditorActionContributor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.RetargetTextEditorAction;

// public class ExpressActionContributor extends BasicTextEditorActionContributor {
public class ExpressListActionContributor extends TextEditorActionContributor {

	protected RetargetTextEditorAction fContentAssistProposalAction;
//	protected ExpressCompilerEditorAction fCompilerAction;

	/**
	 * Default constructor.
	 */
	public ExpressListActionContributor() {
		super();
		fContentAssistProposalAction = new RetargetTextEditorAction(ExpressListEditorMessages.getResourceBundle(), "ContentAssistProposal."); //$NON-NLS-1$
//		fCompilerAction = ExpressCompilerEditorAction.getInstance();
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
		
		IToolBarManager toolBarManager= bars.getToolBarManager();
		if (toolBarManager != null) {
			toolBarManager.add(new Separator());
//			toolBarManager.add(fCompilerAction);
//			fCompilerAction.setHelpContextId(ExpressCompilerPlugin.EXPRESS_COMPILER_PLUGIN_ID + ".CompileFileContextId");
//			HelpListener xx = fCompilerAction.getHelpListener();

//			.addHelpListener();
			
			//			toolBarManager.appendToGroup("Express",fCompilerAction);
//			toolBarManager.appendToGroup("net.jsdai.express_compiler.actionSet",fCompilerAction);
//			toolBarManager.insertAfter("net.jsdai.express_compiler.actions.CompileAction",fCompilerAction);
		}
	}

	private void doSetActiveEditor(IEditorPart part) {
		super.setActiveEditor(part);

		ITextEditor editor= null;
		if (part instanceof ITextEditor)
			editor= (ITextEditor) part;

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
}

