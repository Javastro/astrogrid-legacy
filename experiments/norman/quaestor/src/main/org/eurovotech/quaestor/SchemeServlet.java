package org.eurovotech.quaestor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.GenericServlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sisc.interpreter.SchemeException;
import sisc.data.Procedure;

/**
 * The main servlet for the Quaestor (and other) application
 */
public class SchemeServlet extends GenericServlet {

    private java.util.Map<String,Procedure> requestHandlerMap;

    /**
     * Initialise the servlet.
     *
     * <p>The <code>web.xml</code> file is expected to provide two
     * parameters, <code>main-script</code>, which is the name of a
     * SISC program which is loaded by this method, and
     * <code>main-script-initialiser</code>, which is the name of a
     * procedure within that script, which is immediately evaluated,
     * with a reference to this object as argument.  The
     * <code>main-script</code> parameter is the path to the script,
     * relative to the webapp directory.
     *
     * <p>The initialiser procedure
     * should do any initialisation it requires, and make at least one
     * call to the {@link #registerHandler} method.  Hint: to pass a
     * SISC procedure to this method, it must be wrapped in
     * <code>(java-wrap ...)</code>.
     */
    public void init()
            throws ServletException {
        requestHandlerMap = new java.util.HashMap<String,Procedure>();

        // find the main script and initialiser parameters
        String mainScriptName = getInitParameter("main-script");
        String initialiserProcedure
                = getInitParameter("main-script-initialiser");
        if (mainScriptName == null || initialiserProcedure == null)
            throw new ServletException
                    ("Parameters main-script and main-script-initialiser must both have values");

        try {
            // load the script
            java.io.InputStream is = getServletContext()
                    .getResourceAsStream(mainScriptName);
            if (is == null)
                throw new ServletException
                        ("Failed to find resource " + mainScriptName);
            SchemeWrapper.getInstance().evalInputStream(is);
            log("Loaded main script from " + mainScriptName
                + " with initialiser " + initialiserProcedure);

            // ...and initialise it
            Object val = SchemeWrapper
                    .getInstance()
                    .eval(initialiserProcedure,
                          new Object[] { this });
            if (val instanceof String) {
                throw new ServletException
                        ("Failed to initialise main-script " + mainScriptName
                         + ": procedure " + initialiserProcedure
                         + " reported " + val);
            }
        } catch (IOException e) {
            throw new ServletException
                    ("Error starting SISC (code=" + mainScriptName + ")", e);
        } catch (SchemeException e) {
            log("Scheme exception: " + e);
            throw new ServletException(e);
        }
    }

    /** 
     * Register a Scheme handler procedure with this servlet.
     *
     * <p>This method is called by the Scheme initialiser to register
     * a procedure which will handle a given context/method pair.
     *
     * @param method the name of the method, such as <code>GET</code>,
     *   <code>POST</code> and so on
     * @param context the webapp context, such as <code>/path</code>
     * @param proc a Scheme procedure as a (subclass of)
     *   {@link sisc.data.Procedure}
     */
    public void registerHandler(String method, String context, Procedure proc) {
        String key = method + context;
        log("registerHandler: " + key + " -> " + proc.toString());
        requestHandlerMap.put(key, proc);
    }

    public void service(ServletRequest genreq, ServletResponse genres)
            throws ServletException, IOException {
        assert(genreq instanceof HttpServletRequest
               && genres instanceof HttpServletResponse);
        HttpServletRequest  request  = (HttpServletRequest)genreq;
        HttpServletResponse response = (HttpServletResponse)genres;

        String method = request.getMethod();
        String context = request.getServletPath();
        Procedure proc = requestHandlerMap.get(method+context);

        if (proc == null) {
            // error: unrecognised method+context combination
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
                    ("Method " + method + " not implemented for context <"
                     + context
                     + "> (can have " + mapkeys.toString() + ")");
            out.flush();
        } else {
            // OK: normal case
            StringBuffer msg = new StringBuffer();
            msg.append(method)
                    .append(context)
                    .append(": calling procedure on <")
                    .append(request.getPathInfo())
                    .append(">");
            log(msg.toString());

            // following statuses will very probably be overridden within the
            // quaestorMethod procedure, but they're here in order to provide
            // sane defaults.
            response.setContentType("text/plain");
            response.setStatus(response.SC_OK);

            try {
                Object val = SchemeWrapper
                        .getInstance()
                        .eval(proc,
                              new Object[] { request, response });
                if (val instanceof String) {
                    PrintWriter out =
                            LazyOutputStream.getLazyOutputStream(response)
                            .getWriter();
                    out.print(val);
                    out.flush();
                }
            } catch (SchemeException e) {
                log("Scheme exception: " + e);
                throw new ServletException(e);
            }
        }
    }
    
}


                          
