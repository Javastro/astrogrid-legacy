/*$Id: VizierResourcePlugin.java,v 1.1 2005/02/17 18:37:34 mch Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.dataservice.impl.cds;

import java.io.IOException;
import javax.xml.rpc.ServiceException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.metadata.VoDescriptionServer;
import org.astrogrid.dataservice.metadata.VoResourcePlugin;
import org.astrogrid.dataservice.service.ServletHelper;

/** Returns Registry resources for Vizier proxy service.  Because of the size
 * of Vizier, the resource documents are cached, but only in memory so that
 * we get a refresh if we restart the service.
 * <p>
 * The resources here are separately cached so we don't keep hitting VizieR - I
 * don't know if this is a good thing, it just means this service ahs to be restarted
 * if Vizier adds a new catalogue
 */
public class VizierResourcePlugin implements VoResourcePlugin {
   
   private static String[] cache = null;
   
   /**
    * Returns an array of VOResource elements of the metadata.  Returns a string (rather than
    * DOM element)
    * so that we can combine them easily; some DOMs do not mix well.
    */
   public synchronized String[] getVoResources() throws IOException {
      if (cache == null) {
         cache = new String[] { getQueryable(), /* getRdbmsResource() */ };
      }
      
      return cache;
   }
   
   
   /**
    * Returns Queryable, which describes the way the query can be built.
    */
   public String getQueryable() throws IOException {
         String resource =
         "<Resource xsi:type='Queryable'>\n"+
              "<Identifier>"+
                  "<AuthorityID>"+SimpleConfig.getSingleton().getString(VoDescriptionServer.AUTHID_KEY)+"</AuthorityID>"+
                  "<ResourceKey>"+SimpleConfig.getSingleton().getString(VoDescriptionServer.RESKEY_KEY)+"/queryable</ResourceKey>"+
              "</Identifier>"+
              "<ForceFlat>Intersection</ForceFlat>\n"+
              "<Field name='Target' optional='true' datatype='varchar'>"+
                  "<Description>Name of target, eg M31.  Do not specify RA/DEC/CIRCLE if naming the target</Description>"+
              "</Field>\n"+
              "<Field name='RA' optional='true' datatype='double'>"+
                  "<Description>RA of search center </Description>"+
                  "<Unit>deg</Unit>"+
                  "<UCD>POS_RA_MAIN</UCD>"+
              "</Field>\n"+
              "<Field name='DEC' optional='true' datatype='double'>"+
                  "<Description>DEC of search center </Description>"+
                  "<Unit>deg</Unit>"+
                  "<UCD>POS_DEC_MAIN</UCD>"+
              "</Field>\n"+
              "<Field name='Radius' optional='false' datatype='varchar'>"+
                  "<Description>Radius of search from target or RA/DEC</Description>"+
                  "<Unit>deg</Unit>"+
              "</Field>\n"+
              "<Field name='Wavelength' optional='true' datatype='options'>"+
                  "<Option>Radio</Option>"+
                  "<Option>IR</Option>"+
                  "<Option>Optical</Option>"+
                  "<Option>UV</Option>"+
                  "<Option>EUV</Option>"+
                  "<Option>X-Ray</Option>"+
                  "<Option>Gamma-Ray</Option>"+
              "</Field>\n"+
              "<Field name='Text' optional='false' datatype='varchar'>"+
                  "<Description>Name of target, eg M31.  Do not specify RA/DEC/CIRCLE if naming the target</Description>"+
              "</Field>\n"+
//              "<Field name='Unit' optional='false' datatype='options'>"+
//                  "<Option>arcmin</Option>"+
//                 "<Option>arcsec</Option>"+
//                  "<Option>deg</Option>"+
//              "</Field>"+
              "<Functions><Function>CIRCLE</Function></Functions>\n"+
              "<Formats><Format>VOTABLE</Format><Format>HTML</Format><Format>CSV</Format></Formats>\n"+
              "<SkyNodeInterface version='0.7.4'>"+
                  "<AccessURL>"+ServletHelper.getUrlStem()+"/services/SkyNode074</AccessURL>"+
              "</SkyNodeInterface>\n"+
              "<AstroGridInterface version='0.6'>"+
                  "<AccessURL>"+ServletHelper.getUrlStem()+"/services/AxisDataService06</AccessURL>"+
              "</AstroGridInterface>\n"+
            "</Resource>\n";
         
         return resource;

   }
   
   /**
    * Returns RdbmsResource, for the portal query builder until QUeryable is settled
    *
   public String getRdbmsResource() throws IOException {
         String resource =
            "<Resource xsi:type='RdbmsMetadata'>\n"+
              "<Identifier>"+
                  "<AuthorityID>"+SimpleConfig.getSingleton().getString(VoDescriptionServer.AUTHID_KEY)+"</AuthorityID>"+
                  "<ResourceKey>"+SimpleConfig.getSingleton().getString(VoDescriptionServer.RESKEY_KEY)+"/rdbms</ResourceKey>"+
              "</Identifier>"+
              "<Table>\n"+
                  "<Name>Vizier</Name>\n"+
                  "<Description>A 'virtual' table that maps onto what fields can be searched on the Vizier SOAP interface, "+
                  "details of which are <a href='http://cdsweb.u-strasbg.fr/cdsws/vizierAccess.gml'>here</a>. "+
                  "You can use either Target+Radius, OR the CIRCLE function, but not both."+
                  "</Description>\n"+
                  "<Column>"+
                     "<Name>Target</Name>"+
                     "<Description>Name of target (eg 'M31')</Description>"+
                  "</Column>\n"+
                  "<Column>"+
                     "<Name>Radius</Name>"+
                     "<Unit>deg</Unit>"+
                     "<Description>Radius around named target to look in</Description>"+
                  "</Column>\n"+
                  "<Column>"+
                     "<Name>Wavelength</Name>"+
                     "<Description>Wavelength can be "+asCSList(VizierWavelength.getAll(VizierWavelength.class))+
                     "</Description>"+
                  "</Column>\n"+
                  "<Column>"+
                     "<Name>Text</Name>"+
                     "<Description>Not sure yet.  Some kind of keyword thing"+
                     "</Description>"+
                  "</Column>\n"+
               "</Table>\n"+
              "<Functions><Function>CIRCLE</Function></Functions>\n"+
            "</Resource>\n";
         
         return resource;

   }
   
   /** Just a simple convenience method to write out a list of objects as comma separated strings */
   private String asCSList(Object[] list) {
      StringBuffer s = new StringBuffer();
      for (int i = 0; i < list.length; i++) {
         s.append(", "+list[i]);
      }
      return s.toString().substring(2);
   }
   
   
}


/*
 $Log: VizierResourcePlugin.java,v $
 Revision 1.1  2005/02/17 18:37:34  mch
 *** empty log message ***

 Revision 1.1.1.1  2005/02/16 17:11:24  mch
 Initial checkin

 
 */
