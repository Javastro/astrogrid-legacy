/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/security/manager/SecurityManagerCoreDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/19 14:43:14 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityManagerCoreDelegate.java,v $
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

import java.rmi.RemoteException ;

import org.astrogrid.community.common.security.manager.SecurityManager ;
import org.astrogrid.community.client.service.CommunityServiceCoreDelegate ;

/**
 * The core delegate code for our SecurityManager service.
 * This acts as a wrapper for a SecurityManager service, and handles any RemoteExceptions internally.
 *
 */
public class SecurityManagerCoreDelegate
	extends CommunityServiceCoreDelegate
    implements SecurityManager, SecurityManagerDelegate
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
    protected SecurityManagerCoreDelegate()
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
		this.setCommunityService(manager) ;
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
    }
