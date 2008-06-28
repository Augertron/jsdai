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

package jsdai.edit;

import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.datatransfer.*;
import java.net.*;

import javax.swing.*;
import javax.swing.plaf.metal.*;
import javax.swing.event.*;
import javax.swing.border.*;

import jsdai.lang.*;
import jsdai.beans.*;
import jsdai.dictionary.*;
import jsdai.mapping.*;
import jsdai.util.*;

/**
* Visual tool which allow to browse and edit step data.
* To run it write "java jsdai.util.SdaiEdit".
*/
public class SdaiEdit extends JTabbedPane {

	static boolean mappingEditorFlag = false;

	public static int bookCount = 0;
	Properties props = new Properties();
	static final String theTitle = "SdaiEdit";
	JFrame frame = new JFrame(theTitle);

	SdaiSessionBean sessionBean;
	SdaiRepositoryBean repositoryBean;
	SdaiModelBean modelBean;
	SchemaInstanceBean schemaBean;
	EntityExtentBean extentBean;
	BaseEntityBean entityBean;
	AggregateBean aggregateBean;
	ExtentMappingBean extentMapBean;
	EntityMappingBean entityMapBean;
	AggregateMappingPage aggregateMapPage;
    MapEditEntitiesPanel mapEditEntitiesPanel = null;
    MapEditEntMappingsPanel mapEditEntMappingsPanel = null;
	PropertiesTab propertiesTab;
	AboutBean aboutBean;
    
    boolean doRefreshData = true;

	String names[] = {"Main", "Repository", "SchemaInstance", "Model", "EntityExtent", "EntityInstance",
						   "Aggregate", "ExtentMapping", "InstanceMapping", "AggregateMapping", 
						   "Properties", "About", "Temporary"};
    public static final String MAPPING_EDITOR_ENTITIES = "MappingEditor";
    public static final String MAPPING_EDITOR_ENTITY_MAPPINGS = "EntityMappings";

	NavigationBar navbar = new NavigationBar();
	boolean chainLock = false;
	Vector chain = new Vector();
	int currentChain = -1;

	int lastTabIndex = 0;
	boolean checkedit = true;

	//	Menu Bar Item names
	public static String OPTIONS_MENU_LBL = "View";
	public static String LF_MENU_LBL = "Look and Feel";
	public static String LF_THEMES_MENU_LBL = "Look and Feel Themes";
	//	Menu bar commands
	public static final String CHOOSE_LF_DLG = "OpenLFDlg";
	public static final String EXIT_SDAIEDIT = "ExitSdaiEdit";
	//	Look and feel
	protected String selectedLookAndFeel;
	protected HashMap metalThemes;

  	public static void main(String[] args) {
		for (int i = 0; i < args.length; i++) {
			if(args[i].equals("-mapping-editor")) {
				mappingEditorFlag = true;
			}
		}
		bookCount++;
		SdaiEdit sdaiedit = new SdaiEdit();
		try {
			synchronized(theTitle) { theTitle.wait(); }
		} catch (InterruptedException e) { }
		System.exit(0);
  	}

	public static void run() {
		bookCount++;
		SdaiEdit sdaiedit = new SdaiEdit();
		try {
			synchronized(theTitle) { theTitle.wait(); }
		} catch (InterruptedException e) { }
	}

	public SdaiEdit() {
		try {
			init();
		} catch (SdaiException ex) {
			processMessage(ex);
		}
	}

	private void init() throws SdaiException {
		SdaiSession session = (SdaiSession.getSession() != null) ? SdaiSession.getSession() : SdaiSession.openSession();
 		int cacheSize = 32 * 1024;
 		SdaiSession.getSession().getSdaiImplementation().setFeature("mapping-read-cache-size", cacheSize);

		sessionBean			= new SdaiSessionBean(frame);
		repositoryBean		= new SdaiRepositoryBean();
		schemaBean			= new SchemaInstanceBean();
		modelBean			= new SdaiModelBean();
		extentBean			= new EntityExtentBean();
		entityBean			= new BaseEntityBean();
		aggregateBean 		= new AggregateBean();
		extentMapBean 		= new ExtentMappingBean();
		entityMapBean 		= new EntityMappingBean();
		aggregateMapPage	= new AggregateMappingPage();
		if(mappingEditorFlag) {
			mapEditEntitiesPanel = new MapEditEntitiesPanel();
			mapEditEntMappingsPanel = new MapEditEntMappingsPanel();
		}
		propertiesTab     	= new PropertiesTab(this);
		aboutBean			= new AboutBean();
		metalThemes 		= new HashMap();

		setTabPlacement(SwingConstants.RIGHT);

		addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				try {
//Debug.println("Page changed");
					if (checkEditMode()) {
						Component com = getComponentAt(getSelectedIndex());
						if (com instanceof SdaiPanel) {
							SdaiPanel page = (SdaiPanel)com;
							lastTabIndex = getSelectedIndex();
							
                            if (doRefreshData) {
                                page.refreshData();
                            }
                            
							doTabPressed(page);
						}
					} else {
						checkedit = false;
						setSelectedIndex(lastTabIndex);
						checkedit = true;
					}
				} catch (SdaiException h) {
					processMessage(h);
				}
			}
		});
		props = takeProperties(props);
		setProperties(props);

//session
		SdaiSession.setLogWriter(null);
		SimpleOperations.enshureReadWriteTransaction();
		sessionBean.setSession(session);
		sessionBean.setProperties(props);
		sessionBean.addGoListener(goListener);
		addTab(names[0], sessionBean);
//repository
		repositoryBean.addGoListener(goListener);
		addTab(names[1], repositoryBean);
		setEnabledAt(1, false);
//schema_instance
		schemaBean.setProperties(props);
		schemaBean.addGoListener(goListener);
		addTab(names[2], schemaBean);
		setEnabledAt(2, false);
//model
		modelBean.setProperties(props);
		modelBean.addGoListener(goListener);
		addTab(names[3], modelBean);
		setEnabledAt(3, false);
//EntityExtent
		extentBean.addGoListener(goListener);
		addTab(names[4], extentBean);
		setEnabledAt(4, false);
//EntityInstance
		entityBean.addGoListener(goListener);
		entityBean.setProperties(props);
		addTab(names[5], entityBean);
		setEnabledAt(5, false);
//Aggregate
		aggregateBean.addGoListener(goListener);
		aggregateBean.getList().setBackground(aggregateBean.getBackground());
		aggregateBean.setEditable(true);
		aggregateBean.setType(true);
		addTab(names[6], aggregateBean);
		setEnabledAt(6, false);
//Extent_mappingBean
		extentMapBean.addGoListener(goListener);
		extentMapBean.setProperties(props);
		addTab(names[7], extentMapBean);
		setEnabledAt(7, false);
//Entity_mappingBean
		entityMapBean.addGoListener(goListener);
		entityMapBean.setProperties(props);
		addTab(names[8], entityMapBean);
		setEnabledAt(8, false);
//AggregateMappingBean
		aggregateMapPage.addGoListener(goListener);
		aggregateMapPage.setProperties(props);
		addTab(names[9], aggregateMapPage);
		setEnabledAt(9, false);
//MapEditEntitiesPanel
		if(mapEditEntitiesPanel != null) {
			mapEditEntitiesPanel.addGoListener(goListener);
			mapEditEntitiesPanel.setProperties(props);
			addTab(MAPPING_EDITOR_ENTITIES, mapEditEntitiesPanel);
			setEnabledAt(getTabCount() - 1, false);
		}
//MapEditEntMappingsPanel
		if(mapEditEntMappingsPanel != null) {
			mapEditEntMappingsPanel.addGoListener(goListener);
			mapEditEntMappingsPanel.setProperties(props);
			addTab(MAPPING_EDITOR_ENTITY_MAPPINGS, mapEditEntMappingsPanel);
			setEnabledAt(getTabCount() - 1, false);
		}
//Properties
		propertiesTab.setProperties(props);
		addTab(names[10], propertiesTab);
//About
		aboutBean.setVersion(session.getSdaiImplementation().getLevel());
        aboutBean.setCopyright(Implementation.copyright);
		addTab(names[11], aboutBean);

		frame.setIconImage((new ImageIcon(getClass().getResource("images/icon.gif"))).getImage());
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(navbar, BorderLayout.NORTH);
		frame.getContentPane().add(this, BorderLayout.CENTER);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exit();
    	  	}
	   	});

		//	JMenu bar
		frame.setJMenuBar(createMenu());

		frame.setGlassPane(new MyGlassPane());
		frame.show();
        // We set the properties again in order to fix window positioning
		setProperties(props);
		/* FIXME: Uncomment this to get registration code back
		int ii = (new SampleSerialCode()).checkSerialNumber(license, serial, key);
		if (ii < 0) {
			JOptionPane.showMessageDialog(this, "Evaluation version. Expires in "+((-1)*ii)+" days.", "SdaiEdit", JOptionPane.PLAIN_MESSAGE);
		} else if (ii == SampleSerialCode.S_EXPIRED) {
			JOptionPane.showMessageDialog(this, "Evaluation period expired.", "SdaiEdit", JOptionPane.PLAIN_MESSAGE);
    	    	System.exit(0);
		} else if (ii == SampleSerialCode.EDIT_VALID_KEY) {
//All is ok.
		} else if (ii == SampleSerialCode.S_INVALID) {
			JOptionPane.showMessageDialog(this, "Wrong serial number!", "SdaiEdit", JOptionPane.PLAIN_MESSAGE);
			System.exit(0);
		}*/
	}

	private JMenuBar createMenu ()
	{
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.setMnemonic(KeyEvent.VK_X);
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		});
		exitItem.setActionCommand(EXIT_SDAIEDIT);
		fileMenu.add(exitItem);
		menuBar.add(fileMenu);

		//	View menu
		JMenu optionsMenu = new JMenu(OPTIONS_MENU_LBL);
		optionsMenu.setMnemonic(KeyEvent.VK_V);

		//	Look and feel item
		JMenu lfMenu = new JMenu(LF_MENU_LBL);
		//lfMenu.setMnemonic(KeyEvent.VK_L);
		optionsMenu.add(lfMenu);

		menuBar.add(optionsMenu);

		LookAndFeelActionListener lfListener = new LookAndFeelActionListener();
		ButtonGroup group = new ButtonGroup();

		// attempt to load a custom l&f, specified by user:
		String customTheme = getLastUsedTheme();
		if (customTheme != null && !customTheme.equals("")) {
			loadCustomTheme(customTheme);
			// make sure the LF has been applied to the menu bar!
			SwingUtilities.updateComponentTreeUI(menuBar);
		}

		UIManager.LookAndFeelInfo[] lfInfos = UIManager.getInstalledLookAndFeels();

		String selectedLookAndFeelName = UIManager.getLookAndFeel().getName();
		String currentLookAndFeel;
		JRadioButtonMenuItem menuItem;

		for (int i=0; i<lfInfos.length; i++)
		{
			currentLookAndFeel = lfInfos[i].getName();
			menuItem = new JRadioButtonMenuItem(currentLookAndFeel);
			menuItem.setActionCommand(lfInfos[i].getClassName());
			menuItem.addActionListener(lfListener);
			group.add(menuItem);
			lfMenu.add(menuItem);

			if (selectedLookAndFeelName.equals(currentLookAndFeel))
			{
				menuItem.setSelected(true);
				selectedLookAndFeel = lfInfos[i].getClassName();
			}
		}

		ThemeActionListener themeListener = new ThemeActionListener();

		JMenu themesMenu = new JMenu(LF_THEMES_MENU_LBL);
		themesMenu.setMnemonic(KeyEvent.VK_T);
		lfMenu.add(themesMenu);

		group = new ButtonGroup();

		DefaultMetalTheme defaultMT = new DefaultMetalTheme();

		metalThemes.put(defaultMT.getName(), defaultMT);

		menuItem = new JRadioButtonMenuItem(defaultMT.getName());
		menuItem.setActionCommand(defaultMT.getName());
		menuItem.addActionListener(themeListener);
		group.add(menuItem);
		themesMenu.add(menuItem);

		/*
		if ("javax.swing.plaf.metal.MetalLookAndFeel".equals(selectedLookAndFeel))
		{
			menuItem.setSelected(true);
		}
*/

		// add menu item which allows to choose any theme pack.
		try {
			Class skinObject = Class.forName("com.l2fprod.gui.plaf.skin.Skin");
			JMenuItem dlgItem = new JMenuItem("Install another Look & Feel...");
			dlgItem.addActionListener(themeListener);
			dlgItem.setActionCommand(CHOOSE_LF_DLG);
			themesMenu.add(dlgItem);
		} catch (Exception ex) {
			System.out.println("Warning: If you want to be able to change L&F skins please add skinlf.jar to your CLASSPATH and restart SdaiEdit.");			
		}
		return menuBar;
	}


	protected void exit() {
		try {
			SdaiSession ses = SdaiSession.getSession();
			if (ses.isModified()) {
				SdaiTransaction tra = ses.getActiveTransaction();
				int result = JOptionPane.showConfirmDialog(frame, "Do you want to commit changes?",
						"The data was changed.", JOptionPane.YES_NO_CANCEL_OPTION);
				switch (result) {
					case JOptionPane.YES_OPTION:
						tra.commit();
						break;
					case JOptionPane.NO_OPTION:
						tra.abort();
						break;
					case JOptionPane.CANCEL_OPTION:
						return;
				}
			}
		}
		catch (SdaiException ex) {
			processMessage(ex);
		}
		sessionBean.getProperties(props);
		modelBean.getProperties(props);
		entityBean.getProperties(props);
		entityMapBean.getProperties(props);
		getProperties(props);
		saveProperties(props);
		bookCount--;
		frame.dispose();
		if (bookCount < 1) {
			synchronized(theTitle) { theTitle.notify(); }
		}
	}

	public String getLastUsedTheme() {
		return props.getProperty("theme-option");
	}

	public void setLastUsedTheme(String path2File) {
		if (path2File == null)
			props.setProperty("theme-option", "");
		else
			props.setProperty("theme-option", path2File);
		saveProperties(props);
	}

	public String getLastUsedDir() {
		return props.getProperty("lastDir", ".");
	}

	public void setLastUsedDir(File usedFile) {
		if (usedFile == null) {
			return;
		}
		try {
			if (usedFile.isFile()) {
				usedFile = usedFile.getParentFile();
			}
			String fileName = usedFile.getCanonicalPath();
			props.put("lastDir", fileName);
			saveProperties(props);
		} catch (Exception ex) {
			// eat exception, it's for debugging only.
			ex.printStackTrace();
		}
	}

	/**
	*	A convenience method to load custom theme.
	*	@return	Returns true if load of custom thema was successfull.
	*	@param	path2File		Path to file which contains theme definition (.zip file).
	*/
	private boolean loadCustomTheme(String path2File) {

			boolean status = true;
			try {
				Class aClass = Class.forName("com.l2fprod.gui.plaf.skin.Skin");
				Object skin = null;
				skin = com.l2fprod.gui.plaf.skin.SkinLookAndFeel.loadThemePack(path2File);

				if (skin != null) {
					aClass = Class.forName("com.l2fprod.gui.plaf.skin.SkinLookAndFeel");
					Object skinLookAndFeelInstance = aClass.newInstance();

					com.l2fprod.gui.plaf.skin.SkinLookAndFeel.setSkin((com.l2fprod.gui.plaf.skin.Skin)skin);
					UIManager.setLookAndFeel((LookAndFeel) skinLookAndFeelInstance);
					updateComponentLookAndFeel();
				}
				else {
					JOptionPane.showMessageDialog(frame,
							"Failed to load new Look & Feel from specified file ("+path2File+")!",
							theTitle,
							JOptionPane.ERROR_MESSAGE);
					status = false;
				}
			}
			catch (MalformedURLException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(frame,
							"Failed to load new Look & Feel from specified file ("+path2File+")!",
							theTitle,
							JOptionPane.ERROR_MESSAGE);
				status = false;
			}
			catch (UnsupportedLookAndFeelException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(frame,
							"Failed to load new Look & Feel from specified file ("+path2File+"): unsupported type of Look & Feel!",
							theTitle,
							JOptionPane.ERROR_MESSAGE);
				 status = false;
			}
			catch (ClassNotFoundException ex) {
				System.out.println("Warning: skin holder not found!");
				status = false;
			}
			catch (Exception ex) {
				JOptionPane.showMessageDialog(frame,
							"Failed to load new Look & Feel from specified file("+path2File+")! Maybe it's not a Look & Feel file?",
							theTitle,
							JOptionPane.ERROR_MESSAGE);
				// thrown by LF package
				ex.printStackTrace();
				status = false;
			}

			return status;
	}

	public void updateComponentLookAndFeel()
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SwingUtilities.updateComponentTreeUI(frame);
				repaint();
				validate();
				// a precaution step to tell all active frames to be updated
				// as well (some may get refreshed twice- but it's a problem
				// of Swing to protect against this).
				Frame[] all = Frame.getFrames();
				for (int i=0;i<all.length;i++) {
					SwingUtilities.updateComponentTreeUI(all[i]);

				}
			}
		});
	}

	class NavigationBar extends JPanel {
		public ButtonWithMenu bBackward;
		public ButtonWithMenu bForward;
//		public JButton bGo;
		public JButton bRefresh;
		public JButton bCopy;
		public JButton bNew;
		public JTextField position = new JTextField();

		void goBackward() {
			try {
				if (checkEditMode()) {
					if (currentChain != -1) {
						currentChain--;
						chainLock = !chainLock;
						ChainElement element = (ChainElement)chain.elementAt(currentChain);
						setValue(element);
						doGoingPossibilities();
						chainLock = !chainLock;
						position.setText(element.panel.getTreeLeave());
					}
				}
			}
			catch (SdaiException h) {
				processMessage(h);
			}
		}

		NavigationBar() {
			setLayout(new BorderLayout());
			JPanel panel = new JPanel(new FlowLayout());
//Bacward
			bBackward = new ButtonWithMenu(new ImageIcon(getClass().getResource("images/backward.gif")));
			bBackward.orentation = -1;
			bBackward.setEnabled(false);
			bBackward.setToolTipText("Backward");
			bBackward.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				    goBackward();
				}
			});
			panel.add(bBackward);
//Forward
			bForward = new ButtonWithMenu(new ImageIcon(getClass().getResource("images/forward.gif")));
			bForward.orentation = 1;
			bForward.setEnabled(false);
			bForward.setToolTipText("Forward");
			bForward.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						if (checkEditMode()) {
							if (currentChain != chain.size()-1) {
								currentChain++;
								chainLock = !chainLock;
								ChainElement element = (ChainElement)chain.elementAt(currentChain);
								setValue(element);
								doGoingPossibilities();
								chainLock = !chainLock;
								position.setText(element.panel.getTreeLeave());
							}
						}
					} catch (SdaiException h) {
						processMessage(h);
					}
				}
			});
			panel.add(bForward);
//Go
//			bGo = new JButton("Go");
//			bGo.setToolTipText("Go to instance");
//			bGo.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					doSelectionProcessed((SdaiSelectable)getSelectedComponent());
//				}
//			});
//			panel.add(bGo);
//New
			bNew = new JButton(new ImageIcon(getClass().getResource("images/new.gif")));
			bNew.setToolTipText("New SdaiEdit application");
			bNew.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						SdaiEdit.bookCount++;
					    SdaiEdit.class.newInstance();
					} catch(InstantiationException exc) {
					    processMessage(exc);
					} catch(IllegalAccessException exc) {
					    processMessage(exc);
					}
				}
			});
			panel.add(bNew);
//Refresh
			bRefresh = new JButton(new ImageIcon(getClass().getResource("images/refresh.gif")));
			bRefresh.setToolTipText("Refresh");
			bRefresh.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						SdaiPanel panelsanel = (SdaiPanel)getSelectedComponent();
						panelsanel.refreshData();
					} catch (Exception h) {
						processMessage(h);
					}
				}
			});
			panel.add(bRefresh);
//Copy all
			bCopy = new JButton(new ImageIcon(getClass().getResource("images/copy.gif")));
//			bCopy.setToolTipText("Copies all contents of current page");
                        bCopy.setToolTipText("Copy text");
			bCopy.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						SdaiPanel panelsanel = (SdaiPanel)getSelectedComponent();
						Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
						clip.setContents(new StringSelection(panelsanel.copyContentsAsText()), null);
					} catch (Exception h) {
						processMessage(h);
					}
				}
			});
			panel.add(bCopy);
			add(panel, BorderLayout.WEST);

			JPanel pp = new JPanel(new BorderLayout());
			pp.setBorder(new TitledBorder(""));
			pp.add(new JLabel("Location: "), BorderLayout.WEST);
			position.setBackground(this.getBackground());
			position.setForeground(this.getForeground());
			position.setEditable(false);
			position.setBorder(null);
			pp.add(position, BorderLayout.CENTER);
			add(pp, BorderLayout.CENTER);

		}
	}

	class LookAndFeelActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent aEvent)
		{
			String lfClassName = aEvent.getActionCommand();

			selectedLookAndFeel = lfClassName;

			try
			{
				UIManager.setLookAndFeel(lfClassName);
				updateComponentLookAndFeel();
			}
			catch(UnsupportedLookAndFeelException ulfExc)
			{
				//Ignore, this should never occure.
				ulfExc.printStackTrace();
			}
			catch(Exception exc)
			{
				exc.printStackTrace();
			}
			// if theme was applied successfully, then delete from cfg file the custom theme
			setLastUsedTheme(null);
		}
	}

	class ThemeActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent aEvent)
		{
			String themeName = aEvent.getActionCommand();
			if (CHOOSE_LF_DLG.equals(themeName)) {
				// open the FileOpen dialog to choose the right theme
				// pack. Then expect it to contain some predefined class
				// to be used to load look and feel.
				JFileChooser fc = new JFileChooser(getLastUsedDir());
				fc.setMultiSelectionEnabled(false);
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int status = fc.showOpenDialog(frame);
				if (status == JFileChooser.APPROVE_OPTION) {
					File selection = fc.getSelectedFile();
					if (selection == null)
						return;
					try {
						if (loadCustomTheme(selection.getCanonicalPath())) {
							// in this context we should report on success.
							JOptionPane.showMessageDialog(frame,
									"A new Look & Feel has been applied. However, it's strongly recommended to restart\n"+
									"the program to ensure all components are properly updated.",
									theTitle,
									JOptionPane.INFORMATION_MESSAGE);
							setLastUsedTheme(selection.getCanonicalPath());
							setLastUsedDir(selection);
						}
					}
					catch (IOException ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(frame,
									"Failed to load new Look & Feel because of file access problems.",
									theTitle,
									JOptionPane.ERROR_MESSAGE);
					}
				}
				return; // don't invoke default functionality- as user has visited this special option.
			}


			try
			{
				MetalLookAndFeel.setCurrentTheme(
					(MetalTheme)metalThemes.get(themeName));

				if ("javax.swing.plaf.metal.MetalLookAndFeel".equals(selectedLookAndFeel))
				{
					UIManager.setLookAndFeel(new MetalLookAndFeel());
					updateComponentLookAndFeel();
				}
				// if we are here, make sure we cleanup the theme property in the cfg file (as we use built-in themes- no chance to specify
				// external file). Notice that we are doing this only in case of successfull change of theme.
				setLastUsedTheme(null);
			}
			catch(Exception exc)
			{
				exc.printStackTrace();
			}

		}
	}

//	private void doSelectionProcessed(SdaiSelectable selectable) {
//		try {
//			frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//			if (!chainLock) {
//				repaint();
//				chainLock = !chainLock;
//				ChainElement element = getChain(selectable);
//				if (element != null) {
//					addChain(element);
//					setValue(element);
//					doGoingPossibilities();
//					navbar.position.setText(element.panel.getTreeLeave());
//				}
//				chainLock = !chainLock;
//			}
//		} catch (SdaiException ex) {
//			processMessage(ex);
//		} finally {
//			frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
//		}
//	}

	private void doTabPressed(SdaiPanel panel) throws SdaiException {
		if (!chainLock) {
			chainLock = !chainLock;
			ChainElement element = getChain(panel);
			addChain(element);
			doGoingPossibilities();
			chainLock = !chainLock;
			navbar.position.setText(panel.getTreeLeave());
		}
	}

	private void addChain(ChainElement element) throws SdaiException {
			currentChain++;
			chain.add(currentChain, element);
	}

	private ChainElement getChain(SdaiPanel panel) throws SdaiException {
		ChainElement element = new ChainElement();
		element.panel = panel;
		if (panel instanceof SdaiSessionBean) {
			element.values.add(((SdaiSessionBean)panel).getSession());
		} else if (panel instanceof SdaiRepositoryBean) {
			element.values.add(((SdaiRepositoryBean)panel).getRepository());
		} else if (panel instanceof SdaiModelBean) {
			element.values.add(((SdaiModelBean)panel).getModel());
		} else if (panel instanceof SchemaInstanceBean) {
			element.values.add(((SchemaInstanceBean)panel).getSchema());
		} else if (panel instanceof ExtentMappingBean) {
			element.values.add(((ExtentMappingBean)panel).getModels());
			element.values.add(((ExtentMappingBean)panel).getEntityMapping());
		} else if (panel instanceof EntityMappingBean) {
			element.values.add(((EntityMappingBean)panel).getInstance());
			element.values.add(((EntityMappingBean)panel).getMapping());
			element.values.add(((EntityMappingBean)panel).getDataDomain());
		} else if (panel instanceof AggregateMappingPage) {
			element.values.add(((AggregateMappingPage)panel).getInstances());
			element.values.add(((AggregateMappingPage)panel).getMapping());
			element.values.add(((AggregateMappingPage)panel).getDataDomain());
		} else if (panel instanceof MapEditEntitiesPanel ||
                   panel instanceof MapEditEntMappingsPanel ||
				   panel instanceof EntityExtentBean ||
				   panel instanceof BaseEntityBean || 
				   panel instanceof AggregateBean) {
			panel.pushChainElementValues(element.values);
		}
		return element;
	}

	private void setValue(ChainElement element) throws SdaiException {
		enshurePageEnabled(element.panel);
		if (element.panel instanceof SdaiSessionBean) {
			setSelectedIndex(indexOfTab(names[0]));
			((SdaiSessionBean)element.panel).setSession((SdaiSession)element.values.elementAt(0));
		} else if (element.panel instanceof SdaiRepositoryBean) {
			setSelectedIndex(indexOfTab(names[1]));
			((SdaiRepositoryBean)element.panel).setRepository((SdaiRepository)element.values.elementAt(0));
		} else if (element.panel instanceof SchemaInstanceBean) {
			setSelectedIndex(indexOfTab(names[2]));
			((SchemaInstanceBean)element.panel).setSchema((SchemaInstance)element.values.elementAt(0));
		} else if (element.panel instanceof SdaiModelBean) {
			doRefreshData = false;
            setSelectedIndex(indexOfTab(names[3]));
			doRefreshData = true;
			((SdaiModelBean)element.panel).setModel((SdaiModel)element.values.elementAt(0));
		} else if (element.panel instanceof EntityExtentBean) {
			setSelectedIndex(indexOfTab(names[4]));
			element.panel.popChainElementValues(element.values);
		} else if (element.panel instanceof BaseEntityBean) {
			setSelectedIndex(indexOfTab(names[5]));
			element.panel.popChainElementValues(element.values);
		} else if (element.panel instanceof AggregateBean) {
			setSelectedIndex(indexOfTab(names[6]));
			element.panel.popChainElementValues(element.values);
		} else if (element.panel instanceof ExtentMappingBean) {
			setSelectedIndex(indexOfTab(names[7]));
			((ExtentMappingBean)element.panel).setModels_and_definition((ASdaiModel)element.values.elementAt(0), (EEntity_mapping)element.values.elementAt(1));
		} else if (element.panel instanceof EntityMappingBean) {
			setSelectedIndex(indexOfTab(names[8]));
			((EntityMappingBean)element.panel).setInstanceAndMappingAndDomain((EEntity)element.values.elementAt(0),
				    (EEntity_mapping)element.values.elementAt(1), (ASdaiModel)element.values.elementAt(2));
		} else if (element.panel instanceof AggregateMappingPage) {
			setSelectedIndex(indexOfTab(names[9]));
			((AggregateMappingPage)element.panel).setMappingAndInstancesAndDomain((EEntity_mapping)element.values.elementAt(1),
				    (AEntity)element.values.elementAt(0), (ASdaiModel)element.values.elementAt(2));
		} else if (element.panel instanceof MapEditEntitiesPanel) {
			setSelectedIndex(indexOfTab(MAPPING_EDITOR_ENTITIES));
			element.panel.popChainElementValues(element.values);
		} else if (element.panel instanceof MapEditEntMappingsPanel) {
			setSelectedIndex(indexOfTab(MAPPING_EDITOR_ENTITY_MAPPINGS));
			element.panel.popChainElementValues(element.values);
		}
	}

	private void doGoingPossibilities() {
		navbar.bBackward.setEnabled(currentChain > 0);
		navbar.bForward.setEnabled(currentChain < chain.size()-1);
	}

	class ButtonWithMenu extends JPanel {
		StupidButton buttonMain;
		StupidButton buttonMenu = new StupidButton(new ImageIcon(getClass().getResource("images/submenu.gif")));

		JPopupMenu popup = new JPopupMenu();

		ButtonWithMenu(Icon i) {
			setLayout(new BorderLayout());
			buttonMain = new StupidButton(i);
			add(buttonMain, BorderLayout.CENTER);
			add(buttonMenu, BorderLayout.EAST);
			buttonMenu.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					showPopup();
				}
			});
			buttonMenu.setMargin(new Insets(0, 0, 0, 0));
		}

		final int menuItemCount = 10;

		int orentation = 0;

		public void setEnabled(boolean enable) {
			buttonMain.setEnabled(enable);
			buttonMenu.setEnabled(enable);
		}

		void showPopup() {
			popup.removeAll();
			JMenuItem menuItem;
			int i = 1;
			while ((i <= menuItemCount) && (chain.size() > currentChain+(orentation*i)) && (0 <= currentChain+(orentation*i))) {
				menuItem = new JMenuItem(((ChainElement)chain.elementAt(currentChain+(orentation*i))).toSdaiString());
				menuItem.addMouseListener(menuListener);
				menuItem.setFont(new Font(menuItem.getFont().getName(), Font.BOLD, 11));
				popup.add(menuItem);
				i++;
			}
			popup.show(buttonMenu, 0, buttonMenu.getHeight());
		}

		public void addActionListener(ActionListener al) {
			buttonMain.addActionListener(al);
		}

		MouseAdapter menuListener = new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				currentChain = currentChain+(orentation*popup.getComponentIndex((JMenuItem)e.getSource()));
				buttonMain.firefire();
			}
		};

		class StupidButton extends JButton {
			public StupidButton(Icon i) {
				super(i);
			}

			public void firefire() {
				fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Menu"));
			}
		}
	}

	class ChainElement {
		public SdaiPanel panel;
		public Vector values = new Vector();

		public ChainElement() {
		}

		public ChainElement(SdaiPanel panel, Object value) {
		    this.panel = panel;
			values.add(value);
		}

		public String toSdaiString() {
			String result = "";
			for (int i = 0; i < values.size(); i++) {
				result += SimpleOperations.getSdaiName(values.elementAt(i));
			}
			return result;
		}

		public String toString() {
			String result = "";
			result += panel.toString()+" %% ";
			for (int i = 0; i < values.size(); i++) {
				result += values.elementAt(i)+" @@ ";
			}
			return result;
		}
	}

	private boolean checkEditMode() throws SdaiException {
		SdaiPanel panel = (SdaiPanel)getComponentAt(lastTabIndex);
		if (panel.isEdit() && checkedit) {
			JOptionPane.showMessageDialog(this, "First you must go out from edit mode!", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		} else {
			return true;
		}
	}

	public void processMessage(Exception e) {
		e.printStackTrace();
		JOptionPane.showMessageDialog(this, e, "Error", JOptionPane.ERROR_MESSAGE);
	}


//	Leaved for new java version
	public class MyGlassPane extends JPanel {
		public MyGlassPane() {
			addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent e) {
//					System.out.println("KEY");
				}
			});
			addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
//					System.out.println("MOUSE");
				}
			});
			super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		}
	}

	private Properties takeProperties(Properties props) {
		try {
		    SdaiSession session = (SdaiSession.getSession() != null) ? SdaiSession.getSession() : SdaiSession.openSession();
			props = session.loadApplicationProperties(SdaiEdit.class);
		} catch (SdaiException ex) { processMessage(ex); }
		return props;
	}

	private void saveProperties(Properties props) {
		try {
			SdaiSession.getSession().storeApplicationProperties(SdaiEdit.class, props, "Generated by SdaiEdit. Don't edit by hand.");
		} catch (SdaiException ex) { processMessage(ex); }
	}

	void setProperties(Properties props) {
		String width = props.getProperty("frame.width");
		String height = props.getProperty("frame.height");
		frame.setSize((width != null)?Integer.parseInt(width):600,
                      (height != null)?Integer.parseInt(height):600);
		String top = props.getProperty("frame.top");
		String left = props.getProperty("frame.left");
		frame.setLocation((left != null)?Integer.parseInt(left):0, (top != null)?Integer.parseInt(top):0);
	}

	Properties getProperties(Properties props) {
        Dimension size = frame.getSize();
        Point location = frame.getLocationOnScreen();
        Dimension screenSize = frame.getToolkit().getScreenSize();
        if(size.width <= screenSize.width || size.height <= screenSize.height ||
           location.x >= 0 || location.y >= 0) {
            props.setProperty("frame.width", String.valueOf(size.width));
            props.setProperty("frame.height", String.valueOf(size.height));
            props.setProperty("frame.top", String.valueOf(location.y));
            props.setProperty("frame.left", String.valueOf(location.x));
        }
		/* FIXME: Uncomment this to get registration code back
		props.setProperty("main.license", license);
		props.setProperty("main.serial", serial);
		props.setProperty("main.key", key);*/
		return props;
	}

	public void resetProperties(Properties props) {
		setProperties(props);
		sessionBean.setProperties(props);
		schemaBean.setProperties(props);
		modelBean.setProperties(props);
		entityBean.setProperties(props);
		extentMapBean.setProperties(props);
		entityMapBean.setProperties(props);
		aggregateMapPage.setProperties(props);
	}

	public Properties retakeProperties(Properties props) {
		getProperties(props);
		sessionBean.getProperties(props);
		schemaBean.getProperties(props);
		modelBean.getProperties(props);
		entityBean.getProperties(props);
		extentMapBean.getProperties(props);
		entityMapBean.getProperties(props);
		aggregateMapPage.getProperties(props);
		return props;
	}

	void enshurePageEnabled(Component component) {
		int i = indexOfComponent(component);
		if (!isEnabledAt(i)) {
			setEnabledAt(i, true);
		}
	}

	GoListener goListener = new GoListener() {
		public void goPerformed(GoEvent e) {
			try {
				frame.getGlassPane().setVisible(true);

				Object source = e.getSource();
				String target = e.getTarget();
				Object value = e.getValue();

				if (target.equals("Repository")) {
					enshurePageEnabled(repositoryBean);
					repositoryBean.setRepository((SdaiRepository)value);
					setSelectedIndex(indexOfTab(names[1]));
				} else if (target.equals("SchemaInstance")) {
                    enshurePageEnabled(schemaBean);
					schemaBean.setSchema((SchemaInstance)value);
					setSelectedIndex(indexOfTab(names[2]));
                    if(mapEditEntitiesPanel != null &&
					   mapEditEntitiesPanel.setSchemaInstance((SchemaInstance)value))
							enshurePageEnabled(mapEditEntitiesPanel);
				} else if (target.equals("Model")) {
					enshurePageEnabled(modelBean);
					modelBean.setModel((SdaiModel)value);
					doRefreshData = false;
                    setSelectedIndex(indexOfTab(names[3]));
                    doRefreshData = true;
                    if(mapEditEntitiesPanel != null &&
					   mapEditEntitiesPanel.setModel((SdaiModel)value))
							enshurePageEnabled(mapEditEntitiesPanel);
				} else if (target.equals("EntityExtent")) {
                    enshurePageEnabled(extentBean);
					Object s[] = (Object[])value;
					extentBean.setExtentModelInstance((EEntity_definition)s[0], s[1]);
					setSelectedIndex(indexOfTab(names[4]));
				} else if (target.equals("EntityInstance")) {
					enshurePageEnabled(entityBean);
					Object s[] = (Object[])value;
					entityBean.setEntityAndModelInstance((EEntity)s[0], s[1]);
					setSelectedIndex(indexOfTab(names[5]));
					if (source == entityBean) {
						ChainElement ce = new ChainElement();
						ce.panel = entityBean;
						ce.values.add(s[0]);
						ce.values.add(s[1]);
						addChain(ce);
					}
				} else if (target.equals("Aggregate")) {
					enshurePageEnabled(aggregateBean);
					Object s[] = (Object[])value;
					aggregateBean.setInstance((EEntity)s[0]);
					aggregateBean.setAggregateAndDomain((Aggregate)s[1], s[2]);
					setSelectedIndex(indexOfTab(names[6]));
					if (source == aggregateBean) {
						ChainElement ce = new ChainElement();
						ce.panel = aggregateBean;
						ce.values.add(s[0]);
						ce.values.add(s[1]);
						ce.values.add(s[2]);
						addChain(ce);
					}
				} else if (target.equals("ExtentMapping")) {
					enshurePageEnabled(extentMapBean);
					Object s[] = (Object[])value;
					extentMapBean.setModels_and_definition((ASdaiModel)s[1], (EEntity_mapping)s[0]);
					setSelectedIndex(indexOfTab(names[7]));
				} else if (target.equals("InstanceMapping")) {
				   enshurePageEnabled(entityMapBean);
					Object s[] = (Object[])value;
					EEntity_mapping em = null;
					if (s[1] instanceof AEntity_mapping) {
						AEntity_mapping aem = (AEntity_mapping)s[1];
						if (aem.getMemberCount() > 0) {
							em = aem.getByIndex(1);
						}
					} else {
						em = (EEntity_mapping)s[1];
					}
					entityMapBean.setInstanceAndMappingAndDomain((EEntity)s[0], em, (ASdaiModel)s[2]);
					setSelectedIndex(indexOfTab(names[8]));
					if (source == entityMapBean) {
						ChainElement ce = new ChainElement();
						ce.panel = entityMapBean;
						ce.values.add(s[0]);
						ce.values.add(em);
						ce.values.add(s[2]);
						addChain(ce);
					}
				} else if (target.equals("AggregateMapping")) {
					enshurePageEnabled(aggregateMapPage);
					Object s[] = (Object[])value;
					EEntity_mapping em = (EEntity_mapping)s[1];
					aggregateMapPage.setMappingAndInstancesAndDomain(em, ((s[0] instanceof AEntity)?(AEntity)s[0]:SimpleOperations.arrayToAEntity(((Collection)s[0]).toArray())), (ASdaiModel)s[2]);
					setSelectedIndex(indexOfTab(names[9]));
//					addChain(new ChainElement(aggregateBean, value));
				} else if (target.equals(MAPPING_EDITOR_ENTITY_MAPPINGS)) {
					enshurePageEnabled(mapEditEntMappingsPanel);
                    MapEditEntityInfo entityInfo = (MapEditEntityInfo)value;
                    MapEditEntitiesPanel sourceMapEdit = (MapEditEntitiesPanel)source;
                    mapEditEntMappingsPanel.setMapEditEntitiesPanel(sourceMapEdit);
					mapEditEntMappingsPanel.setEntityInfo(entityInfo);
					setSelectedIndex(indexOfTab(MAPPING_EDITOR_ENTITY_MAPPINGS));
				} else if (target.equals("Hell")) {
                    setEnabledAt(5, false);
                    if(getSelectedIndex() == 5)
                        navbar.goBackward();
				} else {
					Debug.println("Unknown target page : "+target.toString());
				}
			} catch (SdaiException ex) {
				processMessage(ex);
			} finally {
				frame.getGlassPane().setVisible(false);
			}

		}
        
	};

}
