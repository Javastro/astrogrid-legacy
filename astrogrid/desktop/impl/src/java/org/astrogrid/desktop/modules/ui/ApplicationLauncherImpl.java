/*$Id: ApplicationLauncherImpl.java,v 1.6 2005/11/01 09:19:46 nw Exp $
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
import org.astrogrid.acr.ui.ApplicationLauncher;
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
import org.astrogrid.desktop.modules.system.HelpServerInternal;
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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.AbstractAction;
import javax.swing.Action;
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
/** Implementation of the Application Launcher component
 * <p>
 * not just a thin wrapper around the composite tool editor;
 * @author Noel Winstanley nw@jb.man.ac.uk 12-May-2005
 *
 */
public class ApplicationLauncherImpl extends UIComponent  implements ApplicationLauncher {


    public ApplicationLauncherImpl(  PicoContainer pico,Configuration conf, HelpServerInternal help, UIInternal ui) {
            super(conf, help, ui);
            editor =  new CompositeToolEditorPanel(this,true,pico);
            this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            this.setLocationRelativeTo(ui.getComponent());
            this.setSize(400,500);
            JPanel pane = getJContentPane();
             pane.add(editor, java.awt.BorderLayout.CENTER);
            this.setContentPane(pane);
            this.setTitle("Application Launcher");
            getHelpServer().enableHelpKey(this.getRootPane(),"userInterface.applicationLauncher");        
            editor.getToolModel().addToolEditListener(new ToolEditAdapter() {

                public void toolSet(ToolEditEvent te) {
                    setTitle("Application Launcher - " + editor.getToolModel().getInfo().getName());
                }
                public void toolCleared(ToolEditEvent te) {
                    setTitle("Application Launcher");    
                }                
            });
    }
    
    private final AbstractToolEditorPanel editor;
  
}

/* 
$Log: ApplicationLauncherImpl.java,v $
Revision 1.6  2005/11/01 09:19:46  nw
messsaging for applicaitons.

Revision 1.5  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.4.6.1  2005/10/12 09:21:38  nw
added java help system

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