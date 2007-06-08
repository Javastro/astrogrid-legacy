/*$Id: ConeResources.java,v 1.2 2007/06/08 13:16:11 clq2 Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.dataservice.service.cone.v0_10;

import java.io.IOException;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.metadata.VoResourcePlugin;
import org.astrogrid.dataservice.metadata.v0_10.VoResourceSupport;
import org.astrogrid.dataservice.service.ServletHelper;
import org.astrogrid.query.Query;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.tableserver.metadata.TableInfo;
     

/** Returns Registry resources for a service type that indicates that this
 * service can provide cone searches
 */
public class ConeResources extends VoResourceSupport implements VoResourcePlugin {
   
   /**
    * Returns ServiceType for cone search
    */
   public String getVoResource() throws IOException {
      String coneList = "";
      TableInfo[] coneTables = 
         TableMetaDocInterpreter.getConesearchableTables();
      for (int i = 0; i < coneTables.length; i++) {
         coneList = coneList + makeVoResource(
               coneTables[i].getCatalogName(),
               coneTables[i].getName());
      }
      return coneList;
   }


   protected String makeVoResource(String catName, String tabName) throws IOException {
      String cone =
         makeVoResourceElement(
             //"ServiceType",
             "cs:ConeSearch",
             // Namespaces
             "xmlns:cs='http://www.ivoa.net/xml/ConeSearch/v0.3'",
             // Schema locations
            "http://www.ivoa.net/xml/ConeSearch/v0.3 http://www.ivoa.net/xml/ConeSearch/v0.3" 
        )+
         makeCore(catName+"/"+tabName+"/cone", "Searching catalog " + catName +
               ", table " + tabName)+
      
        //"<Subject>Stars</Subject>"+ //etc
        //"<ContentLevel>Research</ContentLevel>"+ //etc
         "<vr:interface qtype=\"GET\" xmlns:vs=\"http://www.ivoa.net/xml/VODataService/v0.5\" xsi:schemaLocation=\"http://www.ivoa.net/xml/VODataService/v0.5 http://software.astrogrid.org/schema/vo-resource-types/VODataService/v0.5/VODataService.xsd\" xsi:type=\"vs:ParamHTTP\">\n"+
         "  <vr:accessURL use=\"base\">"+ServletHelper.getUrlStem()+"SubmitCone?DSACAT="+catName+"&amp;DSATAB="+tabName+"&amp;</vr:accessURL>\n"+
         "  <vs:resultType/>\n"+
         "</vr:interface>\n"+
         "<cs:capability>\n" +
         "  <cs:maxSR>180</cs:maxSR>\n"+ //to do
         "  <cs:maxRecords>"+ConfigFactory.getCommonConfig().getString(Query.MAX_RETURN_KEY,"0")+"</cs:maxRecords>\n"+
         "  <cs:verbosity>false</cs:verbosity>\n"+ //no idea
         "</cs:capability>\n"+

         "</"+VORESOURCE_ELEMENT+">\n";
      
      return cone;
   }
   
   
}


/*
 $Log: ConeResources.java,v $
 Revision 1.2  2007/06/08 13:16:11  clq2
 KEA-PAL-2169

 Revision 1.1.2.4  2007/06/08 13:06:40  kea
 Ready for trial merge.

 Revision 1.1.2.3  2007/06/01 16:54:32  kea
 Nearly there.

 Revision 1.1.2.2  2007/05/18 16:34:12  kea
 Still working on new metadoc / multi conesearch.

 Revision 1.1.2.1  2007/05/16 11:03:52  kea
 Removing siap stuff, not in use.

 Revision 1.1  2007/03/06 11:35:31  kea
 Moved ConeResources.java into version-specific subdir.

 Revision 1.11  2006/10/17 10:11:41  clq2
 PAL_KEA_1869

 Revision 1.10.8.1  2006/10/12 16:40:15  kea
 Tweaks while fixing registration issues (see bugzilla ticket 1920)

 Revision 1.10  2006/06/30 06:33:42  clq2
 dave's fix for cone search

 Revision 1.9.18.1  2006/06/29 22:35:22  dave
 Fixed missing element in registration document, and typo in install instructions

 Revision 1.9  2006/02/09 09:54:09  clq2
 KEA_1521_pal

 Revision 1.8.50.1  2006/02/06 11:02:47  kea
 Reinstated conesearch registration, added schemaLocations to
 registrations and generally tidied.

 Revision 1.8  2005/06/09 08:53:58  clq2
 200506081212

 Revision 1.7.22.1  2005/06/09 01:31:57  dave
 Fixed bugs in the metedata generator(s).
 Note - updated Date patch may not work in other timezones.
 Note - <resource> element may not be the right one.

 Revision 1.7  2005/03/24 17:50:48  mch
 Fixed various resource bits

 Revision 1.6  2005/03/23 17:54:15  mch
 removed rbmsresours

 Revision 1.5  2005/03/21 18:45:55  mch
 Naughty big lump of changes

 Revision 1.4  2005/03/10 22:39:17  mch
 Fixed tests more metadata fixes

 Revision 1.3  2005/03/10 15:13:49  mch
 Seperating out fits, table and xdb servers

 Revision 1.2  2005/03/10 13:49:53  mch
 Updating metadata

 Revision 1.1  2005/03/08 18:05:57  mch
 updating resources to v0.10


 
 */
