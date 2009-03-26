/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.l2fprod.common.swing.BaseDialog;

/**
 * Simple dialog that allows a user confirmation can be captured before proceeding. 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 20, 20074:01:58 PM
 */
public class ConfirmDialog extends BaseDialog {
    private Runnable okAction2;

    public static ConfirmDialog newConfirmDialog(Component parent, String title,String message, Runnable action) {
        Window window = parent instanceof Window
                      ? (Window) parent
                      : (Window) SwingUtilities.getWindowAncestor(parent);

        if (window instanceof Frame) {
          return new ConfirmDialog((Frame)window,title,message,action);
        } else if (window instanceof Dialog) {
          return new ConfirmDialog((Dialog)window,title,message,action);      
        } else {
          return new ConfirmDialog(title,message,action);
        }
    }
    

    public ConfirmDialog(Dialog owner,String title, String message, Runnable action) {
        super(owner);
        init(title,message,action);
        setLocationRelativeTo(owner);
    }
    
    public ConfirmDialog(Frame owner,String title, String message, Runnable action) {
        super(owner);
        init(title,message,action);
        setLocationRelativeTo(owner);
    }

    
    public ConfirmDialog(String title, String message, Runnable action) {
        init(title,message,action);
         centerOnScreen();
    }
    
    private final void init(String title,String message,Runnable okAction) {
        this.okAction2 = okAction;
        this.setTitle(title);
        this.getBanner().setTitle(title);
        this.getBanner().setSubtitle(message);
        this.getBanner().setIcon(UIManager.getIcon("OptionPane.questionIcon"));
        this.setModal(false);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setDialogMode(BaseDialog.OK_CANCEL_DIALOG);
        this.pack();
        
    }
    @Override
    public void ok() {
        super.ok();
        okAction2.run();
    }
    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) {
            toFront();
            requestFocus();
        }
    }
}
