package org.astrogrid.portal.resources;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.ObjectModelHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.axis.components.logger.LogFactory;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.util.DomHelper;

//import org.astrogrid.registry.client.admin.RegistryAdminDocumentHelper;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.registry.RegistryException;

import org.astrogrid.query.sql.Sql2Adql;

import org.astrogrid.config.Config;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;

import org.astrogrid.util.DomHelper;
//import org.astrogrid.registry.common.WSDLBasicInformation;
import org.astrogrid.store.Ivorn;


/**
 * @author Phil Nicolson (pjn3@star.le.ac.uk) Jan 05
 * @version $Name:  $Revision: 1.12 $Date:
 * @version $Name:  $Revision: 1.12 $Date:
 */
public class ResourceAction extends AbstractAction {

  /** Compile-time switch used to turn tracing on/off. 
   * Set this to false to eliminate all trace statements within the byte code.*/
  private static final boolean TRACE_ENABLED = false;

  /** Compile-time switch used to turn certain debugging statements on/off. 
   * Set this to false to eliminate these debugging statements within the byte code.*/
  private static final boolean DEBUG_ENABLED = false;

  private static Log log = LogFactory.getLog("ResourceAction");     

  public static final String 
      ACTION_PARAM_TAG = "action" ,
      ACTION_CATALOG_QUERY = "search for catalogs" ,
      ACTION_TASK_QUERY = "search for tasks" ,
      ACTION_FILESTORE_QUERY = "filestore_query" ,
	  ERROR_MESSAGE_PARAMETER = "resource_error_message" ,
	  ERROR_INFO_PARAMETER = "resource_info_message" ,
	  PARAM_RESULT_COUNT = "resource_result_count" ;

  // General constraints
  public static final String
      PARAM_RESOURCE_NAME = "resourceNameField",
      PARAM_DESCRIPTION = "descriptionField",
      PARAM_PUBLISHER = "publisherField",
      PARAM_TITLE = "titleField",
      PARAM_CONSTRAINT_JOIN = "ConstraintAndOr",
      PARAM_CONSTRAINT_ALL = "finalAndOr",
  // Wavelength
      PARAM_WAVELENGTH = "wavelength" ,
      PARAM_WAVELENGTH_JOIN = "WavelengthAndOr",
  // Mission
      PARAM_MISSION = "mission" ,
      PARAM_MISSION_JOIN = "MissionAndOr",
  // Keyword
      PARAM_KEYWORD = "keyword",
      PARAM_KEYWORD_JOIN = "KeywordAndOr",
      
  // Task
      PARAM_AUTHORITY_FIELD = "taskNameField",
      PARAM_TASK_JOIN = "TaskAndOr", 
      
  // Microbrowser
	  PARAM_MAIN_ELEMENT = "mainelement",
	  PARAM_PARENT_AUTHORITY_ID = "parent_authId";

  public static Config conf = null;   
     
  static {
    if(conf == null) {
      conf = org.astrogrid.config.SimpleConfig.getSingleton();
    }      
  }
 

  /**
  * Our action method.
  *
  */
  public Map act ( Redirector redirector,
                   SourceResolver resolver,
                   Map objectModel,
                   String source,
                   Parameters params ) {                  
     if( TRACE_ENABLED ) trace( "ResourceAction.act() entry" );  

     ResourceActionImpl myAction = null;
     Map retMap = null;

     try {     
        myAction = new ResourceActionImpl( redirector,
                                           resolver,
                                           objectModel,
                                           source,
                                           params );

        retMap = myAction.act();                                            
     }
     catch ( Exception ex ) {
        ex.printStackTrace();
     }
     finally {
        if( TRACE_ENABLED ) trace( "ResourceAction.act() exit" );  
     }

     return retMap; 
 
  } // end of act()
  
  private class ResourceActionImpl {
        
    private Redirector redirector;
    private SourceResolver resolver;
    private Map objectModel, results;
    private String source;
    private Parameters params;
    private Request request;
    private Session session;    
    private String action;     
        
    public ResourceActionImpl( Redirector redirector,
                               SourceResolver resolver,
                               Map objectModel,
                               String source,
                               Parameters params ) {                                   
      if( TRACE_ENABLED ) trace( "ResourceActionImpl() entry" ); 
            
      try {          
        this.redirector = redirector;
        this.resolver = resolver;
        this.objectModel = objectModel;
        this.source = source;
        this.params = params;         
        this.results = new HashMap();
            
        // Get current request and session.
        this.request = ObjectModelHelper.getRequest( objectModel );
        this.session = request.getSession();
            
        this.action = request.getParameter( ACTION_PARAM_TAG );              
                    
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
          
      finally {
        if( TRACE_ENABLED ) trace( "ResourceActionImpl() exit" ); 
      }
              
    } // end of ResourceActionImpl()
        
        
    public Map act() {
      if( TRACE_ENABLED ) trace( "ResourceActionImpl.act() entry" );      
        
      try {

        debug( "action is: " + action );
                    
        if( action == null )
        {
          // first time into resource browser - need to set attributes to empty string to prevent front end displaying them as null
		  session.setAttribute( ERROR_MESSAGE_PARAMETER, "" ) ;
		  session.setAttribute( ERROR_INFO_PARAMETER, "" ) ;
		  session.setAttribute( PARAM_RESULT_COUNT, "" );           
        }      
              
        else if ( ACTION_CATALOG_QUERY.equals(action) )  {
          this.catalogQuery();
        }
        else if ( ACTION_TASK_QUERY.equals(action) )  { 
          this.taskQuery();                   
        }
        else if ( ACTION_FILESTORE_QUERY.equals(action) )  {
          this.filestoreQuery(); 
        }
        else {
          results = null;
          debug( "unsupported action"); 
          throw new UnsupportedOperationException(
                 action + " no longer supported" );
        }
      }
      catch( ConsistencyException cex ) {
        results = null;
        debug( "ConsistencyException occurred");
      }
      //PJN Note: these should only be here during testing...
      catch( Exception ex) {
        results = null;
        debug( "Exception: ex" );
        ex.printStackTrace();
      }
      //PJN Note: these should only be here during testing...
      catch( Throwable th ) {
        results = null;
        debug( "Throwable th" );
        th.printStackTrace();
      }
      finally {
        if( TRACE_ENABLED ) trace( "ResourceActionImpl.act() exit" );  
      }

	  // Added for mb 
	  String parent_authid = request.getParameter(PARAM_PARENT_AUTHORITY_ID);
	  if ( parent_authid != null && ( parent_authid.length() == 0 || parent_authid.equals("null") ) ) 
	    parent_authid = null;
	  request.setAttribute( PARAM_PARENT_AUTHORITY_ID, parent_authid );
	  String mainelement = request.getParameter(PARAM_MAIN_ELEMENT);
	  if ( mainelement != null && ( mainelement.length() == 0 || mainelement.equals("null") ) ) 
		mainelement = null;
	  request.setAttribute( PARAM_MAIN_ELEMENT, mainelement );	  
	  
    
      
      return results;
            
    } // end of ResourceActionImpl.act()


   /**
     * Generate xml string to pass to Registry 
     */
    private void catalogQuery() throws ConsistencyException {
      if( TRACE_ENABLED ) trace( "ResourceActionImpl.catalogQuery() entry" );
              
              
      String resource = "";
      String description = "";
      String publisher = "";
      String title = "";
      String constraintjoin = "";
      String wavelength[] = {""};
      String wavelengthjoin = "";
      String mission[] = {""};
      String missionjoin = "";
      String keyword[] = {""};
      String keywordjoin = "";
      String categoryjoin = "";
      String query = "";
      String sqlQuery = "";
      String andor = "";
      boolean andreqd = false; // used to add 'and' between e.g. (constraints) and (wavelength)
                    
      try { 
        // General constraints
        resource = request.getParameter( PARAM_RESOURCE_NAME ).trim();
        description = request.getParameter( PARAM_DESCRIPTION ).trim();
        publisher = request.getParameter( PARAM_PUBLISHER ).trim();
        title = request.getParameter( PARAM_TITLE ).trim();
        constraintjoin = request.getParameter( PARAM_CONSTRAINT_JOIN ).trim();         

        // Wavelength
        wavelength = request.getParameterValues( PARAM_WAVELENGTH );
        wavelengthjoin = request.getParameter( PARAM_WAVELENGTH_JOIN ).trim();

        // Mission
        mission = request.getParameterValues( PARAM_MISSION );       
        missionjoin = request.getParameter( PARAM_MISSION_JOIN ).trim();                
        
        // Keyword         
        keyword = request.getParameterValues( PARAM_KEYWORD );
        keywordjoin = request.getParameter( PARAM_KEYWORD_JOIN ).trim(); 
        
        categoryjoin = request.getParameter( PARAM_CONSTRAINT_ALL ).trim();
                 
        sqlQuery = "Select * from Regitry where ( ( vr:content/vr:type='Catalog' )";
        // Lets build up the XML for a query.

         // is this an empty search?
		 if ( resource.length() > 0 || description.length() > 0 || 
		      publisher.length() >0 || title.length() > 0 ||
		      ( wavelength != null && wavelength.length > 0 ) || 
		      ( mission != null && mission.length > 0 )  ||
		      ( keyword != null && keyword.length > 0 ) ) 
		  {
            sqlQuery += " and (";
        
            // GENERAL CONSTRAINTS
            if ( resource.length() > 0 || description.length() > 0 ||
                 publisher.length() >0 || title.length() > 0 ) {        
                
              sqlQuery += " ( "; // start of general constraint
              if ( resource.length() > 0 ) 
              {
                sqlQuery += " vr:identifier like '%" + resource + "%' ";
                andreqd = true;
              }
              if ( description.length() > 0 ) 
              {
                if (andreqd) 
                    sqlQuery += constraintjoin;

                sqlQuery += " vr:content/vr:description like '%" + description + "%' ";
                andreqd = true;
              }
              if ( publisher.length() > 0 ) 
              {
                if (andreqd)
                    sqlQuery += constraintjoin;

                sqlQuery += " vr:curration/vr:publisher like '%" + publisher + "%' ";
                andreqd = true;
              }
              if ( title.length() > 0 ) 
              {
                if (andreqd)
                    sqlQuery += constraintjoin;

                sqlQuery += " vr:title like '%" + title + "%' ";
                andreqd = true;
              }
              
              sqlQuery += " ) "; // end of general constraint
              
            } // end of GENERAL CONSTRAINTS

            // WAVELENGTH
            if ( wavelength != null && wavelength.length > 0 ) 
            {
			  if (andreqd)
                  sqlQuery += categoryjoin;
                
			  sqlQuery += " ( "; // start of wavelength
              
              for ( int i=0; i< wavelength.length; i++ ) {
                if ( i > 0 ) 
                {                    
                  sqlQuery += wavelengthjoin;
                }
                sqlQuery += " vs:coverage/vs:spectral/vs:waveband = '" + wavelength[i].trim() + "' ";
              }
			  andreqd = true;
              
              sqlQuery += " ) "; // end of wavelength
              
            } // end of WAVELENGTH

            // MISSION
            if ( mission != null && mission.length > 0 ) 
            {
			  if (andreqd)
                  sqlQuery += categoryjoin;
           
			  sqlQuery += " ( "; // start of mission
			  
              for ( int i=0; i< mission.length; i++ ) {
                if ( i > 0 ) 
                {
                  sqlQuery += missionjoin;
                } 
                
                sqlQuery += " vr:facility = '" + mission[i].trim() + "' ";
              }
			  andreqd = true;
              
			  sqlQuery += " ) "; // end of mission
			  
            } // end of MISSION

            // KEYWORD
            if ( keyword != null && keyword.length > 0 ) 
            {
			  if (andreqd)
                  sqlQuery += categoryjoin;
          
			  sqlQuery += " ( "; // start of keyword
			  
              for ( int i=0; i< keyword.length; i++ ) 
              {
                if ( i > 0 ) 
                {
                    sqlQuery += keywordjoin;
                }
                
                sqlQuery += " vr:content/vr:subject = '" + keyword[i].trim() + "' ";
              
              }
              
              sqlQuery += " ) "; // end of keyword
              
            } // end of KEYWORD
            
            sqlQuery += " ) ";
            
		  }
		 
        //  End of Query.
		 sqlQuery += " ) ";
		 
      }
      catch(Exception ex) {
		session.setAttribute( ERROR_MESSAGE_PARAMETER, ex.getMessage() ) ;      	
		debug("Error thrown whilst creating task query xml: "); 
		debug( ex.getMessage() ) ;
        query = "";
      }        

      try {      
        debug( sqlQuery ); 
        if ( sqlQuery.length() > 0 ) 
          submitQuery( sqlQuery );
      }
      catch(Exception ex) {
        ex.printStackTrace();
      }
      finally {
        if( TRACE_ENABLED ) trace( "ResourceActionImpl.catalogQuery() exit" );
      }                    
    } // end of catalogQuery()

      
    /**
      * Generate xml string to pass to Registry 
      */
    private void taskQuery() throws ConsistencyException {
      if( TRACE_ENABLED ) trace( "ResourceActionImpl.taskQuery() entry" );

      String authorityId = "";
      String taskTitle = "";
      String description = ""; 
      String taskJoin = "";          
      String query = "";
      String sqlQuery = "";
	  boolean andreqd = false; // used to add 'and' between e.g. (constraints) and (wavelength)
  
      try {        
        authorityId = request.getParameter( PARAM_AUTHORITY_FIELD ).trim();				
        taskTitle = request.getParameter( PARAM_TITLE ).trim();
        description = request.getParameter( PARAM_DESCRIPTION ).trim();
        taskJoin = request.getParameter( PARAM_TASK_JOIN ).trim();        

        // Lets build up the XML for a query.
        sqlQuery = " Select * from Registry where ( @xsi:type = 'cea:CeaApplicationType' or ";
        sqlQuery += " @xsi:type = 'cea:CeaHttpApplicationType' ";
        sqlQuery += ") and (@status = 'active'";
                
        
		// is this an empty search?
		if ( authorityId.length() > 0 || taskTitle.length() > 0 || description.length() > 0 )
		{				
        sqlQuery += " and (";       

          if ( authorityId.length() > 0 ) 
          {
            sqlQuery += " vr:identifier like '%" + authorityId + "%' ";
			andreqd = true;
          }
          if ( taskTitle.length() > 0 ) 
          {
			if (andreqd)
              sqlQuery += taskJoin;
      
			sqlQuery += " vr:title like '%" + taskTitle + "%' ";
			andreqd = true;
          }
          if ( description.length() > 0 ) 
          {
		    if (andreqd) 
               sqlQuery += taskJoin;
            
		     sqlQuery += " vr:content/vr:description like '%" + description + "%' ";
          }
		
          sqlQuery += ")";
		}		                 	
		         
		sqlQuery += ")"; // End of query
trace("xxxxx sql: " + sqlQuery);		
		
      }
      catch(Exception ex){
		session.setAttribute( ERROR_MESSAGE_PARAMETER, ex.getMessage() ) ;      	
		debug("Error thrown whilst creating task query xml: "); 
		debug( ex.getMessage() ) ;
        query = "";
      }
      
      try {      
        debug( sqlQuery ); 
        if ( sqlQuery.length() > 0 )         
          submitQuery( sqlQuery );                
      }
      catch(Exception ex) 
      {     	
		ex.printStackTrace() ;
      }
      finally {
        if( TRACE_ENABLED ) trace( "ResourceActionImpl.taskQuery() exit" );
      }                    
    } // end of taskQuery()


    /**
      * This method does nothing at present! To follow...
      */
    private void filestoreQuery() throws ConsistencyException {
      if( TRACE_ENABLED ) trace( "ResourceActionImpl.filestoreQuery() entry" );
              
      try {          
      }
      catch(Exception ex){
      }
      finally {
        if( TRACE_ENABLED ) trace( "ResourceActionImpl.filestoreQuery() exit" );
      }                    
    } // end of filestoreQuery()


    /**
     * This method submits query to Registry
     * 
     * @param String query 	xml query string.     
     * @return void
     */
    private void submitQuery( String query ) throws ConsistencyException {
      if( TRACE_ENABLED ) trace( "ResourceActionImpl.submitQuery() entry" );
              
      try {
        ArrayList resultlist = null;
        String count = "";         
          // Now lets submit the query.
        RegistryService rs = RegistryDelegateFactory.createQuery( );
        debug( "Service = " + rs );
        if (query.length() > 0 )
        // if error thrown in building query string,
        // string will be empty so do not submit.
        {
           String adqlString = Sql2Adql.translateToAdql074(query);
           debug("ADQL String in PORTAL for REGISTRY = " + adqlString);
           /*
           Document doc = rs.submitQuery( query );
           */
           Document doc = rs.search(adqlString);

          //create the results and put it in the request.
           resultlist = createList( doc );
           count = "" + resultlist.size();
           
		   debug( "Number of Result = " + count );
		   session.setAttribute( PARAM_RESULT_COUNT, count );           

          // Not sure if this is req'd, but do we really want to retrieve everything from registry?
          if (resultlist.size() <= 100 ) {
            session.setAttribute("resultDoc", doc);			
          }
          else 
          {
			session.setAttribute( ERROR_INFO_PARAMETER, "Your search will return more than 100 entries, please narrow it" ) ;
          }
        }                                                
      }

      catch( Exception ex ) 
      {   
          ex.printStackTrace();
		session.setAttribute( ERROR_MESSAGE_PARAMETER, ex.getMessage() ) ;      	
        debug("Error thrown whilst submitting query: "); 
        debug( ex.getMessage() ) ;        
      }
        
      finally {
        if( TRACE_ENABLED ) trace( "ResourceActionImpl.submitQuery() exit" );
      }                    
    } // end of submitQuery()

    /**
     * This method gets a list of key elements from the result document.
     * @param doc Query results in a DOM tree format.
     * @return ArrayList of relevant the String XML results.
     */
    private ArrayList createList( Document doc ) {
       //NodeList nodes = doc.getDocumentElement().getChildNodes();
       NodeList nodes = doc.getDocumentElement().getElementsByTagNameNS("*","Resource");
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
      }
      return al;
    }    
    
  } // end of inner class ResourceActionImpl


  private class ConsistencyException extends Exception {
  }

    private void trace( String traceString ) {
      // logger.debug( traceString );
      System.out.println( traceString );
  }
    
  private void debug( String logString ){
    // logger.debug( logString );
    System.out.println( logString );
  }     

} // end of class ResourceAction
