/*
 * @(#)JobControllerException.java   1.0
 *
 * AstroGrid Copyright notice.
 * 
 */

package org.astrogrid.jes.jobscheduler;

import org.astrogrid.jes.i18n.*;

public class JobSchedulerException extends JesException {

    public JobSchedulerException( Message message ) {
    	super( message ) ;
    }

    public JobSchedulerException( Message message, Exception exception ) {
    	super( message, exception ) ;
    }
     
} // end of class JobControllerException
