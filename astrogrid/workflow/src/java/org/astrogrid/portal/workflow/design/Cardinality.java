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

import org.apache.log4j.Logger ;

/**
 * The <code>Cardinality</code> class represents the maximum and mimimum
 * allowed cardinalities of a <code>Parameter</code>. It is a simple holder of 
 * cardinalities.
 * <p>
 * At present it throws no exceptions but tries to set defaults on nonsensical values.
 * In a later iteration should throw exceptions.
 *
 * @author  Jeff Lusted
 * @version 1.0 20-Nov-2003  
 * @since   AstroGrid 1.4
 * @deprecated use workflow-object object model
 */
public class Cardinality {
    
    /** Compile-time switch used to turn tracing on/off. 
      * Set this to false to eliminate all trace statements within the byte code.*/         
    private static final boolean 
        TRACE_ENABLED = true ;
        
    private static Logger 
        logger = Logger.getLogger( Cardinality.class ) ;    
     
    /**
     * A cardinality with no upper limit must be represented somehow.
     * As usual the protocol is to use -1.
     */   
    public static final int
        UNLIMITED = -1 ;
        
    /**
     * The default minimum cardinality is 1.
     */   
    public static final int
        MIN_DEFAULT = 1 ;
        
    /**
     * The default maximum cardinality is 1.
     */   
    public static final int
        MAX_DEFAULT = 1 ;
        
    private int
        minimum,
        maximum ;
      
    public Cardinality() {
        this.minimum = MIN_DEFAULT ;
        this.maximum = MAX_DEFAULT ;
    }
    
    
    /** 
      * Cardinality constructor with maximum and minimum as integers.
      * 
      * @param minimum  minimum cardinality desired
      * @param maximum  maximum cardinality desired
      **/        
    public Cardinality ( int minimum, int maximum ) {
        this.minimum = minimum ;
        this.maximum = maximum ;
        this.checkAndSetForSensibleValues() ;
    }
    
    
    /** 
      * Cardinality constructor with maximum and minimum as Strings.
      * <p>
      * Any invalid parameters
      * 
      * @param minString  minimum cardinality desired
      * @param maxString  maximum cardinality desired
      **/   
    public Cardinality ( String minString, String maxString ) {
        if( TRACE_ENABLED ) trace( "entry: Cardinality(String,String)") ;  
        
        Integer
            min,
            max ;
        
        try {
            try { min = new Integer( minString ); } catch( NumberFormatException nfe ) { min = new Integer(MIN_DEFAULT); }
            try { max = new Integer( maxString ); } catch( NumberFormatException nfe ) { max = new Integer(MAX_DEFAULT); }
            this.minimum = min.intValue() ;
            this.maximum = max.intValue() ;
            this.checkAndSetForSensibleValues() ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "exit: Cardinality(String,String)") ; 
        }
        
    }
    
   
	/**
     * Getter for maximum cardinality.
     * 
     * @return maximum cardinality as an integer
	  */
	public int getMaximum() {
		return maximum;
	}

	/**
     * Getter for minimum cardinality.
     * 
     * @return minimum cardinality as an integer
	  */
	public int getMinimum() {
		return minimum;
	}
    
    /**
     * Tester for presence of unlimited maximum cardinality.
     * 
     * @return boolean true for unlimited maximum cardinality
     */
    public boolean isUnlimited() {
        return (maximum == Cardinality.UNLIMITED) ;
    }
    
    private void checkAndSetForSensibleValues() {
        
        if( this.maximum < Cardinality.UNLIMITED ) {
            this.maximum = Cardinality.UNLIMITED ;
        }
        else if( this.maximum == 0 ) {
            this.maximum = MAX_DEFAULT ;
        }
    
        if( this.minimum < 0 ) {
            this.minimum = MIN_DEFAULT ;
        }
               
        if( (this.maximum != Cardinality.UNLIMITED)
            &&
            (this.maximum < this.minimum) ) {
            this.minimum = Cardinality.MIN_DEFAULT ;
            this.maximum = Cardinality.MAX_DEFAULT ;       
        }
           
    }
    
    
    private static void trace( String traceString ) {
        System.out.println( traceString ) ;
        // logger.debug( traceString ) ;
    }
    
    
    private static void debug( String logString ){
        System.out.println( logString ) ;
        // logger.debug( logString ) ;
    }

} // end of class Cardinality
