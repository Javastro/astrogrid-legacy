/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/explorer/Attic/AstPortalView.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/12 18:19:38 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: AstPortalView.java,v $
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

import java.util.Map ;
import java.util.Iterator ;
import java.util.Hashtable ;

import java.rmi.RemoteException ;
import javax.xml.rpc.ServiceException ;

//
// Import the generated client stubs.
import org.astrogrid.portal.mock.myspace.client.MySpaceItem ;
import org.astrogrid.portal.mock.myspace.client.MySpaceService ;
import org.astrogrid.portal.mock.myspace.client.MySpaceServiceService ;
import org.astrogrid.portal.mock.myspace.client.MySpaceServiceServiceLocator ;

/**
 * A class to encapsulate an explorer view in the protal.
 *
 */
public class AstPortalView
	extends AstPortalBase
	{

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
	protected MySpaceServiceService locator = new MySpaceServiceServiceLocator() ;

	/**
	 * Connection to our MySpace service.
	 * The interface and stub classes comes from the Axis WSDL2Java toolkit.
	 *
	 */
	protected MySpaceService myspace ;

	/**
	 * Initialise our MySpace service.
	 * 
	 */
	public boolean initMySpaceService()
		{
		boolean result = false ;
		if (null == myspace)
			{
			try {
				myspace = locator.getmyspace() ;
				result = true ;
				}
			catch (ServiceException ouch)
				{
//
// FIXME ....
//
				}
			}
		return result ;
		}

	/**
	 * Ping our MySpace service.
	 * 
	 */
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

	}
