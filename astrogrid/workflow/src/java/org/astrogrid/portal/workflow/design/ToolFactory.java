/*
 * @(#)ToolFactory.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.portal.workflow.design;

import org.astrogrid.portal.workflow.*;
import java.util.ArrayList ;
import java.util.Collections ;
import java.util.List ;
import java.util.ListIterator ;
import java.util.Iterator ;
import org.apache.log4j.Logger ;
import org.w3c.dom.* ;

/**
 * The <code>ToolFactory</code> class represents... 
 * <p>
 *
 * <p>
 * The class... 
 * 
 *
 * @author  Jeff Lusted
 * @version 1.0 20-Nov-2003
 * @see     
 * @see     
 * @since   AstroGrid 1.4
 */
public class ToolFactory {
 
    /** Compile-time switch used to turn tracing on/off. 
      * Set this to false to eliminate all trace statements within the byte code.*/         
    private static final boolean 
        TRACE_ENABLED = true ;
        
    private static Logger 
        logger = Logger.getLogger( ToolFactory.class ) ; 
  
        
    protected static Iterator readToolList( String communitySnippet ) {
        if( TRACE_ENABLED ) trace( "ToolFactory.readToolList(String) entry") ;
        
        Iterator
            iterator = null ;
        int
            numberTools ;
        
        try {
            
            numberTools = new Integer( WKF.getProperty( WKF.TOOLS_LIST_CATEGORY, WKF.TOOLS_LIST_TOTAL ) ).intValue();
                                                                      
        }
        finally {
            if( TRACE_ENABLED ) trace( "ToolFactory.readToolList(String) exit") ;
        }

        return null ;
        
    }
    
    
    protected static Tool createTool( String communitySnippet
                                    , String name ) {
        if( TRACE_ENABLED ) trace( "ToolFactory.createTool(String,String) entry") ;
        
        Tool
            tool = null ;
        ToolFactory
            factory = null ;
            
        try {
            factory = new ToolFactory( name ) ;
            tool = new Tool( factory ) ;                                                                          
        }
        finally {
            if( TRACE_ENABLED ) trace( "ToolFactory.createTool(String,String) exit") ;
        }
        return tool ;
        
    } // end of ToolFactory.createTool(String,String)
    
    
    protected static Tool createTool( String communitySnippet
                                    , Element element ) {
        if( TRACE_ENABLED ) trace( "ToolFactory.createTool() entry") ; 
        
        String
            name ;
        Tool
            tool ;
        ToolFactory
            factory ;
        
        try {
            
            name = element.getAttribute( WorkflowDD.TOOL_NAME_ATTR ) ;
            tool = ToolFactory.createTool( communitySnippet, name ) ;
            factory = tool.getFactory() ;
            
            NodeList
               nodeList = element.getChildNodes() ; 
                           
            for( int i=0 ; i < nodeList.getLength() ; i++ ) {           
                
                if( nodeList.item(i).getNodeType() == Node.ELEMENT_NODE ) {
                    
                    element = (Element) nodeList.item(i) ;
                
                    if ( element.getTagName().equals( WorkflowDD.INPUT_ELEMENT ) ) {
                        tool.setInputParameters( factory.createParameters( element ) ) ;   
                    }
                    else if ( element.getTagName().equals( WorkflowDD.OUTPUT_ELEMENT ) ) {
                        tool.setOutputParameters( factory.createParameters( element ) ) ;   
                    }  
                    
                } // end if
                                
            } // end for        
           
        }
        finally {
            if( TRACE_ENABLED ) trace( "Tool(Element) exit") ;
        }
                                                                           
        return tool ;
        
    }
    
       
    private List
        inputDescriptors ,
        outputDescriptors ; 
      
    private String
        name,
        documentation ;
        
    private ToolFactory( String name ) {  
        this.name = name;
    }
    
    private ToolFactory( String name
                       , String documentation
                       , List inputDescriptors
                       , List outputDescriptors ) { 
        this.name = name;
        this.documentation = documentation ;
        this.inputDescriptors = inputDescriptors ;
        this.outputDescriptors = outputDescriptors ; 
    }
    
    
    /**
      */
    public String getName() {
        return name;
    }
    

	/**
     * 
	 */
	public String getDocumentation() {
		return documentation;
	}


	/**
	  */
	public ListIterator getInputDescriptors() {
		return inputDescriptors.listIterator() ;
	}


	/**
	  */
	public ListIterator getOutputDescriptors() {
		return outputDescriptors.listIterator() ;
	}
    
    
    protected List createParameters( Element element ){
        if( TRACE_ENABLED ) trace( "ToolFactory.createParameters() entry") ;
        
        List
            list = new ArrayList() ;
        
        try {
            
            NodeList
               nodeList = element.getChildNodes() ; 
                           
            for( int i=0 ; i < nodeList.getLength() ; i++ ) {           
                
                if( nodeList.item(i).getNodeType() == Node.ELEMENT_NODE ) {
                    
                    element = (Element) nodeList.item(i) ;
                
                    if ( element.getTagName().equals( WorkflowDD.PAREMETER_ELEMENT ) ) {
                           
                    }
                    
                } // end if
                                
            } // end for       
             
        }
        finally {
            if( TRACE_ENABLED ) trace( "ToolFactory.createParameters() exit") ;
        }

        return list ;
        
    } // end of 
    
    
    private static void trace( String traceString ) {
        System.out.println( traceString ) ;
        // logger.debug( traceString ) ;
    }
    
    
    private static void debug( String logString ){
        System.out.println( logString ) ;
        // logger.debug( logString ) ;
    }

} // end of class ToolDescriptor
