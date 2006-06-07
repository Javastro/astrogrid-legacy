package org.astrogrid.security;

import java.util.Hashtable;
import org.apache.axis.SimpleTargetedChain;
import org.apache.axis.ConfigurationException;
import org.apache.axis.configuration.SimpleProvider;
import org.apache.axis.transport.http.HTTPSender;
import org.apache.axis.transport.java.JavaSender;
import org.apache.axis.transport.local.LocalSender;

/**
 * A SimpleProvider set up with hardcoded configuration for a client.
 * The transports "java", "local" and "http" are configured as handler chains
 * with their appropriate pivot handlers. In each chain, AstroGrid's security
 * handler is set as the request handler and no response handlers are set.
 *
 * @author Guy Rixon
 */
public class SecuredClientConfig extends SimpleProvider {

  /**
   * Constructor a configuration for a client engine with security handlers.
   */
  public SecuredClientConfig() throws ConfigurationException {

    // This is the handler for signing messages.
    AxisClientCredentialHandler h = new AxisClientCredentialHandler();
    
    // These are the message transports. "Local" is for unit tests.
    // For each transport we make a chain including the handler which delivers
    // the messages and the handler that signs them.
    deployTransport("java",  new SimpleTargetedChain(h, new JavaSender(),  null));
    deployTransport("local", new SimpleTargetedChain(h, new LocalSender(), null));
    deployTransport("http",  new SimpleTargetedChain(h, new HTTPSender(),  null));
    
    // These options stop the message-sending handlers from fiddling with the
    // XML after the messages are signed. If the XML is tampered with the
    // signatures become invalid.
    Hashtable options = super.getGlobalOptions();
    if (options == null) {
      options = new Hashtable();
      super.setGlobalOptions(options);
    }
    options.put("enableNamespacePrefixOptimization", Boolean.FALSE);
    options.put("disablePrettyXML", Boolean.TRUE);
  }

}