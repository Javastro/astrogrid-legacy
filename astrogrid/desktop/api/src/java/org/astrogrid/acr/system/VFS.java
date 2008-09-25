/**
 * 
 */
package org.astrogrid.acr.system;

import java.net.URI;

/**
 * Uniform file access and management API.
 * @exclude not impelemnted 
 * @author Noel.Winstanley@manchester.ac.uk

 * @service system.vfs
 */
public interface VFS {
	String[] listProtocols();
	
	boolean exists(URI uri);
	
	void createFile(URI uri);
	
	void createFolder(URI uri);

	
	URI getParent(URI uri);
	
	String[] listChildren(URI uri);
	
	URI[] listChildURIs(URI uri);
	
	// add in search methods?
	
	
	void refresh(URI uri);
	
	void delete(URI uri);
	
	void deleteMany(URI uri, String wildcard);// good idea?
	
	URI move(URI src,URI dest); // check - should it be parent,newName instead of dest?
	
	URI copy(URI src, URI dest); // check - would I prefer parent,newName?
	
	String read(URI src);
	
	void write(URI dest,String content);
	
	byte[] readBinary(URI src);
	
	void writeBinary(URI dest,byte[] content);
	
	// readcontenturl??
	// writecontenturl...
	// protocol specific.
	
	
	
	// later?? some way to call diffferent kinds of ops.
	//String[] listAdditionalOperations(URI uri); // extension point
	
	
}
