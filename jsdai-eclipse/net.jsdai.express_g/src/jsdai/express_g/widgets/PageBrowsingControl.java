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

package jsdai.express_g.widgets;

import jsdai.express_g.editors.ExpressGEditor;
import jsdai.express_g.editors.IPageControl;
import jsdai.express_g.editors.PageHandlingContextMenu;
import jsdai.express_g.exp2.EGToolKit;
import jsdai.express_g.exp2.Paging;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.VisualPage;
import jsdai.express_g.exp2.ui.command.UpdateCommand;
import jsdai.express_g.exp2.ui.event.PageChangeEvent;
import jsdai.express_g.exp2.ui.event.PageListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/**
 * @author Mantas Balnys
 *
 */
public class PageBrowsingControl extends Composite {
	protected Table table;
	protected int lastSelection = 1; // first line of table is selected by default

	protected TableEditor editor;
	// editing the second column
	protected static final int EDITABLECOLUMN = 1;
	
	protected PropertySharing prop = null;
	protected IPageControl internalEditor = null;
	protected int egEditorPage = -1;

	/**
	 * @param parent
	 * @param style
	 */
	public PageBrowsingControl(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout());
		table = new Table(this, SWT.SINGLE | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn index = new TableColumn(table, SWT.RIGHT);
		index.setText("No.");
		index.pack();
		TableColumn name = new TableColumn(table, SWT.LEFT);
		name.setText("Page name");
		name.setWidth(index.getWidth() * 12);

		// creating cell editor
		editor = new TableEditor(table);
		//The editor must have the same size as the cell and must
		//not be any smaller than 50 pixels.
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		editor.minimumWidth = 50;
		
		// listeners
		table.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			
			public void widgetSelected(SelectionEvent e) {
				// Clean up any previous editor control
				disposeEditor(true);
				
				// Identify the selected row
				TableItem item = (TableItem)e.item;
				Object obj = item.getData();
				VisualPage vp = null;
				if (obj instanceof VisualPage) vp = (VisualPage)obj;
				
				if ((vp != null) && (vp.getPage() == lastSelection)) {
					// The control that will be the editor must be a child of the Table
					Text newEditor = new Text(table, SWT.NONE);
					newEditor.setText(item.getText(EDITABLECOLUMN));
					newEditor.addKeyListener(new KeyAdapter() {
						public void keyPressed(KeyEvent e) {
//							Text text = (Text)editor.getEditor();
							if (e.character == SWT.ESC) { // cancel editing
								disposeEditor(false);
							} else if (e.character == '\r') { // accept changes (RR)
								disposeEditor(true);
							} 
						}
					});
					newEditor.addFocusListener(new FocusAdapter() {
						/* (non-Javadoc)
						 * @see org.eclipse.swt.events.FocusAdapter#focusLost(org.eclipse.swt.events.FocusEvent)
						 */
						public void focusLost(FocusEvent e) {
							disposeEditor(true);
						}
					});
					newEditor.selectAll();
					newEditor.setFocus();
					editor.setEditor(newEditor, item, EDITABLECOLUMN);
				} else {
					lastSelection = vp == null ? 0 : vp.getPage();
		
				}
			}
		});	

		table.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent e) {
				// Update selection
				if (prop != null) {
					int nr = table.getSelectionIndex() + 1;
					if (nr > 0)	prop.handler().setPage(nr);
				}

				lastSelection = -1;
				disposeEditor(true);

				if (internalEditor != null) {
					internalEditor.setActivePage(egEditorPage);
				}
			}
		});
	}
	
	void disposeEditor(boolean saveState) {
		Text text = (Text)editor.getEditor();
		if (text != null && !text.isDisposed()) {
			if (saveState) {
				String name = text.getText();
				TableItem item = editor.getItem();
				item.setText(EDITABLECOLUMN, name);
				VisualPage vp = (VisualPage)item.getData();
				vp.setName(name);
				pageChanged();	// RR - attempting to update page numbers immediately - ok working in Info Editor when switching between @ and non-@
				// ExpressGEditer 
//System.out.println("<disposeEditor> prop: " + prop);
				prop.getExpressGEditor().updateTabNames();  // RR - updates page numbers at the bottom, working ok when switching between @ and non-@
//				prop.getExpressGEditor().updateModifiedStatus();
				//prop.getExpressGEditor().reInitEditor(); // RR - creates empty pages
//				prop.setModified(true);

//					application.handler().startCommand(new UpdateCommand(ExpressGEditor.this));
					prop.handler().startCommand(new UpdateCommand((ExpressGEditor)prop.getExpressGEditor()));


				// another attempt
/*
				for (int i = 1; i <= prop.handler().getMaxPage(); i++) {
					//prop.handler().update(i);
	  			// maybe not everything needs to be inside the loop
//	  	    EGToolKit.PageRef.changePage(prop, prop.handler().drawable(i), Paging.INVISIBLE_PAGE);
	  	    EGToolKit.PageRef.changePage(prop, prop.handler().drawable(i), Paging.ANY_PAGE);
					prop.handler().update(i);
	  	    //EGToolKit.PageRef.changePage(prop, prop.handler().drawable(i), i);
//  	  	  prop.handler().update(prop.handler().getPage());
	
		    	prop.handler().update(prop.handler().getPage());
  	  	}
  		  prop.handler().repaint(true);
*/	
				// end of another attempt
				
			}
			text.dispose();
		}
	}
	
	private PageListener internalPageListener = null;
	
	public void setProperties(PropertySharing prop) {
		if (this.prop != prop) {
			if (this.prop != null) {
				this.prop.handler().removePageListener(internalPageListener);
			}
			this.prop = prop;
			
			if (this.prop != null) {
				internalPageListener = new PageListener() {
					/* (non-Javadoc)
					 * @see jsdai.express_g.exp2.ui.event.PageListener#pageChanged(jsdai.express_g.exp2.ui.event.PageChangeEvent)
					 */
					public void pageChanged(PageChangeEvent e) {
						PageBrowsingControl.this.pageChanged();
					}
				};
				prop.handler().addPageListener(internalPageListener);
				
				// add context menu
				new PageHandlingContextMenu(table, prop);
			} else {
				internalPageListener = null;
			}

			pageChanged();
		}
	}
	
	public void setInternalEditor(IPageControl internalEditor, int egEditorPage) {
		this.egEditorPage = egEditorPage;
		if (this.internalEditor != internalEditor) {
			this.internalEditor = internalEditor; 
			setProperties(internalEditor.getProperties());
		}
	}

	protected void pageChanged() {
		table.removeAll();
		if (prop != null) {
			for (int i = 1; i <= prop.handler().getMaxPage(); i++) {
				TableItem item = new TableItem(table, SWT.NONE);
				VisualPage vp = prop.handler().getVisualPage(i);
				item.setText(0, EGToolKit.renumberInTab(prop, i));
				item.setText(1, vp.getName());
				item.setData(vp);
//System.out.println("<pageChanged> i: " + i + ", number: " + EGToolKit.renumberInTab(prop, i) + ", name: " + vp.getName());
			}
			table.setSelection(prop.handler().getPage() - 1);
		}
	}
}
