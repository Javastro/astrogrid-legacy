/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/security/manager/SecurityManagerMock.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityManagerMock.java,v $
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
 *   Revision 1.3.52.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.security.manager ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.community.common.service.CommunityServiceMock ;

/**
 * Mock implemetation for our SecurityManager service.
 *
 */
public class SecurityManagerMock
    extends CommunityServiceMock
    implements SecurityManager
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(SecurityManagerMock.class);

    /**
     * Public constructor.
     *
     */
    public SecurityManagerMock()
        {
        super() ;
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("SecurityManagerMock()") ;
        }

    /**
     * Set an account password.
     *
     */
    public boolean setPassword(String ident, String value)
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("SecurityManagerMock.setPassword()") ;
        log.debug("  Ident : " + ident) ;
        log.debug("  Value : " + value) ;
        //
        // Always return true.
        return true ;
        }

    }
