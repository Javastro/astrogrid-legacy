package org.astrogrid.applications.authorization;

import java.security.GeneralSecurityException;
import java.security.Principal;

import org.apache.log4j.Logger;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.description.execution.ExecutionSummaryType;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.security.SecurityGuard;

/**
 * An AccessPolicy for CEA job that requires the caller to authenticate an
 * identity.
 * 
 * The policy distinguishes operations that start a job (e.g. init in the the
 * CEC interface) from those that address an existing job. It does not use the
 * operation name, but instead looks for the states owner of the operation - the
 * request-attribute cea.job.owner. If this is present, then it is assumed to be
 * the principal of the owner of an existing job, and the policy requires that
 * the caller have the same principal. If the attribute is absent, the operation
 * is assumed to be creating a new job. In this case, no further checks are made
 * in this class: all such requests succeed. However, sub-classes may add
 * constraints by over-riding the {@link discriminate} method.
 * 
 * @author Guy Rixon
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 15 May 2009
 */
public class CeaAuthenticatedAccessPolicy implements AuthorizationPolicy {

    private static Logger logger = Logger
    .getLogger(CeaAuthenticatedAccessPolicy.class);
    private final ExecutionHistory eh;

    CeaAuthenticatedAccessPolicy(ExecutionHistory executionHistory) {
        eh = executionHistory;
    }

    public void decide(CEAOperation op, String ExecutionID,
            String applicationID, SecurityGuard guard)
    throws GeneralSecurityException {
        // All callers must be authenticated.
        Principal caller = guard.getX500Principal();
        if (caller == null) {
            throw new GeneralSecurityException("Caller is not authenticated.");
        }
        if (logger.isDebugEnabled())
            logger.debug("caller = " + caller.toString());
 

        if(op != CEAOperation.INIT)
        {
            // If this is a return to an on-going execution, only the party that
            // started
            // execution can view or steer it. Therefore we have to have the same
            // principal as that party.

            if(eh.isApplicationInCurrentSet(ExecutionID))
            {

                Application app;
                try {
                    app = eh.getApplicationFromCurrentSet(ExecutionID);
                } catch (CeaException e) {
                    throw new GeneralSecurityException("problem retrieving job="+ExecutionID, e);
                }
                Principal owner = (Principal) app.getSecurityGuard().getX500Principal();
                if (owner != null) {
                    if (!owner.equals(caller)) {
                        throw new GeneralSecurityException(caller.getName()
                                + " tried to use a job owned by " + owner.getName());
                    }
                }
            } else {
                try {
                    ExecutionSummaryType summ = eh.getApplicationFromArchive(ExecutionID);
                } catch (CeaException e) {
                    throw new GeneralSecurityException("problem retrieving job="+ExecutionID, e);
                }
                //FIXME anyone allowed to look at old things at the moment - need to put owner into the UWS schema.
            }
        }

        // If this is a new execution, then we may need to restrict access based
        // on the caller's identity.
        else {
            this.discriminate(caller, op, applicationID);
        }

    }

    public void discriminate(Principal caller, CEAOperation op,
            String applicationID) throws SecurityException,
            GeneralSecurityException {
        // No checks in this class. Over-ride this in sub-classes to
        // discriminate
        // by identity.
    }

}
