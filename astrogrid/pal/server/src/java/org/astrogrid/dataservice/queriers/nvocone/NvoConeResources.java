/*
 * $Id: NvoConeResources.java,v 1.1 2005/02/17 18:37:35 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.dataservice.queriers.nvocone;

import java.io.IOException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.metadata.VoDescriptionServer;
import org.astrogrid.dataservice.metadata.VoResourcePlugin;
import org.astrogrid.dataservice.service.ServletHelper;

/**
 * The National Virtual Observatory, an American effort, defined a simple
 * cone search service:
 * @see http://www.us-vo.org/metadata/conesearch/
 * <p>
 * This resource
 * plugin returns details for a PAL service that proxies to these cone searches
 * <p>
 *
 * @author M Hill
 */

public class NvoConeResources implements VoResourcePlugin {
   
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
    * Returns Queryable, which describes the way the query can be built.
    */
   public String getQueryable() throws IOException {
         String resource =
         "<Resource xsi:type='Queryable'>\n"+
              "<Identifier>"+
                  "<AuthorityID>"+SimpleConfig.getSingleton().getString(VoDescriptionServer.AUTHID_KEY)+"</AuthorityID>"+
                  "<ResourceKey>"+SimpleConfig.getSingleton().getString(VoDescriptionServer.RESKEY_KEY)+"/queryable</ResourceKey>"+
              "</Identifier>\n"+
              "<Scope>";

      //add scope - tables available
      String[] tables = NvoConePlugin.getTables();
      
      for (int i = 0; i < tables.length; i++) {
         resource = resource + "<Table><Name>"+tables[i]+"</Name>"+
              "<ForceFlat>Intersection</ForceFlat>\n"+
              "<Field name='RA' optional='false' datatype='double'>"+
                  "<Description>RA of search center </Description>"+
                  "<Unit>deg</Unit>"+
                  "<UCD>POS_RA_MAIN</UCD>"+
              "</Field>\n"+
              "<Field name='DEC' optional='false' datatype='double'>"+
                  "<Description>DEC of search center </Description>"+
                  "<Unit>deg</Unit>"+
                  "<UCD>POS_DEC_MAIN</UCD>"+
              "</Field>\n"+
              "<Field name='Radius' optional='false' datatype='varchar'>"+
                  "<Description>Radius of search from target or RA/DEC</Description>"+
                  "<Unit>deg</Unit>"+
              "</Field>\n"+
            "<Table>";
      }
      
      resource = resource +
              "</Scope>"+
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
              "<Identifier>"+
                  "<AuthorityID>"+SimpleConfig.getSingleton().getString(VoDescriptionServer.AUTHID_KEY)+"</AuthorityID>"+
                  "<ResourceKey>"+SimpleConfig.getSingleton().getString(VoDescriptionServer.RESKEY_KEY)+"/rdbms</ResourceKey>"+
              "</Identifier>";
      
      //add scope - tables available
      String[] tables = NvoConePlugin.getTables();

      for (int i = 0; i < tables.length; i++) {
         resource = resource + "<Table><Name>"+tables[i]+"</Name>"+
                  "<Column>"+
                     "<Name>RA</Name>"+
                     "<Unit>deg</Unit>"+
                     "<Description>RA of center point to search</Description>"+
                  "</Column>\n"+
                  "<Column>"+
                     "<Name>DEC</Name>"+
                     "<Unit>deg</Unit>"+
                     "<Description>DEC of center point to search</Description>"+
                  "</Column>\n"+
                  "<Column>"+
                     "<Name>Radius</Name>"+
                     "<Unit>deg</Unit>"+
                     "<Description>Radius around named target to look in</Description>"+
                  "</Column>\n"+
               "</Table>\n";
      }

      resource = resource +
            "<Functions><Function>CIRCLE</Function></Functions>\n"+
            "</Resource>\n";
      
      return resource;

   }
   
   
   
}

/*
$Log: NvoConeResources.java,v $
Revision 1.1  2005/02/17 18:37:35  mch
*** empty log message ***

Revision 1.1.1.1  2005/02/16 17:11:24  mch
Initial checkin

Revision 1.2  2004/11/12 10:44:54  mch
More resources, siap stuff, ssap stuff, SSS

Revision 1.1  2004/11/11 23:23:29  mch
Prepared framework for SSAP and SIAP

Revision 1.4  2004/11/03 00:17:56  mch
PAL_MCH Candidate 2 merge

Revision 1.3.6.1  2004/10/27 00:43:39  mch
Started adding getCount, some resource fixes, some jsps

Revision 1.3  2004/10/18 13:11:30  mch
Lumpy Merge

Revision 1.2.2.1  2004/10/15 19:59:06  mch
Lots of changes during trip to CDS to improve int test pass rate

Revision 1.2  2004/10/06 21:12:17  mch
Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

Revision 1.1  2004/09/28 15:02:13  mch
Merged PAL and server packages

Revision 1.2  2004/08/02 11:34:33  mch
Completed the askQuery


*/



