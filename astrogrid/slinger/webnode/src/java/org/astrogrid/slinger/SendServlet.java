
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
import org.astrogrid.io.mime.MimeTypes;
import org.astrogrid.slinger.Slinger;
import org.astrogrid.slinger.sources.StringSource;
import org.astrogrid.slinger.sourcetargets.UrlSourceTarget;
import org.astrogrid.slinger.targets.TargetIdentifier;

/**
 * A servlet that takes a URI and attempts to resolve it to a stream,
 * piping that stream to the response
 *
 * @author MCH
 */

public class SendServlet extends HttpServlet
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
      
      String targetUri = request.getParameter("TargetUri");
      
      if ( (targetUri == null) || (targetUri.trim().length() == 0) ) {
         response.sendError(response.SC_BAD_REQUEST, "TargetUri  empty");
      }
      
      String text = request.getParameter("Text");
      if (text == null) {
         response.sendError(response.SC_BAD_REQUEST, "'Text' is  empty");
      }
      
      Principal user = getUser(request);
      
      response.setContentType(MimeTypes.PLAINTEXT);
      response.getWriter().write("Sending '"+text+"' to "+targetUri+" for user "+user.getName());
      
      TargetIdentifier target = new UrlSourceTarget(new URL(targetUri));

      StringSource source = new StringSource(text);
      Slinger.sling(source, target);

      response.getWriter().write("\nDone");
      response.getWriter().flush();
   }
}

