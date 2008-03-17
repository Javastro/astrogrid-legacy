package org.astrogrid.desktop.modules.ui.actions;

import java.io.File;
import java.net.URI;
import java.net.URL;

import org.apache.commons.vfs.FileObject;

/** represents a 'copy as' command, where the source plus a target filename are specified.
 * target filename - is only a recommendation, and is only the filename, not the path.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 17, 20082:56:18 PM
 */
public class CopyAsCommand extends CopyCommand {

    public CopyAsCommand(File src, String target) {
        super(src);
        this.targetFilename = target;
    }

    public CopyAsCommand(FileObject src, String target) {
        super(src);
        this.targetFilename = target;
    }

    public CopyAsCommand(URI src, String target) {
        super(src);
        this.targetFilename = target;
    }

    public CopyAsCommand(URL src, String target) {
        super(src);
        this.targetFilename = target;
    }

    protected String targetFilename;

    public final String getTargetFilename() {
        return this.targetFilename;
    }
    
}