/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/store/adapter/aladin/AladinAdapterMock.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/03/11 13:37:05 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: AladinAdapterMock.java,v $
 *   Revision 1.2  2005/03/11 13:37:05  clq2
 *   new filemanager merged with filemanager-nww-jdt-903-943
 *
 *   Revision 1.1.2.2  2005/03/01 15:07:27  nw
 *   close to finished now.
 *
 *   Revision 1.1.2.1  2005/02/27 23:03:12  nw
 *   first cut of talking to filestore
 *
 *   Revision 1.5  2005/01/13 11:27:39  jdt
 *   Merges from myspace-nww-890
 *
 *   Revision 1.4.8.1  2005/01/12 17:08:20  nw
 *   implemented getURL
 *
 *   Revision 1.4  2004/11/17 16:22:53  clq2
 *   nww-itn07-704
 *
 *   Revision 1.3.16.2  2004/11/16 20:22:06  nw
 *   put this back in, even though it isn't really needed, incase any existing code imports it.
 *
 *   Revision 1.3  2004/10/05 15:39:29  dave
 *   Merged changes to AladinAdapter ...
 *
 *   Revision 1.2.4.1  2004/10/05 15:30:44  dave
 *   Moved test base from test to src tree ....
 *   Added MimeTypeUtil
 *   Added getMimeType to the adapter API
 *   Added logout to the adapter API
 *
 *   Revision 1.2  2004/09/28 10:24:19  dave
 *   Added AladinAdapter interfaces and mock implementation.
 *
 *   Revision 1.1.2.8  2004/09/27 22:46:53  dave
 *   Added AdapterFile interface, with input and output stream API.
 *
 *   Revision 1.1.2.7  2004/09/24 01:36:18  dave
 *   Refactored File as Node and Container ...
 *
 *   Revision 1.1.2.6  2004/09/24 01:12:09  dave
 *   Added initial test for child nodes.
 *
 *   Revision 1.1.2.5  2004/09/23 16:32:02  dave
 *   Added better Exception handling ....
 *   Added initial mock container ....
 *   Added initial root container tests ...
 *
 *   Revision 1.1.2.4  2004/09/23 12:21:31  dave
 *   Added mock security service and login test ...
 *
 *   Revision 1.1.2.3  2004/09/23 10:12:19  dave
 *   Added config properties for JUnit tests ....
 *   Added test for null password.
 *
 *   Revision 1.1.2.2  2004/09/23 09:18:13  dave
 *   Renamed AbstractTest to TestBase to exclude it from batch test ....
 *   Added first test for null account ....
 *
 *   Revision 1.1.2.1  2004/09/22 16:47:37  dave
 *   Added initial classes and tests for AladinAdapter.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.store.adapter.aladin ;

import org.astrogrid.community.common.exception.CommunitySecurityException;
import org.astrogrid.community.common.ivorn.CommunityIvornParser;
import org.astrogrid.community.common.security.data.SecurityToken;
import org.astrogrid.community.common.security.service.SecurityServiceMock;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.util.MimeTypeUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A mock implementation of the AladinAdapter interface.
 *@deprecated - just here for backwards compatability. use client accessed from  {@link org.astrogrid.filemanager.client.FileManagerClientFactory} instead.
 *
 */
public class AladinAdapterMock
	implements AladinAdapter
	{

	/**
	 * Our test account identifier.
	 *
	 */
	private Ivorn account ;

	/**
	 * Set the test account identifier.
	 * @param ivorn The test account identifier.
	 *
	 */
	public void setTestAccount(Ivorn ivorn)
		{
		this.account = ivorn ;
		}

	/**
	 * Set the test account password.
	 *
	 */
	public void setTestPassword(String password)
		{
		//
		// Setup the security service password.
		security.setPassword(password) ;
		}

	/**
	 * Our mock security service.
	 *
	 */
	SecurityServiceMock security = new SecurityServiceMock() ;

	/**
	 * Public constructor.
	 *
	 */
	public AladinAdapterMock()
		{
		}

	/**
	 * Our current secutiry token.
	 *
	 */
	private SecurityToken token ;

	/**
	 * Login to the AstroGrid community.
	 * @param ivorn    The 'ivo://...' identifier for the AstroGrid account.
	 * @param password The account password.
     * @throws AladinAdapterLoginException   If the login fails.
     * @throws AladinAdapterServiceException If unable to handle the request.
	 *
	 */
	public void login(Ivorn ivorn, String password)
		throws AladinAdapterLoginException, AladinAdapterServiceException
		{
		if (null == ivorn)
			{
			throw new IllegalArgumentException(
				"Null account"
				) ;
			}
		if (null == password)
			{
			throw new IllegalArgumentException(
				"Null password"
				) ;
			}
		//
		// Check the password with our resolver.
		try {
			//
			// Try login to the account.
			this.token = null ;
			this.token = security.checkPassword(
				ivorn.toString(),
				password
				) ;
			//
			// Initialise our file system.
			this.init() ;
			}
		catch (CommunitySecurityException ouch)
			{
			throw new AladinAdapterLoginException(
				"Login failed",
				ouch
				) ;
			}
		catch (Throwable ouch)
			{
			throw new AladinAdapterServiceException(
				"Failed to perform request",
				ouch
				) ;
			}
		}

	/**
	 * Access to the current security token.
	 * @return The current security token, or null if not logged in.
	 *
	 */
	public SecurityToken getToken()
		{
		return this.token ;
		}

	/**
	 * Logout from the AstroGrid community.
	 *
	 */
	public void logout()
		{
		this.token = null ;
		}

	/**
	 * Our fake myspace file system.
	 *
	 */
	private Map myspace = new HashMap() ;

	/**
	 * Get the root node of the account home space.
	 * @return An AladinAdapterContainer representing the root of the account myspace.
     * @throws AladinAdapterSecurityException If the adapter is not logged in.
     * @throws AladinAdapterServiceException  If unable to handle the request.
	 *
	 */
	public AladinAdapterContainer getRoot()
		throws AladinAdapterSecurityException, AladinAdapterServiceException
		{
		//
		// Check we are logged in.
		if (null == this.token)
			{
			throw new AladinAdapterSecurityException(
				"Not logged in"
				) ;
			}
		//
		// If we are logged in.
		else {
			return (AladinAdapterContainer) myspace.get(
				this.token.getAccount()
				) ;
			}
		}

	/**
	 * Initialise our mock myspace.
	 *
	 */
	private void init()
		throws AladinAdapterServiceException
		{
		//
		// If we have a valid token.
		if (null != this.token)
			{
			//
			// If we don't have a root node for the account.
			if (false == myspace.containsKey(this.token.getAccount()))
				{
				try {
					//
					// Parse current the account ivorn.
					CommunityIvornParser parser = new CommunityIvornParser(
						this.token.getAccount()
						) ;
					//
					// Create the root node for this account.
					MockContainer root = new MockContainer(
							parser.getAccountName()
							) ;
					//
					// Add the default child containers.
					root.addContainer(
						"workflow"
						) ;
					//
					// Add the root container to our map.
					myspace.put(
						this.token.getAccount(),
						root
						) ;
					}
				catch (Exception ouch)
					{
					throw new AladinAdapterServiceException(
						"Failed to initialise account space",
						ouch
						) ;
					}
				}
			}
		}

	/**
	 * Inner class to represent a myspace container.
	 *
	 */
	public class MockContainer
		implements AladinAdapterContainer
		{
		/**
		 * Public constructor.
		 * @param name The container name.
		 *
		 */
		public MockContainer(String name)
			{
			if (null == name)
				{
				throw new IllegalArgumentException(
					"Null container name"
					) ;
				}
			this.name = name ;
			}

		/**
		 * The name of this container.
		 *
		 */
		private String name ;

		/**
		 * Access to the name.
		 *
		 */
		public String getName()
			{
			return this.name ;
			}

		/**
		 * Check if this represents a file.
		 * @return true if this is a file.
		 *
		 */
		public boolean isFile()
			{
			return false ;
			}

		/**
		 * Check if this represents a container.
		 * @return true if this is a container.
		 *
		 */
		public boolean isContainer()
			{
			return true ;
			}

		/**
		 * A map of our child nodes.
		 *
		 */
		private Map nodes = new HashMap() ;

		/**
		 * Get a list (collection) of the current child nodes.
		 * Note, you cannot add a child node by adding a node to the collection.
		 * @return An unmodifiable collection of AladinAdapterNode(s) for the child nodes.
		 *
		 */
		public Collection getChildNodes()
			{
		    return Collections.unmodifiableCollection(nodes.values());
			}

		/**
		 * Add a child container.
		 * @param name The container name.
	     * @throws AladinAdapterDuplicateException If the container already exists.
		 * @throws AladinAdapterServiceException   If the service is unable to handle the request.
		 *
		 */
		public AladinAdapterContainer addContainer(String name)
			throws AladinAdapterDuplicateException
			{
			if (nodes.containsKey(name))
				{
				throw new AladinAdapterDuplicateException() ;
				}
			else {
				AladinAdapterContainer node = new MockContainer(
					name
					) ;
				nodes.put(
					name,
					node
					) ;
				return node ;
				}
			}

		/**
		 * Add a file to this container.
		 * @param name The container name.
	     * @throws AladinAdapterDuplicateException If the container already exists.
		 * @throws AladinAdapterServiceException   If the service is unable to handle the request.
		 *
		 */
		public AladinAdapterFile addFile(String name)
			throws AladinAdapterDuplicateException
			{
			if (nodes.containsKey(name))
				{
				throw new AladinAdapterDuplicateException() ;
				}
			else {
				AladinAdapterFile node = new MockFile(
					name
					) ;
				nodes.put(
					name,
					node
					) ;
				return node ;
				}
			}
		}

	/**
	 * Inner class to represent a myspace file.
	 *
	 */
	public class MockFile
		implements AladinAdapterFile
		{
		/**
		 * Public constructor.
		 * @param name The file name.
		 *
		 */
		public MockFile(String name)
			{
			if (null == name)
				{
				throw new IllegalArgumentException(
					"Null file name"
					) ;
				}
			this.name = name ;
			}

		/**
		 * The name of this file.
		 *
		 */
		private String name ;

		/**
		 * Access to the name.
		 *
		 */
		public String getName()
			{
			return this.name ;
			}

		/**
		 * Check if this represents a file.
		 * @return true if this is a file.
		 *
		 */
		public boolean isFile()
			{
			return true ;
			}

		/**
		 * Check if this represents a container.
		 * @return true if this is a container.
		 *
		 */
		public boolean isContainer()
			{
			return false ;
			}

		/**
		 * Get the mime type for the file.
		 * @return The mime type for the file contents, or null if is not set..
		 *
		 */
		public String getMimeType() {
			MimeTypeUtil util = new MimeTypeUtil() ;
			return util.resolve(
				this.getName()
				) ;
			}

		/**
		 * Our internal byte array.
		 *
		 */
		private byte[] data = new byte[0] ;

		/**
		 * Get an OutputStream to send data to the file.
		 * Openning a new stream to an existing file will over-write the file contents.
		 * The client MUST close the output stream to force the transfer to complete.
	     * @throws AladinAdapterServiceException If the service is unable to handle the request.
		 *
		 */
		public OutputStream getOutputStream()
			{
			return new ByteArrayOutputStream()
				{
				public void close()
					{
					data = this.toByteArray() ;
					}
				} ;
			}

		/**
		 * Get an InputStream to read data from the file.
	     * @throws AladinAdapterServiceException If the service is unable to handle the request.
		 *
		 */
		public InputStream getInputStream()
			{
			return new ByteArrayInputStream(
				data
				) ;
			}

        /**
         * @see org.astrogrid.store.adapter.aladin.AladinAdapterFile#getURL()
         */
        public URL getURL() throws AladinAdapterServiceException {
            
            try {
                return new URL("http://www.astrogrid.org");
            } catch (MalformedURLException e) {
                throw new AladinAdapterServiceException("malformed url",e);
            }
        }
		}
	}
