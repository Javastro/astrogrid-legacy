/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/java/org/astrogrid/community/resolver/ant/Attic/CommunityTokenTask.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/30 01:40:03 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityTokenTask.java,v $
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

import org.astrogrid.community.resolver.CommunityTokenResolver ;

/**
 * An Ant task to validate a Community toekn.
 * 
 */
public class CommunityTokenTask
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
    public CommunityTokenTask()
        {
        //
        // Initialise our base class.
        super() ;
        }

    /**
     * Public constructor.
     *
     */
    public CommunityTokenTask(Task parent)
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
        if (DEBUG_FLAG) System.out.println("CommunityTokenTask.init()");
        }

    /**
     * Execute our Task.
     *
     */
    public void execute()
        throws BuildException
        {
        if (DEBUG_FLAG) System.out.println("----\"----");
        if (DEBUG_FLAG) System.out.println("CommunityTokenTask.execute()");
        if (DEBUG_FLAG) System.out.println("  Token : " + this.getToken());
        //
        // Load our config properties.
        this.configure() ;
        //
        // Try login to the account.
        try {
			//
			// Create our resolver.
            CommunityTokenResolver resolver ;
			//
			// Use our registry endpoint, if we have one.
			if (null != this.getRegistry())
				{
	            resolver = new CommunityTokenResolver(
	            	new URL(
	            		this.getRegistry()
	            		)
	            	) ;
				}
			//
			// Otherwise, just create a default resolver.
			else {
	            resolver = new CommunityTokenResolver() ;
				}
            //
            // Ask our resolver to check the password
            this.setToken(
                resolver.checkToken(
                    this.getToken()
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

