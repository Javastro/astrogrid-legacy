package org.astrogrid.jes.job;

import org.astrogrid.jes.i18n.*;

public class JobException extends JesException {

    public JobException( Message message ) {
		super( message ) ;
    }

    public JobException( Message message, Exception exception ) {
		super( message, exception ) ;
    }
    
}
