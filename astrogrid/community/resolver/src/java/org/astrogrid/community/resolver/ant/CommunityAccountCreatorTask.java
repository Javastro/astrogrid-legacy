/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/java/org/astrogrid/community/resolver/ant/Attic/CommunityAccountCreatorTask.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityAccountCreatorTask.java,v $
 *   Revision 1.4  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.3.14.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
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

import org.astrogrid.community.client.policy.manager.PolicyManagerDelegate ;
import org.astrogrid.community.resolver.policy.manager.PolicyManagerResolver ;

/**
 * An Ant task to create a Community Account.
 * 
 */
public class CommunityAccountCreatorTask
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
    public CommunityAccountCreatorTask()
        {
        //
        // Initialise our base class.
        super() ;
        }

    /**
     * Public constructor.
     *
     */
    public CommunityAccountCreatorTask(Task parent)
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
        if (DEBUG_FLAG) System.out.println("CommunityAccountCreatorTask.init()");
        }

    /**
     * Execute our Task.
     *
     */
    public void execute()
        throws BuildException
        {
        if (DEBUG_FLAG) System.out.println("----\"----");
        if (DEBUG_FLAG) System.out.println("CommunityAccountCreatorTask.execute()");
        if (DEBUG_FLAG) System.out.println("  Account : " + this.account);
        //
        // Load our config properties.
        this.configure() ;
        //
        // Try creating our account.
        try {
            //
            // Create our resolver.
            PolicyManagerResolver resolver ;
            //
            // Use our registry endpoint, if we have one.
            if (null != this.getRegistry())
                {
                resolver = new PolicyManagerResolver(
                    new URL(
                        this.getRegistry()
                        )
                    ) ;
                }
            //
            // Otherwise, just create a default resolver.
            else {
                resolver = new PolicyManagerResolver() ;
                }
            if (DEBUG_FLAG) System.out.println("  PASS : Got resolver");
            //
            // Ask our resolver for a manager delegate.
            PolicyManagerDelegate delegate = resolver.resolve(
                new Ivorn(this.account)
                ) ;
            if (DEBUG_FLAG) System.out.println("  PASS : Got delegate");
            //
            // Ask the PolicyManagerDelegate to create the Account.
            AccountData account =  delegate.addAccount(
                this.account
                ) ;
            if (DEBUG_FLAG) System.out.println("  PASS : Got account");
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

