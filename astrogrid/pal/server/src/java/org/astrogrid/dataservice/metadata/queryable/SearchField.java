/*
 * $Id: SearchField.java,v 1.2 2005/03/10 13:49:52 mch Exp $
 */
package org.astrogrid.dataservice.metadata.queryable;

/**
 * Wrapper for metadata about a search field (eg column, or XML leaf) -
 * its units, ucds, dimensions, related
 * error column, etc
 */
import java.util.Hashtable;
import java.util.Vector;
import org.astrogrid.units.Units;

public class SearchField {
   
   private String id;
   private String name;
   private String groupName;
   private String description;
   
   private String datatype;
   private Class javaType;
   private String errorField;
   private Units units;

   //look up of UCDs of different versions (ie 1, 1+)
   private Hashtable ucds = new Hashtable();

   //links to more info about the field
   private Vector links = new Vector();
   
   public void setDescription(String description)
   {
      this.description = description;
   }
   
   public String getDescription()      {        return description;   }
   
   public void setGroup(String group)  {        this.groupName = group;   }
   
   public String getGroup()            {        return groupName;   }
   
   public void setJavaType(Class javaType) {    this.javaType = javaType;   }
   
   public Class getJavaType()          {        return javaType;   }
   
   public void setUnits(String units) {
      this.units = new Units(units);
   }

   public void setUnits(Units units) {
      this.units = units;
   }
   
   public Units getUnits() {
      return units;
   }
   
   public void setErrorField(String errorColId) {
      this.errorField = errorColId;
   }
   
   public String getErrorField() {
      return errorField;
   }
   
   /**
    * Sets Ucd with the given version
    */
   public void setUcd(String ucd, String version) {
      ucds.put(version, ucd);
   }
   
   /**
    * Returns Ucd of the given version
    */
   public String getUcd(String version) {
      return (String) ucds.get(version);
   }
   
   public void setDatatype(String datatype) {      this.datatype = datatype;   }
   
   public String getDatatype() {      return datatype;   }
   
   public void setId(String id) {      this.id = id;   }
   
   public String getId() {      return id;   }
   
   public void setName(String name) {      this.name = name;   }
   
   public String getName() {      return name;   }
   
   public String[] getLinks() {
      return (String[]) links.toArray(new String[] {} );
   }
   
}
