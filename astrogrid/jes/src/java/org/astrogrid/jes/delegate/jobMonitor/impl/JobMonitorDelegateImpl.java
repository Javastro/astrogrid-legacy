/*
 * @(#)JobMonitorDelegateImpl.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.jes.delegate.jobMonitor.impl;

import org.astrogrid.jes.delegate.jobMonitor.* ;
import java.net.URL ;
import java.net.MalformedURLException ;
import java.rmi.RemoteException ;
import org.astrogrid.jes.delegate.JesDelegateException ;

/**
 * The <code>JobMonitorDelegateImpl</code> class represents... 
 * <p>
 *
 * <p>
 * The class... 
 * 
 *
 * @author  Jeff Lusted
 * @version 1.0 19-Dec-2003
 * @see     
 * @see     
 * @since   AstroGrid 1.4
 */
public class JobMonitorDelegateImpl extends JobMonitorDelegate {
    
    public JobMonitorDelegateImpl( String targetEndPoint ) {
      super( targetEndPoint ) ;
    }
    
    public JobMonitorDelegateImpl( String targetEndPoint, int timeout ) {
      super( targetEndPoint, timeout ) ;
    }
    
    
    public void monitorJob( String jobURN
                          , int stepNumber
                          , Status status ) throws JesDelegateException {  
        this.monitorJob( jobURN, stepNumber, status, "" ) ;
    }  
    
    
    public void monitorJob( String jobURN
                          , int stepNumber
                          , Status status
                          , String comment ) throws JesDelegateException {
        if(TRACE_ENABLED) trace( "JobMonitorDelegate.monitorJob() entry" ) ;
               
        JobMonitorServiceSoapBindingStub 
            binding = null ;
        String 
            request = JobMonitorDelegate.formatMonitorRequest( jobURN
                                                             , stepNumber
                                                             , status
                                                             , comment ) ;           
            
        try {
            binding = (JobMonitorServiceSoapBindingStub)
                          new JobMonitorServiceLocator().getJobMonitorService( new URL( this.getTargetEndPoint() ) );                        
            binding.setTimeout( this.getTimeout() ) ;    
            binding.monitorJob(request);
        }
        catch( MalformedURLException mex ) {
            throw new JesDelegateException( mex ) ;
        }
        catch( RemoteException rex) {
            throw new JesDelegateException( rex ) ;            
        }
        catch( javax.xml.rpc.ServiceException sex ) {
            throw new JesDelegateException( sex ) ;    
        }
        finally {
            if(TRACE_ENABLED) trace( "JobMonitorDelegate.monitorJob() exit" ) ;   
        }
              
        return ;
  
    } // end of monitorJob()    
    

    private static void trace( String traceString ) {
        System.out.println( traceString ) ;
        // logger.debug( traceString ) ;
    }
    
    private static void debug( String logString ){
        System.out.println( logString ) ;
        // logger.debug( logString ) ;
    }

}
