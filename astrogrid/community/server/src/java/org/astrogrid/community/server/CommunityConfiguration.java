package org.astrogrid.community.server;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;

/**
 * The configured properties of the community service.
 * This class abstracts the configuration mechanism and collates the formal
 * names of properties.
 *
 * @author Guy Rixon
 */
public class CommunityConfiguration {
    
  /**
   * Reveals the authority under which the community service is published.
   */
  public String getPublishingAuthority() {
      
    // There are three possible formats for the property: 
    // just the authority;
    // authority plus path (path = IVOA "resource key");
    // full IVORN including ivo:// prefix.
    
    String p0 = getConfig().getString("org.astrogrid.community.ident");
    String p1 = (p0.startsWith("ivo://"))? p0.substring(6) : p0;
    int slash = p1.indexOf('/');
    return (slash == -1)? p1 : p1.substring(0, slash);
  }

  /**
   * Overrides the authority under which the community service is published.
   */
  public void setPublishingAuthority(String s) {
    getConfig().setProperty("org.astrogrid.community.ident", s);
  }
  
  /**
   * Reveals the URL (HTTPS) at which the community is installed).
   */
  public URL getBaseUrlHttps() {
    try {
      return new URL(getConfig().getString("org.astrogrid.vosi.baseurlsecure"));
    } catch (MalformedURLException e) {
      throw new RuntimeException(
          "The community's base URL is mis-configured. " +
          "Set the property org.astrogrid.vosi.baseurlsecure to the URL " +
          "(beginning https://) at which the community is installed.", e
      );
    }
  }

  /**
   * Reveals the URL (plain HTTP) at which the community is installed).
   */
  public URL getBaseUrlHttp() {
    try {
      return new URL(getConfig().getString("org.astrogrid.vosi.baseurl"));
    } catch (MalformedURLException e) {
      throw new RuntimeException(
          "The community's base URL is mis-configured. " +
          "Set the property org.astrogrid.vosi.baseurl to the URL " +
          "(beginning http://) at which the community is installed.", e
      );
    }
  }

  /**
   * Reveals the IVORN for the VOSpace service holding the community's
   * home spaces.
   */
  public URI getVoSpaceIvorn() {
    try {
      return new URI(getConfig().getString("org.astrogrid.community.default.vospace"));
    }
    catch (URISyntaxException e) {
      throw new RuntimeException(
          "The IVORN for the community's VOSpace is mis-configured. " +
          "Set the property org.astrogrid.community.default.vospace to the IVO " +
          "identifer (URI starting ivo://) for the VOSpace service of choice.", e
      );
    }
  }

  /**
   * Overrides the IVORN for the VOSpace service holding the community's
   * home spaces.
   */
  public void setVoSpaceIvorn(String s) {
    getConfig().setProperty("org.astrogrid.community.default.vospace", s);
  }

  /**
   * Reveals the SOAP endpoint for querying the preferred registry.
   */
  public URL getRegistryQueryUrl() {
    return getConfig().getUrl("org.astrogrid.registry.query.endpoint");
  }

  /**
   * Reveals the SOAP endpoint for querying the alternate registry.
   */
  public URL getRegistryQueryAlternateUrl() {
    return getConfig().getUrl("org.astrogrid.registry.query.altendpoint");
  }

  /**
   * Reveals the URL of the configuration file for the community database.
   */
  public URL getDatabaseConfigurationUrl() {
    return getConfig().getUrl("org.astrogrid.community.dbconfigurl");
  }

  /**
   * Overrides the URL of the configuration file for the community database.
   */
  public void setDatabaseConfigurationUrl(URL u) {
    getConfig().setProperty("org.astrogrid.community.dbconfigurl", u.toString());
  }

  /**
   * Reveals the directory holding the credentials for community members.
   */
  public File getCredentialDirectory() {
    return new File(getConfig().getString("org.astrogrid.community.myproxy"));
  }

  /**
   * Overrides the directory holding the credentials for community members.
   */
  public void setCredentialDirectory(File f) {
    getConfig().setProperty("org.astrogrid.community.myproxy", f.getAbsolutePath());
  }

  /**
   * Reveals the file holding the certificate authority's certificate.
   */
  public File getCaCertificateFile() {
    return new File(getConfig().getString("org.astrogrid.community.cacert"));
  }

  /**
   * Reveals the file holding the certificate authority's private key.
   */
  public File getCaKeyFile() {
    return new File(getConfig().getString("org.astrogrid.community.cakey"));
  }

  /**
   * Reveals the file holding the certificate authority's serial-number record.
   */
  public File getCaSerialNumberFile() {
    return new File(getConfig().getString("org.astrogrid.community.caserial"));
  }

  /**
   * Reveals the underlying configuration-object.
   * This method encapsulates the method calls to find the
   * configuration singleton.
   */
  private Config getConfig() {
    return SimpleConfig.getSingleton();
  }

}
