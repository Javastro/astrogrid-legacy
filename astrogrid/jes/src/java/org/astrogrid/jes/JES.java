/*
 * @(#)JES.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.jes ;

import org.astrogrid.Configurator ;

/**
 * The <code>JES</code> class represents 
 *
 * @author  Jeff Lusted
 * @version 1.0 08-Jul-2003
 * @see     
 * @see     
 * @since   AstroGrid 1.2
 */
public class JES extends org.astrogrid.Configurator {
    
    private static final String 
    /** Three letter acronym for this subsystem within the overall AstroGrid system... 
     *  "JES" stands for Job Entry System  */  
        SUBSYSTEM_ACRONYM = "JES" ; 
            
    private static final String 
    /** Configuration file for this component. */  
        CONFIG_FILENAME = "ASTROGRID_jesconfig.xml" ;
    
    public static final String 
    /** Jes category within the component's configuration */  
        JES_CATEGORY = "JES" ,    
    /** Key within component's configuration identifying the unique JES identifier. */  
        JES_ID = "ID" ;   
	    
	public static final String 
	/** Controller category within the component's configuration */  
		CONTROLLER_CATEGORY = "CONTROLLER" ,	
	/** Key within component's configuration identifying the template used for submit job response. */  
		CONTROLLER_SUBMIT_JOB_RESPONSE_TEMPLATE = "TEMPLATE.SUBMIT_JOB_RESPONSE",
	/** Key within component's configuration identifying the url to the current JobController */  
		CONTROLLER_URL = "URL",
	/** Key within component's configuration signifying whether the web service request
	 *  document is to be parsed with validation turned on or off*/  
		CONTROLLER_PARSER_VALIDATION = "PARSER.VALIDATION" ;	 		
			
	public static final String 
	/** Scheduler category within the component's configuration */  
		SCHEDULER_CATEGORY = "SCHEDULER" ,	
	/** Key within the component's configuration identifying the template
	 *  used for the schedule job request */  
		SCHEDULER_JOB_REQUEST_TEMPLATE = "TEMPLATE.SCHEDULE_JOB_REQUEST",
	/** Key within the component's configuration identifying the url
	 *  to be used for JobScheduler requests */  
		SCHEDULER_URL = "URL",
	/** Key within component's configuration signifying whether the web service request
	 *  document is to be parsed with validation turned on or off*/  
        SCHEDULER_PARSER_VALIDATION = "PARSER.VALIDATION" ;	
        
	public static final String         
	/** Monitor category within the component's configuration */  
		MONITOR_CATEGORY = "MONITOR" ,	
	/** Key within component's configuration identifying the url to the JobMonitor */  		
		MONITOR_URL = "URL" ,     
	/** Key within component's configuration signifying whether the web service request
	 *  document is to be parsed with validation turned on or off */
		MONITOR_PARSER_VALIDATION = "PARSER.VALIDATION" ,	
    /** Key within component's configuration identifying the sleep time to be used
     *  by the JobMinotaurDaemon between checking for unfinished jobsteps.
     *  The figure is given in milliseconds.  */       
        MONITOR_DAEMON_SLEEPTIME = "DAEMON_SLEEPTIME" ;           		
					   			    
	public static final String 
	/** Message log category within the component's configuration */  
		MESSAGE_LOG_CATEGORY = "MESSAGE_LOG" ,	
	/** Key within the component's configuration identifying the template
	 *  used in formatting a message document for the AstroGrid message log */  
		MESSAGE_LOG_REQUEST_TEMPLATE  = "TEMPLATE.MESSAGE_LOG_REQUEST.",
	/** Key within the component's configuration identifying the url
	 *  to be used for AstroGrid message log requests */  
		MESSAGE_LOG_URL = "URL" ;
		
	public static final String 
	/** Registry category within the component's configuration */  
		REGISTRY_CATEGORY = "REGISTRY" ,	
	/** Key within the component's configuration identifying the template
	 *  used for the schedule job request */  
        REGISTRY_REQUEST_TEMPLATE = "TEMPLATE.REGISTRY_REQUEST",
	/** Key within the component's configuration identifying the url
	 *  to be used for JobScheduler requests */  
		REGISTRY_URL = "URL";
        
    public static final String 
    /** Job category within the component's configuration */  
        JOB_CATEGORY = "JOB" ,    
    /** Key within the component's configuration identifying the factory implementation class */  
        JOB_FACTORY = "FACTORY",
    /** Key within the component's configuration identifying the Job datasource */  
        JOB_DATASOURCE = "DATASOURCE",
    /** Key within the component's configuration identifying the Job table name */  
        JOB_TABLENAME_JOB = "TABLENAME.JOB",
    /** Key within the component's configuration identifying the JobStep table name */  
        JOB_TABLENAME_JOBSTEP = "TABLENAME.JOBSTEP",
    /** Key within the component's configuration identifying the Query table name */  
        JOB_TABLENAME_QUERY = "TABLENAME.QUERY",
    /** Key within the component's configuration identifying the Catalog table name */  
        JOB_TABLENAME_CATALOG = "TABLENAME.CATALOG",
    /** Key within the component's configuration identifying the Table table name */  
        JOB_TABLENAME_TABLE = "TABLENAME.TABLE",
    /** Key within the component's configuration identifying the Service table name */  
        JOB_TABLENAME_SERVICE = "TABLENAME.SERVICE", 
    /** Key within the component's configuration identifying the JobId table name */  
        JOB_TABLENAME_JOBID = "TABLENAME.JOBID",         
    /** Key within the component's configuration identifying the range of Job 
     *  identifiers (an int) that can be pre-booked (for performance reasons).
     *  The range is held in the JOBID table. We do not use a native RDBMS technique
     *  such as is available within DB2 or Oracle because these are essentially
     *  not portable.   */  
        JOB_ID_BOOKABLERANGE = "ID.BOOKABLERANGE";  
         
    
    private static JES
        singletonJES = new JES() ;
        
        
    private JES(){
        super() ;
    }
    
    
    public static JES getInstance() {
        return singletonJES ;
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
    
	 
} // end of class JES
