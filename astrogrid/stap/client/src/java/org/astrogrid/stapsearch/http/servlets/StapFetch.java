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
 *  $Id: StapFetch.java,v 1.1 2009/06/20 14:48:21 pah Exp $
 */

package org.astrogrid.stapsearch.http.servlets;

import java.io.File;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.stapsearch.IStapFetch;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.io.PrintWriter;
import java.io.OutputStream;

import java.io.IOException;
import java.util.TimeZone;
import org.astrogrid.config.Config;

/**
 * Helper servlet for initializing the log4j framework in a webcontainer.
 */
public class StapFetch extends HttpServlet {    
    
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
        
        System.setProperty("http.agent","astrogrid-stap-" + req.getServerName());
        res.setContentType("application/octet-stream");
        String service = req.getParameter("service");
        
            if(service == null || service.trim().length() == 0) {
                System.out.println("Service name not part of the request exiting now, later default it");
                return;
            }
            
            String className = conf.getString("stapsearch.service." + service);
            System.out.println("classname found = " + className );            
            try {
                //PrintWriter output = res.getWriter();
                OutputStream output = res.getOutputStream();
                try {
                    Class cl = Class.forName(className);
                    IStapFetch ss = (IStapFetch)cl.newInstance();
                    ss.fetch(req.getParameterMap(), output);
                }catch(ClassNotFoundException cfe) {
                    output.write(("Cannot Find class to implement service = " + service).getBytes());
                }catch(InstantiationException ie) {
                    output.write(("Cannot instantiate the service = " + service).getBytes());
                }catch(IllegalAccessException iae) {
                    output.write(("Illegal access to instantiate service = "  + service).getBytes());
                }
                output.close();
            }catch(IOException ioe) {
                ioe.printStackTrace();
            }
    }
}