/*
 * @(#)JobControllerException.java   1.0
 *
 * AstroGrid Copyright notice.
 * 
 */

package org.astrogrid.jes.jobmonitor;

import org.astrogrid.jes.i18n.*;

public class JobMonitorException extends JesException {

    public JobMonitorException( Message message ) {
    	super( message ) ;
    }

    public JobMonitorException( Message message, Exception exception ) {
    	super( message, exception ) ;
    }
     
} // end of class JobControllerException
