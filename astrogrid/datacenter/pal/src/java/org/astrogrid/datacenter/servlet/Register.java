/*
 * $Id: Register.java,v 1.4 2004/10/25 13:14:19 jdt Exp $
 */

package org.astrogrid.datacenter.servlet;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.metadata.VoDescriptionServer;
import org.astrogrid.datacenter.service.ServletHelper;
import org.astrogrid.registry.client.RegistryDelegateFactory;

/**
 * Calling this servlet sends the registry entry to the configured Registry
 *
 * @author mch
 */
public class Register extends StdServlet {
   
   public void doGet(HttpServletRequest request,
                     HttpServletResponse response) throws ServletException, IOException {

      ServletHelper.setUrlStem(request);
      
      try {
         String regParam = request.getParameter("RegistryUrl");
         if ((regParam == null) || (regParam.trim().length()==0)) {
            regParam =  SimpleConfig.getSingleton().getString(RegistryDelegateFactory.ADMIN_URL_PROPERTY);
         }

         VoDescriptionServer.pushToRegistry(new URL(regParam));
         
         //submit query - and return just the query ID
         response.setContentType("text/plain");
         Writer w = response.getWriter();
         w.write(regParam);
      }
      catch (Throwable th) {
         LogFactory.getLog(request.getContextPath()).error(th);
         doError(response, "Registering",th);
      }
   }
   
}
