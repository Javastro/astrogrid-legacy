/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/data/CommunityIdent.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/12 06:56:45 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityIdent.java,v $
 *   Revision 1.3  2004/02/12 06:56:45  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.2.4.1  2004/01/26 13:18:07  dave
 *   Added new DatabaseManager to enable local JUnit testing
 *
 *   Revision 1.2  2004/01/07 10:45:38  dave
 *   Merged development branch, dave-dev-20031224, back into HEAD
 *
 *   Revision 1.1.2.1  2004/01/05 06:47:18  dave
 *   Moved policy data classes into policy.data package
 *
 *   Revision 1.1.2.1  2003/12/24 05:54:48  dave
 *   Initial Maven friendly structure (only part of the service implemented)
 *
 *   Revision 1.5  2003/11/06 15:35:26  dave
 *   Replaced tabs with spaces
 *
 *   Revision 1.4  2003/09/17 19:47:21  dave
 *   1) Fixed classnotfound problems in the build.
 *   2) Added the JUnit task to add the initial accounts and groups.
 *   3) Got the build to work together with the portal.
 *   4) Fixed some bugs in the Account handling.
 *
 *   Revision 1.3  2003/09/13 02:18:52  dave
 *   Extended the jConfig configuration code.
 *
 *   Revision 1.2  2003/09/10 06:03:27  dave
 *   Added remote capability to Accounts
 *
 *   Revision 1.1  2003/09/08 20:28:50  dave
 *   Added CommunityIdent, with isLocal() and isValid()
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.data ;

import org.astrogrid.community.common.config.CommunityConfig ;

public class CommunityIdent
    {
    /**
     * The default account name.
     *
     */
    public static String DEFAULT_ACCOUNT = "nobody" ;

    /**
     * The separator for name and community.
     *
     */
    public static char IDENT_SEPARATOR = '@' ;

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
    public CommunityIdent(String ident)
        {
        //
        // Check for a null ident.
        if ((null == ident) || (0 == ident.length()))
            {
            ident = DEFAULT_ACCOUNT ;
            }
        //
        // Convert everything to lowercase.
        ident = ident.toLowerCase() ;
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
                this.name = ident.substring(0, first) ;
                this.community = ident.substring(first + 1) ;
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
            this.ident = this.name +  IDENT_SEPARATOR + this.community ;
            this.valid = true ;
            this.local = true ;
            }
        }

    /**
     * Public constructor.
     *
     */
    public CommunityIdent(String name, String community)
        {
        this(name +  IDENT_SEPARATOR + community) ;
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
