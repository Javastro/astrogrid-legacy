/*
 * $Id: JargonFileAdaptor.java,v 1.1 2005/02/16 15:02:46 mch Exp $
 */

package org.astrogrid.storeclient.api.srb;

import edu.sdsc.grid.io.FileFactory;
import edu.sdsc.grid.io.GeneralFile;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.security.Principal;
import java.util.Date;
import org.astrogrid.storeclient.api.StoreFile;
import org.astrogrid.slinger.mime.MimeFileExts;
import java.net.URISyntaxException;

/**
 * Adaptor to the Jargon library so we get access to Storage Resource Broker
 */


public class JargonFileAdaptor implements StoreFile
{
   GeneralFile jargonFile;
   
   public static final String DEMO_SRB_URI = "srb://demouser.npaci:DEMO@srb.sdsc.edu:8080/home/demouser.npaci/";
   
   
   public JargonFileAdaptor(GeneralFile file) {
      this.jargonFile = file;
   }
   
   /** Create a file for the given SRB URI. This URI is of the form
    * srb:// [ userName . domainHome [ : password ] @ ] host [ : port ][ / path ]
    * eg
    * srb://testuser.sdsc:PASSWORD@srb.sdsc.edu:5555/home/testuser.sdsc/testfile.txt
    * srb:// demouser.npaci:DEMO@srb.sdsc.edu:5555/home/demouser.npaci/
    */
   public JargonFileAdaptor(URI srbUri) throws IOException {
      this.jargonFile = FileFactory.newFile(srbUri);
   }
   
   
   /** Returns the date the file was last modified (null if unknown) */
   public Date getModified()
   {
      return new Date(jargonFile.lastModified());
   }
   
   /** Returns the size of the file in bytes (-1 if unknown) */
   public long getSize()
   {
      return jargonFile.length();
   }
   
   /** Returns true if this is a self-contained file.  For example, a database
    * table might be represented as a StoreFile but it is not a file */
   public boolean isFile()
   {
      return jargonFile.isFile();
   }
   
   /** Returns true if this is a container that can hold other files/folders */
   public boolean isFolder()
   {
      return jargonFile.isDirectory();
   }
   
   /** does nothing */
   public void refresh() throws IOException
   {
   }

   /** Returns true if it exists - eg it may be a reference to a file about to be
    * created */
   public boolean exists() {
      return jargonFile.exists();
   }
   
   /** If this is not a folder, creates a stream that reads from it */
   public InputStream openInputStream(Principal user) throws IOException
   {
      return FileFactory.newFileInputStream(jargonFile);
   }
   
   /** Renames the file to the given filename. Affects only the name, not the
    * path */
   public void renameTo(String newFilename, Principal user) throws IOException
   {
//    FileFactory.newFile();
//    jargonFile.renameTo();
      throw new UnsupportedOperationException("todo");
   }
   
   /** If this is a folder, creates an output stream to a child file */
   public OutputStream outputChild(String filename, Principal user, String mimeType) throws IOException
   {
      GeneralFile child = FileFactory.newFile(jargonFile, filename);
      return FileFactory.newFileOutputStream(child);
   }
   
   /** Does nothing */
   public void setMimeType(String newMimeType, Principal user) throws IOException
   {
   }
   
   /** Returns true if this represents the same file as the given one, within
    * this server.  This
    * won't check for references from different stores to the same file */
   public boolean equals(StoreFile anotherFile)
   {
      if (anotherFile instanceof JargonFileAdaptor) {
         return jargonFile.equals( ((JargonFileAdaptor) anotherFile).jargonFile);
      }
      return false;
   }
   
   /** If this is not a folder, creates a stream that outputs to it */
   public OutputStream openOutputStream(Principal user, String mimeType, boolean append) throws IOException
   {
      return FileFactory.newFileOutputStream(jargonFile);
   }
   
   /** Returns parent folder of this file/folder, if permission granted */
   public StoreFile getParent(Principal user) throws IOException
   {
      return new JargonFileAdaptor(jargonFile.getParentFile());
   }
   
   /** Returns an appropriate RL (eg url, msrl) for this file */
   public String getUri()
   {
      return jargonFile.toURI().toString();
   }
   
   /** Deletes this file */
   public void delete(Principal user) throws IOException
   {
      jargonFile.delete();
   }
   
   /** Lists children files if this is a container - returns null otherwise */
   public StoreFile[] listFiles(Principal user) throws IOException
   {
      GeneralFile[] jargonChilds = jargonFile.listFiles();
      JargonFileAdaptor[] adaptors = new JargonFileAdaptor[jargonChilds.length];
      for (int i = 0; i < jargonChilds.length; i++)
      {
         adaptors[i] = new JargonFileAdaptor(jargonChilds[i]);
      }
      return adaptors;
   }
   
   /** Returns the mime type (null if unknown) */
   public String getMimeType()
   {
      return MimeFileExts.guessMimeType(jargonFile.getName());
   }
   
   /** IF this is a folder, creats a subfolder */
   public StoreFile makeFolder(String newFolderName, Principal user) throws IOException
   {
      GeneralFile childFolder = FileFactory.newFile(jargonFile, newFolderName);
      childFolder.mkdir();
      return new JargonFileAdaptor(childFolder);
   }
   
   /** Returns the creation date  (null if unknown) */
   public Date getCreated()
   {
      return null;
   }
   
   /** Returns the path to this file on the server, including the filename */
   public String getPath()
   {
      return jargonFile.getAbsolutePath();
   }
   
   /** Returns the owner of the file */
   public String getOwner()
   {
      return null;
   }
   
   /** Returns the file/folder/table name without path */
   public String getName()
   {
      return jargonFile.getName();
   }
   
   /** Returns true if the URI looks like an SRB uri */
   public static boolean isSRB(String uri) {
      return uri.startsWith("srb://");
   }
   
   /** For simple tests/debugging */
   public static void main(String[] args) throws IOException, URISyntaxException
   {
      String uri = DEMO_SRB_URI;
      System.out.println("Creating file for "+uri+"...");
      StoreFile file = new JargonFileAdaptor(new URI(uri));
      System.out.println("...done");
   }
   
}

