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

    public void init() {
        // nothing to do
    }

    /**
     * Handle a PUT method which contains some code to be included in the
     * Quaestor instance.
     *
     * <p>Check the value of the <code>status</code> init-parameter:
     * it must be one of <code>enabled</code> or <code>disabled</code>.
     */
    public void doPut(HttpServletRequest request,
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
                Object val = SchemeWrapper
                        .getInstance().evalInput(request.getInputStream());
                // XXX following is pretty cruddy
                if (val == null) {
                    response.setStatus(response.SC_NO_CONTENT);
                } else if (val instanceof String) {
                    response.setStatus(response.SC_OK);
                    response.getWriter().println(val);
                } else if (val instanceof Boolean) {
                    response.setStatus(response.SC_OK);
                    response.getWriter()
                            .println(((Boolean)val).booleanValue() ? "#t" : "#f");
                } else {
                    response.setStatus(response.SC_BAD_REQUEST); // ???
                    PrintWriter p = response.getWriter();
                    p.println(";; Bad request?");
                    p.println(val.toString());
                }
            } else if (codeStatus.equals("disabled")) {
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
