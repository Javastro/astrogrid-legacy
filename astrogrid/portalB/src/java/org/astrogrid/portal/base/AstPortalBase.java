/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/base/Attic/AstPortalBase.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/12 18:19:38 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: AstPortalBase.java,v $
 * Revision 1.1  2003/06/12 18:19:38  dave
 * Initial import into cvs ...
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.base ;

import org.astrogrid.portal.base.AstPortalIdent ;

/**
 * A base class for portal objects.
 *
 */
public class AstPortalBase
	{

	/**
	 * Public constructor.
	 *
	 */
	public AstPortalBase()
		{
		//
		// Not a lot ....
		//
		}

	/**
	 * Our unique identifier.
	 *
	 */
	private AstPortalIdent ident = new AstPortalIdent() ;

	/**
	 * Access to our identifier.
	 *
	 */
	public String getIdent()
		{
		return this.ident.toString() ;
		}

	}
