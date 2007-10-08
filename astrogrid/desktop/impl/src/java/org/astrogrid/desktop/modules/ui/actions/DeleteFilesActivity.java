/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.Selectors;
import org.apache.commons.vfs.provider.AbstractFileSystem;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.dialogs.ResultDialog;
import org.astrogrid.desktop.modules.ivoa.resource.HtmlBuilder;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;

/** delete one or more files.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 10, 20071:03:46 PM
 */
public class DeleteFilesActivity extends AbstractFileActivity {

    private final FileSystemManager vfs;
	
	
    // applies to all non-local files and folders.
	protected boolean invokable(FileObject f) { 
	    try {
            return f.isWriteable();
        } catch (FileSystemException x) {
            logger.error("FileSystemException",x);
            return false;
        }
	}


	public DeleteFilesActivity(final FileSystemManager vfs) {
		super();
        this.vfs = vfs;
		setText("Delete");
		setIcon(IconHelper.loadIcon("editdelete16.png"));		
		setToolTipText("Delete this files");
	}


	public void actionPerformed(ActionEvent e) {
		final List l = computeInvokable(); 
		logger.debug(l);
		if (! confirm("Delete these " + l.size() + " files?")) {
		    return; 
		}

		(new BackgroundWorker(uiParent.get(),"Deleting files") {
            protected Object construct() throws Exception {
                Set parents = new HashSet();
                Map errors = new HashMap();
                for (Iterator i = l.iterator(); i.hasNext();) {
                    FileObject f = (FileObject) i.next();         
                    try {
                        FileObject parent = f.getParent();
                        if (parent != null) {
                            parents.add(parent);
                        }
                        f.delete(Selectors.SELECT_ALL);
                    } catch(FileSystemException x) {
                        errors.put(f,x);
                    }
                }
                for (Iterator i = parents.iterator(); i.hasNext();) {
                    FileObject p = (FileObject) i.next();
                    FileSystem fs = p.getFileSystem();
                    if (fs instanceof AbstractFileSystem) {
                        ((AbstractFileSystem)fs).fireFileChanged(p);
                    }
                }
                return errors;
            }
            
            protected void doFinished(Object result) {
                Map errors = (Map)result;
                if (errors.size() ==0) {
                    parent.showTransientMessage("Deleted files","");
                    return;
                }
                HtmlBuilder msgBuilder = new HtmlBuilder();             
                msgBuilder.h2("Encountered errors while deleting some files");
                for (Iterator i = errors.entrySet().iterator(); i.hasNext();) {
                    Map.Entry err = (Map.Entry) i.next();
                    FileObject f = (FileObject)err.getKey();
                    Throwable e = (Throwable)err.getValue();
                    msgBuilder.append(f.getName().getPath()).append("<br>");
                    msgBuilder.append(ExceptionFormatter.formatException(e,ExceptionFormatter.ALL));
                    msgBuilder.append("<p>");                    
                }
                ResultDialog rd = new ResultDialog(parent.getFrame(),msgBuilder);
                rd.show();
            }            
		}).start();
		
	}
	
	

}
