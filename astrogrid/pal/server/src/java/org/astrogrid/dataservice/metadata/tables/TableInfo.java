/*
 * $Id: TableInfo.java,v 1.1 2005/02/17 18:37:34 mch Exp $
 */
package org.astrogrid.dataservice.metadata.tables;

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
