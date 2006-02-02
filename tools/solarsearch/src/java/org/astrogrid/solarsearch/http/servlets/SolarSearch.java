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
 *  $Id: SolarSearch.java,v 1.1 2006/02/02 12:01:20 KevinBenson Exp $
 */

package org.astrogrid.solarsearch.http.servlets;

import java.io.File;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.solarsearch.ISolarSearch;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.TimeZone;
import org.astrogrid.config.Config;

/**
 * Helper servlet for initializing the log4j framework in a webcontainer.
 */
public class SolarSearch extends HttpServlet {    
    
    /**
     * conf - Config variable to access the configuration for the server normally
     * jndi to a config file.
     * @see org.astrogrid.config.Config
     */   
    public static Config conf = null;
    
    /**
     * Static to be used on the initiatian of this class for the config
     */   
    static {
       if(conf == null) {
          conf = org.astrogrid.config.SimpleConfig.getSingleton();
       }
    }    
    
    
    /**
     * Method: destroy
     * Description: destroy method for the servlet conatiner to call when killing the servlet. Cleans up any
     * timer information making sure they are cancelled.  Otherwise the timer class may not let the servlet container
     * shutdown till ti runs.
     *
     */
    public void destroy() {
        super.destroy();
    }
    
    /**
     *  Empty method.
     *
     * @param req HTTP Request object
     * @param res HTTP Response object
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res) {
        
           

        SimpleDateFormat dateFormat = new SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));

        try {
            Date startTime = dateFormat.parse(req.getParameter("startTime"));
            Date endTime = dateFormat.parse(req.getParameter("endTime"));
            Calendar startTimeCal = Calendar.getInstance();
            Calendar endTimeCal = Calendar.getInstance();
            startTimeCal.setTime(startTime);
            endTimeCal.setTime(endTime);
            
            String service = req.getParameter("service");
            if(service == null || service.trim().length() == 0) {
                System.out.println("Service name not part of the request exiting now, later default it");
                return;
            }
            String className = conf.getString("solarsearch.service." + service);
            try {
                PrintWriter output = res.getWriter();        
                try {
                    Class cl = Class.forName(className);
                    ISolarSearch ss = (ISolarSearch)cl.newInstance();
                    ss.execute(startTimeCal, endTimeCal, req.getParameterMap(), output);
                }catch(ClassNotFoundException cfe) {
                    output.print("Cannot Find class to implement service = " + service);
                }catch(InstantiationException ie) {
                    output.print("Cannot instantiate the service = " + service);
                }catch(IllegalAccessException iae) {
                    output.print("Illegal access to instantiate service = "  + service);
                }
                output.flush();
                output.close();
            }catch(IOException ioe) {
                ioe.printStackTrace();
            }
        }catch(java.text.ParseException pe){
            pe.printStackTrace();
        }
    }
    
}
