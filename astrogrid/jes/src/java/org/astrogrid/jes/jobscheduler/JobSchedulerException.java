/*
 * @(#)JobSchedulerException.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.jes.jobscheduler;

import org.astrogrid.jes.*;
import org.astrogrid.i18n.*;

public class JobSchedulerException extends JesException {

    public JobSchedulerException( AstroGridMessage message ) {
    	super( message ) ;
    }

    public JobSchedulerException( AstroGridMessage message, Exception exception ) {
    	super( message, exception ) ;
    }
     
} // end of class JobControllerException
