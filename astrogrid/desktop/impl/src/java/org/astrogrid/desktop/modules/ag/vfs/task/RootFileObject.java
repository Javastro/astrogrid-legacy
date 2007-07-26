/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs.task;

import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;

import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;
import org.astrogrid.desktop.modules.ag.vfs.task.RPParser.RootFileName;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 23, 20076:55:21 PM
 */
public class RootFileObject extends RPFileObject {

    /**
     * @param rootFileName
     * @param sys
     */
    public RootFileObject(RootFileName rootFileName,
            RPFileSystem sys) {
        super(rootFileName,sys);
    }
    
    protected String[] doListChildren() throws Exception {
        URI[] ids = processFileSystem.getRpmi().list();
        String[] children = new String[ids.length];
        for (int i = 0; i < ids.length; i++) {
            children[i]= URLEncoder.encode(ids[i].toString());
        }
        return children;
    }

}
