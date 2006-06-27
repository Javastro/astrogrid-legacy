/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.awt.event.MouseEvent;

import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.sendto.SendToMenu;
import org.astrogrid.desktop.modules.ui.sendto.TreeNodePreferredTransferable;

import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.event.ControlAdapter;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/** prefuse control that will display the send-to context menu over suitable items.
 * @author Noel Winstanley
 * @since Jun 25, 20062:09:49 AM
 */
public class SendToMenuControl extends ControlAdapter {

	public SendToMenuControl(SendToMenu sendTo,UIComponent parent) {
		this.sendTo = sendTo;
		this.parent = parent;
		this.trans = new TreeNodePreferredTransferable();
	}
	private final SendToMenu sendTo;
	private final UIComponent parent;
	private final TreeNodePreferredTransferable trans;
	
	public void itemClicked(VisualItem item, MouseEvent e) {
		if ( ( e.isPopupTrigger() || e.isControlDown() || e.getButton() == MouseEvent.BUTTON3) 
			&& item instanceof NodeItem
			&& ((NodeItem)item).getAttribute(VizModel.NOMENU_ATTRIBUTE) == null
			) {
			TreeNode node = (TreeNode)item.getEntity();
			trans.setTreeNode(node);
			sendTo.show(trans,parent,e.getComponent(),e.getX(),e.getY());
		}
	}
}
