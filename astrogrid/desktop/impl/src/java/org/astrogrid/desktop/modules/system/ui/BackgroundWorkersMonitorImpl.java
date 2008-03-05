/**
 * 
 */
package org.astrogrid.desktop.modules.system.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventListener;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.BackgroundWorker.Info;
import org.astrogrid.desktop.modules.ui.comp.AbstractCloseAction;
import org.astrogrid.desktop.modules.ui.comp.IndeterminateProgressIndicator;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import ca.odell.glazedlists.CompositeList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.FunctionList;
import ca.odell.glazedlists.ObservableElementList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.matchers.AbstractMatcherEditor;
import ca.odell.glazedlists.matchers.Matcher;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.swing.EventComboBoxModel;
import ca.odell.glazedlists.swing.JEventListPanel;

/** implementatio of the process monitor.
 * unlike everything else, this is _not_ a UIComponent.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 23, 20072:37:30 PM
 */
public class BackgroundWorkersMonitorImpl extends JFrame implements BackgroundWorkersMonitor{

    
    private JComboBox subsetCombo;

    public void showAll() {
        subsetCombo.setSelectedIndex(0);
        show();
    }

    public void showProcessesFor(UIComponent window) {
        subsetCombo.setSelectedItem(window);
        show();
    }

    public void showSystem() {
        subsetCombo.setSelectedIndex(1);
        show();
    }
/*
    public BackgroundWorkersMonitorImpl(EventList backgroundTasks, EventList windows)
            throws HeadlessException {
*/
    public BackgroundWorkersMonitorImpl(final UIContext context)
    throws HeadlessException {
        super();
        JPanel panel = new JPanel(new BorderLayout());
        // top panel of the ui.
        FormLayout fl = new FormLayout("2dlu,fill:p:grow,5dlu,p,2dlu","2dlu,p,2dlu,p");
        CellConstraints cc = new CellConstraints();
        PanelBuilder pb = new PanelBuilder(fl);
        pb.addLabel("Show processes belonging to:",cc.xy(2,2));
                
        // contents of combo box to select which subset of processes to view        
        // create a self-updating list that detects when the title property changes.
        ObservableElementList observableWindows = new ObservableElementList(
                context.getWindowList(), new UIComponentTitleConnector());
        // now prefix this list with two other operations.
        CompositeList comboList = new CompositeList(observableWindows.getPublisher(),observableWindows.getReadWriteLock());
        EventList statics = comboList.createMemberList();
        statics.add("Everything");
        statics.add("System");
        comboList.addMemberList(statics);
        comboList.addMemberList(observableWindows);
        
        
        subsetCombo = new JComboBox(new EventComboBoxModel(comboList));
        subsetCombo.setEditable(false);
        subsetCombo.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList list,
                    Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                String title;
                if (value instanceof String) {
                    title = (String)value;
                } else if (value != null){                    
                    title = ((UIComponent)value).getTitle();
                } else {
                    title = "Dialogue";
                }
                return super.getListCellRendererComponent(list, title, index, isSelected,
                        cellHasFocus);
            }
        });
        pb.add(subsetCombo,cc.xy(2,4));
        
// start of the eventlist pipeline
        // convert each task into a ui component.
        FunctionList taskPanels = new FunctionList(context.getTasksList(),new FunctionList.AdvancedFunction() {
            public void dispose(Object src, Object trans) {
                ((BackgroundWorkerCell)trans).cleanup();
            }
            public Object evaluate(Object arg0) {
                BackgroundWorker w = (BackgroundWorker)arg0;
                return new BackgroundWorkerCell(w);
            }
            public Object reevaluate(Object src, Object trans) {
                // background worker has updated - so reload.
                // hopefully this doesn't cause flicker..
                // if it does, just need to register each individual panel as an observer
                // on it's associated bg worker.
                ((BackgroundWorkerCell)trans).reload();
                return trans;
            }
        });
        

   // filter after mapping - to save needless re-creation of ui objects.
        // filter by listening to selection in combo box- and generate a matcher for that ui component.
        MatcherEditor matcherEditor = new AbstractMatcherEditor() {
            {// initializer                
                subsetCombo.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        int ix = subsetCombo.getSelectedIndex();
                        switch(ix) {
                            case 0:
                                fireMatchAll();
                                break;
                            case 1:
                                fireChanged(systemMatcher);
                                break;
                            default:
                                final UIComponent comp = (UIComponent)subsetCombo.getItemAt(ix);
                                fireChanged(new Matcher() {
                                    public boolean matches(Object arg0) {
                                        final BackgroundWorker worker = ((BackgroundWorkerCell)arg0).getWorker();
                                        return worker.getParent() == comp && ! worker.getInfo().isSystem();
                                    }                                    
                                });
                        }
                    }
                });
            }
            Matcher systemMatcher = new Matcher() {
                public boolean matches(Object arg0) {
                    return ((BackgroundWorkerCell)arg0).getWorker().getInfo().isSystem();
                }
            };                
        };// end matcher editor
                          
        final FilterList filteredTasks = new FilterList(taskPanels, matcherEditor);

        JButton haltAll = new JButton("Halt All");
        haltAll.setToolTipText("Halt all currently listed processes");
        haltAll.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (Iterator i = filteredTasks.iterator(); i.hasNext(); ) {
                    BackgroundWorkerCell cell = (BackgroundWorkerCell)i.next();
                    cell.getWorker().interrupt();
                }
            }
        });
        pb.add(haltAll,cc.xy(4,4));
        panel.add(pb.getPanel(),BorderLayout.NORTH);
        
        // display the list of ui components.
        JEventListPanel taskDisplay = new JEventListPanel(filteredTasks,new MonitorFormat());
        taskDisplay.setElementColumns(1);
        taskDisplay.setBorder(null);
        panel.add(new JScrollPane(taskDisplay,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)
                        , BorderLayout.CENTER);
// other ui configuration        
        getContentPane().add(panel);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setSize(400,300);
        JMenuBar mb = new JMenuBar();
        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);
        file.add(new AbstractCloseAction() {
            public void actionPerformed(ActionEvent e) {
                hide();
            }
        });
        file.addSeparator();
        file.add(UIComponentMenuBar.createExitMenuItem(context));
        mb.add(file);
        
        mb.add(context.createWindowMenu());
        
        JMenu help = new JMenu("Help");
        help.setMnemonic(KeyEvent.VK_H);
        help.add(UIComponentMenuBar.createApplicationHelpMenuItem(context,title,helpId));
        help.add(UIComponentMenuBar.createHelpContentsMenuItem(context));
        mb.add(help);
        
        setJMenuBar(mb);
        setTitle(title);
        context.getHelpServer().enableHelpKey(this.getRootPane(),helpId);
    }
    private static final String title = "Background Processes";
    private static final String helpId = "ui.backgroundProcesses";
    
/** layout for the tasks list */
    private static class MonitorFormat extends JEventListPanel.AbstractFormat {

        public MonitorFormat() {
            super("p,1dlu,p" // rows
                    ,"20dlu,2dlu,fill:40dlu:grow,2dlu,p,p" // cols
                    ,"3dlu" // row spacing
                    ,"0dlu" // colspacing.
                    , new String[] {"3,1","1,1,1,3","3,3","5,1,1,3","6,1,1,3"}
                    );        
        }
        
        public JComponent getComponent(Object arg0, int arg1) {
            BackgroundWorkerCell cell = (BackgroundWorkerCell)arg0;
            switch(arg1) {
                case 0:
                    return cell.getWorkerTitle();
                case 1:
                    return cell.getStatus();
                case 2:
                    return cell.getMessage();
                case 3:
                    return cell.getHalt();
                case 4:
                    return cell.getSingleDialogue();
                default:
                    return new JLabel("Invalid index - programming error" + arg1);                
            }
        }
        public int getComponentsPerElement() {
            return 5;
        }
    }

    
    /** bridge between the title property change and glazed lists */
    private static class UIComponentTitleConnector implements ObservableElementList.Connector, PropertyChangeListener {

        private ObservableElementList list;

        public EventListener installListener(Object arg0) {
            ((UIComponent)arg0).getComponent().addPropertyChangeListener("title",this);
            return this;
        }

        public void setObservableElementList(ObservableElementList list) {
            this.list = list;
        }

        public void uninstallListener(Object arg0, EventListener arg1) {
            if (arg1 == this) {
                ((UIComponent)arg0).getComponent().removePropertyChangeListener("title",this);
            }
        }

        public void propertyChange(PropertyChangeEvent evt) {
            list.elementChanged(evt.getSource()); // relies on UIComponent.getComponent() returning self.
        }
    }

    /** display for a standard background worker */
    public static class BackgroundWorkerCell implements ActionListener {
        private final BackgroundWorker worker;
        private final IndeterminateProgressIndicator status;
        private final JLabel workerTitle;
        private final JLabel message;
        private final JButton halt;
        private final JButton singleDialogue;
        
        private static final String HALT = "halt";
        private static final String SINGLE = "single";
        public BackgroundWorkerCell(BackgroundWorker worker) {
            this.worker = worker;
            this.status = new IndeterminateProgressIndicator();
            this.status.setDisplayedWhenStopped(true);
            this.workerTitle = new JLabel(worker.getInfo().getWorkerTitle());
            this.message = new JLabel();
            
            this.halt = new JButton(IconHelper.loadIcon("stop16.png"));
            halt.setToolTipText("Stop this process");
            halt.setActionCommand(HALT);            
            halt.addActionListener(this);
            
            this.singleDialogue = new JButton(IconHelper.loadIcon("show16.png"));
            singleDialogue.setToolTipText("Display the progress of this process in a separate window");
            singleDialogue.setActionCommand(SINGLE);
            singleDialogue.addActionListener(this);
            
            reload(); // do first populate of controls
        }
        
        public final BackgroundWorker getWorker() {
            return this.worker;
        }
        /** called when background worker has updated */
        public void reload() {
            final Info info = worker.getInfo();
            int sz = info.getProgressMessages().size();
            if (sz > 0) {
                message.setText((String)info.getProgressMessages().get(sz-1));
            }
            switch(info.getStatus()) {
                case BackgroundWorker.RUNNING:
                    if (info.getMaxProgress() == BackgroundWorker.MAX_VALUE_UNSPECIFIED) {
                        status.startAnimation();                
                    } else {
                        status.setProgress(info.getCurrentProgress(),info.getMaxProgress());
                    }
                    break;
                case BackgroundWorker.COMPLETED:
                    halt.setEnabled(false);
                    status.stopAnimation();
                    status.setDisplayedWhenStopped(false);
            }

        }
        
        public void cleanup() {
            status.stopAnimation();
        }

        public final IndeterminateProgressIndicator getStatus() {
            return this.status;
        }

        public final JLabel getWorkerTitle() {
            return this.workerTitle;
        }

        public final JLabel getMessage() {
            return this.message;
        }

        public void actionPerformed(ActionEvent e) {
            if (HALT.equals(e.getActionCommand())) {
                worker.reportProgress("Halting..");                
                worker.interrupt();
                worker.reportProgress("Halted");
            } else if (SINGLE.equals(e.getActionCommand())) {
                worker.getControl().showSingleDialogue();
            }
            
        }

        public final JButton getHalt() {
            return this.halt;
        }

        public final JButton getSingleDialogue() {
            return this.singleDialogue;
        }
        
        
    }
    
}
