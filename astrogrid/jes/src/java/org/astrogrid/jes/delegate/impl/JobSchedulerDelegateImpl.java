/*$Id: JobSchedulerDelegateImpl.java,v 1.4 2004/03/03 01:13:41 nw Exp $
 * Created on 06-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.delegate.impl;

import org.astrogrid.jes.delegate.JesDelegateException;
import org.astrogrid.jes.delegate.v1.jobscheduler.JobScheduler;
import org.astrogrid.jes.delegate.v1.jobscheduler.JobSchedulerServiceLocator;
import org.astrogrid.jes.delegate.v1.jobscheduler.JobSchedulerServiceSoapBindingStub;
import org.astrogrid.jes.types.v1.JobURN;

import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.rpc.ServiceException;

/**
 * SOAP-based implementation of a job scheduler delegate.
 * @author Noel Winstanley nw@jb.man.ac.uk 06-Feb-2004
 *
 */
public class JobSchedulerDelegateImpl extends JobSchedulerDelegate {
    /** Construct a new JobSchedulerDelegateImpl
     * @param targetEndPoint
     */
    public JobSchedulerDelegateImpl(String targetEndPoint) {
        this.targetEndPoint = targetEndPoint;
    }
    /** Construct a new JobSchedulerDelegateImpl
     * @param targetEndPoint
     * @param timeout
     */
    public JobSchedulerDelegateImpl(String targetEndPoint, int timeout) {
        this.targetEndPoint = targetEndPoint;
        this.timeout= timeout;
    }
    /** schedule a job */
     public void scheduleNewJob(JobURN r) throws JesDelegateException { 
            
         try {

            JobScheduler binding = createDelegate();  
            binding.scheduleNewJob(r);
         }
         catch( MalformedURLException mex ) {
             throw new JesDelegateException( mex ) ;
         }
         catch(IOException rex) {
             throw new JesDelegateException( rex ) ;            
         }
         catch( javax.xml.rpc.ServiceException sex ) {
             throw new JesDelegateException( sex ) ;    
         }
  
     }
    protected JobScheduler createDelegate() throws ServiceException, MalformedURLException {
         JobSchedulerServiceSoapBindingStub binding = (JobSchedulerServiceSoapBindingStub)
                       new JobSchedulerServiceLocator().getJobSchedulerService( new URL( targetEndPoint ) );                        
         binding.setTimeout( timeout ) ;    
        
        return binding;
    }
    /**
     * @see org.astrogrid.jes.delegate.JobScheduler#resumeJob(org.astrogrid.jes.types.v1.JobInfo)
     */
    public void resumeJob(JobIdentifierType id,MessageType info) throws JesDelegateException {
        try {
            JobScheduler binding = createDelegate();
            binding.resumeJob(id,info);
        } catch (IOException e) {
            throw new JesDelegateException(e);
        } catch (ServiceException e) {
            throw new JesDelegateException(e);
        }
    } 
}


/* 
$Log: JobSchedulerDelegateImpl.java,v $
Revision 1.4  2004/03/03 01:13:41  nw
updated jes to work with regenerated workflow object model

Revision 1.3  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.2.2.4  2004/02/19 13:40:49  nw
updated to match new interfaces for scheduler

Revision 1.2.2.3  2004/02/17 12:25:38  nw
improved javadocs for classes

Revision 1.2.2.2  2004/02/17 11:00:15  nw
altered delegate interfaces to fit strongly-types wsdl2java classes

Revision 1.2.2.1  2004/02/11 16:09:10  nw
refactored delegates (again)

Revision 1.2  2004/02/09 11:41:44  nw
merged in branch nww-it05-bz#85

Revision 1.1.2.1  2004/02/06 18:11:21  nw
reworked the delegate classes
- introduced wrapper class and interfaces, plus separate impl
package with abstract base class. moved delegate classes into the correct
packages, deprecated old methods / classes. fitted in castor object model
 
*/