package org.astrogrid.community.delegate.authentication;

//import org.astrogrid.community.policy.data.PolicyCredentials;

import org.astrogrid.community.auth.server.AuthenticationManager ;
import org.astrogrid.community.auth.server.AuthenticationManagerService ;
import org.astrogrid.community.auth.server.AuthenticationManagerServiceLocator ;

import java.net.URL;

public class AuthenticationDelegate2 {
   
   
   /**
    * Switch for our debug statements.
    *
    */
   private static final boolean DEBUG_FLAG = true ;
   
   private static final String localTarget = "local";
   
   private String communityURL = "local";
   
   private AuthenticationManager service = null;
   
   public AuthenticationDelegate2() {
      try {
         service = getService(communityURL);
      }catch(Exception e) {
         e.printStackTrace();
         service = null;
      }      
   }   
   
   public void setCommunityURL(String url) {
      this.communityURL = url; 
   }
   
   public String getCommunityURL() {
      return this.communityURL;
   }
   
   public String authenticateToken(String account, String token) throws Exception {
      return authenticateToken(account,token,localTarget);
   }
  /*    
   public String authenticateToken(String token,PolicyCredentials polCredentials) {
      return authenticateToken(polCredentials.getAccount(),token,polCredentials.getGroup());
   }

   */
   
   /**
    * Service health check.
    *
    */
   public String authenticateToken(String account,String token,String target) throws Exception {
      return service.authenticateToken(account,token,target);
   }
   
   
   /**
    * Setup our tests.
    *
    */
   protected AuthenticationManager getService(String url)
      throws Exception
      {
      if (DEBUG_FLAG) System.out.println("") ;
      if (DEBUG_FLAG) System.out.println("----\"----") ;
      if (DEBUG_FLAG) System.out.println("setUp()") ;

      AuthenticationManagerService locator = null;
      AuthenticationManager service = null;
      //
      // Create our service locator.
      locator = new AuthenticationManagerServiceLocator();

      //
      // Create our service.
      service = locator.getauthService(new URL(url));

      if (DEBUG_FLAG) System.out.println("----\"----") ;
      if (DEBUG_FLAG) System.out.println("") ;
      return service;
   }   
}