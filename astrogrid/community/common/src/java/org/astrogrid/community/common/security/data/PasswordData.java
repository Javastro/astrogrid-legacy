/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/security/data/Attic/PasswordData.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/20 21:11:05 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * Not required !!!
 * Simpler to just do it with strings.
 *
 *
 * <cvs:log>
 *   $Log: PasswordData.java,v $
 *   Revision 1.3  2004/02/20 21:11:05  dave
 *   Merged development branch, dave-dev-200402120832, into HEAD
 *
 *   Revision 1.2.2.1  2004/02/16 15:20:54  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.2  2004/02/12 08:12:13  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.1.2.1  2004/01/30 14:55:46  dave
 *   Added PasswordData object
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.security.data ;

public class PasswordData
    {
    /**
     * No encryption, clear text password.
     * This is the default at the moment.
     *
     */
    public static final String NO_ENCRYPTION = "CLEAR_TEXT" ;

    /**
     * Public constructor.
     *
     */
    public PasswordData()
        {
        this(null, null) ;
        }

    /**
     * Public constructor.
     *
     */
    public PasswordData(String account, String password)
        {
        this.setAccount(account) ;
        this.setPassword(password) ;
        this.setEncryption(NO_ENCRYPTION) ;
        }

    /**
     * Our Account identifier.
     *
     */
    private String account ;

    /**
     * Access to our Account identifier.
     *
     */
    public String getAccount()
        {
        return this.account ;
        }

    /**
     * Access to our Account identifier.
     *
     */
    public void setAccount(String value)
        {
        this.account = value ;
        }

    /**
     * Our Account password.
     *
     */
    private String password ;

    /**
     * Access to our Account password.
     *
     */
    public String getPassword()
        {
        return this.password ;
        }

    /**
     * Access to our Account password.
     *
     */
    public void setPassword(String value)
        {
        this.password = value ;
        }

    /**
     * Our Account encryption.
     *
     */
    private String encryption ;

    /**
     * Access to our Account encryption.
     *
     */
    public String getEncryption()
        {
        return this.encryption ;
        }

    /**
     * Access to our Account encryption.
     *
     */
    public void setEncryption(String value)
        {
        this.encryption = value ;
        }

    /*
     * Compare this with another PasswordData.
     * This checks both the the Account identifier and password.
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
            // If the object is a PasswordData
            if (object instanceof PasswordData)
                {
                PasswordData that = (PasswordData) object ;
                //
                // If our account identifier is null
                if (null == this.getAccount())
                    {
                    //
                    // Check that account identifier is null.
                    return (null == that.getAccount()) ;
                    }
                //
                // If our account identifier is not null.
                else {
                    //
                    // Check both the account identifier and password are the same.
                    return (
                        this.getAccount().equals(that.getAccount())
                        &&
                        this.getPassword().equals(that.getPassword())
                        ) ;
                    }
                }
            //
            // If object is not a PasswordData
            else {
                return false ;
                }
            }
        }

    /**
     * Generate a hash code for comparison tests.
     * This combines the hash code for both identifier and password.
     *
     */
    public synchronized int hashCode()
        {
        int result = 0 ;
        //
        // If we have a valid account.
        if (null != this.getAccount())
            {
            result += this.getAccount().hashCode() ;
            }
        //
        // If we have a valid password.
        if (null != this.getPassword())
            {
            result += this.getPassword().hashCode() ;
            }
        return result ;
        }
    }
