/*
 * $Id: ColumnInfo.java,v 1.1 2009/05/13 13:20:49 gtr Exp $
 */
package org.astrogrid.tableserver.metadata;

import org.astrogrid.dataservice.metadata.StdDataTypes;
import org.astrogrid.dataservice.metadata.queryable.SearchField;

/**
 * Wrapper for metadata about a column - its units, ucds, dimensions, related
 * error column, etc
 */

public class ColumnInfo extends SearchField {
   
   /** Bean constructor */
   public ColumnInfo() {
   }
   
   /** Convenience constructor for tests etc */
   public ColumnInfo(String name, String ID, String groupName, String groupID, String description, String type, String ucd, String units) {
      setName(name);
      setGroup(groupName, groupID);
      setId(ID);
      setDescription(description);
      setJavaType(StdDataTypes.getJavaType(type));
      setPublicType(type);
      setUcd(ucd, "1");
      setUnits(units);
   }
   
   /** Convenience constructor for tests etc */
   public ColumnInfo(String name, String ID, String groupName, String groupID, String description, Class javatype, String ucd, String units) {
      setName(name);
      setGroup(groupName, groupID);
      setId(ID);
      setDescription(description);
      setJavaType(javatype);
      setPublicType(StdDataTypes.getStdType(javatype));
      setUcd(ucd, "1");
      setUnits(units);
   }
}
