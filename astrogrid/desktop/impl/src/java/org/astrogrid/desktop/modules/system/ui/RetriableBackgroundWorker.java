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

/** Subclass of background worker for a worker which can be 'retried'.
 * 
 * nb: all subclasses of this class will use modal errors (ncessary to prompt user to retry).
 * Hence any calls to BackgroundWorker.setTransient() will be ignored.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 26, 20073:32:38 PM
 */
public abstract class RetriableBackgroundWorker extends BackgroundWorker {

    public RetriableBackgroundWorker(final UIComponent parent, final String msg,
            final int priority) {
        super(parent, msg, priority);
    }

    public RetriableBackgroundWorker(final UIComponent parent, final String msg,
            final TimeoutEnum timeout, final int priority) {
        super(parent, msg, timeout, priority);
    }

    public RetriableBackgroundWorker(final UIComponent parent, final String msg,
            final TimeoutEnum timeout) {
        super(parent, msg, timeout);
    }

    public RetriableBackgroundWorker(final UIComponent parent, final String msg) {
        super(parent, msg);
    }

    public RetriableBackgroundWorker(final UIContext context, final String msg,
            final TimeoutEnum timeout) {
        super(context, msg, timeout);
    }

    public RetriableBackgroundWorker(final UIContext context, final String msg) {
        super(context, msg);
    }
    
    
    
    /**To support retry-on-failure,  implement this method to 
     * create a new worker task to be executed on 'retry'. Note that this method
     * may return a non-retriable worker.
     * @return
     */
    public abstract BackgroundWorker createRetryWorker() ;

    
    @Override
    protected final void doError(final Throwable ex) {
        final String t = "An error occurred while " + workerTitle.toLowerCase();
        if (GraphicsEnvironment.isHeadless()) {
            return; // don't show dialogue if we're headless.
        }
        // give user the option to retry
        final ConfirmDialog cd = ConfirmDialog.newConfirmDialog(parent.getComponent(),"An Error Occurred","<html>" + ExceptionFormatter.formatException(ex),new Runnable() {
            public void run() {
                final BackgroundWorker bw = createRetryWorker();
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
