/*
 * $Id: GetMetadata.java,v 1.2 2004/09/06 20:23:00 mch Exp $
 */

package org.astrogrid.datacenter.servlet;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.datacenter.metadata.VoDescriptionServer;
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
         response.getWriter().write(DomHelper.DocumentToString(VoDescriptionServer.getVoDescription()));
      }
      catch (Throwable th) {
         doError(response, "Geting Metadata",th);
      }
   }


}
