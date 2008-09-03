/*
 * $Id: UWSUtils.java,v 1.2 2008/09/03 14:18:34 pah Exp $
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	redirectURL.append("/jobs/");
	if (jobid != null) {
	    redirectURL.append(jobid);
	}
	java.net.URL rURL = new java.net.URL("http", request.getServerName(),
		request.getServerPort(), redirectURL.toString());
	response.setStatus(HttpServletResponse.SC_SEE_OTHER);
	response.setHeader("Location", rURL.toString());

    }
}

/*
 * $Log: UWSUtils.java,v $
 * Revision 1.2  2008/09/03 14:18:34  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/08/29 07:40:40  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 */
