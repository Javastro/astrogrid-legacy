/**
 * 
 */
package org.astrogrid.acr.file;

import java.net.URI;

import org.astrogrid.acr.ACRException;

/**
 * Read, Write, and Navigate Filesystems.
 * @author Noel.Winstanley@manchester.ac.uk

 * @service file.manager
 * @see Name
 * @see Info 
 * @see Systems
 * @since 1.2.3 
 */
public interface Manager {

	/** Create a file, if it does not exist. 
	 * 
	 * Also creates any ancestor folders which do not exist.
    This method does nothing if the file already exists and is a file.
	 * @param uri the filepath to create
	 * @throws ACRException If the file already exists with the wrong type, 
	 * or the parent folder is read-only, 
	 * or on error creating this file or one of its ancestors.
    */
	void createFile(URI uri) throws ACRException;
	
	/** Create a folder, if it does not exist.
	 *  
	 * Also creates any ancestor folders which do not exist. 
	 * Does nothing if the folder already exists. 
	 * @param uri the folderpath to create
	 * @throws ACRException If the folder already exists with the wrong type, 
	 * or the parent folder is read-only, 
	 * or on error creating this folder or one of its ancestors. */	
	void createFolder(URI uri) throws ACRException;
	
	/** Resynchronize the information about a file with the remote filesystem.
	 * 
	 * May be necessary in cases when another client is writing to this file */
	void refresh(URI uri) throws ACRException; 
	
	/**
	 * Delete a file and all it's descendants.
	 * 
	 * Does nothing if this file does not exist.
	 * @param uri filepath to delete
	 * @return true if the file was deleted.
	 */
	boolean delete(URI uri)throws ACRException;

	/**
	 * List the names of the children of a folder.
	 * @param uri the folder to list
	 * @return An array containing the names of the children of this folder. 
	 * The array is unordered. 
	 * If the file does not have any children, a zero-length array is returned. This method never returns null.
	 * @throws ACRException If this file does not exist, or is not a folder, 
	 * or on error listing this file's children.
	 */
	String[] listChildren(URI uri) throws ACRException;
	
    /**
     * List the URI of the children of a folder.
     * @param uri the folder to list
     * @return An array containing the URIs of the children of this folder. 
     * The array is unordered. 
     * If the file does not have any children, a zero-length array is returned. This method never returns null.
     * @throws ACRException If this file does not exist, or is not a folder, 
     * or on error listing this file's children.
     */
	URI[] listChildUris(URI uri) throws ACRException;
	

	// Further navigation / search methods:	
	//boolean hasChild(URI uri, String s); // maybe it returns a URI?
	// maybe findCHild?
	
	// search methods?
	//findFiles(URI, wildcard-pattern-or-constant
	// resolve(URI, page)


	/** Read the contents of a file.
	 * @param src the file to read from
	 * @return the contents of the file. An empty file will return an empty string, never NULL
	 * @throws ACRException if the file is not readable, or a folder, or an error occurs when reading.
	 * 
	 */
    String read(URI src) throws ACRException;
    
    /** Write to a file.
     * 
     * @param dest the file to write to
     * @param content the contents to write to the file.
     * @throws ACRException if the file is read-only, or a folder, or an error occurs when writing.
     */
    void write(URI dest,String content) throws ACRException;
    
    // should we have a readLine, appendLine too??
    
    /** Read the contents of a file as binary data.
     * @param src the file to read from
     * @return the contents of the file. Never NULL - an empty file will produce a zero-length array.
     * @throws ACRException if the file is not readable, or a folder, or an error occurs when reading.

     */
    byte[] readBinary(URI src) throws ACRException;
    
    /** Write binary data to a file.
     * 
     * @param dest the file to write to
     * @param content the contents to write to the file.
     * @throws ACRException if the file is read-only, or a folder, or an error occurs when writing.
     */    
    void writeBinary(URI dest,byte[] content) throws ACRException;
  
	
    /** Append to a file.
     * 
     * @param dest the file to write to
     * @param content the contents to append to the file.
     * @throws ACRException if the file is read-only, or a folder, or an error occurs when writing.
     */
    void append(URI dest,String content) throws ACRException;
    
    /** Append binary data to a file.
     * 
     * @param dest the file to write to
     * @param content the contents to append to the file.
     * @throws ACRException if the file is read-only, or a folder, or an error occurs when writing.
     */    
    void appendBinary(URI dest,byte[] content) throws ACRException;
      
    /**
     * Copy a file, and all it's descendants, to another location.
     * @param src the source file / folder
     * @param dest the destination to copy to. The destination may be on a different 
     * filesystem to {@code src}
     * @throws ACRException
     */
    void copy(URI src, URI dest) throws ACRException;
    
    /**
     * Move a file (and all it's descendants) to another location.
     * @param src the file to move
     * @param dest the destination to move to. This may be on a different filesystem to {@code src}
     * @throws ACRException
     */
    void move(URI src, URI dest) throws ACRException;
//   put in a separate bulk api. 
//    /** Copy a number of files (and their descendants) into a folder.
//     * 
//     * @param srcs a map describing the files to copy. Each element of the map
//     * consists of: a URI to copy from; and a String which is the target filename.
//     * If a file with that name already exists in the folder, a unique suffix will
//     * be added to the filename.
//     * The string may be null or empty, in which case a suitable filename will be
//     * deduced from the URI.
//     * @param destFolder the folder to copy all the sources to.
//     * @throws ACRException if destFolder is a file
//     *
//     */
//    void copyTo(Map<URI,String> srcs, URI destFolder) throws ACRException;

// don't think this one is that useful
// doesnt' commonly occur, and can do same with copyTo followed by delete.    
//    /** Move a number of files  into a folder
//     * 
//     * @param srcs an array of source files to move.
//     * @param dest
//     * @throws ACRException if destFolder is a file
//     *
//     */    
//    void moveTo(URI[] srcs, URI destFolder) throws ACRException;    
    
}
