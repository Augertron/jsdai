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

package jsdai.express_g.exp2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Vector;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import jsdai.SExpress_g_schema.APage_reference_to;
import jsdai.SExpress_g_schema.EPage_reference_to;
import jsdai.SExtended_dictionary_schema.ESchema_definition;
import jsdai.SExtended_dictionary_schema.ESimple_type;
import jsdai.express_g.SdaieditPlugin;
import jsdai.express_g.editors.RepositoryHandler;
import jsdai.express_g.exp2.Paging;
import jsdai.express_g.exp2.eg.AbstractEGBox;
import jsdai.express_g.exp2.eg.AbstractEGObject;
import jsdai.express_g.exp2.eg.AbstractEGRelation;
import jsdai.express_g.exp2.eg.EGDefined;
import jsdai.express_g.exp2.eg.EGEntity;
import jsdai.express_g.exp2.eg.EGEntityRef;
import jsdai.express_g.exp2.eg.EGPage;
import jsdai.express_g.exp2.eg.EGPageFrom;
import jsdai.express_g.exp2.eg.EGPageFromRef;
import jsdai.express_g.exp2.eg.EGPageRef;
import jsdai.express_g.exp2.eg.EGPageRefInterschema;
import jsdai.express_g.exp2.eg.EGPageRefLocalFrom;
import jsdai.express_g.exp2.eg.EGPageRefLocalTo;
import jsdai.express_g.exp2.eg.EGPageRefToSimple;
import jsdai.express_g.exp2.eg.EGPageTo;
import jsdai.express_g.exp2.eg.EGPageToRef;
import jsdai.express_g.exp2.eg.EGPageToSimple;
import jsdai.express_g.exp2.eg.EGRelationSimple;
import jsdai.express_g.exp2.eg.EGRelationTree;
import jsdai.express_g.exp2.eg.EGSimple;
import jsdai.express_g.exp2.eg.IHidenRef;
import jsdai.express_g.exp2.eg.Wire;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.VisualPage;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;

public class EGToolKit {
	
	/**
	 * special method for renumbering. Gets String representation of page number for editor tabs and info page
	 * @param prop
	 * @param page current page
	 * @return
	 */
	public static String renumberInTab(PropertySharing prop, int page) {
		return String.valueOf(EGToolKit.renumber(prop, page)) + (page > prop.getPageRenumber() ? "" : "S");
	}

	/**
	 * special method to count visible maximum pages (not real) after renumbering of pages
	 * renumbering allows to restart numbering from specified page and there are two maximums:
	 * one equals renumbering page, other total pages - renumbered page
	 * @param prop
	 * @param current_page current page
	 * @return
	 */
	public static int renumberMaxPage_old(PropertySharing prop, int current_page) {
//		int page_ren = prop.getMaxPageRenumber(current_page);
		int page_ren = prop.getPageRenumber();
		int page_max = prop.handler().getMaxPage();
		if (current_page > page_ren) 
			return page_max - page_ren;
		else
			return Math.min(page_ren, page_max);
	}
	public static int renumberMaxPage(PropertySharing prop, int current_page) {
		int page_ren = prop.getMaxPageRenumber(current_page);
//		int page_ren = prop.getPageRenumber();
		// int page_max = prop.handler().getMaxPage();
		//if (current_page > page_ren) 
			//return page_max - page_ren;
		//else
			//return Math.min(page_ren, page_max);
		return page_ren;
	}
	
	/**
	 * special method to count visible page number (not real) after renumbering of pages
	 * renumbering allows to restart numbering from specified page
	 * @param prop
	 * @param page_number current page
	 * @return
	 */
	public static int renumber(PropertySharing prop, int page_number) {

//  	int page_ren = prop.getPageRenumber();
  	int page_ren = prop.getPageRenumber(page_number);
		if (page_number > page_ren) 
			return page_number - page_ren;
		else
			return page_number;
	}
	
	
	/**
	 * calls update() method for all items in collection
	 * @param items collection of Updateable items
	 */
	public static void update(Collection items) {
		Iterator iter = items.iterator();
		while (iter.hasNext()) {
			Object item = iter.next();
			if (item instanceof Updateable) {
				((Updateable)item).update();
			}
		}
	}

	/**
	 * resets all information about object placement, for example resets placement to point 0,0 and etc.
	 * works only on objects that are on INVISIBLE_PAGE !!! 
	 * @param col
	 */
	public static void resetObjectPlacementInfo(Collection col) {
		Iterator iter = col.iterator();
		while (iter.hasNext()) {
			Object item = iter.next();
			if (item instanceof AbstractDraw)
				resetObjectPlacementInfo((AbstractDraw)item);
		}
	}

	/**
	 * resets all information about object placement, for example resets placement to point 0,0 and etc.
	 * works only on objects that are on INVISIBLE_PAGE !!! 
	 * @param object
	 */
	public static void resetObjectPlacementInfo(AbstractDraw draw) {
		if (draw.isOnPage(Paging.INVISIBLE_PAGE)) {
			draw.setBounds(new Rectangle(0, 0, 0, 0));
			
			if (draw instanceof AbstractEGObject) {
				AbstractEGObject object = (AbstractEGObject)draw; 
				object.setLabelNew(true);
				object.setVisible(AbstractEGObject.VISIBLE_TRUE);

				/*/
				if (object instanceof AbstractEGBox) {
					AbstractEGBox box = (AbstractEGBox)object;
				} else /**/
				if (object instanceof EGPageRef) {
					EGPageRef ref = (EGPageRef)object;
					Iterator wit = ref.getWires().iterator();
					while (wit.hasNext()) {
						AbstractEGRelation rel = ((Wire)wit.next()).getRelation();
						EGPageRef ref1 = ref.getOneFor(rel);
						if (ref1 != ref) {
							draw.prop().handler().drawableAddR(ref1);
						}
//						UPGRADE no update in page change						rel.update(1);
					}
//					UPGRADE no update in page change					ref.update(2);
				} else
				if (object instanceof AbstractEGRelation) {
					AbstractEGRelation rel = (AbstractEGRelation)object;
			        rel.setHint1(new Point(0, 0));
			        rel.setHint2(new Point(0, 0));
			        
			        /*
			        if (rel instanceof EGRelationSimple) {
			        	EGRelationSimple simple = (EGRelationSimple)rel;
			        } else /**/
			        if (rel instanceof EGRelationTree) {
			        	EGRelationTree tree = (EGRelationTree)rel;
			        	tree.setSpecialDraw(false);
			        }
			        
				}
			}
		}
	}
	
	
	/**
	 * resolves Express schema name from dictionary schema name
	 * @param dictionary_schema_name
	 * @return express schema name or null if not found
	 */
	public static final String resolveExpressSchemaName(String dictionary_schema_name) {
		int end = dictionary_schema_name.lastIndexOf("_DICTIONARY_DATA");
		if (end > 0) 
			return "_EXPRESS_" + dictionary_schema_name.substring(0, end);
		else
			return null;
	}
	
	/**
	 * gets page names from application properties
	 * @param prop application properties
	 * @param allowNewPage if true, adds additional string to the end of array - "<new page>"
	 * @return array of page names
	 */
	public static String[] resolvePageNames(PropertySharing prop, boolean allowNewPage) {
		int size = prop.handler().getMaxPage();
		if (allowNewPage) size++;
		String[] pages = new String[size];
		for (int i = 1; i <= prop.handler().getMaxPage(); i++) {
			pages[i - 1] = prop.handler().getVisualPage(i).getName();
		}
		if (allowNewPage) {
			pages[size - 1] = "<new page>";
		}
		return pages;
	}
	
	/**
	 * creates temp file with name "temp_*.tmp"
	 * @return
	 */
	public static File getTempFile() {
		return getTempFile(null, null);
	}

	/**
	 * creates temp file with extension *.tmp
	 * @param suggestName
	 * @return
	 */
	public static File getTempFile(String suggestName) {
		return getTempFile(suggestName, null);
	}
	
	/**
	 * create file in temp directory
	 * @param suggestName
	 * @param extension
	 * @return
	 */
	public static File getTempFile(String suggestName, String extension) {
		if (suggestName == null) suggestName = "temp";
		if (extension == null) extension = "tmp";
		File file = null;
		int err = 0;
		while (err++ < 100 && file == null) try {
			file = File.createTempFile(suggestName + "_" + System.currentTimeMillis(), extension);
		} catch (IOException iox) {}
		return file;
	}
	
	/**
	 * delete file or delete file on exit
	 * @param file
	 */
	public static void delTempFile(File file) {
		if (!file.delete()) file.deleteOnExit();
	}

	/**
	 * Maximum size of drawable page - graphical context
	 */
	public static final int MAX_PAGE_SIZE = 8388608; // = 2^23 maximum memmory for image under Win2000
	
	public static final int MAX_PAGE_WIDTH = 2048;

	public static final int MAX_PAGE_HEIGHT = 8192;

	/*
	 * public static long NO_UID = 0; private static long uid = 1; /** each time
	 * called returns unique ID @return public static long getNewUID() { return
	 * uid++; } /** creates new EGPageTo object depending on reference type can
	 * create EGPageTo, EGPageToSimple, EGPageToRef ... @param pageFrom @param
	 * referenced @param current @return @throws SdaiException
	 */
	public static EGPageTo newPageTo(PropertySharing prop, EGPageFrom pageFrom,
			EEntity referenced, SdaiModel current) throws SdaiException {
		EGPageTo pageT;
		if (referenced instanceof ESimple_type)
			pageT = new EGPageToSimple(prop, pageFrom);
		else if (!(referenced instanceof ESchema_definition)
				&& (referenced.findEntityInstanceSdaiModel() != current))
			pageT = new EGPageToRef(prop, pageFrom);
		else
			pageT = new EGPageTo(prop, pageFrom);
		return pageT;
	}

	/**
	 * creates new EGPageTo object depending on reference type can create
	 * EGPageTo, EGPageToSimple, EGPageToRef ...
	 * 
	 * @param pageFrom
	 * @param referenced
	 * @return
	 */
	public static EGPageTo newPageTo(PropertySharing prop, EGPageFrom pageFrom,
			AbstractEGBox referenced) {
		EGPageTo pageT;
		if (referenced instanceof EGSimple)
			pageT = new EGPageToSimple(prop, pageFrom);
		else if (referenced instanceof EGEntityRef)
			pageT = new EGPageToRef(prop, pageFrom);
		else
			pageT = new EGPageTo(prop, pageFrom);
		return pageT;
	}

	/**
	 * creates new EGPageFrom object depending on reference type can create
	 * EGPageFrom, EGPageFromRef ...
	 * 
	 * @param child
	 * @param referenced
	 * @param current
	 * @return
	 * @throws SdaiException
	 */
	public static EGPageFrom newPageFrom(PropertySharing prop,
			AbstractEGBox child, APage_reference_to referenced,
			SdaiModel current) throws SdaiException {
		EGPageFrom pageF = null;
		SdaiIterator sit = referenced.createIterator();
		if (sit.next()) {
			EPage_reference_to page = referenced.getCurrentMember(sit);
			EEntity ref = page.getParent(null);
			if (!(ref instanceof ESchema_definition)
					&& (ref.findEntityInstanceSdaiModel() != current))
				pageF = new EGPageFromRef(prop, child);
			else
				pageF = new EGPageFrom(prop, child);
		}
		return pageF;
	}

	/**
	 * creates new EGPageFrom object depending on reference type can create
	 * EGPageFrom, EGPageFromRef ...
	 * 
	 * @param child
	 * @param referenced
	 * @return
	 */
	public static EGPageFrom newPageFrom(PropertySharing prop,
			AbstractEGBox child, AbstractEGBox referenced) {
		EGPageFrom pageF;
		if (referenced instanceof EGEntityRef)
			pageF = new EGPageFromRef(prop, child);
		else
			pageF = new EGPageFrom(prop, child);
		return pageF;
	}

	/**
	 * copies file
	 * 
	 * @param from
	 * @param to
	 */
	public static boolean copyFile(File from, File to) {
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(from);
			out = new FileOutputStream(to);
			byte[] copyBuffer = new byte[65536];
			long bytesCopied = 0;
			int read = -1;
			while ((read = in.read(copyBuffer, 0, copyBuffer.length)) != -1) {
				out.write(copyBuffer, 0, read);
				bytesCopied += read;
			}
			in.close();
			out.close();
			return true;
		} catch (IOException e) {
			SdaieditPlugin.log(e);
			if (in != null) try {
				in.close();
			} catch (IOException e1) {
				SdaieditPlugin.log(e1);
			}
			if (out != null) try {
				out.close();
			} catch (IOException e2) {
				SdaieditPlugin.log(e2);
			}
			return false;
		}

	}

	public static boolean copyFile(String from, String to) {
		return copyFile(new File(from), new File(to));
	}

	/**
	 * parses schema name to name root and extension extension = everything
	 * after the last occurence of "_" name = everythig till the last occurence
	 * of "_"
	 * 
	 * @param schemaName
	 * @return array of three strings: 1 = name, 2 = extension, 3 = doc file
	 *         name (something specific depending on extension)
	 */
	public static String[] parseSchemaName(String schemaName) {
		String[] output = new String[3];
		int index = schemaName.lastIndexOf('_');
		if (index <= 0) {
			output[0] = schemaName;
			output[1] = "";
			output[2] = "4_info_reqs.xml";
		} else {
			output[1] = schemaName.substring(index + 1);
			if (output[1].equalsIgnoreCase("ARM")) {
				output[0] = schemaName.substring(0, index);
				output[2] = "4_info_reqs.xml";
			} else if (output[1].equalsIgnoreCase("MIM")) {
				output[0] = schemaName.substring(0, index);
				output[2] = "5_mim.xml";
			} else {
				output[0] = schemaName;
// file names in resources do not contain schema names - as done originally - very logical solution, names are not needed
//				output[1] = "";
// making file names in resources also to contain the schema name (because of the naming convention)
				output[1] = schemaName;  // experimenting - RR
				output[2] = ".xml";
			}
		}
		return output;
	}

	/**
	 * returns distance between two points
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double distance(Point p1, Point p2) {
		int dx = p2.x - p1.x;
		int dy = p2.y - p1.y;
		return Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * counts an angle between two points (p1 as center)
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double angle(Point p1, Point p2) {
		double angle = Math.asin((double)(p2.y - p1.y)
				/ EGToolKit.distance(p1, p2));
		if (p1.x > p2.x) {
			if (p1.y > p2.y)
				angle = -Math.PI - angle;
			else
				angle = Math.PI - angle;
		}
		return normalizeAngle(angle);
	}

	/**
	 * normalize angle after this operation angle is in range 0 .. 2*PI
	 * 
	 * @param ang
	 * @return
	 */
	public static double normalizeAngle(double ang) {
		while (ang < 0)
			ang += 2 * Math.PI;
		while (ang > 2 * Math.PI)
			ang -= 2 * Math.PI;
		return ang;
	}

	/**
	 * moves all items on page <from>to page <to>
	 * 
	 * @param prop
	 * @param from
	 * @param to
	 */
	private static void change_all_page(PropertySharing prop, int from, int to) {
		Iterator iter = prop.handler().drawableIterator(from);
		while (iter.hasNext()) {
			Paging item = (Paging)iter.next();
			if (!(item instanceof VisualPage) && (item.getPage() == from))
				item.setPage(to);
		}
	}

	/**
	 * moves pages moving from start till the end or viceversa depending on
	 * direction direction < 0 - deleting pages direction > 0 - inserting pages
	 * 
	 * @param prop
	 * @param start
	 * @param direction
	 */
	public static void rearange_pages(PropertySharing prop, int start,
			int direction) {
		// if (direction < 0) for i = start; i - direction <= max; i++ do
		// page[i] = page[i - direction]
		// if (direction > 0) for i = max; i >= start; i-- do page[i +
		// direction] = page[i]
		if (direction < 0) {
			int i;
			for (i = 0; i < -direction; i++) { //delete pages
				selectAll(prop, start + i, prop);
				prop.getSelectionHandler().deleteSelected();
				
				VisualPage vp = prop.handler().getVisualPage(start + i);
				vp.eliminate();
			}
			for (i = start; i - direction <= prop.handler().getMaxPage(); i++) {
				change_all_page(prop, i - direction, i);
				// moving page objects
				VisualPage vp = prop.handler().getVisualPage(i - direction);
				vp.setPage(i);
				prop.handler().setVisualPage(vp);
			}
			if (i > 1) prop.handler().setMaxPage(i - 1);
			int pageNr = prop.handler().getPage();
			if (pageNr > 1)
				prop.handler().setPage(pageNr - 1);
			else if (pageNr > 0)
				prop.handler().setPage(pageNr);
		} else if (direction > 0) {
			//      prop.handler().setMaxPage(prop.handler().getMaxPage() +
			// direction);
			for (int i = prop.handler().getMaxPage(); i >= start; i--) {
				change_all_page(prop, i, i + direction);
				// moving page objects
				VisualPage vp = prop.handler().getVisualPage(i);
				vp.setPage(i + direction);
				prop.handler().setVisualPage(vp);
			}
			for (int i = start; i < start + direction; i++)
				prop.handler().setVisualPage(new VisualPage(prop, i));
			// refresh
			prop.handler().setPage(prop.handler().getPage());
		}
	}

	/**
	 * moves a page with all contents to new location
	 * 
	 * @param prop
	 * @param from
	 * @param to
	 */
	public static boolean movePage(PropertySharing prop, int from, int to) {
		if ((from > 0) && (from <= prop.handler().getMaxPage()) && (to > 0)
				&& (from != to)) {
			int pgTmp = prop.handler().getMaxPage() + 1; // temporary page
			// move selected page to temporary place
			prop.handler().setMaxPage(pgTmp);
			change_all_page(prop, from, pgTmp);
			// push all pages
//			prop.handler().setPage(to);
			int dd;
			if (to > from) {
				dd = 1;
			} else {
				dd = -1;
			}
			VisualPage vpF = prop.handler().getVisualPage(from);
			for (int i = from; i != to;) {
				change_all_page(prop, i + dd, i);
				// moving page objects
				VisualPage vp = prop.handler().getVisualPage(i + dd);
				vp.setPage(i);
				prop.handler().setVisualPage(vp);
				i += dd;
			}
			vpF.setPage(to);
			prop.handler().setVisualPage(vpF);
			change_all_page(prop, pgTmp, to);
			prop.handler().setMaxPage(pgTmp - 1);
			prop.handler().setPage(to);
			return true;
		} else
			return false;
	}

	/**
	 * selects all items on defined page allows ANY_PAGE
	 * 
	 * @param prop
	 * @param pgNr
	 * @param invoker
	 */
	public static void selectAll(PropertySharing prop, int pgNr, Object invoker) {
		Iterator iter;
		if (pagesIdentical(pgNr, Paging.ANY_PAGE))
			iter = prop.handler().drawableIterator();
		else
			iter = prop.handler().drawableIterator(pgNr);
		Set items = new HashSet();
		while (iter.hasNext()) {
			Object item = iter.next();
			if (item instanceof Paging) {
				if ((((Paging)item).getPage() == pgNr) && !(item instanceof VisualPage))
					items.add(item);
			}
		}
		prop.getSelectionHandler().setSelected(invoker, items);
		prop.getPainting().redraw();
	}

	/**
	 * converts page number int to String format, converting not standart pages
	 * to letters
	 * 
	 * @param pgNr int
	 * @return String
	 */
	public static String convertPageNrToString(int pgNr) {
		String page;
		if (pgNr == Paging.INVISIBLE_PAGE)
			page = "N";
		else if (pgNr == Paging.ANY_PAGE)
			page = "A";
		else
			page = String.valueOf(pgNr);
		return page;
	}

	protected static void addRecursively(Collection col, AbstractEGBox box) {
		//System.out.println("creating box: " + box + "<" +
		// box.getClass().getName() + ">");
		if (col.contains(box)) {
			Iterator iter = box.getWires().iterator();
			while (iter.hasNext()) {
				Wire wire = (Wire)iter.next();
				if (wire.isAttribute())
					addRecursively(col, wire.getRelation());
			}
		} else {
			if (!((box instanceof EGDefined) || (box instanceof EGEntity))) {
				col.add(box);
				addRecursively(col, box);
			}
		}
		//else System.out.println("box exists: " + box + "<" +
		// box.getClass().getName() + ">");
	}

	protected static void addRecursively(Collection col,
			AbstractEGRelation relation) {
		//System.out.println("creating relation: " + relation.getParent() + "<"
		// + relation.getParent().getClass().getName() + ">" + " -o " +
		// relation.getChild() + "<" + relation.getChild().getClass().getName()
		// + ">");
		if (!col.contains(relation))
			col.add(relation);
		Iterator iter = relation.getChilds().iterator();
		while (iter.hasNext()) {
			AbstractEGBox box = (AbstractEGBox)iter.next();
			addRecursively(col, box);
		}
		//else System.out.println("relation exists: " + relation.getParent() +
		// "<" + relation.getParent().getClass().getName() + ">" + " -o " +
		// relation.getChild() + "<" + relation.getChild().getClass().getName()
		// + ">");
	}

	/**
	 * updates collection of objects with related simple types (Enumeration,
	 * Selection, Simple)
	 * 
	 * @param selection
	 * @return
	 */
	public static Collection updateForSimpleTypes(Collection selection) {
		Collection col = new HashSet(selection);
		Iterator iter = selection.iterator();
		while (iter.hasNext()) {
			Object item = iter.next();
			if (item instanceof AbstractEGBox)
				addRecursively(col, (AbstractEGBox)item);
			else if (item instanceof AbstractEGRelation)
				addRecursively(col, (AbstractEGRelation)item);
		}
		return col;
	}

	/*
	 * protected static void addRecursively(Collection col, AbstractEGBox box) {
	 * if ((box instanceof EGDefined) || (box instanceof EGSelect)) { Iterator
	 * wires = box.getWires().iterator(); while (wires.hasNext()) { Wire wire =
	 * (Wire)wires.next(); if
	 * ((wire.isAttribute())&&(!col.contains(wire.getRelation()))) {
	 * col.add(wire.getRelation()); addRecursively(col, wire.getRelation()); } } } }
	 * protected static void addRecursively(Collection col, AbstractEGRelation
	 * relation) { Iterator childs = relation.getChilds().iterator(); while
	 * (childs.hasNext()) { AbstractEGBox box = (AbstractEGBox)childs.next(); if
	 * ((!((box instanceof EGEntity)||(box instanceof
	 * EGDefined)))&&(!col.contains(box))) { col.add(box); addRecursively(col,
	 * box); } } } /** updates collection of objects with related simple types
	 * (Enumeration, Selection, Simple) @param selection @return public static
	 * Collection updateForSimpleTypes(Collection selection) { HashSet set = new
	 * HashSet(selection); Iterator iter = selection.iterator(); while
	 * (iter.hasNext()) { Object item = iter.next(); if (item instanceof
	 * AbstractEGBox) { addRecursively(set, (AbstractEGBox)item); } else if
	 * (item instanceof AbstractEGRelation) { addRecursively(set,
	 * (AbstractEGRelation)item); } } return set; } /** compares two page
	 * numbers including ANY_PAGE and NO_PAGE @param pg1 @param pg2 @return
	 */
	public static boolean pagesIdentical(int pg1, int pg2) {
		return (pg1 != Paging.NO_PAGE)
				&& (pg2 != Paging.NO_PAGE)
				&& ((pg1 == pg2) || (pg1 == Paging.ANY_PAGE) || (pg2 == Paging.ANY_PAGE));
	}

	/**
	 * returns Enumeration of awt.Point each point defines new box place
	 * 
	 * @param width
	 * @param height
	 * @return
	 */
	public static Enumeration getObjectPlacementMatrix(double width,
			double height) {
		return new ObjectPlacementMatrix(0, 0, width, height);
	}

	/**
	 * returns Enumeration of awt.Point each point defines new box place
	 * 
	 * @param insetX
	 * @param insetY
	 * @param width
	 * @param height
	 * @return
	 */
	public static Enumeration getObjectPlacementMatrix(double insetX,
			double insetY, double width, double height) {
		return new ObjectPlacementMatrix(insetX, insetY, width, height);
	}

	/**
	 * between all relations finds page reference object
	 * 
	 * @param box AbstractEGBox
	 * @param type int
	 * @return EGPageFrom
	 */
	public static EGPageFrom getPageReference(AbstractEGBox box, int type) {
		EGPageFrom page = null;
		Vector wires = box.getWires();
		Iterator iter = wires.iterator();
		while ((iter.hasNext()) && (page == null)) {
			Wire wire = (Wire)iter.next();
			if (!wire.isAttribute()) {
				AbstractEGBox parent = wire.getRelation().getParent();
				if ((wire.getRelation().getType() == type)
						&& (parent instanceof EGPageFrom)
						&& !(parent instanceof EGPageFromRef))
					page = (EGPageFrom)parent;
			}
		}
		return page;
	}

	/**
	 * between all relations finds page reference object
	 * 
	 * @param box AbstractEGBox
	 * @param type int
	 * @return EGPageFrom
	 */
	public static EGPageRefLocalFrom getPageRef(AbstractEGBox box, int type) {
		EGPageRefLocalFrom page = null;
		Vector wires = box.getWires();
		Iterator iter = wires.iterator();
		while ((iter.hasNext()) && (page == null)) {
			Wire wire = (Wire)iter.next();
			if (!wire.isAttribute()) {
				AbstractEGBox parent = wire.getRelation().getParent();
				if ((wire.getRelation().getType() == type)
						&& (parent instanceof EGPageRefLocalFrom))
					page = (EGPageRefLocalFrom)parent;
			}
		}
		return page;
	}

	/**
	 * creates new relation between first and second objects
	 * 
	 * @param prop
	 * @param first
	 * @param second
	 * @param type
	 * @return
	 */
	public static AbstractEGRelation createRelation(PropertySharing prop,
			Selection first, Selection second, int type) {
		if (((Paging)first).isOnPage(((Paging)second).getPage())) {
			AbstractEGRelation r = null;
			if (first instanceof EGRelationSimple) {
				if (second instanceof AbstractEGBox) {
					EGRelationSimple rs = (EGRelationSimple)first;
					EGRelationTree rt = new EGRelationTree(prop, rs);
					rt.addChild((AbstractEGBox)second);
					rs.eliminate();
					prop.handler().drawableRemove(rs);
					prop.handler().drawableAdd(rt);
					r = rt;
				}
			} else if (first instanceof EGRelationTree) {
				if (second instanceof AbstractEGBox) {
					EGRelationTree rt = (EGRelationTree)first;
					rt.addChild((AbstractEGBox)second);
					r = rt;
				}
			} else {
				if (first instanceof AbstractEGBox && second instanceof AbstractEGBox) {
					r = new EGRelationSimple(prop, (AbstractEGBox)first,
						(AbstractEGBox)second, type);
					prop.handler().drawableAdd(r);
				}
			}
			//      r.setPage(((Paging)first).getPage());
			r.update(2);
			r.update(2);
			return r;
		} else {
			if (first instanceof AbstractEGBox && second instanceof AbstractEGBox) {
				Point pLoc = new Point((first.getLocation().x + second
						.getLocation().x) / 2, (first.getLocation().y + second
						.getLocation().y) / 2);
				EGPageFrom pgf = getPageReference((AbstractEGBox)second, type);
				boolean pgfCreated = true;
				if (pgf == null) {
					pgf = newPageFrom(prop, (AbstractEGBox)second,
							(AbstractEGBox)first);
					pgf.setPage(((Paging)second).getPage());
					pgf.setLocation(pLoc);
					if (createRelation(prop, pgf, second, type) == null)
						pgfCreated = false;
				}
				EGPageTo pgt = newPageTo(prop, pgf, (AbstractEGBox)second);
				pgt.setPage(((Paging)first).getPage());
				pgt.setLocation(pLoc);
				if (pgfCreated) {
					AbstractEGRelation r = createRelation(prop, first, pgt, type);
					if (r != null) {
						prop.handler().drawableAdd(pgt);
						prop.handler().drawableAdd(pgf);
						r.update(2);
					}
					//        r.update(2);
					return r;
				} else {
					return null;
				}
			} else
				return null;
		}
	}

	public static class PageRef {

		/***********************************************************************
		 * private static boolean canBeInvisible(AbstractEGRelation rel) {
		 * boolean can = true; if (rel instanceof EGRelationSimple) { can =
		 * rel.getChild().isOnPage(Paging.INVISIBLE_PAGE) && rel.getChild()
		 * instanceof EGPage; } else { Iterator iter =
		 * rel.getChilds().iterator(); int pgref = 0; while ((iter.hasNext()) &&
		 * (can)) { AbstractEGBox child = (AbstractEGBox) iter.next(); can =
		 * child.isOnPage(Paging.INVISIBLE_PAGE); if (child instanceof EGPage)
		 * pgref++; } if ((can)&&(pgref != 1)) can = false; } return can; }
		 * protected static Collection changePage(PropertySharing prop,
		 * AbstractEGRelation rel, int pgNr, AbstractEGBox invoker) { Collection
		 * changes = new LinkedHashSet(1); if (!rel.isOnPage(pgNr)) {
		 * AbstractEGBox parent = rel.getParent(); if ((parent instanceof
		 * EGPage)&&(((rel.getType() ==
		 * AbstractEGRelation.TYPE_AGREGATION)&&(pgNr ==
		 * Paging.INVISIBLE_PAGE))||(rel.getChild().isOnPage(pgNr)))) {
		 * rel.setPage(pgNr); changes.add(rel); changes.addAll(changePage(prop,
		 * parent, pgNr)); } else if ((parent.isOnPage(pgNr))||((pgNr ==
		 * Paging.INVISIBLE_PAGE)&&(canBeInvisible(rel)))) { rel.setPage(pgNr);
		 * changes.add(rel); } // create references Iterator itc =
		 * rel.getChilds().iterator(); int relPage = rel.getPage(); // if
		 * (relPage != Paging.INVISIBLE_PAGE) while (itc.hasNext()) {
		 * AbstractEGBox child = (AbstractEGBox)itc.next(); if
		 * (!child.isOnPage(relPage)) { if ((child instanceof EGPage)||((child
		 * instanceof EGSimple)&&(child.getWires().size() == 1))) { // move
		 * specific child with relation changes.addAll(changePage(prop, child,
		 * relPage)); } else if ((rel instanceof
		 * EGRelationTree)&&(child.isOnPage(Paging.INVISIBLE_PAGE))&&((child !=
		 * invoker)||(child instanceof EGPage))) { } else { //if
		 * (((!((child.isOnPage(Paging.INVISIBLE_PAGE))&&(child instanceof
		 * EGPage)))||(rel instanceof EGRelationSimple))&& (!(parent instanceof
		 * EGPage))) { Point pLoc = new Point();
		 * pLoc.setLocation((parent.getLocation().getX() +
		 * child.getLocation().getX()) / 2, (parent.getLocation().getY() +
		 * child.getLocation().getY()) / 2); EGPageFrom pgf =
		 * EGToolKit.getPageReference(child, rel.getType()); if (pgf == null) {
		 * pgf = new EGPageFrom(child); pgf.setPage(child.getPage());
		 * pgf.setLocation(pLoc); EGRelationSimple r = new EGRelationSimple(pgf,
		 * child, rel.getType()); changes.add(r); prop.handler().drawableAdd(r);
		 * prop.handler().drawableAdd(pgf); r.setPage(child.getPage());
		 * r.update(2); } EGPageTo pgt = new EGPageTo(pgf); changes.add(pgf);
		 * changes.add(pgt); pgt.setPage(parent.getPage());
		 * pgt.setLocation(pLoc); rel.removeChild(child); rel.addChild(pgt);
		 * prop.handler().drawableAdd(pgt); pgt.update(3); pgf.update(3); } } } }
		 * return changes; } private static boolean
		 * twoBoxesOnSamePage(AbstractEGRelation rel) { boolean eq = false; int
		 * pgNr = rel.getParent().getPage(); if (pgNr != Paging.INVISIBLE_PAGE) {
		 * Iterator iter = rel.getChilds().iterator(); while
		 * ((iter.hasNext())&&(!eq)) { AbstractEGBox box = (AbstractEGBox)
		 * iter.next(); eq = box.isOnPage(pgNr); if ((!eq)&&(box instanceof
		 * EGPageTo)) eq = ((EGPageTo)box).getReferencedObject().isOnPage(pgNr); } }
		 * return eq; } private static boolean relationVisible(Wire wire) {
		 * boolean visible = false; AbstractEGRelation rel = wire.getRelation();
		 * if (!rel.isOnPage(Paging.INVISIBLE_PAGE)) visible = true; else { //
		 * if relation is on invisible page if (twoBoxesOnSamePage(rel)) visible =
		 * true; // allways visible if two boxes are on same page else if
		 * (wire.isAttribute()) { if ((rel.getParent() instanceof
		 * EGEntity)||(rel.getParent() instanceof EGSelect)) { if (rel
		 * instanceof EGRelationSimple) { if (rel.getChild() instanceof EGPage)
		 * visible = true; // bring references together } else { } } else
		 * visible = true; // not entity attributes are allways visible } else {
		 * if (rel.getType() == AbstractEGRelation.TYPE_INHERITANCE) visible =
		 * true; // supertype allways visible else { if (rel.getParent()
		 * instanceof EGPage) visible = true; // bring references together
		 * /**@todo references* } } } return visible; } protected static
		 * Collection changePage(PropertySharing prop, AbstractEGBox box, int
		 * pgNr) { Collection changes = new LinkedHashSet(1); if
		 * (!box.isOnPage(pgNr)) { if (box instanceof EGPage) {
		 * AbstractEGRelation rel = ((EGPage)box).getRefRelation(); if
		 * (rel.isOnPage(pgNr)) { box.setPage(pgNr); changes.add(box); } else if
		 * ((pgNr == Paging.INVISIBLE_PAGE)) {//&&(rel instanceof
		 * EGRelationTree)) { box.setPage(pgNr); changes.add(box); boolean
		 * allInvisible = true; Iterator itc = rel.getChilds().iterator(); while
		 * ((itc.hasNext())&&(allInvisible)) allInvisible =
		 * ((AbstractEGBox)itc.next()).isOnPage(Paging.INVISIBLE_PAGE); if
		 * (allInvisible) changes.addAll(changePage(prop, rel, pgNr, box)); if
		 * (box instanceof EGPageFrom) { Iterator iter =
		 * ((EGPageFrom)box).getReferences().iterator(); while (iter.hasNext()) {
		 * EGPageTo pgt = (EGPageTo)iter.next(); changes.addAll(changePage(prop,
		 * pgt, pgNr)); } } } } else { box.setPage(pgNr); changes.add(box);
		 * Iterator iter = box.getWires().iterator(); while (iter.hasNext()) {
		 * Wire wire = (Wire) iter.next(); AbstractEGRelation rel =
		 * wire.getRelation(); // if
		 * ((!rel.isOnPage(Paging.INVISIBLE_PAGE))||(twoBoxesOnSamePage(rel))|| //
		 * (rel.getParent() instanceof EGPage)) { if (relationVisible(wire)) {
		 * changes.addAll(changePage(prop, rel, pgNr, box)); } } } } return
		 * changes; } /
		 **********************************************************************/
		/**
		 * Collection of items will change page number returns Collection of
		 * affected items
		 * 
		 * @param prop PropertySharing
		 * @param items Collection
		 * @return Collection
		 */
		public static Collection changePage(PropertySharing prop,
				Collection items, int pgNr) {
//System.out.println("<0XO><08>changePage: " + pgNr);
			return changePage(prop, items.iterator(), pgNr);
		}

		private static boolean hasVisibleWires(AbstractEGBox box) {
			Iterator wit = box.getWires().iterator();
			boolean has = false;
			while (!has && wit.hasNext()) {
				Wire wire = (Wire)wit.next();
				if (wire.getRelation().getPage() != Paging.INVISIBLE_PAGE)
					has = true;
			}
			return has;
		}

		/**
		 * Collection of items will change page number returns Collection of
		 * affected items
		 * 
		 * @param prop PropertySharing
		 * @param items Collection
		 * @return Collection
		 */
		public static Collection changePage(PropertySharing prop,
				Iterator iter, int pgNr) {
			LinkedHashSet changes = new LinkedHashSet();
			relationsToSplit.clear();
			referencesToChange.clear();
			while (iter.hasNext()) {
				Object item = iter.next();
				if (item instanceof AbstractDraw && !((AbstractDraw)item).locked()) {
					if (item instanceof EGPage) {

					} else if (item instanceof EGPageRef) {
						if (((EGPageRef)item).isOnPage(Paging.INVISIBLE_PAGE)) {
							changes.addAll(changePage(prop, (AbstractEGBox)item, pgNr));
						} else
						if (!hasVisibleWires((EGPageRef)item)) { // auto delete empty references
							changes.addAll(changePage(prop, (EGPageRef)item, Paging.INVISIBLE_PAGE));
						} else
						if ((/*pgNr == Paging.INVISIBLE_PAGE)
								&& (!hasVisibleWires((EGPageRef)item) ||*/
						// allows to delete empty references (should not be needed if everything works good)
								((prop.getEditMode() & PropertySharing.MODE_LAYOUT_PARTIAL_MASK) != 0))) // for partial layout
							changes.addAll(changePage(prop, (AbstractEGBox)item, pgNr));
					} else if (item instanceof AbstractEGBox) {
						changes.addAll(changePage(prop, (AbstractEGBox)item, pgNr));
					} else if (item instanceof AbstractEGRelation) {
						changes.addAll(changePage(prop, (AbstractEGRelation)item, pgNr));
					} else /*if (item instanceof Paging) all AbstractDraw instances of Paging*/ {
						Paging pg = (Paging)item;
						if (pg.getPage() != Paging.ANY_PAGE)
							pg.setPage(pgNr);
					}
				}
			}
//System.out.println("<0XO><01>before updatePageRefs: " + changes);

			updatePageRefs(prop, changes);
			updateRelations(prop, changes);

			changes.removeAll(removePageRef(prop, changes));

			resetObjectPlacementInfo(changes);
//			UPGRADE no update in page change			prop.handler().repaint(true);

			return changes;
		}

		/**
		 * new version of splitRelation()
		 * 
		 * @param prop
		 * @param changes
		 */
		private static void updateRelations(PropertySharing prop,
				Collection changes) {
			Iterator iter = relationsToSplit.iterator();
			GC gc = prop.getPainting().getLastGraphics();
			while (iter.hasNext()) {
				AbstractEGRelation rel = (AbstractEGRelation)iter.next();
				AbstractEGBox parent = rel.getParent();
				if (parent != null) {
/*					if (rel.getPage() != parent.getPage()) { // ASSERT
						System.err.println("parent page != rel page, REL:"
								+ rel.getName());
						rel.setPage(parent.getPage());
					}*/

					Iterator cit = rel.getChilds().iterator();
					while (cit.hasNext()) {
						AbstractEGBox child = (AbstractEGBox)cit.next();
						if (child.getPage() == parent.getPage()) {
							rel.setPage(parent.getPage());
						} else
						if (child.getPage() != rel.getPage()) { // split relation with page refs
							if (child instanceof EGPageRef) { // ASSERT
//								System.err.println("ALERT:Unallowed type appeared in relation page changing:" + child + " <" + child.getClass() + ">");
								child.setPage(rel.getPage());
								changes.add(child);
								continue;
							}
							Point pLoc = new Point(
									(parent.getLocation().x + child
											.getLocation().x) / 2, (parent
											.getLocation().y + child
											.getLocation().y) / 2);
							EGPageRef pgt = EGToolKit.PageRef
									.getRefForReferenced(prop, child, true);
							changes.add(pgt);
							pgt.setPage(parent.getPage());
							pgt.setLocation(pLoc);
							prop.handler().drawableAdd(pgt);
							rel.removeChild(child);
							rel.addChild(pgt);
							rel.setPage(parent.getPage());    // 2005-12-07 relation not brought to requered page
//							UPGRADE no update in page change							pgt.update(3);
							if (pgt instanceof EGPageRefLocalTo) {
								EGPageRefLocalFrom pgf = EGToolKit.getPageRef(
										child, rel.getType());
								if (pgf == null) {
									pgf = new EGPageRefLocalFrom(prop, child);
									pgf.setPage(child.getPage());
									pgf.setLocation(pLoc);

									EGRelationSimple r = new EGRelationSimple(
											prop, pgf, child, rel.getType());
									changes.add(r);
									prop.handler().drawableAdd(r);
									prop.handler().drawableAdd(pgf);
									r.setPage(child.getPage());
//									UPGRADE no update in page change									r.update(2);
								}
								changes.add(pgf);
								pgf.addLinkedRef((EGPageRefLocalTo)pgt);
//								UPGRADE no update in page change								pgf.update(3);
								pgf.draw(gc);
							}
							pgt.draw(gc);
						}
					}
				}
			}

		}

		private static void updatePageRefs(PropertySharing prop,
				Collection changes) {
			Iterator iter = referencesToChange.iterator();
			while (iter.hasNext()) {
				Object item = iter.next();
				if (item instanceof EGPageRef) {
					EGPageRef ref = (EGPageRef)item;
					Hashtable map = new Hashtable(2);
					Iterator wit = ref.getWires().iterator();
					while (wit.hasNext()) {
						Wire wire = (Wire)wit.next();
						Integer page = new Integer(wire.getRelation().getPage());
						if (map.size() == 0) {
							map.put(page, ref);
//System.out.println("<0XO><08>before setPage: " + page  + ", ref: " + ref);
							ref.setPage(page.intValue());
							changes.add(ref);
						} else {
							if (map.containsKey(page)) {
								EGPageRef ref2 = (EGPageRef)map.get(page);
								if (ref != ref2) {
									ref2.addReferencedRelation(wire
											.getRelation());
								}
							} else {
								EGPageRef ref2 = ref.getOneFor(wire
										.getRelation());
								map.put(page, ref2);
								ref2.setPage(page.intValue());
								prop.handler().drawableAddR(ref2);
								changes.add(ref2);
							}
						}
					}
				}
			}

		}

		private static Collection referencesToChange = new HashSet();

		private static Collection relationsToSplit = new HashSet();
/*
		private static Collection changePage(PropertySharing prop,
				EGPageRef ref, AbstractEGRelation rel, int pgNr) {
			LinkedHashSet changes = new LinkedHashSet(1); // saves changed
														  // objects
			EGPageRef newone = ref.getOneFor(rel);
			newone.setPage(pgNr);
			if (newone != ref)
				prop.handler().drawableAddR(newone);
			changes.add(newone);
			rel.setPage(pgNr);
			changes.add(rel);
			return changes;
		}
*/
		/**
		 * creates page reference for referenced object
		 * 
		 * @param prop
		 * @param referenced
		 * @param hasLinks (in some cases OPTIONAL)
		 * @return
		 */
		public static EGPageRef getRefForReferenced(PropertySharing prop,
				AbstractEGBox referenced, boolean hasLinks) {
			EGPageRef ref = null;
			if (referenced instanceof EGEntityRef)
				ref = new EGPageRefInterschema(prop, (EGEntityRef)referenced);
			else // simple type
			if (referenced instanceof EGSimple)
				ref = new EGPageRefToSimple(prop, (EGSimple)referenced);
			else { // local schema off-page connector
				if (hasLinks) {
					ref = new EGPageRefLocalTo(prop, referenced);
				} else {
					ref = new EGPageRefLocalFrom(prop, referenced);
				}
			}
			return ref;
		}

		public static EGPageRef getRefForReferenced(PropertySharing prop,
				AbstractEGBox referenced) {
			return getRefForReferenced(prop, referenced, false);
		}

		protected static int getRealChildCount(AbstractEGRelation rel) {
			Iterator it = rel.getChilds().iterator();
			int count = 0;
			while (it.hasNext()) {
				Object child = it.next();
				if (!(child instanceof EGPageRef)
						&& (rel.getPage() == ((Paging)child).getPage()))
					count++;
			}
			return count;
		}

		private static Collection changePage(PropertySharing prop,
				AbstractEGRelation rel, int pgNr) {

			LinkedHashSet changes = new LinkedHashSet(); // saves changed objects
/*					if (rel instanceof EGRelationSimple) {
				if (rel.getParent() instanceof EGPage)
					changes.addAll(changePage(prop, rel.getParent(), pgNr));
				else if (rel.getChild() instanceof EGPage)
					changes.addAll(changePage(prop, rel.getChild(),	pgNr));
				else if (rel.getChild() instanceof EGPageRef)
					;
				else
					changes.addAll(changePage(prop, rel.getChild(), pgNr)); // add any attribute
			}*/
			AbstractEGBox parent = rel.getParent();
			if (parent instanceof IHidenRef) {
				EGPageRef ref = getRefForReferenced(prop, parent);
				prop.handler().drawableAddR(ref);
				rel.setParent(ref);
				referencesToChange.add(ref);
				relationsToSplit.add(rel);
				
/*				Iterator cit = rel.getChilds().iterator();
				while (cit.hasNext())
					changes.addAll(changePage(prop, (AbstractEGBox)cit.next(), pgNr));*/
			} else
				changes.addAll(changePage(prop, parent, pgNr));
//			if ((prop.getEditMode() & PropertySharing.MODE_LAYOUT_PARTIAL_MASK) != 0) {
			rel.setPage(pgNr);
			changes.add(rel);
			relationsToSplit.add(rel);
//			}
			return changes;
		}
		/**
		 * ateina tik sveiki boksai, be jokiu page referencu.
		 * 
		 * @param prop
		 * @param box
		 * @param pgNr
		 * @return
		 */
		private static Collection changePage(PropertySharing prop,
				AbstractEGBox box, int pgNr) {
			LinkedHashSet changes = new LinkedHashSet(); // saves changed
														 // objects
			if (box != null && !box.isOnPage(pgNr)) { // perform only if box is not on
									   // specified page
				// change page for main box
				int oldBoxPage = box.getPage();
				//System.out.println("CHANGE BOX: " + box + "<" +
				// box.hashCode() + "> :" + pgNr);
				// wires of this box
				Vector wires = box.getWires();
				Iterator iter;
				// bring page references together
				iter = wires.iterator();
				while (iter.hasNext()) {
					Wire wire = (Wire)iter.next();
					AbstractEGRelation rel = wire.getRelation();
					if ((oldBoxPage != rel.getPage())
							&& (rel.getPage() == Paging.INVISIBLE_PAGE)) { // skipping

					} else {
						if (wire.isAttribute()) {
							//            if (movePartial(prop, rel.getPage())) { // do
							// nothing with attributes in this case
							//System.out.println("PARTIAL MOVE attribute" +
							// wire);
							// XXX

							//            } else { // else create page references for all
							// attributes
							rel.setPage(pgNr);
							changes.add(rel);
							Iterator itc = rel.getChilds().iterator();
							while (itc.hasNext()) {
								AbstractEGBox child = (AbstractEGBox)itc.next();
								if (child instanceof EGPage) { // move page
															   // references
															   // together
									child.setPage(pgNr);
									changes.add(child);
								} else if (child instanceof EGPageRef) {
									referencesToChange.add(child);
									//                	changes.addAll(changePage(prop,
									// (EGPageRef)child, rel, pgNr));
								} else if (child instanceof IHidenRef) {
									EGPageRef ref = getRefForReferenced(prop,
											child);
									ref.setPage(pgNr);
									prop.handler().drawableAddR(ref);
									rel.removeChild(child);
									rel.addChild(ref);
									referencesToChange.add(ref);
									//                	changes.addAll(changePage(prop, ref, rel,
									// pgNr));
								} else { // what to do if its not a page
										 // reference
								//                	rel.setPage(pgNr);
								//                	changes.add(rel);
									relationsToSplit.add(rel);
									//                  splitRelation(prop, rel, child, changes);
								}
							}
							//            }
						} else { // wire is not an attribute
							AbstractEGBox parent = rel.getParent();
							if (parent instanceof EGPage) { // move page
															// references
															// together
								parent.setPage(pgNr);
								changes.add(parent);
								rel.setPage(pgNr);
								changes.add(rel);
							} else if (parent instanceof EGPageRef) {
								if (getRealChildCount(rel) < 2) {
									rel.setPage(pgNr);
									changes.add(rel);
									referencesToChange.add(parent);
									referencesToChange.addAll(rel.getChilds());
								}
								relationsToSplit.add(rel);
								//            	changes.addAll(changePage(prop,
								// (EGPageRef)parent, rel, pgNr));
							} else if (parent instanceof IHidenRef) {
								EGPageRef ref = getRefForReferenced(prop,
										parent);
								prop.handler().drawableAddR(ref);
								rel.setParent(ref);
								rel.setPage(pgNr);
								changes.add(rel);
								referencesToChange.add(ref);
								relationsToSplit.add(rel);
								//            	changes.addAll(changePage(prop, ref, rel,
								// pgNr));
							} else { // what to do if its not a page reference
							//              if (movePartial(prop, rel.getPage())) { // do
							// nothing with references in this case
							//System.out.println("PARTIAL MOVE reference" +
							// wire);
								// XXX
								//              } else {
								//                rel.setPage(parent.getPage());
								//                splitRelation(prop, rel, box, changes);
								if (box instanceof EGPageRef) {
									rel.setPage(pgNr);
									changes.add(rel);
								}
								relationsToSplit.add(rel);
								//              }
							}
						}
					}
				}
				box.setPage(pgNr);
				changes.add(box);
				if (box instanceof EGPageRef) 
					referencesToChange.add(box);
			}
			return changes;
		}

		/**
		 * assert: - parent and child cannot be page ref - relation is on parent
		 * page
		 * 
		 * @param prop
		 * @param rel
		 * @param child
		 * @param changes
		 *
		private static void splitRelation(PropertySharing prop,
				AbstractEGRelation rel, AbstractEGBox child, Collection changes) {
			// create references
			Iterator itc = rel.getChilds().iterator();
			int relPage = rel.getPage();
			AbstractEGBox parent = rel.getParent();
			//System.out.println("SPLITTING " + rel + " :" + relPage);
			//System.out.println("parent: " + parent + " :" +
			// parent.getPage());
			//System.out.println("child: " + child + " :" + child.getPage());
			if (!child.isOnPage(relPage)) { // child is not on same page
				if ((child == parent) || (child instanceof EGPage)
						|| (parent instanceof EGPage)) {
					rel.setPage(parent.getPage());
					changes.add(rel);
				} else {
					Point pLoc = new Point(
							(parent.getLocation().x + child.getLocation().x) / 2,
							(parent.getLocation().y + child.getLocation().y) / 2);
					EGPageFrom pgf = EGToolKit.getPageReference(child, rel
							.getType());
					if (pgf == null) {
						pgf = newPageFrom(prop, child, parent);
						pgf.setPage(child.getPage());
						pgf.setLocation(pLoc);

						EGRelationSimple r = new EGRelationSimple(prop, pgf,
								child, rel.getType());
						changes.add(r);
						prop.handler().drawableAdd(r);
						prop.handler().drawableAdd(pgf);
						r.setPage(child.getPage());
						r.update(2);
					}
					EGPageTo pgt = newPageTo(prop, pgf, pgf
							.getReferencedObject());
					changes.add(pgf);
					changes.add(pgt);
					pgt.setPage(parent.getPage());
					pgt.setLocation(pLoc);
					rel.removeChild(child);
					rel.addChild(pgt);
					prop.handler().drawableAdd(pgt);
					pgt.update(3);
					pgf.update(3);
					//System.out.println("splitted:");
					//System.out.println(parent + "<" + parent.hashCode() + ">
					// -o " + pgt + "<" + pgt.hashCode() + "> ~ " + pgf + "<" +
					// pgf.hashCode() + "> -o " + child + "<" + child.hashCode()
					// + ">");
				}
			}
		}

		/*
		 * private static void updateCollectionForRelationParts(Collection col,
		 * AbstractEGBox box) { if (box instanceof EGPageTo) { EGPageFrom pgf =
		 * ((EGPageTo)box).getReferenced(); col.add(box); col.add(pgf); Iterator
		 * itf = pgf.getWires().iterator(); AbstractEGRelation rel = null; while
		 * ((itf.hasNext())&&(rel == null)) { Wire wire = (Wire)itf.next(); if
		 * (wire.isAttribute()) rel = wire.getRelation(); } col.add(rel); } else
		 * if (box instanceof EGPageFrom) { Iterator itt =
		 * ((EGPageFrom)box).getReferences().iterator(); while (itt.hasNext()) {
		 * Iterator itc = ((EGPageTo)itt.next()).getWires().iterator();
		 * AbstractEGRelation rel = null; while (itc.hasNext()) { Wire wire =
		 * (Wire)itc.next(); if (!wire.isAttribute()) rel = wire.getRelation(); }
		 * col.add(rel); } } } /** updates Collection of items for page
		 * reference objects returns new Collection appended with new items
		 * @param prop PropertySharing @param items Collection @return
		 * Collection public static Collection updatePageRef(PropertySharing
		 * prop, Collection items) { LinkedHashSet hash = new
		 * LinkedHashSet(items); hash.addAll(createPageRef(prop, hash));
		 * Iterator iter = items.iterator(); while (iter.hasNext()) { Object
		 * item = iter.next(); if (item instanceof AbstractEGRelation) {
		 * AbstractEGRelation rel = (AbstractEGRelation)item;
		 * updateCollectionForRelationParts(hash, rel.getParent()); Iterator itc =
		 * rel.getChilds().iterator(); while (itc.hasNext())
		 * updateCollectionForRelationParts(hash, (AbstractEGBox)itc.next()); } }
		 * removePageRef(prop, hash); return hash; } /** creates page references
		 * on wires passed into items returns Collection of items that have
		 * changed @param prop PropertySharing @param items Collection @return
		 * Collection public static Collection createPageRef(PropertySharing
		 * prop, Collection wires) { Collection items = new Vector(); Iterator
		 * iter = wires.iterator(); while (iter.hasNext()) { // creating page
		 * references as needed Object obj = iter.next(); if ((obj instanceof
		 * AbstractEGRelation)&&(!((AbstractEGRelation)obj).isOnPage(Paging.INVISIBLE_PAGE))) {
		 * AbstractEGRelation wire = (AbstractEGRelation)obj; Iterator itr =
		 * wire.getChilds().iterator(); AbstractEGBox parent = wire.getParent();
		 * while (itr.hasNext()) { AbstractEGBox box = (AbstractEGBox)
		 * itr.next(); if (!box.isOnPage(parent.getPage())) { // all boxes that
		 * are not on this page if (box instanceof EGPageTo) {
		 * box.setPage(parent.getPage()); if (!items.contains(box))
		 * items.add(box); } else { Point pLoc = new Point();
		 * pLoc.setLocation((parent.getLocation().getX() +
		 * box.getLocation().getX()) / 2, (parent.getLocation().getY() +
		 * box.getLocation().getY()) / 2); EGPageFrom pgf =
		 * EGToolKit.getPageReference(box, wire.getType()); if (pgf == null) {
		 * pgf = new EGPageFrom(box); pgf.setPage(box.getPage());
		 * pgf.setLocation(pLoc); EGRelationSimple r = new EGRelationSimple(pgf,
		 * box, wire.getType()); items.add(r); prop.handler().drawableAdd(r);
		 * prop.handler().drawableAdd(pgf); // r.setPage(box.getPage());
		 * r.update(2); } EGPageTo pgt = new EGPageTo(pgf); if
		 * (!items.contains(pgf)) items.add(pgf); if (!items.contains(pgt))
		 * items.add(pgt); pgt.setPage(parent.getPage()); pgt.setLocation(pLoc);
		 * wire.removeChild(box); wire.addChild(pgt);
		 * prop.handler().drawableAdd(pgt); pgt.update(3); } } } } } return
		 * items; } /** removes page references as needed page reference objects
		 * (instance of EGPage) must be passed into items param @param prop
		 * PropertySharing @param items Collection @return Collection of removed
		 * items (may include items that were not passed to parameter items)
		 */
		protected static Collection removePageRef(PropertySharing prop,
				Collection items) {
			Iterator iter = items.iterator();
			Collection removed = new Vector();
			/*******************************************************************
			 * System.out.println(); for (Iterator i = items.iterator();
			 * i.hasNext();) { Object item = i.next(); System.out.println(item + " | " +
			 * item.getClass()); } System.out.println("Remove page ref"); /
			 ******************************************************************/
			while (iter.hasNext()) { // removing page references as needed
				Object box = iter.next();
				if ((box instanceof EGPage) && (!removed.contains(box))) {
					//System.out.println("its page");
					Collection pagesTo;
					EGPageFrom pageFrom;
					if (box instanceof EGPageTo) {
						pagesTo = new Vector();
						pagesTo.add(box);
						pageFrom = ((EGPageTo)box).getReferenced();
					} else {
						pageFrom = (EGPageFrom)box;
						pagesTo = pageFrom.getReferences();
					}
					Iterator itpt = pagesTo.iterator();
					while (itpt.hasNext()) {
						EGPageTo pageTo = (EGPageTo)itpt.next();
						if (pageFrom.isOnPage(pageTo.getPage())) { // remove
																   // page
																   // references
						//System.out.println("on same page");
							AbstractEGRelation rel = pageTo.getRefRelation(); // main
																			  // referenced
																			  // relation
							AbstractEGRelation reldel = pageFrom
									.getRefRelation(); // reference relation
													   // (delete)
							//System.err.println("pageFrom " + pageFrom + " <"
							// + pageFrom.getClass() + ">" + pageFrom.hashCode()
							// + " rel: " + reldel + " child: " +
							// reldel.getChild() + " <" +
							// reldel.getChild().getClass() + "> " +
							// reldel.getChild().hashCode());
							//System.err.println("pageTo " + pageTo + " <" +
							// pageTo.getClass() + ">" + pageTo.hashCode() + "
							// rel: " + rel + " parent: " + rel.getParent() + "
							// <" + rel.getParent().getClass() + "> " +
							// rel.getParent().hashCode());
							AbstractEGBox child = pageTo.getReferencedObject();
							//System.err.println(child);

							if ((rel != null) && (reldel != null)) {
								//System.err.println("START: " + rel);
								//System.err.println(rel.getParent() + " <" +
								// rel.getParent().getClass() + ">");
								//System.err.println(rel.getChild() + " <" +
								// rel.getChild().getClass() + ">");
								rel.removeChild(pageTo);
								rel.addChild(child);
								prop.handler().drawableRemove(pageTo);
								removed.add(pageTo);
								pageFrom.removeReference(pageTo);
								//            items.remove(pageTo);
								//System.err.println("rel del (" +
								// pageFrom.getReferencesCount() + ") " +
								// reldel.getClass() + "<" + reldel + ">");
								if (pageFrom.getReferencesCount() <= 0) {
									//System.err.println("removing:" + reldel);
									prop.handler().drawableRemove(reldel);
									prop.handler().drawableRemove(pageFrom);
									removed.add(reldel);
									removed.add(pageFrom);
									reldel.removeChild(child);
									reldel.setParent(null);
									//              child.removeWire(reldel);
									//              items.remove(pageFrom);
									//              items.remove(reldel);
								}// else pageFrom.update(1);
								//System.err.println("updating:");
								//System.err.println(rel == reldel);
								//System.err.println(rel.getParent());
								//System.err.println(rel.getChild());
								//rel.update(2);
							} //else
//								System.err
//										.println("Warning: null relation when removing page references");
						}
					}
				} else if (box instanceof EGPageRefLocalFrom) {
					EGPageRefLocalFrom egf = (EGPageRefLocalFrom)box;
					if (!egf.getLinkedIterator().hasNext()) { // remove empty page references
						Iterator wit = egf.getWires().iterator();
						while (wit.hasNext()) {
							Wire wire = (Wire)wit.next();
							AbstractEGRelation rel = wire.getRelation();
							prop.handler().drawableRemove(rel);
							removed.add(rel);
						}
						egf.eliminate();
						prop.handler().drawableRemove(egf);
						removed.add(egf);
					}
				}
			}
			return removed;
		}
	}

}

class ObjectPlacementMatrix implements Enumeration {

	protected Point objectPlacementMatrix = new Point(0, 0);

	protected Point objectPlacementMatrixMax = new Point(0, 0);

	protected boolean objectPlacementMatrixDirection = true;

	protected double objectPlacementMatrixWidth = 0;

	protected double objectPlacementMatrixHeight = 0;

	protected double objectPlacementMatrixInsetX = 0;

	protected double objectPlacementMatrixInsetY = 0;

	public ObjectPlacementMatrix(double insetX, double insetY, double width,
			double height) {
		objectPlacementMatrixInsetX = insetX;
		objectPlacementMatrixInsetY = insetY;
		setSize(width, height);
	}

	protected void setSize(double width, double height) {
		objectPlacementMatrixWidth = width;
		objectPlacementMatrixHeight = height;
	}

	public boolean hasMoreElements() {
		return true;
	}

	public Object nextElement() {
		Point p = new Point(
				(int)((objectPlacementMatrix.x + 0.5) * objectPlacementMatrixWidth),
				(int)((objectPlacementMatrix.y + 0.5) * objectPlacementMatrixHeight));
		if (objectPlacementMatrixDirection) {
			objectPlacementMatrix.x++;
			if (objectPlacementMatrix.x > objectPlacementMatrixMax.x) {
				objectPlacementMatrixDirection = !objectPlacementMatrixDirection;
				objectPlacementMatrix.y = 0;
				objectPlacementMatrixMax.x++;
			}
		} else {
			objectPlacementMatrix.y++;
			if (objectPlacementMatrix.y > objectPlacementMatrixMax.y) {
				objectPlacementMatrixDirection = !objectPlacementMatrixDirection;
				objectPlacementMatrix.x = 0;
				objectPlacementMatrixMax.y++;
			}
		}
		p.x += objectPlacementMatrixInsetX;
		p.y += objectPlacementMatrixInsetY;
		return p;
	}

}