/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs.plastic;

import java.io.File;
import java.util.Collection;

import org.apache.commons.vfs.Capability;
import org.apache.commons.vfs.FileListener;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSelector;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.provider.AbstractFileSystem;
import org.apache.commons.vfs.provider.AbstractOriginatingFileProvider;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

/** implementaiton of a plastic filesystem.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Aug 7, 20075:19:52 PM
 */
public class PlasticFileSystem extends AbstractFileSystem implements ListEventListener {


    private final EventList plasticList;

    /**
     * @param rootName
     * @param fileSystemOptions
     * @param plasticList
     */
    public PlasticFileSystem(FileName rootName,
            FileSystemOptions fileSystemOptions, EventList plasticList) {
        super(rootName,null,fileSystemOptions);
        this.plasticList = plasticList;
        plasticList.addListEventListener(this);
    }

    protected void addCapabilities(Collection caps) {
        caps.addAll(PlasticProvider.caps);
    }

    protected FileObject createFile(FileName name) throws Exception {
        return new PlasticFileObject(name,this);
    }

    // listen to changes in the list.
    public void listChanged(ListEvent arg0) {
        while (arg0.hasNext()) {
            arg0.next();
            
            switch (arg0.getType()) {
                case ListEvent.DELETE:
                case ListEvent.INSERT:
                case ListEvent.UPDATE:
            }
        }
    }




}
