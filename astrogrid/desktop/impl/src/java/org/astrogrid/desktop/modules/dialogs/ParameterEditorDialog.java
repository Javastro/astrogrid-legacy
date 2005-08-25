/*$Id: ParameterEditorDialog.java,v 1.2 2005/08/25 16:59:58 nw Exp $
 * Created on 23-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.dialogs;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.Unmarshaller;
import org.w3c.dom.Document;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
/** dialog that allows the user to edit a tool document - i.e. a set of parameters.
 * 
 * <p>
 * tool editing business done by {@link org.astrogrid.desktop.modules.dialogs.ParametersPanel}
 * this class is just concerned with the dialogue side of thinigs
 * @author Noel Winstanley nw@jb.man.ac.uk 23-Mar-2005
 *
 */
class ParameterEditorDialog extends JDialog implements PropertyChangeListener {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ParameterEditorDialog.class);
    
    protected final ResourceChooserInternal resourceChooser;
    
    private JOptionPane jOptionPane = null;
    private ParametersPanel parametersPanel = null;
    
   	private JLabel topLabel = null;
    
    public ParameterEditorDialog(ResourceChooserInternal chooser) {
        super();          
        resourceChooser =chooser;
        this.setTitle("Parameter Editor");
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
            /*
             * Instead of directly closing the window,
             * we're going to change the JOptionPane's
             * value property.
             */
                jOptionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
        }
    });                
        this.setModal(true);
        this.setContentPane(getJOptionPane());   
    }
    
    public void resetAndHide() {
        setVisible(false);
        getTopLabel().setText(null);
        getTopLabel().setToolTipText(null);
        getParametersPanel().clear();
    }
    
    public Tool getTool() {
        return getParametersPanel().getTool();        
    }
    
    public void populate(Tool t, ApplicationInformation desc) {
        getParametersPanel().populate(t,desc);
        getTopLabel().setText(desc.getName());
        getTopLabel().setToolTipText(desc.getDescription());
    }
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        if (isVisible()
         && (e.getSource() == jOptionPane)
         && (JOptionPane.VALUE_PROPERTY.equals(prop) ||
             JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
            Object value = jOptionPane.getValue();

            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                //ignore reset
                return;
            }

            //Reset the JOptionPane's value.
            //If you don't do this, then if the user
            //presses the same button next time, no
            //property change event will be fired.
            jOptionPane.setValue(
                    JOptionPane.UNINITIALIZED_VALUE);

            if (JOptionPane.OK_OPTION == ((Integer)value).intValue()) {
                    resetAndHide();                
            } else { //user closed dialog or clicked cancel           
              getParametersPanel().clearTool();
                resetAndHide();
            }
        }
    }
    
	/**
     * @return
     */
    private JOptionPane getJOptionPane() {
       if (jOptionPane == null) {
           jOptionPane = new JOptionPane(new JComponent[]{getTopLabel(), getParametersPanel()},JOptionPane.PLAIN_MESSAGE,JOptionPane.OK_CANCEL_OPTION);
           jOptionPane.addPropertyChangeListener(this);
       }
       return jOptionPane;
    }

    
    private ParametersPanel getParametersPanel() {
        if (parametersPanel == null) {
            parametersPanel = new ParametersPanel(resourceChooser);
        }
        return parametersPanel;
    }
    
    private JLabel getTopLabel() {
        if (topLabel == null) {
            topLabel = new JLabel();
        }
        return topLabel;
    }
      
   
}  //  @jve:decl-index=0:visual-constraint="10,10"


/* 
$Log: ParameterEditorDialog.java,v $
Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.7  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.6  2005/07/08 11:08:01  nw
bug fixes and polishing for the workshop

Revision 1.5  2005/06/23 09:08:26  nw
changes for 1.0.3 release

Revision 1.4  2005/06/22 08:48:52  nw
latest changes - for 1.0.3-beta-1

Revision 1.3  2005/06/08 14:51:59  clq2
1111

Revision 1.2.8.1  2005/06/02 14:34:32  nw
first release of application launcher

Revision 1.2  2005/04/27 13:42:41  clq2
1082

Revision 1.1.2.1  2005/04/22 10:54:03  nw
start of a new module.

Revision 1.2  2005/04/13 12:59:18  nw
checkin from branch desktop-nww-998

Revision 1.1.2.3  2005/04/04 16:43:48  nw
made frames remember their previous positions.
synchronized guiLogin, so only one login box ever comes up.
made refresh action in jobmonitor more robust

Revision 1.1.2.2  2005/04/04 08:49:27  nw
working job monitor, tied into pw launcher.

Revision 1.1.2.1  2005/03/23 14:36:18  nw
got pw working
 
*/