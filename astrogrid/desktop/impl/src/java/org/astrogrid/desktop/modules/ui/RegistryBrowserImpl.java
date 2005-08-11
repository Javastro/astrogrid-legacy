/*$Id: RegistryBrowserImpl.java,v 1.1 2005/08/11 10:15:00 nw Exp $
 * Created on 30-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui;

import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.system.HelpServer;
import org.astrogrid.acr.ui.RegistryBrowser;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.CommunityInternal;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.desktop.modules.system.transformers.Xml2XhtmlTransformer;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.store.Ivorn;

import org.w3c.dom.Document;

import EDU.oswego.cs.dl.util.concurrent.misc.SwingWorker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringWriter;
import java.net.URI;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Mar-2005
 *
 */
public class RegistryBrowserImpl extends UIComponent implements ActionListener, RegistryBrowser
{
    private JPanel topPanel = null;
	private JLabel jLabel = null;
	private JTextField ivornField = null;
	private JButton goButton = null;
	private JEditorPane recordDisplay = null;
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getTopPanel() {
		if (topPanel == null) {
			jLabel = new JLabel();
			topPanel = new JPanel();
			topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
			jLabel.setText("Resource Key:");
			topPanel.add(jLabel, null);
			topPanel.add(getIvornField(), null);
			topPanel.add(getGoButton(), null);
		}
		return topPanel;
	}
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getIvornField() {
		if (ivornField == null) {
			ivornField = new JTextField();
            ivornField.setToolTipText("<html>Enter the key of a resource to view it <br>- e.g <tt>ivo://uk.ac.le.star/filemanager</tt></html>");
			ivornField.addActionListener(this);
		}
		return ivornField;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getGoButton() {
		if (goButton == null) {
			goButton = new JButton();
			goButton.setIcon(IconHelper.loadIcon("update.gif"));
   		goButton.setToolTipText("Retrieve this resource");
			goButton.addActionListener(this);
		}
		return goButton;
	}
	/**
	 * This method initializes jEditorPane	
	 * 	
	 * @return javax.swing.JEditorPane	
	 */    
	private JEditorPane getRecordDisplay() {
		if (recordDisplay == null) {
			recordDisplay = new JEditorPane();
			recordDisplay.setEditable(false);
			recordDisplay.setContentType("text/html");
		}
		return recordDisplay;
	}
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getRecordDisplay());
		}
		return jScrollPane;
	}

	/*
	 * This is the default constructor
	 */
	public RegistryBrowserImpl() throws TransformerConfigurationException, TransformerFactoryConfigurationError {
		super();
		initialize();
	}
    
    public RegistryBrowserImpl(Registry reg, HelpServer hs,UIInternal ui,Configuration conf) throws TransformerConfigurationException, TransformerFactoryConfigurationError {
        super(conf,hs,ui);
        this.reg=reg;
        
        initialize();

        
    }
    protected Registry reg;
	private JScrollPane jScrollPane = null;
    protected Transformer transformer;
	/**
	 * This method initializes this
	 * 
	 * @return void
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerConfigurationException
	 */
	private void initialize() throws TransformerConfigurationException, TransformerFactoryConfigurationError {
        Source styleSource = Xml2XhtmlTransformer.getStyleSource();
        transformer = TransformerFactory.newInstance().newTransformer(styleSource);
         
		this.setSize(500, 600);   
        JPanel pane = getJContentPane();
        pane.add(getTopPanel(), java.awt.BorderLayout.NORTH);
        pane.add(getJScrollPane(), java.awt.BorderLayout.CENTER);        
		this.setContentPane(pane);
		this.setTitle("Registry Browser");
	}

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        // either search, or cancel..
        if (worker == null) { // start a search
            if (! ivornField.getText().startsWith("ivo://")) {
                ivornField.setText("ivo://" + ivornField.getText());
            }
            // do query in worker thread
            worker= new BackgroundOperation("Searching for " + ivornField.getText()) {
                protected Object construct() throws Exception {   
                 URI ivorn = new URI(ivornField.getText());
                Document dom = reg.getRecord(ivorn);
 
                 Source source = new DOMSource(dom);
                 StringWriter sw = new StringWriter();
                 Result result = new StreamResult(sw);
                 transformer.transform(source,result);
                 return sw.toString();
                }
                protected void doFinished(Object result) {
                        recordDisplay.setText(result.toString());
                }
                protected void doAlways() {
                        goButton.setText("Go");
                        worker = null; // remove reference to self;                    
                }
            };
            worker.start();
            goButton.setText("Cancel");
        } else { // stop a search    
            worker.interrupt();
            recordDisplay.setText("");
        }        
        
    }
    private SwingWorker worker;
}  //  @jve:decl-index=0:visual-constraint="10,10"


/* 
$Log: RegistryBrowserImpl.java,v $
Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.8  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.7  2005/07/10 18:07:38  nw
files for 1.0.5

Revision 1.6  2005/06/20 16:56:40  nw
fixes for 1.0.2-beta-2

Revision 1.5  2005/05/12 15:59:08  clq2
nww 1111 again

Revision 1.3.8.1  2005/05/11 14:25:22  nw
javadoc, improved result transformers for xml

Revision 1.3  2005/04/27 13:42:40  clq2
1082

Revision 1.2.2.1  2005/04/25 11:18:50  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2  2005/04/13 12:59:18  nw
checkin from branch desktop-nww-998

Revision 1.1.2.6  2005/04/13 12:23:28  nw
refactored a common base class for ui components

Revision 1.1.2.5  2005/04/06 16:18:50  nw
finished icon set

Revision 1.1.2.4  2005/04/05 11:42:40  nw
tidied imports

Revision 1.1.2.3  2005/04/04 16:43:48  nw
made frames remember their previous positions.
synchronized guiLogin, so only one login box ever comes up.
made refresh action in jobmonitor more robust

Revision 1.1.2.2  2005/04/04 08:49:27  nw
working job monitor, tied into pw launcher.

Revision 1.1.2.1  2005/04/01 19:03:10  nw
beta of job monitor
 
*/