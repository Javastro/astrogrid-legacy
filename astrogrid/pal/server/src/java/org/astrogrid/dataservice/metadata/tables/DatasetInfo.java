/*
 * $Id: DatasetInfo.java,v 1.1 2005/02/17 18:37:34 mch Exp $
 */
package org.astrogrid.dataservice.metadata.tables;

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
