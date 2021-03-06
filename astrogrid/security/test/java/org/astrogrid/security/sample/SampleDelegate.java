package org.astrogrid.security.sample;

import java.net.URL;
import org.apache.axis.client.Stub;
import org.astrogrid.security.AxisClientSecurityGuard;
import org.astrogrid.security.SecurityGuard;


/**
 * A delegate class for the sample secured service. This
 * delegate demonstrates how to use the security facade
 * in client software.
 *
 * @author Guy Rixon
 */
public class SampleDelegate {

  private URL endpoint;
  private SamplePortType proxy;


  /**
   * Constructs a new delegate to use a given Axis configuration
   * end service endpoint.
   *
   * @param config The configuration for the AxisEngine.
   * @param endpoint The service endpoint.
   */
  public SampleDelegate(URL endpoint) throws Exception {
    SampleServiceLocator locator = new SampleServiceLocator(AxisClientSecurityGuard.getEngineConfiguration());
    this.endpoint = endpoint;
    this.proxy = locator.getSamplePort(endpoint);
  }

  /**
   * Causes the service to echo the authenticated identity.
   *
   * @return The identity authenticated by the service.
   */
  public String whoAmI() throws Exception {
    return proxy.whoAmI();
  }

  /**
   * Sets properties that control the signing of outgoing messages.
   * These properties relate to the loading of a Java KeyStore object
   * from a file. This method encapsulates the configuration keys used
   * by WSS4J. The stub parameter may be obtained by casting an Axis
   * stub generated by WSDL2java to org.apache.axis.client.Stub.
   *
   * @param g The guard object holding the credentials.
   */
  public void setCredentials(SecurityGuard sg1) throws Exception {
    AxisClientSecurityGuard sg2 = new AxisClientSecurityGuard(sg1);
    sg2.configureStub((Stub)(this.proxy));
  }

}