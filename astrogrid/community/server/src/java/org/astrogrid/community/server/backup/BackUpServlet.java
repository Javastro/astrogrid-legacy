package org.astrogrid.community.server.backup;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.SimpleConfig;
import org.xml.sax.InputSource;

/**
 * A servlet to back up a community's database and credential cache.
 * An HTTP-GET request to this servlet receives in reply a ZIP set of the
 * information.
 *
 * @author Guy Rixon
 * @version
 */
public class BackUpServlet extends HttpServlet {
  
  private static final Log log = LogFactory.getLog(BackUpServlet.class);
  
  /** Handles the HTTP <code>GET</code> method.
   * @param request servlet request
   * @param response servlet response
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException {
    response.setContentType("application/zip");
    
    ZipOutputStream zip = null;
    try {
      zip = new ZipOutputStream(response.getOutputStream());
      zipUserCredentials(zip);
      zipDatabase(zip);
    }
    finally {
      if (zip != null);
      zip.close();
    }
  }
  
  /**
   * Adds to the zip file all the files of user credentials.
   */
  protected void zipUserCredentials(ZipOutputStream zip) throws IOException {
    assert zip != null;
    log.info("Zipping up the user credentials...");
    String credentialsDirectoryName = 
        SimpleConfig.getSingleton().getString("org.astrogrid.community.myproxy");
    File credentialsDirectory = new File(credentialsDirectoryName);
    copyDirectoryToZip(zip, credentialsDirectory);
  }
  
  /**
   * Adds to the zip file the community database. This only records information
   * if the database is an HSQL database; for other kinds of database, nothing
   * is written.
   */
  protected void zipDatabase(ZipOutputStream zip) throws IOException {
    assert zip != null;
    URL dbConfigurationUrl =
        SimpleConfig.getSingleton().getUrl("org.astrogrid.community.dbconfigurl");
    log.info("Zipping up the database defined by " + dbConfigurationUrl);
    InputSource is = new InputSource(dbConfigurationUrl.openStream());
    XPath xp = XPathFactory.newInstance().newXPath();
    try {
      String dbUrl = 
          xp.evaluate("//database[@name='org.astrogrid.community.database']/driver/@url", is);
      String[] parts = dbUrl.split(":");
      if (parts.length >= 3 &&
          parts[0].equals("jdbc") &&
          parts[1].equals("hsqldb")) {
        String dbDirectory = parts[2];
        log.info("Database is HSQL with files at " + dbDirectory);
        copyFileToZip(zip, new File(dbDirectory + ".properties"));
        copyFileToZip(zip, new File(dbDirectory + ".script"));
      }
      else {
        log.info("Database is not HSQL and so cannot be zipped up.");
      }
    } catch (XPathExpressionException ex) {
      ex.printStackTrace();
      throw new IOException("Failed to read the database configuration");
    }
  }
  
  /**
   * Copies the contents of a named directory to a ZIP stream.
   */
  protected void copyDirectoryToZip(ZipOutputStream zip, 
                                    File            directory) throws IOException {
    log.info("Zipping directory " + directory.getAbsolutePath());
    assert directory.isDirectory();
    for (File f : directory.listFiles()) {
      if (f.isDirectory()) {
        copyDirectoryToZip(zip, f);
      }
      else if (f.isFile()) {
        copyFileToZip(zip, f);
      }
      else {
        log.warn(f + " is neither directory nor plain file so cannot be zipped.");
      }
    }
  }
  

  /**
   * Copies the contents of a named file to a ZIP stream.
   *
   * @param zip The ZIP stream to receive the data.
   * @param fileName The name of the file to be copied.
   * @throws IOException If the ZIP stream is not in the right state.
   * @throws IOException If the file cannot be read.
   * @throws IOException If the data cannot be added to the ZIP stream.
   */
  protected void copyFileToZip(final ZipOutputStream zip, 
                               final File            f) throws IOException {
    log.info("Zipping file " + f.getAbsolutePath());
    FileInputStream fis = new FileInputStream(f);
    BufferedInputStream bis = new BufferedInputStream(fis);
    zip.putNextEntry(new ZipEntry(f.getAbsolutePath()));
    byte[] b = new byte[1024];
    while (true) {
      if (bis.read(b) == -1) {
        break;
      }
      else {
        zip.write(b);
      }
    }
    zip.closeEntry();
  }

}
