/**
 * 
 */
package org.astrogrid.desktop.modules.system.ui;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.astrogrid.desktop.modules.dialogs.ConfirmDialog;
import org.astrogrid.desktop.modules.system.ui.BackgroundWorkersMonitorImpl.BackgroundWorkerCell;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.BackgroundWorker.Info;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.l2fprod.common.swing.BaseDialog;

/** Individual progress dialogue for a background worker.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 26, 20074:39:55 PM
 */
public class ProgressDialogue extends BaseDialog implements Observer {

    private final BackgroundWorker worker;
    private BackgroundWorkerCell display;
    private JCheckBox hide;
    private DefaultListModel lm;
    private JList list;

    /**
     * @param backgroundWorker
     */
    private ProgressDialogue(Frame f,BackgroundWorker backgroundWorker) {
        super(f);
        this.worker = backgroundWorker;
        init();
        setLocationRelativeTo(f);
        
        
    }
    private ProgressDialogue(Dialog f,BackgroundWorker backgroundWorker) {
        super(f);
        this.worker = backgroundWorker;
        init();
        setLocationRelativeTo(f);
        
    }
   /**
     * 
     */
    private void init() {
        Info info = worker.getInfo();
        setTitle("Progress: " + info.getWorkerTitle());
        getBanner().setVisible(false);
        setModal(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setDialogMode(BaseDialog.CLOSE_DIALOG);        
        display = new BackgroundWorkersMonitorImpl.BackgroundWorkerCell(worker);
        worker.addObserver(this);
        
        JPanel p = (JPanel)getContentPane();
        FormLayout fl = new FormLayout("20dlu,1dlu,fill:200dlu:grow,1dlu,p"
                    ,"p,2dlu,fill:70dlu:grow,2dlu,p");
        CellConstraints cc = new CellConstraints();
        PanelBuilder pb = new PanelBuilder(fl,p);
        
        pb.add(display.getStatus(),cc.xy(1,1));
        pb.add(display.getWorkerTitle(),cc.xy(3,1));
        pb.add(display.getHalt(),cc.xy(5,1));      
               
        lm = new DefaultListModel();
        list = new JList(lm);
        pb.add(new JScrollPane(list,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED)
            ,cc.xy(3,3));       
        hide = new JCheckBox("Close this dialogue when task completes");
        pb.add(hide,cc.xy(3,5));
        pack();
      
    }
 private ProgressDialogue(BackgroundWorker backgroundWorker) {
        this.worker = backgroundWorker;
        init();
        centerOnScreen();
        
    }    
    
    public static ProgressDialogue newProgressDialogue(BackgroundWorker worker) {
        Window window = (Window)SwingUtilities.getAncestorOfClass(Window.class, worker.getParent().getComponent());
        if (window instanceof Frame) {
          return new ProgressDialogue((Frame)window,worker);
        } else if (window instanceof Dialog) {
          return new ProgressDialogue((Dialog)window,worker);      
        } else {
          return new ProgressDialogue(worker);
        }
    }
    public void update(Observable o, Object arg) {
        display.reload();
        // check for new messages.
        final Info info = worker.getInfo();
        String m = info.getProgressMessage();
        int lastix =lm.getSize() -1; 
        if (lastix < 0 || ! lm.get(lastix).equals(m)) {
            lm.addElement(m);
            list.ensureIndexIsVisible(lastix+1);
        }
        if (info.getStatus() == BackgroundWorker.COMPLETED && hide.isSelected()) {
            setVisible(false);
        }
        
    }
}
