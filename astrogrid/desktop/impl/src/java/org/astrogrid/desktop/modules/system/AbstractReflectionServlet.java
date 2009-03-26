/*$Id: AbstractReflectionServlet.java,v 1.10 2009/03/26 18:04:11 nw Exp $
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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.Principal;
import java.util.StringTokenizer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.builtin.ComponentDescriptor;
import org.astrogrid.acr.builtin.MethodDescriptor;
import org.astrogrid.acr.builtin.ModuleDescriptor;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.framework.ACRInternal;
import org.astrogrid.desktop.framework.Module;
import org.astrogrid.desktop.framework.SessionManagerInternal;

/** Abstract servlet class for exposing services.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 31-Jan-2005
  */
public abstract class AbstractReflectionServlet extends HttpServlet {

    /** retreive services from the servlet context */
    @Override
    public void init(ServletConfig conf) throws ServletException {
        super.init(conf);
        reg = (ACRInternal)conf.getServletContext().getAttribute(WebServer.ACR_CONTEXT_KEY);
        session = (SessionManagerInternal)conf.getServletContext().getAttribute("session-manager");        
    }

    protected ACRInternal reg;
    protected SessionManagerInternal session;

    /** parse the request path, calling a different abstract method to process each level 
     * @throws IOException */
    protected void navigate(HttpServletRequest request,
            HttpServletResponse response) throws IOException  {
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
            // put this thread into the correct session, if any
            Principal sess = session.findSessionForKey(StringUtils.substringBetween(request.getRequestURI(),"/","/"));
            session.adoptSession(sess);            
            callMethod(methodD,resultType,component,request,response);
       } catch (ACRException t) {
            reportError("Failed to access component",t, request, response);
        } catch (RuntimeException t) {
            reportError(t,request,response);
        } catch(IOException e) {
        	reportError(e,request,response);
        } catch (ServletException e) {
        	reportError(e,request,response);
        } finally {
        	session.clearSession();
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

    /** process a request to a service */
    protected abstract void processComponent(ModuleDescriptor m,ComponentDescriptor cd, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

    /** process a request to a method in a service */
    protected abstract void processMethod(ModuleDescriptor m, ComponentDescriptor cd,MethodDescriptor md,
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

    /** process an invokation of a method in a service
     * 
     * @param resultType format to return result in (html/xml/plain)
     * @param md name of method to invoke
     * @param service servie the method belongs to.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    
    protected abstract void callMethod(MethodDescriptor md, String resultType, 
            Object service, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException;

    /** handle reporting an error  - may be extended.
     * @throws IOException */
    protected void reportError(Throwable t, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        response.sendError(500,fmt(t));
    }
    
    protected void reportError(String msg, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        response.sendError(500,msg);
    }
    
    protected void reportError(String msg, Throwable t,HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        response.sendError(500,msg + "\n" + fmt(t));
    }
    
    
    private String fmt(Throwable t) {
    	StringWriter sw = new StringWriter();
    	PrintWriter pw = new PrintWriter(sw);
    	t.printStackTrace(pw);
    	return sw.toString();
    }
    

    @Override
    protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
        navigate(arg0,arg1);
    }

    @Override
    protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
        navigate(arg0,arg1);
    }

}

/*
 * $Log: AbstractReflectionServlet.java,v $
 * Revision 1.10  2009/03/26 18:04:11  nw
 * source code improvements - cleaned imports, @override, etc.
 *
 * Revision 1.9  2007/06/18 17:00:13  nw
 * javadoc fixes.
 *
 * Revision 1.8  2007/03/22 19:03:48  nw
 * added support for sessions and multi-user ar.
 *
 * Revision 1.7  2007/01/29 11:11:36  nw
 * updated contact details.
 *
 * Revision 1.6  2006/06/27 19:18:32  nw
 * adjusted todo tags.
 *
 * Revision 1.5  2006/06/15 09:50:01  nw
 * fixed so that exceptions are reported to user.
 *
 * Revision 1.4  2006/06/02 00:16:15  nw
 * Moved Module, Component and Method-Descriptors from implementation code into interface. Then added methods to ApiHelp that provide access to these beans.
 *
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