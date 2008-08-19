/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

import org.astrogrid.desktop.modules.ui.UIComponent;

import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.event.ControlAdapter;

/** prefuse control that will display the send-to context menu over suitable items.
 * 
 * @fixme - need to alter how this works. right-clicking should select the current
 * node? or just display menu for current selection? either way, this code should just
 * display the popup, and not concern with transferables.
 * @author Noel Winstanley
 * @since Jun 25, 20062:09:49 AM
 */
public class SendToMenuControl extends ControlAdapter {

	public SendToMenuControl(final JPopupMenu actions,final UIComponent parent) {
		this.popup = actions;
		this.parent = parent;
		//this.trans = new TreeNodePreferredTransferable();
	}
	private final JPopupMenu popup;
	private final UIComponent parent;
//	private final TreeNodePreferredTransferable trans;
	
	@Override
    public void itemClicked(final VisualItem item, final MouseEvent e) {
		if ( ( e.isPopupTrigger() || e.isControlDown() || e.getButton() == MouseEvent.BUTTON3) 
			&& item.getEntity() instanceof FileProducingTreeNode) {
			//FileProducingTreeNode node = (FileProducingTreeNode)item.getEntity();
		//	trans.setTreeNode(node);
			//@fixme update current selection at this point..
			//popup.show(trans,parent,e.getComponent(),e.getX(),e.getY());
			popup.show(e.getComponent(),e.getX(),e.getY());
		}
	}
}
