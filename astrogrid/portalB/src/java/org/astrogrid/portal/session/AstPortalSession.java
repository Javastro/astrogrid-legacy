/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/session/Attic/AstPortalSession.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/24 15:36:41 $</cvs:author>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 * $Log: AstPortalSession.java,v $
 * Revision 1.3  2003/06/24 15:36:41  dave
 * Adding initial XSP pages
 *
 * Revision 1.2  2003/06/23 11:19:03  dave
 * Added service location to view pages
 *
 * Revision 1.1  2003/06/12 18:19:38  dave
 * Initial import into cvs ...
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
		if (null != ident)
			{
			return (AstPortalQuery) this.queries.get(ident) ;
			}
		else {
			return null ;
			}
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

	/**
	 * Our list of explorer views for this session.
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
		if (null != ident)
			{
			return (AstPortalView) this.views.get(ident) ;
			}
		else {
			return null ;
			}
		}

	/**
	 * Create a new view.
	 * Using the default service location.
	 *
	 */
	public AstPortalView createView(String path)
		{
		AstPortalView view = new AstPortalView(this, path) ;
		this.views.put(view.getIdent(), view) ;
		return view ;
		}

	/**
	 * Create a new view.
	 * Using a specific service location.
	 *
	 */
	public AstPortalView createView(String service, String path)
		{
		AstPortalView view = new AstPortalView(this, service, path) ;
		this.views.put(view.getIdent(), view) ;
		return view ;
		}

	/**
	 * Delete a specific view.
	 *
	 */
	public void deleteView(String ident)
		{
		if (null != ident)
			{
			this.views.remove(ident) ;
			}
		}

	}
