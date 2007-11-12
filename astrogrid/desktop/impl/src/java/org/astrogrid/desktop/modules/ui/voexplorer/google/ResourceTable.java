package org.astrogrid.desktop.modules.ui.voexplorer.google;


import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import javax.swing.table.TableModel;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.dnd.ResourceListTransferable;
import org.astrogrid.desktop.modules.ui.dnd.ResourceTransferable;
import org.astrogrid.desktop.modules.votech.VoMonInternal;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.EventSelectionModel;
import ca.odell.glazedlists.swing.EventTableModel;


/** A table for displaying resource titles
 * @todo add cut,copy,paste support later.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 12, 20077:33:02 PM
 */
public class ResourceTable extends JTable implements MouseListener {
    private class SelectAllAction extends AbstractAction {
        /**
      * 
      */
     public SelectAllAction() {
         super("Select All");
     }
     
     public void actionPerformed(ActionEvent e) {
         selectAll();
     }
    }
    
    private class InvertSelectionAction extends AbstractAction {
        /**
      * 
      */
     public InvertSelectionAction() {
         super("Invert Selection");
     }
     public void actionPerformed(ActionEvent e) {
         ((EventSelectionModel)getSelectionModel()).invertSelection();
     }
    }
    private class ClearSelectionAction extends AbstractAction {
        /**
         * 
         */
        public ClearSelectionAction() {
            super("Clear Selection");
        }

        public void actionPerformed(ActionEvent e) {
            clearSelection();
        }
        
    }
	private final List items;
	private final VoMonInternal vomon;

	/**
	 * 
	 * @param dm the table model to show.
	 * @param items list (probablby a glazed list) of items that the table model is based upon.
	 * @param vomon monitoring information.
	 */
	public ResourceTable(TableModel dm, List items, VoMonInternal vomon) {
		super(dm);
		this.items = items;
		this.vomon = vomon;
		setAutoResizeMode(AUTO_RESIZE_NEXT_COLUMN);
		setBorder(BorderFactory.createEmptyBorder());	
		setShowGrid(false);
        getFormat().configureColumnModel(getColumnModel());
		
		// drag and drop.
		resourceImage = IconHelper.loadIcon("doc16.png").getImage();
		setTransferHandler( new ResourceTableTransferHandler());
		setDragEnabled(true);
		//right-click menu.
		popup = new JPopupMenu();
		//1.5 only :( setComponentPopupMenu(popup);
		addMouseListener(this);
		
		// setup the actionmap.
		ActionMap map = getActionMap();
	      map.put(UIComponentMenuBar.EditMenuBuilder.COPY,TransferHandler.getCopyAction());
	      map.put(UIComponentMenuBar.EditMenuBuilder.SELECT_ALL,new SelectAllAction());
	      map.put(UIComponentMenuBar.EditMenuBuilder.INVERT_SELECTION,new InvertSelectionAction());
	      map.put(UIComponentMenuBar.EditMenuBuilder.CLEAR_SELECTION,new ClearSelectionAction());	      	 				
	}

	/** construct a tool tip, based on vomon information */
	public String getToolTipText(MouseEvent e) {
		java.awt.Point p = e.getPoint();
		int rowIndex = rowAtPoint(p);
		int colIndex = columnAtPoint(p);
		int realColumnIndex = convertColumnIndexToModel(colIndex);
		
		if (rowIndex > -1) { 
			Resource ri =(Resource) items.get(rowIndex);  // weakness - possiblility of getting the wrong tooltip if the list is rapidly updating. not the end of the world.
            return getFormat().getToolTipText(ri, realColumnIndex);
        } else { 
			return super.getToolTipText(e);
		}
	}

    /**
     * Returns the TableFormat associated with this table.
     *
     * @return  resource table format
     */
    private ResourceTableFomat getFormat() {
        return (ResourceTableFomat) ((EventTableModel) getModel()).getTableFormat();
    }
	
	private JPopupMenu popup;
	
	public void setPopup(JPopupMenu p) {
		this.popup = p;
	}
	public static final Point OFFSET = new Point(8,8);
	
	public static  final ImageIcon RESOURCES_ICON = IconHelper.loadIcon("multiplefile16.png");

	private final Image resourceImage;

	
	private class ResourceTableTransferHandler extends TransferHandler {
		public int getSourceActions(JComponent c) {
			return COPY;
		}
		public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
			return false;
		}
		protected Transferable createTransferable(JComponent c) {
			return getSelectionTransferable();
		}
	}
		
	
	public Transferable getSelectionTransferable() {
		final EventList selected = ((EventSelectionModel)getSelectionModel()).getSelected();
		switch (selected.size()) {
		case 0:
			// ignore
			return null;
		case 1:
			return new ResourceTransferable((Resource)selected.get(0));
		default:
			return  new ResourceListTransferable(selected);
		} 
	}

	// mouse listener interface.
	public void mouseClicked(MouseEvent e) {
	    //ignored
	}

	public void mouseEntered(MouseEvent e) {
	    //ignored
	}

	public void mouseExited(MouseEvent e) {
	    //ignored
	}

	public void mousePressed(MouseEvent e) {
		maybeShowPopup(e);
	}

	public void mouseReleased(MouseEvent e) {
		maybeShowPopup(e);
	}
	
	 private void maybeShowPopup(MouseEvent e) {
		 //@todo change selection if the click didn't happen in the selection.
	        if (e.isPopupTrigger()) {
	            popup.show(e.getComponent(),
	                       e.getX(), e.getY());
	        }
	    }

}
