/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/explorer/Attic/AstPortalView.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/18 12:05:43 $</cvs:author>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 * $Log: AstPortalView.java,v $
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

import org.astrogrid.portal.services.myspace.client.LookupResponseParser ;
import org.astrogrid.portal.services.myspace.client.DataItemRecord ;

//
// Import the generated mock client stubs.
//import org.astrogrid.portal.mock.myspace.client.MySpaceItem ;
//import org.astrogrid.portal.mock.myspace.client.MySpaceService ;
//import org.astrogrid.portal.mock.myspace.client.MySpaceServiceService ;
//import org.astrogrid.portal.mock.myspace.client.MySpaceServiceServiceLocator ;
//
// Import the WSDL generated client stubs.
// ----"----
import org.astrogrid.portal.services.myspace.client.MySpaceManager ;
import org.astrogrid.portal.services.myspace.client.MySpaceManagerService ;
import org.astrogrid.portal.services.myspace.client.MySpaceManagerServiceLocator ;
// ----"----
//

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
	 * Connection to our MySpace service locator.
	 * The interface and stub classes comes from the Axis WSDL2Java toolkit.
	 *
	 */
	protected MySpaceManagerService locator = new MySpaceManagerServiceLocator() ;

	/**
	 * Connection to our MySpace service.
	 * The interface and stub classes comes from the Axis WSDL2Java toolkit.
	 *
	 */
	protected MySpaceManager myspace ;

	/**
	 * Initialise our MySpace service.
	 * 
	 */
	public boolean initMySpaceService()
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AstPortalView.initMySpaceService()") ;
		boolean result = false ;
		if (null == myspace)
			{
			try {
				myspace = locator.getMySpaceManager() ;
				result = true ;
				}
			catch (ServiceException ouch)
				{
//
// FIXME ....
//
				if (DEBUG_FLAG) System.out.println("Exception while initialising MySpaceManager") ;
				if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;

				}
			}
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		return result ;
		}

	/**
	 * Ping our MySpace service.
	 * Deprecated .. ping method not available on the live MySpaceService
	public boolean pingMySpaceService()
		{
		boolean result = false ;
		if (null != myspace)
			{
			try {
				myspace.ping() ;
				result = true ;
				}
			catch (RemoteException ouch)
				{
//
// FIXME ....
//
				}
			}
		return result ;
		}
	 */

	/**
	 * Our current list of DataItems.
	 *
	 */
	private Collection items ;

	/**
	 * Access to our list of DataItems.
	 *
	 */
	public Collection getDataItems()
		{
		return this.items ;
		}

	/**
	 * Call the lookupDataHoldersDetails on our MySpaceManager.
	 *
	 */
	public void lookupDataHoldersDetails()
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AstPortalView.lookupDataHoldersDetails()") ;
		//
		// Create our request, using our path.
		String request =
			"<request>" +
				"<userID>frog</userID>" +
				"<communityID>frogs</communityID>" +
				"<jobID>0000</jobID>" +
				"<mySpaceAction>lookup</mySpaceAction>" +
				"<query>" + 
				this.getPath() +
				"</query>" +
			"</request>"
			;

		if (DEBUG_FLAG)
			{
			System.out.println("----") ;
			System.out.println(request) ;
			System.out.println("----") ;
			}

		//
		// Call our MySpaceManager service.
		String response = null ;
		try {
			response = myspace.lookupDataHoldersDetails(request) ;
			}
		catch (RemoteException ouch)
			{
			//
			// FIXME ....
			if (DEBUG_FLAG) System.out.println("Exception calling lookupDataHoldersDetails()") ;
			if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
			}
		//
		// If we got a response.
		if (null != response)
			{
			try {
				//
				// Check we can create a parser.
				LookupResponseParser parser = new LookupResponseParser() ;
				//
				// Parse the response.
				parser.parseResponse(response) ;
				//
				// Collect the DataItems.
				items = parser.getResults() ;
				}
			catch (IOException ouch)
				{
				//
				// FIXME ....
				if (DEBUG_FLAG) System.out.println("IOException in LookupResponseParser") ;
				if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
				}
			catch (SAXException ouch)
				{
				//
				// FIXME ....
				if (DEBUG_FLAG) System.out.println("SAXException in LookupResponseParser") ;
				if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
				}
			}
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}


	}
