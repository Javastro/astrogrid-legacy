
package org.astrogrid.slinger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.Principal;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.io.mime.MimeTypes;
import org.astrogrid.io.piper.DefaultPipeListener;
import org.astrogrid.io.piper.PipeListener;
import org.astrogrid.io.piper.StreamPiper;
import org.astrogrid.slinger.sources.SourceIdentifier;
import org.astrogrid.slinger.sourcetargets.UrlSourceTarget;
import org.astrogrid.slinger.targets.TargetIdentifier;

/**
 * A servlet that takes source and target URIs and pipes them, sending status
 * info to response
 *
 * @author MCH
 */

public class CopyServlet extends HttpServlet
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
                     final HttpServletResponse response) throws ServletException, IOException {
      
      String sourceUri = request.getParameter("SourceUri");
      String targetUri = request.getParameter("TargetUri");
      
      if ((sourceUri == null) || (targetUri == null) || (sourceUri.trim().length() ==0)
         || (targetUri.trim().length() == 0) ) {
         
         response.sendError(response.SC_BAD_REQUEST, "TargetUri or SourceUri empty");
      }
      
      response.setContentType(MimeTypes.PLAINTEXT);

      Date start = new Date();
      
      SourceIdentifier source = new UrlSourceTarget(new URL(sourceUri));
      response.getWriter().write("Source: "+source+"\n");
      
      TargetIdentifier target = new UrlSourceTarget(new URL(targetUri));
      response.getWriter().write("Target: "+target+"\n");

      Principal user = getUser(request);
      response.getWriter().write("User: "+user.getName()+"\n");

      StreamPiper piper = new StreamPiper();
      response.getWriter().write("Piping, please wait (each dot is "+piper.getBlockSize()+" bytes)\n");

      InputStream in = source.openInputStream();
      OutputStream out = target.openOutputStream();

      PipeListener listener = new DefaultPipeListener() {
         public void blockPiped(long bytes) {
            try {
               response.getWriter().write(".");
            }
            catch (IOException e) { /* ignore - if there's a problem here it's too late */ }
         }
      };
      
      piper.pipe(in, out, listener);
      
      in.close();
      out.close();
      response.getWriter().write("\nDone in "+(new Date().getTime() - start.getTime())+"s");
      response.getWriter().flush();
         
   }
}

