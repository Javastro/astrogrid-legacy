/*
 * @(#)MYS.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.templateservice ;

import org.astrogrid.Configurator ;


/**
 * The <code>MYS</code> class represents 
 *
 * @author  Jeff Lusted
 * @version 1.0 08-Jul-2003
 * @see     
 * @see     
 * @since   AstroGrid 1.2
 */
public class MYS extends org.astrogrid.Configurator {
    
    public static final String 
     /** Three letter acronym for this component within the overall AstroGrid system... 
      *  "MYC" stands for DaTaCenter  */  
         SUBSYSTEM_ACRONYM = "MYS" ; 
            
     public static final String 
     /** Configuration file for this component. */  
         CONFIG_FILENAME = "ASTROGRID_mycomponentconfig.xml" ;    
	    
	public static final String 
	/** DatasetAgent category within the component's configuration */  
		DATASETAGENT_CATEGORY = "DATASETAGENT" ,	
	/** Key within component's configuration signifying whether the web service request
	 *  document is to be parsed with validation turned on or off*/  
		DATASETAGENT_PARSER_VALIDATION = "PARSER.VALIDATION" ;	 		
        
	public static final String         
	/** Monitor category within the component's configuration */  
		MONITOR_CATEGORY = "MONITOR" ,	
	/** Key within component's configuration signifying whether the web service request
	 *  document is to be parsed with validation turned on or off */
		MONITOR_REQUEST_TEMPLATE = "TEMPLATE.REQUEST" ;	
        
    public static final String         
    /** Votable category within the component's configuration */  
        VOTABLE_CATEGORY = "VOTABLE" ,  
    /** Key within component's configuration identifying the Votable factory implementation class */
        VOTABLE_FACTORY = "FACTORY" ;          		
		
	public static final String         
	/** Job category within the component's configuration */  
		JOB_CATEGORY = "JOB" ,	
	/** Key within component's configuration signifying JNDI location of Job datasource */
	    JOB_DATASOURCE_LOCATION = "DATASOURCE" ,	
    /** Key within component's configuration signifying name of Job table */
        JOB_TABLENAME = "TABLENAME" ,
    /** Key within component's configuration identifying the Job factory implementation class */
        JOB_FACTORY = "FACTORY" ;   
           
    public static final String         
    /** MySpace category within the component's configuration */  
        MYSPACE_CATEGORY = "MYSPACE" ,  
    /** Key within component's configuration signifying directory
     *  where VOTables are to be written to */
        MYSPACE_CACHE_DIRECTORY = "CACHE_DIRECTORY" ,   
    /** Key within component's configuration identifying the MySpaceManager service location */
        MYSPACE_URL = "URL" ,  
    /** Key within component's configuration identifying the MySpace factory implementation class */
        MYSPACE_FACTORY = "FACTORY" ,    
    /** Key within component's configuration identifying the MySpace factory implementation class */
        MYSPACE_REQUEST_TEMPLATE = "REQUEST_TEMPLATE" ;                         
        
    public static final String         
    /** Catalog category within the component's configuration */  
        CATALOG_CATEGORY = "CATALOG" ,  
    /** Key within component's configuration identifying the default query factory implementation class */
        CATALOG_DEFAULT_QUERYFACTORY = ".QUERYFACTORY" ,   
    /** Key within component's configuration identifying the default datasource */
        CATALOG_DEFAULT_DATASOURCE = ".DATASOURCE" ,  
    /** Key within component's configuration identifying the default query factory implementation class */
        CATALOG_USNOB_QUERYFACTORY = "USNOB.QUERYFACTORY" ,   
    /** Key within component's configuration identifying the default datasource */
        CATALOG_USNOB_DATASOURCE = "USNOB.DATASOURCE" ; 
        
    public static final String         
    /** UCD category within the component's configuration */  
        UCD_CATEGORY = "UCD" ; 
        
    private static MYS
        singletonMYS = new MYS() ;
        
        
    private MYS(){
        super() ;
    }
    
    
    public static MYS getInstance() {
        return singletonMYS ;
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


    /**
     * 
     * @see org.astrogrid.Configurator#getJNDIName()
     */
    protected String getJNDIName() {
      return null;
    }         
        					   			    
} // end of class MYS
