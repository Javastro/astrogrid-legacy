/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/data/Attic/ServiceData.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/17 19:47:21 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: ServiceData.java,v $
 *   Revision 1.4  2003/09/17 19:47:21  dave
 *   1) Fixed classnotfound problems in the build.
 *   2) Added the JUnit task to add the initial accounts and groups.
 *   3) Got the build to work together with the portal.
 *   4) Fixed some bugs in the Account handling.
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
package org.astrogrid.community.policy.data ;

public class ServiceData
	{
	/**
	 * Our config file location.
	 *
	 */
	private String config ;

	/**
	 * Access to our config file location.
	 *
	 */
	public String getConfigPath()
		{
		return this.config ;
		}

	/**
	 * Access to our config file location.
	 *
	 */
	public void setConfigPath(String value)
		{
		this.config = value ;
		}

	/**
	 * Our community name.
	 *
	 */
	private String community ;

	/**
	 * Access to our community name.
	 *
	 */
	public String getCommunityName()
		{
		return this.community ;
		}

	/**
	 * Access to our community name.
	 *
	 */
	public void setCommunityName(String value)
		{
		this.community = value ;
		}

	/**
	 * Our service URL.
	 *
	 */
	private String service ;

	/**
	 * Access to our service URL.
	 *
	 */
	public String getServiceUrl()
		{
		return this.service ;
		}

	/**
	 * Access to our service URL.
	 *
	 */
	public void setServiceUrl(String value)
		{
		this.service = value ;
		}

	/**
	 * Our manager URL.
	 *
	 */
	private String manager ;

	/**
	 * Access to our manager URL.
	 *
	 */
	public String getManagerUrl()
		{
		return this.manager ;
		}

	/**
	 * Access to our manager URL.
	 *
	 */
	public void setManagerUrl(String value)
		{
		this.manager = value ;
		}
	}
