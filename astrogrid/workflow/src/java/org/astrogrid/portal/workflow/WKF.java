/*
 * @(#)WKF.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.portal.workflow;

import org.astrogrid.Configurator ;

/**
 * The <code>WKF</code> class represents 
 *
 * @author  Jeff Lusted
 * @version 1.0 08-Jul-2003
 * @see     
 * @see     
 * @since   AstroGrid 1.2
 */
public class WKF extends org.astrogrid.Configurator {
    
    private static final String 
    /** Three letter acronym for this subsystem within the overall AstroGrid system... 
     *  "WKF" stands for Workflow  */  
        SUBSYSTEM_ACRONYM = "WKF" ; 
            
    private static final String 
    /** Configuration file for this component. */  
        CONFIG_FILENAME = "ASTROGRID_workflowconfig.xml" ;
	    
	public static final String 
	/** Workflow category within the component's configuration */  
		WORKFLOW_CATEGORY = "WORKFLOW" ,	
	/** Key within component's configuration identifying the template used for single step jobs. */  
        WORKFLOW_TEMPLATE_SINGLESTEP = "TEMPLATE.SINGLESTEP" ,
    /** Key within component's configuration identifying the template used for two step sequence. */  
        WORKFLOW_TEMPLATE_TWOSTEPSEQUENCE = "TEMPLATE.TWOSTEPSEQUENCE" ,
    /** Key within component's configuration identifying the template used for two step flow. */  
        WORKFLOW_TEMPLATE_TWOSTEPFLOW = "TEMPLATE.TWOSTEPFLOW" ;  
    
    public static final String 
    /** MySpace category within the component's configuration */  
        MYSPACE_CATEGORY = "MYSPACE" ,    
    /** Key within component's configuration identifying the MySpace URL. */  
        MYSPACE_URL = "URL" ,      
    /** Key within component's configuration identifying the MySpace cache directory. */  
        MYSPACE_CACHE_DIRECTORY = "/tmp" ,
    /** Key within component's configuration identifying the template for making a MySpace request. */     
        MYSPACE_REQUEST_TEMPLATE = "TEMPLATE.REQUEST" ;    
        
    public static final String 
    /** Jes category within the component's configuration */  
        JES_CATEGORY = "JES" ,    
    /** Key within component's configuration identifying the Jes URL. */  
        JES_URL = "URL" ;
    
    private static WKF
        singletonWKF = new WKF() ;
        
        
    private WKF(){
        super() ;
    }
    
    
    public static WKF getInstance() {
        return singletonWKF ;
    }
        
    
    
    /**
      *  
      * Static getter for properties from the component's configuration.
      * <p>
      * 
      * @param key - the property key within category
      * @param category - the category within the configuration
      * @return the String value of the property, or the empty string if null
      * 
      * @see org.jconfig.jConfig
      **/       
    public static String getProperty( String key, String category ) {
        return Configurator.getProperty( SUBSYSTEM_ACRONYM, key, category ) ;
    }
 
    protected String getConfigFileName() { return CONFIG_FILENAME ; }    
    protected String getSubsystemAcronym() { return SUBSYSTEM_ACRONYM ; }
    
	 
} // end of class WKF
