package org.astrogrid.desktop.modules.ui.actions;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.UIManager;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.provider.AbstractFileSystem;
import org.astrogrid.desktop.modules.dialogs.ResultDialog;
import org.astrogrid.desktop.modules.ivoa.resource.HtmlBuilder;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;

/** worker class which does a bulk move of a bunch of files/folders to another target.
 * @future upgrade this to use the CopyCommands instead.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 25, 20074:49:51 PM
 */
public final class BulkMoveWorker extends BackgroundWorker<Map<FileObject, FileSystemException>> {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(BulkMoveWorker.class);

    /**
     * 
     */
    private final File saveDir;
    /**
     * 
     */
    private final List<FileObject> l;
    private final FileSystemManager vfs;
    private final FileObject saveObject;

    /**
     * @param parent ui parent
     * @param saveDir directory to copy data to.
     * @param l list of file objects to copy.
     */
    public BulkMoveWorker(final FileSystemManager vfs,final UIComponent parent,final File saveDir, final List<FileObject> l) {
        super(parent,  "Moving to " + saveDir,BackgroundWorker.VERY_LONG_TIMEOUT);
        this.vfs = vfs;
        this.saveDir = saveDir;
        saveObject = null;
        this.l = l;
    }
    
    public BulkMoveWorker(final FileSystemManager vfs,final UIComponent parent,final FileObject saveObject, final List<FileObject> l) {
        super(parent,  "Moving to " + saveObject.getName().getPath(),BackgroundWorker.VERY_LONG_TIMEOUT);
        this.vfs = vfs;
        this.saveObject = saveObject;
        this.saveDir = null;
        this.l = l;
    }
    {
        setWouldLikeIndividualMonitor(true);
    }

    protected FileObject saveTarget;
    protected Map<FileObject, FileSystemException> construct() throws Exception {
        final int tasksCount = l.size() + 1;
        int progress = 0;
        setProgress(progress,tasksCount);
        reportProgress("Validating save location");        
        final Set<FileObject> moveSourceDirs = new HashSet<FileObject>();
        if (saveObject == null) {
            saveTarget = vfs.resolveFile(this.saveDir.toURI().toString());
        } else {
            saveTarget = saveObject;
        }
        if (! saveTarget.exists()) {
            reportProgress("Creating save location");
            saveTarget.createFolder();
        }
        if (! saveTarget.isWriteable()) {
            throw new Exception("Not permitted to write to " + saveTarget.getName());
        }
        reportProgress("Save location validated");
        setProgress(++progress,tasksCount);        
        // will records errors copy indivitual files, continue, and report at the end.
        final Map<FileObject, FileSystemException> errors = new HashMap<FileObject, FileSystemException>();
        // go through each file in turn.
        for (final FileObject src: l ) {
                reportProgress("Processing " + src.getName().getBaseName());                
                FileObject dest = null;
                try {
                final String name = StringUtils.substringBeforeLast(src.getName().getBaseName(),".");
                String ext = StringUtils.substringAfterLast(src.getName().getBaseName(),".");
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
                
                
                // cool. now got a non-existent destination file.
                final FileObject srcParent = src.getParent();
                if (srcParent != null) {
                    moveSourceDirs.add(srcParent);
                }
                src.moveTo(dest);
            } catch (final FileSystemException x) {
                errors.put(src,x);
                reportProgress("Move from " + src.getName().getBaseName() + " failed");                
            }finally {
                setProgress(++progress,tasksCount);                
            }
        }
        // notify the parent object that we've made changes - cause a refresh.
    //    if (saveTarget != null) {
            FileSystem fs = saveTarget.getFileSystem();
            if (fs instanceof AbstractFileSystem) {
                ((AbstractFileSystem)fs).fireFileChanged(saveTarget);
            }
     //   }
        // also need to notify old parents of the files we've moved.
        for (final FileObject p: moveSourceDirs) {           
            fs = p.getFileSystem();
            if (fs instanceof AbstractFileSystem) {
                ((AbstractFileSystem)fs).fireFileChanged(p);
            }            
        }
        return errors;
    }

    protected void doFinished(final Map<FileObject, FileSystemException> errors) {       
        if (errors.size() ==0) {
            parent.showTransientMessage("Finished moving files","");
            return;
        }
        final HtmlBuilder msgBuilder = new HtmlBuilder();			    
        for (final Map.Entry<FileObject,FileSystemException> err : errors.entrySet()) {           
            final FileObject f = err.getKey();
            final Throwable e = err.getValue();
            logger.warn(f.getName().getPath(),e);
            msgBuilder.append(f.getName().getPath()).append("<br>");
            msgBuilder.append(ExceptionFormatter.formatException(e,ExceptionFormatter.ALL));
            msgBuilder.append("<p>");
        }
        if (!GraphicsEnvironment.isHeadless()) {
            final ResultDialog rd = ResultDialog.newResultDialog(parent.getComponent(),msgBuilder);
            rd.getBanner().setVisible(true);
            rd.getBanner().setTitle("Errors encountered while moving files");
            rd.getBanner().setSubtitleVisible(false);
            rd.getBanner().setIcon(UIManager.getIcon("OptionPane.warningIcon"));      
            rd.pack();
            rd.show();
        }
    }
}