/* $Id: ConfigurationKeys.java,v 1.1 2003/08/28 15:52:00 mch Exp $
 * Created on 19-Aug-2003
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 */
package org.astrogrid.datacenter.impl;

/**
 * Interface of constant key values.
 * <p>
 * Can be used to lookup associated values in a {@link Configuration} object
 * <p>
 * These constants were factored out of the {@link DTC} class, so they would still be accessible after {@link DTC} was made package-private.
 * @author Noel Winstanley nw@jb.man.ac.uk 20-Aug-2003
 *@see Configuration
 *@sea Configurable
 */
public interface ConfigurationKeys {
   public static final String
   /** Three letter acronym for this subsystem within the overall AstroGrid system...
    *  "DTC" stands for DaTaCenter  */
   SUBSYSTEM_ACRONYM = "DTC";

   public static final String
   /** Configuration file for this component. */
   CONFIG_FILENAME = "ASTROGRID_datacenterconfig.xml";

   public static final String
   /** DatasetAgent category within the component's configuration */
   DATASETAGENT_CATEGORY = "DATASETAGENT",
   /** Key within component's configuration signifying whether the web service request
    *  document is to be parsed with validation turned on or off*/
   DATASETAGENT_PARSER_VALIDATION = "PARSER.VALIDATION";


   public static final String
   /** Monitor category within the component's configuration */
   MONITOR_CATEGORY = "MONITOR",
   /** Key within component's configuration signifying the xml request template for
    *  input to the JobMonitor */
   MONITOR_REQUEST_TEMPLATE = "TEMPLATE.REQUEST";


   public static final String
   /** Votable category within the component's configuration */
   VOTABLE_CATEGORY = "VOTABLE",
   /** Key within component's configuration identifying the Votable factory implementation class */
   VOTABLE_FACTORY = "FACTORY";



   public static final String
   /** Job category within the component's configuration */
   JOB_CATEGORY = "JOB",
   /** Key within component's configuration signifying JNDI location of Job datasource */
   JOB_DATASOURCE_LOCATION = "DATASOURCE",
   /** Key within component's configuration signifying name of Job table */
   JOB_TABLENAME = "TABLENAME",
   /** Key within component's configuration identifying the Job factory implementation class */
   JOB_FACTORY = "FACTORY";



   public static final String
   /** MySpace category within the component's configuration */
   MYSPACE_CATEGORY = "MYSPACE",
   /** Key within component's configuration signifying directory
    *  where VOTables are to be written to */
   MYSPACE_CACHE_DIRECTORY = "CACHE_DIRECTORY",
   /** Key within component's configuration identifying the MySpaceManager service location */
   MYSPACE_URL = "URL",
   /** Key within component's configuration identifying the MySpace factory implementation class */
   MYSPACE_FACTORY = "FACTORY",
   /** Key within component's configuration signifying the xml request template for
    *  input to the MySpace manager */
   MYSPACE_REQUEST_TEMPLATE = "REQUEST_TEMPLATE";

   public static final String
   /** Catalog category within the component's configuration */
   CATALOG_CATEGORY = "CATALOG",
   /** Key within component's configuration identifying the default query factory implementation class */
   CATALOG_DEFAULT_QUERYFACTORY = ".QUERYFACTORY",
   /** Key within component's configuration identifying the default datasource */
   CATALOG_DEFAULT_DATASOURCE = ".DATASOURCE",
   /** Key within component's configuration identifying the default query factory implementation class */
   CATALOG_USNOB_QUERYFACTORY = "USNOB.QUERYFACTORY",
   /** Key within component's configuration identifying the default datasource */
   CATALOG_USNOB_DATASOURCE = "USNOB.DATASOURCE";

   public static final String
   /** UCD category within the component's configuration */
   UCD_CATEGORY = "UCD";

   public static final String
   /** Key within component's configuration identifying separator between database and table */
   DATABASE_TABLE_SEPARATOR = "DATABASE.TABLE.SEPARATOR";

}
/*
 * $Log: ConfigurationKeys.java,v $
 * Revision 1.1  2003/08/28 15:52:00  mch
 * New Configuration package
 *
 * Revision 1.1  2003/08/20 14:42:59  nw
 * added a configuration package -
 * wraps the existing DTC class, and provides somewhere to
 * manage dynamically loaded factories.
 *
*/
