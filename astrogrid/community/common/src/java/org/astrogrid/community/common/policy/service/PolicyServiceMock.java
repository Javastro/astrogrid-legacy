/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/service/PolicyServiceMock.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/08 13:42:33 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyServiceMock.java,v $
 *   Revision 1.3  2004/03/08 13:42:33  dave
 *   Updated Maven goals.
 *   Replaced tabs with Spaces.
 *
 *   Revision 1.2.2.1  2004/03/08 12:53:17  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.2  2004/03/05 14:03:23  dave
 *   Added first client side SOAP test - SecurityServiceSoapDelegateTestCase
 *
 *   Revision 1.1.2.1  2004/03/04 16:09:37  dave
 *   Added PolicyService delegates
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.service ;

import java.util.Map ;
import java.util.HashMap ;

import org.astrogrid.community.common.policy.data.PolicyPermission  ;
import org.astrogrid.community.common.policy.data.PolicyCredentials ;

import org.astrogrid.community.common.service.CommunityServiceMock ;

public class PolicyServiceMock
    extends CommunityServiceMock
    implements PolicyService
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Public constructor.
     *
     */
    public PolicyServiceMock()
        {
        super() ;
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyServiceMock()") ;
        }

    /**
     * Confirm access permissions.
     *
     */
    public PolicyPermission checkPermissions(PolicyCredentials credentials, String resource, String action)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyServiceMock.checkPermissions()") ;
        if (DEBUG_FLAG) System.out.println("  Credentials : " + credentials) ;
        if (DEBUG_FLAG) System.out.println("  Resource    : " + resource) ;
        if (DEBUG_FLAG) System.out.println("  Action      : " + action) ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyServiceMock.checkMembership()") ;
        if (DEBUG_FLAG) System.out.println("  Credentials : " + credentials) ;
        //
        // TODO - return something useful.
        return null ;
        }
    }
