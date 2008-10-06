/*
 * $Id: MetadataException.java,v 1.1 2008/10/06 12:12:36 pah Exp $
 * 
 * Created on 27 Mar 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
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
 * Exception when dealing with metadata.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 27 Mar 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class MetadataException extends CeaException {

    public MetadataException(String message) {
	super(message);
   }

    public MetadataException(String message, Throwable cause) {
	super(message, cause);
    }

}


/*
 * $Log: MetadataException.java,v $
 * Revision 1.1  2008/10/06 12:12:36  pah
 * factor out classes common to server and client
 *
 * Revision 1.2  2008/09/03 14:18:43  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/03/27 13:34:36  pah
 * now producing correct registry documents
 *
 */
