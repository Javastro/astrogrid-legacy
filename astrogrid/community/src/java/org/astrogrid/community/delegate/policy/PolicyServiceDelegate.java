package org.astrogrid.community.delegate.policy;

import org.astrogrid.community.policy.data.PolicyCredentials;
import org.astrogrid.community.policy.data.PolicyPermission;

import org.astrogrid.community.policy.server.PolicyService ;
import org.astrogrid.community.policy.server.PolicyServiceService ;
import org.astrogrid.community.policy.server.PolicyServiceServiceLocator ;
import org.astrogrid.community.common.CommunityConfig;
import java.net.URL;


public class PolicyServiceDelegate {
   

   /**
    * Switch for our debug statements.
    *
    */
   private static final boolean DEBUG_FLAG = true ;

   /**
    *  service variable to our PolicyManager.
    */
   PolicyService service = null;
   
   PolicyPermission perm = null;
   
   /**
    * Public constructor deals with getting our service (link) to the webservice.
    *
    */
   public PolicyServiceDelegate() {
      try {
         service = getService(CommunityConfig.getServiceUrl());
      }catch(Exception e) {
         e.printStackTrace();
         service = null;
      }      
   }
   
   public boolean checkPermissions(String account,String group, String resource, String action) throws Exception {
      return checkPermissions(new PolicyCredentials(account,group),resource,action);
   }
   
   

   public boolean checkPermissions(PolicyCredentials credentials, String resource, String action) throws Exception {
      perm = service.checkPermissions(credentials,resource,action);   
      return perm.isValid();
   }
   
   public PolicyPermission getPolicyPermission() {
      return this.perm;
   }
   
   /**
     * Return our service object which is our link to the webservice.
     * @return
     * @throws Exception
     */
    private PolicyService getService(String targetEndPoint)
       throws Exception
       {
       if (DEBUG_FLAG) System.out.println("") ;
       if (DEBUG_FLAG) System.out.println("----\"----") ;
       if (DEBUG_FLAG) System.out.println("setUp()") ;

       PolicyServiceService locator = null;
       PolicyService service = null;
       //
       // Create our service locator.
       locator = new PolicyServiceServiceLocator();
      
       //
       // Create our service.
       service = locator.getPolicyService(new URL(targetEndPoint));

       if (DEBUG_FLAG) System.out.println("----\"----") ;
       if (DEBUG_FLAG) System.out.println("") ;
       return service;
    }   
}