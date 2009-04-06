/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.awt.datatransfer.Transferable;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.system.ui.ActivitiesManager;
import org.astrogrid.desktop.modules.ui.dnd.FileObjectListTransferable;
import org.astrogrid.desktop.modules.ui.dnd.FileObjectTransferable;

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
 * A shared data/selection/dnd model for all linked file views.
 * 
 * - ensures that Dnd behaviour is uniform between all views that are linked to the same model.
 * in the same principle, this class manages a single selection model which can be 
 * used by all linked views too.
 * 
 * this abstract class handles the core functionality - subclass handles the Dnd aspects.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 30, 20073:45:32 PM
 * @TEST think about testing this.
 */
public class Filemodel implements ListSelectionListener{


    /**
     * @return the selection
     */
    public final EventSelectionModel<FileObjectView> getSelection() {
        return this.selection;
    }

    /**
     * @return the files
     */
    public final SortedList<FileObjectView> getChildrenList() {
        return this.files;
    }

    /***     determine whether event should trigger popup menu
     * then update selection model before displpaying the popup
     * @param event
     */
        public final void maybeShowPopupMenu( final MouseEvent event ){
           if ( event.isPopupTrigger() && activities.getPopupMenu() != null ) {               
               //minor optimization - we should only update activities if what we've clicked on is not alreays part of the selection
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
            if (activities.getPopupMenu() != null && activities.getPopupMenu().isVisible()) {
                // if the popup menu is showing, then whatever click caused a change of the selection should really be directed at the popup menu
                // so we should ignore it here.
                return;
            }
            final Transferable tran =getSelectionTransferable();
                if (tran == null) {
                    activities.clearSelection();
                } else {
                    activities.setSelection(tran);
                }
        }
        
        protected Transferable getSelectionTransferable() {
            final EventList<FileObjectView> selected =selection.getSelected();
            switch (selected.size()) {
            case 0:
                return null;
            case 1:
                return new FileObjectTransferable(selected.get(0));
            default:
                return  new FileObjectListTransferable(selected);
            }       
        }
        /**
         * @param mode
         */
        public final void setSelectionMode(final int mode) {
            selection.setSelectionMode(mode);
        }   
        
        /** add an additional filter to the file view */
        public final void installFilter(final Matcher<? super FileObjectView> m) {
            programmaticFilter.setMatcher(m);
        }

    /** A matcher that filters out hidden files.
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Aug 30, 20072:20:51 PM
     */
    protected static final class NoHiddenFiles implements Matcher<FileObjectView> {
        public boolean matches(final FileObjectView fo) {
                return !(fo.isHidden() || fo.getBasename().charAt(0) == '.') ;
        }
    }
    //
    /**
     * Logger for this class
     */
    protected static final Log logger = LogFactory
            .getLog(Filemodel.class);

    protected final EventSelectionModel<FileObjectView> selection;
    private final SortedList<FileObjectView> files;
    private final IconFinder icons;
    protected final VFSOperations ops;

    private final ActivitiesManager activities;

    private final MutableMatcherEditor<FileObjectView> programmaticFilter;

    private final MutableMatcherEditor<FileObjectView> hiddenFilter;
    private final TransferHandler handler;
    // when selection changes.
    public void valueChanged(final ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        updateActivities();
        
    }
    

    public void enableDragAndDropFor(final JComponent comp) {
        
        comp.setTransferHandler(handler); 
       
    }
    

    public Filemodel(final SortedList<FileObjectView> files,final MutableMatcherEditor<FileObjectView> programmaticFilter, final MutableMatcherEditor<FileObjectView> hiddenFilter, final ActivitiesManager activities,final IconFinder icons, final VFSOperations ops) {
        
        super();
        this.programmaticFilter = programmaticFilter;
        this.hiddenFilter = hiddenFilter;
        this.activities = activities;
        this.ops = ops;
        this.files = files;
        this.selection = new EventSelectionModel<FileObjectView>(files);
        selection.setSelectionMode(ListSelection.MULTIPLE_INTERVAL_SELECTION_DEFENSIVE);
        selection.addListSelectionListener(this); // listen to currently selected files.
            
        this.icons = icons;
        this.handler = new FileModelTransferHandler(this);
    }

    // factory method
    /** a complex object to build - need to use a factory method */
    public static final Filemodel newInstance(final MatcherEditor<FileObjectView> ed,final ActivitiesManager activities,final IconFinder icons, final VFSOperations ops) {
        final MutableMatcherEditor<FileObjectView> programmaticFilter = new MutableMatcherEditor<FileObjectView>();
        final MutableMatcherEditor<FileObjectView> hiddenFilter = new MutableMatcherEditor<FileObjectView>();
        hiddenFilter.setMatcher(new NoHiddenFiles());
        // make a composite out of all these matchers.
        final CompositeMatcherEditor<FileObjectView> composite = new CompositeMatcherEditor<FileObjectView>();
        composite.setMode(CompositeMatcherEditor.AND);
        composite.getMatcherEditors().add(programmaticFilter);
        composite.getMatcherEditors().add(hiddenFilter);
        if (ed != null) {
            composite.getMatcherEditors().add(ed);
        }
        final EventList<FileObjectView> filteredFiles = new FilterList<FileObjectView>(new BasicEventList<FileObjectView>(),composite);
        final SortedList<FileObjectView> list = new SortedList<FileObjectView>(filteredFiles, FileObjectViewComparator.getInstance());
//       return new FileModelAWTImpl(list,programmaticFilter, hiddenFilter,activities,icons,ops);
        return new Filemodel(list,programmaticFilter, hiddenFilter,activities,icons,ops);

    }

    /**
     * @return the icons
     */
    public IconFinder getIcons() {
        return this.icons;
    }

}