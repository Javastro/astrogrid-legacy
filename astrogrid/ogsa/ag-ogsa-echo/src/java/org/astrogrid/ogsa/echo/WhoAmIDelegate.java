package org.astrogrid.ogsa.echo;

import java.net.URL;
import java.util.GregorianCalendar;
import javax.xml.rpc.Stub;
import org.astrogrid.ogsa.echo.EchoServiceLocator;
import org.astrogrid.ogsa.echo.EchoPortType;
import org.globus.axis.gsi.GSIConstants;
import org.globus.ogsa.impl.security.authentication.Constants;
import org.globus.ogsa.impl.security.authorization.NoAuthorization;
import org.globus.ogsa.utils.GridServiceFactory;
import org.gridforum.ogsi.Factory;
import org.gridforum.ogsi.LocatorType;
import org.gridforum.ogsi.OGSIServiceLocator;

public class WhoAmIDelegate {
  
  private URL serviceFactory;


  public WhoAmIDelegate (String factoryHandle) throws Exception {
    this.serviceFactory = new URL(factoryHandle);
  }

  
  /**
   * Returns the identity derived from the caller's
   * security credentials, as reported by a 
   * remote service, or null if the service
   * cannot find the identity.
   */
  public String whoAmI () throws Exception {
    System.out.println("Entering whoAmi");
 
    // Get access to the factory port.  Use the GridServiceFactory
    // wrapper to the port to simplify the interface of the
    // createService method.
    OGSIServiceLocator factoryLocator = new OGSIServiceLocator();
    Factory factory = factoryLocator.getFactoryPort(this.serviceFactory);
    GridServiceFactory gridFactory = new GridServiceFactory(factory);
        
    // Create a service instance and locate its echo port.
    // Set a 10-minute timeout.
    GregorianCalendar timeout = new GregorianCalendar();
    timeout.add(GregorianCalendar.MINUTE, 10);
    LocatorType instanceLocator = gridFactory.createService(timeout);
    EchoServiceGridLocator portLocator = new EchoServiceGridLocator();
    EchoPortType port = portLocator.getEchoPortType(instanceLocator);
    
    // Set up authenticated access: make credentials available in
    // calls to the service.
    Stub portStub = (Stub)port;
    portStub._setProperty(GSIConstants.GSI_MODE,
                          GSIConstants.GSI_MODE_LIMITED_DELEG);
    portStub._setProperty(Constants.MSG_SEC_TYPE,
                          Constants.SIGNATURE);
    portStub._setProperty(Constants.AUTHORIZATION,
                          NoAuthorization.getInstance());
                                 
    // Invoke the echo port.
    return port.whoAmI();
  }
}
