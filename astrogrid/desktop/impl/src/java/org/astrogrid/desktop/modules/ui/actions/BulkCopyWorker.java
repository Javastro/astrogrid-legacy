package org.astrogrid.desktop.modules.ui.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.UIManager;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.Selectors;
import org.apache.commons.vfs.provider.AbstractFileSystem;
import org.apache.commons.vfs.provider.url.UrlFileObject;
import org.apache.commons.vfs.provider.url.UrlFileSystem;
import org.astrogrid.desktop.modules.ag.vfs.myspace.MyspaceFileObject;
import org.astrogrid.desktop.modules.dialogs.ResultDialog;
import org.astrogrid.desktop.modules.ivoa.resource.HtmlBuilder;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;

/** worker class which does a bulk copy of a bunch of files/folders to another target.
 * 
 * api is a form of the command pattern - it processes a set of copy commands.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 25, 20074:49:51 PM
 */
public class BulkCopyWorker extends BackgroundWorker {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(BulkCopyWorker.class);
    
    /**
     * 
     */
    private final File saveDir;
    /**
     * 
     */
    protected final CopyCommand[] cmds;
    private final FileSystemManager vfs;
    private final FileObject saveObject;
    private final URI saveLoc;

    /**
     * @param parent ui parent
     * @param saveDir directory to copy data to.
     * @param l list of files to copy. 
     */
    public BulkCopyWorker(FileSystemManager vfs,UIComponent parent,File saveDir, CopyCommand[] l) {
        super(parent,  "Copying to " + saveDir,BackgroundWorker.VERY_LONG_TIMEOUT);
        this.vfs = vfs;
        this.saveDir = saveDir;
        saveObject = null;
        saveLoc = null;
        this.cmds = l;
    }
    
    public BulkCopyWorker(FileSystemManager vfs,UIComponent parent,FileObject saveObject, CopyCommand[] l) {
        super(parent,  "Copying to " + saveObject.getName().getPath(),BackgroundWorker.VERY_LONG_TIMEOUT);
        this.vfs = vfs;
        this.saveObject = saveObject;
        this.saveDir = null;
        saveLoc = null;
        this.cmds = l;
    }
    
    public BulkCopyWorker(FileSystemManager vfs,UIComponent parent,URI saveLoc, CopyCommand[] l) {
        super(parent,  "Copying to " + saveLoc,BackgroundWorker.VERY_LONG_TIMEOUT);
        this.vfs = vfs;
        this.saveLoc = saveLoc;
        this.saveObject = null;
        this.saveDir = null;
        this.cmds = l;
    }
    {
        setWouldLikeIndividualMonitor(true);
    }

    /** necessary to work around a bug - vfs doesn't respect the query portion of a url
     * subclassing allows me to access the constructor. - and constructing an instance by hand fixes the problem. 
     * not required any more - have fixed this at the core.
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Sep 11, 200712:09:07 PM
     */
//    private static class MyUrlFileObject extends UrlFileObject {
//        /**
//             * @param fs
//             * @param fileName
//             */
//            public MyUrlFileObject(UrlFileSystem fs, FileName fileName) {
//                super(fs, fileName);
//            }
//        }
  
    protected FileObject saveTarget;
    protected Object construct() throws Exception {
        final int tasksCount = cmds.length + 1;
        int progress = 0;
        setProgress(progress,tasksCount);
        reportProgress("Validating save location");
        if (saveObject != null) { // we've been given an file object
            saveTarget = saveObject;
        } else if (saveDir != null) { // we've been given a file
            saveTarget = vfs.resolveFile(this.saveDir.toURI().toString());
        } else { // have been given a UI 
            saveTarget = vfs.resolveFile(saveLoc.toString());
        }
          
        boolean saveToMyspace = saveTarget instanceof MyspaceFileObject; 
            
        if (! saveTarget.exists()) {
            reportProgress("Creating save location");
            saveTarget.createFolder();
        }
        if (! saveTarget.isWriteable()) {
            throw new Exception("Not permitted to write to " + saveTarget.getName());
        }                
        
        reportProgress((saveToMyspace ? "VOSpace " : "" ) + "Save location validated");
        setProgress(++progress,tasksCount);
        // go through each file in turn.
        List destList = new ArrayList();
        for (int i  = 0; i < this.cmds.length; i++ ) {
                CopyCommand cmd = cmds[i];
                FileObject src =cmd.resolveSourceFileObject(vfs);
                reportProgress("Processing " + src.getName());
                String name;
                String ext;
                if (cmd instanceof CopyAsCommand) {
                    // already specifies a name
                    CopyAsCommand ccmd = (CopyAsCommand)cmd;
                    name = StringUtils.substringBeforeLast(ccmd.getTargetFilename(),".");
                    ext = StringUtils.substringAfterLast(ccmd.getTargetFilename(),".");
                } else {
                    // compute a suitable name
                    name = StringUtils.substringBeforeLast(src.getName().getBaseName(),".");
                    ext = StringUtils.substringAfterLast(src.getName().getBaseName(),".");
                }
                FileObject dest = null;
                try {
                    if (StringUtils.isNotBlank(ext)) {
                        ext = "." + ext;
                    }
                    int n = 0;
                    // create a target filename - and if it already exists, change it by adding a number.
                    do {
                        dest = saveTarget.resolveFile(name + (n == 0 ? "" : "-" + n ) +  ext);
                        n++;
                    } while  (dest.exists());
                    reportProgress("Destination will be " + dest.getName().getBaseName());
                    // good. now got a non-existent destination file                    
                    dest.copyFrom(src, Selectors.SELECT_ALL); // creates and copies from src. handles folders and files. nice!
                    cmd.recordSuccess(dest.getName());
                    destList.add(dest);
                } catch (FileSystemException x) {
                    cmd.recordError(x);
                    reportProgress("Copy from " + src.getName().getBaseName() + " failed");
                    reportProgress("Cause: " + exFormatter.format(x,ExceptionFormatter.INNERMOST));
                    
                } finally {
                    setProgress(++progress,tasksCount);
                }
        }
        // notify the parent object that we've made changes - cause a refresh.
    //    if (saveTarget != null) {
            FileSystem fs = saveTarget.getFileSystem();
            if (fs instanceof AbstractFileSystem) {
                AbstractFileSystem afs = (AbstractFileSystem) fs;
                afs.fireFileChanged(saveTarget);
                for (Iterator it = destList.iterator(); it.hasNext();) {
                    ((FileObject) it.next()).refresh();
                }
            }
      //  }
        return cmds; // results have been recorded in here.
    }
    private ExceptionFormatter exFormatter = new ExceptionFormatter();

    protected void doFinished(Object ignored) {
        // check if we've seen any errors.
        boolean seenErrs = false;
        for (int i = 0; i < cmds.length; i++) {
            if (cmds[i].failed()) {
                seenErrs = true;
                break;
            }
        }
        if (! seenErrs) {             
                parent.showTransientMessage("Copy completed",
                cmds.length == 1 
                        ? cmds[0].formatResult()
                        : cmds.length + " files copied to " + saveTarget.getName().getURI());            
            return;
        }
        HtmlBuilder msgBuilder = new HtmlBuilder();			    
        for (int i = 0; i < cmds.length; i++) {
            CopyCommand cmd = cmds[i];
            if (! cmd.failed()) {
                continue;
            }
            String string = cmd.formatResult();
            logger.warn(string);
            msgBuilder.append(string);
            msgBuilder.append("<p>");
        }
        
        if (!GraphicsEnvironment.isHeadless() && ! Boolean.getBoolean("unit.testing")) {           
            ResultDialog rd = ResultDialog.newResultDialog(parent.getComponent(),msgBuilder);
            rd.getBanner().setVisible(true);
            rd.getBanner().setTitle("Errors encountered while copying files");
            rd.getBanner().setSubtitleVisible(false);
            rd.getBanner().setIcon(UIManager.getIcon("OptionPane.warningIcon"));
            rd.pack();
            rd.show();
        }
    }
}
