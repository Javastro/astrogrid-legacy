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
// Specify whether de-bugging output is to be produced.

//   private static boolean DEBUG = false;
   private static boolean DEBUG = true;

//
// Specify whether the MySpace manager will check whether the requested
// operation is permitted by calling an AstroGrid permissions server.
// If permissions are disabled then operations are always permitted.

   private static boolean CHECKPERMISSIONS = false;

//
// Specify whether the manager is run as a full MySpace system (with 
// the manager and one or more servers as separate Web Services) or a
// `mini-MySpace system' in which the manager has a single server
// included within it in a single Java application.  In this latter case
// the manager and its internal server run as a single Web Service.

   private static boolean INTERNALSERVER = true;

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
      this.INTERNALSERVER = true;
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
 * Return the INTERNALSERVER success flag.
 */

   public boolean getINTERNALSERVER()
   {  return INTERNALSERVER;
   }
}
