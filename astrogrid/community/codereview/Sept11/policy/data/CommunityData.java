/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/codereview/Sept11/policy/data/Attic/CommunityData.java,v $</cvs:source>
 * <cvs:author>$Author: KevinBenson $</cvs:author>
 * <cvs:date>$Date: 2003/09/11 10:24:21 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityData.java,v $
 *   Revision 1.1  2003/09/11 10:24:21  KevinBenson
 *   *** empty log message ***
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
package org.astrogrid.community.policy.data ;

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
   public String getAuthenticationUrl() {
         return this.authentication ;
   }

      /**
       * Access to our Community url.
       *
       */
   public void setAuthenticationUrl(String value) {
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

	}
