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

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Iterator;

import jsdai.express_g.exp2.ColorSchema;
import jsdai.express_g.exp2.EGToolKit;
import jsdai.express_g.exp2.IXMLDefinition;
import jsdai.express_g.exp2.eg.AbstractEGObject;
import jsdai.express_g.exp2.ui.VisualPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

/**
 * @author Mantas Balnys
 *
 */
public class ExportToStepmod extends AbstractCommand {
	public static final int IMAGE_INSETS = 10;
	
	protected File file = null;
	protected File schemaFile = null;
	protected String schema_ext = "";
	protected String schema_name = "";
	protected String doc_file = "";

	/**
	 * @param invoker
	 */
	public ExportToStepmod(CommandInvoker invoker) {
		super(invoker);
	}
	
	public void setDirectory(String directory) {
		file = new File(directory);
		file.mkdirs();
	}
	
	protected void createPage(Point startat, GC gc, PrintStream ps, int pgNr) {
		Iterator iter = prop.handler().drawableRevIterator(pgNr);
		while (iter.hasNext()) {
			Object item = iter.next();
			if (item instanceof AbstractEGObject) {
				AbstractEGObject obj = (AbstractEGObject)item;
				if (obj instanceof VisualPage) {
					// skip Page frame
				} else {
					obj.print(gc);
					if (obj instanceof IXMLDefinition) {
						String xml = ((IXMLDefinition)obj).getXMLDefinition(startat, schema_name, schema_ext, doc_file);
						if (xml != null) ps.println(xml);
					}
				}
			}
		}
	}
	
	protected void createPage(String fileName, int i) throws Exception {
		VisualPage vp = prop.handler().getVisualPage(i);
		Rectangle bounds = vp.getMinRoundingBox();
		if (bounds == null) bounds = new Rectangle(0, 0, 0, 0);
		// GIF init:
		Image imageTmp = new Image(Display.getCurrent(), bounds.x + bounds.width + IMAGE_INSETS, bounds.y + bounds.height + IMAGE_INSETS);
		GC gcTmp = new GC(imageTmp);
		try {
			gcTmp.setAntialias(SWT.ON);
			gcTmp.setTextAntialias(SWT.ON);
		} catch (SWTException e) {
			// Ignore antialiasing problems
			//SdaieditPlugin.log(e); // DEBUG
		}
		vp.simpleSchema.apply(gcTmp);
		gcTmp.setFont(prop.getFont1());
		gcTmp.setBackground(ColorSchema.COLOR[ColorSchema.COLOR_WHITE]); // XXX ???
		gcTmp.fillRectangle(imageTmp.getBounds());
		Point startat = new Point(bounds.x - IMAGE_INSETS, bounds.y - IMAGE_INSETS);
		// XML  init:
		PrintStream ps = new PrintStream(new FileOutputStream(schemaFile.getAbsolutePath() + File.separator + fileName + ".xml"));
		ps.println("<?xml version=\"1.0\"?>");
		ps.println("<?xml-stylesheet type=\"text/xsl\" href=\"../../../xsl/imgfile.xsl\"?>");
		ps.println("<!DOCTYPE imgfile.content SYSTEM \"../../../dtd/text.ent\">");
		ps.println("<imgfile.content module=\"" + schema_name + "\" file=\"" + fileName + ".xml\">");
		ps.println("<img src=\"" + fileName + ".gif\">");
		// create single page GIF and XML:
		createPage(startat, gcTmp, ps, i);
		// GIF finalize:
		int gifWidth = Math.max(1, bounds.width + 2 * IMAGE_INSETS);
		int gifHeight = Math.max(1, bounds.height + 2 * IMAGE_INSETS);
		Rectangle gifBounds = new Rectangle(0, 0, gifWidth, gifHeight);
		/*
		ImageData data = new ImageData(Math.max(1, bounds.width + 2 * IMAGE_INSETS), Math.max(1, bounds.height + 2 * IMAGE_INSETS), 8, palete);
		Image image = new Image(Display.getDefault(), data);
		GC gc = new GC(image);
		gc.setBackground(ColorSchema.COLOR_WHITE); // XXX ???
		gc.fillRectangle(image.getBounds());
		gc.drawImage(imageTmp, -startat.x, -startat.y);
		gcTmp.dispose();
		imageTmp.dispose();
		ImageData idata = image.getImageData();
		data.data = idata.data;
		*/
//		ImageData data = new ImageData(gifWidth, gifHeight, 8, palete, gifWidth, new byte[gifWidth * gifHeight]);
		Image image = new Image(Display.getCurrent(), gifBounds);
		GC gc = new GC(image);
		gc.setBackground(ColorSchema.COLOR[ColorSchema.COLOR_WHITE]); // XXX ???
		gc.fillRectangle(image.getBounds());
		gc.drawImage(imageTmp, -startat.x, -startat.y);
/*		
		Display display = Display.getCurrent();
		for (int c = 0; c < 256; c++) {
			Color color = new Color(display, c, 0, 0);
			gc.setForeground(color);
			gc.drawPoint(c, 0);
			color.dispose();
		}
		for (int c = 0; c < 256; c++) {
			Color color = new Color(display, 0, c, 0);
			gc.setForeground(color);
			gc.drawPoint(c, 1);
			color.dispose();
		}
		for (int c = 0; c < 256; c++) {
			Color color = new Color(display, 0, 0, c);
			gc.setForeground(color);
			gc.drawPoint(c, 2);
			color.dispose();
		}
*/		
		gcTmp.dispose();
		imageTmp.dispose();
		ImageData data = image.getImageData();
		ImageData data2;
		if (data.palette.isDirect) {
			data2 = getGIFformatImageData(data);
		} else {
			data2 = data;
		}
		
		loader.data = new ImageData[]{data2};
		loader.save(schemaFile.getAbsolutePath() + File.separator + fileName + ".gif", SWT.IMAGE_GIF);
		gc.dispose();
		image.dispose();
		// XML finalize:
		ps.println("</img>");
		ps.println("</imgfile.content>");
		ps.close();
	}
	
	private ImageLoader loader = null;
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.command.Command#start()
	 */
	public void start() {
		if (file.exists()) {
			prop.status().setStatus("Exporting to Stepmod", prop.handler().getMaxPage());
			// GIF initial startup
			loader = new ImageLoader();
			// XML initial startup
			String[] names = EGToolKit.parseSchemaName(prop.getName());
			schema_name = names[0];
			schema_ext = names[1];
			doc_file = names[2];
			schemaFile = new File(file.getAbsolutePath() + File.separator + schema_name.toLowerCase());
			schemaFile.mkdirs();
			
			for (int i = 1; i <= prop.handler().getMaxPage(); i++) {
				String fileName = schema_ext.toLowerCase() + "expg" + i;
				try {
					createPage(fileName, i);
					prop.status().setStatus(i);
				} catch (Exception e) {
					prop.status().log(e);
				}
			}
		} else {
			setExitCode(INTERUPTED);
		}
		prop.handler().commandDone();
	}
	
	
	private static PaletteData palete = new PaletteData(ColorSchema.PALETTE);

	public static ImageData getGIFformatImageData(ImageData data1) {
		ImageData data2 = new ImageData(data1.width, data1.height, 8, palete);
		data2.transparentPixel = data1.transparentPixel;
/*		
SdaieditPlugin.console("CONVERSION:");
SdaieditPlugin.console(data1.depth + "bit\t" + data1.width + "(" + data1.bytesPerLine + ") x " + data1.height + " = " + data1.data.length);		
SdaieditPlugin.console(data2.depth + "bit\t" + data2.width + "(" + data2.bytesPerLine + ") x " + data2.height + " = " + data2.data.length);		
SdaieditPlugin.console("white = " + data1.data[0] + ", " + data1.data[1] + ", " + data1.data[2]);

int d = data1.depth / 8;
for (int i = 0; i < 256; i++) {
	String array = "";
	for (int c = 0; c < 3; c++) {
		for (int j = 0; j < d; j++) {
			int val = data1.data[data1.bytesPerLine * c + i * d + j];
			String dec = Integer.toString(val);
			String bin = Integer.toBinaryString(val);
			while (dec.length() < 4) dec = " " + dec;
			while (bin.length() < 8) bin = "0" + bin;
			if (bin.length() > 8) bin = bin.substring(bin.length() - 8);
			array += bin + "(" + dec + ") ";
		}
		array += " | ";
	}
	SdaieditPlugin.console(array);
}
*/

		int pix1 = 0;
		int pix2 = 0;
		int dx1;
		int dx2;
		switch (data1.depth) {
			case 16 :
				dx1 = data1.bytesPerLine - data1.width * 2;
				dx2 = data2.bytesPerLine - data2.width;
				for (int i = 0; i < data2.height; i++) {
					for (int j = 0; j < data2.width; j++) {
						int red = (data1.data[pix1 + 1] >> 2) & 31;
						int green = ((data1.data[pix1 + 1] & 3) << 3) | ((data1.data[pix1] >> 5) & 7);  
						int blue = data1.data[pix1] & 31;
						data2.data[pix2] = (byte)ColorSchema.get_nearest32(red, green, blue);
						pix2++;
						pix1 += 2;
					}
					pix1 += dx1;
					pix2 += dx2;
				}
				break;
			case 24 :
				dx1 = data1.bytesPerLine - data1.width * 3;
				dx2 = data2.bytesPerLine - data2.width;
				for (int i = 0; i < data2.height; i++) {
					for (int j = 0; j < data2.width; j++) {
						data2.data[pix2] = (byte)ColorSchema.get_nearest256(data1.data[pix1], data1.data[pix1 + 1], data1.data[pix1 + 2]);
						pix2++;
						pix1 += 3;
					}
					pix1 += dx1;
					pix2 += dx2;
				}
				break;
			case 32 :
				dx1 = data1.bytesPerLine - data1.width * 4;
				dx2 = data2.bytesPerLine - data2.width;
				for (int i = 0; i < data2.height; i++) {
					for (int j = 0; j < data2.width; j++) {
						data2.data[pix2] = (byte)ColorSchema.get_nearest256(data1.data[pix1 + 2], data1.data[pix1 + 1], data1.data[pix1]);
						pix2++;
						pix1 += 4;
					}
					pix1 += dx1;
					pix2 += dx2;
				}
				break;
			default :
				throw new SWTException(SWT.ERROR_UNSUPPORTED_DEPTH);
		}
		
		
		return data2;
	}
}
