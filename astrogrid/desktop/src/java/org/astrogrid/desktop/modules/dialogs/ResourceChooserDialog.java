/*$Id: ResourceChooserDialog.java,v 1.4 2005/05/12 15:59:08 clq2 Exp $
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

import org.astrogrid.acr.astrogrid.Myspace;
import org.astrogrid.acr.ui.MyspaceBrowser;
import org.astrogrid.desktop.modules.system.UIImpl;
import org.astrogrid.desktop.modules.ui.AbstractVospaceBrowser;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.AbstractVospaceBrowser.Actions;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.filemanager.common.NodeNotFoundFault;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;
import javax.swing.JSplitPane;
/** Dialog that allows the user to choose a resource in file:// or ivo:// space.
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Apr-2005

 */
public class ResourceChooserDialog extends JDialog {
    
    public static URI chooseResource(Myspace vos) {
        ResourceChooserDialog chooser = new ResourceChooserDialog(vos);
        chooser.show();
        URI u = chooser.getUri();
        chooser.dispose();
        return u;
    }
    
    public static URI chooseResource(Myspace vos,boolean enableMySpaceTab) {
        ResourceChooserDialog chooser = new ResourceChooserDialog(vos,enableMySpaceTab);
        chooser.show();
        URI u = chooser.getUri();
        chooser.dispose();
        return u;        
    }
    
    public static URI chooseResource(Myspace vos,String prompt, boolean enableMySpaceTab) {
        ResourceChooserDialog chooser = new ResourceChooserDialog(vos,prompt,enableMySpaceTab);
        chooser.show();
        URI u = chooser.getUri();
        chooser.dispose();
        return u;        
    }
    
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ResourceChooserDialog.class);

	private javax.swing.JPanel jContentPane = null;
	private JTextField urlField = null;
	private JFileChooser jFileChooser = null;
	private JPanel jPanel1 = null;  //  @jve:decl-index=0:visual-constraint="556,166"
	private JButton okButton = null;
	private JButton cancelButton = null;
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getUrlField() {
		if (urlField == null) {
			urlField = new JTextField();
            urlField.setEditable(false);
            urlField.setColumns(50);

		}
		return urlField;
	}
	/**
	 * This method initializes jFileChooser	
	 * 	
	 * @return javax.swing.JFileChooser	
	 */    
	private JFileChooser getJFileChooser() {
		if (jFileChooser == null) {
			jFileChooser = new JFileChooser();
			jFileChooser.setDialogTitle("Choose file");
			jFileChooser.setControlButtonsAreShown(false);
           jFileChooser.addPropertyChangeListener("SelectedFileChangedProperty",new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                File f = jFileChooser.getSelectedFile();
                if (f != null) {
                    setUri(f.toURI());
                }
            }
           });

		}
		return jFileChooser;
	}
    
    class VospaceChooser extends AbstractVospaceBrowser {
        public VospaceChooser(Myspace vos) {
            super(vos);
        }
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
        private Actions actions;
    }

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.setSize(211, 35);
			jPanel1.add(getOkButton(), null);
			jPanel1.add(getCancelButton(), null);
		}
		return jPanel1;
	}
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setText("Ok");
            okButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                 try {
                        setUri(new URI(getUrlField().getText()));
                        dispose();                        
                   } catch (URISyntaxException e1){
                        UIComponent.showError(ResourceChooserDialog.this,"Invalid URI",e1);
                    }

                }
            });
		}
		return okButton;
	}
    protected URI uri;
	private JPanel jPanel = null;
	private JLabel jLabel = null;
	private JTabbedPane jTabbedPane = null;
    
	private JComponent myspacePanel = null;

    private final boolean enableMySpace;
	/**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText("Cancel");
            cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setUri(null);
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
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jLabel = new JLabel();
			jPanel.setLayout(new BorderLayout());
			jLabel.setText("Resource URI");
			jPanel.add(getJPanel1(), java.awt.BorderLayout.SOUTH);
			jPanel.add(jLabel, java.awt.BorderLayout.WEST);
			jPanel.add(getUrlField(), java.awt.BorderLayout.CENTER);
		}
		return jPanel;
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
			jTabbedPane.addTab("Local Disk", null, getJFileChooser(), "Choose a local file");
            if (this.enableMySpace) {
                jTabbedPane.addTab("Myspace", null, getMyspacePanel(), "Choose a file in myspace");
            }
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
            VospaceChooser chooser = new VospaceChooser(vos);
			JSplitPane split = new JSplitPane();
            split.setDividerSize(5);
            split.setDividerLocation(300);
            split.setLeftComponent(new JScrollPane(chooser.getFolderTree()));
            split.setRightComponent(new JScrollPane(chooser.getFileList()));
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.add(split,BorderLayout.CENTER);
            panel.add(chooser.getBottomPanel(),BorderLayout.SOUTH);           
            panel.add(chooser.getToolBar(),BorderLayout.NORTH);
            chooser.readRoot();
            myspacePanel = panel;
		}
		return myspacePanel;
	}

	/**
	 * This is the default constructor
	 */
	public ResourceChooserDialog(Myspace vos) {
		this(vos,true);
    }
    
    public ResourceChooserDialog(Myspace vos,boolean enableMySpace) {
        this(vos,"Choose Resource",enableMySpace);
    }
    
    public ResourceChooserDialog(Myspace vos,String prompt, boolean enableMySpace) {
        super();
        this.vos = vos;
        this.enableMySpace = enableMySpace;
		initialize(prompt);
	}
    private final Myspace vos;
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize(String prompt){
		this.setModal(true);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);            
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                setUri(null); // clear selection if user closed window.
            }
        });
		this.setTitle(prompt);
		this.setSize(601, 402);
		this.setContentPane(getJContentPane());
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new java.awt.BorderLayout());
			jContentPane.setPreferredSize(new java.awt.Dimension(575,400));
			jContentPane.add(getJPanel(), java.awt.BorderLayout.SOUTH);
			jContentPane.add(getJTabbedPane(), java.awt.BorderLayout.CENTER);            
		}
		return jContentPane;
	}
    public URI getUri() {
        return this.uri;
    }
    
    public void setUri(URI uri) {
        this.uri = uri;
        if (uri != null) {
            this.getUrlField().setText(uri.toString());
        } else {
            this.getUrlField().setText(null);
        }
    }
}  //  @jve:decl-index=0:visual-constraint="100,127"


/* 
$Log: ResourceChooserDialog.java,v $
Revision 1.4  2005/05/12 15:59:08  clq2
nww 1111 again

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