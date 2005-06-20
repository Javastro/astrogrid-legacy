/*$Id: UIComponent.java,v 1.4 2005/06/20 16:56:40 nw Exp $
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
import org.astrogrid.acr.system.UI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import EDU.oswego.cs.dl.util.concurrent.misc.SwingWorker;

import java.awt.Component;
import java.awt.HeadlessException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/** abstract baseclass for ui components.
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Apr-2005
 *
 */
public class UIComponent extends PositionRememberingJFrame {

    /** Construct a new UIComponent
     * @throws HeadlessException
     */
    public UIComponent() throws HeadlessException {
        super();
    }

    /** Construct a new UIComponent
     * @param conf
     * @param ui
     * @throws HeadlessException
     */
    public UIComponent(Configuration conf,HelpServer hs, UI ui) throws HeadlessException {
        super(conf,hs, ui);
    }

    
    
    private JPanel bottomPanel = null;
    private JLabel bottomLabel = null;
    private JProgressBar progressBar = null;

    /**
     * This method initializes jPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    public JPanel getBottomPanel() {
    	if (bottomPanel == null) {
    		bottomPanel = new JPanel();
    		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
    		bottomPanel.add(getBottomLabel(), null);
    		bottomPanel.add(getProgressBar(), null);
    	}
    	return bottomPanel;
    }

    /**
     * This method initializes jProgressBar	
     * 	
     * @return javax.swing.JProgressBar	
     */
    protected JProgressBar getProgressBar() {
    	if (progressBar == null) {
    		progressBar = new JProgressBar();
    		progressBar.setToolTipText("Activity Indicator");
    	}
    	return progressBar;
    }
    
    protected JLabel getBottomLabel() {
        if (bottomLabel == null) {
            bottomLabel = new JLabel();
            bottomLabel.setText(" ");
        }
        return bottomLabel;
    }
    
    protected void setStatusMessage(String s) {
        getBottomLabel().setText(s);
    }
    
    protected void setBusy(boolean b) {
        getProgressBar().setIndeterminate(b);
    }
    
    private JPanel jContentPane;
    
    protected JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new javax.swing.JPanel();
            jContentPane.setLayout(new java.awt.BorderLayout());
            jContentPane.add(getBottomPanel(), java.awt.BorderLayout.SOUTH);            
        }
        return jContentPane;
    }

    protected void showError(String msg, Throwable e) {
        showError(this,msg,e);
    }
    
    // static variant - handy to have.
    public static final void showError(Component parent,String msg, Throwable e) {
        logger.info(msg,e); 
        JOptionPane.showMessageDialog(parent,msg + "\nCause\n" + e,"Error",JOptionPane.ERROR_MESSAGE);        
    }

    /**
     * Commons Logger for this class
     */
    protected static final Log logger = LogFactory.getLog(UIComponent.class);
            

    /** abstract class for all background threads - takes care of starting and  stopping
     * notification, etc
     * @author Noel Winstanley nw@jb.man.ac.uk 02-Apr-2005
     *
     */
    protected abstract class BackgroundOperation extends SwingWorker {
        /**
         *  Construct a new BackgroundOperation
         * @param msg message to display in status bar.
         */
        public BackgroundOperation(String msg) {
            super();
            this.msg = msg;
        }
        protected final String msg;
        
        public synchronized void start() {
            setBusy(true);
            setStatusMessage(msg);            
            super.start();
        }        
            protected final void finished() {
                try {
                    Object result = this.get();
                    doFinished(result);
                } catch (InterruptedException e) {
                    // not bothered.
                } catch (InvocationTargetException e) {
                    Throwable ex= e.getCause() != null ? e.getCause() : e;
                    showError(msg + ": Failed",ex);
                } finally {
                    setBusy(false);
                    setStatusMessage("");
                    doAlways();
                }
        }
        /** when finished, back in event-dispatching thread */
        protected void doFinished(Object result) {
        }
        /** always executed. */
        protected void doAlways() {
        }
        

}
    
}


/* 
$Log: UIComponent.java,v $
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