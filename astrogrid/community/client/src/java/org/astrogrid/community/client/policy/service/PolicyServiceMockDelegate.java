/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/policy/service/PolicyServiceMockDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/08 13:42:33 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyServiceMockDelegate.java,v $
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
 *   Revision 1.1.2.1  2004/03/04 16:09:37  dave
 *   Added PolicyService delegates
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.policy.service ;

import org.astrogrid.community.common.policy.data.PolicyPermission  ;
import org.astrogrid.community.common.policy.data.PolicyCredentials ;

import org.astrogrid.community.common.policy.service.PolicyService ;
import org.astrogrid.community.common.policy.service.PolicyServiceMock ;

import org.astrogrid.community.common.service.data.ServiceStatusData ;

/**
 * Mock delegate for our PolicyService service.
 *
 */
public class PolicyServiceMockDelegate
    extends PolicyServiceCoreDelegate
    implements PolicyServiceDelegate
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
    public PolicyServiceMockDelegate()
        {
        super() ;
        //
        // Set our PolicyService service.
        this.setPolicyService(
            new PolicyServiceMock()
            ) ;
        }
    }
