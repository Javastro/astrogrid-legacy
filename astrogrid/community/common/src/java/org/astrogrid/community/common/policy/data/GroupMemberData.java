/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/data/GroupMemberData.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: GroupMemberData.java,v $
 *   Revision 1.5  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.4.62.1  2004/06/17 13:38:58  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.data ;

/**
 * A Castor data object to represent a member of a Group.
 * This should only be used server side, not client side.
 *
 */
public class GroupMemberData
    {
    /**
     * Public constructor.
     *
     */
    public GroupMemberData()
        {
        this(null, null) ;
        }

    /**
     * Public constructor.
     *
     */
    public GroupMemberData(String account, String group)
        {
        this.group   = group   ;
        this.account = account ;
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
     * Access to our Account ident.
     *
     */
    public void setAccount(CommunityIdent value)
        {
        this.account = (null != value) ? value.toString() : null ;
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
     * Access to our Group ident.
     *
     */
    public void setGroup(CommunityIdent value)
        {
        this.group = (null != value) ? value.toString() : null ;
        }

    }
