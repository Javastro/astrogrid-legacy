/*
 * @(#)JobMinotaurServlet.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.jes.jobmonitor;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.astrogrid.jes.*;
import org.astrogrid.i18n.* ;
import org.astrogrid.AstroGridException;

import org.apache.log4j.Logger;


/**
 * The <code>JobMinotaurServlet</code> class represents 
 *
 * @author  Jeff Lusted
 * @version 1.0 30-Jul-2003
 *
 * @since   AstroGrid 1.2
 */
public class JobMinotaurServlet extends GenericServlet {
    
    /** Compile-time switch used to turn tracing on/off. 
      * Set this to false to eliminate all trace statements within the byte code.*/         
    private static final boolean 
        TRACE_ENABLED = true ;
        
    private static final String
        ASTROGRIDWARNING_MINOTAUR_DAEMON_USING_DEFAULT_SLEEPTIME = "AGJESW01000" ; 
                    
    private static Logger 
        logger = Logger.getLogger( JobMinotaurServlet.class ),
        daemonLogger = Logger.getLogger( MinotaurDaemon.class ) ;   
        
    private static final long
        DEFAULT_SLEEP_TIME = 300000 ;
        
    private long
        sleepTime = DEFAULT_SLEEP_TIME ;
        
    private Thread 
        daemonThread ;
        
    private boolean
        shutdown = false ;

	/**
	  * Class 
	  */
	public JobMinotaurServlet() {
        if( TRACE_ENABLED ) logger.debug( "JobMinotaurServlet() entry/exit") ;
	}
    
    public void init() {
        if( TRACE_ENABLED ) logger.debug( "JobMinotaurServlet.init(): entry") ;
        
        try {           
            JES.getInstance().checkPropertiesLoaded() ;   
            String
               sleepTimeString = JES.getProperty( JES.MONITOR_DAEMON_SLEEPTIME
                                                , JES.MONITOR_CATEGORY ) ; 
            sleepTime = new Long( sleepTimeString ).longValue() ;
        }
        catch( AstroGridException agex ) {
            logUsingDefaultSleepTime( agex.getAstroGridMessage().toString() ) ;  
        }
        catch( NumberFormatException  nfex ) {
            logUsingDefaultSleepTime( nfex.getLocalizedMessage() ) ;
        }
        finally {
            daemonThread = new MinotaurDaemon( this ) ;
            daemonThread.start();
            if( TRACE_ENABLED ) logger.debug( "JobMinotaurServlet.init(): exit") ;  
        }
                    
    } // end of init()
    
    
    public void destroy() {
        if( TRACE_ENABLED ) logger.debug( "JobMinotaurServlet.destroy(): entry") ;  
              
        try {      
            this.setShutdown( true ) ;
            daemonThread.interrupt() ;
        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "JobMinotaurServlet.destroy(): exit") ;
        }
      
    } // end of destroy() 
    
    
	public void service( ServletRequest request, ServletResponse response )
		                 throws ServletException, IOException {
        if( TRACE_ENABLED ) logger.debug( "JobMinotaurServlet.service(): entry/exit") ;
	}


	protected synchronized void setShutdown( boolean shutdown ) {
        this.shutdown = shutdown ;
	}


	protected synchronized boolean getShutdown() {
		return this.shutdown ;
	}
    
    
    private void logUsingDefaultSleepTime( String exceptionMessage ) {
        // First log the exception that caused us to default...
        logger.warn( exceptionMessage ) ;
        // Now log we are using the default sleep time...
        AstroGridMessage
            message = new AstroGridMessage( ASTROGRIDWARNING_MINOTAUR_DAEMON_USING_DEFAULT_SLEEPTIME
                                          , new Long( sleepTime ) ) ;
        logger.warn( message.toString() ) ;
    }


    private class MinotaurDaemon extends Thread {
        
        private JobMinotaurServlet 
            servlet;

        public MinotaurDaemon( JobMinotaurServlet servlet ) {
            this.servlet = servlet;
        }
        
        
        public void run() {
            if( TRACE_ENABLED ) daemonLogger.debug( "MinotaurDaemon.run(): entry") ;
            
            try {
                
                while ( servlet.getShutdown() == false ) {
                    
                    try{
                        MinotaurDaemon.sleep( servlet.sleepTime ) ;
                        monitorJobs() ;
                    }
                    catch ( InterruptedException iex ) {
                        continue ;
                    }
                                 
                }
                
            }
            finally {
                if( TRACE_ENABLED ) daemonLogger.debug( "MinotaurDaemon.run(): exit") ; 
            }
            
        } // end of MinotaurDaemon.run()
        
        
        private void monitorJobs() {   
        }
        
         
    } // end of inner class MinotaurDaemon


} // end of class JobMinotaurServlet