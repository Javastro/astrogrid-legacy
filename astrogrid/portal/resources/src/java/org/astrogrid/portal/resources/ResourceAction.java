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

import org.astrogrid.registry.client.admin.RegistryAdminDocumentHelper;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.registry.RegistryException;

import org.astrogrid.config.Config;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;

import org.astrogrid.util.DomHelper;
import org.astrogrid.registry.common.WSDLBasicInformation;
import org.astrogrid.store.Ivorn;


/**
 * @author Phil Nicolson (pjn3@star.le.ac.uk) Jan 05
 * @version $Name:  $Revision: 1.2 $Date:
 */
public class ResourceAction extends AbstractAction {

  /** Compile-time switch used to turn tracing on/off. 
   * Set this to false to eliminate all trace statements within the byte code.*/
  private static final boolean TRACE_ENABLED = true;

  /** Compile-time switch used to turn certain debugging statements on/off. 
   * Set this to false to eliminate these debugging statements within the byte code.*/
  private static final boolean DEBUG_ENABLED = true;

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
      PARAM_TASK_JOIN = "TaskAndOr";  

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
                             
        // Lets build up the XML for a query.
        query = "<query>\n"
         + "<selectionSequence>\n" // start of main sequence
         + "<selection item='searchElements' itemOp='EQ' value='Resource'/>\n"
         + "<selectionOp op='$and$'/>\n"
         + "<selection item='vr:Type' itemOp='EQ' value='Catalog'/>\n" ;
		 

         // is this an empty search?
		 if ( resource.length() > 0 || description.length() > 0 || 
		      publisher.length() >0 || title.length() > 0 ||
		      ( wavelength != null && wavelength.length > 0 ) || 
		      ( mission != null && mission.length > 0 )  ||
		      ( keyword != null && keyword.length > 0 ) ) 
		  {
            query += "\n<selectionOp op='AND'/>";
			query +=  "\n<selectionSequence>\n"; // start of inner sequence
        
            // GENERAL CONSTRAINTS
            if ( resource.length() > 0 || description.length() > 0 ||
                 publisher.length() >0 || title.length() > 0 ) {        
                 
			  query +=  "\n<selectionSequence>\n"; // start of constraints sequence
			         
              if ( resource.length() > 0 ) 
              {
                query +=  "\n<selectionSequence>\n";          	
                query += "<selection item='vr:Identifier/vr:AuthorityID' itemOp='CONTAINS' value='" + resource + "'/>";
  			    query += "<selectionOp op='OR'/>";
			    query += "<selection item='vr:Identifier/vr:ResourceKey' itemOp='CONTAINS' value='" + resource + "'/>";
                query +=  "\n</selectionSequence>\n";
                andreqd = true;
              }
              if ( description.length() > 0 ) 
              {
                if (andreqd) 
                  query += "\n<selectionOp op='"+constraintjoin+"'/>";
                query += "<selection item='vr:Summary/vr:Description' itemOp='CONTAINS' value='" + description + "'/>";
                andreqd = true;
              }
              if ( publisher.length() > 0 ) 
              {
                if (andreqd)
                  query += "\n<selectionOp op='"+constraintjoin+"'/>";
                query += "<selection item='vr:Curation/vr:Publisher/vr:Title' itemOp='CONTAINS' value='" + publisher + "'/>";
                andreqd = true;
              }
              if ( title.length() > 0 ) 
              {
                if (andreqd)
                  query += "\n<selectionOp op='"+constraintjoin+"'/>";
                query += "<selection item='vr:Title' itemOp='CONTAINS' value='" + title + "'/>";
                andreqd = true;
              }
              query +=  "\n</selectionSequence>\n"; // end of constraints sequence
            } // end of GENERAL CONSTRAINTS

            // WAVELENGTH
            if ( wavelength != null && wavelength.length > 0 ) 
            {
			  if (andreqd)
                query +=  "\n<selectionOp op='" + categoryjoin + "'/>" ;
			  query += "<selectionSequence>"; // start of wavelength sequence
              for ( int i=0; i< wavelength.length; i++ ) {
                if ( i > 0 ) 
                {
                  query += "\n<selectionOp op='" + wavelengthjoin + "'/>";
                }
                query += "<selection item='vs:Coverage/vs:Spectral/vs:Waveband'"
                      + " itemOp='EQ' value='" + wavelength[i].trim() + "'/>";          
              }
			  andreqd = true;
              query +=  "\n</selectionSequence>"; // end of wavelength sequence
            } // end of WAVELENGTH

            // MISSION
            if ( mission != null && mission.length > 0 ) 
            {
			  if (andreqd)
                query +=  "\n<selectionOp op='" + categoryjoin + "'/>";
              query += "\n<selectionSequence>"; // start of mission sequence
              for ( int i=0; i< mission.length; i++ ) {
                if ( i > 0 ) 
                {
                  query += "\n<selectionOp op='" + missionjoin + "'/>";
                }
                query += "<selection item='vr:Facility'"
                      + " itemOp='EQ' value='" + mission[i].trim() + "'/>";          
              }
			  andreqd = true;
              query +=  "\n</selectionSequence>"; // end of main mission
            } // end of MISSION

            // KEYWORD
            if ( keyword != null && keyword.length > 0 ) 
            {
			  if (andreqd)
                query +=  "\n<selectionOp op='" + categoryjoin + "'/>" ;
              query += "\n<selectionSequence>"; // start of keyword sequence
              for ( int i=0; i< keyword.length; i++ ) 
              {
                if ( i > 0 ) 
                {
                  query += "\n<selectionOp op='" + keywordjoin + "'/>";
                }
              query += "<selection item='vr:Subject'"
                    + " itemOp='EQ' value='" + keyword[i].trim() + "'/>";          
              }
              query +=  "\n</selectionSequence>";  // end of keyword sequence
            } // end of KEYWORD
            
		  query +=  "\n</selectionSequence>"; // end of inner sequence
            
		  }
		  
	    query += "\n</selectionSequence>\n";  // end of main sequence  
        //  End of Query.
        query += "\n</query>";  
      }
      catch(Exception ex) {
		session.setAttribute( ERROR_MESSAGE_PARAMETER, ex.getMessage() ) ;      	
		debug("Error thrown whilst creating task query xml: "); 
		debug( ex.getMessage() ) ;
        query = "";
      }        

      try {      
        debug( query ); 
        if ( query.length() > 0 ) 
          submitQuery( query );
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
	  boolean andreqd = false; // used to add 'and' between e.g. (constraints) and (wavelength)
  
      try {        
        authorityId = request.getParameter( PARAM_AUTHORITY_FIELD ).trim();				
        taskTitle = request.getParameter( PARAM_TITLE ).trim();
        description = request.getParameter( PARAM_DESCRIPTION ).trim();
		taskJoin = request.getParameter( PARAM_TASK_JOIN ).trim();        

        // Lets build up the XML for a query.
        query = "<query>\n"
                + "<selectionSequence>\n"
				+ "<selectionSequence>\n"
                + "<selection item='searchElements' itemOp='EQ'"
                + " value='Resource'/>\n"
                + "<selectionOp op='$and$'/>"
                + "<selection item='@xsi:Type' itemOp='EQ'"
                + " value='CeaHttpApplicationType'/>"
                + "<selectionOp op='OR'/>"                
                + "\n<selection item='@xsi:type' itemOp='EQ'"
                + " value='CeaApplicationType'/>"
                + "<selectionOp op='OR'/>"                
                + "<selection item='@xsi:Type' itemOp='EQ'"
                + " value='cea:CeaHttpApplicationType'/>"
                + "<selectionOp op='OR'/>"                
                + "\n<selection item='@xsi:type' itemOp='EQ'"
                + " value='cea:CeaApplicationType'/>"
				+  "\n</selectionSequence>\n";

		// is this an empty search?
		if ( authorityId.length() > 0 || taskTitle.length() > 0 || description.length() > 0 )
		{					 
		  query += "\n<selectionOp op='AND'/>"
		        +  "\n<selectionSequence>\n"; // start of inner sequence

          if ( authorityId.length() > 0 ) 
          {
			query +=  "\n<selectionSequence>\n";          	
			query += "<selection item='vr:Identifier/vr:AuthorityID' itemOp='CONTAINS' value='" + authorityId + "'/>";
		    query += "<selectionOp op='OR'/>";
			query += "<selection item='vr:Identifier/vr:ResourceKey' itemOp='CONTAINS' value='" + authorityId + "'/>";
			query +=  "\n</selectionSequence>\n";
			andreqd = true;
          }
          if ( taskTitle.length() > 0 ) 
          {
			if (andreqd) 
			  query += "\n<selectionOp op='"+taskJoin+"'/>";          	
			query += "<selection item='vr:Title' itemOp='CONTAINS' value='" + taskTitle + "'/>";
			andreqd = true;
          }
          if ( description.length() > 0 ) 
          {
		    if (andreqd) 
			  query += "\n<selectionOp op='"+taskJoin+"'/>";          	          	
			  query += "<selection item='vr:Summary/vr:Description' itemOp='CONTAINS' value='" + description + "'/>";
          }
		
		  query +=  "\n</selectionSequence>"; // end of inner sequence
		}
		  
		query += "\n</selectionSequence>\n";  // end of main sequence                  
        //  End of Query.
        query += "\n</query>";               
      }
      catch(Exception ex){
		session.setAttribute( ERROR_MESSAGE_PARAMETER, ex.getMessage() ) ;      	
		debug("Error thrown whilst creating task query xml: "); 
		debug( ex.getMessage() ) ;
        query = "";
      }
      
      try {      
        debug( query ); 
        if ( query.length() > 0 )         
          submitQuery( query );                
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
           Document doc = rs.submitQuery( query );        

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
       NodeList nodes = doc.getDocumentElement().getChildNodes();
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
