/*
 * $Id: TableInfo.java,v 1.1.1.1 2009/05/13 13:20:49 gtr Exp $
 */
package org.astrogrid.tableserver.metadata;

/**
 * Wrapper for metadata about a table
 */
import org.astrogrid.dataservice.metadata.queryable.SearchGroup;

public class TableInfo extends SearchGroup {
   
   private boolean conesearchable = false;
   // If the table is conesearchable, the Names of the RA and Dec cols
   private String coneRAColName = "";
   private String coneDecColName = "";
   
   public void setCatalog(String catalogName, String catalogID)
   {
      setGroup(catalogName, catalogID);
   }
   
   public String getCatalogName()
   {
      return getGroupName();
   }
   public String getCatalogID()
   {
      return getGroupID();
   }

   // Conesearch methods
   public void setConesearchable(boolean conesearchable)
   {
      this.conesearchable = conesearchable;
   }
   public boolean getConesearchable()
   {
      return this.conesearchable;
   }

   public void setConeRAColName(String coneRAColName)
   {
      this.coneRAColName = coneRAColName;
   }
   public String getConeRAColName()
   {
      return this.coneRAColName;
   }

   public void setConeDecColName(String coneDecColName)
   {
      this.coneDecColName = coneDecColName;
   }
   public String getConeDecColName()
   {
      return this.coneDecColName;
   }
}
