/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/data/GroupData.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: GroupData.java,v $
 *   Revision 1.6  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.5.24.1  2004/03/21 06:41:41  dave
 *   Refactored to include Exception handling.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.data ;

public class GroupData
    {
    /**
     * Type value to for a normal Group.
     * @todo Chnage this to public and private.
     *
     */
    public static final String MULTI_TYPE = "MULTI";

    /**
     * Type value to for an Account Group.
     * @todo Chnage this to public and private.
     *
     */
    public static final String SINGLE_TYPE = "SINGLE";

    /**
     * Public constructor.
     *
     */
    public GroupData()
        {
        this(null) ;
        }

    /**
     * Public constructor.
     * @param ident The Account ident.
     * @todo Add syntax checking.
     *
     */
    public GroupData(String ident)
        {
        this.setIdent(ident) ;
        }

    /**
     * The Group identifier.
     * At the monet, the syntax is 'name@community', this will be refactored to 'ivo:community/name'.
     * @todo Refactor to use Ivorns.
     * @todo Move this to a common data object base class
     *
     */
    private String ident ;

    /**
     * Access to the Group identifier.
     * @return The Group identifier.
     * @todo Move this to a common data object base class
     *
     */
    public String getIdent()
        {
        return this.ident ;
        }

    /**
     * Access to the Group identifier.
     * This will fail the the identifier is already set - you can't change the identifier of an existing Group.
     * @param value The Group identifier.
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
     * The Group display name.
     * This is intended for use in the portal display pages.
     *
     */
    private String display ;

    /**
     * Access to the Group display name.
     * @return The current display name.
     *
     */
    public String getDisplayName()
        {
        return this.display ;
        }

    /**
     * Access to the Group display name.
     * @param value The new display name.
     *
     */
    public void setDisplayName(String value)
        {
        this.display = value ;
        }

    /**
     * The Group description.
     * This is intended for use in the portal display pages.
     *
     */
    private String description ;

    /**
     * Access to the Group description.
     * @return The current description.
     *
     */
    public String getDescription()
        {
        return this.description ;
        }

    /**
     * Access to the Group description.
     * @param value The new description.
     *
     */
    public void setDescription(String value)
        {
        this.description = value ;
        }

    /**
     * Our Group type, MULTI_TYPE or SINGLE_TYPE.
     * Defaults to MULTI_TYPE if not set.
     * @todo Change this to public and protected, and add a simple test method.
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
     * Access to our Group type.
     *
     */
    public void setType(String value)
        {
        this.type = value ;
        }

    /*
     * Compare this with another GroupData.
     * All we want to check is the Group ident.
     * @param object The Object to compare.
     * @return true if the two object represetnt the same Group (the idents are equivalent).
     * @todo Needs to refactored to check for local community in the ident.
     * @todo Move this to a common data object base class
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
     * @todo Needs to refactored to check for local community in the ident.
     * @todo Move this to a common data object base class
     *
     */
    public synchronized int hashCode()
        {
        return (null != this.ident) ? this.ident.hashCode() : 0 ;
        }

    }
