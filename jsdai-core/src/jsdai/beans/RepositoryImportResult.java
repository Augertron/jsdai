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

package jsdai.beans;

import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;

import javax.swing.*;
import java.lang.*;
import java.io.*;
import java.util.*;
import jsdai.lang.*;
import jsdai.dictionary.*;

public class RepositoryImportResult extends JFrame
{
   JTextArea list;
   JButton bOk;

   JButton bCopyAll;

   SdaiRepository theRepository;

   Clipboard clipboard;

   public RepositoryImportResult()
   {
      super("Repository import results");

      clipboard = getToolkit().getSystemClipboard();

      JPanel mainPanel = new JPanel();
      mainPanel.setLayout(new BorderLayout());

      list = new JTextArea();
      list.setEditable(false);


      addKeyListener(new AreaKeyListener());

      mainPanel.add(new JScrollPane(list), BorderLayout.CENTER);

      JPanel pBar = new JPanel();
      pBar.setLayout(new BoxLayout(pBar, BoxLayout.X_AXIS));

      bOk = new JButton("Ok");
      bCopyAll = new JButton("Copy-All");

      bOk.setDefaultCapable(true);
      bOk.setEnabled(false);
      bOk.addActionListener(new ActionListener()
      {
            public void actionPerformed(ActionEvent e)
            {
               dispose();
            }
      });

      bCopyAll.setEnabled(false);
      bCopyAll.addActionListener(new ActionListener()
      {
            public void actionPerformed(ActionEvent e)
            {
               copyAllToClipboard();
            }
      });

      pBar.add(Box.createHorizontalGlue());
      pBar.add(bOk);
      pBar.add(Box.createRigidArea(new Dimension(5, 0)));
      pBar.add(bCopyAll);
      pBar.add(Box.createHorizontalGlue());

      mainPanel.add(pBar, BorderLayout.SOUTH);

      getContentPane().add(mainPanel);
      setSize(400, 400);
      show();
   }

   private void copyAllToClipboard()
   {
      StringSelection selection =
         new StringSelection(list.getText());

      clipboard.setContents(selection, selection);
   }

   public SdaiRepository importRepo(String repoName, String file, String location)
   {
      PrintWriter old_writer = SdaiSession.getLogWriter();

      CaptureOutputStream buffer = new CaptureOutputStream();

      PrintWriter writer = new PrintWriter(buffer, true);
      SdaiSession.setLogWriter(writer);
      try
      {
         writer.println("Importing file: "+file);
         theRepository = SdaiSession.getSession().importClearTextEncoding(repoName, file, location);
         writer.println("Repository \""+theRepository.getName()+"\" imported succesfully.");
         writer.println("Creation time: "+theRepository.getChangeDate());

         ASdaiModel models = theRepository.getModels();
         SdaiIterator it_models= models.createIterator();
         int instance_count = 0;
         HashSet schemas = new HashSet();
         while (it_models.next())
         {
            SdaiModel model = models.getCurrentMember(it_models);
            schemas.add(model.getUnderlyingSchema());
            instance_count += model.getInstanceCount();
         }
         Iterator it_schemas = schemas.iterator();
         writer.println("Schemas:");
         while (it_schemas.hasNext())
         {
            ESchema_definition schema = (ESchema_definition)it_schemas.next();
            writer.println(" - "+schema.getName(null));
         }
         writer.println("# of data sections(models): "+models.getMemberCount());
         writer.println("# of instances: "+instance_count);

      }
      catch (SdaiException e)
      {
         writer.println("Error: "+e.getMessage());
      }
      catch(Exception e)
      {
         e.printStackTrace();
         writer.println("Error: "+e.getMessage());
      }
      finally
      {
         bOk.setEnabled(true);
         bCopyAll.setEnabled(true);

         SdaiSession.setLogWriter(old_writer);
      }
		return theRepository;
   }

   public SdaiRepository getSdaiRepository()
   {
      return theRepository;
   }

   private void write(String s)
   {
      list.append(s);
      update(getGraphics());
   }

   private class CaptureOutputStream extends OutputStream
   {
      public void write(byte[] b)
      {
         RepositoryImportResult.this.write(String.valueOf(b));
      }
      public void write(byte[] b, int off, int len)
      {
         RepositoryImportResult.this.write((new String(b, off, len)).toString());
      }
      public void write(int b)
      {
         RepositoryImportResult.this.write(String.valueOf(b));
      }
   }

   private class AreaKeyListener implements KeyListener
   {
      public void keyPressed(KeyEvent e)
      {
        if ((e.getModifiers() & InputEvent.CTRL_MASK) != 0 &&
             e.getKeyCode() == KeyEvent.VK_INSERT
           )
        {
           StringSelection selection =
              new StringSelection(list.getSelectedText());

           clipboard.setContents(selection, selection);
        }
      }
      public void keyReleased(KeyEvent e)
      {
      }

      public void keyTyped(KeyEvent e)
      {
      }
   }
}