/**
 * Handler.java
 *
 * @author Created by Omnicore CodeGuide
 */

package org.astrogrid.slinger.agfm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URLConnection;
import org.astrogrid.community.common.exception.CommunityException;
import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.filemanager.client.FileManagerClientFactory;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.io.mime.MimeFileExts;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.slinger.Slinger;
import org.astrogrid.store.Ivorn;
import org.astrogrid.community.resolver.CommunityAccountSpaceResolver;


/** There is one of these per connection. Note that the property ivo-account
 * should be set to account:name@community before connecting.  No idea how
 * the security is going to work though... */

public class FileManagerConnection extends URLConnection {

   //public final static String IVO_ACCOUNT_PROPERTY = "ivo-account";
   
   /** Delegate connection to service */
   private FileManagerClient delegate = null;
   
   /** Common IVORN ID */
   private org.astrogrid.store.Ivorn id = null;
   
   /** See Handler.parseUrl to see how the URL is created from a homespace */
   public FileManagerConnection(Ivorn ivorn) {
      super(null);
      id = ivorn;
   }
   
   /**
    * Opens a communications link to the resource referenced by this
    * URL, if such a connection has not already been established.
    * <p>
    * If the <code>connect</code> method is called when the connection
    * has already been opened (indicated by the <code>connected</code>
    * field having the value <code>true</code>), the call is ignored.
    * <p>
    * URLConnection objects go through two phases: first they are
    * created, then they are connected.  After being created, and
    * before being connected, various options can be specified
    * (e.g., doInput and UseCaches).  After connecting, it is an
    * error to try to set them.  Operations that depend on being
    * connected, like getContentLength, will implicitly perform the
    * connection, if necessary.
    *
    * @exception  IOException  if an I/O error occurs while opening the
    *               connection.
    * @see java.net.URLConnection#connected */
   public synchronized void connect() throws IOException {
      if (delegate == null) {
         FileManagerClientFactory factory = new FileManagerClientFactory();
         delegate = factory.login();
      }
   }
   
   public String getHeaderField(String name) {
      //if (name.equals("content-type")) {
      //   return homespace.getMimeType();
      //}
      return null;
   }
   
   public OutputStream getOutputStream() throws IOException {
      if (delegate == null) connect();
      try {
         FileManagerNode node = null;
         
         //if the file doesn't exist, we need to make it
         if (delegate.exists(id) == null) {
            node = delegate.createFile(id);
         }
         else {
            node = delegate.node(id);
         }
         return new FMCompleterStream(node, node.writeContent());
         //return node.writeContent();
      }
      catch (IOException e) {
         throw Slinger.renewIOException(" resolving output stream to "+id, e);
      }
      catch (CommunityException e) {
         throw Slinger.newIOException(e+" resolving output stream to "+id, e);
      }
      catch (RegistryException e) {
         throw Slinger.newIOException(e+" resolving output stream to "+id, e);
      }
      catch (URISyntaxException e) {
         // The FileManager delegate currently fails to resolve abstract
         // ivorns into concrete ones properly (in some cases anyway),
         // leading to a URISyntaxException.
         // If we get one, try resolving the abstract ivorn ourselves
         // and have another go.
         try {
            FileManagerNode node = null;
            CommunityAccountSpaceResolver resolver =
                     new CommunityAccountSpaceResolver();
            Ivorn concrete = resolver.resolve(id);
            //if the file doesn't exist, we need to make it
            if (delegate.exists(concrete) == null) {
               node = delegate.createFile(concrete);
            }
            else {
               node = delegate.node(concrete);
            }
            return new FMCompleterStream(node, node.writeContent());
         }
         catch (Exception e2) {
            // Now we really give up
            throw Slinger.newIOException(e2+" resolving output stream to "+id, e2);
         }
      }
      /**/
   }
   /** Used to set the mime type of the data about to be sent to the source. Guesses
    * from the filename */
   public String getContentType() {
      return MimeFileExts.guessMimeType(id.getPath());
   }
   
   public InputStream getInputStream() throws IOException {
      if (delegate == null) connect();
      try {
         return delegate.node(id).readContent();
      }
      catch (CommunityException e) {
         throw Slinger.newIOException(e+" resolving input stream to "+id, e);
      }
      catch (RegistryException e) {
         throw Slinger.newIOException(e+" resolving input stream to "+id, e);
      }
      catch (URISyntaxException e) {
         throw Slinger.newIOException(e+" resolving input stream to "+id, e);
      }
      /**/
   }
}

