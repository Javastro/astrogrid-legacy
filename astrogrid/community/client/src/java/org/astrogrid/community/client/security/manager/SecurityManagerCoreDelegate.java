/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/security/manager/SecurityManagerCoreDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/08 13:42:33 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityManagerCoreDelegate.java,v $
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
 *   Revision 1.1.2.2  2004/03/04 16:09:37  dave
 *   Added PolicyService delegates
 *
 *   Revision 1.1.2.1  2004/03/04 08:57:10  dave
 *   Started work on the install xdocs.
 *   Started work on the Security delegates.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.security.manager ;

import java.rmi.RemoteException ;

import org.astrogrid.community.common.service.data.ServiceStatusData ;

import org.astrogrid.community.common.security.manager.SecurityManager ;

/**
 * The core delegate code for our SecurityManager service.
 * This acts as a wrapper for a SecurityManager service, and handles any RemoteExceptions internally.
 *
 */
public class SecurityManagerCoreDelegate
    implements SecurityManagerDelegate
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
    public SecurityManagerCoreDelegate()
        {
        }

    /**
     * Our SecurityManager service.
     *
     */
    private SecurityManager manager = null ;

    /**
     * Get a reference to our SecurityManager service.
     *
     */
    protected SecurityManager getSecurityManager()
        {
        return this.manager ;
        }

    /**
     * Set our our SecurityManager service.
     *
     */
    protected void setSecurityManager(SecurityManager manager)
        {
        this.manager = manager ;
        }

    /**
     * Set an account password.
     * @param ident - The Account identifier.
     * @param value - The password.
     * @return True if the password was changed.
     *
     */
    public boolean setPassword(String ident, String value)
        {
        boolean result = false ;
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                result = this.manager.setPassword(ident, value) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Unpack the RemoteException, and re-throw the real Exception.
                //
                }
            }
        return result ;
        }

    /**
     * Service health check.
     * @return ServiceStatusData with details of the Service status.
     * TODO -refactor this to a base class
     *
     */
    public ServiceStatusData getServiceStatus()
        {
        ServiceStatusData result = null ;
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                result = this.manager.getServiceStatus() ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Unpack the RemoteException, and re-throw the real Exception.
                //
                }
            }
        return result ;
        }
    }
