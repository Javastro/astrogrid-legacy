/*
 * $Id: HttpResponseTarget.java,v 1.2 2004/12/07 01:33:36 jdt Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.targets;


import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.security.Principal;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.slinger.targets.out.NoCloseWriter;

/**
 * Where the target is an http response - eg a servlet/JSP output for a browser
 * or other tool.
 *
 */

public class HttpResponseTarget implements TargetIdentifier {
   
   
   protected HttpServletResponse response = null;
   
   /** Construct with closeIt to *false* */
   public HttpResponseTarget(HttpServletResponse targetResponse) throws IOException {
      
      response = targetResponse;
   }

   public String toString() {
      return "HttpResponseTarget";
   }
   
   /** All targets must be able to resolve to a writer. The user is required
    * for permissions */
   public Writer resolveWriter(Principal user) throws IOException {
      return new NoCloseWriter(response.getWriter());
   }
   
   /** All targets must be able to resolve to a stream.  The user is required
    * for permissioning. */
   public OutputStream resolveOutputStream(Principal user) throws IOException {
      return response.getOutputStream();
   }

   /** Return response */
   public HttpServletResponse getResponse() {
      return response;
   }

   /** Used to set the mime type of the data about to be sent to the target. *Must*
    * be set before any data is set, as HttpResponse can't cope otherwise */
   public void setMimeType(String mimeType, Principal user) {
      response.setContentType(mimeType);
   }
   
}
/*
 $Log: HttpResponseTarget.java,v $
 Revision 1.2  2004/12/07 01:33:36  jdt
 Merge from PAL_Itn07

 Revision 1.1.2.1  2004/11/22 00:46:28  mch
 New Slinger Package

 Revision 1.1.2.1  2004/11/17 11:15:46  mch
 Changes for serving images


 */



