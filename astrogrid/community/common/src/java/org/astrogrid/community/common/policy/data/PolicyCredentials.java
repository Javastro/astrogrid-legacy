/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/data/PolicyCredentials.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyCredentials.java,v $
 *   Revision 1.4  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.3.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
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

public class PolicyCredentials
    {
    /**
     * Status code for valid credentials.
     *
     */
    public static final int STATUS_VALID = 0xFF ;

    /**
     * Status code for credentials unchecked.
     *
     */
    public static final int STATUS_NOT_KNOWN = 0x00 ;

    /**
     * Status code for credentials invalid.
     *
     */
    public static final int STATUS_NOT_VALID = 0x01 ;

    /**
     * Public constructor.
     *
     */
    public PolicyCredentials()
        {
        this(null, null) ;
        }

    /**
     * Public constructor.
     *
     */
    public PolicyCredentials(String account, String group)
        {
        this.group   = group   ;
        this.account = account ;
        this.status  = STATUS_NOT_KNOWN ;
        }

    /**
     * Our Account ident.
     *
     */
    private String account ;

    /**
     * Access to our Account ident.
     *
     */
    public String getAccount()
        {
        return this.account ;
        }

    /**
     * Access to our Account ident.
     *
     */
    public void setAccount(String value)
        {
        this.account = value ;
        }

    /**
     * Our Group ident.
     *
     */
    private String group ;

    /**
     * Access to our Group ident.
     *
     */
    public String getGroup()
        {
        return this.group ;
        }

    /**
     * Access to our Group ident.
     *
     */
    public void setGroup(String value)
        {
        this.group = value ;
        }

    /**
     * The status value.
     *
     */
    private int status ;

    /**
     * Access to the status.
     *
     */
    public int getStatus()
        {
        return this.status ;
        }

    /**
     * Access to the status.
     *
     */
    public void setStatus(int value)
        {
        this.status = value ;
        }

    /**
     * Access to the status.
     *
     */
    public boolean isValid()
        {
        return (STATUS_VALID == this.status) ;
        }

    /**
     * The status reason, explains why.
     *
     */
    private String reason ;

    /**
     * Access to the reason.
     *
     */
    public String getReason()
        {
        return this.reason ;
        }

    /**
     * Access to the reason.
     *
     */
    public void setReason(String value)
        {
        this.reason = value ;
        }
    }
