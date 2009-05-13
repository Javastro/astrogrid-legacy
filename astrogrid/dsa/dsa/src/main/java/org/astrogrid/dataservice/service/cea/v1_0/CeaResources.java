/*
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.service.cea.v1_0;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.contracts.StandardIds;
import org.astrogrid.dataservice.metadata.VoDescriptionServer;
import org.astrogrid.dataservice.metadata.VoResourcePlugin;
import org.astrogrid.dataservice.metadata.VoResourceSupportBase;
import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.tableserver.metadata.TableInfo;
import org.astrogrid.dataservice.service.cea.DatacenterApplicationDescription;

/**
 * Serves the CEA capabilities and application registrations.  
 * @author M Hill, K Andrews
 */

public class CeaResources extends VoResourceSupportBase {
   protected static Log log = LogFactory.getLog(VoDescriptionServer.class);
   
   /**
    * Returns a CeaServiceType resource element
    * @deprecated  Used with old-style push registration. Not relevant now
    * that we are using new IVOA registration conventions, and NO LONGER
    * SUPPORTED.
    *
    */
   public static String getCeaAppResource(String catalogName) 
      throws IOException, MetadataException {

      // Set up relationship tag
      String[] end = new String[1];
      end[0] = VoResourceSupportBase.makeIvorn(catalogName);
      // We are allowed "service-for" but not "served-by" in the 
      // relationship tag, so let's make the best of it with related-to
      // (which at least allows you to find the server actually supporting
      // this CEA Application type)
      String relTag = VoResourceSupportBase.makeRelationshipTag(
          "related-to", end);
      String core = VoResourceSupportBase.makeDublinCore(
            catalogName+"/ceaApplication","Cea Application",relTag);
      return  (
         VoResourceSupportBase.openVoResourceElement_1_0("cea:CeaApplication") +
         core + 
         getCeaApplicationDefinition(catalogName) + 
         VoResourceSupportBase.closeVoResourceElement_1_0() + "\n"
       );
   }

   /**
    * Returns just the applicationDefinition section of a CeaApplication 
    * resource element.
    */
   public static String getCeaApplicationDefinition(String catalogName) 
      throws IOException, MetadataException {

      String conableTables="", coneParams="", coneInters="";
      String multiConeParams="", multiConeInters="";
      //String appId = 
       //  VoResourceSupportBase.makeIvorn(catalogName+"/ceaApplication");

      String catalogID = 
         TableMetaDocInterpreter.getCatalogIDForName(catalogName);

      /*
      // NOTE: At the moment, cone and multicone are *always* enabled
      // via CEA if any conesearchable tables are defined.  
      boolean coneEnabled = false;
      boolean multiconeEnabled = false;
      String coneConfig = ConfigFactory.getCommonConfig().getString(
                         "datacenter.implements.conesearch","false");
      String multiconeConfig = ConfigFactory.getCommonConfig().getString(
                         "datacenter.implements.multicone");
      if ("true".equals(coneConfig.toLowerCase())) {
         coneEnabled = true;
      }
      if ("true".equals(multiconeConfig.toLowerCase())) {
         multiconeEnabled = true;
      }

      if (coneEnabled == true) {
      */
         TableInfo[] tables =
              TableMetaDocInterpreter.getConesearchableTables(catalogID);
         if (tables.length > 0) {
            // We have some conesearchable tables in this catalog,
            // so generate CEA-cone interfaces for them
            conableTables = 
               // Conesearch table
                "      <parameterDefinition id='CatTable' type='text'>\n"+
                "        <name>Table</name>\n"+
                "        <description>Which table should be conesearched"+
                "</description>\n"+
               /*
               "<defaultValue>" + catalogName +
                        "." + tables[0].getName() + "</ceapd:DefaultValue>\n"+
               */
               "        <optionList>\n";

            for (int i = 0; i < tables.length; i++) {
               String fullTable = catalogName + "." + tables[i].getName();
               conableTables = conableTables + "          <optionVal>" +
                  fullTable + "</optionVal>\n";
            }
            conableTables = conableTables + 
               "        </optionList>\n    </parameterDefinition>\n";

            coneParams = conableTables+ 
                  // Conesearch RA
                  "      <parameterDefinition id='RA' type='RA'>\n"+
                  "        <name>RA</name>\n"+
                  "        <description>Right-Ascension of cone search centre"+
                  "</description>\n"+
                  "        <unit>deg</unit>\n"+
                  "        <ucd>POS_RA_MAIN</ucd>\n"+
                  "      </parameterDefinition>\n"+

                  // Conesearch Dec
                  "      <parameterDefinition id='DEC' type='Dec'>\n"+
                  "        <name>DEC</name>\n"+
                  "        <description>Declination of cone search centre"+
                  "</description>\n"+
                  "        <unit>deg</unit>\n"+
                  "        <ucd>POS_DEC_MAIN</ucd>\n"+
                  "      </parameterDefinition>\n"+
                  // Conesearch Radius
                  "      <parameterDefinition id='Radius' type='real'>\n"+
                  "        <name>Radius</name>\n"+
                  "        <description>Radius of cone search area"+
                  "</description>\n"+
                  "        <unit>deg</unit>\n"+
                  "        <ucd>PHYS_SIZE_RADIUS</ucd>\n"+
                  "      </parameterDefinition>\n";


            coneInters=
                  "      <interfaceDefinition id='"+
                           DatacenterApplicationDescription.CONE_IFACE+"'>\n"+
                  "        <input>\n"+
                  "          <pref ref='CatTable'/>\n"+
                  "          <pref ref='RA'/>\n"+
                  "          <pref ref='DEC'/>\n"+
                  "          <pref ref='Radius'/>\n"+
                  "          <pref ref='Format'/>\n"+
                  "        </input>\n"+
                  "        <output>\n"+
                  "          <pref ref='Result'/>\n"+
                  "        </output>\n"+
                  "      </interfaceDefinition>\n";

            /*
            if (multiconeEnabled == true) {
            */
               multiConeParams = 
                     // Multicone RA expression
                     "      <parameterDefinition id='RA_Column_Name' type='text'>\n"+
                     "        <name>RA column name</name>\n"+
                     "        <description>Name for input Right-Ascension column (or expression): column data in degrees"+
                     "</description>\n"+
                     "        <unit>deg</unit>\n"+
                     "      </parameterDefinition>\n"+

                     // Multicone Dec expression
                     "      <parameterDefinition id='Dec_Column_Name' type='text'>\n"+
                     "        <name>DEC column name</name>\n"+
                     "        <description>Name for input Declination column (or expression): column data in degrees"+
                     "</description>\n"+
                     "      </parameterDefinition>\n"+

                     // Multicone find mode 
                     "      <parameterDefinition id='Find_Mode' type='text'>\n"+
                     "        <name>Find Mode</name>\n"+
                     "        <description>Find mode for matches: BEST or ALL"+
                     "</description>\n"+
                     "        <defaultValue>ALL</defaultValue>\n"+
                     "        <optionList>\n"+
                     "          <optionVal>BEST</optionVal>\n"+
                     "          <optionVal>ALL</optionVal>\n"+
                     "        </optionList>\n"+
                     "      </parameterDefinition>\n"+

                     // Multicone input VOTable url
                     "      <parameterDefinition id='Input_VOTable' type='text'>\n"+
                     "        <name>Input VOTable</name>\n"+
                     "        <description>Input VOTable, containing Right Ascension and Declination columns, for matching against"+
                     "</description>\n"+
                     "      </parameterDefinition>\n";

               multiConeInters=
                     "      <interfaceDefinition id='"+
                           DatacenterApplicationDescription.MULTICONE_IFACE+"'>\n"+
                     "        <input>\n"+
                     "          <pref ref='CatTable'/>\n"+
                     "          <pref ref='Input_VOTable'/>\n"+
                     "          <pref ref='RA_Column_Name'/>\n"+
                     "          <pref ref='Dec_Column_Name'/>\n"+
                     "          <pref ref='Radius'/>\n"+
                     "          <pref ref='Find_Mode'/>\n"+
                     "        </input>\n"+
                     "        <output>\n"+
                     "          <pref ref='Result'/>\n"+
                     "        </output>\n"+
                     "      </interfaceDefinition>\n";
        /*
            }
         }
         */
      }


      /*
      // Set up relationship tag
      String[] end = new String[1];
      end[0] = VoResourceSupportBase.makeIvorn(catalogName);
      // We are allowed "service-for" but not "served-by" in the 
      // relationship tag, so let's make the best of it with related-to
      // (which at least allows you to find the server actually supporting
      // this CEA Application type)
      String relTag = VoResourceSupportBase.makeRelationshipTag(
          "related-to", end);

      String core = VoResourceSupportBase.makeDublinCore(catalogName+"/ceaApplication","Cea Application",relTag);

      String ceaApplication =
         VoResourceSupportBase.openVoResourceElement_1_0("cea:CeaApplication") + 
         core + 
         */

      return
         // Basic parameters used in the interfaces
         "  <applicationDefinition>\n"+
         "    <parameters>\n"+
               
         // Input ADQL query
         "      <parameterDefinition id='Query' type='ADQL'>\n"+
         "        <name>Query</name>\n"+
         "        <description>Astronomy Data Query Language that defines the search criteria</description>\n"+
         "      </parameterDefinition>\n"+

         // Outgoing query results 
         "      <parameterDefinition id='Result' type='text'>\n"+
         "        <name>Result</name>\n"+
         "        <description>Query results</description>\n"+
         "      </parameterDefinition>\n"+

         // Required results format
         "      <parameterDefinition id='Format' type='text'>\n"+
         "        <name>Format</name>\n"+
         "        <description>Return format for the results.</description>\n"+
         "        <defaultValue>VOTABLE</defaultValue>\n"+
         "        <optionList>\n"+
         "          <optionVal>VOTABLE</optionVal>\n"+
         "          <optionVal>VOTABLE-BINARY</optionVal>\n"+
         "          <optionVal>COMMA-SEPARATED</optionVal>\n"+
         "          <optionVal>HTML</optionVal>\n"+
         "        </optionList>\n"+
         "      </parameterDefinition>\n"+

         // Adding the conesearch entries if required
               coneParams +
               multiConeParams +
         "    </parameters>\n"+
            
         // Interfaces available
         "    <interfaces>\n"+
         "      <interfaceDefinition id='"+
                DatacenterApplicationDescription.ADQL_IFACE+"'>\n"+
         "        <input>\n"+
         "          <pref ref='Query'/>\n"+
         "          <pref ref='Format'/>\n"+
         "        </input>\n"+
         "        <output>\n"+
         "          <pref ref='Result'/>\n"+
         "        </output>\n"+
         "      </interfaceDefinition>\n"+
         // Adding the conesearch entries if required
         coneInters +
         multiConeInters +
         "    </interfaces>\n"+
         "  </applicationDefinition>\n";
         /*
         VoResourceSupportBase.closeVoResourceElement_1_0() + "\n";
         return ceaApplication;
         */
   }

   public static String getAppID(String catalogName) throws IOException {
      return VoResourceSupportBase.makeIvorn(catalogName+"/ceaApplication");
   }

   public static String getCeaServerCapabilities(String catalogName) throws IOException {
      String endpoint =
           ConfigFactory.getCommonConfig().getString("datacenter.url");
      if (!endpoint.endsWith("/")) {
         endpoint = endpoint + "/";  // Add trailing separator if missing
      }
      String appId = getAppID(catalogName);

      StringBuffer cap = new StringBuffer();
      cap.append("  <capability xsi:type=\"cea:CeaCapability\" standardID=\"" +
            StandardIds.CEA_1_0 + "\">\n");
      cap.append("    <description>Access to two applications: general ADQL query, and asynchronous cone-search where relevant/enabled.</description>\n");
      cap.append("    <interface  xsi:type=\"cea:CECInterface\">\n");
      cap.append("      <accessURL use='full'>" + endpoint + 
            "services/CommonExecutionConnectorService</accessURL>\n");
      cap.append("    </interface>\n");
      cap.append("    <managedApplications>\n");
      cap.append("      <ApplicationReference>" + appId + "</ApplicationReference>\n");
      cap.append("    </managedApplications>\n");
      cap.append("  </capability>\n");
      return cap.toString();
   }
}
