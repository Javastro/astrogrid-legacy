/*
 * $Id: GetMetadata.java,v 1.2 2005/03/10 20:19:21 mch Exp $
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

         //see if a particular resource type has bee requested
         String resourceType = request.getParameter("Resource");
         
         //nothing - all
         if (resourceType == null) {
            response.getWriter().write(VoDescriptionServer.makeVoDescription());
         }
         //CEA resources
         else if (resourceType.toUpperCase().equals("CEA")) {
            response.getWriter().write(CEAComponentManagerFactory.getInstance().getMetadataService().returnRegistryEntry());
         }
         //look for the one with the same name
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
