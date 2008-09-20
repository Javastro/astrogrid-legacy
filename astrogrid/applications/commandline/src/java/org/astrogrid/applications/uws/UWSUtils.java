/*
 * $Id: UWSUtils.java,v 1.6 2008/09/20 15:54:01 pah Exp $
 * 
 * Created on 28 Aug 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.uws;

import java.net.MalformedURLException;
import java.security.cert.CertificateException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.astrogrid.security.HttpsServiceSecurityGuard;
import org.astrogrid.security.SecurityGuard;

/**
 * Useful utilities for UWS.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 16 Sep 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class UWSUtils {
    private UWSUtils() {
	// stop instantiation
    }

    /**
     * setup the response for a 303 redirect relative to the UWS root. This is only necessary because the
     * {@link HttpServletResponse#sendRedirect(String)} sends a 302 response
     * unfortunately...
     * 
     * @param request
     * @param response
     * @param jobid
     * @throws MalformedURLException
     */
    static public void redirect(HttpServletRequest request,
	    HttpServletResponse response, String jobid)
	    throws MalformedURLException {
	StringBuffer redirectURL = new StringBuffer(request.getContextPath());
	redirectURL.append(request.getServletPath());
	redirectURL.append("/jobs");
	if (jobid != null) {
	    redirectURL.append("/");
	    redirectURL.append(jobid);
	}
	java.net.URL rURL = new java.net.URL("http", request.getServerName(),
		request.getServerPort(), redirectURL.toString());
	response.setStatus(HttpServletResponse.SC_SEE_OTHER);
	response.setHeader("Location", rURL.toString());

    }

    public static SecurityGuard createSecurityGuard(HttpServletRequest request) throws CertificateException {
	
	HttpsServiceSecurityGuard retval = new HttpsServiceSecurityGuard();
	retval.loadHttpsAuthentication(request);
	return retval ;

    }
}

/*
 * $Log: UWSUtils.java,v $
 * Revision 1.6  2008/09/20 15:54:01  pah
 * clean up jobs redirect to not have trailing slash
 *
 * Revision 1.5  2008/09/18 08:46:45  pah
 * improved javadoc
 *
 * Revision 1.4  2008/09/15 17:19:05  pah
 * get securityguard into UWS chain
 *
 * Revision 1.3  2008/09/04 21:20:02  pah
 * ASSIGNED - bug 2825: support VOSpace
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2825
 * Added the basic implementation to support VOSpace  - however essentially untested on real deployement - also UWS security will not be functional
 *
 * Revision 1.2  2008/09/03 14:18:34  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/08/29 07:40:40  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 */
