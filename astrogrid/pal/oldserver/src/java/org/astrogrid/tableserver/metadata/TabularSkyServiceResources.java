/*
 * $Id: TabularSkyServiceResources.java,v 1.2 2008/02/14 15:02:01 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.metadata;

import java.io.IOException;
import org.astrogrid.dataservice.metadata.VoResourcePlugin;
import org.astrogrid.tableserver.metadata.ColumnInfo;
import org.astrogrid.tableserver.metadata.TableInfo;
import org.astrogrid.dataservice.metadata.v0_10.VoResourceSupport;

/**
 * Generates Resource elements for tabular sky services.
 */

public class TabularSkyServiceResources extends VoResourceSupport implements VoResourcePlugin  {
   
   
   /** Generates a voResource element about the database.
    *  */
   public String getVoResource() throws IOException {

      StringBuffer tabularDb = new StringBuffer(
         makeVoResourceElement("vor:TabularSkyService", "", "")+
         makeCore("SkyService","")
      );
      
      tabularDb.append(
         "</"+VORESOURCE_ELEMENT+">");
      
      return tabularDb.toString();
   }
   
}


