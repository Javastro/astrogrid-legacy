/*$Id: ToolEditorDialog.java,v 1.24 2008/11/04 14:35:52 nw Exp $
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

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.TypesafeObjectBuilder;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.UIDialogueComponentImpl;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;
import org.astrogrid.desktop.modules.ui.taskrunner.TaskParametersForm;
import org.astrogrid.desktop.modules.ui.taskrunner.TaskRunnerImpl;
import org.astrogrid.desktop.modules.ui.taskrunner.UIComponentWithMenu;
import org.astrogrid.desktop.modules.ui.taskrunner.TaskRunnerImpl.TaskRunnerToolbar;
import org.astrogrid.workflow.beans.v1.Tool;

/** Dialog that allows the user to edit a tool document - i.e. a set of parameters.
 * 
 * this class is just concerned with the dialogue side of thinigs
 * 
 * @todo extend to handle TAP and other similar protocols.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 23-Mar-2005
 * 
 * @todo add a right-click menu - cut / copy / delete.
 *
 */
public class ToolEditorDialog extends UIDialogueComponentImpl implements UIComponentWithMenu{
    
    private final JMenu editMenu;
    private final TaskParametersForm parametersPanel;
    private final ApplicationsInternal apps;
    private final ChooseAppAction chooseAppAction;
   
    private class ChooseAppAction extends AbstractAction {
        private final RegistryGoogleInternal chooser;

        public ChooseAppAction(final RegistryGoogleInternal chooser) {
            super("New Task"+ UIComponentMenuBar.ELLIPSIS,IconHelper.loadIcon("clearright16.png"));
            this.chooser = chooser;
            this.putValue(SHORT_DESCRIPTION,"Select a different task to run");
            this.setEnabled(false);
        }
                 
        public void actionPerformed(final ActionEvent e) {
            final Resource[] rs = chooser.selectResourceXQueryFilterWithParent(
                    "Choose a task"
                    ,false // single selection
                    ,apps.getRegistryXQuery() // list all applications
                    , ToolEditorDialog.this
                    );

            if (rs.length > 0 && (rs[0] instanceof CeaApplication)) {
                final CeaApplication cea = (CeaApplication)rs[0]; // only a single selection
               
                new BackgroundWorker(ToolEditorDialog.this,"Retrieving tool metadata",BackgroundWorker.LONG_TIMEOUT,Thread.MAX_PRIORITY) {

                    @Override
                    protected Object construct() throws Exception {
                        return apps.createTemplateTool(null,cea); 
                    }
                    @Override
                    protected void doFinished(final Object result) {
                        final Tool t = (Tool)result;
                        parametersPanel.buildForm(t,t.getInterface(),cea);    
                        super.doFinished(result);
                    }
                    @Override
                    protected void doError(final Throwable ex) {
                        ExceptionFormatter.showError(ToolEditorDialog.this,"An error occurred while " + workerTitle.toLowerCase(),ex);
                    }            
                }.start();                
            }
        }
    }
   	
   	public ToolEditorDialog(final UIContext context,
   	        final TypesafeObjectBuilder builder,
   	        final ApplicationsInternal apps
   	        ,final RegistryGoogleInternal chooser
   	        ) throws HeadlessException {
   	    super(context,"Task Editor","dialog.tool");
   	    this.apps = apps;
   	    setSize(900,600);

   	    this.editMenu = new JMenu("Edit");
   	    
   	    this.parametersPanel = builder.createTaskParametersForm(this);
   	    chooseAppAction = new ChooseAppAction(chooser);
   	    final TaskRunnerToolbar toolbar = new TaskRunnerImpl.TaskRunnerToolbar(parametersPanel,chooseAppAction);
   	    parametersPanel.setToolbar(toolbar);
   	    parametersPanel.setRightPane("ignored",null);
   	    final JPanel main = getMainPanel();
   	    main.add(parametersPanel,BorderLayout.CENTER);
   	    getContentPane().add(main);
   	  //  final JMenuBar mb = new JMenuBar();
   	  //  mb.add(editMenu);
   	// unsure whether a menubar is generally useful or not.    
   	//    setJMenuBar(mb);
    }
   	
   
    /** show the dialogue and return the user's input.
     * 
     * @return the edited tool, or null if cancel was pressed
     */
    public Tool getTool() {
        Tool result = null;
        if (!populated) { // i.e it's not been populated.
            SwingUtilities.invokeLater(new Runnable() {
                // show the chooser dialogue as soon as this dialogue is shown.
                public void run() {
                    chooseAppAction.setEnabled(true);
                    chooseAppAction.actionPerformed(null);
                }                
            });
        }
        if (ask()) {
         result  = parametersPanel.getTool();
        } 
        parametersPanel.clear();    
        chooseAppAction.setEnabled(false);
        populated = false;
        return result;
    }
    
    private boolean populated = false;
     
    public void populate(final Tool t) {
        populated = true;
        chooseAppAction.setEnabled(false);           
        new BackgroundWorker(this,"Retrieving tool metadata",BackgroundWorker.LONG_TIMEOUT,Thread.MAX_PRIORITY) {

            @Override
            protected Object construct() throws Exception {
                final URI uri = new URI(t.getName().startsWith("ivo://") ? t.getName() : "ivo://" + t.getName());
                return apps.getCeaApplication(uri);   
            }
            @Override
            protected void doFinished(final Object result) {
                final CeaApplication desc = (CeaApplication)result;
                parametersPanel.buildForm(t,t.getInterface(),desc); 
            }
            @Override
            protected void doError(final Throwable ex) {
                ExceptionFormatter.showError(ToolEditorDialog.this,"An error occurred while " + workerTitle.toLowerCase(),ex);
            }            
        }.start();
    }
    
    /** load tool from a storage location */
    public void load(final URI location) {
        populated = true;        
        chooseAppAction.setEnabled(false);        
        new BackgroundWorker(this,"Loading tool from " + location,BackgroundWorker.LONG_TIMEOUT,Thread.MAX_PRIORITY) {

            @Override
            protected Object construct() throws Exception {
                Reader is = null;                
                try {
                    is = new InputStreamReader(location.toURL().openStream());
                    return Tool.unmarshalTool(is);
                } finally {
                    try {
                        is.close();
                    } catch (final IOException e) {
                        // don 't care
                    }
                }
            }
            @Override
            protected void doFinished(final Object result) {
                populate((Tool)result);
            }
            // overridden to display error in front of dialogue.
            @Override
            protected void doError(final Throwable ex) {
                ExceptionFormatter.showError(ToolEditorDialog.this,"An error occurred while " + workerTitle.toLowerCase(),ex);
            }
        }.start();
    }


        public JMenu getContextMenu() {
            return editMenu;
        } 
}


/* 
$Log: ToolEditorDialog.java,v $
Revision 1.24  2008/11/04 14:35:52  nw
javadoc polishing

Revision 1.23  2008/10/23 16:34:03  nw
Incomplete - taskadd support for TAP

Revision 1.22  2008/08/21 12:56:30  nw
Complete - task 103: tool editor dialogue

Revision 1.21  2008/03/28 13:09:01  nw
help-tagging

Revision 1.20  2008/03/10 18:41:05  nw
made innder class static

Revision 1.19  2007/11/26 14:44:46  nw
Complete - task 224: review configuration of all backgroiund workers

Revision 1.18  2007/11/26 12:01:49  nw
added framework for progress indication for background processes

Revision 1.17  2007/11/20 06:02:55  nw
added help ids.

Revision 1.16  2007/10/12 10:58:24  nw
re-worked dialogues to use new ui baseclass and new ui components.

Revision 1.15  2007/10/07 10:41:31  nw
pass context menu into adql editor

Revision 1.14  2007/09/21 16:35:15  nw
improved error reporting,
various code-review tweaks.

Revision 1.13  2007/07/23 12:21:18  nw
stopgap implementations of tool editor dialog - uses new codebase, but not tested at the moment.

Revision 1.12  2007/04/18 15:47:10  nw
tidied up voexplorer, removed front pane.

Revision 1.11  2007/01/29 16:45:09  nw
cleaned up imports.

Revision 1.10  2007/01/29 11:11:37  nw
updated contact details.

Revision 1.9  2007/01/10 19:12:16  nw
integrated with preferences.

Revision 1.8  2006/08/16 11:10:23  pjn3
Possible resolution to OK/Cancl bug #1505

Revision 1.7  2006/08/15 10:21:34  nw
added constructor that specifies tool editor to use.upgraded to use new reg model.

Revision 1.6  2006/07/24 09:51:00  KevinBenson
having the parameterized workflow just use the basiceditor for input.

Revision 1.5  2006/06/27 19:11:52  nw
fixed to filter on cea apps when needed.

Revision 1.4  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.3.30.2  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.3.30.1  2006/03/28 13:47:35  nw
first webstartable version.

Revision 1.3  2005/11/21 18:25:39  pjn3
basic task editor help added

Revision 1.2  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.1.6.1  2005/10/12 09:21:38  nw
added java help system

Revision 1.1  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.7  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

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