/*
 * $Id: TabularSkyServiceResources.java,v 1.1 2005/03/10 16:42:55 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.metadata;

import java.io.IOException;
import org.astrogrid.dataservice.metadata.VoResourcePlugin;
import org.astrogrid.tableserver.metadata.ColumnInfo;
import org.astrogrid.tableserver.metadata.TableInfo;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.dataservice.metadata.v0_10.VoResourceSupport;

/**
 * Generates Resource elements for tabular sky services.
 */

public class TabularSkyServiceResources extends VoResourceSupport implements VoResourcePlugin  {
   
   
   /** Generates a voResource element about the database.
    *  */
   public String getVoResource() throws IOException {

      TableMetaDocInterpreter reader = new TableMetaDocInterpreter();

      StringBuffer tabularDb = new StringBuffer(
         makeVoResourceElement("vor:TabularSkyService")+
         makeCore("/SkyService")
      );
      
      tabularDb.append(
         "</vor:Resource>");
      
      return tabularDb.toString();
   }
   
}


