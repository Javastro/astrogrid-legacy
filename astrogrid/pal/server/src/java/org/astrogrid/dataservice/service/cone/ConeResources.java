/*$Id: ConeResources.java,v 1.3 2005/03/10 15:13:49 mch Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.dataservice.service.cone;

import java.io.IOException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.metadata.VoResourcePlugin;
import org.astrogrid.dataservice.metadata.v0_10.VoResourceSupport;
import org.astrogrid.dataservice.service.ServletHelper;
import org.astrogrid.query.Query;

/** Returns Registry resources for a service type that indicates that this
 * service can provide cone searches
 */
public class ConeResources extends VoResourceSupport implements VoResourcePlugin {
   
   /**
    * Returns ServiceType for cone search
    */
   public String getVoResource() throws IOException {

      String cone =
         makeVoResourceElement("ServiceType")+
         makeCore("/cone")+
      
        //"<Subject>Stars</Subject>"+ //etc
        //"<ContentLevel>Research</ContentLevel>"+ //etc
         "<Capability xmlns:q1='http://www.ivoa.net/xml/ConeSearch/v0.2' xsi:type='q1:ConeSearchType'>"+
         "  <q1:MaxSR>180</q1:MaxSR>"+ //to do
         "  <q1:MaxRecords>"+SimpleConfig.getSingleton().getString(Query.MAX_RETURN_KEY,"0")+"</q1:MaxRecords>"+
         "  <q1:Verbosity>false</q1:Verbosity>"+ //no idea
         "</Capability>"+
         "<Interface>"+
         "  <Invocation>Custom</Invocation>"+
         "  <AccessURL>"+ServletHelper.getUrlStem()+"cone/SubmitCone?</AccessURL>"+
         "</Interface>"+
         "</Resource>";
      
      return cone;
   }
   
   
}


/*
 $Log: ConeResources.java,v $
 Revision 1.3  2005/03/10 15:13:49  mch
 Seperating out fits, table and xdb servers

 Revision 1.2  2005/03/10 13:49:53  mch
 Updating metadata

 Revision 1.1  2005/03/08 18:05:57  mch
 updating resources to v0.10


 
 */
