/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/data/AccountData.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.9 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountData.java,v $
 *   Revision 1.9  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.8.2.2  2004/03/21 06:41:41  dave
 *   Refactored to include Exception handling.
 *
 *   Revision 1.8.2.1  2004/03/20 06:54:11  dave
 *   Added addAccount(AccountData) to PolicyManager et al.
 *   Added XML loader for AccountData.
 *
 * </cvs:log>
 *
 *
 */
package org.astrogrid.community.common.policy.data ;

/**
 * A data object to represent an Account.
 * @todo Refactor this into AccountData and AccountDetails.
 * @todo Refactor this to inherit from CommunityBase (common code for equals and ident handling).
 *
 */
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
     * @param ident The Account ident.
     * @todo Add syntax checking.
     *
     */
    public AccountData(String ident)
        {
        this.setIdent(ident) ;
        }

    /**
     * The Account identifier.
     * At the monet, the syntax is 'name@community', this will be refactored to 'ivo:community/name'.
     * @todo Refactor to use Ivorns.
     * @todo Move this to a common data object base class
     *
     */
    private String ident ;

    /**
     * Access to the Account identifier.
     * @return The Account identifier.
     * @todo Move this to a common data object base class
     *
     */
    public String getIdent()
        {
        return this.ident ;
        }

    /**
     * Access to the Account identifier.
     * This will fail the the identifier is already set - you can't change the identifier of an existing Account.
     * No syntax checking is applied to the value.
     * @param value The Account identifier.
     * @todo Refactor to use Ivorns.
     * @todo Move this to a common data object base class
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
     * @todo Add syntax checking.
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
     * Access to the Account home space URI (Ivorn).
     * No syntax checking is applied to the value.
     * @param value The new home space URI.
     * @todo Add syntax checking.
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
     * @todo Add syntax checking.
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
     * @todo Add syntax checking.
     *
     */
    public void setEmailAddress(String value)
        {
        this.email = value ;
        }

    /*
     * Compare this with another AccountData.
     * All we want to check is the Account ident.
     * @param object The Object to compare.
     * @return true if the two object represetnt the same Account (the idents are equivalent).
     * @todo Needs to refactored to check for local community in the ident.
     * @todo Move this to a common data object base class
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
     * @todo This needs to refactored to check for local community in the ident.
     * @todo Move this to a common data object base class
     *
     */
    public synchronized int hashCode()
        {
        return (null != this.ident) ? this.ident.hashCode() : 0 ;
        }
    }
