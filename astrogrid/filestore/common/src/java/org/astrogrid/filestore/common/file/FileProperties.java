/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/file/FileProperties.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/21 18:11:55 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: FileProperties.java,v $
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

import java.io.IOException ;
import java.io.InputStream ;
import java.io.OutputStream ;

import java.util.Map ;
import java.util.Map.Entry ;
import java.util.Iterator ;
import java.util.Properties ;
import java.util.Enumeration ;

/**
 * A wrapper for a set of file properties.
 *
 */
public class FileProperties
	{
	/**
	 * The property key for the store service identifier.
	 *
	 */
	public static final String STORE_SERVICE_IDENTIFIER  = "org.astrogrid.filestore.service" ;

	/**
	 * The property key for the internal identifier.
	 *
	 */
	public static final String STORE_INTERNAL_IDENTIFIER  = "org.astrogrid.filestore.internal" ;

	/**
	 * The property key for the transfer source URL.
	 *
	 */
	public static final String TRANSFER_SOURCE_PROPERTY  = "org.astrogrid.filestore.transfer.source" ;

	/**
	 * The property key for content type .
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
	public static final String MIME_TYPE_TEXT = "text/raw"  ;
	public static final String MIME_TYPE_XML  = "text/xml"  ;
	public static final String MIME_TYPE_HTML = "text/html" ;

	/**
	 * The property key for IVOA type.
	 *
	 */
	public static final String IVOA_TYPE_PROPERTY  = "ivoa.type" ;

	/**
	 * Known IVOA type values.
	 *
	 */
	public static final String IVOA_TYPE_VOLIST   = "ivo.volist"  ;
	public static final String IVOA_TYPE_VOTABLE  = "ivo.votable" ;

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
				if (STORE_SERVICE_IDENTIFIER.equals(entry.getKey()))
					{
					continue ;
					}
				if (STORE_INTERNAL_IDENTIFIER.equals(entry.getKey()))
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
				if (STORE_SERVICE_IDENTIFIER.equals(array[i].getName()))
					{
					continue ;
					}
				if (STORE_INTERNAL_IDENTIFIER.equals(array[i].getName()))
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
	}

