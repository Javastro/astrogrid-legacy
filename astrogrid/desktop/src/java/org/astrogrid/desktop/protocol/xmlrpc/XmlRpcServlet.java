/*$Id: XmlRpcServlet.java,v 1.1 2005/02/21 11:25:07 nw Exp $
 * Created on 31-Jan-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.protocol.xmlrpc;

import org.astrogrid.desktop.DesktopServer;
import org.astrogrid.desktop.Main;
import org.astrogrid.desktop.service.Community;
import org.astrogrid.desktop.service.MetadataHelper;
import org.astrogrid.desktop.service.MethodDoc;
import org.astrogrid.desktop.service.ParamDoc;
import org.astrogrid.desktop.service.ReturnDoc;
import org.astrogrid.desktop.service.ServiceDoc;
import org.astrogrid.desktop.service.Services;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcHandler;
import org.apache.xmlrpc.XmlRpcServer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Implementation of full-features xmlrpc server over the Service objects.
 *  - uses annotations to publish methods, and implement 'system' methods.
 * @author Noel Winstanley nw@jb.man.ac.uk 31-Jan-2005
 *
 */
public class XmlRpcServlet extends HttpServlet {

    /** process an xmlrpc call */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        byte[] result = xmlrpc.execute (request.getInputStream ());
        response.setContentType ("text/xml");
        response.setContentLength (result.length);
        OutputStream out = response.getOutputStream();
        out.write (result);
        out.flush ();        
    }

    /** return a bit of documentation */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        //can't call a service - so just list out the methods we've got.
         PrintWriter out = response.getWriter();
        out.println("<html><body><a href='./.' >up</a><h1>XMLRPC Server</h1>");
        out.println("To call service, use POST");
        out.println("<h2>Available Services</h2><ul>");
        try {
        SystemXmlRpcHandler system = (SystemXmlRpcHandler)xmlrpc.getHandlerMapping().getHandler("system.listMethods");
        for (Iterator i = system.listMethods().iterator(); i.hasNext(); ) {
            out.println("<li>");
            out.println(i.next());
            out.println("</li>");
        }
        out.println("</ul>");
        } catch (Exception e) {
            throw new ServletException(e);
        }
        out.println("</body></html>");        
}
    /** the server implemntation that this servlet exposes */
    protected final XmlRpcServer xmlrpc = new XmlRpcServer ();
    protected Community community;
    /** create the xml server, register all the services */
    public void init(ServletConfig conf) throws ServletException {
       Services services = (Services) conf.getServletContext().getAttribute(DesktopServer.SERVICES);
       community = (Community)services.getService("community");
       for (Iterator i = services.getServiceNames().iterator(); i.hasNext(); ) {
           String name =(String) i.next();
           xmlrpc.addHandler(name,new ServiceXmlRpcHandler(services.getService(name)));
       }
       xmlrpc.addHandler("system",new SystemXmlRpcHandler(services));
    }
    
    /** class that exposes one of our annotated services as a xml service */
    public class ServiceXmlRpcHandler implements XmlRpcHandler {
        public ServiceXmlRpcHandler(Object service) {
            this.service = service;
            sd = MetadataHelper.getServiceDoc(service);            
        }
        protected final Object service;
        protected final ServiceDoc sd;
        
        /**
         * @see org.apache.xmlrpc.XmlRpcHandler#execute(java.lang.String, java.util.Vector)
         */
        public Object execute(String method, Vector inputArgs) throws Exception {

            if (! (method.equalsIgnoreCase("community.login") || method.startsWith("configuration."))) {
                community.loginIfNecessary();
            }            
            method = method.split("\\.")[1]; // remove the prefix.
            Method m = MetadataHelper.getMethodByName(service.getClass(),method);
            MethodDoc md = MetadataHelper.getMethodDoc(m);
            if (md == null) {
                throw new XmlRpcException(1,"This method is not available");   
            }
            List pds = MetadataHelper.getParamDocs(m);
            ReturnDoc rd = MetadataHelper.getReturnDoc(m);        
            Class[] parameterTypes = m.getParameterTypes();
            if (inputArgs.size() != parameterTypes.length) {
                throw new XmlRpcException(2,"Incorrect number of parameters supplied");
            }
            // convert parameters to correct types.
            Object[] args = new Object[parameterTypes.length];            
            for (int i =0; i < parameterTypes.length; i++) {
                args[i] = ( (ParamDoc) pds.get(i)).getConverter().convert(parameterTypes[i],inputArgs.get(i));
            }
            // call method
            Object result = MethodUtils.invokeMethod(service,method,args);
            return rd.getRts().getXmlrpcTransformer().transform(result);
        }
    }
   
    /** implementation of the system-introspection service */
    public static class SystemXmlRpcHandler implements XmlRpcHandler {
        public SystemXmlRpcHandler(Services services) {
            this.services = services;
        }
        protected final Services services;
        /**
         * @see org.apache.xmlrpc.XmlRpcHandler#execute(java.lang.String, java.util.Vector)
         */
        public Object execute(String arg0, Vector arg1) throws Exception {
            if ("system.listMethods".equalsIgnoreCase(arg0)) {
                return listMethods();
            }
            if ("system.methodSignature".equalsIgnoreCase(arg0)) {
                return methodSignature(arg1.get(0).toString());
            }
            if ("system.methodHelp".equalsIgnoreCase(arg0)){
                return methodHelp(arg1.get(0).toString());
            }
            throw new XmlRpcException(0,"Unknown operation " + arg0);
        }
        
        
        public Vector listMethods() {
            Vector result = new Vector();
            result.add("system.listMethods");
            result.add("system.methodSignature");
            result.add("system.methodHelp");
            for (Iterator i = services.getServices().iterator(); i.hasNext(); ) {
                Object service = i.next();
                ServiceDoc sd = MetadataHelper.getServiceDoc(service);
                for (Iterator j = MetadataHelper.getMethodDocsForClass(service.getClass()).iterator(); j.hasNext(); ) {
                    MethodDoc md = (MethodDoc)j.next();
                    result.add(sd.getName() + "." + md.getName());
                }
            }
            return result;
        }
        
        public Vector methodSignature(String methodName) {
            Vector sigs = new Vector();
            Vector sig = new Vector();
            sigs.add(sig);
            String[] names = methodName.split("\\.");
            if (names[0].equalsIgnoreCase("system")) {
                if (names[1].equalsIgnoreCase("listMethods")) {
                    sig.add("array");
                    sig.add("string");
                }
                if (names[1].equalsIgnoreCase("methodSignature")) {
                    sig.add("array");
                    sig.add("string");
                }
                if (names[1].equalsIgnoreCase("methodHelp")) {
                    sig.add("string");
                    sig.add("string");
                }
            } else {
            Object service = services.getService(names[0]);
            Method m = MetadataHelper.getMethodByName(service.getClass(),names[1]);
            ReturnDoc rd = MetadataHelper.getReturnDoc(m);
            sig.add(rd.getRts().getXmlrpcType());
            for (Iterator i = MetadataHelper.getParamDocs(m).iterator(); i.hasNext(); ) {
                sig.add( ((ParamDoc)i.next()).getXmlrpcType());
            }           
            }
            return sigs;
        }

        public String methodHelp(String methodName) {
            String[] names = methodName.split("\\.");
            if (names[0].equalsIgnoreCase("system")) {
                if (names[1].equalsIgnoreCase("listMethods")) {
                    return "This method may be used to enumerate the methods implemented by the XML-RPC server.\n" +
                            " It requires no parameters. \n" +
                            "It returns an array of strings, each of which is the name of a method implemented by the server.";                    
                }
                if (names[1].equalsIgnoreCase("methodSignature")) {
                    return "This method takes one parameter, the name of a method implemented by the XML-RPC server.\n" +
                            "It returns an array of possible signatures for this method. A signature is an array of types. \n" +
                            "The first of these types is the return type of the method, the rest are parameters.\n" +
                            "Multiple signatures (ie. overloading) are permitted: this is the reason that an array of signatures are returned by this method.\n" +
                            "Signatures themselves are restricted to the top level parameters expected by a method. \n" +
                            "For instance if a method expects one array of structs as a parameter, and it returns a string, its signature is simply 'string, array'.\n" +
                            " If it expects three integers, its signature is 'string, int, int, int'.";
                }
                if (names[1].equalsIgnoreCase("methodHelp")) {
                    return "This method takes one parameter, the name of a method implemented by the XML-RPC server.\n" +
                            " It returns a documentation string describing the use of that method. \n" +
                            "If no such string is available, an empty string is returned.";
                }         
                return "unknown method";
            } else {
            Object service = services.getService(names[0]);
            ServiceDoc sd = MetadataHelper.getServiceDoc(service);
            Method m = MetadataHelper.getMethodByName(service.getClass(),names[1]);
            MethodDoc md = MetadataHelper.getMethodDoc(m);
            ReturnDoc rd = MetadataHelper.getReturnDoc(m);            
            StringBuffer result = new StringBuffer();
            result.append("Object ")
                    .append(sd.getName())
                    .append("\n")
                    .append(sd.getDescription())
                    .append("\n")
                    .append("Method ")
                    .append(md.getName())
                    .append("\n")
                    .append(md.getDescription());
            for (Iterator params =  MetadataHelper.getParamDocs(m).iterator(); params.hasNext(); ) {
                result.append("\n")
                        .append(params.next());
            }
            result.append("\n")
                    .append(rd.toString());
            return result.toString();
            }
        }        
    }
    
}


/* 
$Log: XmlRpcServlet.java,v $
Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/