/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/data/CommunityData.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityData.java,v $
 *   Revision 1.7  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.6.38.1  2004/06/17 13:38:58  dave
 *   Tidied up old CVS log entries
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
     *
     */
    public synchronized int hashCode()
        {
        return (null != this.ident) ? this.ident.hashCode() : 0 ;
        }

    }
