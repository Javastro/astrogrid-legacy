package org.eurovotech.jackdaw;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 
 * The main servlet for the Jackdaw application
 */
public class Jackdaw extends javax.servlet.http.HttpServlet {

    public void init()
            throws ServletException {
        // nothing to do!
        log("Jackdaw started!");
    }

    @SuppressWarnings("unchecked")
    /**
     * Handle a GET request.
     * @param req a servlet request
     * @param res a servlet response
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        handleParameters(req.getParameterMap(), res);
    }
    
    @SuppressWarnings("unchecked")
    /**
     * Handle a POST request.
     * @param req a servlet request
     * @param res a servlet response
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        handleParameters(req.getParameterMap(), res);
    }

    /**
     * Handle the parameters of a GET or POST request.  This should be
     * called from the {@link #doGet} or {@link #doPost} method.
     * <p>This handles the response.  If the parameter map does not
     * have precisely one <code>url</code> parameter, this produces a
     * 400 (Bad Request) response; otherwise 200 (OK).
     *
     * @param params a {@link java.util.Map<String,String[]>} object,
     * as returned from the {@link HttpServletRequest#getParameterMap} method
     * @param res the {@link HttpServletResponse} given to the handler method
     */
    private void handleParameters(java.util.Map<String,String[]> params,
                                  HttpServletResponse res)
            throws IOException {

        String[] vals = params.get("url");
        if (vals == null || vals.length != 1) {
            res.setStatus
                    (javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST);
            res.setContentType("text/plain");
            PrintWriter pw = res.getWriter();

            pw.println("Bad request: there must be precisely one url= parameter");
        } else {
            // OK
            String[] urls = vals[0].split(" *, *");

            XMLBuilder.Doc xb = new XMLBuilder()
                    .setIndent(true)
                    .newDocument("suggestions");
            xb.addAttribute("xmlns", "http://ns.eurovotech.org/jackdaw");

            XMLBuilder.Node oneSuggestion = xb.newChild("group");
            oneSuggestion
                    .addAttribute("type",
                                  "http://ns.eurovotech.org/jackdaw/group#identity")
                    .addAttribute("description", "Identity suggestion");
            for (int i=0; i<urls.length; i++)
                oneSuggestion.newChild("url", urls[i]);

            java.io.StringWriter sw = new java.io.StringWriter();
            xb.serialise(new java.io.PrintWriter(sw));
            String s = sw.toString();

            res.setStatus(javax.servlet.http.HttpServletResponse.SC_OK);
            // for discussion of XML MIME types, see RFC 3023
            res.setContentType("application/xml");
            res.setContentLength(s.length());

            res.setCharacterEncoding("UTF-8");
            PrintWriter pw = res.getWriter();
            pw.print(s);
        } 
    }
}
