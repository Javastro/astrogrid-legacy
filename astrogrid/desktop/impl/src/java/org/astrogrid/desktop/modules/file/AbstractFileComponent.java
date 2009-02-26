/**
 * 
 */
package org.astrogrid.desktop.modules.file;

import java.net.URI;

import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;

/** Abstract class for the various components in the 'file' module.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Dec 30, 20086:57:49 PM
 */
public class AbstractFileComponent {

    /**
     * @param vfs
     */
    public AbstractFileComponent(final FileSystemManager vfs) {
        super();
        this.vfs = vfs;
    }

    protected final FileSystemManager vfs;

    
    protected final void nullCheck(final URI u) throws InvalidArgumentException {
        if (u == null) {
            throw new InvalidArgumentException("Null URI provided");
        }        
    }
    
    protected String mkString(final URI a) throws InvalidArgumentException {
        nullCheck(a);
        return a.toString();
    }
    
    /** resolve uri to file object.
     * 
     * @param u the uri
     * @return the file object. never null
     * @throws ACRException
     */
    protected FileObject fo(final URI u) throws ACRException {
        final String s = mkString(u);
        try {
            return vfs.resolveFile(s);
        } catch (final FileSystemException x) {
            throw new ACRException(x.getMessage());
        }
    }
    /** resolve uri to file name
     * 
     * @param u the uri
     * @return the file object. never null
     * @throws ACRException
     */
    protected FileName fn(final URI u) throws ACRException {
        final String s = mkString(u);
        try {
            return vfs.resolveURI(s);
        } catch (final FileSystemException x) {
            throw new ACRException(x.getMessage());
        }
    }    
}
