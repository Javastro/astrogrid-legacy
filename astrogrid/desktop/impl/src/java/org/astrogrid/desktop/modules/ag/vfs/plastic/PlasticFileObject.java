/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs.plastic;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.provider.AbstractFileObject;

/** abandoned - too hard for now.
 * stumbling block was how to send a plastic message from 'dogetooutputStream'
 * when really I wanted to detect filecreation (or whatever it is that happens
 * when an existing file is dragged into a drop-box).
 * Will revisit this later.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Aug 7, 20075:36:28 PM
 */
public class PlasticFileObject extends AbstractFileObject implements FileObject {

    private final PlasticFileSystem pfs;

    /**
     * @param name
     * @param plasticFileSystem
     */
    public PlasticFileObject(FileName name, PlasticFileSystem plasticFileSystem) {
        super(name,plasticFileSystem);
        this.pfs = plasticFileSystem;
    }

    protected long doGetContentSize() throws Exception {
        throw new FileSystemException("Not supported.");
    }

    protected InputStream doGetInputStream() throws Exception {
        throw new FileSystemException("Not supported");
    }

    protected FileType doGetType() throws Exception {
        return null; // todo
    }

    protected String[] doListChildren() throws Exception {
        return null; // todo
    }
    
    protected boolean doIsReadable() throws Exception {
        return getType().hasChildren(); // only folders are readable.
    }
    
    protected OutputStream doGetOutputStream(boolean append) throws Exception {
        return null; //todo
    }

}
