/*$Id: ConeResourceServer.java,v 1.1 2004/11/12 10:44:54 mch Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.cone;

import java.io.IOException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.metadata.VoDescriptionServer;
import org.astrogrid.datacenter.metadata.VoResourcePlugin;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.service.ServletHelper;

/** Returns Registry resources for a service type that indicates that this
 * service can provide cone searches
 */
public class ConeResourceServer implements VoResourcePlugin {
   
   /**
    * Returns an array of VOResource elements of the metadata.  Returns a string (rather than
    * DOM element)
    * so that we can combine them easily; some DOMs do not mix well.
    */
   public synchronized String[] getVoResources() throws IOException {
      return new String[] { getConeServiceType() };
   }
   
   /**
    * Returns ServiceType for cone search
    */
   public String getConeServiceType() throws IOException {
      StringBuffer resource = new StringBuffer("<Resource xsi:type='ServiceType'>");
      
      VoDescriptionServer.appendIdentifier(resource, "/cone");
      VoDescriptionServer.appendSummary(resource);
      VoDescriptionServer.appendCuration(resource);
      
      resource.append(
              //"<Subject>Stars</Subject>"+ //etc
              //"<ContentLevel>Research</ContentLevel>"+ //etc
            "<Capability xmlns:q1='http://www.ivoa.net/xml/ConeSearch/v0.2' xsi:type='q1:ConeSearchType'>"+
            "  <q1:MaxSR>180</q1:MaxSR>"+ //to do
            "  <q1:MaxRecords>"+SimpleConfig.getSingleton().getString(Query.MAX_RETURN_KEY)+"</q1:MaxRecords>"+
            "  <q1:Verbosity>false</q1:Verbosity>"+ //no idea
            "</Capability>"+
            "<Interface>"+
            "  <Invocation>Custom</Invocation>"+
            "  <AccessURL>"+ServletHelper.getUrlStem()+"cone/SubmitCone?</AccessURL>"+
            "</Interface>"+
            "</Resource>"
      );
         
         return resource.toString();
   }
   
   
}


/*
 $Log: ConeResourceServer.java,v $
 Revision 1.1  2004/11/12 10:44:54  mch
 More resources, siap stuff, ssap stuff, SSS

 
 */
