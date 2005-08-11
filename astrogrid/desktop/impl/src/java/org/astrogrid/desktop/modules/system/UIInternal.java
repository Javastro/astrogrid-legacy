/*$Id: UIInternal.java,v 1.1 2005/08/11 10:15:00 nw Exp $
 * Created on 26-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import org.astrogrid.acr.system.UI;

import java.awt.Component;

import javax.swing.JMenu;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 26-Jul-2005
 *
 */
public interface UIInternal extends UI{

    /** get the AWT component that implements the user interface - used for centering new windows, dialogues, etc. */
    public Component getComponent();
    
    /** access the help menu */
    public JMenu getHelpMenu();
   /** access the main modules menu */
    public JMenu getModulesMenu();

}


/* 
$Log: UIInternal.java,v $
Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/