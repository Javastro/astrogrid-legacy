/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/data/CommunityData.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/20 21:11:05 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityData.java,v $
 *   Revision 1.5  2004/02/20 21:11:05  dave
 *   Merged development branch, dave-dev-200402120832, into HEAD
 *
 *   Revision 1.4.2.1  2004/02/16 15:20:54  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.4  2004/02/12 08:12:13  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.2.4.1  2004/01/30 14:55:46  dave
 *   Added PasswordData object
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
 *   Revision 1.4  2003/11/06 15:35:26  dave
 *   Replaced tabs with spaces
 *
 *   Revision 1.3  2003/09/08 20:28:50  dave
 *   Added CommunityIdent, with isLocal() and isValid()
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

public class CommunityData
    {
    /**
     * Public constructor.
     *
     */
    public CommunityData()
        {
        this.ident = null ;
        this.service = null ;
        this.manager = null ;
        this.description = null ;
        }

    /**
     * Public constructor.
     *
     */
    public CommunityData(String ident)
        {
        this(ident, null) ;
        }

    /**
     * Public constructor.
     *
     */
    public CommunityData(String ident, String description)
        {
        this.ident = ident ;
        this.service = null ;
        this.manager = null ;
        this.description = description ;
        }

    /**
     * Our Community ident.
     *
     */
    private String ident ;

    /**
     * Access to our Community ident.
     *
     */
    public String getIdent()
        {
        return this.ident ;
        }

    /**
     * Access to our Community ident.
     *
     */
    public void setIdent(String value)
        {
        this.ident = value ;
        }

    /**
     * Our service url.
     *
     */
    private String service ;

    /**
     * Access to our service url.
     *
     */
    public String getServiceUrl()
        {
        return this.service ;
        }

    /**
     * Access to our Community url.
     *
     */
    public void setServiceUrl(String value)
        {
        this.service = value ;
        }

    /**
     * Our manager url.
     *
     */
    private String manager ;

    /**
     * Access to our manager url.
     *
     */
    public String getManagerUrl()
        {
        return this.manager ;
        }

    /**
     * Access to our Community url.
     *
     */
    public void setManagerUrl(String value)
        {
        this.manager = value ;
        }
      
      /**
       * Our manager url.
       *
       */
   private String authentication ;

    /**
     * Access to our manager url.
     *
     */
    public String getAuthenticationUrl()
        {
        return this.authentication ;
        }

    /**
     * Access to our Community url.
     *
     */
    public void setAuthenticationUrl(String value)
        {
        this.authentication = value ;
        }

    /**
     * Our Community description.
     *
     */
    private String description ;

    /**
     * Access to our Community description.
     *
     */
    public String getDescription()
        {
        return this.description ;
        }

    /**
     * Access to our Community description.
     *
     */
    public void setDescription(String value)
        {
        this.description = value ;
        }

    /*
     * Compare this with another CommunityData.
     * All we want to check is the community ident.
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
            // If the object is an CommunityData
            if (object instanceof CommunityData)
                {
                CommunityData that = (CommunityData) object ;
                //
                // If our ident is null
                if (null == this.ident)
                    {
                    //
                    // Check that ident is null.
                    return (null == that.getIdent()) ;
                    }
                //
                // If our ident is not null.
                else {
                    //
                    // Check that ident is the same.
                    return (this.ident.equals(that.getIdent())) ;
                    }
                }
            //
            // If that is not an CommunityData
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
