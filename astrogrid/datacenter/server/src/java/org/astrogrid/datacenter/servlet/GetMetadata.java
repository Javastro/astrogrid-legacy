/*
 * $Id: GetMetadata.java,v 1.3 2004/09/08 11:22:56 mch Exp $
 */

package org.astrogrid.datacenter.servlet;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.applications.component.CEAComponentManagerFactory;
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

         String resourceType = request.getParameter("Resource");
         if (resourceType == null) {
            response.getWriter().write(VoDescriptionServer.makeVoDescription());
         }
         else if (resourceType.toUpperCase().equals("CEA")) {
            response.getWriter().write(CEAComponentManagerFactory.getInstance().getMetadataService().returnRegistryEntry());
         }
         else  {
            response.getWriter().write(DomHelper.ElementToString(VoDescriptionServer.getResource(resourceType)));
         }
      }
      catch (Throwable th) {
         doError(response, "Geting Metadata",th);
      }
   }


}
