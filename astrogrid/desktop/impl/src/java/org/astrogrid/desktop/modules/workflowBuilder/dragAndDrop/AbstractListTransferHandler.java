/* AbstractListTransferHandler.java
 * Created on 02-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.workflowBuilder.dragAndDrop;

import org.astrogrid.acr.astrogrid.ApplicationInformation;

import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 * @author pjn3
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractListTransferHandler extends TransferHandler {
    protected abstract ApplicationInformation exportTask(JComponent c);
    
    protected Transferable createTransferable(JComponent c) {
        return new TransferableNode(null, null, exportTask(c), null);
    }
    
    public int getSourceActions(JComponent c) {
        return MOVE;
    }
    
}
