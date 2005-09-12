/* DefaultListTransferHandler.java
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

import javax.swing.JComponent;
import javax.swing.JList;


/**
 * @author pjn3
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DefaultListTransferHandler extends AbstractListTransferHandler {

	    private ApplicationInformation taskDetails = null;
	            

	    protected ApplicationInformation exportTask(JComponent c) {
	        JList list = (JList)c;
	        taskDetails = (ApplicationInformation)list.getSelectedValue();
	        
	        return taskDetails;
	    }



}
