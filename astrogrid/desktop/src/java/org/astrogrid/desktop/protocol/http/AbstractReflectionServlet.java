/*$Id: AbstractReflectionServlet.java,v 1.1 2005/02/21 11:25:07 nw Exp $
 * Created on 31-Jan-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.protocol.http;

import org.astrogrid.desktop.DesktopServer;
import org.astrogrid.desktop.Main;
import org.astrogrid.desktop.service.Community;
import org.astrogrid.desktop.service.Services;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Abstract servlet class for working with services.
 * @author Noel Winstanley nw@jb.man.ac.uk 31-Jan-2005
 *  
 */
public abstract class AbstractReflectionServlet extends HttpServlet {

    /** retreive services from the servlet context */
    public void init(ServletConfig conf) throws ServletException {
        super.init(conf);
        services = (Services) conf.getServletContext().getAttribute(
                DesktopServer.SERVICES);
        community = (Community)services.getService("community");
    }

    protected Services services;
    protected Community community;

    /** parse the request path, calling a different abstract method to process each level */
    protected void navigate(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        // tokenize the request path..
        try {
            if (request.getPathInfo() == null || request.getPathInfo().trim().length() < 1) {
                processRoot(request, response);
                return;
            }
            StringTokenizer tok = new StringTokenizer(request.getPathInfo(), "/");
            if (!tok.hasMoreTokens()) {
                processRoot(request, response);
                return;
            }
            Object service = services.getService(tok.nextToken());
            if (service == null) {
                throw new IllegalArgumentException("Unknown service");
            }
            if (!tok.hasMoreTokens()) {
                processService(service, request, response);
                return;
            }
            String method = tok.nextToken();
            if (! tok.hasMoreTokens()) {
                processMethod(method, service, request, response);
                return;
            }
            String resultType = tok.nextToken();
            callMethod(resultType,method,service,request,response);
        } catch (Throwable t) {
            reportError(t, request, response);
        }
    }

    /** process a request to the root */
    protected abstract void processRoot(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException;

    /** process a request to a service */
    protected abstract void processService(Object service,
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

    /** process a request to a method in a service */
    protected abstract void processMethod(String methodName, Object service,
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

    /** process an invokation of a method in a service
     * 
     * @param resultType format to return result in (html/xml/plain)
     * @param methodName name of method to invoke
     * @param service servie the method belongs to.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    
    protected abstract void callMethod(String resultType, String methodName,
            Object service, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException;

    /** handle reporting an error  - may be extended.*/
    protected void reportError(Throwable t, HttpServletRequest request,
            HttpServletResponse response) throws ServletException {
        throw new ServletException(t);
    }

    protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
        navigate(arg0,arg1);
    }

    protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
        navigate(arg0,arg1);
    }

}

/*
 * $Log: AbstractReflectionServlet.java,v $
 * Revision 1.1  2005/02/21 11:25:07  nw
 * first add to cvs
 *
 */