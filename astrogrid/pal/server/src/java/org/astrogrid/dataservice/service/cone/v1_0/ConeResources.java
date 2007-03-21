/*$Id: ConeResources.java,v 1.1 2007/03/21 18:54:53 kea Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.dataservice.service.cone.v1_0;

import java.io.IOException;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.metadata.VoResourcePlugin;
import org.astrogrid.dataservice.metadata.v1_0.VoResourceSupport;
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

      // Tofix take out later
      throw new IOException(
            "DON'T USE v1.0 REGISTRATIONS YET!  CODE NOT READY!");
/*
      String cone =
         makeVoResourceElement(
             //"ServiceType",
             "vs:CatalogService",
             // Namespaces
             "xmlns:cs='http://www.ivoa.net/xml/ConeSearch/v1.0' "+
             "xmlns:stc='http://www.ivoa.net/xml/STC/stc-v1.30.xsd' "+
             "xmlns:xlink='http://www.w3.org/1999/xlink' ",
             // Schema locations
            "http://www.ivoa.net/xml/ConeSearch/v1.0 http://www.ivoa.net/xml/ConeSearch/v1.0 " +
             "http://www.ivoa.net/xml/STC/stc-v1.30.xsd http://www.ivoa.net/xml/STC/stc-v1.30.xsd "
        )+
         makeCore("cone")+
      
        //"<Subject>Stars</Subject>"+ //etc
        //"<ContentLevel>Research</ContentLevel>"+ //etc
         "<vr:interface qtype=\"GET\" xmlns:vs=\"http://www.ivoa.net/xml/VODataService/v0.5\" xsi:schemaLocation=\"http://www.ivoa.net/xml/VODataService/v0.5 http://software.astrogrid.org/schema/vo-resource-types/VODataService/v0.5/VODataService.xsd\" xsi:type=\"vs:ParamHTTP\">\n"+
         "  <vr:accessURL use=\"base\">"+ServletHelper.getUrlStem()+"SubmitCone?</vr:accessURL>\n"+
         "  <vs:resultType/>\n"+
         "</vr:interface>\n"+
         "<cs:capability>\n" +
         "  <cs:maxSR>180</cs:maxSR>\n"+ //to do
         "  <cs:maxRecords>"+ConfigFactory.getCommonConfig().getString(Query.MAX_RETURN_KEY,"0")+"</cs:maxRecords>\n"+
         "  <cs:verbosity>false</cs:verbosity>\n"+ //no idea
         "</cs:capability>\n"+

         "</"+VORESOURCE_ELEMENT+">\n";
      
      return cone;
      */
   }
}
