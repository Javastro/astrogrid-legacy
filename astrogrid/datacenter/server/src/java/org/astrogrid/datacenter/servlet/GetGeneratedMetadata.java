/*
 * $Id: GetGeneratedMetadata.java,v 1.1 2004/09/02 08:02:17 mch Exp $
 */

package org.astrogrid.datacenter.servlet;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.datacenter.metadata.MetadataInitialiser;
import org.astrogrid.util.DomHelper;

/**
 * A servlet for returning the generated metadata
 *
 * @author mch
 */
public class GetGeneratedMetadata extends StdServlet {
   
   public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

      try {
         response.setContentType("text/xml");
         response.getWriter().write(DomHelper.DocumentToString(MetadataInitialiser.generateMetadata()));
      }
      catch (Throwable th) {
         doError(response, "Generating Metadata",th);
      }
   }


}
