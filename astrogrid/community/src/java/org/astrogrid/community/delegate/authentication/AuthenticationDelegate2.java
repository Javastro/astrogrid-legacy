package org.astrogrid.community.delegate.authentication;

//import org.astrogrid.community.policy.data.PolicyCredentials;

import org.astrogrid.community.authentication.server.AuthenticationManager ;
import org.astrogrid.community.authentication.server.AuthenticationManagerService ;
import org.astrogrid.community.authentication.server.AuthenticationManagerLocator ;


public class AuthenticationDelegate2 {
   
   
   /**
    * Switch for our debug statements.
    *
    */
   private static final boolean DEBUG_FLAG = true ;
   
   private static final String localTarget = "local";
   
   private String communityURL = "local";
   
   public void setCommunityURL(String url) {
      this.communityURL = url; 
   }
   
   public String getCommunityURL() {
      return this.communityURL;
   }
   
   public String authenticateToken(String account, String token) {
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
   public String authenticateToken(String account,String token,String target) {
      AuthenticationManager am = getAuthenticationManager(this.communityURL);
      return am.authenticateToken(account,token,target);
   }
   
   
   /**
    * Setup our tests.
    *
    */
   protected AuthenticationManager getAuthenticationManager(String url)
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
      locator.setEndPoint(url);
      //
      // Create our service.
      service = locator.getGroupManager();

      if (DEBUG_FLAG) System.out.println("----\"----") ;
      if (DEBUG_FLAG) System.out.println("") ;
   }

   
}