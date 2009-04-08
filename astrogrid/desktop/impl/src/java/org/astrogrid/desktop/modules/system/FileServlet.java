/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.file.Systems;

/** Servlet that provides trivial access to content of any VFS-accessible file.
 * Do a http get, single paramter {@code id} containing URI of the resource to retrieve.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 8, 200910:56:10 AM
 */
public class FileServlet extends HttpServlet {
        
  

    private Systems systems;
    private FileSystemManager vfs;

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException { // don't bother catching exceptions from vfs - let them bubble out instead.
            final String id = req.getParameter("id");
            if (StringUtils.isBlank(id)) {
                throw new ServletException("Provide the URI of the file to retrieve using the ?id= query parameter. \nSupported schemes are " + ArrayUtils.toString(systems.listSchemes()));
            }
            
            final String scheme = StringUtils.substringBefore(id,":");
            // not parsing as URI, because spaces cause if to foul up.
            if (! ArrayUtils.contains(systems.listSchemes(), scheme)) {
                throw new ServletException("Unknown URI scheme:  supported schemes are " + ArrayUtils.toString(systems.listSchemes()));
            }

        
        final FileObject fileObject = vfs.resolveFile(id);
        if (! fileObject.getType().hasContent()) {
            throw new ServletException("Not a file : " + id);
        }
        if (! fileObject.isReadable()) {
            throw new ServletException("Unreadable : " + id);
        }
       
        //resp.setContentType("text/xml"); @todo do I need to set content-type?
        OutputStream os  = null;
        InputStream is = null;
        try {
            os = resp.getOutputStream();
            is = fileObject.getContent().getInputStream();
            IOUtils.copy(is,os);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }      
    }
        
    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
        final ACR acr = (ACR)config.getServletContext().getAttribute("module-registry");
        try {
            // access the components we need.
           systems = (Systems)acr.getService(Systems.class);
           vfs = (FileSystemManager)config.getServletContext().getAttribute("vfs");
        } catch (final InvalidArgumentException x) {
            throw new ServletException(x);
        } catch (final NotFoundException x) {
            throw new ServletException(x);
        } catch (final ACRException x) {
            throw new ServletException(x);
        }
    }

}
