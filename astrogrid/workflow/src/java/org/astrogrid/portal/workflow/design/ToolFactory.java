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

import java.util.ArrayList ;
import java.util.Collections ;
import java.util.List ;
import java.util.ListIterator ;
import java.util.Iterator ;
import org.apache.log4j.Logger ;

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
        return null ;
    }
    
    protected static Tool readTool( String communitySnippet
                                  , String name ) {
        return null ;
    }
       
    private List
        inputDescriptors ,
        outputDescriptors ; 
      
    private String
        name,
        documentation ;
        
    private ToolFactory() {  
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
    
    
    public Tool createTool() {
        return new Tool( this ) ;
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

} // end of class ToolDescriptor
