/*$Id: ChooseAToolEditorPanel.java,v 1.4 2005/11/24 01:13:24 nw Exp $
 * Created on 08-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.dialogs.editors;

import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditAdapter;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditEvent;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolModel;
import org.astrogrid.desktop.modules.dialogs.registry.RegistryChooserPanel;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.commons.lang.StringUtils;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/** Tool Editor Panel that prompts the user to search for and select a tool.
 * <p>
 * just a wapper of some event listeners around the registry chooser panel - nice!
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Sep-2005
 *
 */
public class ChooseAToolEditorPanel extends AbstractToolEditorPanel {

    public ChooseAToolEditorPanel(ToolModel tm,final UIComponent parent, Registry reg, final ApplicationsInternal apps, Boolean allApps) {
        super(tm);
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        add(new JLabel("Select an Application:"));
       final RegistryChooserPanel rcp = new RegistryChooserPanel( parent,reg) ;
        rcp.setMultipleResources(false);

        rcp.setFilter(" ((@xsi:type like '%CeaApplicationType' " +
                " or @xsi:type like '%CeaHttpApplicationType' " + 
                ( allApps.booleanValue() ? " or @xsi:type like '%ConeSearch' " + 
                        " or @xsi:type like '%SimpleImageAccess' " 
                        : "") + 
                " ) and @status = 'active')");
        toolModel.addToolEditListener(new ToolEditAdapter() {
            public void toolCleared(ToolEditEvent te) {
                rcp.clear();
            }            
        });
        rcp.getSelectedResourcesModel().addListDataListener(new ListDataListener() {

            public void intervalAdded(ListDataEvent e) {
                ResourceInformation[] ri = rcp.getSelectedResources();
                if (ri.length > e.getIndex0()) {
                    ResourceInformation resource = ri[e.getIndex0()];
                    if (resource instanceof ApplicationInformation) {
                        if (toolModel.getTool() != null) { // already got some data on the go..
                            int result = JOptionPane.showConfirmDialog(ChooseAToolEditorPanel.this,"Discard the tool currently being edited?","Replace the current tool?",JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
                            if (result != JOptionPane.OK_OPTION) {
                                System.out.println("aborted");
                                return;
                            }
                        }
                    ApplicationInformation app = (ApplicationInformation)resource;
                    String ifaceName = app.getInterfaces()[0].getName();
                    if (app.getInterfaces().length > 1) {
                        String[] names = new String[app.getInterfaces().length];
                        for (int i = 0; i < names.length; i++) {
                            names[i] = app.getInterfaces()[i].getName();
                        }
                        ifaceName =(String) JOptionPane.showInputDialog(ChooseAToolEditorPanel.this,"Select an interface","Which Interface?"
                                , JOptionPane.QUESTION_MESSAGE,null,names,names[0]);
                    }
                    Tool t = apps.createTemplateTool(ifaceName,app);
                    toolModel.populate(t,app); // fires notification, etc - lets anything else grab this.
                    } else {
                        parent.setStatusMessage(resource.getName() + " is not a known kind of Application");
                    }
                }
            }

            public void intervalRemoved(ListDataEvent e) {
            }

            public void contentsChanged(ListDataEvent e) {
            }
        });
        add(rcp);        
    }



    /** applicable always */
    public boolean isApplicable(Tool t, ApplicationInformation info) {
        return true;
    }
    
}


/* 
$Log: ChooseAToolEditorPanel.java,v $
Revision 1.4  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.3.2.2  2005/11/23 04:45:51  nw
removed dev code from query.

Revision 1.3.2.1  2005/11/17 21:18:22  nw
*** empty log message ***

Revision 1.3  2005/11/11 18:39:40  nw
2 final tweaks

Revision 1.2  2005/11/01 09:19:46  nw
messsaging for applicaitons.

Revision 1.1  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder
 
*/