package org.astrogrid.security;

import java.io.File;
import java.net.URL;
import java.util.Properties;
import junit.framework.TestCase;
import org.apache.axis.AxisEngine;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.Call;
import org.apache.axis.client.Stub;
import org.apache.axis.handlers.soap.SOAPService;
import org.apache.axis.configuration.XMLStringProvider;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.components.crypto.CryptoFactory;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.astrogrid.security.sample.SampleDelegate;


/**
 * JUnit tests for end-to-end operation of the security facade in
 * a simulated web-service. Axis' "loop-back" mode is used, in
 * which all parts of the Axis handler-chain are exercise, but there
 * is no HTTP communication and hence no need to deploy the SUT
 * into a web-container.
 *
 * @author Guy Rixon
 */
public class EndToEndTest extends TestCase {

  public void testGoodCredentials() throws Exception {

    // Make the URI scheme 'local' known to java.net.URL.
    Call.initialize();

    // Sign on to unlock credentials. The guard holds the credentials.
    SecurityGuard g = new SecurityGuard();
    g.setSsoUsername("user");
    g.setSsoPassword("testing");
    g.setSsoKeyStore(new File("test-user-1-keystore.jks"));
    g.signOn();

    // Make a delegate using the local transport. The service
    // name SamplePort matches the name declared in
    // server-config.wsdd.
    URL endpoint = new URL("local:///SamplePort");
    SampleDelegate delegate = new SampleDelegate(endpoint);
    delegate.setCredentials(g);

    // Exercise the delegate, the handler chains and the service
    // implementation.  This simulates a call to a real web-service.
    String name = delegate.whoAmI();
    System.out.println("Returned name: " + name);
  }


  public void testMissingCredentials() throws Exception {

    // Make the URI scheme 'local' known to java.net.URL.
    Call.initialize();

    // Set the username but do not sign on. This causes the
    // client handlers to be invoked without a Crypto object;
    // they should deal with this.
    SecurityGuard g = new SecurityGuard();
    g.setSsoUsername("user");
    g.setSsoPassword("testing");

    // Make a delegate using the local transport. The service
    // name SamplePort matches the name declared in
    // server-config.wsdd.
    URL endpoint = new URL("local:///SamplePort");
    SampleDelegate delegate = new SampleDelegate(endpoint);
    try {
      delegate.setCredentials(g);
      fail("The SUT failed to trap the absence of credentials.");
    }
    catch(Exception e) {
      // This is expected.
    }
  }

}