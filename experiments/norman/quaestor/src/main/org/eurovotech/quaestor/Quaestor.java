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
    
    public void init()
            throws ServletException {
        try {
            String qt = getServletContext().getRealPath("WEB-INF/quaestor.scm");
//             Object o = wrapper.eval("(with-failure-continuation (lambda (err cont) (format #f \"Error at ~a: ~a\" (error-location err) (error-message err))) (lambda () (load \"" + qt + "\") #t))");
//             if (o instanceof String) {
//                 throw new ServletException
//                         ("Failed to load quaestor from " + qt
//                          + ": "+ o);
//             }
            if (!SchemeWrapper.getInstance().load(qt)) {
                throw new ServletException
                        ("Failed to load Quaestor from " + qt);
            }

        } catch (SchemeException e) {
            throw new ServletException(e);
        }
    }

    /**
     * Service HTTP methods by handing them over to the corresponding
     * Quaestor procedures.  That procedure is responsible for handling the
     * request, examining the headers, and setting the response status.
     */
    private void callQuaestorHandler(String quaestorMethod,
                                     HttpServletRequest request,
                                     HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("text/html");
        log("Quaestor calling " + quaestorMethod);
        try {
            Object val = SchemeWrapper
                    .getInstance()
                    .eval(quaestorMethod, new Object[] { request, response });
            if (val instanceof String) {
                PrintWriter out = response.getWriter();
                out.println(val);
            }
        } catch (SchemeException e) {
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
        callQuaestorHandler("http-get", request, response);
    }

    /**
     * Handle a PUT method by handing it over to the quaestor put
     * procedure.
     */
    public void doPut(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException, ServletException {
        callQuaestorHandler("http-put", request, response);
    }

    /**
     * Handle a DELETE method by handing it over to the quaestor put
     * procedure.
     */
    public void doDelete(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException, ServletException {
        callQuaestorHandler("http-delete", request, response);
    }

    /**
     * Handle a POST method by handing it over to the quaestor put
     * procedure.
     */
    public void doPost(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException, ServletException {
        callQuaestorHandler("http-post", request, response);
    }
}
