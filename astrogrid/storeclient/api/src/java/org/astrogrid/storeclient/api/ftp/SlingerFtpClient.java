/*
 $Id: SlingerFtpClient.java,v 1.3 2005/03/29 20:13:51 mch Exp $

 (c) Copyright...
 */

package org.astrogrid.storeclient.api.ftp;
import java.io.*;

import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.astrogrid.account.LoginAccount;

/**
 * An FTP client that wraps/extends whatever implementation we settle on using.
 * Separate from FtpFile so that I can mess about with connections/disconnections
 * more easily than using the file tree.
 *
 * This implementation uses the apache commons net clients.  As far as I can tell,
 * this is *not* threadsafe, as there are commands that get partially executed, and
 * need tidying up when complete..
 *
 * @author MCH
 */
public class SlingerFtpClient extends FTPClient {
   

   protected  static Log log = LogFactory.getLog(SlingerFtpClient.class);
   
   /** New client connected to given host with given login account */
   public SlingerFtpClient(URL url, LoginAccount ftpUser) throws IOException {
      super();
      if (url.getPort() == -1) {
         connect(url.getHost());
      } else {
         connect(url.getHost(), url.getPort());
      }
      login(ftpUser.getName(), ftpUser.getPassword());
   }
   
   /** Close connection (& log off) */
   public void close() throws IOException {
      super.logout();
      super.disconnect();
   }
   
   /** Returns true if the given URL is an FTP */
   public static boolean isUrlFtp(URL url) {
      return url.getProtocol().equals("ftp");
   }

   /** Returns the URL for this server */
   public String getUrl() {
      return "ftp://"+getRemoteAddress().getHostName()+":"+getRemotePort();
   }
   
   /** Returns the FTPFile apache.net representation of the single file/folder
    * at the given path */
   public FTPFile getFile(String path) throws IOException {
         //get rid of final '/' so that listFiles only returns one match for directories, not the list of children
      if (!path.equals("/") && path.endsWith("/")) { path = path.substring(0, path.length()-1);}
      FTPFile[] files = listFiles(path);
      if ((files == null) || (files.length==0)) {
         throw new FileNotFoundException(path+" not found (or unavailable) on "+getUrl());
      }
      return files[0];
   }
   
   public boolean removeFolder(String folderPath) throws IOException {
      return removeDirectory(folderPath);
   }

   /** For backwards compatibility */
   public boolean createFolder(String folderPath) throws IOException {
      return makeDirectory(folderPath);
   }
   
   public String toString() {
      return "Slinger FTP Client to "+getUrl();
   }
   
   /** Override to provide special closing streams */
   public InputStream  retrieveFileStream(String path) throws IOException {
      InputStream in = new SlingerFtpInputStream(super.retrieveFileStream(path));
      if (!FTPReply.isPositiveIntermediate(getReplyCode())) {
         throw new IOException("Failed to open inputstream to "+getUrl()+"#"+path);
      }
      return in;
   }

      /** Override to provide special closing streams */
   public OutputStream  storeFileStream(String path) throws IOException {
      OutputStream out = new SlingerFtpOutputStream(super.storeFileStream(path));
      if (!FTPReply.isPositiveIntermediate(getReplyCode())) {
         throw new IOException("Failed to open outputtstream to "+getUrl()+"#"+path);
      }
      return out;
   }

   /** InputStream that does the right ftp completion stuff when closed */
   public class SlingerFtpInputStream extends FilterInputStream {
      public SlingerFtpInputStream(InputStream in) {
         super(in);
      }
      public void close() throws IOException {
         in.close();
         completePendingCommand();
      }
   }
   public class SlingerFtpOutputStream extends FilterOutputStream {
      public SlingerFtpOutputStream(OutputStream out) {
         super(out);
      }
      public void close() throws IOException {
         out.close();
         completePendingCommand();
      }
   }
   
   /** Override to synchronize to reduce number of spawned hits on server?
   public FTPFile[] listFiles(String path) throws IOException {
      return super.listFiles(path);
   }
    /**/
   
   /** test stuff */
   public static void main(String[] args) throws IOException {
      String roeFtp = "ftp://ftp.roe.ac.uk";
      SlingerFtpClient client = new SlingerFtpClient(new URL("ftp://ftp.roe.ac.uk"), LoginAccount.ANONYMOUS);

      /*
      URLConnection conn = new URL("ftp://ftp.at.debian.org/debian/").openConnection();
      InputStream ftpIn = conn.getInputStream();
      StringWriter sw = new StringWriter();
      Piper.pipe(new InputStreamReader(ftpIn), sw);
      String s = sw.toString();
      System.out.println(s);
       */
   }

}
