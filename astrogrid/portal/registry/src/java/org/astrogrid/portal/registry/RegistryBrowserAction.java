package org.astrogrid.portal.registry;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.astrogrid.query.sql.Sql2Adql;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.Set;
import java.util.Iterator;
import java.io.File;
import java.io.IOException;
//import org.astrogrid.registry.client.admin.RegistryAdminDocumentHelper;
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
import org.astrogrid.util.DomHelper;

import org.astrogrid.registry.NoResourcesFoundException;
import org.astrogrid.registry.RegistryException;

import org.astrogrid.config.Config;

import org.astrogrid.util.DomHelper;
//import org.astrogrid.registry.common.WSDLBasicInformation;
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
   
	 public static final String TABLE_ACTION = "getTable";
   
/* Following change done by PFO to uniform the UniqueTableID using !
 * instead of / as component separators
 */
/*	 public static final String SEPARATOR = "/";   */
	 public static final String SEPARATOR = "!";   

	 public static final String SEPARATORHACK = "!";   

/* Now the parameters passed back to Cocoon */
	 public static final String PARAM_SERVER = "Server";
   
	 public static final String PARAM_ID = "identifier";
   
	 public static final String UNIQUE_ID = "uniqueID";
   
	 public static final String PARAM_TITLE = "title";
   
	 public static final String PARAM_AUTHORITY_ID = "authId";
	
	 public static final String PARAM_PARENT_AUTHORITY_ID = "parent_authId";   
   
	 public static final String PARAM_RESOURCE_KEY = "resourceKey";
   
	 public static final String PARAM_COLUMN = "column";
      
	 public static final String PARAM_COLUMN_NAME = "colname";
      
	 public static final String PARAM_TABLE_NAME = "tabname";
      
	 public static final String PARAM_UCD = "colucd";
      
	 public static final String PARAM_UNITS = "colunits";
      
	 public static final String PARAM_Description = "coldescr";
      
	 public static final String PARAM_RESULT_LIST = "resultlist";
      
	 public static final String QUERY_RESULT = "queryresult";
      
	 public static final String RESULT_IDENTIFIER = "resultidentifier";
      
	 public static final String CATALOG_SEARCH = "Catalog";

	 public static final String TOOL_SEARCH = "Tool";

	 public static final String TABLE_SEARCH = "Table";

	 public static final String ERROR_MESSAGE = "errormessage";
   
	 public static final String SESSION_TABLEID = "tableID";
	 public static final String SESSION_SINGLE_CATALOG = "resultSingleCatalog";
	 public static final String SESSION_UNIQUEID = "uniqueID";   
   
	 public static Config conf = null;   
     
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
			ArrayList resultlist = null;
			NodeList resultNodes = null;     
			String result = null;
			String resultid = null;
			String method = "Action()";
         String adqlString = null;

			// Get paramters from Cocoon           
			String action = request.getParameter(PARAM_ACTION);
			String mainElem = request.getParameter(PARAM_MAIN_ELEMENT);
			String identifier = request.getParameter(PARAM_ID);
			if ( identifier != null && (identifier.length() == 0
							 || identifier.equals("null") ) ) identifier = null;
			String authid = request.getParameter(PARAM_AUTHORITY_ID);
			if ( authid != null && ( authid.length() == 0
							 || authid.equals("null") ) ) authid = null; 
		String parent_authid = request.getParameter(PARAM_PARENT_AUTHORITY_ID);
		if ( parent_authid != null && ( parent_authid.length() == 0
					|| parent_authid.equals("null") ) ) parent_authid = null;                
			String resourcekey = request.getParameter(PARAM_RESOURCE_KEY);
			if ( resourcekey != null && ( resourcekey.length() == 0
							 || resourcekey.equals("null") ) ) resourcekey = null;
			String title = request.getParameter(PARAM_TITLE);
			if ( title != null && ( title.length() == 0
							 || title.equals("null") ) ) title = null;
			String colname = request.getParameter(PARAM_COLUMN_NAME);
			if ( colname != null && ( colname.length() == 0
							 || colname.equals("null") ) ) colname = null;
			String tabname = request.getParameter(PARAM_TABLE_NAME);
			if ( tabname != null && ( tabname.length() == 0
							 || tabname.equals("null") ) ) tabname = null;
			String ucd = request.getParameter(PARAM_UCD);
			if ( ucd != null && ( ucd.length() == 0
							 || ucd.equals("null") ) ) ucd = null;
			String units = request.getParameter(PARAM_UNITS);
			if ( units != null && ( units.length() == 0
							 || units.equals("null") ) ) units = null;
			String coldesc = request.getParameter(PARAM_Description);
			if ( coldesc != null && ( coldesc.length() == 0
							 || coldesc.equals("null") ) ) coldesc = null;

     
			if(DEBUG_FLAG) {
				 printDebug( method, "the action is = " + action );      
				 printDebug( method, "the mainElem = " + mainElem );
				 printDebug( method, "the title = " + title );
		 printDebug( method, "the authid = " + authid );
		 printDebug( method, "the parent_authid = " + parent_authid );
		 printDebug( method, "the resourcekey = " + resourcekey );
		 printDebug( method, "the identifier = " + identifier );
		 printDebug( method, "column name = " + colname );
		 printDebug( method, "table name = " + tabname );
		 printDebug( method, "UCD = " + ucd );
		 printDebug( method, "units = " + units );
		 printDebug( method, "descr = " + coldesc );
			}            

			// Initial Query Action.
			if( QUERY_ACTION.equals(action) ) {

				 // Lets build up the XML for a query.
				 String query = buildQuery( mainElem, authid, resourcekey,
																							title, tabname, colname,
																							ucd, units, coldesc );
				 printDebug( method, "Query = \n" + query);

				 try {
						// Now lets submit the query.
						RegistryService rs = RegistryDelegateFactory.createQuery( );
						printDebug( method, "Service = " + rs);
                  adqlString = Sql2Adql.translateToAdql074(query);                        
						//Document doc = rs.submitQuery( query );
                  Document doc = rs.search( adqlString );
						request.setAttribute("resultDoc", (Node) doc);

						//create the results and put it in the request.
						resultlist = createList( doc );
						resultNodes = createNodes( doc );
						request.setAttribute("resultlist", resultlist);
						request.setAttribute("resultNodes", resultNodes);

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
	  
		} else if ( TABLE_ACTION.equals(action) ) {
				try 
				{	
					String table = "";		 
					printDebug( method, "In table action!!" ); 
					String uniqueID = request.getParameter(UNIQUE_ID);
					String tableQuery = null;
					if (uniqueID != null && uniqueID.length() > 0)
					{
							printDebug( method, "uniqueID = " + uniqueID);  
							String authorityID = uniqueID.substring( 0,
																				 uniqueID.indexOf(SEPARATOR));
					printDebug( method, "uniqueID - auth = " + authorityID );
							String resourceKey = uniqueID.substring(
																				 uniqueID.indexOf(SEPARATOR)+1,
																				 uniqueID.lastIndexOf(SEPARATOR)
																			 );
					printDebug( method, "uniqueID - res = " + resourceKey);
/*
 * the next line was returned to its "initial" state by PFO to make sure of
 * consistency in registry functions, yet knowing that we need to find
 * another solution!
 */
 /*
							table = uniqueID.substring( uniqueID.lastIndexOf(SEPARATORHACK)+1
							*/
							table = uniqueID.substring( uniqueID.lastIndexOf(SEPARATOR)+1
																						).trim();
					printDebug( method, "uniqueID -table = " + table);	

							tableQuery = buildQuery( TABLE_SEARCH, authorityID, 
																					 resourceKey, null, null, null,
																					 null, null, null);

					printDebug( method, "tableQuery = " + tableQuery);
				
					}	
							  
				RegistryService rs = RegistryDelegateFactory.createQuery();
				printDebug( method, "Service = " + rs);
				//Document doc = rs.submitQuery( tableQuery );
            adqlString = Sql2Adql.translateToAdql074(tableQuery);                
            Document doc = rs.search( adqlString );
			 			 
				//request.setAttribute("tableID", table);			  
				//request.setAttribute("resultSingleCatalog", doc);
				session.setAttribute(SESSION_TABLEID, table);
				session.setAttribute(SESSION_SINGLE_CATALOG, doc);
				session.setAttribute(SESSION_UNIQUEID, uniqueID);
			  
					} 
					catch( NoResourcesFoundException nrfe ) 
					{
					errorMessage = "Your query produced no results." ;
				printDebug( method, errorMessage + " : " + nrfe );
				if ( STACK_TRACE) nrfe.printStackTrace();
			} 
			catch( RegistryException re ) 
			{
				 errorMessage =
		"A error occurred in processing your query with the Registry.";
				 printDebug( method, errorMessage + " : " + re );
				 if ( STACK_TRACE) re.printStackTrace();
			} 
			catch( Exception e ) 
			{
				 errorMessage = "An exception occurred. " + e;
			 printDebug( method, errorMessage );
			 if ( STACK_TRACE) e.printStackTrace();
			}

			} else if ( action == null ) {
				 printDebug( method, "First Time set action" );
				 action = QUERY_ACTION;
			}
      
			// set up the return parameters
			request.setAttribute( PARAM_ACTION, action );
			request.setAttribute( PARAM_MAIN_ELEMENT, mainElem );
			request.setAttribute( PARAM_AUTHORITY_ID, authid );
		request.setAttribute( PARAM_PARENT_AUTHORITY_ID, parent_authid );
			request.setAttribute( PARAM_RESOURCE_KEY, resourcekey );
			request.setAttribute( ERROR_MESSAGE, errorMessage );

			//Create a new HashMap for our results.  Will be used to
			//pass to the transformer (xsl page)
			Map results = new HashMap() ;
			results.put( PARAM_MAIN_ELEMENT, mainElem );
			results.put( PARAM_RESULT_LIST, resultlist );
			results.put( PARAM_AUTHORITY_ID, authid );
		results.put( PARAM_PARENT_AUTHORITY_ID, parent_authid );
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
	 private NodeList createNodes( Document doc ) {
			NodeList nodes = doc.getDocumentElement().getChildNodes();
			return nodes;
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
		* This method build the query string for a Authentification key.
		* @param main the type of query (either Catalog or Resource).
		* @param id identifier keywords.
		* @param key keywords in the description.
		* @return the Query as a String.
		*/
	 private String buildQuery( String main, String id, String key ) {
	 return buildQuery( main, id, key, null, null, null, null, null, null);
	 }

	 /**
		* This method build the query string with the specified criteria.
		* @param main the type of query (either Catalog or Resource).
		* @param id keywords in the identifier.
		* @param key keywords in the description.
		* @param title keywords in the description.
		* @param tabname keywords in the description.
		* @param colname keywords in the description.
		* @param ucd keywords in the description.
		* @param units keywords in the description.
		* @return the Query as a String.
		*/
	 private String buildQuery( String main, String id, String key,
															String title, String tabname, String colname,
															String ucd, String units, String desc ) {
		 printDebug( "BuildQuery", main + " : " + id + "/" + key + " : " + title
																		+ " : " + tabname + " : " + colname );
		 // Lets build up the XML for a query.
		 String query = "<query>\n<selectionSequence>";
		 query += "\n<selection item='searchElements' itemOp='EQ' value='Resource'/>";
		 query += "\n<selectionOp op='$and$'/>";
       String sqlQuery = "Select * from Registry where ";
		 if ( CATALOG_SEARCH.equals( main ) ) {
             sqlQuery += "vr:content/vr:type='Catalog' ";
		 }
		 else if ( TOOL_SEARCH.equals( main ) ) {
             sqlQuery += "@xsi:type='cea:CeaApplicationType' ";
//		Following removed as there seems to be a confusion over id or key and the value of
//		authid was getting included as part of the search string in the form:
//				 vr:Identifier/vr:AuthorityID = "tool_name" which prevented anything from being found!
//		pjn 26/10/04
//			 key = null;       
			}
		 else if ( TABLE_SEARCH.equals( main ) ) {
          sqlQuery += "vr:content/vr:type='Catalog' ";
		 }

		 // Now lets check for other filters.
		 if ( id != null ) {
          sqlQuery += " and vr:identifier like '%" + id +"%' ";
		 }
		 if ( key != null ) {
          sqlQuery += " and vr:identifier like '%" + key +"%' ";
		 }

		 if ( title != null ) {
          sqlQuery += " and vr:title like '%" + title +"%' ";             
		 }
		 if ( tabname != null ) {
          sqlQuery += " and (vs:table/vs:name = '" + tabname + "' or tdb:db/tdb:table/vs:name = '" + tabname + "') ";
		 }
		 if ( colname != null ) {
          sqlQuery += " and (vs:table/vs:column/vs:name = '" + colname + "' or tdb:db/tdb:table/vs:column/vs:name = '" + colname + "') ";             
          
		 }
		 if ( ucd != null ) {
          sqlQuery += " and (vs:table/vs:column/vs:ucd = '" + ucd + "' or tdb:db/tdb:table/vs:column/vs:ucd = '" + ucd + "') ";
		 }
		 if ( units != null ) {
          sqlQuery += " and (vs:table/vs:column/vs:unit = '" + units + "' or tdb:db/tdb:table/vs:column/vs:unit = '" + units + "') ";
		 }
		 if ( desc != null ) {
          sqlQuery += " and (vs:table/vs:column/vs:description like '%" + desc + "%' or tdb:db/tdb:table/vs:column/vs:description like '%" + desc + "%') ";             
		 }
       return sqlQuery;
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
