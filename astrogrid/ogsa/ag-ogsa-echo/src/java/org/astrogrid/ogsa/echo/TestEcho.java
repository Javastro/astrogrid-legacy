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
public class TestEcho {

  public static void main (String[] args) throws Exception {

    if (args.length != 2) {
      System.err.println("usage: echo <string> <factory-URL>");
      return;
    }

    try {
      // Get access to the factory port.  Use the GridServiceFactory
      // wrapper to the port to simplify the interface of the
      // createService method.
      OGSIServiceLocator factoryLocator = new OGSIServiceLocator();
      Factory factory = factoryLocator.getFactoryPort(new URL(args[1]));
      GridServiceFactory gridFactory = new GridServiceFactory(factory);
        
      // Create a service instance and locate its echo port.
      // Set a 10-minute timeout.
      GregorianCalendar timeout = new GregorianCalendar();
      timeout.add(GregorianCalendar.MINUTE, 10);
      LocatorType instanceLocator = gridFactory.createService(timeout);
      EchoServiceGridLocator portLocator = new EchoServiceGridLocator();
      EchoPortType port = portLocator.getEchoPortType(instanceLocator);
        
      // Invoke the echo port.
      System.out.println(port.echo(args[0]));
    }
    catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
