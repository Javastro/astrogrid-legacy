/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/server/src/java/org/astrogrid/filestore/server/servlet/FileStoreServlet.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/08/18 19:00:01 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreServlet.java,v $
 *   Revision 1.2  2004/08/18 19:00:01  dave
 *   Myspace manager modified to use remote filestore.
 *   Tested before checkin - integration tests at 91%.
 *
 *   Revision 1.1.2.7  2004/08/04 19:48:57  dave
 *   Fixed mime type in servlet
 *
 *   Revision 1.1.2.6  2004/08/04 19:15:28  dave
 *   Refactored servlet to get reply with file contents
 *
 *   Revision 1.1.2.5  2004/08/04 17:03:33  dave
 *   Added container to servlet
 *
 *   Revision 1.1.2.4  2004/08/04 16:10:19  dave
 *   Added config to servlet
 *
 *   Revision 1.1.2.3  2004/08/04 16:06:31  dave
 *   Added config to servlet
 *
 *   Revision 1.1.2.2  2004/08/04 15:56:44  dave
 *   Added config to servlet
 *
 *   Revision 1.1.2.1  2004/08/04 06:35:02  dave
 *   Added initial stubs for servlet ....
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.server.servlet ;

import java.io.IOException ;
import java.io.PrintWriter ;

import javax.servlet.ServletException ;

import javax.servlet.http.HttpServlet ;
import javax.servlet.http.HttpServletRequest ;
import javax.servlet.http.HttpServletResponse ;

import org.astrogrid.filestore.common.file.FileProperties ;
import org.astrogrid.filestore.common.file.FileIdentifier ;

import org.astrogrid.filestore.common.exception.FileStoreException ;

import org.astrogrid.filestore.server.repository.Repository ;
import org.astrogrid.filestore.server.repository.RepositoryImpl ;
import org.astrogrid.filestore.server.repository.RepositoryConfig ;
import org.astrogrid.filestore.server.repository.RepositoryContainer ;
import org.astrogrid.filestore.server.repository.RepositoryConfigImpl ;


/**
 * A file server servlet to handle HTTP GET requests.
 *
 */
public class FileStoreServlet
	extends HttpServlet
	{
	/**
	 * The default content type (used if the type was not set when the file was stored).
	 *
	 */
	public static final String DEFAULT_CONTENT_TYPE = "text/plain" ;

	/**
	 * Reference to our repository config.
	 *
	 */
	protected RepositoryConfig config ;

	/**
	 * Reference to our local repository.
	 *
	 */
	protected Repository repository ;

	/**
	 * Initialise our servlet.
	 *
	 */
	public void init()
		throws ServletException
		{
		//
		// Initialise our config.
		config = new RepositoryConfigImpl() ;
		//
		// Initialise our repository.
		repository = new RepositoryImpl(
			config
			);
		}

	/**
	 * Handle a GET request.
	 *
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException
		{
		//
		// Get the request path.
		String path = request.getPathInfo() ;
		//
		// If the path is valid.
		if (null != path)
			{
			//
			// Try locating the container.
			try {
				//
				// Strip off the leading '/' and convert to an identifier.
				// This relies on the FileIdentifier to check that the identifier is valid (which it don't at the moment).
				FileIdentifier ident = new FileIdentifier(
					path.substring(1)
					) ;
				//
				// Locate the container.
				RepositoryContainer container = repository.load(
					ident.toString()
					) ;
				//
				// Set the response content type.
				String type = container.properties().getProperty(
					FileProperties.MIME_TYPE_PROPERTY
					) ;
				if (null != type)
					{
					response.setContentType(
						type
						);
					}
				else {
					response.setContentType(
						DEFAULT_CONTENT_TYPE
						);
					}
				//
				// Transfer the data in our response.
				container.exportData(
					response.getOutputStream()
					) ;
				}
			catch (FileStoreException ouch)
				{
				response.sendError(
					HttpServletResponse.SC_NOT_FOUND,
					ouch.getMessage()
					) ;
				}
			}
/*
 *
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<body>");
		out.println("<head>");
		out.println("<title>Request Information Example</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h3>Request Information Example</h3>");
		out.println("Method: " + request.getMethod());
		out.println("<br>");
		out.println("Request URI: " + request.getRequestURI());
		out.println("<br>");
		out.println("Protocol: " + request.getProtocol());
		out.println("<br>");
		out.println("PathInfo: " + request.getPathInfo());
		out.println("<br>");
		out.println("Remote Address: " + request.getRemoteAddr());
		out.println("<hr>");
		out.println("Repository: " + config.getDataDirectory().toString());
		out.println("<hr>");
		out.println("</body>");
		out.println("</html>");
 *
 */
		}

	/**
	 * Handle a POST request.
	 *
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException
		{
		doGet(request, response);
		}
	}

