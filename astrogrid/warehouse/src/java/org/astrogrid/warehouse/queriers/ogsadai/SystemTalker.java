package org.astrogrid.warehouse.queriers.ogsadai;

import java.io.*;
import java.util.*;

public class SystemTalker {

  private TalkResult outputResult;
  private boolean outputFinished;
  private boolean errorFinished;
  private boolean gotError;

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

  public SystemTalker() {
    outputResult = new TalkResult();
  }

  public synchronized void setOutputFinished() {
    outputFinished = true;
    notifyAll();
  }

  public synchronized void setErrorFinished(boolean gotError) {
    errorFinished = true;
    this.gotError = gotError;
    notifyAll();
  }

  public void setOutputResult(int errorCode, String stdout) {
    outputResult.setStdout(stdout);
    outputResult.setErrorCode(errorCode);
  }
  public void setErrorResult(int errorCode, String stderr) {
    outputResult.setStderr(stderr);
    outputResult.setErrorCode(errorCode);
  }

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
