package org.astrogrid.common.wrapper;

import java.util.*;
import java.io.*;
import org.astrogrid.tools.ascii.*;
import org.astrogrid.log.*;

import org.astrogrid.tools.io.*;
import org.astrogrid.tools.votable.*;

/**
 * ProgramExecutor - a class for conveniently wrapping calls to 3rd party
 * programs. In practice each 3rd party program will probably be very
 * different, so this class is provided as an example with a few 'helper'
 * methods.
 * It would be intended that one of these instances corresponds to one
 * call to a program, and it is then disposed of afterwards.
 * I've not checked this for thread safety yet.
 *
 * NB - the program output stream(s).  You *must* keep reading these streams
 * or the process may sieze.  What we do here is to start a separate thread
 * that reads both streams and writes to the outputSplitter, which has no
 * forwarding streams.  You can then
 * add outher streams to that if need be (eg a PipedStream pair so that you
 * could read the data)
 *
 * Program --> process.getInputStream ---> StreamPiper  ---> OutputSplitter
 *           & process.getErrorStream ---> StreamPiper  -----^

 * @author M Hill
 */
public class ProgramExecutor
{
   private String workingDir = null;
   //private String tempPrefix = null;   //prefix to be used for all temporary files
   private String programPath = null;  //full path to the 3rd party application
   private String[] programArgs = null;  //array of arguments for the 3rd party app
   //private String cmdLine = null;      //full command line used to start program

   private OutputStreamSplitter outputSplitter = null; //a 'manifold' to plug in to, to pipe the program output
   private AsciiOutputStream programInput = null;  //a handle on writing to the programs std in
   private StreamPiper procErrorPiper = null;   //a thread that directs the program err stream to the output manifold
   private StreamPiper procOutPiper = null;//a thread that directs the program out stream to the output manifold

   private Process process = null;

   /**
    * Constructor - specify the path to the program to execute,
    * the path to where temporary files should be created (& destroyed), and the
    * path and prefix to where output files should be left.
    * For example, the output prefix might be "/home/mch/output/MCH_" which
    * will store the output files in /home/mch/output with the prefix MCH_
    */
   public ProgramExecutor(String givenProgramPath, String workingDir)
   {
      this.programPath = givenProgramPath;

      //org.astrogrid.log.Log.affirm(new File(programPath).exists(), "Program '"+programPath+"' does not exist"); sometimes this returns false even when it is there...
      org.astrogrid.log.Log.affirm(new File(workingDir).exists(), "Working Directory '"+workingDir+"' does not exist");

      this.workingDir = workingDir;
//      this.tempPrefix = tempPrefix;

      //create this now, so that users of this class can plug in their monitors
      //etc to the output before starting the program.
      outputSplitter = new OutputStreamSplitter();

   }

   /** Used to prefix temporary file names, directories, etc so that instances
    * of this service do not interfere with other instances.  This prefix
    * applies to all files that do not outlast an instance of this class.
    */
//   protected String getTempPrefix()   {      return tempPrefix;   }

   /** the directory in which the program will run
    */
   protected String getWorkingDirectory()   {      return workingDir;   }

   /** the directory in which the program will run
    */
   public void setWorkingDirectory(String dir)   { workingDir = dir;   }

   /**
    * Program filename
    */
   protected String getProgramFilename()  {      return new File(programPath).getName();   }

   /**
    * path to the 3rd party program to run to carry out service
    */
   protected String getProgramPath()  {      return programPath;   }

   /** String for the user giving current status for long-running
    * applications.  May be called by other threads.
    */
   public synchronized String getStatus()
   {
      if (process == null)
         return "Ready";
      else
         return "Running";
   }

   /**
    * Returns a 'splitter' stream that all the program output (std err and
    * std out) goes through.
    * Other streams can then be 'plugged' in to this using addStream(), for
    * example if you want to log all the output.
    */
   protected OutputStreamSplitter getProgramOutput()   { return outputSplitter; }

   /**
    * Returns a stream that can be used to write to the program via
    * std in.
    */
   protected AsciiOutputStream getProgramInput()   { return programInput; }

   /**
    * returns Command Line
    */
   protected String getCommandLine()
   {
      String cmdLine = programPath;
      if (programArgs != null)
      {
         for (int a=0;a<programArgs.length;a++)
         {
            cmdLine = cmdLine + " "+programArgs[a];
         }
      }
      return cmdLine;
   }

   /**
    * Sets the command line arguments.  Could do with an addArg too...
    */
   public void setArgs(String[] args)
   {
      programArgs = args;
   }

   /**
    * Starts the 3rd party program that will carry out
    * the service, waits for it to complete, then tidies up afterwards.  making
    * it final prevents accidents in subclasses where this is overriden - instead
    * startProgram() etc should be overridden.
    */
   public final void runProgram() throws IOException
   {
      startProgram();
      waitForProgram();
      endProgram();
   }

   /**
    * Starts the 3rd party program that will carry out
    * the service - cmdLine must have been set.
    */
   public Process startProgram() throws IOException
   {
      Log.affirm(process == null,
                 "Cannot start program "+this+" if process is already running");

      Log.trace("Starting program '"+getCommandLine()+"'...");

      //start program in working directory
      Runtime runtime = Runtime.getRuntime();
      process = runtime.exec(getCommandLine(), null, new File(workingDir));

      //redirect process outputs to programOutput
      //wrapping process in/err streams in ascii is not good enough, as
      //if they are not read the program can hang.

      //set up active pipes that read from the program std out and err files
      //and write to the splitter.  These need to be reasonably high priority
      //so that the output is dealt with timely.  We really ought to have
      //proper waitFor stuff though.
      procErrorPiper = new StreamPiper(getProgramFilename()+" err",  process.getErrorStream(), outputSplitter);
      procOutPiper = new StreamPiper(getProgramFilename()+" out", process.getInputStream(), outputSplitter);

//      procErrorPiper = new StreamPiper(getProgramFilename()+" err", process.getErrorStream(), System.out);
//      procOutPiper = new StreamPiper(getProgramFilename()+" out", process.getInputStream(), System.out);

      procErrorPiper.setPriority(Thread.currentThread().getPriority()+1);
      procOutPiper.setPriority(Thread.currentThread().getPriority()+1);

      //register the std in
      programInput = new AsciiOutputStream(process.getOutputStream());


      return process;
   }

   /**
    * Waits for the program to complete; throws IOException if some kind of
    * termination problem occurs.
    */
   public void waitForProgram() throws IOException
   {
      Log.affirm(process != null,
                 "Cannot wait for program "+this+" if process is not yet running");
      try
      {
         Log.trace("...waiting for program to complete...");

         //wait for program to complete
         int exitValue = process.waitFor();        //wait until finished

         Log.trace("...program '"+getProgramFilename()+"' completed, exit code "+exitValue);

         if (exitValue != 0)
         {
            throw new IOException("Program ["+getProgramFilename()+"] failed, exit code="+exitValue);
         }

      }
      catch (InterruptedException ie)
      {
         //don't know how this could happen, but still
         throw new IOException("Program ["+getProgramFilename()+"] execution interrupted: "+ie);
      }

   }

   /** tidies up.  See also terminate program
    */
   public void endProgram() throws IOException
   {
      Log.affirm(process != null,
                 "Cannot end program "+this+" if process is not yet running");

      programInput = null;
      procErrorPiper.terminate();
      procOutPiper.terminate();
      process = null;
   }

   /** stops the process and tidies up.  NB = may be called by a different
    * thread.
    */
   public void terminateProgram() throws IOException
   {
      process.destroy();
      endProgram();
   }


   /** Returns a human-readable string describing this instance.  In this case,
    * the name of the program
    */
   public String toString()
   {
      return getProgramFilename();
   }

   /** A convenience routine that creates an AsciiInputStream of the program
    * output, so that the output can be read as the program writes it.
    * not tested.
    */
   public AsciiInputStream makeAsciiProgramOutput() throws IOException
   {
      //catch output in a pipe
      PipedOutputStream pipedOut = new PipedOutputStream();
      outputSplitter.addStream(pipedOut);

      // and provide a handle to the other end of the pipe for reading
      return new AsciiInputStream(new PipedInputStream(pipedOut));
   }

}

