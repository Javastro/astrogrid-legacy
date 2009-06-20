/*
 *  eXist Open Source Native XML Database
 *  Copyright (C) 2001-03 Wolfgang M. Meier
 *  wolfgang@exist-db.org
 *  http://exist.sourceforge.net
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *  $Id: StapSearch.java,v 1.1 2009/06/20 14:48:21 pah Exp $
 */

package org.astrogrid.stapsearch.http.servlets;

import java.io.File;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.stapsearch.IStapSearch;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.TimeZone;
import org.astrogrid.config.Config;

/**
 * Helper servlet for initializing the log4j framework in a webcontainer.
 */
public class StapSearch extends HttpServlet {

    /**
     * conf - Config variable to access the configuration for the server
     * normally jndi to a config file.
     * 
     * @see org.astrogrid.config.Config
     */
    public static Config conf = null;

    /**
     * Static to be used on the initiatian of this class for the config
     */
    static {
	if (conf == null) {
	    conf = org.astrogrid.config.SimpleConfig.getSingleton();
	}
    }

    /**
     * Method: destroy Description: destroy method for the servlet conatiner to
     * call when killing the servlet. Cleans up any timer information making
     * sure they are cancelled. Otherwise the timer class may not let the
     * servlet container shutdown till ti runs.
     * 
     */
    public void destroy() {
	super.destroy();
    }

    /**
     * Empty method.
     * 
     * @param req
     *                HTTP Request object
     * @param res
     *                HTTP Response object
     * @throws throws
     *                 ServletException, IOException
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res)
	    throws ServletException, IOException {

	System.setProperty("http.agent", "astrogrid-stap-"
		+ req.getServerName());
	res.setContentType("text/xml;charset=UTF-8");
	SimpleDateFormat dateFormat = new SimpleDateFormat(
		"yyyy-MM-dd'T'HH:mm:ss");
	dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));

	try {
	    Date startTime = dateFormat.parse(req.getParameter("START"));
	    Date endTime = dateFormat.parse(req.getParameter("END"));
	    Calendar startTimeCal = Calendar.getInstance();
	    Calendar endTimeCal = Calendar.getInstance();
	    startTimeCal.setTime(startTime);
	    endTimeCal.setTime(endTime);

	    String service = req.getParameter("service");
	    if (service == null || service.trim().length() == 0) {
	        System.out
	    	    .println("Service name not part of the request exiting now, later default it");
	        return;
	    }

	    String className = conf.getString("stapsearch.service." + service);
	    System.out.println("classname found = " + className);

	    PrintWriter output = res.getWriter();

	    Class cl = Class.forName(className);
	    IStapSearch ss = (IStapSearch) cl.newInstance();
	    ss.execute(startTimeCal, endTimeCal, req.getParameterMap(), output);
	    output.close();
	} catch (Exception e) {
	    throw new ServletException("problem with search", e);
	}

    }
}
