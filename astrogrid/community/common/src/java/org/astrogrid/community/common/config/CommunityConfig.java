/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/config/CommunityConfig.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/02/17 16:01:10 $</cvs:date>
 * <cvs:version>$Revision: 1.9 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityConfig.java,v $
 *   Revision 1.9  2005/02/17 16:01:10  jdt
 *   Rolled back to 15 Feb 00:00, before community_pah_910, mySpace_pah_910 and Reg_KMB_913
 *
 *   Revision 1.7  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.6.70.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.6  2004/07/14 13:50:07  dave
 *   Merged development branch, dave-dev-200406301228, into HEAD
 *
 *   Revision 1.5.12.1  2004/07/05 14:18:55  dave
 *   Tried to remove the JConfig libraries
 *
 *   Revision 1.5  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.4.54.1  2004/06/17 13:38:58  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.config ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

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
 * @todo deprecate the whole class.
 *
 */
public class CommunityConfig
    {
    /**
     * Our debug logger.
     *
     */
	private static Log log = LogFactory.getLog(CommunityConfig.class);

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
     * The name of the property for our authentication URL.
     *
     */
    public static final String AUTHENTICATOR_PROPERTY_NAME = "authentication.service.url";

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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityConfig.getJndiProperty()") ;
        String value = null ;
        try {
            log.debug("  JNDI name  : " + DEFAULT_JNDI_NAME) ;
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            value = (String) envCtx.lookup(DEFAULT_JNDI_NAME);
            log.debug("  JNDI value : " + value) ;
            }
        catch(Exception ouch)
            {
            log.debug("Exception while trying JNDI lookup") ;
            value = null;
            }
        log.debug("----\"----") ;
        log.debug("") ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityConfig.loadConfig()") ;
        //
        // If we havn't already loaded our config.
        if (null == config)
            {
            String path = null ;
            //
            // Try using the JNDI lookup.
            if (null == path)
                {
                log.debug("Trying JNDI property") ;
                path = getJndiProperty(DEFAULT_JNDI_NAME) ;
                if (null != path)
                    {
                    log.debug("PASS : Got JNDI property") ;
                    }
                }
            //
            // Try using the System property.
            if (null == path)
                {
                log.debug("Trying system property") ;
                path = System.getProperty(DEFAULT_PROPERTY_NAME) ;
                if (null != path)
                    {
                    log.debug("PASS : Got system property") ;
                    }
                }
            //
            // If we still don't have a path.
            if (null == path)
                {
                log.debug("FAIL : Unable to get config file path") ;
                }
            //
            // Try loading our config file.
            if (null != path)
                {
                loadConfig(path) ;
                }
            }
        log.debug("----\"----") ;
        log.debug("") ;
        return config ;
        }

    /**
     * Static method to load our configuration, given the config file location.
     *
     */
    public static Configuration loadConfig(String path)
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityConfig.loadConfig()") ;
        log.debug("  Path  : " + path) ;
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
                    log.debug("  ----") ;
                    String[] names = config.getPropertyNames(DEFAULT_CATEGORY) ;
                    for (int i = 0 ; i < names.length ; i++)
                        {
                        String name = names[i] ;
                        String value = config.getProperty(name, "", DEFAULT_CATEGORY) ;
                        log.debug("  '" + name + "' : '" + value + "'") ;
                        }
                    log.debug("  ----") ;
                    }
                }
            }
        log.debug("----\"----") ;
        log.debug("") ;
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
            log.debug("getHostName()") ;
            log.debug("  Trying config property '" + LOCALHOST_PROPERTY_NAME + "'") ;
            localhost = getProperty(LOCALHOST_PROPERTY_NAME) ;
            log.debug("  Config property result : " + localhost) ;
            }
        //
        // Try reading our system property.
        if ((null == localhost) || (localhost.length() <= 0))
            {
            String name = CONFIG_NAME + "." + LOCALHOST_PROPERTY_NAME ;
            log.debug("getHostName()") ;
            log.debug("  Trying system property '" + name + "'") ;
            localhost = System.getProperty(name) ;
            log.debug("  System property result : " + localhost) ;
            }
        //
        // Try using our hostname.
        if ((null == localhost) || (localhost.length() <= 0))
            {
            log.debug("getHostName()") ;
            log.debug("  Trying localhost ....") ;
            //
            // Try our local host address.
            try {
                localhost = InetAddress.getLocalHost().getHostName() ;
                }
            catch (Exception ouch)
                {
                localhost = "localhost" ;
                }
            log.debug("  Localhost result : " + localhost) ;
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
            log.debug("getCommunityName()") ;
            log.debug("  Trying config property '" + COMMUNITY_PROPERTY_NAME + "'") ;
            community = getProperty(COMMUNITY_PROPERTY_NAME) ;
            log.debug("  Config property result : " + community) ;
            }
        //
        // Try reading our system property.
        if ((null == community) || (community.length() <= 0))
            {
            String name = CONFIG_NAME + "." + COMMUNITY_PROPERTY_NAME ;
            log.debug("getCommunityName()") ;
            log.debug("  Trying system property '" + name + "'") ;
            community = System.getProperty(name) ;
            log.debug("  System property result : " + community) ;
            }
        //
        // Try using our hostname.
        if ((null == community) || (community.length() <= 0))
            {
            log.debug("getCommunityName()") ;
            log.debug("  Trying localhost ....") ;
            //
            // Try our local host address.
            community = getHostName() ;
            log.debug("  Localhost result : " + community) ;
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
        log.debug("getManagerUrl()") ;
        //
        // Try reading our config property.
        if ((null == manager) || (manager.length() <= 0))
            {
            log.debug("getManagerUrl()") ;
            log.debug("  Trying config property '" + POLICY_MANAGER_PROPERTY_NAME + "'") ;
            manager = getProperty(POLICY_MANAGER_PROPERTY_NAME) ;
            if (null != manager) manager = manager.trim() ;
            log.debug("  Config property result : " + manager) ;
            }
        //
        // Try using our local host name.
        if ((null == manager) || (manager.length() <= 0))
            {
            log.debug("getManagerUrl()") ;
            log.debug("  Trying localhost ....") ;
            manager = "http://" + getHostName() + ":8080/axis/services/PolicyManager" ;
            log.debug("  Localhost result : " + manager) ;
            }
        log.debug("  Manager URL : " + manager) ;
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
        log.debug("getServiceUrl()") ;
        //
        // Try reading our config property.
        if ((null == service) || (service.length() <= 0))
            {
            log.debug("getServiceUrl()") ;
            log.debug("  Trying config property '" + POLICY_SERVICE_PROPERTY_NAME + "'") ;
            service = getProperty(POLICY_SERVICE_PROPERTY_NAME) ;
            if (null != service) service = service.trim() ;
            log.debug("  Config property result : " + service) ;
            }
        //
        // Try using our local host name.
        if ((null == service) || (service.length() <= 0))
            {
            log.debug("getServiceUrl()") ;
            log.debug("  Trying localhost ....") ;
            service = "http://" + getHostName() + ":8080/axis/services/PolicyService" ;
            log.debug("  Localhost result : " + service) ;
            }
        log.debug("  Service URL : " + service) ;
        return service ;
        }


    /**
     * Our local authentication service URL.
     *
     */
    private static String authenticator = null ;

    /**
     * Static method to get our local authentication service URL.
     *
     */
    public static String getAuthenticationServiceUrl()
        {
        log.debug("getAuthenticationServiceUrl()") ;
        //
        // Try reading our config property.
        if ((null == authenticator) || (authenticator.length() <= 0))
            {
            log.debug("getAuthenticationServiceUrl()") ;
            log.debug("  Trying config property '" + AUTHENTICATOR_PROPERTY_NAME + "'") ;
            authenticator = getProperty(AUTHENTICATOR_PROPERTY_NAME) ;
            if (null != authenticator) authenticator = authenticator.trim() ;
            log.debug("  Config property result : " + authenticator) ;
            }
        //
        // Try using our local host name.
        if ((null == authenticator) || (authenticator.length() <= 0))
            {
            log.debug("getAuthenticationServiceUrl()") ;
            log.debug("  Trying localhost ....") ;
            authenticator = "http://" + getHostName() + ":8080/axis/services/AuthenticationService" ;
            log.debug("  Localhost result : " + authenticator) ;
            }
        log.debug("  Authenticator URL : " + authenticator) ;
        return authenticator ;
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