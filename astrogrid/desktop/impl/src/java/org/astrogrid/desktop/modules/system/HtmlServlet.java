/*$Id: HtmlServlet.java,v 1.15 2008/11/04 14:35:49 nw Exp $
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
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.astrogrid.acr.builtin.ComponentDescriptor;
import org.astrogrid.acr.builtin.Descriptor;
import org.astrogrid.acr.builtin.MethodDescriptor;
import org.astrogrid.acr.builtin.ModuleDescriptor;
import org.astrogrid.acr.builtin.ValueDescriptor;
import org.astrogrid.desktop.framework.ReflectionHelper;

/** Servlet that exposes AR functions over plain old HTTP.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 31-Jan-2005
 * 
 */
public class HtmlServlet extends AbstractReflectionServlet {
    public void init(final ServletConfig conf) throws ServletException {
        super.init(conf);
        final ServletContext servletContext = conf.getServletContext();
        conv = (Converter)servletContext.getAttribute("converter");
        //trans = (ResultTransformerSet)servletContext.getAttribute("transformer");
        plain = (Transformer)servletContext.getAttribute("plainResultTransformer");
        html = (Transformer)servletContext.getAttribute("htmlResultTransformer");
    }

    /**
     * @see org.astrogrid.desktop.modules.system.AbstractReflectionServlet#processRoot(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void processRoot(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final PrintWriter out = response.getWriter();
        header(out);
        out.println("<h1>Modules</h1><dl>");
        for (final Iterator ms = reg.getDescriptors().values().iterator(); ms.hasNext(); ) {
            final ModuleDescriptor m = (ModuleDescriptor)ms.next();
            out.println("<dt>");
            out.print("<a href='./");
            out.print(m.getName());
            out.print("/'>");
            out.println(m.getName());
            out.println("</a>");
            out.println("</dt><dd>");
            out.println(m.getDescription());
            out.println("</dd>");
        }
        out.println("</dl>");
        out.println("<h1><a href='./preferences'>Preferences</a></h1>");
        out.println("View and edit the configuration settings for this AR<p/>");
        out.println("<a href='./system/configuration/reset'>Reset configuration to factory settings</a>");
        out.println("<a href='./xmlrpc'><h1>XML-RPC interface</h1></a>");
        out.println("Endpoint for the XML-RPC interface to this AR");
        footer(out);
    }
    
    /**
     * @throws IOException
     */
    protected void processModule(final ModuleDescriptor md, final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final PrintWriter out = response.getWriter();
        header(out);
        out.println("<p><a href='../.'>up</a></p>");
        out.print("<h1>Module: ");
        out.print(md.getName());
        out.println("</h1>");
         formatDescription(out,md);
        descriptorList(out, md.componentIterator());
        footer(out);                    
    }




    protected void processComponent(final ModuleDescriptor md,final ComponentDescriptor cd, final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final PrintWriter out = response.getWriter();
        header(out);
        out.println("<p><a href='../.'>up</a></p>");
        out.print("<h1>Component: <a href='../.'>" );
        out.print(md.getName());
        out.print("</a>.");
        out.print(cd.getName());
        out.print("</h1>");
         formatDescription(out,cd);
        descriptorList(out,cd.methodIterator());
        footer(out);
    }


    protected void processMethod(final ModuleDescriptor md, final ComponentDescriptor cd,final MethodDescriptor methodDescriptor, final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {        
        final PrintWriter out = response.getWriter();
        header(out);
        out.println("<p><a href='../.'>up</a></p>");        
        out.print("<h1>Function: <a href='../../.'>");
        out.print(md.getName());
        out.print( "</a>.<a href='../.'>");
        out.print(cd.getName());
        out.print("</a>.");
        out.print( methodDescriptor.getName());
        out.print("()</h1>");
        formatDescription(out,methodDescriptor);
         out.println("<h2>Parameters</h2><dl>");    
        {
        final Iterator params = methodDescriptor.parameterIterator();
        if (!params.hasNext()) {
            out.println("<dt><i>none</i></dt><dd></dd>");
        } else {
            while  (params.hasNext()) {
                final ValueDescriptor v = (ValueDescriptor)params.next();
                out.println("<dt>");
                out.println(v.getName() );
                out.println("</dt><dd>");
                out.println(v.getDescription());
                out.println("<br /><i>Type:</i> ");
                out.println(v.getUitype());
                out.println("</dd>");           
            }        
        }
        }
        out.println("</dl><h2>Return</h2>");
        final ValueDescriptor v = methodDescriptor.getReturnValue();
        out.println("<dl><dt>");
       //never contains useful nformation: out.println(v.getName() );
        out.println("</dt><dd>");
        out.println(v.getDescription());       
        out.println("<br /><i>Type:</i> ");
        out.println(v.getUitype());       
        out.println("</dd></dl>");    
                
        out.println("<hr/><h1>Call Function</h1>");
        for (final Iterator i = resultTypes.iterator(); i.hasNext(); ) {
            final String method = i.next().toString();
            out.println("<hr/><h2>");
            out.print(method);
            out.print(" result</h2>");
            out.println("<form name='call' method='POST' action='./" + method+ "'><table>");
            for (final Iterator params = methodDescriptor.parameterIterator(); params.hasNext(); ) {
                out.println("<tr><td>");
                final ValueDescriptor p  = (ValueDescriptor)params.next();
                out.println(p.getName());
                out.println("</td><td>");
                out.println("<input type='text' name='" +p.getName() + "'>");
                out.println("</td></tr>");
            }
            out.println("</table><input type='submit' value='Call'></form>");
        }       
        
        out.println("<hr/><h1>Invocation URL</h1>");
        out.print("<i>base-url</i>/");
        out.print(md.getName());
        out.print("/");
        out.print(cd.getName());
        out.print("/");
        out.print(methodDescriptor.getName());
        out.println("/<i>[");
        out.println(StringUtils.join(resultTypes.iterator(),'|'));
        out.print("]</i>?");
        for (final Iterator params = methodDescriptor.parameterIterator(); params.hasNext();) {
            final ValueDescriptor p = (ValueDescriptor)params.next();
            out.print(p.getName());
            out.print("=<i>val</i>");
            if (params.hasNext()) {
                out.print("&amp;");
            }
        }
        
        
        
        footer(out);
    }
    
    private void formatDescription(final PrintWriter out, final Descriptor d) {
        out.println("<p>");
        out.println(StringUtils.replace(d.getDescription(),"\n","<br />"));
        out.println("</p>");
    }
    
    
    private void descriptorList(final PrintWriter out, final Iterator ds) {
        out.println("<dl>");
        while (ds.hasNext()) {
            final Descriptor d = (Descriptor)ds.next();
            out.println("<dt>");
            out.println("<a href='./" + d.getName() + "/'>");
            out.println(d.getName());
            out.println("</a>");
            out.println("</dt><dd>");
            out.println(StringUtils.substringBefore(d.getDescription(),"."));
            out.println("</dd>");
        }
        out.println("</dl>");
    }

    protected Converter conv;
    protected Transformer html;
    protected Transformer plain;   
    
    /** list of supported result types */
    private static final Set resultTypes = new HashSet();
    static {
        resultTypes.add("html");
        resultTypes.add("plain");
    }
    


  
    
    //@todo merge ApiHelp.callFunction,  XMLRPCServlet.execute() and HtmlServlet.callMethod

    protected void callMethod(final MethodDescriptor md,final String resultType, final Object component, final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        if (resultType == null || ! resultTypes.contains(resultType.toLowerCase())) {
            throw new ServletException("Unknown result type " + resultType);
        }
        // call the method
        try {
        final Method m =ReflectionHelper.getMethodByName(component.getClass(),md.getName()); 
        final Class[] parameterTypes = m.getParameterTypes();
        final Object[] args = new Object[parameterTypes.length];
        // pluck parameter values out of request, convert to correct types.
        final Iterator i = md.parameterIterator();
        for (int j = 0; j < parameterTypes.length &&  i.hasNext(); j++) {
            final ValueDescriptor p = (ValueDescriptor)i.next();
            final String strValue = request.getParameter(p.getName());
            args[j] = conv.convert(parameterTypes[j],strValue);
        }

        final Object result = MethodUtils.invokeMethod(component,md.getName(),args);
        response.setContentType("text/" + resultType.trim().toLowerCase());
        final PrintWriter out = response.getWriter();
        // fix for bz #1647
        if (m.getReturnType().equals(Void.TYPE)) {
        	out.println("OK");
        }  else if (result == null) {
            out.println("null");
        } else if (resultType.equalsIgnoreCase("html")) {
            out.println(html.transform(result));
        } else if (resultType.equalsIgnoreCase("plain")) {
            out.println( plain.transform(result));
        } else {
        	// fall back to plain.
        	out.println(plain.transform(result));
        }
        } catch (final Exception e) {
        	reportError("Exception thrown when calling method " + md.getName(),e,request,response);
        }
    }    
    /**
     * @param out
     */
    private void header(final PrintWriter out) {
        out.println("<html><body>");
    }
    
    /**
     * @param out
     */
    private void footer(final PrintWriter out) {
        out.println("</body></html>");
        out.flush();
        out.close();

    }




}


/* 
$Log: HtmlServlet.java,v $
Revision 1.15  2008/11/04 14:35:49  nw
javadoc polishing

Revision 1.14  2008/04/25 08:58:04  nw
tested

Revision 1.13  2008/03/10 18:52:19  nw
used sets in place of lists.

Revision 1.12  2007/10/09 14:56:19  nw
improved error handling.

Revision 1.11  2007/09/21 16:35:13  nw
improved error reporting,
various code-review tweaks.

Revision 1.10  2007/06/18 16:58:47  nw
Added link to 'Configuration.reset()'

Revision 1.9  2007/03/22 19:03:48  nw
added support for sessions and multi-user ar.

Revision 1.8  2007/01/29 11:11:36  nw
updated contact details.

Revision 1.7  2007/01/10 14:53:57  nw
added preferences editor

Revision 1.6  2006/08/31 21:32:49  nw
doc fixes.

Revision 1.5  2006/06/15 09:50:36  nw
fixed so that exceptions are reported to user.fixed bz #1647

Revision 1.4  2006/06/02 00:16:15  nw
Moved Module, Component and Method-Descriptors from implementation code into interface. Then added methods to ApiHelp that provide access to these beans.

Revision 1.3  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.2.60.4  2006/04/18 18:49:03  nw
version to merge back into head.

Revision 1.2.60.3  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.2.60.2  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.2.60.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development

Revision 1.2  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.4  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.3  2005/04/27 13:42:41  clq2
1082

Revision 1.2.2.1  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2  2005/04/13 12:59:12  nw
checkin from branch desktop-nww-998

Revision 1.1.2.3  2005/04/05 11:41:48  nw
added 'hidden.modules',
allowed more methods to be called from ui.

Revision 1.1.2.2  2005/04/04 08:49:27  nw
working job monitor, tied into pw launcher.

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.

Revision 1.2  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/