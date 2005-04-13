/*$Id: XmlRpcServlet.java,v 1.2 2005/04/13 12:59:12 nw Exp $
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

import org.astrogrid.desktop.framework.Module;
import org.astrogrid.desktop.framework.ModuleRegistry;
import org.astrogrid.desktop.framework.ReflectionHelper;
import org.astrogrid.desktop.framework.descriptors.ComponentDescriptor;
import org.astrogrid.desktop.framework.descriptors.MethodDescriptor;
import org.astrogrid.desktop.framework.descriptors.ModuleDescriptor;
import org.astrogrid.desktop.framework.descriptors.ValueDescriptor;
import org.astrogrid.desktop.modules.system.converters.DefaultConverter;
import org.astrogrid.desktop.modules.system.transformers.DefaultResultTransformerSet;
import org.astrogrid.desktop.modules.system.transformers.ResultTransformerSet;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcHandler;
import org.apache.xmlrpc.XmlRpcServer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Implementation of full-features xmlrpc server over the Service objects.
 * @todo future - look at other xmlrpc implementations..
 * @author Noel Winstanley nw@jb.man.ac.uk 31-Jan-2005
 *
 */
public class XmlRpcServlet extends HttpServlet {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(XmlRpcServlet.class);

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
    
    /** create the xml server, register all the services */
    public void init(ServletConfig conf) throws ServletException {
        ModuleRegistry reg = (ModuleRegistry) conf.getServletContext().getAttribute(WebServer.MODULE_REGISTRY);        
       for (Iterator i = reg.moduleIterator(); i.hasNext(); ) {
           Module m = (Module)i.next();
           ModuleDescriptor md = m.getDescriptor();
           for (Iterator j = md.componentIterator(); j.hasNext(); ) {
               ComponentDescriptor cd = (ComponentDescriptor)j.next();
            String name1= md.getName() + "." + cd.getName();
               String name = name1;               
               xmlrpc.addHandler(name,new ComponentXmlRpcHandler(m,cd));
           }
       }
     xmlrpc.addHandler("system",new SystemXmlRpcHandler(reg));
    }
    
    /** class that exposes one of our annotated services as a xml service */
    public class ComponentXmlRpcHandler implements XmlRpcHandler {
        /**
         * Commons Logger for this class
         */
        private final Log logger = LogFactory.getLog(ComponentXmlRpcHandler.class);

        public ComponentXmlRpcHandler(Module m, ComponentDescriptor cd){
            this.m = m;
            this.cd = cd;            
        }
        protected final Module m;
        protected final ComponentDescriptor cd;
        
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
                for (int i =0; i < parameterTypes.length; i++) {
                    Converter conv = DefaultConverter.getInstance();                 
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
                ResultTransformerSet trans = getTransformerSet(md.getReturnValue());
                return trans.getXmlrpcTransformer().transform(result);  
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
    /**    
        * @param returnValue
         * @return
         */
        public static ResultTransformerSet getTransformerSet(ValueDescriptor returnValue) {
            String s = returnValue.getProperty(RESULT_TRANSFORMER);
            if (s == null) {
                return DefaultResultTransformerSet.getInstance();
            }
            ResultTransformerSet trans = null;
            try {
                Class c = Class.forName(s.trim());
                Object o = c.newInstance();
                if (o instanceof ResultTransformerSet) {
                    trans = (ResultTransformerSet)o;
                }
            } catch (ClassNotFoundException e) {
                logger.warn("ClassNotFoundException",e);
            } catch (InstantiationException e) {
                logger.warn("InstantiationException",e);
            } catch (IllegalAccessException e) {
                logger.warn("IllegalAccessException",e);
            }        
        return trans != null ? trans : DefaultResultTransformerSet.getInstance();
    } 
    
    public static Converter getConverter(ValueDescriptor parameter) {
        String s = parameter.getProperty(PARAMETER_CONVERTER);
        if (s == null) {
            return DefaultConverter.getInstance();
        }
        Converter conv = null;
        try {
            Class c = Class.forName(s.trim());
            Object o = c.newInstance();
            if (o instanceof Converter) {
                conv = (Converter)o;
            }
        } catch (ClassNotFoundException e) {
            logger.warn("ClassNotFoundException",e);
        } catch (InstantiationException e) {
            logger.warn("InstantiationException",e);
        } catch (IllegalAccessException e) {
            logger.warn("IllegalAccessException",e);
        }        
    return conv != null ? conv : DefaultConverter.getInstance();        
    }
        
    public static final String XMLRPC_TYPE_KEY = "system.xmlrpc.type";
    public static final String RESULT_TRANSFORMER="system.result.transformer";
    public static final String PARAMETER_CONVERTER="system.parameter.converter";
   
    /** implementation of the system-introspection service */
    public static class SystemXmlRpcHandler implements XmlRpcHandler {
        /**
         * Commons Logger for this class
         */
        private static final Log logger = LogFactory.getLog(SystemXmlRpcHandler.class);

        public SystemXmlRpcHandler(ModuleRegistry reg) {
            this.reg= reg;
        }
        protected final ModuleRegistry reg;
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
            for (Iterator i = reg.moduleIterator(); i.hasNext(); ) {
                Module m = (Module)i.next();
                for(Iterator j = m.getDescriptor().componentIterator(); j.hasNext(); ) {
                    ComponentDescriptor cd = (ComponentDescriptor)j.next();
                    for (Iterator k = cd.methodIterator(); k.hasNext(); ) {
                        MethodDescriptor md = (MethodDescriptor)k.next();
                        result.add(m.getDescriptor().getName() + "." + cd.getName() + "." + md.getName());
                    }
                }
            }
            return result;
        }
        
        public Vector methodSignature(String methodName) throws XmlRpcException {
            Vector sigs = new Vector();
            Vector sig = new Vector();
            sigs.add(sig);
            String[] names = methodName.split("\\.");
            if (names[0].equalsIgnoreCase("system")) {
                if (names[1].equalsIgnoreCase("listMethods")) {
                    sig.add("array");
                    sig.add("string");
                    return sig;
                }
                if (names[1].equalsIgnoreCase("methodSignature")) {
                    sig.add("array");
                    sig.add("string");
                    return sig;
                }
                if (names[1].equalsIgnoreCase("methodHelp")) {
                    sig.add("string");
                    sig.add("string");
                    return sig;
                }
            }
                Module m = reg.getModule(names[0]);
                if (m == null) {
                    throw new XmlRpcException(100, "Unknown module " + names[0]);
                }
                ComponentDescriptor cd = m.getDescriptor().getComponent(names[1]);
                if (cd == null) {
                    throw new XmlRpcException(101,"Unknown component " + names[1]);
                }                
                MethodDescriptor md = cd.getMethod(names[2]);
                if (md == null) {
                    throw new XmlRpcException(102,"Unknown method "+ names[2]);
                }                
                sig.add(getXMLRPCType(md.getReturnValue()));
                for (Iterator i = md.parameterIterator(); i.hasNext();) {
                    sig.add(getXMLRPCType( ((ValueDescriptor)i.next())));
                }       
            
            return sigs;
        }
        
        protected String getXMLRPCType(ValueDescriptor vd) {
            String type = vd.getProperty(XMLRPC_TYPE_KEY);
            return type == null ? "string" : type.trim();
        }

        public String methodHelp(String methodName) throws XmlRpcException {
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
            } 
            Module m = reg.getModule(names[0]);
            if (m == null) {
                throw new XmlRpcException(100, "Unknown module " + names[0]);
            }
            ComponentDescriptor cd = m.getDescriptor().getComponent(names[1]);
            if (cd == null) {
                throw new XmlRpcException(101,"Unknown component " + names[1]);
            }                
            MethodDescriptor md = cd.getMethod(names[2]);
            if (md == null) {
                throw new XmlRpcException(102,"Unknown method "+ names[2]);
            }
         
            StringBuffer result = new StringBuffer();
            result.append("Module ")
                    .append(m.getDescriptor().getName())
                    .append("\n\t")
                    .append(m.getDescriptor().getDescription())                       
                    .append("\nComponent ")
                    .append(cd.getName())
                    .append("\n\t")
                    .append(cd.getDescription())
                    .append("\nMethod ")
                    .append(md.getName())
                    .append("\n\t")
                    .append(md.getDescription());
            for (Iterator i = md.parameterIterator(); i.hasNext(); ) {
                ValueDescriptor vd = (ValueDescriptor)i.next();
                result.append("\n")
                        .append(vd.getName())
                        .append(" : ")
                        .append(getXMLRPCType(vd))
                        .append("\n")
                        .append(vd.getDescription());                                
            }           
            result.append("\n")
                    .append(md.getReturnValue().getName())
                    .append(" : ")
                    .append(getXMLRPCType(md.getReturnValue()))
                    .append("\n")
                    .append(md.getReturnValue().getDescription());
            return result.toString();
            }
        }        
    }
    



/* 
$Log: XmlRpcServlet.java,v $
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