/*
 * Created on Jun 11, 2003
 *
 */
package org.astrogrid.mySpace.mySpaceUtil;

import java.io.File;
import org.globus.ftp.DataChannelAuthentication;
import org.globus.ftp.GridFTPClient;
import org.globus.ftp.Session;
import org.globus.ftp.exception.ClientException;
import org.globus.ftp.exception.ServerException;
import org.globus.io.streams.GlobusFileInputStream;
import org.globus.io.streams.GlobusFileOutputStream;
import org.globus.io.streams.GlobusInputStream;
import org.globus.io.streams.GlobusOutputStream;
import org.globus.io.streams.HTTPInputStream;
import org.globus.util.GlobusURL;
import org.gridforum.jgss.ExtendedGSSManager;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;

/**
 * A manager for the download of a single data-set from a URL.
 *
 * Each FileTransfer operates a single download from a fixed
 * source URL to a fixed, local distination-file, the source
 * and destination being set when the FileTransfer is constructed.
 * There is no way to reuse a FileTransfer to download from a
 * different URL.
 *
 * The client is expected to construct a new FileTransfer for each
 * download.  The intended usage is: construction; transfer;
 * check results using accessors.
 *
 * Exceptions while transferring are caught.  On catching an
 * exception for a given URL, the class discards the exception
 * and goes on to the next URL in the input set.  If there are
 * no more URLs to try, then a FileTransferException is thrown.
 *
 * A FileTransfer is not MT-safe.
 *
 * @author Jia Yu
 * @author Guy Rixon
 */
public class FileTransfer {

  private String[]    urls;
  private File        file;
  private String      urlUsed;
  private Exception[] errors;
  private byte[]      buffer = new byte[2048];  // Transfer buffer.
  private int         bytes  = 0;  // Bytes transferred.


  /**
   * Creates a FileTransfer with a set of source URLs and
   * one destination file.  The source URLs must be given
   * in order of preference.
   */
  public FileTransfer (String[] urls, String file) {
    this.urls   = urls;
    this.file   = new File(file);
    this.errors = new Exception[urls.length];
  }


  /**
   * Creates a FileTransfer with a single source URL and
   * one destination file.
   */
  public FileTransfer (String url, String file) {
	String[] urls = {url};
    this.urls     = urls;
    this.file     = new File(file);
    this.errors   = new Exception[1];
  }



  /**
   * Executes the transfer using the source(s) and destination
   * set during construction.  The given sources are tried in
   * order until a transfer succeeds or there are no more sources.
   *
   * @throws FileTransferException if all sources fail.
   */
  public void transfer () throws FileTransferException {
	boolean success = false;
	for (int i = 0; i < this.urls.length && !success; i++) {
      try {
        this.transfer(this.urls[i], this.file);
        success = true;
        this.urlUsed = urls[i];
      }
      catch (Exception e) {
        System.out.println(this.urls[i] + " failed: " + e.getMessage());
        this.errors[i] = e;
      }
    }
    if (!success) {
      throw new FileTransferException(this.file +
                                      " was not obtained;" +
                                      " all file-transfer protocols failed.");
    }
  }


  /**
   * Returns the URL used in the last transfer.  Returns null if
   * no transfer has been made since construction.
   */
  public String getChosenUrl () {
	return this.urlUsed;
  }


  /**
   * Returns the Exceptions associated with each source URL.
   * Before the transfer all the exceptions are null.  After
   * the transfer, exceptions are non-null only where a source
   * URL has been tried and has failed.  I.e., the exceptions
   * are non-null at the start of the returned array and are
   * null for the successful URL and any URL following it.
   *
   * @return array of Exceptions mapped on-for-one to the
   * array of source URLs given at construction.
   */
  public Exception[] getErrors () {
	return this.errors;
  }


  /**
   * Attempts one transfer, using one source URL.
   */
  private void transfer(String urlStr,File localFile) throws Exception {

    GlobusInputStream  in  = null;
    GlobusOutputStream out = null;
    GlobusURL          url = null;

    System.out.println("FileTransfer.transfer(): copying from " +
                       urlStr +
                       " to " +
                       localFile.getAbsolutePath());


    // Catch any exception and rethrow as a FileTransferException.
    try {

      // Parse the source URL.  GlobusURL is used insted of java.net.URL
      // in order to include gsiftp URLs.
      url = new GlobusURL(urlStr);

      // Do the transfer.
      if( url.getProtocol().equalsIgnoreCase("gsiftp") ||
          url.getProtocol().equalsIgnoreCase("gridftp")){
      this.useGridFtp(url, localFile);
      }
      else if (url.getProtocol().equalsIgnoreCase("http")) {
	    this.useHttp(url, localFile);
      }
      else if (url.getProtocol().equalsIgnoreCase("file")) {
        this.useFile(url, localFile);
      }
      else {
	    System.out.println("FileTransfer.transfer: unknown protocol: " + url.getProtocol());
	    throw new Exception("Unknown protocol: " + url.getProtocol());
      }

    }
    catch (Exception e) {
      throw new FileTransferException(e.getMessage());
    }
  }


  /**
   * Transfers from a GridFTP URL (scheme gsiftp or gridftp).
   */
  private void useGridFtp (GlobusURL url, File localFile) throws Exception {

    GlobusInputStream  in        = null;
    GlobusOutputStream out       = null;
    GSSCredential      cred      = null;
    GridFTPClient      hotClient = null;

    System.out.println("FileTransfer.transfer : using gsiftp");

    try {
      hotClient = new GridFTPClient(url.getHost(), url.getPort());
      System.out.println("FileTransfer.transfer : created a GridFTP client.");

      // Create a credential
      System.out.println("FileTransfer.transfer : creaing credentials");
      ExtendedGSSManager manager =
          (ExtendedGSSManager)ExtendedGSSManager.getInstance();
      cred = manager.createCredential(GSSCredential.INITIATE_AND_ACCEPT);

      // Authenticate to the server
      System.out.println("FileTransfer.transfer : authenticating");
      hotClient.authenticate(cred);

      // Set security parameters such as data channel authentication
      // (defined by the GridFTP protocol) and data channel
      // protection (defined by RFC 2228).
      // If these are not specified, data channels are authenticated
      // by default.
      System.out.println("FileTransfer.transfer : setting data channel");
      hotClient.setDataChannelAuthentication(DataChannelAuthentication.NONE);
      hotClient.setType(Session.TYPE_IMAGE);

      // Download the data.
      System.out.println("FileTransfer.transfer : getting data");
      if (!hotClient.exists(url.getPath())) {
        throw new FileTransferException("the File does not exist");
      }
      else {
       hotClient.get(url.getPath(), localFile);
      }

    }
    catch (Exception e) {
      throw e;
    }
    finally {
      System.out.println("FileTransfer.transfer : closing");
      if (hotClient != null) hotClient.close();
    }
  }


  /**
   * Attempts a transfer from an HTTP URL.
   */
  private void useHttp (GlobusURL url, File localFile) throws Exception {

    System.out.println("FileTransfer.transfer : using http");

    HTTPInputStream        in  = null;
    GlobusFileOutputStream out = null;

    try {
      in  = new HTTPInputStream(url.getHost(),url.getPort(),url.getPath());
      out = new GlobusFileOutputStream(localFile.getPath(), false);
      int size = in.getSize();
      if (size == -1) {
        throw new FileTransferException("source size: unknown");
      }
      while( (bytes = in.read(buffer)) != -1) {
        out.write(buffer, 0, bytes);
        out.flush();
      }
    }
    catch (Exception e) {
      throw e;
    }
    finally {
      if (in  != null) in.close();
      if (out != null) out.close();
	}
  }


  /**
   * Attempts a transfer from a file URL.
   */
  private void useFile (GlobusURL url, File localFile) throws Exception {

    GlobusFileInputStream  in  = null;
    GlobusFileOutputStream out = null;

    System.out.println("FileTransfer.transfer : using 'file' protocol.");

    try {
      in  = new GlobusFileInputStream(url.getPath());
      out = new GlobusFileOutputStream(localFile.getPath(),false);
      int size = in.getSize();
      if (size == -1) {
        throw new FileTransferException("source size: unknown");
      }
      while ((bytes = in.read(buffer)) != -1) {
        out.write(buffer, 0, bytes);
        out.flush();
      }
    }
    catch (Exception e) {
      throw e;
    }
    finally {
      if (in  != null) in.close();
      if (out != null) out.close();
    }
  }

}
