/*
 * $Id: GetMetadata.java,v 1.1 2005/02/17 18:37:35 mch Exp $
 */

package org.astrogrid.dataservice.service.servlet;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.applications.component.CEAComponentManagerFactory;
import org.astrogrid.dataservice.metadata.VoDescriptionServer;
import org.astrogrid.util.DomHelper;
import org.astrogrid.webapp.DefaultServlet;

/**
 * A servlet for returning the generated metadata
 *
 * @author mch
 */
public class GetMetadata extends DefaultServlet {
   
   
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
            //we still need to wrap it in a VoDescription so that it's a valid document
            response.getWriter().write(VoDescriptionServer.VODESCRIPTION_ELEMENT);
            response.getWriter().write(DomHelper.ElementToString(VoDescriptionServer.getResource(resourceType)));
            response.getWriter().write(VoDescriptionServer.VODESCRIPTION_ELEMENT_END);
         }
      }
      catch (Throwable th) {
         doError(response, "Geting Metadata",th);
      }
   }


}
