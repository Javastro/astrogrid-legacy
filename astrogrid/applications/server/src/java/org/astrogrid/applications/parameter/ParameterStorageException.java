/*
 * $Id: ParameterStorageException.java,v 1.2 2011/09/02 21:55:52 pah Exp $
 * 
 * Created on 13 Jul 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.parameter;

public class ParameterStorageException extends ParameterAdapterException {

    public ParameterStorageException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public ParameterStorageException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

}


/*
 * $Log: ParameterStorageException.java,v $
 * Revision 1.2  2011/09/02 21:55:52  pah
 * result of merging the 2931 branch
 *
 * Revision 1.1.2.1  2009/07/15 09:49:36  pah
 * redesign of parameterAdapters
 *
 */
