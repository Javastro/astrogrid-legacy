/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/old-portal/src/java/org/astrorgid/portal/session/Attic/AstPortalSession.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/03 13:16:47 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: AstPortalSession.java,v $
 * Revision 1.1  2003/06/03 13:16:47  dave
 * Added initial iter 02 code
 *
 * Revision 1.6  2003/05/28 15:29:20  Dumbledore
 * Moved actions back into the page.
 *
 * Revision 1.5  2003/05/28 12:00:49  Dumbledore
 * Added action classes to session.
 *
 * Revision 1.4  2003/05/27 20:42:39  Dumbledore
 * Added actions to create and delete views.
 *
 * Revision 1.3  2003/05/27 14:25:06  Dumbledore
 * Added initial code for explorer views.
 *
 * Revision 1.2  2003/05/25 22:24:23  Dumbledore
 * Initial attempt at an XSP action - hand written Java code to start with.
 *
 * Revision 1.1  2003/05/25 11:04:19  Dumbledore
 * First import into local CVS.
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.session ;

import org.astrogrid.portal.base.AstPortalBase  ;
import org.astrogrid.portal.base.AstPortalIdent ;

import org.astrogrid.portal.user.AstPortalUser ;
import org.astrogrid.portal.query.AstPortalQuery ;
import org.astrogrid.portal.explorer.AstPortalView ;

import java.util.Map ;
import java.util.Iterator ;
import java.util.Hashtable ;

/**
 * A class to encapsulate a user session in the portal.
 *
 */
public class AstPortalSession
	extends AstPortalBase
	{
	/**
	 * The attribute key used to store an AstPortalSession in a HttpSession.
	 *
	 */
	public static final String HTTP_SESSION_TAG = "org.astrogrid.portal.session.AstPortalSession" ;

	/**
	 * Public constructor.
	 *
	 */
	public AstPortalSession()
		{
		//
		// Initialise our base class.
		super() ;
		}

////////// Action handlers
//
//

//
//
//////////

////////// User account data
//
//
	/**
	 * Our current session user account.
	 * May be null if not logged in.
	 *
	 */
	protected AstPortalUser user ;
	
	/**
	 * Access to the session user.
	 *
	 */ 
	public AstPortalUser getUser()
		{
		return this.user ;
		}
//
//
//////////

////////// Database queries
//
//
	/**
	 * Our list of queries for this session.
	 *
	 */
	protected Map queries = new Hashtable() ;
	
	/**
	 * Access to our list of current queries.
	 *
	 */
	public Iterator getCurrentQueries()
		{
		return this.queries.values().iterator() ;
		}

	/**
	 * Access to a specific query.
	 *
	 */
	public AstPortalQuery getQuery(String ident)
		{
		return (AstPortalQuery) this.queries.get(ident) ;
		}

	/**
	 * Create a new query.
	 *
	 */
	public AstPortalQuery createQuery()
		{
		AstPortalQuery query = new AstPortalQuery() ;
		this.queries.put(query.getIdent(), query) ;
		return query ;
		}
//
//
//////////

////////// Explorer views
//
//
	/**
	 * Our list of explorere views for this session.
	 *
	 */
	protected Map views = new Hashtable() ;

	/**
	 * Access to our list of current views.
	 *
	 */
	public Iterator getCurrentViews()
		{
		return this.views.values().iterator() ;
		}

	/**
	 * Access to a specific view.
	 *
	 */
	public AstPortalView getView(String ident)
		{
		return (AstPortalView) this.views.get(ident) ;
		}

	/**
	 * Create a new view.
	 *
	 */
	public AstPortalView createView(String path)
		{
		AstPortalView view = new AstPortalView(this, path) ;
		this.views.put(view.getIdent(), view) ;
		return view ;
		}

	/**
	 * Delete a specific view.
	 *
	 */
	public void deleteView(String ident)
		{
		this.views.remove(ident) ;
		}

//
//
//////////

		
	}
