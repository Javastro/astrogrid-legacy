/*
 * $Id: SearchField.java,v 1.1 2005/02/17 18:37:34 mch Exp $
 */
package org.astrogrid.dataservice.metadata.queryable;

/**
 * Wrapper for metadata about a search field (eg column, or XML leaf) -
 * its units, ucds, dimensions, related
 * error column, etc
 */
public class SearchField {
   
   private String id;
   private String name;
   private String groupName;
   private String description;
   
   private String datatype;
   private Class javaType;
   private String ucd;
   private String ucdPlus;
   private String errorField;
   private String units;
   private String dimensions;
   private String scale;
   
   public void setDescription(String description)
   {
      this.description = description;
   }
   
   public String getDescription()
   {
      return description;
   }
   
   public void setGroup(String group)
   {
      this.groupName = group;
   }
   
   public String getGroup()
   {
      return groupName;
   }
   
   /**
    * Sets JavaType
    *
    * @param    JavaType            a  Class
    */
   public void setJavaType(Class javaType) {
      this.javaType = javaType;
   }
   
   /**
    * Returns JavaType
    *
    * @return    a  Class
    */
   public Class getJavaType() {
      return javaType;
   }
   
   
   /**
    * Sets Scale
    */
   public void setScale(String scale) {
      this.scale = scale;
   }
   
   /**
    * Returns Scale
    */
   public String getScale() {
      return scale;
   }
   
   /**
    * Sets Dimensions
    */
   public void setDimensions(String dimensions) {
      this.dimensions = dimensions;
   }
   
   /**
    * Returns Dimensions
    */
   public String getDimensions() {
      return dimensions;
   }
   
   /**
    * Sets Units
    */
   public void setUnits(String units) {
      this.units = units;
   }
   
   /**
    * Returns Units
    */
   public String getUnits() {
      return units;
   }
   
   /**
    * Sets ErrorColId
    */
   public void setErrorField(String errorColId) {
      this.errorField = errorColId;
   }
   
   /**
    * Returns ErrorColId
    */
   public String getErrorField() {
      return errorField;
   }
   
   /**
    * Sets UcdPlus
    */
   public void setUcdPlus(String ucdPlus) {
      this.ucdPlus = ucdPlus;
   }
   
   /**
    * Returns UcdPlus
    */
   public String getUcdPlus() {
      return ucdPlus;
   }
   
   /**
    * Sets Ucd
    */
   public void setUcd(String ucd) {
      this.ucd = ucd;
   }
   
   /**
    * Returns Ucd
    */
   public String getUcd() {
      return ucd;
   }
   
   /**
    * Sets Datatype
    */
   public void setDatatype(String datatype) {
      this.datatype = datatype;
   }
   
   /**
    * Returns Datatype
    */
   public String getDatatype() {
      return datatype;
   }
   
   /**
    * Sets Id
    */
   public void setId(String id) {
      this.id = id;
   }
   
   /**
    * Returns Id
    */
   public String getId() {
      return id;
   }
   
   /**
    * Sets Name
    */
   public void setName(String name) {
      this.name = name;
   }
   
   /**
    * Returns Name
    */
   public String getName() {
      return name;
   }
   
}
