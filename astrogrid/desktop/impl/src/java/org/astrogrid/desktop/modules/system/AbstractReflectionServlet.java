/*$Id: AbstractReflectionServlet.java,v 1.3 2006/04/18 23:25:44 nw Exp $
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

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.framework.ACRInternal;
import org.astrogrid.desktop.framework.Module;
import org.astrogrid.desktop.framework.descriptors.ComponentDescriptor;
import org.astrogrid.desktop.framework.descriptors.MethodDescriptor;
import org.astrogrid.desktop.framework.descriptors.ModuleDescriptor;

/** Abstract servlet class for exposing services.
 * @author Noel Winstanley nw@jb.man.ac.uk 31-Jan-2005
 *  @todo work out how to add a handler for servlet exceptions that displays pretty error messages.
 */
public abstract class AbstractReflectionServlet extends HttpServlet {

    /** retreive services from the servlet context */
    public void init(ServletConfig conf) throws ServletException {
        super.init(conf);
        reg = (ACRInternal)conf.getServletContext().getAttribute(WebServer.ACR_CONTEXT_KEY);
    }

    protected ACRInternal reg;

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
            final String requestedModule = tok.nextToken();
            Module m = reg.getModule(requestedModule);
            if (m == null) {
                reportError("Unknown module '" + requestedModule + "'" ,request,response);
            }
            ModuleDescriptor md = m.getDescriptor();
            if (!tok.hasMoreTokens()) {
                processModule(md,request,response);
                return;
            }                       
            final String requestedComponent = tok.nextToken();
            ComponentDescriptor cd = md.getComponent(requestedComponent);
            if (cd == null) {
                reportError("Unknown component '"  + requestedComponent + "'",request,response);
            }
            if (!tok.hasMoreTokens()) {
                processComponent(md,cd, request, response);
                return;
            }
            final String requestedMethod = tok.nextToken();
            MethodDescriptor methodD = cd.getMethod(requestedMethod);
            if (methodD == null) {
                reportError("unknown method '" + requestedMethod + "'",request,response);
            }
            if (! tok.hasMoreTokens()) {
                processMethod(md,cd,methodD, request, response);
                return;
            }
            String resultType = tok.nextToken();           
            Object component = m.getComponent(requestedComponent);
            if (component == null) {
                reportError("component not found '" + requestedComponent + "'",request,response);
            }
            callMethod(methodD,resultType,component,request,response);
       } catch (ACRException t) {
            reportError("Failed to access component",t, request, response);
        } catch (RuntimeException t) {
            reportError(t,request,response);
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
    protected abstract void processComponent(ModuleDescriptor m,ComponentDescriptor cd, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

    /** process a request to a method in a service */
    protected abstract void processMethod(ModuleDescriptor m, ComponentDescriptor cd,MethodDescriptor md,
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
    
    protected void reportError(String msg, HttpServletRequest request,
            HttpServletResponse response) throws ServletException {
        throw new ServletException(msg);
    }
    
    protected void reportError(String msg, Throwable t,HttpServletRequest request,
            HttpServletResponse response) throws ServletException {
        throw new ServletException(msg,t);
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
 * Revision 1.3  2006/04/18 23:25:44  nw
 * merged asr development.
 *
 * Revision 1.2.66.3  2006/04/14 02:45:01  nw
 * finished code.extruded plastic hub.
 *
 * Revision 1.2.66.2  2006/04/04 10:31:26  nw
 * preparing to move to mac.
 *
 * Revision 1.2.66.1  2006/03/22 18:01:30  nw
 * merges from head, and snapshot of development
 *
 * Revision 1.2  2005/08/25 16:59:58  nw
 * 1.1-beta-3
 *
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