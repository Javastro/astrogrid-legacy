/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/java/org/astrogrid/community/resolver/ant/Attic/CommunityAccountResolverTask.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/30 04:44:01 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityAccountResolverTask.java,v $
 *   Revision 1.2  2004/03/30 04:44:01  dave
 *   Merged development branch, dave-dev-200403300258, into HEAD
 *
 *   Revision 1.1.2.3  2004/03/30 04:42:31  dave
 *   Added CommunityAccountSpaceResolverTask.
 *
 *   Revision 1.1.2.2  2004/03/30 03:58:31  dave
 *   Added CommunityTokenResolverTask.
 *
 *   Revision 1.1.2.1  2004/03/30 03:24:57  dave
 *   Fixes to resolver tasks.
 *
 * </cvs:log>
 *
 *
 */
package org.astrogrid.community.resolver.ant ;

import java.net.URL ;

import org.apache.tools.ant.Task ;
import org.apache.tools.ant.BuildException ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.community.common.policy.data.AccountData ;

import org.astrogrid.community.resolver.CommunityAccountResolver ;

/**
 * An Ant task to resolve a Community Account.
 * 
 */
public class CommunityAccountResolverTask
    extends CommunityResolverTask
    {
    /**
     * Our debug flag.
     *
     */
    private static final boolean DEBUG_FLAG = true ;

    /**
     * Public constructor.
     *
     */
    public CommunityAccountResolverTask()
        {
        //
        // Initialise our base class.
        super() ;
        }

    /**
     * Public constructor.
     *
     */
    public CommunityAccountResolverTask(Task parent)
        {
        //
        // Initialise our base class.
        super() ;
        //
        // Set our project.
        setProject(parent.getProject()) ;
        }

    /**
     * Our account identifier.
     *
     */
    private String account ;

    /**
     * Get our account identifier.
     *
     */
    public String getAccount()
        {
        return this.account ;
        }

    /**
     * Set our account identifier.
     *
     */
    public void setAccount(String value)
        {
        this.account = value ;
        }

    /**
     * Initialise our Task.
     *
     */
    public void init()
        throws BuildException
        {
        if (DEBUG_FLAG) System.out.println("----\"----");
        if (DEBUG_FLAG) System.out.println("CommunityAccountResolverTask.init()");
        }

    /**
     * Execute our Task.
     *
     */
    public void execute()
        throws BuildException
        {
        if (DEBUG_FLAG) System.out.println("----\"----");
        if (DEBUG_FLAG) System.out.println("CommunityAccountResolverTask.execute()");
        if (DEBUG_FLAG) System.out.println("  Account : " + this.account);
        //
        // Load our config properties.
        this.configure() ;
        //
        // Try login to the account.
        try {
			//
			// Create our resolver.
            CommunityAccountResolver resolver ;
			//
			// Use our registry endpoint, if we have one.
			if (null != this.getRegistry())
				{
	            resolver = new CommunityAccountResolver(
	            	new URL(
	            		this.getRegistry()
	            		)
	            	) ;
				}
			//
			// Otherwise, just create a default resolver.
			else {
	            resolver = new CommunityAccountResolver() ;
				}
            //
            // Ask our resolver to get the Account details.
            AccountData account = resolver.resolve(
				new Ivorn(
					this.account
					)
                ) ;
	        if (DEBUG_FLAG) System.out.println("----");
	        if (DEBUG_FLAG) System.out.println("Account - " + account.getIdent());
	        if (DEBUG_FLAG) System.out.println("  Display name : " + account.getDisplayName());
	        if (DEBUG_FLAG) System.out.println("  Description  : " + account.getDescription());
	        if (DEBUG_FLAG) System.out.println("  Home space   : " + account.getHomeSpace());
	        if (DEBUG_FLAG) System.out.println("----");
            }
        catch (Exception ouch)
            {
            throw new BuildException(ouch) ;
            }
        }
    }

