/*$Id: UIComponentImpl.java,v 1.13 2007/05/02 15:38:29 nw Exp $
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
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.builtin.ShutdownListener;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.dialogs.ResultDialog;
import org.astrogrid.desktop.modules.plastic.PlasticApplicationDescription;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.comp.EventListPopupMenuManager;
import org.astrogrid.desktop.modules.ui.comp.MessageTimerProgressBar;

import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.FunctionList;
import ca.odell.glazedlists.RangeList;
import ca.odell.glazedlists.matchers.Matcher;
import ca.odell.glazedlists.swing.EventListModel;

import com.l2fprod.common.swing.StatusBar;


/** baseclass for ui components.
 *<p>
 *extends position-remembering frame, adds a progress bar / status message at the bottom, and
 *provides a worker class that indicates progress using these.
 *
 *Also provides a place to have convenient common functionality - definitions of a 
 *close operation, help menu, etc.
 *@see org.astrogrid.desktop.modules.ui.BackgroundWorker
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 07-Apr-2005
 *
 */ 
public class UIComponentImpl extends PositionRememberingJFrame implements UIComponent, ShutdownListener {
/** convenience constant for the empty border - using this declutters the ui somewhat.*/
    public static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder();

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
                UIComponent u = (UIComponent)parent;
                try {
                    Map m = u.getContext().getConfiguration().list();
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

     /** Construct a new UIComponentImpl
     * @param context
     * @param ui
     * @throws HeadlessException
     */
    public UIComponentImpl(UIContext context) throws HeadlessException {
        super(context);
        context.registerWindow(this);
        tasksList = new FilterList(context.getTasksList(),new Matcher() {
			public boolean matches(Object arg0) {
				BackgroundWorker w = (BackgroundWorker)arg0;
				return w.special || w.parent == UIComponentImpl.this;
			}
        });
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
            	getContext().unregisterWindow(UIComponentImpl.this);
            	setVisible(false);
            	// detach myself from the context.
            	//should I do any further cleanup - listeners, etc?
            	throbber.setModel(null);
            	tasksList.dispose();
            	((EventListModel)plasticList.getModel()).dispose();
            	login.setModel(null);
            	dispose();
            }
        });        
    }
     private JProgressBar progressBar;
    private StatusBar bottomPanel;
    private JButton helpButton;
    private JPanel jContentPane;
    private JButton login;
    private JList plasticList;
    private JButton tasksButton;
    private final FilterList tasksList;
    private JButton throbber;
    
    
    public JFrame getFrame() {
		return this;
	}
    // default implementation of Shutdown listener - just ensure this window gets closed.
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
    /** get the maximum value for the progress bar */
    public int getProgressMax() {
        return getProgressBar().getMaximum();
    }

    /** get the current progress value - between <tt>0</tt> and <tt>getProgressMax()</tt> */
    public int getProgressValue() {
        return getProgressBar().getValue();
    }

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
    
    
    /** display a well-formatted error message in a popup dialogue.
     * 
     * @param msg message
     * @param e the exception that is the cause.
     */
      
    public void showError(String msg, Throwable e) {
        showError(this,msg,e);
    }
    private JProgressBar getProgressBar() {
        if (progressBar == null) {
            progressBar = new MessageTimerProgressBar(0,100);
            progressBar.setBorder(EMPTY_BORDER);
        }
        return progressBar;
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
    
    private StatusBar getBottomPanel() {
    	if (bottomPanel == null) {
    		bottomPanel = new StatusBar();
            bottomPanel.setBorder(EMPTY_BORDER);
            bottomPanel.setZoneBorder(EMPTY_BORDER);   
            
            bottomPanel.addZone("throbber",getThrobber(),"20");
            bottomPanel.addZone("background tasks",getTasksButton(),"20");

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
            helpButton.addActionListener(getContext().getHelpServer().createContextSensitiveHelpListener());
        }
        return helpButton;
    }
    

	private JComponent getLogin() {
		if (login == null) {
			login = new JButton();
			login.setModel(getContext().getLoggedInModel());
			login.setText("");
	        login.setDisabledIcon(IconHelper.loadIcon("connect_no16.png"));
	        login.setIcon(IconHelper.loadIcon("connect_established16.png"));
	        login.setToolTipText("Indicates login status");
		}
		return login;
	}
    private JList getPlasticList() {
		if (plasticList == null) {
			plasticList = new JList(new EventListModel(getContext().getPlasticList()));
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
    
    private JButton getTasksButton() {
        if (tasksButton == null) {
            tasksButton = new JButton(IconHelper.loadIcon("loader.gif"));
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
            	Icon completed = IconHelper.loadIcon("tick16.png");
            	Icon pending = IconHelper.loadIcon("idle16.png");
            	Icon running = IconHelper.loadIcon("loader.gif");
            	public Object evaluate(Object arg0) {
            		BackgroundWorker w = (BackgroundWorker)arg0;
            		JMenuItem mi = new JMenuItem(w.getMessage());
            		mi.putClientProperty(BackgroundWorker.class,w); // store the worker for this menu item.
            		mi.addActionListener(l);
            		switch (w.getStatus()) {
            		case BackgroundWorker.PENDING:
            			mi.setIcon(pending);
            			break;
            		case BackgroundWorker.RUNNING:
            			mi.setIcon(running);
            			break;
            		case BackgroundWorker.COMPLETED:
            			mi.setIcon(completed);
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
	        throbber.setModel(getContext().getThrobbingModel());
	        throbber.setText("");
	        throbber.setIcon(IconHelper.loadIcon("running16.png"));
	       // throbber.setIcon(IconHelper.loadIcon("circle-ball-dark-antialiased.gif"));
	       // throbber.setIcon(IconHelper.loadIcon("loader.gif"));
	        throbber.setDisabledIcon(IconHelper.loadIcon("idle16.png"));   
	        throbber.setToolTipText("When active, something is communicating with VO services");
	    }
	    return throbber;
	}

    /** create an action that will display the help viewer */
    protected Action createHelpAction() {
    	return new AbstractAction("Help Contents") {
    		public void actionPerformed(ActionEvent e) {
    			getContext().getHelpServer().showHelpForTarget("contents");
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
    
    /** @deprecated - use {@link #getMainPanel()} instead */
    protected JPanel getJContentPane() {
        return getMainPanel();
    }


    /**
     * @param s
     * @throws HeadlessException
     */
    protected void showError(String s) throws HeadlessException {
        JOptionPane.showMessageDialog(this,s,"Error",JOptionPane.ERROR_MESSAGE);
    }

	/** Convenience class - a local subclass of {@link BackgroundWorker} that ties
     * into the enclosing UIComponentImpl instance.
     * Prefer {@link BackgroundWorker} if there's any chance that operations may be resuable.
     * */
     protected abstract class BackgroundOperation extends BackgroundWorker {
         public BackgroundOperation(String msg) {
             super(UIComponentImpl.this,msg);
         }
         
         public BackgroundOperation(String msg,int priority) {
             super(UIComponentImpl.this,msg,priority);
         }
         public BackgroundOperation(String msg,long timeout) {
             super(UIComponentImpl.this,msg,timeout);
         }
         public BackgroundOperation(String msg,long timeout, int priority) {
             super(UIComponentImpl.this,msg,timeout,priority);
         }
     }

	/** generic close window action */
     public final class CloseAction extends AbstractAction {
         public CloseAction() {
             super("Close");
             this.putValue(SHORT_DESCRIPTION,"Close");
             this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_C));
         }
         public void actionPerformed(ActionEvent e) {
             hide();
             getContext().unregisterWindow(UIComponentImpl.this);
             dispose();
         }
     }

    
     
}


/* 
$Log: UIComponentImpl.java,v $
Revision 1.13  2007/05/02 15:38:29  nw
changes for 2007.3.alpha1

Revision 1.12  2007/04/18 15:47:05  nw
tidied up voexplorer, removed front pane.

Revision 1.11  2007/03/08 17:43:59  nw
first draft of voexplorer

Revision 1.10  2007/01/29 11:11:37  nw
updated contact details.

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