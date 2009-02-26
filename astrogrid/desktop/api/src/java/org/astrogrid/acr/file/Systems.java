/**
 * 
 */
package org.astrogrid.acr.file;

/** Access information about the supported kinds of filesystem.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since 1.2.3
 * @service file.systems
 * @see Manager
 */
public interface Systems {
    /** List the URI schemes of the kinds of filesystem that Astro Runtime is able to access.
     * 
     * This function can be used to trivially verify that input URIs will be readable by AR.
     * 
     * @return a non-null array of URI schemes - expect to contain at least {@code file}
     * ,{@code http}, {@code ftp}, {@code sftp}, {@code ivo}, {@code vos}. */    
    String[] listSchemes(); 
}
