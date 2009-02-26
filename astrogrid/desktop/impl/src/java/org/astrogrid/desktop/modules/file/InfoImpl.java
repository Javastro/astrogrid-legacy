/**
 * 
 */
package org.astrogrid.desktop.modules.file;

import java.net.URI;
import java.util.Map;

import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.file.Info;

/** Implementation of info component.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Dec 30, 20086:56:00 PM
 */
public class InfoImpl extends AbstractFileComponent implements Info {

    
    /**
     * @param vfs
     */
    public InfoImpl(final FileSystemManager vfs) {
        super(vfs);
    }

    public boolean exists(final URI arg0) throws ACRException {
        try {
            return fo(arg0).exists();
        } catch (final FileSystemException x) {
            throw new ACRException(x.getMessage());
        }
    }

    public Map getAttributes(final URI arg0) throws ACRException {
        try {
            return fo(arg0).getContent().getAttributes();
        } catch (final FileSystemException x) {
            throw new ACRException(x.getMessage());
        }
    }

    public String getContentType(final URI arg0) throws ACRException {       
        try {
            return fo(arg0).getContent().getContentInfo().getContentType();
        } catch (final FileSystemException x) {
            throw new ACRException(x.getMessage());
        }
    }

    public long getLastModifiedTime(final URI arg0) throws ACRException {
        try {
            return fo(arg0).getContent().getLastModifiedTime();
        } catch (final FileSystemException x) {
            throw new ACRException(x.getMessage());
        }
    }

    public long getSize(final URI arg0) throws ACRException {
        try {
            return fo(arg0).getContent().getSize();
        } catch (final FileSystemException x) {
            throw new ACRException(x.getMessage());
        }
    }

    public boolean isFile(final URI arg0) throws ACRException {
        try {
            return fo(arg0).getType().hasContent();
        } catch (final FileSystemException x) {
            throw new ACRException(x.getMessage());
        }
    }

    public boolean isFolder(final URI arg0) throws ACRException {
        try {
            return fo(arg0).getType().hasChildren();
        } catch (final FileSystemException x) {
            throw new ACRException(x.getMessage());
        }
    }

    public boolean isHidden(final URI arg0) throws ACRException {
        try {
            return fo(arg0).isHidden();
        } catch (final FileSystemException x) {
            throw new ACRException(x.getMessage());
        }
    }

    public boolean isReadable(final URI arg0) throws ACRException {
        try {
            return fo(arg0).isReadable();
        } catch (final FileSystemException x) {
            throw new ACRException(x.getMessage());
        }
    }

    public boolean isWritable(final URI arg0) throws ACRException {
        try {
            return fo(arg0).isWriteable();
        } catch (final FileSystemException x) {
            throw new ACRException(x.getMessage());
        }
    }

}
