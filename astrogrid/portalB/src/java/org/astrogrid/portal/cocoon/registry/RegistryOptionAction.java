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

/**
 *
 *
 */
public class RegistryOptionAction extends AbstractAction
{
   /**
    * Switch for our debug statements.
    *
    */
   public static boolean DEBUG_FLAG = true;
   
   /**
    * Cocoon param for the user param in the session.
    *
    */
   private static final String PARAM_ACTION = "action";
   
   private static final String PARAM_CRITERIA_NUMBER = "criteria_number";   

   private static final String PARAM_MAIN_SEARCH_ELEMENT = "searchelement";   
   
   private static final Integer DEFAULT_CRITERIA_NUMBER = new Integer(1);
   
   private static final String ADD_ACTION = "add";
   
   private static final String UPDATE_ACTION = "update";   
   
   private static final String REMOVE_ACTION = "remove";   

   private static final String ADD_CRITERIA_ACTION = "addcriteria";   
   
   public static final String ORGANISATION_OPTION = "Organisation";
   public static final String AUTHORITY_OPTION = "Authority";
   public static final String REGISTRY_OPTION = "Registry";
   public static final String SKYSERVICE_OPTION = "SkyService";
   public static final String TABULARSKYSERVICE_OPTION = "TabularSkyService";
   public static final String DATACOLLECTION_OPTION = "DataCollection";

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


      ArrayList mainQueryPieces = new ArrayList();
      mainQueryPieces.add("Organisation");
      mainQueryPieces.add("Registry");
      mainQueryPieces.add("Authority");
      mainQueryPieces.add("SkyService");
      mainQueryPieces.add("TabularSkyService");
      mainQueryPieces.add("DataCollection");
      request.setAttribute("MainQueryPieces",mainQueryPieces);
      
      
      //
      //Create a new HashMap for our results.  Will be used to
      //pass to the transformer (xsl page)
      Map results = new HashMap() ;
      //results.put(PARAM_CREDENTIAL,credential);
      return results;
   }  
}