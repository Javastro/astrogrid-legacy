/*
 * $Id: SearchField.java,v 1.2 2010/12/08 12:46:35 gtr Exp $
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
   private String groupID;
   private String parentName; //To support one more level of hierarchy
   private String parentID;
   private String description;
   
   /** interoperating type - presented to the outside world */
   private String publicType;
   /** Java type to hold the values */
   private Class javaType;
   /** The type as given by the packing store - eg the SQL type */
   private String backType;
   private String errorField;
   private Units units;

   //look up of UCDs of different versions (ie 1, 1+)
   private Hashtable ucds = new Hashtable();

   /**
    * The data-model node, or Utype. Not all fields have a Utype.
    */
   private String utype;

   //links to more info about the field
   private Vector links = new Vector();
   
   public void setBackType(String givenType) {      this.backType = givenType;  }
   
   public String getBackType() {    return backType;  }
   
   public void setDescription(String description)
   {
      this.description = description;
   }
   
   public String getDescription()      {        return description;   }
   
   public void setGroup(String groupName, String groupID)  {        
      this.groupName = groupName;   
      this.groupID = groupID;   
   }
   public void setParent(String parentName, String parentID)  {        
      this.parentName = parentName;   
      this.parentID = parentID;   
   }
   
   public String getGroupName()          {        return groupName;   }
   public String getGroupID()            {        return groupID;     }
   public String getParentName()         {        return parentName;  }
   public String getParentID()           {        return parentID;    }
   
   public void setJavaType(Class javaType) {    this.javaType = javaType;   }
   
   public Class getJavaType()          {        return javaType;   }
   
   public void setUnits(String units) {
      this.units = new Units(units);
   }

   /**
    * Reveals the data-model node, if the field has one.
    *
    * @return The Utype (null if no utype known for this field).
    */
   public String getUtype() {
     return utype;
   }

   /**
    * Sets the data-model node for this field.
    *
    * @param utype (null => field has no utype).
    */
   public void setUtype(String utype) {
     this.utype = utype;
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
      if (ucd == null) {
         ucds.remove(version);
      }
      else {
         ucds.put(version, ucd);
      }
   }
   
   /**
    * Returns Ucd of the given version
    */
   public String getUcd(String version) {
      return (String) ucds.get(version);
   }
   
   public void setPublicType(String datatype) {      this.publicType = datatype;   }
   
   public String getPublicType() {      return publicType;   }
   
   public void setId(String id) {      this.id = id;   }
   
   public String getId() {      return id;   }
   
   public void setName(String name) {      this.name = name;   }
   
   public String getName() {      return name;   }
   
   public String[] getLinks() {
      return (String[]) links.toArray(new String[] {} );
   }
   
}

