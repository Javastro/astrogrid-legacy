/*$Id: HtmlServlet.java,v 1.1 2005/02/21 11:25:07 nw Exp $
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

import org.astrogrid.desktop.service.Configuration;
import org.astrogrid.desktop.service.MetadataHelper;
import org.astrogrid.desktop.service.MethodDoc;
import org.astrogrid.desktop.service.ParamDoc;
import org.astrogrid.desktop.service.ReturnDoc;
import org.astrogrid.desktop.service.ServiceDoc;

import org.apache.commons.beanutils.MethodUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 31-Jan-2005
 *
 */
public class HtmlServlet extends AbstractReflectionServlet {

    /**
     * @see org.astrogrid.desktop.protocol.http.AbstractReflectionServlet#processRoot(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void processRoot(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        header(out);
        out.println("<h1>Services</h1>");
        for (Iterator ms = services.getServiceDocs().iterator(); ms.hasNext(); ) {
            out.println(ms.next().toString());
        }
        footer(out);
    }

    /**
     * @see org.astrogrid.desktop.protocol.http.AbstractReflectionServlet#processService(java.lang.Object, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void processService(Object service, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        header(out);
        ServiceDoc meta = MetadataHelper.getServiceDoc(service);
        out.println("<p><a href='../.'>up</a></p>");
        out.println("<h1>"+ meta.getName() + "</h1>");
        out.println("<p>" + meta.getDescription() + "</p>");
        Collection allMethods = Arrays.asList(service.getClass().getMethods());
        for (Iterator ms = allMethods.iterator(); ms.hasNext(); ) {
            Method m = (Method)ms.next();
            if (MetadataHelper.hasMethodDoc(m)) {
                out.println(MetadataHelper.getMethodDoc(m));
            }
        }
        footer(out);
    }

    /**
     * @see org.astrogrid.desktop.protocol.http.AbstractReflectionServlet#processMethod(java.lang.String, java.lang.Object, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void processMethod(String methodName, Object service, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Method m = MetadataHelper.getMethodByName(service.getClass(),methodName);
        ServiceDoc serviceMeta = MetadataHelper.getServiceDoc(service);        
        MethodDoc meta = MetadataHelper.getMethodDoc(m);
        List paramDocs = MetadataHelper.getParamDocs(m);  
        ReturnDoc returnDoc = MetadataHelper.getReturnDoc(m);

        PrintWriter out = response.getWriter();
        header(out);
        out.println("<p><a href='../.'>up</a></p>");        
        out.println("<h1>"+ serviceMeta.getName() + "." +  meta.getName() + "</h1>");
        out.println("<p>" + meta.getDescription() + "</p>");     
        for (Iterator params = paramDocs.iterator(); params.hasNext(); ) {
            out.println(params.next());
            out.println("<br/>");
        }        
        out.println(returnDoc);

        
        out.println("<hr/><h2>Call HTML endpoint</h2><form name='call' method='POST' action='./html'><table>");
        for (Iterator params = paramDocs.iterator(); params.hasNext(); ) {
            out.println("<tr><td>");
            ParamDoc p  = (ParamDoc)params.next();
            out.println(p.getName());
            out.println("</td><td>");
            out.println("<input type='text' name='" +p.getName() + "'>");
            out.println("</td></tr>");
        }
        out.println("</table><input type='submit' value='Call'></form>");
        
        out.println("<hr/><h2>Endpoints</h2><table>");
        out.println("<tr><th>Endpoint</th><th>POST?</th><th>GET?</th></td>");
        out.println("<tr><td><a href='./html'>html</a></td><td>Y</td><td>Y</td></tr>");
        out.println("<tr><td><a href='./plain'>plain</a></td><td>Y</td><td>Y</td></tr>");
        out.println("<tr><td><a href='./xml'>xml</a></td><td>Y</td><td>Y</td></tr>");
        out.println("<tr><td><a href='../../xmlrpc'>xmlrpc</a></td><td>N</td><td>Y</td></tr>");        
        out.println("</table>");                
        footer(out);
    }
    
    /** list of supported result types */
    private static final List resultTypes = new ArrayList();
    static {
        resultTypes.add("html");
        resultTypes.add("plain");
        resultTypes.add("xml");
    }
    


  
    
    /**
     * @see org.astrogrid.desktop.protocol.http.AbstractReflectionServlet#callMethod(java.lang.String, java.lang.String, java.lang.Object, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void callMethod(String resultType, String methodName, Object service, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (resultType == null || ! resultTypes.contains(resultType.toLowerCase())) {
            throw new IllegalArgumentException("Unknown result type " + resultType);
        }
        if (! (service instanceof Configuration ||  service == community && methodName.equals("login"))) {
            try {
            community.loginIfNecessary();
            } catch (Exception e) {
                throw new ServletException("Failed to login",e);
            }
        }
        Method m = MetadataHelper.getMethodByName(service.getClass(),methodName);        
        List paramDocs = MetadataHelper.getParamDocs(m);
        ReturnDoc returnDoc = MetadataHelper.getReturnDoc(m);
        Class[] parameterTypes = m.getParameterTypes();
        Object[] args = new Object[paramDocs.size()];
        // pluck parameter values out of request, convert to correct types.
        for (int i = 0; i < paramDocs.size(); i++) {
            ParamDoc p = (ParamDoc)paramDocs.get(i);
            String strValue = request.getParameter(p.getName());
            args[i] = p.getConverter().convert(parameterTypes[i],strValue);
        }
        // call the method
        try {
        Object result = MethodUtils.invokeMethod(service,methodName,args);
        response.setContentType("text/" + resultType.trim().toLowerCase());
        PrintWriter out = response.getWriter();
        if (resultType.equalsIgnoreCase("html")) {
            out.println(returnDoc.getRts().getHtmlTransformer().transform(result));
        } else if (resultType.equalsIgnoreCase("plain")) {
            out.println( returnDoc.getRts().getPlainTransformer().transform(result));
        } else if (resultType.equalsIgnoreCase("xml")) {
            out.println(returnDoc.getRts().getXmlTransformer().transform(result));
        } else {
            throw new IllegalStateException("Really can't get here");
        }
        } catch (Exception e) {
            throw new ServletException("Could not call method " + methodName,e);
        }
    }    
    /**
     * @param out
     */
    private void header(PrintWriter out) {
        out.println("<html><body>");
    }
    
    /**
     * @param out
     */
    private void footer(PrintWriter out) {
        out.println("</body></html>");
        out.flush();
        out.close();

    }


}


/* 
$Log: HtmlServlet.java,v $
Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/