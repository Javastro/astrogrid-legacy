/*
 * @(#)Query.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.portal.workflow.design;

import org.apache.log4j.Logger ;
import org.w3c.dom.* ;
import org.apache.axis.utils.XMLUtils ;
import java.util.Iterator ;
import org.xml.sax.* ;
import java.io.StringReader ;
import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate;
import org.astrogrid.portal.workflow.*;
import org.astrogrid.community.common.util.CommunityMessage ;

import org.astrogrid.portal.workflow.design.unittest.* ;

/**
 * The <code>Query</code> class represents... 
 * <p>
 *
 * <p>
 * The class... 
 * 
 *
 * @author  Jeff Lusted
 * @version 1.0 25-Aug-2003
 * @see     
 * @see     
 * @since   AstroGrid 1.3
 */
public final class Query implements Tool {
    
    /** Compile-time switch used to turn tracing on/off. 
      * Set this to false to eliminate all trace statements within the byte code.*/         
    private static final boolean 
        TRACE_ENABLED = true ;
        
    private static Logger 
        logger = Logger.getLogger( Query.class ) ; 
    
    private String
        name = null ,
        description = null ;
        
    private Element
        queryElement = null ;
        
        
    public static Query readQuery( String userid
                                 , String community
                                 , String communitySnippet
                                 , String name ) {
        if( TRACE_ENABLED ) trace( "Query.readQuery() entry") ; 
        
        Query
           query = null;
        StringBuffer
           pathBuffer = new StringBuffer( 64 ) ;
        String
           xmlString = null ;
           String
               mySpaceLocation = null ;
         
        try {
            
            mySpaceLocation =  WKF.getProperty( WKF.MYSPACE_URL, WKF.MYSPACE_CATEGORY ) ;
           
           debug( "mySpaceLocation: " + mySpaceLocation ) ;
           
            // This is here purely for test situations...
            if( mySpaceLocation == null || mySpaceLocation.trim().equals("") ) {
                query = WorkflowHelper.readQuery( userid, community, communitySnippet, name ) ;
               return query ;
            }
            
            
               
            MySpaceManagerDelegate
                mySpace = new MySpaceManagerDelegate( WKF.getProperty( WKF.MYSPACE_URL, WKF.MYSPACE_CATEGORY ) ) ;
                
            pathBuffer
                .append( "/")
                .append( userid )
                .append( "@")
                .append( community )
                .append( "/")
                .append( "serv1")
                .append( "/")
                .append( "query")
                .append( "/")
                .append( name ) ;
            
            xmlString = mySpace.getDataHolding( userid
                                              , community
                                              , CommunityMessage.getGroup( communitySnippet )
                                              , pathBuffer.toString() ) ;                      

            InputSource
               source = new InputSource( new StringReader( xmlString ) );
                         
            query = new Query( XMLUtils.newDocument(source) ) ;
            query.setName( name ) ;

         }
         catch ( Exception ex ) {
             ex.printStackTrace() ;
         }
         finally {
             if( TRACE_ENABLED ) trace( "Query.readQuery() exit") ; 
         }
       
         return query ;
        
    } // end of readQuery()
    
    
    public static Iterator readQueryList( String userid
                                        , String community
                                        , String communitySnippet
                                        , String filter ) {
        if( TRACE_ENABLED ) trace( "Query.readQuery() entry") ; 
        
        // JBL: For the moment we are ignoring filter.
        
       Iterator
          iterator = null ;
       java.util.Vector
          vector = null ;
       StringBuffer
          argumentBuffer = new StringBuffer( 64 ) ;
       String
          mySpaceLocation ;
        
       try {
           
           mySpaceLocation =  WKF.getProperty( WKF.MYSPACE_URL, WKF.MYSPACE_CATEGORY ) ;
           
            // This is here purely for test situations...
            if( mySpaceLocation == null || mySpaceLocation.trim().equals("") ) {
                vector = WorkflowHelper.readQueryList( userid, community, communitySnippet, filter) ;
                return vector.iterator() ;
            } 
                
          MySpaceManagerDelegate
             mySpace = new MySpaceManagerDelegate( WKF.getProperty( WKF.MYSPACE_URL, WKF.MYSPACE_CATEGORY ) ) ;
                
          argumentBuffer
             .append( "/")
             .append( userid )
             .append( "@")
             .append( community )
             .append( "/")
             .append( "serv1")
             .append( "/" )
             .append( "query")
             .append( "/")
             .append( "*" ) ;
            
           vector = mySpace.listDataHoldings( userid
                                            , community
                                            , CommunityMessage.getGroup( communitySnippet )
                                            , argumentBuffer.toString() ) ;
                                              
           iterator = vector.iterator() ;  
                          
       }
       catch ( Exception ex ) {
           ex.printStackTrace() ;
       }
       finally {
           if( TRACE_ENABLED ) trace( "Workflow.readWorkflowList() exit") ; 
       }
       
       return iterator ;

    } // end of readQueryList()
    
        
    public Query() {
        if( TRACE_ENABLED ) trace( "Query() entry/exit") ;  
    }
    
    
    public Query( Document document ) {
        //JBL this probably requires some further filtering...
        this( document.getDocumentElement() ) ;
    }
    
    
    public Query( Element element ) {
        if( TRACE_ENABLED ) trace( "Query(Element) entry") ; 
               
        try {
            
            this.queryElement = element ;
                     
            NodeList
               nodeList = element.getChildNodes() ; 
                           
            for( int i=0 ; i < nodeList.getLength() ; i++ ) {           
                if( nodeList.item(i).getNodeType() == Node.ELEMENT_NODE ) {
                    
                    element = (Element) nodeList.item(i) ;
 
 // JL Note: These will require change according to how name and description tags
 // finally end up in iteration 3...  
 
                    name = "name";
                    description = "description";             
                    if ( element.getTagName().equals( WorkflowDD.SELECT_NAME_ELEMENT ) ) {
                        name = element.getFirstChild().getNodeValue().trim() ;  
                    }  
                    else if( element.getTagName().equals( WorkflowDD.SELECT_DESCRIPTION_ELEMENT ) ) {
                        description = element.getFirstChild().getNodeValue().trim() ;
                    }  
                    
                } // end if
                                
            } // end for        

            
        }
        finally {
            if( TRACE_ENABLED ) trace( "Query(Element) exit") ;
        }  
    }
        
        
    public String getToolType() { return "Query" ; }
        
	public String toXMLString() { 
        if( TRACE_ENABLED ) trace( "Query.toXMLString() entry") ;  
          
        String 
           response = null ;
                                     
        try {
/*            
            Object []
               inserts = new Object[1] ;
            inserts[0] = XMLUtils.ElementToString( queryElement ) ;

            response = MessageFormat.format( WorkflowDD.QUERY_TEMPLATE, inserts ) ;
*/
            response = XMLUtils.ElementToString( queryElement ) ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "Query.toXMLString() exit") ;    
        }       
        
        return response ;        
         
    } // end of toXMLString()


    public String toJESXMLString() { 
        if( TRACE_ENABLED ) trace( "Query.toJESXMLString() entry") ;  
          
        String 
           response = null ;
                                     
        try {
/*            
            Object []
               inserts = new Object[1] ;
            inserts[0] = XMLUtils.ElementToString( queryElement ) ;

            response = MessageFormat.format( WorkflowDD.JOBQUERY_TEMPLATE, inserts ) ;
*/

            response = XMLUtils.ElementToString( queryElement ) ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "Query.toJESXMLString() exit") ;    
        }       
        
        return response ;        
         
    } // end of toJESXMLString()
        
    
    public String getName() { return name ; }
    public void setName( String name ) { this.name = name ; }
    public void setDescription( String description ) { this.description = description ; }
    public String getDescription() { return description ; }
    
    private static void trace( String traceString ) {
        System.out.println( traceString ) ;
        // logger.debug( traceString ) ;
    }
    
    private static void debug( String logString ){
        System.out.println( logString ) ;
        // logger.debug( logString ) ;
    }  

} // end of class Query
