package org.astrogrid.dataservice;

/**
 * An exception arising from a mistake in configuration of a DSA service.
 * The error messages are all prefixed by "Configuration error".
 * 
 * @author Guy Rixon
 */
public class DsaConfigurationException extends Exception {

  DsaConfigurationException(String message) {
    super("Configuration error: " + message);
  }

  DsaConfigurationException(String message, Throwable cause) {
    super("Configuration error: " + message, cause);
  }

  DsaConfigurationException(Throwable cause) {
    super("Configuration error", cause);
  }
}
