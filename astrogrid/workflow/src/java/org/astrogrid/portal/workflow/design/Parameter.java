/*
 * @(#)Parameter.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.portal.workflow.design;

import java.net.*; 
import org.w3c.dom.* ;

/**
 * The <code>Parameter</code> class represents... 
 * <p>
 *
 * <p>
 * The class... 
 * 
 *
 * @author  Jeff Lusted
 * @version 1.0 18-Nov-2003
 * @see     
 * @see     
 * @since   AstroGrid 1.4
 */
public class Parameter {
    
    private ParameterDescriptor
        descriptor ;
        
    private URL
        location = null ;
        
    private String
        contents = null ;
        
    private Parameter() {
    }
        
    protected Parameter( ParameterDescriptor descriptor ) {
        this.descriptor = descriptor ;              
    }
    
    public Parameter( Element element ) {
    }
        
  
    public String getName() {
        return this.descriptor.getName() ;  
    }
    
    public String getDocumentation() {
        return this.descriptor.getDocumentation() ;
    }
    
    public String getType() {
        return this.descriptor.getType() ;
    }
   
    public Cardinality getCardinality() {
        return this.descriptor.getCardinality() ;
    }


	public String getContents() {
		return contents;
	}


	public URL getLocation() {
		return location;
	}


	public void setContents(String string) {
		contents = string;
	}


	public void setLocation(URL url) {
		location = url;
	}

} // end of class Parameter
