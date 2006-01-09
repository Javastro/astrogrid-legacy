package org.astrogrid.applications.manager;

import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.SimpleConfig;

/**
 *
 * @author gtr
 */
public class DefaultMetadataServiceURLs implements DefaultMetadataService.URLs {
  private static final Log logger = LogFactory.getLog(DefaultMetadataServiceURLs.class);
  
      /** key to query config for the url of the registry template to use (optional, default='/CEARegistryTemplate.xml' on classpath) 
     * @see #registerDefaultVOProvider(MutablePicoContainer, Config)*/
    public static final String REGISTRY_TEMPLATE_URL  ="cea.registry.template.url";
    /** key to query config for the url of this services endpoint (optional, recommended, otherwise makes a best guess)
     * @see #registerDefaultVOProvider(MutablePicoContainer, Config)*/
    public static final String SERVICE_ENDPOINT_URL = "cea.service.endpoint.url";
    
    /**
     * Gets the URL for the registration template. The preferred URL is the
     * one set in the general configuration. If this is not set, or is not
     * set to a syntactically-valid URL, or is set to a URL that cannot be
     * opened (i.e. there is no file at the stated location), then a default
     * URL is returned, leading to a template file packed inside the web
     * application.
     *
     * @return The URL (never null).
     */
    public URL getRegistryTemplate() {
      try {
        // This throws and exception if the property is not set, and a different
        // exception if the property as string is not a URL.
        URL url = SimpleConfig.getSingleton().getUrl(REGISTRY_TEMPLATE_URL);
        
        // This throws an exception if the target of the URL is unavailable.
        // We don't need to use the connection here; we're just checking that
        // it will work.
        url.openConnection();
        return url;
      }
      catch (Exception e) {
        URL url = this.getClass().getResource("/CEARegistryTemplate.xml");
        logger.warn(REGISTRY_TEMPLATE_URL +
                    " does not lead to a registration-template file. " +
                    url.toString() +
                    " is the default template.");
        return url;
      }
    }
            
    public URL getServiceEndpoint() {
      try {
        return SimpleConfig.getSingleton().getUrl(SERVICE_ENDPOINT_URL,
                                                  new URL("http://localhost:8080/astrogrid-cea-server/services/CommonExecutionConnectorService"));
      }
      catch (Exception e) {
        return null;
      }
    }
}
