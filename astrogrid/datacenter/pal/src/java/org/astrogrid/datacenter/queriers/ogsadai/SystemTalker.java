package org.astrogrid.datacenter.queriers.ogsadai;

import java.io.*;
import java.util.*;

/**
 * A utility class to enable running external shell commands 
 * from within a JVM, and to capture their stdout and stderr streams.
 *
 * @author K Andrews
 */
public class SystemTalker {

  private TalkResult outputResult;
  private boolean outputFinished;
  private boolean errorFinished;
  private boolean gotError;

  /* (non-javadoc) 
   * Internal class allowing harvesting of output streams 
   */
  class StreamGobbler extends Thread {
    InputStream is;
    String type;
    SystemTalker systemTalker;

    public StreamGobbler(InputStream is, String type, SystemTalker s) {
      this.is = is;
      this.type = type;
      systemTalker = s;
    }

    public void run() {
      try {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        String retString = "";
        while ( (line = br.readLine()) != null) {
          retString = retString + line + "\n";  //TOFIX ENDLINE CHAR?
        }
        if (type.equals("Error")) {
          systemTalker.setErrorResult(0,retString);
          systemTalker.setErrorFinished(false);
        }
        else if (type.equals("Output")) {
          systemTalker.setOutputResult(0,retString);
          systemTalker.setOutputFinished();
        }
      }
      catch (IOException e) {
        systemTalker.setErrorResult(1,
            "Read of external tool's output failed with IOException");
        systemTalker.setErrorFinished(true);
      }
    }
  }

  /**
   * Default constructor.
   */
  public SystemTalker() {
    outputResult = new TalkResult();
  }

  /* (non-javadoc)
   * Tell talker that stdout output has finished.
   */
  private synchronized void setOutputFinished() {
    outputFinished = true;
    notifyAll();
  }

  /* (non-javadoc)
   * Tell talker that stderr output has finished.
   */
  private synchronized void setErrorFinished(boolean gotError) {
    errorFinished = true;
    this.gotError = gotError;
    notifyAll();
  }

  /* (non-javadoc)
   * Capture stdout content and return code.
   */
  private void setOutputResult(int errorCode, String stdout) {
    outputResult.setStdout(stdout);
    outputResult.setErrorCode(errorCode);
  }

  /* (non-javadoc)
   * Capture stderr content and return code.
   */
  private void setErrorResult(int errorCode, String stderr) {
    outputResult.setStderr(stderr);
    outputResult.setErrorCode(errorCode);
  }

  /** 
   * Runs a command at the shell, and returns the results, including
   * stdout and stderr content and return code.
   *
   * @param cmdArgs The array of string arguments to be run
   * @param input The input stream data to be supplied to the program (if any)
   * @return TalkResult A structure containing the stdout and stderr output 
   *   and return code produced by the external command 
   */
  public synchronized TalkResult talk(String[] cmdArgs, String input) {
    int returnValue = 0;
    outputFinished = false;
    errorFinished = false;
    gotError = false;

    Process p = null;
    try { 
      p = Runtime.getRuntime().exec(cmdArgs);
      StreamGobbler errorGobbler =
          new StreamGobbler(p.getErrorStream(), "Error", this);
      StreamGobbler outputGobbler =
          new StreamGobbler(p.getInputStream(), "Output", this);
      errorGobbler.start();
      outputGobbler.start();
      if (input != null) {
        PrintWriter writer = new PrintWriter(p.getOutputStream());
        writer.println(input);
        writer.close();
      }
      try {
        returnValue = p.waitFor();
        while ((outputFinished == false) || (errorFinished == false)) {
          this.wait(1);
        }
      }
      catch (InterruptedException e) {
        return new TalkResult(2,"","Call to external tool interrupted");
      }
      if (gotError) {
        return outputResult;  //This was populated by the error gobbler
      }
      if (returnValue != 0) {
        outputResult.setErrorCode(returnValue);
      }
      else {
        outputResult.setErrorCode(0);
      }
      return outputResult;
    }
    catch (IOException e) { 
      return new TalkResult(1, "",
        "Call to external tool failed with IOException");
    }
  }
} 
