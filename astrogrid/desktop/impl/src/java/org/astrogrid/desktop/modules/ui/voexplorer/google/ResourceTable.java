package org.astrogrid.desktop.modules.ui.voexplorer.google;


import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
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


/** A table for displaying resources.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 12, 20077:33:02 PM
 */
public class ResourceTable extends JTable {
    private class SelectAllAction extends AbstractAction {
        /**
      * 
      */
     public SelectAllAction() {
         super("Select All");
     }
     
     public void actionPerformed(final ActionEvent e) {
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
     public void actionPerformed(final ActionEvent e) {
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

        public void actionPerformed(final ActionEvent e) {
            clearSelection();
        }
        
    }
	private final List items;
	private final VoMonInternal vomon;
    private boolean popClick;

	/**
	 * 
	 * @param dm the table model to show.
	 * @param items list (probablby a glazed list) of items that the table model is based upon.
	 * @param vomon monitoring information.
	 */
	public ResourceTable(final TableModel dm, final List items, final VoMonInternal vomon) {
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
		
		// setup the actionmap.
		final ActionMap map = getActionMap();
	      map.put(UIComponentMenuBar.EditMenuBuilder.COPY,TransferHandler.getCopyAction());
	      map.put(UIComponentMenuBar.EditMenuBuilder.SELECT_ALL,new SelectAllAction());
	      map.put(UIComponentMenuBar.EditMenuBuilder.INVERT_SELECTION,new InvertSelectionAction());
	      map.put(UIComponentMenuBar.EditMenuBuilder.CLEAR_SELECTION,new ClearSelectionAction());	      	 				
	}

	/** construct a tool tip, based on vomon information */
	public String getToolTipText(final MouseEvent e) {
		final java.awt.Point p = e.getPoint();
		final int rowIndex = rowAtPoint(p);
		if (rowIndex > -1) { 
		    final int colIndex = columnAtPoint(p);
		    final int realColumnIndex = convertColumnIndexToModel(colIndex);
		
			final Resource ri =(Resource) items.get(rowIndex);  // weakness - possiblility of getting the wrong tooltip if the list is rapidly updating. not the end of the world.
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
	
	public void setPopup(final JPopupMenu p) {
		this.popup = p;
	}
	public static final Point OFFSET = new Point(8,8);
	
	public static  final ImageIcon RESOURCES_ICON = IconHelper.loadIcon("multiplefile16.png");

	private final Image resourceImage;

	
	private class ResourceTableTransferHandler extends TransferHandler {
		public int getSourceActions(final JComponent c) {
			return COPY;
		}
		public boolean canImport(final JComponent comp, final DataFlavor[] transferFlavors) {
			return false;
		}
		protected Transferable createTransferable(final JComponent c) {
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

    /**
     * Hijack mouse events to handle the popup menu.
     * Do it this way rather than adding a MouseListener to work round a
     * nasty issue in OSX java 1.4 and 1.5 (popup gesture ctrl-click can
     * also deselect a selected item) - bugzilla 2610.
     */
    protected void processMouseEvent(final MouseEvent e) {
        final int mods = e.getModifiers();

        // If we're coming out of a click which posted the popup menu, 
        // don't give anyone else a chance to process this event - processing
        // this is what causes the trouble in OS X.
        if (popClick && (mods & MouseEvent.MOUSE_RELEASED) != 0) {
            popClick = false;
        }

        // Under other circumstances, post popup as required, and make sure to
        // invoke superclass implementation.
        else {
            if (e.isPopupTrigger()) {
                popup.show(e.getComponent(), e.getX(), e.getY());
                if ((mods & MouseEvent.MOUSE_PRESSED) != 0) {
                    popClick = true;
                }
            }
            super.processMouseEvent(e);
        }
    }
}
