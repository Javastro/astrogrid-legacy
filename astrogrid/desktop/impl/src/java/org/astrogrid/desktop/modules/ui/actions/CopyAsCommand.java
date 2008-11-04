package org.astrogrid.desktop.modules.ui.actions;

import java.io.File;
import java.net.URI;
import java.net.URL;

import org.apache.commons.vfs.FileObject;

/** A 'copy as' command to the Bulk Copy Woeker.
 * The source plus a target filename are specified.
 * target filename is only a recommendation, and is only the filename, not the path.
 * @see BulkCopyWorker
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 17, 20082:56:18 PM
 */
public class CopyAsCommand extends CopyCommand {

    public CopyAsCommand(final File src, final String target) {
        super(src);
        this.targetFilename = target;
    }

    public CopyAsCommand(final FileObject src, final String target) {
        super(src);
        this.targetFilename = target;
    }

    public CopyAsCommand(final URI src, final String target) {
        super(src);
        this.targetFilename = target;
    }

    public CopyAsCommand(final URL src, final String target) {
        super(src);
        this.targetFilename = target;
    }

    protected String targetFilename;

    public final String getTargetFilename() {
        return this.targetFilename;
    }
    
}