/*
 * $Id: DatasetInfo.java,v 1.1.1.1 2009/05/13 13:20:49 gtr Exp $
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
