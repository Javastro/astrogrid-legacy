package org.astrogrid.dataservice.service.tap;

import java.io.File;

/**
 *
 * @author Guy Rixon
 */
public class ResultFile {
  
  private static File directory = new File ("/tmp");
  private File file;
  private String mimeType;
  
  /**
   * Constructs a ResultFile for a given job and format.
   * This uses a new file with an extension appropriate to the
   * format.
   */
  public ResultFile(String jobId, String format) {
    if ("VOTABLE".equals(format)) {
      file = new File(directory, jobId + ".vot");
      mimeType = "application/x-votable+xml";
    }
    else if ("CSV".equals(format)) {
      file = new File(directory, jobId + ".csv");
      mimeType = "text/csv";
    }
    else if ("HTML".equals(format)) {
      file = new File(directory, jobId + ".html");
      mimeType = "text/html";
    }
    else {
      throw new IllegalArgumentException("Format must be VOTABLE, CSV or HTML");
    }
  }
  
  /**
   * Constructs a ResultFile for a given job.
   * This uses an existing file, trying different extensions to the
   * file-name until a match is found.
   */
  public ResultFile(String jobId) {
    file = new File(directory, jobId + ".vot");
    if (file.exists()) {
      mimeType = "application/x-votable+xml";
    }
    else {
      file = new File(directory, jobId + ".csv");
      if (file.exists()) {
        mimeType = "text/csv";
      }
      else {
        file = new File(directory, jobId + ".html");
        if (file.exists()) {
          mimeType = "text/html";
        }
        else {
          throw new IllegalArgumentException("Job " + jobId + " has no results file");
        }
      }
    }
  }
  
  /**
   * Reveals the file-name set at construction.
   */
  public File getFile() {
    return file;
  }
  
  /**
   * Reveals the MIME-type set at construction.
   */
  public String getMimeType() {
    return mimeType;
  }
}
