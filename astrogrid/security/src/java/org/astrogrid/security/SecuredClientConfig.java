package org.astrogrid.security;

import org.apache.axis.SimpleTargetedChain;
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
  public SecuredClientConfig() {

    AxisClientCredentialHandler h = new AxisClientCredentialHandler();
    deployTransport("java",  new SimpleTargetedChain(h, new JavaSender(),  null));
    deployTransport("local", new SimpleTargetedChain(h, new LocalSender(), null));
    deployTransport("http",  new SimpleTargetedChain(h, new HTTPSender(),  null));
  }

}