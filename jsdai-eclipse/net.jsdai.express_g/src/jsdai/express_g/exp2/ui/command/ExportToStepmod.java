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
import java.util.HashMap;
import java.util.Iterator;

import jsdai.SExpress_g_schema.EGraphics_diagram;
import jsdai.SExtended_dictionary_schema.ESchema_definition;
import jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema;
import jsdai.common.CommonPlugin;
import jsdai.express_g.SdaieditPlugin;
import jsdai.express_g.editors.ExpressGEditor;
import jsdai.express_g.editors.IExpressGEditor;
import jsdai.express_g.editors.ModelHandler;
import jsdai.express_g.editors.RepositoryHandler;
import jsdai.express_g.editors.RepositoryHandlerInput;
import jsdai.express_g.exp2.ColorSchema;
import jsdai.express_g.exp2.EGToolKit;
import jsdai.express_g.exp2.IXMLDefinition;
import jsdai.express_g.exp2.eg.AbstractEGObject;
import jsdai.express_g.exp2.ui.Application;
import jsdai.express_g.exp2.ui.VisualPage;
import jsdai.express_g.util.xml.ExpressXmlConverter;
import jsdai.express_g.util.xml.IPAdder;
import jsdai.express_g.util.xml.IsoDbTools;
import jsdai.express_g.wizards.ExportStepmod;
import jsdai.lang.ASdaiModel;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiRepository;
import jsdai.lang.SdaiSession;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
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
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.MessageConsoleStream;

/**
 * @author Mantas Balnys
 *
 */
public class ExportToStepmod extends AbstractCommand {
	public static final int IMAGE_INSETS = 10;
	boolean fStepmod = true;
	protected File file = null;
	protected File schemaFile = null;
	protected String schema_ext = "";
	protected String schema_name = "";
	protected String doc_file = "";
    String warning_messages = "";
    MessageConsoleStream stream;
    
	/**
	 * @param invoker
	 */
	public ExportToStepmod(CommandInvoker invoker) {
		super(invoker);
	}
	                                                                                                                                                                                                                                                                        
	public void setDirectory(String directory) {
		file = new File(directory);
		file.mkdirs();
//System.out.println("<EXPORTING A DIAGRAM OR DIAGRAMS>");
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
/*
    it is clear that for resources some header information has to be changed,
    not sure what about mim and arm
    How to recognize?
    fileName:
    mimexpg1
    mimexpg2 
    etc
    armexpg1
    armexpg2
		etc
*/

    boolean flag_arm_mim = false;
    if ((fileName.startsWith("mimexpg") || fileName.startsWith("armexpg")) && (fileName.length() < 10)) {
    	flag_arm_mim = true;
    }

   if (flag_arm_mim)
		ps.println("<?xml-stylesheet type=\"text/xsl\" href=\"../../../xsl/imgfile.xsl\"?>");
   else
		ps.println("<?xml-stylesheet type=\"text/xsl\" href=\"../../../xsl/res_doc/imgfile.xsl\"?>");
		ps.println("<!DOCTYPE imgfile.content SYSTEM \"../../../dtd/text.ent\">");
	if (flag_arm_mim)
		ps.println("<imgfile.content module=\"" + schema_name + "\" file=\"" + fileName + ".xml\">");
	else {	

// RR 2009-11-17
// as proposed by Lothar in bug 3453, but also a warning is needed
//		ps.println("<imgfile.content module=\"visual_presentation\" file=\"" + fileName + ".xml\">");
		//HashMap hm_iso_ids = IsoDbTools.getIsoIds();
		HashMap hm_part_names = IsoDbTools.getPartNames();
    String part_name = null;
		if (hm_part_names != null) {
			part_name = (String)hm_part_names.get(schema_name);
			if (part_name == null) {
				part_name = "";
			}
		} else {
			part_name = "";
		}
		//ps.println("<imgfile.content module=\"\" file=\"" + fileName + ".xml\">");
		ps.println("<imgfile.content module=\"" + part_name + "\" file=\"" + fileName + ".xml\">");
    // System.out.println("WARNING - fill the module attribute with the part name in file " + fileName);
		if (part_name.equals("")) {
			if (stream == null) {
				stream = CommonPlugin.getDefault().getConsole();
			}
			stream.println("WARNING - fill the module attribute with the part name in the file " + fileName + ".xml");
		}
	}
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
//System.out.println("prop: " + prop);
			// GIF initial startup
			loader = new ImageLoader();
			// XML initial startup
//System.out.println("prop name: " + prop.getName());
			String[] names = null;
			if (prop.getName() != null) {
				names = EGToolKit.parseSchemaName(prop.getName());
			} else {
				// this is probably a case o an orfan diagram wihout a schema_definition
				// the name should be perhaps constructed from the model name
				names = new String [3];
//				names[0] = "my_schema_name";
//				names[1] = "my_schema_ext";
				
        // RepositoryHandler rh = prop.getRepositoryHandler();
         		
				// it is probably safe to assume without checking that prop is Application and IExpressGEditor is ExpressGEditor, etc.
				
				IExpressGEditor g_editor = ((Application)prop).getEditor();
				RepositoryHandlerInput input = ((ExpressGEditor)g_editor).getInput();
				ModelHandler mh = input.getModelHandler();
				names[0] = mh.getName();
				names[1] = names[0];
				names[2] = ".xml";   // perhaps "4_info_reqs.xml" ? - not mim, not arm
  
			}
			schema_name = names[0];
			schema_ext = names[1];
			doc_file = names[2];

			String modules_resources = null;
			if (schema_ext.equalsIgnoreCase("ARM") || schema_ext.equalsIgnoreCase("MIM")) {
				modules_resources = "modules";
			} else {
				modules_resources = "resources";
			}
			
			fStepmod = ExportStepmod.isStepmod();
			String schema_file_str = null;
// flat - arms mims and resources all together - original as found - suitable for exporting only one-by-one
//			schemaFile = new File(file.getAbsolutePath() + File.separator + schema_name.toLowerCase());
// non-flat modules/... resources/...
			
			if (fStepmod) {
				schema_file_str = file.getAbsolutePath() + File.separator + modules_resources + File.separator + schema_name.toLowerCase();
			} else {
				schema_file_str = file.getAbsolutePath() + File.separator + schema_name.toLowerCase();
			}
			schemaFile = new File(schema_file_str);
//			schemaFile = new File(file.getAbsolutePath() + File.separator + modules_resources + File.separator + schema_name.toLowerCase());
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
			// RR adding xml generation here
			generateXmlFile(schema_file_str, schema_ext);
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

	void generateXmlFile(String schema_file_str, String schema_ext) {

		SdaiModel modelg0 = null;
		SdaiModel model0 = null;
		SdaiRepository repo0 = null;
		SdaiRepository repo2 = null;
		SdaiSession session0 = null;
		SdaiModel work0 = null;
		boolean flag_is_active = false;
		boolean flag_closed = true;
		RepositoryHandlerInput rphinput0 = null;
		IExpressGEditor g_editor0 = null;
		ModelHandler mhDict0 = null;
		RepositoryHandler rph0 = null;
		EGraphics_diagram gd0 = null;
		ESchema_definition sd0 = null;
		//ESchema_definition sd = null;

		try {
	    g_editor0 = ((Application)prop).getEditor();
//System.out.println("<retreaved g-editor: " +  g_editor0);    
			rphinput0 = ((ExpressGEditor)g_editor0).getInput();
//System.out.println("<retreaved repository handler input: " +  rphinput0);    
			//rphinput.getModelHandler();
			//rphinput.getRepositoryHandler();
			mhDict0 = rphinput0.getModelHandler();
//System.out.println("<retreaved model handler: " +  mhDict0);    
			rph0 = mhDict0.getRepositoryHandler();
//System.out.println("<retreaved repository handler: " +  rph0);    
			repo0 = rph0.getRepository();
//System.out.println("<retreaved repository: " +  repo0);    

			flag_is_active = repo0.isActive();
			if (!flag_is_active) {
			    repo0.openRepository();
					flag_closed = false;
			}

	      gd0 = mhDict0.getDiagram_definition();
	//System.out.println("<retreaved diagram: " +  gd0);    
  	      if (gd0.testDic_schema(null)) {	
  	    	  sd0 = gd0.getDic_schema(null);
  	      } else {
  	    	// this is the case of orphan diagram without a schema definition, nothing to generate  
  				if (!flag_is_active) {
  					repo0.closeRepository();
  	    		flag_closed = true;
  	    	}
  	    	return;
  	      }
			  model0 = repo0.findSdaiModel(sd0.getName(null).toUpperCase() + "_DICTIONARY_DATA");


		session0 = repo0.getSession();
    	work0 = repo0.createSdaiModel("working", SExtended_dictionary_schema.class);

//        String xml_file = model.getName().toLowerCase();
        // String xml_file = sd0.getName(null).toLowerCase();

        String xml_path = null;
              // - no, just leave arm/mim xml names, they are in separate subdirectories anyway.
              /*
			  if (!fStepmod) { // we want flat, without modules/resources
					xml_path = schema_file_str + File.separator + sd0.getName(null).toLowerCase() + ".xml";
			  } else
			  */
			  if (schema_ext.equalsIgnoreCase("ARM")) {
					xml_path = schema_file_str + File.separator + "arm.xml";
			  } else
			  if (schema_ext.equalsIgnoreCase("MIM")) {
					xml_path = schema_file_str + File.separator + "mim.xml";
			  } else { // resources or non-stepmod
					xml_path = schema_file_str + File.separator + sd0.getName(null).toLowerCase() + ".xml";
			  }

        //    int end_index = xml_file.lastIndexOf("_dictionary_data");
//            if (end_index  >0) xml_file = xml_file.substring(0,end_index);

//            String xml_path = rph0.getRepoPath().toOSString();
//            int last_slash = xml_path.lastIndexOf(File.separator);
//            if (last_slash > 0) xml_path = xml_path.substring(0,last_slash);
//            xml_path = xml_path + File.separator + xml_file + ".xml"; 				

//			System.out.println("<>underlying schema: " + model.getUnderlyingSchema().getName(null));
//			System.out.println("<>xml: " + xml_path);


			String iso_id = null;
			  
			  
		  

            String informal_propositions = "";
            String iso_db_path = "";

						ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getSelection();
            
            IProject fProject = null;

						if (selection instanceof IStructuredSelection) {

							Object obj = ((IStructuredSelection)selection).getFirstElement();
							if (obj instanceof IResource) {
								fProject = ((IResource)obj).getProject();
							} else {
								System.out.println("selection element is NOT a resource: " + obj);
							}
						}
						stream = CommonPlugin.getDefault().getConsole();

            if (fProject != null) {
            	informal_propositions = fProject.getLocation().toOSString() + File.separator + "informal_propositions.txt";
            	iso_db_path = fProject.getLocation().toOSString() + File.separator + "document_reference.txt";
          	} else {
          		stream.println("WARNING! Export was not initiated in a project, references will not be set, informal_propositions stage skipped");
          	}

						String schema_name = sd0.getName(null).toLowerCase();
						// IsoDbTools.readIsoNumbersOfSchemas(iso_db_path, stream);						
						//IsoDbTools.readIsoIdsAndPartNamesOfSchemas(iso_db_path, stream);						
						HashMap hm_iso_ids = IsoDbTools.getIsoIds();
						//HashMap hm_part_names = IsoDbTools.getPartNames();
						
						if (hm_iso_ids != null) {
							iso_id = (String)hm_iso_ids.get(schema_name);
							if (iso_id == null) {
								iso_id = "";
								warning_messages += "WARNING! Schema " + schema_name + " not found in document_reference.txt\n";
								//stream.println("WARNING! Schema " + schema_name + " not found in document_reference.txt");
							} else {
								// stream.println("OK! schema found in document_reference.txt: " + schema_name);
							}
							
						} else {
							// stream.println("WARNING! document_reference.txt not found in the project, empty references will be generated");
						}
						
						//if (iso_number.equals("")) {
						//	iso_number = null;
						//}



            ExpressXmlConverter.convertModel(model0, session0, repo0, work0, xml_path, iso_id, false);

            if (fProject != null) {
            	IPAdder.addIPsForSchema(schema_name, xml_path, informal_propositions, stream);
            }

     		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IPath xml_location = rph0.getRepoPath().removeLastSegments(1);
       		// System.out.println("<location> : " + xml_location);
      		IResource xml_res = (root.findContainersForLocation(xml_location))[0];
			xml_res.refreshLocal(1, null);

		}
		catch (Exception e) {
			System.out.println("<exception>: " +  e);
			SdaieditPlugin.log(e);
			e.printStackTrace();
		}
		finally {
		  
		  try {
			if ((work0 != null) & (work0 instanceof SdaiModel)) work0.deleteSdaiModel();
			if (!flag_is_active && !flag_closed) {
				repo0.closeRepository();
				flag_closed = true;
			}

			if (!warning_messages.equals("")) {
				stream.print(warning_messages);
			}

 		  } catch (SdaiException e) {
			// TODO Auto-generated catch block
			  SdaieditPlugin.log(e);
			  e.printStackTrace();
		   }	
						
}
		
	}

	
	
}
