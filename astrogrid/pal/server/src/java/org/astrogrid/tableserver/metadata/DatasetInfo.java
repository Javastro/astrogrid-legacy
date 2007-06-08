/*
 * $Id: DatasetInfo.java,v 1.2 2007/06/08 13:16:09 clq2 Exp $
 */
package org.astrogrid.tableserver.metadata;

/**
 * Wrapper for metadata about a table
 */
import org.astrogrid.dataservice.metadata.queryable.SearchGroup;

public class DatasetInfo extends SearchGroup {
   
   public void setTable(String tableName, String tableID) {
      setGroup(tableName, tableID);
   }
   
   public String getTableID() {
      return getGroupID();
   }
   
   public String getTableName() {
      return getGroupName();
   }
}
