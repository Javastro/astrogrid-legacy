/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/security/manager/SecurityManagerMockDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:19 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityManagerMockDelegate.java,v $
 *   Revision 1.4  2004/06/18 13:45:19  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.3.52.2  2004/06/17 15:10:03  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.3.52.1  2004/06/17 13:38:58  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.security.manager ;

import org.astrogrid.community.common.security.manager.SecurityManagerMock ;

/**
 * Mock delegate for our SecurityManager service.
 *
 */
public class SecurityManagerMockDelegate
    extends SecurityManagerCoreDelegate
    implements SecurityManagerDelegate
    {
    /**
     * Public constructor.
     *
     */
    public SecurityManagerMockDelegate()
        {
        super() ;
        //
        // Set our SecurityManager service.
        this.setSecurityManager(
            new SecurityManagerMock()
            ) ;
        }
    }
