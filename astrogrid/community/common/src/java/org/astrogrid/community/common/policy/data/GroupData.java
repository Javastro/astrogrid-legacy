/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/data/GroupData.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/12 06:56:45 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: GroupData.java,v $
 *   Revision 1.3  2004/02/12 06:56:45  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.2.4.3  2004/01/27 19:09:52  dave
 *   Fixed dumb typo ...
 *
 *   Revision 1.2.4.2  2004/01/27 19:07:28  dave
 *   Fixed PMD report violations
 *   1) Removed unused imports.
 *   2) Added override for hashCode() to match custom equals().
 *
 *   Revision 1.2.4.1  2004/01/27 05:48:02  dave
 *   Added custom equals() to GroupData
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
package org.astrogrid.community.common.policy.data ;

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

	/*
	 * Compare this with another GroupData.
	 * All we want to check is the Group ident.
	 * TODO This needs to refactored to check for local community in the ident.
	 *
	 */
	public synchronized boolean equals(Object that)
		{
		//
		// If that is null.
		if (null == that)
			{
			return false ;
			}
		//
		// If that is not null.
		else {
			//
			// If that is an GroupData
			if (that instanceof GroupData)
				{
				GroupData group = (GroupData) that ;
				//
				// If our ident is null
				if (null == this.getIdent())
					{
					//
					// Check that ident is null.
					return (null == group.getIdent()) ;
					}
				//
				// If our ident is not null.
				else {
					//
					// Check that ident is the same.
					return (this.getIdent().equals(group.getIdent())) ;
					}
				}
			//
			// If that is not an GroupData
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
