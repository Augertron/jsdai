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

package jsdai.express_g.exp2.ui;

import java.io.File;
import java.util.Collection;
import java.util.Properties;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;

import jsdai.express_g.editors.RepositoryHandler;
import jsdai.express_g.exp2.Named;
import jsdai.express_g.exp2.ui.UINaming;
import jsdai.express_g.exp2.ui.command.CommandHandler;
import jsdai.express_g.exp2.ui.command.SelectCommand;
import jsdai.express_g.exp2.ui.event.StatusListener;
import jsdai.express_g.exp2.ui.panels.IPaintPanel;

/**
 * <p>Title: JSDAI Express-G</p>
 * <p>Description: </p>
 * @author Mantas Balnys
 * @version 1.0
 */

public interface PropertySharing extends UINaming, Named {

  /**
   * file we are working with (single file interface)
   * @return File
   */
  public File getFile();

  /**
   * file we are working with (single file interface)
   * @param file File
   */
  public void setFile(File file);

  /**
   * returns command handler
   * @return CommandHandler
   */
  public CommandHandler handler();

  /**
   * returns status listener
   * @return StatusListener
   */
  public StatusListener status();

  /**
   * returns Panel
   * @return PaintPanel
   */
  public IPaintPanel getPainting();
  
  public void setPainting(IPaintPanel panel);

  /**
   * returns main command which handles selection of objects
   * @return SelectCommand
   */
  public SelectCommand getSelectionHandler();

	public static final String PROP_EDITING_MODE = "Editing_mode";

  public static final int MODE_LAYOUT_MASK = 4; 		// 000100
  public static final int MODE_LAYOUT_PARTIAL_MASK = 2;	// 000010
  public static final int MODE_LAYOUT_COMPLETE_MASK = 1;  	// 000001
  public static final int MODE_SCHEMA_MASK = 16;		// 010000
  public static final int MODE_LONGFORM_MASK = 32;		// 100000
  public static final int MODE_EDIT = 8; 				// 001000
  public static final int MODE_LAYOUT_PARTIAL = MODE_LAYOUT_MASK | MODE_LAYOUT_PARTIAL_MASK;	// 000110
  public static final int MODE_LAYOUT_COMPLETE = MODE_LAYOUT_MASK | MODE_LAYOUT_COMPLETE_MASK;	// 000101
  public static final int MODE_LAYOUT_COMPLETE_LONG = MODE_LAYOUT_COMPLETE | MODE_LONGFORM_MASK;// 100101
  public static final int MODE_LAYOUT_PARTIAL_LONG = MODE_LAYOUT_PARTIAL | MODE_LONGFORM_MASK;	// 100110
  // RR we don't need this at all
  public static final int MODE_XML = 0; 
  
  public int getEditMode();

  public void setEditMode(int mode);
  
  public boolean wasEditModeChanged();

  public void resetEditModeChanged();
  
  /**
   * additional drawable objects
   * @return Collection
   */
  public Collection getNonMovableDrawObjects();

  /**
   * returns current page format
   * two rectangles: 0 - page bounds; 1 - visible area bounds
   * @return PageFormat
   */
  public Rectangle[] getPageFormat();

  /**
   * sets current page format with scaling = 1
   * two rectangles: 0 - page bounds; 1 - visible area bounds
   * @param pageFormat PageFormat
   */
  public void setPageFormat(Rectangle[] pageFormat);

  /**
   * sets current page format
   * two rectangles: 0 - page bounds; 1 - visible area bounds
   * @param pageFormat PageFormat
   * @param scaleX scaling
   * @param scaleY scaling
   */
  public void setPageFormat(Rectangle[] pageFormat, double scaleX, double scaleY);

  /**
   * returns reason message, why it's unallowed. null means it's allowed
   * @return
   */
  public String getSaveAllowed();
  
  public String getOpenAllowed();
  
  public void setSaveAllowed(String state);
  
  public void setOpenAllowed(String state);
  
  /**
   * true if performing mass updating
   * usefull to turn off redraw function on some GUI objects
   * @param multi
   */
  public void setMultiUpdate(boolean multi);
  
  public boolean isMultiUpdate();
  
  /**
   * name of Diagram model
   * @param name
   */
  public void setNameEG(String name);
  
  public String getNameEG();
  
  /**
   * repository handler (the only way to access repository)
   * @return
   */
  public RepositoryHandler getRepositoryHandler();
  
  public void setRepositoryHandler(RepositoryHandler handler);

  /**
   * modified state
   * @return
   */
  public boolean isModified();
  
  public void setModified(boolean modified);
  
  /**
   * frees up all resources
   *
   */
  public void dispose();
  
  /**
   * returns unique ID for page reference
   * @return
   */
  public int getNewID();
  
  /**
   * Fonts
   * don't change constants, same name is used in repository
   */
  	public static final String PROP_FONT1 = "Font_Entity";
  	public static final String PROP_FONT2 = "Font_Attribute";

  	public static final String PROP_DEFAULT_FONT1 = "Default_Font_Entity";
  	public static final String PROP_DEFAULT_FONT2 = "Default_Font_Attribute";
  	
  	public static final String DEFAULT_FONT1_DATA = "1|Times New Roman|10|0|WINDOWS|1|-13|0|0|0|0|0|0|0|1|0|0|0|0|Times New Roman";
  	public static final String DEFAULT_FONT2_DATA = "1|Times New Roman|9|0|WINDOWS|1|-12|0|0|0|0|0|0|0|1|0|0|0|0|Times New Roman";
  	
  	public void setFont1(Font font);
  	
  	public Font getFont1();

  	public void setFont2(Font font);
  	
  	public Font getFont2();
  	
  	/**
  	 * some kind of properties
  	 * @return
  	 */
  	public Properties properties();
  	
	public static final String PAGE_MASK_NAME = "%name";
	public static final String PAGE_MASK_PAGE = "%page";
	public static final String PAGE_MASK_MAX_PAGE = "%max_page";
  	public static final String PROP_DEFAULT_PAGE_MASK = "Default_page_mask";
  	public static final String PROP_SPECIFIC_PAGE_MASK = "Page_mask_"; // + pg_nr TODO unused

  	public static final String PROP_PAGES_OF_SAME_SIZE = "Pages_of_same_size";

  	public void setPagesSameSize(boolean same);
  	public boolean isPagesSameSize();

  	public static final String PROP_PAGE_RENUMBER = "Page_renumber_after_page";
  	public static final String PROP_DEFAULT_PAGE_RENUMBER = "Default_Page_renumber_after_page";

  	public void setPageRenumber(int page);
  	public int getPageRenumber();
  	public int getPageRenumber(int page);
  	public int getMaxPageRenumber(int page);
  	
  	/**
  	 * storing last used visibility of simple types
  	 */
  	
  	public short getSimpleTypeVisibilityDefault();
  	
  	public void setSimpleTypeVisibilityDefault(short visibility);
}
