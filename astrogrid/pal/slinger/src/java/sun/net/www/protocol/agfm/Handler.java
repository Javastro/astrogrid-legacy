/**
 * Handler.java
 *
 * @author Created by Omnicore CodeGuide
 */

package sun.net.www.protocol.agfm;
import org.astrogrid.slinger.agfm.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import org.astrogrid.store.Ivorn;

/** Handler for 'homespace' urls.  Only one of these is every created; provides
 * parsing function and a connection factory*/


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
         //remove the 'agfm' bit, giving an ivorn
         Ivorn ivorn = new Ivorn(spec.substring(start));
         
         if ((ivorn.getFragment() == null) || (ivorn.getFragment().trim().length() ==0)) {
            throw new IllegalArgumentException("No path given in '"+spec+"'");
         }
         
         String filepath = ivorn.getFragment();
         if (filepath != null) filepath = "/"+filepath;
         
         //NB the scheme does not actually change when you call this
         setURL(u, Ivorn.SCHEME, null, -1, ivorn.getPath(), null, filepath, null, null);
         //System.out.println(u.toString());
      }
      catch (URISyntaxException e) {
         throw new IllegalArgumentException(e+" in "+spec);
      }
   }
   
   /**
    * Opens a connection to the object referenced by the
    * <code>URL</code> argument.
    *
    * @param      u   the URL that this connects to.
    * @return     a <code>URLConnection</code> object for the <code>URL</code>.
    * @exception  IOException  if an I/O error occurs while opening the
    *               connection.
    */
   protected URLConnection openConnection(URL u) throws IOException {

      //the url 'authority'
      Ivorn i = new Ivorn(u.getAuthority(), u.getPath());
      
      return new FileManagerConnection(i);
      
   }
   
   
}

