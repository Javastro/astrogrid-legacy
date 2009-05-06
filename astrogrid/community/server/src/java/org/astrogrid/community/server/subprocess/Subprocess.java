package org.astrogrid.community.server.subprocess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * A wrapper for the OpenSSL command-line utility.
 *
 * @author Guy Rixon
 */
public class Subprocess {

  /**
   * The command - one word per element.
   */
  private String[] command;
  
  /**
   * The process running the command.
   */
  private Process process;


  /**
   * Starts a command in a sub-process.
   *
   * @throws IOException If the sub-process does not start.
   */
  public Subprocess(String[] command) throws IOException {
    this.command = command;
    process = Runtime.getRuntime().exec(command);
  }

  /**
   * Sends a given string to the standard input of the subprocess.
   *
   * @param s The string to be sent.
   * @throws java.io.IOException If the process does not accept the sending.
   */
  public void sendToStdin(String s) throws IOException {
    OutputStream os = process.getOutputStream();
    os.write(s.getBytes());
    os.flush();
    os.close();
  }

  /**
   * Waits for the process to complete or fail. On failure, recovers the
   * process' error-output as an exception.
   *
   * @throws SubprocessException If the command ended with bad status.
   * @throws java.lang.InterruptedException If the command was aborted.
   */
  public void waitForEnd() throws SubprocessException, InterruptedException {
    process.waitFor(); // Throws InterruptedException if process is aborted.
    if (process.exitValue() != 0) {
      InputStream is = process.getErrorStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(is));
      StringBuilder whinge = new StringBuilder();
      whinge.append(command[0]);
      whinge.append(" failed:");
      while(true) {
        try {
          String line = br.readLine();
          if (line == null) {
            break;
          }
          whinge.append("\n");
          whinge.append(line);
        }
        catch (Exception e) {
          break;
        }
      }
      throw new SubprocessException(whinge.toString());
    }
  }

  /**
   * Aborts a subprocess. Any threads waiting for the process to end will
   * receive an InterruptedException.
   */
  public void abort() {
    process.destroy();
  }
}
