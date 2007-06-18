package org.astrogrid.desktop.modules.ui.voexplorer.google;


import java.awt.Component;
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.dnd.ResourceListTransferable;
import org.astrogrid.desktop.modules.ui.dnd.ResourceTransferable;
import org.votech.VoMon;
import org.votech.VoMonBean;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.EventSelectionModel;


/** A table for displaying resource titles
 * @todo add cut,copy,paste support later.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 12, 20077:33:02 PM
 */
public class ResourceTable extends JTable implements DragGestureListener, DragSourceListener, MouseListener {
	private final List items;
	private final VoMon vomon;
	private final CapabilityIconFactory iconFac;
	private final DragSource dragSource;

	/**
	 * 
	 * @param dm the table model to show.
	 * @param items list (probablby a glazed list) of items that the table model is based upon.
	 * @param vomon monitoring information.
	 */
	public ResourceTable(TableModel dm, List items, VoMon vomon,CapabilityIconFactory iconFac) {
		super(dm);
		this.items = items;
		this.vomon = vomon;
		this.iconFac = iconFac;
		setAutoResizeMode(AUTO_RESIZE_NEXT_COLUMN);
		setBorder(BorderFactory.createEmptyBorder());	
		setShowGrid(false);
		final TableColumnModel cm = getColumnModel();

		cm.getColumn(0).setPreferredWidth(40);
		cm.getColumn(0).setMaxWidth(40);
		cm.getColumn(0).setResizable(true);
		
		cm.getColumn(1).setResizable(true);
		
		cm.getColumn(2).setPreferredWidth(80);
		cm.getColumn(2).setMaxWidth(100);
		cm.getColumn(2).setResizable(true);
		
		cm.getColumn(3).setPreferredWidth(90);
		cm.getColumn(3).setMaxWidth(90);
		cm.getColumn(3).setResizable(true);		
		
		// drag and drop.
		resourceImage = IconHelper.loadIcon("doc16.png").getImage();
		this.dragSource = DragSource.getDefaultDragSource();
		dragSource.createDefaultDragGestureRecognizer(this,DnDConstants.ACTION_LINK,this);
		setTransferHandler( new ResourceTableTransferHandler());
		setDragEnabled(true);
		//right-click menu.
		popup = new JPopupMenu();
		//1.5 only :( setComponentPopupMenu(popup);
		addMouseListener(this);
	}

	/** construct a tool tip, based on vomon information */
	public String getToolTipText(MouseEvent e) {
		String tip = null;
		java.awt.Point p = e.getPoint();
		int rowIndex = rowAtPoint(p);
		int colIndex = columnAtPoint(p);
		int realColumnIndex = convertColumnIndexToModel(colIndex);
		
		if (rowIndex > -1) { 
			Resource ri =(Resource) items.get(rowIndex);  // weakness - possiblility of getting the wrong tooltip if the list is rapidly updating. not the end of the world.
			StringBuffer result = new StringBuffer();
			result.append("<html>");
			switch (realColumnIndex) {
			case 0:// status
				if (ri instanceof Service) {
					VoMonBean b = vomon.checkAvailability(ri.getId());
					if (b == null) {
						result.append("The Monitoring service knows nothing about this service");
					} else {
						result.append("Status at ")
						.append(b.getTimestamp())
						.append(" - <b>" )
						.append(b.getStatus())
						.append("</b>");
					}
				} else if (ri instanceof CeaApplication) {
					VoMonBean[] arr = vomon.checkCeaAvailability(ri.getId());
					if (arr == null || arr.length == 0) {
						result.append("The monitoring service knows of no providers of this application");
					} else {
						result.append("<br>Provided by<ul>");
						for (int i =0; i < arr.length; i++) {
							VoMonBean b = arr[i];
							result.append("<li>")
							.append(b.getId())
							.append(" - status at ")
							.append(b.getTimestamp())
							.append(" - <b>")
							.append(b.getStatus())
							.append("</b>");
						}
						result.append("</ul>");
					}
				}
				break;
			case 1: // title
				result.append("<b>").append(ri.getTitle()).append("</b>");
				result.append("<br><i>").append(ri.getShortName()).append("</i>");
				result.append("<br><i>").append(ri.getId()).append("</i>");
				break;
			case 2: // capabilties.
				Icon ic = (Icon)getModel().getValueAt(rowIndex,2);
				result.append(iconFac.getTooltip(ic));
			default:
				createTooltipFor(realColumnIndex,ri,result);
				break;
			}
			result.append("</html>");                                          
			tip= result.toString(); 
		} else { 
			tip = super.getToolTipText(e);
		}
		return tip;
	}
	
	/** extensionPoint */
	protected void createTooltipFor(int ix,Resource r,StringBuffer sb) {
	}
	
	private JPopupMenu popup;
	
	public void setPopup(JPopupMenu p) {
		this.popup = p;
	}
	public static final Point OFFSET = new Point(8,8);
	public static  final Image RESOURCES_IMAGE = IconHelper.loadIcon("multiplefile16.png").getImage();
	private final Image resourceImage;
	// listen for drag gestures.
	public void dragGestureRecognized(DragGestureEvent dge) {
		Transferable trans = getSelectionTransferable();
		try {
			dge.startDrag(DragSource.DefaultLinkDrop,trans instanceof ResourceListTransferable ? RESOURCES_IMAGE : resourceImage,OFFSET,trans,this); 
		} catch (InvalidDnDOperationException e) {
			}
	}
	
	private class ResourceTableTransferHandler extends TransferHandler {
		public int getSourceActions(JComponent c) {
			return DnDConstants.ACTION_LINK;
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

	// drag source listener - unsure whether there's anything I want to listen to here..
	public void dragDropEnd(DragSourceDropEvent dsde) {
	}

	public void dragEnter(DragSourceDragEvent dsde) {
	}

	public void dragExit(DragSourceEvent dse) {
	}

	public void dragOver(DragSourceDragEvent dsde) {
	}

	public void dropActionChanged(DragSourceDragEvent dsde) {
	}

	// mouse listener interface.
	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
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