/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.provider.AbstractFileSystem;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectView;

import com.l2fprod.common.swing.BaseDialog;

/** Rename a single file.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 10, 20071:03:46 PM
 * @TEST
 */
public class RenameActivity extends AbstractFileActivity {

    /** Dialog that captures user input and performs minimalistic checking
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Nov 21, 20079:46:40 AM
     */
    private final class RenameDialog extends BaseDialog {
        /**
         * 
         */
        private final FileObjectView original;
        private final JTextField tf = new JTextField();
        private String originalName;

        /** 
         * @param owner
         * @param original
         */
        private RenameDialog(final Frame owner, final FileObjectView original)
                throws HeadlessException {
            super(owner);
            this.original = original;  
            init();
        }
        
        private RenameDialog(final Dialog owner, final FileObjectView original) {
            super(owner);
            this.original = original;
            init();
        }
          
        /**
         * @param original2
         */
        public RenameDialog(final FileObjectView original2) {
            this.original = original2;
            init();
        }

        private void init(){
            setModal(false);
            setDialogMode(BaseDialog.OK_CANCEL_DIALOG);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            setTitle("Rename");
            originalName = this.original.getBasename();
            getBanner().setTitle("Renaming " + originalName);
            getBanner().setSubtitle("Enter a new name");
            getBanner().setSubtitleVisible(true);
            tf.setText(originalName);
            tf.setColumns(20);
            
            final Container cp = getContentPane();
            cp.setLayout(new java.awt.FlowLayout());
            cp.add(new JLabel("Filename :"));
            cp.add(tf);
            pack();
            tf.selectAll();
            tf.requestFocusInWindow();
            setLocationRelativeTo(uiParent.get().getComponent());
        }

        /** process input */
        @Override
        public void ok() {
            (new BackgroundWorker<String>(uiParent.get(),"Renaming " + originalName,Thread.MAX_PRIORITY) {
                
                @Override
                protected String construct() throws Exception {
                    final FileObject parent = original.getFileObject().getParent();
                    final String nuName = tf.getText();
                    if (StringUtils.isEmpty(nuName) || originalName.equals(nuName)) {
                            return null; // no input, or no change - just close the dialogue
                        }
                   final FileObject f = parent.resolveFile(nuName);
                   if (f.exists()) {
                       return nuName + " already exists";
                   }
                   // ok, user has made some input, and it's fairly valid - we can close the dialogue immediately.
                   SwingUtilities.invokeLater(new Runnable() {
                       public void run() {
                          RenameDialog.super.ok();
                       }
                      });                     
                   // now get on with the task in hand.
                    original.getFileObject().moveTo(f);
                    final FileSystem fs = parent.getFileSystem();
                    if (fs instanceof AbstractFileSystem) {
                        ((AbstractFileSystem)fs).fireFileChanged(parent);
                    }
                    return null;
                }
                @Override
                protected void doFinished(final String result) {
                    if (result == null && isVisible()) {
                        RenameDialog.super.ok();
                    } else {
                        getBanner().setSubtitleColor(Color.RED);
                        getBanner().setSubtitle(result + " - Choose another name");
                    }
                }
            }).start();
        }
  	                
    }

    private final FileSystemManager vfs;
	
	
	@Override
    protected boolean invokable(final FileObjectView f) { 
            return (! f.isDelegate() 
                    && f.isWritable());
	}


	public RenameActivity(final FileSystemManager vfs) {
		super();
		setHelpID("activity.rename");
        this.vfs = vfs;
		setText("Rename"+UIComponentMenuBar.ELLIPSIS);
		//setIcon(IconHelper.loadIcon("browser16.png"));		
		setToolTipText("rename this file");
	}
	
	// can only handle a single selection.
	@Override
    public void manySelected(final FileObjectView[] list) {
		noneSelected();
	}
	
	@Override
    public void actionPerformed(final ActionEvent e) {
		final List<FileObjectView> l = computeInvokable();
		logger.debug(l);
		final FileObjectView original = l.get(0);
        final Component pc = uiParent.get().getComponent();
        final Window w = pc instanceof Window
                 ? (Window) pc
                 : SwingUtilities.getWindowAncestor(pc);
		
		final BaseDialog d;
		if (w instanceof Frame) {
		    d = new RenameDialog((Frame)w, original);
		} else if (w instanceof Dialog) {
            d = new RenameDialog((Dialog)w, original);		    
		} else {
            d = new RenameDialog(original);		    
		}
		d.setVisible(true);
		
		
	}
	
	

}
