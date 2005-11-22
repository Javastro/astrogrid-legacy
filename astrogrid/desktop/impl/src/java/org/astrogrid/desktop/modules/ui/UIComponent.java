/*$Id: UIComponent.java,v 1.9 2005/11/22 12:14:20 pjn3 Exp $
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

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.dialogs.ResultDialog;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.UIInternal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.l2fprod.common.swing.StatusBar;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;


/** baseclass for ui components.
 *<p>
 *extends position-remembering frame, adds a progress bar / status message at the bottom, and
 *provides a worker class that indicates progress using these.
 *@see org.astrogrid.desktop.modules.ui.BackgroundWorker
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Apr-2005
 *
 */
public class UIComponent extends PositionRememberingJFrame {
            

    /** Convenience class - a local subclass of {@link BackgroundWorker} that ties
     * into the enclosing UIComponent instance.
     * Prefer {@link BackgroundWorker} if there's any chance that operations may be resuable.
     * */
     protected abstract class BackgroundOperation extends BackgroundWorker {
         public BackgroundOperation(String msg) {
             super(UIComponent.this,msg);
         }
     }

     /**
     * Commons Logger for this class - can be used by subclasses too.
     */
    protected static final Log logger = LogFactory.getLog(UIComponent.class);

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

            if (parent != null && parent instanceof UIComponent) {            
                pw.println();
                UIComponent ui = (UIComponent)parent;
                try {
                    Map m = ui.configuration.list();
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
        }
    }
    private JLabel bottomLabel = null;
    


    private StatusBar bottomPanel = null;
    
    private JPanel jContentPane;
    private JProgressBar progressBar = null;


    /** Construct a new UIComponent
     * @param conf
     * @param ui
     * @throws HeadlessException
     */
    public UIComponent(Configuration conf,HelpServerInternal hs, UIInternal ui) throws HeadlessException {
        super(conf,hs, ui);
    }
    
    /** access the main panel, where other components can be added..
     * 
     * @return a JPane with {@link BorderLayout}. The southern segment is already taken by the activity indicator & status message.
     */ 
    public JPanel getMainPanel() {       
        if (jContentPane == null) {
            jContentPane = new javax.swing.JPanel();
            jContentPane.setLayout(new java.awt.BorderLayout());
            jContentPane.add(getBottomPanel(), java.awt.BorderLayout.SOUTH);            
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
        getBottomLabel().setText(s);
    }

    /** display a well-formatted error message in a popup dialogue.
     * 
     * @param msg message
     * @param e the exception that is the cause.
     */
      
     
    public void showError(String msg, Throwable e) {
        showError(this,msg,e);
    }
    
    private JLabel getBottomLabel() {
        if (bottomLabel == null) {
            bottomLabel = new JLabel();
            bottomLabel.setText(" ");
        }
        return bottomLabel;
    }

    /**
     * Access the bottom panel - status bar, messages, progress ticker, that sort of thing.
     * @return javax.swing.JPanel	
     */
    protected StatusBar getBottomPanel() {
    	if (bottomPanel == null) {
    		bottomPanel = new StatusBar();
            bottomPanel.addZone("status",getBottomLabel(),"*");
            bottomPanel.addZone("background tasks",getTasksButton(),"20");
            bottomPanel.addZone("progress",getProgressBar(),"60");
            bottomPanel.addZone("help",getHelpButton(),"20");
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
    	}
    	return progressBar;
    }
    
    private JButton getTasksButton() {
        if (tasksButton == null) {
            tasksButton = new JButton(IconHelper.loadIcon("stop.gif"));
            tasksButton.setEnabled(false);
            tasksButton.setToolTipText("Halt running background processes");
            tasksButton.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    getTasksMenu().show(tasksButton,e.getX(),e.getY());
                }
            });

        }
        return tasksButton;
    }
    private JButton tasksButton;
    
    private JButton getHelpButton() {
        if (helpButton == null) {
            helpButton = new JButton(IconHelper.loadIcon("help.gif"));
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
            tasksMenu.setLabel("Running processes:");
        }
        return tasksMenu;
    }
    
    /** called by a {@link BackgroundWorker} to notify the UI that it's started executing */ 
    public void addBackgroundWorker(final BackgroundWorker w) {
        getTasksMenu().add(w.getMenuItem());
        setStatusMessage(w.getMessage());   
        setBusy(true);
  
    }
    
    /** called by {@link BackgroundWorker} to notify the UI it's finished running */
    public void removeBackgroundWorker(BackgroundWorker w) {
        int ix = getTasksMenu().getComponentIndex(w.getMenuItem());
        if (ix >= 0) {
            getTasksMenu().remove(ix);
        }
        if (getTasksMenu().getSubElements().length == 0) {
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
    
    
    
    
}


/* 
$Log: UIComponent.java,v $
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