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

package jsdai.express_g.exp2.ui.command;

import java.util.Iterator;

import jsdai.express_g.exp2.ColorSchema;
import jsdai.express_g.exp2.Printable;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Display;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * @author Mantas Balnys
 * @version 1.0
 */

public class AbstractPrintableCommand extends AbstractCommand {
	public static final String PRINT_JOB_NAME = "Express-G Editor ";

	public AbstractPrintableCommand(CommandInvoker invoker) {
		super(invoker);
	}

  /**
   * Prints the page at the specified index into the specified {@link Graphics}
   * context in the specified format.
   *
   * @param graphics the context into which the page is drawn
   * @param pageFormat the size and orientation of the page being drawn
   * @param pageIndex the zero based index of the page to be drawn
   * @return PAGE_EXISTS if the page is rendered successfully or NO_SUCH_PAGE if
   *   <code>pageIndex</code> specifies a non-existent page.
   * @throws PrinterException thrown when the print job is terminated.
   */
  public void print(PrinterData data) {
  	int start = 1;
  	int end = prop.handler().getMaxPage();
  	if (data.scope == PrinterData.PAGE_RANGE) {
  		start = Math.max(start, data.startPage);
  		end = Math.min(end, data.endPage);
  	} else 
  	if (data.scope == PrinterData.SELECTION) {
  		start = end = prop.handler().getPage();
  	}
  	Printer printer = new Printer(data);
    Point pp = printer.getDPI();
    Point dp = Display.getDefault().getDPI();
    Rectangle trim = printer.computeTrim(0, 0, 0, 0);
    Rectangle bounds = printer.getClientArea();
	prop.setPageFormat(new Rectangle[]{bounds, 
			new Rectangle(bounds.x - trim.x, bounds.y - trim.y, bounds.width - trim.width, bounds.height - trim.height)}, 
			(double)dp.x / (double)pp.x, (double)dp.y / (double)pp.y);
  	if (printer.startJob(PRINT_JOB_NAME + prop.getNameEG())) {
  	    GC pgc = new GC(printer);
  	  	Image image = new Image(printer, prop.getPageFormat()[0]);
  	    Rectangle ib = image.getBounds();
  	  	GC gc = new GC(image);
  	  	
  	    for (int i = start; i <= end; i++) {
  	  	    if (printer.startPage()) {
  	    	    Iterator iter = prop.handler().drawableRevIterator(i);
  	    	    gc.setBackground(ColorSchema.COLOR[ColorSchema.COLOR_WHITE]);
  	    	    gc.fillRectangle(ib);
  	    	    while (iter.hasNext()) {
  	    	    	Object item = iter.next();
  	    	    	if ((item instanceof Printable)/*&&(((Paging)item).isOnPage(pageIndex))*/) 
  	    	    			((Printable)item).print(gc);
  	    	    }
  	    	    pgc.drawImage(image, ib.x, ib.y, ib.width, ib.height, bounds.x, bounds.y, bounds.width, bounds.height);
  	    	    printer.endPage();
  	  	    }
  	    }
  	    
  	    gc.dispose();
  	    image.dispose();
  	    pgc.dispose();
  	}
	printer.endJob();
    printer.dispose();
  }
}
