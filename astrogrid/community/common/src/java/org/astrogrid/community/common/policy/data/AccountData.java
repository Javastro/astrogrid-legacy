/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/data/AccountData.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/15 07:49:30 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountData.java,v $
 *   Revision 1.7  2004/03/15 07:49:30  dave
 *   Merged development branch, dave-dev-200403121536, into HEAD
 *
 *   Revision 1.6.2.1  2004/03/12 17:42:09  dave
 *   Replaced tabs with spaces
 *
 *   Revision 1.6  2004/03/12 15:22:17  dave
 *   Merged development branch, dave-dev-200403101018, into HEAD
 *
 *   Revision 1.5.18.1  2004/03/10 13:32:00  dave
 *   Added home space to AccountData.
 *   Improved null param checking in AccountManager.
 *   Improved null param checking in AccountManager tests.
 *
 *   Revision 1.5  2004/02/20 21:11:05  dave
 *   Merged development branch, dave-dev-200402120832, into HEAD
 *
 *   Revision 1.4.2.1  2004/02/16 15:20:54  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.4  2004/02/12 08:12:13  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.2.4.6  2004/01/30 03:21:23  dave
 *   Added initial code for SecurityManager and SecurityService
 *
 *   Revision 1.2.4.5  2004/01/27 19:09:52  dave
 *   Fixed dumb typo ...
 *
 *   Revision 1.2.4.4  2004/01/27 19:07:28  dave
 *   Fixed PMD report violations
 *   1) Removed unused imports.
 *   2) Added override for hashCode() to match custom equals().
 *
 *   Revision 1.2.4.3  2004/01/17 13:54:18  dave
 *   Removed password from AccountData
 *
 *   Revision 1.2.4.2  2004/01/13 15:17:40  dave
 *   Put password back in for a moment
 *
 *   Revision 1.2.4.1  2004/01/13 14:29:41  dave
 *   Added initial JUnit tests
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
 *   Revision 1.4  2003/09/09 16:41:53  KevinBenson
 *   Added password field
 *
 *   Revision 1.3  2003/09/06 20:10:07  dave
 *   Split PolicyManager into separate components.
 *
 *   Revision 1.2  2003/09/04 23:58:10  dave
 *   Experimenting with using our own DataObjects rather than the Axis generated ones ... seems to work so far
 *
 *   Revision 1.1  2003/09/03 06:39:13  dave
 *   Rationalised things into one set of SOAP stubs and one set of data objects for both client and server.
 *
 * </cvs:log>
 *
 * TODO : Refactor this into AccountData and AccountDetails.
 * TODO : Refactor this to inherit from CommunityBase (common code for equals and ident handling).
 *
 */
package org.astrogrid.community.common.policy.data ;

public class AccountData
    {
    /**
     * Public constructor.
     *
     */
    public AccountData()
        {
        this(null) ;
        }

    /**
     * Public constructor.
     * No syntax checking is applied to the ident.
     * @param ident The Account ident.
     * @TODO Add syntax checking.
     *
     */
    public AccountData(String ident)
        {
        this.setIdent(ident) ;
        }

    /**
     * The Account ident.
     * At the monet, the syntax is 'name@community', this will be refactored to 'ivo:community/name'.
     * No syntax checking is applied to the value.
     * @TODO Refactor the Account ident to 'ivo:community/name'.
     * @TODO Add syntax checking.
     *
     */
    private String ident ;

    /**
     * Access to the Account ident.
     * @return The current Account ident.
     *
     */
    public String getIdent()
        {
        return this.ident ;
        }

    /**
     * Access to the Account ident.
     * This will fail the the ident is already set - you can't change the ident of an existing Account.
     * No syntax checking is applied to the value.
     * @param value The Account ident.
     * @TODO Add syntax checking.
     *
     */
    public void setIdent(String value)
        {
        if (null == this.ident)
            {
            this.ident = value ;
            }
        }

    /**
     * The Account display name.
     * This is intended for use in the portal display pages.
     * Displaying 'DaveMorris' in the pages, rather than 'ivo:astrogrid.org/dave'
     *
     */
    private String display ;

    /**
     * Access to the Account display name.
     * @return The current display name.
     *
     */
    public String getDisplayName()
        {
        return this.display ;
        }

    /**
     * Access to the Account display name.
     * @param value The new display name.
     *
     */
    public void setDisplayName(String value)
        {
        this.display = value ;
        }

    /**
     * The Account description.
     * This is intended for use in the portal display pages.
     *
     */
    private String description ;

    /**
     * Access to the Account description.
     * @return The current description.
     *
     */
    public String getDescription()
        {
        return this.description ;
        }

    /**
     * Access to the Account description.
     * @param value The new description.
     *
     */
    public void setDescription(String value)
        {
        this.description = value ;
        }

    /**
     * The Account home space URI.
     * This is the MySpace or VoSpace URI for the account home.
     * No syntax checking is applied to the value.
     * @TODO Add syntax checking.
     *
     */
    private String home ;

    /**
     * Access to the Account home space URI.
     * @return The current home space URI.
     *
     */
    public String getHomeSpace()
        {
        return this.home ;
        }

    /**
     * Access to the Account home space URI.
     * No syntax checking is applied to the value.
     * @param value The new home space URI.
     * @TODO Add syntax checking.
     *
     */
    public void setHomeSpace(String value)
        {
        this.home = value ;
        }

    /**
     * The Account email address.
     * The contact email address for the Account owner.
     * No syntax checking is applied to the value.
     * @TODO Add syntax checking.
     *
     */
    private String email ;

    /**
     * Access to the Account email address.
     * @return The current email address.
     *
     */
    public String getEmailAddress()
        {
        return this.email ;
        }

    /**
     * Access to the Account email address.
     * No syntax checking is applied to the value.
     * @param value The new email address.
     * @TODO Add syntax checking.
     *
     */
    public void setEmailAddress(String value)
        {
        this.email = value ;
        }

    /*
     * Compare this with another AccountData.
     * All we want to check is the Account ident.
     * @TODO This needs to refactored to check for local community in the ident.
     * @param object The Object to compare.
     * @return true if the two object represetnt the same Account (the idents are equivalent).
     *
     */
    public synchronized boolean equals(Object object)
        {
        //
        // If the object is null.
        if (null == object)
            {
            return false ;
            }
        //
        // If the object is not null.
        else {
            //
            // If the object is an AccountData
            if (object instanceof AccountData)
                {
                AccountData that = (AccountData) object ;
                //
                // If our ident is null
                if (null == this.ident)
                    {
                    //
                    // Check that ident is null.
                    return (null == that.getIdent()) ;
                    }
                //
                // If our ident is not null.
                else {
                    //
                    // Check that ident is the same.
                    return (this.ident.equals(that.getIdent())) ;
                    }
                }
            //
            // If that is not an AccountData
            else {
                return false ;
                }
            }
        }

    /**
     * Generate a hash code for comparison tests.
     * At the moment, this just uses the ident.hashCode().
     * @TODO This needs to refactored to check for local community in the ident.
     *
     */
    public synchronized int hashCode()
        {
        return (null != this.ident) ? this.ident.hashCode() : 0 ;
        }
    }
