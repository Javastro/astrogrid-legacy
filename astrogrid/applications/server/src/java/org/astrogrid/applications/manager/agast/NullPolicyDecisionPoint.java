/**
 * 
 */
package org.astrogrid.applications.manager.agast;

import java.security.GeneralSecurityException;
import java.util.Map;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.security.authorization.AccessPolicy;
import org.apache.log4j.Logger ;

/**
 * @author jl99
 *
 */
public class NullPolicyDecisionPoint implements AccessPolicy {
	
	private static final Logger log = Logger.getLogger( NullPolicyDecisionPoint.class ) ;
	
	public NullPolicyDecisionPoint( String qadiUrl ) {}
	
	/* (non-Javadoc)
	 * @see org.astrogrid.security.authorization.AccessPolicy#decide(org.astrogrid.security.SecurityGuard, java.util.Map)
	 */
	public Map decide( SecurityGuard securityGuard, Map request ) 
	                   throws SecurityException,
			                  GeneralSecurityException, 
			                  Exception {
		return null ;
		
	}


}
