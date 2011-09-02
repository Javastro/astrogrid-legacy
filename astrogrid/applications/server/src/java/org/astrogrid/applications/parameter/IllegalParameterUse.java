/*
 * $Id: IllegalParameterUse.java,v 1.2 2011/09/02 21:55:51 pah Exp $
 * 
 * Created on 16 Jul 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.parameter;

/**
 * Exception thrown when parameter adapter used incorrectly. Typically this is when an attempt is made to write back to an input parameter.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 16 Jul 2009
 * @version $Name:  $
 * @since AIDA Stage 1
 */
public class IllegalParameterUse extends ParameterAdapterException {

    public IllegalParameterUse(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public IllegalParameterUse(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

}


/*
 * $Log: IllegalParameterUse.java,v $
 * Revision 1.2  2011/09/02 21:55:51  pah
 * result of merging the 2931 branch
 *
 * Revision 1.1.2.1  2009/07/16 19:48:05  pah
 * ASSIGNED - bug 2950: rework parameterAdapter
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2950
 *
 */
