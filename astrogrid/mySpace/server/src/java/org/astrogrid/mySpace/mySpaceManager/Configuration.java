package org.astrogrid.mySpace.mySpaceManager;

import org.astrogrid.Configurator;
import org.astrogrid.i18n.*;

import org.astrogrid.mySpace.mySpaceManager.MMC;

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
 * @since Iteration 4.
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

   private static boolean DEBUG = false;

//
// Specify whether the MySpace manager will check whether the requested
// operation is permitted by calling an AstroGrid permissions server.
// If permissions are disabled then operations are always permitted.

   private static boolean CHECKPERMISSIONS = true;

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
 * Create a <code>Configuration</code> object and read the configuration
 * options from the properties file.
 *
 * @param dummy A dummy argument to specify this constructor.  By
 *  convention this argument has the value "properties");
 */

   public Configuration (String dummy)
   {  
//
//   Obtain the DEBUG option.

      String debugProperty = MMC.getProperty(MMC.DEBUG, MMC.CATLOG);

      DEBUG = false;

      if (debugProperty.equalsIgnoreCase("true") )
      {  DEBUG = true;
      }

//
//   Obtain the CHECKPERMISSIONS option.

      String checkPermissionsProperty =
        MMC.getProperty(MMC.CHECKPERMISSIONS, MMC.CATLOG);

      CHECKPERMISSIONS = false;

      if (checkPermissionsProperty.equalsIgnoreCase("true") )
      {  CHECKPERMISSIONS = true;
      }

//
//   Obtain the SERVERDEPLOYMENT option.

      String serverDeploymentProperty =
        MMC.getProperty(MMC.SERVERDEPLOYMENT, MMC.CATLOG);

      SERVERDEPLOYMENT = INTERNALSERVERS;

      if (serverDeploymentProperty.equalsIgnoreCase("SEPARATESERVERS") )
      {  SERVERDEPLOYMENT = SEPARATESERVERS;
      }
      else if (serverDeploymentProperty.equalsIgnoreCase("MANAGERONLY") )
      {  SERVERDEPLOYMENT = MANAGERONLY;
      }
   }

/**
 * Create a <code>Configuration</code> object and set the configuration
 * options.
 *
 * @param debug Flag indicating whether de-bugging output is to be
 *   produced.
 * @param checkPermissions Flag indicating whether the MySpaceManger is
 *   to make checks against an external AstroGrid permissions server to
 *   determine whether each requested operation is permitted.  If
 *   checkPermissions is set to false all operations are permitted and
 *   no checks are made.  This mode is primarily useful during software
 *   development.
 * @param serverDeployment Flag indicating how the servers are to be
 *   deployed.  The permitted values are: Configuration.SEPARATESERVERS,
 *   Configuration.INTERNALSERVERS and Configuration.MANAGERONLY
 */

   public Configuration (boolean debug, boolean checkPermissions,
     int serverDeployment)
   {  this.DEBUG = debug;
      this.CHECKPERMISSIONS = checkPermissions;

      if ((serverDeployment == SEPARATESERVERS) ||
          (serverDeployment == INTERNALSERVERS) ||
          (serverDeployment == MANAGERONLY))
      {  this.SERVERDEPLOYMENT = serverDeployment;
      }
      else
      {  this.SERVERDEPLOYMENT = INTERNALSERVERS;
      }
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

//
// ----------------------------------------------------------------------
//
// Other methods.

/**
 * Reset a <code>Configuration</code> object.  The configuration options
 * are reset to the initial, hard-wired values that they had when the
 * object was created.
 */

   public void reset ()
   {  this.DEBUG = false;
      this.CHECKPERMISSIONS = true;
      this.SERVERDEPLOYMENT = INTERNALSERVERS;
   }
}
