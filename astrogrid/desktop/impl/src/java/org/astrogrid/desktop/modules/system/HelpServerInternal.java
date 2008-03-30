/*$Id: HelpServerInternal.java,v 1.8 2008/03/30 14:44:28 nw Exp $
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
import javax.swing.JButton;

import org.astrogrid.acr.system.HelpServer;
import org.astrogrid.desktop.modules.system.SchedulerInternal.DelayedContinuation;

/** Internal interface for the help server component
 * 
 * implementation of DelayedContinuation is just an implementation detail
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 11-Oct-2005
 *
 */
public interface HelpServerInternal extends HelpServer, DelayedContinuation{
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
     * associate a helpId with a component.
     * Preferred to {@link CSH#setHelpIDString(Component, String)} - although
     * that is an alternative is an instance of helpServer is not available.
     * @param comp some ui widget
     * @param helpId the id of the help doc associated with this component.
     */
    public void enableHelp(Component comp, String helpId);
    
    /** associate a helpId with a menu item
      * Preferred to {@link CSH#setHelpIDString(MenuItem, String)} - although
     * that is an alternative is an instance of helpServer is not available.
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
    
    /** convenience funtion. - creates a small buttons iwth a '?' icon which will
     * take to the specified help location when clicked
     * @param helpId
     */
    
    public JButton createHelpButton(String helpId);
}


/* 
$Log: HelpServerInternal.java,v $
Revision 1.8  2008/03/30 14:44:28  nw
Complete - task 363: race condition at startup.

Revision 1.7  2007/06/18 17:00:13  nw
javadoc fixes.

Revision 1.6  2007/01/29 11:11:36  nw
updated contact details.

Revision 1.5  2007/01/12 13:20:05  nw
made sure every ui app has a help menu.

Revision 1.4  2007/01/11 18:15:49  nw
fixed help system to point to ag site.

Revision 1.3  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.2.46.1  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.2  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.1.2.1  2005/10/12 09:21:38  nw
added java help system
 
*/