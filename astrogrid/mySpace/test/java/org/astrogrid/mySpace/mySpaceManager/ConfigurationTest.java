package org.astrogrid.mySpace.mySpaceManager;

import junit.framework.*;
import java.util.*;

import org.astrogrid.Configurator;
import org.astrogrid.i18n.*;

import org.astrogrid.mySpace.mySpaceManager.MMC;
import org.astrogrid.mySpace.mySpaceManager.Configuration;

/**
 * Junit tests for the <code>Configuration</code> class.
 *
 * <p>
 * The <code>Configuration</code> class is quite simple and there is
 * little to test other than the constructors and set methods.
 *
 * @author A C Davenhall (Edinburgh)
 * @since Iteration 4.
 * @version Iteration 4.
 */

public class ConfigurationTest extends TestCase
{

/**
 * Standard constructor for JUnit test classes.
 */

   public ConfigurationTest (String name)
   {  super(name);
   }

/**
 * Test the default constructor.
 */

  public void testDefault()
   {  Configuration config = new Configuration();

      Assert.assertEquals(config.getDEBUG(), false);
      Assert.assertEquals(config.getCHECKPERMISSIONS(), true);
      Assert.assertEquals(config.getSERVERDEPLOYMENT(),
        config.INTERNALSERVERS);
   }


/**
 * Test the constructor which reads values from the properties file.
 * The Configuration values are tested against the values actually in
 * the properties file.
 */

  public void testProp()
   {  Configuration config = new Configuration("properties");

      String debugProperty = MMC.getProperty(MMC.DEBUG, MMC.CATLOG);
      boolean debug = false;
      if (debugProperty.equalsIgnoreCase("true") )
      {  debug = true;
      }

      String checkPermissionsProperty =
        MMC.getProperty(MMC.CHECKPERMISSIONS, MMC.CATLOG);
      boolean checkPermissions = false;
      if (checkPermissionsProperty.equalsIgnoreCase("true") )
      {  checkPermissions = true;
      }

      String serverDeploymentProperty =
        MMC.getProperty(MMC.SERVERDEPLOYMENT, MMC.CATLOG);
      int serverDeployment = config.INTERNALSERVERS;
      if (serverDeploymentProperty.equalsIgnoreCase("SEPARATESERVERS") )
      {  serverDeployment = config.SEPARATESERVERS;
      }
      else if (serverDeploymentProperty.equalsIgnoreCase("MANAGERONLY") )
      {  serverDeployment = config.MANAGERONLY;
      }


      Assert.assertEquals(config.getDEBUG(), debug);
      Assert.assertEquals(config.getCHECKPERMISSIONS(), checkPermissions);
      Assert.assertEquals(config.getSERVERDEPLOYMENT(), serverDeployment);
   }


/**
 * Test the constructor in which values are passed as arguments.
 */

  public void testSet()
   {  Configuration config = new Configuration(true, false,
        Configuration.MANAGERONLY);

      Assert.assertEquals(config.getDEBUG(), true);
      Assert.assertEquals(config.getCHECKPERMISSIONS(), false);
      Assert.assertEquals(config.getSERVERDEPLOYMENT(),
        config.MANAGERONLY);
   }

/**
 * Test the <code>reset</code> method.
 */

   public void testReset()
   {  Configuration config = new Configuration(true, false,
        Configuration.MANAGERONLY);

      config.reset();

      Assert.assertEquals(config.getDEBUG(), false);
      Assert.assertEquals(config.getCHECKPERMISSIONS(), true);
      Assert.assertEquals(config.getSERVERDEPLOYMENT(),
        config.INTERNALSERVERS);
   }


/**
 * Main method to run the class.
 */

   public static void main (String[] args)
   {
//    junit.textui.TestRunner.run (ConfigurationTest.class);
      junit.swingui.TestRunner.run (ConfigurationTest.class);
   }
}