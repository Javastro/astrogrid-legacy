package org.astrogrid.security;

import java.net.URL;
import junit.framework.TestCase;
import org.apache.axis.client.Call;
import org.astrogrid.security.sample.SamplePortType;
import org.astrogrid.security.sample.SampleServiceLocator;

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

  public void testAll() throws Exception {

    // Make the URI scheme 'local' known to java.net.URL.
    Call.initialize();

    // Make a delegate using the local transport. The service
    // name security-sample matches the name declared in
    // server-config.wsdd.
    URL endpoint = new URL("local:///security-sample");
    SampleServiceLocator locator = new SampleServiceLocator();
    SamplePortType delegate = locator.getSecuritySamplePort(endpoint);

    // Exercise the delegate, the handler chains and the service
    // implementation.  This simulates a call to a real web-service.
    String name = delegate.whoAmI();
    System.out.println("Returned name: " + name);
  }
}