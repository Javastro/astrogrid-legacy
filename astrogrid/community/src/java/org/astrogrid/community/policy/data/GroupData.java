/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/data/Attic/GroupData.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/11/06 15:35:26 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: GroupData.java,v $
 *   Revision 1.5  2003/11/06 15:35:26  dave
 *   Replaced tabs with spaces
 *
 *   Revision 1.4  2003/09/09 13:48:09  dave
 *   Added addGroupMember - only local accounts and groups to start with.
 *
 *   Revision 1.3  2003/09/09 10:02:17  dave
 *   Fixed problems introduced by conflicts.
 *
 *   Revision 1.2  2003/09/08 11:01:35  KevinBenson
 *   A check in of the Authentication authenticateToken roughdraft and some changes to the groudata and community data
 *   along with an AdministrationDelegate
 *
 *   Revision 1.1  2003/09/06 20:10:07  dave
 *   Split PolicyManager into separate components.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.data ;

public class GroupData
    {
    /**
     * Type value to for a normal Group.
     *
     */
    public static final String MULTI_TYPE = "MULTI";

    /**
     * Type value to for an Account Group.
     *
     */
    public static final String SINGLE_TYPE = "SINGLE";

    /**
     * Public constructor.
     *
     */
    public GroupData()
        {
        this(null, null) ;
        }

    /**
     * Public constructor.
     *
     */
    public GroupData(String ident)
        {
        this(ident, null) ;
        }

    /**
     * Public constructor.
     *
     */
    public GroupData(String ident, String description)
        {
        this.ident = ident ;
        this.description = description ;
        }

    /**
     * Our Group ident.
     *
     */
    private String ident ;

    /**
     * Access to our Group ident.
     *
     */
    public String getIdent()
        {
        return this.ident ;
        }

    /**
     * Access to our Group ident.
     *
     */
    public void setIdent(String value)
        {
        this.ident = value ;
        }

    /**
     * Our Group description.
     *
     */
    private String description ;

    /**
     * Access to our Group description.
     *
     */
    public String getDescription()
        {
        return this.description ;
        }

    /**
     * Access to our Group description.
     *
     */
    public void setDescription(String value)
        {
        this.description = value ;
        }

    /**
     * Our Group type, MULTI_TYPE or SINGLE_TYPE.
     * Defaults to MULTI_TYPE if not set.
     * Maybe should change this to an integer to make it easier to check in Java.
     *
     */
    private String type = MULTI_TYPE ;

    /**
     * Access to our Group type.
     *
     */
    public String getType()
     {
     return this.type ;
     }

    /**
     * Access to our Group description.
     *
     */
    public void setType(String value)
        {
        this.type = value ;
        }
    }
