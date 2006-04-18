/*$Id: XmlRpcServlet.java,v 1.3 2006/04/18 23:25:44 nw Exp $
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
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcHandler;
import org.apache.xmlrpc.XmlRpcServer;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.ApiHelp;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.framework.ACRInternal;
import org.astrogrid.desktop.framework.Module;
import org.astrogrid.desktop.framework.ReflectionHelper;
import org.astrogrid.desktop.framework.descriptors.ComponentDescriptor;
import org.astrogrid.desktop.framework.descriptors.MethodDescriptor;
import org.astrogrid.desktop.framework.descriptors.ModuleDescriptor;
import org.astrogrid.desktop.framework.descriptors.ValueDescriptor;
/** Implementation of full-featured XMLRPC server that exposes the ACR functions
 * @author Noel Winstanley nw@jb.man.ac.uk 31-Jan-2005
 *
 */
public class XmlRpcServlet extends HttpServlet {
    protected Converter conv;
    protected Transformer trans;    
    /** class that exposes one of our annotated services as a xml service */
    public class ComponentXmlRpcHandler implements XmlRpcHandler {
        protected final ComponentDescriptor cd;
        protected final Module m    ;

        /**
         * Commons Logger for this class
         */
        private final Log logger = LogFactory.getLog(ComponentXmlRpcHandler.class);
        public ComponentXmlRpcHandler(Module m, ComponentDescriptor cd){
            this.m = m;
            this.cd = cd;         
        }
        
        /**
         * @see org.apache.xmlrpc.XmlRpcHandler#execute(java.lang.String, java.util.Vector)
         */
        public Object execute(String method, Vector inputArgs) throws Exception{
            if (logger.isDebugEnabled()) {
                logger.debug(m.getDescriptor().getName() + "." + cd.getName() + " - " + method + " - " + inputArgs);
            }
            method = method.split("\\.")[2]; 
            Object service = m.getComponent(cd.getName());
            Method m = ReflectionHelper.getMethodByName(service.getClass(),method);
            MethodDescriptor md = cd.getMethod(method);
            if (md == null) {
                logger.info("method not available");
                throw new XmlRpcException(1,"This method is not available");   
            }      
            Class[] parameterTypes = m.getParameterTypes();
            if (inputArgs.size() != parameterTypes.length) {
                logger.info("Incorrect number of parameters supplied");
                throw new XmlRpcException(2,"Incorrect number of parameters supplied");
            }
            try {
                // convert parameters to correct types.
                logger.debug("Converting args...");
                Object[] args = new Object[parameterTypes.length];
                Iterator it = md.parameterIterator();
                for (int i =0; i < parameterTypes.length; i++) {
                    ValueDescriptor vd = (ValueDescriptor)it.next();
                    args[i] = conv.convert(parameterTypes[i],inputArgs.get(i));
                }
                // call method

                logger.debug("Calling method...");
                Object result = MethodUtils.invokeMethod(service,method,args);
                if (m.getReturnType().equals(Void.TYPE)) { // i.e. it's not returning anything
                    return "OK";
                } else if  (result == null) { // xmlrpc can't send back null values..
                    logger.info("Method returned null - bodging");
                    result = "NULL";
                }
                return trans.transform(result);  
            } catch (InvocationTargetException e) {
                logger.warn("Exception in calling method",e.getCause());
                if (e.getCause() instanceof Exception) { // hamstrung by the method signature
                    throw (Exception) e.getCause();
                } else {
                    throw e;
                }
            } catch (Exception t) {                
                logger.warn("Exception in calling method",t);
               throw t;
            }
        }

   
    }

    /** implementation of the system-introspection service */
    public static class SystemXmlRpcHandler implements XmlRpcHandler {
        /**
         * Commons Logger for this class
         */
        private static final Log logger = LogFactory.getLog(SystemXmlRpcHandler.class);

        protected final ApiHelp apiHelp;
        public SystemXmlRpcHandler(ACR reg) throws ACRException {            
            apiHelp = (ApiHelp)reg.getService(ApiHelp.class);
        }
        /**
         * @see org.apache.xmlrpc.XmlRpcHandler#execute(java.lang.String, java.util.Vector)
         */
        public Object execute(String arg0, Vector arg1) throws Exception {
            if ("system.listMethods".equalsIgnoreCase(arg0)) {
                return new Vector(Arrays.asList(apiHelp.listMethods()));
            }
            if ("system.methodSignature".equalsIgnoreCase(arg0)) {
                return new Vector(Arrays.asList(apiHelp.methodSignature(arg1.get(0).toString())));
            }
            if ("system.methodHelp".equalsIgnoreCase(arg0)){
                return apiHelp.methodHelp(arg1.get(0).toString());
            }    
            throw new XmlRpcException(0,"Unknown operation " + arg0);
        }
        
    }

    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(XmlRpcServlet.class);
    /** the server implemntation that this servlet exposes */
    protected final XmlRpcServer xmlrpc = new XmlRpcServer ();
    
    /** create the xml server, register all the services */
    public void init(ServletConfig conf) throws ServletException {
        final ServletContext servletContext = conf.getServletContext();
        ACRInternal reg = (ACRInternal) servletContext.getAttribute(WebServer.ACR_CONTEXT_KEY);
        conv = (Converter)servletContext.getAttribute("converter");
        trans = (Transformer)servletContext.getAttribute("rpcResultTransformer");   
       for (Iterator i = reg.moduleIterator(); i.hasNext(); ) {
           Module m = (Module)i.next();
           ModuleDescriptor md = m.getDescriptor();
           for (Iterator j = md.componentIterator(); j.hasNext(); ) {
               ComponentDescriptor cd = (ComponentDescriptor)j.next();
            String name= md.getName() + "." + cd.getName();
               xmlrpc.addHandler(name,new ComponentXmlRpcHandler(m,cd));
           }
       }
     try {
        xmlrpc.addHandler("system",new SystemXmlRpcHandler(reg));
    } catch (ACRException e) {
        throw new ServletException(e);
    }
    }
    
    /** return a bit of documentation */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        //can't call a service - so just list out the methods we've got.
         PrintWriter out = response.getWriter();
        out.println("<html><body><a href='./.' >up</a><h1>XMLRPC Server</h1>");
        out.println("To call service, use POST");

        out.println("</body></html>");        
}
     
    /** process an xmlrpc call */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("doPost(request = " + request + ", response = " + response + ") - start");
        }
        
        byte[] result = xmlrpc.execute (request.getInputStream ());
        response.setContentType ("text/xml");
        response.setContentLength (result.length);
        OutputStream out = response.getOutputStream();
        out.write (result);
        out.flush ();        

        if (logger.isDebugEnabled()) {
            logger.debug("doPost() - end");
        }
    }
   
        
    }
    



/* 
$Log: XmlRpcServlet.java,v $
Revision 1.3  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.2.60.4  2006/04/18 18:49:03  nw
version to merge back into head.

Revision 1.2.60.3  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.2.60.2  2006/04/04 10:31:25  nw
preparing to move to mac.

Revision 1.2.60.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development

Revision 1.2  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.5  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.4  2005/06/23 09:08:26  nw
changes for 1.0.3 release

Revision 1.3  2005/04/27 13:42:41  clq2
1082

Revision 1.2.2.1  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2  2005/04/13 12:59:12  nw
checkin from branch desktop-nww-998

Revision 1.1.2.2  2005/03/22 12:04:03  nw
working draft of system and ag components.

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.

Revision 1.2  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/