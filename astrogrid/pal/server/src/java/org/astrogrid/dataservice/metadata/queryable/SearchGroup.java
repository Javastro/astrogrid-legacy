/*
 * $Id: SearchGroup.java,v 1.1 2005/02/17 18:37:34 mch Exp $
 */
package org.astrogrid.dataservice.metadata.queryable;

/**
 * Wrapper for metadata about a group of search fields, such as a table or
 * a node in an XML document
 */
public class SearchGroup {
   
   private String id;
   private String name;
   private String group;
   private String description;
   
   public SearchGroup() {
   }

   public SearchGroup(String anId, String aName, String parentGroup, String aDesc) {
      this.id = anId;
      this.name = aName;
      this.group = parentGroup;
      this.description = aDesc;
   }
   
   
   public void setGroup(String group)
   {
      this.group = group;
   }
   
   public String getGroup()
   {
      return group;
   }
   
   public void setName(String name)
   {
      this.name = name;
   }
   
   public String getName()
   {
      return name;
   }
   
   
   public void setId(String id)
   {
      this.id = id;
   }
   
   public String getId()
   {
      return id;
   }
   
   public void setDescription(String description)
   {
      this.description = description;
   }
   
   public String getDescription()
   {
      return description;
   }
   
   
}
