/*
 * $Id: GetMetadata.java,v 1.6 2006/09/26 15:34:42 clq2 Exp $
 */

package org.astrogrid.dataservice.service.servlet;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.applications.component.CEAComponentManagerFactory;
import org.astrogrid.dataservice.metadata.VoDescriptionServer;
import org.astrogrid.xml.DomHelper;
import org.astrogrid.webapp.DefaultServlet;
import org.astrogrid.tableserver.test.SampleStarsPlugin;
import org.astrogrid.cfg.ConfigFactory;


/**
 * A servlet for returning the generated metadata
 *
 * @author mch
 */
public class GetMetadata extends DefaultServlet {
   
   public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

      // Initialise SampleStars plugin if required (may not be initialised
      // if admin has not run the self-tests)
      String plugin = ConfigFactory.getCommonConfig().getString(
           "datacenter.querier.plugin");
      if (plugin.equals("org.astrogrid.tableserver.test.SampleStarsPlugin")) {
         // This has no effect if the plugin is already initialised
         SampleStarsPlugin.initialise();  // Just in case
      }
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
//            response.getWriter().write(".... test string ....");
//            response.getWriter().write(
//				CEAComponentManagerFactory.getInstance().getMetadataService().returnRegistryEntry()
//				);
            response.getWriter().write(
				DomHelper.ElementToString(
					CEAComponentManagerFactory.getInstance().getMetadataService().returnRegistryEntry().getDocumentElement()
					)
				);
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
         doError(response, "Getting Metadata",th);
      }
   }


}
