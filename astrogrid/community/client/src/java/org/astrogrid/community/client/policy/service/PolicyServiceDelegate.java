/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/policy/service/PolicyServiceDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/19 14:43:14 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyServiceDelegate.java,v $
 *   Revision 1.3  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.2.16.1  2004/03/19 00:18:09  dave
 *   Refactored delegate Exception handling
 *
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/04 13:26:17  dave
 *   1) Added Delegate interfaces.
 *   2) Added Mock implementations.
 *   3) Added MockDelegates
 *   4) Added SoapDelegates
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.policy.service ;

import org.astrogrid.community.common.policy.data.PolicyPermission  ;
import org.astrogrid.community.common.policy.data.PolicyCredentials ;
import org.astrogrid.community.common.policy.service.PolicyService ;

import org.astrogrid.community.client.service.CommunityServiceDelegate ;

/**
 * Interface for our PolicyService delegate.
 * This mirrors the PolicyService interface, without the RemoteExceptions.
 *
 */
public interface PolicyServiceDelegate
    extends CommunityServiceDelegate
    {

    /**
     * Confirm access permissions.
     *
     */
    public PolicyPermission checkPermissions(PolicyCredentials credentials, String resource, String action) ;

    /**
     * Confirm group membership.
     *
     */
    public PolicyCredentials checkMembership(PolicyCredentials credentials) ;

    }
