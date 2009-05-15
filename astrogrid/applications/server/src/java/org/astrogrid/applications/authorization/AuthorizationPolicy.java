package org.astrogrid.applications.authorization;

import java.security.GeneralSecurityException;

import org.astrogrid.security.SecurityGuard;

/**
 * CEA Authorization policy.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 6 May 2009
 * @version $Name:  $
 * @since AIDA Stage 1
 */
public interface AuthorizationPolicy {
	/**
	 * Decide whether a particular operation is to be allowed for a particular user represed by the caller.
	 * @param op Operation to be authorized.
	 * @param ExecutionID The executionID of the job - null if the job has not yet been created.
	 * @param applicationID The application URI.
	 * @param caller The SecurityGuard for the caller. //IMPL better to just pass the Principal?
	 * @throws GeneralSecurityException
	 */
	public void decide(CEAOperation op, String executionID, String applicationID, SecurityGuard caller) throws GeneralSecurityException;
}
