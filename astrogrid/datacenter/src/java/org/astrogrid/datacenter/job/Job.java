/*
 * @(#)Job.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.datacenter.job;

import java.util.Date;

import org.apache.log4j.Logger;
import org.astrogrid.datacenter.Util;
import org.astrogrid.datacenter.config.Configurable;
import org.astrogrid.datacenter.config.ConfigurableImpl;

/** some documentation wouldn't go amiss here.. */
public abstract class Job  extends ConfigurableImpl implements Configurable {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( Job.class ) ;
        
    private final static String
        SUBCOMPONENT_NAME = Util.getComponentName( Job.class ) ;
		

	   
	public static  final String
	    STATUS_INITIALIZED = "INITIALIZED",  // Newly created but not yet running
	    STATUS_RUNNING = "RUNNING",          // Currently executing
	    STATUS_COMPLETED = "COMPLETED",      // Completed OK
	    STATUS_IN_ERROR = "ERROR" ;          // Something bad happened


   
    	
   
    public abstract void informJobMonitor() ;

    public abstract String getStatus() ;
    public abstract void setStatus(String status) ;

    public abstract String getId() ;   // Job URN
    
    public abstract String getName() ;
    public abstract void setName( String name ) ;
    
	public abstract Date getDate() ;
	public abstract void setDate( Date date ) ;
    
    public abstract String getUserId() ;
    public abstract void setUserId( String name ) ;
    
    public abstract String getCommunity() ;
    public abstract void setCommunity( String community ) ;
    
    public abstract String getJobMonitorURL() ;
    public abstract void setJobMonitorURL( String url ) ;
    
    public abstract JobStep getJobStep() ;
    public abstract void setJobStep( JobStep jobStep ) ;
    
	public abstract String getComment() ;
	public abstract void setComment( String comment ) ;
	
	public abstract Object getImplementation() ;
 
} // end of class Job
