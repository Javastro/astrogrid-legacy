/*
 * @(#)JobMonitorDelegateImpl.java   1.1
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.jes.delegate.impl;

import org.astrogrid.jes.delegate.JesDelegateException;
import org.astrogrid.jes.delegate.v1.jobmonitor.JobMonitorServiceLocator;
import org.astrogrid.jes.delegate.v1.jobmonitor.JobMonitorServiceSoapBindingStub;

import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
    SOAP-based implementation of a job monitor delegate
 */
public class JobMonitorDelegateImpl extends JobMonitorDelegate {
    
    public JobMonitorDelegateImpl( String targetEndPoint ) {
      this.targetEndPoint = targetEndPoint;
    }
    
    public JobMonitorDelegateImpl( String targetEndPoint, int timeout ) { 
      this.targetEndPoint = targetEndPoint;
      this.timeout = timeout;
    }
    

    
    public void monitorJob(JobIdentifierType id,MessageType info ) throws JesDelegateException {
          
            
        try {
            JobMonitorServiceSoapBindingStub binding = (JobMonitorServiceSoapBindingStub)
                          new JobMonitorServiceLocator().getJobMonitorService( new URL( this.getTargetEndPoint() ) );                        
            binding.setTimeout( this.getTimeout() ) ;    
            binding.monitorJob(id,info);
        }
        catch( MalformedURLException mex ) {
            throw new JesDelegateException( mex ) ;
        }
        catch( IOException  rex) {
            throw new JesDelegateException( rex ) ;            
        }
        catch( javax.xml.rpc.ServiceException sex ) {
            throw new JesDelegateException( sex ) ;    
        }

  
    } // end of monitorJob()    
    
}
