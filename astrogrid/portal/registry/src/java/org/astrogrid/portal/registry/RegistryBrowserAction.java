package org.astrogrid.portal.registry;

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
import java.util.TreeMap;
import java.util.Set;
import java.util.Iterator;
import java.io.File;
import java.io.IOException;
import org.astrogrid.registry.client.admin.RegistryAdminDocumentHelper;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.apache.commons.logging.Log;
import org.apache.axis.components.logger.LogFactory;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.RegistryService;

import org.astrogrid.registry.NoResourcesFoundException;
import org.astrogrid.registry.RegistryException;

import org.astrogrid.config.Config;

import org.astrogrid.util.DomHelper;
import org.astrogrid.registry.common.WSDLBasicInformation;
import org.astrogrid.store.Ivorn;

import java.io.InputStream;
import java.net.URL;


/**
 *
 *
 */
public class RegistryBrowserAction extends AbstractAction
{
   private static Log log = LogFactory.getLog("RegistryBrowserAction");
   /**
    * Switch for our debug statements.
    *
    */
   public static boolean DEBUG_FLAG = true;
   public static boolean STACK_TRACE = false;
   
   public static final String PARAM_MAIN_ELEMENT = "mainelement";   
   
   /**
    * Cocoon param for the user param in the session.
    *
    */
/* First the Actions */
   public static final String PARAM_ACTION = "action";
   
   public static final String QUERY_ACTION = "queryregistry";

   public static final String SELECT_ACTION = "selectentry";

   public static final String CONFIRM_ACTION = "Verify";

/* Now the parameters passed back to Cocoon */
   public static final String PARAM_SERVER = "Server";
   
   public static final String PARAM_ID = "identifier";
   
   public static final String PARAM_TITLE = "title";
   
   public static final String PARAM_AUTHORITY_ID = "authId";
   
   public static final String PARAM_RESOURCE_KEY = "resourceKey";
   
   public static final String PARAM_COLUMN = "column";
      
   public static final String PARAM_RESULT_LIST = "resultlist";
      
   public static final String QUERY_RESULT = "queryresult";
      
   public static final String RESULT_IDENTIFIER = "resultidentifier";
      
   public static final String CATALOG_SEARCH = "Catalog";

   public static final String TOOL_SEARCH = "Tool";

   public static final String ERROR_MESSAGE = "errormessage";
   
   public static Config conf = null;   
   
   //* This is a list of current servers (should be in Config file) */
   private static int NSERVERS = 5;
   private static final String SERVERS[][] = {
      {"localhost",
       "localhost:8080/astrogrid-registry-SNAPSHOT" },
      {"RAL",
       "stargrid1.bnsc.rl.ac.uk:8080/astrogrid-registry-webapp" },
      {"Leicester",
       "twmbarlwm.astrogrid.org:8080/astrogrid-registry-SNAPSHOT" },
      {"Manchester",
       "astrogrid3.jb.man.ac.uk:8080/astrogrid-registry-SNAPSHOT" },
      {"MSSL",
       "msslxy.mssl.ucl.ac.uk:8080/astrogrid-registry-SNAPSHOT" },
      {"Cambridge", "" },
      {"Edinburgh", "" },
      {"", "" }
   };   
  
   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
      }      
   }
 

   /**
    * Action page to do a query.
    *
    */
   public Map act(
      Redirector redirector, 
      SourceResolver resolver, 
      Map objectModel, 
      String source, 
      Parameters params) {
      
      //
      // Get our current request and session.
      Request request = ObjectModelHelper.getRequest(objectModel);
      Session session = request.getSession();
      String errorMessage = null;
      int crit_number = 0;
      Document registryDocument = null;
      // LinkedHashMap compareTypeList = new LinkedHashMap();            
      // ArrayList joinTypes = new ArrayList();
      LinkedHashMap Servers = new LinkedHashMap();
      ArrayList resultlist = null;      
      String result = null;
      String resultid = null;
      String method = "Action()";

      // Get paramters from Cocoon           
      String action = request.getParameter(PARAM_ACTION);
      String mainElem = request.getParameter(PARAM_MAIN_ELEMENT);
      String identifier = request.getParameter(PARAM_ID);
      String authid = request.getParameter(PARAM_AUTHORITY_ID);
      String resourcekey = request.getParameter(PARAM_RESOURCE_KEY);
      String title = request.getParameter(PARAM_TITLE);
      String server = request.getParameter(PARAM_SERVER);

     
      if(DEBUG_FLAG) {
         printDebug( method, "the action is = " + action );      
         printDebug( method, "the mainElem = " + mainElem );
         printDebug( method, "the server = " + server );
         printDebug( method, "the authority = " + identifier );
         printDebug( method, "the title = " + title );
		 printDebug( method, "the authid = " + authid );
		 printDebug( method, "the resourcekey = " + resourcekey );
      }            

      String endpoint = null;

      // Check for server name and get web service
      for ( int i = 0; i < NSERVERS; i++ ) {
         Servers.put( SERVERS[i][0],
                  "http://" + SERVERS[i][1] + "/services/Registry");
         printDebug( method, "Server " + i + " = " +
                                     SERVERS[i][0] + " (" +SERVERS[i][1] +")");
         if ( SERVERS[i][0].equals(server) )
            endpoint="http://" + SERVERS[i][1] + "/services/Registry";
      }
      request.setAttribute("Servers", Servers);       

      printDebug( method, "Service = " + endpoint);

      // Initial Query Action.
      if( QUERY_ACTION.equals(action) ) {

         // Lets build up the XML for a query.
         String query = buildQuery( mainElem, identifier, title );
         printDebug( method, "Query = \n" + query);

         try {
            // Now lets submit the query.
            URL url = new URL( endpoint );               
            RegistryService rs = RegistryDelegateFactory.createQuery(url);
            printDebug( method, "Service = " + rs);
            //Document doc = rs.submitQuery( query );
            Document doc = rs.submitQueryStringDOM( query );

            //create the results and put it in the request.
            resultlist = createList( doc );
            request.setAttribute("resultlist", resultlist);

            printDebug( method, "Number of Result = " + resultlist.size());
            if( resultlist.size() == 0 )
               throw new NoResourcesFoundException("No Results found");

         } catch( NoResourcesFoundException nrfe ) {
            errorMessage = "Your query produced no results." ;
            printDebug( method, errorMessage + " : " + nrfe );
            if ( STACK_TRACE) nrfe.printStackTrace();
         } catch( RegistryException re ) {
            errorMessage =
              "A error occurred in processing your query with the Registry.";
            printDebug( method, errorMessage + " : " + re );
            if ( STACK_TRACE) re.printStackTrace();
         } catch( Exception e ) {
            errorMessage = "An exception occurred. " + e;
            printDebug( method, errorMessage );
            if ( STACK_TRACE) e.printStackTrace();
         }
         action = SELECT_ACTION;
      } else if ( SELECT_ACTION.equals(action) ) {
         resultid = request.getParameter( PARAM_ID );
         printDebug( method, "Result id = " + resultid );
      } else if ( action == null ) {
         printDebug( method, "First Time set action" );
         action = QUERY_ACTION;
      }
      
      // set up the return parameters
      request.setAttribute( PARAM_ACTION, action );
      request.setAttribute( PARAM_MAIN_ELEMENT, mainElem );
      request.setAttribute( PARAM_AUTHORITY_ID, authid );
      request.setAttribute( PARAM_RESOURCE_KEY, resourcekey );
      request.setAttribute( ERROR_MESSAGE, errorMessage );

      //Create a new HashMap for our results.  Will be used to
      //pass to the transformer (xsl page)
      Map results = new HashMap() ;
      results.put( PARAM_MAIN_ELEMENT, mainElem );
      results.put( PARAM_RESULT_LIST, resultlist );
      results.put( PARAM_AUTHORITY_ID, authid );
      results.put( PARAM_RESOURCE_KEY, resourcekey  );
      results.put( ERROR_MESSAGE, errorMessage );
      if( result != null && result.length() > 0 ) {
         results.put( QUERY_RESULT, result );
      }
      return results;
      
   }
   

   /**
    * This method gets a list of key elements from the result document.
    * @param doc Query results in a DOM tree format.
    * @return ArrayList of relevant the String XML results.
    */
   private ArrayList createList( Document doc ) {
      NodeList nodes = doc.getDocumentElement().getChildNodes();
      return listNodes( nodes );
   }
   private ArrayList createList( Document doc, String Elem ) {
      NodeList nodes = doc.getElementsByTagName(Elem);
      return listNodes( nodes );
   }

   private ArrayList listNodes( NodeList nl ) {
      ArrayList al = new ArrayList();
      for(int i = 0; i < nl.getLength(); i++) {
         String element = null;
         Node node = nl.item(i);
         if( node instanceof org.w3c.dom.Element ) {
            element = XMLUtils.ElementToString( (Element) node );
            al.add( element );
         } 
         if (DEBUG_FLAG && i < 2 )
            printDebug( "CreateList", "Result " + i + " ("
                        + node.getNodeType() + ") = " + element);
      }
      return al;
   }
   
   /**
    * This method build the query string with the specified criteria.
    * @param mainElem the type of query (either Catalog or Resource).
    * @param identifier keywords in the identifier.
    * @param title keywords in the description.
    * @return the Query as a String.
    */
   private String buildQuery( String main, String identifier,
                              String title ) {
     // Lets build up the XML for a query.
     String query = "<query>\n<selectionSequence>"
      + "\n<selection item='searchElements' itemOp='EQ' value='Resource'/>";
     if ( CATALOG_SEARCH.equals( main ) ) {
       query += "\n<selectionOp op='$and$'/>" +
                "\n<selectionSequence>\n" +
                "<selection item='Type' itemOp='EQ' value='Catalog'/>" +
                "<selectionOp op='OR'/>" +
                "<selection item='Type' itemOp='EQ' value='Archive'/>" +
                "\n</selectionSequence>";
     }
     else if ( TOOL_SEARCH.equals( main ) ) {
       query += "\n<selectionOp op='$and$'/>" +
     "\n<selection item='@*:type' itemOp='EQ' value='CeaApplicationType'/>";
     }

     // Now lets check for other filters.
     if ( identifier != null && identifier.length() > 0 )
        query += "\n<selectionOp op='AND'/>" +
"<selection item='AuthorityID' itemOp='CONTAINS' value='"+identifier+"'/>";
     if ( title != null && title.length() > 0 )
        query += "\n<selectionOp op='AND'/>" +
           "<selection item='Title' itemOp='CONTAINS' value='"+title+"'/>";

     // End of Query.
     query += "\n</selectionSequence></query>";

     return query;
   }

   private String getResultMessage(Document doc) {
      String message = null;
      NodeList nl = doc.getElementsByTagName("error");      
      if(nl != null && nl.getLength() > 0) {
         message = nl.item(0).getFirstChild().getNodeValue();
      }//if
      return message;  
   }
   
   private String getId( Document doc ) {
      String id = null;
      NodeList nl = doc.getElementsByTagName("Identifier");      
      if( nl != null && nl.getLength() > 0 ) {
         id = nl.item(0).getFirstChild().getNodeValue();
      }
      nl = doc.getElementsByTagName("AuthorityId");      
      if( nl != null && nl.getLength() > 0 ) {
         id = nl.item(0).getFirstChild().getNodeValue();
      }
      nl = doc.getElementsByTagName("ResourceKey");      
      if( nl != null && nl.getLength() > 0 ) {
         id += "/" + nl.item(0).getFirstChild().getNodeValue();
      }
      return id;  
   }
   
   /**
      * Small convenience method to print DEBUG Messages.
      * Now converted to use commons.logging
      * @param method The java function
      * @param message The Debug message
      */   
   
   private void printDebug (String method, String message) {
      if( DEBUG_FLAG ) {
         log.debug( method + " : \n" + message );      
         System.out.println( method + " : " + message );      
      }      
   }
   private void printMessage (String message) {
      log.info( message );      
   }     
}
