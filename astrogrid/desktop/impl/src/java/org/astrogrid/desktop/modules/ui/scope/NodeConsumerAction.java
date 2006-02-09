/*$Id: NodeConsumerAction.java,v 1.1 2006/02/09 15:40:01 nw Exp $
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

import edu.berkeley.guir.prefuse.event.FocusListener;
import edu.berkeley.guir.prefuse.focus.FocusSet;

import javax.swing.AbstractAction;

/** Something that consumes a set of selected nodes - used as a plugin for the astroscope ui,
 * 
 * extends from AbstractAction, but also implements focusListener, to determine
 * when this action is enabled. (implementors must provide the actionPerformed and 
 * focusChanged methods)
 * 
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Feb-2006
 *
 */
public  abstract class  NodeConsumerAction extends AbstractAction implements FocusListener{
        public NodeConsumerAction(String name,String description,FocusSet selectedNodes) {
            putValue(NAME,name);
            putValue(SHORT_DESCRIPTION,description);
            putValue(LONG_DESCRIPTION,description);
            this.selectedNodes = selectedNodes;
            selectedNodes.addFocusListener(this);
            setEnabled(false); // to start with, disabled.
        }
        
        protected final FocusSet selectedNodes;
        
        
        
        
        
        
    
}


/* 
$Log: NodeConsumerAction.java,v $
Revision 1.1  2006/02/09 15:40:01  nw
finished refactoring of astroscope.
added vospec viewer
 
*/