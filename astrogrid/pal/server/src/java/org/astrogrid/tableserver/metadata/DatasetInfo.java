/*
 * $Id: DatasetInfo.java,v 1.1 2005/03/10 16:42:55 mch Exp $
 */
package org.astrogrid.tableserver.metadata;

/**
 * Wrapper for metadata about a table
 */
import org.astrogrid.dataservice.metadata.queryable.SearchGroup;

public class DatasetInfo extends SearchGroup {
   
   public void setTable(String table) {
      setGroup(table);
   }
   
   public String getTable() {
      return getGroup();
   }
   
}
