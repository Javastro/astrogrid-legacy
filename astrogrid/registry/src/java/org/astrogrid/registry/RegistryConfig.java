/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/registry/src/java/org/astrogrid/registry/Attic/RegistryConfig.java,v $</cvs:source>
 * <cvs:author>$Author: KevinBenson $</cvs:author>
 * <cvs:date>$Date: 2003/12/16 21:51:57 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: RegistryConfig.java,v $
 *   Revision 1.4  2003/12/16 21:51:57  KevinBenson
 *   *** empty log message ***
 *
 *   Revision 1.1.2.5  2003/12/16 20:21:38  KevinBenson
 *   New config file and some instructions.  Still debugging 1 last thing.
 *
 *   Revision 1.1.2.4  2003/12/02 16:11:56  KevinBenson
 *   Dusting off the harvester still some more work to do on the harvester, but
 *   it is getting their.
 *   Also added a couple of more junit tests and another template
 *
 *   Revision 1.1.2.3  2003/12/01 10:25:51  KevinBenson
 *   Added more templates for the client.  So it can do the TabularSky service and 
 *   other varoius services.  Also javadoc commented several of the java files,
 *   so it is a little more understandable.
 *
 *   Revision 1.1.2.2  2003/11/28 11:50:48  KevinBenson
 *   *** empty log message ***
 *
 *   Revision 1.1.2.1  2003/11/26 11:32:46  KevinBenson
 *   Some new code changes.  Still has some bugs that need to be worked out, but 
 *   getting their.  On my next commit I will explain in more detail what was changed.
 *   Mainly just want to commit
 * 
 * </cvs:log>
 *
 */
package org.astrogrid.registry;

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
public class RegistryConfig
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
	public static final String CONFIG_NAME = "org.astrogrid.registry";

	/**
	 * The default category to use in the config file.
	 * Corresponds to the name of the category element in the config file.
	 * <pre>
	 *     &lt;properties&gt;
	 *         &lt;category name="org.astrogrid.registry"&gt;
	 *         ....
	 *     &lt;/properties&gt;
	 * </pre>
	 *
	 */
	public static final String DEFAULT_CATEGORY = "org.astrogrid.registry";

	/**
	 * The default JNDI name.
	 * Corresponds to the name in the env-entry-name entry in your web.xml.
	 * <pre>
	 *     &lt;env-entry&gt;
	 *         &lt;env-entry-name&gt;org.astrogrid.registry.config&lt;/env-entry-name&gt;
	 *         &lt;env-entry-value&gt;....&lt;/env-entry-value&gt;
	 *         &lt;env-entry-type&gt;java.lang.String&lt;/env-entry-type&gt;
	 *     &lt;/env-entry&gt;
	 * </pre>
	 *
	 */
	public static final String DEFAULT_JNDI_NAME = "org.astrogrid.registry.config";

	/**
	 * The default system property name.
	 *
	 */
	public static final String DEFAULT_PROPERTY_NAME = "org.astrogrid.registry.config";

	/**
	 * The name and location of the registry file.
	 *
	 */
	public static final String REGISTRY_FILE = "registry.file";
   
   /**
    * The name of the Organisation Template used by portal
    *
    */
   public static final String ORGANISATION_XML_TEMPLATE = "organisation.xml.template";
   
   /**
    * The name of the Service Template used by portal
    *
    */
   public static final String SERVICE_XML_TEMPLATE = "service.xml.template";

   /**
    * The name of the Sky Service Template used by portal
    *
    */
   public static final String SKY_SERVICE_XML_TEMPLATE = "sky.service.xml.template";

   /**
    * The name of the Tabular Sky Service Template used by portal
    *
    */
   public static final String TABULAR_SKY_SERVICE_XML_TEMPLATE = "tabular.sky.service.xml.template";

   /**
    * The name of the Data Collection Service Template used by portal
    *
    */
   public static final String DATACOLLECTION_XML_TEMPLATE = "datacollection.xml.template";
   
   /**
    * The name of the Authority Template used by portal
    *
    */
   public static final String AUTHORITY_XML_TEMPLATE = "authority.xml.template";
   
   /**
    * The name of the Authority Template used by portal
    *
    */
   public static final String REGISTRY_XML_TEMPLATE = "registry.xml.template";

   /**
    * The name of the Authority Template used by portal
    *
    */
   public static final String RESOURCE_XML_TEMPLATE = "resource.xml.template";
   

	/**
	 * The name of the publsih registry's authority id used by this registry. (Currently not used)
	 *
	 */
	public static final String REGISTRY_AUTHORITY_ID = "registry.authority.id";
   
   /**
    * The name of the full registry's authority id used by this registry. (Currently not used)
    *
    */
   public static final String FULL_REGISTRY_AUTHORITY_ID = "full.registry.authority.id";
   
   /**
    * The name harvest template used by the server side for harvesting.
    *
    */
   public static final String HARVESTER_QUERY_TEMPLATE = "harvester.query.template";
   

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
		if (DEBUG_FLAG) System.out.println("RegistryConfig.getJndiProperty()") ;
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
	 *         &lt;env-entry-name&gt;org.astrogrid.registry.config&lt;/env-entry-name&gt;
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
		if (DEBUG_FLAG) System.out.println("RegistryConfig.loadConfig()") ;
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
		if (DEBUG_FLAG) System.out.println("RegistryConfig.loadConfig()") ;
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
	 * The publish registry authority id
	 *
	 */
	private static String authority_id = null ;

	/**
	 * Static method to get the publish registry's authority id.
	 *
	 */
	public static String getAuthorityID() {
		//
		// Try reading our config property.
		if ((null == authority_id) || (authority_id.length() <= 0))
			{
			if (DEBUG_FLAG) System.out.println("getAuthorityID()") ;
			if (DEBUG_FLAG) System.out.println("  Trying config property '" + REGISTRY_AUTHORITY_ID + "'") ;
         authority_id = getProperty(REGISTRY_AUTHORITY_ID) ;
			if (DEBUG_FLAG) System.out.println("  Config property result : " + authority_id) ;
			}
		//
		// Try reading our system property.
		if ((null == authority_id) || (authority_id.length() <= 0))
			{
			String name = CONFIG_NAME + "." + REGISTRY_AUTHORITY_ID ;
			if (DEBUG_FLAG) System.out.println("getAuthorityID()") ;
			if (DEBUG_FLAG) System.out.println("  Trying system property '" + name + "'") ;
         authority_id = System.getProperty(name) ;
			if (DEBUG_FLAG) System.out.println("  System property result : " + authority_id) ;
		}
		return authority_id ;
   }
   
	/**
	 * The file and location of the registry file.
	 *
	 */
	private static String regFile = null ;

	/**
	 * Method to return a File object of the current registry file.
	 *
	 */
	public static File getRegistryFile() {
		if (DEBUG_FLAG) System.out.println("getRegistryFile()") ;
		//
		// Try reading our config property.
		if ((null == regFile) || (regFile.length() <= 0))
			{
			if (DEBUG_FLAG) System.out.println("getRegistryFile()") ;
			if (DEBUG_FLAG) System.out.println("  Trying config property '" + REGISTRY_FILE + "'") ;
         regFile = getProperty(REGISTRY_FILE);
			if (null != regFile) regFile = regFile.trim();
			if (DEBUG_FLAG) System.out.println("  Config property result : " + regFile);
		}
      File fi = new File(regFile);
      return fi;
   }
   
   /**
    * 
    */
   private static String harvestQueryFile = null;
   
   /**
    *
    */
   public static File getHarvestQueryTemplate() {
      if (DEBUG_FLAG) System.out.println("getRegistryFile()") ;
      //
      // Try reading our config property.
      if ((null == harvestQueryFile) || (harvestQueryFile.length() <= 0))
         {
         if (DEBUG_FLAG) System.out.println("getHarvestQueryTemplate()") ;
         if (DEBUG_FLAG) System.out.println("  Trying config property '" + HARVESTER_QUERY_TEMPLATE + "'") ;
         harvestQueryFile = getProperty(HARVESTER_QUERY_TEMPLATE);
         if (null != harvestQueryFile) harvestQueryFile = harvestQueryFile.trim();
         if (DEBUG_FLAG) System.out.println("  Config property result : " + harvestQueryFile);
      }
      File fi = new File(harvestQueryFile);
      return fi;
   }
   
/*
 * Template Section
 * @author Kevin Benson
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */   

   /**
   * Organisatin Template file and location
   *
   */
   private static String regOrganisationTemplate = null ;

   /**
   * Static method to get our Organisation template
   *
   */
   public static File getRegistryOrganisationTemplate() {
    //
    // Try reading our config property.
    if ((null == regOrganisationTemplate) || (regOrganisationTemplate.length() <= 0))
       {
       if (DEBUG_FLAG) System.out.println("getRegistryOrganisationTemplate()") ;
       if (DEBUG_FLAG) System.out.println("  Trying config property '" + ORGANISATION_XML_TEMPLATE + "'") ;
       regOrganisationTemplate = getProperty(ORGANISATION_XML_TEMPLATE);
       if (null != regOrganisationTemplate) regOrganisationTemplate = regOrganisationTemplate.trim();
       if (DEBUG_FLAG) System.out.println("  Config property result : " + regOrganisationTemplate);
    }
    File fi = new File(regOrganisationTemplate);
    return fi;
   }
   
   /**
   * Organisatin Template file and location
   *
   */
   private static String regRegistryTemplate = null ;

   /**
   * Static method to get our Organisation template
   *
   */
   public static File getRegistryTemplate() {
    //
    // Try reading our config property.
    if ((null == regRegistryTemplate) || (regRegistryTemplate.length() <= 0))
       {
       if (DEBUG_FLAG) System.out.println("getRegistryTemplate()") ;
       if (DEBUG_FLAG) System.out.println("  Trying config property '" + REGISTRY_XML_TEMPLATE + "'") ;
       regRegistryTemplate = getProperty(REGISTRY_XML_TEMPLATE);
       if (null != regRegistryTemplate) regRegistryTemplate = regRegistryTemplate.trim();
       if (DEBUG_FLAG) System.out.println("  Config property result : " + regRegistryTemplate);
    }
    File fi = new File(regRegistryTemplate);
    return fi;
   }
   
   /**
   * Organisatin Template file and location
   *
   */
   private static String regResourceTemplate = null ;

   /**
   * Static method to get our Organisation template
   *
   */
   public static File getResourceTemplate() {
    //
    // Try reading our config property.
    if ((null == regResourceTemplate) || (regResourceTemplate.length() <= 0))
       {
       if (DEBUG_FLAG) System.out.println("getResourceTemplate()") ;
       if (DEBUG_FLAG) System.out.println("  Trying config property '" + RESOURCE_XML_TEMPLATE + "'") ;
       regResourceTemplate = getProperty(RESOURCE_XML_TEMPLATE);
       if (null != regResourceTemplate) regResourceTemplate = regResourceTemplate.trim();
       if (DEBUG_FLAG) System.out.println("  Config property result : " + regResourceTemplate);
    }
    File fi = new File(regResourceTemplate);
    return fi;
   }
   
   

   /**
   * Static method to get our Authority template
   *
   */
   private static String regAuthorityTemplate = null ;

   /**
    * Static method to get Authority template
    *
    */
   public static File getRegistryAuthorityTemplate() {
      //
      // Try reading our config property.
      if ((null == regAuthorityTemplate) || (regAuthorityTemplate.length() <= 0))
         {
         if (DEBUG_FLAG) System.out.println("getRegistryAuthorityTemplate()") ;
         if (DEBUG_FLAG) System.out.println("  Trying config property '" + AUTHORITY_XML_TEMPLATE + "'") ;
         regAuthorityTemplate = getProperty(AUTHORITY_XML_TEMPLATE);
         if (null != regAuthorityTemplate) regAuthorityTemplate = regAuthorityTemplate.trim();
         if (DEBUG_FLAG) System.out.println("  Config property result : " + regAuthorityTemplate);
      }
      File fi = new File(regAuthorityTemplate);
      return fi;
     }

     /**
      * Our file and location of the Data Collection template
      *
      */
       private static String regDataCollTemplate = null ;

   /**
   * Get our Data Collection Service Template
   *
   */
   public static File getDataCollectionTemplate() {
        //
        // Try reading our config property.
        if ((null == regDataCollTemplate) || (regDataCollTemplate.length() <= 0))
           {
           if (DEBUG_FLAG) System.out.println("getDataCollectionTemplate()") ;
           if (DEBUG_FLAG) System.out.println("  Trying config property '" + DATACOLLECTION_XML_TEMPLATE + "'") ;
           regDataCollTemplate = getProperty(DATACOLLECTION_XML_TEMPLATE);
           if (null != regDataCollTemplate) regDataCollTemplate = regDataCollTemplate.trim();
           if (DEBUG_FLAG) System.out.println("  Config property result : " + regDataCollTemplate);

        }
        System.out.println("the regdatacolltemplate = " + regDataCollTemplate);
        File fi = new File(regDataCollTemplate);
        return fi;
   }
       
   /**
   * Our Sky Service Template
   *
   */
   private static String regSkyServiceTemplate = null ;

   /**
   * Sddtatic method to get our Sky Service Template
   *
   */
   public static File getSkyServiceTemplate() {
         //
         // Try reading our config property.
         if ((null == regSkyServiceTemplate) || (regSkyServiceTemplate.length() <= 0))
            {
            if (DEBUG_FLAG) System.out.println("getSkyServiceTemplate()") ;
            if (DEBUG_FLAG) System.out.println("  Trying config property '" + SKY_SERVICE_XML_TEMPLATE + "'") ;
            regSkyServiceTemplate = getProperty(SKY_SERVICE_XML_TEMPLATE);
            if (null != regSkyServiceTemplate) regSkyServiceTemplate = regSkyServiceTemplate.trim();
            if (DEBUG_FLAG) System.out.println("  Config property result : " + regSkyServiceTemplate);
         }
         File fi = new File(regSkyServiceTemplate);
         return fi;
   }     

      /**
       * Our local manager URL.
       *
       */
        private static String regServiceTemplate = null ;

   /**
   * Static method to get our Service Template
   *
   */
   public static File getServiceTemplate() {
         //
         // Try reading our config property.
         if ((null == regServiceTemplate) || (regServiceTemplate.length() <= 0))
            {
            if (DEBUG_FLAG) System.out.println("getServiceTemplate()") ;
            if (DEBUG_FLAG) System.out.println("  Trying config property '" + SERVICE_XML_TEMPLATE + "'") ;
            regServiceTemplate = getProperty(SERVICE_XML_TEMPLATE);
            if (null != regServiceTemplate) regServiceTemplate = regServiceTemplate.trim();
            if (DEBUG_FLAG) System.out.println("  Config property result : " + regServiceTemplate);
         }
         File fi = new File(regServiceTemplate);
         return fi;
   }
        
   /**
   * Our Tabular Sky Service Template
   *
   */
   private static String regTabSkyService = null ;

   /**
   * Static method to get Tabular Sky Service Template
   *
   */
   public static File getTabularSkyServiceTemplate() {
         //
         // Try reading our config property.
         if ((null == regTabSkyService) || (regTabSkyService.length() <= 0))
            {
            if (DEBUG_FLAG) System.out.println("getTabularSkyServiceTemplate()") ;
            if (DEBUG_FLAG) System.out.println("  Trying config property '" + TABULAR_SKY_SERVICE_XML_TEMPLATE + "'") ;
            regTabSkyService = getProperty(TABULAR_SKY_SERVICE_XML_TEMPLATE);
            if (null != regTabSkyService) regTabSkyService = regTabSkyService.trim();
            if (DEBUG_FLAG) System.out.println("  Config property result : " + regTabSkyService);
         }
         File fi = new File(regTabSkyService);
         return fi;
   }               
     

}