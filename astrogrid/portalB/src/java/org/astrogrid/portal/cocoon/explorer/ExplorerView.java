/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/cocoon/explorer/Attic/ExplorerView.java,v $</cvs:source>
 * <cvs:date>$Author: KevinBenson $</cvs:date>
 * <cvs:author>$Date: 2003/07/10 14:22:38 $</cvs:author>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 * $Log: ExplorerView.java,v $
 * Revision 1.3  2003/07/10 14:22:38  KevinBenson
 * I might have to get rid of these WS_FTP.log files not sure how they got their.  Anyays just fixing some compiling mistakes.
 *
 * Revision 1.2  2003/06/29 02:45:22  dave
 * Fixed display styles in explorer and add VOTable transform
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

import java.net.URLEncoder ;

import java.io.File ;
import java.io.Reader ;
import java.io.StringReader ;
import java.io.IOException ;
import java.io.UnsupportedEncodingException ;

import java.util.Map ;
import java.util.Iterator ;
import java.util.Hashtable ;
import java.util.Collection ;

import java.rmi.RemoteException ;
import javax.xml.rpc.ServiceException ;

import org.xml.sax.SAXException ;
import org.xml.sax.SAXParseException ;

import org.astrogrid.portal.util.xml.sax.SAXElementHandler  ;
import org.astrogrid.portal.util.xml.sax.SAXDocumentHandler ;
import org.astrogrid.portal.util.xml.sax.SAXAttributeHandler ;
import org.astrogrid.portal.util.xml.sax.SAXCharacterHandler ;

import org.astrogrid.portal.services.myspace.client.actions.lookup.LookupRequestBuilder ;
import org.astrogrid.portal.services.myspace.client.actions.lookup.LookupResponseParser ;

import org.astrogrid.portal.services.myspace.client.actions.details.DetailsRequestBuilder ;
import org.astrogrid.portal.services.myspace.client.actions.details.DetailsResponseParser ;

import org.astrogrid.portal.services.myspace.client.actions.copy.CopyRequestBuilder ;
import org.astrogrid.portal.services.myspace.client.actions.copy.CopyResponseParser ;

import org.astrogrid.portal.services.myspace.client.actions.move.MoveRequestBuilder ;
import org.astrogrid.portal.services.myspace.client.actions.move.MoveResponseParser ;

import org.astrogrid.portal.services.myspace.client.actions.delete.DeleteRequestBuilder ;
import org.astrogrid.portal.services.myspace.client.actions.delete.DeleteResponseParser ;

import org.astrogrid.portal.services.myspace.client.actions.create.CreateRequestBuilder ;
import org.astrogrid.portal.services.myspace.client.actions.create.CreateResponseParser ;

import org.astrogrid.portal.services.myspace.client.actions.export.ExportRequestBuilder ;
import org.astrogrid.portal.services.myspace.client.actions.export.ExportResponseParser ;

import org.astrogrid.portal.services.myspace.client.data.DataNode ;
import org.astrogrid.portal.services.myspace.client.status.StatusNode ;

//
// Import the WSDL generated client stubs.
//import org.astrogrid.portal.services.myspace.client.MySpaceManager ;
//import org.astrogrid.portal.services.myspace.client.MySpaceManagerService ;
//import org.astrogrid.portal.services.myspace.client.MySpaceManagerServiceLocator ;

import org.astrogrid.portal.generated.myspace.client.MySpaceManager ;
import org.astrogrid.portal.generated.myspace.client.MySpaceManagerService ;
import org.astrogrid.portal.generated.myspace.client.MySpaceManagerServiceLocator ;



/**
 * A class to encapsulate an explorer view in the Cocoon protal.
 *
 */
public class ExplorerView
	extends AstPortalBase
	{

	/**
	 * Switch for our debug statements.
	 *
	 */
	public static boolean DEBUG_FLAG = true ;

	/**
	 * Our request parameter tag.
	 *
	 */
	public static final String HTTP_REQUEST_TAG = "AST-VIEW" ;

	/**
	 * Reference to our session.
	 *
	 */
	protected AstPortalSession session ;

	/**
	 * Public constructor.
	 *
	 */
	public ExplorerView(AstPortalSession session, String path)
		{
		//
		// Use next constructor.
		this(session, null, path) ;
		}

	/**
	 * Public constructor.
	 *
	 */
	public ExplorerView(AstPortalSession session, String service, String path)
		{
		//
		// Initialise our base class.
		super() ;
		//
		// Initialise our session.
		this.session = session ;
		//
		// Initialise our service location.
		setMySpaceLocation(service) ;
		//
		// Initialise our path.
		setExplorerPath(path) ;
		}

	/**
	 * Encode a string for use in a URL.
	 *
	 */
	public String encode(String string)
		{
		try {
			string = URLEncoder.encode(string, "UTF-8") ;
			}
		catch (UnsupportedEncodingException ouch)
			{
			//
			// FIXME
			if (DEBUG_FLAG) System.out.println("Exception encoding a string") ;
			if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
			}
		return string ;
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
	public String getExplorerPath()
		{
		return (null != path) ? this.path : "" ;
		}

	/**
	 * Access to our search path, URL encoded.
	 *
	 */
	public String getExplorerPathEncoded()
		{
		return encode(this.getExplorerPath()) ;
		}

	/**
	 * Access to our search path.
	 *
	 */
	public void setExplorerPath(String path)
		{
		this.path = path ;
		}

	/**
	 * Our current item path.
	 *
	 */
	protected String current ;

	/**
	 * Access to our current item.
	 *
	 */
	public String getCurrentPath()
		{
		return (null != this.current) ? this.current : "" ;
		}

	/**
	 * Access to our current name.
	 *
	 */
	public String getCurrentName()
		{
		//
		// FIXME
		if (null != this.current)
			{
			File file = new File(this.current) ;
			return file.getName() ;
			}
		else {
			return "" ;
			}
		}

	/**
	 * Access to our current item, URL encoded.
	 *
	 */
	public String getCurrentPathEncoded()
		{
		return encode(this.getCurrentPath()) ;
		}

	/**
	 * Access to our current item.
	 *
	 */
	public void setCurrentPath(String path)
		{
		this.current = path ;
		}

	/**
	 * Our selected item path.
	 *
	 */
	protected String selected ;

	/**
	 * Access to our selected item.
	 *
	 */
	public String getSelectedPath()
		{
		return (null != this.selected) ? this.selected : "" ;
		}

	/**
	 * Access to our selected name.
	 *
	 */
	public String getSelectedName()
		{
		//
		// FIXME
		if (null != this.selected)
			{
			File file = new File(this.selected) ;
			return file.getName() ;
			}
		else {
			return "" ;
			}
		}

	/**
	 * Access to our selected item, URL encoded.
	 *
	 */
	public String getSelectedPathEncoded()
		{
		return encode(this.getSelectedPath()) ;
		}

	/**
	 * Access to our selected item.
	 *
	 */
	public void setSelectedPath(String path)
		{
		this.selected = path ;
		}

	/**
	 * Our selected action name.
	 *
	 */
	protected String action ;

	/**
	 * Access to our selected action.
	 *
	 */
	public String getSelectedAction()
		{
		return this.action ;
		}

	/**
	 * Access to our selected action.
	 *
	 */
	public void setSelectedAction(String action)
		{
		this.action = action ;
		}

	/**
	 * Our MySpace service location.
	 *
	 */
	protected String myspaceLocation ;

	/**
	 * Access to our MySpace service location.
	 *
	 */
	public String getMySpaceLocation()
		{
		return this.myspaceLocation ;
		}

	/**
	 * Access to our MySpace service location.
	 *
	 */
	public void setMySpaceLocation(String location)
		{
		//
		// If the location is null, use the default address.
		if (null == location)
			{
			this.myspaceLocation = myspaceLocator.getMySpaceManagerAddress() ;
			}
		//
		// If the location is not null.
		if (null != location)
			{
			//
			// If the location is not blank.
			if (location.trim().length() > 0)
				{
				this.myspaceLocation = location ;
				}
			//
			// If the location is blank, use the default address
			else {
				this.myspaceLocation = myspaceLocator.getMySpaceManagerAddress() ;
				}
			}
		//
		// Reset our service instance.
		this.initMySpaceService(true) ;
		}

	/**
	 * Connection to our MySpace service locator.
	 *
	 */
	protected MySpaceManagerService myspaceLocator = new MySpaceManagerServiceLocator() ;

	/**
	 * Connection to our MySpace service.
	 *
	 */
	protected MySpaceManager myspaceService ;

	/**
	 * Initialise our MySpace service.
	 * Calls the next methods with reset = false.
	 *
	 */
	public boolean initMySpaceService()
		{
		return this.initMySpaceService(false) ;
		}

	/**
	 * Initialise our MySpace service.
	 * Resets to the default service if given a blank string or null.
	 * 
	 */
	public boolean initMySpaceService(boolean reset)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AstPortalView.initMySpaceService()") ;
		if (DEBUG_FLAG) System.out.println("Service : " + myspaceLocation) ;

		//
		// If we need to reset the service.
		if (reset)
			{
			myspaceService = null ;
			}

		//
		// If we don't have a service instance.
		if (null == myspaceService)
			{
			try {
				//
				// If we have a service location.
				if (null != myspaceLocation)
					{
					//
					// Use the specific location.
					myspaceService = myspaceLocator.getMySpaceManager(new java.net.URL(myspaceLocation)) ;
					}
				//
				// If we don't have a specific location.
				else {
					//
					// Use the default location.
					myspaceService = myspaceLocator.getMySpaceManager() ;
					}
				}
			catch (ServiceException ouch)
				{
				//
				// FIXME ....
				//
				if (DEBUG_FLAG) System.out.println("Exception initialising WebService") ;
				if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
				}
			catch (java.net.MalformedURLException ouch)
				{
				//
				// FIXME ....
				//
				if (DEBUG_FLAG) System.out.println("Exception initialising WebService") ;
				if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
				}
			}
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		return (null != myspaceService)  ;
		}

	/**
	 * Request a tree of DataNodes matching our path.
	 *
	 */
	public DataNode getTree()
		{
		DataNode   tree   = null ;
		StatusNode status = null ;
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AstPortalView.getTree()") ;
		try {
			//
			// Create our service request
			LookupRequestBuilder request = new LookupRequestBuilder(this.getExplorerPath() + "*") ;
			if (DEBUG_FLAG)
				{
				System.out.println("----") ;
				System.out.println(request) ;
				System.out.println("----") ;
				}
			//
			// Create our service instance.
			initMySpaceService() ;
			//
			// Call our service method.
			String response = myspaceService.lookupDataHoldersDetails(request.toString()) ;
			if (DEBUG_FLAG)
				{
				System.out.println("----") ;
				System.out.println(response) ;
				System.out.println("----") ;
				}
			//
			// Create a parser.
			LookupResponseParser parser = new LookupResponseParser() ;
			//
			// Parse the response.
			parser.parse(response) ;
			//
			// Get the result status.
			status = parser.getStatus() ;
			//
			// Get the result tree.
			tree = parser.getTree() ;
			}
		catch (RemoteException ouch)
			{
			//
			// FIXME ....
			if (DEBUG_FLAG) System.out.println("Exception calling WebService") ;
			if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
			}
		catch (IOException ouch)
			{
			//
			// FIXME ....
			if (DEBUG_FLAG) System.out.println("IOException in response parser") ;
			if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
			}
		catch (SAXException ouch)
			{
			//
			// FIXME ....
			if (DEBUG_FLAG) System.out.println("SAXException in response parser") ;
			if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
			}
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		//
		// Return our tree of nodes.
		return tree ;
		}

	/**
	 * Create a new container.
	 *
	 */
	public StatusNode createContainer(String name, String path)
		{
		DataNode   data   = null ;
		StatusNode status = null ;
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AstPortalView.createContainer()") ;
		try {
			//
			// Create our service request
			CreateRequestBuilder request = new CreateRequestBuilder(name, path) ;
			if (DEBUG_FLAG)
				{
				System.out.println("----") ;
				System.out.println(request) ;
				System.out.println("----") ;
				}
			//
			// Create our service instance.
			initMySpaceService() ;
			//
			// Call our service method.
			String response = myspaceService.createContainer(request.toString()) ;
			if (DEBUG_FLAG)
				{
				System.out.println("----") ;
				System.out.println(response) ;
				System.out.println("----") ;
				}
			//
			// Create a parser.
			CreateResponseParser parser = new CreateResponseParser() ;
			//
			// Parse the response.
			parser.parse(response) ;
			//
			// Get the result status.
			status = parser.getStatus() ;
			//
			// Get the result data.
			data = parser.getData() ;
			}
		catch (RemoteException ouch)
			{
			//
			// FIXME ....
			if (DEBUG_FLAG) System.out.println("Exception calling WebService") ;
			if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
			}
		catch (IOException ouch)
			{
			//
			// FIXME ....
			if (DEBUG_FLAG) System.out.println("IOException in response parser") ;
			if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
			}
		catch (SAXException ouch)
			{
			//
			// FIXME ....
			if (DEBUG_FLAG) System.out.println("SAXException in response parser") ;
			if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
			}
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		//
		// Return our status node.
		return status ;
		}

	/**
	 * Copy a data item.
	 *
	 */
	public StatusNode copyItem(String from, String dest)
		{
		DataNode   data   = null ;
		StatusNode status = null ;
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("ExplorerView.copyItem()") ;
		try {
//
// FIXME : Could be nicer ... call the MySpace service to check for an existing item.
// Problem is that doing a lookup for something that isn't there breaks the MySpace service.
//
			//
			// Create our service request
			CopyRequestBuilder request = new CopyRequestBuilder(from, dest) ;
			if (DEBUG_FLAG)
				{
				System.out.println("----") ;
				System.out.println(request) ;
				System.out.println("----") ;
				}
			//
			// Create our service instance.
			initMySpaceService() ;
			//
			// Call our service method.
			String response = myspaceService.copyDataHolder(request.toString()) ;
			if (DEBUG_FLAG)
				{
				System.out.println("----") ;
				System.out.println(response) ;
				System.out.println("----") ;
				}
			//
			// Create a parser.
			CopyResponseParser parser = new CopyResponseParser() ;
			//
			// Parse the response.
			parser.parse(response) ;
			//
			// Get the result status.
			status = parser.getStatus() ;
			//
			// Get the result data.
			data = parser.getData() ;
			}
		catch (RemoteException ouch)
			{
			//
			// FIXME ....
			if (DEBUG_FLAG) System.out.println("Exception calling WebService") ;
			if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
			}
		catch (IOException ouch)
			{
			//
			// FIXME ....
			if (DEBUG_FLAG) System.out.println("IOException in response parser") ;
			if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
			}
		catch (SAXException ouch)
			{
			//
			// FIXME ....
			if (DEBUG_FLAG) System.out.println("SAXException in response parser") ;
			if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
			}
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		//
		// Return our status node.
		return status ;
		}

	/**
	 * Move a data item.
	 *
	 */
	public StatusNode moveItem(String from, String dest)
		{
		DataNode   data   = null ;
		StatusNode status = null ;
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("ExplorerView.moveItem()") ;
		try {
			//
			// Create our service request
			MoveRequestBuilder request = new MoveRequestBuilder(from, dest) ;
			if (DEBUG_FLAG)
				{
				System.out.println("----") ;
				System.out.println(request) ;
				System.out.println("----") ;
				}
			//
			// Create our service instance.
			initMySpaceService() ;
			//
			// Call our service method.
			String response = myspaceService.moveDataHolder(request.toString()) ;
			if (DEBUG_FLAG)
				{
				System.out.println("----") ;
				System.out.println(response) ;
				System.out.println("----") ;
				}
			//
			// Create a parser.
			MoveResponseParser parser = new MoveResponseParser() ;
			//
			// Parse the response.
			parser.parse(response) ;
			//
			// Get the result status.
			status = parser.getStatus() ;
			//
			// Get the result data.
			data = parser.getData() ;
			}
		catch (RemoteException ouch)
			{
			//
			// FIXME ....
			if (DEBUG_FLAG) System.out.println("Exception calling WebService") ;
			if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
			}
		catch (IOException ouch)
			{
			//
			// FIXME ....
			if (DEBUG_FLAG) System.out.println("IOException in response parser") ;
			if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
			}
		catch (SAXException ouch)
			{
			//
			// FIXME ....
			if (DEBUG_FLAG) System.out.println("SAXException in response parser") ;
			if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
			}
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		//
		// Return our status node.
		return status ;
		}

	/**
	 * Delete a data item.
	 *
	 */
	public StatusNode deleteItem(String path)
		{
		DataNode   data   = null ;
		StatusNode status = null ;
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("ExplorerView.deleteItem()") ;
		if (DEBUG_FLAG) System.out.println("Path : " + path) ;
		try {
			//
			// Create our service request
			DeleteRequestBuilder request = new DeleteRequestBuilder(path) ;
			if (DEBUG_FLAG)
				{
				System.out.println("----") ;
				System.out.println(request) ;
				System.out.println("----") ;
				}
			//
			// Create our service instance.
			initMySpaceService() ;
			//
			// Call our service method.
			String response = myspaceService.deleteDataHolder(request.toString()) ;
			if (DEBUG_FLAG)
				{
				System.out.println("----") ;
				System.out.println(response) ;
				System.out.println("----") ;
				}
			//
			// Create a parser.
			DeleteResponseParser parser = new DeleteResponseParser() ;
			//
			// Parse the response.
			parser.parse(response) ;
			//
			// Get the result status.
			status = parser.getStatus() ;
			//
			// Get the result data.
			data = parser.getData() ;
			}
		catch (RemoteException ouch)
			{
			//
			// FIXME ....
			if (DEBUG_FLAG) System.out.println("Exception calling WebService") ;
			if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
			}
		catch (IOException ouch)
			{
			//
			// FIXME ....
			if (DEBUG_FLAG) System.out.println("IOException in response parser") ;
			if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
			}
		catch (SAXException ouch)
			{
			//
			// FIXME ....
			if (DEBUG_FLAG) System.out.println("SAXException in response parser") ;
			if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
			}
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		//
		// Return our status node.
		return status ;
		}

	/**
	 * Lookup a data item.
	 *
	 */
	public DataNode lookupItem(String path)
		{
		DataNode   data   = null ;
		StatusNode status = null ;
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("ExplorerView.lookupItem()") ;
		if (DEBUG_FLAG) System.out.println("Path : " + path) ;
		try {
			//
			// Create our service request
			DetailsRequestBuilder request = new DetailsRequestBuilder(path) ;
			if (DEBUG_FLAG)
				{
				System.out.println("----") ;
				System.out.println(request) ;
				System.out.println("----") ;
				}
			//
			// Create our service instance.
			initMySpaceService() ;
			//
			// Call our service method.
			String response = myspaceService.lookupDataHolderDetails(request.toString()) ;
			if (DEBUG_FLAG)
				{
				System.out.println("----") ;
				System.out.println(response) ;
				System.out.println("----") ;
				}
			//
			// Create a parser.
			DetailsResponseParser parser = new DetailsResponseParser() ;
			//
			// Parse the response.
			parser.parse(response) ;
			//
			// Get the result status.
			status = parser.getStatus() ;
			//
			// Get the result data.
			data = parser.getData() ;
			}
		catch (RemoteException ouch)
			{
			//
			// FIXME ....
			if (DEBUG_FLAG) System.out.println("Exception calling WebService") ;
			if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
			}
		catch (IOException ouch)
			{
			//
			// FIXME ....
			if (DEBUG_FLAG) System.out.println("IOException in response parser") ;
			if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
			}
		catch (SAXException ouch)
			{
			//
			// FIXME ....
			if (DEBUG_FLAG) System.out.println("SAXException in response parser") ;
			if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
			}
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		//
		// Return our data node.
		return data ;
		}

	/**
	 * Export a data item.
	 *
	 */
	public DataNode exportItem(String path)
		{
		DataNode   data   = null ;
		StatusNode status = null ;
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("ExplorerView.exportItem()") ;
		if (DEBUG_FLAG) System.out.println("Path : " + path) ;
		try {
			//
			// Create our service request
			ExportRequestBuilder request = new ExportRequestBuilder(path) ;
			if (DEBUG_FLAG)
				{
				System.out.println("----") ;
				System.out.println(request) ;
				System.out.println("----") ;
				}
			//
			// Create our service instance.
			initMySpaceService() ;
			//
			// Call our service method.
			String response = myspaceService.exportDataHolder(request.toString()) ;
			if (DEBUG_FLAG)
				{
				System.out.println("----") ;
				System.out.println(response) ;
				System.out.println("----") ;
				}
			//
			// Create a parser.
			ExportResponseParser parser = new ExportResponseParser() ;
			//
			// Parse the response.
			parser.parse(response) ;
			//
			// Get the result status.
			status = parser.getStatus() ;
			//
			// Get the result data.
			data = parser.getData() ;
			}
		catch (RemoteException ouch)
			{
			//
			// FIXME ....
			if (DEBUG_FLAG) System.out.println("Exception calling WebService") ;
			if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
			}
		catch (IOException ouch)
			{
			//
			// FIXME ....
			if (DEBUG_FLAG) System.out.println("IOException in response parser") ;
			if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
			}
		catch (SAXException ouch)
			{
			//
			// FIXME ....
			if (DEBUG_FLAG) System.out.println("SAXException in response parser") ;
			if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
			}
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		//
		// Return our data node.
		return data ;
		}

	}
