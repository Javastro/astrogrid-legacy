
package org.astrogrid.slinger;
import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.net.URISyntaxException;
import java.security.Principal;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.account.LoginAccount;
import org.astrogrid.io.Piper;
import org.astrogrid.slinger.mime.MimeTypes;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.slinger.targets.TargetMaker;

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
      
      try {
         TargetIdentifier target = TargetMaker.makeTarget(targetUri);
               
         StringReader reader = new StringReader(text);
         Writer writer = target.resolveWriter(user);
         
         Piper.bufferedPipe(reader, writer);
         
         reader.close();
         writer.close();
   
         response.getWriter().write("\nDone");
         response.getWriter().flush();
      }
      catch (URISyntaxException e) {
         throw new ServletException(e+", targetUri="+targetUri, e);
      }
   }
}

