/*
 * @(#)ParameterDescriptor.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.portal.workflow.design;

import java.net.URL; 

/**
 * The <code>ParameterDescriptor</code> class represents... 
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
public class ParameterDescriptor {

    private String 
        name,
        documentation,
        type ;
        
    private Cardinality
        cardinality ;
        
    private ParameterDescriptor() {
    }
        
    protected ParameterDescriptor( String name
                                 , String documentation
                                 , String type
                                 , Cardinality cardinality ) {
        this.name = name ;
        this.documentation = documentation ;
        this.type = type ;
        this.cardinality = cardinality ;
    }
    

	public void setName(String name) {
		this.name = name;
	}


	public String getName() {
		return name;
	}


	public Cardinality getCardinality() {
		return cardinality;
	}


	public String getDocumentation() {
		return documentation;
	}


	public String getType() {
		return type;
	}
    

} // end of class ParameterDescriptor
