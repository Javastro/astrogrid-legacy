/*
 * @(#)Job.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
 
package org.astrogrid.portal.workflow.jes;

import java.util.HashMap ;
import java.util.ListIterator;
import java.util.Map ;
import java.util.Collections ;
import java.util.Vector ;
import java.util.Iterator ;
import java.text.MessageFormat ;
import java.io.InputStream ;
import org.xml.sax.* ;
import java.io.StringReader ;

import org.apache.log4j.Logger ;
import org.apache.axis.utils.XMLUtils ;
import org.w3c.dom.* ;

import org.astrogrid.i18n.*;
import org.astrogrid.AstroGridException ;
import org.astrogrid.jes.delegate.jobController.*;

import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate;
import org.astrogrid.portal.workflow.*;
import org.astrogrid.portal.workflow.design.activity.*;
import org.w3c.dom.Document ;

/**
 * The <code>Job</code> class represents 
 * 
 *
 * @author  Jeff Lusted
 * @version 1.0 21-Aug-2003
 * @see     
 * @see     
 * @since   AstroGrid 1.3
 */
public class Job {
	
    /** Compile-time switch used to turn tracing on/off. 
      * Set this to false to eliminate all trace statements within the byte code.*/         
    private static final boolean 
        TRACE_ENABLED = true ;
        
    private static Logger 
        logger = Logger.getLogger( Job.class ) ; 
        
    private static final String
        ASTROGRIDERROR_SOMEMESSAGE = "AGWKFE00050" ; // none so far 
        
        
    public static Job readJob( String userid, String community, String name ) {
        if( TRACE_ENABLED ) trace( "Job.readJob() entry") ; 
        
        Job
            job = null;
        StringBuffer
            pathBuffer = new StringBuffer( 64 ) ;
        String
            xmlString = null ;
         
        try {
      
        }
        catch ( Exception ex ) {
            ex.printStackTrace() ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "Job.readJob() exit") ; 
        }
       
        return job ;
        
    } // end of readJob() 
    
    
    public static boolean deleteJob( String userid, String community, String name  ) {
        if( TRACE_ENABLED ) trace( "Job.deleteJob() entry") ; 
        
        boolean
            retValue = true ;
         
        try {         

        }
        catch( Exception ex ) {
            ex.printStackTrace() ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "Job.deleteJob() exit") ; 
        }
        
        return retValue ;
        
    } // end of deleteJob()
    
    
    public static boolean cancelJob( Job job ) {
        if( TRACE_ENABLED ) trace( "Job.cancelJob() entry") ; 

        boolean
            retValue = false ;
        String
            request = null,
            jesLocation = null ;
        JobControllerDelegate
            jobController = null ;
                    
        try {
            jesLocation = WKF.getProperty( WKF.JES_URL, WKF.JES_CATEGORY ) ;

            jobController = new JobControllerDelegate( jesLocation ) ;
//            jobController.submitJob( request ) ;            
        }
        catch( Exception ex ) {
            ex.printStackTrace() ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "Job.cancelJob() exit") ; 
        }
        
        return false ;

    } // end of cancelJob()
    
    
    /*
     * 
     */
    public static ListIterator readJobList( String userid, String community, String filter ) {
        if( TRACE_ENABLED ) trace( "Job.readJobList() entry") ; 
        
        // JBL: For the moment we are ignoring filter.
        
        ListIterator
           iterator = null ;
        java.util.Vector
           vector = null ;
        StringBuffer
           argumentBuffer = new StringBuffer( 64 ) ;
        
        try {
                          
        }
        catch ( Exception ex ) {
            ex.printStackTrace() ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "Job.readJobList() exit") ; 
        }
       
        return iterator ;
        
    } // end of readJobList()
    
     
    private String
        name,
        description,
        userid,
        community ;            
        
    /**
      * <p> 
      * Default constructor.
      * <p>
      * 
      * 
      **/           
    private Job() {
        if( TRACE_ENABLED ) trace( "Job() entry") ;

        if( TRACE_ENABLED ) trace( "Job() exit") ;
    }
    
    
public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserid() {
		return userid;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	public String getCommunity() {
		return community;
	}
    
    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    } 
     
     
    private static void trace( String traceString ) {
        System.out.println( traceString ) ;
        // logger.debug( traceString ) ;
    }
    
    private static void debug( String logString ){
        System.out.println( logString ) ;
        // logger.debug( logString ) ;
    }
    
} // end of class Job