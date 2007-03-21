/*
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.service.cea.v1_0;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.metadata.VoDescriptionServer;
import org.astrogrid.dataservice.metadata.VoResourcePlugin;
import org.astrogrid.dataservice.metadata.v0_10.VoResourceSupport;

/**
 * Serves the CEA resources.  Bit of a mangled fudge at the moment to get
 * the right stuff from the right bit into the right bit.
 *
 * TOFIX THIS IS STILL SERVING OLD VERSION!!! JUST A PLACEHOLDER!! 
 * <p>
 * @author M Hill, K Andrews
 */

public class CeaResources extends VoResourceSupport implements VoResourcePlugin {
   protected static Log log = LogFactory.getLog(VoDescriptionServer.class);
   
   /**
    * Returns a CeaServiceType resource element
    */
   public String getVoResource() throws IOException {

      // Tofix take out later
      throw new IOException(
            "DON'T USE v1.0 REGISTRATIONS YET!  CODE NOT READY!");

      /*
      String endpoint = 
         ConfigFactory.getCommonConfig().getString("datacenter.url");
      if (!endpoint.endsWith("/")) {
         endpoint = endpoint + "/";  // Add trailing separator if missing
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
         makeCore("ceaService")+
         "<interface xsi:type='vs:WebService'>"+
            "<accessURL use='full'>"+ endpoint + 
            "services/CommonExecutionConnectorService"+
            "</accessURL>"+
         "</interface>"+
         //reference to the application that this serves
         "<cea:ManagedApplications>"+
            "<cea:ApplicationReference>"+
               makeId("ceaApplication")+
            "</cea:ApplicationReference>"+
         "</cea:ManagedApplications>"+
         "</"+VORESOURCE_ELEMENT+">";
      

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
         makeCore("ceaApplication")+
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
                  "<ceapd:Units>deg</ceapd:Units>\n"+
               "</cea:ParameterDefinition>\n"+
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
               "<ceab:Interface name='cone'>\n"+
                  "<ceab:input>\n"+
                     "<ceab:pref maxoccurs='1' minoccurs='1' ref='RA'/>\n"+
                     "<ceab:pref maxoccurs='1' minoccurs='1' ref='DEC'/>\n"+
                     "<ceab:pref maxoccurs='1' minoccurs='1' ref='Radius'/>\n"+
                     "<ceab:pref maxoccurs='1' minoccurs='1' ref='Format'/>\n"+
                  "</ceab:input>\n"+
                  "<ceab:output>\n"+
                     "<ceab:pref maxoccurs='1' minoccurs='1' ref='Result'/>\n"+
                  "</ceab:output>\n"+
               "</ceab:Interface>\n"+
            "</cea:Interfaces>\n"+
         "</cea:ApplicationDefinition>\n"+
         "</"+VORESOURCE_ELEMENT+">\n";
         
      //System.out.println(ceaService+ceaApplication);
         return ceaService+ceaApplication;
      */
   }

}
