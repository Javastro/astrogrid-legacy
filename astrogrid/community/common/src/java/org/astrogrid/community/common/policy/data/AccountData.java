/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/data/AccountData.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/20 21:11:05 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountData.java,v $
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
     *
     */
    public AccountData(String value)
        {
        this.setIdent(value) ;
        }

    /**
     * Our Account ident.
     *
     */
    private String ident ;

    /**
     * Access to our Account ident.
     *
     */
    public String getIdent()
        {
        return this.ident ;
        }

    /**
     * Access to our Account ident.
     *
     */
    public void setIdent(String value)
        {
        this.ident = value ;
        }

    /**
     * Our Account description.
     *
     */
    private String description ;

    /**
     * Access to our Account description.
     *
     */
    public String getDescription()
        {
        return this.description ;
        }

    /**
     * Access to our Account description.
     *
     */
    public void setDescription(String value)
        {
        this.description = value ;
        }

    /*
     * Compare this with another AccountData.
     * All we want to check is the Account ident.
     * TODO This needs to refactored to check for local community in the ident.
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
     * Just uses the ident.hashCode().
     * TODO This needs to refactored to check for local community in the ident.
     *
     */
    public synchronized int hashCode()
        {
        return (null != this.ident) ? this.ident.hashCode() : 0 ;
        }
    }
