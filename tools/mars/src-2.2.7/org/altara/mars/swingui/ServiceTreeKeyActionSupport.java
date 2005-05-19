/* MARS Network Monitor Swing User Interface
   Copyright (C) 1999 Brian H. Trammell
   Copyright (C) 2002-2004 Leapfrog Research & Development, LLC

	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program; if not, it is available at 
	http:///www.gnu.org/copyleft/gpl.html, or by writing to the
	Free Software Foundation, Inc., 59 Temple Place - Suite 330,
	Boston, MA  02111-1307, USA.
*/

package org.altara.mars.swingui;

import org.altara.util.*;
import org.altara.mars.*;
import org.altara.mars.engine.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

/** Implements the serviceTree's key actions.
 */

public class ServiceTreeKeyActionSupport
    extends TreeKeyActionSupport {

    private Action newHostAction;
    private Action newServiceAction;
	private Action editAction;
	private Action deleteAction;
	private Action duplicateAction;
    private JTree tree;
    private MarsView view;

    public ServiceTreeKeyActionSupport(MarsView view, JTree serviceTree) {
        super(serviceTree);
        
        this.view = view;
        this.tree = tree;

        initializeActions();
    }

    private void initializeActions() {
        // create each action
		newHostAction = new AbstractAction("New Host...") {
                public void actionPerformed(ActionEvent ae) {
                    MarsModel model = Main.getMain().getController().getModel();
                    new EditorDialog(view, new HostEditorPanel(model));
                }
            };

		newServiceAction = new AbstractAction("New Service...") {
                public void actionPerformed(ActionEvent ae) {
                    Object invoked = getLastInvoked();
                    if (invoked == null) return;
                    if (invoked instanceof Host) {
                        new EditorDialog(view, new ServiceEditorPanel((Host)invoked));
                    } else if (invoked instanceof Service) {
                        new EditorDialog(view, new ServiceEditorPanel(((Service)invoked).getHost()));
                    }
                }
            };

        editAction = new AbstractAction("Edit...") {
                public void actionPerformed(ActionEvent ae) {
                    Object invoked = getLastInvoked();
                    if (invoked == null) return;
                    if (invoked instanceof Host) {
                        new EditorDialog(view, new HostEditorPanel((Host)invoked));
                    } else if (invoked instanceof Service) {
                        new EditorDialog(view, new ServiceEditorPanel((Service)invoked));
                    }
                }
            };

        deleteAction = new AbstractAction("Delete...") {
                public void actionPerformed(ActionEvent ae) {
                    Object invoked = getLastInvoked();
                    if (invoked == null) return;
                    if (invoked instanceof Host) {
                        if (confirmDelete(invoked)) {
                            ((Host)invoked).getModel().removeHost((Host)invoked);
                            view.postCommitUpdate();
                        }

                    } else if (invoked instanceof Service) {
                        if (confirmDelete(invoked)) {
                            ((Service)invoked).getHost().removeService((Service)invoked);
                            view.postCommitUpdate();
                        }
                    }
                }
            };

        duplicateAction = new AbstractAction("Duplicate Host...") {
                public void actionPerformed(ActionEvent ae) {
                    Object invoked = getLastInvoked();
                    if (invoked == null) return;
                    if (invoked instanceof Host) {
                        Host newHost = ((Host)invoked).duplicate();
                        new EditorDialog(view, new HostEditorPanel(newHost));
                    }
                }
            };

 
        // add them to the KeyActionSupport
        addNormalKey(KeyEvent.VK_ENTER,editAction);
        addNormalKey(KeyEvent.VK_BACK_SPACE,deleteAction);
        addNormalKey(KeyEvent.VK_DELETE,deleteAction);
        addCommandKey(KeyEvent.VK_T,newHostAction);
        addCommandKey(KeyEvent.VK_R,newServiceAction);
        addCommandKey(KeyEvent.VK_D,duplicateAction);
           
    }

	private boolean confirmDelete(Object obj) {
		String deleteMsg = "Are you sure you want to delete ";
		if (obj instanceof Host) {
			deleteMsg += "host "+((Host)obj).getName();
		} else {
			deleteMsg += "service "+((Service)obj).getName();
		}
		deleteMsg += "?";

		int confirmed =
			JOptionPane.showConfirmDialog(view,deleteMsg,"Confirm Delete",
			JOptionPane.YES_NO_OPTION);
		return confirmed == JOptionPane.YES_OPTION;
	}
}