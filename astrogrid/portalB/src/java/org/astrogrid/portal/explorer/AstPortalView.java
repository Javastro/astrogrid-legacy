/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/explorer/Attic/AstPortalView.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/22 22:29:48 $</cvs:author>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 * $Log: AstPortalView.java,v $
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

import java.io.Reader ;
import java.io.StringReader ;
import java.io.IOException ;

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

import org.astrogrid.portal.services.myspace.client.data.DataNode ;
import org.astrogrid.portal.services.myspace.client.status.StatusNode ;

//
// Import the WSDL generated client stubs.
import org.astrogrid.portal.services.myspace.client.MySpaceManager ;
import org.astrogrid.portal.services.myspace.client.MySpaceManagerService ;
import org.astrogrid.portal.services.myspace.client.MySpaceManagerServiceLocator ;

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

	/**
	 * Our selected item path.
	 *
	 */
	protected String item ;

	/**
	 * Access to our selected item.
	 *
	 */
	public String getItem()
		{
		return this.item ;
		}

	/**
	 * Access to our selected item.
	 *
	 */
	public void setItem(String path)
		{
		this.item = path ;
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
	 * Access to our destination path.
	 *
	 */
	public void setDestPath(String path)
		{
		this.dest = path ;
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
	 * Access to our destination path.
	 *
	 */
	public void setDestName(String name)
		{
		this.name = name ;
		}

	/**
	 * Connection to our MySpace service locator.
	 *
	 */
	protected MySpaceManagerService locator = new MySpaceManagerServiceLocator() ;

	/**
	 * Connection to our MySpace service.
	 *
	 */
	protected MySpaceManager myspace ;

	/**
	 * Initialise our MySpace service.
	 * 
	 */
	public boolean initService()
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AstPortalView.initMySpaceService()") ;
		boolean result = false ;
		//
		// If we don't already have a service.
		if (null == myspace)
			{
			try {
				myspace = locator.getMySpaceManager() ;
				}
			catch (ServiceException ouch)
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
		return (null != myspace)  ;
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
			LookupRequestBuilder request = new LookupRequestBuilder(this.getPath() + "/*") ;
			if (DEBUG_FLAG)
				{
				System.out.println("----") ;
				System.out.println(request) ;
				System.out.println("----") ;
				}
			//
			// Call our service method.
			String response = myspace.lookupDataHoldersDetails(request.toString()) ;
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
			DetailsRequestBuilder request = new DetailsRequestBuilder(this.getItem()) ;
			if (DEBUG_FLAG)
				{
				System.out.println("----") ;
				System.out.println(request) ;
				System.out.println("----") ;
				}
			//
			// Call our service method.
			String response = myspace.lookupDataHolderDetails(request.toString()) ;
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
			// Create our service request
			CopyRequestBuilder request = new CopyRequestBuilder(this.getItem(), (this.getDestPath() + "/" + this.getDestName())) ;
			if (DEBUG_FLAG)
				{
				System.out.println("----") ;
				System.out.println(request) ;
				System.out.println("----") ;
				}
			//
			// Call our service method.
			String response = myspace.copyDataHolder(request.toString()) ;
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
			MoveRequestBuilder request = new MoveRequestBuilder(this.getItem(), (this.getDestPath() + "/" + this.getDestName())) ;
			if (DEBUG_FLAG)
				{
				System.out.println("----") ;
				System.out.println(request) ;
				System.out.println("----") ;
				}
			//
			// Call our service method.
			String response = myspace.moveDataHolder(request.toString()) ;
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
	public StatusNode deleteItem(String item)
		{
		DataNode   data   = null ;
		StatusNode status = null ;
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AstPortalView.deleteItem()") ;
		try {
			//
			// Create our service request
			DeleteRequestBuilder request = new DeleteRequestBuilder(item) ;
			if (DEBUG_FLAG)
				{
				System.out.println("----") ;
				System.out.println(request) ;
				System.out.println("----") ;
				}
			//
			// Call our service method.
			String response = myspace.deleteDataHolder(request.toString()) ;
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

	}
