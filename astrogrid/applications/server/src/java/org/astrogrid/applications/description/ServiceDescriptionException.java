/*
 * $Id: ServiceDescriptionException.java,v 1.2 2008/09/03 14:18:43 pah Exp $
 * 
 * Created on 17 Mar 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description;

import org.astrogrid.applications.CeaException;

/**
 * an error occurred while creating a description of the service.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 17 Mar 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class ServiceDescriptionException extends MetadataException {

    public ServiceDescriptionException(String message, Throwable cause) {
	super(message, cause);
	// TODO Auto-generated constructor stub
    }

    /** Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 7351230697592276625L;

}


/*
 * $Log: ServiceDescriptionException.java,v $
 * Revision 1.2  2008/09/03 14:18:43  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.2  2008/03/27 13:34:36  pah
 * now producing correct registry documents
 *
 * Revision 1.1.2.1  2008/03/19 23:10:53  pah
 * First stage of refactoring done - code compiles again - not all unit tests passed
 *
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 *
 */
