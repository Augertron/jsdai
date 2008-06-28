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

package jsdai.express_compiler.preferences;

import jsdai.express_compiler.help.IExpressCompilerHelpContextIds;

import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
//import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.editors.text.EditorsPlugin;

public class P21EditorPreferences extends PreferencePage  implements IWorkbenchPreferencePage {

	public static final String  PREFIX = "net.jsdai.express_compiler.preferences.P21EditorPreferences.";

	static P21EditorPreferences fP21EditorPreferences;
	IPreferenceStore fStore;
	
	/*
		  
		  possible solutions:
		  
		  1. To have a separate header partition and its scanner,
		  and, therefore - separate colors for everything in headers
		  
		  2. The hierarchy of color selection could go:
		  
		  ISO
		  Section
		  Header
		  	Keyword
		  	String
		  	Undefined
		  	Delimeter
		  	Others/Default/Error
		  Data
		  	simple entities
		  		Instance
		  		Entity
		  		Type
		  		Values
		  			Integer
		  			Real
		  			Logical
		  			Enumeration
		  			String
		  			Binary
		  			Undefined
		  			Redefined
		  		Delimeter
		  		Error	
		  	complex entities
		  		Instance
		  		Entity
		  		Type
		  		Values
		  			Integer
		  			Real
		  			Logical
		  			Enumeration
		  			String
		  			Binary
		  			Undefined
		  			Redefined
					Delimeter
					Error		  
			Comment


			Simplified:
			
			ISO (unless included inside header|
			Header - includes Keywords, without explicitly naming "Keywords": FILE_....
  		Instance
  		Entity
  		Type
  		Values
  			Integer
  			Real
  			Logical
  			Enumeration
  			String
  			Binary
  			Undefined
  			Redefined
			Delimeter  (?)
			Error		  (???) - perhaps use the same
			Complex entities
	  		Instance
	  		Entity
	  		Type
	  		Values
	  			Integer
	  			Real
	  			Logical
	  			Enumeration
	  			String
	  			Binary
	  			Undefined
	  			Redefined
				Delimeter  (?)
				Error		  (???) - perhaps use the same
			Comment				
			
			
			perhaps do this:
			
			ISO  
			Section
			Header keywords
			Header + simple entities
	  		Instance
	  		Entity
	  		Type
	  		Values
	  			Integer
	  			Real
	  			Logical
	  			Enumeration
	  			String
	  			Binary
	  			Undefined
	  			Redefined
				Delimeter
			Complex Entities
	  		Instance
	  		Entity
	  		Type
	  		Values
	  			Integer
	  			Real
	  			Logical
	  			Enumeration
	  			String
	  			Binary
	  			Undefined
	  			Redefined
				Delimeter
			Comment
			Error
			
	
	*/
	
	

	private ColorSelector fColorEditor;
	private ColorSelector fBgColorEditor;
//	private List fColorList;
//	private Button fColorDefault;
    private Button fTransparentCheckBox;
    private Button fBoldCheckBox;
    private Button fItalicCheckBox;
    private Button fStrikethroughCheckBox;
    private Button fUnderlineCheckBox;
	
	
	
	
	Tree fSyntaxHighlightingTree;

	/*
		ISO-10303-21
		END-ISO-10303-21;

	*/
	TreeItem fIso; 
	private static final String strIso = "ISO tag";
	public static final String idFgIso  = PREFIX + "fgIso";
	public static final String idBgIso  = PREFIX + "bgIso";
	public static final String idTrIso  = PREFIX + "trIso";
	public static final String idBIso  = PREFIX + "bIso";
	public static final String idIIso  = PREFIX + "iIso";
	public static final String idSIso  = PREFIX + "sIso";
	public static final String idUIso  = PREFIX + "uIso";
	private static final String fgIso = "0,0,0";
	private static final String bgIso = "255,255,255";
	private static final boolean bIso = true;
	private static final boolean iIso = false;
	private static final boolean sIso = false;
	private static final boolean uIso = false;

	/*
			HEADER
			DATA
			ENDSEC
	*/
	TreeItem fSection;
	private static final String strSection = "Section";
	public static final String idFgSection  = PREFIX + "fgSection";
	public static final String idBgSection  = PREFIX + "bgSection";
	public static final String idTrSection  = PREFIX + "trSection";
	public static final String idBSection  = PREFIX + "bSection";
	public static final String idISection  = PREFIX + "iSection";
	public static final String idSSection  = PREFIX + "sSection";
	public static final String idUSection  = PREFIX + "uSection";
	private static final String fgSection = "0,0,128";
	private static final String bgSection = "255,255,255";
	private static final boolean bSection = true;
	private static final boolean iSection = false;
	private static final boolean sSection = false;
	private static final boolean uSection = false;

	/*
			FILE_DESCRIPTION
			FILE_NAME
			FILE_SCHEMA
			FILE_POPULATION
	*/
	TreeItem fHeaderKeyword;
	private static final String strHeaderKeyword = "Header keyword";
	public static final String idFgHeaderKeyword  = PREFIX + "fgHeaderKeyword";
	public static final String idBgHeaderKeyword  = PREFIX + "bgHeaderKeyword";
	public static final String idTrHeaderKeyword  = PREFIX + "trHeaderKeyword";
	public static final String idBHeaderKeyword  = PREFIX + "bHeaderKeyword";
	public static final String idIHeaderKeyword  = PREFIX + "iHeaderKeyword";
	public static final String idSHeaderKeyword  = PREFIX + "sHeaderKeyword";
	public static final String idUHeaderKeyword  = PREFIX + "uHeaderKeyword";
	private static final String fgHeaderKeyword = "128,0,0";
	private static final String bgHeaderKeyword = "255,255,255";
	private static final boolean bHeaderKeyword = false;
	private static final boolean iHeaderKeyword = false;
	private static final boolean sHeaderKeyword = false;
	private static final boolean uHeaderKeyword = false;
	
	
	/*
			Header + simple entities
	*/
	TreeItem fSimpleEntities;  
	String  strSimpleEntities = "Simple entities + header"; 
	
	TreeItem fSimpleEntitiesInstance;
	private static final String strSimpleEntitiesInstance = "Instance";
	public static final String idFgSimpleEntitiesInstance  = PREFIX + "fgSimpleEntitiesInstance";
	public static final String idBgSimpleEntitiesInstance  = PREFIX + "bgSimpleEntitiesInstance";
	public static final String idTrSimpleEntitiesInstance  = PREFIX + "trSimpleEntitiesInstance";
	public static final String idBSimpleEntitiesInstance  = PREFIX + "bSimpleEntitiesInstance";
	public static final String idISimpleEntitiesInstance  = PREFIX + "iSimpleEntitiesInstance";
	public static final String idSSimpleEntitiesInstance  = PREFIX + "sSimpleEntitiesInstance";
	public static final String idUSimpleEntitiesInstance  = PREFIX + "uSimpleEntitiesInstance";
	private static final String fgSimpleEntitiesInstance = "64,0,0";
	private static final String bgSimpleEntitiesInstance = "255,255,255";
	private static final boolean bSimpleEntitiesInstance = false;
	private static final boolean iSimpleEntitiesInstance = false;
	private static final boolean sSimpleEntitiesInstance = false;
	private static final boolean uSimpleEntitiesInstance = false;
	
	TreeItem fSimpleEntitiesEntity;
	private static final String  strSimpleEntitiesEntity = "Entity";
	public static final String idFgSimpleEntitiesEntity  = PREFIX + "fgSimpleEntitiesEntity";
	public static final String idBgSimpleEntitiesEntity  = PREFIX + "bgSimpleEntitiesEntity";
	public static final String idTrSimpleEntitiesEntity  = PREFIX + "trSimpleEntitiesEntity";
	public static final String idBSimpleEntitiesEntity  = PREFIX + "bSimpleEntitiesEntity";
	public static final String idISimpleEntitiesEntity  = PREFIX + "iSimpleEntitiesEntity";
	public static final String idSSimpleEntitiesEntity  = PREFIX + "sSimpleEntitiesEntity";
	public static final String idUSimpleEntitiesEntity  = PREFIX + "uSimpleEntitiesEntity";
	private static final String fgSimpleEntitiesEntity = "0,0,128";
	private static final String bgSimpleEntitiesEntity = "255,255,255";
	private static final boolean bSimpleEntitiesEntity = false;
	private static final boolean iSimpleEntitiesEntity = false;
	private static final boolean sSimpleEntitiesEntity = false;
	private static final boolean uSimpleEntitiesEntity = false;

	TreeItem fSimpleEntitiesType;   
	private static final String  strSimpleEntitiesType = "Type";   
	public static final String idFgSimpleEntitiesType  = PREFIX + "fgSimpleEntitiesType";
	public static final String idBgSimpleEntitiesType  = PREFIX + "bgSimpleEntitiesType";
	public static final String idTrSimpleEntitiesType  = PREFIX + "trSimpleEntitiesType";
	public static final String idBSimpleEntitiesType  = PREFIX + "bSimpleEntitiesType";
	public static final String idISimpleEntitiesType  = PREFIX + "iSimpleEntitiesType";
	public static final String idSSimpleEntitiesType  = PREFIX + "sSimpleEntitiesType";
	public static final String idUSimpleEntitiesType  = PREFIX + "uSimpleEntitiesType";
	private static final String fgSimpleEntitiesType = "0,0,255";
	private static final String bgSimpleEntitiesType = "255,255,255";
	private static final boolean bSimpleEntitiesType = false;
	private static final boolean iSimpleEntitiesType = false;
	private static final boolean sSimpleEntitiesType = false;
	private static final boolean uSimpleEntitiesType = false;

	TreeItem fSimpleEntitiesValues;   
	private static final String  strSimpleEntitiesValues = "Values";   

	TreeItem fSimpleEntitiesValuesInteger;
	private static final String strSimpleEntitiesValuesInteger = "Integer";
	public static final String idFgSimpleEntitiesValuesInteger  = PREFIX + "fgSimpleEntitiesValuesInteger";
	public static final String idBgSimpleEntitiesValuesInteger  = PREFIX + "bgSimpleEntitiesValuesInteger";
	public static final String idTrSimpleEntitiesValuesInteger  = PREFIX + "trSimpleEntitiesValuesInteger";
	public static final String idBSimpleEntitiesValuesInteger  = PREFIX + "bSimpleEntitiesValuesInteger";
	public static final String idISimpleEntitiesValuesInteger  = PREFIX + "iSimpleEntitiesValuesInteger";
	public static final String idSSimpleEntitiesValuesInteger  = PREFIX + "sSimpleEntitiesValuesInteger";
	public static final String idUSimpleEntitiesValuesInteger  = PREFIX + "uSimpleEntitiesValuesInteger";
	private static final String fgSimpleEntitiesValuesInteger = "0,64,128";
	private static final String bgSimpleEntitiesValuesInteger = "255,255,255";
	private static final boolean bSimpleEntitiesValuesInteger = false;
	private static final boolean iSimpleEntitiesValuesInteger = false;
	private static final boolean sSimpleEntitiesValuesInteger = false;
	private static final boolean uSimpleEntitiesValuesInteger = false;

	TreeItem fSimpleEntitiesValuesReal;
	private static final String  strSimpleEntitiesValuesReal = "Real";
	public static final String idFgSimpleEntitiesValuesReal  = PREFIX + "fgSimpleEntitiesValuesReal";
	public static final String idBgSimpleEntitiesValuesReal  = PREFIX + "bgSimpleEntitiesValuesReal";
	public static final String idTrSimpleEntitiesValuesReal  = PREFIX + "trSimpleEntitiesValuesReal";
	public static final String idBSimpleEntitiesValuesReal  = PREFIX + "bSimpleEntitiesValuesReal";
	public static final String idISimpleEntitiesValuesReal  = PREFIX + "iSimpleEntitiesValuesReal";
	public static final String idSSimpleEntitiesValuesReal  = PREFIX + "sSimpleEntitiesValuesReal";
	public static final String idUSimpleEntitiesValuesReal  = PREFIX + "uSimpleEntitiesValuesReal";
	private static final String fgSimpleEntitiesValuesReal = "0,64,128";
	private static final String bgSimpleEntitiesValuesReal = "255,255,255";
	private static final boolean bSimpleEntitiesValuesReal = false;
	private static final boolean iSimpleEntitiesValuesReal = false;
	private static final boolean sSimpleEntitiesValuesReal = false;
	private static final boolean uSimpleEntitiesValuesReal = false;

	TreeItem fSimpleEntitiesValuesLogical;
	private static final String strSimpleEntitiesValuesLogical = "Logical";
	public static final String idFgSimpleEntitiesValuesLogical  = PREFIX + "fgSimpleEntitiesValuesLogical";
	public static final String idBgSimpleEntitiesValuesLogical  = PREFIX + "bgSimpleEntitiesValuesLogical";
	public static final String idTrSimpleEntitiesValuesLogical  = PREFIX + "trSimpleEntitiesValuesLogical";
	public static final String idBSimpleEntitiesValuesLogical  = PREFIX + "bSimpleEntitiesValuesLogical";
	public static final String idISimpleEntitiesValuesLogical  = PREFIX + "iSimpleEntitiesValuesLogical";
	public static final String idSSimpleEntitiesValuesLogical  = PREFIX + "sSimpleEntitiesValuesLogical";
	public static final String idUSimpleEntitiesValuesLogical  = PREFIX + "uSimpleEntitiesValuesLogical";
	private static final String fgSimpleEntitiesValuesLogical = "0,64,128";
	private static final String bgSimpleEntitiesValuesLogical = "255,255,255";
	private static final boolean bSimpleEntitiesValuesLogical = false;
	private static final boolean iSimpleEntitiesValuesLogical = false;
	private static final boolean sSimpleEntitiesValuesLogical = false;
	private static final boolean uSimpleEntitiesValuesLogical = false;

	TreeItem fSimpleEntitiesValuesEnumeration;
	private static final String strSimpleEntitiesValuesEnumeration = "Enumeration";
	public static final String idFgSimpleEntitiesValuesEnumeration  = PREFIX + "fgSimpleEntitiesValuesEnumeration";
	public static final String idBgSimpleEntitiesValuesEnumeration  = PREFIX + "bgSimpleEntitiesValuesEnumeration";
	public static final String idTrSimpleEntitiesValuesEnumeration  = PREFIX + "trSimpleEntitiesValuesEnumeration";
	public static final String idBSimpleEntitiesValuesEnumeration  = PREFIX + "bSimpleEntitiesValuesEnumeration";
	public static final String idISimpleEntitiesValuesEnumeration  = PREFIX + "iSimpleEntitiesValuesEnumeration";
	public static final String idSSimpleEntitiesValuesEnumeration  = PREFIX + "sSimpleEntitiesValuesEnumeration";
	public static final String idUSimpleEntitiesValuesEnumeration  = PREFIX + "uSimpleEntitiesValuesEnumeration";
	private static final String fgSimpleEntitiesValuesEnumeration = "0,64,128";
	private static final String bgSimpleEntitiesValuesEnumeration = "255,255,255";
	private static final boolean bSimpleEntitiesValuesEnumeration = false;
	private static final boolean iSimpleEntitiesValuesEnumeration = false;
	private static final boolean sSimpleEntitiesValuesEnumeration = false;
	private static final boolean uSimpleEntitiesValuesEnumeration = false;

	TreeItem fSimpleEntitiesValuesString;
	private static final String strSimpleEntitiesValuesString = "String";
	public static final String idFgSimpleEntitiesValuesString  = PREFIX + "fgSimpleEntitiesValuesString";
	public static final String idBgSimpleEntitiesValuesString  = PREFIX + "bgSimpleEntitiesValuesString";
	public static final String idTrSimpleEntitiesValuesString  = PREFIX + "trSimpleEntitiesValuesString";
	public static final String idBSimpleEntitiesValuesString  = PREFIX + "bSimpleEntitiesValuesString";
	public static final String idISimpleEntitiesValuesString  = PREFIX + "iSimpleEntitiesValuesString";
	public static final String idSSimpleEntitiesValuesString  = PREFIX + "sSimpleEntitiesValuesString";
	public static final String idUSimpleEntitiesValuesString  = PREFIX + "uSimpleEntitiesValuesString";
	private static final String fgSimpleEntitiesValuesString = "64,128,128";
	private static final String bgSimpleEntitiesValuesString = "255,255,255";
	private static final boolean bSimpleEntitiesValuesString = false;
	private static final boolean iSimpleEntitiesValuesString = false;
	private static final boolean sSimpleEntitiesValuesString = false;
	private static final boolean uSimpleEntitiesValuesString = false;

	TreeItem fSimpleEntitiesValuesBinary;
	private static final String strSimpleEntitiesValuesBinary = "Binary";
	public static final String idFgSimpleEntitiesValuesBinary  = PREFIX + "fgSimpleEntitiesValuesBinary";
	public static final String idBgSimpleEntitiesValuesBinary  = PREFIX + "bgSimpleEntitiesValuesBinary";
	public static final String idTrSimpleEntitiesValuesBinary  = PREFIX + "trSimpleEntitiesValuesBinary";
	public static final String idBSimpleEntitiesValuesBinary  = PREFIX + "bSimpleEntitiesValuesBinary";
	public static final String idISimpleEntitiesValuesBinary  = PREFIX + "iSimpleEntitiesValuesBinary";
	public static final String idSSimpleEntitiesValuesBinary  = PREFIX + "sSimpleEntitiesValuesBinary";
	public static final String idUSimpleEntitiesValuesBinary  = PREFIX + "uSimpleEntitiesValuesBinary";
	private static final String fgSimpleEntitiesValuesBinary = "0,64,128";
	private static final String bgSimpleEntitiesValuesBinary = "255,255,255";
	private static final boolean bSimpleEntitiesValuesBinary = false;
	private static final boolean iSimpleEntitiesValuesBinary = false;
	private static final boolean sSimpleEntitiesValuesBinary = false;
	private static final boolean uSimpleEntitiesValuesBinary = false;

	TreeItem fSimpleEntitiesValuesUndefined;
	private static final String strSimpleEntitiesValuesUndefined = "Undefined";
	public static final String idFgSimpleEntitiesValuesUndefined  = PREFIX + "fgSimpleEntitiesValuesUndefined";
	public static final String idBgSimpleEntitiesValuesUndefined  = PREFIX + "bgSimpleEntitiesValuesUndefined";
	public static final String idTrSimpleEntitiesValuesUndefined  = PREFIX + "trSimpleEntitiesValuesUndefined";
	public static final String idBSimpleEntitiesValuesUndefined  = PREFIX + "bSimpleEntitiesValuesUndefined";
	public static final String idISimpleEntitiesValuesUndefined  = PREFIX + "iSimpleEntitiesValuesUndefined";
	public static final String idSSimpleEntitiesValuesUndefined  = PREFIX + "sSimpleEntitiesValuesUndefined";
	public static final String idUSimpleEntitiesValuesUndefined  = PREFIX + "uSimpleEntitiesValuesUndefined";
	private static final String fgSimpleEntitiesValuesUndefined = "0,64,128";
	private static final String bgSimpleEntitiesValuesUndefined = "255,255,255";
	private static final boolean bSimpleEntitiesValuesUndefined = false;
	private static final boolean iSimpleEntitiesValuesUndefined = false;
	private static final boolean sSimpleEntitiesValuesUndefined = false;
	private static final boolean uSimpleEntitiesValuesUndefined = false;

	TreeItem fSimpleEntitiesValuesRedefined;
	private static final String strSimpleEntitiesValuesRedefined = "Redefined";
	public static final String idFgSimpleEntitiesValuesRedefined  = PREFIX + "fgSimpleEntitiesValuesRedefined";
	public static final String idBgSimpleEntitiesValuesRedefined  = PREFIX + "bgSimpleEntitiesValuesRedefined";
	public static final String idTrSimpleEntitiesValuesRedefined  = PREFIX + "trSimpleEntitiesValuesRedefined";
	public static final String idBSimpleEntitiesValuesRedefined  = PREFIX + "bSimpleEntitiesValuesRedefined";
	public static final String idISimpleEntitiesValuesRedefined  = PREFIX + "iSimpleEntitiesValuesRedefined";
	public static final String idSSimpleEntitiesValuesRedefined  = PREFIX + "sSimpleEntitiesValuesRedefined";
	public static final String idUSimpleEntitiesValuesRedefined  = PREFIX + "uSimpleEntitiesValuesRedefined";
	private static final String fgSimpleEntitiesValuesRedefined = "0,64,128";
	private static final String bgSimpleEntitiesValuesRedefined = "255,255,255";
	private static final boolean bSimpleEntitiesValuesRedefined = false;
	private static final boolean iSimpleEntitiesValuesRedefined = false;
	private static final boolean sSimpleEntitiesValuesRedefined = false;
	private static final boolean uSimpleEntitiesValuesRedefined = false;

	TreeItem fSimpleEntitiesDelimeter;
	private static final String strSimpleEntitiesDelimeter = "Delimeter";
	public static final String idFgSimpleEntitiesDelimeter  = PREFIX + "fgSimpleEntitiesDelimeter";
	public static final String idBgSimpleEntitiesDelimeter  = PREFIX + "bgSimpleEntitiesDelimeter";
	public static final String idTrSimpleEntitiesDelimeter  = PREFIX + "trSimpleEntitiesDelimeter";
	public static final String idBSimpleEntitiesDelimeter  = PREFIX + "bSimpleEntitiesDelimeter";
	public static final String idISimpleEntitiesDelimeter  = PREFIX + "iSimpleEntitiesDelimeter";
	public static final String idSSimpleEntitiesDelimeter  = PREFIX + "sSimpleEntitiesDelimeter";
	public static final String idUSimpleEntitiesDelimeter  = PREFIX + "uSimpleEntitiesDelimeter";
	private static final String fgSimpleEntitiesDelimeter = "0,0,0";
	private static final String bgSimpleEntitiesDelimeter = "255,255,255";
	private static final boolean bSimpleEntitiesDelimeter = false;
	private static final boolean iSimpleEntitiesDelimeter = false;
	private static final boolean sSimpleEntitiesDelimeter = false;
	private static final boolean uSimpleEntitiesDelimeter = false;

	/*
			Complex entities
	*/
	TreeItem fComplexEntities; 
	String strComplexEntities = "Complex entities";
	
	TreeItem fComplexEntitiesInstance;
	static final String strComplexEntitiesInstance = "Instance";
	public static final String idFgComplexEntitiesInstance  = PREFIX + "fgComplexEntitiesInstance";
	public static final String idBgComplexEntitiesInstance  = PREFIX + "bgComplexEntitiesInstance";
	public static final String idTrComplexEntitiesInstance  = PREFIX + "trComplexEntitiesInstance";
	public static final String idBComplexEntitiesInstance  = PREFIX + "bComplexEntitiesInstance";
	public static final String idIComplexEntitiesInstance  = PREFIX + "iComplexEntitiesInstance";
	public static final String idSComplexEntitiesInstance  = PREFIX + "sComplexEntitiesInstance";
	public static final String idUComplexEntitiesInstance  = PREFIX + "uComplexEntitiesInstance";
	private static final String fgComplexEntitiesInstance = "96,32,32";
	private static final String bgComplexEntitiesInstance = "255,255,255";
	private static final boolean bComplexEntitiesInstance = true;
	private static final boolean iComplexEntitiesInstance = false;
	private static final boolean sComplexEntitiesInstance = false;
	private static final boolean uComplexEntitiesInstance = false;

	TreeItem fComplexEntitiesEntity;
	private static final String  strComplexEntitiesEntity = "Entity";
	public static final String idFgComplexEntitiesEntity  = PREFIX + "fgComplexEntitiesEntity";
	public static final String idBgComplexEntitiesEntity  = PREFIX + "bgComplexEntitiesEntity";
	public static final String idTrComplexEntitiesEntity  = PREFIX + "trComplexEntitiesEntity";
	public static final String idBComplexEntitiesEntity  = PREFIX + "bComplexEntitiesEntity";
	public static final String idIComplexEntitiesEntity  = PREFIX + "iComplexEntitiesEntity";
	public static final String idSComplexEntitiesEntity  = PREFIX + "sComplexEntitiesEntity";
	public static final String idUComplexEntitiesEntity  = PREFIX + "uComplexEntitiesEntity";
	private static final String fgComplexEntitiesEntity = "32,32,160";
	private static final String bgComplexEntitiesEntity = "255,255,255";
	private static final boolean bComplexEntitiesEntity = true;
	private static final boolean iComplexEntitiesEntity = false;
	private static final boolean sComplexEntitiesEntity = false;
	private static final boolean uComplexEntitiesEntity = false;

	TreeItem fComplexEntitiesType;   
	private static final String  strComplexEntitiesType = "Type";   
	public static final String idFgComplexEntitiesType  = PREFIX + "fgComplexEntitiesType";
	public static final String idBgComplexEntitiesType  = PREFIX + "bgComplexEntitiesType";
	public static final String idTrComplexEntitiesType  = PREFIX + "trComplexEntitiesType";
	public static final String idBComplexEntitiesType  = PREFIX + "bComplexEntitiesType";
	public static final String idIComplexEntitiesType  = PREFIX + "iComplexEntitiesType";
	public static final String idSComplexEntitiesType  = PREFIX + "sComplexEntitiesType";
	public static final String idUComplexEntitiesType  = PREFIX + "uComplexEntitiesType";
	private static final String fgComplexEntitiesType = "0,0,255";
	private static final String bgComplexEntitiesType = "255,255,255";
	private static final boolean bComplexEntitiesType = true;
	private static final boolean iComplexEntitiesType = false;
	private static final boolean sComplexEntitiesType = false;
	private static final boolean uComplexEntitiesType = false;

	TreeItem fComplexEntitiesValues;   
	private static final String  strComplexEntitiesValues = "Values";   

	TreeItem fComplexEntitiesValuesInteger;
	private static final String strComplexEntitiesValuesInteger = "Integer";
	public static final String idFgComplexEntitiesValuesInteger  = PREFIX + "fgComplexEntitiesValuesInteger";
	public static final String idBgComplexEntitiesValuesInteger  = PREFIX + "bgComplexEntitiesValuesInteger";
	public static final String idTrComplexEntitiesValuesInteger  = PREFIX + "trComplexEntitiesValuesInteger";
	public static final String idBComplexEntitiesValuesInteger  = PREFIX + "bComplexEntitiesValuesInteger";
	public static final String idIComplexEntitiesValuesInteger  = PREFIX + "iComplexEntitiesValuesInteger";
	public static final String idSComplexEntitiesValuesInteger  = PREFIX + "sComplexEntitiesValuesInteger";
	public static final String idUComplexEntitiesValuesInteger  = PREFIX + "uComplexEntitiesValuesInteger";
	private static final String fgComplexEntitiesValuesInteger = "32,96,160";
	private static final String bgComplexEntitiesValuesInteger = "255,255,255";
	private static final boolean bComplexEntitiesValuesInteger = true;
	private static final boolean iComplexEntitiesValuesInteger = false;
	private static final boolean sComplexEntitiesValuesInteger = false;
	private static final boolean uComplexEntitiesValuesInteger = false;

	TreeItem fComplexEntitiesValuesReal;
	private static final String  strComplexEntitiesValuesReal = "Real";
	public static final String idFgComplexEntitiesValuesReal  = PREFIX + "fgComplexEntitiesValuesReal";
	public static final String idBgComplexEntitiesValuesReal  = PREFIX + "bgComplexEntitiesValuesReal";
	public static final String idTrComplexEntitiesValuesReal  = PREFIX + "trComplexEntitiesValuesReal";
	public static final String idBComplexEntitiesValuesReal  = PREFIX + "bComplexEntitiesValuesReal";
	public static final String idIComplexEntitiesValuesReal  = PREFIX + "iComplexEntitiesValuesReal";
	public static final String idSComplexEntitiesValuesReal  = PREFIX + "sComplexEntitiesValuesReal";
	public static final String idUComplexEntitiesValuesReal  = PREFIX + "uComplexEntitiesValuesReal";
	private static final String fgComplexEntitiesValuesReal = "32,96,160";
	private static final String bgComplexEntitiesValuesReal = "255,255,255";
	private static final boolean bComplexEntitiesValuesReal = true;
	private static final boolean iComplexEntitiesValuesReal = false;
	private static final boolean sComplexEntitiesValuesReal = false;
	private static final boolean uComplexEntitiesValuesReal = false;

	TreeItem fComplexEntitiesValuesLogical;
	private static final String strComplexEntitiesValuesLogical = "Logical";
	public static final String idFgComplexEntitiesValuesLogical  = PREFIX + "fgComplexEntitiesValuesLogical";
	public static final String idBgComplexEntitiesValuesLogical  = PREFIX + "bgComplexEntitiesValuesLogical";
	public static final String idTrComplexEntitiesValuesLogical  = PREFIX + "trComplexEntitiesValuesLogical";
	public static final String idBComplexEntitiesValuesLogical  = PREFIX + "bComplexEntitiesValuesLogical";
	public static final String idIComplexEntitiesValuesLogical  = PREFIX + "iComplexEntitiesValuesLogical";
	public static final String idSComplexEntitiesValuesLogical  = PREFIX + "sComplexEntitiesValuesLogical";
	public static final String idUComplexEntitiesValuesLogical  = PREFIX + "uComplexEntitiesValuesLogical";
	private static final String fgComplexEntitiesValuesLogical = "32,96,160";
	private static final String bgComplexEntitiesValuesLogical = "255,255,255";
	private static final boolean bComplexEntitiesValuesLogical = true;
	private static final boolean iComplexEntitiesValuesLogical = false;
	private static final boolean sComplexEntitiesValuesLogical = false;
	private static final boolean uComplexEntitiesValuesLogical = false;

	TreeItem fComplexEntitiesValuesEnumeration;
	private static final String strComplexEntitiesValuesEnumeration = "Enumeration";
	public static final String idFgComplexEntitiesValuesEnumeration  = PREFIX + "fgComplexEntitiesValuesEnumeration";
	public static final String idBgComplexEntitiesValuesEnumeration  = PREFIX + "bgComplexEntitiesValuesEnumeration";
	public static final String idTrComplexEntitiesValuesEnumeration  = PREFIX + "trComplexEntitiesValuesEnumeration";
	public static final String idBComplexEntitiesValuesEnumeration  = PREFIX + "bComplexEntitiesValuesEnumeration";
	public static final String idIComplexEntitiesValuesEnumeration  = PREFIX + "iComplexEntitiesValuesEnumeration";
	public static final String idSComplexEntitiesValuesEnumeration  = PREFIX + "sComplexEntitiesValuesEnumeration";
	public static final String idUComplexEntitiesValuesEnumeration  = PREFIX + "uComplexEntitiesValuesEnumeration";
	private static final String fgComplexEntitiesValuesEnumeration = "32,96,160";
	private static final String bgComplexEntitiesValuesEnumeration = "255,255,255";
	private static final boolean bComplexEntitiesValuesEnumeration = true;
	private static final boolean iComplexEntitiesValuesEnumeration = false;
	private static final boolean sComplexEntitiesValuesEnumeration = false;
	private static final boolean uComplexEntitiesValuesEnumeration = false;

	TreeItem fComplexEntitiesValuesString;
	private static final String strComplexEntitiesValuesString = "String";
	public static final String idFgComplexEntitiesValuesString  = PREFIX + "fgComplexEntitiesValuesString";
	public static final String idBgComplexEntitiesValuesString  = PREFIX + "bgComplexEntitiesValuesString";
	public static final String idTrComplexEntitiesValuesString  = PREFIX + "trComplexEntitiesValuesString";
	public static final String idBComplexEntitiesValuesString  = PREFIX + "bComplexEntitiesValuesString";
	public static final String idIComplexEntitiesValuesString  = PREFIX + "iComplexEntitiesValuesString";
	public static final String idSComplexEntitiesValuesString  = PREFIX + "sComplexEntitiesValuesString";
	public static final String idUComplexEntitiesValuesString  = PREFIX + "uComplexEntitiesValuesString";
	private static final String fgComplexEntitiesValuesString = "96,160,160";
	private static final String bgComplexEntitiesValuesString = "255,255,255";
	private static final boolean bComplexEntitiesValuesString = true;
	private static final boolean iComplexEntitiesValuesString = false;
	private static final boolean sComplexEntitiesValuesString = false;
	private static final boolean uComplexEntitiesValuesString = false;

	TreeItem fComplexEntitiesValuesBinary;
	private static final String strComplexEntitiesValuesBinary = "Binary";
	public static final String idFgComplexEntitiesValuesBinary  = PREFIX + "fgComplexEntitiesValuesBinary";
	public static final String idBgComplexEntitiesValuesBinary  = PREFIX + "bgComplexEntitiesValuesBinary";
	public static final String idTrComplexEntitiesValuesBinary  = PREFIX + "trComplexEntitiesValuesBinary";
	public static final String idBComplexEntitiesValuesBinary  = PREFIX + "bComplexEntitiesValuesBinary";
	public static final String idIComplexEntitiesValuesBinary  = PREFIX + "iComplexEntitiesValuesBinary";
	public static final String idSComplexEntitiesValuesBinary  = PREFIX + "sComplexEntitiesValuesBinary";
	public static final String idUComplexEntitiesValuesBinary  = PREFIX + "uComplexEntitiesValuesBinary";
	private static final String fgComplexEntitiesValuesBinary = "32,96,160";
	private static final String bgComplexEntitiesValuesBinary = "255,255,255";
	private static final boolean bComplexEntitiesValuesBinary = true;
	private static final boolean iComplexEntitiesValuesBinary = false;
	private static final boolean sComplexEntitiesValuesBinary = false;
	private static final boolean uComplexEntitiesValuesBinary = false;

	TreeItem fComplexEntitiesValuesUndefined;
	private static final String strComplexEntitiesValuesUndefined = "Undefined";
	public static final String idFgComplexEntitiesValuesUndefined  = PREFIX + "fgComplexEntitiesValuesUndefined";
	public static final String idBgComplexEntitiesValuesUndefined  = PREFIX + "bgComplexEntitiesValuesUndefined";
	public static final String idTrComplexEntitiesValuesUndefined  = PREFIX + "trComplexEntitiesValuesUndefined";
	public static final String idBComplexEntitiesValuesUndefined  = PREFIX + "bComplexEntitiesValuesUndefined";
	public static final String idIComplexEntitiesValuesUndefined  = PREFIX + "iComplexEntitiesValuesUndefined";
	public static final String idSComplexEntitiesValuesUndefined  = PREFIX + "sComplexEntitiesValuesUndefined";
	public static final String idUComplexEntitiesValuesUndefined  = PREFIX + "uComplexEntitiesValuesUndefined";
	private static final String fgComplexEntitiesValuesUndefined = "32,96,160";
	private static final String bgComplexEntitiesValuesUndefined = "255,255,255";
	private static final boolean bComplexEntitiesValuesUndefined = true;
	private static final boolean iComplexEntitiesValuesUndefined = false;
	private static final boolean sComplexEntitiesValuesUndefined = false;
	private static final boolean uComplexEntitiesValuesUndefined = false;

	TreeItem fComplexEntitiesValuesRedefined;
	private static final String strComplexEntitiesValuesRedefined = "Redefined";
	public static final String idFgComplexEntitiesValuesRedefined  = PREFIX + "fgComplexEntitiesValuesRedefined";
	public static final String idBgComplexEntitiesValuesRedefined  = PREFIX + "bgComplexEntitiesValuesRedefined";
	public static final String idTrComplexEntitiesValuesRedefined  = PREFIX + "trComplexEntitiesValuesRedefined";
	public static final String idBComplexEntitiesValuesRedefined  = PREFIX + "bComplexEntitiesValuesRedefined";
	public static final String idIComplexEntitiesValuesRedefined  = PREFIX + "iComplexEntitiesValuesRedefined";
	public static final String idSComplexEntitiesValuesRedefined  = PREFIX + "sComplexEntitiesValuesRedefined";
	public static final String idUComplexEntitiesValuesRedefined  = PREFIX + "uComplexEntitiesValuesRedefined";
	private static final String fgComplexEntitiesValuesRedefined = "32,96,160";
	private static final String bgComplexEntitiesValuesRedefined = "255,255,255";
	private static final boolean bComplexEntitiesValuesRedefined = true;
	private static final boolean iComplexEntitiesValuesRedefined = false;
	private static final boolean sComplexEntitiesValuesRedefined = false;
	private static final boolean uComplexEntitiesValuesRedefined = false;

	TreeItem fComplexEntitiesDelimeter;
	private static final String strComplexEntitiesDelimeter = "Delimeter";
	public static final String idFgComplexEntitiesDelimeter  = PREFIX + "fgComplexEntitiesDelimeter";
	public static final String idBgComplexEntitiesDelimeter  = PREFIX + "bgComplexEntitiesDelimeter";
	public static final String idTrComplexEntitiesDelimeter  = PREFIX + "trComplexEntitiesDelimeter";
	public static final String idBComplexEntitiesDelimeter  = PREFIX + "bComplexEntitiesDelimeter";
	public static final String idIComplexEntitiesDelimeter  = PREFIX + "iComplexEntitiesDelimeter";
	public static final String idSComplexEntitiesDelimeter  = PREFIX + "sComplexEntitiesDelimeter";
	public static final String idUComplexEntitiesDelimeter  = PREFIX + "uComplexEntitiesDelimeter";
	private static final String fgComplexEntitiesDelimeter = "32,32,32";
	private static final String bgComplexEntitiesDelimeter = "255,255,255";
	private static final boolean bComplexEntitiesDelimeter = true;
	private static final boolean iComplexEntitiesDelimeter = false;
	private static final boolean sComplexEntitiesDelimeter = false;
	private static final boolean uComplexEntitiesDelimeter = false;


	TreeItem fComment;
	private static final String strComment = "Comment";
	public static final String idFgComment  = PREFIX + "fgComment";
	public static final String idBgComment  = PREFIX + "bgComment";
	public static final String idTrComment  = PREFIX + "trComment";
	public static final String idBComment  = PREFIX + "bComment";
	public static final String idIComment  = PREFIX + "iComment";
	public static final String idSComment  = PREFIX + "sComment";
	public static final String idUComment  = PREFIX + "uComment";
	private static final String fgComment = "128,128,128";
	private static final String bgComment = "255,255,255";
	private static final boolean bComment = false;
	private static final boolean iComment = true;
	private static final boolean sComment = false;
	private static final boolean uComment = false;

	TreeItem fError; // let's have the same Error both in simple and in complex entities
	private static final String strError = "Error";
	public static final String idFgError  = PREFIX + "fgError";
	public static final String idBgError  = PREFIX + "bgError";
	public static final String idTrError  = PREFIX + "trError";
	public static final String idBError  = PREFIX + "bError";
	public static final String idIError  = PREFIX + "iError";
	public static final String idSError  = PREFIX + "sError";
	public static final String idUError  = PREFIX + "uError";
//	private static final String fgError = "255,0,0";
//	private static final String bgError = "255,255,255";
	private static final String fgError = "0,0,0";
	private static final String bgError = "255,0,0";
	private static final boolean bError = true;
	private static final boolean iError = false;
	private static final boolean sError = false;
	private static final boolean uError = false;

	private static final String strIds[] = new String [] {
		strIso,
		strSection,
		strHeaderKeyword,

		strSimpleEntitiesInstance,
		strSimpleEntitiesEntity,
		strSimpleEntitiesType,
		strSimpleEntitiesValuesInteger,
		strSimpleEntitiesValuesReal,
		strSimpleEntitiesValuesLogical,
		strSimpleEntitiesValuesEnumeration,
		strSimpleEntitiesValuesString,
		strSimpleEntitiesValuesBinary,
		strSimpleEntitiesValuesUndefined,
		strSimpleEntitiesValuesRedefined,
		strSimpleEntitiesDelimeter,

		strComplexEntitiesInstance,
		strComplexEntitiesEntity,
		strComplexEntitiesType,
		strComplexEntitiesValuesInteger,
		strComplexEntitiesValuesReal,
		strComplexEntitiesValuesLogical,
		strComplexEntitiesValuesEnumeration,
		strComplexEntitiesValuesString,
		strComplexEntitiesValuesBinary,
		strComplexEntitiesValuesUndefined,
		strComplexEntitiesValuesRedefined,
		strComplexEntitiesDelimeter,
	
		strComment,
		strError

	
	};
	
	private static final String fgIds [] = new String []{

		idFgIso,
		idFgSection,
		idFgHeaderKeyword,

		idFgSimpleEntitiesInstance,
		idFgSimpleEntitiesEntity,
		idFgSimpleEntitiesType,
		idFgSimpleEntitiesValuesInteger,
		idFgSimpleEntitiesValuesReal,
		idFgSimpleEntitiesValuesLogical,
		idFgSimpleEntitiesValuesEnumeration,
		idFgSimpleEntitiesValuesString,
		idFgSimpleEntitiesValuesBinary,
		idFgSimpleEntitiesValuesUndefined,
		idFgSimpleEntitiesValuesRedefined,
		idFgSimpleEntitiesDelimeter,

		idFgComplexEntitiesInstance,
		idFgComplexEntitiesEntity,
		idFgComplexEntitiesType,
		idFgComplexEntitiesValuesInteger,
		idFgComplexEntitiesValuesReal,
		idFgComplexEntitiesValuesLogical,
		idFgComplexEntitiesValuesEnumeration,
		idFgComplexEntitiesValuesString,
		idFgComplexEntitiesValuesBinary,
		idFgComplexEntitiesValuesUndefined,
		idFgComplexEntitiesValuesRedefined,
		idFgComplexEntitiesDelimeter,
	
		idFgComment,
		idFgError
		
	};

	private String currentFgValues [];
	private static final String fgValues [] = new String []{

		fgIso,
		fgSection,
		fgHeaderKeyword,

		fgSimpleEntitiesInstance,
		fgSimpleEntitiesEntity,
		fgSimpleEntitiesType,
		fgSimpleEntitiesValuesInteger,
		fgSimpleEntitiesValuesReal,
		fgSimpleEntitiesValuesLogical,
		fgSimpleEntitiesValuesEnumeration,
		fgSimpleEntitiesValuesString,
		fgSimpleEntitiesValuesBinary,
		fgSimpleEntitiesValuesUndefined,
		fgSimpleEntitiesValuesRedefined,
		fgSimpleEntitiesDelimeter,

		fgComplexEntitiesInstance,
		fgComplexEntitiesEntity,
		fgComplexEntitiesType,
		fgComplexEntitiesValuesInteger,
		fgComplexEntitiesValuesReal,
		fgComplexEntitiesValuesLogical,
		fgComplexEntitiesValuesEnumeration,
		fgComplexEntitiesValuesString,
		fgComplexEntitiesValuesBinary,
		fgComplexEntitiesValuesUndefined,
		fgComplexEntitiesValuesRedefined,
		fgComplexEntitiesDelimeter,
	
		fgComment,
		fgError
		
	};

	private static final String bgIds [] = new String []{

		idBgIso,
		idBgSection,
		idBgHeaderKeyword,

		idBgSimpleEntitiesInstance,
		idBgSimpleEntitiesEntity,
		idBgSimpleEntitiesType,
		idBgSimpleEntitiesValuesInteger,
		idBgSimpleEntitiesValuesReal,
		idBgSimpleEntitiesValuesLogical,
		idBgSimpleEntitiesValuesEnumeration,
		idBgSimpleEntitiesValuesString,
		idBgSimpleEntitiesValuesBinary,
		idBgSimpleEntitiesValuesUndefined,
		idBgSimpleEntitiesValuesRedefined,
		idBgSimpleEntitiesDelimeter,

		idBgComplexEntitiesInstance,
		idBgComplexEntitiesEntity,
		idBgComplexEntitiesType,
		idBgComplexEntitiesValuesInteger,
		idBgComplexEntitiesValuesReal,
		idBgComplexEntitiesValuesLogical,
		idBgComplexEntitiesValuesEnumeration,
		idBgComplexEntitiesValuesString,
		idBgComplexEntitiesValuesBinary,
		idBgComplexEntitiesValuesUndefined,
		idBgComplexEntitiesValuesRedefined,
		idBgComplexEntitiesDelimeter,
	
		idBgComment,
		idBgError
		
	};

	private String currentBgValues [];
	private static final String bgValues [] = new String []{

		bgIso,
		bgSection,
		bgHeaderKeyword,

		bgSimpleEntitiesInstance,
		bgSimpleEntitiesEntity,
		bgSimpleEntitiesType,
		bgSimpleEntitiesValuesInteger,
		bgSimpleEntitiesValuesReal,
		bgSimpleEntitiesValuesLogical,
		bgSimpleEntitiesValuesEnumeration,
		bgSimpleEntitiesValuesString,
		bgSimpleEntitiesValuesBinary,
		bgSimpleEntitiesValuesUndefined,
		bgSimpleEntitiesValuesRedefined,
		bgSimpleEntitiesDelimeter,

		bgComplexEntitiesInstance,
		bgComplexEntitiesEntity,
		bgComplexEntitiesType,
		bgComplexEntitiesValuesInteger,
		bgComplexEntitiesValuesReal,
		bgComplexEntitiesValuesLogical,
		bgComplexEntitiesValuesEnumeration,
		bgComplexEntitiesValuesString,
		bgComplexEntitiesValuesBinary,
		bgComplexEntitiesValuesUndefined,
		bgComplexEntitiesValuesRedefined,
		bgComplexEntitiesDelimeter,
	
		bgComment,
		bgError
		
	};

	private static final String trIds [] = {

			idTrIso,
			idTrSection,
			idTrHeaderKeyword,

			idTrSimpleEntitiesInstance,
			idTrSimpleEntitiesEntity,
			idTrSimpleEntitiesType,
			idTrSimpleEntitiesValuesInteger,
			idTrSimpleEntitiesValuesReal,
			idTrSimpleEntitiesValuesLogical,
			idTrSimpleEntitiesValuesEnumeration,
			idTrSimpleEntitiesValuesString,
			idTrSimpleEntitiesValuesBinary,
			idTrSimpleEntitiesValuesUndefined,
			idTrSimpleEntitiesValuesRedefined,
			idTrSimpleEntitiesDelimeter,

			idTrComplexEntitiesInstance,
			idTrComplexEntitiesEntity,
			idTrComplexEntitiesType,
			idTrComplexEntitiesValuesInteger,
			idTrComplexEntitiesValuesReal,
			idTrComplexEntitiesValuesLogical,
			idTrComplexEntitiesValuesEnumeration,
			idTrComplexEntitiesValuesString,
			idTrComplexEntitiesValuesBinary,
			idTrComplexEntitiesValuesUndefined,
			idTrComplexEntitiesValuesRedefined,
			idTrComplexEntitiesDelimeter,
		
			idTrComment,
			idTrError
			
		};

	private boolean currentTrValues [];
	private static final boolean trValues [] = {
			true,
			true,
			true,

			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,

			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,

			true,
			true
	};

	private static final String bIds [] = new String []{

		idBIso,
		idBSection,
		idBHeaderKeyword,

		idBSimpleEntitiesInstance,
		idBSimpleEntitiesEntity,
		idBSimpleEntitiesType,
		idBSimpleEntitiesValuesInteger,
		idBSimpleEntitiesValuesReal,
		idBSimpleEntitiesValuesLogical,
		idBSimpleEntitiesValuesEnumeration,
		idBSimpleEntitiesValuesString,
		idBSimpleEntitiesValuesBinary,
		idBSimpleEntitiesValuesUndefined,
		idBSimpleEntitiesValuesRedefined,
		idBSimpleEntitiesDelimeter,

		idBComplexEntitiesInstance,
		idBComplexEntitiesEntity,
		idBComplexEntitiesType,
		idBComplexEntitiesValuesInteger,
		idBComplexEntitiesValuesReal,
		idBComplexEntitiesValuesLogical,
		idBComplexEntitiesValuesEnumeration,
		idBComplexEntitiesValuesString,
		idBComplexEntitiesValuesBinary,
		idBComplexEntitiesValuesUndefined,
		idBComplexEntitiesValuesRedefined,
		idBComplexEntitiesDelimeter,
	
		idBComment,
		idBError
		
	};


	private boolean currentBValues [];
	private static final boolean bValues [] = new boolean []{

		bIso,
		bSection,
		bHeaderKeyword,

		bSimpleEntitiesInstance,
		bSimpleEntitiesEntity,
		bSimpleEntitiesType,
		bSimpleEntitiesValuesInteger,
		bSimpleEntitiesValuesReal,
		bSimpleEntitiesValuesLogical,
		bSimpleEntitiesValuesEnumeration,
		bSimpleEntitiesValuesString,
		bSimpleEntitiesValuesBinary,
		bSimpleEntitiesValuesUndefined,
		bSimpleEntitiesValuesRedefined,
		bSimpleEntitiesDelimeter,

		bComplexEntitiesInstance,
		bComplexEntitiesEntity,
		bComplexEntitiesType,
		bComplexEntitiesValuesInteger,
		bComplexEntitiesValuesReal,
		bComplexEntitiesValuesLogical,
		bComplexEntitiesValuesEnumeration,
		bComplexEntitiesValuesString,
		bComplexEntitiesValuesBinary,
		bComplexEntitiesValuesUndefined,
		bComplexEntitiesValuesRedefined,
		bComplexEntitiesDelimeter,
	
		bComment,
		bError
		
	};


	private static final String iIds [] = new String []{

		idIIso,
		idISection,
		idIHeaderKeyword,

		idISimpleEntitiesInstance,
		idISimpleEntitiesEntity,
		idISimpleEntitiesType,
		idISimpleEntitiesValuesInteger,
		idISimpleEntitiesValuesReal,
		idISimpleEntitiesValuesLogical,
		idISimpleEntitiesValuesEnumeration,
		idISimpleEntitiesValuesString,
		idISimpleEntitiesValuesBinary,
		idISimpleEntitiesValuesUndefined,
		idISimpleEntitiesValuesRedefined,
		idISimpleEntitiesDelimeter,

		idIComplexEntitiesInstance,
		idIComplexEntitiesEntity,
		idIComplexEntitiesType,
		idIComplexEntitiesValuesInteger,
		idIComplexEntitiesValuesReal,
		idIComplexEntitiesValuesLogical,
		idIComplexEntitiesValuesEnumeration,
		idIComplexEntitiesValuesString,
		idIComplexEntitiesValuesBinary,
		idIComplexEntitiesValuesUndefined,
		idIComplexEntitiesValuesRedefined,
		idIComplexEntitiesDelimeter,
	
		idIComment,
		idIError
		
	};

	private boolean currentIValues [];
	private static final boolean iValues [] = new boolean []{

		iIso,
		iSection,
		iHeaderKeyword,

		iSimpleEntitiesInstance,
		iSimpleEntitiesEntity,
		iSimpleEntitiesType,
		iSimpleEntitiesValuesInteger,
		iSimpleEntitiesValuesReal,
		iSimpleEntitiesValuesLogical,
		iSimpleEntitiesValuesEnumeration,
		iSimpleEntitiesValuesString,
		iSimpleEntitiesValuesBinary,
		iSimpleEntitiesValuesUndefined,
		iSimpleEntitiesValuesRedefined,
		iSimpleEntitiesDelimeter,

		iComplexEntitiesInstance,
		iComplexEntitiesEntity,
		iComplexEntitiesType,
		iComplexEntitiesValuesInteger,
		iComplexEntitiesValuesReal,
		iComplexEntitiesValuesLogical,
		iComplexEntitiesValuesEnumeration,
		iComplexEntitiesValuesString,
		iComplexEntitiesValuesBinary,
		iComplexEntitiesValuesUndefined,
		iComplexEntitiesValuesRedefined,
		iComplexEntitiesDelimeter,
	
		iComment,
		iError
		
	};


	private static final String sIds [] = new String []{

		idSIso,
		idSSection,
		idSHeaderKeyword,

		idSSimpleEntitiesInstance,
		idSSimpleEntitiesEntity,
		idSSimpleEntitiesType,
		idSSimpleEntitiesValuesInteger,
		idSSimpleEntitiesValuesReal,
		idSSimpleEntitiesValuesLogical,
		idSSimpleEntitiesValuesEnumeration,
		idSSimpleEntitiesValuesString,
		idSSimpleEntitiesValuesBinary,
		idSSimpleEntitiesValuesUndefined,
		idSSimpleEntitiesValuesRedefined,
		idSSimpleEntitiesDelimeter,

		idSComplexEntitiesInstance,
		idSComplexEntitiesEntity,
		idSComplexEntitiesType,
		idSComplexEntitiesValuesInteger,
		idSComplexEntitiesValuesReal,
		idSComplexEntitiesValuesLogical,
		idSComplexEntitiesValuesEnumeration,
		idSComplexEntitiesValuesString,
		idSComplexEntitiesValuesBinary,
		idSComplexEntitiesValuesUndefined,
		idSComplexEntitiesValuesRedefined,
		idSComplexEntitiesDelimeter,
	
		idSComment,
		idSError
		
	};

	private boolean currentSValues [];
	private static final boolean sValues [] = new boolean []{

		sIso,
		sSection,
		sHeaderKeyword,

		sSimpleEntitiesInstance,
		sSimpleEntitiesEntity,
		sSimpleEntitiesType,
		sSimpleEntitiesValuesInteger,
		sSimpleEntitiesValuesReal,
		sSimpleEntitiesValuesLogical,
		sSimpleEntitiesValuesEnumeration,
		sSimpleEntitiesValuesString,
		sSimpleEntitiesValuesBinary,
		sSimpleEntitiesValuesUndefined,
		sSimpleEntitiesValuesRedefined,
		sSimpleEntitiesDelimeter,

		sComplexEntitiesInstance,
		sComplexEntitiesEntity,
		sComplexEntitiesType,
		sComplexEntitiesValuesInteger,
		sComplexEntitiesValuesReal,
		sComplexEntitiesValuesLogical,
		sComplexEntitiesValuesEnumeration,
		sComplexEntitiesValuesString,
		sComplexEntitiesValuesBinary,
		sComplexEntitiesValuesUndefined,
		sComplexEntitiesValuesRedefined,
		sComplexEntitiesDelimeter,
	
		sComment,
		sError
		
	};


	private static final String uIds [] = new String []{

		idUIso,
		idUSection,
		idUHeaderKeyword,

		idUSimpleEntitiesInstance,
		idUSimpleEntitiesEntity,
		idUSimpleEntitiesType,
		idUSimpleEntitiesValuesInteger,
		idUSimpleEntitiesValuesReal,
		idUSimpleEntitiesValuesLogical,
		idUSimpleEntitiesValuesEnumeration,
		idUSimpleEntitiesValuesString,
		idUSimpleEntitiesValuesBinary,
		idUSimpleEntitiesValuesUndefined,
		idUSimpleEntitiesValuesRedefined,
		idUSimpleEntitiesDelimeter,

		idUComplexEntitiesInstance,
		idUComplexEntitiesEntity,
		idUComplexEntitiesType,
		idUComplexEntitiesValuesInteger,
		idUComplexEntitiesValuesReal,
		idUComplexEntitiesValuesLogical,
		idUComplexEntitiesValuesEnumeration,
		idUComplexEntitiesValuesString,
		idUComplexEntitiesValuesBinary,
		idUComplexEntitiesValuesUndefined,
		idUComplexEntitiesValuesRedefined,
		idUComplexEntitiesDelimeter,
	
		idUComment,
		idUError
		
	};

	private boolean currentUValues [];
	private static final boolean uValues [] = new boolean []{

		uIso,
		uSection,
		uHeaderKeyword,

		uSimpleEntitiesInstance,
		uSimpleEntitiesEntity,
		uSimpleEntitiesType,
		uSimpleEntitiesValuesInteger,
		uSimpleEntitiesValuesReal,
		uSimpleEntitiesValuesLogical,
		uSimpleEntitiesValuesEnumeration,
		uSimpleEntitiesValuesString,
		uSimpleEntitiesValuesBinary,
		uSimpleEntitiesValuesUndefined,
		uSimpleEntitiesValuesRedefined,
		uSimpleEntitiesDelimeter,

		uComplexEntitiesInstance,
		uComplexEntitiesEntity,
		uComplexEntitiesType,
		uComplexEntitiesValuesInteger,
		uComplexEntitiesValuesReal,
		uComplexEntitiesValuesLogical,
		uComplexEntitiesValuesEnumeration,
		uComplexEntitiesValuesString,
		uComplexEntitiesValuesBinary,
		uComplexEntitiesValuesUndefined,
		uComplexEntitiesValuesRedefined,
		uComplexEntitiesDelimeter,
	
		uComment,
		uError
		
	};

	
	public P21EditorPreferences () {
		super();
		fP21EditorPreferences = this;
		setPreferenceStore(EditorsPlugin.getDefault().getPreferenceStore());
    setDescription("P21 editor preferences");
		initDefaults();
	}
	
	public void init(IWorkbench workbench) {
		fStore = getPreferenceStore();
	  //setDescription("P21 editor preferences");


		currentFgValues = new String[fgIds.length];
		currentBgValues = new String[bgIds.length];
		currentTrValues = new boolean[trIds.length];
		currentBValues = new boolean[bIds.length];
		currentIValues = new boolean[iIds.length];
		currentSValues = new boolean[sIds.length];
		currentUValues = new boolean[uIds.length];

		for (int i = 0; i < fgIds.length; i++) {
			currentFgValues[i] = fStore.getString(fgIds[i]);
			currentBgValues[i] = fStore.getString(bgIds[i]);
			currentTrValues[i] = fStore.getBoolean(trIds[i]);
			currentBValues[i] = fStore.getBoolean(bIds[i]);
			currentIValues[i] = fStore.getBoolean(iIds[i]);
			currentSValues[i] = fStore.getBoolean(sIds[i]);
			currentUValues[i] = fStore.getBoolean(uIds[i]);

//System.out.println(strIds[i] + " FG: " + currentFgValues[i]);		
//System.out.println(strIds[i] + " BG: " + currentBgValues[i]);		
//System.out.println(strIds[i] + "  B: " + currentBValues[i]);		
//System.out.println(strIds[i] + "  I: " + currentIValues[i]);		
//System.out.println(strIds[i] + "  S: " + currentSValues[i]);		
//System.out.println(strIds[i] + "  U: " + currentUValues[i]);		
		}
		
	}

	void initDefaults() {
	
		IPreferenceStore store = getPreferenceStore();


		
		
		for (int i = 0; i < fgIds.length; i++) {
			store.setDefault(fgIds[i], fgValues[i]);
			store.setDefault(bgIds[i], bgValues[i]);
			store.setDefault(trIds[i], trValues[i]);
			store.setDefault(bIds[i], bValues[i]);
			store.setDefault(iIds[i], iValues[i]);
			store.setDefault(sIds[i], sValues[i]);
			store.setDefault(uIds[i],uValues[i]);

			
		}

	
	}



	
	protected Control createContents(Composite parent) {


		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, IExpressCompilerHelpContextIds.P21_EDITOR_PREFERENCE_PAGE);


		Group colorGroup = new Group(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight= 5;
		layout.numColumns = 4;
		colorGroup.setLayout(layout);
		// which one?
		GridData gd= new GridData(GridData.FILL_HORIZONTAL);
		gd= new GridData(GridData.HORIZONTAL_ALIGN_FILL);

		gd.heightHint= convertHeightInCharsToPixels(26);
		colorGroup.setLayoutData(gd);
		//colorGroup.setFont(font);
		colorGroup.setText("Colors for syntax highlighting");
		gd.horizontalSpan= 4;
		colorGroup.setLayoutData(gd);		

		createColorPreferences(colorGroup);		

		return parent;
	}

		void createColorPreferences(Composite colorGroup) {

/*
	    fColorList = new List(colorGroup, SWT.SINGLE | SWT.V_SCROLL | SWT.BORDER);
//		GridData gd= new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL);
		GridData gd= new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd.heightHint= convertHeightInCharsToPixels(10);
		fColorList.setLayoutData(gd);
*/		
		
		// let's try one-by-one first
		/*
		for (int i= 0; i < fColorListObjects.length; i++)
			fColorList.add(fColorObjects[i]);
			
		}
		*/
	
		/*
	
		 new version, more categories:
		 
		 ISO
		 Section
		 Header
		 Data - Simple Entities
		 Data - Complex Entities
		 
		 Simple Entities
		 		Instance
		 		Entity
		 		Type
		 		Values
		 			Integer
		 			Real
		 			Logical
		 			Enumeration
		 			Binary
		 			String
		 			Undefined
		 			Redefined
		 		Delimeters	
		 			
		 Complex Entities

		Comment
		Error
	
	*/

		fSyntaxHighlightingTree = new Tree(colorGroup,SWT.SINGLE | SWT.BORDER);
//	 	fSyntaxHighlightingTree.setSize(convertWidthInCharsToPixels(20),convertHeightInCharsToPixels(22));
		// fSyntaxHighlightingTree.setLocation(5,5);
//		GridData gd= new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL);
		GridData gd= new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd.heightHint= convertHeightInCharsToPixels(24);
		fSyntaxHighlightingTree.setLayoutData(gd);


		int index = 0;
	 	
		fIso = new TreeItem(fSyntaxHighlightingTree, SWT.NONE);
		fIso.setText(strIso);
		fIso.setData(new Integer(index++));

		fSection = new TreeItem(fSyntaxHighlightingTree, SWT.NONE);
		fSection.setText(strSection);
		fSection.setData(new Integer(index++));

		fHeaderKeyword = new TreeItem(fSyntaxHighlightingTree, SWT.NONE);
		fHeaderKeyword.setText(strHeaderKeyword);
		fHeaderKeyword.setData(new Integer(index++));

		fSimpleEntities = new TreeItem(fSyntaxHighlightingTree, SWT.NONE);
		fSimpleEntities.setText(strSimpleEntities);
		fSimpleEntities.setData(new Integer(-2));

		fSimpleEntitiesInstance = new TreeItem(fSimpleEntities, SWT.NONE);
		fSimpleEntitiesInstance.setText(strSimpleEntitiesInstance);
		fSimpleEntitiesInstance.setData(new Integer(index++));
		
		fSimpleEntitiesEntity = new TreeItem(fSimpleEntities, SWT.NONE);
		fSimpleEntitiesEntity.setText(strSimpleEntitiesEntity);
		fSimpleEntitiesEntity.setData(new Integer(index++));

		fSimpleEntitiesType = new TreeItem(fSimpleEntities, SWT.NONE);   
		fSimpleEntitiesType.setText(strSimpleEntitiesType);
		fSimpleEntitiesType.setData(new Integer(index++));
		
		fSimpleEntitiesValues = new TreeItem(fSimpleEntities, SWT.NONE);   
		fSimpleEntitiesValues.setText(strSimpleEntitiesValues);
		fSimpleEntitiesValues.setData(new Integer(-1));

		fSimpleEntitiesValuesInteger = new TreeItem(fSimpleEntitiesValues, SWT.NONE);
		fSimpleEntitiesValuesInteger.setText(strSimpleEntitiesValuesInteger);
		fSimpleEntitiesValuesInteger.setData(new Integer(index++));

		fSimpleEntitiesValuesReal = new TreeItem(fSimpleEntitiesValues, SWT.NONE);
		fSimpleEntitiesValuesReal.setText(strSimpleEntitiesValuesReal);
		fSimpleEntitiesValuesReal.setData(new Integer(index++));

		fSimpleEntitiesValuesLogical = new TreeItem(fSimpleEntitiesValues, SWT.NONE);
		fSimpleEntitiesValuesLogical.setText(strSimpleEntitiesValuesLogical);
		fSimpleEntitiesValuesLogical.setData(new Integer(index++));

		fSimpleEntitiesValuesEnumeration = new TreeItem(fSimpleEntitiesValues, SWT.NONE);
		fSimpleEntitiesValuesEnumeration.setText(strSimpleEntitiesValuesEnumeration);
		fSimpleEntitiesValuesEnumeration.setData(new Integer(index++));

		fSimpleEntitiesValuesString = new TreeItem(fSimpleEntitiesValues, SWT.NONE);
		fSimpleEntitiesValuesString.setText(strSimpleEntitiesValuesString);
		fSimpleEntitiesValuesString.setData(new Integer(index++));

		fSimpleEntitiesValuesBinary = new TreeItem(fSimpleEntitiesValues, SWT.NONE);
		fSimpleEntitiesValuesBinary.setText(strSimpleEntitiesValuesBinary);
		fSimpleEntitiesValuesBinary.setData(new Integer(index++));

		fSimpleEntitiesValuesUndefined = new TreeItem(fSimpleEntitiesValues, SWT.NONE);
		fSimpleEntitiesValuesUndefined.setText(strSimpleEntitiesValuesUndefined);
		fSimpleEntitiesValuesUndefined.setData(new Integer(index++));

		fSimpleEntitiesValuesRedefined = new TreeItem(fSimpleEntitiesValues, SWT.NONE);
		fSimpleEntitiesValuesRedefined.setText(strSimpleEntitiesValuesRedefined);
		fSimpleEntitiesValuesRedefined.setData(new Integer(index++));

		fSimpleEntitiesDelimeter = new TreeItem(fSimpleEntities, SWT.NONE);
		fSimpleEntitiesDelimeter.setText(strSimpleEntitiesDelimeter);
		fSimpleEntitiesDelimeter.setData(new Integer(index++));

		fComplexEntities = new TreeItem(fSyntaxHighlightingTree, SWT.NONE);
		fComplexEntities.setText(strComplexEntities);
		fComplexEntities.setData(new Integer(-2));

		fComplexEntitiesInstance = new TreeItem(fComplexEntities, SWT.NONE);
		fComplexEntitiesInstance.setText(strComplexEntitiesInstance);
		fComplexEntitiesInstance.setData(new Integer(index++));

		fComplexEntitiesEntity = new TreeItem(fComplexEntities, SWT.NONE);
		fComplexEntitiesEntity.setText(strComplexEntitiesEntity);
		fComplexEntitiesEntity.setData(new Integer(index++));

		fComplexEntitiesType = new TreeItem(fComplexEntities, SWT.NONE);   
		fComplexEntitiesType.setText(strComplexEntitiesType);
		fComplexEntitiesType.setData(new Integer(index++));

		fComplexEntitiesValues = new TreeItem(fComplexEntities, SWT.NONE);   
		fComplexEntitiesValues.setText(strComplexEntitiesValues);
		fComplexEntitiesValues.setData(new Integer(-1));

		fComplexEntitiesValuesInteger = new TreeItem(fComplexEntitiesValues, SWT.NONE);
		fComplexEntitiesValuesInteger.setText(strComplexEntitiesValuesInteger);
		fComplexEntitiesValuesInteger.setData(new Integer(index++));

		fComplexEntitiesValuesReal = new TreeItem(fComplexEntitiesValues, SWT.NONE);
		fComplexEntitiesValuesReal.setText(strComplexEntitiesValuesReal);
		fComplexEntitiesValuesReal.setData(new Integer(index++));

		fComplexEntitiesValuesLogical = new TreeItem(fComplexEntitiesValues, SWT.NONE);
		fComplexEntitiesValuesLogical.setText(strComplexEntitiesValuesLogical);
		fComplexEntitiesValuesLogical.setData(new Integer(index++));

		fComplexEntitiesValuesEnumeration = new TreeItem(fComplexEntitiesValues, SWT.NONE);
		fComplexEntitiesValuesEnumeration.setText(strComplexEntitiesValuesEnumeration);
		fComplexEntitiesValuesEnumeration.setData(new Integer(index++));

		fComplexEntitiesValuesString = new TreeItem(fComplexEntitiesValues, SWT.NONE);
		fComplexEntitiesValuesString.setText(strComplexEntitiesValuesString);
		fComplexEntitiesValuesString.setData(new Integer(index++));

		fComplexEntitiesValuesBinary = new TreeItem(fComplexEntitiesValues, SWT.NONE);
		fComplexEntitiesValuesBinary.setText(strComplexEntitiesValuesBinary);
		fComplexEntitiesValuesBinary.setData(new Integer(index++));

		fComplexEntitiesValuesUndefined = new TreeItem(fComplexEntitiesValues, SWT.NONE);
		fComplexEntitiesValuesUndefined.setText(strComplexEntitiesValuesUndefined);
		fComplexEntitiesValuesUndefined.setData(new Integer(index++));

		fComplexEntitiesValuesRedefined = new TreeItem(fComplexEntitiesValues, SWT.NONE);
		fComplexEntitiesValuesRedefined.setText(strComplexEntitiesValuesRedefined);
		fComplexEntitiesValuesRedefined.setData(new Integer(index++));

		fComplexEntitiesDelimeter = new TreeItem(fComplexEntities, SWT.NONE);
		fComplexEntitiesDelimeter.setText(strComplexEntitiesDelimeter);
		fComplexEntitiesDelimeter.setData(new Integer(index++));


		fComment = new TreeItem(fSyntaxHighlightingTree, SWT.NONE);
		fComment.setText(strComment);
		fComment.setData(new Integer(index++));

		fError = new TreeItem(fSyntaxHighlightingTree, SWT.NONE);
		fError.setText(strError);
		fError.setData(new Integer(index++));






		fSyntaxHighlightingTree.getDisplay().asyncExec(new Runnable() {
			public void run() {
				TreeItem [] selected_items = new TreeItem [1];
				selected_items[0] = fIso; 
				if (fSyntaxHighlightingTree != null && !fSyntaxHighlightingTree.isDisposed()) {
					fSyntaxHighlightingTree.setSelection(selected_items);
					handleTreeSelection();
				}
			}
		});






//		fSyntaxHighlightingTree.setSelection(selected_items);
//		handleTreeSelection();
		
		fSyntaxHighlightingTree.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
//				TreeItem t = fSyntaxHighlightingTree.getSelection();
//System.out.println("selected: " + t.getText());

				handleTreeSelection();
				
//				TreeItem[] t = fSyntaxHighlightingTree.getSelection();
				
//				System.out.println("Selection: ");
//				for(int i = 0; i < t.length; i++) {
//					System.out.println("selected: " + ((Integer)t[i].getData()).intValue());
//					break; // we are using SWT.SINGLE, only one item is returned
//				}

//System.out.println();
			}
		});


		fSyntaxHighlightingTree.addTreeListener(new TreeListener() {
			public void treeCollapsed(TreeEvent e) {
//System.out.println("Tree collapsed.");
			}
			public void treeExpanded(TreeEvent e) {
//System.out.println("Tree expanded.");
			}
		});



		Composite stylesComposite= new Composite(colorGroup, SWT.NONE);
		GridLayout layout= new GridLayout();
		layout.marginHeight= 0;
		layout.marginWidth= 0;
		layout.numColumns= 2;
		stylesComposite.setLayout(layout);
		stylesComposite.setLayoutData(new GridData(GridData.FILL_BOTH));


		fColorEditor = new ColorSelector(stylesComposite);
		Button foregroundColorButton= fColorEditor.getButton();
		gd= new GridData(GridData.BEGINNING);
		gd.horizontalIndent= 10;
		foregroundColorButton.setLayoutData(gd);
		Label l= new Label(stylesComposite, SWT.TRAIL);
		l.setText("Foreground Color"); 
		gd= new GridData();
		l.setLayoutData(gd);

		fBgColorEditor = new ColorSelector(stylesComposite);
		Button backgroundColorButton= fBgColorEditor.getButton();
		gd= new GridData(GridData.BEGINNING);
		gd.horizontalIndent= 10;
		backgroundColorButton.setLayoutData(gd);
		Label l2 = new Label(stylesComposite, SWT.TRAIL);
		l2.setText("Background Color"); 
		gd= new GridData();
		l2.setLayoutData(gd);

		SelectionListener transparentCheckBoxSelectionListener = new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing
			}
			public void widgetSelected(SelectionEvent e) {


				if (fSyntaxHighlightingTree == null) {
					return;
				}

				TreeItem[] t = fSyntaxHighlightingTree.getSelection();
				// we are using SWT.SINGLE, so, only one is selected
				if (t.length != 1) {
					return;
				}
				int index = ((Integer)t[0].getData()).intValue();
				// if index > 0 - set that node to the color
				// if index == -1 - set all the direct children to the color
				// if index == -2 - set all the children recursively to the color
				if (index >= 0) {
					String key = trIds[index];
					getPreferenceStore().setValue(key, fTransparentCheckBox.getSelection());
				} else
				if (index == -1) {
					TreeItem [] items = t[0].getItems();
					for (int i = 0; i < items.length; i++) {
						TreeItem item = items[i];
						int index2 = ((Integer)item.getData()).intValue();
						if (index2 >= 0) {
							String key = trIds[index2];
							getPreferenceStore().setValue(key, fTransparentCheckBox.getSelection());
						}
					}
				} else 
				if (index == -2) {
					// the same as -1, but also recursion (one level only needed)
					TreeItem [] items = t[0].getItems();
					for (int i = 0; i < items.length; i++) {
						TreeItem item = items[i];
						int index2 = ((Integer)item.getData()).intValue();
						if (index2 >= 0) {
							String key = trIds[index2];
							getPreferenceStore().setValue(key, fTransparentCheckBox.getSelection());
						} else
						if (index2 == -1) {
							TreeItem [] items2 = item.getItems();
							for (int j = 0; j < items2.length; j++) {
								TreeItem item2 = items2[j];
								int index3 = ((Integer)item2.getData()).intValue();
								if (index3 >= 0) {
									String key = trIds[index3];
									getPreferenceStore().setValue(key, fTransparentCheckBox.getSelection());
								}
							}
							
						}
					}
				}
			}
		};

		fTransparentCheckBox= new Button(stylesComposite, SWT.CHECK);
		fTransparentCheckBox.setText("Transparent"); 
		gd= new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalIndent= 20;
		gd.horizontalSpan= 4;
		fTransparentCheckBox.setLayoutData(gd);
		fTransparentCheckBox.addSelectionListener(transparentCheckBoxSelectionListener);

		SelectionListener boldCheckBoxSelectionListener = new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing
			}
			public void widgetSelected(SelectionEvent e) {


				if (fSyntaxHighlightingTree == null) {
					return;
				}

				TreeItem[] t = fSyntaxHighlightingTree.getSelection();
				// we are using SWT.SINGLE, so, only one is selected
				if (t.length != 1) {
					return;
				}
				int index = ((Integer)t[0].getData()).intValue();
				// if index > 0 - set that node to the color
				// if index == -1 - set all the direct children to the color
				// if index == -2 - set all the children recursively to the color
				if (index >= 0) {
					String key = bIds[index];
					getPreferenceStore().setValue(key, fBoldCheckBox.getSelection());
				} else
				if (index == -1) {
					TreeItem [] items = t[0].getItems();
					for (int i = 0; i < items.length; i++) {
						TreeItem item = items[i];
						int index2 = ((Integer)item.getData()).intValue();
						if (index2 >= 0) {
							String key = bIds[index2];
							getPreferenceStore().setValue(key, fBoldCheckBox.getSelection());
						}
					}
				} else 
				if (index == -2) {
					// the same as -1, but also recursion (one level only needed)
					TreeItem [] items = t[0].getItems();
					for (int i = 0; i < items.length; i++) {
						TreeItem item = items[i];
						int index2 = ((Integer)item.getData()).intValue();
						if (index2 >= 0) {
							String key = bIds[index2];
							getPreferenceStore().setValue(key, fBoldCheckBox.getSelection());
						} else
						if (index2 == -1) {
							TreeItem [] items2 = item.getItems();
							for (int j = 0; j < items2.length; j++) {
								TreeItem item2 = items2[j];
								int index3 = ((Integer)item2.getData()).intValue();
								if (index3 >= 0) {
									String key = bIds[index3];
									getPreferenceStore().setValue(key, fBoldCheckBox.getSelection());
								}
							}
							
						}
					}
				}


//				int i= fColorList.getSelectionIndex();
//				if (i == -1) return;
//				String key= fColorObjectBoldIDs[i];
//				getPreferenceStore().setValue(key, fBoldCheckBox.getSelection());
			}
		};

		fBoldCheckBox= new Button(stylesComposite, SWT.CHECK);
		fBoldCheckBox.setText("Bold"); 
		gd= new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalIndent= 20;
		gd.horizontalSpan= 4;
		fBoldCheckBox.setLayoutData(gd);
		fBoldCheckBox.addSelectionListener(boldCheckBoxSelectionListener);


		SelectionListener italicCheckBoxSelectionListener = new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing
			}
			public void widgetSelected(SelectionEvent e) {


				if (fSyntaxHighlightingTree == null) {
					return;
				}

				TreeItem[] t = fSyntaxHighlightingTree.getSelection();
				// we are using SWT.SINGLE, so, only one is selected
				if (t.length != 1) {
					return;
				}
				int index = ((Integer)t[0].getData()).intValue();
				// if index > 0 - set that node to the color
				// if index == -1 - set all the direct children to the color
				// if index == -2 - set all the children recursively to the color
				if (index >= 0) {
					String key = iIds[index];
					getPreferenceStore().setValue(key, fItalicCheckBox.getSelection());
				} else
				if (index == -1) {
					TreeItem [] items = t[0].getItems();
					for (int i = 0; i < items.length; i++) {
						TreeItem item = items[i];
						int index2 = ((Integer)item.getData()).intValue();
						if (index2 >= 0) {
							String key = iIds[index2];
							getPreferenceStore().setValue(key, fItalicCheckBox.getSelection());
						}
					}
				} else 
				if (index == -2) {
					// the same as -1, but also recursion (one level only needed)
					TreeItem [] items = t[0].getItems();
					for (int i = 0; i < items.length; i++) {
						TreeItem item = items[i];
						int index2 = ((Integer)item.getData()).intValue();
						if (index2 >= 0) {
							String key = iIds[index2];
							getPreferenceStore().setValue(key, fItalicCheckBox.getSelection());
						} else
						if (index2 == -1) {
							TreeItem [] items2 = item.getItems();
							for (int j = 0; j < items2.length; j++) {
								TreeItem item2 = items2[j];
								int index3 = ((Integer)item2.getData()).intValue();
								if (index3 >= 0) {
									String key = iIds[index3];
									getPreferenceStore().setValue(key, fItalicCheckBox.getSelection());
								}
							}
							
						}
					}
				}



//				int i= fColorList.getSelectionIndex();
//				if (i == -1) return;
//				String key= fColorObjectItalicIDs[i];
//				getPreferenceStore().setValue(key, fItalicCheckBox.getSelection());
			}
		};
		
		fItalicCheckBox= new Button(stylesComposite, SWT.CHECK);
		fItalicCheckBox.setText("Italic"); 
		gd= new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalIndent= 20;
		gd.horizontalSpan= 4;
		fItalicCheckBox.setLayoutData(gd);
		fItalicCheckBox.addSelectionListener(italicCheckBoxSelectionListener);

		SelectionListener strikethroughCheckBoxSelectionListener = new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing
			}
			public void widgetSelected(SelectionEvent e) {


				if (fSyntaxHighlightingTree == null) {
					return;
				}

				TreeItem[] t = fSyntaxHighlightingTree.getSelection();
				// we are using SWT.SINGLE, so, only one is selected
				if (t.length != 1) {
					return;
				}
				int index = ((Integer)t[0].getData()).intValue();
				// if index > 0 - set that node to the color
				// if index == -1 - set all the direct children to the color
				// if index == -2 - set all the children recursively to the color
				if (index >= 0) {
					String key = sIds[index];
					getPreferenceStore().setValue(key, fStrikethroughCheckBox.getSelection());
				} else
				if (index == -1) {
					TreeItem [] items = t[0].getItems();
					for (int i = 0; i < items.length; i++) {
						TreeItem item = items[i];
						int index2 = ((Integer)item.getData()).intValue();
						if (index2 >= 0) {
							String key = sIds[index2];
							getPreferenceStore().setValue(key, fStrikethroughCheckBox.getSelection());
						}
					}
				} else 
				if (index == -2) {
					// the same as -1, but also recursion (one level only needed)
					TreeItem [] items = t[0].getItems();
					for (int i = 0; i < items.length; i++) {
						TreeItem item = items[i];
						int index2 = ((Integer)item.getData()).intValue();
						if (index2 >= 0) {
							String key = sIds[index2];
							getPreferenceStore().setValue(key, fStrikethroughCheckBox.getSelection());
						} else
						if (index2 == -1) {
							TreeItem [] items2 = item.getItems();
							for (int j = 0; j < items2.length; j++) {
								TreeItem item2 = items2[j];
								int index3 = ((Integer)item2.getData()).intValue();
								if (index3 >= 0) {
									String key = sIds[index3];
									getPreferenceStore().setValue(key, fStrikethroughCheckBox.getSelection());
								}
							}
							
						}
					}
				}


//				int i= fColorList.getSelectionIndex();
//				if (i == -1) return;
//				String key= fColorObjectStrikeIDs[i];
//				getPreferenceStore().setValue(key, fStrikethroughCheckBox.getSelection());
			}
		};
	
		fStrikethroughCheckBox= new Button(stylesComposite, SWT.CHECK);
		fStrikethroughCheckBox.setText("Strikethrough"); 
		gd= new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalIndent= 20;
		gd.horizontalSpan= 4;
		fStrikethroughCheckBox.setLayoutData(gd);
		fStrikethroughCheckBox.addSelectionListener(strikethroughCheckBoxSelectionListener);
	

		SelectionListener underlineCheckBoxSelectionListener = new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing
			}
			public void widgetSelected(SelectionEvent e) {

				if (fSyntaxHighlightingTree == null) {
					return;
				}

				TreeItem[] t = fSyntaxHighlightingTree.getSelection();
				// we are using SWT.SINGLE, so, only one is selected
				if (t.length != 1) {
					return;
				}
				int index = ((Integer)t[0].getData()).intValue();
				// if index > 0 - set that node to the color
				// if index == -1 - set all the direct children to the color
				// if index == -2 - set all the children recursively to the color
				if (index >= 0) {
					String key = uIds[index];
					getPreferenceStore().setValue(key, fUnderlineCheckBox.getSelection());
				} else
				if (index == -1) {
					TreeItem [] items = t[0].getItems();
					for (int i = 0; i < items.length; i++) {
						TreeItem item = items[i];
						int index2 = ((Integer)item.getData()).intValue();
						if (index2 >= 0) {
							String key = uIds[index2];
							getPreferenceStore().setValue(key, fUnderlineCheckBox.getSelection());
						}
					}
				} else 
				if (index == -2) {
					// the same as -1, but also recursion (one level only needed)
					TreeItem [] items = t[0].getItems();
					for (int i = 0; i < items.length; i++) {
						TreeItem item = items[i];
						int index2 = ((Integer)item.getData()).intValue();
						if (index2 >= 0) {
							String key = uIds[index2];
							getPreferenceStore().setValue(key, fUnderlineCheckBox.getSelection());
						} else
						if (index2 == -1) {
							TreeItem [] items2 = item.getItems();
							for (int j = 0; j < items2.length; j++) {
								TreeItem item2 = items2[j];
								int index3 = ((Integer)item2.getData()).intValue();
								if (index3 >= 0) {
									String key = uIds[index3];
									getPreferenceStore().setValue(key, fUnderlineCheckBox.getSelection());
								}
							}
							
						}
					}
				}



//				int i= fColorList.getSelectionIndex();
//				if (i == -1) return;
//				String key= fColorObjectUnderlineIDs[i];
//				getPreferenceStore().setValue(key, fUnderlineCheckBox.getSelection());
			}
		};

		fUnderlineCheckBox= new Button(stylesComposite, SWT.CHECK);
		fUnderlineCheckBox.setText("Underline"); 
		gd= new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalIndent= 20;
		gd.horizontalSpan= 4;
		fUnderlineCheckBox.setLayoutData(gd);
		fUnderlineCheckBox.addSelectionListener(underlineCheckBoxSelectionListener);


		foregroundColorButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
//System.out.println("foreground color button - DEFAULT, event: " + e );
				// do nothing
			}
			public void widgetSelected(SelectionEvent e) {

				if (fSyntaxHighlightingTree == null) {
					return;
				}

				TreeItem[] t = fSyntaxHighlightingTree.getSelection();
				// we are using SWT.SINGLE, so, only one is selected
				if (t.length != 1) {
					return;
				}
				int index = ((Integer)t[0].getData()).intValue();
				// if index > 0 - set that node to the color
				// if index == -1 - set all the direct children to the color
				// if index == -2 - set all the children recursively to the color
				if (index >= 0) {
					String key = fgIds[index];
					PreferenceConverter.setValue(getPreferenceStore(), key, fColorEditor.getColorValue());
				} else
				if (index == -1) {
					TreeItem [] items = t[0].getItems();
					for (int i = 0; i < items.length; i++) {
						TreeItem item = items[i];
						int index2 = ((Integer)item.getData()).intValue();
						if (index2 >= 0) {
							String key = fgIds[index2];
							PreferenceConverter.setValue(getPreferenceStore(), key, fColorEditor.getColorValue());
						}
					}
				} else 
				if (index == -2) {
					// the same as -1, but also recursion (one level only needed)
					TreeItem [] items = t[0].getItems();
					for (int i = 0; i < items.length; i++) {
						TreeItem item = items[i];
						int index2 = ((Integer)item.getData()).intValue();
						if (index2 >= 0) {
							String key = fgIds[index2];
							PreferenceConverter.setValue(getPreferenceStore(), key, fColorEditor.getColorValue());
						} else
						if (index2 == -1) {
							TreeItem [] items2 = item.getItems();
							for (int j = 0; j < items2.length; j++) {
								TreeItem item2 = items2[j];
								int index3 = ((Integer)item2.getData()).intValue();
								if (index3 >= 0) {
									String key = fgIds[index3];
									PreferenceConverter.setValue(getPreferenceStore(), key, fColorEditor.getColorValue());
								}
							}
							
						}
					}
				}

			}
		});
		backgroundColorButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
//System.out.println("foreground color button - DEFAULT, event: " + e );
				// do nothing
			}
			public void widgetSelected(SelectionEvent e) {

				if (fSyntaxHighlightingTree == null) {
					return;
				}

				TreeItem[] t = fSyntaxHighlightingTree.getSelection();
				// we are using SWT.SINGLE, so, only one is selected
				if (t.length != 1) {
					return;
				}
				int index = ((Integer)t[0].getData()).intValue();
				// if index > 0 - set that node to the color
				// if index == -1 - set all the direct children to the color
				// if index == -2 - set all the children recursively to the color
				if (index >= 0) {
					String key = bgIds[index];
					PreferenceConverter.setValue(getPreferenceStore(), key, fBgColorEditor.getColorValue());
				} else
				if (index == -1) {
					TreeItem [] items = t[0].getItems();
					for (int i = 0; i < items.length; i++) {
						TreeItem item = items[i];
						int index2 = ((Integer)item.getData()).intValue();
						if (index2 >= 0) {
							String key = bgIds[index2];
							PreferenceConverter.setValue(getPreferenceStore(), key, fBgColorEditor.getColorValue());
						}
					}
				} else 
				if (index == -2) {
					// the same as -1, but also recursion (one level only needed)
					TreeItem [] items = t[0].getItems();
					for (int i = 0; i < items.length; i++) {
						TreeItem item = items[i];
						int index2 = ((Integer)item.getData()).intValue();
						if (index2 >= 0) {
							String key = bgIds[index2];
							PreferenceConverter.setValue(getPreferenceStore(), key, fBgColorEditor.getColorValue());
						} else
						if (index2 == -1) {
							TreeItem [] items2 = item.getItems();
							for (int j = 0; j < items2.length; j++) {
								TreeItem item2 = items2[j];
								int index3 = ((Integer)item2.getData()).intValue();
								if (index3 >= 0) {
									String key = bgIds[index3];
									PreferenceConverter.setValue(getPreferenceStore(), key, fBgColorEditor.getColorValue());
								}
							}
							
						}
					}
				}




//				int i= fColorList.getSelectionIndex();
//System.out.println("foreground color button - i: " + i );
//				if (i == -1)
//					return;

//				String key= fColorObjectBackgroundIDs[i]; // [i][1];
//System.out.println("foreground color button - , key: " + key );
//				PreferenceConverter.setValue(getPreferenceStore(), key, fBgColorEditor.getColorValue());
			}
		});



/*
		fColorList.add("ISO");
		fColorList.add("File");
		fColorList.add("Section");
		fColorList.add(fSimpleEntities);
		fColorList.add(fComplexEntities);
		fColorList.add("Comment");
		fColorList.add("Error");
*/
	
/*
		fColorList.getDisplay().asyncExec(new Runnable() {
			public void run() {
				if (fColorList != null && !fColorList.isDisposed()) {
					fColorList.select(0);
					handleColorListSelection();
				}
			}
		});


		fSimpleEntities = new Tree(fColorList, SWT.SINGLE);
	 	fSimpleEntities.setText("Simple Entities");
	 	// fSimlpeEntities.setSize(150,150);
		// fSimlpeEntities.setLocation(5,5);
		fSimpleInstance = new TreeItem(fSimpleEntities, SWT.NONE);
		fSimpleInstance.setText("Instance");
		fSimpleInstance = new TreeItem(fSimpleEntities, SWT.NONE);
		fSimpleInstance.setText("Entity");
		fSimpleInstance = new TreeItem(fSimpleEntities, SWT.NONE);
		fSimpleInstance.setText("Type");

		fComplexEntities = new Tree(fColorList, SWT.SINGLE);
	 	fComplexEntities.setText("Complex Entities");
	 	// fSimlpeEntities.setSize(150,150);
		// fSimlpeEntities.setLocation(5,5);
		fComplexInstance = new TreeItem(fSimpleEntities, SWT.NONE);
		fComplexInstance.setText("Instance");
		fComplexInstance = new TreeItem(fSimpleEntities, SWT.NONE);
		fComplexInstance.setText("Entity");
		fComplexInstance = new TreeItem(fSimpleEntities, SWT.NONE);
		fComplexInstance.setText("Type");
*/

	}		



    private void handleTreeSelection() {	
			if (fSyntaxHighlightingTree == null) {
				return;
			}

			TreeItem[] t = fSyntaxHighlightingTree.getSelection();
			// we are using SWT.SINGLE, so, only one is selected
			if (t.length != 1) {
				return;
			}
			int index = ((Integer)t[0].getData()).intValue();
			if (index < 0) {
				// make everything disabled
				// we may want to do differently  on the Values node:
				// to set enabled, and perhaps set color of integer child
				// so that it is possible to set all the values to the same color
				// or perhaps do the same with simple/complex level as well,
				// yes, a good idea
				// so for both -2 and -1, take the color and other attributes of the first direct child

				/*
				fColorEditor.setEnabled(false);
				fBgColorEditor.setEnabled(false);
				fBoldCheckBox.setEnabled(false);
				fItalicCheckBox.setEnabled(false);
				fStrikethroughCheckBox.setEnabled(false);
				fUnderlineCheckBox.setEnabled(false);
				*/
				TreeItem t1 = t[0].getItem(0);
				if (t1 == null) {
					fColorEditor.setEnabled(false);
					fBgColorEditor.setEnabled(false);
					fTransparentCheckBox.setEnabled(false);
					fBoldCheckBox.setEnabled(false);
					fItalicCheckBox.setEnabled(false);
					fStrikethroughCheckBox.setEnabled(false);
					fUnderlineCheckBox.setEnabled(false);
				    return;			
				} else {

					int index2 = ((Integer)t1.getData()).intValue();
					if (index2 < 0) {
						fColorEditor.setEnabled(false);
						fBgColorEditor.setEnabled(false);
						fTransparentCheckBox.setEnabled(false);
						fBoldCheckBox.setEnabled(false);
						fItalicCheckBox.setEnabled(false);
						fStrikethroughCheckBox.setEnabled(false);
						fUnderlineCheckBox.setEnabled(false);
						return;	
					}
					
					fColorEditor.setEnabled(true);
					fBgColorEditor.setEnabled(true);
					fTransparentCheckBox.setEnabled(true);
					fBoldCheckBox.setEnabled(true);
					fItalicCheckBox.setEnabled(true);
					fStrikethroughCheckBox.setEnabled(true);
					fUnderlineCheckBox.setEnabled(true);
					
					String key = fgIds[index2]; 
					RGB rgb = PreferenceConverter.getColor(getPreferenceStore(), key);
					fColorEditor.setEnabled(true);
					fColorEditor.setColorValue(rgb);		

					key = bgIds[index2];
					rgb = PreferenceConverter.getColor(getPreferenceStore(), key);
					fBgColorEditor.setEnabled(true);
					fBgColorEditor.setColorValue(rgb);		
					
					fTransparentCheckBox.setSelection(getPreferenceStore().getBoolean(trIds[index2]));
					fBoldCheckBox.setSelection(getPreferenceStore().getBoolean(bIds[index2]));
					fItalicCheckBox.setSelection(getPreferenceStore().getBoolean(iIds[index2]));
					fStrikethroughCheckBox.setSelection(getPreferenceStore().getBoolean(sIds[index2]));
					fUnderlineCheckBox.setSelection(getPreferenceStore().getBoolean(uIds[index2]));

				}
			} else {

				
				fColorEditor.setEnabled(true);
				fBgColorEditor.setEnabled(true);
				fTransparentCheckBox.setEnabled(true);
				fBoldCheckBox.setEnabled(true);
				fItalicCheckBox.setEnabled(true);
				fStrikethroughCheckBox.setEnabled(true);
				fUnderlineCheckBox.setEnabled(true);
				

				String key = fgIds[index]; 
				RGB rgb = PreferenceConverter.getColor(getPreferenceStore(), key);
				fColorEditor.setEnabled(true);
				fColorEditor.setColorValue(rgb);		

				key = bgIds[index];
				rgb = PreferenceConverter.getColor(getPreferenceStore(), key);
				fBgColorEditor.setEnabled(true);
				fBgColorEditor.setColorValue(rgb);		
				
				fTransparentCheckBox.setSelection(getPreferenceStore().getBoolean(trIds[index]));
				fBoldCheckBox.setSelection(getPreferenceStore().getBoolean(bIds[index]));
				fItalicCheckBox.setSelection(getPreferenceStore().getBoolean(iIds[index]));
				fStrikethroughCheckBox.setSelection(getPreferenceStore().getBoolean(sIds[index]));
				fUnderlineCheckBox.setSelection(getPreferenceStore().getBoolean(uIds[index]));
						
			}


//		updateColorWidgets(fColorObjectDefaults[i]);  // [i][2]
	}



    protected void performDefaults() {
    	super.performDefaults();
			
			for (int i = 0; i < fgIds.length; i++) {
				fStore.setToDefault(fgIds[i]);
				fStore.setToDefault(bgIds[i]);
				fStore.setToDefault(trIds[i]);
				fStore.setToDefault(bIds[i]);
				fStore.setToDefault(iIds[i]);
				fStore.setToDefault(sIds[i]);
				fStore.setToDefault(uIds[i]);
			}
			handleTreeSelection();
			

    }


    public boolean performOk() {
    	super.performOk(); // I think it does nothing, the supertype implementation is empty
        return true;
    }

	public static P21EditorPreferences getP21EditorPreferences() {
		return fP21EditorPreferences;
	}
    

	/*
	      because the current implementation stores values immediately,
	      performApply() cannot do much.
	      performOk() is also not needed,
	      At least, performCancel() should restore the original values
	      Then, if performeApply() was pressed, performCancel()
	      should restore the values present at the moment when the performApply() 
	      was pressed the last time.
	 */   
	protected void performApply() {
        super.performApply(); // not necessary, probably

		for (int i = 0; i < fgIds.length; i++) {
			currentFgValues[i] = fStore.getString(fgIds[i]);
			currentBgValues[i] = fStore.getString(bgIds[i]);
			currentTrValues[i] = fStore.getBoolean(trIds[i]);
			currentBValues[i] = fStore.getBoolean(bIds[i]);
			currentIValues[i] = fStore.getBoolean(iIds[i]);
			currentSValues[i] = fStore.getBoolean(sIds[i]);
			currentUValues[i] = fStore.getBoolean(uIds[i]);
		}
	        
	
	}

	    public boolean performCancel() {
	    	super.performCancel(); // probably not needed

			for (int i = 0; i < fgIds.length; i++) {
				fStore.setValue(fgIds[i], currentFgValues[i]);
				fStore.setValue(bgIds[i], currentBgValues[i]);
				fStore.setValue(trIds[i], currentTrValues[i]);
				fStore.setValue(bIds[i], currentBValues[i]);
				fStore.setValue(iIds[i], currentIValues[i]);
				fStore.setValue(sIds[i], currentSValues[i]);
				fStore.setValue(uIds[i], currentUValues[i]);
			}
	    	
	    	return true;
	    	
	    }
	
}

