/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.awt.Component;
import java.awt.HeadlessException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.system.ui.UIContext;

import com.l2fprod.common.swing.BaseDialog;

/** baseclass fror dialogue ui components.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 12, 200711:35:27 AM
 */
public class UIDialogueComponentImpl extends BaseDialog implements UIComponent {
    protected static final Log logger = LogFactory.getLog(UIDialogueComponentImpl.class);
    private final UIContext context;
    private UIComponentAssist assist;
    public UIDialogueComponentImpl(UIContext context) throws HeadlessException {
        super();
        getBanner().setVisible(false);
        setModal(true);
        this.context = context;
        this.assist = new UIComponentAssist(this);
        
    }
    
    /** based on code reading
     * not usually needed, but handy when you need to disable ok based on user input.
     *  */
    protected JButton getOkButton() {
        return getRootPane().getDefaultButton();
    }

    public UIContext getContext() {
        return context;
    }

    public Component getComponent() {
        return this;
    }

    // delegation methods after this point
    /**
     * @return
     * @see org.astrogrid.desktop.modules.ui.UIComponentAssist#getMainPanel()
     */
    public JPanel getMainPanel() {
        return this.assist.getMainPanel();
    }

    /**
     * @return
     * @see org.astrogrid.desktop.modules.ui.UIComponentAssist#getProgressMax()
     */
    public int getProgressMax() {
        return this.assist.getProgressMax();
    }

    /**
     * @return
     * @see org.astrogrid.desktop.modules.ui.UIComponentAssist#getProgressValue()
     */
    public int getProgressValue() {
        return this.assist.getProgressValue();
    }

    /**
     * 
     * @see org.astrogrid.desktop.modules.ui.UIComponentAssist#haltMyTasks()
     */
    public void haltMyTasks() {
        this.assist.haltMyTasks();
    }

    /**
     * @param i
     * @see org.astrogrid.desktop.modules.ui.UIComponentAssist#setProgressMax(int)
     */
    public void setProgressMax(int i) {
        this.assist.setProgressMax(i);
    }

    /**
     * @param i
     * @see org.astrogrid.desktop.modules.ui.UIComponentAssist#setProgressValue(int)
     */
    public void setProgressValue(int i) {
        this.assist.setProgressValue(i);
    }

    /**
     * @param s
     * @see org.astrogrid.desktop.modules.ui.UIComponentAssist#setStatusMessage(java.lang.String)
     */
    public void setStatusMessage(String s) {
        this.assist.setStatusMessage(s);
    }

    /**
     * @param msg
     * @param e
     * @see org.astrogrid.desktop.modules.ui.UIComponentAssist#showError(java.lang.String, java.lang.Throwable)
     */
    public void showError(String msg, Throwable e) {
        this.assist.showError(msg, e);
    }

    /**
     * @param s
     * @see org.astrogrid.desktop.modules.ui.UIComponentAssist#showError(java.lang.String)
     */
    public void showError(String s) {
        this.assist.showError(s);
    }

    /**
     * @param title
     * @param message
     * @see org.astrogrid.desktop.modules.ui.UIComponentAssist#showTransientError(java.lang.String, java.lang.String)
     */
    public void showTransientError(String title, String message) {
        this.assist.showTransientError(title, message);
    }

    /**
     * @param title
     * @param message
     * @see org.astrogrid.desktop.modules.ui.UIComponentAssist#showTransientMessage(java.lang.String, java.lang.String)
     */
    public void showTransientMessage(String title, String message) {
        this.assist.showTransientMessage(title, message);
    }

    /**
     * @param title
     * @param message
     * @see org.astrogrid.desktop.modules.ui.UIComponentAssist#showTransientWarning(java.lang.String, java.lang.String)
     */
    public void showTransientWarning(String title, String message) {
        this.assist.showTransientWarning(title, message);
    }

}
