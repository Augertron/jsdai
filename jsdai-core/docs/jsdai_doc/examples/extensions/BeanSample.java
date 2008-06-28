// BeanSample.java
// Copyright (c) LKSoftWare GmbH, 2000-2003. All Rights Reserved.
// This software is provided "AS IS," without any warranty

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import jsdai.lang.*;

import jsdaix.pdm_beans.*;
import jsdaix.look_feel.*;
/**
* JSDAI BeanSample shows the simple usage of pdm_beans.
*/

public class BeanSample extends JFrame implements WindowListener
{
    /**
    * PdmBase is the base class that provides
    * other classes with the working model and notifies 
    * all registered SdaiDomain listeners when working model changes.
    */
    PdmBase          theBase;

    PersonList       theList;
    PersonInfo       theInfo;

    /**
    * We need this page, because it provides 
    * default working model.
    */
    SessionPage      theModels;

    /**
    * Adaptor class that will route events from PersonList to
    * PersonInfo.
    */
    PdmAdapter       thePdmAdapter;

/**
*  Public constructor
*/
    public BeanSample() throws SdaiException
    {
       super("JSDAI Beans sample");
       setFont(new Font("Dialog", Font.PLAIN, 10));
       addWindowListener(this);

       JDialog theListDlg = new JDialog( this, "Person list"); 
       JDialog theInfoDlg = new JDialog( this, "Person info"); 
         
       /**
       * SessionPage is the 'must have' component in any application
       * using pdm beans. It allows to manipulate repositories and 
       * models and provides other classes with working model.
       */

       theModels = new SessionPage(this);

       /**
       * Create PersonList
       */
       theList = new PersonList();

       /**
       * Create PersonInfo
       */
       theInfo = new PersonInfo();

       /**
       * Retrieve the PdmBase from InfoPage
       */
       theBase = theModels.getBase();

       /**
       * Add listeners to PdmBase
       */
       theBase.addSdaiDomainListener(theList);
       theBase.addSdaiDomainListener(theInfo);

       /**
       * Create adaptor
       */
       thePdmAdapter = new PdmAdapter();

       /** 
       * Register listeners, so that 
       * the list knows about chages in the Info page
       * and the Info page knows about currently selected list item.
       */
       theList.addPdmListener(thePdmAdapter);
       theInfo.addPdmChangeListener(theList);

       /* Set up user interface */
       getContentPane().add(theModels);
       theListDlg.getContentPane().add(theList);
       theInfoDlg.getContentPane().add(theInfo);

       setSize(350,450);
       theListDlg.setSize(theListDlg.getPreferredSize());
       theListDlg.setLocation (100,100);
       theInfoDlg.setSize(theListDlg.getPreferredSize());
       theInfoDlg.setLocation (100+theListDlg.getPreferredSize().width,100);

       show();
       theListDlg.show();
       theInfoDlg.show();
    }

    /**
    * Window listener overrides.
    */
    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
      System.exit(0);
      e.getWindow().dispose();
    }

    public void windowClosed(WindowEvent e) {
            System.exit(0);
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    /**
    * Adaptor class used to pass selection events from PersonList 
    * to PersonInfo.
    * NOTE : this class is automaticaly created by IDE 
    * that supports beans.
    */
    class PdmAdapter implements PdmListener
    {
       public void pdmObjectChanged(PdmEvent aEvent) 
       {
          theInfo.setPdmObject(aEvent);
       }
    }

    /**
    * This is ordinary Main.
    */
    public static final void main(String argv[]) throws
        SdaiException, 
        ClassNotFoundException,
        NoSuchMethodException,
        IOException
    {
       try
       {
          /**
          * Set up Look and Feel 
          */
          MetalLookAndFeel.setCurrentTheme( new JSdaiTheme());
          UIManager.setLookAndFeel(new MetalLookAndFeel());
          JSdaiLookAndFeel.installLookAndFeel();
       }
       catch (Exception exc)
       {
          System.err.println("Error loading L&F: " + exc);
       }

       BeanSample aFrame = new BeanSample();
    }
}
