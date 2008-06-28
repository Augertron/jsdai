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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jsdai.express_compiler.ExpressCompilerPlugin;
import jsdai.express_compiler.editor.ExpressEditor;
import jsdai.express_compiler.help.IExpressCompilerHelpContextIds;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewerExtension5;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.internal.editors.text.EditorsPlugin;
import org.eclipse.ui.texteditor.AbstractTextEditor;

public class ExpressEditorPreferences extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
//public class ExpressEditorPreferences extends PreferencePage implements IWorkbenchPreferencePage {

	static ExpressEditorPreferences fEditorPreferences;

	public static final String AUTO_COMPILE = "express.autoBuild";

	public static final String  EXPRESS_KEYWORD_COLOR = "express.colorKeyword";
	public static final String  EXPRESS_TYPE_COLOR = "express.colorType";
	public static final String  EXPRESS_OPERATOR_COLOR = "express.colorOperator";
	public static final String  EXPRESS_BUILTIN_CONSTANT_COLOR = "express.colorBuiltinConstant";
	public static final String  EXPRESS_BUILTIN_FUNCTION_COLOR = "express.colorBuiltinFunction";
	public static final String  EXPRESS_BUILTIN_PROCEDURE_COLOR = "express.colorBuiltinProcedure";
	public static final String  EXPRESS_STRING_COLOR = "express.colorString";
	public static final String  EXPRESS_SINGLE_COLOR = "express.colorSingle";
	public static final String  EXPRESS_MULTI_COLOR = "express.colorMulti";
	public static final String  EXPRESS_DEFAULT_COLOR = "express.colorDefault";

	public static final String  EXPRESS_KEYWORD_BG_COLOR = "express.colorKeywordBG";
	public static final String  EXPRESS_TYPE_BG_COLOR = "express.colorTypeBG";
	public static final String  EXPRESS_OPERATOR_BG_COLOR = "express.colorOperatorBG";
	public static final String  EXPRESS_BUILTIN_CONSTANT_BG_COLOR = "express.colorBuiltinConstantBG";
	public static final String  EXPRESS_BUILTIN_FUNCTION_BG_COLOR = "express.colorBuiltinFunctionBG";
	public static final String  EXPRESS_BUILTIN_PROCEDURE_BG_COLOR = "express.colorBuiltinProcedureBG";
	public static final String  EXPRESS_STRING_BG_COLOR = "express.colorStringBG";
	public static final String  EXPRESS_SINGLE_BG_COLOR = "express.colorSingleBG";
	public static final String  EXPRESS_MULTI_BG_COLOR = "express.colorMultiBG";
	public static final String  EXPRESS_DEFAULT_BG_COLOR = "express.colorDefaultBG";

	public static final String  EXPRESS_KEYWORD_BOLD = "express.boldKeyword";
	public static final String  EXPRESS_TYPE_BOLD = "express.boldType";
	public static final String  EXPRESS_OPERATOR_BOLD = "express.boldOperator";
	public static final String  EXPRESS_BUILTIN_CONSTANT_BOLD = "express.boldBuiltinConstant";
	public static final String  EXPRESS_BUILTIN_FUNCTION_BOLD = "express.boldBuiltinFunction";
	public static final String  EXPRESS_BUILTIN_PROCEDURE_BOLD = "express.boldBuiltinProcedure";
	public static final String  EXPRESS_STRING_BOLD = "express.boldString";
	public static final String  EXPRESS_SINGLE_BOLD = "express.boldSingle";
	public static final String  EXPRESS_MULTI_BOLD = "express.boldMulti";
	public static final String  EXPRESS_DEFAULT_BOLD = "express.boldDefault";

	public static final String  EXPRESS_KEYWORD_ITALIC = "express.italicKeyword";
	public static final String  EXPRESS_TYPE_ITALIC = "express.italicType";
	public static final String  EXPRESS_OPERATOR_ITALIC = "express.italicOperator";
	public static final String  EXPRESS_BUILTIN_CONSTANT_ITALIC = "express.italicBuiltinConstant";
	public static final String  EXPRESS_BUILTIN_FUNCTION_ITALIC = "express.italicBuiltinFunction";
	public static final String  EXPRESS_BUILTIN_PROCEDURE_ITALIC = "express.italicBuiltinProcedure";
	public static final String  EXPRESS_STRING_ITALIC = "express.italicString";
	public static final String  EXPRESS_SINGLE_ITALIC = "express.italicSingle";
	public static final String  EXPRESS_MULTI_ITALIC = "express.italicMulti";
	public static final String  EXPRESS_DEFAULT_ITALIC = "express.italicDefault";

	public static final String  EXPRESS_KEYWORD_STRIKE = "express.strikeKeyword";
	public static final String  EXPRESS_TYPE_STRIKE = "express.strikeType";
	public static final String  EXPRESS_OPERATOR_STRIKE = "express.strikeOperator";
	public static final String  EXPRESS_BUILTIN_CONSTANT_STRIKE = "express.strikeBuiltinConstant";
	public static final String  EXPRESS_BUILTIN_FUNCTION_STRIKE = "express.strikeBuiltinFunction";
	public static final String  EXPRESS_BUILTIN_PROCEDURE_STRIKE = "express.strikeBuiltinProcedure";
	public static final String  EXPRESS_STRING_STRIKE = "express.strikeString";
	public static final String  EXPRESS_SINGLE_STRIKE = "express.strikeSingle";
	public static final String  EXPRESS_MULTI_STRIKE = "express.strikeMulti";
	public static final String  EXPRESS_DEFAULT_STRIKE = "express.strikeDefault";

	public static final String  EXPRESS_KEYWORD_UNDERLINE = "express.underlineKeyword";
	public static final String  EXPRESS_TYPE_UNDERLINE = "express.underlineType";
	public static final String  EXPRESS_OPERATOR_UNDERLINE = "express.underlineOperator";
	public static final String  EXPRESS_BUILTIN_CONSTANT_UNDERLINE = "express.underlineBuiltinConstant";
	public static final String  EXPRESS_BUILTIN_FUNCTION_UNDERLINE = "express.underlineBuiltinFunction";
	public static final String  EXPRESS_BUILTIN_PROCEDURE_UNDERLINE = "express.underlineBuiltinProcedure";
	public static final String  EXPRESS_STRING_UNDERLINE = "express.underlineString";
	public static final String  EXPRESS_SINGLE_UNDERLINE = "express.underlineSingle";
	public static final String  EXPRESS_MULTI_UNDERLINE = "express.underlineMulti";
	public static final String  EXPRESS_DEFAULT_UNDERLINE = "express.underlineDefault";

	public static final String  EXPRESS_KEYWORD_TRANSPARENT = "express.transparentKeyword";
	public static final String  EXPRESS_TYPE_TRANSPARENT = "express.transparentType";
	public static final String  EXPRESS_OPERATOR_TRANSPARENT = "express.transparentOperator";
	public static final String  EXPRESS_BUILTIN_CONSTANT_TRANSPARENT = "express.transparentBuiltinConstant";
	public static final String  EXPRESS_BUILTIN_FUNCTION_TRANSPARENT = "express.transparentBuiltinFunction";
	public static final String  EXPRESS_BUILTIN_PROCEDURE_TRANSPARENT = "express.transparentBuiltinProcedure";
	public static final String  EXPRESS_STRING_TRANSPARENT = "express.transparentString";
	public static final String  EXPRESS_SINGLE_TRANSPARENT = "express.transparentSingle";
	public static final String  EXPRESS_MULTI_TRANSPARENT = "express.transparentMulti";
	public static final String  EXPRESS_DEFAULT_TRANSPARENT = "express.transparentDefault";
	

	private ColorSelector fColorEditor;
	private ColorSelector fBgColorEditor;
	private List fColorList;
//	private Button fColorDefault;
    private Button fBoldCheckBox;
    private Button fItalicCheckBox;
    private Button fStrikethroughCheckBox;
    private Button fUnderlineCheckBox;
    private Button fTransparentCheckBox;
		private Button backgroundColorButton;
    Composite fParent;
    
    // names
	private final String fColorObjectNames [] = new String []{
		"Keywords",
		"Types",
		"Operators",
		"Built-in Constants",
		"Built-in Functions",
		"Built-in Procedures",
		"Strings",
		"Single line comments",
		"Multi line comments",
		"Others",
		};

	
	private final String fColorObjectIDs [] = new String []{
		
		EXPRESS_KEYWORD_COLOR,
		EXPRESS_TYPE_COLOR,
		EXPRESS_OPERATOR_COLOR,
		EXPRESS_BUILTIN_CONSTANT_COLOR,
		EXPRESS_BUILTIN_FUNCTION_COLOR,
		EXPRESS_BUILTIN_PROCEDURE_COLOR,
		EXPRESS_STRING_COLOR,
		EXPRESS_SINGLE_COLOR,
		EXPRESS_MULTI_COLOR,
		EXPRESS_DEFAULT_COLOR,
	};


	private final String fColorObjectBackgroundIDs [] = new String [] {

		EXPRESS_KEYWORD_BG_COLOR,
		EXPRESS_TYPE_BG_COLOR,
		EXPRESS_OPERATOR_BG_COLOR,
		EXPRESS_BUILTIN_CONSTANT_BG_COLOR,
		EXPRESS_BUILTIN_FUNCTION_BG_COLOR,
		EXPRESS_BUILTIN_PROCEDURE_BG_COLOR,
		EXPRESS_STRING_BG_COLOR,
		EXPRESS_SINGLE_BG_COLOR,
		EXPRESS_MULTI_BG_COLOR,
		EXPRESS_DEFAULT_BG_COLOR,
	};

	private final String fColorObjectBoldIDs [] = new String []{
		
		EXPRESS_KEYWORD_BOLD,
		EXPRESS_TYPE_BOLD,
		EXPRESS_OPERATOR_BOLD,
		EXPRESS_BUILTIN_CONSTANT_BOLD,
		EXPRESS_BUILTIN_FUNCTION_BOLD,
		EXPRESS_BUILTIN_PROCEDURE_BOLD,
		EXPRESS_STRING_BOLD,
		EXPRESS_SINGLE_BOLD,
		EXPRESS_MULTI_BOLD,
		EXPRESS_DEFAULT_BOLD,
	};

	private final String fColorObjectItalicIDs [] = new String []{
		
		EXPRESS_KEYWORD_ITALIC,
		EXPRESS_TYPE_ITALIC,
		EXPRESS_OPERATOR_ITALIC,
		EXPRESS_BUILTIN_CONSTANT_ITALIC,
		EXPRESS_BUILTIN_FUNCTION_ITALIC,
		EXPRESS_BUILTIN_PROCEDURE_ITALIC,
		EXPRESS_STRING_ITALIC,
		EXPRESS_SINGLE_ITALIC,
		EXPRESS_MULTI_ITALIC,
		EXPRESS_DEFAULT_ITALIC,
	};

	private final String fColorObjectStrikeIDs [] = new String []{
			
			EXPRESS_KEYWORD_STRIKE,
			EXPRESS_TYPE_STRIKE,
			EXPRESS_OPERATOR_STRIKE,
			EXPRESS_BUILTIN_CONSTANT_STRIKE,
			EXPRESS_BUILTIN_FUNCTION_STRIKE,
			EXPRESS_BUILTIN_PROCEDURE_STRIKE,
			EXPRESS_STRING_STRIKE,
			EXPRESS_SINGLE_STRIKE,
			EXPRESS_MULTI_STRIKE,
			EXPRESS_DEFAULT_STRIKE,
	};
	

	private final String fColorObjectUnderlineIDs [] = new String []{
			
			EXPRESS_KEYWORD_UNDERLINE,
			EXPRESS_TYPE_UNDERLINE,
			EXPRESS_OPERATOR_UNDERLINE,
			EXPRESS_BUILTIN_CONSTANT_UNDERLINE,
			EXPRESS_BUILTIN_FUNCTION_UNDERLINE,
			EXPRESS_BUILTIN_PROCEDURE_UNDERLINE,
			EXPRESS_STRING_UNDERLINE,
			EXPRESS_SINGLE_UNDERLINE,
			EXPRESS_MULTI_UNDERLINE,
			EXPRESS_DEFAULT_UNDERLINE
	};
	
	private final String fColorObjectTransparentIDs [] = new String []{
			
			EXPRESS_KEYWORD_TRANSPARENT,
			EXPRESS_TYPE_TRANSPARENT,
			EXPRESS_OPERATOR_TRANSPARENT,
			EXPRESS_BUILTIN_CONSTANT_TRANSPARENT,
			EXPRESS_BUILTIN_FUNCTION_TRANSPARENT,
			EXPRESS_BUILTIN_PROCEDURE_TRANSPARENT,
			EXPRESS_STRING_TRANSPARENT,
			EXPRESS_SINGLE_TRANSPARENT,
			EXPRESS_MULTI_TRANSPARENT,
			EXPRESS_DEFAULT_TRANSPARENT
	};



	
	private final String fColorObjectDefaults [] = new String []{
		
		"127,0,85",
		"64,0,200",
		"200,0,0",
		"64,64,200",
		"64,64,200",
		"64,64,200",
		"0,0,255",
		"64,128,128",
		"64,128,128",
		"0,0,0",
	};

	private final String fColorObjectBackgroundDefaults [] = new String []{
		
		"255,255,255",
		"255,255,255",
		"255,255,255",
		"255,255,255",
		"255,255,255",
		"255,255,255",
		"255,255,255",
	    "255,255,255",
		"255,255,255",
		"255,255,255"
	};






/*
	private final String fColorObjectsEnabled [] = new String []{
		
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
	};
*/

	private final boolean fColorObjectBoldDefaults [] = new boolean []{
		
		true,
		true,
		true,
		true,
		true,
		true,
		false,
		false,
		false,
		false,
	};

	private final boolean fColorObjectItalicDefaults [] = new boolean []{
		
		false,
		false,
		false,
		false,
		false,
		false,
		false,
		false,
		false,
		false,
	};

	private final boolean fColorObjectStrikeDefaults [] = new boolean []{
		
		false,
		false,
		false,
		false,
		false,
		false,
		false,
		false,
		false,
		false,
	};

	private final boolean fColorObjectUnderlineDefaults [] = new boolean []{
		
		false,
		false,
		false,
		false,
		false,
		false,
		false,
		false,
		false,
		false,
	};

	private final boolean fColorObjectTransparentDefaults [] = new boolean []{
		
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
	};

	private Map initialValueMap = new HashMap();

  public ExpressEditorPreferences() {
    super(GRID);
//	setPreferenceStore(ExpressCompilerPlugin.getDefault().getPreferenceStore());
		setPreferenceStore(EditorsPlugin.getDefault().getPreferenceStore());
    setDescription("Express editor preferences");
  	initializeDefaults();
  	// createFieldEditors();
		fEditorPreferences = this;
	}

	private void initializeDefaults() {

//System.out.println("Is here DEFAULT?");
		IPreferenceStore store = getPreferenceStore();

//		store.setDefault(AUTO_COMPILE, false);

		store.setDefault(EXPRESS_KEYWORD_COLOR, "127,0,85");
		store.setDefault(EXPRESS_TYPE_COLOR, "64,0,200");
		store.setDefault(EXPRESS_OPERATOR_COLOR, "200,0,0");
		store.setDefault(EXPRESS_BUILTIN_CONSTANT_COLOR, "64,64,200");
		store.setDefault(EXPRESS_BUILTIN_FUNCTION_COLOR, "64,64,200");
		store.setDefault(EXPRESS_BUILTIN_PROCEDURE_COLOR, "64,64,200");
		store.setDefault(EXPRESS_STRING_COLOR, "0,0,255");
		store.setDefault(EXPRESS_SINGLE_COLOR, "64,128,128");
		store.setDefault(EXPRESS_MULTI_COLOR, "64,128,128");
		store.setDefault(EXPRESS_DEFAULT_COLOR, "0,0,0");

		store.setDefault(EXPRESS_KEYWORD_BG_COLOR, "255,255,255");
		store.setDefault(EXPRESS_TYPE_BG_COLOR, "255,255,255");
		store.setDefault(EXPRESS_OPERATOR_BG_COLOR, "255,255,255");
		store.setDefault(EXPRESS_BUILTIN_CONSTANT_BG_COLOR, "255,255,255");
		store.setDefault(EXPRESS_BUILTIN_FUNCTION_BG_COLOR, "255,255,255");
		store.setDefault(EXPRESS_BUILTIN_PROCEDURE_BG_COLOR, "255,255,255");
		store.setDefault(EXPRESS_STRING_BG_COLOR, "255,255,255");
		store.setDefault(EXPRESS_SINGLE_BG_COLOR, "255,255,255");
		store.setDefault(EXPRESS_MULTI_BG_COLOR, "255,255,255");
		store.setDefault(EXPRESS_DEFAULT_BG_COLOR, "255,255,255");


		store.setDefault(EXPRESS_KEYWORD_BOLD, true);
		store.setDefault(EXPRESS_TYPE_BOLD, true);
		store.setDefault(EXPRESS_OPERATOR_BOLD, true);
		store.setDefault(EXPRESS_BUILTIN_CONSTANT_BOLD, true);
		store.setDefault(EXPRESS_BUILTIN_FUNCTION_BOLD, true);
		store.setDefault(EXPRESS_BUILTIN_PROCEDURE_BOLD, true);
		store.setDefault(EXPRESS_STRING_BOLD, false);
		store.setDefault(EXPRESS_SINGLE_BOLD, false);
		store.setDefault(EXPRESS_MULTI_BOLD, false);
		store.setDefault(EXPRESS_DEFAULT_BOLD, false);
		
		store.setDefault(EXPRESS_KEYWORD_ITALIC, false);
		store.setDefault(EXPRESS_TYPE_ITALIC, false);
		store.setDefault(EXPRESS_OPERATOR_ITALIC, false);
		store.setDefault(EXPRESS_BUILTIN_CONSTANT_ITALIC, false);
		store.setDefault(EXPRESS_BUILTIN_FUNCTION_ITALIC, false);
		store.setDefault(EXPRESS_BUILTIN_PROCEDURE_ITALIC, false);
		store.setDefault(EXPRESS_STRING_ITALIC, false);
		store.setDefault(EXPRESS_SINGLE_ITALIC, false);
		store.setDefault(EXPRESS_MULTI_ITALIC, false);
		store.setDefault(EXPRESS_DEFAULT_ITALIC, false);
		
		store.setDefault(EXPRESS_KEYWORD_STRIKE, false);
		store.setDefault(EXPRESS_TYPE_STRIKE, false);
		store.setDefault(EXPRESS_OPERATOR_STRIKE, false);
		store.setDefault(EXPRESS_BUILTIN_CONSTANT_STRIKE, false);
		store.setDefault(EXPRESS_BUILTIN_FUNCTION_STRIKE, false);
		store.setDefault(EXPRESS_BUILTIN_PROCEDURE_STRIKE, false);
		store.setDefault(EXPRESS_STRING_STRIKE, false);
		store.setDefault(EXPRESS_SINGLE_STRIKE, false);
		store.setDefault(EXPRESS_MULTI_STRIKE, false);
		store.setDefault(EXPRESS_DEFAULT_STRIKE, false);

		store.setDefault(EXPRESS_KEYWORD_UNDERLINE, false);
		store.setDefault(EXPRESS_TYPE_UNDERLINE, false);
		store.setDefault(EXPRESS_OPERATOR_UNDERLINE, false);
		store.setDefault(EXPRESS_BUILTIN_CONSTANT_UNDERLINE, false);
		store.setDefault(EXPRESS_BUILTIN_FUNCTION_UNDERLINE, false);
		store.setDefault(EXPRESS_BUILTIN_PROCEDURE_UNDERLINE, false);
		store.setDefault(EXPRESS_STRING_UNDERLINE, false);
		store.setDefault(EXPRESS_SINGLE_UNDERLINE, false);
		store.setDefault(EXPRESS_MULTI_UNDERLINE, false);
		store.setDefault(EXPRESS_DEFAULT_UNDERLINE, false);

		store.setDefault(EXPRESS_KEYWORD_TRANSPARENT, true);
		store.setDefault(EXPRESS_TYPE_TRANSPARENT, true);
		store.setDefault(EXPRESS_OPERATOR_TRANSPARENT, true);
		store.setDefault(EXPRESS_BUILTIN_CONSTANT_TRANSPARENT, true);
		store.setDefault(EXPRESS_BUILTIN_FUNCTION_TRANSPARENT, true);
		store.setDefault(EXPRESS_BUILTIN_PROCEDURE_TRANSPARENT, true);
		store.setDefault(EXPRESS_STRING_TRANSPARENT, true);
		store.setDefault(EXPRESS_SINGLE_TRANSPARENT, true);
		store.setDefault(EXPRESS_MULTI_TRANSPARENT, true);
		store.setDefault(EXPRESS_DEFAULT_TRANSPARENT, true);

		
		//public void performDefaults() {
//		super.performDefaults();
//		handleAppearanceColorListSelection();


	}


  protected void createFieldEditors() {

		PlatformUI.getWorkbench().getHelpSystem().setHelp(getFieldEditorParent(), IExpressCompilerHelpContextIds.EXPRESS_EDITOR_PREFERENCE_PAGE);
	  
	  // add if needed anything for general preferences


/*
	    Font font = getFieldEditorParent().getFont();
	    Group colorGroup = new Group(getFieldEditorParent(), SWT.NONE);
	    GridLayout layout = new GridLayout();
	    layout.numColumns = 1;
	    colorGroup.setLayout(layout);
	    colorGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	    colorGroup.setFont(font);
//	    colorGroup.setText("Colors for syntax highlighting");
//		addField(new ColorFieldEditor(EXPRESS_BACKGROUND_COLOR, ExpressCompilerPlugin.getResourceString("preferences.colorBackground"), colorGroup));

		// experiment ####################################################################
*/
		
	    Font font = getFieldEditorParent().getFont();

		
		
		Composite parent = getFieldEditorParent();
		fParent = parent;

		
		
		
//		addFiller(parent, 5);

	    Group colorGroup = new Group(parent, SWT.NONE);
	    GridLayout layout = new GridLayout();
		layout.marginHeight= 5;
	    layout.numColumns = 4;
	    colorGroup.setLayout(layout);
		GridData gd= new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint= convertHeightInCharsToPixels(16);
	    colorGroup.setLayoutData(gd);
	    colorGroup.setFont(font);
	    colorGroup.setText("Colors for syntax highlighting");
		
//		GridData gd= new GridData(GridData.HORIZONTAL_ALIGN_FILL);
//		gd= new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_VERTICAL);
		gd= new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan= 4;
		colorGroup.setLayoutData(gd);		




        Label ref_label = new Label(fParent, SWT.LEFT);
		gd= new GridData(GridData.BEGINNING);
        ref_label.setLayoutData(gd);
        ref_label.setText("       ");

	    fColorList = new List(colorGroup, SWT.SINGLE | SWT.V_SCROLL | SWT.BORDER);
//		gd= new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL);
		gd= new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
//		gd.heightHint= convertHeightInCharsToPixels(10);
		gd.heightHint= convertHeightInCharsToPixels(15);
		fColorList.setLayoutData(gd);



		for (int i= 0; i < fColorObjectNames.length; i++)
			fColorList.add(fColorObjectNames[i]);
		
		fColorList.getDisplay().asyncExec(new Runnable() {
			public void run() {
				if (fColorList != null && !fColorList.isDisposed()) {
					fColorList.select(0);
					handleColorListSelection();
				}
			}
		});


/*
		fColorList.add("Keywords");
		fColorList.add("Types");
		fColorList.add("Operators");
		fColorList.add("Built-In");
		fColorList.add("Strings");
		fColorList.add("Single line comments");
		fColorList.add("Multi line comments");
		fColorList.add("Default");
		fColorList.add("Background");
*/
		Composite stylesComposite= new Composite(colorGroup, SWT.NONE);
		layout= new GridLayout();
		layout.marginHeight= 0;
		layout.marginWidth= 0;
		layout.numColumns= 2;
		stylesComposite.setLayout(layout);
		stylesComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		


/*

		SelectionListener colorDefaultSelectionListener= new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				boolean systemDefault= fColorDefault.getSelection();
				fColorEditor.getButton().setEnabled(!systemDefault);
				
				int i= fColorList.getSelectionIndex();
				if (i == -1)
					return;

				String key= fColorObjectDefaults[i];
System.out.println("colorDefaultSelectionListener, key: " + key);
				if (key != null)
					getPreferenceStore().setValue(key, systemDefault);
			}
			public void widgetDefaultSelected(SelectionEvent e) {}
		};

*/
/*
		
		fColorDefault= new Button(stylesComposite, SWT.CHECK);
		fColorDefault.setText("Enable"); 
		gd= new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalAlignment= GridData.BEGINNING;
		gd.horizontalSpan= 4;
		fColorDefault.setLayoutData(gd);
		fColorDefault.setVisible(false);
		fColorDefault.addSelectionListener(colorDefaultSelectionListener);
*/
		fColorEditor = new ColorSelector(stylesComposite);
		Button foregroundColorButton= fColorEditor.getButton();
//		gd= new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd= new GridData(GridData.BEGINNING);
		gd.horizontalIndent= 10;
//		gd.horizontalAlignment= GridData.BEGINNING;
		foregroundColorButton.setLayoutData(gd);

		Label l= new Label(stylesComposite, SWT.TRAIL);
		l.setText("Foreground Color"); 
		gd= new GridData();
//		gd.horizontalSpan= 2;
//		gd.horizontalAlignment= GridData.END;
		l.setLayoutData(gd);

		fBgColorEditor = new ColorSelector(stylesComposite);
		backgroundColorButton= fBgColorEditor.getButton();
//		gd= new GridData(GridData.);
		gd= new GridData(GridData.BEGINNING);
		gd.horizontalIndent= 10;
//		gd.horizontalAlignment= GridData.BEGINNING;
		backgroundColorButton.setLayoutData(gd);

//backgroundColorButton.setEnabled(false);

		Label l2 = new Label(stylesComposite, SWT.TRAIL);
		l2.setText("Background Color"); 
		gd= new GridData();
//		gd.horizontalAlignment= GridData.END;
		l2.setLayoutData(gd);
	
		SelectionListener transparentCheckBoxSelectionListener = new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing
			}
			public void widgetSelected(SelectionEvent e) {
				int i= fColorList.getSelectionIndex();
				if (i == -1) return;
				String key= fColorObjectTransparentIDs[i];
				getPreferenceStore().setValue(key, fTransparentCheckBox.getSelection());
				backgroundColorButton.setEnabled(!fTransparentCheckBox.getSelection());

			}
		};

		fTransparentCheckBox= new Button(stylesComposite, SWT.CHECK);
		fTransparentCheckBox.setText("Transparent Background"); 
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
				int i= fColorList.getSelectionIndex();
				if (i == -1) return;
				String key= fColorObjectBoldIDs[i];
				getPreferenceStore().setValue(key, fBoldCheckBox.getSelection());
			}
		};
	
		

		fBoldCheckBox= new Button(stylesComposite, SWT.CHECK);
		fBoldCheckBox.setText("Bold"); 
		gd= new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalIndent= 20;
		gd.horizontalSpan= 4;
		fBoldCheckBox.setLayoutData(gd);
		fBoldCheckBox.addSelectionListener(boldCheckBoxSelectionListener);

/*
		fBoldCheckBox = new Button(stylesComposite, SWT.CHECK);
		fBoldCheckBox.setText("Bold"); 
//		gd= new GridData(GridData.FILL_HORIZONTAL);
		gd= new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
//		gd.horizontalAlignment= GridData.BEGINNING;
		gd.horizontalIndent= 20;
		gd.horizontalSpan= 4;
		fBoldCheckBox.setLayoutData(gd);
		fBoldCheckBox.setVisible(true);
		fBoldCheckBox.addSelectionListener(boldCheckBoxSelectionListener);
*/

		SelectionListener italicCheckBoxSelectionListener = new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing
			}
			public void widgetSelected(SelectionEvent e) {
				int i= fColorList.getSelectionIndex();
				if (i == -1) return;
				String key= fColorObjectItalicIDs[i];
				getPreferenceStore().setValue(key, fItalicCheckBox.getSelection());
			}
		};
		
		fItalicCheckBox= new Button(stylesComposite, SWT.CHECK);
		fItalicCheckBox.setText("Italic"); 
		gd= new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalIndent= 20;
		gd.horizontalSpan= 4;
		fItalicCheckBox.setLayoutData(gd);
		fItalicCheckBox.addSelectionListener(italicCheckBoxSelectionListener);
	
/*
		fItalicCheckBox= new Button(stylesComposite, SWT.CHECK);
		fItalicCheckBox.setText("Italic"); 
		gd= new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalAlignment= GridData.BEGINNING;
		gd.horizontalIndent= 20;
		gd.horizontalSpan= 4;
		fItalicCheckBox.setLayoutData(gd);
		fItalicCheckBox.setVisible(true);
		fItalicCheckBox.addSelectionListener(italicCheckBoxSelectionListener);
*/
		SelectionListener strikethroughCheckBoxSelectionListener = new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing
			}
			public void widgetSelected(SelectionEvent e) {
				int i= fColorList.getSelectionIndex();
				if (i == -1) return;
				String key= fColorObjectStrikeIDs[i];
				getPreferenceStore().setValue(key, fStrikethroughCheckBox.getSelection());
			}
		};
	
		fStrikethroughCheckBox= new Button(stylesComposite, SWT.CHECK);
		fStrikethroughCheckBox.setText("Strikethrough"); 
		gd= new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalIndent= 20;
		gd.horizontalSpan= 4;
		fStrikethroughCheckBox.setLayoutData(gd);
		fStrikethroughCheckBox.addSelectionListener(strikethroughCheckBoxSelectionListener);
	
	
/*		
		fStrikethroughCheckBox = new Button(stylesComposite, SWT.CHECK);
		fStrikethroughCheckBox.setText("Strikethrough"); 
		gd= new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalAlignment= GridData.BEGINNING;
		gd.horizontalSpan= 2;
		fStrikethroughCheckBox.setLayoutData(gd);
		fStrikethroughCheckBox.setVisible(true);
		fStrikethroughCheckBox.addSelectionListener(strikethroughCheckBoxSelectionListener);
*/

		SelectionListener underlineCheckBoxSelectionListener = new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing
			}
			public void widgetSelected(SelectionEvent e) {
				int i= fColorList.getSelectionIndex();
				if (i == -1) return;
				String key= fColorObjectUnderlineIDs[i];
				getPreferenceStore().setValue(key, fUnderlineCheckBox.getSelection());
			}
		};

		fUnderlineCheckBox= new Button(stylesComposite, SWT.CHECK);
		fUnderlineCheckBox.setText("Underline"); 
		gd= new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalIndent= 20;
		gd.horizontalSpan= 4;
		fUnderlineCheckBox.setLayoutData(gd);
		fUnderlineCheckBox.addSelectionListener(underlineCheckBoxSelectionListener);

 /*		
		Button foregroundUnderlineButton= new Button(stylesComposite, SWT.CHECK);
		foregroundUnderlineButton.setText("Underline"); 
		gd= new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalAlignment= GridData.BEGINNING;
		gd.horizontalSpan= 2;
		foregroundUnderlineButton.setLayoutData(gd);
		foregroundUnderlineButton.setVisible(true);
		foregroundUnderlineButton.addSelectionListener(foregroundUnderlineButtonSelectionListener);
*/

		fColorList.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
//System.out.println("selected in color list - DEFAULT, event: " + e );
				// do nothing
			}
			public void widgetSelected(SelectionEvent e) {
//System.out.println("selected in color list, event: " + e );
				handleColorListSelection();
			}
		});
		foregroundColorButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
//System.out.println("foreground color button - DEFAULT, event: " + e );
				// do nothing
			}
			public void widgetSelected(SelectionEvent e) {
				int i= fColorList.getSelectionIndex();
//System.out.println("foreground color button - i: " + i );
				if (i == -1)
					return;

				String key= fColorObjectIDs[i]; // [i][1];
//System.out.println("foreground color button - , key: " + key );
				PreferenceConverter.setValue(getPreferenceStore(), key, fColorEditor.getColorValue());
			}
		});
		backgroundColorButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
//System.out.println("foreground color button - DEFAULT, event: " + e );
				// do nothing
			}
			public void widgetSelected(SelectionEvent e) {
				int i= fColorList.getSelectionIndex();
//System.out.println("foreground color button - i: " + i );
				if (i == -1)
					return;

				String key= fColorObjectBackgroundIDs[i]; // [i][1];
//System.out.println("foreground color button - , key: " + key );
				PreferenceConverter.setValue(getPreferenceStore(), key, fBgColorEditor.getColorValue());
			}
		});

		// This is a hack to make auto compile filed invisible. Since ExpressEditorPreferences
		// extends FieldEditorPreferencePage it needs to have at least one field. So we simply
		// make the invisible field.
		BooleanFieldEditor autoCompileField = new BooleanFieldEditor(AUTO_COMPILE, ExpressCompilerPlugin.getResourceString("preferences.autoCompile"), getFieldEditorParent()) {
			protected void doFillIntoGrid(Composite parent, int numColumns) {}
			protected void adjustForNumColumns(int numColumns) { }
		};
		addField(autoCompileField);

//		String  default_reference = "More colors and fonts can be configured on the <a href=\"org.eclipse.ui.preferencePages.GeneralTextEditor\">Text Editors</a> and on the <a href=\"org.eclipse.ui.preferencePages.ColorsAndFonts\">Colors and Fonts</a> preference pages.";
		String  default_reference1 = "More colors and other settings be configured on the <a href=\"org.eclipse.ui.preferencePages.GeneralTextEditor\">Text Editors</a> page.";
		String  default_reference2 = "Fonts can be configured on the <a href=\"org.eclipse.ui.preferencePages.ColorsAndFonts\">Colors and Fonts</a> preference page.";


//		Label gap = new Label(fParent, SWT.LEFT);
//		gap.setText("                             "); 
//		gd= new GridData(GridData.BEGINNING);
////		gd.horizontalSpan= 2;
////		gd.horizontalAlignment= GridData.END;
//		gap.setLayoutData(gd);

		
		Label gap2 = new Label(fParent, SWT.LEFT);
		gap2.setText("                             "); 
		gd= new GridData(GridData.BEGINNING);
//		gd.horizontalSpan= 2;
//		gd.horizontalAlignment= GridData.END;
		gap2.setLayoutData(gd);
		
		Link link = new Link(fParent, SWT.LEFT);
		link.setText(default_reference1);
		link.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				PreferencesUtil.createPreferenceDialogOn(fParent.getShell(), e.text, null, null); //$NON-NLS-1$
			}
		});
		Link link2 = new Link(fParent, SWT.LEFT);
		link2.setText(default_reference2);
		link2.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				PreferencesUtil.createPreferenceDialogOn(fParent.getShell(), e.text, null, null); //$NON-NLS-1$
			}
		});

		saveInitialValue();


  }

	protected Control createContents(Composite parent) {
		Control control = super.createContents(parent);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(control, IExpressCompilerHelpContextIds.EXPRESS_EDITOR_PREFERENCE_PAGE);
    return control;
}

    private void handleColorListSelection() {	
		if (fColorList == null) {
			return;
//			createFieldEditors();
		}
		int i= fColorList.getSelectionIndex();
		if (i == -1)
			return;
		String key= fColorObjectIDs[i];  // [i][1]
		RGB rgb= PreferenceConverter.getColor(getPreferenceStore(), key);
		fColorEditor.setColorValue(rgb);		

		key= fColorObjectBackgroundIDs[i];  // [i][1]
		rgb= PreferenceConverter.getColor(getPreferenceStore(), key);
		fBgColorEditor.setColorValue(rgb);		

		fBoldCheckBox.setSelection(getPreferenceStore().getBoolean(fColorObjectBoldIDs[i]));
		fItalicCheckBox.setSelection(getPreferenceStore().getBoolean(fColorObjectItalicIDs[i]));
		fStrikethroughCheckBox.setSelection(getPreferenceStore().getBoolean(fColorObjectStrikeIDs[i]));
		fUnderlineCheckBox.setSelection(getPreferenceStore().getBoolean(fColorObjectUnderlineIDs[i]));
		fTransparentCheckBox.setSelection(getPreferenceStore().getBoolean(fColorObjectTransparentIDs[i]));
		backgroundColorButton.setEnabled(!fTransparentCheckBox.getSelection());


//		updateColorWidgets(fColorObjectDefaults[i]);  // [i][2]
	}

/*    
	private void updateColorWidgets(String systemDefaultKey) {
		if (systemDefaultKey == null) {
			fColorDefault.setSelection(false);
			fColorDefault.setVisible(false);
			fColorEditor.getButton().setEnabled(true);
		} else {
			boolean systemDefault= getPreferenceStore().getBoolean(systemDefaultKey);
			fColorDefault.setSelection(systemDefault);
			fColorDefault.setVisible(true);
			fColorEditor.getButton().setEnabled(!systemDefault);
		}
	}
*/



  public void init(IWorkbench workbench) {
    // see above, also may be needed or not

/*
		for (int i= 0; i < fColorObjects0.length; i++)
			fColorList.add(fColorObjects0[i]);
		
		fColorList.getDisplay().asyncExec(new Runnable() {
			public void run() {
				if (fColorList != null && !fColorList.isDisposed()) {
					fColorList.select(0);
					handleAppearanceColorListSelection();
				}
			}
		});
*/
  }

	public static ExpressEditorPreferences getEditorPreferences() {
		return fEditorPreferences;
	}

	
	/*
	 	this is a very stupid temp solution
	 */
	public boolean performOk_not_using() {
		boolean ok = super.performOk();

	
		IWorkbenchPage page =PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		IEditorPart active = page.getActiveEditor();
		IEditorInput active_input = null;

		
		if (active != null) {
			active_input = active.getEditorInput();
		}
		IEditorPart[] editors = page.getEditors();
		for (int k = 0; k < editors.length; k++) {
			IEditorPart current = editors[k];
			if (current instanceof ExpressEditor) {
//				ExpressEditor exd = (ExpressEditor)current;
			}


			IEditorInput input = current.getEditorInput();
			String e_name = input.getName();
			// if the name ends with .exp, close it and open again
			
			if (e_name.endsWith(".exp")) {
		
//				String cursor_position_description = null;
				int cursor_offset = -1;
				
				 ISourceViewer viewer = (SourceViewer) ((ExpressEditor)current).getExpressSourceViewer();
//			    ((ExpressEditor)current).initializeExpressViewerColors(viewer);
//			    viewer.invalidateTextPresentation();
					StyledText styledText= viewer.getTextWidget();
					cursor_offset = styledText.getCaretOffset();

//					Caret caret = styledText.getCaret();

//					((ITextEditorExtension3) caret).getInsertMode();
//					cursor_position_description = ((ExpressEditor)current).getExpressCursorPosition();
//					cursor_offset = widgetOffset2ModelOffset(viewer, styledText.getCaretOffset());
				
				boolean was_dirty = false;
				String doc_backup = null;
				if (current.isDirty()) {
					// get document to string, close and reopen the editor and put the document back,
					// also get the cursor position and restore it as well
					
		            if (current instanceof ExpressEditor) {
		            	// sure it is ExpressEditor
//		        		ISourceViewer viewer = (SourceViewer) ((ExpressEditor)current).getExpressViewer();
						IDocument document = viewer.getDocument();
/*
System.out.println("document: " + document);
String [] categories = document.getPositionCategories();
for (int ii = 0; ii < categories.length; ii++) {
 System.out.println("category: " + categories[ii]);	
}
*/
						viewer.invalidateTextPresentation();
						doc_backup = document.get();

						
//System.out.println("cursor position:  " + cursor_position_description);

						//						document.
						was_dirty = true;
						page.closeEditor(current, false);
		            }
				} else {
					// should not happen attempt to save
					page.closeEditor(current, true);
				}
				IEditorPart new_editor = null;
				try {
					new_editor = page.openEditor(input, "net.jsdai.express_compiler.editor.ExpressEditor", true);
				} catch (PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//        		ISourceViewer viewer2 = (SourceViewer) ((ExpressEditor)new_editor).getExpressSourceViewer();
				if (was_dirty) {
					// replace the content with the backup contents
					IDocument document = viewer.getDocument();
					document.set(doc_backup);
					
//					document.
				}
				((AbstractTextEditor) new_editor).setHighlightRange(cursor_offset, 0, true);
				((AbstractTextEditor) new_editor).setHighlightRange(0, 0, false);
//				StyledText styledText = viewer.getTextWidget();
//				styledText.setCaretOffset(cursor_offset);
			
				if (active_input == input) {
					active = new_editor;
				}
				
			}
		}

		if (active != null) {
	    	page.bringToTop(active);
	    }




		return ok;
	}

	public boolean performCancel() {
		restoreInitialValues();
		return true;
	}

	protected void performApply() {
		saveInitialValue();
		super.performApply();
	}

	public void performDefaults() {
		super.performDefaults();
//System.out.println("another attempt at defaults");
//		handleAppearanceColorListSelection();
//		initializeDefaults();

		for (int i= 0; i < fColorObjectIDs.length; i++) {
			String key= fColorObjectIDs[i]; // [i][1];
//	   		PreferenceConverter.setValue(getPreferenceStore(), key, PreferenceConverter.getColor(getPreferenceStore(),fColorObjects2[i]));
	   		PreferenceConverter.setValue(getPreferenceStore(), key, StringConverter.asRGB(fColorObjectDefaults[i]));
//			getPreferenceStore().setDefault(fColorObjects1[i], fColorObjects2[i]);

			key = fColorObjectBackgroundIDs[i]; // [i][1];
	   		PreferenceConverter.setValue(getPreferenceStore(), key, StringConverter.asRGB(fColorObjectBackgroundDefaults[i]));
	
			key = fColorObjectBoldIDs[i]; // [i][1];
			getPreferenceStore().setValue(key, fColorObjectBoldDefaults[i]);
			
			key = fColorObjectItalicIDs[i]; // [i][1];
			getPreferenceStore().setValue(key, fColorObjectItalicDefaults[i]);

			key = fColorObjectStrikeIDs[i]; // [i][1];
			getPreferenceStore().setValue(key, fColorObjectStrikeDefaults[i]);

			key = fColorObjectUnderlineIDs[i]; // [i][1];
			getPreferenceStore().setValue(key, fColorObjectUnderlineDefaults[i]);

			key = fColorObjectTransparentIDs[i]; // [i][1];
			getPreferenceStore().setValue(key, fColorObjectTransparentDefaults[i]);

		}
			handleColorListSelection();

	}

	private void saveInitialValue() {
		IPreferenceStore preferenceStore = getPreferenceStore();
		for (int i= 0; i < fColorObjectIDs.length; i++) {
			String key= fColorObjectIDs[i];
			RGB rgb= PreferenceConverter.getColor(preferenceStore, key);
			initialValueMap.put(key, rgb);

			key = fColorObjectBackgroundIDs[i];
			rgb= PreferenceConverter.getColor(preferenceStore, key);
			initialValueMap.put(key, rgb);
	
			key = fColorObjectBoldIDs[i];
			initialValueMap.put(key, Boolean.valueOf(preferenceStore.getBoolean(key)));
			
			key = fColorObjectItalicIDs[i];
			initialValueMap.put(key, Boolean.valueOf(preferenceStore.getBoolean(key)));

			key = fColorObjectStrikeIDs[i];
			initialValueMap.put(key, Boolean.valueOf(preferenceStore.getBoolean(key)));

			key = fColorObjectUnderlineIDs[i];
			initialValueMap.put(key, Boolean.valueOf(preferenceStore.getBoolean(key)));

			key = fColorObjectTransparentIDs[i];
			initialValueMap.put(key, Boolean.valueOf(preferenceStore.getBoolean(key)));

		}
		
	}

	private void restoreInitialValues() {
		IPreferenceStore preferenceStore = getPreferenceStore();
		for (Iterator i = initialValueMap.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			String key = (String) entry.getKey();
			Object value = entry.getValue();
			if(value instanceof RGB) {
		   		PreferenceConverter.setValue(preferenceStore, key, (RGB)value);
			} else if(value instanceof Boolean) {
				preferenceStore.setValue(key, ((Boolean)value).booleanValue());
			}
		}
	}

	int widgetOffset2ModelOffset(ISourceViewer viewer, int widgetOffset) {
		if (viewer instanceof ITextViewerExtension5) {
			ITextViewerExtension5 extension= (ITextViewerExtension5) viewer;
			return extension.widgetOffset2ModelOffset(widgetOffset);
		}
		return widgetOffset + viewer.getVisibleRegion().getOffset();
	}


}
