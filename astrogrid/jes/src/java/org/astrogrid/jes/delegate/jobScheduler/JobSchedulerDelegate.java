package org.astrogrid.jes.delegate.jobScheduler;

import org.astrogrid.jes.beans.v1.Job;
import org.astrogrid.jes.delegate.JesDelegateException;
import org.astrogrid.jes.delegate.JobScheduler;
import org.astrogrid.jes.delegate.impl.AbstractDelegate;
import org.astrogrid.jes.delegate.impl.JobSchedulerDelegateImpl;
import org.astrogrid.jes.delegate.impl.TestJobSchedulerDelegateImpl;

import org.exolab.castor.xml.CastorException;

import java.io.StringReader;

/**
 * The <code>JobSchedulerDelegate</code> class. 
 *
 * @deprecated use {@link org.astrogrid.jes.delegate.JesDelegateFactory} instead
 * @author  Jeff Lusted
 * @version 1.0 02-Aug-2003
 * @since   AstroGrid 1.2
 */
public abstract class JobSchedulerDelegate extends AbstractDelegate implements JobScheduler{
    public static JobSchedulerDelegate buildDelegate( String targetEndPoint ){
        return buildDelegate( targetEndPoint, DEFAULT_TIMEOUT ) ;
    }
    
    /**

     * @param targetEndPoint
     * @param timeout
     * @return
     */
    public static JobSchedulerDelegate buildDelegate( String targetEndPoint
                                                  , int timeout ) { 
            if( AbstractDelegate.isTestDelegateRequired(targetEndPoint) ) {
                 return new TestJobSchedulerDelegateImpl();
            } else {
                return new JobSchedulerDelegateImpl(targetEndPoint, timeout ) ;
            }   
    }      
    
    /** @deprecated - use oo-orientated methods instead */
    public void scheduleJob(String req) throws JesDelegateException {
        try {
            Job j = Job.unmarshal(new StringReader(req));
            this.scheduleJob(j);
        } catch (CastorException e) {
            throw new JesDelegateException("Could not marshall old snippet into new object model",e);
        }
        
    }
    
    public abstract void scheduleJob(Job j) throws JesDelegateException;
    
 

} // end of class JobSchedulerDelegate