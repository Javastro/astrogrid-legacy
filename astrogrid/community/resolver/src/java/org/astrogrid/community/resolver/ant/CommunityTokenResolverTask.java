/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/java/org/astrogrid/community/resolver/ant/Attic/CommunityTokenResolverTask.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/30 04:44:01 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityTokenResolverTask.java,v $
 *   Revision 1.2  2004/03/30 04:44:01  dave
 *   Merged development branch, dave-dev-200403300258, into HEAD
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

import org.astrogrid.community.resolver.CommunityTokenResolver ;

/**
 * An Ant task to validate a Community toekn.
 * 
 */
public class CommunityTokenResolverTask
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
    public CommunityTokenResolverTask()
        {
        //
        // Initialise our base class.
        super() ;
        }

    /**
     * Public constructor.
     *
     */
    public CommunityTokenResolverTask(Task parent)
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
        if (DEBUG_FLAG) System.out.println("CommunityTokenResolverTask.init()");
        }

    /**
     * Execute our Task.
     *
     */
    public void execute()
        throws BuildException
        {
        if (DEBUG_FLAG) System.out.println("----\"----");
        if (DEBUG_FLAG) System.out.println("CommunityTokenResolverTask.execute()");
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
            // Ask our resolver to check the token.
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

