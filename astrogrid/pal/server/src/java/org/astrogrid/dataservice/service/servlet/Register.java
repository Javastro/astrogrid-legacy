/*
 * $Id: Register.java,v 1.4 2006/09/26 06:54:10 clq2 Exp $
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
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.metadata.VoDescriptionServer;
import org.astrogrid.dataservice.service.ServletHelper;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.DelegateProperties;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.dataservice.metadata.v0_10.VoResourceSupport;

import org.w3c.dom.Document;
import org.astrogrid.xml.DomHelper;

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
            regParam =  ConfigFactory.getCommonConfig().getString(DelegateProperties.ADMIN_URL_PROPERTY);
         }
         /*
         // KEA TOFIX COMPLETE THIS REGISTRY CHECK IN BRANCH FOLLOWING 
         // PAL_KEA_1716
         // Check the status of the Authority ID in the publishing registry.
         // If it doesn't exist, we need to create it.
         // If it does exist, it needs to be managed by the publishing 
         // registry and not some other registry.
         
         // Need to look for ivorn 'ivo://{authID}/authority'
         // If it exists, this ivorn needs to have the ivo-id attribute
         // in the <managingOrg> tag set to the same ivorn, e.g.
         // <managingOrg ivo-id="ivo://{authID}/authority">blah</managingOrg>
         String authIDIvorn = VoResourceSupport.makeAuthorityId();
         System.out.println("AUTH ID IS " + authIDIvorn);
         authIDIvorn = "ivo://org.astrogrid.test1+authority";
         System.out.println("AUTH ID IS NOW " + authIDIvorn);

         //String publishRegQuery = regParam.
         //URL publishRegistry = new URL(regParam); 

         RegistryService service = 
             RegistryDelegateFactory.createQuery();
         Document registration = service.getResourceByIdentifier(authIDIvorn);
         System.out.println("REG AUTH ID IS :");
         System.out.println(DomHelper.DocumentToString(registration));
         */
         
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
