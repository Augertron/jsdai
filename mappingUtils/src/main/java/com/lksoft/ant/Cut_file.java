/* Cut_file.java
 *
 * Created on February 17, 2003, 3:05 PM
 */
package com.lksoft.ant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * @author Raimundas Vedegys
 */

public class Cut_file extends Task {
  private File in_file_name = null;
  private File out_file_name = null;

  public void setInputfile(File file) {
    this.in_file_name = file;
  }

  public void setOutputfile(File file) {
    this.out_file_name = file;
  }

  // The method executing the task
  public void execute() throws BuildException {
    String buff;
    BufferedReader in_file;
    PrintWriter out_file;
    try {
      in_file = new BufferedReader(new FileReader(in_file_name));
      out_file = new PrintWriter(new FileWriter(out_file_name));
      while ((buff = in_file.readLine()) != null) {
        out_file.println(buff);
        if (buff.equalsIgnoreCase("END_SCHEMA;")) {
          break;
        }
      }
      out_file.close();
      in_file.close();
    }
    catch (FileNotFoundException ex1) {
    }
    catch (IOException ex2) {
    }
  }
}
