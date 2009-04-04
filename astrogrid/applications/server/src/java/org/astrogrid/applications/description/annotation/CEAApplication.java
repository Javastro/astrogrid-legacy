/*
 * $Id: CEAApplication.java,v 1.1 2009/04/04 20:41:54 pah Exp $
 * 
 * Created on 2 Apr 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CEAApplication {
    String id();
    String description();
    String referenceURL();
}


/*
 * $Log: CEAApplication.java,v $
 * Revision 1.1  2009/04/04 20:41:54  pah
 * ASSIGNED - bug 2113: better configuration for java CEC
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2113
 * Introduced annotations
 *
 */
