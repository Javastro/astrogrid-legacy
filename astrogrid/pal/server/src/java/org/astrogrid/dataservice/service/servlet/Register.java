/*
 * $Id: Register.java,v 1.9 2007/12/04 17:31:39 clq2 Exp $
 */

package org.astrogrid.dataservice.service.servlet;
import org.astrogrid.webapp.*;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.metadata.VoDescriptionServer;
import org.astrogrid.dataservice.service.ServletHelper;
import org.astrogrid.registry.client.RegistryDelegateFactory;
//import org.astrogrid.registry.client.DelegateProperties;
//import org.astrogrid.registry.client.query.RegistryService;
//import org.astrogrid.registry.client.query.v1_0.RegistryService;
import org.astrogrid.cfg.PropertyNotFoundException;

import org.w3c.dom.Document;
import org.astrogrid.xml.DomHelper;

/**
 * Calling this servlet sends the registry entry to the configured Registry
 *
 * @author mch
 */
public class Register extends DefaultServlet {
   
   /** Registers this DSA to the specified registry endpoint.
    * Servlet version */
   public void doGet(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

      try {
         String regParam = request.getParameter("RegistryUrl");
         Writer w = response.getWriter();
         /*
         for (int i = 0; i < VoDescriptionServer.VERSIONS.length; i++) {
         //for (int i = 1; i < 2; i++) {
            if (VoDescriptionServer.isEnabled(
                  VoDescriptionServer.VERSIONS[i])) {
                w.write("\nPushing registration for version " +
                    VoDescriptionServer.VERSIONS[i] + ":\n");
                VoDescriptionServer.pushToRegistry(
                    VoDescriptionServer.VERSIONS[i], new URL(regParam));
              //submit query - and return just the query ID
                response.setContentType("text/plain");
                w.write(regParam);
            }
         }
      */
         String results = doRegistration(regParam);
         w.write(results);
      }
      catch (Throwable th) {
         LogFactory.getLog(request.getContextPath()).error(th+" Registering",th);
         doError(response, "Registering",th);
      }
   }
   
   /** Registers this DSA to the specified registry endpoint.
    * Non-servlet version */
   public String doRegistration(String registryURL) 
      throws IOException
   {
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

      if ((registryURL == null) || (registryURL.trim().length()==0)) {
         try {
             registryURL =  ConfigFactory.getCommonConfig().getString(RegistryDelegateFactory.ADMIN_URL_PROPERTY);
         }
         catch (PropertyNotFoundException pnfe) {
            //Shouldn't get here if self-tests ok
            throw new IOException("Configuration error: Property " +
                  RegistryDelegateFactory.ADMIN_URL_PROPERTY +
                  " is not set, please set it!"); 
         }
      }
      //We need to check that the publishing registry recognises our
      //authority ID. 
      //First, construct publishing registry's query url
      //NASTY HACK!!
      String queryRegUrl = "";
      int tailIndex = registryURL.lastIndexOf("RegistryUpdate");
      if (tailIndex == -1) {
         throw new IOException("Programming logic error: Expected property " +
               RegistryDelegateFactory.ADMIN_URL_PROPERTY +
               " to contain substring 'RegistryUpdate', but it doesn't.");
      }
      queryRegUrl = registryURL.substring(0,tailIndex)+"RegistryQuery";
      //Get authority ID
      String authorityID = "";
      try {
        authorityID=  ConfigFactory.getCommonConfig().getString(
              "datacenter.authorityId");
      }
      catch (PropertyNotFoundException pnfe) {
         //Shouldn't get here if self-tests ok
         throw new IOException(
               "Configuration error: Property datacenter.authorityId" +
               " is not set, please set it!"); 
      }
      StringBuffer stringBuffer = new StringBuffer();
      for (int i = 0; i < VoDescriptionServer.VERSIONS.length; i++) {
         String version = VoDescriptionServer.VERSIONS[i];
         if (VoDescriptionServer.isEnabled(version)) {

            stringBuffer.append("\n<h2>Registration results for version " +
                 version + "</h2>\n");

            // Get querier to check for authority ID
            HashMap hm = null;
            try {
               if (version.equals(VoDescriptionServer.V0_10)) {
                  org.astrogrid.registry.client.query.RegistryService rs = 
                      RegistryDelegateFactory.createQuery(
                        new java.net.URL(queryRegUrl));
                  hm = rs.managedAuthorities();
               }
               else if (version.equals(VoDescriptionServer.V1_0)) {
                  org.astrogrid.registry.client.query.v1_0.RegistryService rs = 
                     RegistryDelegateFactory.createQueryv1_0(
                      new java.net.URL(queryRegUrl+"v1_0"));
                  hm = rs.managedAuthorities();
               }
               else {
               }
            }
            catch (Exception e) {
               stringBuffer.append(
                   "<p><font size=\"+1\"color=\"red\">" +
                   "<b>Registration failed</b></font>" +
                   " using registry endpoint '" + 
                    registryURL + "'...</p>");
               stringBuffer.append("<p><b>The cause was:</b><br/>" + 
                     e.getMessage()+ "</p>");
            }

            // Now do registration
            try {
               // Check for authority ID
               if (!hm.containsKey(authorityID)) {
                  throw new IOException("The authority ID " + 
                        authorityID  + 
                    " is not currently managed by the registry at " +
                    registryURL +
                   ".<br/>This means you cannot self-register your DSA on that registry.<br/>If this a new authority ID, you will need to configure the specified registry to manage it.<br/>Please consult your registry administrator for further advice.");
               }
               VoDescriptionServer.pushToRegistry(
                     version, new URL(registryURL));
               stringBuffer.append(
                   "<p><font size=\"+1\"color=\"green\">" +
                   "<b>Registration succeeded</b></font>" +
                   " using registry endpoint '" + 
                    registryURL + "'...</p>");
            }
            catch (Exception e) {
               stringBuffer.append(
                   "<p><font size=\"+1\"color=\"red\">" +
                   "<b>Registration failed</b></font>" +
                   " using registry endpoint '" + 
                    registryURL + "'...</p>");
               stringBuffer.append("<p><b>The cause was:</b><br/>" + 
                     e.getMessage()+ "</p>");
            }
         }
      }
      return stringBuffer.toString();
   }
}
