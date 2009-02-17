/*$Id: HtmlServlet.java,v 1.17 2009/02/17 13:45:18 nw Exp $
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
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
        header(null,out);
        out.println("<h1>Astro Runtime API</h1>");
        out.println("See <a href='http://www.astrogrid.org/wiki/Help/AstroRuntime'>www.astrogrid.org</a> for documentation.");
        out.println("<h2>Modules</h2>");
        out.println("Follow the links below to explore what's available, and call AR API functions.");
        out.println("<dl>");
        for (final Iterator ms = reg.getDescriptors().values().iterator(); ms.hasNext(); ) {
            final ModuleDescriptor m = (ModuleDescriptor)ms.next();
            if (m.isExcluded()) {
                continue;
            }
            out.println("<dt>");
            out.print("<a href='./");
            out.print(m.getName());
            out.print("/'>");
            out.println(m.getName());
            out.println("</a>");
            out.println("</dt><dd>");
            final String title = StringUtils.capitalize(m.getDescription());
            out.print(title);
            if (! title.endsWith(".")) {
                out.print(".");
            }
            out.println("</dd>");
        }
        out.println("</dl>");
        out.println("<h2>Configuration Editor</h2>");
        out.println("<a id='Preferences' href='./preferences'>View and edit the configuration settings</a> for this Astro Runtime<p/>");
        out.println("or <a href='./system/configuration/reset'>reset the configuration to factory settings</a>.");
        out.println("<h2>XML-RPC interface</h2>");
        out.println("<a href='./xmlrpc'>Endpoint for the XML-RPC</a> interface to this Astro Runtime.");
        footer(out);
    }
    
    /**
     * @throws IOException
     */
    protected void processModule(final ModuleDescriptor md, final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final PrintWriter out = response.getWriter();
        header(md.getName() + " Module",out);
        out.println("<p><a href='../.'>up</a></p>");
        out.print("<h1>");
        out.print(md.getName());
        out.print(" Module");
        out.println("</h1>");
         formatDescription(out,md);
         out.print("<h2>Components in ");
         out.print(md.getName());
         out.println("</h2>");
        descriptorList(out, md.componentIterator());
        footer(out);                    
    }




    protected void processComponent(final ModuleDescriptor md,final ComponentDescriptor cd, final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final PrintWriter out = response.getWriter();
        header(md.getName() + '.' + cd.getName() + " Component",out);
        out.println("<p><a href='../.'>up</a></p>");
        out.print("<h1><a href='../.'>" );
        out.print(md.getName());
        out.print("</a>.");
        out.print(cd.getName());
        out.print(" Component");
        out.print("</h1>");
         formatDescription(out,cd);
         out.print("<h2>Functions in ");
         out.print(cd.getName());
         out.println("</h2>");
        descriptorList(out,cd.methodIterator());
        footer(out);
    }


    protected void processMethod(final ModuleDescriptor md, final ComponentDescriptor cd,final MethodDescriptor methodDescriptor, final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {        
        final PrintWriter out = response.getWriter();
        header(md.getName() + '.' + cd.getName() + "." + methodDescriptor.getName() + "()",out);
        out.println("<p><a href='../.'>up</a></p>");    
        
        // fn prototype
        out.print("<h2><tt>");
        final ValueDescriptor returnValue = methodDescriptor.getReturnValue();
        out.print(returnValue.getUitype());
        out.print(" <a href='../../.'>");
        out.print(md.getName());
        out.print( "</a>.<a href='../.'>");
        out.print(cd.getName());
        out.print("</a>.");
        out.print( methodDescriptor.getName());
        out.print("(");
        boolean parameterDescriptionSeen = false;
        final ValueDescriptor[] parameters = methodDescriptor.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            if (i > 0) {
                out.print(", ");
            }
            final ValueDescriptor p = parameters[i];
            out.print(p.getUitype());
            out.print("&nbsp;");
            out.print(p.getName());
            parameterDescriptionSeen = parameterDescriptionSeen 
            ||  StringUtils.isNotBlank(p.getDescription()) ;            
        }
        out.print(")</tt></h2>");
        // description
        formatDescription(out,methodDescriptor);
        // parameters
        if (parameters.length > 0 && parameterDescriptionSeen){
            out.println("<h2>Parameters</h2><dl>");    
            for (int i = 0; i < parameters.length; i++) {
                final ValueDescriptor v= parameters[i];
                out.println("<dt><tt><b>");
                out.println(v.getName() );
                out.println("</b></tt></dt><dd>");
                out.println(v.getDescription());
                out.println("</dd>");           
            }        
            out.println("</dl>");
        }

        // return value
        if (StringUtils.isNotBlank(returnValue.getDescription())) {
            out.println("<h2>Return</h2>");
            out.println("<blockquote>");
            out.println(returnValue.getDescription());       
            out.println("</blockquote>");   
        }

        out.println("<hr/><h1>Try out this function</h1>");
        for (final Iterator i = resultTypes.iterator(); i.hasNext(); ) {
            final String method = i.next().toString();
            out.println("<h2>");
            out.print(method);
            out.print(" output</h2>");
            out.println("<form name='call' method='POST' action='./" + method+ "'><table>");
            for (int j = 0; j < parameters.length; j++) {
                final ValueDescriptor p = parameters[j];
                
                out.println("<tr><td><tt><b>");
                out.println(p.getName());
                out.println("</b></tt></td><td>");
                out.println("<input type='text' name='" +p.getName() + "'>");
                out.println("</td></tr>");
            }
            out.println("</table><input type='submit' value='Execute'></form>");
        }       

        out.println("<hr/><h1>Use this function from a script</h1>");
        out.println("<h2>RMI Access</h2>");        
        out.println("<tt>");
        out.print(cd.getInterfaceClass().getName());
        out.print(".");
        out.print(methodDescriptor.getName());
        out.println("()</tt>");
        out.println("<h2>XML-RPC Access</h2>");
        out.println("<tt>");
        out.print(md.getName());
        out.print(".");
        out.print(cd.getName());
        out.print(".");
        out.print(methodDescriptor.getName());
        out.println("()</tt>");
        out.println("<h2>HTTP Access</h2>");
        out.print("<tt><i>base-url</i>/");
        out.print(md.getName());
        out.print("/");
        out.print(cd.getName());
        out.print("/");
        out.print(methodDescriptor.getName());
        out.println("/<i>[");
        out.println(StringUtils.join(resultTypes.iterator(),'|'));
        out.print("]</i>?");
        for (int i = 0; i < parameters.length; i++) {
            final ValueDescriptor p = parameters[i];
            if (i > 0) {
                out.print("&amp;");
            }
            out.print(p.getName());
            out.print("=<i>val</i>");
        }
        out.print("</tt>");

        footer(out);
    }

    private void formatDescription(final PrintWriter out, final Descriptor d) {
        out.println("<p>");
        out.println(d.getDescription());
      //  out.println(StringUtils.replace(d.getDescription(),"\n","<br />"));
        out.println("</p>");
    }
    
    
    private void descriptorList(final PrintWriter out, final Iterator ds) {
        out.println("<dl>");
        while (ds.hasNext()) {
            final Descriptor d = (Descriptor)ds.next();
            if (d.isExcluded()) {
                continue;
            }        
            out.println("<dt>");
            out.println("<a href='./" + d.getName() + "/'>");
            out.println(d.getName());
            out.println("</a>");
            out.println("</dt><dd>");
            final String description = d.getDescription();
            final String title = summarize(description);
            out.print(title);
            if (StringUtils.isNotEmpty(title) && ! title.endsWith(".")) {
                out.print(".");
            }
            out.println("</dd>");
        }
        out.println("</dl>");
    }

    /** Summarize a description. - extracts the first sentance, stripping noise at front.
     * @param description
     * @return
     */
    static String summarize(final String description) {
        final Matcher m = summarizePattern.matcher(description);
        if (! m.find()) {
            return "";
        }
        final String title = m.group(1);
        return title;
    }

    protected Converter conv;
    protected Transformer html;
    protected Transformer plain;   

    // pattern used in summarizing.
    private static Pattern summarizePattern = Pattern.compile(
            "\\A\\s*(?:AR\\s+(?:System\\s+)?Service\\s*:\\s*)?(.+?)(\\.|<dl|<p|\\z|$)"
            ,Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    
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
        if (result instanceof byte[]) {
            // not working. Need to read up why .
            final byte[] data = (byte[])result;
            response.setContentType("application/octet-stream");
            final ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(data); 
           
                    //Base64.encodeBase64((byte[])result));
           // outputStream.close();
           
        } else {
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
        }
        } catch (final Exception e) {
        	reportError("Exception thrown when calling method " + md.getName(),e,request,response);
        }
    }    
    /**
     * @param out
     */
    private void header(final String title,final PrintWriter out) {
        out.print("<html><head><title>Astro Runtime API");
        if (StringUtils.isNotBlank(title)) {
            out.print(": ");
            out.print(title);
        }
        out.println("</title></head><body>");
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
Revision 1.17  2009/02/17 13:45:18  nw
Complete - taskfix http input of binary parameters.

Revision 1.16  2008/12/22 18:18:00  nw
improved in-program API help.

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