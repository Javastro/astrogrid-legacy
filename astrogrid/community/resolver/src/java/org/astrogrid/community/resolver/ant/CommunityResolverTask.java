/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/java/org/astrogrid/community/resolver/ant/Attic/CommunityResolverTask.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/04/01 07:09:39 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityResolverTask.java,v $
 *   Revision 1.4  2004/04/01 07:09:39  dave
 *   Merged development branch, dave-dev-200403300547, into HEAD
 *
 *   Revision 1.3.2.1  2004/04/01 01:50:14  dave
 *   Updated install and resolver docs.
 *
 *   Revision 1.3  2004/03/30 04:44:01  dave
 *   Merged development branch, dave-dev-200403300258, into HEAD
 *
 *   Revision 1.2.2.1  2004/03/30 03:24:57  dave
 *   Fixes to resolver tasks.
 *
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

import java.util.Properties ;
import java.util.Enumeration ;

import java.io.FileInputStream ;

import org.apache.tools.ant.Task ;
import org.apache.tools.ant.BuildException ;

import org.astrogrid.config.Config ;
import org.astrogrid.config.SimpleConfig ;

import org.astrogrid.community.common.security.data.SecurityToken ;

/**
 * Base class for our Ant tasks.
 * 
 */
public class CommunityResolverTask
    extends Task
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
    public CommunityResolverTask()
        {
        //
        // Initialise our base class.
        super() ;
        }

    /**
     * Public constructor.
     *
     */
    public CommunityResolverTask(Task parent)
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
        if (DEBUG_FLAG) System.out.println("CommunityResolverTask.init()");
        if (DEBUG_FLAG) System.out.println("  Token : " + this.getToken());
        }

    /**
     * The name of our token property.
     *
     */
    public static final String TOKEN_PROPERTY = "org.astrogrid.community.token" ;

    /**
     * Get our security token.
     *
     */
    protected SecurityToken getToken()
        {
        //
        // If we have a project
        if (null != this.getProject())
            {
            //
            // Get the current property.
            String string = this.getProject().getProperty(TOKEN_PROPERTY) ;
            //
            // If we found a property.
            if (null != string)
                {
                //
                // Create a new token.
                return new SecurityToken(string) ;
                }
            }
        return null ;
        }

    /**
     * Set our security token.
     *
     */
    protected void setToken(SecurityToken token)
        {
        //
        // If we have a project
        if (null != this.getProject())
            {
            //
            // Set our token property.
            this.getProject().setProperty(
                TOKEN_PROPERTY,
                token.toString()
                ) ;
            }
        }

    /**
     * The AstroGrid configuration.
     *
     */
    protected static Config config = SimpleConfig.getSingleton() ;

	/**
	 * Our registry endpoint.
	 *
	 */
	private String registry ;

	/**
	 * Access to our registry endpoint.
	 *
	 */
	public void setRegistry(String value)
		{
		this.registry = value ;
		}

	/**
	 * Access to our registry endpoint.
	 *
	 */
	public String getRegistry()
		{
		return this.registry ;
		}

    /**
     * Our properties location.
     *
     */
    private String properties ;

    /**
     * Get our properties location.
     *
     */
    public String getProperties()
        {
        return this.properties ;
        }

    /**
     * Set our properties location.
     *
     */
    public void setProperties(String value)
        {
        this.properties = value ;
        }

    /**
     * Load our config properties.
     *
     */
    public void configure()
        throws BuildException
        {
        if (DEBUG_FLAG) System.out.println("----\"----");
        if (DEBUG_FLAG) System.out.println("CommunityResolverTask.configure()");
        //
        // If our properties are set.
        if (null != this.properties)
            {
	        //
	        // Try parse our properties.
	        try {
	            if (DEBUG_FLAG) System.out.println("  Properties  : " + this.properties);
	            //
	            // Load our properties file.
	            Properties map = new Properties() ;
	            map.load(
	                new FileInputStream(
	                    this.properties
	                    )
	                ) ;
	            //
	            // Add the properties to our config.
	            Enumeration enum = map.keys() ;
	            while (enum.hasMoreElements())
	                {
	                if (DEBUG_FLAG) System.out.println("----");
	                //
	                // Get the property name and value.
	                String name = (String)enum.nextElement() ;
	                if (DEBUG_FLAG) System.out.println("  Name  : " + name);
	                String value = map.getProperty(name) ;
	                if (DEBUG_FLAG) System.out.println("  Value : " + value);
	                //
	                // Process the property.
	                if (null != this.getProject())
	                    {
	                    value = this.getProject().replaceProperties(value) ;
	                    }
	                if (DEBUG_FLAG) System.out.println("  Value : " + value);
	                //
	                // Add the property to our config.
	                config.setProperty(name, value) ;
	                if (DEBUG_FLAG) System.out.println("----");
	                }
                }
	        catch (Exception ouch)
	            {
	            throw new BuildException(ouch) ;
	            }
            }
        }

    /**
     * Execute our Task.
     *
     */
    public void execute()
        throws BuildException
        {
        if (DEBUG_FLAG) System.out.println("----\"----");
        if (DEBUG_FLAG) System.out.println("CommunityResolverTask.execute()");
        if (DEBUG_FLAG) System.out.println("  Registry   : " + this.registry);
        if (DEBUG_FLAG) System.out.println("  Properties : " + this.properties);
        //
        // Try loading our config properties.
		this.configure() ;
        }
    }

