/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/old-portal/src/java/org/astrorgid/portal/explorer/Attic/AstPortalView.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/05 09:05:56 $</cvs:author>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 * $Log: AstPortalView.java,v $
 * Revision 1.2  2003/06/05 09:05:56  dave
 * Added JWS and RPC WebService tests.
 *
 * Revision 1.1  2003/06/03 13:16:47  dave
 * Added initial iter 02 code
 *
 * Revision 1.4  2003/05/28 15:29:20  Dumbledore
 * Moved actions back into the page.
 *
 * Revision 1.3  2003/05/28 14:03:44  Dumbledore
 * Added actions to the view.
 *
 * Revision 1.2  2003/05/28 12:00:49  Dumbledore
 * Added action classes to session.
 *
 * Revision 1.1  2003/05/27 14:25:06  Dumbledore
 * Added initial code for explorer views.
 *
 * Revision 1.1  2003/05/25 11:04:19  Dumbledore
 * First import into local CVS.
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.explorer ;

import org.astrogrid.portal.base.AstPortalBase  ;
import org.astrogrid.portal.base.AstPortalIdent ;

import org.astrogrid.portal.session.AstPortalSession ;

import java.util.Map ;
import java.util.Iterator ;
import java.util.Hashtable ;

/**
 * A class to encapsulate an explorer view in the protal.
 *
 */
public class AstPortalView
	extends AstPortalBase
	{

	/**
	 * reference to our session.
	 *
	 */
	protected AstPortalSession session ;

	/**
	 * Public constructor.
	 *
	 */
	public AstPortalView(AstPortalSession session, String path)
		{
		//
		// Initialise our base class.
		super() ;
		//
		// Initialise our session.
		this.session = session ;
		//
		// Initialise our path.
		this.path = path ;
		}

	/**
	 * Our view search path.
	 *
	 */
	protected String path ;

	/**
	 * Access to our search path.
	 *
	 */
	public String getPath()
		{
		return this.path ;
		}

	/**
	 * Access to our search path.
	 *
	 */
	public void setPath(String path)
		{
		this.path = path ;
		}


	}
