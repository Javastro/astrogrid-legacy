/*
 * $Id: HttpResponseTarget.java,v 1.1.1.1 2009/05/13 13:20:41 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.targets;


import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.security.Principal;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.io.NoCloseWriter;

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
   public Writer openWriter() throws IOException {
      return new NoCloseWriter(response.getWriter());
   }
   
   /** All targets must be able to resolve to a stream.  The user is required
    * for permissioning. */
   public OutputStream openOutputStream() throws IOException {
      return response.getOutputStream();
   }

   /** Return response */
   public HttpServletResponse getResponse() {
      return response;
   }

   /** Used to set the mime type of the data about to be sent to the target. *Must*
    * be set before any data is set, as HttpResponse can't cope otherwise */
   public void setMimeType(String mimeType) {
      response.setContentType(mimeType);
   }
   
}
/*
 $Log: HttpResponseTarget.java,v $
 Revision 1.1.1.1  2009/05/13 13:20:41  gtr


 Revision 1.2  2006/09/26 15:34:42  clq2
 SLI_KEA_1794 for slinger and PAL_KEA_1974 for pal and xml, deleted slinger jar from repo, merged with pal

 Revision 1.1.2.1  2006/09/11 11:40:46  kea
 Moving slinger functionality back into DSA (but preserving separate
 org.astrogrid.slinger namespace for now, for easier replacement of
 slinger functionality later).

 Revision 1.2  2005/05/27 16:21:01  clq2
 mchv_1

 Revision 1.1.20.1  2005/04/21 17:09:03  mch
 incorporated homespace etc into URLs

 Revision 1.1  2005/02/14 20:47:38  mch
 Split into API and webapp

 Revision 1.4  2005/02/14 17:53:38  mch
 Split between webnode (webapp) and library, prepare to split between API and special implementations

 Revision 1.3  2005/01/26 17:31:57  mch
 Split slinger out to scapi, swib, etc.

 Revision 1.1.2.2  2004/12/13 15:53:39  mch
 Moved stuff to IO package and new progress monitoring streams

 Revision 1.1.2.1  2004/11/22 00:46:28  mch
 New Slinger Package

 Revision 1.1.2.1  2004/11/17 11:15:46  mch
 Changes for serving images


 */



