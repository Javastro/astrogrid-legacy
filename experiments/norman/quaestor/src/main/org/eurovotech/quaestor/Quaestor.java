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
//             log("initialised wrapper " + wrapper + " in servlet " + this
//                 + " in thread " + Thread.currentThread()
//                 + " with quaestor from " + qt);

        } catch (SchemeException e) {
            throw new ServletException(e);
        }
    }

//     public void destroy() {
// //         log("destroying wrapper " + wrapper + " in servlet " + this
// //             + " in thread " + Thread.currentThread());
//         //wrapper = null;         // I don't know if this is necessary
//     }

    /**
     * Handle a GET method by handing it over to the quaestor get
     * procedure.  That procedure is responsible for handling the
     * request, examining the headers, and setting the response status.
     */
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("text/html");
//         log("GET in servlet " + this);
        try {
            Object val = SchemeWrapper
                    .getInstance()
                    .eval("get", new Object[] { request, response });
            if (val instanceof String) {
                PrintWriter out = response.getWriter();
                out.println(val);
            }
        } catch (SchemeException e) {
            throw new ServletException(e);
        }
    }

    /**
     * Handle a PUT method by handing it over to the quaestor put
     * procedure.  That procedure is responsible for handling the
     * request, examining the headers, and setting the response status.
     */
    public void doPut(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("text/html");
        try {
            Object val = SchemeWrapper
                    .getInstance()
                    .eval("put", new Object[] { request, response });
            if (val instanceof String) {
                PrintWriter out = response.getWriter();
                out.println(val);
            }
        } catch (SchemeException e) {
            throw new ServletException(e);
        }
    }
}
