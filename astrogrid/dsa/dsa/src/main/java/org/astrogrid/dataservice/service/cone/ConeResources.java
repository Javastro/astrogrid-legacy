/*$Id: ConeResources.java,v 1.5 2010/03/25 10:25:53 gtr Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.dataservice.service.cone;

import java.io.IOException;
import org.astrogrid.dataservice.Configuration;
import org.astrogrid.dataservice.DsaConfigurationException;
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
   public static String getConeCapabilities(String catalogName) throws IOException,
                                                                       DsaConfigurationException {
      String coneList = "";
      if (Configuration.isConeSearchEnabled()) {
         String catalogID = 
            TableMetaDocInterpreter.getCatalogIDForName(catalogName);
         TableInfo[] coneTables = 
            TableMetaDocInterpreter.getConesearchableTables(catalogID);
         for (int i = 0; i < coneTables.length; i++) {
            coneList = coneList +
                       getVoConeCapability(coneTables[i].getCatalogName(),
                                           coneTables[i].getName());
         }
      }
      return coneList;
   }

  protected static String getVoConeCapability(String catName,
                                              String tabName) throws IOException,
                                                                     DsaConfigurationException {
    // There is one URL for secured cone-searches and
    // another for insecure searches. In any given installation, all the
    // cone-search endpoints will be insecure or all will be secure.
    if (Configuration.isConeSearchSecure()) {
      return String.format(
        "  <capability xsi:type='cs:ConeSearch'\n" +
        "      standardID='ivo://ivoa.net/std/ConeSearch'>\n" +
        "    <description>%s, %s: cone search</description>\n" +
        "    <interface xsi:type='vs:ParamHTTP' role='std'>\n" +
        "      <accessURL use='base'>%s/SubmitCone?DSACAT=%s&amp;DSATAB=%s&amp;</accessURL>\n" +
        "      <securityMethod standardID='ivo://ivoa.net/sso#tls-with-client-certificate'/>\n" +
        "    </interface>\n" +
        "    <maxSR>%f</maxSR>\n" +
        "    <maxRecords>%d</maxRecords>\n" +
        "    <verbosity>false</verbosity>\n" +
        "    <testQuery>\n" +
        "      <ra>96.0</ra>\n" +
        "      <dec>5.0</dec>\n" +
        "      <sr>0.001</sr>\n" +
        "    </testQuery>\n" +
        "  </capability>\n",
        catName,
        tabName,
        Configuration.getSecureBaseUri(),
        catName,
        tabName,
        Configuration.getConeSearchRadiusLimit(),
        Configuration.getConeSearchRowLimit());
     }
     else {
       return String.format(
         "  <capability xsi:type='cs:ConeSearch'\n" +
         "      standardID='ivo://ivoa.net/std/ConeSearch'>\n" +
         "    <description>%s, %s: cone search</description>\n" +
         "    <interface xsi:type='vs:ParamHTTP' role='std'>\n" +
         "      <accessURL use='base'>%s/SubmitCone?DSACAT=%s&amp;DSATAB=%s&amp;</accessURL>\n" +
         "    </interface>\n" +
         "    <maxSR>%f</maxSR>\n" +
         "    <maxRecords>%d</maxRecords>\n" +
         "    <verbosity>false</verbosity>\n" +
         "    <testQuery>\n" +
         "      <ra>96.0</ra>\n" +
         "      <dec>5.0</dec>\n" +
         "      <sr>0.001</sr>\n" +
         "    </testQuery>\n" +
         "  </capability>\n",
         catName,
         tabName,
         Configuration.getBaseUri(),
         catName,
         tabName,
         Configuration.getConeSearchRadiusLimit(),
         Configuration.getConeSearchRowLimit());
    }
      
  }
  
}
