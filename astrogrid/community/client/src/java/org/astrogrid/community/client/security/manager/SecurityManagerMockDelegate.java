/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/security/manager/SecurityManagerMockDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityManagerMockDelegate.java,v $
 *   Revision 1.5  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.4.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
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

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

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
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(SecurityManagerMockDelegate.class);

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
