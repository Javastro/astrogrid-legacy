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

import java.util.List ;
import java.util.ListIterator ;
import org.apache.log4j.Logger ;
import org.w3c.dom.* ;

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
public class Tool {
    
    private ToolFactory
        factory ;
        
    private List
        inputParameters,
        outputParameters ;    
        
    protected Tool( ToolFactory factory ) {
        this.factory = factory ;
        // create standard set of input and output parameters here
        
    }
    
    public Tool( Element element ) {
    }
    
    public ToolFactory getFactory() {
        return factory ;
    }
    
    public ListIterator getInputParameters() {
        return null ;
    }
    
    public ListIterator getOutputParameters() {
        return null ;
    }
    
    public Parameter createInputParameter( String name ) {
        return null ;
    }
    
    public Parameter createOutputParameter( String name ) {
        return null ;
    }
    
    protected String toXMLString() {
        return null ;
    }
    
    protected String toJESXMLString() {
        return null ;
    }

}
