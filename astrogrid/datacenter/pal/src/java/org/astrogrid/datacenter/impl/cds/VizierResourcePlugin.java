/*$Id: VizierResourcePlugin.java,v 1.5 2004/11/08 14:26:56 mch Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.impl.cds;

import java.io.IOException;
import java.net.URL;
import org.astrogrid.datacenter.impl.cds.generated.vizier.VizieR;
import org.astrogrid.datacenter.impl.cds.generated.vizier.VizieRService;
import org.astrogrid.datacenter.impl.cds.generated.vizier.VizieRServiceLocator;
import org.astrogrid.datacenter.metadata.VoDescriptionServer;
import org.astrogrid.datacenter.metadata.VoResourcePlugin;
import org.astrogrid.datacenter.service.ServletHelper;
import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;

/** Returns Registry resources for Vizier proxy service.  Because of the size
 * of Vizier, the resource documents are cached, but only in memory so that
 * we get a refresh if we restart the service.
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
         cache = new String[] { getQueryable(), getRdbmsResource() };
      }
      
      return cache;
   }
   
   
   /**
    * Returns Queryable, which describes the way the query can be built
    */
   public String getQueryable() throws IOException {
         String resource =
         "<Resource xsi:type='Queryable'>\n"+
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
              "<Field name='Text' optional='true' datatype='varchar'>"+
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
    */
   public String getRdbmsResource() throws IOException {
         String resource =
            "<Resource xsi:type='RdbmsMetadata'>\n"+
              "<Table>\n"+
                  "<Name>Vizier</Name>\n"+
                  "<Column>"+
                     "<Name>Target</Name>"+
                  "</Column>\n"+
                  "<Column>"+
                     "<Name>Radius</Name>"+
                     "<Unit>deg</Unit>"+
                  "</Column>\n"+
                  "<Column>"+
                     "<Name>Wavelength</Name>"+
                  "</Column>\n"+
                  "<Column>"+
                     "<Name>Text</Name>"+
                  "</Column>\n"+
               "</Table>\n"+
              "<Functions><Function>CIRCLE</Function></Functions>\n"+
            "</Resource>\n";
         
         return resource;

   }
   
   
   /** Gets all metadata from VizieR */
   public String getVizierMetadata() throws IOException, ServiceException, MalformedURLException {
         VizieRService service = new VizieRServiceLocator();
         VizieR vizier = service.getVizieR(new URL("http://cdsws.u-strasbg.fr/axis/services/VizieR"));
         String meta = vizier.metaAll();
      return meta;
   }

   /**
    * test harness Quick check that it's valid
    */
   public static void main(String[] args) throws IOException, IOException, ServiceException {
      System.out.print(new VizierResourcePlugin().getVizierMetadata());
   }
}


/*
 $Log: VizierResourcePlugin.java,v $
 Revision 1.5  2004/11/08 14:26:56  mch
 Fixed queryable resource

 Revision 1.4  2004/11/08 02:59:13  mch
 Fixes to connect to Vizier

 Revision 1.3  2004/11/03 05:14:33  mch
 Bringing Vizier back online

 Revision 1.2  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.1.4.1  2004/10/15 19:59:05  mch
 Lots of changes during trip to CDS to improve int test pass rate

 Revision 1.1  2004/10/05 20:26:43  mch
 Prepared for better resource metadata generators

 Revision 1.1  2004/10/05 19:19:18  mch
 Merged CDS implementation into PAL

 Revision 1.5  2004/09/29 18:45:55  mch
 Bringing Vizier into line with new(er) metadata stuff

 Revision 1.4  2004/08/14 14:35:42  acd
 Fix the cone search in the Vizier Proxy.

 Revision 1.4  2004/08/13 16:50:00  acd
 Added static final String METADATA.

 Revision 1.3  2004/08/12 17:31:00  acd
 Added static final String CATALOGUE_NAME.

 Revision 1.2  2004/03/14 04:14:20  mch
 Wrapped output target in TargetIndicator

 Revision 1.1  2004/03/13 23:40:59  mch
 Changes to adapt to It05 refactor

 Revision 1.6  2003/12/09 16:25:08  nw
 wrote plugin documentation

 Revision 1.5  2003/12/01 16:50:11  nw
 first working tested version

 Revision 1.4  2003/11/28 19:12:16  nw
 getting there..

 Revision 1.3  2003/11/25 11:14:51  nw
 upgraded to new service interface

 Revision 1.2  2003/11/20 15:47:18  nw
 improved testing

 Revision 1.1  2003/11/18 11:23:49  nw
 mavenized cds delegate

 Revision 1.1  2003/11/18 11:10:05  nw
 mavenized cds delegate
 
 */
