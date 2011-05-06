package org.astrogrid.dataservice;

import java.net.URI;
import java.net.URISyntaxException;
import org.astrogrid.config.PropertyNotFoundException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.tableserver.metadata.TableInfo;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;

/**
 * A type-safe configuration for DSA.
 * <p>
 * This class reads the configuration properties and returns their values in
 * the appropriate types. Where the useful information depends on combinations
 * of properties, this class makes the combinations.
 *
 * @author Guy Rixon
 */
public class Configuration {

  public static URI getBaseUri() throws DsaConfigurationException {
    try {
      return new URI(getProperty("datacenter.url"));
    }
    catch (URISyntaxException ex) {
      throw new DsaConfigurationException("datacenter.url is not a valid URI", ex);
    }
  }

  public static URI getSecureBaseUri() throws DsaConfigurationException {
    try {
      return new URI(getProperty("datacenter.url.secure"));
    }
    catch (URISyntaxException ex) {
      throw new DsaConfigurationException("datacenter.url.secure is not a valid URI", ex);
    }
  }

  public static URI getConeSearchBaseUri() throws DsaConfigurationException {
    return (Boolean.getBoolean(getProperty("cone.search.secure"))?
        getSecureBaseUri() :
        getBaseUri());
  }
  
  public static String getProperty(String name) throws DsaConfigurationException {
    try {
      String p = SimpleConfig.getProperty(name);
      if (p.trim().length() > 0) {
        return p;
      }
      else {
        throw new DsaConfigurationException(name + " is set to an empty string");
      }
    }
    catch (PropertyNotFoundException e) {
      throw new DsaConfigurationException(name + " is not set");
    }
  }

  public static String getProperty(String name, String defaultValue) {
    try {
      return getProperty(name);
    }
    catch (DsaConfigurationException e) {
      return defaultValue;
    }
  }

  public static boolean isConeSearchEnabled() throws DsaConfigurationException {
    String s = SimpleConfig.getProperty("datacenter.implements.conesearch", "true");
    return Boolean.parseBoolean(s.trim());
  }

  public static boolean isMultiConeEnabled() throws DsaConfigurationException {
    String s = SimpleConfig.getProperty("datacenter.implements.multicone", "true");
    return Boolean.parseBoolean(s.trim());
  }

  public static boolean hasConesearchableTables() throws DsaConfigurationException {
    try {
      return TableMetaDocInterpreter.getConesearchableTables().length > 0;
    } catch (MetadataException e) {
      throw new DsaConfigurationException("The database configuration is unreadable", e);
    }
  }

  public static TableInfo[] getConesearchableTables() throws DsaConfigurationException {
    try {
      return TableMetaDocInterpreter.getConesearchableTables();
    } catch (MetadataException e) {
      throw new DsaConfigurationException("The database configuration is unreadable", e);
    }
  }

  public static boolean isConeSearchSecure() throws DsaConfigurationException {
    String value = getProperty("cone.search.secure", "false");
    return Boolean.parseBoolean(value);
  }

  public static int getConeSearchRowLimit() throws DsaConfigurationException {
    String value = getProperty("datacenter.max.return", "999999999");
    return Integer.parseInt(value);
  }

  public static double getConeSearchRadiusLimit() throws DsaConfigurationException {
    String value = getProperty("conesearch.radius.limit", "180.0");
    return Double.parseDouble(value);
  }

  public static boolean isTapSecure() throws DsaConfigurationException {
    String value = getProperty("tap.secure", "false");
    return Boolean.parseBoolean(value);
  }

  public static boolean isCecSecure() throws DsaConfigurationException {
    String value = getProperty("cea.secure", "false");
    return Boolean.parseBoolean(value);
  }

  public static String getTestTable() throws DsaConfigurationException {
    return getProperty("datacenter.self-test.table");
  }

  public static String getTestColumn1() throws DsaConfigurationException {
    return getProperty("datacenter.self-test.column1");
  }

  public static String getTestColumn2() throws DsaConfigurationException {
    return getProperty("datacenter.self-test.column2");
  }

  public static String getAdqlStylesheetName() throws DsaConfigurationException {
    return getProperty("datacenter.sqlmaker.xslt");
  }

  public static void setAdqlStylesheetName(String s) {
    SimpleConfig.setProperty("datacenter.sqlmaker.xslt", s);
  }

  public static String getSqlMakerClassName() throws DsaConfigurationException {
    return getProperty("datacenter.querier.plugin.sql.translator", "org.astrogrid.tableserver.jdbc.AdqlSqlMaker2");
  }

}
