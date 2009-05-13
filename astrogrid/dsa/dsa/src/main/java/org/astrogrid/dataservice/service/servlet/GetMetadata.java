/*
 * $Id: GetMetadata.java,v 1.1 2009/05/13 13:20:34 gtr Exp $
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
 * @deprecated  Used with old-style push registration. Not relevant now
 * that we are using new IVOA registration conventions, and NO LONGER 
 * SUPPORTED.
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

         // Get version
         String version = request.getParameter("Version");
         if (version == null) {
            //version = VoDescriptionServer.V0_10;
            throw new IOException("Please specify which version of metadata you require ('v0.10', 'v1.0' etc)");
            
            //If no version given, do all enabled versions
            /*
            getVersionedMetadata(
                  response,resourceType,VoDescriptionServer.V0_10);
            getVersionedMetadata(
                  response,resourceType,VoDescriptionServer.V1_0);
            */
         }
         else {
            getVersionedMetadata(response,resourceType,version);
         }
      }
      catch (Throwable th) {
         doError(response, "Getting Metadata",th);
      }
   }

   protected void getVersionedMetadata(HttpServletResponse response, 
         String resourceType, String version) throws IOException {
      try {
         //nothing - all
         if (resourceType == null) {
            response.getWriter().write(DomHelper.DocumentToString(org.astrogrid.dataservice.metadata.VoDescriptionServer.getVoDescription(version)));
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
            response.getWriter().write(DomHelper.DocumentToString(VoDescriptionServer.getWrappedResource(version, resourceType)));
         }
      }
      catch (Throwable th) {
         doError(response, "Getting Metadata",th);
      }
   }


}
