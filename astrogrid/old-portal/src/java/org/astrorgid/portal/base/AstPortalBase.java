/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/old-portal/src/java/org/astrorgid/portal/base/Attic/AstPortalBase.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/05 09:05:56 $</cvs:author>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 * $Log: AstPortalBase.java,v $
 * Revision 1.2  2003/06/05 09:05:56  dave
 * Added JWS and RPC WebService tests.
 *
 * Revision 1.1  2003/06/03 13:16:47  dave
 * Added initial iter 02 code
 *
 * Revision 1.2  2003/05/27 14:25:05  Dumbledore
 * Added initial code for explorer views.
 *
 * Revision 1.1  2003/05/25 11:04:19  Dumbledore
 * First import into local CVS.
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

//////// Unique identifier
//
//
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
//
//
////////

//////// XML generator stuff ...
//
//


//
//
////////

	}
