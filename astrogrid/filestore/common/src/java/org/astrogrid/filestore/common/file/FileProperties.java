/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/file/FileProperties.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/09 01:19:50 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 * <cvs:log>
 *   $Log: FileProperties.java,v $
 *   Revision 1.7  2004/09/09 01:19:50  dave
 *   Updated MIME type handling in MySpace.
 *   Extended test coverage for MIME types in FileStore and MySpace.
 *   Added VM memory data to community ServiceStatusData.
 *
 *   Revision 1.6.6.3  2004/09/08 13:43:37  dave
 *   Added VoList mime type.
 *
 *   Revision 1.6.6.2  2004/09/08 12:41:56  dave
 *   Added adql mime type.
 *
 *   Revision 1.6.6.1  2004/09/08 12:39:18  dave
 *   Added votable and volist mime types.
 *
 *   Revision 1.6  2004/09/02 10:25:41  dave
 *   Updated FileStore and MySpace to handle mime type and file size.
 *   Updated Community deployment script.
 *
 *   Revision 1.5.2.1  2004/09/01 02:58:07  dave
 *   Updated to use external mime type for imported files.
 *
 *   Revision 1.5  2004/08/27 22:43:15  dave
 *   Updated filestore and myspace to report file size correctly.
 *
 *   Revision 1.4.12.2  2004/08/27 11:48:41  dave
 *   Added getContentSize to FileProperties.
 *
 *   Revision 1.4.12.1  2004/08/26 19:06:50  dave
 *   Modified filestore to return file size in properties.
 *
 *   Revision 1.4  2004/08/18 19:00:01  dave
 *   Myspace manager modified to use remote filestore.
 *   Tested before checkin - integration tests at 91%.
 *
 *   Revision 1.3.16.3  2004/08/09 13:38:21  dave
 *   Added getResourceUrl() to properties API.
 *
 *   Revision 1.3.16.2  2004/08/06 22:25:06  dave
 *   Refactored bits and broke a few tests ...
 *
 *   Revision 1.3.16.1  2004/08/02 14:54:15  dave
 *   Trying to get integration tests to run
 *
 *   Revision 1.3  2004/07/21 18:11:55  dave
 *   Merged development branch, dave-dev-200407201059, into HEAD
 *
 *   Revision 1.2.6.1  2004/07/21 16:28:16  dave
 *   Added content properties and tests
 *
 *   Revision 1.2  2004/07/14 13:50:29  dave
 *   Merged development branch, dave-dev-200406301228, into HEAD
 *
 *   Revision 1.1.2.2  2004/07/14 10:34:08  dave
 *   Reafctored server to use array of properties
 *
 *   Revision 1.1.2.1  2004/07/13 16:37:29  dave
 *   Refactored common and client to use an array of FileProperties (more SOAP friendly)
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.common.file ;

import java.net.URL ;

import java.io.IOException ;
import java.io.InputStream ;
import java.io.OutputStream ;

import java.util.Map ;
import java.util.Map.Entry ;
import java.util.Iterator ;
import java.util.Properties ;
import java.util.Enumeration ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.filestore.common.ivorn.FileStoreIvornFactory ;
import org.astrogrid.filestore.common.exception.FileStoreIdentifierException ;

/**
 * A wrapper for a set of file properties.
 *
 */
public class FileProperties
	{
	/**
	 * The property key for the service identifier.
	 *
	 */
	public static final String STORE_SERVICE_IDENT  = "org.astrogrid.filestore.service.ident" ;

	/**
	 * The property key for the service ivorn.
	 *
	 */
	public static final String STORE_SERVICE_IVORN  = "org.astrogrid.filestore.service.ivorn" ;

	/**
	 * The property key for the resource identifier.
	 *
	 */
	public static final String STORE_RESOURCE_IDENT  = "org.astrogrid.filestore.resource.ident" ;

	/**
	 * The property key for the resource ivorn.
	 *
	 */
	public static final String STORE_RESOURCE_IVORN  = "org.astrogrid.filestore.resource.ivorn" ;

	/**
	 * The property key for the resource URL.
	 *
	 */
	public static final String STORE_RESOURCE_URL  = "org.astrogrid.filestore.resource.url" ;

	/**
	 * The property key for the transfer source URL.
	 *
	 */
	public static final String TRANSFER_SOURCE_URL  = "org.astrogrid.filestore.transfer.source.url" ;

	/**
	 * The property key for content size.
	 *
	 */
	public static final String CONTENT_SIZE_PROPERTY  = "org.astrogrid.filestore.content.size" ;

	/**
	 * The property key for content type.
	 *
	 */
	public static final String MIME_TYPE_PROPERTY  = "org.astrogrid.filestore.content.type" ;

	/**
	 * The property key for content encoding.
	 *
	 */
	public static final String MIME_ENCODING_PROPERTY  = "org.astrogrid.filestore.content.encoding" ;

	/**
	 * Known MIME type values.
	 *
	 */
	public static final String MIME_TYPE_TEXT     = "text/raw"  ;
	public static final String MIME_TYPE_XML      = "text/xml"  ;
	public static final String MIME_TYPE_HTML     = "t"  ;

	public static final String MIME_TYPE_VOLIST   = "text/xml +org.astrogrid.mime.volist"  ;
	public static final String MIME_TYPE_VOTABLE  = "text/xml +org.astrogrid.mime.votable" ;

	public static final String MIME_TYPE_JOB      = "text/xml +org.astrogrid.mime.job"      ;
	public static final String MIME_TYPE_WORKFLOW = "text/xml +org.astrogrid.mime.workflow" ;

	public static final String MIME_TYPE_ADQL     = "text/xml +org.astrogrid.mime.adql"     ;

	/**
	 * Public constructor.
	 *
	 */
	public FileProperties()
		{
		}

	/**
	 * Public constructor from an array of values.
	 * @param array An array of property values.
	 *
	 */
	public FileProperties(FileProperty[] array)
		{
		if (null != array)
			{
			for (int i = 0 ; i < array.length ; i++)
				{
				this.setProperty(
					array[i].getName(),
					array[i].getValue()
					) ;
				}
			}
		}

	/**
	 * Public constructor from another set of properties.
	 * @param that The other set of properties.
	 *
	 */
	public FileProperties(FileProperties that)
		{
		this.merge(that) ;
		}

	/**
	 * Our internal map of properties.
	 *
	 */
	private Properties properties = new Properties() ;

	/**
	 * Access to our list of properties.
	 *
	 */
	protected Map getProperties()
		{
		return this.properties ;
		}

	/**
	 * Get a property.
	 *
	 */
	public String getProperty(String key)
		{
		return properties.getProperty(key) ;
		}

	/**
	 * Set a named property.
	 * @param key The property key (name).
	 * @param value The property value.
	 *
	 */
	public void setProperty(String key, String value)
		{
		if (null != key)
			{
			if (null != value)
				{
				properties.setProperty(key, value) ;
				}
			}
		}

	/**
	 * Merge the properties from another.
	 *
	 */
	public void merge(FileProperties that)
		{
		if (null != that)
			{
			Iterator iter = that.properties.entrySet().iterator() ;
			while (iter.hasNext())
				{
				Entry entry = (Entry) iter.next() ;
				//
				// Skip the reserved keys.
				// These should be set by the local system, when the data is saved.
				if (STORE_SERVICE_IDENT.equals(entry.getKey()))
					{
					continue ;
					}
				if (STORE_SERVICE_IVORN.equals(entry.getKey()))
					{
					continue ;
					}
				if (STORE_RESOURCE_IDENT.equals(entry.getKey()))
					{
					continue ;
					}
				if (STORE_RESOURCE_IVORN.equals(entry.getKey()))
					{
					continue ;
					}
				if (STORE_RESOURCE_URL.equals(entry.getKey()))
					{
					continue ;
					}
				//
				// Copy the rest of the keys.
				this.setProperty(
					(String) entry.getKey(),
					(String) entry.getValue()
					) ;
				}
			}
		}

	/**
	 * Merge the properties from an array.
	 *
	 */
	public void merge(FileProperty[] array)
		{
		if (null != array)
			{
			for (int i = 0 ; i < array.length ; i++)
				{
				//
				// Skip the reserved keys.
				if (STORE_SERVICE_IDENT.equals(array[i].getName()))
					{
					continue ;
					}
				if (STORE_SERVICE_IVORN.equals(array[i].getName()))
					{
					continue ;
					}
				if (STORE_RESOURCE_IDENT.equals(array[i].getName()))
					{
					continue ;
					}
				if (STORE_RESOURCE_IVORN.equals(array[i].getName()))
					{
					continue ;
					}
				if (STORE_RESOURCE_URL.equals(array[i].getName()))
					{
					continue ;
					}
				//
				// Copy the rest of the keys.
				this.setProperty(
					array[i].getName(),
					array[i].getValue()
					) ;
				}
			}
		}

	/**
	 * Convert our properties into an array.
	 *
	 */
	public FileProperty[] toArray()
		{
		//
		// Create an empty array.
		FileProperty[] array = new FileProperty[this.properties.size()] ;
		//
		// Transfer the values.
		Iterator iter = this.properties.entrySet().iterator() ;
		for (int i = 0 ; i < array.length ; i++)
			{
			array[i] = new FileProperty(
				(Entry) iter.next()
				) ;
			}
		return array ;
		}

	/**
	 * Load our properties from a file.
	 *
	 */
	public void load(InputStream stream)
		throws IOException
		{
		this.properties.load(
			stream
			) ;
		}

	/**
	 * Save our properties to a file.
	 *
	 */
	public void save(OutputStream stream)
		throws IOException
		{
		this.properties.store(
			stream,
			this.getClass().getName()
			) ;
		}

	/**
	 * Get the service identifier.
	 *
	 */
	public String getServiceIdent()
		{
		return getProperty(
			STORE_SERVICE_IDENT
			) ;
		}

	/**
	 * Get the (internal) resource identifier.
	 *
	 */
	public String getResourceIdent()
		{
		return getProperty(
			STORE_RESOURCE_IDENT
			) ;
		}

	/**
	 * Get the content size.
	 *
	 */
	public long getContentSize()
		{
		String string = getProperty(
			FileProperties.CONTENT_SIZE_PROPERTY
			) ;
		if (null != string)
			{
			try {
				return Long.parseLong(string) ;
				}
			catch (NumberFormatException ouch)
				{
				return -1L ;
				}
			}
		else {
			return -1L ;
			}
		}

	/**
	 * Get the content mime type.
	 *
	 */
	public String getContentType()
		{
		return getProperty(
			FileProperties.MIME_TYPE_PROPERTY
			) ;
		}

	/**
	 * Get the (external) resource ivorn.
	 * @throws FileStoreIdentifierException if the service or resource identifiers are invalid.
	 *
	 */
	public Ivorn getResourceIvorn()
		{
		try {
			return new Ivorn(
				this.getProperty(
					STORE_RESOURCE_IVORN
					)
				) ;
			}
		catch (Exception ouch)
			{
			return null ;
			}
		}

	/**
	 * Get the (external) resource URL.
	 * @throws FileStoreIdentifierException if the service or resource identifiers are invalid.
	 *
	 */
	public URL getResourceUrl()
		{
		try {
			return new URL(
				this.getProperty(
					STORE_RESOURCE_URL
					)
				) ;
			}
		catch (Exception ouch)
			{
			return null ;
			}
		}
	}

