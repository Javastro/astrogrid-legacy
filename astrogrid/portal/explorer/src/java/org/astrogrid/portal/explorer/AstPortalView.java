/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portal/explorer/src/java/org/astrogrid/portal/explorer/Attic/AstPortalView.java,v $</cvs:source>
 * <cvs:date>$Author: anoncvs $</cvs:date>
 * <cvs:author>$Date: 2003/12/16 14:16:54 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: AstPortalView.java,v $
 * Revision 1.1  2003/12/16 14:16:54  anoncvs
 * Add explorer actions source files
 *
 * Revision 1.11  2003/07/10 14:22:38  KevinBenson
 * I might have to get rid of these WS_FTP.log files not sure how they got their.  Anyays just fixing some compiling mistakes.
 *
 * Revision 1.10  2003/06/24 15:36:41  dave
 * Adding initial XSP pages
 *
 * Revision 1.9  2003/06/24 10:43:25  dave
 * Fixed bugs in DataTreeWalker and tree page
 *
 * Revision 1.8  2003/06/23 23:21:11  dave
 * Updated the page actions
 *
 * Revision 1.7  2003/06/23 11:19:03  dave
 * Added service location to view pages
 *
 * Revision 1.6  2003/06/22 22:29:48  dave
 * Added message, actions and page for move
 *
 * Revision 1.5  2003/06/22 04:03:41  dave
 * Added actions and parsers for MySpace messages
 *
 * Revision 1.4  2003/06/18 12:05:43  dave
 * Added debug and response status
 *
 * Revision 1.3  2003/06/18 01:33:15  dave
 * Moved message parser into separate class and added service lookup to pages
 *
 * Revision 1.2  2003/06/17 15:17:34  dave
 * Added links to live MySpace, including initial XML parser for lookup results
 *
 * Revision 1.1  2003/06/12 18:19:38  dave
 * Initial import into cvs ...
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.explorer ;

import org.astrogrid.portal.base.AstPortalBase  ;
import org.astrogrid.portal.base.AstPortalIdent ;

import org.astrogrid.portal.session.AstPortalSession ;

import java.net.URLEncoder ;

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
 * A class to encapsulate an explorer view in the protal.
 *
 */
public class AstPortalView
	extends AstPortalBase
	{

	/**
	 * Switch for our debug statements.
	 *
	 */
	public static boolean DEBUG_FLAG = true ;

	/**
	 * Reference to our session.
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
		// Use next constructor.
		this(session, null, path) ;
		}

	/**
	 * Public constructor.
	 *
	 */
	public AstPortalView(AstPortalSession session, String service, String path)
		{
		//
		// Initialise our base class.
		super() ;
		//
		// Initialise our session.
		this.session = session ;
		//
		// Initialise our service location.
		this.setMySpaceLocation(service) ;
		//
		// Initialise our path.
		this.setPath(path) ;
		}

	/**
	 * Encode a string.
	 *
	 */
	public String URLEncode(String string)
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
	public String getPath()
		{
		return this.path ;
		}

	/**
	 * Access to our search path, URL encoded.
	 *
	 */
	public String getURLEncodedPath()
		{
		return URLEncode(this.getPath()) ;
		}

	/**
	 * Access to our search path.
	 *
	 */
	public void setPath(String path)
		{
		this.path = path ;
		}

	/**
	 * Our selected item path.
	 *
	 */
	protected String item ;

	/**
	 * Access to our selected item.
	 *
	 */
	public String getItemPath()
		{
		return this.item ;
		}

	/**
	 * Access to our selected item, URL encoded.
	 *
	 */
	public String getURLEncodedItemPath()
		{
		return URLEncode(this.getItemPath()) ;
		}

	/**
	 * Access to our selected item.
	 *
	 */
	public void setItemPath(String item)
		{
		this.item = item ;
		}

	/**
	 * Our destination path for copy and move.
	 *
	 */
	protected String dest ;

	/**
	 * Access to our destination path.
	 *
	 */
	public String getDestPath()
		{
		return this.dest ;
		}

	/**
	 * Access to our destination path, URL encoded.
	 *
	 */
	public String getURLEncodedDestPath()
		{
		return URLEncode(this.getDestPath()) ;
		}

	/**
	 * Access to our destination path.
	 *
	 */
	public void setDestPath(String dest)
		{
		this.dest = dest ;
		}

	/**
	 * Our destination name for copy and move.
	 *
	 */
	protected String name ;

	/**
	 * Access to our destination name.
	 *
	 */
	public String getDestName()
		{
		return this.name ;
		}

	/**
	 * Access to our destination name, URL encoded.
	 *
	 */
	public String getURLEncodedDestName()
		{
		return URLEncode(this.getDestName()) ;
		}

	/**
	 * Access to our destination path.
	 *
	 */
	public void setDestName(String name)
		{
		this.name = name ;
		}

	/**
	 * Access to our destination file name.
	 *
	 */
	public String getDestFile()
		{
		return this.dest + "/" + this.name ;
		}

	/**
	 * Access to our destination file name, URL encoded.
	 *
	 */
	public String getURLEncodedDestFile()
		{
		return URLEncode(this.getDestFile()) ;
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
			this
			.myspaceLocation = myspaceLocator.getMySpaceManagerAddress() ;
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
			LookupRequestBuilder request = new LookupRequestBuilder(this.getPath() + "*") ;
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
	 * Request a DataNode for our selected item.
	 *
	 */
	public DataNode getData()
		{
		DataNode   data   = null ;
		StatusNode status = null ;
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AstPortalView.getData()") ;
		try {
			//
			// Create our service request
			DetailsRequestBuilder request = new DetailsRequestBuilder(this.getItemPath()) ;
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
	 * Copy our selected data item.
	 *
	 */
	public StatusNode copyItem()
		{
		DataNode   data   = null ;
		StatusNode status = null ;
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AstPortalView.copyItem()") ;
		try {
//
// FIXME : Could be nicer ... call the MySpace service to check for an existing item.
// Problem is that doing a lookup for something that isn't there breaks the MySpace service.
//
			//
			// If we are copying the item into the same directory.
			if (this.getItemPath().equals(this.getDestFile()))
				{
				//
				// Add 'Copy of' to the name
				this.setDestName("Copy-of-" + this.getDestName()) ;
				}
			//
			// Create our service request
			CopyRequestBuilder request = new CopyRequestBuilder(this.getItemPath(), this.getDestFile()) ;
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
	 * Move our selected data item.
	 *
	 */
	public StatusNode moveItem()
		{
		DataNode   data   = null ;
		StatusNode status = null ;
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AstPortalView.moveItem()") ;
		try {
			//
			// Create our service request
			MoveRequestBuilder request = new MoveRequestBuilder(this.getItemPath(), this.getDestFile()) ;
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
	public StatusNode deleteItem(String name)
		{
		DataNode   data   = null ;
		StatusNode status = null ;
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AstPortalView.deleteItem()") ;
		try {
			//
			// Create our service request
			DeleteRequestBuilder request = new DeleteRequestBuilder(name) ;
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

	}
