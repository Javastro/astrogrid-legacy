/* TransferableNode.java
 * Created on 17-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.workflowBuilder.dragAndDrop;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.workflowBuilder.TaskDetails;

/**
 * @author pjn3
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TransferableNode implements Transferable {
	
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(Transferable.class);
	
	public static final DataFlavor NODE_FLAVOR = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType, "Node");
	public static final DataFlavor EXPANDED_STATE_FLAVOR = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType, "Vector");
	public static final DataFlavor TASK_DETAILS_FLAVOR = new DataFlavor(TaskDetails.class, "Task Details");
	public static final DataFlavor STRING_FLAVOR = new DataFlavor(java.lang.String.class, "Activity String");

	private DefaultMutableTreeNode node;
	private Vector expandedStates;
	private TaskDetails taskDetails;
	private String activityString;
	private DataFlavor[] flavors = { NODE_FLAVOR, EXPANDED_STATE_FLAVOR, TASK_DETAILS_FLAVOR, STRING_FLAVOR };
	
	public TransferableNode(DefaultMutableTreeNode nd, Vector es, TaskDetails td, String activity) {
		node = nd;
		expandedStates = es;
		taskDetails = td;
		activityString = activity;
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
	 */
	public DataFlavor[] getTransferDataFlavors() {
		return flavors;
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
	 */
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return Arrays.asList(flavors).contains(flavor);
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
	 */
	public synchronized Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (flavor == NODE_FLAVOR) {
			return node;
		}
		else if (flavor == EXPANDED_STATE_FLAVOR) {
			return expandedStates;
		}
		else if (flavor == TASK_DETAILS_FLAVOR) {
			return taskDetails;
		}
		else if (flavor == STRING_FLAVOR) {
			return activityString;
		}
		else {
			logger.error("Unsupported flavor error!!");
			throw new UnsupportedFlavorException(flavor);
		}
	}

}
