package org.astrogrid.jes.job;

import org.astrogrid.i18n.AstroGridMessage;
import org.astrogrid.jes.JesException;

public class JobException extends JesException {

    public JobException( AstroGridMessage message ) {
		super( message ) ;
    }

    public JobException( AstroGridMessage message, Exception exception ) {
		super( message, exception ) ;
    }
    
}
