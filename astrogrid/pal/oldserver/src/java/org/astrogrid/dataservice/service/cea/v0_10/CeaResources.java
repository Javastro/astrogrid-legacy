/*
 * $Id: CeaResources.java,v 1.2 2008/02/14 15:02:00 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.service.cea.v0_10;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.metadata.VoDescriptionServer;
import org.astrogrid.dataservice.metadata.VoResourcePlugin;
import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.dataservice.metadata.v0_10.VoResourceSupport;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.tableserver.metadata.TableInfo;

/**
 * Serves the CEA resources.  Bit of a mangled fudge at the moment to get
 * the right stuff from the right bit into the right bit.
 * <p>
 * @author M Hill
 * @deprecated  Used with old-style push registration. Not relevant now
 * that we are using new IVOA registration conventions, and NO LONGER
 * SUPPORTED.
 */

public class CeaResources extends VoResourceSupport implements VoResourcePlugin {

   protected static Log log = LogFactory.getLog(VoDescriptionServer.class);
   
   /**
    * Returns a CeaServiceType resource element
    */
   public String getVoResource() throws IOException {
      String endpoint = 
         ConfigFactory.getCommonConfig().getString("datacenter.url");
      if (!endpoint.endsWith("/")) {
         endpoint = endpoint + "/";  // Add trailing separator if missing
      }
      String[] catalogNames = TableMetaDocInterpreter.getCatalogNames();
      String[] catalogDescs = TableMetaDocInterpreter.getCatalogDescriptions();       if (catalogNames.length == 0) {
         throw new MetadataException("Server error: no catalog or table metadata are defined for this DSA/catalog installation;  please check your metadoc file and/or configuration!");
      }
      StringBuffer ceaResources = new StringBuffer("");
      // If we get here, have at least one catalog.
      for (int i = 0 ; i < catalogNames.length; i++) {
         String catalogName = catalogNames[i];
         if (catalogName == null) {
            throw new MetadataException("Server error: no catalog or table metadata are defined for this DSA/catalog installation;  please check your metadoc file and/or configuration!");
         }
         String catNamePrefix = "";
         if (catalogNames.length > 1) {
            //More than one catalog, need a cat-specific prefix
            catNamePrefix = catalogName+"/";
         }
         String ceaService =
            makeVoResourceElement(
                "cea:CeaServiceType",
                // Namespaces
                "xmlns:vs='http://www.ivoa.net/xml/VODataService/v0.5' " +
                "xmlns:cea='http://www.ivoa.net/xml/CEAService/v0.2' ",
                // Schema locations
                "http://www.ivoa.net/xml/VODataService/v0.5 http://software.astrogrid.org/schema/vo-resource-types/VODataService/v0.5/VODataService.xsd" + " " +
                "http://www.ivoa.net/xml/CEAService/v0.2 http://software.astrogrid.org/schema/vo-resource-types/CEAService/v0.2/CEAService.xsd"
                ) +
            makeCore(catNamePrefix+"ceaService","")+
            "<interface xsi:type='vs:WebService'>"+
               "<accessURL use='full'>"+ endpoint + 
               "services/CommonExecutionConnectorService"+
               "</accessURL>"+
            "</interface>"+
            //reference to the application that this serves
            "<cea:ManagedApplications>"+
               "<cea:ApplicationReference>"+
                  makeId(catNamePrefix+"ceaApplication")+
               "</cea:ApplicationReference>"+
            "</cea:ManagedApplications>"+
            "</"+VORESOURCE_ELEMENT+">";
         
         String coneParams = ""; 
         String coneInters = ""; 
         String multiConeParams = ""; 
         String multiConeInters = ""; 

         // NOTE: At the moment, cone and multicone are *always* enabled
         // via CEA if any conesearchable tables are defined.
         /*
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
            String catalogID =
               TableMetaDocInterpreter.getCatalogIDForName(catalogName);
            TableInfo[] tables =
               TableMetaDocInterpreter.getConesearchableTables(catalogID);
            if (tables.length == 0) {
               coneParams = "";
               coneInters = "";
            }
            else {
               coneParams = 
               "<cea:ParameterDefinition name='CatTable' type='text'>\n"+
                  "<ceapd:UI_Name>Search Table</ceapd:UI_Name>\n"+
                  "<ceapd:UI_Description>Which table should be conesearched</ceapd:UI_Description>\n"+
                  /*
                  "<ceapd:DefaultValue>" + tables[0].getCatalogName() + 
                           "." + tables[0].getName() + "</ceapd:DefaultValue>\n"+
                  */
                  "<ceapd:OptionList>\n";

               for (int j = 0; j < tables.length; j++) {
                  String fullTable = tables[j].getCatalogName() + "." +
                     tables[j].getName();
                  coneParams = coneParams + "<ceapd:OptionVal>" +
                     fullTable + "</ceapd:OptionVal>\n";
               }
               coneParams = coneParams +
                  "</ceapd:OptionList>\n"+
               "</cea:ParameterDefinition>\n"+
               "<cea:ParameterDefinition name='RA' type='double'>\n"+
                  "<ceapd:UI_Name>RA</ceapd:UI_Name>\n"+
                  "<ceapd:UI_Description>Right-Ascension of cone</ceapd:UI_Description>\n"+
                  "<ceapd:UCD>POS_RA_MAIN</ceapd:UCD>\n"+
                  "<ceapd:Units>deg</ceapd:Units>\n"+
               "</cea:ParameterDefinition>\n"+
               "<cea:ParameterDefinition name='DEC' type='double'>\n"+
                  "<ceapd:UI_Name>DEC</ceapd:UI_Name>\n"+
                  "<ceapd:UI_Description>Declination of cone</ceapd:UI_Description>\n"+
                  "<ceapd:UCD>POS_DEC_MAIN</ceapd:UCD>\n"+
                  "<ceapd:Units>deg</ceapd:Units>\n"+
               "</cea:ParameterDefinition>\n"+
               "<cea:ParameterDefinition name='Radius' type='double'>\n"+
                  "<ceapd:UI_Name>Radius</ceapd:UI_Name>\n"+
                  "<ceapd:UI_Description>Radius of cone</ceapd:UI_Description>\n"+
                  "<ceapd:UCD>PHYS_SIZE_RADIUS</ceapd:UCD>\n"+
                  "<ceapd:Units>deg</ceapd:Units>\n"+
               "</cea:ParameterDefinition>\n";

               coneInters = 
               "<ceab:Interface name='cone'>\n"+
                  "<ceab:input>\n"+
                     "<ceab:pref maxoccurs='1' minoccurs='1' ref='CatTable'/>\n"+
                     "<ceab:pref maxoccurs='1' minoccurs='1' ref='RA'/>\n"+
                     "<ceab:pref maxoccurs='1' minoccurs='1' ref='DEC'/>\n"+
                     "<ceab:pref maxoccurs='1' minoccurs='1' ref='Radius'/>\n"+
                     "<ceab:pref maxoccurs='1' minoccurs='1' ref='Format'/>\n"+
                  "</ceab:input>\n"+
                  "<ceab:output>\n"+
                     "<ceab:pref maxoccurs='1' minoccurs='1' ref='Result'/>\n"+
                  "</ceab:output>\n"+
               "</ceab:Interface>\n";


               /*
               if (multiconeEnabled == true) {
               */
                  multiConeParams = 
                     // Multicone RA expression
                     "      <cea:ParameterDefinition name='RA_Column_Name' type='text'>\n"+
                     "        <ceapd:UI_Name>RA column name</ceapd:UI_Name>\n"+
                     "        <ceapd:UI_Description>Name for input Right-Ascension column (or expression): column data in degrees"+
                     "</ceapd:UI_Description>\n"+
                     "        <ceapd:Units>deg</ceapd:Units>\n"+
                     "      </cea:ParameterDefinition>\n"+

                     // Multicone Dec expression
                     "      <cea:ParameterDefinition name='Dec_Column_Name' type='text'>\n"+
                     "        <ceapd:UI_Name>Dec column name</ceapd:UI_Name>\n"+
                     "        <ceapd:UI_Description>Name for input Declination column (or expression): column data in degrees"+
                     "</ceapd:UI_Description>\n"+
                     "      </cea:ParameterDefinition>\n"+

                     // Multicone find mode 
                     "      <cea:ParameterDefinition name='Find_Mode' type='text'>\n"+
                     "        <ceapd:UI_Name>Find Mode</ceapd:UI_Name>\n"+
                     "        <ceapd:UI_Description>Find mode for matches: BEST or ALL"+
                     "</ceapd:UI_Description>\n"+
                     "<ceapd:DefaultValue>ALL</ceapd:DefaultValue>\n"+
                     "<ceapd:OptionList>\n"+
                          "<ceapd:OptionVal>BEST</ceapd:OptionVal>\n"+
                          "<ceapd:OptionVal>ALL</ceapd:OptionVal>\n"+
                     "</ceapd:OptionList>\n"+
                     "      </cea:ParameterDefinition>\n"+

                     // Multicone input VOTable url
                     "      <cea:ParameterDefinition name='Input_VOTable' type='text'>\n"+
                     "        <ceapd:UI_Name>Input VOTable</ceapd:UI_Name>\n"+
                     "        <ceapd:UI_Description>Input VOTable, containing Right Ascension and Declination columns, for matching against)"+
                     "</ceapd:UI_Description>\n"+
                     "      </cea:ParameterDefinition>\n";

                  multiConeInters=
                     "      <ceab:Interface name='multicone'>\n"+
                     "        <ceab:input>\n"+
                     "          <ceab:pref maxoccurs='1' minoccurs='1' ref='CatTable'/>\n"+
                     "          <ceab:pref maxoccurs='1' minoccurs='1' ref='Input_VOTable'/>\n"+
                     "          <ceab:pref maxoccurs='1' minoccurs='1' ref='RA_Column_Name'/>\n"+
                     "          <ceab:pref maxoccurs='1' minoccurs='1' ref='Dec_Column_Name'/>\n"+
                     "          <ceab:pref maxoccurs='1' minoccurs='1' ref='Radius'/>\n"+
                     "          <ceab:pref maxoccurs='1' minoccurs='0' ref='Find_Mode'/>\n"+
                     "        </ceab:input>\n"+
                     "        <ceab:output>\n"+
                     "          <ceab:pref maxoccurs='1' minoccurs='1' ref='Result'/>\n"+
                     "        </ceab:output>\n"+
                     "      </ceab:Interface>\n";
               /*
               }
            }
            */
         }

         String ceaApplication =
            makeVoResourceElement(
                "cea:CeaApplicationType",
                // Namespaces
                "xmlns:cea='http://www.ivoa.net/xml/CEAService/v0.2' "   +
                "xmlns:ceapd='http://www.astrogrid.org/schema/AGParameterDefinition/v1' " +
                "xmlns:ceab='http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1' ",
                // Schema locations
                "http://www.ivoa.net/xml/CEAService/v0.2 http://software.astrogrid.org/schema/vo-resource-types/CEAService/v0.2/CEAService.xsd" + " " +
                "http://www.astrogrid.org/schema/AGParameterDefinition/v1 http://software.astrogrid.org/schema/jes/AGParameterDefinition/v1.0/AGParameterDefinition.xsd" + " " + 
                "http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1 http://software.astrogrid.org/schema/cea/CommonExecutionArchitectureBase/v1.0/CommonExecutionArchitectureBase.xsd"
                ) +
            makeCore(catNamePrefix+"ceaApplication","")+
            "<cea:ApplicationDefinition>\n"+
               "<cea:Parameters>\n"+
                  "<cea:ParameterDefinition name='Query' type='ADQL'>\n"+
                     "<ceapd:UI_Name>Query</ceapd:UI_Name>\n"+
                     "<ceapd:UI_Description>Astronomy Data Query Language that defines the search criteria</ceapd:UI_Description>\n"+
                     //"<ceapd:UCD/>
                     //"<ceapd:DefaultValue/>
                     //"<ceapd:Units/>
                  "</cea:ParameterDefinition>\n"+
                  "<cea:ParameterDefinition name='Result' type='text'>\n"+
                     "<ceapd:UI_Name>Result</ceapd:UI_Name>\n"+
                     "<ceapd:UI_Description>Query results</ceapd:UI_Description>\n"+
                  "</cea:ParameterDefinition>\n"+
                  "<cea:ParameterDefinition name='Format' type='text'>\n"+
                     "<ceapd:UI_Name>Format</ceapd:UI_Name>\n"+
                     "<ceapd:UI_Description>How the results are to be returned.  VOTABLE, VOTABLE-BINARY or CSV for now</ceapd:UI_Description>\n"+
                     "<ceapd:DefaultValue>VOTABLE</ceapd:DefaultValue>\n"+
                     "<ceapd:OptionList>\n"+
                          "<ceapd:OptionVal>VOTABLE</ceapd:OptionVal>\n"+
                          "<ceapd:OptionVal>VOTABLE-BINARY</ceapd:OptionVal>\n"+
                          "<ceapd:OptionVal>COMMA-SEPARATED</ceapd:OptionVal>\n"+
                          "<ceapd:OptionVal>HTML</ceapd:OptionVal>\n"+
                     "</ceapd:OptionList>\n"+
                  "</cea:ParameterDefinition>\n"+

                  coneParams +

                  multiConeParams +

               "</cea:Parameters>\n"+
               
               "<cea:Interfaces>\n"+
                  "<ceab:Interface name='adql'>\n"+
                     "<ceab:input>\n"+
                        "<ceab:pref maxoccurs='1' minoccurs='1' ref='Query'/>\n"+
                        "<ceab:pref maxoccurs='1' minoccurs='1' ref='Format'/>\n"+
                     "</ceab:input>\n"+
                     "<ceab:output>\n"+
                        "<ceab:pref maxoccurs='1' minoccurs='1' ref='Result'/>\n"+
                     "</ceab:output>\n"+
                  "</ceab:Interface>\n"+

                  coneInters +

                  multiConeInters +

               "</cea:Interfaces>\n"+
            "</cea:ApplicationDefinition>\n"+
            "</"+VORESOURCE_ELEMENT+">\n";
            
        ceaResources.append(ceaApplication);
        ceaResources.append(ceaService);
      }
      return ceaResources.toString();
   }

}
