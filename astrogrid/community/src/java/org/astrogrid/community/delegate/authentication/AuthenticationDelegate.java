/*
 * $Id: AuthenticationDelegate.java,v 1.5 2003/11/06 15:35:26 dave Exp $
 * 
 * Created on 10-Sep-2003 by pah
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.community.delegate.authentication;

import java.net.URL ;
import java.net.MalformedURLException ;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.astrogrid.community.common.CommunityConfig;

import org.astrogrid.community.delegate.authentication.TokenAuthenticator;
import org.astrogrid.community.service.authentication.data.SecurityToken;

/**
 * First Attempt delegate to hide some of the token authenticator soap stuff. 
 * A better delegate would hide more and be more tuned towards the needs of its users.
 * 
 * @author pah
 * @version $Name:  $
 * @since iteration3
 */
public class AuthenticationDelegate implements TokenAuthenticator {

    /**
     * Switch for our debug statements.
     *
     */
    private static final boolean DEBUG_FLAG = true ;

    /**
     * An internal reference to our SOAP stub.
     *
     */
    private AuthenticationServiceSoapBindingStub binding;

    /**
     * Public constructor, automatically generates the SOAP stub based on the URL settings from the local configuration.
     *
     */
    public AuthenticationDelegate()
        {
        initService() ;
//
// Not sure we need to specify this.
// If we do, then it should be configurable.
//        binding.setTimeout(60000);
        }

    /**
     * Initialise our SOAP stub based on the URL settings from the local configuration.
     *
     */
    private void initService()
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AuthenticationDelegate.initService()") ;

        //
        // Try reading the service URL from the local config.
        String address = CommunityConfig.getAuthenticationServiceUrl() ;
        //
        // Initialise the SOAP stub.
        initService(address) ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Initialise our SOAP stub.
     *
     */
    private void initService(String address)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AuthenticationDelegate.initService()") ;
        if (DEBUG_FLAG) System.out.println("  Address : '" + address + "'") ;

        try {
            //
            // Create our service locator.
            TokenAuthenticatorServiceLocator locator = new TokenAuthenticatorServiceLocator();

            //
            // If we have a specific service url.
            if ((null != address) && (address.length() > 0))
                {
                //
                // Use the service url to create our stub.
                binding = (AuthenticationServiceSoapBindingStub) locator.getAuthenticationService(new URL(address));
                }
            //
            // If we don't have a specific service url.
            else {
                //
                // Create a default (localhost) stub.
                binding = (AuthenticationServiceSoapBindingStub) locator.getAuthenticationService();
                }
            }
        catch (MalformedURLException ouch)
            {
            if (DEBUG_FLAG) System.out.println("") ;
            if (DEBUG_FLAG) System.out.println("MalformedURLException while trying to locate the AuthenticationServiceSoapBindingStub") ;
            if (DEBUG_FLAG) System.out.println("") ;
            binding = null ;
            }

        catch (ServiceException ouch)
            {
            if (DEBUG_FLAG) System.out.println("") ;
            if (DEBUG_FLAG) System.out.println("ServiceException while trying to locate the AuthenticationServiceSoapBindingStub") ;
            if (ouch.getLinkedCause() != null)
                {
                ouch.getLinkedCause().printStackTrace();
                }
            binding = null ;
            if (DEBUG_FLAG) System.out.println("") ;
            }
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }


   /* (non-Javadoc)
    * @see org.astrogrid.community.service.authentication.TokenAuthenticator#authenticateLogin(java.lang.String, java.lang.String)
    */
   public SecurityToken authenticateLogin(String account, String password)
      throws RemoteException {
/*
 * Temp fix to allow other teams to use the portal.
 * Need to mofiy the code to enable portal to use a remote community service.
 *
         SecurityToken token =
         binding.authenticateLogin(account, password);
        return token;
 *
 */
   SecurityToken token = new SecurityToken() ;
   token.setAccount(account) ;
   token.setUsed(Boolean.FALSE) ;
   return token ;
   }

   /* (non-Javadoc)
    * @see org.astrogrid.community.service.authentication.TokenAuthenticator#authenticateToken(org.astrogrid.community.service.authentication.data.SecurityToken)
    */
   public SecurityToken authenticateToken(SecurityToken token)
      throws RemoteException {
/*
 * Temp fix to allow other teams to use the portal.
 * Need to mofiy the code to enable portal to use a remote community service.
 *
         SecurityToken rettoken = binding.authenticateToken(token);
         return rettoken;
 *
 */
   return token ;
   }

   /* (non-Javadoc)
    * @see org.astrogrid.community.service.authentication.TokenAuthenticator#createToken(java.lang.String, org.astrogrid.community.service.authentication.data.SecurityToken, java.lang.String)
    */
   public SecurityToken createToken(
      String account,
      SecurityToken token,
      String target) {
/*
 * Temp fix to allow other teams to use the portal.
 * Need to mofiy the code to enable portal to use a remote community service.
 *
      throws RemoteException {
        SecurityToken rettoken = binding.createToken(account, token, target);
        return rettoken;
 *
 */
   return token ;
   }

}
