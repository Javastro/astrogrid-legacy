/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/data/ResourceData.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: ResourceData.java,v $
 *   Revision 1.3  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.2.70.2  2004/06/17 13:38:58  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.data ;

import org.astrogrid.community.common.identifier.ResourceIdentifier ;

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
     * @param ident The Resource identifier.
     *
     */
    public ResourceData(String ident)
        {
        this(ident, null) ;
        }

    /**
     * Public constructor.
     * @param ident The Resource identifier.
     *
     */
    public ResourceData(ResourceIdentifier ident)
        {
        this(ident.toString(), null) ;
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
