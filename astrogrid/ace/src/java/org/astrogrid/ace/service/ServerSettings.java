/*
 $Id: ServerSettings.java,v 1.1.1.1 2003/08/25 18:36:14 mch Exp $

 Date         Author      Changes
 8 Nov 2002   M Hill      Created

 (c) Copyright...
 */

package org.astrogrid.ace.service;

import org.astrogrid.tools.util.Options;
import java.awt.Point;
import java.awt.Dimension;
import java.io.IOException;

/**
 * Persistent set of options, using the util.Options class for persistence
 *
 * @author M Hill
 */

public class ServerSettings
{
   public static String FILE_NAME         = "server.cfg";
   
   public static String WORKING_PARENT    = "WorkingParent";
   public static String COMMON_DIR        = "CommonDirectory";
   public static String APP_EXE           = "Application";
   
   private static Options instance = newOptions();

   private static Options newOptions()
   {
      try
      {
         return new Options(FILE_NAME, "ACE server settings");
      }
      catch (IOException ioe)
      {
         //it'll crash now when running, so need to make this fatal
         throw new RuntimeException(""+ioe);
      }
   }
   
   public static Options getInstance()
   {
      return instance;
   }

   /**
    * Meant really just to create an example, rather than a version that
    * will work anywhere
    */
   public static void setDefaults()
   {
      //comment out as required

      // FOR EDINBURGH
      instance.setProperty(APP_EXE,       "/fannich/mch/sextractor2.2.2/sex");
      instance.setProperty(WORKING_PARENT,"/fannich/mch/sex/");
      instance.setProperty(COMMON_DIR,    "/fannich/mch/sex/");

      // FOR CAMBRIDGE
      //setApplicationPath("/data/cass123a/kea/bin/sex");
      //setTempDir("/data/cass123a/kea/AvoDemo/WORKING/tmp");
      //setWorkingDir("/data/cass123a/kea/AvoDemo/WORKING/");

      //FOR WINDOWS
      //setApplicationPath("C:\\Program Files\\sextractor\\analyse.exe");
      //setWorkingParent("C:\\Documents and Settings\\Administrator\\astrogrid\\sex");
      //setCommonDir("C:\\Documents and Settings\\Administrator\\astrogrid\\sex");
   }

   
   
}

