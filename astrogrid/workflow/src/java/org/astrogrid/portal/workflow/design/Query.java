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
import org.astrogrid.portal.workflow.design.myspace.*;
import java.util.ListIterator ;

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
        
        
    public static Query readQuery( String userid, String community, String name ) {
        if( TRACE_ENABLED ) trace( "Query.readQuery() entry") ; 
        
        Query
            query = null;
         
        try {
                      
            query = MySpaceHelper.readQuery( userid, community, name ) ;
            
        }
        catch ( Exception ex ) {
        }
        finally {
            if( TRACE_ENABLED ) trace( "Query.readQuery() exit") ; 
        }
       
        return query ;
        
    } // end of readQuery()
    
    
    public static ListIterator readQueryList( String userid, String community, String name ) {
        if( TRACE_ENABLED ) trace( "Query.readQuery() entry") ; 
        
            ListIterator
                iterator = null;
         
        try {
            
            iterator = MySpaceHelper.readQueryList( userid, community ) ;
            
        }
        catch ( Exception ex ) {
        }
        finally {
            if( TRACE_ENABLED ) trace( "Query.readQuery() exit") ; 
        }
       
        return iterator ;

    } // end of readQueryList()
    
        
    public Query() {
        if( TRACE_ENABLED ) trace( "Query() entry/exit") ;  
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
                
                    if ( element.getTagName().equals( WorkflowDD.QUERY_NAME_ELEMENT ) ) {
                        name = element.getFirstChild().getNodeValue().trim() ;  
                    }  
                    else if( element.getTagName().equals( WorkflowDD.QUERY_DESCRIPTION_ELEMENT ) ) {
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
        
	public String toXMLString() { return XMLUtils.ElementToString( queryElement ) ; }   
    public String toJESXMLString() { return XMLUtils.ElementToString( queryElement ) ; }
    
    public String getName() { return name ; }
    public String getDescription() { return description ; }
    
    private static void trace( String traceString ) {
        System.out.println( traceString ) ;
        // logger.debug( traceString ) ;
    }
    
    private static void debug( String logString ){
        System.out.println( logString ) ;
        // logger.debug( logString ) ;
    }  

} // end of class QueryTool
