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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import jsdai.express_g.SdaieditPlugin;

public final class PCSpeedStatistics {
	public static final int TIME_TO_UPDATEABLE_MOVE = 100;		// ms
	public static final int TIME_TO_REDRAW_ON_DESELECT = 400;	// ms
	public static final int TIME_TO_REDRAW_ON_DESELECT_RELATION = 1000;	// ms
	
	
	private static Properties saved = null;
	
	private static double item_draw_time = 0.0;
	private static long item_total = 0;
	private static long current_total = 0;
	private static double total_draw_time = item_draw_time * item_total;
	
	private static long last_save = 0;
	
	private static void init_properties() {
		saved = new Properties();
		load_properties();
	}

	private static void update_properties() {
		saved.setProperty("item_draw_time", String.valueOf(item_draw_time));
		saved.setProperty("item_total", String.valueOf((long)((item_total + current_total) * 0.9)));
	}
	
	private static void load_properties() {
		try {
			String file_name = SdaieditPlugin.getDefault().getStateLocation().append(".pc_statistics").toOSString();
			if (file_name != null) {
				File file = new File(file_name);
				if (file.exists()) try {
					FileInputStream input = new FileInputStream(file);
					if (input != null) try {
						saved.load(input);
						String p = saved.getProperty("item_draw_time");
						if (p != null) try {
							item_draw_time = Double.parseDouble(p);
						} catch (NumberFormatException ex) {}
						p = saved.getProperty("item_total");
						if (p != null) try {
							item_total = Long.parseLong(p);
						} catch (NumberFormatException ex) {}
						total_draw_time = item_draw_time * item_total;
					} catch (IOException ex) {
					}
				} catch (FileNotFoundException ex) {
				}
			}
		} catch (NullPointerException ex) {
		}
	}
	
	private static void save_properties() {
		update_properties();
		try {
			String file_name = SdaieditPlugin.getDefault().getStateLocation().append(".pc_statistics").toOSString();
			if (file_name != null) {
				File file = new File(file_name);
				try {
					FileOutputStream output = new FileOutputStream(file);
					if (output != null) try {
						saved.store(output, "PC statistics by DrM");
					} catch (IOException ex) {
					}
				} catch (FileNotFoundException ex) {
				}
			}
		} catch (NullPointerException ex) {
		}
	}
	
	public static double get_item_draw_time() {
		if (saved == null) init_properties();
		return item_draw_time;
	}
	
	public static double get_draw_time(int entity_count) {
		return get_item_draw_time() * entity_count;
	}
	
	public static void add_statistics(int entity_count, long time) {
		total_draw_time += time;
		current_total += entity_count;
		item_total += entity_count;
		
		item_draw_time = total_draw_time / item_total;
		
		long save_time = System.currentTimeMillis();
		if (save_time - last_save > 60000)
			save_properties();
	}

}
