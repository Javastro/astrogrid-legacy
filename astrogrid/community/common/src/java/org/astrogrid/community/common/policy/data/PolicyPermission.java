/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/data/PolicyPermission.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyPermission.java,v $
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

public class PolicyPermission
    {
    /**
     * Status code for permission granted.
     *
     */
    public static final int STATUS_PERMISSION_GRANTED = 0xFF ;

    /**
     * Status code for no permission.
     *
     */
    public static final int STATUS_PERMISSION_UNKNOWN = 0x00 ;

    /**
     * Status code for permission revoked.
     *
     */
    public static final int STATUS_PERMISSION_REVOKED = 0x01 ;

    /**
     * Status code for credentials invalid.
     *
     */
    public static final int STATUS_CREDENTIALS_INVALID = 0x02 ;

    /**
     * The XML status code for permission granted.
     *
     */
    public static final String XML_PERMISSION_GRANTED = "GRANT" ;

    /**
     * The XML status code no permission.
     *
     */
    public static final String XML_PERMISSION_UNKNOWN = "UNKNOWN" ;

    /**
     * The XML status code for permission revoked.
     *
     */
    public static final String XML_PERMISSION_REVOKED = "REVOKED" ;

    /**
     * The XML status code for credentials invalid.
     *
     */
    public static final String XML_CREDENTIALS_INVALID = "INVALID" ;

    /**
     * The reason text for permission unknown.
     *
     */
    public static final String REASON_PERMISSION_UNKNOWN = "Permissions unknown" ;

    /**
     * The reason text for no permission set.
     *
     */
    public static final String REASON_NO_PERMISSION = "No permissions set" ;

    /**
     * The reason text for invalid credentials.
     *
     */
    public static final String REASON_CREDENTIALS_INVALID = "Credentials invalid" ;


    /**
     * Public constructor.
     *
     */
    public PolicyPermission()
        {
        }

    /**
     * Public constructor.
     *
     */
    public PolicyPermission(String resource, String group, String action)
        {
        this.group    = group    ;
        this.action   = action   ;
        this.resource = resource ;
        this.status   = STATUS_PERMISSION_UNKNOWN ;
        this.reason   = REASON_PERMISSION_UNKNOWN ;
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
     * Our Resource ident.
     *
     */
    private String resource ;

    /**
     * Access to our Resource ident.
     *
     */
    public String getResource()
        {
        return this.resource ;
        }

    /**
     * Access to our Resource ident.
     *
     */
    public void setResource(String value)
        {
        this.resource = value ;
        }

    /**
     * The allowed action.
     *
     */
    private String action ;

    /**
     * Access to the action.
     *
     */
    public String getAction()
        {
        return this.action ;
        }

    /**
     * Access to the action.
     *
     */
    public void setAction(String value)
        {
        this.action = value ;
        }

    /**
     * The permission status.
     *
     */
    private int status ;

    /**
     * Access to the status code.
     *
     */
    public int getStatus()
        {
        return this.status ;
        }

    /**
     * Set the status code to a specific value.
     *
     */
    public void setStatus(int value)
        {
        this.status = value ;
        }

    /**
     * Set the status using a String from an XML attribute.
     *
     */
    public void setStatus(String value)
        {
        //
        // Default to unknown.
        setStatus(STATUS_PERMISSION_UNKNOWN) ;
        //
        // Decode the status string.
        if (XML_PERMISSION_GRANTED.equals(value))
            {
            setStatus(STATUS_PERMISSION_GRANTED) ;
            }
        if (XML_PERMISSION_UNKNOWN.equals(value))
            {
            setStatus(STATUS_PERMISSION_UNKNOWN) ;
            }
        if (XML_PERMISSION_REVOKED.equals(value))
            {
            setStatus(STATUS_PERMISSION_REVOKED) ;
            }
        if (XML_CREDENTIALS_INVALID.equals(value))
            {
            setStatus(STATUS_CREDENTIALS_INVALID) ;
            }
        }

    /**
     * Check if the status is granteed.
     *
     */
    public boolean isValid()
        {
        return (STATUS_PERMISSION_GRANTED == this.status) ;
        }

    /**
     * The status reason, explains status.
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
