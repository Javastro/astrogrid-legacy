/**
 * 
 */
package org.astrogrid.acr.file;

import java.net.URI;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;

/** Construct and manipulate URI file references.
 *
 * For two URI references ({@code u1},{@code u2}) to be equal, the following
 * must be true:
 * {@source getRoot(u1) == getRoot(u2) && getPath(u1) == getRoot(u2)} 
 * @since 1.2.3
 * @see Manager
 * @see Info
 * @service file.name
 * */
public interface Name {
    
    /**  Returns the scheme component of a URI.
     * 
     * {@example getScheme( "http://server.org/index.html" ) == "http"}
     * @param uri the URI
     * @return the scheme component, or null if not defined 
     */
    String getScheme(URI uri) throws ACRException;
    
    /**
     * Return the root of the filesystem that a URI belongs to.
     * {@example
     * getRoot( "ftp://server.com/a/b/file.txt" ) == "ftp://server.com/"
     * }
     * @param uri the URI
     * @return the URI of the filsystem root.
     */
    URI getRoot(URI uri)  throws ACRException;
    
    /**
     * Return the name of a URI. This is just the
     * last name in the path sequence. The root file of a file system has an empty name.
     * 
     * {@example 
     * getName( "ftp://server.com/a/b/file.txt ") == "file.txt"
     * getName( "ftp://server.com/a/b/" )         == "b" 
     * getName( "ftp://server.com/" )             == ""                 
     * }
     * @param uri the uri to extract a name from
     * @return The name. Never returns null.
     * 
     */
    String getName(URI uri)  throws ACRException;

    /**
     * Returns the file extension of a URI.
     * {@example 
     * getExtension( "ftp://server.com/a/b/file.txt" ) == "txt"
     * getExtension( "ftp://server.com/a/b/" )         == "" 
     * getExtension( "ftp://server.com/" )             == ""                 
     * }     
     * @param uri the uri to extract an extension from
     * @return the extension of the file name, or null.
     */
    String getExtension(URI uri)  throws ACRException;
     
    /**
     * Returns the absolute path of a URI. 
     * This path is normalised, so that {@code .} and {@code ..} elements 
     * have been removed. The path only contains {@code /} as its 
     * separator character and always starts with {@code /}
     * The root of a file system has {@code /} as its absolute path.
     * {@example
     * getPath( "ftp://server.com/a/b/file.txt" )      == "/a/b/file.txt"
     * getPath( "ftp://server.com/a/b/" )              == "/a/b" 
     * getPath( "ftp://server.com/" )                  == "/"    
     * getPath( "ftp://server.com/a/b/.././file.txt" ) == "/a/file.txt"  
     * } 
     * @param uri the uri to extract the path from.
     * @return the path, never null.
     */
    String getPath(URI uri)  throws ACRException;
    
    /**
     * Returns the URI of the parent (enclosing folder) of a URI. 
     * The root of a file system has no parent.
     * {@example
     * getParent( "ftp://server.com/a/b/file.txt" ) == "ftp://server.com/a/b/"
     * getParent( "ftp://server.com/a/b/" )          == "ftp://server.com/a/" 
     * }      
     * @param uri
     * @return the parent of {@code uri}
     * @throws InvalidArgumentException if {@code uri} is the root of the filesystem
     */
    URI getParent(URI uri)  throws ACRException;
    

        
    /** Relativize a URI against a base URI.
     * {@example
     * relativize( "ftp://server.com/a/b", "ftp://server.com/a/b/file.txt" ) == "file.txt"     
     * relativize( "ftp://server.com/", "ftp://server.com/a/b/file.txt" )    == "a/b/file.txt"     
     * relativize( "ftp://server.com/a/b/file.txt", "ftp://server.com/a/b" ) == ".."     
     * relativize( "ftp://server.com/a/b/file.txt", "ftp://server.com/" )    == "../../.."     
     * relativize( "ftp://server.com/a/b", "ftp://server.com/a/b )           == "."             
     * }
     * @param base the base uri to relativize against
     * @param uri the uri to relativize 
     * @return the relative path
     * @equivalence relativize( base, resolve( base, path ) ) == path
     */
    String relativize(URI base, URI uri)  throws ACRException;
    
    /** Resolve a path relative to a base URI.
     * {@example
     * resolve( "ftp://server.com", "a/b/file.txt" )  == "ftp://server.com/a/b/file.txt"
     * resolve( "ftp://server.com/a/b", ".." )        == "ftp://server.com/a"
     * resolve( "ftp://server.com/a/b", "file.txt" )  == "ftp://server.com/a/b/file.txt"
     * resolve( "ftp://server.com/a/b", "/file.txt" ) == "ftp://server.com/file.txt"
     * }
     * @param base the uri to resolve against
     * @param path the path to resolve
     * @return the resulting resolved URI
     * @equivalence resolve( base, relativize( base, url ) ) == url
     */
    URI resolve(URI base, String path) throws ACRException;
        
    /**
     * Test whether one URI is an ancestor of another.
     * {@example
     * isAncestor( "ftp://server.com/a/b/file.txt", "ftp://server.com" )              == True
     * isAncestor( "ftp://server.com/a/b/file.txt", "ftp://server.com/a" )            == True
     * isAncestor( "ftp://server.com/a/b", "ftp://server.com/a/b/file.txt" )          == False                
     * isAncestor( "ftp://server.com/a/b/file.txt", "ftp://server.com/a/b/file.txt" ) == False                
     * isAncestor( "ftp://server.com", "ftp://server.com" )                           == False
     * }
     * @param u a uri
     * @param suspectedAncestor URI to test whether it ancestor of {@code u}
     * @return boolean value
     */
    boolean isAncestor(URI u,URI suspectedAncestor)  throws ACRException;
  
    
}