/*$Id: HelpServerInternal.java,v 1.3 2006/04/18 23:25:44 nw Exp $
 * Created on 11-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import java.awt.Component;
import java.awt.MenuItem;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import org.astrogrid.acr.system.HelpServer;

/** Internal interface for the help server component
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Oct-2005
 *
 */
public interface HelpServerInternal extends HelpServer{
    /** call on the rootPane of a JFrame to trap all 'F1' key presses. and display help.
     * <p>
     * if object with current focus has a helpId, this is displayed, otherwise the defaultHelpId is displayed 
     * @param comp component to trap 'F1' for
     * @param defaultHelpId default help for this component 
     *
     *
     */
    public void enableHelpKey(Component comp, String defaultHelpId);
   
    /**
     * associate a helpId with a component
     * @param comp some ui widget
     * @param helpId the id of the help doc associated with this component.
     */
    public void enableHelp(Component comp, String helpId);
    
    /** associate a helpId with a menu item
     * 
     * @param comp a menu item
     * @param helpId the id of the help doc associated with this menu item.
     */
    public void enableHelp(MenuItem comp, String helpId);
    
    /** add a listener to a button that will display help when pressed
     * 
     * @param b the button
     * @param helpId id of the help doc to display when the button is pressed.
     */
    public void enableHelpOnButton(AbstractButton b, String helpId);
    
    
    /** add a listener to a menu item that will display help when pressed
     * 
     * @param b the menu item
     * @param helpId id of the help doc to display when the menu item is selected
     */
    public void enableHelpOnButton(MenuItem b, String helpId);
    
    public ActionListener createContextSensitiveHelpListener();
}


/* 
$Log: HelpServerInternal.java,v $
Revision 1.3  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.2.46.1  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.2  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.1.2.1  2005/10/12 09:21:38  nw
added java help system
 
*/