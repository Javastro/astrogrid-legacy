/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.ListCellRenderer;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.plastic.PlasticApplicationDescription;
import org.astrogrid.desktop.modules.ui.comp.EventListPopupMenuManager;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;
import org.astrogrid.desktop.modules.ui.comp.IndeterminateProgressIndicator;
import org.astrogrid.desktop.modules.ui.comp.MessageTimerProgressBar;
import org.astrogrid.desktop.modules.ui.comp.TimedPopup;
import org.astrogrid.desktop.modules.ui.comp.UIConstants;

import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.FunctionList;
import ca.odell.glazedlists.RangeList;
import ca.odell.glazedlists.matchers.Matcher;
import ca.odell.glazedlists.swing.EventListModel;

import com.l2fprod.common.swing.StatusBar;
/**
 * common implementation of most of the UIComponent functionality.
 * as we want an implementation of UIComponent that extends JFrame, and an
 * implementation of UIComponent that extends JDialog, this functionality
 * can't be placed in a baseclass common to both - instead it's in this separate
 * class - an instance of this can be created in the UIComponent implementation,
 * and then delegated to for most of the public methods.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 12, 200712:01:08 PM
 */
public final class UIComponentAssist {
    /** construct a new uicomponentassist
     * 
     * @param parent the uicomponent which 'owns' and is 'delagating'
     * to this assistant.
     */
    public UIComponentAssist(final UIComponent parent) {
        super();
        this.parent = parent;
        tasksList = new FilterList(parent.getContext().getTasksList(),new Matcher() {
            public boolean matches(Object arg0) {
                BackgroundWorker w = (BackgroundWorker)arg0;
                return w.special || w.parent == parent;
            }
        });            
    }

    private StatusBar bottomPanel;
    private JButton helpButton;
    private JButton login;
    private JPanel mainPane;
    private final UIComponent parent;
    private JList plasticList;
    private JProgressBar progressBar;
    private JButton tasksButton;
    private final FilterList tasksList;
    private JButton throbber;

    /** detach this class from the Context, stop listening to things, etc */
    public void cleanup() {
        //should I do any further cleanup - listeners, etc?
        throbber.setModel(null);
        tasksList.dispose();
        ((EventListModel)plasticList.getModel()).dispose();
        login.setModel(null);            
    }
    
    public JPanel getMainPanel() {       
        if (mainPane == null) {
            mainPane = new javax.swing.JPanel();
            mainPane.setLayout(new java.awt.BorderLayout());
            mainPane.add(getBottomPanel(), java.awt.BorderLayout.SOUTH);
            mainPane.setBorder(null);
        }
        return mainPane;
    }
    
    /** get the maximum value for the progress bar */
    public int getProgressMax() {
        return getProgressBar().getMaximum();
    }
    
    
    /** get the current progress value - between <tt>0</tt> and <tt>getProgressMax()</tt> */
    public int getProgressValue() {
        return getProgressBar().getValue();
    }        
    /** halt all tasks owned by this component (and not special) */
    public void haltMyTasks() {
        for (Iterator i = tasksList.iterator(); i.hasNext();) {
            BackgroundWorker w = (BackgroundWorker) i.next();
            if (!w.special) {
                w.interrupt();
            }
        }
    }
    /** set maximum value for progress bar */
    public void setProgressMax(int i) {
        getProgressBar().setMaximum(i);
        getProgressBar().setString("");
    }
    /** set current value in progress bar - between <tt>0</tt> and <tt>getProgressMax()</tt> */
    public void setProgressValue(int i) {
        getProgressBar().setValue(i);
    }
    /** set the status message at the bottom of this pane
     * 
     * @param s a message ("" to clear a previous message");
     */
    public void setStatusMessage(String s) {
        getProgressBar().setString(s);
    }
    
    /**
     * @param s
     * @throws HeadlessException
     */
    public void showError(String s) {
        JOptionPane.showMessageDialog(parent.getComponent(),s,"Error",JOptionPane.ERROR_MESSAGE);
    }
    /** display a well-formatted error message in a popup dialogue.
     * 
     * @param msg message
     * @param e the exception that is the cause.
     */
      
    public void showError(String msg, Throwable e) {
        ExceptionFormatter.showError(parent.getComponent(),msg,e);
    }
    /** display an error message in a popup, which will vanish after a few seconds */
    public void showTransientError(String title, String message) {
        TimedPopup.showErrorMessage(getTasksButton(),title,message);
    }
    /** display a message in a popup, which will vanish after a few seconds */
    public void showTransientMessage(String title, String message) {
        TimedPopup.showInfoMessage(getTasksButton(),title,message);
    }        
    /** display a warning in a popup, which will vanish after a few seconds */
        public void showTransientWarning(String title, String message) {
            TimedPopup.showWarningMessage(getTasksButton(),title,message);     
        }
    private StatusBar getBottomPanel() {
        if (bottomPanel == null) {
            bottomPanel = new StatusBar();
            bottomPanel.setBorder(null);
            bottomPanel.setZoneBorder(null);   
            
            bottomPanel.addZone("throbber",getThrobber(),"20");
            bottomPanel.addZone("background tasks",getTasksButton(),"25");
            
            bottomPanel.addZone("status",getProgressBar(),"*");
            bottomPanel.addZone("help",getContextSensitiveHelpButton(),"20");
            bottomPanel.addZone("login",getLogin(),"18");
            bottomPanel.addZone("plasticApps",getPlasticList(),"100");            
        }
        return bottomPanel;
    }
    
    private JButton getContextSensitiveHelpButton() {
        if (helpButton == null) {
            helpButton = new JButton(IconHelper.loadIcon("contexthelp18.png"));
            helpButton.putClientProperty("is3DEnabled",Boolean.TRUE);
            helpButton.setBorder(BorderFactory.createEtchedBorder());            
            helpButton.setToolTipText("<html><b>Click</b> for context-sensitive help,<br> or press <b>'F1'</b> for overview help</html>");
            helpButton.addActionListener(parent.getContext().getHelpServer().createContextSensitiveHelpListener());
        }
        return helpButton;
    }
    
    
    private JComponent getLogin() {
        if (login == null) {
            login = new JButton();
            login.setModel(parent.getContext().getLoggedInModel());
            login.setText("");
            login.setDisabledIcon(IconHelper.loadIcon("connect_no16.png"));
            login.setIcon(IconHelper.loadIcon("connect_established16.png"));
            login.setToolTipText("Indicates login status");
        }
        return login;
    }
    private JList getPlasticList() {
        if (plasticList == null) {
            plasticList = new JList(new EventListModel(parent.getContext().getPlasticList()));
            //plasticApps.setToolTipText("Connected Vizualization Tools");
            plasticList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
            plasticList.setVisibleRowCount(1);
            plasticList.setFocusable(false);
            plasticList.setBorder(BorderFactory.createEtchedBorder());
            plasticList.setCellRenderer(new ListCellRenderer() {
                JLabel l = new JLabel();
                {
                    l.setMaximumSize(new Dimension(16,16));
                    l.setFont(new Font("Dialog",Font.PLAIN,8));
                }
                public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    PlasticApplicationDescription plas = (PlasticApplicationDescription)value;
                    if (plas.getIcon() != null) {
                        ImageIcon scaled = new ImageIcon((plas.getIcon()).getImage().getScaledInstance(-1,16,Image.SCALE_SMOOTH));
                        l.setIcon(scaled);
                    } else {
                        l.setIcon(IconHelper.loadIcon("plasticeye.gif"));
                    }
                    l.setToolTipText(StringUtils.capitalize(plas.getName() + " is connected"));
                    return l;
                }
            });
            
        }
        return plasticList;
    }
    
    private JProgressBar getProgressBar() {
        if (progressBar == null) {
            progressBar = new MessageTimerProgressBar(0,100);
            progressBar.setBorder(null);
        }
        return progressBar;
    }
    
    
    private JButton getTasksButton() {
        if (tasksButton == null) {
            tasksButton = new IndeterminateProgressIndicator.Button();
            tasksButton.putClientProperty("is3DEnabled",Boolean.TRUE);
            tasksButton.setBorder(BorderFactory.createEtchedBorder());
            tasksButton.setToolTipText("<html>List background tasks.<br> Click a task to halt and cancel it.");
            final JPopupMenu tasksMenu = new JPopupMenu();
            tasksButton.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    tasksMenu.show(tasksButton,e.getX(),e.getY());
                }
            });
            RangeList rangeList = new RangeList(tasksList);
            rangeList.setHeadRange(0,30); // only display 30 items.
            final ActionListener l  = new ActionListener() {// handler for menu items.
                public void actionPerformed(ActionEvent e) {
                    // retrive the worker for this item.
                    BackgroundWorker w = (BackgroundWorker) ((JComponent)e.getSource()).getClientProperty(BackgroundWorker.class);
                    w.interrupt();
                    //@todo display a fuller management dialogue here instead.
                }
            };
            final FunctionList jcomponentList = new FunctionList(rangeList,new FunctionList.Function() {
                
                public Object evaluate(Object arg0) {
                    BackgroundWorker w = (BackgroundWorker)arg0;
                    JMenuItem mi = new JMenuItem(w.getMessage());
                    mi.putClientProperty(BackgroundWorker.class,w); // store the worker for this menu item.
                    mi.addActionListener(l);
                    switch (w.getStatus()) {
                        case BackgroundWorker.PENDING:
                            mi.setIcon(UIConstants.PENDING_ICON);
                            break;
                        case BackgroundWorker.RUNNING:
                            mi.setIcon(UIConstants.RUNNING_ICON);
                            break;
                        case BackgroundWorker.COMPLETED:
                            mi.setIcon(UIConstants.COMPLETED_ICON);
                            break;                         
                    }
                    return mi;    
                }
            });
            // utility class that manages populating the popup.
            new EventListPopupMenuManager(jcomponentList,tasksButton,tasksMenu,false);
        }
        return tasksButton;
    }
    private JComponent getThrobber() {
        if (throbber == null) {
            throbber = new JButton();
            throbber.setModel(parent.getContext().getThrobbingModel());
            throbber.setText("");
            throbber.setIcon(IconHelper.loadIcon("running16.png"));
            // throbber.setIcon(IconHelper.loadIcon("circle-ball-dark-antialiased.gif"));
            // throbber.setIcon(IconHelper.loadIcon("loader.gif"));
            throbber.setDisabledIcon(IconHelper.loadIcon("idle16.png"));   
            throbber.setToolTipText("When active, something is communicating with VO services");
        }
        return throbber;
    }
}