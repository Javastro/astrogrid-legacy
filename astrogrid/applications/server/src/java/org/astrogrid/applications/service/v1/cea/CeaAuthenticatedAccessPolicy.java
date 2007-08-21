package org.astrogrid.applications.service.v1.cea;

import java.security.GeneralSecurityException;
import java.security.Principal;
import java.util.Map;
import org.apache.log4j.Logger;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.security.authorization.AccessPolicy;

/**
 * An AccessPolicy for CEA job that requires the caller to 
 * authenticate an identity.
 *
 * The policy distinguishes operations that start a job (e.g. init in the
 * the CEC interface) from those that address an existing job. It does not
 * use the operation name, but instead looks for the states owner of the
 * operation - the request-attribute cea.job.owner. If this is present, then
 * it is assumed to be the principal of the owner of an existing job, and the
 * policy requires that the caller have the same principal. If the attribute
 * is absent, the operation is assumed to be creating a new job. In this case,
 * no further checks are made in this class: all such requests succeed.
 * However, sub-classes may add constraints by over-riding the 
 * {@link discriminate} method.
 *
 * @author Guy Rixon
 */
public class CeaAuthenticatedAccessPolicy implements AccessPolicy {
  
  private static Logger log = Logger.getLogger(CeaAuthenticatedAccessPolicy.class);
  
  public Map decide(SecurityGuard guard, Map request)
      throws GeneralSecurityException, Exception  {
    
    // All callers must be authenticated.
    Principal caller = guard.getX500Principal();
    if (caller == null) {
      throw new GeneralSecurityException("Caller is not authenticated.");
    }
    
    // If this is a return to an on-going execution, only the party that started
    // execution can view or steer it. Therefore we have to have the same
    // principal as that party.
    Principal owner = (Principal) request.get("cea.job.owner");
    if (owner != null) {
      if (!owner.equals(caller)) {
        throw new GeneralSecurityException(
          caller.getName() +
          " tried to use a job owned by " +
          owner.getName()
        );
      }
    }
    
    // If this is a new execution, then we may need to restrict access based
    // on the caller's identity.
    else {
      this.discriminate(caller, request);
    }
    
    return request;
  }
  
  public void discriminate(Principal caller, Map request) 
      throws SecurityException, GeneralSecurityException, Exception {
    // No checks in this class. Over-ride this in sub-classes to discriminate
    // by identity.
  }
  
}
