/**
 * 
 */
package org.astrogrid.applications.authorization;

import java.security.GeneralSecurityException;

import org.apache.log4j.Logger;
import org.astrogrid.security.SecurityGuard;

/**
 * A {@link AuthorizationPolicy} that allows any access.
 * @author jl99
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 15 May 2009 - changed to {@link AuthorizationPolicy} & and gave a simple constructor.
 *
 */
public class NullPolicyDecisionPoint implements AuthorizationPolicy {
	
	private static final Logger log = Logger.getLogger( NullPolicyDecisionPoint.class ) ;
	
	public NullPolicyDecisionPoint(  ) {}

    public void decide(CEAOperation op, String ExecutionID,
            String applicationID, SecurityGuard caller)
            throws GeneralSecurityException {
       return; // just allow everything through.
    }
	

}
