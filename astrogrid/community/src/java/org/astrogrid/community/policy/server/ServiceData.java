/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/Attic/ServiceData.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/08/28 17:33:56 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: ServiceData.java,v $
 *   Revision 1.1  2003/08/28 17:33:56  dave
 *   Initial policy prototype
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.server ;

public class ServiceData
	{
	/**
	 * Our service ident.
	 *
	 */
	private String ident ;

	/**
	 * Access to our service ident.
	 *
	 */
	public String getIdent()
		{
		return this.ident ;
		}

	/**
	 * Access to our service ident.
	 *
	 */
	public void setIdent(String ident)
		{
		this.ident = ident ;
		}

	}
