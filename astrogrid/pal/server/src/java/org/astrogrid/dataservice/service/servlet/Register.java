/*
 * $Id: Register.java,v 1.1 2005/02/17 18:37:35 mch Exp $
 */

package org.astrogrid.dataservice.service.servlet;
import org.astrogrid.webapp.*;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.metadata.VoDescriptionServer;
import org.astrogrid.dataservice.service.ServletHelper;
import org.astrogrid.registry.client.RegistryDelegateFactory;

/**
 * Calling this servlet sends the registry entry to the configured Registry
 *
 * @author mch
 */
public class Register extends DefaultServlet {
   
   public void doGet(HttpServletRequest request,
                     HttpServletResponse response) throws ServletException, IOException {

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
         LogFactory.getLog(request.getContextPath()).error(th+" Registering",th);
         doError(response, "Registering",th);
      }
   }
   
}
