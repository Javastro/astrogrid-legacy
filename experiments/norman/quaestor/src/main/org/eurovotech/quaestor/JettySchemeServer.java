package org.eurovotech.quaestor;

import java.io.PrintWriter;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import org.mortbay.jetty.Request;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.handler.HandlerList;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.log.Log;

import sisc.interpreter.SchemeException;
import sisc.data.Procedure;

/**
 * Implement a standalone scheme server within Jetty.
 *
 * <p>This is all pretty cruddy.  The various properties are all
 * built-in, rather than being read from a config file; all the
 * classes below are in-line in this file rather than being broken
 * out; the logging is primitive; and it's probably too low-level,
 * since I'm following a pattern I worked out quickly some while ago,
 * which uses the Handler.handle interface, rather than the
 * higher-level one using servlets.  The whole thing should be largely
 * rewritten once I get a chance.  On top of all that, the layout
 * which is managed by the build.xml is more customised for the Tomcat
 * version of this, and feels a bit weird for this version (as well,
 * there's no security, so that there's nothing stopping you
 * retrieving the WEB-INF/web.xml file).
 */
public class JettySchemeServer {

    static String versionString = "???";

    public static void main(String[] args) {
        int portNumber = 8080;   // default - can be overridden in properties or at command line
        boolean chatter = false; // default - can be overridden in properties or at command line

        Pattern opts = Pattern.compile("^--([a-z]+)(=(.*))?");

        Properties configProperties = new Properties();
        java.io.InputStream propStream = ClassLoader.getSystemResourceAsStream("JettySchemeServer.properties");
        if (propStream != null) {
            Log.info("Found JettySchemeServer.properties");
            try {
                configProperties.load(propStream);
                String s = configProperties.getProperty("version");
                if (s != null)
                    versionString = s;
                s = configProperties.getProperty("port");
                if (s != null)
                    portNumber = Integer.parseInt(s);
                s = configProperties.getProperty("verbose");
                if (s != null)
                    chatter = Boolean.parseBoolean(s);
            } catch (java.io.IOException e) {
                System.err.println("Malformed properties file! (shouldn't happen)");
                System.exit(1);
            } catch (NumberFormatException e) {
                System.err.println("Malformed port number: " + e);
                System.exit(1);
            }
        }

        // parse arguments
        for (int i=0; i<args.length; i++) {
            Matcher m = opts.matcher(args[i]);
            if (m.matches()) {
                String opt = m.group(1);

                if (opt.equals("port")) {
                    String p = m.group(3);
                    if (p == null)
                        Usage();
                    try {
                        portNumber = Integer.parseInt(p);
                    } catch (NumberFormatException e) {
                        System.err.println("Malformed port number <" + p + ">");
                        System.exit(1);
                    }

                } else if (opt.equals("verbose")) {
                    chatter = true;

                } else if (opt.equals("quiet")) {
                    chatter = false;

                } else if (opt.equals("help")) {
                    Usage();

                } else {
                    System.err.println("Unrecognised option --" + opt);
                    Usage();
                }
            } else {
                System.err.println("Non-option argument spotted");
                Usage();
            }
        }

        System.err.println("Quaestor starting on http://localhost:"
                           + portNumber + "/");

        JettyServer s = new JettyServer(portNumber, chatter, configProperties);
        s.run();
    }

    private static void Usage() {
        System.err.println("JettySchemeServer " + versionString);
        System.err.println("Usage:");
        System.err.println("  JettySchemeServer [--verbose] [--port=int] [--help]");
        System.err.println("Starts a server on the nominated port");
        System.err.println("  --verbose   chatter on stderr");
        System.err.println("  --quiet     ...or not");
        System.err.println("  --port=n    start the service on port n (default 8080)");
        System.err.println("  --help      display this help");
        System.exit(1);
    }

    /**
     * The main server, which marshalls the others.
     */
    public static class JettyServer
            implements QuaestorServlet {
        private boolean chatter;
        private int portNumber;

        private JettySchemeHandler kbHandler;
        private Properties servletProperties;

        public JettyServer(int port, boolean chatter, Properties props) {
            this.chatter = chatter;
            this.portNumber = port;
            this.servletProperties = props;
        }

        public void run() {
            String mainScriptName = "UNKNOWN";
            int exitStatus = 1; // error, unless cleared at the end of the try block

            try {

                // log name and version number
                Log.info("JettySchemeServer " + versionString);

                org.mortbay.jetty.Server server = new org.mortbay.jetty.Server(portNumber);

                // create and install the handlers which this server will manage
                kbHandler = new JettySchemeHandler();
                Handler docHandler = new JettyDocHandler(getInitParameter("doc-base", "war"));

                Handler codeHandler = new JettyCodeHandler(getInitParameter("repl-status","disabled").equals("enabled"));
                HandlerList handlerList = new HandlerList();
                handlerList.setHandlers(new Handler[] { kbHandler,
                                                        docHandler,
                                                        codeHandler });
                server.addHandler(handlerList);

                mainScriptName = getInitParameter("main-script");//"quaestor.scm";
                String initialiserProcedure = getInitParameter("main-script-initialiser");//"initialise-quaestor";
                if (mainScriptName == null || initialiserProcedure == null)
                    throw new ServletException("Failed to find initialisation properties");

                if (chatter)
                    Log.info("Classpath=" + System.getProperty("java.class.path"));
                java.io.InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(mainScriptName);
                if (is == null)
                    throw new ServletException
                            ("Failed to find resource " + mainScriptName);

                SchemeWrapper.getInstance().evalInputStream(is);
                if (chatter)
                    Log.info("Loaded main script from " + mainScriptName
                    + " with initialiser " + initialiserProcedure);

                // ...and initialise it
                Object val = SchemeWrapper
                        .getInstance()
                        .eval(initialiserProcedure, new Object[] {this});
                if (val instanceof String) {
                    throw new ServletException
                            ("Failed to initialise main-script " + mainScriptName
                             + ": procedure " + initialiserProcedure
                             + " reported " + val);
                }

//                 Handler[] handlerList
//                         = new Handler[] { new JettySchemeHandler() };
//                 // If chatter is true, then pass a logger on to the
//                 // handlers, if they have a setLogger() method.
//                 // I do like the smell of duck-typing in the morning; smells like victory....
//                 if (chatter) {
//                     for (int i=0; i<handlerList.length; i++) {
//                         try {
//                             java.lang.reflect.Method m = handlerList[i].getClass().getDeclaredMethod("setLogger", Logger.class);
//                             m.invoke(handlerList[i], logger);
//                         } catch (java.lang.NoSuchMethodException e) {
//                             // do nothing -- that's OK
//                         }
//                     }
//                 }
//                 server.setHandlers(handlerList);

                server.start();
                server.join();

                exitStatus = 0;

            } catch (ServletException e) {
                Log.info("Error starting servlet: " + e);
            } catch (java.io.IOException e) {
                Log.info("Error starting SISC (code=" + mainScriptName + ") [" + e + "]");
            } catch (SchemeException e) {
                Log.info("Scheme exception: " + e);
            } catch (Exception e) {
                // thrown by server.start
                Log.info("Server failed to start: " + e);
            }
            System.exit(exitStatus);
        }

        /** 
         * Register a Scheme handler procedure with this servlet.
         *
         * <p>This method is called by the Scheme initialiser to register
         * a procedure which will handle a given context/method pair.
         * The registrations are actually delegated to the kb-handler,
         * which is the thing which needs this information.
         *
         * @param method the name of the method, such as <code>GET</code>,
         *   <code>POST</code> and so on
         * @param context the webapp context, such as <code>/path</code>
         * @param proc a Scheme procedure as a (subclass of)
         *   {@link sisc.data.Procedure}
         */
        public void registerHandler(String method, String context, Procedure proc) {
            assert kbHandler != null;
            kbHandler.registerHandler(method, context, proc);
        }

        // implement getInitParameter from QuaestorServlet
        public String getInitParameter(String key) {
            return servletProperties.getProperty(key);
        }

        /**
         * Provide an initialisation parameter to a servlet, with default.
         * @param key the parameter name
         * @param defaultValue the value to return if the parameter is not set
         * @return the parameter value, or the default value if no parameter is set
         */
        public String getInitParameter(String key, String defaultValue) {
            return servletProperties.getProperty(key, defaultValue);
        }

        // implement log(String) from QuaestorServlet
        public void log(String message) {
            Log.info(message);
        }
    }

    /**
     * The main Jetty server for the Quaestor application -- invoke
     * handlers in the server's scheme support (quaestor.scm)
     */
    private static class JettySchemeHandler 
            extends org.mortbay.jetty.handler.AbstractHandler {
        private java.util.Map<String,Procedure> requestHandlerMap;
        private java.util.regex.Pattern servletPattern = java.util.regex.Pattern.compile("^/*(/[^/]+)/*(/.*)?");

        public JettySchemeHandler() {
            // Constructor has nothing exciting to do
        }

        public void handle(String target,
                           HttpServletRequest request,
                           HttpServletResponse response,
                           int dispatch)
                throws java.io.IOException, ServletException {

            Request rq;
            if (request instanceof Request)
                rq = (Request)request;
            else
                rq = HttpConnection.getCurrentConnection().getRequest();

            assert target.indexOf("/") == 0;

            // It seems unwise to set this this far down the call
            // stack, but safer for it not to be null.
            rq.setContextPath("");

            // Separate out the servlet path ("/foo") from the path-info (everything after that),
            // avoiding getting confused by multiple leading slashes
            java.util.regex.Matcher m = servletPattern.matcher(target);
            String servletPath;
            if (m.matches()) {
                servletPath = m.group(1);
                rq.setPathInfo(m.group(2));
            } else {
                // the only case where the pattern doesn't match is if 'target' is just a sequence of slashes
                // (or if the 'target' doesn't start with a slash, but we've asserted that it does, above)
                servletPath = "/";
                rq.setPathInfo("");
            }
            rq.setServletPath(servletPath);

            String method = rq.getMethod();
            Procedure proc = requestHandlerMap == null ? null : requestHandlerMap.get(method+servletPath);

            if (proc == null) {
                // error: unrecognised method+servletPath combination

                // Are we supposed to handle this servletPath?
                assert requestHandlerMap != null;
                if (requestHandlerMap.get(servletPath) == null) {
                    // no, we weren't
                    // fall through
                    rq.setHandled(false);
                } else {                        
                    // yes, this is one of 'our' servletPaths
                    response.setContentType("text/plain");
                    response.setStatus(response.SC_NOT_IMPLEMENTED);

                    StringBuffer mapkeys = new StringBuffer();
                    boolean firsttime = true;
                    for (String key : requestHandlerMap.keySet()) {
                        if (firsttime)
                            firsttime = false;
                        else
                            mapkeys.append(", ");
                        mapkeys.append(key);
                    }
            
                    PrintWriter out = LazyOutputStream.getLazyOutputStream(response)
                            .getWriter();
                    out.println
                            ("Method " + method + " not implemented for servletPath <"
                             + servletPath
                             + "> (can have " + mapkeys.toString() + ")");
                    out.flush();

                    rq.setHandled(true);
                }
            } else {
                // OK: normal case
                StringBuffer msg = new StringBuffer();
                msg.append(method)
                        .append(servletPath)
                        .append(": calling procedure on path ")
                        .append(rq.getPathInfo());
                Log.info(msg.toString());

                // following statuses will very probably be overridden within the
                // quaestorMethod procedure, but they're here in order to provide
                // sane defaults.
                //response.setContentType("text/plain");
                response.setStatus(response.SC_NOT_IMPLEMENTED);

                try {
                    Object val = SchemeWrapper.getInstance().eval(proc, new Object[] { rq, response });
                    //Log.info("Jetty value: " + val);
                    if (val instanceof String) {
                        // I don't _think_ we actually need LazyOutputStream any more
                        PrintWriter out = LazyOutputStream.getLazyOutputStream(response).getWriter();
                        out.print(val);
                        out.flush();
                    }
                } catch (SchemeException e) {
                    Log.warn("Scheme exception: " + e);
                    // with that logged, create a new exception, with fewer implementation details,
                    // which should end up being reported to the caller
                    throw new ServletException("Error evaluating servlet on path " + rq.getPathInfo()
                                               + "; see log for full details");
                }
                
                rq.setHandled(true);
            }
        }

        // implement registerHandler from QuaestorServlet
        public void registerHandler(String method, String servletPath, Procedure proc) {
            if (method == null || servletPath == null || proc == null)
                throw new IllegalArgumentException("registerHandler: must have non-null arguments");

            String key = method + servletPath;
            //Log.info("registerHandler: " + key + " -> " + proc.toString());
            if (requestHandlerMap == null)
                requestHandlerMap = new java.util.HashMap<String,Procedure>();

            assert requestHandlerMap != null;
            requestHandlerMap.put(key, proc);
            // what we put in the servletPath-only slot doesn't matter, as long as it's not null
            requestHandlerMap.put(servletPath, proc);
        }
    }

    /**
     * Handle documents.  This wraps a Jetty ResourceHandler, but the
     * latter isn't very good: it doesn't appear to handle HEADs, and
     * its MIME types are suspect (it returns .tar.gz as type
     * application/x-gtar without any Content-Encoding).  But it'll do
     * for now.
     */
    private static class JettyDocHandler
            extends org.mortbay.jetty.handler.AbstractHandler {
        
        org.mortbay.jetty.handler.ResourceHandler docHandler;

        /**
         * Construct a documentation handler.
         * @param base the location of the document base as a string naming a directory
         */
        public JettyDocHandler(String base) {
            docHandler = new org.mortbay.jetty.handler.ResourceHandler();
            java.io.File docBase = new java.io.File(base);
            Log.info("docBase=" + docBase + " absolute=" + docBase.getAbsolutePath());
            docHandler.setResourceBase("file:" + docBase.getAbsolutePath() + "/");
        }

        public void handle(String target,
                           HttpServletRequest request,
                           HttpServletResponse response,
                           int dispatch)
                throws java.io.IOException, ServletException {
            Request rq = request instanceof Request
                    ? (Request)request
                    : HttpConnection.getCurrentConnection().getRequest();
            rq.setPathInfo(target); // not done by default
            Log.info("Doc handling " + target);
            docHandler.handle(target, rq, response, dispatch);
        }
    }

    /**
     * Support running arbitrary code in the servlet's scheme REPL.
     * This should be subject to configuration!
     */
    private static class JettyCodeHandler
            extends org.mortbay.jetty.handler.AbstractHandler {

        boolean replEnabled = false;

        public JettyCodeHandler(boolean enableRepl) {
            this.replEnabled = enableRepl;
        }

        /**
         * Handle a POST method which contains some code to be included in the
         * Quaestor instance.
         *
         * <p>Check the value of the <code>status</code> init-parameter:
         * it must be one of <code>enabled</code> or <code>disabled</code>.
         */
        public void handle(String target,
                           HttpServletRequest request,
                           HttpServletResponse response,
                           int dispatch)
                throws java.io.IOException, ServletException {
            Request rq = request instanceof Request
                    ? (Request)request
                    : HttpConnection.getCurrentConnection().getRequest();

            Log.info("Code handling " + target);
            if (rq.getMethod().equals("POST") && target.equals("/code")) {

                // we're definitely going to handle something
                rq.setHandled(true);

                try {
                    response.setContentType("text/plain");
                    if (replEnabled) {
                        java.io.OutputStream out = response.getOutputStream();
                        java.io.PrintStream ps = new java.io.PrintStream(out);

                        Log.info("Evaluating scheme code from " + request.getRemoteHost());
                        
                        Object val = SchemeWrapper.getInstance().evalInputStream(request.getInputStream(), out);

                        Log.info("Value returned: " + val);

                        // the following isn't particularly elegant, but it suffices
                        if (val == null) {
                            response.setStatus(response.SC_NO_CONTENT);
                        } else if (val instanceof String) {
                            response.setStatus(response.SC_OK);
                            ps.println(val);
                        } else if (val instanceof Boolean) {
                            response.setStatus(response.SC_OK);
                            ps.println(((Boolean)val).booleanValue() ? "#t" : "#f");
                        } else {
                            // I'm not sure how this would happen, and not be
                            // a SchemeException
                            response.setStatus(response.SC_BAD_REQUEST); // is this the best response?
                            ps.println(";; Bad request?");
                            ps.println(";; returned object <" 
                                       + val.toString()
                                       + "> of type "
                                       + val.getClass().toString());
                        }
                    } else {
                        Log.warn("Received POST of scheme code from "
                                 + request.getRemoteHost()
                                 + " while code manager is disabled");
                        response.setStatus(response.SC_FORBIDDEN);
                        response.getWriter().println("Code updating forbidden by configuration");
                    }
                } catch (SchemeException e) {
                    Log.info("Threw exception: text=" + e.getMessage());
                    response.setStatus(response.SC_BAD_REQUEST);
                    // ie, we presume this is the user's fault, not ours!
                    java.io.PrintStream ps = new java.io.PrintStream(response.getOutputStream());
                    ps.println(e.getMessageText());
                }
            }
        }
    }
}
