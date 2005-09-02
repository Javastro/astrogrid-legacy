/*$Id: HtmlServlet.java,v 1.2 2005/09/02 14:03:34 nw Exp $
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

import org.astrogrid.acr.builtin.Module;
import org.astrogrid.desktop.framework.DefaultModule;
import org.astrogrid.desktop.framework.ReflectionHelper;
import org.astrogrid.desktop.framework.descriptors.ComponentDescriptor;
import org.astrogrid.desktop.framework.descriptors.Descriptor;
import org.astrogrid.desktop.framework.descriptors.MethodDescriptor;
import org.astrogrid.desktop.framework.descriptors.ModuleDescriptor;
import org.astrogrid.desktop.framework.descriptors.ValueDescriptor;
import org.astrogrid.desktop.modules.system.transformers.ResultTransformerSet;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.iterators.FilterIterator;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** servlet that exposes ACR functions over plain old http.
 * @author Noel Winstanley nw@jb.man.ac.uk 31-Jan-2005
 *
 */
public class HtmlServlet extends AbstractReflectionServlet {

    /**
     * @see org.astrogrid.desktop.modules.system.AbstractReflectionServlet#processRoot(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void processRoot(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        header(out);
        out.println("<h1>Modules</h1><dl>");
        for (Iterator ms = reg.moduleIterator(); ms.hasNext(); ) {
            DefaultModule m = (DefaultModule)ms.next();
            out.println("<dt>");
            out.println("<a href='./" + m.getDescriptor().getName() + "/'>");
            out.println(m.getDescriptor().getName());
            out.println("</a>");
            out.println("</dt><dd>");
            out.println(m.getDescriptor().getDescription());
            out.println("</dd>");
        }
        out.println("</dl>");
        footer(out);
    }
    
    /**
     * @throws IOException
     * @see org.astrogrid.desktop.modules.system.AbstractReflectionServlet#processModule(org.astrogrid.desktop.framework.Module, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void processModule(ModuleDescriptor md, HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        header(out);
        descriptorSummary(out, md);
        descriptorList(out, new FilterIterator(md.componentIterator(), new Predicate() {
            public boolean evaluate(Object arg0) {
                Descriptor d = (Descriptor)arg0;
                String value = d.getProperty("hidden.component");
                return value == null || ! (value.trim().equals("true"));
            }
        }));
        footer(out);                    
    }



    /**
     * @see org.astrogrid.desktop.modules.system.AbstractReflectionServlet#processComponent(Module, java.lang.Object, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void processComponent(ComponentDescriptor cd, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        header(out);
        descriptorSummary(out,cd);
        descriptorList(out,cd.methodIterator());
        footer(out);
    }

    /**
     * @see org.astrogrid.desktop.modules.system.AbstractReflectionServlet#processMethod(java.lang.String, java.lang.Object, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void processMethod(MethodDescriptor methodDescriptor, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        
        PrintWriter out = response.getWriter();
        header(out);
        out.println("<p><a href='../.'>up</a></p>");        
        out.println("<h1>" + methodDescriptor.getName() + "</h1>");
        out.println("<p>" + methodDescriptor.getDescription() + "</p><h2>Parameters</h2><dl>");     
        for (Iterator params = methodDescriptor.parameterIterator(); params.hasNext(); ) {
           ValueDescriptor v = (ValueDescriptor)params.next();
            out.println("<dt>");
            out.println(v.getName() );
            out.println("</dt><dd>");
            out.println(v.getDescription());
            out.println("</dd>");           
        }        
        out.println("</dl><h2>Return</h2>");
        ValueDescriptor v = methodDescriptor.getReturnValue();
        out.println("<dl><dt>");
        out.println(v.getName() );
        out.println("</dt><dd>");
        out.println(v.getDescription());
        out.println("</dd></dl>");    
                
        
        for (Iterator i = resultTypes.iterator(); i.hasNext(); ) {
            String method = i.next().toString();
            out.println("<hr/><h1>Call " + method +" endpoint</h1>");
            out.println("<form name='call' method='POST' action='./" + method+ "'><table>");
            for (Iterator params = methodDescriptor.parameterIterator(); params.hasNext(); ) {
                out.println("<tr><td>");
                ValueDescriptor p  = (ValueDescriptor)params.next();
                out.println(p.getName());
                out.println("</td><td>");
                out.println("<input type='text' name='" +p.getName() + "'>");
                out.println("</td></tr>");
            }
            out.println("</table><input type='submit' value='Call'></form>");
        }       
        footer(out);
    }
    
    /**
     * @param out
     * @param md
     */
    private void descriptorSummary(PrintWriter out, Descriptor md) {
        out.println("<p><a href='../.'>up</a></p>");
        out.println("<h1>" + md.getName() + "</h1>");
        out.println("<p>" + md.getDescription() + "</p>");
    }
    
    
    private void descriptorList(PrintWriter out, Iterator ds) {
        out.println("<dl>");
        while (ds.hasNext()) {
            Descriptor d = (Descriptor)ds.next();
            out.println("<dt>");
            out.println("<a href='./" + d.getName() + "/'>");
            out.println(d.getName());
            out.println("</a>");
            out.println("</dt><dd>");
            out.println(d.getDescription());
            out.println("</dd>");
        }
        out.println("</dl>");
    }
    
    /** list of supported result types */
    private static final List resultTypes = new ArrayList();
    static {
        resultTypes.add("html");
        resultTypes.add("plain");
        /** not supported for now
        resultTypes.add("xml");
        */
    }
    


  
    
    /**
     * @see org.astrogrid.desktop.modules.system.AbstractReflectionServlet#callMethod(java.lang.String, java.lang.String, java.lang.Object, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void callMethod(MethodDescriptor md,String resultType, Object component, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (resultType == null || ! resultTypes.contains(resultType.toLowerCase())) {
            throw new ServletException("Unknown result type " + resultType);
        }
        // call the method
        try {
        Method m =ReflectionHelper.getMethodByName(component.getClass(),md.getName()); 
        Class[] parameterTypes = m.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];
        // pluck parameter values out of request, convert to correct types.
        Iterator i = md.parameterIterator();
        for (int j = 0; j < parameterTypes.length &&  i.hasNext(); j++) {
            ValueDescriptor p = (ValueDescriptor)i.next();
            String strValue = request.getParameter(p.getName());
            Converter conv = XmlRpcServlet.getConverter(p); 
            args[j] = conv.convert(parameterTypes[j],strValue);
        }

        Object result = MethodUtils.invokeMethod(component,md.getName(),args);
        response.setContentType("text/" + resultType.trim().toLowerCase());
        PrintWriter out = response.getWriter();
        ResultTransformerSet rts = XmlRpcServlet.getTransformerSet(md.getReturnValue());
        if (resultType.equalsIgnoreCase("html")) {
            out.println(rts.getHtmlTransformer().transform(result));
        } else if (resultType.equalsIgnoreCase("plain")) {
            out.println( rts.getPlainTransformer().transform(result));
        } else if (resultType.equalsIgnoreCase("xml")) {
            out.println(rts.getXmlTransformer().transform(result));
        } else {
            throw new IllegalStateException("Really can't get here");
        }
        } catch (Exception e) {
            throw new ServletException("Could not call method " + md.getName(),e);
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