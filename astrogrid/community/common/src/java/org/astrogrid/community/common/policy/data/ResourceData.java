/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/data/ResourceData.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/01/07 10:45:38 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: ResourceData.java,v $
 *   Revision 1.2  2004/01/07 10:45:38  dave
 *   Merged development branch, dave-dev-20031224, back into HEAD
 *
 *   Revision 1.1.2.1  2004/01/05 06:47:18  dave
 *   Moved policy data classes into policy.data package
 *
 *   Revision 1.1.2.1  2003/12/24 05:54:48  dave
 *   Initial Maven friendly structure (only part of the service implemented)
 *
 *   Revision 1.3  2003/11/06 15:35:26  dave
 *   Replaced tabs with spaces
 *
 *   Revision 1.2  2003/09/10 00:08:45  dave
 *   Added getGroupMembers, ResourceIdent and JUnit tests for ResourceManager
 *
 *   Revision 1.1  2003/09/09 19:15:10  KevinBenson
 *   New ResourceData file
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.data ;

public class ResourceData
    {
    /**
     * Public constructor.
     *
     */
    public ResourceData()
        {
        this(null, null) ;
        }

    /**
     * Public constructor.
     *
     */
    public ResourceData(String ident)
        {
        this(ident, null) ;
        }

    /**
     * Public constructor.
     *
     */
    public ResourceData(String ident, String description)
        {
        this.ident = ident ;
        this.description = description ;
        }

    /**
     * Our Resource ident.
     *
     */
    private String ident ;

    /**
     * Access to our Resource ident.
     *
     */
    public String getIdent()
        {
        return this.ident ;
        }

    /**
     * Access to our Resource ident.
     *
     */
    public void setIdent(String value)
        {
        this.ident = value ;
        }


      /**
       * Our Resource description.
       *
       */
      private String description ;

      /**
       * Access to our Resource description.
       *
       */
      public String getDescription()
         {
         return this.description ;
         }

      /**
       * Access to our Resource description.
       *
       */
      public void setDescription(String value)
         {
         this.description = value ;
         }
    }
