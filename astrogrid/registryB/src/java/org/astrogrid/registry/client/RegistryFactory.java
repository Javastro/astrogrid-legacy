package org.astrogrid.registry.client;


import org.astrogrid.registry.client.delegate.RegistryMockService;
import org.astrogrid.registry.client.delegate.RegistryService;
import org.astrogrid.registry.RegistryInterface;

import org.astrogrid.registry.client.stubbs.*;

import java.net.URL;

public class RegistryFactory {
   
   /**
    * Switch for our debug statements.
    *
    */
   private static final boolean DEBUG_FLAG = true ;
   
   public static RegistryInterface createService() throws Exception {
      return createService("http://localhost:8080/axis/services/Registry");
   }
   
   
   public static RegistryInterface createService(String targetEndPoint) throws Exception {
      if (DEBUG_FLAG) System.out.println("") ;
      if (DEBUG_FLAG) System.out.println("----\"----") ;
      if (DEBUG_FLAG) System.out.println("setUp()") ;
      if (DEBUG_FLAG) System.out.println("  Target : '" + targetEndPoint + "'") ;

      RegistryInterfaceService locator = null;
      RegistryInterface reg = null;
      //
      // Create our service locator.
      locator = new RegistryInterfaceServiceLocator();

      //
      // Create our service.
      reg = locator.getRegistry(new URL(targetEndPoint));
      return new RegistryService(reg);
   }

   public static RegistryInterface createMockService() throws Exception {
      return createMockService("http://localhost:8080/axis/services/Registry");
   }

   public static RegistryInterface createMockService(String targetEndPoint) throws Exception {
      if (DEBUG_FLAG) System.out.println("") ;
      if (DEBUG_FLAG) System.out.println("----\"----") ;
      if (DEBUG_FLAG) System.out.println("setUp()") ;
      if (DEBUG_FLAG) System.out.println("  Target : '" + targetEndPoint + "'") ;

      RegistryInterfaceService locator = null;
      RegistryInterface reg = null;
      //
      // Create our service locator.
      locator = new RegistryInterfaceServiceLocator();

      //
      // Create our service.
      reg = locator.getRegistry(new URL(targetEndPoint));
      return new RegistryMockService(reg);
   }
   
   
   
}
