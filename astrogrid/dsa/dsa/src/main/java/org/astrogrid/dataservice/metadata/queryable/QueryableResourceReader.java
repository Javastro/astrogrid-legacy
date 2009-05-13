/*
 * $Id: QueryableResourceReader.java,v 1.1 2009/05/13 13:20:24 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.metadata.queryable;


import java.io.IOException;

/**
 * Things that represent queryable data can present their metadata through this
 * interface.  Publishing the whole thing may be too large for aggregate systems
 * (eg Vizier) so this allows us to ask for a bit at a time, suitably eg for
 * query builders.
 * A queryable resource consists of 'tree' like search fields, where a search field
 * (eg a column) may be a member of a group (eg a table),  and that group may be part
 * of a bigger group (eg a dataset)
 */

public interface QueryableResourceReader {
   

   /** Returns group information about the group with the given ID within the parent
    * group */
   public SearchGroup getGroup(SearchGroup parent, String groupId) throws IOException ;

   /** Returns field information about the field with the given ID within the parent group */
   public SearchField getField(SearchGroup parent, String fieldId) throws IOException;
   
   /** Returns the list of 'root' groups - ie top level groups */
   public SearchGroup[] getRootGroups()  throws IOException;

   /** Returns the list of groups in the given parent group */
   public SearchGroup[] getGroups(SearchGroup parent) throws IOException;
   
   /** Returns the list of fields in the given parent group */
   public SearchField[] getFields(SearchGroup parent)  throws IOException;
   
   /** Special case for spatial searches.  Returns the groups that contain
    * fields suitable for spatial searching */
   public SearchGroup[] getSpatialGroups() throws IOException;

   /** Special case for spatial searches.  Returns the fields in the given
    * group that can be used for spatial searching */
   public SearchField[] getSpatialFields(SearchGroup parent) throws IOException;
}


