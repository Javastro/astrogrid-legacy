/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/data/ResourceIdent.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/01/07 10:45:38 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: ResourceIdent.java,v $
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
 *   Revision 1.2  2003/09/13 02:18:52  dave
 *   Extended the jConfig configuration code.
 *
 *   Revision 1.1  2003/09/10 00:08:45  dave
 *   Added getGroupMembers, ResourceIdent and JUnit tests for ResourceManager
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.data ;

import org.astrogrid.community.common.config.CommunityConfig ;

public class ResourceIdent
    {
    /**
     * The separator for name and community.
     *
     */
    public static char IDENT_SEPARATOR = ':' ;

    /**
     * Flag to indicate the ident is valid.
     *
     */
    private boolean valid = false ;

    /**
     * Flag to indicate the ident is local.
     *
     */
    private boolean local = false ;

    /**
     * Our ident.
     *
     */
    private String ident ;

    /**
     * Our ident name.
     *
     */
    private String name ;

    /**
     * Our Ident community.
     *
     */
    private String community ;

    /**
     * Public constructor.
     *
     */
    public ResourceIdent(String ident)
        {
        //
        // Save the ident.
        this.ident = ident ;
        //
        // Find the first and last separator.
        int first = ident.indexOf(IDENT_SEPARATOR) ;
        int last  = ident.lastIndexOf(IDENT_SEPARATOR) ;
        //
        // If the ident contains separator.
        if (-1 != first)
            {
            //
            // If the first and last are the same.
            if (first == last)
                {
                //
                // Split the ident into name and community.
                this.community = ident.substring(0, first) ;
                this.name      = ident.substring(first + 1) ;
//
// Check the lengths ...
//
                //
                // Check if the community is local.
                if (CommunityConfig.getCommunityName().equals(this.community))
                    {
                    this.valid = true ;
                    this.local = true ;
                    }
                //
                // If the community is not local.
                else {
                    this.valid = true ;
                    this.local = false ;
                    }
                }
            //
            // If the first and last do not match.
            else {
                this.valid = false ;
                this.local = false ;
                this.name  = null ;
                this.community = null ;
                }
            }
        //
        // If the ident does not contain a community.
        else {
            //
            // Use the local community ident.
            this.name = ident ;
            this.community = CommunityConfig.getCommunityName() ;
            this.ident = this.community +  IDENT_SEPARATOR + this.name ;
            this.valid = true ;
            this.local = true ;
            }
        }

    /**
     * Is this a valid ident.
     *
     */
    public boolean isValid()
        {
        return this.valid ;
        }

    /**
     * Is this a local ident.
     *
     */
    public boolean isLocal()
        {
        return this.local ;
        }

    /**
     * Get the name from this ident.
     *
     */
    public String getName()
        {
        return this.name ;
        }

    /**
     * Get the community name from this ident.
     *
     */
    public String getCommunity()
        {
        return this.community ;
        }

    /**
     * Convert the ident as a string.
     *
     */
    public String toString()
        {
        return this.ident ;
        }

    }
