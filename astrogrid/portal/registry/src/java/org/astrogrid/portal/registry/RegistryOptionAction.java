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

import org.astrogrid.config.Config;



/**
 * Main Options component to let the user determine the next course of action which are Query, Add, or Harvest new
 * registry entry.
 *
 */
public class RegistryOptionAction extends AbstractAction
{
   /**
    * Switch for our debug statements.
    *
    */
   public static boolean DEBUG_FLAG = true;
   
   private static final String PARAM_MAIN_ELEMENT = "mainelement";   
   private static final String PARAM_CONE_SEARCH = "conesearch";
   private static final String PARAM_SIA = "sia";
   private static final String PARAM_HTTP = "paramhttp";
   
   public static Config conf = null;   
   
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
   
   public static final String RESOURCE_OPTION = "Resource";   
   public static final String ORGANISATION_OPTION = "Organisation";
   public static final String SERVICE_OPTION = "Service";
   public static final String AUTHORITY_OPTION = "Authority";
   public static final String REGISTRY_OPTION = "Registry";
   public static final String SKYSERVICE_OPTION = "SkyService";
   public static final String TABULARSKYSERVICE_OPTION = "TabularSkyService";
   public static final String DATACOLLECTION_OPTION = "DataCollection";
   
   private static final String ORGANISATION_XML_URL_TEMPLATE_PROPERTY = "org.astrogrid.registry.organisation.template.url";
   private static final String RESOURCE_XML_URL_TEMPLATE_PROPERTY = "org.astrogrid.registry.resource.template.url";
   private static final String SERVICE_XML_URL_TEMPLATE_PROPERTY = "org.astrogrid.registry.service.template.url";
   private static final String AUTHORITY_XML_URL_TEMPLATE_PROPERTY = "org.astrogrid.registry.authority.template.url";
   private static final String REGISTRY_XML_URL_TEMPLATE_PROPERTY = "org.astrogrid.registry.registry.template.url";
   private static final String SKYSERVICE_XML_URL_TEMPLATE_PROPERTY = "org.astrogrid.registry.skyservice.template.url";
   private static final String TABULARSKYSERVICE_XML_URL_TEMPLATE_PROPERTY = "org.astrogrid.registry.tabularskyservice.template.url";
   private static final String DATACOLLECTION_XML_URL_TEMPLATE_PROPERTY = "org.astrogrid.registry.datacollection.template.url";   
   
   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
      }      
   }
   

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
      ArrayList mainQueryPieces = new ArrayList();
      mainQueryPieces.add(RESOURCE_OPTION);
      mainQueryPieces.add(SERVICE_OPTION);      
      mainQueryPieces.add(ORGANISATION_OPTION);
      mainQueryPieces.add(REGISTRY_OPTION);
      mainQueryPieces.add(AUTHORITY_OPTION);
      mainQueryPieces.add(SKYSERVICE_OPTION);
      mainQueryPieces.add(TABULARSKYSERVICE_OPTION);
      mainQueryPieces.add(DATACOLLECTION_OPTION);
      request.setAttribute("MainQueryPieces",mainQueryPieces);
      
      
      //
      //Create a new HashMap for our results.  Will be used to
      //pass to the transformer (xsl page)
      Map results = new HashMap();
      return results;
   }
   
   /**
    * Method used by Query and Admin components to get a paricular template bassed off of a
    * reequest object.
    * @param request
    * @return
    */
   public static String getTemplate(Request request) {

      String mainElem = request.getParameter(PARAM_MAIN_ELEMENT);

      String templateURL = null;
      if(RegistryOptionAction.ORGANISATION_OPTION.equals(mainElem)) {         
         templateURL = conf.getString(ORGANISATION_XML_URL_TEMPLATE_PROPERTY);
      }else if(RegistryOptionAction.RESOURCE_OPTION.equals(mainElem)) {
         templateURL = conf.getString(RESOURCE_XML_URL_TEMPLATE_PROPERTY);
      } else if(RegistryOptionAction.AUTHORITY_OPTION.equals(mainElem)) {
         templateURL = conf.getString(AUTHORITY_XML_URL_TEMPLATE_PROPERTY);
      }else if(RegistryOptionAction.REGISTRY_OPTION.equals(mainElem)) {
         templateURL = conf.getString(REGISTRY_XML_URL_TEMPLATE_PROPERTY);
      }else if(RegistryOptionAction.SKYSERVICE_OPTION.equals(mainElem)) {
         templateURL = conf.getString(SKYSERVICE_XML_URL_TEMPLATE_PROPERTY);
      }else if(RegistryOptionAction.TABULARSKYSERVICE_OPTION.equals(mainElem)) {
         templateURL = conf.getString(TABULARSKYSERVICE_XML_URL_TEMPLATE_PROPERTY);
      }else if(RegistryOptionAction.DATACOLLECTION_OPTION.equals(mainElem)) {
         templateURL = conf.getString(DATACOLLECTION_XML_URL_TEMPLATE_PROPERTY);         
      }else if(RegistryOptionAction.SERVICE_OPTION.equals(mainElem)) {
         templateURL = conf.getString(SERVICE_XML_URL_TEMPLATE_PROPERTY);         
      }
      return templateURL;
   }
     
}