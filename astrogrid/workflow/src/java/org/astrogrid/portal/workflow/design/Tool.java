/*
 * @(#)Tool.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.portal.workflow.design;

import org.astrogrid.portal.workflow.intf.*;

import org.apache.axis.utils.XMLUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * The <code>Tool</code> class represents... 
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
public class Tool implements ITool {
  
    /** Compile-time switch used to turn tracing on/off. 
      * Set this to false to eliminate all trace statements within the byte code.*/         
    private static final boolean 
        TRACE_ENABLED = true ;
        
    private static Logger 
        logger = Logger.getLogger( Tool.class ) ; 
             
    private static List createParameters( Element element ){
        if( TRACE_ENABLED ) trace( "Tool.createParameters() entry") ;
        
        List
            list = new ArrayList() ;
        
        try {
            
            NodeList
               nodeList = element.getChildNodes() ; 
                           
            for( int i=0 ; i < nodeList.getLength() ; i++ ) {           
                
                if( nodeList.item(i).getNodeType() == Node.ELEMENT_NODE ) {
                    
                    element = (Element) nodeList.item(i) ;
                
                    if ( element.getTagName().equals( WorkflowDD.PAREMETER_ELEMENT ) ) {
                           list.add( new Parameter( element ) )  ;
                    }
                    
                } // end if
                                
            } // end for       
             
        }
        finally {
            if( TRACE_ENABLED ) trace( "Tool.createParameters() exit") ;
        }

        return list ;
        
    } // end of Tool.createParameters()  
    
    
    private String
        name,
        documentation ;
        
    private List
        inputParameters = new ArrayList(),
        outputParameters = new ArrayList() ;    
        
        
    private Tool() {
    }
    // pah - relaxed the accessibility to public for testing purposes...
    protected Tool( String name ) {
        this.name = name ;
    }
        
         
    protected Tool( Element element ) {
        if( TRACE_ENABLED ) trace( "Tool( Element ) entry") ;  
        
        try {
            
            name = element.getAttribute( WorkflowDD.TOOL_NAME_ATTR ) ;
            
            NodeList
               nodeList = element.getChildNodes() ; 
                           
            for( int i=0 ; i < nodeList.getLength() ; i++ ) {           
                
                if( nodeList.item(i).getNodeType() == Node.ELEMENT_NODE ) {
                    
                    element = (Element) nodeList.item(i) ;
                
                    if ( element.getTagName().equals( WorkflowDD.INPUT_ELEMENT ) ) {
                        this.inputParameters = Tool.createParameters( element ) ;   
                    }
                    else if ( element.getTagName().equals( WorkflowDD.OUTPUT_ELEMENT ) ) {
                        this.outputParameters = Tool.createParameters( element ) ;   
                    } 
                    else if ( element.getTagName().equals( WorkflowDD.DOCUMENTATION_ELEMENT ) ){
                        //bug#106
                        this.documentation = XMLUtils.getChildCharacterData( element ) ;   
                    }   
                    
                } // end if
                                
            } // end for        
           
        }
        finally {
            if( TRACE_ENABLED ) trace( "Tool(Element) exit") ;
        }
                   
    }
    
    
    public String getName() {
        return this.name;
    }
    
    
    public String getDocumentation() {
        return this.documentation ;
    }
    
    
    public Iterator getInputParameters() {; 
        return this.inputParameters.listIterator() ;
    }
    
    
    public Iterator getOutputParameters() { 
        return this.outputParameters.listIterator() ;
    }
    
    
    protected IParameter newInputParameter( String name ) {
        return Tool.newParameter( name, this.inputParameters ) ;  
    } 
 
 
    protected IParameter newInputParameter( IParameter param ) {
        return Tool.newParameter( param.getName(), this.inputParameters ) ; 
    }
 
    
    protected IParameter newOutputParameter( String name ) {
        return Tool.newParameter( name, this.outputParameters ) ;
    } 
    
    
    protected IParameter newOutputParameter( IParameter param ) {
        return Tool.newParameter( param.getName(), this.outputParameters ) ;
     }
    
       
    public String toXMLString() {
        if( TRACE_ENABLED ) trace( "Tool.toXMLString() entry") ;
          
        String 
           response = null,
           inputParams,
           outputParams ;
        StringBuffer
           buffer = null ;
                                     
        try {
            
            buffer = new StringBuffer(128) ;
            ListIterator
                it = inputParameters.listIterator();
            while( it.hasNext() ) {
                buffer.append( ((Parameter)it.next()).toXMLString()) ;
            }
            inputParams = buffer.toString() ;
            buffer = new StringBuffer(128) ;
            it = outputParameters.listIterator();
            while( it.hasNext() ) {
                buffer.append( ((Parameter)it.next()).toXMLString()) ;
            }
            outputParams = buffer.toString() ;
            
            Object []
               inserts = new Object[4] ;
            inserts[0] = this.getName() ;
            inserts[1] = this.getDocumentation() ;
            inserts[2] = inputParams ;
            inserts[3] = outputParams ;

            response = MessageFormat.format( WorkflowDD.TOOL_TEMPLATE, inserts ) ;

        }
        finally {
            if( TRACE_ENABLED ) trace( "Tool.toXMLString() exit") ;    
        }       
        
        return response ;        
        
    }
    
    
    public String toJESXMLString() {
        if( TRACE_ENABLED ) trace( "Tool.toJESXMLString() entry") ;
        
        String 
            response = null,
            inputParams,
            outputParams ;
        StringBuffer
            buffer = null ;
                                     
        try {
            
            buffer = new StringBuffer(128) ;
            ListIterator
                it = inputParameters.listIterator();
            while( it.hasNext() ) {
                buffer.append( ((Parameter)it.next()).toJESXMLString()) ;
            }
            inputParams = buffer.toString() ;
            buffer = new StringBuffer(128) ;
            it = outputParameters.listIterator();
            while( it.hasNext() ) {
                buffer.append( ((Parameter)it.next()).toJESXMLString()) ;
            }
            outputParams = buffer.toString() ;
            
            Object []
                inserts = new Object[3] ;
            inserts[0] = this.getName() ;
            inserts[1] = inputParams ;
            inserts[2] = outputParams ;

            response = MessageFormat.format( WorkflowDD.JOBTOOL_TEMPLATE, inserts ) ;

        }
        finally {
            if( TRACE_ENABLED ) trace( "Tool.toJESXMLString() exit") ;    
        }       
        
        return response ;      

    }
    
     
    private static void trace( String traceString ) {
        System.out.println( traceString ) ;
        // logger.debug( traceString ) ;
    }
    
    
    private static void debug( String logString ){
        System.out.println( logString ) ;
        // logger.debug( logString ) ;
    }


	/**
	   */
	public void setDocumentation(String string) {
		documentation = string;
	}

	/**
	   */
	public void setInputParameters(List list) {
		inputParameters = list;
	}

	/**
	   */
	public void setName(String string) {
		name = string;
	}

	/**
	   */
	public void setOutputParameters(List list) {
		outputParameters = list;
	}
    
    
    private static IParameter newParameter( String name
                                         , List parameterList ) {
        if( TRACE_ENABLED ) trace( "Tool.newParameter(name,parameterList) entry") ;
        
        Parameter 
            p = null ,
            sourceParam = null ;        
        ListIterator iterator = null ;            
        int index = 0 ;
        
        try {

            iterator = parameterList.listIterator() ;
            
            while( iterator.hasNext() ) {
                p = (Parameter)iterator.next() ;
                if( p.getName().equals( name ) ) {
                    sourceParam = p ;
                    index++ ;      
                }
            } // end while
            
            p = new Parameter( name ) ;
            Tool.setBaseValues( p, sourceParam ) ;
            
            if( index == 0 ) {
                parameterList.add( p ) ;
            }
            else {
                parameterList.add( index, p ) ;
            }
                        
        }
        finally {
            if( TRACE_ENABLED ) trace( "Tool.newParameter(name,parameterList) exit") ;
        }
              
        return p ;
        
    } // end of Tool.newParameter(name,parameterList)

    
    private static IParameter setBaseValues( IParameter target
                                          , IParameter source ) {
        if( source != null ) {
//          target.setContents( source.getContents() ) ; 
          target.setDocumentation( source.getDocumentation() ) ;
//          target.setLocation( source.getLocation()) ;
          target.setType( source.getType() ) ;
          target.setCardinality( source.getCardinality() ) ;          
        }
        
        return target ; 

     } // end of setBaseValues()                  

} // end of class Tool
