/*
 * $Id: InvalidVOSUri.java,v 1.2 2008/09/17 08:16:05 pah Exp $
 * 
 * Created on Oct 23, 2006 by Paul Harrison (pharriso@eso.org)
 * Copyright 2006 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.common.id;

import java.net.URI;

import org.astrogrid.common.AGCommonException;

/**
 * Thrown when an invalid vos: scheme URI is found. Note that the uri could be invalid (even when a valid {@link URI}) because there are additional rules for vos: uri
 * e.g.
 * <li> in level 1 vospaces containers are not allowed in the path part
 * <li> the requested authority must match the authority of the vospace of the current vospace. 
 * 
 * @author Paul Harrison (pharriso@eso.org) Nov 6, 2006
 * @version $Name:  $
 * @since VOTech Stage 4
 */
public class InvalidVOSUri extends AGCommonException {

	public InvalidVOSUri(String message, Throwable cause) {
		super(message, cause);
		
		
	}

	public InvalidVOSUri(String message) {
		super(message);
		
	}

}


/*
 * $Log: InvalidVOSUri.java,v $
 * Revision 1.2  2008/09/17 08:16:05  pah
 * result of merge of pah_community_1611 branch
 *
 * Revision 1.1.2.1  2008/05/17 20:55:13  pah
 * safety checkin before interop
 *
 * Revision 1.1  2007/01/11 15:48:51  pharriso
 * reorganised so that the project would build better with maven
 *
 * Revision 1.2  2006/11/10 07:17:52  pharriso
 * basic vospace working with NGAS backend
 *
 * Revision 1.1  2006/10/24 07:05:48  pharriso
 * basic in-memory vospace working with authentication
 *
 */
