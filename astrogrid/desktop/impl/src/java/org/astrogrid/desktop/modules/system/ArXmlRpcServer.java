/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.map.LazyMap;
import org.apache.commons.lang.StringUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcHandler;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.XmlRpcRequestConfig;
import org.apache.xmlrpc.common.XmlRpcInvocationException;
import org.apache.xmlrpc.common.XmlRpcStreamRequestConfig;
import org.apache.xmlrpc.parser.XmlRpcRequestParser;
import org.apache.xmlrpc.server.XmlRpcHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcNoSuchHandlerException;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.util.SAXParsers;
import org.apache.xmlrpc.webserver.XmlRpcServletServer;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.builtin.ComponentDescriptor;
import org.astrogrid.acr.builtin.MethodDescriptor;
import org.astrogrid.acr.builtin.ModuleDescriptor;
import org.astrogrid.acr.system.ApiHelp;
import org.astrogrid.desktop.framework.ACRInternal;
import org.astrogrid.desktop.framework.Module;
import org.astrogrid.desktop.framework.ReflectionHelper;
import org.astrogrid.desktop.modules.system.ArXmlRpcServer.SystemXmlRpcHandler.PythonIntrospectionHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import edu.emory.mathcs.backport.java.util.Arrays;

/** Custom and configured XmlRpcServer
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 22, 20085:45:45 PM
 */
public class ArXmlRpcServer extends XmlRpcServletServer implements XmlRpcHandlerMapping {

    private final  SystemXmlRpcHandler systemHandler;
    private final PythonIntrospectionHandler introspectionHandler;
    private final Converter conv;
    
    private final Map<String,XmlRpcHandler> handlers;
    private final Transformer trans;
    private final XmlRpcHandler plasticClient;
    
    /**
     * Create a new xmlrpc server
     * @param reg the AR, used to retreive components to provde access to
     * @param trans converts invocation results to formats suitable for xmlrpc
     * @param conv converts invocation parameters from xmlrpc to java types
     * @param apiHelp used for xmlrpc 'introspection'
     * @param hub custom handler for plastic hub (bypasses usual handler mechanism).
     * @param plasticClient custom handler used for plastic client (bypasses usual handler mech)
     */
    public ArXmlRpcServer(
            final ACRInternal reg, final Transformer trans, final Converter conv, final ApiHelp apiHelp, final XmlRpcHandler hub, final XmlRpcHandler plasticClient) {
        super();
        this.trans = trans;
        this.conv = conv;
        this.plasticClient = plasticClient;
        this.systemHandler = new SystemXmlRpcHandler(apiHelp);
        this.introspectionHandler = new PythonIntrospectionHandler(apiHelp);
                
        // create a map of handlers.
        // nifty here. It's a lazy map - the transformer is used to create new
        // map elements on demand. the LazyMap javadoc says it's not threadsafe,
        // and to wrap it in a synchronizedMap if needed.
        final Map<String,XmlRpcHandler> baseMap = new HashMap<String,XmlRpcHandler>();
        // populate with pre-canned xmlrpc handlers.
        baseMap.put("plastic.hub",hub); 
        baseMap.put("plastic.client",plasticClient); // legacy way of calliing into the plastic client - more recent hubs drops the 'plastic.client' prefix.
        
        // all the rest will be lazily created.
        handlers = Collections.synchronizedMap(LazyMap.decorate(baseMap, new Transformer() {
            public Object transform(final Object moduleDotComponent) {
                final String[] bits = StringUtils.split((String)moduleDotComponent,'.');
                if (bits == null) {
                    throw new IllegalArgumentException("Module name was null");
               } else if ( bits.length != 2) {
                   throw new IllegalArgumentException((String)moduleDotComponent);
               }
               final Module module = reg.getModule(bits[0]);
               final ModuleDescriptor md = module.getDescriptor();
               final ComponentDescriptor cd = md.getComponent(bits[1]);
               return new ComponentXmlRpcHandler(module,cd);
            }
        }));
        
        final XmlRpcServerConfigImpl xmlrpcConfig = new XmlRpcServerConfigImpl();
        // would be maybe nicer to enable some of these, but for max compatibility, stick to the standard.
        xmlrpcConfig.setContentLengthOptional(false); 
        xmlrpcConfig.setEnabledForExtensions(false);
        xmlrpcConfig.setKeepAliveEnabled(false);

        // finally, configure the server.
        setConfig(xmlrpcConfig);
        setHandlerMapping(this);
    }

    /** defines the handler mapping -
     * this implementation has a variety of rules to select the correct handler. 
     */
    public XmlRpcHandler getHandler(final String arg0)
            throws XmlRpcNoSuchHandlerException, XmlRpcException {
        final String moduleDotComponent = StringUtils.substringBeforeLast(arg0,".");
        if (arg0.endsWith("__")) { // some kind of magic used by python.
            return introspectionHandler;
        }
        if ("perform".equals(arg0)) { // callback from hub into plastic client.
            return plasticClient;
        }
        // special case for 'system' - as it doesn't follow 'module.component' syntax.
        if ("system".equalsIgnoreCase(moduleDotComponent)) {
            return systemHandler;
        }
        // else it's one of the AR interfaces - created in a lazy way.
         final XmlRpcHandler handler = handlers.get(moduleDotComponent);
         if (handler == null) {
             throw new XmlRpcNoSuchHandlerException(arg0);
         } else {
             return handler;
         }
    }
    /** provides the XML-RPC introspection functions - {@code system.listMethod()}, {@code system.methodHelp()}, etc */
    public static class SystemXmlRpcHandler implements XmlRpcHandler {
        public SystemXmlRpcHandler(final ApiHelp apiHelp) {
            super();
            this.apiHelp = apiHelp;
        }
    private final ApiHelp apiHelp;

    public Object execute(final XmlRpcRequest arg0) throws XmlRpcException {
        final String mName = arg0.getMethodName();
        if ("system.methodHelp".equalsIgnoreCase(mName) && arg0.getParameterCount() >0) {
            try {
                return apiHelp.methodHelp(arg0.getParameter(0).toString());
            } catch (final NotFoundException x) {
                throw new XmlRpcException("unknown method: " + arg0);
            }            
        } else if ("system.methodSignature".equalsIgnoreCase(mName) && arg0.getParameterCount() >0) {
            try {
                return apiHelp.methodSignature(arg0.getParameter(0).toString());
            } catch (final NotFoundException x) {
                throw new XmlRpcException("unknown method: " + arg0);
            }             
        } else if ("system.listMethods".equalsIgnoreCase(mName)) {
            return apiHelp.listMethods();
        } else {
            throw new XmlRpcNoSuchHandlerException("Unknown method: " + mName);
        }
    }
    
    /**  Handles introspection calls from python. 
      basic bit of help. - sadly can't supply __doc__ , which would be the most sensible way of 
      sending documntation back to python, as the python xmlrpc client blocks this call.
     * */
    public static class PythonIntrospectionHandler implements XmlRpcHandler {

        private final ApiHelp apiHelp;

        /**
         * @param apiHelp
         */
        public PythonIntrospectionHandler(final ApiHelp apiHelp) {
            this.apiHelp = apiHelp;
        }

        public Object execute(final XmlRpcRequest request) throws XmlRpcException {
            final List<String> components = new ArrayList(Arrays.asList(StringUtils.split(request.getMethodName(),".")));
            // now work out what we've got.
            final String fn = components.remove(components.size()-1);

            final StringBuilder sb = new StringBuilder();
            if ("__str__".equals(fn)) {
                info(components, sb,true);    
            } else if ("__repr__".equals(fn) ) {                 
                info(components, sb,false);                
            } else {
                sb.append(request.getMethodName());
                sb.append("(");
                for (int i = 0; i < request.getParameterCount(); i++) {
                    sb.append(request.getParameter(i));
                    if (i+1 < request.getParameterCount()) {
                        sb.append(", ");
                    }
                }
                throw new XmlRpcNoSuchHandlerException(sb.toString());
            }
            return sb.toString();
        }

        /**
         * @param components
         * @param sb
         */
        private void info(final List<String> components, final StringBuilder sb, final boolean showHelp) {
            if (! showHelp) {
                sb.append("<");
            }
            sb.append("AstroRuntime: ");
            String help = null;
            switch(components.size()) {
                case 0:
                    break;
                case 1:
                    try {
                        help = apiHelp.moduleHelp(components.get(0));                        
                        sb.append("Module ");
                    } catch (final NotFoundException e) {
                        sb.append("Unknown module ");
                    }
                    if (! showHelp) {
                        sb.append(components.get(0));
                    }
                    break;
                case 2:
                    try {
                        help = apiHelp.componentHelp(components.get(0) + "." + components.get(1));                            
                        sb.append("Component ");
                    } catch (final NotFoundException e) {
                        sb.append("Unknown component ");
                    } 
                    if (! showHelp) {
                        sb.append(components.get(0))
                        .append(".")
                        .append(components.get(1));
                    }
                    break;
                case 3:
                    try {
                        help = apiHelp.methodHelp(
                                components.get(0) + "." + components.get(1) + "." + components.get(2));                            
                        sb.append("Function ");
                    } catch (final NotFoundException e) {
                        sb.append("Unknown function ");
                    }       
                    if (! showHelp) {
                        sb.append(components.get(0))
                        .append(".")
                        .append(components.get(1))
                        .append(".")
                        .append(components.get(2));
                    }
                    break;                            
            }
            if (showHelp && help != null) {
                sb.append("\n")
                .append(help);
            }
            if (! showHelp) {
                sb.append(">");
            }
        }               
    }    
    }

    
/** handler for one component */
    private class ComponentXmlRpcHandler implements XmlRpcHandler {
        public ComponentXmlRpcHandler(final Module m, final ComponentDescriptor cd){
            if (m == null) {
                throw new IllegalArgumentException("Null module provided");                
            }
            if (cd == null) {
                throw new IllegalArgumentException("Null component descriptor provided");
            }
            this.m = m;
            this.cd = cd;         
        }
        protected final ComponentDescriptor cd;       
        protected final Module m    ;
        public Object execute(final XmlRpcRequest request) throws XmlRpcException {
            final String methodName = StringUtils.substringAfterLast(request.getMethodName(),".");
            Object service;
            Method method;
            try {
                service = m.getComponent(cd.getName());
                method = ReflectionHelper.getMethodByName(service.getClass(),methodName);
            } catch (final Exception e) {
                final XmlRpcNoSuchHandlerException exception = new XmlRpcNoSuchHandlerException(request.getMethodName() + " is not available:");
                exception.initCause(e);
                throw exception;
            }
            final MethodDescriptor md = cd.getMethod(methodName);
            if (md == null) {
                throw new XmlRpcNoSuchHandlerException(request.getMethodName() + " is not available");   
            }      
            final Class[] parameterTypes = method.getParameterTypes();
            if (request.getParameterCount() != parameterTypes.length) {
                throw new XmlRpcNoSuchHandlerException("Incorrect number of parameters:" + request.getMethodName() + " takes " + parameterTypes.length + " parameters");  
            }
            // convert parameters to correct types.
            final Object[] args = new Object[parameterTypes.length];
            for (int i =0; i < parameterTypes.length ;  i++) {
                args[i] = conv.convert(parameterTypes[i],request.getParameter(i));
//                System.err.println(request.getParameter(i) 
//                        + ", " + request.getParameter(i).getClass().getName()
//                        + " -> " + args[i]
//                         + ", " + args[i].getClass().getName()
//                        );
                
            }
            // call method
            Object result;
            try {
                result = MethodUtils.invokeMethod(service,methodName,args);
            } catch (final InvocationTargetException e) {
                final Throwable t = e.getCause();
                throw new XmlRpcInvocationException(methodName + " failed: " 
                        + t.getMessage() == null ? "" : t.getMessage()
                         + " , "
                         + t.getClass().getName()
                        ,t);
            } catch (final NoSuchMethodException x) {
                throw new XmlRpcNoSuchHandlerException(request.getMethodName() + " is not available");   
            } catch (final IllegalAccessException x) {
                throw new XmlRpcNoSuchHandlerException(request.getMethodName() + " is not available");   
            } 
            if (method.getReturnType().equals(Void.TYPE)) { // i.e. it's not returning anything
                return "OK";
            } else if  (result == null) { // xmlrpc can't send back null values..
                result = "NULL";
            }

            return trans.transform(result);  
        }

    }
    
     
// overridden method, to fix problem with encodings seen when talking to VIRGO.

    @Override
    protected XmlRpcRequest getRequest(final XmlRpcStreamRequestConfig pConfig,
                                       final InputStream pStream) throws XmlRpcException {
        final XmlRpcRequestParser parser = new XmlRpcRequestParser(pConfig, getTypeFactory());
        final XMLReader xr = SAXParsers.newXMLReader();
        xr.setContentHandler(parser);
        try {           
            // added the construction of a reader to wrap the pStream.
            // this means that the reader deduces the encoding of the xml, rather
            //than using the ?xml encoding attr - Virgo has encoding='system', 
            // which makes the system fall down with an 'unsupportedEncodingException'
            final InputStreamReader reader = new InputStreamReader(pStream);
            final InputSource input = new InputSource(reader);
            xr.parse(input);
        } catch (final SAXException e) {
            final Exception ex = e.getException();
            if (ex != null  &&  ex instanceof XmlRpcException) {
                throw (XmlRpcException) ex;
            }
            throw new XmlRpcException("Failed to parse XML-RPC request: " + e.getMessage(), e);
        } catch (final IOException e) {
            throw new XmlRpcException("Failed to read XML-RPC request: " + e.getMessage(), e);
        }
        final List params = parser.getParams();
        return new XmlRpcRequest(){
            public XmlRpcRequestConfig getConfig() { return pConfig; }
            public String getMethodName() { return parser.getMethodName(); }
            public int getParameterCount() { return params == null ? 0 : params.size(); }
            public Object getParameter(final int pIndex) { return params.get(pIndex); }
        };
    }   

    
}
