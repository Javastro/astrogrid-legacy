/*
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.metadata;
import org.astrogrid.cfg.ConfigFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.astrogrid.registry.client.query.v1_0.RegistryService;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;

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

	/**
   * Supplies the base URI of the HTTPS installation from the service
   * configuratation. The URI always ends in a slash.
   *
   * @return The URI
   */
	public static String getInstallationSecureBaseURL() throws MetadataException {
		String serverRoot =
        ConfigFactory.getCommonConfig().getString("datacenter.url.secure");
    if ((serverRoot == null) || ("".equals(serverRoot))) {
      throw new MetadataException(errorMessage);
    }
		if (!serverRoot.endsWith("/")) {
			serverRoot = serverRoot+"/";
		}
		return serverRoot;
	}


   /** Produces an HTML table containing links for registering the wrapped 
    * catalogs and/or editing and refreshing the registrations.
    */
   public static String getRegisterUpdateTable() throws MetadataException 
   {
     
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

     StringBuilder table = new StringBuilder(4096);
     table.append("<table>\n");
     addRegistrationTableRow(table,
                             regRoot,
                             serverRoot,
                             authID,
                             resKey,
                             null);
     for (int i = 0; i < catalogNames.length; i++) {
       String catalogName = catalogNames[i];
       addRegistrationTableRow(table,
                               regRoot,
                               serverRoot,
                               authID,
                               resKey,
                               catalogName);
     }
      
     table.append("</table>\n");
     return table.toString();
  }

  private static void addRegistrationTableRow(StringBuilder table,
                                              String        regRoot,
                                              String        serverRoot,
                                              String        authID,
                                              String        resKey,
                                              String        catalogName) {

    table.append("<tr>\n");

    String ivorn = (catalogName == null)?
        "ivo://"+authID+"/"+resKey :
        "ivo://"+authID+"/"+resKey+"/"+catalogName;
    boolean isRegistered = ivornIsRegistered(ivorn, regRoot);

    // First column: catalog name
    String label = (catalogName == null)? 
      "All catalogues on one service" :
      "Virtual service for catalogue " + catalogName;
    table.append(String.format("<td><b>%s</b></td>\n", label));

    // Second column: action buttons.
    table.append("<td>\n");
    if (isRegistered) {
      String uri1 = regRoot + "registration/DublinCore";
      table.append(String.format("<td><form method='get' action='%s'>", uri1));
      table.append(String.format("<input type='hidden' name='IVORN', value='%s'/>", ivorn));
      table.append("<input type='submit' value='Edit core metadata'/>");
      table.append("</form>\n");

          /*
          String uri2 = regRoot + "admin/harvestVOSI.jsp";
          table.append(String.format("<td><form method='post' action='%s'>", uri2));
          table.append(String.format("<input type='hidden' name='ident', value='%s'/>", ivorn));
          table.append("<input type='hidden' name='doHarvest', value='true/>");
          table.append("<input type='submit' value='Reload service metadata'/>");
          table.append("</form>\n");
           */

      String uri2 = regRoot + "registration/ServiceMetadata";
      table.append(String.format("<td><form method='post' action='%s'>", uri2));
      table.append(String.format("<input type='hidden' name='IVORN', value='%s'/>", ivorn));
      String vosiUrl = (catalogName == null)?
          serverRoot + "VOSI/capabilities" :
          serverRoot + "VOSI/capabilities?COLLECTION=" + catalogName;
      table.append(String.format("<input type='hidden' name='VOSI_Capabilities', value='%s'/>", vosiUrl));
      table.append("<input type='submit' value='Load service metadata'/>");
      table.append("</form>\n");
    }
    else {
      String uri = regRoot + "registration/NewIdentifier";
      String key = (catalogName == null)? resKey : resKey + "/" + catalogName;
      table.append(String.format("<td><form method='post' action='%s'>", uri));
      table.append("<input type='hidden' name='xsiType', value='vs:CatalogService'/>");
      table.append(String.format("<input type='hidden' name='authority', value='%s'/>", authID));
      table.append(String.format("<input type='hidden' name='resourceKey', value='%s'/>", key));
      table.append("<input type='submit' value='Register new resource'/>");
      table.append("</form>\n");
    }
    table.append("</td>\n");
    table.append("</tr>\n");
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

  /**
   * Determines whether cone searches are secure on this installation.
   * (An installation is not allowed to have a mix of secure and insecure
   * cone-search endpoints.)
   *
   * @return true if cone searches are secure.
   */
  public static boolean isConeSearchSecure() {
    return ConfigFactory.getCommonConfig().getBoolean("cone.search.secure", false);
  }
}
