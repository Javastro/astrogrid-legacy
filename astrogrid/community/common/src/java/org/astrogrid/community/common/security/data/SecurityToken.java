/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/security/data/SecurityToken.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/05 17:19:59 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityToken.java,v $
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.3  2004/03/04 16:33:50  dave
 *   Added String constructor to SecurityToken
 *
 *   Revision 1.1.2.2  2004/03/02 15:29:35  dave
 *   Working round Castor problem with PasswordData - objects remain in database cache
 *
 *   Revision 1.1.2.1  2004/02/27 16:22:14  dave
 *   Added SecurityService interface, mock and test
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.security.data ;

public class SecurityToken
    {
    /**
     * Status value for an invalid token.
     *
     */
    public static final int INVALID_TOKEN = 0x00 ;

    /**
     * Status value for a valid token.
     *
     */
    public static final int VALID_TOKEN = 0x01 ;

    /**
     * Delimiter for String version of SecurityToken.
     *
     */
    public static final String DELIMTER_STRING = "," ;

    /**
     * Public constructor.
     *
     */
    public SecurityToken()
        {
        this(null, null) ;
        }

    /**
     * Public constructor from a String.
     *
     */
    public SecurityToken(String value)
        {
		if (null != value)
			{
	        //
			// Split the string on delimiters.
			String[] split = value.split(DELIMTER_STRING) ;
			//
			// If we have a Token value.
			if (split.length > 0)
				{
				this.setToken(split[0]) ;
				}
			//
			// If we have an Account ident.
			if (split.length > 1)
				{
				this.setAccount(split[1]) ;
				}
			}
        }

    /**
     * Public constructor.
     *
     */
    public SecurityToken(String account, String token)
        {
        this.setAccount(account) ;
        this.setToken(token) ;
        this.setStatus(INVALID_TOKEN) ;
        }

    /**
     * The Account identifier that this token was issued to.
     *
     */
    private String account ;

    /**
     * Get our Account identifier.
     *
     */
    public String getAccount()
        {
        return this.account ;
        }

    /**
     * Set our Account identifier.
     * Fails if the identifier is already set.
     *
     */
    public void setAccount(String value)
        {
        if (null == this.account)
        	{
        	this.account = value ;
			}
        }

    /**
     * Our token value.
     *
     */
    private String token ;

    /**
     * Get the token value.
     *
     */
    public String getToken()
        {
        return this.token ;
        }

    /**
     * Set our token value.
     * Fails if the token is already set.
     *
     */
    public void setToken(String value)
        {
        if (null == this.token)
        	{
        	this.token = value ;
			}
        }

	/**
	 * The token status.
	 *
	 */
	private int status ;

	/**
	 * Get the token status.
	 *
	 */
	public int getStatus()
		{
		return this.status ;
		}

	/**
	 * Set the token status.
	 *
	 */
	public void setStatus(int value)
		{
		this.status = value ;
		}

	/**
	 * Chec the token status.
	 *
	 */
	public boolean isValid()
		{
		return (this.status == VALID_TOKEN) ;
		}

	/**
	 * Convert the token to a string.
	 *
	 */
	public String toString()
		{
		return this.token + DELIMTER_STRING + this.account ;
		}

    /*
     * Compare this with another SecurityToken.
     * This checks both the the Account identifier and value.
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
            // If the object is a SecurityToken
            if (object instanceof SecurityToken)
                {
                SecurityToken that = (SecurityToken) object ;
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
                    // Check both the account identifier and token are the same.
                    return (
                        this.getAccount().equals(that.getAccount())
                        &&
                        this.getToken().equals(that.getToken())
                        ) ;
                    }
                }
            //
            // If object is not a SecurityToken
            else {
                return false ;
                }
            }
        }

    /**
     * Generate a hash code for comparison tests.
     * This combines the hash code for both identifier and token value.
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
        // If we have a valid token.
        if (null != this.getToken())
            {
            result += this.getToken().hashCode() ;
            }
        return result ;
        }
    }
