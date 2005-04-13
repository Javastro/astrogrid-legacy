/*$Id: ParameterEditor.java,v 1.2 2005/04/13 12:59:18 nw Exp $
 * Created on 23-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.pw;

import org.astrogrid.applications.beans.v1.Interface;
import org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 23-Mar-2005
 *
 */
public class ParameterEditor extends JDialog {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ParameterEditor.class);

	private javax.swing.JPanel jContentPane = null;
	private JPanel buttonBox = null;
	private JButton executeButton = null;
	private JButton cancelButton = null;
	private JPanel parametersBox = null;
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
			executeButton = new JButton();
			executeButton.setText("Execute");
			executeButton.setToolTipText("Submit the workflow for execution");
			executeButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
                    dispose(); // causes dialogue to return
				}
			});
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
			cancelButton = new JButton();
			cancelButton.setText("Cancel");
			cancelButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
                    tool  = null; // we've canceled, so don't return anything.
                    dispose();
				}
			});
		}
		return cancelButton;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getParametersBox() {
		if (parametersBox == null) {
			parametersBox = new JPanel();
            GridLayout l = new GridLayout();
            l.setColumns(2);
            l.setRows(0);
            
            parametersBox.setLayout(l);
		}
		return parametersBox;
	}
          public static void main(String[] args) {
    }
	/**
	 * This is the default constructor
	 */
	public ParameterEditor() {
		super();
        desc = null;
		initialize();
	}
    
    public ParameterEditor(ApplicationDescription desc, Component component) {
        super();
        this.desc = desc;
        this.setLocationRelativeTo(component);
        initialize();
    }
    protected final ApplicationDescription desc;
    protected Tool tool;
    
    
	private JLabel topLabel = null;
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setTitle("Parameter Editor");
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setSize(300, 333);
		this.setContentPane(getJContentPane());
        this.setModal(true); //@todo does it have to be modal?
        if (desc != null) {
            tool = desc.createToolFromDefaultInterface();
            topLabel.setText(desc.getName());
            Interface iface = desc.getInterfaces().get_interface(0); // will always be the first, as we're using the default.
           for (int i = 0; i < tool.getInput().getParameterCount(); i++) {
               ParameterValue v = tool.getInput().getParameter(i);
               BaseParameterDefinition def = desc.getDefinitionForValue(v,iface);
              addField(v,def);
           }
           for (int i = 0; i < tool.getOutput().getParameterCount(); i++) {
               ParameterValue v = tool.getInput().getParameter(i);
               BaseParameterDefinition def = desc.getDefinitionForValue(v,iface);
              addField(v,def);
           }           
        }
		this.setVisible(false);
	}
    
    
    private String mkToolTip(BaseParameterDefinition def) {
        StringBuffer result = new StringBuffer();
        result.append("<html>");
        result.append(def.getUI_Description().getContent());
        result.append("<dl>");
        if (def.getUCD() != null && def.getUCD().trim().length() > 0) 
                result.append("<dt><b>").append("UCD").append("</b></dt><dd>").append(def.getUCD()).append("</dd>");
        if (def.getUnits() != null && def.getUnits().trim().length() > 0) 
                result.append("<dt><b>").append("Units").append("</b></dt><dd>").append(def.getUnits()).append("</dd>");
        if (def.getType() != null) 
            result.append("<dt><b>").append("Type").append("</b></dt><dd>").append(def.getType()).append("</dd>");
        if (def.getSubType() != null && def.getSubType().trim().length() > 0) 
                result.append("<dt><b>").append("Subtype").append("</b></dt><dd>").append(def.getSubType()).append("</dd>");
        if (def.getAcceptEncodings() != null && def.getAcceptEncodings().trim().length() > 0) 
                 result.append("<dt><b>").append("Encodings").append("</b></dt><dd>").append(def.getAcceptEncodings()).append("</dd>");        
        if (def.getOptionList() != null) 
                result.append("<dt><b>").append("One of").append("</b></dt><dd>").append(Arrays.asList(def.getOptionList().getOptionVal())).append("</dd>");        
                
        result.append("</dl></html>");
        logger.debug(result.toString());
        return result.toString();
    }
    
	/** add a field into the builder.
     * @param v
     * @param def
     */
    private void addField(final ParameterValue v, BaseParameterDefinition def) {
        JLabel fieldName = new JLabel();
        fieldName.setText(def.getUI_Name());
        JTextField input = new JTextField();
        fieldName.setToolTipText(mkToolTip(def));
        input.setText(def.getDefaultValue());
        input.setColumns(40);
        JPanel b = getParametersBox();
        b.add(fieldName);
        b.add(input);
        
        // funny work-around - listen to all changes to document, so are immediately reflected in the tool model.
        input.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                Document d = e.getDocument();
                try {
                    v.setValue(d.getText(0,d.getLength()));
                } catch (BadLocationException e1) {
                    logger.error("BadLocationException",e1);
                }
            }

            public void insertUpdate(DocumentEvent e) {
                Document d = e.getDocument();
                try {
                    v.setValue(d.getText(0,d.getLength()));
                } catch (BadLocationException e1) {
                    logger.error("BadLocationException",e1);
                }                
            }

            public void removeUpdate(DocumentEvent e) {
                Document d = e.getDocument();
                try {
                    v.setValue(d.getText(0,d.getLength()));
                } catch (BadLocationException e1) {
                    logger.error("BadLocationException",e1);
                }                
            }
        });
    }
    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			topLabel = new JLabel();
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new java.awt.BorderLayout());
			jContentPane.add(getButtonBox(), java.awt.BorderLayout.SOUTH);
			jContentPane.add(topLabel, java.awt.BorderLayout.NORTH);
            Box b = Box.createVerticalBox();
            b.add(getParametersBox());
            b.add(Box.createVerticalGlue());
			jContentPane.add(b, java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}
    public Tool getTool() {
        return this.tool;
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"


/* 
$Log: ParameterEditor.java,v $
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