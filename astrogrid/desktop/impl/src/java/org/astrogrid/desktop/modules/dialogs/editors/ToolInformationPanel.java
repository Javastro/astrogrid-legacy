/*$Id: ToolInformationPanel.java,v 1.4 2005/11/24 01:13:24 nw Exp $
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
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditAdapter;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditEvent;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolModel;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.workflow.beans.v1.Tool;

import java.awt.BorderLayout;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/** an 'editor' that just displays information about the tool - doesn't permit any editing.
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Sep-2005
 *
 */
public class ToolInformationPanel extends AbstractToolEditorPanel {

    /** Construct a new ToolInformationPanel
     * @param tm
     */
    public ToolInformationPanel(ToolModel tm, Applications apps, UIComponent parent) {
        super(tm);
        initialize(apps,parent);
    }

    /** Construct a new ToolInformationPanel
     * 
     */
    public ToolInformationPanel(Applications apps, UIComponent parent) {
        super();  
        initialize(apps,parent);
    }
    
    private void initialize(final Applications apps,final UIComponent parent ) {
        final JEditorPane ed =  new JEditorPane("text/plain",null);
        final JLabel label = new JLabel();
        ed.setEditable(false);
        this.setLayout(new BorderLayout());
        this.add(label,BorderLayout.NORTH);
        this.add(new JScrollPane(ed),BorderLayout.CENTER);
        getToolModel().addToolEditListener(new ToolEditAdapter() {
            public void toolSet(ToolEditEvent te) {
                (new BackgroundWorker(parent,"Building Documentation"){

                    protected Object construct()  {
                        try {
                        return apps.getDocumentation(getToolModel().getInfo().getId());
                        } catch (Exception e) {
                            return e.getMessage();
                        }
                    }
                    
                    protected void doFinished(Object result) {
                        ed.setText(result.toString());
                        if (getToolModel().getInfo() != null) {
                            label.setText(getToolModel().getInfo().getName());
                        }
                    }
                }).start();

            }
            public void toolCleared(ToolEditEvent te) {
                ed.setText("");
                label.setText("");
            }   
        });
    }


    public boolean isApplicable(Tool t, ApplicationInformation info) {
        return t != null;
    }

}


/* 
$Log: ToolInformationPanel.java,v $
Revision 1.4  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.3.14.1  2005/11/23 04:45:00  nw
checks for null - occurs when used in pw launcher.

Revision 1.3  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.1.6.1  2005/10/10 16:24:29  nw
reviewed phils workflow builder
skeletal javahelp

Revision 1.2  2005/10/05 11:53:00  nw
handles missing registry entries more gracefully

Revision 1.1  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder
 
*/