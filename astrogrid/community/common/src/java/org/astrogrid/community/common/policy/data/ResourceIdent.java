/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/data/ResourceIdent.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/14 13:50:07 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: ResourceIdent.java,v $
 *   Revision 1.4  2004/07/14 13:50:07  dave
 *   Merged development branch, dave-dev-200406301228, into HEAD
 *
 *   Revision 1.3.12.1  2004/07/05 14:18:55  dave
 *   Tried to remove the JConfig libraries
 *
 *   Revision 1.3  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.2.70.1  2004/06/17 13:38:58  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.data ;

import org.astrogrid.community.common.config.CommunityConfig ;

/**
 * @todo deprecate the whole class.
 *
 *
 */
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
