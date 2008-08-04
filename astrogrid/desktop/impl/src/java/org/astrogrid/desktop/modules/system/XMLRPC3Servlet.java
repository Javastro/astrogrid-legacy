/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.xmlrpc.webserver.XmlRpcServletServer;
import org.astrogrid.desktop.framework.SessionManagerInternal;

/** Minimalistic XMLRPC servlet. Enters the supplied session, and then delegates to the xmlrpc server.
 *  - an instance of this servlet is created for each session. Howeever, the more weighty xmlrpc server is shared between all sessions.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 22, 20082:06:16 PM
 */
public class XMLRPC3Servlet extends HttpServlet {
    private  XmlRpcServletServer xmlrpc ;     
    private SessionManagerInternal session;
   
   // servlet interface.
   @Override
   public void init(ServletConfig conf) throws ServletException {
       final ServletContext servletContext = conf.getServletContext();
       xmlrpc = (XmlRpcServletServer)servletContext.getAttribute("xmlrpc");
       session = (SessionManagerInternal)servletContext.getAttribute("session-manager");        
   }
   
   /** return a bit of documentation */
   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
   throws ServletException, IOException {
       //can't call a service - so just list out the methods we've got.
        PrintWriter out = response.getWriter();
       out.println("<html><body><a href='./.' >up</a><h1>XMLRPC Interface to AR</h1>");
       out.println("To call service, use POST");
       out.println("</body></html>");        
}
    
   /** process an xmlrpc call */
   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
       // put this thread into the correct session, if any
       Principal sess = session.findSessionForKey(StringUtils.substringBetween(request.getRequestURI(),"/","/"));
       session.adoptSession(sess);
       try {
           xmlrpc.execute(request,response);
       } finally {
           session.clearSession();
       }
   }
}
