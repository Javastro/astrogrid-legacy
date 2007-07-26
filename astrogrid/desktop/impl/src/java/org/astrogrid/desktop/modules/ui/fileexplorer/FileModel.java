/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.awt.Component;
import java.awt.Image;
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
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.astrogrid.desktop.modules.system.ui.ActivitiesManager;
import org.astrogrid.desktop.modules.ui.dnd.FileObjectListTransferable;
import org.astrogrid.desktop.modules.ui.dnd.FileObjectTransferable;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.desktop.modules.ui.voexplorer.ResourceLists;
import org.astrogrid.desktop.modules.ui.voexplorer.google.ResourceTable;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.ListSelection;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.swing.EventSelectionModel;

/** 
 * Class that provides a shared data/selection/dnd model for all linked file views.
 * 
 * - ensures that Dnd behaviour is uniform between all views that are linked to the same model.
 * in the same principle, this class manages a single selection model which can be 
 * used by all linked views too.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 30, 20072:13:44 PM
 */
public class FileModel implements DragGestureListener, DragSourceListener, DropTargetListener, ListSelectionListener{
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(FileModel.class);

	private final EventSelectionModel selection;
	private final SortedList files;
	private final IconFinder icons;
	private final VFSOperations ops;

    private final ActivitiesManager activities;
	
	public FileModel(SortedList files,ActivitiesManager activities,IconFinder icons, VFSOperations ops) {
		
		super();
        this.activities = activities;
		this.ops = ops;
		this.files = files;
		this.selection = new EventSelectionModel(files);
        selection.setSelectionMode(ListSelection.MULTIPLE_INTERVAL_SELECTION_DEFENSIVE);
        selection.addListSelectionListener(this); // listen to currently selected files.
    		
		this.icons = icons;
	}

	public void dragGestureRecognized(DragGestureEvent dge) {
		Transferable trans = getSelectionTransferable();
		Image i;
		EventList selected = selection.getSelected();
		if (selected.size() > 1) {
			i = ResourceTable.RESOURCES_IMAGE; // multiple selection
		} else {
			i = icons.find((FileObject)selected.get(0)).getImage(); 
		}
		try {
		dge.startDrag(DragSource.DefaultLinkDrop,i,ResourceTable.OFFSET,trans,this); 
		} catch (InvalidDnDOperationException e) {
		}				
	}
	
	public Transferable getSelectionTransferable() {
		final EventList selected =selection.getSelected();
		switch (selected.size()) {
		case 0:
			return null;
		case 1:
			try {
					return new FileObjectTransferable((FileObject)selected.get(0));
				} catch (Exception x) {
					logger.error("FileSystemException",x);
					return null;
				} 
		default:
			return  new FileObjectListTransferable(selected);
		} 		
	}

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
// drop target code.
	
	private final static DataFlavor[] inputFlavors = new DataFlavor[] {
		VoDataFlavour.LOCAL_FILEOBJECT
		,VoDataFlavour.LOCAL_FILEOBJECT_LIST
		//@todo add some external dataflavors here.
	};
	
	private final static Predicate dragPredicate = new Predicate() {
		public boolean evaluate(Object arg0) {
			return ArrayUtils.contains(inputFlavors,arg0);
		}
	};
	
	public void dragEnter(DropTargetDragEvent dtde) {
		Component component = dtde.getDropTargetContext().getComponent();
		//@todo should I check the source of this dnd operation?
		if (CollectionUtils.exists(dtde.getCurrentDataFlavorsAsList(),dragPredicate)) {
			dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
			if (component instanceof JComponent) {// most likely.
				((JComponent)component).setBorder(ResourceLists.DROP_BORDER);
			}
		}
	}

	public void dragExit(DropTargetEvent dte) {
		Component component = dte.getDropTargetContext().getComponent();
		if (component instanceof JComponent) {// most likely.
			((JComponent)component).setBorder(ResourceLists.EMPTY_BORDER);
		}
	}

	public void dragOver(DropTargetDragEvent dtde) {
	}

	public void drop(DropTargetDropEvent dtde) {
		Component component = dtde.getDropTargetContext().getComponent();
		if (component instanceof JComponent) {// most likely.
			((JComponent)component).setBorder(ResourceLists.EMPTY_BORDER);
		}	
		Transferable t = dtde.getTransferable();
		if (CollectionUtils.exists(dtde.getCurrentDataFlavorsAsList(),dragPredicate)) {
			dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
			try {
				List fileObjects = null;
				if (t.isDataFlavorSupported(VoDataFlavour.LOCAL_FILEOBJECT)) {
					fileObjects = Collections.singletonList(t.getTransferData(VoDataFlavour.LOCAL_FILEOBJECT));
				} else if (t.isDataFlavorSupported(VoDataFlavour.LOCAL_FILEOBJECT_LIST)) {
					fileObjects = (List)t.getTransferData(VoDataFlavour.LOCAL_FILEOBJECT_LIST);
				} else {
					logger.warn("Unknown type of transferable " + t);					
				}
				 if (fileObjects != null) {
					 ops.copyOrMoveToCurrent(fileObjects);
					 //@todo refresh at this point.
					 dtde.dropComplete(true);
				 } else {
					 dtde.dropComplete(false);
				 }
			} catch (Exception e) {
				dtde.dropComplete(false);
			}
		} else {
			dtde.rejectDrop();
		}
	}

	public void dropActionChanged(DropTargetDragEvent dtde) {
	}

	/** enabled drag and drop for a components contents, with this object being the handler */
	public void enableDragAndDropFor(JComponent comp) {
		DragSource dragSource = DragSource.getDefaultDragSource();
		dragSource.createDefaultDragGestureRecognizer(comp,DnDConstants.ACTION_LINK | DnDConstants.ACTION_COPY_OR_MOVE,this);
		comp.setDropTarget(new DropTarget(comp,DnDConstants.ACTION_COPY_OR_MOVE,this));
		
	}

    /**
     * @return the selection
     */
    public final EventSelectionModel getSelection() {
        return this.selection;
    }

    /**
     * @return the files
     */
    public final SortedList getFiles() {
        return this.files;
    }

    /**
     * @return the activities
     */
    public final ActivitiesManager getActivities() {
        return this.activities;
    }
   
    /***     determine whether event should trigger popup menu
 * then update selection model before displpaying the popup
 * @param event
 */
    public void maybeShowPopupMenu( MouseEvent event ){
       if ( event.isPopupTrigger() ) {
           updateActivities();
            activities.getPopupMenu().show( event.getComponent(),
                    event.getX(), event.getY() );
       }
    }

    /**
     * update activities to reflect current selection.
     * rarely need to call this method, as selecction model events cause it
     * to be triggered anyhow.
     */
    public void updateActivities() {
        Transferable tran =getSelectionTransferable();
            if (tran == null) {
                activities.clearSelection();
            } else {
                activities.setSelection(tran);
            }
    }

    // when selection changes.
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        updateActivities();
        
    }   
    
	

}
