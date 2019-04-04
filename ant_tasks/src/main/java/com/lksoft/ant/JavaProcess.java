/*
 * $Id$
 *
 * Copyright (C) LKSoftWare GmbH, 2002. All Rights Reserved.
 *
 */

package com.lksoft.ant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.ExecuteWatchdog;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.taskdefs.PumpStreamHandler;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.Environment;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

/**
 * Launcher for Java applications that can be killed by kill-process task.
 *
 * @author Vaidas Nargelas
 */
public class JavaProcess extends Task {

  private ProcessExecute processExecute;

  public Process getProcess() {
    return processExecute != null ? processExecute.getProcess() : null;
  }

  private CommandlineJava cmdl = new CommandlineJava();
  private Environment env = new Environment();
  private boolean newEnvironment = false;
  private File dir = null;
  private File out;
  private PrintStream outStream = null;
  private boolean failOnError = false;
  private boolean append = false;
  private Long timeout = null;

  /**
   * Do the execution.
   */
  @Override
  public void execute() throws BuildException {
    File savedDir = dir;

    int err = -1;
    try {
      if ((err = executeJava()) != 0) {
        if (failOnError) {
          throw new BuildException("Java returned: " + err, location);
        }
        else {
          log("Java Result: " + err, Project.MSG_ERR);
        }
      }
    }
    finally {
      dir = savedDir;
    }
  }

  /**
   * Do the execution and return a return code.
   *
   * @return the return code from the execute java class if it was
   * executed in a separate VM (fork = "yes").
   */
  public int executeJava() throws BuildException {
    String classname = cmdl.getClassname();
    if (classname == null && cmdl.getJar() == null) {
      throw new BuildException("Classname must not be null.");
    }

    log(cmdl.describeCommand(), Project.MSG_VERBOSE);

    try {
      return run(cmdl.getCommandline());
    }
    catch (BuildException e) {
      if (failOnError) {
        throw e;
      }
      else {
        log(e.getMessage(), Project.MSG_ERR);
        return 0;
      }
    }
    catch (Throwable t) {
      if (failOnError) {
        throw new BuildException(t);
      }
      else {
        log(t.getMessage(), Project.MSG_ERR);
        return 0;
      }
    }
  }

  /**
   * Set the classpath to be used when running the Java class
   *
   * @param s an Ant Path object containing the classpath.
   */
  public void setClasspath(Path s) {
    createClasspath().append(s);
  }

  /**
   * Adds a path to the classpath.
   */
  public Path createClasspath() {
    return cmdl.createClasspath(project).createPath();
  }

  /**
   * Classpath to use, by reference.
   */
  public void setClasspathRef(Reference r) {
    createClasspath().setRefid(r);
  }

  /**
   * The location of the JAR file to execute.
   */
  public void setJar(File jarfile) throws BuildException {
    if (cmdl.getClassname() != null) {
      throw new BuildException("Cannot use 'jar' and 'classname' " + "attributes in same command.");
    }
    cmdl.setJar(jarfile.getAbsolutePath());
  }

  /**
   * Sets the Java class to execute.
   */
  public void setClassname(String s) throws BuildException {
    if (cmdl.getJar() != null) {
      throw new BuildException("Cannot use 'jar' and 'classname' " + "attributes in same command");
    }
    cmdl.setClassname(s);
  }

  /**
   * Deprecated: use nested arg instead.
   * Set the command line arguments for the class.
   *
   * @ant.attribute ignore="true"
   */
  public void setArgs(String s) {
    log("The args attribute is deprecated. " + "Please use nested arg elements.", Project.MSG_WARN);
    cmdl.createArgument().setLine(s);
  }

  /**
   * Adds a command-line argument.
   */
  public Commandline.Argument createArg() {
    return cmdl.createArgument();
  }

  /**
   * Can be only true. Used for backward compatiblity.
   */
  public void setFork(boolean s) throws BuildException {
    if (!s) {
      throw new BuildException("fork cannot be false");
    }
  }

  /**
   * Set the command line arguments for the JVM.
   */
  public void setJvmargs(String s) {
    log("The jvmargs attribute is deprecated. " + "Please use nested jvmarg elements.", Project.MSG_WARN);
    cmdl.createVmArgument().setLine(s);
  }

  /**
   * Adds a JVM argument.
   */
  public Commandline.Argument createJvmarg() {
    return cmdl.createVmArgument();
  }

  /**
   * Set the command used to start the VM
   */
  public void setJvm(String s) {
    cmdl.setVm(s);
  }

  /**
   * Adds a system property.
   */
  public void addSysproperty(Environment.Variable sysp) {
    cmdl.addSysproperty(sysp);
  }

  /**
   * If true, then fail if the command exits with a
   * returncode other than 0
   */
  public void setFailonerror(boolean fail) {
    failOnError = fail;
  }

  /**
   * The working directory of the process
   */
  public void setDir(File d) {
    this.dir = d;
  }

  /**
   * File the output of the process is redirected to.
   */
  public void setOutput(File out) {
    this.out = out;
  }

  /**
   * Corresponds to -mx or -Xmx depending on VM version.
   */
  public void setMaxmemory(String max) {
    cmdl.setMaxmemory(max);
  }

  /**
   * Sets the JVM version.
   *
   * @param value JVM version
   */
  public void setJVMVersion(String value) {
    cmdl.setVmversion(value);
  }

  /**
   * Adds an environment variable.
   *
   * <p>
   * Will be ignored if we are not forking a new VM.
   *
   * @since Ant 1.5
   */
  public void addEnv(Environment.Variable var) {
    env.addVariable(var);
  }

  /**
   * If true, use a completely new environment.
   *
   * <p>
   * Will be ignored if we are not forking a new VM.
   *
   * @since Ant 1.5
   */
  public void setNewenvironment(boolean newenv) {
    newEnvironment = newenv;
  }

  /**
   * If true, append output to existing file.
   *
   * @since Ant 1.5
   */
  public void setAppend(boolean append) {
    this.append = append;
  }

  /**
   * Timeout in milliseconds after which the process will be killed.
   *
   * @since Ant 1.5
   */
  public void setTimeout(Long value) {
    timeout = value;
  }

  /**
   * Pass output sent to System.out to specified output file.
   *
   * @since Ant 1.5
   */
  @Override
  protected void handleOutput(String line) {
    if (outStream != null) {
      outStream.println(line);
    }
    else {
      super.handleOutput(line);
    }
  }

  /**
   * Pass output sent to System.out to specified output file.
   *
   * @since Ant 1.5.2
   */
  @Override
  protected void handleFlush(String line) {
    if (outStream != null) {
      outStream.print(line);
    }
    else {
      super.handleFlush(line);
    }
  }

  /**
   * Pass output sent to System.err to specified output file.
   *
   * @since Ant 1.5
   */
  @Override
  protected void handleErrorOutput(String line) {
    if (outStream != null) {
      outStream.println(line);
    }
    else {
      super.handleErrorOutput(line);
    }
  }

  /**
   * Pass output sent to System.err to specified output file.
   *
   * @since Ant 1.5.2
   */
  @Override
  protected void handleErrorFlush(String line) {
    if (outStream != null) {
      outStream.println(line);
    }
    else {
      super.handleErrorOutput(line);
    }
  }

  /**
   * Executes the given classname with the given arguments in a separate VM.
   */
  private int run(String[] command) throws BuildException {
    FileOutputStream fos = null;
    try {
      processExecute = null;
      if (out == null) {
        processExecute = new ProcessExecute(new LogStreamHandler(this, Project.MSG_INFO, Project.MSG_WARN), createWatchdog());
      }
      else {
        fos = new FileOutputStream(out.getAbsolutePath(), append);
        processExecute = new ProcessExecute(new PumpStreamHandler(fos), createWatchdog());
      }

      processExecute.setAntRun(project);

      if (dir == null) {
        dir = project.getBaseDir();
      }
      else if (!dir.exists() || !dir.isDirectory()) {
        throw new BuildException(dir.getAbsolutePath() + " is not a valid directory", location);
      }

      processExecute.setWorkingDirectory(dir);

      String[] environment = env.getVariables();
      if (environment != null) {
        for (int i = 0; i < environment.length; i++) {
          log("Setting environment variable: " + environment[i], Project.MSG_VERBOSE);
        }
      }
      processExecute.setNewenvironment(newEnvironment);
      processExecute.setEnvironment(environment);

      processExecute.setCommandline(command);
      try {
        int rc = processExecute.execute();
        if (processExecute.killedProcess()) {
          log("Timeout: killed the sub-process", Project.MSG_WARN);
        }
        return rc;
      }
      catch (IOException e) {
        throw new BuildException(e, location);
      }
    }
    catch (IOException io) {
      throw new BuildException(io, location);
    }
    finally {
      if (fos != null) {
        try {
          fos.close();
        }
        catch (IOException io) {
        }
      }
      processExecute = null;
    }
  }

  /**
   * Clear out the arguments to this java task.
   */
  public void clearArgs() {
    cmdl.clearJavaArgs();
  }

  /**
   * Create the Watchdog to kill a runaway process.
   *
   * @since Ant 1.5
   */
  protected ExecuteWatchdog createWatchdog() throws BuildException {
    if (timeout == null) {
      return null;
    }
    return new ExecuteWatchdog(timeout.longValue());
  }

}
