/*$Id: NodeConsumerButton.java,v 1.3 2007/01/29 10:43:49 nw Exp $
 * Created on 03-Feb-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui.scope;

import java.awt.event.ActionListener;

import javax.swing.JButton;

import edu.berkeley.guir.prefuse.event.FocusListener;
import edu.berkeley.guir.prefuse.focus.FocusSet;

/** Something that consumes a set of selected nodes - used as a plugin for the astroscope ui,
 * 
 * extends from AbstractAction, but also implements focusListener, to determine
 * when this action is enabled. (implementors must provide the actionPerformed and 
 * focusChanged methods)
 * 
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 03-Feb-2006
 *
 */
public  abstract class  NodeConsumerButton extends JButton implements FocusListener, ActionListener{
        public NodeConsumerButton(String name,String description,FocusSet selectedNodes) {
            this.setText(name);
            this.setToolTipText(description);
            this.selectedNodes = selectedNodes;
            selectedNodes.addFocusListener(this);
            setEnabled(false); // to start with, disabled.
            addActionListener(this);
        }
        
        protected final FocusSet selectedNodes;
        
        
        
        
        
        
    
}


/* 
$Log: NodeConsumerButton.java,v $
Revision 1.3  2007/01/29 10:43:49  nw
documentation fixes.

Revision 1.2  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.1.2.1  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.1  2006/02/24 15:26:53  nw
build framework for dynamically adding buttons

Revision 1.1  2006/02/09 15:40:01  nw
finished refactoring of astroscope.
added vospec viewer
 
*/