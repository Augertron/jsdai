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

import jsdai.SExpress_g_schema.APage;
import jsdai.SExpress_g_schema.EPage;
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
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiRepository;

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
	
	//RR
	public IExpressGEditor getExpressGEditor() {
		return editorPart;
	}
	
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

    // RR I need to get this thing for exporting orphan diagrams
		public IExpressGEditor getEditor() {
			return editorPart;
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
//System.out.println("<<Application>> setting name: " + name); 
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.exp.Named#getName()
	 */
	public String getName() {
//System.out.println("<<Application>> getting name: " + name); 
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


		// modified version - RR
  	public void setPageRenumber_new(int page) {
  		if (page < 0) throw new IllegalArgumentException("unallowed negative page number: " + page);
			int new_page = page; //recalculatedPage(page);
  		if (pageRenumber != new_page) {
  	  		pageRenumber = new_page;
  	  		setModified(true);
  	  		handler.updatePageRefText();
  	  		editorPart.updateTabNames();
  		}
  	}

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

  	/*
  		 based on VisualPage, not on the dictionary data
  	*/
  	public int getPageRenumberV(int page) {
  		int recalculated_page = recalculatedPageV(page);
//System.out.println("IN <getPageRenumberV> page: " + page + ", recalculated page: " + recalculated_page);
//  		Throwable thr = new Throwable();
//  		thr.printStackTrace();
  		return (page-recalculated_page);
  	}

  	public int getPageRenumber(int page) {
  		int recalculated_page = recalculatedPage(page);
  		//return pageRenumber;
//System.out.println("<###> old: " + page + ", new: " + recalculated_page);
//System.out.println("IN <getPageRenumber> page: " + page + ", recalculated page: " + recalculated_page);
  		return (page-recalculated_page);
  	}

	/*
		will inform other parts of express-g if the current diagram is using special symbols in the names to establish a hierarchy.
		If not - the old implementation may be used in other parts of express-g, no need to complicate things.
	*/

	public boolean refreshHierarchyFlag() {
		return false;
	}


	/*
		this method based on VisualPage, not on the dictionary data
	*/
	public boolean hierarchyOnV() {

		for (int i = 1; i <= handler().getMaxPage(); i++) {
			String page_name = handler().getVisualPage(i).getName();
			if (page_name.startsWith("@")) {
				// hierarchy is on
				return true;
			} else {
			}							
		} // for
		return false;
	}
	
	
	public boolean hierarchyOn() {

		String model_name = null;
		SdaiModel model = null;
		RepositoryHandler rh = null;
		SdaiRepository rp = null;
		try {
			model_name = getNameEG();
			if (model_name != null) {
				model_name = model_name.toUpperCase() + "_EXPRESS_G_DATA";
				rh = getRepositoryHandler();
				if (rh != null) {
					rp = rh.getRepository();
					if (rp != null) {
						model = rp.findSdaiModel(model_name);
					} else {
						// System.out.println("<SETTING PAGE RENUMBER - Repository is NULL>: " + model_name);      
					}
				} else {
					// System.out.println("<SETTING PAGE RENUMBER - RepositoryHandler is NULL>: " + model_name);      
				}
			} else {
				// System.out.println("<SETTING PAGE RENUMBER - model name is NULL>");      
			}
			// model = getRepositoryHandler().getRepository().findSdaiModel(model_name);
			if (model != null) {
				APage pages = (APage)model.getInstances(EPage.class);
				SdaiIterator i_pages = pages.createIterator();
				boolean was_last = false;
				int current_page_nr = 0;
				int page_count = 0;
				int total_count = 0;
				for (int i = pages.getMemberCount(); i > 0; i--) {
	  			EPage a_page = (EPage)pages.getByIndex(i);
					String page_name = "";
					if (a_page.testComment(null)) {
						page_name = a_page.getComment(null);
					}
//System.out.println("<--->hierarchyOn - page name: " + page_name);
					if (page_name.startsWith("@")) {
						// hierarchy is on
						return true;
					} else {
					}							
				} // for
 				return false;
			} else { // model null
				return false; // hm, should not happen
			}
		} catch (SdaiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false; // we assume that the new hierarchical implementation is working only with correct data
		}

    	
	} // method

    /*
    	this implementation is based on VisualPage and not on the dictionary data,
    	therefore should work without re-opening of the exg file.
    	
    */
		int recalculatedPageV(int page) {
			boolean was_last = false;
	  	int current_page_nr = 0;
	  	int page_count = 0;
	  	int total_count = 0;
			
//System.out.println("<Application-recalculatedPageV> page: " + page + ", handler().getMaxPage(): " + handler().getMaxPage());
			for (int i = 1; i <= handler().getMaxPage(); i++) {
				String page_name = handler().getVisualPage(i).getName();
  			int page_number = -1;

	  		page_number = handler().getVisualPage(i).getPage();
//System.out.println("<Application-recalculatedPageV> page_number: " + page_number + ", page_name: " + page_name);
				if (page_name.startsWith("@")) {
					// the last page - the next page will be 1
					total_count++;
					if (was_last) {
						page_count = 1;
					} else {
						if (page_number > 0)
							page_count++;
					}							
					was_last = true;
				} else {
					total_count++;
					if (was_last) {
						page_count = 1;
					} else {
						if (page_number > 0)
							page_count++;
					}
					was_last = false;
				}
				if (page == page_number) {
//System.out.println("<RE-CALCULATING> RETURN IN LOOP: " + page_count);
					return page_count;
				}
			} // for
//System.out.println("<RE-CALCULATING> RETURN AFTER LOOP: " + page_count);
  		if (page == 0) {  
  			// Visual pages start from 1 and their number is 1 less than pages in the dictionary where also 0 page is present
  			return 0;
  		}
  		return page_count; 
		}


  	/*
		here we attempt to correct the page number - schema and entity level diagram pages numbered separately
		we we'll try this approach: the last page of a level has @ in its name, let's say, it starts with @
		so, the next page will have number 1, etc.
  	*/
  	int recalculatedPage(int page) {
//System.out.println("<RE-CALCULATING> page nr: " + page);
  		// get the model first
  		String model_name = null;
  		SdaiModel model = null;
  		RepositoryHandler rh = null;
  		SdaiRepository rp = null;
  		try {
				model_name = getNameEG();
				if (model_name != null) {
					model_name = model_name.toUpperCase() + "_EXPRESS_G_DATA";
  				rh = getRepositoryHandler();
  				if (rh != null) {
  					rp = rh.getRepository();
  					if (rp != null) {
  						model = rp.findSdaiModel(model_name);
  					} else {
//				  		System.out.println("<SETTING PAGE RENUMBER - Repository is NULL>: " + model_name);      
  					}
  				} else {
//			  		System.out.println("<SETTING PAGE RENUMBER - RepositoryHandler is NULL>: " + model_name);      
  				}
  			} else {
//		  		System.out.println("<SETTING PAGE RENUMBER - model name is NULL>");      
  			}
  			// model = getRepositoryHandler().getRepository().findSdaiModel(model_name);
  			if (model != null) {
  				// ok, we're in business!
  				// we can assume that all the pages of this model belong to the same diagram (the 1st has number 0) 
				APage pages = (APage)model.getInstances(EPage.class);
	  			SdaiIterator i_pages = pages.createIterator();
	  			boolean was_last = false;
	  			int current_page_nr = 0;
	  			int page_count = 0;
	  			int total_count = 0;
//System.out.println("<RE-CALCULATING> nr of pages: " + pages.getMemberCount());
//System.out.println("<Application-recalculatedPage> page: " + page + ", pages.getMemberCount(): " + pages.getMemberCount());
	  			for (int i = pages.getMemberCount(); i > 0; i--) {
//	  			while (i_pages.next()) {
//		  			EPage a_page = (EPage)pages.getCurrentMember(i_pages);
//	  				System.out.println("<RE-CALCULATING> index: " + i);
		  			EPage a_page = (EPage)pages.getByIndex(i);
					  String page_name = "";
		  			int page_number = -1;
		  			if (a_page.testComment(null)) {
			  			page_name = a_page.getComment(null);
		  			}
		  			if (a_page.testPage_number(null)) {
			  			page_number = a_page.getPage_number(null);
		  			}
//System.out.println("<Application-recalculatedPage> page_number: " + page_number + ", page_name: " + page_name);
						if (page_name.startsWith("@")) {
							// the last page - the next page will be 1
							total_count++;
							if (was_last) {
								page_count = 1;
							} else {
								if (page_number > 0)
									page_count++;
							}							
							was_last = true;
						} else {
							total_count++;
							if (was_last) {
								page_count = 1;
							} else {
								if (page_number > 0)
									page_count++;
							}
							was_last = false;
						}
//System.out.println("<RE-CALCULATING> page name: " + page_name + ", dict.number: " + page_number + ", current nr: " + page_count);
						if (page == page_number) {
//System.out.println("<RE-CALCULATING> RETURN IN LOOP: " + page_count);
							return page_count;
						}
						
					} // while
  				
//System.out.println("<RE-CALCULATING> RETURN AFTER LOOP: " + page_count);
  				return page_count; // why not found inside the loop?
  				// (new Throwable()).printStackTrace();
  			} else {
//System.out.println("<RE-CALCULATING> RETURN MODEL NULL: " + page);
  				return page;
  			}
  		} catch (SdaiException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
//System.out.println("<RE-CALCULATING> RETURN EXCEPTION: " + page);
				return page;
  		}
  		//System.out.println("<SETTING PAGE RENUMBER - model>: " + model);      
  		//return page;	
  	}

  	public int getMaxPageRenumber(int page) {
  		int recalculated_page = recalculatedMaxPage(page);
  		//return pageRenumber;
//System.out.println("<###-2> old: " + page + ", new: " + recalculated_page);
  		return recalculated_page;
  	}

  	public int getMaxPageRenumberV(int page) {
  		int recalculated_page = recalculatedMaxPageV(page);
  		return recalculated_page;
  	}

		/*
			 this implementation is based on VisualPage as opposed to the dictionary data
		*/
  	int recalculatedMaxPageV(int page) {
			boolean was_last = false;
	  	int current_page_nr = 0;
	  	int page_count = 0;
	  	int total_count = 0;

			for (int i = 1; i <= handler().getMaxPage(); i++) {
				String page_name = handler().getVisualPage(i).getName();
  			int page_number = -1;

	  		page_number = handler().getVisualPage(i).getPage();

				if (page_name.startsWith("@")) {
					// the last page - the next page will be 1
					total_count++;
					if (was_last) {
						page_count = 1;
					} else {
						if (page_number > 0)
							page_count++;
					}							
					was_last = true;
					if (page <= page_number) {
						return page_count;
					}
				} else {
					total_count++;
					if (was_last) {
						page_count = 1;
					} else {
						if (page_number > 0)
							page_count++;
					}
					was_last = false;
				}
				if (page == page_number) {
				}

			} // for
  		// Visual pages start from 1 and their number is 1 less than pages in the dictionary where also 0 page is present
			// do we need similar correction here as in renumberV() ?
  		//if (page == 0) {  
  		//	return 0;
  		//}
			return page_count; 

		}
  	
  	/*
		here we attempt to calculate the max page number - sechama and entity level diagram pages numbered separately
		we we'll try this approach: the last page of a level has @ in its name, let's say, it starts with @
		so, the next page will have number 1, etc.
		so, there is a separate max page for schema level and for entity level, for example
  	*/
  	int recalculatedMaxPage(int page) {
//System.out.println("<RE-CALCULATING> page nr: " + page);
  		// get the model first
  		String model_name = null;
  		SdaiModel model = null;
  		RepositoryHandler rh = null;
  		SdaiRepository rp = null;
  		try {
				model_name = getNameEG();
				if (model_name != null) {
					model_name = model_name.toUpperCase() + "_EXPRESS_G_DATA";
  				rh = getRepositoryHandler();
  				if (rh != null) {
  					rp = rh.getRepository();
  					if (rp != null) {
  						model = rp.findSdaiModel(model_name);
  					} else {
//				  		System.out.println("<SETTING PAGE RENUMBER - Repository is NULL>: " + model_name);      
  					}
  				} else {
//			  		System.out.println("<SETTING PAGE RENUMBER - RepositoryHandler is NULL>: " + model_name);      
  				}
  			} else {
//		  		System.out.println("<SETTING PAGE RENUMBER - model name is NULL>");      
  			}
  			// model = getRepositoryHandler().getRepository().findSdaiModel(model_name);
  			if (model != null) {
  				// ok, we're in business!
  				// we can assume that all the pages of this model belong to the same diagram (the 1st has number 0) 
				APage pages = (APage)model.getInstances(EPage.class);
	  			SdaiIterator i_pages = pages.createIterator();
	  			boolean was_last = false;
	  			int current_page_nr = 0;
	  			int page_count = 0;
	  			int total_count = 0;
//System.out.println("<RE-CALCULATING> nr of pages: " + pages.getMemberCount());
	  			for (int i = pages.getMemberCount(); i > 0; i--) {
//	  			while (i_pages.next()) {
//		  			EPage a_page = (EPage)pages.getCurrentMember(i_pages);
//	  				System.out.println("<RE-CALCULATING> index: " + i);
		  			EPage a_page = (EPage)pages.getByIndex(i);
					  String page_name = "";
		  			int page_number = -1;
		  			if (a_page.testComment(null)) {
			  			page_name = a_page.getComment(null);
		  			}
		  			if (a_page.testPage_number(null)) {
			  			page_number = a_page.getPage_number(null);
		  			}
						if (page_name.startsWith("@")) {
							// the last page - the next page will be 1
							total_count++;
							if (was_last) {
								page_count = 1;
							} else {
								if (page_number > 0)
									page_count++;
							}							
							was_last = true;
							if (page <= page_number) {
								return page_count;
							}
						} else {
							total_count++;
							if (was_last) {
								page_count = 1;
							} else {
								if (page_number > 0)
									page_count++;
							}
							was_last = false;
						}
//System.out.println("<RE-CALCULATING> page name: " + page_name + ", dict.number: " + page_number + ", current nr: " + page_count);
						if (page == page_number) {
//System.out.println("<RE-CALCULATING> RETURN IN LOOP: " + page_count);
//							return page_count;
						}
						
					} // while
  				
//System.out.println("<RE-CALCULATING> RETURN AFTER LOOP: " + page_count);
  				return page_count; // why not found inside the loop?
  				// (new Throwable()).printStackTrace();
  			} else {
//System.out.println("<RE-CALCULATING> RETURN MODEL NULL: " + page);
  				return page;
  			}
  		} catch (SdaiException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
//System.out.println("<RE-CALCULATING> RETURN EXCEPTION: " + page);
				return page;
  		}
  		//System.out.println("<SETTING PAGE RENUMBER - model>: " + model);      
  		//return page;	
  	}


}


