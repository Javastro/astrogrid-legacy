/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/cocoon/explorer/Attic/ExplorerAction.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/30 12:42:31 $</cvs:author>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 * $Log: ExplorerAction.java,v $
 * Revision 1.2  2003/06/30 12:42:31  dave
 * Added user name session attribute
 *
 * Revision 1.1  2003/06/26 14:15:10  dave
 * Added explorer pages and actions to Cocoon
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.cocoon.explorer ;

import org.astrogrid.portal.base.AstPortalBase  ;
import org.astrogrid.portal.base.AstPortalIdent ;

import org.astrogrid.portal.session.AstPortalSession ;

import java.io.File;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import org.xml.sax.EntityResolver;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.parameters.ParameterException ;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.ObjectModelHelper;

/**
 * A Cocoon action to handle our explorer commands.
 *
 */
public class ExplorerAction
	extends AbstractAction
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	public static boolean DEBUG_FLAG = true ;

	/**
	 * Prefix for copying items to the same location.
	 *
	 */
	public static final String COPY_PREFIX = "Copy of " ;

	/**
	 * Cocoon param for the user param in the session.
	 *
	 */
	public static final String USER_PARAM_NAME = "user-param" ;

	/**
	 * Http request param for a path value.
	 *
	 */
	public static final String EXPLORER_PATH_PARAM = "AST-PATH" ;

	/**
	 * Http request param for a name value.
	 *
	 */
	public static final String EXPLORER_NAME_PARAM = "AST-NAME" ;

	/**
	 * Http request param for a service name.
	 *
	 */
	public static final String EXPLORER_SERVICE_PARAM = "AST-SERVICE" ;

	/**
	 * Http request param for the action.
	 *
	 */
	public static final String ACTION_PARAM_TAG = "action" ;

	/**
	 * Http request param to confirm an action.
	 *
	 */
	public static final String CONFIRM_PARAM_TAG = "confirm" ;

	/**
	 * Http request action to create a new view.
	 *
	 */
	public static final String CREATE_VIEW_ACTION = "create-view" ;

	/**
	 * Http request action to delete a new view.
	 *
	 */
	public static final String DELETE_VIEW_ACTION = "delete-view" ;

	/**
	 * Http request action to change the explorer path.
	 *
	 */
	public static final String EXPLORER_PATH_ACTION = "explorer-path" ;

	/**
	 * Http request action to change the current item.
	 *
	 */
	public static final String CURRENT_PATH_ACTION = "current-path" ;

	/**
	 * Http request action to create a new view.
	 *
	 */
	public static final String CREATE_FOLDER_ACTION = "create-folder" ;

	/**
	 * Http request action to cut an item.
	 *
	 */
	public static final String CUT_ITEM_ACTION = "cut-item" ;

	/**
	 * Http request action to copy an item.
	 *
	 */
	public static final String COPY_ITEM_ACTION = "copy-item" ;

	/**
	 * Http request action to paste an item.
	 *
	 */
	public static final String PASTE_ITEM_ACTION = "paste-item" ;

	/**
	 * Http request action to rename an item.
	 *
	 */
	public static final String RENAME_ITEM_ACTION = "rename-item" ;

	/**
	 * Http request action to delete an item.
	 *
	 */
	public static final String DELETE_ITEM_ACTION = "delete-item" ;

	/**
	 * Our action method.
	 *
	 */
	public Map act(
		Redirector redirector, 
		SourceResolver resolver, 
		Map objectModel, 
		String source, 
		Parameters params)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("ExplorerAction.act()") ;

		//
		// Get our current request and session.
		Request request = ObjectModelHelper.getRequest(objectModel);
		Session session = request.getSession();
		//
		// Create a new HashMap for our results.
		Map results = new HashMap() ;

		//
		// Get our current user name from the session.
		String tag  = null ;
		String user = null ;
		try {
			tag = params.getParameter(USER_PARAM_NAME) ;
			}
		catch (ParameterException ouch)
			{
			tag = null ;
			}
		if (null != tag)
			{
			user = (String) session.getAttribute(tag) ;
			}
		if (DEBUG_FLAG) System.out.println("User tag  : " + tag) ;
		if (DEBUG_FLAG) System.out.println("User name : " + user) ;

		//
		// Our current AstSession.
		AstPortalSession portal = null ;
		//
		// Our current ExplorerView.
		ExplorerView view = null ;

		//
		// Load our AstSession from our HttpSession.
		portal = (AstPortalSession) session.getAttribute(AstPortalSession.HTTP_SESSION_TAG) ;
		//
		// If we do not have a portal session.
		if (null == portal)
			{
			//
			// Create a new AstSession.
			portal = new AstPortalSession() ;
			//
			// Add it to our HttpSession.
			session.setAttribute(AstPortalSession.HTTP_SESSION_TAG, portal) ;
			}

		//
		// If we have a portal session.
		if (null != portal)
			{
			//
			// Pass our session object to the rest of our pipeline.
			request.setAttribute(AstPortalSession.HTTP_REQUEST_TAG, portal) ;

			//
			// Get our current ExplorerView.
			view = portal.getExplorerView(request.getParameter(ExplorerView.HTTP_REQUEST_TAG)) ;

			//
			// Get the current action parameter.
			String action = request.getParameter(ACTION_PARAM_TAG) ;
			if (DEBUG_FLAG) System.out.println("Action  : " + action) ;
			//
			// Get the current confirm parameter.
			String confirm = request.getParameter(CONFIRM_PARAM_TAG) ;
			if (DEBUG_FLAG) System.out.println("Confirm : " + confirm) ;

			//
			// If the action is create-view.
			if (CREATE_VIEW_ACTION.equals(action))
				{
				//
				// If the action has been confirmed.
				if ("true".equals(confirm))
					{
					//
					// Get the request params.
					String path    = request.getParameter(EXPLORER_PATH_PARAM) ;
					String service = request.getParameter(EXPLORER_SERVICE_PARAM) ;
					if (DEBUG_FLAG) System.out.println("Creating a new view ....") ;
					if (DEBUG_FLAG) System.out.println("User    : " + user) ;
					if (DEBUG_FLAG) System.out.println("Path    : " + path) ;
					String base = "" ;
					//
					// If we have a user name.
					if (null != user)
						{
						base += "/" + user ;
						}
					//
					// If we have a path.
					if (null != path)
						{
						base += "/" + path ;
						}
					//
					// Create the view.
					if (DEBUG_FLAG) System.out.println("Base    : " + base) ;
					if (DEBUG_FLAG) System.out.println("Service : " + service) ;
					view = portal.createExplorerView(service, base) ;
					//
					// Clear the action and confirm values.
					// FIXME : Only do this is the action worked.
					action  = null ;
					confirm = null ;
					}
				}

			//
			// If we have a current view
			if (null != view)
				{
				//
				// If the action is delete-view
				if (DELETE_VIEW_ACTION.equals(action))
					{
					//
					// If the action has been confirmed.
					if ("true".equals(confirm))
						{
						if (DEBUG_FLAG) System.out.println("Deleteing current view") ;
						//
						// Delete the current view.
						portal.deleteExplorerView(view.getIdent()) ;
						//
						// Release the current view.
						view = null ;
						//
						// Pick the next available view.
						if (DEBUG_FLAG) System.out.println("Selecting next available view") ;
						Iterator iter = portal.getExplorerViews() ;
						if (iter.hasNext())
							{
							view = (ExplorerView) iter.next() ;
							}
						//
						// Clear the action and confirm values.
						// FIXME : Only do this is the action worked.
						action  = null ;
						confirm = null ;
						}
					//
					// If the action hasn't been confirmed.
					else {
						//
						// ....
						//
						}
					}

				//
				// If the action is explorer-path
				if (EXPLORER_PATH_ACTION.equals(action))
					{
					//
					// Get the path from the request params.
					String path = request.getParameter(EXPLORER_PATH_PARAM) ;
					if (DEBUG_FLAG) System.out.println("Selecting explorer path ....") ;
					if (DEBUG_FLAG) System.out.println("Path    : " + path) ;
					//
					// Change the explorer path.
					view.setExplorerPath(path) ;
					//
					// Clear the action and confirm values.
					// FIXME : Only do this is the action worked.
					action  = null ;
					confirm = null ;
					}

				//
				// If the action is current-path
				if (CURRENT_PATH_ACTION.equals(action))
					{
					//
					// Get the path from the request params.
					String path = request.getParameter(EXPLORER_PATH_PARAM) ;
					if (DEBUG_FLAG) System.out.println("Selecting current path ....") ;
					if (DEBUG_FLAG) System.out.println("Path    : " + path) ;
					//
					// Change the current path.
					view.setCurrentPath(path) ;
					//
					// Clear the selected path and action.
					view.setSelectedPath(null) ;
					view.setSelectedAction(null) ;
					//
					// Clear the action and confirm values.
					// FIXME : Only do this is the action worked.
					action  = null ;
					confirm = null ;
					}

				//
				// If the action is create-folder
				if (CREATE_FOLDER_ACTION.equals(action))
					{
					//
					// Get the path from the request params.
					String path = request.getParameter(EXPLORER_PATH_PARAM) ;
					String name = request.getParameter(EXPLORER_NAME_PARAM) ;
					if (DEBUG_FLAG) System.out.println("Create a new folder") ;
					if (DEBUG_FLAG) System.out.println("Name    : " + name) ;
					if (DEBUG_FLAG) System.out.println("Path    : " + path) ;
					//
					// If the action has been confirmed.
					if ("true".equals(confirm))
						{
						if (DEBUG_FLAG) System.out.println("Creating a new folder ....") ;
						//
						// Create the container.
						view.createContainer(name, path) ;
						//
						// Clear the action and confirm values.
						// FIXME : Only do this is the action worked.
						action  = null ;
						confirm = null ;
						}
					//
					// If the action has not been confirmed yet.
					else {
						//
						// Save the path as the current path.
						view.setCurrentPath(path) ;
						}
					}

				//
				// If the action is cut-item
				if (CUT_ITEM_ACTION.equals(action))
					{
					//
					// Get the path from the request params.
					String path = request.getParameter(EXPLORER_PATH_PARAM) ;
					if (DEBUG_FLAG) System.out.println("Cut an item") ;
					if (DEBUG_FLAG) System.out.println("Path    : " + path) ;
					//
					// Change the current path.
					view.setCurrentPath(path) ;
					//
					// Change the selected path.
					view.setSelectedPath(path) ;
					//
					// Change the selected action.
					view.setSelectedAction(CUT_ITEM_ACTION) ;
					//
					// Clear the action and confirm values.
					// FIXME : Only do this is the action worked.
					action  = null ;
					confirm = null ;
					}

				//
				// If the action is copy-item
				if (COPY_ITEM_ACTION.equals(action))
					{
					//
					// Get the path from the request params.
					String path = request.getParameter(EXPLORER_PATH_PARAM) ;
					if (DEBUG_FLAG) System.out.println("Copy an item") ;
					if (DEBUG_FLAG) System.out.println("Path    : " + path) ;
					//
					// Change the current path.
					view.setCurrentPath(path) ;
					//
					// Change the selected path.
					view.setSelectedPath(path) ;
					//
					// Change the selected action.
					view.setSelectedAction(COPY_ITEM_ACTION) ;
					//
					// Clear the action and confirm values.
					// FIXME : Only do this is the action worked.
					action  = null ;
					confirm = null ;
					}

				//
				// If the action is paste-item
				if (PASTE_ITEM_ACTION.equals(action))
					{
					//
					// Get the path from the request params.
					String path = request.getParameter(EXPLORER_PATH_PARAM) ;
					if (DEBUG_FLAG) System.out.println("Paste an item") ;
					if (DEBUG_FLAG) System.out.println("Path    : " + path) ;
					//
					// If we have a destination path.
					if (null != path)
						{
						//
						// If we have a selected item.
						if (null != view.getSelectedPath())
							{
							//
							// Split the selected path into name and path.
							String from = view.getSelectedPath() ;
							File file = new File(from) ;
							String name = file.getName() ;
							String base = file.getParent() ;
							//
							// If the selected action is cut-item.
							if (CUT_ITEM_ACTION.equals(view.getSelectedAction()))
								{
								//
								// If we are moving to the same location.
								if (path.equals(base))
									{
									if (DEBUG_FLAG) System.out.println("ACTION : move item") ;
									if (DEBUG_FLAG) System.out.println("ERROR  : source and destination are the same") ;
									}
								//
								// If we are moving to a different location.
								else {
									//
									// Create the full destination.
									String dest = path + "/" + name ;
									//
									// Move the item to the new location.
									view.moveItem(from, dest) ;
									//
									// Change the current path.
									view.setCurrentPath(dest) ;
									//
									// Clear the selected path and action.
									view.setSelectedPath(null) ;
									view.setSelectedAction(null) ;
									}
								}
							//
							// If the selected action is copy-item.
							if (COPY_ITEM_ACTION.equals(view.getSelectedAction()))
								{
								//
								// If we are copying to the same location.
								if (path.equals(base))
									{
									//
									// Add a prefix to the name.
									name = COPY_PREFIX + name ;
									}
								//
								// Create the full destination.
								String dest = path + "/" + name ;
								//
								// Copy the item to the new location.
								view.copyItem(from, dest) ;
								//
								// Change the current path.
								view.setCurrentPath(dest) ;
								//
								// Clear the selected path and action.
								view.setSelectedPath(null) ;
								view.setSelectedAction(null) ;
								}
							}
						}
					//
					// Clear the action and confirm values.
					// FIXME : Only do this is the action worked.
					action  = null ;
					confirm = null ;
					}

				//
				// If the action is rename-item
				if (RENAME_ITEM_ACTION.equals(action))
					{
					//
					// Get the path from the request params.
					String from = request.getParameter(EXPLORER_PATH_PARAM) ;
					String name = request.getParameter(EXPLORER_NAME_PARAM) ;
					if (DEBUG_FLAG) System.out.println("Rename an item") ;
					if (DEBUG_FLAG) System.out.println("From    : " + from) ;
					if (DEBUG_FLAG) System.out.println("Name    : " + name) ;
					//
					// If we have a path.
					if (null != from)
						{
						//
						// If the action has been confirmed.
						if ("true".equals(confirm))
							{
							//
							// Get the base folder.
							File file = new File(from) ;
							String base = file.getParent() ;
							//
							// Create the destination path.
							String dest = base + "/" + name ;
							//
							// If the source and destination are the same.
							if (from.equals(base))
								{
								if (DEBUG_FLAG) System.out.println("ACTION : move item") ;
								if (DEBUG_FLAG) System.out.println("ERROR  : source and destination are the same") ;
								}
							//
							// If the source and destination are different.
							else {
								//
								// Move the item to the new location.
								view.moveItem(from, dest) ;
								}
							//
							// Change the current path.
							view.setCurrentPath(dest) ;
							//
							// Clear the selected path and action.
							// FIXME : Only do this is the action worked.
							view.setSelectedPath(null) ;
							view.setSelectedAction(null) ;
							//
							// Clear the action and confirm values.
							// FIXME : Only do this is the action worked.
							action  = null ;
							confirm = null ;
							}
						//
						// If the action has not been confirmed.
						else {
							//
							// Change the current path.
							view.setCurrentPath(from) ;
							//
							// Change the selected path and action.
							view.setSelectedPath(from) ;
							view.setSelectedAction(RENAME_ITEM_ACTION) ;
							//
							// Set the action and confirm values.
							action  = RENAME_ITEM_ACTION ;
							confirm = "false" ;
							}
						}
					}

				//
				// If the action is delete-item
				if (DELETE_ITEM_ACTION.equals(action))
					{
					//
					// Get the path from the request params.
					String path = request.getParameter(EXPLORER_PATH_PARAM) ;
					if (DEBUG_FLAG) System.out.println("Delete an item") ;
					if (DEBUG_FLAG) System.out.println("Path    : " + path) ;
					//
					// If we have a path.
					if (null != path)
						{
						//
						// If the action has been confirmed.
						if ("true".equals(confirm))
							{
							//
							// Delete the item.
							view.deleteItem(path) ;
							//
							// Clear the current path.
							view.setCurrentPath(null) ;
							//
							// Clear the selected path and action.
							// FIXME : Only do this is the action worked.
							view.setSelectedPath(null) ;
							view.setSelectedAction(null) ;
							//
							// Clear the action and confirm values.
							// FIXME : Only do this is the action worked.
							action  = null ;
							confirm = null ;
							}
						//
						// If the action has not been confirmed.
						else {
							//
							// Change the current path.
							view.setCurrentPath(path) ;
							//
							// Change the selected path and action.
							view.setSelectedPath(path) ;
							view.setSelectedAction(DELETE_ITEM_ACTION) ;
							//
							// Set the action and confirm values.
							action  = DELETE_ITEM_ACTION ;
							confirm = "false" ;
							}
						}
					}
				}

			//
			// If we have an explorer view.
			if (null != view)
				{
				if (DEBUG_FLAG) System.out.println("View : " + view.getIdent()) ;
				//
				// Pass our view to the rest of our pipeline.
				request.setAttribute(ExplorerView.HTTP_REQUEST_TAG, view) ;
				}

			//
			// Pass our action flag to the rest of our pipeline.
			results.put(ACTION_PARAM_TAG, ((null != action) ? action : "none")) ;
			//
			// Pass our confirm flag to the rest of our pipeline.
			results.put(CONFIRM_PARAM_TAG, ((null != confirm) ? confirm : "false")) ;

			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		return results ;
		}
	}