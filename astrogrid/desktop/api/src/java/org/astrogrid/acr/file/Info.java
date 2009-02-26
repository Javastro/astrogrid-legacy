/**
 * 
 */
package org.astrogrid.acr.file;

import java.net.URI;
import java.util.Map;

import org.astrogrid.acr.ACRException;

/** Inspect metadata about URI file references.
 * 
 * This module provides methods to check whether a URI reference exists, 
 * and whether is a file, a folder, is hidden, readable or writable. For files, the size, content-type
 * and attributes may also be accessed. 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since 1.2.3
 * @see Manager
 * @see Name
 * @service file.info
 */
public interface Info {
    
    /** Determine if a URI exists.
     * 
     * @param uri the resource to test
     * @return {@code true} if this URI exists, else {@code false}
     * @throws ACRException 
     * 
     */
    boolean exists(URI uri) throws ACRException;
    /**
     * Test whether a URI exists and is a file.
     * 
     * @param uri the resource to test
     * @return {@code true} if this is an existing file.
     * @throws ACRException
     * @see #exists(URI)     
     */
    boolean isFile(URI uri)throws ACRException;
    /**
     * Tests whether a URI exists and is a folder.
     * 
     * @param uri the resource to test
     * @return {@code true} if this is an existing folder.
     * @throws ACRException
     * @see #exists(URI)
     */
    boolean isFolder(URI uri)throws ACRException;
    
    /** Determine if a URI exists and is hidden.
     * 
     * If the resource does not exist, will return {@code false}
     * @param uri the resource to test   
     * @return {@code true} if this resource is hidden  
     */
    boolean isHidden(URI uri)throws ACRException;
    /**
     * Determine if a URI exists and is readable.
     * 
     * @param uri the resurce to test
     * @return {@code true} if this resource is readable
     * @throws ACRException
     */
    boolean isReadable(URI uri)throws ACRException;
    /**
     * Determine if a URI is writable. 
     * 
     * If the resource does not yet exist, tests whether this resource could be created
     * and written to.
     * @param uri the resource to test
     * @return {@code true} if this resource is writable.
     * @throws ACRException
     */
    boolean isWritable(URI uri)throws ACRException;
    
    
    /**
     * Determine the last-modified timestamp of a resource.
     * @param uri the resource to inspect
     * @return The last-modified timestamp. 
     * This represents the number milliseconds since the standard base time known as "the epoch", namely January 1, 1970, 00:00:00 GMT.
     * @throws ACRException if the resource does not exist, or time cannot be read.
     */
    long getLastModifiedTime(URI uri) throws ACRException;
    // later - setModifiedTime??
    /**
     * Inspect the attributes of this resource.
     * 
     * 
     * Returns a dictionary of attributes. The particular entries in the dictionary
     * will vary according to the kind of  filesystem the resource belongs to. Dictionary
     * keys will be strings, as most commonly will be the associated values.
     * @note 
     * For a Myspace file, two attributes which will always be present are :
     * <dl>
     *  <dt>{@code ContentURL}</dt>
     *  <dd>A URL (typically HTTP or FTP) from which the contents of this file can be directly read</dd>
     *  <dt>{@code ContentMethod}</dt>
     *  <dd>The method (for example GET) to use to access the {@code ContentURL}</dd>       
     * </dl>
     * @param uri the resource to inspect
     * @return read-only map of this resource's attributes.
     * @throws ACRException If the resource does not exist, or does not support attributes
     */
    Map getAttributes(URI uri)throws ACRException;
    
    // later - set / remove attributes.
    
    /**
     * Determines the size of a file, in bytes.
     * 
     * @param uri the resource to inspect.
     * @return the size of the file, in bytes
     * @throws ACRException If the file does not exist, or is a folder, or is being written to
     * , or on error determining the size.
     */
    long getSize(URI uri)throws ACRException;
    
    /** Determine the content type of a file.
     * 
     * Note that content type <i>may</i> be deduced by file-suffix - 
     * so results might differ from actual contents, especially for
     * empty / non-existent files.
     * 
     * @param uri the file to test
     * @return the content type of the file, or null if no type. Some typical values are:
     * {@code image/jpeg}, {@code text/plain}, {@code application/xml}, {@code application/x-votable+xml}
     * @throws ACRException on  error.
     */
    String getContentType(URI uri)throws ACRException;
    
    //String getContentEncoding(URI uri) throws ACRException;
}
