/*$Id: SkyNodeResourceServer.java,v 1.1 2004/11/12 10:44:54 mch Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.service.skynode.v074;

import java.io.IOException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.metadata.VoDescriptionServer;
import org.astrogrid.datacenter.metadata.VoResourcePlugin;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.service.ServletHelper;

/** Returns Registry resources for a service type that indicates that this
 * service can provide cone searches
 */
public class SkyNodeResourceServer implements VoResourcePlugin {
   
   /**
    * Returns an array of VOResource elements of the metadata.  Returns a string (rather than
    * DOM element)
    * so that we can combine them easily; some DOMs do not mix well.
    */
   public synchronized String[] getVoResources() throws IOException {
      return new String[] { getSkyNodeServiceType() };
   }
   
   /**
    * Returns ServiceType for cone search
    */
   public String getSkyNodeServiceType() throws IOException {
      StringBuffer resource = new StringBuffer("<Resource xsi:type='ServiceType'>");
      
      VoDescriptionServer.appendIdentifier(resource, "/skynode");
      VoDescriptionServer.appendSummary(resource);
      VoDescriptionServer.appendCuration(resource);
      
      resource.append(
              //"<Subject>Stars</Subject>"+ //etc
              //"<ContentLevel>Research</ContentLevel>"+ //etc
            "<Capability xmlns:q1='http://www.ivoa.net/xml/SkyNode/v0.1' xsi:type='q1:SkyNodeType'>"+
               "<q1:Compliance>BASIC</q1:Compliance>"+
               "<q1:Latitude>0</q1:Latitude>"+
               "<q1:Longitude>0</q1:Longitude>"+
            "  <q1:MaxRecords>"+SimpleConfig.getSingleton().getString(Query.MAX_RETURN_KEY)+"</q1:MaxRecords>"+
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
 $Log: SkyNodeResourceServer.java,v $
 Revision 1.1  2004/11/12 10:44:54  mch
 More resources, siap stuff, ssap stuff, SSS

 
 */
