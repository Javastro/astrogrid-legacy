/*$Id: SiapResourceServer.java,v 1.1 2005/02/17 18:37:35 mch Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.dataservice.service.siap;

import java.io.IOException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.metadata.VoResourcePlugin;
import org.astrogrid.dataservice.metadata.VoResourceSupport;
import org.astrogrid.dataservice.service.ServletHelper;
import org.astrogrid.query.Query;

/** Returns Registry resources for a service type that indicates that this
 * service can provide Simple Image Access Protocol
 */
public class SiapResourceServer extends VoResourceSupport implements VoResourcePlugin {
   
   /**
    * Returns an array of VOResource elements of the metadata.  Returns a string (rather than
    * DOM element)
    * so that we can combine them easily; some DOMs do not mix well.
    */
   public synchronized String[] getVoResources() throws IOException {
      return new String[] { getSiapServiceType() };
   }
   
   /**
    * Returns ServiceType for cone search
    */
   public String getSiapServiceType() throws IOException {
      StringBuffer resource = new StringBuffer("<Resource xsi:type='ServiceType'>");
      
      appendIdentifier(resource, "/siap");
      appendSummary(resource);
      appendCuration(resource);
      
      resource.append(
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
               "<q1:MaxRecords>"+SimpleConfig.getSingleton().getString(Query.MAX_RETURN_KEY,"0")+"</q1:MaxRecords>"+
            "</Capability>"+
            "<Interface>"+
            "  <Invocation>Custom</Invocation>"+
            "  <AccessURL>"+ServletHelper.getUrlStem()+"services/SkyNode074</AccessURL>"+
            "</Interface>"+
            "</Resource>"
      );
      return resource.toString();
   }
   
   
}


/*
 $Log: SiapResourceServer.java,v $
 Revision 1.1  2005/02/17 18:37:35  mch
 *** empty log message ***

 Revision 1.1.1.1  2005/02/16 17:11:24  mch
 Initial checkin

 Revision 1.1.2.2  2004/12/10 12:37:13  mch
 Cone searches to look in metadata, lots of metadata interpreterrs

 Revision 1.1.2.1  2004/12/05 19:38:37  mch
 changed skynode to 'raw' soap (from axis) and bug fixes

 Revision 1.2.2.1  2004/11/17 11:15:46  mch
 Changes for serving images

 Revision 1.2  2004/11/12 10:45:50  mch
 fixed method name

 Revision 1.1  2004/11/12 10:44:54  mch
 More resources, siap stuff, ssap stuff, SSS

 
 */

