/*$Id: JobSchedulerDelegateImpl.java,v 1.2 2004/02/09 11:41:44 nw Exp $
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

import org.astrogrid.jes.beans.v1.Job;
import org.astrogrid.jes.delegate.JesDelegateException;
import org.astrogrid.jes.delegate.jobScheduler.JobSchedulerDelegate;
import org.astrogrid.jes.delegate.jobScheduler.JobSchedulerServiceLocator;
import org.astrogrid.jes.delegate.jobScheduler.JobSchedulerServiceSoapBindingStub;

import org.exolab.castor.xml.CastorException;

import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

/**
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
     public void scheduleJob(Job j) throws JesDelegateException { 
            
         try {
            
             StringWriter sw = new StringWriter();
             j.marshal(sw);
             sw.close();
             String req = sw.toString(); 
             JobSchedulerServiceSoapBindingStub binding = (JobSchedulerServiceSoapBindingStub)
                           new JobSchedulerServiceLocator().getJobSchedulerService( new URL( targetEndPoint ) );                        
             binding.setTimeout( timeout ) ;    
             binding.scheduleJob(req);
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
         catch (CastorException cax) {
             throw new JesDelegateException("Could not marhsall job to xml",cax);
         }

  
     } // end of scheduleJob()
}


/* 
$Log: JobSchedulerDelegateImpl.java,v $
Revision 1.2  2004/02/09 11:41:44  nw
merged in branch nww-it05-bz#85

Revision 1.1.2.1  2004/02/06 18:11:21  nw
reworked the delegate classes
- introduced wrapper class and interfaces, plus separate impl
package with abstract base class. moved delegate classes into the correct
packages, deprecated old methods / classes. fitted in castor object model
 
*/