/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/config/CommunityConfig.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/02/18 19:48:31 $</cvs:date>
 * <cvs:version>$Revision: 1.10 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityConfig.java,v $
 *   Revision 1.10  2005/02/18 19:48:31  clq2
 *   Reg_KMB_913 again merging again.
 *
 *   Revision 1.7.48.1  2005/02/10 12:59:56  KevinBenson
 *   getting rid of jconfig stuff.  And moved things into web.xml and get rid of the META-INF/context.xml one.
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

import org.astrogrid.config.Config;

/*
import org.jconfig.Configuration ;
import org.jconfig.ConfigurationManager ;
import org.jconfig.handler.URLHandler ;
import org.jconfig.handler.XMLFileHandler ;
*/

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
     * The configuration name.
     *
     */
    public static final String CONFIG_NAME = "org.astrogrid.community";


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
     * conf - Config variable to access the configuration for the server normally
     * jndi to a config file.
     * @see org.astrogrid.config.Config
     */   
    public static Config config = null;
    
    /**
     * Static to be used on the initiatian of this class for the config
     */
    static {
       if(config == null) {
          config = org.astrogrid.config.SimpleConfig.getSingleton();
       }      
    }

    /**
     * Static method to get a config property.
     *
     */
    public static String getProperty(String propName)
        {
        return config.getString(propName);
        }

    /**
     * Static method to get a config property.
     */
    public static String getProperty(String propName, String defaultVal)
        {
        return config.getString(propName, defaultVal);
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