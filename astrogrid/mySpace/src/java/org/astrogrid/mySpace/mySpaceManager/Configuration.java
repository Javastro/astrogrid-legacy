package org.astrogrid.mySpace.mySpaceManager;

/**
 * The <code>Configuration</code> class controls the configuration of a
 * MySpace manager.
 *
 * <p>
 * The class has a number of static member variables.  The values of
 * these variables control configuration of the MySpace manager and
 * hence the way that it behaves.
 *
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 4.
 */

public class Configuration
{
//
//Public constants defining the permitted codes for the SERVERDEPLOYMENT.

   public static final int SEPARATESERVERS = 1;
   public static final int INTERNALSERVERS = 2;
   public static final int MANAGERONLY = 3;

//
// Specify whether de-bugging output is to be produced.

//   private static boolean DEBUG = false;
   private static boolean DEBUG = true;

//
// Specify whether the MySpace manager will check whether the requested
// operation is permitted by calling an AstroGrid permissions server.
// If permissions are disabled then operations are always permitted.

   private static boolean CHECKPERMISSIONS = false;

//
// Specify how the MySpace system is to be configured.  The options are
// as follows:
//
// SEPARATESERVERS
//   The Servers are run as separate Web services (a full deplyment).
//
// INTERNALSERVERS
//   The servers are incorporated within the Java program program
//   constituting the Manager.
//
// MANAGERONLY
//   The Manager is run without servers.  This mode is useful for
//   debugging only.  

   private static int SERVERDEPLOYMENT = INTERNALSERVERS;

//
// ----------------------------------------------------------------------
//
// Constructors.

/**
 * Create a <code>Configuration</code> object.
 */

   public Configuration ()
   {
   }

/**
 * Create a <code>Configuration</code> object and reset the member
 * variables.  A single dummy argument is passed.
 */

   public Configuration (String dummy)
   {  this.DEBUG = false;
      this.CHECKPERMISSIONS = false;
      this.SERVERDEPLOYMENT = INTERNALSERVERS;
   }

//
// ----------------------------------------------------------------------
//
// Get methods.

/**
 * Return the DEBUG success flag.
 */

   public boolean getDEBUG()
   {  return DEBUG;
   }

/**
 * Return the CHECKPERMISSIONS success flag.
 */

   public boolean getCHECKPERMISSIONS()
   {  return CHECKPERMISSIONS;
   }

/**
 * Return the SERVERDEPLOYMENT success flag.
 */

   public int getSERVERDEPLOYMENT()
   {  return SERVERDEPLOYMENT;
   }
}
