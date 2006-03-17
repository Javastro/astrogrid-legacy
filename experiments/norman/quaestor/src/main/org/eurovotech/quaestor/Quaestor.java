package org.eurovotech.quaestor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sisc.interpreter.SchemeException;

/**
 * The main servlet for the Quaestor application
 */
public class Quaestor extends HttpServlet {

    private java.util.Map requestHandlerMap;
    
    public void init()
            throws ServletException {
        String qt = null;
        try {
            qt = getServletContext().getRealPath("WEB-INF/quaestor.scm");
            if (!SchemeWrapper.getInstance().loadOnce(qt)) {
                // shouldn't happen -- load returns true or throws exception
                throw new ServletException
                        ("Unexpected return from Quaestor load: " + qt);
            }
            log("Successfully(?) loaded quaestor.scm from " + qt);
            
            String kbContext = getInitParameter("kb-context");
            String xmlrpcContext = getInitParameter("xmlrpc-context");
            requestHandlerMap = new java.util.HashMap();
            requestHandlerMap.put("GET" + kbContext,
                                  "http-get");
            requestHandlerMap.put("PUT" + kbContext,
                                  "http-put");
            requestHandlerMap.put("DELETE" + kbContext,
                                  "http-delete");
            requestHandlerMap.put("POST" + kbContext,
                                  "http-post");
            requestHandlerMap.put("POST" + xmlrpcContext,
                                  "xmlrpc-handler");
        } catch (IOException e) {
            throw new ServletException("Couldn't parse load file " + qt, e);
        } catch (SchemeException e) {
            throw new ServletException("Error reading file " + qt, e);
        }
    }

    /**
     * Service HTTP methods by handing them over to the corresponding
     * Quaestor procedures.  That procedure is responsible for handling the
     * request, examining the headers, and setting the response status.
     */
    private void callQuaestorHandler(String httpMethod,
                                     HttpServletRequest request,
                                     HttpServletResponse response)
            throws IOException, ServletException {
        String context = request.getServletPath();
        String quaestorMethod
                = (String)requestHandlerMap.get(httpMethod+context);
        if (quaestorMethod == null) {
            response.setContentType("text/plain");
            response.setStatus(response.SC_NOT_IMPLEMENTED);

            StringBuffer mapkeys = new StringBuffer();
            for (java.util.Iterator i=requestHandlerMap.keySet().iterator();
                 i.hasNext(); ) {
                mapkeys.append((String)i.next()).append(", ");
            }
            
            response.getWriter().println
                    ("Method " + httpMethod + " not implemented for context "
                     + context
                     + " (can have" + mapkeys.toString() + ")");
        } else {
            log(httpMethod + context + ": calling procedure " + quaestorMethod);
            try {
                Object val = SchemeWrapper
                        .getInstance()
                        .eval(quaestorMethod,
                              new Object[] { request, response });
                if (val instanceof String) {
                    PrintWriter out = response.getWriter();
                    out.println(val);
                }
            } catch (SchemeException e) {
                log("Scheme exception: " + e);
                throw new ServletException(e);
            }
        }
    }
                
    private void xXcallQuaestorHandler(String quaestorMethod,
                                     HttpServletRequest request,
                                     HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("text/html");
        log("Quaestor calling " + quaestorMethod);
        assert request != null && response != null;
        try {
            Object val = SchemeWrapper
                    .getInstance()
                    .eval(quaestorMethod, new Object[] { request, response });
            if (val instanceof String) {
                PrintWriter out = response.getWriter();
                out.println(val);
            }
        } catch (SchemeException e) {
            log("Scheme exception: " + e);
            throw new ServletException(e);
        }
    }

    /**
     * Handle a GET method by handing it over to the quaestor get
     * procedure.
     */
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException, ServletException {
        callQuaestorHandler("GET", request, response);
    }

    /**
     * Handle a PUT method by handing it over to the quaestor put
     * procedure.
     */
    public void doPut(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException, ServletException {
        callQuaestorHandler("PUT", request, response);
    }

    /**
     * Handle a DELETE method by handing it over to the quaestor put
     * procedure.
     */
    public void doDelete(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException, ServletException {
        callQuaestorHandler("DELETE", request, response);
    }

    /**
     * Handle a POST method by handing it over to the quaestor put
     * procedure.
     */
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws IOException, ServletException {
        callQuaestorHandler("POST", request, response);
    }
}
