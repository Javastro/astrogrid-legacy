/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/java/org/astrogrid/community/resolver/ant/Attic/CommunityLoginTask.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/30 01:40:03 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityLoginTask.java,v $
 *   Revision 1.2  2004/03/30 01:40:03  dave
 *   Merged development branch, dave-dev-200403242058, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/30 01:38:14  dave
 *   Refactored resolver and install toolkits.
 *
 * </cvs:log>
 *
 *
 */
package org.astrogrid.community.resolver.ant ;

import java.net.URL ;

import org.apache.tools.ant.Task ;
import org.apache.tools.ant.BuildException ;

import org.astrogrid.community.resolver.CommunityPasswordResolver ;

/**
 * An Ant task to login to a Community service.
 * 
 */
public class CommunityLoginTask
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
    public CommunityLoginTask()
        {
        //
        // Initialise our base class.
        super() ;
        }

    /**
     * Public constructor.
     *
     */
    public CommunityLoginTask(Task parent)
        {
        //
        // Initialise our base class.
        super() ;
        //
        // Set our project.
        setProject(parent.getProject()) ;
        }

    /**
     * Initialise our Task.
     *
     */
    public void init()
        throws BuildException
        {
        if (DEBUG_FLAG) System.out.println("----\"----");
        if (DEBUG_FLAG) System.out.println("CommunityLoginTask.init()");
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
     * Our account password.
     *
     */
    private String password ;

    /**
     * Get our account password.
     *
     */
    public String getPassword()
        {
        return this.password ;
        }

    /**
     * Set our account password.
     *
     */
    public void setPassword(String value)
        {
        this.password = value ;
        }

    /**
     * Execute our Task.
     *
     */
    public void execute()
        throws BuildException
        {
        if (DEBUG_FLAG) System.out.println("----\"----");
        if (DEBUG_FLAG) System.out.println("CommunityLoginTask.execute()");
        if (DEBUG_FLAG) System.out.println("  Account  : " + this.account);
        if (DEBUG_FLAG) System.out.println("  Password : " + this.password);
        //
        // Load our config properties.
        this.configure() ;
        //
        // Try login to the account.
        try {
			//
			// Create our resolver.
            CommunityPasswordResolver resolver ;
			//
			// Use our registry endpoint, if we have one.
			if (null != this.getRegistry())
				{
	            resolver = new CommunityPasswordResolver(
	            	new URL(
	            		this.getRegistry()
	            		)
	            	) ;
				}
			//
			// Otherwise, just create a default resolver.
			else {
	            resolver = new CommunityPasswordResolver() ;
				}
            //
            // Ask our resolver to check the password
            this.setToken(
                resolver.checkPassword(
                    this.account,
                    this.password
                    )
                ) ;
            }
        catch (Exception ouch)
            {
            throw new BuildException(ouch) ;
            }
        if (DEBUG_FLAG) System.out.println("----");
        if (DEBUG_FLAG) System.out.println("Token : " + this.getToken());
        if (DEBUG_FLAG) System.out.println("----");
        }
    }

