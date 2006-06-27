package org.astrogrid.desktop.modules.ui.lookout;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.astrogrid.desktop.modules.ag.MessageRecorderInternal;
import org.astrogrid.desktop.modules.ag.MessageRecorderInternal.MessageContainer;

/** Table that lists titles of messages for an application.
 * @author Noel Winstanley
 * @since Jun 16, 200611:55:12 PM
 */
public final class MessageTable extends JTable {
	
	public void clear() {
		clearSelection();
		popup.reset();
	}
	
	private final MessageTablePopupMenu popup;
	/**
	 * @param dm
	 */
	public MessageTable(final MessageRecorderInternal recorder) {
		super(recorder.getMessageList());
		popup = new MessageTablePopupMenu(this);
		addMouseListener(popup);     		
	    setShowVerticalLines(false);
	    setShowHorizontalLines(false);
	    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);            		
	    getTableHeader().setReorderingAllowed(false);
	    TableColumn titleColumn = getColumnModel().getColumn(0);
	    titleColumn.setPreferredWidth(150);
	    titleColumn.setCellRenderer(new DefaultTableCellRenderer() {
	        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	            super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
	            MessageContainer msg = recorder.getMessage(row);                    
	            if (msg!= null && msg.isUnread() ) { 
	                setText("<html><b>" + getText() + "</b></html>");
	            }
	            return this;
	        }               
	    });
	}
}