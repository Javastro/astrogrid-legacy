/*$Id: ToolEditorDialog.java,v 1.14 2007/09/21 16:35:15 nw Exp $
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
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.TypesafeObjectBuilder;
import org.astrogrid.desktop.modules.ui.UIComponentImpl;
import org.astrogrid.desktop.modules.ui.taskrunner.TaskParametersForm;
import org.astrogrid.workflow.beans.v1.Tool;
/** dialog that allows the user to edit a tool document - i.e. a set of parameters.
 * 
 * <p>
 * tool editing business done by {@link org.astrogrid.desktop.modules.dialogs.editors.BasicToolEditorPanel}
 * this class is just concerned with the dialogue side of thinigs
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 23-Mar-2005
 *
 */
public class ToolEditorDialog extends JDialog implements PropertyChangeListener {

    
    JOptionPane jOptionPane = null;
    private final UIComponentImpl parent;
    
   	private JLabel topLabel = null;
    private TaskParametersForm parametersPanel;
   
   	
   	public ToolEditorDialog(UIContext context,TypesafeObjectBuilder builder) throws HeadlessException {
        super();          
        this.parent = new UIComponentImpl(context);    
        this.parametersPanel = builder.createTaskParametersForm(parent,new MouseAdapter(){}); // pass in a stub mouse adapter for now.
        init();
    }


	/**
	 * 
	 */
	private void init() {
		this.setTitle("Task Editor");
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
            /*
             * Instead of directly closing the window,
             * we're going to change the JOptionPane's
             * value property.
             */
                jOptionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
        }
    });                
        this.setModal(true);
        this.setContentPane(getJOptionPane());
	}   	
    
    public void resetAndHide() {
        setVisible(false);
        getTopLabel().setText(null);
        getTopLabel().setToolTipText(null);
        parametersPanel.clear();
    }
    
    private Tool editedTool = null;
    
    public Tool getTool() {
        return editedTool;
    }
    
    /** call this before setVisible() to restrict possible selections to only cea apps
     * only applies to next display of dialog - after which, goes back to 'all'
     *
     */
    public void nextDisplayShowCEAOnly() {
        //@todo implement or remove this
    //	parametersPanel.putClientProperty(CompositeToolEditorPanel.CEA_ONLY_CLIENT_PROPERTY,Boolean.TRUE);
    }
    /** overridden - resets client property */
    public void setVisible(boolean b) {
    	super.setVisible(b);
    	// once we've gotten here, modal dialogue has returned. so safe to remove the client property, if it exists
   // 	parametersPanel.putClientProperty(CompositeToolEditorPanel.CEA_ONLY_CLIENT_PROPERTY,null);
    }
    
    
    public void populate(Tool t, CeaApplication desc) {
        editedTool = null;
        //@todo check the interfacename for validity first.
        parametersPanel.buildForm(t,t.getInterface(),desc);    
        getTopLabel().setText(desc.getTitle());
        getTopLabel().setToolTipText(desc.getContent().getDescription());
    }

    
    
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        if (isVisible()
         && (e.getSource() == jOptionPane)
         && (JOptionPane.VALUE_PROPERTY.equals(prop) ||
             JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
            Object value = jOptionPane.getValue();

            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                //ignore reset
                return;
            }

            //Reset the JOptionPane's value.
            //If you don't do this, then if the user
            //presses the same button next time, no
            //property change event will be fired.
            jOptionPane.setValue(
                    JOptionPane.UNINITIALIZED_VALUE);

            if (JOptionPane.OK_OPTION == ((Integer)value).intValue()) {
                editedTool = parametersPanel.getTool();
                    resetAndHide();                
            } else { //user closed dialog or clicked cancel           
                editedTool = null;
                resetAndHide();
            }
        }
    }
    

    private JOptionPane getJOptionPane() {
       if (jOptionPane == null) {
           JPanel main = parent.getMainPanel();
           parent.remove(main);
           main.add(getTopLabel(),BorderLayout.NORTH);
           main.add(parametersPanel,BorderLayout.CENTER);
           JScrollPane detailsPane =  new JScrollPane(main);
           detailsPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
           jOptionPane = new JOptionPane(detailsPane,JOptionPane.PLAIN_MESSAGE,JOptionPane.OK_CANCEL_OPTION);
           //jOptionPane = new JOptionPane(main,JOptionPane.PLAIN_MESSAGE,JOptionPane.OK_CANCEL_OPTION);
           jOptionPane.addPropertyChangeListener(this);
       }
       return jOptionPane;
    }

    

    
    private JLabel getTopLabel() {
        if (topLabel == null) {
            topLabel = new JLabel();
        }
        return topLabel;
    }
      
   
}  //  @jve:decl-index=0:visual-constraint="10,10"


/* 
$Log: ToolEditorDialog.java,v $
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