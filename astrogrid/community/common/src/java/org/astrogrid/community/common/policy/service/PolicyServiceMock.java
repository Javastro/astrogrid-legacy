/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/service/PolicyServiceMock.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyServiceMock.java,v $
 *   Revision 1.5  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.4.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.4  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.3.52.2  2004/06/17 14:50:03  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.3.52.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.service ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.community.common.policy.data.PolicyPermission  ;
import org.astrogrid.community.common.policy.data.PolicyCredentials ;

import org.astrogrid.community.common.service.CommunityServiceMock ;

public class PolicyServiceMock
    extends CommunityServiceMock
    implements PolicyService
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(PolicyServiceMock.class);

    /**
     * Public constructor.
     *
     */
    public PolicyServiceMock()
        {
        super() ;
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PolicyServiceMock()") ;
        }

    /**
     * Confirm access permissions.
     *
     */
    public PolicyPermission checkPermissions(PolicyCredentials credentials, String resource, String action)
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PolicyServiceMock.checkPermissions()") ;
        log.debug("  Credentials : " + credentials) ;
        log.debug("  Resource    : " + resource) ;
        log.debug("  Action      : " + action) ;
        //
        // TODO - return something useful.
        return null ;
        }

    /**
     * Confirm group membership.
     *
     */
    public PolicyCredentials checkMembership(PolicyCredentials credentials)
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PolicyServiceMock.checkMembership()") ;
        log.debug("  Credentials : " + credentials) ;
        //
        // TODO - return something useful.
        return null ;
        }
    }
