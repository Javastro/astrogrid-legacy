/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/common/Attic/CommunityConfig.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/18 15:50:03 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityConfig.java,v $
 *   Revision 1.6  2003/09/18 15:50:03  dave
 *   Fixing bugs in configuration
 *
 *   Revision 1.5  2003/09/17 19:47:21  dave
 *   1) Fixed classnotfound problems in the build.
 *   2) Added the JUnit task to add the initial accounts and groups.
 *   3) Got the build to work together with the portal.
 *   4) Fixed some bugs in the Account handling.
 *
 *   Revision 1.4  2003/09/14 21:19:08  KevinBenson
 *   Added a PolicyServiceDelegate and some Configuration stuff added.
 *
 *   Revision 1.3  2003/09/13 02:18:52  dave
 *   Extended the jConfig configuration code.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common ;

import java.io.File ;
import java.net.InetAddress ;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.jconfig.Configuration ;
import org.jconfig.ConfigurationManager ;
import org.jconfig.handler.URLHandler ;
import org.jconfig.handler.XMLFileHandler ;

/**
 * Class to encapsulate the Community configuration.
 *
 */
public class CommunityConfig
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	private static final boolean DEBUG_FLAG = true ;

	/**
	 * The configuration name to use within jConfig.
	 *
	 */
	public static final String CONFIG_NAME = "org.astrogrid.community";

	/**
	 * The default category to use in the config file.
	 * Corresponds to the name of the category element in the config file.
	 * <pre>
	 *     &lt;properties&gt;
	 *         &lt;category name="org.astrogrid.community"&gt;
	 *         ....
	 *     &lt;/properties&gt;
	 * </pre>
	 *
	 */
	public static final String DEFAULT_CATEGORY = "org.astrogrid.community";

	/**
	 * The default JNDI name.
	 * Corresponds to the name in the env-entry-name entry in your web.xml.
	 * <pre>
	 *     &lt;env-entry&gt;
	 *         &lt;env-entry-name&gt;org.astrogrid.community.config&lt;/env-entry-name&gt;
	 *         &lt;env-entry-value&gt;....&lt;/env-entry-value&gt;
	 *         &lt;env-entry-type&gt;java.lang.String&lt;/env-entry-type&gt;
	 *     &lt;/env-entry&gt;
	 * </pre>
	 *
	 */
	public static final String DEFAULT_JNDI_NAME = "org.astrogrid.community.config";

	/**
	 * The default system property name.
	 *
	 */
	public static final String DEFAULT_PROPERTY_NAME = "org.astrogrid.community.config";

	/**
	 * The name of the config property for our local host name.
	 *
	 */
	public static final String LOCALHOST_PROPERTY_NAME = "community.host";

	/**
	 * The name of the config property for our local community name.
	 *
	 */
	public static final String COMMUNITY_PROPERTY_NAME = "community.name";

	/**
	 * The name of the property for our policy manager URL.
	 *
	 */
	public static final String POLICY_MANAGER_PROPERTY_NAME = "policy.manager.url";

	/**
	 * The name of the property for our service URL.
	 *
	 */
	public static final String POLICY_SERVICE_PROPERTY_NAME = "policy.service.url";

	/**
	 * Our JConfig Configuration instance.
	 *
	 */
	private static Configuration config = null ;

	/**
	 * Static method to read a JNDI property.
	 *
	 */
	public static String getJndiProperty(String name)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("CommunityConfig.getJndiProperty()") ;
		String value = null ;
		try {
			if (DEBUG_FLAG) System.out.println("  JNDI name  : " + DEFAULT_JNDI_NAME) ;
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			value = (String) envCtx.lookup(DEFAULT_JNDI_NAME);
			if (DEBUG_FLAG) System.out.println("  JNDI value : " + value) ;
			}
		catch(Exception ouch)
			{
			if (DEBUG_FLAG) System.out.println("Exception while trying JNDI lookup") ;
			value = null;
			}
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		return value ;
		}

	/**
	 * Static method to load our configuration.
	 * Tries using the the default JNDI name, and then reverts to the default system property.
	 * The JNDI name corresponds to the name in the env-entry-name entry in your web.xml.
	 * <pre>
	 *     &lt;env-entry&gt;
	 *         &lt;env-entry-name&gt;org.astrogrid.community.config&lt;/env-entry-name&gt;
	 *         &lt;env-entry-value&gt;....&lt;/env-entry-value&gt;
	 *         &lt;env-entry-type&gt;java.lang.String&lt;/env-entry-type&gt;
	 *     &lt;/env-entry&gt;
	 * </pre>
	 *
	 */
	public static Configuration loadConfig()
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("CommunityConfig.loadConfig()") ;
		//
		// If we havn't already loaded our config.
		if (null == config)
			{
			String path = null ;
			//
			// Try using the JNDI lookup.
			if (null == path)
				{
				if (DEBUG_FLAG) System.out.println("Trying JNDI property") ;
				path = getJndiProperty(DEFAULT_JNDI_NAME) ;
				if (null != path)
					{
					if (DEBUG_FLAG) System.out.println("PASS : Got JNDI property") ;
					}
				}
			//
			// Try using the System property.
			if (null == path)
				{
				if (DEBUG_FLAG) System.out.println("Trying system property") ;
				path = System.getProperty(DEFAULT_PROPERTY_NAME) ;
				if (null != path)
					{
					if (DEBUG_FLAG) System.out.println("PASS : Got system property") ;
					}
				}
			//
			// If we still don't have a path.
			if (null == path)
				{
				if (DEBUG_FLAG) System.out.println("FAIL : Unable to get config file path") ;
				}
			//
			// Try loading our config file.
			if (null != path)
				{
				loadConfig(path) ;
				}
			}
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		return config ;
		}

	/**
	 * Static method to load our configuration, given the config file location.
	 *
	 */
	public static Configuration loadConfig(String path)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("CommunityConfig.loadConfig()") ;
		if (DEBUG_FLAG) System.out.println("  Path  : " + path) ;
		//
		// If we havn't already loaded our config.
		if (null == config)
			{
			//
			// If the path is not null.
			if (null != path)
				{
				ConfigurationManager manager = ConfigurationManager.getInstance();
				try {
					if (null != path)
						{
						if (path.startsWith("http"))
							{
							URLHandler handler = new URLHandler();
							handler.setURL(path);
							manager.load(handler, CONFIG_NAME);
							}
						else {
							File file = new File(path);
							XMLFileHandler handler = new XMLFileHandler();
							handler.setFile(file);
							handler.load(file);
							manager.load(handler, CONFIG_NAME);
							}
						config = ConfigurationManager.getConfiguration(CONFIG_NAME);
						}
					}
				catch(Exception e)
					{
					e.printStackTrace();
					config = null;
					}
				if (null != config)
					{
					//
					// Store the path used to load it.
					config.setProperty("config.location", path) ;
					if (DEBUG_FLAG)
						{
						if (DEBUG_FLAG) System.out.println("  ----") ;
						String[] names = config.getPropertyNames(DEFAULT_CATEGORY) ;
						for (int i = 0 ; i < names.length ; i++)
							{
							String name = names[i] ;
							String value = config.getProperty(name, "", DEFAULT_CATEGORY) ;
							if (DEBUG_FLAG) System.out.println("  '" + name + "' : '" + value + "'") ;
							}
						if (DEBUG_FLAG) System.out.println("  ----") ;
						}
					}
				}
			}
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		return config ;
		}

	/**
	 * Static method to get a config property.
	 *
	 */
	public static String getProperty(String propName)
		{
		return getProperty(propName, null, DEFAULT_CATEGORY);
		}

	/**
	 * Static method to get a config property.
	 *
	 */
	public static String getProperty(String propName, String defaultVal)
		{
		return getProperty(propName, defaultVal, DEFAULT_CATEGORY);
		}

	/**
	 * Static method to get a config property.
	 *
	 */
	public static String getProperty(String propName, String defaultVal, String category)
		{
		if (null == config)
			{
			config = ConfigurationManager.getConfiguration(CONFIG_NAME);
			}
		return (null != config) ? config.getProperty(propName, defaultVal, category) : null ;
		}

	/**
	 * Our local host name.
	 *
	 */
	private static String localhost = null ;

	/**
	 * Static method to get our localhost name.
	 * If not specified in the config file or a system property, defaults to our local host name.
	 *
	 */
	public static String getHostName()
		{
		//
		// Try reading our config property.
		if ((null == localhost) || (localhost.length() <= 0))
			{
			if (DEBUG_FLAG) System.out.println("getHostName()") ;
			if (DEBUG_FLAG) System.out.println("  Trying config property '" + LOCALHOST_PROPERTY_NAME + "'") ;
			localhost = getProperty(LOCALHOST_PROPERTY_NAME) ;
			if (DEBUG_FLAG) System.out.println("  Config property result : " + localhost) ;
			}
		//
		// Try reading our system property.
		if ((null == localhost) || (localhost.length() <= 0))
			{
			String name = CONFIG_NAME + "." + LOCALHOST_PROPERTY_NAME ;
			if (DEBUG_FLAG) System.out.println("getHostName()") ;
			if (DEBUG_FLAG) System.out.println("  Trying system property '" + name + "'") ;
			localhost = System.getProperty(name) ;
			if (DEBUG_FLAG) System.out.println("  System property result : " + localhost) ;
			}
		//
		// Try using our hostname.
		if ((null == localhost) || (localhost.length() <= 0))
			{
			if (DEBUG_FLAG) System.out.println("getHostName()") ;
			if (DEBUG_FLAG) System.out.println("  Trying localhost ....") ;
			//
			// Try our local host address.
			try {
				localhost = InetAddress.getLocalHost().getHostName() ;
				}
			catch (Exception ouch)
				{
				localhost = "localhost" ;
				}
			if (DEBUG_FLAG) System.out.println("  Localhost result : " + localhost) ;
			}
		return localhost ;
		}

	/**
	 * Our local community name.
	 *
	 */
	private static String community = null ;

	/**
	 * Static method to get our community name.
	 * If not specified in the config file or a system property, defaults to our local host name.
	 *
	 */
	public static String getCommunityName()
		{
		//
		// Try reading our config property.
		if ((null == community) || (community.length() <= 0))
			{
			if (DEBUG_FLAG) System.out.println("getCommunityName()") ;
			if (DEBUG_FLAG) System.out.println("  Trying config property '" + COMMUNITY_PROPERTY_NAME + "'") ;
			community = getProperty(COMMUNITY_PROPERTY_NAME) ;
			if (DEBUG_FLAG) System.out.println("  Config property result : " + community) ;
			}
		//
		// Try reading our system property.
		if ((null == community) || (community.length() <= 0))
			{
			String name = CONFIG_NAME + "." + COMMUNITY_PROPERTY_NAME ;
			if (DEBUG_FLAG) System.out.println("getCommunityName()") ;
			if (DEBUG_FLAG) System.out.println("  Trying system property '" + name + "'") ;
			community = System.getProperty(name) ;
			if (DEBUG_FLAG) System.out.println("  System property result : " + community) ;
			}
		//
		// Try using our hostname.
		if ((null == community) || (community.length() <= 0))
			{
			if (DEBUG_FLAG) System.out.println("getCommunityName()") ;
			if (DEBUG_FLAG) System.out.println("  Trying localhost ....") ;
			//
			// Try our local host address.
			community = getHostName() ;
			if (DEBUG_FLAG) System.out.println("  Localhost result : " + community) ;
			}
		return community ;
		}

	/**
	 * Our local manager URL.
	 *
	 */
	private static String manager = null ;

	/**
	 * Static method to get our local manager URL.
	 *
	 */
	public static String getManagerUrl()
		{
		if (DEBUG_FLAG) System.out.println("getManagerUrl()") ;
		//
		// Try reading our config property.
		if ((null == manager) || (manager.length() <= 0))
			{
			if (DEBUG_FLAG) System.out.println("getManagerUrl()") ;
			if (DEBUG_FLAG) System.out.println("  Trying config property '" + POLICY_MANAGER_PROPERTY_NAME + "'") ;
			manager = getProperty(POLICY_MANAGER_PROPERTY_NAME) ;
			if (null != manager) manager = manager.trim() ;
			if (DEBUG_FLAG) System.out.println("  Config property result : " + manager) ;
			}
		//
		// Try using our local host name.
		if ((null == manager) || (manager.length() <= 0))
			{
			if (DEBUG_FLAG) System.out.println("getManagerUrl()") ;
			if (DEBUG_FLAG) System.out.println("  Trying localhost ....") ;
			manager = "http://" + getHostName() + ":8080/axis/services/PolicyManager" ;
			if (DEBUG_FLAG) System.out.println("  Localhost result : " + manager) ;
			}
		if (DEBUG_FLAG) System.out.println("  Manager URL : " + manager) ;
		return manager ;
		}

	/**
	 * Our local service URL.
	 *
	 */
	private static String service = null ;

	/**
	 * Static method to get our local service URL.
	 *
	 */
	public static String getServiceUrl()
		{
		if (DEBUG_FLAG) System.out.println("getServiceUrl()") ;
		//
		// Try reading our config property.
		if ((null == service) || (service.length() <= 0))
			{
			if (DEBUG_FLAG) System.out.println("getServiceUrl()") ;
			if (DEBUG_FLAG) System.out.println("  Trying config property '" + POLICY_SERVICE_PROPERTY_NAME + "'") ;
			service = getProperty(POLICY_SERVICE_PROPERTY_NAME) ;
			if (null != service) service = service.trim() ;
			if (DEBUG_FLAG) System.out.println("  Config property result : " + service) ;
			}
		//
		// Try using our local host name.
		if ((null == service) || (service.length() <= 0))
			{
			if (DEBUG_FLAG) System.out.println("getServiceUrl()") ;
			if (DEBUG_FLAG) System.out.println("  Trying localhost ....") ;
			service = "http://" + getHostName() + ":8080/axis/services/PolicyService" ;
			if (DEBUG_FLAG) System.out.println("  Localhost result : " + manager) ;
			}
		if (DEBUG_FLAG) System.out.println("  Service URL : " + manager) ;
		return service ;
		}

      /**
       * Our Administrator for a portal or the community
       *
       */
      private static String admin = null ;

      /**
       * The name of the property for admin name
       *
       */
      public static final String ASTROGRID_ADMIN_PROPERTY_NAME = "astrogrid.admin";

      /**
       * Static method to get our local service URL.
       *
       */
      public static String getAdministrator()
         {
         //
         // Try reading our config property.
         if (null == admin)
            {
            admin = getProperty(ASTROGRID_ADMIN_PROPERTY_NAME) ;
            }
            return admin;
      }
      
      /**
       * Our Administrator for a portal or the community
       *
       */
      private static String adminEmail = null ;

      /**
       * The name of the property for admin name
       *
       */
      public static final String ASTROGRID_ADMIN_EMAIL_PROPERTY_NAME = "astrogrid.adminEmail";

      /**
       * Static method to get our local service URL.
       *
       */
      public static String getAdministratorEmail()
         {
         //
         // Try reading our config property.
         if (null == adminEmail)
            {
            adminEmail = getProperty(ASTROGRID_ADMIN_EMAIL_PROPERTY_NAME) ;
            }
            return adminEmail;
      }
	}