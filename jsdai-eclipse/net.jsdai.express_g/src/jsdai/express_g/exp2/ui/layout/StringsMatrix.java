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

package jsdai.express_g.exp2.ui.layout;

import java.util.Iterator;
import java.util.Vector;

import jsdai.express_g.exp2.Drawable;

import org.eclipse.swt.graphics.Point;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Mantas Balnys
 * @version 1.0
 */

public class StringsMatrix {

	protected Vector items = new Vector();

	protected int colCount = 0;

	protected int rowCount = 0;

	public int GLOBAL_INSETS_H = 10;

	public int GLOBAL_INSETS_V = 20;

	private static class MatrixItem {

		private Point index;

		private Drawable item;

		public MatrixItem(Point index, Drawable item) {
			this.index = index;
			this.item = item;
		}

		public boolean equals(Object other) {
			if (other instanceof MatrixItem)
				return index.equals(((MatrixItem)other).index);
			else
				return item.equals(other);
		}
		
		/** 
		 * @return
		 * @see java.lang.Object#hashCode()
		 */
		
		public int hashCode() {
			return index.hashCode();
		}

		public String toString() {
			return super.toString() + " [index(" + index.x + ", " + index.y
					+ "); item: " + item + "]";
		}
	}

	public StringsMatrix() {}

	/**
	 * returns indexes of item in the matrix
	 * 
	 * @param item Object
	 * @return Point
	 */
	public Point indexOf(Object item) {
		int index = items.indexOf(item);
		if (index >= 0) {
			MatrixItem mit = (MatrixItem)items.get(index);
			if (mit != null)
				return new Point(mit.index.x, mit.index.y);
		}
		return null;
	}

	/**
	 * true if matrix contains item
	 * 
	 * @param item Object
	 * @return boolean
	 */
	public boolean contains(Object item) {
		return items.contains(item);
	}

	/**
	 * width of matrix - number of columns
	 * 
	 * @return int
	 */
	public int getWidth() {
		return colCount;
	}

	/**
	 * height of matrix - number of rows
	 * 
	 * @return int
	 */
	public int getHeight() {
		return rowCount;
	}

	public Drawable put(Drawable item, Point p) {
		colCount = Math.max(colCount, p.x + 1);
		rowCount = Math.max(rowCount, p.y + 1);
		MatrixItem mit = new MatrixItem(p, item);
		int old = items.indexOf(mit);
		Drawable oldItem = null;
		if (old >= 0)
			oldItem = ((MatrixItem)items.remove(old)).item;
		items.add(mit);
		return oldItem;
	}

	/**
	 * puts item into the matrix matrix expands if needed returns object which
	 * was in that place, null if none
	 * 
	 * @param item Object
	 * @param col int
	 * @param row int
	 * @return Object
	 */
	public Drawable put(Drawable item, int col, int row) {
		return put(item, new Point(col, row));
	}

	public void insertColAfter(Point p) {
		insertColsAfter(p, 1);
	}

	/**
	 * inserts count columuns into the matrix after the p column updates p for
	 * column changes
	 * 
	 * @param p Point
	 * @param count int
	 */
	public void insertColsAfter(Point p, int count) {
		insertColsBefore(new Point(p.x + 1, p.y), count);
	}

	public void insertColBefore(Point p) {
		insertColsBefore(p, 1);
	}

	/**
	 * inserts count columuns into the matrix before the p column updates p for
	 * column changes
	 * 
	 * @param p Point
	 * @param count int
	 */
	public void insertColsBefore(Point p, int count) {
		Iterator iter = items.iterator();
		while (iter.hasNext()) {
			MatrixItem mit = (MatrixItem)iter.next();
			//System.out.println(mit);
			//System.out.println(p);
			if (mit.index.x >= p.x)
				mit.index.x += count;
		}
		p.x += count;
		colCount += count;
	}

	public void insertRowAfter(Point p) {
		insertRowsAfter(p, 1);
	}

	/**
	 * inserts count rows into the matrix after the p row updates p for row
	 * changes
	 * 
	 * @param p Point
	 * @param count int
	 */
	public void insertRowsAfter(Point p, int count) {
		insertRowsBefore(new Point(p.x, p.y + 1), count);
	}

	public void insertRowBefore(Point p) {
		insertRowsBefore(p, 1);
	}

	/**
	 * inserts count rows into the matrix before the p row updates p for row
	 * changes
	 * 
	 * @param p Point
	 * @param count int
	 */
	public void insertRowsBefore(Point p, int count) {
		Iterator iter = items.iterator();
		while (iter.hasNext()) {
			MatrixItem mit = (MatrixItem)iter.next();
			if (mit.index.y >= p.y)
				mit.index.y += count;
		}
		p.y += count;
		rowCount += count;
	}

	/**
	 * sets place parameter for all items in the matrix
	 * 
	 * @param start Point
	 * @param gaps Dimension
	 */
	public void setRealPlacement(Point start, Point gaps) {
		if ((colCount > 0) && (rowCount > 0)) {
			int[] placeX = new int[colCount + 1];
			int[] placeY = new int[rowCount + 1];
			MatrixItem first = (MatrixItem)items.get(0);
			if (gaps.x <= 0) {
				Iterator iter = items.iterator();
				while (iter.hasNext()) {
					MatrixItem mit = (MatrixItem)iter.next();
					placeX[mit.index.x + 1] = Math.max(placeX[mit.index.x + 1],
							(int)mit.item.getSize().x);
				}
				for (int i = 1; i < placeX.length; i++)
					placeX[i] += placeX[i - 1] + GLOBAL_INSETS_H;
				int stp = Math.max(placeX[0], start.x - placeX[first.index.x]);
				for (int i = 0; i < placeX.length; i++)
					placeX[i] += stp;
			} else {
				//Starting X point
				placeX[0] = Math.max(gaps.x / 2, start.x - (first.index.x - 1)
						* gaps.x);
				for (int i = 1; i < placeX.length; i++)
					placeX[i] = placeX[i - 1] + gaps.x;
			}
			if (gaps.y <= 0) {
				Iterator iter = items.iterator();
				while (iter.hasNext()) {
					MatrixItem mit = (MatrixItem)iter.next();
					placeY[mit.index.y + 1] = Math.max(placeY[mit.index.y + 1],
							(int)mit.item.getSize().y);
				}
				for (int i = 1; i < placeY.length; i++)
					placeY[i] += placeY[i - 1] + GLOBAL_INSETS_V;
				int stp = Math.max(placeY[0], start.y - placeY[first.index.y]);
				for (int i = 0; i < placeY.length; i++)
					placeY[i] += stp;
			} else {
				//Starting Y point
				placeY[0] = Math.max(gaps.y / 2, start.y - (first.index.y - 1)
						* gaps.y);
				for (int i = 1; i < placeY.length; i++)
					placeY[i] = placeY[i - 1] + gaps.y;
			}

			Iterator iter = items.iterator();
			while (iter.hasNext()) {
				MatrixItem mit = (MatrixItem)iter.next();
				Point loc = new Point(placeX[mit.index.x], placeY[mit.index.y]);
//				Point loc = new Point(placeX[mit.index.x] - mit.item.getSize().x / 2,
//						placeY[mit.index.y] - mit.item.getSize().y / 2);
				mit.item.setLocation(loc);
			}
		}
	}

}