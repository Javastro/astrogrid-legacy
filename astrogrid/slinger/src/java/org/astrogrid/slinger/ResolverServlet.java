
package org.astrogrid.slinger;
import org.astrogrid.account.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.Principal;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.io.Piper;
import org.astrogrid.slinger.sources.SourceIdentifier;
import org.astrogrid.slinger.sources.SourceMaker;

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
      
      String sourceUri = request.getParameter("sourceUri");
      if (sourceUri != null) {
         try
         {
            SourceIdentifier source = SourceMaker.makeSource(sourceUri);
            
            response.setContentType(source.getMimeType(getUser(request)));
            InputStream in = source.resolveInputStream(getUser(request));
            Piper.bufferedPipe(in, response.getOutputStream());
            in.close();
            response.getOutputStream().flush();
            
         }
         catch (URISyntaxException e) {
            throw new ServletException(e+", sourceUri="+sourceUri);
         }
      }
   }
}

