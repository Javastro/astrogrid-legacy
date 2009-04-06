/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.util.Date;

import javax.swing.Icon;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.provider.DelegateFileObject;
import org.astrogrid.desktop.modules.system.ProgrammerError;
import org.astrogrid.desktop.modules.ui.scope.AstroscopeFileObject;

/** EDT-safe view onto a file object.
 * intention is that this object is computed OFF the EDT, and then it's fields can be 
 * efficiently accessed on the EDT.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 31, 20094:15:27 PM
 */
public final  class FileObjectView {

    
    private final FileObject fo;
    private final FileType type;
    private final boolean exists;  
    private final boolean hidden;
    private final boolean writable;
    /**
     * @return the writable
     */
    public boolean isWritable() {
        return this.writable;
    }
    private final String basename;
    private final Icon icon;
    private final Date lastModified;
    private final long size;
    private final String contentType;
    private final AstroscopeFileObject astroscopeFileObject;
    private final String uri;
    private final String scheme;
    private final boolean delegate;
    private final String innermostScheme;
    /**
     * @return the innermostScheme
     */
    public String getInnermostScheme() {
        return this.innermostScheme;
    }
    /** True if the file object this views upon is a delegate (i.e. a astrscope file object, or some other kind of delegate).
     * @return the delegate
     */
    public boolean isDelegate() {
        return this.delegate;
    }
    /**return the wrapped astroscopefileobject, if one exists, somewhere down the delegation chain.
     * Implies that isDelegate() == true, however, isDelegate() == true doesn't imply that there is an astroscope file object present. 
     * This method is safe to call on EDT, as an astroscope file object doesn't call the external file system to provide file metadata.
     * @return the astroscopeFileObject
     */
    public AstroscopeFileObject getAstroscopeFileObject() {
        return this.astroscopeFileObject;
    }
    /**
     * @return the contentType
     */
    public String getContentType() {
        return this.contentType;
    }
    /**
     * @return the icon
     */
    public Icon getIcon() {
        return this.icon;
    }
    /**
     * @return the hidden
     */
    public boolean isHidden() {
        return this.hidden;
    }
    /**
     * @return the basename
     */
    public String getBasename() {
        return this.basename;
    }
    /**
     * @return the file object
     * @throws ProgrammerError if called on the EDT
     */
    public FileObject getFileObject() {
        if (SwingUtilities.isEventDispatchThread()) {
            throw new ProgrammerError("Oh no you don't! Not releasing a file object back onto the EDT");
        }
        return this.fo;
    }
    /**
     * @return the type
     */
    public FileType getType() {
        return this.type;
    }
    /**
     * @return the exists
     */
    public boolean isExists() {
        return this.exists;
    }
    /**
     * @return the lastModified
     */
    public Date getLastModified() {
        return this.lastModified;
    }
    /**
     * @return the size
     */
    public long getSize() {
        return this.size;
    }

    
    /** construct a fileObjectView
     * 
     * @param fo the file object to provide a view around
     * @throws FileSystemException if can't pre-compute all.
     * @throws ProgrammerError if constructed on EDT
     */
    public FileObjectView(final FileObject fo, final IconFinder ifinder) throws FileSystemException {
        // lots of null handling in here, so it can be used in unit testing.
        this.fo = fo;
        if (SwingUtilities.isEventDispatchThread()) {
            throw new ProgrammerError("Constructing FileObjectView on EDT: defeats the purpose");
        }
        // precompute all the fields.
        this.type = fo.getType();
        this.exists = fo.exists();
        this.hidden = fo.isHidden();
        this.writable = fo.isWriteable();
        this.icon = ifinder != null ? ifinder.find(fo)  : null;

        { // filename
            final FileName name = fo.getName();
            this.basename = name != null ? name.getBaseName() : null;
            this.uri = name != null ? name.getURI() : null;
            this.scheme = name != null ? name.getScheme(): null;        
        }
        
        final FileContent content = fo.getContent();
        { // last modified
            Date lmd = null;
            try {
                lmd = content != null ?  new Date(content.getLastModifiedTime()) : null;
            } catch (final FileSystemException e) { // will throw when it's a virtual file system, but doesn't seem to be a way of detecting this
                // no matter.
            }
            lastModified = lmd;
        }
        
        if (type != null && type.hasContent()) {// size and content type.
            size = content.getSize();
            String cType =  content.getContentInfo().getContentType();
            FileObject x = fo; // starts off as current.
            while (StringUtils.isEmpty(cType) && AstroscopeFileObject.isOnlyDelegateFileObject(x)) {
                x = ((DelegateFileObject)x).getDelegateFile();
                cType = x.getContent().getContentInfo().getContentType();
            }
            contentType = cType;
        } else {
            contentType = null;
            size = 0;
        }
        // if the file object wraps an Astroscope file object, provide access to this.
        astroscopeFileObject = AstroscopeFileObject.findAstroscopeFileObject(fo);
        delegate = astroscopeFileObject != null || fo instanceof DelegateFileObject;
        { // innermost.
            final FileObject ifo = AstroscopeFileObject.findInnermostFileObject(fo);
            if (ifo != null && ifo.getName() != null) {
                innermostScheme = ifo.getName().getScheme();
            } else {
                innermostScheme = null;
            }
        }
        
    }
    /**
     * @return the scheme
     */
    public String getScheme() {
        return this.scheme;
    }
    /**
     * @return the uri
     */
    public String getUri() {
        return this.uri;
    }
    
    @Override
    public String toString() {
        return this.fo.toString();
    }
    
    
    
    
}
