/*
 * $Id: CastorDatabaseFactory.java,v 1.1 2003/09/15 05:45:42 pah Exp $
 * 
 * Created on 09-Sep-2003 by pah
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.community.common.db;

import java.io.IOException;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.DatabaseNotFoundException;
import org.exolab.castor.jdo.JDO;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.util.Logger;

import org.astrogrid.community.common.CommunityConstants;
import org.astrogrid.community.common.Config;
import org.astrogrid.community.common.ConfigLoader;

/**
 * A factory to get Castor database objects. This is implemented as a singleton, and there is only one central database object returned at the moment.
 * @author pah
 * @version $Name:  $
 * @since iteration3
 */
public class CastorDatabaseFactory {

   private Config config = ConfigLoader.LoadConfig();
   private JDO jdo;
   /**
    * The reference to the single instance of this class; 
    */
   private static CastorDatabaseFactory instance = null;

   private Logger logger;

   /**
    * The database mapping for castor.
    */
   private Mapping mapping;

   /**
    * The castor database reference.
    */
   private Database database;

   /**
    * private constructor for singleton.
    */
   private CastorDatabaseFactory() {

      config = ConfigLoader.LoadConfig();
      init();

   }

   /**
    * Get the single instance of the class.
    * @return the instance of this class.
    */
   public static CastorDatabaseFactory getInstance() {
      if (instance == null) {
         instance = new CastorDatabaseFactory();

      }

      return instance;
   }

   /**
    * Get the community database reference.
   * @return the castor database reference.
   */
   public Database getDatabase() {
      return database;
   }

   /**
   * initialize the database.
   */
   private void init() {
      // Create our log writer.
      logger = new Logger(System.out).setPrefix("castor");
      //
      // Load our object mapping.
      mapping = new Mapping(getClass().getClassLoader());
      try {
         mapping.loadMapping(
            config.getProperty(CommunityConstants.MAPPING_CONFIG_KEY));

         //
         // Create our JDO engine.
         jdo = new JDO();
         jdo.setLogWriter(logger);
         jdo.setConfiguration(
            config.getProperty(CommunityConstants.DATABASE_CONFIG_KEY));
         jdo.setDatabaseName(
            config.getProperty(CommunityConstants.DATABASE_NAME_KEY));
         //
         // Create our database connection.
         database = jdo.getDatabase();
      }
      catch (DatabaseNotFoundException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (MappingException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (PersistenceException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }
}
