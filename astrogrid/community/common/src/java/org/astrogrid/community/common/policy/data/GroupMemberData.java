/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/data/GroupMemberData.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/22 13:03:04 $</cvs:date>
 * <cvs:version>$Revision: 1.8 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: GroupMemberData.java,v $
 *   Revision 1.8  2004/11/22 13:03:04  jdt
 *   Merges from Comm_KMB_585
 *
 *   Revision 1.7  2004/10/29 15:50:05  jdt
 *   merges from Community_AdminInterface (bug 579)
 *
 *   Revision 1.6.88.1  2004/10/18 22:10:28  KevinBenson
 *   some bug fixes to the PermissionManager.  Also made it throw some exceptions.
 *   Made  it and GroupManagerImnpl use the Resolver objects to actually get a group(PermissionManageriMnpl)
 *   or account (GroupMember) from the other community.  Changed also for it to grab a ResourceData from the
 *   database to verifity it is in our database.  Add a few of these resolver dependencies as well.
 *   And last but not least fixed the GroupMemberData object to get rid of a few set methods so Castor
 *   will now work correctly in Windows
 *
 *   Revision 1.6  2004/07/14 13:50:07  dave
 *   Merged development branch, dave-dev-200406301228, into HEAD
 *
 *   Revision 1.5.12.1  2004/07/05 14:18:55  dave
 *   Tried to remove the JConfig libraries
 *
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
 * @todo Refactor this to use Ivorn identifiers.
 *
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
  
    public void setAccount(CommunityIdent value)
        {
        this.account = (null != value) ? value.toString() : null ;
        }
   */
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
            if (object instanceof GroupMemberData)
                {
                GroupMemberData that = (GroupMemberData) object ;
                //
                // If our ident is null
                if (null == this.account && null == this.group) {
                    return (null == that.getAccount() && null == that.getGroup()) ;
                }else if(this.account != null && this.group != null) {
                    return (this.account.equals(that.getAccount()) && this.group.equals(that.getGroup()) ) ;
                }else if(this.account == null && this.group != null) {
                    return (null == that.getAccount() && this.group.equals(that.getGroup())) ;
                }else if(this.account != null && this.group == null) {
                    return (null == that.getGroup() && this.account.equals(that.getAccount()) ) ;
                }else {
                   //impossible to get here return false.
                    return false;
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
     * Access to our Group ident.
     *
     
    public void setGroup(CommunityIdent value)
        {
        this.group = (null != value) ? value.toString() : null ;
        }
        */        

    }
