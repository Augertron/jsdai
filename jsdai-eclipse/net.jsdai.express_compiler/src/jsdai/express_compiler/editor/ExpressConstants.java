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

package jsdai.express_compiler.editor;

public class ExpressConstants {

  public static final String EXPRESS_PARTITIONER = "net.jsdai.express_compiler.editor.ExpressPartitioner";

  public final static String EXPRESS_STRING = "__express_string"; //$NON-NLS-1$
  public final static String EXPRESS_SINGLELINE_COMMENT= "__express_singleline_comment"; //$NON-NLS-1$
  public final static String EXPRESS_MULTILINE_COMMENT= "__express_multiline_comment"; //$NON-NLS-1$

  public final static String[] EXPRESS_PARTITION_TYPES = new String[] {EXPRESS_SINGLELINE_COMMENT, EXPRESS_MULTILINE_COMMENT, EXPRESS_STRING};


}