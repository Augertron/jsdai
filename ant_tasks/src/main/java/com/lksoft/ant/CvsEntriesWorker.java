/*
 * LKSoft Baltic UAB
 * @author Viktoras Kovaliovas
 * Created on Mar 15, 2005 7:17:00 PM
 */
package com.lksoft.ant;

import java.io.*;
import java.util.*;

public class CvsEntriesWorker {

  private CvsEntriesWorker() {
  }

  public static void writeEntries(File output, Map entries) throws IOException {

    PrintWriter writer = null;
    try {
      writer = new PrintWriter(new BufferedWriter(new FileWriter(output)));
      for (Iterator i = entries.keySet().iterator(); i.hasNext(); ) {
        String file = (String) i.next();
        String version = (String) entries.get(file);
        writer.print(file);
        writer.print(',');
        writer.println(version);
      }
    }
    finally {
      if (writer != null) {
        writer.close();
      }
    }
  }

  public static Map<String, String> readEntries(File input) throws IOException {

    Map<String, String> entries = new HashMap<String, String>();

    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(input));
      String s;
      while ((s = reader.readLine()) != null) {
        StringTokenizer st = new StringTokenizer(s, ",");
        if (st.countTokens() != 2) {
          continue;
        }

        String file = st.nextToken();
        String version = st.nextToken();

        entries.put(file, version);
      }
    }
    finally {
      if (reader != null) {
        reader.close();
      }
    }

    return entries;
  }

  public static void getEntries(TopDir dir, Map<String, String> entries) {
    File base = new File(dir.getBase(), dir.getModule());
    getEntries(base, dir.getModule() + ":", entries);
  }

  private static void getEntries(File dir, String dirRelative, Map<String, String> entries) {
    File[] files = dir.listFiles(CvsDirFilter.getInstance());
    if (files == null || files.length == 0 || files.length > 1) {
      return;
    }

    File cvsDir = files[0];
    files = cvsDir.listFiles(EntriesFileFilter.getInstance());
    if (files.length == 0 || files.length > 1) {
      return;
    }

    File entriesFile = files[0];
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(entriesFile));
      String s;
      while ((s = reader.readLine()) != null) {
        if (s.length() < 1) {
          continue;
        }

        char type = s.charAt(0);
        // process file
        if (type == '/') {
          StringTokenizer st = new StringTokenizer(s, "/");
          if (st.countTokens() < 2) {
            continue;
          }

          String name = st.nextToken();
          String version = st.nextToken();

          entries.put(dirRelative + name, version);
        }
        // process sub dir
        else if (type == 'D') {
          StringTokenizer st = new StringTokenizer(s, "/");
          if (st.countTokens() < 2) {
            continue;
          }

          st.nextToken(); // skip D
          File subdir = new File(dir, st.nextToken());
          getEntries(subdir, dirRelative + subdir.getName() + "/", entries);
        }
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    finally {
      try {
        if (reader != null) {
          reader.close();
        }
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private static class EntriesFileFilter implements FileFilter {

    private static FileFilter instance;

    public static FileFilter getInstance() {
      if (instance == null) {
        instance = new EntriesFileFilter();
      }

      return instance;
    }

    private EntriesFileFilter() {
    }

    @Override
    public boolean accept(File file) {
      return !file.isDirectory() && file.getName().equals("Entries");
    }
  }

  private static class CvsDirFilter implements FileFilter {

    private static FileFilter instance;

    public static FileFilter getInstance() {
      if (instance == null) {
        instance = new CvsDirFilter();
      }

      return instance;
    }

    private CvsDirFilter() {
    }

    @Override
    public boolean accept(File file) {
      return file.isDirectory() && file.getName().equals("CVS");
    }
  }
}
