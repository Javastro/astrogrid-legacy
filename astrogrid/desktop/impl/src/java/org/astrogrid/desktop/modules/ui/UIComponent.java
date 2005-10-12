/*$Id: UIComponent.java,v 1.3 2005/10/12 13:30:10 nw Exp $
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

import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.system.HelpServer;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.UIInternal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.l2fprod.common.swing.StatusBar;

import EDU.oswego.cs.dl.util.concurrent.misc.SwingWorker;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

import javax.help.CSH;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

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

    /** static helper method - qucick way to show a well-formatted error in a popup dialogue
     * <p/>
     * classes that extend this class should call {@link #showError(String, Throwable)} instead
     */
    public static final void showError(Component parent,String msg, Throwable e) {
        logger.info(msg,e); 
        JLabel l = new JLabel();
        l.setText("<html><b>" + msg +"</b></html");
        JEditorPane resultDisplay = new JEditorPane();
        resultDisplay.setEditable(false);
        resultDisplay.setContentType("text/html");
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        resultDisplay.setText("<html><b>Cause</b><br><pre>" + sw+ "</pre></html>");
        resultDisplay.setCaretPosition(0);
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.add(l,BorderLayout.NORTH);
        JScrollPane js = new JScrollPane(resultDisplay);
        js.setPreferredSize(new Dimension(400,150));                          
        p.add(js,BorderLayout.SOUTH);
 
        JOptionPane.showMessageDialog(parent,p,"Error",JOptionPane.ERROR_MESSAGE);        
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