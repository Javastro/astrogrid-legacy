/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/data/PolicyCredentials.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/01/07 10:45:38 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyCredentials.java,v $
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
 *   Revision 1.4  2003/09/11 03:15:06  dave
 *   1) Implemented PolicyService internals - no tests yet.
 *   2) Added getLocalAccountGroups and getRemoteAccountGroups to PolicyManager.
 *   3) Added remote access to groups.
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
