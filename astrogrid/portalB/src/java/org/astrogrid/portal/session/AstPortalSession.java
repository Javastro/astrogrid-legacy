/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/session/Attic/AstPortalSession.java,v $</cvs:source>
 * <cvs:date>$Author: KevinBenson $</cvs:date>
 * <cvs:author>$Date: 2003/07/28 08:06:04 $</cvs:author>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 * $Log: AstPortalSession.java,v $
 * Revision 1.5  2003/07/28 08:06:04  KevinBenson
 * Got rid of references to AstPortalQuery for now since it is not used in the query.
 *
 * Revision 1.4  2003/06/26 14:15:10  dave
 * Added explorer pages and actions to Cocoon
 *
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
 * This may be replaced soon by a Cocoon friendly version.
 * Need to keep this here for now as other components may be using it.
 *
 *
 */
package org.astrogrid.portal.session ;

import org.astrogrid.portal.base.AstPortalBase  ;
import org.astrogrid.portal.base.AstPortalIdent ;

import org.astrogrid.portal.user.AstPortalUser ;
import org.astrogrid.portal.explorer.AstPortalView ;

import org.astrogrid.portal.cocoon.explorer.ExplorerView ;

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
	 * The key used to store an AstPortalSession in a HttpSession.
	 *
	 */
	public static final String HTTP_SESSION_TAG = "org.astrogrid.portal.session.AstPortalSession" ;

	/**
	 * The key used to store an AstPortalSession in a HttpRequest.
	 *
	 */
	public static final String HTTP_REQUEST_TAG = "org.astrogrid.portal.session.AstPortalSession" ;

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

//
// In the Cocoon pages, AstView has been replaced by ExplorerView.
// I've left the AstView methods here because the JSP pages still use them.
//
	/**
	 * Our list of explorer views for this session.
	 * @deprecated
	 *
	 */
	protected Map views = new Hashtable() ;

	/**
	 * Access to our list of current views.
	 * @deprecated
	 *
	 */
	public Iterator getCurrentViews()
		{
		return this.views.values().iterator() ;
		}

	/**
	 * Access to a specific view.
	 * @deprecated
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
	 * @deprecated
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
	 * @deprecated
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
	 * @deprecated
	 *
	 */
	public void deleteView(String ident)
		{
		if (null != ident)
			{
			this.views.remove(ident) ;
			}
		}

//
// In the Cocoon pages, AstView has been replaced by ExplorerView.
//
	/**
	 * Our list of explorer views for this session.
	 *
	 */
	protected Map explorerViews = new Hashtable() ;

	/**
	 * Access to our list of explorer views.
	 *
	 */
	public Iterator getExplorerViews()
		{
		return this.explorerViews.values().iterator() ;
		}

	/**
	 * Access to a specific view.
	 *
	 */
	public ExplorerView getExplorerView(String ident)
		{
		if (null != ident)
			{
			return (ExplorerView) this.explorerViews.get(ident) ;
			}
		else {
			return null ;
			}
		}

	/**
	 * Create a new explorer view, using the default service location.
	 *
	 */
	public ExplorerView createExplorerView(String path)
		{
		ExplorerView view = new ExplorerView(this, path) ;
		this.explorerViews.put(view.getIdent(), view) ;
		return view ;
		}

	/**
	 * Create a new explorer view, using a specific service location.
	 *
	 */
	public ExplorerView createExplorerView(String service, String path)
		{
		ExplorerView view = new ExplorerView(this, service, path) ;
		this.explorerViews.put(view.getIdent(), view) ;
		return view ;
		}

	/**
	 * Delete a specific view.
	 *
	 */
	public void deleteExplorerView(String ident)
		{
		if (null != ident)
			{
			this.explorerViews.remove(ident) ;
			}
		}

	}
