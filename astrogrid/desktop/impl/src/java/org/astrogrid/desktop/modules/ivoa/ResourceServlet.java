/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

/** Servlet that provides trivial access to XML of registry resources.
 * do a http get, single parameter {@code id} containing the IVOA-ID of the resource to display
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 24, 20095:06:39 PM
 */
public class ResourceServlet extends HttpServlet {

    
    private RegistryInternal reg;

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        final URI id;
        try {
            final String v = req.getParameter("id");
            if (StringUtils.isBlank(v)) {
                throw new ServletException("Provide the IVOA-ID of the resource to display using the ?id= query parameter");
            }
            id  = new URI(v);
            if (! "ivo".equals(id.getScheme())) {
                throw new ServletException("Resource ID must start with 'ivo://'");
            }
        } catch (final URISyntaxException x) {
            throw new ServletException("Failed to parse 'id' parameter",x);
        }
        final Document resourceXML ;
        try {
            resourceXML = reg.getResourceXML(id);
        } catch (final ServiceException x) {
           throw new ServletException(x);
        } catch (final NotFoundException x) {
            throw new ServletException(id + " not found");
        }
        resp.setContentType("text/xml");
        final PrintWriter w = resp.getWriter();
        DomHelper.DocumentToWriter(resourceXML,w);
        w.flush();        
    }
    
    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
        final ACR acr = (ACR)config.getServletContext().getAttribute("module-registry");
        try {
            reg = (RegistryInternal)acr.getService(Registry.class);
        } catch (final InvalidArgumentException x) {
            throw new ServletException(x);
        } catch (final NotFoundException x) {
            throw new ServletException(x);
        } catch (final ACRException x) {
            throw new ServletException(x);
        }
    }
}
