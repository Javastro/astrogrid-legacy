package org.eurovotech.quaestor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sisc.interpreter.SchemeException;

/**
 * A helper class which manages code in the Quaestor REPL.
 */
public class CodeManager extends HttpServlet {

    // The following is required, in order to suppress a -Xlint warning, since this
    // class extends HttpServlet.  Since I don't serialise anything, I don't think this matters,
    // but I might as well consistently keep this as a number based on the CVS revision number:
    // major*1000+minor, when I remember.
    private static final long serialVersionUID = 1009L;

    public void init() {
        // nothing to do
    }

    /**
     * Handle a POST method which contains some code to be included in the
     * Quaestor instance.
     *
     * <p>Check the value of the <code>status</code> init-parameter:
     * it must be one of <code>enabled</code> or <code>disabled</code>.
     */
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws IOException, ServletException {
        try {
            response.setContentType("text/plain");
            String codeStatus = getInitParameter("status");
            if (codeStatus == null) {
                // ooops!
                throw new ServletException
                        ("CodeManager: status init parameter does not exist!");
            } else if (codeStatus.equals("enabled")) {
                java.io.OutputStream out = response.getOutputStream();
                java.io.PrintStream ps = new java.io.PrintStream(out);

                log("Evaluating scheme code from " + request.getRemoteHost());

                Object val = SchemeWrapper
                        .getInstance()
                        .evalInputStream(request.getInputStream(), out);

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
                    response.setStatus(response.SC_BAD_REQUEST); // ???
                    ps.println(";; Bad request?");
                    ps.println(";; returned object <" 
                               + val.toString()
                               + "> of type "
                               + val.getClass().toString());
                }
            } else if (codeStatus.equals("disabled")) {
                log("Received POST of scheme code from "
                    + request.getRemoteHost()
                    + " while code manager is disabled");
                response.setStatus(response.SC_FORBIDDEN);
                response.getWriter().println
                        ("Code updating forbidden by configuration");
            } else {
                throw new ServletException
                        ("CodeManager: status parameter has unrecognised value "
                         + codeStatus);
            }
        } catch (SchemeException e) {
            throw new ServletException(e);
        }
    }
}
