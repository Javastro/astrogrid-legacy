/*
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.metadata;
/*
import java.io.IOException;
import java.util.Vector;
import java.lang.reflect.Constructor;
import java.net.URISyntaxException;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
*/
import org.astrogrid.cfg.ConfigFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.astrogrid.xml.DomHelper;
import org.astrogrid.registry.client.query.v1_0.RegistryService;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.dataservice.service.servlet.VosiServlet;

/**
 * Useful functions for providing metadata-related support to JSPs etc.
 *
 * <p>
 * @author K Andrews
 *
 */

public class MetadataHelper {

   //protected static Log log = LogFactory.getLog(VoDescriptionServer.class);
   protected static String errorMessage = 
      "Your configuration is incorrect!  Please run the DSA self-tests for more information.";

	/** Supplies the base URL of the installation (extracted from config) */
	public static String getInstallationBaseURL() throws MetadataException 
	{
		String serverRoot = ConfigFactory.getCommonConfig().getString(
             "datacenter.url");
      if ((serverRoot == null) || ("".equals(serverRoot))) {
         throw new MetadataException(errorMessage);
      }
		if (!serverRoot.endsWith("/")) {
			serverRoot = serverRoot+"/";
		}
		return serverRoot;
	}

   /** Produces an HTML table containing links for viewing the XML
    * produced by the various VOSI-style endpoints
    */
   public static String getVosiEndpointsTable() throws MetadataException
   {
      String table = 
         "<br/><table class=\"bordertable\">\n"+
         "<tr><td><b>Catalog name</b></td>"+
         "<td><b>View Availability</b></td>"+
         "<td><b>View Capabilites</b></td>"+
         "<td><b>View Tables</b></td>"+
         "<td><b>View CEA Application</b></td>";
      boolean gotErrors = false;
      String[] catalogNames = new String[0];
      catalogNames =
           TableMetaDocInterpreter.getCatalogNames();
      if (catalogNames.length == 0) {
         // Shouldn't get here
         throw new MetadataException("No catalogs are defined!"); 
      }
      // Get the required properties
		String serverRoot = "";

		// HORRIBLE kludge for samplestars case - may fail if this 
		// method is used in other jsp pages than admin/resources.jsp
		String plugin = ConfigFactory.getCommonConfig().getString(
				         "datacenter.querier.plugin");
		if (plugin.equals("org.astrogrid.tableserver.test.SampleStarsPlugin")) {
			serverRoot = "..";
		}
		else {
      	serverRoot = ConfigFactory.getCommonConfig().getString(
             "datacenter.url");
			if ((serverRoot == null) || ("".equals(serverRoot))) {
				throw new MetadataException(errorMessage);
			}
		}
      if (!serverRoot.endsWith("/")) {
         serverRoot = serverRoot + "/";
      }
      for (int i = 0; i < catalogNames.length; i++) {

         String catalogName = catalogNames[i];

         // First column: catalog name 
         table = table + "<tr>\n" + "<td><b>"+catalogName+"</b></td>";

         // Second column: Availability
         String formUrl = serverRoot + catalogName + 
            VosiServlet.AVAILABILITY_SUFFIX;
         table = table + "<td><a href='"+formUrl+"'>View Availability</a></td>";

         // Third column: Capabilities
         formUrl = serverRoot + catalogName + 
            VosiServlet.CAPABILITIES_SUFFIX;
         table = table + "<td><a href='"+formUrl+"'>View Capabilities</a></td>";

         // Fourth column: Tables
         formUrl = serverRoot + catalogName + 
            VosiServlet.TABLES_SUFFIX;
         table = table + "<td><a href='"+formUrl+"'>View Tables</a></td>";

         // Fifth column: CEA App
         formUrl = serverRoot + catalogName + 
            VosiServlet.CEAAPP_SUFFIX;
         table = table + "<td><a href='"+formUrl+"'>View CEA Application</a></td>";
         table = table + "</tr>\n";
      }
      table = table + "</table><br/>\n";
      return table;
   }

   /** Produces an HTML table containing links for registering the wrapped 
    * catalogs and/or editing and refreshing the registrations.
    */
   public static String getRegisterUpdateTable() throws MetadataException 
   {
      String table = 
         "<br/><table class=\"bordertable\">\n"+
         "<tr><td><b>CATALOG NAME</b></td>"+
         "<td><b>REGISTER DSA</b></td>"+
         "<td><b>UPDATE CORE METADATA</b></td>"+
         "<td><b>UPDATE SERVICE METADATA</b></td>";
      boolean gotErrors = false;
      String[] catalogNames = new String[0];
      catalogNames =
           TableMetaDocInterpreter.getCatalogNames();
      if (catalogNames.length == 0) {
         // Shouldn't get here
         throw new MetadataException("No catalogs are defined!"); 
      }
      // Get the required properties
      String serverRoot = ConfigFactory.getCommonConfig().getString(
             "datacenter.url");
      if ((serverRoot == null) || ("".equals(serverRoot))) {
         throw new MetadataException(errorMessage);
      }
      if (!serverRoot.endsWith("/")) {
         serverRoot = serverRoot + "/";
      }
      String regRoot = ConfigFactory.getCommonConfig().getString(
             "datacenter.publishing.registry");
      if ((regRoot == null) || ("".equals(regRoot))) {
         throw new MetadataException(errorMessage);
      }
      if (!regRoot.endsWith("/")) {
         regRoot = regRoot + "/";
      }
      String authID = ConfigFactory.getCommonConfig().getString(
             "datacenter.authorityId");
      if ((authID == null) || ("".equals(authID))) {
         throw new MetadataException(errorMessage);
      }
      String resKey = ConfigFactory.getCommonConfig().getString(
             "datacenter.resourceKey");
      if ((resKey == null) || ("".equals(authID))) {
         throw new MetadataException(errorMessage);
      }
      for (int i = 0; i < catalogNames.length; i++) {

         String catalogName = catalogNames[i];
         String ivorn =  "ivo://"+authID+"/"+resKey+"/"+catalogName;
         boolean isRegistered = ivornIsRegistered(ivorn,regRoot);

         // First column: catalog name 
         table = table + "<tr>\n" + "<td><b>"+catalogName+"</b></td>";

         // Second column: registration 
         if (isRegistered) {
            table = table + "<td><font color='grey' size='-1'>Already registered!</font></td>";
         }
         else {
            String formUrl = regRoot + 
               "registration/NewIdentifier?xsiType=vs:CatalogService" +
               "&authority="+authID+"&resourceKey="+resKey+"/"+catalogName+
               "&vosiURL="+serverRoot+catalogName+"/vosi/capabilities";
            table = table + "<td><a target='regwin' href='"+formUrl+"'>Register now</a></td>";
         }
         // Third column: edit core metadata
         if (isRegistered) {
            String formUrl = regRoot + "registration/DublinCore?IVORN="+ivorn;
            table = table + "<td><a target='regwin' href='"+formUrl+"'>Edit core metadata</a></td>";
         }
         else {
            table = table + "<td><font color='grey' size='-1'>Not registered yet!</font></td>";
         }
         // Fourth column: refresh service metadata (force reg. pull)
         if (isRegistered) {
            String formUrl = regRoot + 
               "admin/harvestVOSI.jsp?doharvest=true"+
               "&ident="+ivorn;
            table = table + "<td><a target='regwin' href='"+formUrl+"'>Force refresh</a></td>";
         }
         else {
            table = table + "<td><font color='grey' size='-1'>Not registered yet!</font></td>";
         }
         
         table = table + "</tr>\n";
      }
      table = table + "</table><br/>\n";
      return table;
   }
   
   private static boolean ivornIsRegistered(String ivorn, String regRoot) 
   {
      String queryRegUrl = regRoot+"services/RegistryQueryv1_0";
      try {
         RegistryService rs = RegistryDelegateFactory.createQueryv1_0(
            new java.net.URL(queryRegUrl));                          
         Document document = rs.getResourceByIdentifier(ivorn);
         if (document == null) {
            return false;
         }
         else {
            // Check not status=deleted
            Element mainElement = document.getDocumentElement();
            String status = mainElement.getAttribute("status");
            if ("deleted".equals(status)) {
               return false;
            }
            return true;
         }
      }
      catch (Exception e) {
         // KONA TOFIX DO SOMETHING MORE SENSIBLE HERE?
         return false;
      }
   }
}
