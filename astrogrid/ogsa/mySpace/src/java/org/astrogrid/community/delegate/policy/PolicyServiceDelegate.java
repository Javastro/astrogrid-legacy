package org.astrogrid.community.delegate.policy;

/**
 * Fake <code>PolicyServiceDelegate</code>.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 3.
 *
 */

public class PolicyServiceDelegate
{
   public PolicyServiceDelegate ()
   {  System.out.println("Created fake PolicyServiceDelegate.");
   }

   public boolean checkPermissions(String userId, String credentials,
     String resource, String oper)
   {  return true;
   }
}
