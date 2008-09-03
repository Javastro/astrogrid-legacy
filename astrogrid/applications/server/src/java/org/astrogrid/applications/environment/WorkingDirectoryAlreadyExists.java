/*
 * $Id: WorkingDirectoryAlreadyExists.java,v 1.2 2008/09/03 14:18:55 pah Exp $
 * 
 * Created on 1 Sep 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.environment;

import org.astrogrid.applications.CeaException;

public class WorkingDirectoryAlreadyExists extends CeaException {

    public WorkingDirectoryAlreadyExists(String message) {
	super(message);
	
    }

    public WorkingDirectoryAlreadyExists(String message, Throwable cause) {
	super(message, cause);
	
    }

}


/*
 * $Log: WorkingDirectoryAlreadyExists.java,v $
 * Revision 1.2  2008/09/03 14:18:55  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/09/03 12:22:55  pah
 * improve unit tests so that they can run in single eclipse gulp
 *
 */
