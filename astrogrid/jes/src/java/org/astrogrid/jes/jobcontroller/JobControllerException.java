/*
 * @(#)JobControllerException.java   1.0
 *
 * AstroGrid Copyright notice.
 * 
 */

package org.astrogrid.jes.jobcontroller;

import org.astrogrid.jes.i18n.*;

public class JobControllerException extends JesException {

    public JobControllerException( Message message ) {
    	super( message ) ;
    }

    public JobControllerException( Message message, Exception exception ) {
    	super( message, exception ) ;
    }
     
} // end of class JobControllerException
