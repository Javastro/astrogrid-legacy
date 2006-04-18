/*$Id: ResourceChooserDialog.java,v 1.10 2006/04/18 23:25:44 nw Exp $
 * Created on 15-Apr-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.desktop.modules.ui.AbstractVospaceBrowser;
import org.astrogrid.filemanager.client.FileManagerNode;
/** Dialog that allows the user to choose a resource in file:// or ivo:// space.
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Apr-2005

 */
class ResourceChooserDialog extends JDialog implements PropertyChangeListener{
    
    class VospaceChooser extends AbstractVospaceBrowser {

        public VospaceChooser() throws HeadlessException {
            super(conf, ResourceChooserDialog.this.help, ResourceChooserDialog.this.ui, vos); 
            // need to disambiguate help and ui, as vospace chooser's parent class has fields of this name too - and it won't compile otherwise
            comm.addUserLoginListener(this);
        }
        private Actions actions;

        protected CurrentNodeManager createCurrentNodeManager() {
            return new CurrentNodeManager() {
                public void updateDisplay() {
                    super.updateDisplay();
                    FileManagerNode n = getCurrent();
                    if (n.isFile()) {
                        try {
                            setUri(new URI(n.getIvorn().toString()));
                        } catch (Exception e) {
                            showError("Could not ascertain ivorn",e);
                        }
                    }else if(enableDirectorySelection && n.isFolder()) {
                        try {
                            setUri(new URI(n.getIvorn().toString()));
                        } catch (Exception e) {
                            showError("Could not ascertain ivorn",e);
                        }                        
                    }
                    
                }
            };
        }
        /**
         * @see org.astrogrid.desktop.modules.ui.AbstractVospaceBrowser#getActions()
         */
        protected Actions getActions() {
            if (actions == null) {
                actions = new Actions(new Action[]{new CreateFileAction(),new CreateFolderAction()});
            }
            return actions;
        }
    } // end class vospace chooser.
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ResourceChooserDialog.class);

    
    protected URI uri;
	private JPanel bottomPanel = null;


	private javax.swing.JOptionPane  optionPane = null;
	private JFileChooser localPanel = null;
	private JTabbedPane jTabbedPane = null;
    
	private JPanel myspacePanel = null;	
	private JTextField resourceUriField = null;
    private JPanel urlPanel = null;
    
    private JTextField urlPanelField = null;
    private final MyspaceInternal vos;  
    private final Configuration conf;
    private final HelpServerInternal help;
    private final UIInternal ui;
    private final Community comm;
    
    
    public ResourceChooserDialog(MyspaceInternal vos,Configuration conf,HelpServerInternal help,UIInternal ui, Community comm) {
        super();
        this.vos = vos;
        this.conf=conf;
        this.help = help;
        this.ui = ui;
        this.comm = comm;
        initialize();
	}
    public URI getUri() {
        return this.uri;
    }

    public void setEnableLocalFilePanel(boolean enableLocalFilePanel) {
        getJTabbedPane().setEnabledAt(0,enableLocalFilePanel); 
        bumpTabs();
    }
        
    public void setEnableMySpacePanel(boolean enableMySpacePanel) {
        getJTabbedPane().setEnabledAt(1,enableMySpacePanel);
        bumpTabs();
    }
    public void setEnableURIPanel(boolean enableURIPanel) {
        getJTabbedPane().setEnabledAt(2,enableURIPanel);
        bumpTabs();
    }
    
    /**
     *   Bumps to the next enabled tab.
     *   When tabs are disabled, Windows can sometimes leave a disabled
     *   tab selected.  
     */
    private void bumpTabs() {
        JTabbedPane tabbedPane = getJTabbedPane();
        int numberOfTabs = tabbedPane.getTabCount();
        int current = tabbedPane.getSelectedIndex();
        int tryThisOne = current;
        int count = 0;
        while (!tabbedPane.isEnabledAt(tryThisOne) && count<numberOfTabs) {
            tryThisOne = (tryThisOne + 1)%numberOfTabs;
            ++count;
        }
        if (tryThisOne!=current) tabbedPane.setSelectedIndex(tryThisOne);
    }
    
    boolean enableDirectorySelection = false;
    public void setEnabledDirectorySelection(boolean enableDirectorySelection) {
        if(enableDirectorySelection) {
            localPanel.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        } else {
            localPanel.setFileSelectionMode(JFileChooser.FILES_ONLY);            
        }
        this.enableDirectorySelection = enableDirectorySelection;
    }
    
    /** resets the dialog, then hides it. */
    public void resetAndHide() {        
        setVisible(false);
        setEnableLocalFilePanel(true);
        setEnableMySpacePanel(true);
        setEnableURIPanel(true);
      //  getResourceUriField().setText(null);
        
    }
    
    public void setUri(URI uri) {
        this.uri = uri;
        if (uri != null) {
            this.getResourceUriField().setText(uri.toString());
        } else {
            this.getResourceUriField().setText(null);
        }
    }
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getBottomPanel() {
		if (bottomPanel == null) {
			bottomPanel = new JPanel();
			JLabel jLabel = new JLabel();
			bottomPanel.setLayout(new BorderLayout());
			jLabel.setText("Resource URI");
			bottomPanel.add(jLabel, java.awt.BorderLayout.WEST);
			bottomPanel.add(getResourceUriField(), java.awt.BorderLayout.CENTER);
		}
		return bottomPanel;
	}


	private JOptionPane getOptionPane() {
		if(optionPane == null) {
            optionPane = new JOptionPane(new JComponent[]{getJTabbedPane(),getBottomPanel()},JOptionPane.PLAIN_MESSAGE,JOptionPane.OK_CANCEL_OPTION);
            optionPane.addPropertyChangeListener(this);     
		}
		return optionPane;
	}

	private JFileChooser getLocalPanel() {
		if (localPanel == null) {
			localPanel = new JFileChooser();
			localPanel.setDialogTitle("Choose file");
			localPanel.setControlButtonsAreShown(false);
           localPanel.addPropertyChangeListener("SelectedFileChangedProperty",new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                File f = localPanel.getSelectedFile();
                if (f != null) {
                    setUri(f.toURI());
                }
            }
           });

		}
		return localPanel;
	}
	/**
	 * This method initializes jTabbedPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */    
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(0,0,0,0));
           jTabbedPane.addTab("Local Disk", null, getLocalPanel(), "Choose a local file");            
          jTabbedPane.addTab("Myspace", null, getMyspacePanel(), "Choose a file in myspace");           
          jTabbedPane.addTab("URL",null,getURLPanel(),"Enter an arbitrary URL (http://, ftp://, etc)");            
		}
		return jTabbedPane;
	}
	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */    
	private JComponent getMyspacePanel() {
		if (myspacePanel == null) {
            VospaceChooser chooser = new VospaceChooser();
            comm.addUserLoginListener(chooser);
			JSplitPane split = new JSplitPane();
            split.setDividerSize(5);
            split.setDividerLocation(300);
            split.setLeftComponent(new JScrollPane(chooser.getFolderTree()));
            split.setRightComponent(new JScrollPane(chooser.getFileList()));
            JPanel panel =chooser.getMainPanel();
            chooser.remove(panel); // as don't want to use it in this frame, but instead splice it into another dialog.
            panel.add(split,BorderLayout.CENTER); 
            panel.add(chooser.getToolBar(),BorderLayout.NORTH);
            chooser.readRoot();
            myspacePanel = panel;
		}
		return myspacePanel;
	}

	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getResourceUriField() {
		if (resourceUriField == null) {
			resourceUriField = new JTextField();
            resourceUriField.setEditable(false); // don't make this editable.
            resourceUriField.setColumns(50);

		}
		return resourceUriField;
	}
    
    private JComponent getURLPanel() {
        if (urlPanel == null) {
            urlPanel = new JPanel();
            urlPanel.setLayout(new FlowLayout());
            JLabel label = new JLabel("Enter URL:");
            urlPanel.add(label);
            urlPanel.add(getUrlPanelField());
        }
        return urlPanel;
    }
    private JTextField getUrlPanelField() {
        if (urlPanelField == null) {
            urlPanelField = new JTextField();
            urlPanelField.setColumns(40);
            urlPanelField.getDocument().addDocumentListener(new DocumentListener() {

                public void changedUpdate(DocumentEvent e) {
                    URI u = null;
                    try {
                        u = new URI(urlPanelField.getText());
                    } catch (URISyntaxException e1) {
                         // not to worry - user is probably in the middle of editing.
                    }
                    setUri(u );
                }

                public void insertUpdate(DocumentEvent e) {
                    changedUpdate(e);
                }

                public void removeUpdate(DocumentEvent e) {
                    changedUpdate(e);
                }
            });
        }
        return urlPanelField;
    }


	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize(){
		this.setModal(true);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);     
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                /*
                 * Instead of directly closing the window,
                 * we're going to change the JOptionPane's
                 * value property.
                 */
                    optionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
            }
        });        
		this.setContentPane(getOptionPane());
        this.setSize(425,600);
	}
    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        if (isVisible()
         && (e.getSource() == optionPane)
         && (JOptionPane.VALUE_PROPERTY.equals(prop) ||
             JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
            Object value = optionPane.getValue();

            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                //ignore reset
                return;
            }

            //Reset the JOptionPane's value.
            //If you don't do this, then if the user
            //presses the same button next time, no
            //property change event will be fired.
            optionPane.setValue(
                    JOptionPane.UNINITIALIZED_VALUE);

            if (JOptionPane.OK_OPTION == ((Integer)value).intValue()) {
                if (getUri() == null) {
                    JOptionPane.showMessageDialog(ResourceChooserDialog.this,"You must choose a resource, or hit Cancel","Choose a resource",JOptionPane.ERROR_MESSAGE);
                } else {
                    resetAndHide();
                }
            } else { //user closed dialog or clicked cancel
                setUri(null);
                resetAndHide();
            }
        }        
        
    }
    /** ensure the local panel is rescanned whenever ui is displaued */
    public void setVisible(boolean b) {
        if (b) {
            localPanel.rescanCurrentDirectory();
        }
        super.setVisible(b);
    }
}


/* 
$Log: ResourceChooserDialog.java,v $
Revision 1.10  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.9.30.1  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.9  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.8.2.1  2005/11/23 04:48:02  nw
when select directories, it's only directories.

Revision 1.8  2005/11/11 10:08:18  nw
cosmetic fixes

Revision 1.7  2005/11/08 18:17:41  jdt
MySpace browser now initialises on an enabled tab - previously it could open up with a disabled tab selected.

Revision 1.6  2005/11/02 09:49:25  KevinBenson
when directories are enabled allowed myspace folders to be selected.

Revision 1.5  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.3.10.2  2005/10/12 09:21:38  nw
added java help system

Revision 1.3.10.1  2005/10/10 18:12:37  nw
merged kev's datascope lite.

Revision 1.4  2005/10/04 20:41:52  KevinBenson
added the ability to select directories on a local file system.  That way myspace can save to a directory.
Only myspacebrowser has this turned on at the moment.

Revision 1.3  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.7  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.6  2005/07/08 11:08:01  nw
bug fixes and polishing for the workshop

Revision 1.5  2005/06/08 14:51:59  clq2
1111

Revision 1.2.8.2  2005/06/02 14:34:32  nw
first release of application launcher

Revision 1.2.8.1  2005/05/09 14:51:02  nw
renamed to 'myspace' and 'workbench'
added confirmation on app exit.

Revision 1.2  2005/04/27 13:42:41  clq2
1082

Revision 1.1.2.4  2005/04/25 16:47:27  nw
little fix.

Revision 1.1.2.3  2005/04/25 16:41:29  nw
added file relocation (move data between stores)

Revision 1.1.2.2  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.1.2.1  2005/04/22 10:54:03  nw
start of a new module.

Revision 1.1.2.1  2005/04/15 13:00:45  nw
got vospace browser working.
 
*/