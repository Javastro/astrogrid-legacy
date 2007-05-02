/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;

import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.swing.EventSelectionModel;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;

/** tabular display of files.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 29, 20071:40:57 AM
 */
public class FilesTable extends JTable implements MouseListener{
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(FilesTable.class);

	private final StorageView view;
	private final SortedList files;
	private final IconFinder icons;
	
	public FilesTable(SortedList files,EventSelectionModel currentSelection, StorageView view, IconFinder icons, FileViewDnDManager dnd) {
		this.view = view;
		this.files = files;
		this.icons = icons;
		// setup the model.
		setModel(new EventTableModel(files,new StorageTableFormat(icons)));
		setSelectionModel(currentSelection);
		//@todo selection doesn't seem to show up..
		// setup the behaviour
		setPreferredScrollableViewportSize(new Dimension(300,300));
		setRowSelectionAllowed(true);
		setColumnSelectionAllowed(false);
		setCellSelectionEnabled(false);		
		setAutoResizeMode(AUTO_RESIZE_NEXT_COLUMN);
		setBorder(BorderFactory.createEmptyBorder());
		setShowGrid(false);
		
		//allow sorting by columns.
		new TableComparatorChooser(this,files,true);
		
		//configure column widths
		final TableColumnModel cm = getColumnModel();
		cm.getColumn(0).setPreferredWidth(40);
		cm.getColumn(0).setMaxWidth(40);
		cm.getColumn(0).setMaxWidth(40);
		cm.getColumn(1).setMinWidth(200);
		cm.getColumn(2).setPreferredWidth(200);
		cm.getColumn(2).setMaxWidth(200);
		cm.getColumn(3).setPreferredWidth(70);
		cm.getColumn(3).setMaxWidth(70);
		cm.getColumn(4).setPreferredWidth(200);
		cm.getColumn(4).setMaxWidth(200);
		
		// alter how some columns are rendered.
		// show date-time for date objects
		setDefaultRenderer(Date.class,new DefaultTableCellRenderer.UIResource() {
			// doesn't give column alignment.DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
			DateFormat formatter = new SimpleDateFormat(" dd MMM yyyy, HH:mm");
			protected void setValue(Object value) {
				setText((value == null) ? "" : formatter.format(value));
			}
		});
		// add kb in some cases, don't display some cases.
		setDefaultRenderer(Long.class,new DefaultTableCellRenderer.UIResource() {
			{
				setHorizontalAlignment(JLabel.RIGHT);
			}
			protected void setValue(Object value) {
				long l =  value == null ? -1: ((Long)value).longValue();
				if (l == -1) {
					setText(""); 
				} else if (l > 1024) {
					setText(l/1024 + " MB");
				} else {
					setText(l + " KB");
				}		
			}
		});		
		
		//dnd
		dnd.enableDragAndDropFor(this);
		setDragEnabled(true);
		
		addMouseListener(this);
	}
	
	public String getToolTipText(MouseEvent e) {
		String tip = null;
		java.awt.Point p = e.getPoint();
		int rowIndex = rowAtPoint(p);
		FileObject item = (FileObject)files.get(rowIndex);
		if (item == null) {
			return "";
		}
		//@todo add more info in here later.
		return item.getName().getFriendlyURI();
	}
	/** traverse on a double-click */
	public void mouseClicked(MouseEvent e) {
		 if(e.getClickCount() == 2){
		     int row = rowAtPoint(e.getPoint());
		     FileObject item = (FileObject)files.get(row);
		     if (item == null) {
		    	 return;
		     }
		     // I thought I'd need to convert index scehems first
		     // seems not
		     try {
		    	 if (item.getType().hasContent()) {
		    		 view.move(item);
		    	 }
		     } catch (FileSystemException ex) {
		    	 // @todo report?
		     }
		 }		
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		checkForTriggerEvent(e);
	}

	public void mouseReleased(MouseEvent e) {
		checkForTriggerEvent(e);		
	}
	
//	 determine whether event should trigger popup menu
	private void checkForTriggerEvent( MouseEvent event ){
	   if ( event.isPopupTrigger() ) {
		   view.notifyStorageTasks();
	      view.getParent().getPopupMenu().show( event.getComponent(),
	         event.getX(), event.getY() );
	   }
	}	
	
	
}
