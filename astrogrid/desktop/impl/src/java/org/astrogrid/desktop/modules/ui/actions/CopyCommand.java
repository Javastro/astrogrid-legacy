package org.astrogrid.desktop.modules.ui.actions;

import java.io.File;
import java.net.URI;
import java.net.URL;

import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectView;

/**A single copy command to the bulk copy worker.
 * this base class represents 'copy the source to the destination folder'
 * @see BulkCopyWorker
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 17, 20082:51:21 PM
 */
public class CopyCommand {
    
    private final Object src;
    private FileName destination;
    private Throwable exception;
    /** 
     * resolve the source into a file object.
     * @return 
     * @throws FileSystemException 
     */
    final FileObject resolveSourceFileObject(final FileSystemManager vfs) throws FileSystemException {
        if (src instanceof FileObject) {
            return (FileObject)src;
        } else if (src instanceof FileObjectView) {
            return ((FileObjectView)src).getFileObject();
        } else {
            final String uriString = src.toString();
            return vfs.resolveFile(uriString);
        }           
    }
    /** construct a command, using a file object as the source */
    public CopyCommand(final FileObjectView src) {
        super();
        this.src = src;
    }
       
    
    /** construct a command, using a file as the source */
    public CopyCommand(final File src) {
        super();
        this.src = src;
    }        
    /** construct a command, using a URI as the source */
    public CopyCommand(final URI src) {
        super();
        this.src = src;
    }
    /** construct a command, using a URL as the source */
    public CopyCommand(final URL src) {
        super();
        this.src = src;
    }
    
    /** format the source as a string
     * - gives variable results depending on what was passed in,
     * but is a private method only used in reporting - so no worry */
    private String srcToString() {
        if (src instanceof FileObject) {
            return ((FileObject)src).getName().getBaseName();
        } else {
            return src.toString();
        }
    }
    
    /** produce a formatted report of the result of this command */
    public final String formatResult() {
        if (_failed) {
            return srcToString() + "<br>" + ExceptionFormatter.formatException(exception,ExceptionFormatter.ALL);           
        } else if (destination == null) {
            return srcToString() + "<br> not copied";
        } else {
            return srcToString() + "<br> copied to <br>" + destination.getURI();
        }
    }
    
    /** access the destination this copy command saved the file to */
    public final FileName getDestination() {
        return this.destination;
    }
    /** called by the copy worker to record where the file contents were written to
     * @param name the destination the file waws written to
     */
    final void recordSuccess(final FileName name) {
        destination = name;
    }
    /** called by the copy worker to record an exception when writing the file contents
     * @param x the exception encountered
     */
    final void recordError(final FileSystemException x) {
        _failed = true;
        exception = x;
    }
    private boolean _failed = false;
    /** test whether this command failed
     * @return
     */
    public final boolean failed() {
        return _failed;
    }
}