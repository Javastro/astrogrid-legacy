/*$Id: CeaHelper.java,v 1.20 2009/06/09 13:12:12 gtr Exp $
 * Created on 20-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

import java.net.URI;
import java.net.URL;
import java.security.Security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.CeaService;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.AccessURL;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.applications.delegate.CEADelegateException;
import org.astrogrid.applications.delegate.CommonExecutionConnectorClient;
import org.astrogrid.applications.delegate.DelegateFactory;
import org.astrogrid.contracts.StandardIds;
import org.astrogrid.desktop.modules.auth.CommunityInternal;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.security.delegation.DelegationClient;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/** Helper object for working with cea services.
 *<p>
 *{@stickyNote it's purpose has evaporated. could roll into ceastrategryimpl instead now.}
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 20-Oct-2005
 */
public class CeaHelper {
  
    public static final Log log = LogFactory.getLog(CeaHelper.class);

    /** Construct a new RemoteCeaHelper
     * 
     */
    public CeaHelper(final Registry reg, final CommunityInternal community) {
        super();
        this.reg = reg;
        this.community = community;
        if (Security.getProvider("BC") == null) {
          Security.addProvider(new BouncyCastleProvider());
        }
    }
    private final CommunityInternal community;
    private final Registry reg;


    private boolean hasDigitalSignatureCredentials() {
        try {
          final SecurityGuard g = this.community.getSecurityGuard();
          g.getCertificateChain(); // throws if credentials are absent
          g.getPrivateKey(); // throws if credentials are absent
          return true;
        }
        catch (final Exception e) {
          return false;
        }
      }
    
    

/** create a delegate to connect to the server described in a resource information
 * delegate will be authenticated if the user is logged in.
 * @throws CEADelegateException 
 * @throws IllegalArgumentException if resource information does not prodvdide an access url */
    public CommonExecutionConnectorClient createCEADelegate(final CeaService server) throws CEADelegateException {
		if (server == null || server.getCapabilities().length == 0) { 
    		throw new IllegalArgumentException("Error: invalid resource information, couldn't find any service capabilities: " + server);
    	}
      URL endpoint = null;
      final Capability[] caps = server.getCapabilities();   
      for (int i = 0; i < caps.length; i++) {
         final URI standardID = caps[i].getStandardID(); 
         if (standardID != null && StandardIds.CEA_1_0.equals(standardID.toString())) {
            // We've found a CEA capability
            final Interface[] ints = caps[i].getInterfaces();
            if (ints.length == 0) {
               throw new IllegalArgumentException("Error: invalid resource information, no service interfaces for CEA capability: " + server);
            }
            for (int j = 0; j < ints.length; j++) {
               // Got some interfaces - let's use the first available 
               // access URL 
               // @todo What about proper round-robin/load-balanced use 
               // of access endpoints?
               final AccessURL[] urls = ints[j].getAccessUrls();
               // Just ignore interfaces with no access urls, for now
               // @todo - is this the best thing to do?
               if (urls.length > 0) {
                  endpoint = urls[0].getValue();
                  break;   // Can stop inner loop on URLs now
               }
            }
            // Inner break out to here
            if (endpoint != null) {
               break;   // Can stop outer loop on interfaces now
            }
         }
      }
      // If we get here with no endpoint set, the registrations are invalid
      if (endpoint == null) {
         throw new IllegalArgumentException("Error: Couldn't find CEA service endpoint for server in resource information: " + server);
      }
      // Now go ahead and make the delegate for the endpoint
      final CommonExecutionConnectorClient del = DelegateFactory.createDelegate(endpoint.toString());
      if (community.isLoggedIn() && hasDigitalSignatureCredentials()) {
          final SecurityGuard guard = this.community.getSecurityGuard();
          del.setCredentials(guard);
      }
      return del;
   }
    
    
  /**
   * Delegates the user's credentials to the service.
   * This uses the registered IVOA-delegation-service if such is known.
   *
   * @param service The registration details of the service.
   * @throws NotFoundException If the service has no delegation endpoint.
   * @throws ServiceException If the endpoint is present but delegation fails.
   */
  public void delegateCredentials(CeaService service) throws NotFoundException,
                                                             ServiceException {
      
    // Go through the registered capabilities of the service looking for delegation.
    URI endpoint = null;
    Capability[] capabilities = service.getCapabilities();
    for (int i = 0; i < capabilities.length; i++) {
      if (capabilities[i].getStandardID().toString().equals("ivo://ivoa.net/std/Delegation")) {
        Interface[] interfaces = capabilities[i].getInterfaces();
        if (interfaces.length > 0) {
          AccessURL[] urls = interfaces[0].getAccessUrls();
          if (urls.length > 0) {
            endpoint = urls[0].getValueURI();
            break;
          }
        }
      }
    }
    if (endpoint == null) {
      throw new NotFoundException(
        String.format("%s does not have a delegation endpoint", service.getId())
      );
    }
    
    // Delegate to the service the current user's credentials.
    DelegationClient client = 
        new DelegationClient(endpoint, community.getSecurityGuard());
    try {
      client.delegate();
      log.info(String.format("Credentials were delegated to %s.", service.getId()));
    }
    catch (Exception e) {
      log.warn("Failed to delegate credentials: " + e);
      throw new ServiceException("Failed to delegate credentials", e);
    }
  }
    


  
}
