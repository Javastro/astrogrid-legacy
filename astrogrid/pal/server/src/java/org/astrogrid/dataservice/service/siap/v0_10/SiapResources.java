/*$Id: SiapResources.java,v 1.1 2007/03/21 18:55:14 kea Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.dataservice.service.siap.v0_10;

import java.io.IOException;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.metadata.VoResourcePlugin;
import org.astrogrid.dataservice.metadata.v0_10.VoResourceSupport;
import org.astrogrid.dataservice.service.ServletHelper;
import org.astrogrid.query.Query;

/** Returns Registry resources for a service type that indicates that this
 * service can provide Simple Image Access Protocol
 */
public class SiapResources extends VoResourceSupport implements VoResourcePlugin {
   
   /**
    * Returns ServiceType for cone search
    */
   public String getVoResource() throws IOException {

      String siap =
         makeVoResourceElement("ServiceType", "", "")+
         makeCore("/siap")+
      
           //"<Subject>Stars</Subject>"+ //etc
           //"<ContentLevel>Research</ContentLevel>"+ //etc
         "<Capability xmlns:q1='http://www.ivoa.net/xml/SIA/v0.6' xsi:type='q1:SimpleImageAccessType'>"+
            "<q1:ImageServiceType>Cutout</q1:ImageServiceType>"+
            "<q1:MaxQueryRegionSize>"+
               "<q1:long>0</q1:long>"+
               "<q1:lat>0</q1:lat>"+
            "</q1:MaxQueryRegionSize>"+
            "<q1:MaxImageExtent>"+
               "<q1:long>0</q1:long>"+
               "<q1:lat>0</q1:lat>"+
               "</q1:MaxImageExtent>"+
            "<q1:MaxImageSize>"+
               "<q1:long>0</q1:long>"+
               "<q1:lat>0</q1:lat>"+
               "</q1:MaxImageSize>"+
            "<q1:MaxFileSize>0</q1:MaxFileSize>"+
            "<q1:MaxRecords>"+ConfigFactory.getCommonConfig().getString(Query.MAX_RETURN_KEY,"0")+"</q1:MaxRecords>"+
         "</Capability>"+
         "<Interface>"+
         "  <Invocation>Custom</Invocation>"+
         "  <AccessURL>"+ServletHelper.getUrlStem()+"SimpleImageQuery</AccessURL>"+
         "</Interface>"+
         "</Resource>";

      return siap;
   }
   
   
}


/*
 $Log: SiapResources.java,v $
 Revision 1.1  2007/03/21 18:55:14  kea
 Adding support structure for v1.0 resources (though not enabled yet).

 Revision 1.5  2006/02/09 09:54:09  clq2
 KEA_1521_pal

 Revision 1.4.74.1  2006/02/06 11:02:47  kea
 Reinstated conesearch registration, added schemaLocations to
 registrations and generally tidied.

 Revision 1.4  2005/03/23 17:54:15  mch
 removed rbmsresours

 Revision 1.3  2005/03/21 18:45:55  mch
 Naughty big lump of changes

 Revision 1.2  2005/03/10 15:13:49  mch
 Seperating out fits, table and xdb servers

 Revision 1.1  2005/03/08 18:05:57  mch
 updating resources to v0.10

 
 */

