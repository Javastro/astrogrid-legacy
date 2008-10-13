/*$Id: ConeResources.java,v 1.6 2008/10/13 10:51:35 clq2 Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.dataservice.service.cone.v1_0;

import java.io.IOException;
import org.astrogrid.cfg.ConfigFactory;
//import org.astrogrid.dataservice.service.ServletHelper;
import org.astrogrid.dataservice.metadata.MetadataHelper;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.tableserver.metadata.TableInfo;

/** Returns Registry resources for a service type that indicates that this
 * service can provide cone searches
 */
public class ConeResources {
   
   /**
    * Returns Capability XML for cone searchable tables in the 
    * specified catalog.
    */
   public static String getConeCapabilities(String catalogName) throws IOException {
      String coneList = "";
      String coneConfig = ConfigFactory.getCommonConfig().getString(
         "datacenter.implements.conesearch","false");
      if ("true".equals(coneConfig.toLowerCase())) {
         String catalogID = 
            TableMetaDocInterpreter.getCatalogIDForName(catalogName);
         TableInfo[] coneTables = 
            TableMetaDocInterpreter.getConesearchableTables(catalogID);
         for (int i = 0; i < coneTables.length; i++) {
            coneList = coneList + getVoConeCapability(
                  coneTables[i].getCatalogName(),
                  coneTables[i].getName());
         }
      }
      return coneList;
   }

   protected static String getVoConeCapability(String catName, String tabName) 
      throws IOException {

      // Get max row limit 
      int maxRows;
      double maxRadius;
      String maxRowStr = ConfigFactory.getCommonConfig().getString(
            "datacenter.max.return");
      if ((maxRowStr == null) || ("".equals(maxRowStr))) {
         //NB shouldn't get here
         maxRows = 999999999; // A very big and obviously silly number
      }
      else if (maxRowStr.equals("0")) {
         // No limit - use a dummy value
         maxRows = 999999999; // A very big and obviously silly number
      }
      else {
         try {
            maxRows = Integer.parseInt(maxRowStr);
         }
         catch (NumberFormatException nfe) {
            throw new IOException("Datacenter is misconfigured: datacenter.max.return has illegal value '" + maxRowStr + "'");
         }
      }
      // Get max search radius
      String maxRadStr = ConfigFactory.getCommonConfig().getString(
            "conesearch.radius.limit");
      if ((maxRadStr == null) || ("".equals(maxRadStr))) {
         //NB shouldn't get here
         maxRadius = 180.0;
      }
      else {
         try {
            maxRadius = Double.parseDouble(maxRadStr);
         }
         catch (NumberFormatException nfe) {
            throw new IOException("Datacenter is misconfigured: conesearch.radius.limit has illegal value '" + maxRadStr + "'");
         }
      }

      String coneCap = 
         "  <capability xsi:type=\"cs:ConeSearch\" standardID=\"ivo://ivoa.net/std/ConeSearch\">\n" +
         "    <description>" + catName + 
             ", " +tabName +": cone search</description>\n" +
         "    <interface xsi:type=\"vs:ParamHTTP\" role=\"std\">\n" + 
         "      <accessURL use=\"base\">" + 
            MetadataHelper.getInstallationBaseURL()+"SubmitCone?DSACAT="+
               catName+"&amp;DSATAB="+tabName+"&amp;" + "</accessURL>\n" +
         "    </interface>\n" +
         "    <maxSR>" + Double.toString(maxRadius) + "</maxSR>\n" +
         "    <maxRecords>" + Integer.toString(maxRows) + "</maxRecords>\n" +
         "    <verbosity>false</verbosity>\n" +
         "    <testQuery>\n" +
         "      <ra>96.0</ra>\n" +
         "      <dec>5.0</dec>\n" +
         "      <sr>0.001</sr>\n" +
         "    </testQuery>\n" +
         "  </capability>\n";

      return coneCap;
   }
}
