/*$Id: DataScopeLauncherImpl.java,v 1.1 2005/10/04 20:46:48 KevinBenson Exp $
 * Created on 12-May-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui;

import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;

import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.dialogs.RegistryChooser;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.system.HelpServer;
import org.astrogrid.acr.ui.DataScopeLauncher;
import org.astrogrid.acr.ui.JobMonitor;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.dialogs.ResultDialog;
import org.astrogrid.desktop.modules.dialogs.editors.AbstractToolEditorPanel;
import org.astrogrid.desktop.modules.dialogs.editors.BasicToolEditorPanel;
import org.astrogrid.desktop.modules.dialogs.editors.CompositeToolEditorPanel;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditAdapter;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditEvent;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.picocontainer.PicoContainer;
import org.w3c.dom.Document;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;
import com.ice.tar.TarInputStream;
import com.ice.tar.TarEntry;
import com.ice.tar.TarHeader;

 
import java.net.URI;
import java.util.Arrays;
import java.util.Comparator;

import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.JFileChooser;
import javax.swing.JCheckBox;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;


/** Implementation of the Application Launcher component
 * <p>
 * not just a thin wrapper around the composite tool editor;
 * @author Noel Winstanley nw@jb.man.ac.uk 12-May-2005
 *
 */
//public class DataScopeLauncherImpl extends UIComponent  implements DataScopeLauncher, ActionListener {
//public class DataScopeLauncherImpl extends VospaceBrowserImpl implements DataScopeLauncher {
public class DataScopeLauncherImpl extends UIComponent implements DataScopeLauncher {

    private VospaceBrowserImpl vbi = null;
    private BrowserControl browser = null;
    
    //public DataScopeLauncherImpl(Configuration conf, HelpServer help, UIInternal ui, BrowserControl bc) throws java.io.IOException {
    public DataScopeLauncherImpl(Configuration conf, HelpServer hs,UIInternal ui, MyspaceInternal vos, Community comm, BrowserControl browser,ResourceChooserInternal chooser) {
        //super(conf, hs , ui, vos, comm, browser, chooser);
        super(conf, hs,ui);
        this.browser = browser;
        vbi = new VospaceBrowserImpl(conf, hs , ui, vos, comm, browser, chooser);

        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(ui.getComponent());
        this.setSize(400,500);
        JPanel pane = getJContentPane();
        pane.add(getTopPanel(),java.awt.BorderLayout.NORTH);
        this.setContentPane(pane);
        this.setTitle("Datascope Launcher");        
    }
    
    public void show() {
        super.show();
        vbi.show();        
        try {
            browser.openURL(new URL("http://heasarc.gsfc.nasa.gov/cgi-bin/vo/datascope/init.pl"));
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    
    

    
    private JPanel getTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));        
        String editorText = "<html><body><p>";
        editorText += "Datascope is an important application in the astronomy community.";
        editorText += "Below you will find Datascope, once saving results on your computer you may";
        editorText += "upload the data into myspace.";
        editorText += "By default any file is uncompressed and individual files are saved into myspace. Click the checkbox if you do not want this to happen";
        editorText += "</p></body></html>";
        editor = new JEditorPane("text/html",editorText);
        topPanel.add(editor);        
        return topPanel;
    }
    
    private JEditorPane editor;
  
}

/* 
$Log: DataScopeLauncherImpl.java,v $
Revision 1.1  2005/10/04 20:46:48  KevinBenson
new datascope launcher and change to module.xml for it.  Vospacebrowserimpl changes to handle file copies to directories on import and export

Revision 1.4  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder

Revision 1.3  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.6  2005/08/09 17:33:07  nw
finished system tests for ag components.

Revision 1.5  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.4  2005/07/08 11:08:01  nw
bug fixes and polishing for the workshop

Revision 1.3  2005/06/22 08:48:52  nw
latest changes - for 1.0.3-beta-1

Revision 1.2  2005/06/08 14:51:59  clq2
1111

Revision 1.1.2.1  2005/06/02 14:34:33  nw
first release of application launcher
 
*/