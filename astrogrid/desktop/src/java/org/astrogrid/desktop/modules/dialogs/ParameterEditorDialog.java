/*$Id: ParameterEditorDialog.java,v 1.6 2005/07/08 11:08:01 nw Exp $
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

import org.astrogrid.acr.astrogrid.Myspace;
import org.astrogrid.applications.beans.v1.Interface;
import org.astrogrid.applications.beans.v1.ParameterRef;
import org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.javasource.JConstructor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Enumeration;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 23-Mar-2005
 *
 */
public class ParameterEditorDialog extends JDialog {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ParameterEditorDialog.class);

	private JPanel jContentPane = null;
	private JPanel buttonBox = null;
	private JButton executeButton = null;
	private JButton cancelButton = null;
    private ParametersPanel parametersPanel = null;
    private Tool tool;

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getButtonBox() {
		if (buttonBox == null) {
			buttonBox = new JPanel();
			buttonBox.add(getExecuteButton(), null);
			buttonBox.add(getCancelButton(), null);
		}
		return buttonBox;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getExecuteButton() {
		if (executeButton == null) {
			executeButton = new JButton() {{
			        setText("Execute");
			        setToolTipText("Submit the workflow for execution");
			        addActionListener(new java.awt.event.ActionListener() { 
			            public void actionPerformed(java.awt.event.ActionEvent e) {    
                            tool = getParametersPanel().getTool();
			                dispose(); // causes dialogue to return
			            }
			        });
            }};
		}
		return executeButton;
	}
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton() {{
			    setText("Cancel");
			    addActionListener(new java.awt.event.ActionListener() { 
			        public void actionPerformed(java.awt.event.ActionEvent e) {    
			            tool  = null; // we've canceled, so don't return anything.                        
			            dispose();
			        }
			    });
            }};
		}
		return cancelButton;
	}


	/**
	 * This is the default constructor
	 */
	public ParameterEditorDialog() {
		super();
        desc = null;
        resourceChooser = null;
        this.allowIndirect = true;
		initialize();
	}
    
    public ParameterEditorDialog(Myspace myspace,ApplicationDescription desc, Component component) {
        super();
        this.desc = desc;
        this.allowIndirect=true;
        resourceChooser = new ResourceChooserDialog(myspace);
        this.setLocationRelativeTo(component);
        initialize();       
    }
    
    public ParameterEditorDialog(Myspace myspace,ApplicationDescription desc, Component component,boolean allowIndirect) {
        super();
        this.desc = desc;
        resourceChooser = new ResourceChooserDialog(myspace);
        this.setLocationRelativeTo(component);
        this.allowIndirect = allowIndirect;
        initialize();       
        
    }
    
    private final boolean allowIndirect;
    
    protected final ResourceChooserDialog resourceChooser;
    protected final ApplicationDescription desc;
   // protected Tool tool;
    
    
	private JLabel topLabel = null;
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setTitle("Parameter Editor");
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setSize(500, 333);
        this.setModal(true);
		this.setContentPane(getJContentPane());
        if (desc != null) {
            tool = desc.createToolFromDefaultInterface();
            topLabel.setText(desc.getName());
            Interface iface = desc.getInterfaces().get_interface(0); // will always be the first, as we're using the default.
            getParametersPanel().populate(tool,desc);
        }
		this.setVisible(false);
	}
    

   
    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
		    jContentPane = new JPanel(){{
		        setLayout(new java.awt.BorderLayout());
		        add(getButtonBox(), java.awt.BorderLayout.SOUTH);            
		        topLabel = new JLabel();
		        add(topLabel, java.awt.BorderLayout.NORTH);   
		        add(getParametersPanel(),BorderLayout.CENTER);
            }};
		}
		return jContentPane;
	}
    public Tool getTool() {
        return tool;
    }
    
    public void populate(Tool t, ApplicationDescription desc) {
        this.tool=t;
        getParametersPanel().populate(t,desc);
    }
    
    private ParametersPanel getParametersPanel() {
        if (parametersPanel == null) {
            //@todo this in not general - quick hack
            parametersPanel = new ParametersPanel(resourceChooser,allowIndirect);;
        }
        return parametersPanel;
    }
    
  
   
}  //  @jve:decl-index=0:visual-constraint="10,10"


/* 
$Log: ParameterEditorDialog.java,v $
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