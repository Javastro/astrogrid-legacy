package org.astrogrid.ogsa.echo;

import java.net.URL;
import java.util.GregorianCalendar;
import org.astrogrid.ogsa.echo.EchoServiceLocator;
import org.astrogrid.ogsa.echo.EchoPortType;
import org.gridforum.ogsi.Factory;
import org.gridforum.ogsi.LocatorType;
import org.gridforum.ogsi.OGSIServiceLocator;
import org.globus.ogsa.utils.GridServiceFactory;

/**
 * Exercises an echo service with a single invocation.
 */
public class TestWhoAmI {

  public static void main (String[] args) throws Exception {

    if (args.length != 1) {
      System.err.println("usage: java org.astrogrid.ogsa.echo.TestWhoAmI <factory-URL>");
      return;
    }

    try {
      whoAmI(args[0]);
    }
    catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
  
  
  private static void whoAmI (String url) throws Exception {
    System.out.println("Client: service factory: " + url);
    
    // Get access to the factory port.  Use the GridServiceFactory
    // wrapper to the port to simplify the interface of the
    // createService method.
    OGSIServiceLocator factoryLocator = new OGSIServiceLocator();
    Factory factory = factoryLocator.getFactoryPort(new URL(url));
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
    SecurityHelper.configureSecureCall(port);
                               
    // Invoke the echo port.
    System.out.println("Client: result from service: " + port.whoAmI());
    System.out.println("Client: the test finished without exceptions.");
  }
}
