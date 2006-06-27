/**
 * 
 */
package org.astrogrid.desktop.modules.ui.lookout;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;
/**
 * popup menu that allows user to filter the messages seen
 * @todo - expose this functionality through a 'filter' button too - right-click menus are too cryptic.
 * @author Noel Winstanley
 * @since Jun 17, 200612:23:17 AM
 */

public class MessageTablePopupMenu extends MouseAdapter {
	
	private final JCheckBoxMenuItem cbMenuItem1, cbMenuItem2, cbMenuItem3, cbMenuItem4;
	
	
	private String messageLevel = "All";
	private final JPopupMenu messageLevelMenu;
	
	protected final MessageTable messageTable;
	public MessageTablePopupMenu(MessageTable messageTable) {
		this.messageTable = messageTable;
		messageLevelMenu = new JPopupMenu();
		cbMenuItem1 = new JCheckBoxMenuItem("All");
        cbMenuItem1.setToolTipText("Display all messages");
        cbMenuItem1.setSelected(true);
        cbMenuItem1.addItemListener(new ItemListener() {
        	public void itemStateChanged(ItemEvent e) {
        		if (e.getStateChange() == ItemEvent.SELECTED) {
            		displayMessageLevel("All");
        		}
        	}
        });
		cbMenuItem2 = new JCheckBoxMenuItem("Status change");
        cbMenuItem2.setToolTipText("Display all status change messages");
        cbMenuItem2.addItemListener(new ItemListener() {
        	public void itemStateChanged(ItemEvent e) {
        		if (e.getStateChange() == ItemEvent.SELECTED) {
            		displayMessageLevel("STATUS CHANGE");
        		}
        	}
        });
		cbMenuItem3 = new JCheckBoxMenuItem("Information");
        cbMenuItem3.setToolTipText("Display all information level messages");
        cbMenuItem3.addItemListener(new ItemListener() {
        	public void itemStateChanged(ItemEvent e) {
        		if (e.getStateChange() == ItemEvent.SELECTED) {
        			displayMessageLevel("INFORMATION");
        		}
        	}
        });
		cbMenuItem4 = new JCheckBoxMenuItem("Results");
        cbMenuItem4.setToolTipText("Display all results messages");
        cbMenuItem4.addItemListener(new ItemListener() {
        	public void itemStateChanged(ItemEvent e) {
        		if (e.getStateChange() == ItemEvent.SELECTED) {
        		displayMessageLevel("RESULTS");
        		}
        	}
        });
        ButtonGroup group = new ButtonGroup();
        group.add(cbMenuItem1);
        group.add(cbMenuItem2);
        group.add(cbMenuItem3);
        group.add(cbMenuItem4);
        messageLevelMenu.add(cbMenuItem1);
        messageLevelMenu.add(cbMenuItem2);
        messageLevelMenu.add(cbMenuItem3);
        messageLevelMenu.add(cbMenuItem4);
	}
	
	public void displayMessageLevel(String level) {            
	    for (int i =0; i < messageTable.getRowCount(); i++) {
	    	if (messageTable.getRowCount() <= 1) { // prevent users being able to hide single row with no way back
	    		cbMenuItem1.setSelected(true);
	    		return;
	    	}
	        if (level.equalsIgnoreCase("ALL")) {
	        	messageTable.setRowHeight(i, 16);
	        } else if (!level.equalsIgnoreCase(messageTable.getValueAt(i,0).toString())) {
	        	messageTable.setRowHeight(i,1);
	        } else {
	        	messageTable.setRowHeight(i, 16);
	        }
	        messageLevel = level;
	    }
	}


	    public void mousePressed(MouseEvent e) {
			if (e.isPopupTrigger()) {
					this.messageLevelMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	 public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
				this.messageLevelMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}
	 public void reset() {
    	cbMenuItem1.doClick();
	}
}