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

package jsdai.express_g.preferences;

import jsdai.express_g.SdaieditPlugin;
import jsdai.express_g.common.StaticTools;
import jsdai.express_g.editors.ExpressGEditor;
import jsdai.express_g.editors.SdaiEditor;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.VisualPage;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * @author Mantas Balnys
 *
 */
public class EGEPageFrame extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {
	private IPreferenceStore store = new PreferenceStore(SdaieditPlugin.getDefault().getStateLocation().append("pref.tmp").toOSString());
	private PropertySharing prop = null;
	private String default_page_mask;
	private String page_mask;
	private Point size;
	private Rectangle margins;
	private static final String PROP_USE_DEFAULT_MASK = "Use_default_mask";
	private static final String PROP_PAGE_FRAME_MASK = "Page_frame_mask";
	private static final String PROP_PAGE_WIDTH = "Page_size_X";
	private static final String PROP_PAGE_HEIGHT = "Page_size_Y";
	private static final String PROP_PAGE_MARGIN_LEFT = "Page_margin_Left";
	private static final String PROP_PAGE_MARGIN_TOP = "Page_margin_Top";
	private static final String PROP_PAGE_MARGIN_RIGHT = "Page_margin_Right";
	private static final String PROP_PAGE_MARGIN_BOTTOM = "Page_margin_Bottom";
	
	/**
	 * @param style
	 */
	public EGEPageFrame() {
		super(GRID);
		setPreferenceStore(store);
		setDescription("No active schema selected");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
		if (prop != null) {
			addField(new BooleanFieldEditor(PropertySharing.PROP_PAGES_OF_SAME_SIZE, 
					"Lock same page size and visibility for all pages",	getFieldEditorParent()));
			addField(new StringFieldEditor(PROP_PAGE_WIDTH, 
					"page width", getFieldEditorParent()));
			addField(new StringFieldEditor(PROP_PAGE_HEIGHT, 
					"page height", getFieldEditorParent()));
			addField(new StringFieldEditor(PROP_PAGE_MARGIN_TOP, 
					"top margin", getFieldEditorParent()));
			addField(new StringFieldEditor(PROP_PAGE_MARGIN_LEFT, 
					"left margin", getFieldEditorParent()));
			addField(new StringFieldEditor(PROP_PAGE_MARGIN_RIGHT, 
					"right margin", getFieldEditorParent()));
			addField(new StringFieldEditor(PROP_PAGE_MARGIN_BOTTOM, 
					"bottom margin", getFieldEditorParent()));
			addField(new BooleanFieldEditor(PROP_USE_DEFAULT_MASK, "use default mask for current page",
					getFieldEditorParent()));
			addField(new StringFieldEditor(PROP_PAGE_FRAME_MASK, 
					"Page frame text mask,\nuse \"" + PropertySharing.PAGE_MASK_NAME + 
					"\" for page name,\n\"" + PropertySharing.PAGE_MASK_PAGE + "\" for page number and\n\"" 
					+ PropertySharing.PAGE_MASK_MAX_PAGE + "\" for total number of pages\n", 
					getFieldEditorParent()));
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		IEditorPart editor = SdaieditPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (editor instanceof SdaiEditor) {
			IWorkbenchPart part = ((SdaiEditor)editor).getActiveInternalPart();
			if (part instanceof ExpressGEditor) {
				prop = ((ExpressGEditor)part).getProperties();
				setDescription("Page size and label");
				
				IPreferenceStore store = getPreferenceStore();
				default_page_mask = prop.properties().getProperty(PropertySharing.PROP_DEFAULT_PAGE_MASK);
				if (default_page_mask == null) default_page_mask = VisualPage.DEFAULT_PAGE_MASK;
				VisualPage vp = prop.handler().getCurrentPage();
				page_mask = vp.getPrintMask();
				store.setDefault(PROP_PAGE_FRAME_MASK, default_page_mask);
				if (page_mask != null) {
					store.setValue(PROP_PAGE_FRAME_MASK, page_mask);
					store.setValue(PROP_USE_DEFAULT_MASK, false);
				} else {
					store.setValue(PROP_USE_DEFAULT_MASK, true);
				}
				store.setValue(PropertySharing.PROP_PAGES_OF_SAME_SIZE, prop.isPagesSameSize());

				size = vp.getSize();
				store.setValue(PROP_PAGE_WIDTH, String.valueOf(size.x));
				store.setValue(PROP_PAGE_HEIGHT, String.valueOf(size.y));
				margins = vp.getMargins();
				store.setValue(PROP_PAGE_MARGIN_TOP, String.valueOf(margins.y));
				store.setValue(PROP_PAGE_MARGIN_LEFT, String.valueOf(margins.x));
				store.setValue(PROP_PAGE_MARGIN_RIGHT, String.valueOf(margins.width - margins.x));
				store.setValue(PROP_PAGE_MARGIN_BOTTOM, String.valueOf(margins.height - margins.y));
			}
		}
	}


	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	public boolean performOk() {
		boolean ok = super.performOk();
		if (prop != null) {
			String new_page_mask = null;
			if (!store.getBoolean(PROP_USE_DEFAULT_MASK))
				new_page_mask = store.getString(PROP_PAGE_FRAME_MASK);
			VisualPage vp = prop.handler().getCurrentPage();
			if (!StaticTools.equalStrings(new_page_mask, page_mask)) 
				vp.setPrintMask(new_page_mask);
			prop.setPagesSameSize(store.getBoolean(PropertySharing.PROP_PAGES_OF_SAME_SIZE));
			
			int pg_ren = store.getInt(PropertySharing.PROP_PAGE_RENUMBER);
			prop.setPageRenumber(pg_ren);			
			
			Point new_size = new Point(0, 0);
			try {
				new_size.x = Integer.parseInt(store.getString(PROP_PAGE_WIDTH));
				new_size.y = Integer.parseInt(store.getString(PROP_PAGE_HEIGHT));
				if (new_size != size && new_size.x > 0 && new_size.y > 0)
					vp.setSize(new_size);
			} catch (NumberFormatException e) {
				SdaieditPlugin.log(e);
			}
			Rectangle new_margins = new Rectangle(0, 0, 0, 0);
			try {
				new_margins.x = Integer.parseInt(store.getString(PROP_PAGE_MARGIN_LEFT));
				new_margins.y = Integer.parseInt(store.getString(PROP_PAGE_MARGIN_TOP));
				new_margins.width = new_margins.x + Integer.parseInt(store.getString(PROP_PAGE_MARGIN_RIGHT));
				new_margins.height = new_margins.y + Integer.parseInt(store.getString(PROP_PAGE_MARGIN_BOTTOM));
				if (new_margins != margins && new_margins.x >= 0 && new_margins.y >= 0 && new_margins.width >= 0 && new_margins.height >= 0)
					vp.setMargins(new_margins);
			} catch (NumberFormatException e) {
				SdaieditPlugin.log(e);
			}
			
			prop.handler().setPage(prop.handler().getPage());
		}
		return ok;
	}
}
