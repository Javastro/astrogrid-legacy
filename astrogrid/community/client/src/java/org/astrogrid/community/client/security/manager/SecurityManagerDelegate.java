/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/security/manager/SecurityManagerDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/05 17:19:59 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityManagerDelegate.java,v $
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/04 08:57:10  dave
 *   Started work on the install xdocs.
 *   Started work on the Security delegates.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.security.manager ;

import org.astrogrid.community.common.service.data.ServiceStatusData ;

import org.astrogrid.community.common.security.manager.SecurityManager ;

/**
 * Public interface for our SecurityManager delegate.
 * This extends the SecurityManager interface, without the RemoteExceptions.
 *
 */
public interface SecurityManagerDelegate
	extends SecurityManager
    {
    /**
     * Set an account password.
     * @param ident - The Account identifier.
     * @param value - The password.
     * @return True if the password was changed.
     *
     */
    public boolean setPassword(String ident, String value) ;

    /**
     * Service health check.
     * @return ServiceStatusData with details of the Service status.
     *
     */
    public ServiceStatusData getServiceStatus() ;

    }
