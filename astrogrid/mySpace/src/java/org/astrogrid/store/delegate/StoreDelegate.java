/*
 $Id: StoreDelegate.java,v 1.5 2004/05/03 13:39:40 mch Exp $

 (c) Copyright...
 */

package org.astrogrid.store.delegate;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.User;
import org.astrogrid.io.Piper;
import org.astrogrid.store.Agsl;

/**
 * Abstract class that implements some of the StoreClient methods in 'naive'
 * ways.  For example, a 'move' is a 'copy' then a 'delete'.
 */

public abstract class StoreDelegate implements StoreClient {
   
   private User operator = User.ANONYMOUS;

   protected Log log = LogFactory.getLog(StoreDelegate.class);
   
   /**
    * Construct myspace client using given endpoint, which is a complete location, eg
    * ftp://ftp.roe.ac.uk/pub/astrogrid
    */
   public StoreDelegate(User anOperator)
   {
      this.operator = anOperator;
   }

   /**
    * Returns the user of this delegate - ie the account it is being used by
    */
   public User getOperator() {
      return operator;
   }
   
   /**
    * Copies the contents of the file at the given source url to the given location.
    * This implementation pipes the
    * contents through this delegate, rather than telling the server to fetch the
    * url
    *
    */
   public void putUrl(URL source, String targetPath, boolean append) throws IOException {
      OutputStream out = putStream(targetPath, append);
      InputStream in = source.openStream();
      Piper.bufferedPipe(in, out);
      out.close();
      in.close();
   }
   
   /**
    * Puts the given byte buffer into the given location using putStream()
    */
   public void putBytes(byte[] bytes, int offset, int length, String targetPath, boolean append) throws IOException {
      OutputStream out = putStream(targetPath, append);
      out.write(bytes, offset, length);
      out.close();
   }

   /**
    * Puts the given string into the given location
    */
   public void putString(String contents, String targetPath, boolean append) throws IOException {
      byte[] b = contents.getBytes();
      putBytes(b, 0, b.length, targetPath, append);
   }
   
   /**
    * Copy a file to a target Agsl - NB this is done naively from the delegate (ie, the file contents
    * pass through this client computer).  Servers that implement copy should have
    * this method overriddent to call that copy
    */
   public void copy(String sourcePath, Agsl target) throws IOException {
      StoreClient targetClient = StoreDelegateFactory.createDelegate(getOperator(), target);
      InputStream in = getStream(sourcePath);
      OutputStream out = targetClient.putStream(target.getPath(), false);
      Piper.bufferedPipe(in, out);
      in.close();
      out.close();
   }
   
   
   /**
    * Copy a file from a source Agsl - NB this is done naively from the delegate (ie, the file contents
    * pass through this client computer).  Servers that implement copy should have
    * this method overriddent to call that copy
    */
   public void copy(Agsl source, String targetPath) throws IOException {
      StoreClient sourceClient = StoreDelegateFactory.createDelegate(getOperator(), source);
      InputStream in = sourceClient.getStream(source.getPath());
      OutputStream out = putStream(targetPath, false);
      Piper.bufferedPipe(in, out);
      in.close();
      out.close();
   }
   
   /**
    * Moves/Renames a file
    */
   public void move(String sourcePath, Agsl targetPath) throws IOException
   {
      copy(sourcePath, targetPath);
      delete(sourcePath);
   }
   
   /**
    * Moves/Renames a file from a source Agsl
    */
   public void move(Agsl source, String targetPath) throws IOException {
      copy(source, targetPath);
      StoreClient client = StoreDelegateFactory.createDelegate(getOperator(), source);
      client.delete(source.getPath());
   }
   
   /**
    * Returns the Agsl for the given source path
    */
   public Agsl getAgsl(String sourcePath) throws IOException {
      return new Agsl(getEndpoint()+"#"+sourcePath);
   }

   /**
     * Prints the given tree to the given stream for display
    */
   public static void printTree(StoreFile root, Writer writer) throws IOException {
      writer.write(root.getPath()+",  "+root.getMimeType()+",  "+root.getSize()+", "+root.getModified()+"\n");
      
      StoreFile[] children = root.listFiles();
      
      if ((children != null) && (children.length>0)) {
         for (int i=0;i<children.length;i++) {
            printTree(root, writer);
         }
      }
   }
}
