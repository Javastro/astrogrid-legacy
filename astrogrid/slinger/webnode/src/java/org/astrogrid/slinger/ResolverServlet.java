
package org.astrogrid.slinger;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.Principal;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.account.LoginAccount;
import org.astrogrid.io.Piper;
import org.astrogrid.slinger.Slinger;
import org.astrogrid.slinger.sources.SourceIdentifier;
import org.astrogrid.slinger.sources.SourceMaker;
import org.astrogrid.slinger.targets.HttpResponseTarget;

/**
 * A servlet that takes a URI and attempts to resolve it to a stream,
 * piping that stream to the response
 *
 * @author MCH
 */

public class ResolverServlet extends HttpServlet
{

   /** Do same on POST requests as GET requests */
   public void doPost(HttpServletRequest request,
                     HttpServletResponse response) throws ServletException, IOException {
      doGet(request, response);
   }

   /** Returns anonymous for now */
   public Principal getUser(HttpServletRequest request) {
      return LoginAccount.ANONYMOUS;
   }
   
   /** Resolves the parameter 'sourceUri' to a stream and pipes that to the response */
   public void doGet(HttpServletRequest request,
                     HttpServletResponse response) throws ServletException, IOException {
      
      String sourceUri = request.getParameter("SourceUri");
      if (sourceUri != null) {
         try {
            SourceIdentifier source = SourceMaker.makeSource(sourceUri);
            HttpResponseTarget target = new HttpResponseTarget(response);
         
            Slinger.sling(source, target, getUser(request));
            
         }
         catch (URISyntaxException e) {
            throw new ServletException(e+", SourceUri="+sourceUri);
         }
      }
   }
}

