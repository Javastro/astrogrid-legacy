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

import org.astrogrid.jes.beans.v1.Job;
import org.astrogrid.jes.delegate.JesDelegateException;
import org.astrogrid.jes.delegate.jobMonitor.JobMonitorDelegate;
import org.astrogrid.jes.delegate.jobMonitor.JobMonitorServiceLocator;
import org.astrogrid.jes.delegate.jobMonitor.JobMonitorServiceSoapBindingStub;

import org.exolab.castor.xml.CastorException;

import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

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
 * @version 1.1 05-Jan-2004 - removed dependency on step number
 * @see     
 * @see     
 * @since   AstroGrid 1.4
 */
public class JobMonitorDelegateImpl extends JobMonitorDelegate {
    
    public JobMonitorDelegateImpl( String targetEndPoint ) {
      this.targetEndPoint = targetEndPoint;
    }
    
    public JobMonitorDelegateImpl( String targetEndPoint, int timeout ) { 
      this.targetEndPoint = targetEndPoint;
      this.timeout = timeout;
    }
    
    /* @deprecated - use oo alternatives
     * 
    temporarily commented out - don't think its used anywhere - not visible..     
    public void monitorJob( String jobURN
                          , Status status ) throws JesDelegateException {  
        this.monitorJob( jobURN, status, "" ) ;
    }  
    */
    
    public void monitorJob( Job job ) throws JesDelegateException {
         
            
        try {
            StringWriter writer = new StringWriter();
            job.marshal(writer);
            writer.close();
            String request = writer.toString();
            JobMonitorServiceSoapBindingStub binding = (JobMonitorServiceSoapBindingStub)
                          new JobMonitorServiceLocator().getJobMonitorService( new URL( this.getTargetEndPoint() ) );                        
            binding.setTimeout( this.getTimeout() ) ;    
            binding.monitorJob(request);
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
        catch (CastorException e) {
            throw new JesDelegateException(e);
        }
  
    } // end of monitorJob()    
    
}
