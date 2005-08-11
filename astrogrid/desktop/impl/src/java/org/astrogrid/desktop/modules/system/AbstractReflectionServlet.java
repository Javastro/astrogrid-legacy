/*$Id: AbstractReflectionServlet.java,v 1.1 2005/08/11 10:15:00 nw Exp $
 * Created on 31-Jan-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.system;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.builtin.Module;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.framework.DefaultModule;
import org.astrogrid.desktop.framework.MutableACR;
import org.astrogrid.desktop.framework.descriptors.ComponentDescriptor;
import org.astrogrid.desktop.framework.descriptors.MethodDescriptor;
import org.astrogrid.desktop.framework.descriptors.ModuleDescriptor;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Abstract servlet class for exposing services.
 * @author Noel Winstanley nw@jb.man.ac.uk 31-Jan-2005
 *  
 */
public abstract class AbstractReflectionServlet extends HttpServlet {

    /** retreive services from the servlet context */
    public void init(ServletConfig conf) throws ServletException {
        super.init(conf);
        reg = (MutableACR)conf.getServletContext().getAttribute(WebServer.ACR_CONTEXT_KEY);
    }

    protected MutableACR reg;

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
            DefaultModule m = (DefaultModule)reg.getModule(tok.nextToken());
            ModuleDescriptor md = m.getDescriptor();
            if (m == null) {
                throw new ServletException("Unknown module");
            }
            if (!tok.hasMoreTokens()) {
                processModule(md,request,response);
                return;
            }                       
            ComponentDescriptor cd = md.getComponent(tok.nextToken());
            if (cd == null) {
                throw new ServletException("Unknown component");
            }
            if (!tok.hasMoreTokens()) {
                processComponent(cd, request, response);
                return;
            }
            MethodDescriptor methodD = cd.getMethod(tok.nextToken());
            if (methodD == null) {
                throw new ServletException("unknown method");
            }
            if (! tok.hasMoreTokens()) {
                processMethod(methodD, request, response);
                return;
            }
            String resultType = tok.nextToken();
            Object component = m.getComponent(cd.getName());
            if (component == null) {
                throw new ServletException("component not found");
            }
            callMethod(methodD,resultType,component,request,response);
        } catch (Throwable t) {
            reportError(t, request, response);
        }
    }

    /**
     * @param m
     * @param request
     * @param response
     * @throws IOException
     */
    protected abstract void processModule(ModuleDescriptor m, HttpServletRequest request, HttpServletResponse response) throws IOException;    

    /** process a request to the root */
    protected abstract void processRoot(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException;

    /** process a request to a service 
     * @param module */
    protected abstract void processComponent(ComponentDescriptor cd, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

    /** process a request to a method in a service */
    protected abstract void processMethod(MethodDescriptor cd,
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
    
    protected abstract void callMethod(MethodDescriptor md, String resultType, 
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
 * Revision 1.1  2005/08/11 10:15:00  nw
 * finished split
 *
 * Revision 1.4  2005/08/05 11:46:55  nw
 * reimplemented acr interfaces, added system tests.
 *
 * Revision 1.3  2005/04/27 13:42:41  clq2
 * 1082
 *
 * Revision 1.2.2.1  2005/04/25 11:18:51  nw
 * split component interfaces into separate package hierarchy
 * - improved documentation
 *
 * Revision 1.2  2005/04/13 12:59:12  nw
 * checkin from branch desktop-nww-998
 *
 * Revision 1.1.2.1  2005/03/18 12:09:31  nw
 * got framework, builtin and system levels working.
 *
 * Revision 1.2  2005/02/22 01:10:31  nw
 * enough of a prototype here to do a show-n-tell on.
 *
 * Revision 1.1  2005/02/21 11:25:07  nw
 * first add to cvs
 *
 */