/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.astrogrid.desktop.modules.system.ui.ActivitiesManager;
import org.astrogrid.desktop.modules.ui.dnd.FileObjectListTransferable;
import org.astrogrid.desktop.modules.ui.dnd.FileObjectTransferable;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.ListSelection;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.matchers.CompositeMatcherEditor;
import ca.odell.glazedlists.matchers.Matcher;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.swing.EventSelectionModel;

/**
 * 
 * Class that provides a shared data/selection/dnd model for all linked file views.
 * 
 * - ensures that Dnd behaviour is uniform between all views that are linked to the same model.
 * in the same principle, this class manages a single selection model which can be 
 * used by all linked views too.
 * 
 * this abstract class handles the core functionality - subclass handles the Dnd aspects.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 30, 20073:45:32 PM
 */
public class Filemodel implements ListSelectionListener{


    /**
     * @return the selection
     */
    public final EventSelectionModel getSelection() {
        return this.selection;
    }

    /**
     * @return the files
     */
    public final SortedList getChildrenList() {
        return this.files;
    }

    /***     determine whether event should trigger popup menu
     * then update selection model before displpaying the popup
     * @param event
     */
        public final void maybeShowPopupMenu( MouseEvent event ){
           if ( event.isPopupTrigger() && activities.getPopupMenu() != null ) {
               //@todo - only update activities if what we've clicked on is not alreays part of the selection
               updateActivities();           
                activities.getPopupMenu().show( event.getComponent(),
                        event.getX(), event.getY() );
           }
        }
        
        /**
         * update activities to reflect current selection.
         * rarely need to call this method directly, as selecction model events cause it
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
        
        protected Transferable getSelectionTransferable() {
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
        /**
         * @param mode
         */
        public final void setSelectionMode(int mode) {
            selection.setSelectionMode(mode);
        }   
        
        /** add an additional filter to the file view */
        public final void installFilter(Matcher m) {
            programmaticFilter.setMatcher(m);
        }

    /**
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Aug 30, 20072:20:51 PM
     */
    protected static final class NoHiddenFiles implements Matcher {
        public boolean matches(Object arg0) {
            FileObject fo = (FileObject)arg0;
            try {
                return !(fo.isHidden() || fo.getName().getBaseName().charAt(0) == '.') ;
            } catch (FileSystemException x) {
                return true;
            }
        }
    }
    //
    /**
     * Logger for this class
     */
    protected static final Log logger = LogFactory
            .getLog(Filemodel.class);

    protected final EventSelectionModel selection;
    protected final SortedList files;
    protected final IconFinder icons;
    protected final VFSOperations ops;

    protected final ActivitiesManager activities;

    protected final MutableMatcherEditor programmaticFilter;

    protected final MutableMatcherEditor hiddenFilter;
    private final TransferHandler handler;
    // when selection changes.
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        updateActivities();
        
    }
    

    public void enableDragAndDropFor(JComponent comp) {
        
        comp.setTransferHandler(handler); 
       
    }
    

    public Filemodel(SortedList files,MutableMatcherEditor programmaticFilter, MutableMatcherEditor hiddenFilter, ActivitiesManager activities,IconFinder icons, VFSOperations ops) {
        
        super();
        this.programmaticFilter = programmaticFilter;
        this.hiddenFilter = hiddenFilter;
        this.activities = activities;
        this.ops = ops;
        this.files = files;
        this.selection = new EventSelectionModel(files);
        selection.setSelectionMode(ListSelection.MULTIPLE_INTERVAL_SELECTION_DEFENSIVE);
        selection.addListSelectionListener(this); // listen to currently selected files.
            
        this.icons = icons;
        this.handler = new FileModelTransferHandler(this);
    }

    // factory method
    /** a complex object to build - need to use a factory method */
    public static final Filemodel newInstance(MatcherEditor ed,ActivitiesManager activities,IconFinder icons, VFSOperations ops) {
        MutableMatcherEditor programmaticFilter = new MutableMatcherEditor();
        MutableMatcherEditor hiddenFilter = new MutableMatcherEditor();
        hiddenFilter.setMatcher(new NoHiddenFiles());
        // make a composite out of all these matchers.
        CompositeMatcherEditor composite = new CompositeMatcherEditor();
        composite.setMode(CompositeMatcherEditor.AND);
        composite.getMatcherEditors().add(programmaticFilter);
        composite.getMatcherEditors().add(hiddenFilter);
        if (ed != null) {
            composite.getMatcherEditors().add(ed);
        }
        EventList filteredFiles = new FilterList(new BasicEventList(),composite);
        SortedList list = new SortedList(filteredFiles, FileObjectComparator.getInstance());
//       return new FileModelAWTImpl(list,programmaticFilter, hiddenFilter,activities,icons,ops);
        return new Filemodel(list,programmaticFilter, hiddenFilter,activities,icons,ops);

    }

}