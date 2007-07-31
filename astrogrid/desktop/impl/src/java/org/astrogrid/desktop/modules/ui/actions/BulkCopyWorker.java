package org.astrogrid.desktop.modules.ui.actions;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.Selectors;
import org.astrogrid.desktop.modules.dialogs.ResultDialog;
import org.astrogrid.desktop.modules.ivoa.resource.HtmlBuilder;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;

/** worker class which does a bulk copy of a bunch of files/folders to another target.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 25, 20074:49:51 PM
 */
public final class BulkCopyWorker extends BackgroundWorker {
    /**
     * 
     */
    private final File saveDir;
    /**
     * 
     */
    private final List l;
    private final FileSystemManager vfs;
    private final FileObject saveObject;

    /**
     * @param parent ui parent
     * @param saveDir directory to copy data to.
     * @param l list of file objects to copy.
     */
    public BulkCopyWorker(FileSystemManager vfs,UIComponent parent,File saveDir, List l) {
        super(parent,  "Copying to " + saveDir);
        this.vfs = vfs;
        this.saveDir = saveDir;
        saveObject = null;
        this.l = l;
    }
    
    public BulkCopyWorker(FileSystemManager vfs,UIComponent parent,FileObject saveObject, List l) {
        super(parent,  "Copying to " + saveObject.getName().getPath());
        this.vfs = vfs;
        this.saveObject = saveObject;
        this.saveDir = null;
        this.l = l;
    }

    protected Object construct() throws Exception {
        FileObject target;
        if (saveObject == null) {
            target = vfs.resolveFile(this.saveDir.toURI().toString());
        } else {
            target = saveObject;
        }
        if (! target.exists()) {
            target.createFolder();
        }
        if (! target.isWriteable()) {
            throw new Exception("Not permitted to write to " + target.getName());
        }
        // will records errors copy indivitual files, continue, and report at the end.
        Map errors = new HashMap();
        // go through each file in turn.
        for (Iterator i = this.l.iterator(); i.hasNext(); ) {
                FileObject src = (FileObject)i.next();
                try {
                String name = StringUtils.substringBeforeLast(src.getName().getBaseName(),".");
                String ext = StringUtils.substringAfterLast(src.getName().getBaseName(),".");
                if (StringUtils.isNotBlank(ext)) {
                    ext = "." + ext;
                }
                FileObject dest = null;
                int n = 0;
                // create a target filename - and if it already exists, change it by adding a number.
                do {
                    dest = target.resolveFile(name + (n == 0 ? "" : "-" + n ) +  ext);
                    n++;
                } while  (dest.exists());
                
                // cool. now got a non-existent destination file.
                dest.copyFrom(src, Selectors.SELECT_ALL); // creates and copies from src. handles folders and files. nice!
            } catch (FileSystemException x) {
                errors.put(src,x);
            }
        }
        return errors;
    }

    protected void doFinished(Object result) {
        Map errors = (Map)result;
        if (errors.size() ==0) {
            return;
        }
        HtmlBuilder msgBuilder = new HtmlBuilder();			    
        msgBuilder.h2("Encountered errors when copying some files");
        for (Iterator i = errors.entrySet().iterator(); i.hasNext();) {
            Map.Entry err = (Map.Entry) i.next();
            FileObject f = (FileObject)err.getKey();
            Throwable e = (Throwable)err.getValue();
            msgBuilder.append(f.getName().getPath()).append(":<ul>");
            do {
                msgBuilder.append("<li>").append(e.getMessage());
                e = e.getCause();
            } while (e != null);
            msgBuilder.append("</ul>");
        }
        ResultDialog rd = new ResultDialog(parent.getFrame(),msgBuilder);
        rd.show();
    }
}