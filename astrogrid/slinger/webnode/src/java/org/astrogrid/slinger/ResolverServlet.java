
package org.astrogrid.slinger;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.Principal;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.slinger.Slinger;
import org.astrogrid.slinger.sources.SourceIdentifier;
import org.astrogrid.slinger.sourcetargets.UrlSourceTarget;
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
         SourceIdentifier source = new UrlSourceTarget(new URL(sourceUri));
         HttpResponseTarget target = new HttpResponseTarget(response);
      
         Slinger.sling(source, target);
      }
   }
}

