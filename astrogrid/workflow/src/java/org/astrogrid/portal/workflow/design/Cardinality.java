/*
 * @(#)Cardinality.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.portal.workflow.design;

/**
 * The <code>Cardinality</code> class represents... 
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
public class Cardinality {
    
    public static final int
        UNLIMITED = -1 ;
        
    private int
        minimum,
        maximum ;
    
    private Cardinality() {
    }
    
    
    private Cardinality( String cardinality ) {
    }
    
    
    public Cardinality ( int minimum, int maximum ) {
        this.minimum = minimum ;
        this.maximum = maximum ;
    }

} // end of class Cardinality
