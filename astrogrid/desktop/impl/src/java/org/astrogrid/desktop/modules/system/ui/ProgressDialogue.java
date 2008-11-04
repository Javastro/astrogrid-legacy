/**
 * 
 */
package org.astrogrid.desktop.modules.system.ui;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.astrogrid.desktop.modules.system.ui.BackgroundWorkersMonitorImpl.BackgroundWorkerCell;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.BackgroundWorker.Info;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.l2fprod.common.swing.BaseDialog;

/** Progress indicator dialogue for a single background worker.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 26, 20074:39:55 PM
 */
public class ProgressDialogue extends BaseDialog implements Observer {

    private final BackgroundWorker worker;
    private BackgroundWorkerCell display;
    private JCheckBox hide;
    private MyListModel lm;
    private JList list;

    
    /** list model tuned to this dialogue. 
     * attached to a standard
     * List, and it knows the list will only ever have items appended */
    private static class MyListModel extends AbstractListModel {

        public MyListModel(final List delegate) {
            super();
            this.delegate = delegate;
            sz = delegate.size();
        }
        int sz;
        private final List delegate;

        public Object getElementAt(final int index) {
            return delegate.get(index);
        }

        public int getSize() {
            return delegate.size();
        }
        /** called when we want to inform the model (and the List component) 
         * that the delegate list object has grown.
         */
        public void notifyListGrown() {
            // see what the new size is
            final int nuSz = delegate.size();
            if (nuSz > sz) {
                fireIntervalAdded(this,sz,nuSz-1);
                sz = nuSz;
            }
        }
        
    }
    /**
     * @param backgroundWorker
     */
    private ProgressDialogue(final Frame f,final BackgroundWorker backgroundWorker) {
        super(f);
        this.worker = backgroundWorker;
        init();
        setLocationRelativeTo(f);
        
        
    }
    private ProgressDialogue(final Dialog f,final BackgroundWorker backgroundWorker) {
        super(f);
        this.worker = backgroundWorker;
        init();
        setLocationRelativeTo(f);
        
    }
   /**
     * 
     */
    private void init() {
        final Info info = worker.getInfo();
        setTitle("Progress: " + info.getWorkerTitle());
        getBanner().setVisible(false);
        setModal(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setDialogMode(BaseDialog.CLOSE_DIALOG);        
        display = new BackgroundWorkersMonitorImpl.BackgroundWorkerCell(worker);
        worker.addObserver(this);
        
        final JPanel p = (JPanel)getContentPane();
        final FormLayout fl = new FormLayout("20dlu,1dlu,fill:200dlu:grow,1dlu,p"
                    ,"p,2dlu,fill:70dlu:grow,2dlu,p");
        final CellConstraints cc = new CellConstraints();
        final PanelBuilder pb = new PanelBuilder(fl,p);
        
        pb.add(display.getStatus(),cc.xy(1,1));
        pb.add(display.getWorkerTitle(),cc.xy(3,1));
        pb.add(display.getHalt(),cc.xy(5,1));      
               
        lm = new MyListModel(info.getProgressMessages());
        list = new JList(lm);
        list.setFocusable(false);// it's not an input, its a display.
        pb.add(new JScrollPane(list,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED)
            ,cc.xy(3,3));       
        hide = new JCheckBox("Close this dialogue when task completes");
        pb.add(hide,cc.xy(3,5));
        pack();
      
    }
 private ProgressDialogue(final BackgroundWorker backgroundWorker) {
        this.worker = backgroundWorker;
        init();
        centerOnScreen();
        
    }    
    
    public static ProgressDialogue newProgressDialogue(final BackgroundWorker worker) {
        final Component pc = worker.getParent().getComponent();
        final Window window = pc instanceof Window
                      ? (Window) pc
                      : SwingUtilities.getWindowAncestor(pc);
        if (window instanceof Frame) {
          return new ProgressDialogue((Frame)window,worker);
        } else if (window instanceof Dialog) {
          return new ProgressDialogue((Dialog)window,worker);      
        } else {
          return new ProgressDialogue(worker);
        }
    }
    public void update(final Observable o, final Object arg) {
        display.reload();
        // check for new messages.
        final Info info = worker.getInfo();
        lm.notifyListGrown();
        list.ensureIndexIsVisible(lm.getSize()-1);       
        if (info.getStatus() == BackgroundWorker.COMPLETED && hide.isSelected()) {
            setVisible(false);
        }
        
    }
}
