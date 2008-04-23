/**
 * 
 */
package org.astrogrid.desktop.modules.system.ui;

import java.awt.GraphicsEnvironment;

import javax.swing.UIManager;

import org.astrogrid.desktop.modules.dialogs.ConfirmDialog;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;

/** Subclass of background worker for a worker which can be 'retried'
 * 
 * nb: all subclasses of this class will use modal errors (ncessary to prompt user to retry).
 * Hence any calls to BackgroundWorker.setTransient() will be ignored.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 26, 20073:32:38 PM
 */
public abstract class RetriableBackgroundWorker extends BackgroundWorker {

    public RetriableBackgroundWorker(UIComponent parent, String msg,
            int priority) {
        super(parent, msg, priority);
    }

    public RetriableBackgroundWorker(UIComponent parent, String msg,
            TimeoutEnum timeout, int priority) {
        super(parent, msg, timeout, priority);
    }

    public RetriableBackgroundWorker(UIComponent parent, String msg,
            TimeoutEnum timeout) {
        super(parent, msg, timeout);
    }

    public RetriableBackgroundWorker(UIComponent parent, String msg) {
        super(parent, msg);
    }

    public RetriableBackgroundWorker(UIContext context, String msg,
            TimeoutEnum timeout) {
        super(context, msg, timeout);
    }

    public RetriableBackgroundWorker(UIContext context, String msg) {
        super(context, msg);
    }
    
    
    
    /**To support retry-on-failure,  implement this method to 
     * create a new worker task to be executed on 'retry'. Note that this method
     * may return a non-retriable worker.
     * @return
     */
    public abstract BackgroundWorker createRetryWorker() ;

    
    protected final void doError(Throwable ex) {
        final String t = "An error occurred while " + workerTitle.toLowerCase();
        if (GraphicsEnvironment.isHeadless()) {
            return; // don't show dialogue if we're headless.
        }
        // give user the option to retry
        ConfirmDialog cd = ConfirmDialog.newConfirmDialog(parent.getComponent(),"An Error Occurred","<html>" + ExceptionFormatter.formatException(ex),new Runnable() {
            public void run() {
                BackgroundWorker bw = createRetryWorker();
                if (bw != null) {
                    bw.start();
                }
            }
        });
        cd.getBanner().setTitle("<html>" + t + "<br>Do you wish to retry this operation?");
        cd.getBanner().setIcon(UIManager.getIcon("OptionPane.errorIcon"));
        cd.setVisible(true);
    }
       


}
