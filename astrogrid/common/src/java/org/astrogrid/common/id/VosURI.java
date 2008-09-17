/*
 * $Id: VosURI.java,v 1.2 2008/09/17 08:16:05 pah Exp $
 * 
 * Created on Aug 7, 2006 by Paul Harrison (pharriso@eso.org)
 * Copyright 2006 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.common.id;

import java.net.URI;
import java.net.URISyntaxException;

import org.astrogrid.common.util.URLEncoderHelper;


/**
 * Utility class for manipulating vos: scheme URIs.
 * @author Paul Harrison (pharriso@eso.org) Aug 7, 2006
 * @version $Name:  $
 * @since initial Coding
 */
public class VosURI {
	URI uri;

	public VosURI(String val) throws InvalidVOSUri
	{
		if(!val.startsWith("vos:")){
			throw new InvalidVOSUri("the scheme is not vos:");
		}
		String urival;
		int i = val.indexOf("/", 6);//index of the / after the authority part.
		//TODO really need to do what is appropriate for the fragment and query part also...
		if(i != -1)
		{
			urival=val.substring(0, i+1) + URLEncoderHelper.encode(val.substring(i+1));
		}
		else
		{
			urival = val;
		}
		try {			
			uri = new URI(urival);
			
		} catch (URISyntaxException e) {
			// rethow with the service specific exception
			throw new InvalidVOSUri(e.getMessage());
		}
	}
	public String getPath()
	{
		return uri.getPath();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((uri == null) ? 0 : uri.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final VosURI other = (VosURI) obj;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		return true;
	}
	
	public URI toURI()
	{
		return uri;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		
		return uri.toString();
	}
	
	public String getAuthority()
	{
		return uri.getAuthority();
	}
	public static VosURI createURI(String string) {
		try {
			VosURI retval = new VosURI(string);
			return retval;
		} catch (InvalidVOSUri e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
}


/*
 * $Log: VosURI.java,v $
 * Revision 1.2  2008/09/17 08:16:05  pah
 * result of merge of pah_community_1611 branch
 *
 * Revision 1.1.2.1  2008/05/17 20:55:13  pah
 * safety checkin before interop
 *
 * Revision 1.3  2007/06/21 15:35:05  pharriso
 * getting properties support to translate to webdav qname style
 *
 * Revision 1.2  2007/03/09 14:23:18  pharriso
 * createNode and pushToVospace are working with Slide NGAS store
 *
 * Revision 1.1  2007/01/11 15:48:51  pharriso
 * reorganised so that the project would build better with maven
 *
 * Revision 1.4  2006/11/10 07:17:52  pharriso
 * basic vospace working with NGAS backend
 *
 * Revision 1.3  2006/10/24 07:05:48  pharriso
 * basic in-memory vospace working with authentication
 *
 * Revision 1.2  2006/09/17 07:29:45  pharriso
 * minimal vospace(no security) working to mock in-memory back end
 *
 * Revision 1.1  2006/08/24 16:11:31  pharriso
 * basic in memory vospace implemented - except no data transfer yet
 *
 */
