package org.astrogrid.jes.job;

import org.astrogrid.jes.*;
import org.astrogrid.i18n.*;

public class JobException extends JesException {

    public JobException( AstroGridMessage message ) {
		super( message ) ;
    }

    public JobException( AstroGridMessage message, Exception exception ) {
		super( message, exception ) ;
    }
    
}
