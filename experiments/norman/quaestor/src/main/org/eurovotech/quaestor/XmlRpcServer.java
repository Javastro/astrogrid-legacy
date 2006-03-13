package org.eurovotech.quaestor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import sisc.interpreter.SchemeException;

/**
 * The main servlet for Quaestor's XML-RPC service.  This is almost
 * identical to the Quaestor servlet, but implements only the
 * <code>POST</code> method, and calls a different
 * <code>quaestor.scm</code> procedure.
 */
public class XmlRpcServer extends HttpServlet {

    public void init()
            throws ServletException {
        try {
            String qt = getServletContext().getRealPath("WEB-INF/quaestor.scm");
            if (!SchemeWrapper.getInstance().loadOnce(qt)) {
                throw new ServletException
                        ("Failed to load Quaestor from " + qt);
            }
        } catch (SchemeException e) {
            throw new ServletException(e);
        }
    }

    /**
     * Service the HTTP POST method containing an XML-RPC call.  Do
     * this by calling the quaestor <code>xmlrpc-post</code> procedure
     * with the request's {@link java.io.Reader}.  That procedure will
     * return a String containing XML, on success or on anticipated
     * errors, or it will throw an exception.  Both should be
     * returned to the client.
     * @param request encapsulating the POST request
     * @param response encapsulating the response to be sent to the server
     */
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws java.io.IOException, ServletException {
        assert request != null && response != null;
        String contentType = request.getContentType();
        if (contentType == null || !contentType.equals("text/xml")) {
            emitErrorResponse
                    (response,
                     javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST,
                     "Content type must be text/xml, not <"
                     + contentType + ">");
            return;             // JUMP OUT
        }
        
        for (java.util.Enumeration e = request.getHeaderNames();
             e.hasMoreElements(); ) {
            String h = ((String)e.nextElement()).toLowerCase();
            if (h.startsWith("content-")) {
                if (h.equals("content-type")
                    || h.equals("content-length")) {
                    // do nothing
                } else {
                    // unrecognised content-* header
                    emitErrorResponse
                            (response,
                             javax.servlet.http.HttpServletResponse.SC_NOT_IMPLEMENTED,
                             "Header type <" + h + "> not recognised");
                }
            }
        }

        String handlerProcedure = getInitParameter("rpc-handler-procedure");
        if (handlerProcedure == null) {
            // ooops!
            throw new ServletException
              ("XmlRpcServer: rpc-handler-procedure parameter does not exist!");
        }
            
        log("XmlRpc called");
        try {
            Object val = SchemeWrapper
                    .getInstance()
                    .eval(handlerProcedure, new Object[] { request });
            if (val instanceof String) {
                response.setContentType("text/html");
                response.setStatus
                        (javax.servlet.http.HttpServletResponse.SC_OK);
                response.setContentLength(((String)val).length());
                PrintWriter out = response.getWriter();
                out.print(val);
            } else {
                emitErrorResponse
                        (response,
                         javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                         "Unexpected response from servlet: " + val.toString());
            }
        } catch (SchemeException e) {
            log("Scheme exception: " + e);
            emitErrorResponse
                    (response,
                     javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                              "Servlet error: " + e);
        }
    }

    private void emitErrorResponse(HttpServletResponse response,
                                   int status,
                                   String errorMessage)
            throws java.io.IOException {
        response.setStatus(status);
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.println(errorMessage);
    }
}
