/**
 * Handler.java
 *
 * @author Created by Omnicore CodeGuide
 */

package sun.net.www.protocol.homespace;
import org.astrogrid.slinger.homespace.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/** Handler for 'homespace' urls.  Only one of these is every created; provides
 * parsing function and a connection factory.  Note that there is also a
 * HomespaceSourceTarget for webapp use. */

public class Handler extends URLStreamHandler {
   

    /**
     * Parses the string representation of a <code>URL</code> into a
     * <code>URL</code> object, using HomespaceName to read it.
     * <p>
     * @param   u       the <code>URL</code> to receive the result of parsing
     *                  the spec.
     * @param   spec    the <code>String</code> representing the URL that
     *                  must be parsed.
     * @param   start   the character index at which to begin parsing. This is
     *                  just past the '<code>:</code>' (if there is one) that
     *                  specifies the determination of the protocol name.
     * @param   limit   the character position to stop parsing at. This is the
     *                  end of the string or the position of the
     *                  "<code>#</code>" character, if present. All information
     *                  after the sharp sign indicates an anchor.
     */
   protected void parseURL(URL u, String spec, int start, int limit) {
      try {
         HomespaceName homespace = new HomespaceName(spec);
         setURL(u, HomespaceName.SCHEME, null, -1, homespace.getAccountName(), null, homespace.getPath(), null, null);
      }
      catch (URISyntaxException e) {
         throw new IllegalArgumentException(e+" in "+spec);
      }
   }
   
   /**
    * Opens a connection to the object referenced by the
    * <code>URL</code> argument.
    * In this case, it resolves the Homespace URL and returns a connection to
    * that.
    *
    * @param      u   the URL that this connects to.
    * @return     a <code>URLConnection</code> object for the <code>URL</code>.
    * @exception  IOException  if an I/O error occurs while opening the
    *               connection.
    */
   protected URLConnection openConnection(URL u) throws IOException {
      HomespaceName homespace = new HomespaceName(u.getAuthority(), u.getPath());
      return resolveConnection(homespace);
   }
   
   
   /** Resolves the homespace to some location.  At the moment, naughtily, this
    * assumes that the location is a FileManaagerId, because the way the filemanager
    * client is written we can't do much else :-(.  */
   public URLConnection resolveConnection(HomespaceName homespace) throws IOException {
      try {
         URL url = new URL("agfm:"+homespace.toIvorn()); //ivo://"+homespace.getCommunity()+"/"+homespace.getIndividual()+"#"+homespace.getPath());
         return url.openConnection();
      }
      catch (IllegalArgumentException iae) {
         //add useful debug information
         IllegalArgumentException newIae = new IllegalArgumentException(iae.getMessage()+" for '"+homespace+"' -> '"+homespace.toIvorn()+"'");
         newIae.setStackTrace(iae.getStackTrace());
         throw newIae;
      }
   }
   
}

