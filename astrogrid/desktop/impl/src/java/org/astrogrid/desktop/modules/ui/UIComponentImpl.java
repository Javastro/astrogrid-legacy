/*$Id: UIComponentImpl.java,v 1.9 2007/01/12 13:20:05 nw Exp $
 * Created on 07-Apr-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.builtin.ShutdownListener;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.dialogs.ResultDialog;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.UIInternal;

import com.l2fprod.common.swing.StatusBar;


/** baseclass for ui components.
 *<p>
 *extends position-remembering frame, adds a progress bar / status message at the bottom, and
 *provides a worker class that indicates progress using these.
 *
 *Also provides a place to have convenient common functionality - definitions of a 
 *close operation, help menu, etc.
 *@see org.astrogrid.desktop.modules.ui.BackgroundWorker
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Apr-2005
 *
 */ 
public class UIComponentImpl extends PositionRememberingJFrame implements UIComponent, ShutdownListener {
/** convenience constant for the empty border - using this declutters the ui somewhat.*/
    protected static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder();

    /** Convenience class - a local subclass of {@link BackgroundWorker} that ties
     * into the enclosing UIComponentImpl instance.
     * Prefer {@link BackgroundWorker} if there's any chance that operations may be resuable.
     * */
     protected abstract class BackgroundOperation extends BackgroundWorker {
         public BackgroundOperation(String msg) {
             super(UIComponentImpl.this,msg);
         }
         
         public BackgroundOperation(String msg,long timeout) {
             super(UIComponentImpl.this,msg,timeout);
         }
         public BackgroundOperation(String msg,int priority) {
             super(UIComponentImpl.this,msg,priority);
         }
         public BackgroundOperation(String msg,long timeout, int priority) {
             super(UIComponentImpl.this,msg,timeout,priority);
         }
     }

     /** generic close window action */
     protected final class CloseAction extends AbstractAction {
         public CloseAction() {
             super("Close",IconHelper.loadIcon("exit_small.png"));
             this.putValue(SHORT_DESCRIPTION,"Close");
             this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_C));
         }

         public void actionPerformed(ActionEvent e) {
             hide();
             dispose();
         }
     }
     /**
     * Commons Logger for this class - can be used by subclasses too.
     */
    protected static final Log logger = LogFactory.getLog(UIComponentImpl.class);

    /** static helper method - show a well-formatted error in a popup dialogue
     * <p/>
     * classes that extend this class should call {@link #showError(String, Throwable)} instead
     */
    public static final void showError(final Component parent,String msg, Throwable e) {
        logger.info(msg,e); 
        JLabel l = new JLabel();
        Throwable innermost = e;
        while (innermost.getCause() != null) {
            innermost = innermost.getCause();            
        }
        String eMsg = null;
        if (innermost.getMessage() != null) { 
            int endOfPrefix = innermost.getMessage().lastIndexOf("Exception:") ; // try to get all nested exception messages
            if (endOfPrefix > -1) {
                eMsg = innermost.getMessage().substring(endOfPrefix+ 10);
            } else {
                eMsg = innermost.getMessage();
            }
        } else { // an exception with no message.
            eMsg = innermost.getClass().getName(); 
        }
        String errorMessage = "<html><body><b>" + msg + 
        "</b><br><b>Cause:</b> " + eMsg + "</body></html>";
        l.setText(errorMessage);
        
        int result = JOptionPane.showOptionDialog(parent,l,"An Error Occurred", 
                JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null,
                new Object[]{"Ok","Details.."}, "Ok"
                );
        if (result == 1) { // user wants to see the gory details      
            StringWriter sw = new StringWriter();        
            PrintWriter pw = new PrintWriter(sw);
            pw.println("<html><body><pre>");
            pw.println("Date of Error: " + (new Date()).toString());
            if (parent != null) {
                pw.println("Within component: " + parent.getClass().getName());
            }
            // maybe add more header info here - user, etc. - hard to get to.
        
            pw.println();
            e.printStackTrace(pw);

            if (parent != null && parent instanceof UIComponentImpl) {            
                pw.println();
                UIComponentImpl u = (UIComponentImpl)parent;
                try {
                    Map m = u.getConfiguration().list();
                    Properties props = new Properties();
                    props.putAll(m);
                    // nggg. clunky.
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    props.save(bos,"Application Configuration");
                    pw.println(bos.toString());
                } catch (ACRException ex) {
                    pw.println("Failed to list configuration");
                    ex.printStackTrace(pw);
                }
            }

            pw.println();   
            Properties sysProps = System.getProperties();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            sysProps.save(bos,"System Properties");
            pw.println(bos.toString());
        
            pw.println();
            pw.println("If you think this is a bug in the Workbench, please email this transcript to");
            pw.println("astrogrid_help@star.le.ac.uk, along with details of your username and a description");
            pw.println("of what was happening at the time of the error");
            
            // finish off the report
            pw.println("</pre></body></html>");

            // display report in a dialogue
            ResultDialog rd = new ResultDialog(parent,sw.toString());
            rd.setVisible(true);
            rd.toFront();
            rd.requestFocus();
        }
    }
    private JProgressBar bottomLabel = null;
    


    private StatusBar bottomPanel = null;
    
    private JPanel jContentPane;
    private JProgressBar progressBar = null;


    /** Construct a new UIComponentImpl
     * @param conf
     * @param ui
     * @throws HeadlessException
     */
    public UIComponentImpl(Configuration conf,HelpServerInternal hs, UIInternal ui) throws HeadlessException {
        super(conf,hs, ui);
    }
    
    /** for gui builder use only
    @deprecated - don't use in own code
    */
    
    /** access the main panel, where other components can be added..
     * @return a JPane with {@link BorderLayout}. The southern segment is already taken by the activity indicator & status message.
     */ 
    public JPanel getMainPanel() {       
        if (jContentPane == null) {
            jContentPane = new javax.swing.JPanel();
            jContentPane.setLayout(new java.awt.BorderLayout());
            jContentPane.add(getBottomPanel(), java.awt.BorderLayout.SOUTH);
            jContentPane.setBorder(EMPTY_BORDER);
        }
        return jContentPane;
    }
    
    /** @deprecated - use {@link #getMainPanel()} instead */
    protected JPanel getJContentPane() {
        return getMainPanel();
    }
    
    /** indicate execution of a background process.
     * 
     * @param b if if true, activity indicator will start throbbing. If false, activity indicator will stop.
     */
    public void setBusy(boolean b) {
        getProgressBar().setIndeterminate(b);
        getTasksButton().setEnabled(b);
    }
    
    /** set the status message at the bottom of this pane
     * 
     * @param s a message ("" to clear a previous message");
     */
    public void setStatusMessage(String s) {
        getBottomLabel().setString(s);
    }

    /** display a well-formatted error message in a popup dialogue.
     * 
     * @param msg message
     * @param e the exception that is the cause.
     */
      
     
    public void showError(String msg, Throwable e) {
        showError(this,msg,e);
    }
    
    private JProgressBar getBottomLabel() {
        if (bottomLabel == null) {
            bottomLabel = new JProgressBar(0,100);
            bottomLabel.setStringPainted(true);     
            bottomLabel.setString("");
            bottomLabel.setBorder(EMPTY_BORDER);
        }
        return bottomLabel;
    }
    
    /** set maximum value for progress bar */
    public void setProgressMax(int i) {
        getBottomLabel().setMaximum(i);
        getBottomLabel().setString("");
    }
    /** set current value in progress bar - between <tt>0</tt> and <tt>getProgressMax()</tt> */
    public void setProgressValue(int i) {
        getBottomLabel().setValue(i);
    }
    /** get the current progress value - between <tt>0</tt> and <tt>getProgressMax()</tt> */
    public int getProgressValue() {
        return getBottomLabel().getValue();
    }
    /** get the maximum value for the progress bar */
    public int getProgressMax() {
        return getBottomLabel().getMaximum();
    }

    /**
     * Access the bottom panel - status bar, messages, progress ticker, that sort of thing.
     * @return javax.swing.JPanel	
     */
    protected StatusBar getBottomPanel() {
    	if (bottomPanel == null) {
    		bottomPanel = new StatusBar();
            bottomPanel.setBorder(EMPTY_BORDER);
            bottomPanel.setZoneBorder(EMPTY_BORDER);            
            bottomPanel.addZone("status",getBottomLabel(),"*");
            bottomPanel.addZone("background tasks",getTasksButton(),"20");
            bottomPanel.addZone("help",getContextSensitiveHelpButton(),"20");
            bottomPanel.addZone("progress",getProgressBar(),"60");
    	}
    	return bottomPanel;
    }

    /**
     * This method initializes jProgressBar	
     * 	
     * @return javax.swing.JProgressBar	
     */
    private JProgressBar getProgressBar() {
    	if (progressBar == null) {
    		progressBar = new JProgressBar();
    		progressBar.setToolTipText("Activity Indicator");
            progressBar.setBorder(EMPTY_BORDER);
    	}
    	return progressBar;
    }
    
    private JButton getTasksButton() {
        if (tasksButton == null) {
            tasksButton = new JButton(IconHelper.loadIcon("stop.gif"));
            tasksButton.setEnabled(false);
            tasksButton.putClientProperty("is3DEnabled",Boolean.TRUE);
            tasksButton.setBorder(BorderFactory.createEtchedBorder());
            tasksButton.setToolTipText("<html>List background tasks.<br> Click a task to halt and cancel it.");
            tasksButton.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    getTasksMenu().show(tasksButton,e.getX(),e.getY());
                }
            });

        }
        return tasksButton;
    }
    private JButton tasksButton;
    private JButton getContextSensitiveHelpButton() {
        if (helpButton == null) {
            helpButton = new JButton(IconHelper.loadIcon("help.gif"));
            helpButton.putClientProperty("is3DEnabled",Boolean.TRUE);
            helpButton.setBorder(BorderFactory.createEtchedBorder());            
            helpButton.setToolTipText("<html><b>Click</b> for context-sensitive help,<br> or press <b>'F1'</b> for overview help</html>");
            helpButton.addActionListener(getHelpServer().createContextSensitiveHelpListener());
        }
        return helpButton;
    }
    private JButton helpButton;
    
    private JPopupMenu tasksMenu;
    
    private JPopupMenu getTasksMenu() {
        if (tasksMenu == null) {
            tasksMenu = new JPopupMenu();
            tasksMenu.setPopupSize(200,600);
            JPanel p = new JPanel(new BorderLayout());
            final JList list = new JList(getTasksModel());
            list.setToolTipText("Click task to cancel it");
            list.setCellRenderer(new DefaultListCellRenderer() {
                Icon pending = IconHelper.loadIcon("sleeping.gif");
                Icon running = IconHelper.loadIcon("flashpoint.gif");
                Icon completed = IconHelper.loadIcon("complete_status.gif");
                public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    JLabel label = (JLabel) super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
                    BackgroundWorker bw = (BackgroundWorker)value;
                    label.setText(bw.getMessage());
                    switch (bw.getStatus()) {
                        case BackgroundWorker.PENDING:
                            label.setIcon(pending);
                            break;
                        case BackgroundWorker.RUNNING:
                            label.setIcon(running);
                            break;
                         case BackgroundWorker.COMPLETED:
                             label.setIcon(completed);
                        defaut:
                            break;                           
                    }
                    return label;
                }
            });
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

                public void valueChanged(ListSelectionEvent e) {
                    Object o = list.getSelectedValue();
                    if (o == null) {
                        return;
                    }
                    ((BackgroundWorker)o).interrupt();                                        
                }
            });
            JScrollPane sp = new JScrollPane(list);
            p.add(sp,BorderLayout.CENTER);

            p.setPreferredSize(new Dimension(200,600));            
            tasksMenu.add(p);
            tasksMenu.setLabel("Running tasks");
            
            tasksMenu.add(getHaltAllButton());
        }
        return tasksMenu;
    }
    
    
    private JButton haltAllButton;
    protected JButton getHaltAllButton() {
    	if (haltAllButton == null) {
    		haltAllButton = new JButton();
    		haltAllButton.setText("Halt all tasks");
    		haltAllButton.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent e) {
    				for (Enumeration i = getTasksModel().elements(); i.hasMoreElements(); ) {
    					Object o = i.nextElement();
    					if (o != null) {
    						((BackgroundWorker)o).interrupt();
    					}
    				}
    			}
    		});
    		
    		
    	}
    	return haltAllButton;
    }
    
    
    private ObservingListModel tasksModel;
    private ObservingListModel getTasksModel() {
        if (tasksModel == null) {
            tasksModel = new ObservingListModel();
        }
        return tasksModel;
    }
    
    /** list model that spots when the contents of the model 'change' in some way, and fires a notification on this 
     * constaint - items in the model must be observables.
     * */
    static class ObservingListModel extends DefaultListModel implements Observer {
        
        public void add(int index, Object element) {
            super.add(index, element);
            ((Observable)element).addObserver(this);
        }
        public void addElement(Object obj) {
            super.addElement(obj);
            ((Observable)obj).addObserver(this);            
        }
        public void clear() {
            for (Enumeration e = elements(); e.hasMoreElements(); ) {
                ((Observable)e.nextElement()).deleteObserver(this);
            }
            super.clear();
        }
        public Object remove(int index) {
            Object o =  super.remove(index);
            ((Observable)o).deleteObserver(this);
            return o;
        }
        public void removeAllElements() {
            for (Enumeration e = elements(); e.hasMoreElements(); ) {
                ((Observable)e.nextElement()).deleteObserver(this);
            }            
            super.removeAllElements();
        }
        public boolean removeElement(Object obj) {
            boolean c =  super.removeElement(obj);
            ((Observable)obj).deleteObserver(this);
            return c;         
        }
        public void removeElementAt(int index) {
            Object o = getElementAt(index);
            ((Observable)o).deleteObserver(this);
            super.removeElementAt(index);
        }

        public Object set(int index, Object element) {
            Object o  =super.set(index, element);
            ((Observable)o).deleteObserver(this);
            ((Observable)element).addObserver(this);
            return o;
        }
        public void setElementAt(Object obj, int index) {
            Object o = getElementAt(index);
            ((Observable)o).deleteObserver(this);
            super.setElementAt(obj, index);
            ((Observable)obj).addObserver(this);
        }
        public void update(Observable o, Object arg) {
            int ix;
            if (o != null && (ix = indexOf(o)) != -1) {
                fireContentsChanged(o,ix,ix);
            }
        }
    }
   
    /** create an action that will display the help viewer */
    protected Action createHelpAction() {
    	return new AbstractAction("Help Contents") {
    		public void actionPerformed(ActionEvent e) {
    			getHelpServer().showHelpForTarget("contents");
    		}
    	};
    }
    /** create a help menu, containing a single action */
    protected JMenu createHelpMenu() {
    		JMenu helpMenu = new JMenu();
    		helpMenu.setText("Help");
    		helpMenu.setMnemonic(KeyEvent.VK_H);
    		helpMenu.add(createHelpAction());
    		return helpMenu;
    }
    
    /** called by a {@link BackgroundWorker} to notify the UI that it's started executing */ 
    public void addBackgroundWorker(final BackgroundWorker w) {        
        getTasksModel().addElement(w);
        setStatusMessage(w.getMessage());   
        setBusy(true);
  
    }
    
    /** called by {@link BackgroundWorker} to notify the UI it's finished running */
    public void removeBackgroundWorker(BackgroundWorker w) {
        getTasksModel().removeElement(w);   
        if (getTasksModel().size() == 0) {
            setBusy(false);
            setStatusMessage("");
        }
    }

    /**
     * @param s
     * @throws HeadlessException
     */
    protected void showError(String s) throws HeadlessException {
        JOptionPane.showMessageDialog(this,s,"Error",JOptionPane.ERROR_MESSAGE);
    }

	
	public JFrame getFrame() {
		return this;
	}
    // default implementation of Shutdown listener - just ensure this window gets closed.
    
    public void halting() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setVisible(false);
                dispose();
            }
        });
    }


    public String lastChance() {
        return null;
    }

    
   

    
}


/* 
$Log: UIComponentImpl.java,v $
Revision 1.9  2007/01/12 13:20:05  nw
made sure every ui app has a help menu.

Revision 1.8  2007/01/11 18:15:49  nw
fixed help system to point to ag site.

Revision 1.7  2006/08/04 15:30:22  jdt
typo

Revision 1.6  2006/07/20 12:32:56  nw
altered position of help button, so is visible on mac.

Revision 1.5  2006/06/27 19:14:56  nw
adjusted todo tags.

Revision 1.4  2006/06/27 10:37:10  nw
added methods to provide a default help menu

Revision 1.3  2006/04/26 15:56:54  nw
added 'halt query' and 'halt all tasks' functinaltiy.

Revision 1.2  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.1.2.1  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.12.6.1  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.12  2006/02/09 15:40:01  nw
finished refactoring of astroscope.
added vospec viewer

Revision 1.11  2005/12/02 13:41:20  nw
improved task-reporting system

Revision 1.10  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.9  2005/11/22 12:14:20  pjn3
Bug #1472 - clear status message once background task finishes

Revision 1.8  2005/11/11 10:08:18  nw
cosmetic fixes

Revision 1.7  2005/11/01 09:19:46  nw
messsaging for applicaitons.

Revision 1.6  2005/10/18 16:52:49  nw
fixes to error-reporting

Revision 1.5  2005/10/18 08:37:44  nw
finished error reporting.

Revision 1.4  2005/10/17 10:49:03  KevinBenson
First draft of the change to the error dialog box.

Revision 1.3  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.2.10.2  2005/10/12 09:21:38  nw
added java help system

Revision 1.2.10.1  2005/10/10 16:24:29  nw
reviewed phils workflow builder
skeletal javahelp

Revision 1.2  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.6  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.5  2005/06/22 08:48:52  nw
latest changes - for 1.0.3-beta-1

Revision 1.4  2005/06/20 16:56:40  nw
fixes for 1.0.2-beta-2

Revision 1.3  2005/04/27 13:42:40  clq2
1082

Revision 1.2.2.3  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2.2.2  2005/04/22 10:55:32  nw
implemented vospace file chooser dialogue.

Revision 1.2.2.1  2005/04/15 13:00:45  nw
got vospace browser working.

Revision 1.2  2005/04/13 12:59:18  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/04/13 12:23:29  nw
refactored a common base class for ui components
 
*/