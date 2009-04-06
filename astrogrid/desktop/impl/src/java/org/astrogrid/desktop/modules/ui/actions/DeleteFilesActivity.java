/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.KeyStroke;
import javax.swing.UIManager;

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
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectView;

/** Delete one or more files.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 10, 20071:03:46 PM
 * @TEST unit test where possibl.
 */
@SuppressWarnings("serial")
public class DeleteFilesActivity extends AbstractFileActivity {

    @Override
    protected boolean invokable(final FileObjectView f) { 
            return f.isWritable();
	}


	public DeleteFilesActivity(final FileSystemManager vfs) {
		super();
		setHelpID("activity.delete");
        setText("Delete");
		setIcon(IconHelper.loadIcon("editdelete16.png"));		
		setToolTipText("Delete this files");
		setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE,UIComponentMenuBar.MENU_KEYMASK));
	}


	@Override
    public void actionPerformed(final ActionEvent e) {
		final List<FileObjectView> l = computeInvokable(); 
		logger.debug(l);


		final BackgroundWorker<Map<FileObjectView,FileSystemException>> act = new BackgroundWorker<Map<FileObjectView,FileSystemException>>(uiParent.get(),"Deleting files",BackgroundWorker.LONG_TIMEOUT) {
		    {
		        setWouldLikeIndividualMonitor(true);
		    }
            @Override
            protected Map<FileObjectView,FileSystemException> construct() throws Exception {
                int count = 0;
                final int max = l.size();
                setProgress(count,max);
                final Set<FileObject> parents = new HashSet<FileObject>();
                final Map<FileObjectView,FileSystemException> errors = new HashMap<FileObjectView,FileSystemException>();
                for (final FileObjectView f : l) {
                    reportProgress("Deleting " + f.getBasename());
                    final FileObject fo = f.getFileObject();
                    try {
                        final FileObject parentFile = fo.getParent();
                        if (parent != null) {
                            parents.add(parentFile);
                        }
                        fo.delete(Selectors.SELECT_ALL);
                    } catch(final FileSystemException x) {
                        errors.put(f,x);
                        reportProgress("Failed to delete");
                    } finally {
                        setProgress(++count,max);
                    }
                }
                for (final FileObject p : parents) {
                    final FileSystem fs = p.getFileSystem();
                    if (fs instanceof AbstractFileSystem) {
                        ((AbstractFileSystem)fs).fireFileChanged(p);
                    }
                }
                return errors;
            }
            
            @Override
            protected void doFinished(final Map<FileObjectView,FileSystemException> errors) {               
                if (errors.size() ==0) {
                    parent.showTransientMessage("Deleted files","");
                    return;
                }
                final HtmlBuilder msgBuilder = new HtmlBuilder();
                for(final Map.Entry<FileObjectView,FileSystemException> err : errors.entrySet()) {
                    final FileObjectView f = err.getKey();
                    final Throwable ex = err.getValue();
                    msgBuilder.append(f.getUri()).append("<br>");
                    msgBuilder.append(ExceptionFormatter.formatException(ex,ExceptionFormatter.ALL));
                    msgBuilder.append("<p>");                    
                }
                final ResultDialog rd =ResultDialog.newResultDialog(parent.getComponent(),msgBuilder);
                rd.getBanner().setVisible(true);
                rd.getBanner().setTitle("Errors encountered while deleting files");
                rd.getBanner().setSubtitleVisible(false);
                rd.getBanner().setIcon(UIManager.getIcon("OptionPane.warningIcon"));
                rd.pack();
                rd.show();
            }            
		};
		confirm("Delete these " + l.size() + " files?",new Runnable() {

            public void run() {
                act.start();
            }
		});
		
	}
	
	

}
