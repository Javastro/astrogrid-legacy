/*
 * $Id: TableInfo.java,v 1.1 2005/03/10 16:42:55 mch Exp $
 */
package org.astrogrid.tableserver.metadata;

/**
 * Wrapper for metadata about a table
 */
import org.astrogrid.dataservice.metadata.queryable.SearchGroup;

public class TableInfo extends SearchGroup {
   
   
   public void setDataset(String dataset)
   {
      setGroup(dataset);
   }
   
   public String getDataset()
   {
      return getGroup();
   }
   
   
}
