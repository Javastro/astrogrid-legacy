package org.astrogrid.mySpace.mySpaceManager;

import java.io.*;
import java.util.*;

/**
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 2.
 */

public class MySpaceManager
{  private RegistryManager reg = new RegistryManager();
                                    // MySpace registry for this mySpace.

//
// Constructors.

/**
 * Construct a mySpace using an existing registry.
 */

   public MySpaceManager(String registryName)
   {  

//
//   Attempt to open the registry.

      reg = new RegistryManager(registryName);
   }

/**
 * Construct a mySpace using a new registry.
 */

   public MySpaceManager(String registryName, String dummy)
   {  

//
//   Attempt to create the registry.

      reg = new RegistryManager(registryName, dummy);
   }

//
// Finaliser.

/**
 * Finish with the mySpace.  The registry is written to disk.
 */

   public void finalize()
   {

//
//   Close the registry.

      reg.finalize();
   }
}
