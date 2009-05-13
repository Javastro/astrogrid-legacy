/*
 * $Id: SearchGroup.java,v 1.1 2009/05/13 13:20:24 gtr Exp $
 */
package org.astrogrid.dataservice.metadata.queryable;

/**
 * Wrapper for metadata about a group of search fields, such as a table or
 * a node in an XML document
 */
public class SearchGroup {
   
   private String id;
   private String name;
   private String groupName;
   private String groupID;
   private String description;
   
   public SearchGroup() {
   }

   public SearchGroup(String anId, String aName, String parentGroupName, String parentGroupID, String aDesc) {
      this.id = anId;
      this.name = aName;
      this.groupName = parentGroupName;
      this.groupID = parentGroupID;
      this.description = aDesc;
   }
   
   
   public void setGroup(String groupName, String groupID)
   {
      this.groupName = groupName;
      this.groupID = groupID;
   }
   
   public String getGroupName()
   {
      return groupName;
   }
   public String getGroupID()
   {
      return groupID;
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
