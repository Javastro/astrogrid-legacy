/*
 * @(#)MyComponentException.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.templateservice.templatecomponent ;

import org.astrogrid.templateservice.MySubsystemException ;
import org.astrogrid.i18n.AstroGridMessage ;

public class MyComponentException extends MySubsystemException {

    public MyComponentException( AstroGridMessage message ) {
    	super( message ) ;
    }

    public MyComponentException( AstroGridMessage message, Exception exception ) {
    	super( message, exception ) ;
    }
     
}
