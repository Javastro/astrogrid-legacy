/*
   $Id: AceContext.java,v 1.1.1.1 2003/08/25 18:36:14 mch Exp $

   Date       Author      Changes
   1 Oct 2002 M Hill      Created

   (c) Copyright...
*/

package org.astrogrid.ace.service;

import org.astrogrid.common.ucd.CdsUcdDictionary;
import org.astrogrid.common.ucd.UcdDictionary;
import org.astrogrid.common.units.CdsUnitDictionary;
import org.astrogrid.common.units.UnitDictionary;
import org.astrogrid.common.wrapper.ContextFactory;
import org.astrogrid.common.wrapper.LocalTermDictionary;
import org.astrogrid.common.wrapper.ParameterBundle;
import java.util.Properties;
import java.io.*;

/**
 * Describes the environmnet under which the Astrogrid
 * Catalogue Extractor (ACE) application, which wraps the SExtractor application,
 * will run.  @see ContextFactory.  A Singleton - there should be only one
 * instance, available from the class method getInstance().

 *
 * @version %I%
 * @author M Hill
 */

public class AceContext implements ContextFactory
{
   private static final AceContext INSTANCE = new AceContext();

   private String namespace = null;
   private String schema = null;
   //private File applicationPath = null;
   //private File workingParent = null;  //parent dir for instance working directories
   //private File commonDir = null; //directory for finding common config files, etc

   private AceContext()
   {
      //load from file
      File f = new File(ServerSettings.FILE_NAME);
      if (!f.exists())
      {
         ServerSettings.setDefaults();
      }

//      setWorkingParent( ServerSettings.getInstance().getProperty(ServerSettings.WORKING_PARENT));
//      setCommonDir( ServerSettings.getInstance().getProperty(ServerSettings.COMMON_DIR));
//      setApplicationPath( ServerSettings.getInstance().getProperty(ServerSettings.APP_EXE));
   }

   /**
    * Returns a reference to an implementation of a UcdDirectory, which will
    * be used by the service to validate, etc UCDs
    */
   public UcdDictionary getUcdDictionary()
   {
      return CdsUcdDictionary.getInstance();
   }

   /**
    * Returns a reference to an implementation of a UnitDirectory, which will
    * be used by the service to validate, etc units
    */
   public UnitDictionary getUnitDictionary()
   {
      return CdsUnitDictionary.getInstance();
   }

   /**
    * Returns an instance of a LocalTermDictionary, used to translate between
    * the public terms used within the XML document, and the terms used by the
    * native program
    */
   public LocalTermDictionary getLocalTermDictionary()
   {
      return AceDictionary.getInstance();
   }

   public String getNamespace()
   {
      return namespace;
   }

   /**
    * The schema could be used for populating the local dictionary, etc
    */
   public String getSchema()
   {
      return schema;
   }

   /**
    * Sets the application path.  Throws exception if the program cannot
    * be found
    */
   public void setApplicationPath(String path) throws IllegalArgumentException
   {
      ServerSettings.getInstance().setProperty(
         ServerSettings.APP_EXE,
         ""+validateFile(path)
      );
   }

   /**
    * Sets the common directory where temporary files are created for a
    * service instance.  Throws exception if the path cannot
    * be found
    */
   public void setCommonDir(String path) throws IllegalArgumentException
   {
      ServerSettings.getInstance().setProperty(
         ServerSettings.COMMON_DIR,
         ""+validatePath(path)
      );
   }

   /**
    * Sets the parent of teh working directories where the program will be run and temporary
    * files created.  The actual directory for every instance will be a subdirectory
    * of this one.
    * .  Throws exception if the path cannot
    * be found
    */
   public void setWorkingParent(String path) throws IllegalArgumentException
   {
      ServerSettings.getInstance().setProperty(
         ServerSettings.WORKING_PARENT,
         ""+validatePath(path)
      );
   }

   /**
    * returns the location of the application - the script/executable
    * called to run it.
    */
   public File getApplicationPath()
   {
      return validateFile(ServerSettings.getInstance().getProperty(ServerSettings.APP_EXE));
   }

   /**
    * Returns the path to where common files exist
    */
   public File getCommonDir()
   {
      return validatePath(ServerSettings.getInstance().getProperty(ServerSettings.COMMON_DIR));
   }

   /**
    * Returns the parent path of where the extraction program will run.
    */
   public File getWorkingParent()
   {
      return validatePath(ServerSettings.getInstance().getProperty(ServerSettings.WORKING_PARENT));
   }

   /**
    * Returns a file if the given string is OK and represents a path, otherwise
    * throws an exception
    */
   private File validatePath(String pathname)
   {
      if (pathname == null)
      {
         throw new IllegalArgumentException("Path is null");
      }
      File f = new File(pathname);
      if (!f.isDirectory())
      {
         throw new IllegalArgumentException("Path "+pathname+" does not refer to a directory");
      }
      return f;
   }

   /**
    * Returns a file if the given string is OK and represents a filo, otherwise
    * throws an exception
    */
   private File validateFile(String filename)
   {
      if (filename == null)
      {
         throw new IllegalArgumentException("Filename is null");
      }
      File f = new File(filename);
      //if (!f.exists()) sometimes this throws false even if the program does exist.
      //{
      //   throw new IllegalArgumentException("Path "+filename+" does not refer to a file");
      //}
      return f;
   }

   /**
    * Creates a new AceParameterBundle from the given arguments
    */
   public ParameterBundle newParameterBundle(String id, String units)
   {
      return new AceParameterBundle(id, units);
   }

   public static AceContext getInstance()
   {
      return INSTANCE;
   }

}

