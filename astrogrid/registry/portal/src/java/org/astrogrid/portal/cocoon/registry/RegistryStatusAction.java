package org.astrogrid.portal.cocoon.registry;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.ObjectModelHelper;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.io.File;
import org.astrogrid.registry.client.admin.RegistryAdminService;
import org.astrogrid.registry.common.RegistryConfig;

/**
 * Main Options component to let the user determine the next course of action which are Query, Add, or Harvest new
 * registry entry.
 *
 */
public class RegistryStatusAction extends AbstractAction
{
   /**
    * Switch for our debug statements.
    *
    */
   public static boolean DEBUG_FLAG = true;
   
   private static final String PARAM_STATUS = "Status";   
   
   /**
    * Cocoon param for the user param in the session.
    *
    */
   private static final String PARAM_ACTION = "action";
   
   
   /**
    * Our action method.
    *
    */
   public Map act(
      Redirector redirector, 
      SourceResolver resolver, 
      Map objectModel, 
      String source, 
      Parameters params)
      {
      
      //
      // Get our current request and session.
      Request request = ObjectModelHelper.getRequest(objectModel);
      Session session = request.getSession();

      //put the main templates into an arraylsit to for displaying.
      RegistryConfig.loadConfig();
      String url = url = RegistryConfig.getProperty("publish.registry.update.url");
      RegistryAdminService ras = new RegistryAdminService(url);
      String status = ras.getStatus();
      request.setAttribute(PARAM_STATUS,status);

      //
      //Create a new HashMap for our results.  Will be used to
      //pass to the transformer (xsl page)
      Map results = new HashMap();
      return results;
   }

}