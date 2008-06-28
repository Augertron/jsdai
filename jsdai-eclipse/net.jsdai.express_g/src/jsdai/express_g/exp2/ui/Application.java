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
import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Display;

import jsdai.express_g.SdaieditPlugin;
import jsdai.express_g.editors.IExpressGEditor;
import jsdai.express_g.editors.RepositoryHandler;
import jsdai.express_g.exp2.ColorSchema;
import jsdai.express_g.exp2.eg.AbstractEGObject;
import jsdai.express_g.exp2.eg.EGPageFooter;
import jsdai.express_g.exp2.ui.command.Command;
import jsdai.express_g.exp2.ui.command.CommandHandler;
import jsdai.express_g.exp2.ui.command.CommandInvoker;
import jsdai.express_g.exp2.ui.command.FastSelectCommand;
import jsdai.express_g.exp2.ui.command.SelectCommand;
import jsdai.express_g.exp2.ui.event.PageChangeEvent;
import jsdai.express_g.exp2.ui.event.PageListener;
import jsdai.express_g.exp2.ui.event.StatusListener;
import jsdai.express_g.exp2.ui.panels.IPaintPanel;

/**
 * @author Mantas Balnys
 *
 */
public class Application implements PropertySharing, StatusListener, CommandInvoker, PageListener {
	private Properties properties = new Properties();
	private CommandHandler handler = new CommandHandler(this);
//	private SelectCommand selectCommand = new SelectCommand(this);
	private SelectCommand selectCommand = new FastSelectCommand(this);

	private SimpleRect[] pageSizeRect = {new SimpleRect(0, 0, 745, 1061), new SimpleRect(24, 30, 697, 1000)};
	private EGPageFooter pageFooter;
	private IExpressGEditor editorPart;
	
	/**
	 * 
	 */
	public Application(IExpressGEditor editorPart) {
		this.editorPart = editorPart;
		// Fonts
		IPreferenceStore store = SdaieditPlugin.getDefault().getPreferenceStore();
		try {
			String f = store.getString(PROP_DEFAULT_FONT1);
			if (f != null) {
				FontData fd = new FontData(f.replaceAll(";", ""));
				font1 = new Font(Display.getDefault(), fd);
			} else
				font1 = null;
		} catch (IllegalArgumentException e) {
			log("Default font for Entity not found");
			font1 = null;
		}
		try {
			String f = store.getString(PROP_DEFAULT_FONT2);
			if (f != null) {
				FontData fd = new FontData(f.replaceAll(";", ""));
				font2 = new Font(Display.getDefault(), fd);
			} else
				font2 = null;
		} catch (IllegalArgumentException e) {
			log("Default font for Attribute not found");
			font2 = null;
		}
		if (font1 == null) {
			font1 = new Font(Display.getDefault(), "Times New Roman", 10, SWT.NORMAL);
//			store.setDefault(PROP_DEFAULT_FONT1, font1.getFontData()[0].toString());
			store.setValue(PROP_DEFAULT_FONT1, font1.getFontData()[0].toString());
		} 
		if (font2 == null) {
			font2 = new Font(Display.getDefault(), "Times New Roman", 9, SWT.NORMAL);
//			store.setDefault(PROP_DEFAULT_FONT2, font2.getFontData()[0].toString());
			store.setValue(PROP_DEFAULT_FONT2, font2.getFontData()[0].toString());
		}
		int ren = store.getInt(PROP_DEFAULT_PAGE_RENUMBER);
		setPageRenumber(ren);
		
		SdaieditPlugin.getDefault().savePluginPreferences();
	//
	    handler.drawableAddR(pageSizeRect[0]);
	    handler.drawableAddR(pageSizeRect[1]);
	    pageFooter = new EGPageFooter(this);
	    handler.drawableAddR(pageFooter);
	    pageSizeRect[1].simpleSchema.lineStyle = ColorSchema.DASHED_LINE;

	    handler.addPageListener(this);
	    handler.startCommand(selectCommand);
	    handler.setPage(handler.getPage());

	    PrinterData printerData = Printer.getDefaultPrinterData();
	    if (printerData == null && Printer.getPrinterList().length > 0) {
	    	printerData = Printer.getPrinterList()[0];
	    }
	    if (printerData != null) {
		    Printer printer = new Printer(printerData);
		    Point pp = printer.getDPI();
		    Point dp = Display.getDefault().getDPI();
		    Rectangle trim = printer.computeTrim(0, 0, 0, 0);
		    Rectangle bounds = printer.getClientArea();
			setPageFormat(new Rectangle[]{bounds, 
					new Rectangle(bounds.x - trim.x, bounds.y - trim.y, bounds.width - trim.width, bounds.height - trim.height)}, 
					(double)dp.x / (double)pp.x, (double)dp.y / (double)pp.y);
	    } else { // in case when no Printer is found
	    	setPageFormat(new Rectangle[]{new Rectangle(0, 0, 745, 1061), new Rectangle(24, 30, 697, 1000)});
	    }
		//*/
	    
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.PropertySharing#getNonMovableDrawObjects()
	 */
	public Collection getNonMovableDrawObjects() {
	    Vector out = new Vector(2);
//	    out.add(pageSizeRect[0]);
//	    out.add(pageSizeRect[1]);
//	    out.add(pageFooter);
	    return out;
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.PropertySharing#getPageFormat()
	 */
	public Rectangle[] getPageFormat() {
		return new Rectangle[]{pageSizeRect[0].getBounds(), pageSizeRect[1].getBounds()};
	}

	/**
	 * set page format with no scaling 
	 */
	public void setPageFormat(Rectangle[] pageFormat) {
		//DEBUG
//		System.out.println("PAGE BOUNDS:" + pageFormat[0] + " VISIBLE:" + pageFormat[1]);
		
		pageSizeRect[0].setBounds(pageFormat[0]);
		pageSizeRect[1].setBounds(pageFormat[1]);

		pageFooter.setBounds(new Rectangle(pageFormat[1].x, 
	    		pageFormat[1].y + pageFormat[1].height - EGPageFooter.DEFAULT_HEIGHT,
		        pageFormat[1].width, EGPageFooter.DEFAULT_HEIGHT));
	}

	public void setPageFormat(Rectangle[] pageFormat, double scaleX, double scaleY) {
		setPageFormat(new Rectangle[]{new Rectangle(
				(int)(pageFormat[0].x * scaleX), (int)(pageFormat[0].y * scaleY), 
				(int)(pageFormat[0].width * scaleX), (int)(pageFormat[0].height * scaleY)), 
					new Rectangle(
				(int)(pageFormat[1].x * scaleX), (int)(pageFormat[1].y * scaleY), 
				(int)(pageFormat[1].width * scaleX), (int)(pageFormat[1].height * scaleY))}); 
	}

	  private String nameEG = "unknown_layout";
	  
	  public void setNameEG(String name) {
	  	nameEG = name;
	  }
	  
	  public String getNameEG() {
	  	return nameEG;
	  }

	/* (non-Javadoc)
	 * @see jsdai.express_g.exp.ui.UINaming#getUIName()
	 */
	public String getUIName() {
		return "JSDAI Express-G Editor";
	}
	
	private String name = "unknown_schema";

	/* (non-Javadoc)
	 * @see jsdai.express_g.exp.Named#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.exp.Named#getName()
	 */
	public String getName() {
		return name;
	}


	  private RepositoryHandler repoHandler = null;
	  
	  public RepositoryHandler getRepositoryHandler() {
	  	return repoHandler;
	  }
	  
	  public void setRepositoryHandler(RepositoryHandler handler) {
	  	this.repoHandler = handler;
	  }

	  private int editMode = MODE_LAYOUT_COMPLETE;
	  private boolean editModeChanged = false;

	  public int getEditMode() {
	    return editMode;
	  }

	  public void setEditMode(int mode) {
	  	if ((((editMode & MODE_LAYOUT_MASK) != 0) && ((mode & MODE_EDIT) != 0)||
	  		(((mode & MODE_LAYOUT_MASK) != 0) && ((editMode & MODE_EDIT) != 0))))
	  	    editModeChanged = true;
	    editMode = mode;
	  }
	  
	  public boolean wasEditModeChanged() {
	  	return editModeChanged;
	  }

	  public void resetEditModeChanged() {
	  	editModeChanged = false;
	  }

	  private String saveAllowed = null;
	  private String openAllowed = null;
	  
	  public String getSaveAllowed() {
	    return saveAllowed;
	  }
	  
	  public String getOpenAllowed() {
	    return openAllowed;
	  }
	  
	  public void setSaveAllowed(String state) {
	    saveAllowed = state;
	  }
	  
	  public void setOpenAllowed(String state) {
	    openAllowed = state;
	  }

	  private boolean multiUpdate = false;
	  /**
	   * true if performing mass updating
	   * usefull to turn off redraw function on some GUI objects
	   * @param multi
	   */
	  public void setMultiUpdate(boolean multi) {
	  	multiUpdate = multi;
	  }
	  
	  public boolean isMultiUpdate() {
	  	return multiUpdate;
	  }

	  protected File file = null;

	  public File getFile() {
	    return file;
	  }

	  public void setFile(File file) {
	    this.file = file;
	  }

	  private IPaintPanel paintPanel;
	  
	  public IPaintPanel getPainting() {
	    return paintPanel;
	  }

	  public void setPainting(IPaintPanel panel) {
	  	paintPanel = panel;
	  }

	  public SelectCommand getSelectionHandler() {
	    return selectCommand;
	  }

	  public CommandHandler handler() {
	    return handler;
	  }

	  public StatusListener status() {
	    return this;
	  }

	  public void commandDone(Command command) {
	  }
	  
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.event.PageListener#pageChanged(jsdai.express_g.exp2.ui.event.PageChangeEvent)
	 */
	public void pageChanged(PageChangeEvent e) {
	}

	private boolean modified = false;
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.PropertySharing#isModified()
	 */
	public boolean isModified() {
		return modified;
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.PropertySharing#setModified(boolean)
	 */
	public void setModified(boolean modified) {
		if (this.modified != modified) {
			this.modified = modified;
			if (editorPart != null) editorPart.updateModifiedStatus();
		}
	}
	
	private IProgressMonitor progress = null;
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.event.StatusListener#getProgressMonitor()
	 */
	public IProgressMonitor getProgressMonitor() {
		return progress;
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.event.StatusListener#setProgressMonitor(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void setProgressMonitor(IProgressMonitor progress) {
		this.progress = progress;
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.event.StatusListener#setStatus(int)
	 */
	public void setStatus(int job) {
		if (progress != null) progress.worked(1);
	}

	private String statusText = "";

	public void setStatus(String text) {
		if (progress != null) progress.setTaskName(text);
		statusText = text;
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.event.StatusListener#setStatus(java.lang.String, int)
	 */
	public void setStatus(String name, int total) {
		if (progress != null) progress.beginTask(name, total);
	}

	private boolean disposed = false;
	
	public boolean isDisposed() {
		return disposed;
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.PropertySharing#dispose()
	 */
	public void dispose() {
		handler.dispose();
		paintPanel.dispose();
		repoHandler = null;
		progress = null;
		pageFooter = null;
		file = null;
		pageSizeRect = null;
		selectCommand = null;
		setFont1(null);
		setFont2(null);
		disposed = true;
	}

	private int uid = 1; 
	/**
	 * returns unique ID for page reference
	 * @return
	 */
	public int getNewID() {
		return uid++;
	}

	/**
	 * Fonts
	 */
	private Font font1 = null;
	private Font font2 = null;
	
	public void setFont1(Font font) {
		if (font != font1) {
			if ((font1 != null)&& !font1.isDisposed()) font1.dispose();
			font1 = font;
		}
	}
	
	public Font getFont1() {
		return font1;
	}

	public void setFont2(Font font) {
		if (font != font2) {
			if ((font2 != null)&& !font2.isDisposed()) font2.dispose();
			font2 = font;
		}
	}
	  	
	public Font getFont2() {
		return font2;
	}
	
	public Properties properties() {
		return properties;
	}
	
	public void log(String message) {
		SdaieditPlugin.log(message, IStatus.INFO);
		SdaieditPlugin.console(message);
	}
	
	public void log(Throwable t) {
		SdaieditPlugin.log(t);
		SdaieditPlugin.console(t.toString());
	}
	
	private boolean pagesSameSize = false;
	
  	public void setPagesSameSize(boolean same) {
  		if (pagesSameSize != same) {
  			pagesSameSize = same;
  			if (same) {
  				VisualPage vp = handler.getCurrentPage();
  				vp.setSize(vp.getSize());
  				vp.setVisible(vp.getVisible());
  			}
  			setModified(true);
  		}
  	}
  	
  	public boolean isPagesSameSize() {
  		return pagesSameSize;
  	}
  	
  	/**
  	 * storing last used visibility of simple types
  	 */
  	
  	private short simpleTypeVisibilityDefault = AbstractEGObject.VISIBLE_TRUE;
  	
  	public short getSimpleTypeVisibilityDefault() {
  		return simpleTypeVisibilityDefault;
  	}
  	
  	public void setSimpleTypeVisibilityDefault(short visibility) {
  		simpleTypeVisibilityDefault = visibility;
  	}
  	
  	private int pageRenumber = 0;

  	public void setPageRenumber(int page) {
  		if (page < 0) throw new IllegalArgumentException("unallowed negative page number: " + page);
  		if (pageRenumber != page) {
  	  		pageRenumber = page;
  	  		setModified(true);
  	  		handler.updatePageRefText();
  	  		editorPart.updateTabNames();
  		}
  	}
  	
  	public int getPageRenumber() {
  		return pageRenumber;
  	}

}


