/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/security/manager/SecurityManagerDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/19 14:43:14 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityManagerDelegate.java,v $
 *   Revision 1.4  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.3.14.1  2004/03/19 00:18:09  dave
 *   Refactored delegate Exception handling
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.security.manager ;

import org.astrogrid.community.client.service.CommunityServiceDelegate ;

/**
 * Public interface for our SecurityManager delegate.
 * This mirrors the SecurityManager interface, without the RemoteExceptions.
 *
 */
public interface SecurityManagerDelegate
    extends CommunityServiceDelegate
    {
    /**
     * Set an account password.
     * @param ident The Account identifier.
     * @param value The password.
     * @return true If the password was changed.
     *
     */
    public boolean setPassword(String ident, String value) ;

    }
