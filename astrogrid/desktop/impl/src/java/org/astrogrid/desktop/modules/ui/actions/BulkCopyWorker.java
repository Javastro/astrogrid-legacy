package org.astrogrid.desktop.modules.ui.actions;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.Selectors;
import org.apache.commons.vfs.provider.AbstractFileSystem;
import org.apache.commons.vfs.provider.url.UrlFileObject;
import org.apache.commons.vfs.provider.url.UrlFileSystem;
import org.astrogrid.desktop.modules.dialogs.ResultDialog;
import org.astrogrid.desktop.modules.ivoa.resource.HtmlBuilder;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;

/** worker class which does a bulk copy of a bunch of files/folders to another target.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 25, 20074:49:51 PM
 */
public class BulkCopyWorker extends BackgroundWorker {
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
    private final URI saveLoc;

    /**
     * @param parent ui parent
     * @param saveDir directory to copy data to.
     * @param l list of files to copy. Contents can be mixture of fileObjects, things with a toString that can be resolved by 
     * vfs - so URI, URL, String, etc. 
     */
    public BulkCopyWorker(FileSystemManager vfs,UIComponent parent,File saveDir, List l) {
        super(parent,  "Copying to " + saveDir);
        this.vfs = vfs;
        this.saveDir = saveDir;
        saveObject = null;
        saveLoc = null;
        this.l = l;
    }
    
    public BulkCopyWorker(FileSystemManager vfs,UIComponent parent,FileObject saveObject, List l) {
        super(parent,  "Copying to " + saveObject.getName().getPath());
        this.vfs = vfs;
        this.saveObject = saveObject;
        this.saveDir = null;
        saveLoc = null;
        this.l = l;
    }
    
    public BulkCopyWorker(FileSystemManager vfs,UIComponent parent,URI saveLoc, List l) {
        super(parent,  "Copying to " + saveLoc);
        this.vfs = vfs;
        this.saveLoc = saveLoc;
        this.saveObject = null;
        this.saveDir = null;
        this.l = l;
    }

    /** necessary to work around a bug - allows me to access the constructor. 
     * 
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Sep 11, 200712:09:07 PM
     */
    private static class MyUrlFileObject extends UrlFileObject {
        /**
             * @param fs
             * @param fileName
             */
            public MyUrlFileObject(UrlFileSystem fs, FileName fileName) {
                super(fs, fileName);
            }
        }
  
    protected FileObject saveTarget;
    protected Object construct() throws Exception {
        if (saveObject != null) { // we've been given an file object
            saveTarget = saveObject;
        } else if (saveDir != null) { // we've been given a file
            saveTarget = vfs.resolveFile(this.saveDir.toURI().toString());
        } else { // have been given a UI 
            saveTarget = vfs.resolveFile(saveLoc.toString());
        }
        
        if (! saveTarget.exists()) {
            saveTarget.createFolder();
        }
        if (! saveTarget.isWriteable()) {
            throw new Exception("Not permitted to write to " + saveTarget.getName());
        }
        // will records destinations or errors of copying each individual file, continue, and report at the end.
        Map outcome = new HashMap();
        // go through each file in turn.
        for (Iterator i = this.l.iterator(); i.hasNext(); ) {
                FileObject src;
                Object something = i.next();
                if (something instanceof FileObject) {
                    src = (FileObject)something;
                } else {
                    final String uriString = something.toString();
                    if (StringUtils.contains(uriString,'?')) { // needs special treatment - just for http queries.
                        // bug in vfs here - if I call 'resolveFile' directly, if drops all after the '?'.
                        // so I need to create an object by hand.
                        //see VfsLibraryTest for proof of this.
                        FileName name = vfs.resolveURI(uriString);                    
                        FileObject root = vfs.resolveFile(uriString); // just so we can get a handle on the correct filesystem.
                        src = new MyUrlFileObject((UrlFileSystem)root.getFileSystem(),name);
                    } else {
                        src = vfs.resolveFile(uriString);
                    }                    
                }
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
                    dest = saveTarget.resolveFile(name + (n == 0 ? "" : "-" + n ) +  ext);
                    n++;
                } while  (dest.exists());
                
                // cool. now got a non-existent destination file.
                dest.copyFrom(src, Selectors.SELECT_ALL); // creates and copies from src. handles folders and files. nice!
                outcome.put(src,dest.getName().getURI());
            } catch (FileSystemException x) {
                outcome.put(src,x);
            }
        }
        // notify the parent object that we've made changes - cause a refresh.
    //    if (saveTarget != null) {
            FileSystem fs = saveTarget.getFileSystem();
            if (fs instanceof AbstractFileSystem) {
                ((AbstractFileSystem)fs).fireFileChanged(saveTarget);
            }
      //  }
        return outcome;
    }

    protected void doFinished(Object result) {
        Map outcome = (Map)result;
        boolean seenErrs = CollectionUtils.exists(outcome.entrySet(),new Predicate() {

            public boolean evaluate(Object arg0) {
                Map.Entry kv = (Map.Entry)arg0;
                return kv.getValue() instanceof Throwable;                    
            }
        });
        if (! seenErrs) {             
                parent.showTransientMessage("Copy completed",
                outcome.size() == 1 
                        ? outcome.keySet().iterator().next() + "<br>  copied to <br>" + outcome.values().iterator().next() 
                        : outcome.size() + " files copied to " + saveTarget.getName().getURI());            
            return;
        }
        HtmlBuilder msgBuilder = new HtmlBuilder();			    
        msgBuilder.h2("Encountered errors while copying files");
        for (Iterator i = outcome.entrySet().iterator(); i.hasNext();) {
            Map.Entry err = (Map.Entry) i.next();
            FileObject f = (FileObject)err.getKey();
            if (! (err.getValue() instanceof Throwable )) {
                continue;
            }
            Throwable e = (Throwable)err.getValue();
            msgBuilder.append(f.getName().getPath()).append("<br>");
            msgBuilder.append(ExceptionFormatter.formatException(e,ExceptionFormatter.ALL));
            msgBuilder.append("<p>");
        }
        ResultDialog rd = new ResultDialog(parent.getFrame(),msgBuilder);
        rd.show();
    }
}