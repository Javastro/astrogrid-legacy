/*
 * Created on 12/8/2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.dialogs;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.MissingMethodException;
import groovy.lang.MissingPropertyException;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import jedit.JEditTextArea;
import jedit.JavaTokenMarker;
import jedit.SyntaxDocument;

import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.workflow.beans.v1.AbstractActivity;
import org.astrogrid.workflow.beans.v1.Script;
import org.codehaus.groovy.control.CompilationFailedException;
import org.exolab.castor.xml.Unmarshaller;
/**
 * Simple dialog that displays script activities
 * @author Phil Nicolson pjn3@star.le.ac.uk 12/8/05
 * 
 * @modified nww - display results in a jedit box - gives syntax coloring. rewrittn to use joptionpane.
 * @modified pjn - groovy validation added
 * @modified pjn - Open/Save/Cut/Copy actions added
 */
public class ScriptDialog extends BaseBeanEditorDialog {
	private JEditTextArea scriptField;
	private JPanel displayPanel;
	private JTextField descField;
	private JMenu fileMenu, editMenu;
	private JMenuBar jJMenuBar;
	private JToolBar toolbar;
	
	protected Action openAction, saveAction, newAction, validateAction, 
	                 pasteAction, copyAction, cutAction, selectAllAction;
	
	protected final ResourceChooserInternal chooser;
    protected final MyspaceInternal myspace; 
    
    
    /** load a script */
	protected final class OpenAction extends AbstractAction {
	    public OpenAction() {
	        super("Open", IconHelper.loadIcon("file_obj.gif"));
	        this.putValue(SHORT_DESCRIPTION,"Load a script from storage");
	        this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_O));
	        this.setEnabled(true);            
	    }
	    public void actionPerformed(ActionEvent e) {
        	final URI u = chooser.chooseResource("Load workflow",true);
            if (u == null) {
                return;
            }
            Reader reader = null;
            try {
                reader = new InputStreamReader(myspace.getInputStream(u));		                		         
                Script s = (Script)Unmarshaller.unmarshal(Script.class, reader);	                
                descField.setText(s.getBody());
                scriptField.setText(s.getBody());
            } catch (Exception ex) {
            	showError("An error occured loading script... ", ex);
            }finally {
            	if (reader != null) {
            		try {
            			reader.close();
            		} catch (IOException ignored) {
            			// ignored
            		}
            	}
            }
            
	    }
	}	
	
	/** New script */
    protected final class NewAction extends AbstractAction {
        public NewAction() {
            super("New",IconHelper.loadIcon("newfile_wiz.gif"));
            this.putValue(SHORT_DESCRIPTION,"Create a new script");
            this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_N));
            this.setEnabled(false);
        }
        public void actionPerformed(ActionEvent e) {
            int code = JOptionPane.showConfirmDialog(null,"Discard current script?","Are you sure?",JOptionPane.OK_CANCEL_OPTION);
            if (code == JOptionPane.OK_OPTION) {
                scriptField.setText("");
                descField.setText("");
            }
        }
    }
    
    /** Save script */
    protected final class SaveAction extends AbstractAction {   	 
        public SaveAction() {
            super("Save",IconHelper.loadIcon("fileexport.png"));
            this.putValue(SHORT_DESCRIPTION,"Save script document");
            this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_S));
            this.setEnabled(false);        
        }

        public void actionPerformed(ActionEvent e) {
            final URI u = chooser.chooseResourceWithParent("Save Task Document",true,true, true,null);
            if (u == null) {
                return;
            }
            if (scriptField.getText().length() <= 0 && descField.getText().length() <= 0)
            	return;
            Writer w = null;
            try {
                w = new OutputStreamWriter(myspace.getOutputStream(u));
                Script s = new Script();
                s.setDescription(descField.getText());
                s.setBody(scriptField.getText());
                ((AbstractActivity)s).marshal(w);
            } catch (Exception ex) {
            	showError("An error ocurred saving your script ", ex);
            } finally {
            	if (w != null) {
            		try {
            			w.close();
            		} catch (IOException ignored) {
            		}
            	}
            }
        }
    }
    
	/** Validate script */
    protected final class ValidateAction extends AbstractAction {
        public ValidateAction() {
            super("Validate",IconHelper.loadIcon("icon_Set.gif"));
            this.putValue(SHORT_DESCRIPTION,"<html>Check if script is valid groovy script. <br>(Scripts are validated in isolation to the workflow document)</html>");
            this.setEnabled(false);
        }
        public void actionPerformed(ActionEvent e) {
        	try{
        		Binding binding = new Binding();
        		GroovyShell shell = new GroovyShell(binding);
        		shell.evaluate(getScriptField().getText()); //@issue is it safe to evaluate all scripts. what about 'rm -rf *' ??
        		JOptionPane.showMessageDialog(null, 
        				                      "Your script appears to be valid",
    										  "Script appears valid", 
    										  JOptionPane.INFORMATION_MESSAGE);
            } 
            catch (CompilationFailedException ex ) {
            	showError("Your script may contain errors ",ex);
            }
        	catch (MissingMethodException ex)  {
        		showError("Your script may contain errors ",ex);
        	}
        	catch (MissingPropertyException ex) {
        		JOptionPane.showMessageDialog(null, 
                                              "<html>Your script appears valid, assuming the following variable is set <br>elsewhere in your workflow document (if not please add a 'SET' activity..)<br><br>" +ex + "</html>" ,
    					                      "Script appears valid", 
    					                      JOptionPane.INFORMATION_MESSAGE);
        	}
        }
    }
    
    protected final class PasteAction extends AbstractAction {
    	public PasteAction() {
    		super("Paste",IconHelper.loadIcon("paste_edit.gif"));
    		this.putValue(SHORT_DESCRIPTION,"Paste");
    		//this.putValue(ACCELERATOR_KEY, KeyStroke.getAWTKeyStroke(new Integer(KeyEvent.VK_V).intValue(),InputEvent.CTRL_DOWN_MASK));
    		this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_P));
    		this.putValue(Action.NAME, "Paste");     	
    	}
        public void actionPerformed(ActionEvent e) {
        		scriptField.paste();
        	}
    }
    
    protected final class CopyAction extends AbstractAction {
    	public CopyAction() {
    		super("Copy",IconHelper.loadIcon("copy.gif"));
    		this.putValue(SHORT_DESCRIPTION,"Copy");
    		//this.putValue(ACCELERATOR_KEY, KeyStroke.getAWTKeyStroke(new Integer(KeyEvent.VK_C).intValue(),InputEvent.CTRL_DOWN_MASK));
    		this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_C));
    		this.putValue(Action.NAME, "Copy"); 
    		this.setEnabled(false);
    	}
        public void actionPerformed(ActionEvent e) {       	
        		scriptField.copy();
        	}
    }
    
    protected final class CutAction extends AbstractAction {
    	public CutAction() {
    		super("Cut",IconHelper.loadIcon("cut_edit.gif"));
    		this.putValue(SHORT_DESCRIPTION,"Cut");
    		this.putValue(Action.NAME, "Cut"); 
    		//this.putValue(ACCELERATOR_KEY, KeyStroke.getAWTKeyStroke(new Integer(KeyEvent.VK_X).intValue(),InputEvent.CTRL_DOWN_MASK));
    		this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_T));
    		this.setEnabled(false);
    	}
        public void actionPerformed(ActionEvent e) {       	
        		scriptField.cut();
        	}
    }
    
    protected final class SelectAllAction extends AbstractAction {
    	public SelectAllAction() {
    		super("Select all",null);
    		this.putValue(SHORT_DESCRIPTION,"Select all");
    		this.putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_A));
    		//this.putValue(ACCELERATOR_KEY, KeyStroke.getAWTKeyStroke(new Integer(KeyEvent.VK_A).intValue(),InputEvent.CTRL_DOWN_MASK));
    		this.putValue(Action.NAME, "Select all");    		
    	}
        public void actionPerformed(ActionEvent e) {       	
        		scriptField.selectAll();
        	}
    }

	
	
	/**
	 * This method initializes jTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */    
	private JEditTextArea getScriptField() {
		if (scriptField == null) {
			scriptField = new JEditTextArea();
            scriptField.setDocument(new SyntaxDocument()); // prevents aliasing between jeditors.
            scriptField.setTokenMarker(new JavaTokenMarker());
            scriptField.setEditable(true);
            scriptField.addCaretListener(new CaretListener(){
            	public void caretUpdate(CaretEvent e) {
            		if (scriptField.getDocumentLength() >0) {
            		    validateAction.setEnabled(true);
            		    saveAction.setEnabled(true);
            		    newAction.setEnabled(true);
            		    cutAction.setEnabled(true);
            		    copyAction.setEnabled(true);
            		} else {
            			validateAction.setEnabled(false);
            			saveAction.setEnabled(false);
            			newAction.setEnabled(false);
            			cutAction.setEnabled(false);
            			copyAction.setEnabled(false);
            		}
            	}
            });
		}
		return scriptField;
	}
	
	private JToolBar getToolbar() {
        if (toolbar == null) {
    		toolbar = new JToolBar();
    		toolbar.setRollover(true);
    		toolbar.setFloatable(false);
    		toolbar.add(newAction);
    		toolbar.add(openAction);
    		toolbar.add(saveAction);
    		toolbar.add(validateAction);    		
    		toolbar.add(cutAction);
    		toolbar.add(copyAction);
    		toolbar.add(pasteAction);    		    		
    		toolbar.add(new JSeparator(SwingConstants.VERTICAL));
    		toolbar.add(getDescriptionPanel());
        }
        return toolbar;
	}
	
	public JPanel getDisplayPanel() {
		if (displayPanel == null) {
			displayPanel = new JPanel();
            displayPanel.setLayout(new BorderLayout());
   
            newAction = new NewAction();
            openAction = new OpenAction();
            saveAction = new SaveAction();
            validateAction = new ValidateAction();
            pasteAction = new PasteAction();
            copyAction = new CopyAction();
            cutAction = new CutAction();
            selectAllAction = new SelectAllAction();
            
            this.setJMenuBar(getJJMenuBar());            
            fileMenu.add(newAction);
            fileMenu.add(openAction);
            fileMenu.add(saveAction); 
            
            editMenu.add(cutAction);
            editMenu.add(copyAction);
            editMenu.add(pasteAction);
            editMenu.add(selectAllAction);           

            displayPanel.add(getToolbar(), BorderLayout.NORTH);
            //displayPanel.add(getDescriptionPanel(), BorderLayout.CENTER);
			displayPanel.add(getScriptField(), BorderLayout.SOUTH);		
		}
		return displayPanel;
	}
	
	private JPanel getDescriptionPanel() {
		JPanel p = new JPanel(new FlowLayout());
		JLabel label = new JLabel("Description:  ");
		descField = new JTextField(30);
		p.add(label);
		p.add(descField);
		p.setBorder(BorderFactory.createEtchedBorder());
		return p;
	}	


    
    public ScriptDialog(Component parentComponent, ResourceChooserInternal chooser, MyspaceInternal myspace) {
        super(parentComponent);        
        this.setTitle("Edit Script");
        this.setSize(685,570);
        this.setLocationRelativeTo(parentComponent);
        this.pack();
        this.chooser = chooser;
        this.myspace = myspace;
    }

    
    protected void validateInput() {
        // no validation here at present.
        Script s = getScript();
        s.setBody(getScriptField().getText());
        s.setDescription(descField.getText());
        accept();
    }
    
    public void setScript(Script s) {
        setTheBean(s);
        getScriptField().setText(s.getBody());
        getScriptField().setCaretPosition(0);
        descField.setText(s.getDescription());        
    }
    
    public Script getScript() {
        return (Script)getTheBean();
    }

    
    /**
     * @param s
     * @throws HeadlessException
     */
    protected void showError(String s, Exception ex) throws HeadlessException {
        JOptionPane.showMessageDialog(this,ex,s,JOptionPane.ERROR_MESSAGE);
    }
       
	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.add(getFileMenu());
			jJMenuBar.add(getEditMenu());
		}
		return jJMenuBar;
	}
   
	private JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu();
			fileMenu.setText("File");
			fileMenu.setMnemonic(KeyEvent.VK_F);
		}
		return fileMenu;
	}
	
	private JMenu getEditMenu() {
		if (editMenu == null) {
			editMenu = new JMenu();
			editMenu.setText("Edit");
			editMenu.setMnemonic(KeyEvent.VK_E);
			}
		return editMenu;
	}
}
