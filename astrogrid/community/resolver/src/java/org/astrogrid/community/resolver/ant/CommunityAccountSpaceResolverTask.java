/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/java/org/astrogrid/community/resolver/ant/Attic/CommunityAccountSpaceResolverTask.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/30 04:44:01 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityAccountSpaceResolverTask.java,v $
 *   Revision 1.2  2004/03/30 04:44:01  dave
 *   Merged development branch, dave-dev-200403300258, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/30 04:42:31  dave
 *   Added CommunityAccountSpaceResolverTask.
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

import org.astrogrid.community.resolver.CommunityAccountSpaceResolver ;

/**
 * An Ant task to resolve a Community Account home space.
 * 
 */
public class CommunityAccountSpaceResolverTask
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
    public CommunityAccountSpaceResolverTask()
        {
        //
        // Initialise our base class.
        super() ;
        }

    /**
     * Public constructor.
     *
     */
    public CommunityAccountSpaceResolverTask(Task parent)
        {
        //
        // Initialise our base class.
        super() ;
        //
        // Set our project.
        setProject(parent.getProject()) ;
        }

    /**
     * Our property name.
     *
     */
    private String property ;

    /**
     * Get our property name.
     *
     */
    public String getProperty()
        {
        return this.property ;
        }

    /**
     * Set our property name.
     *
     */
    public void setProperty(String value)
        {
        this.property = value ;
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
        if (DEBUG_FLAG) System.out.println("CommunityAccountSpaceResolverTask.init()");
        }

    /**
     * Execute our Task.
     *
     */
    public void execute()
        throws BuildException
        {
        if (DEBUG_FLAG) System.out.println("----\"----");
        if (DEBUG_FLAG) System.out.println("CommunityAccountSpaceResolverTask.execute()");
        if (DEBUG_FLAG) System.out.println("  Account : " + this.account);
        //
        // Load our config properties.
        this.configure() ;
        //
        // Try login to the account.
        try {
			//
			// Create our resolver.
            CommunityAccountSpaceResolver resolver ;
			//
			// Use our registry endpoint, if we have one.
			if (null != this.getRegistry())
				{
	            resolver = new CommunityAccountSpaceResolver(
	            	new URL(
	            		this.getRegistry()
	            		)
	            	) ;
				}
			//
			// Otherwise, just create a default resolver.
			else {
	            resolver = new CommunityAccountSpaceResolver() ;
				}
            //
            // Ask our resolver to get the Account home.
            Ivorn ivorn = resolver.resolve(
				new Ivorn(
					this.account
					)
                ) ;
	        if (DEBUG_FLAG) System.out.println("----");
	        if (DEBUG_FLAG) System.out.println("Home : " + ivorn);
	        if (DEBUG_FLAG) System.out.println("----");
	        //
	        // If we have a project
	        if (null != this.getProject())
	            {
				//
				// If we have a property name.
				if (null != this.property)
					{
		            //
		            // Set our project property.
		            this.getProject().setProperty(
		                this.property,
		                ivorn.toString()
		                ) ;
					}
	            }
            }
        catch (Exception ouch)
            {
            throw new BuildException(ouch) ;
            }
        }
    }

