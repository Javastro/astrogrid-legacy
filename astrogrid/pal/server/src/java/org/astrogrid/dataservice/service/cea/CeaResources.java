/*
 * $Id: CeaResources.java,v 1.9 2005/11/21 12:54:18 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.service.cea;

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
 * <p>
 * @author M Hill
 */

public class CeaResources extends VoResourceSupport implements VoResourcePlugin {

   protected static Log log = LogFactory.getLog(VoDescriptionServer.class);
   
   /**
    * Returns a CeaServiceType resource element
    */
   public String getVoResource() throws IOException {

      String ceaService =
         makeVoResourceElement(
             "cea:CeaServiceType",
             "xmlns:vs='http://www.ivoa.net/xml/VODataService/v0.5' " +
             "xmlns:cea='http://www.ivoa.net/xml/CEAService/v0.2' "
             ) +
         makeCore("ceaService")+
         "<interface xsi:type='vs:WebService'>"+
            "<accessURL use='full'>"+
               ConfigFactory.getCommonConfig().getString("datacenter.url")+"services/CommonExecutionConnectorService"+
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
             "xmlns:cea='http://www.ivoa.net/xml/CEAService/v0.2' "   +
             "xmlns:ceapd='http://www.astrogrid.org/schema/AGParameterDefinition/v1' " +
             "xmlns:ceab='http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1' "
             ) +
         makeCore("ceaApplication")+
         "<cea:ApplicationDefinition>"+
            "<cea:Parameters>"+
               "<cea:ParameterDefinition name='Query' type='ADQL'>"+
                  "<ceapd:UI_Name>Query</ceapd:UI_Name>"+
                  "<ceapd:UI_Description>Astronomy Data Query Language that defines the search criteria</ceapd:UI_Description>"+
                  //"<ceapd:UCD/>
                  //"<ceapd:DefaultValue/>
                  //"<ceapd:Units/>
               "</cea:ParameterDefinition>"+
               "<cea:ParameterDefinition name='Result' type='text'>"+
                  "<ceapd:UI_Name>Result</ceapd:UI_Name>"+
                  "<ceapd:UI_Description>Query results</ceapd:UI_Description>"+
               "</cea:ParameterDefinition>"+
               "<cea:ParameterDefinition name='Format' type='text'>"+
                  "<ceapd:UI_Name>Format</ceapd:UI_Name>"+
                  "<ceapd:UI_Description>How the results are to be returned.  VOTABLE or CSV for now</ceapd:UI_Description>"+
                  "<ceapd:DefaultValue>VOTABLE</ceapd:DefaultValue>"+
               "</cea:ParameterDefinition>"+
               "<cea:ParameterDefinition name='RA' type='double'>"+
                  "<ceapd:UI_Name>RA</ceapd:UI_Name>"+
                  "<ceapd:UI_Description>Right-Ascension of cone</ceapd:UI_Description>"+
                  "<ceapd:UCD>POS_RA_MAIN</ceapd:UCD>"+
                  "<ceapd:Units>deg</ceapd:Units>"+
               "</cea:ParameterDefinition>"+
               "<cea:ParameterDefinition name='DEC' type='double'>"+
                  "<ceapd:UI_Name>DEC</ceapd:UI_Name>"+
                  "<ceapd:UI_Description>Declination of cone</ceapd:UI_Description>"+
                  "<ceapd:UCD>POS_DEC_MAIN</ceapd:UCD>"+
                  "<ceapd:Units>deg</ceapd:Units>"+
               "</cea:ParameterDefinition>"+
               "<cea:ParameterDefinition name='Radius' type='double'>"+
                  "<ceapd:UI_Name>Radius</ceapd:UI_Name>"+
                  "<ceapd:UI_Description>Radius of cone</ceapd:UI_Description>"+
                  "<ceapd:Units>deg</ceapd:Units>"+
               "</cea:ParameterDefinition>"+
            "</cea:Parameters>"+
            
            "<cea:Interfaces>"+
               "<ceab:Interface name='adql'>"+
                  "<ceab:input>"+
                     "<ceab:pref maxoccurs='1' minoccurs='1' ref='Query'/>"+
                     "<ceab:pref maxoccurs='1' minoccurs='1' ref='Format'/>"+
                  "</ceab:input>"+
                  "<ceab:output>"+
                     "<ceab:pref maxoccurs='1' minoccurs='1' ref='Result'/>"+
                  "</ceab:output>"+
               "</ceab:Interface>"+
               "<ceab:Interface name='cone'>"+
                  "<ceab:input>"+
                     "<ceab:pref maxoccurs='1' minoccurs='1' ref='RA'/>"+
                     "<ceab:pref maxoccurs='1' minoccurs='1' ref='DEC'/>"+
                     "<ceab:pref maxoccurs='1' minoccurs='1' ref='Radius'/>"+
                     "<ceab:pref maxoccurs='1' minoccurs='1' ref='Format'/>"+
                  "</ceab:input>"+
                  "<ceab:output>"+
                     "<ceab:pref maxoccurs='1' minoccurs='1' ref='Result'/>"+
                  "</ceab:output>"+
               "</ceab:Interface>"+
            "</cea:Interfaces>"+
         "</cea:ApplicationDefinition>"+
         "</"+VORESOURCE_ELEMENT+">";

         
      System.out.println(ceaService+ceaApplication);
         return ceaService+ceaApplication;
   }

}







