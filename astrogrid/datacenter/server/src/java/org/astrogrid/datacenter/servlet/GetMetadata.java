/*
 * $Id: GetMetadata.java,v 1.1 2004/09/02 08:02:17 mch Exp $
 */

package org.astrogrid.datacenter.servlet;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.datacenter.metadata.MetadataServer;
import org.astrogrid.util.DomHelper;

/**
 * A servlet for returning the generated metadata
 *
 * @author mch
 */
public class GetMetadata extends StdServlet {
   
   public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

      try {
         response.setContentType("text/xml");
         response.getWriter().write(DomHelper.DocumentToString(MetadataServer.getMetadata()));
      }
      catch (Throwable th) {
         doError(response, "Geting Metadata",th);
      }
   }


}
