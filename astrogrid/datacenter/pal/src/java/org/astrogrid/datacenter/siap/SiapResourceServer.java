/*$Id: SiapResourceServer.java,v 1.2 2004/11/12 10:45:50 mch Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.siap;

import java.io.IOException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.metadata.VoDescriptionServer;
import org.astrogrid.datacenter.metadata.VoResourcePlugin;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.service.ServletHelper;

/** Returns Registry resources for a service type that indicates that this
 * service can provide Simple Image Access Protocol
 */
public class SiapResourceServer implements VoResourcePlugin {
   
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
      
      VoDescriptionServer.appendIdentifier(resource, "/siap");
      VoDescriptionServer.appendSummary(resource);
      VoDescriptionServer.appendCuration(resource);
      
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
               "<q1:MaxRecords>"+SimpleConfig.getSingleton().getString(Query.MAX_RETURN_KEY)+"</q1:MaxRecords>"+
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
 Revision 1.2  2004/11/12 10:45:50  mch
 fixed method name

 Revision 1.1  2004/11/12 10:44:54  mch
 More resources, siap stuff, ssap stuff, SSS

 
 */

