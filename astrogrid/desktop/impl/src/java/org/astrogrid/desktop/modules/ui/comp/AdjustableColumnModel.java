package org.astrogrid.desktop.modules.ui.comp;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * This ColumnModel provides enhanced functionality over a normal
 * ColumnModel, aimed at making it easy to select which columns
 * in a related TableModel are shown (appear in this ColumnModel)
 * or not shown (don't appear in this ColumnModel).
 * It does this by using an associated ListModel and ListSelectionModel.
 *
 * @author  Mark Taylor (code lifted from uk.ac.starlink.topcat.MetaColumnModel)
 */
public class AdjustableColumnModel extends DefaultTableColumnModel {

    private DefaultListModel listModel;
    private ListSelectionModel visibleModel;
    private TableModel tableModel;
    private BitSet purgedColumns = new BitSet();

    /**
     * Constructs a new AdjustableColumnModel from a base ColumnModel and a
     * TableModel.  The base ColumnModel is taken as the initial list
     * of columns that can appear in this ColumnModel, but they can
     * be deleted from and reinstated to this model without affecting
     * the base.  Subsequent changes to the base don't change this either.
     *
     * @param  baseColumnModel  the base ColumnModel
     * @param  tableModel       the TableModel which supplies the data
     *         for the columns this object describes; must refer to the
     *         same data as baseColumnModel
     */
    public AdjustableColumnModel( TableColumnModel baseColumnModel,
                                  TableModel tableModel ) {
        this.tableModel = tableModel;

        /* Initialise state of this ColumnModel from the base column model. */
        for ( int ipos = 0; ipos < baseColumnModel.getColumnCount(); ipos++ ) {
            super.addColumn( baseColumnModel.getColumn( ipos ) );
        }
        setColumnMargin( baseColumnModel.getColumnMargin() );
        setSelectionModel( baseColumnModel.getSelectionModel() );
        setColumnSelectionAllowed( baseColumnModel
                                  .getColumnSelectionAllowed() );

        /* Create and initialise a ListModel representing all the columns
         * in the original column model. */
        listModel = new DefaultListModel();
        for ( int ipos = 0; ipos < baseColumnModel.getColumnCount(); ipos++ ) {
            listModel.addElement( baseColumnModel.getColumn( ipos ) );
        }

        /* Create and initialise a ListSelectionModel representing 
         * which columns appear in this ColumnModel. */
        visibleModel = new DefaultListSelectionModel();
        for ( int i = 0; i < listModel.getSize(); i++ ) {
            if ( isVisible( i ) ) {
                visibleModel.addSelectionInterval( i, i );
            }
        }

        /* Make sure that this column model reflects any changes to the
         * visible model. */
        visibleModel.addListSelectionListener( new ListSelectionListener() {
            public void valueChanged( ListSelectionEvent evt ) {
                handleSelectionChange( evt );
            }
        } );

        /* Make sure that if the table model changes so that something
         * interesting comes up, the relevant column is displayed. */
        tableModel.addTableModelListener( new TableModelListener() {
            public void tableChanged( TableModelEvent evt ) {
                handleTableChange( evt );
            }
        } );
    }

    /**
     * Returns the ListModel representing all the columns in the 
     * original TableColumnModel.  Every entry in this ListModel will be
     * a {@link javax.swing.table.TableColumn}.
     * 
     * @return  the list model 
     */
    public ListModel getListModel() {
        return listModel;
    }

    /**
     * Returns the ListSelectionModel representing which columns in the 
     * original TableColumnModel are currently visible (are represented in 
     * this ColumnModel) and which are invisible (are not represented in 
     * this ColumnModel).
     *
     * @return  the selection model
     */
    public ListSelectionModel getVisibleModel() {
        return visibleModel;
    }

    /**
     * Indicates whether this TableColumnModel currently contains the
     * column at a given index in the list model.
     *
     * @param  i  the column we want to know about
     * @return  true iff this column model contains the column at index
     *          <tt>i</tt> in the listModel
     */
    private boolean isVisible( int i ) {

        /* Use DefaultTableColumnModel's protected tableColumns Vector to
         * answer the question. */
        return tableColumns.contains( listModel.get( i ) );
    }

    /**
     * When a column is added to this TableColumnModel, add it to the
     * listModel and set it visible in the visibleModel
     */
    @Override
    public void addColumn( TableColumn tcol ) {
        super.addColumn( tcol );
        if ( ! listModel.contains( tcol ) ) {
            int insertPos = listModel.getSize();
            listModel.addElement( tcol );
            visibleModel.addSelectionInterval( insertPos, insertPos );
            purgedColumns.clear( insertPos );
        }
    }

    /**
     * When a column is removed from this TableColumnModel, set it 
     * invisible in the visibleModel.
     */
    @Override
    public void removeColumn( TableColumn tcol ) {
        super.removeColumn( tcol );
        int ipos = listModel.indexOf( tcol );
        if ( ipos >= 0 ) {
            visibleModel.removeSelectionInterval( ipos, ipos );
            purgedColumns.clear( ipos );
        }
    }

    /**
     * Sets a column invisible.  The column is identified by its position
     * in the list model, equal to its position in the visible model,
     * equal to its position in the base column model from which this
     * model was initialised.
     *
     * @param  ipos  position of the column to remove
     */
    public void removeColumn( int ipos ) {
        removeColumn( (TableColumn) listModel.get( ipos ) );
    }

    /**
     * Purges this column model of any column which contains nothing but
     * blank entries.  Columns purged in this way can be reinstated 
     * later by modifying the visibleModel.  Such a column will also
     * become visible automatically if at a later date it acquires a
     * non-blank entry.
     */
    public void purgeEmptyColumns() {
        int nrow = tableModel.getRowCount();
        for ( int ipos = 0; ipos < listModel.getSize(); ipos++ ) {
            TableColumn tcol = (TableColumn) listModel.get( ipos );
            int modelIndex = tcol.getModelIndex();
            boolean stillBlank = true;
            for ( int irow = 0; irow < nrow && stillBlank; irow++ ) {
                Object val = tableModel.getValueAt( irow, modelIndex );
                stillBlank = stillBlank
                          && ( val == null || 
                               val.toString().trim().length() == 0 );
            }
            if ( stillBlank ) {
                removeColumn( tcol );
                purgedColumns.set( ipos );
            }
        }
    }

    /**
     * Returns a menu component which can be used to control the visibility
     * of columns in the model.
     *
     * @param   name  the name of the menu
     * @return  a new JMenu with checkboxes for each column
     */
    public CheckBoxMenu makeCheckBoxMenu( String name ) {
        CheckBoxMenu menu = new CheckBoxMenu( name );
        for ( int i = 0; i < listModel.getSize(); i++ ) {
            TableColumn tcol = (TableColumn) listModel.getElementAt( i );
            Object title = tcol.getHeaderValue();
            menu.addMenuItem( title == null ? null : title.toString() );
        }
        menu.setSelectionModel( visibleModel );
        return menu;
    }

    /**
     * Returns the names of the columns which are currently visible.
     *
     * @return  names of the visible set of columns
     */
    public String[] getVisibleColumnsByName() {
        int ncol = getColumnCount();
        String[] names = new String[ ncol ];
        for ( int icol = 0; icol < ncol; icol++ ) {
            names[ icol ] = getColumnName( getColumn( icol ) );
        }
        return names;
    }

    /**
     * Sets all columns visible.
     */
    public void setAllVisible() {
        visibleModel.setSelectionInterval( 0, listModel.getSize() - 1 );
    }

    /**
     * Configures this model to show the columns whose names are given in
     * a supplied array.  If any element of this array does not match the
     * name of a column known by this model, no action is taken.
     *
     * @param  colNames  names of the columns to constitute the visible set
     * @return  true iff all columns were found and therefore the visible set
     *          was changed successfully
     */
    public boolean setVisibleColumnsByName( String[] colNames ) {

        /* Prepare an array of column indices from the full list corresponding
         * to the supplied array of column names. */
        int[] colIndices = new int[ colNames.length ];
        boolean foundAll = true;
        Set<String> nameSet = new HashSet<String>();
        for ( int ic = 0; ic < colNames.length && foundAll; ic++ ) {
            if ( nameSet.contains( colNames[ ic ] ) ) {
                throw new IllegalArgumentException( "Duplicate column names" );
            }
            else {
                nameSet.add( colNames[ ic ] );
            }
            boolean found = false;
            for ( int jc = 0; jc < listModel.getSize() && ! found; jc++ ) {
                if ( getColumnName( (TableColumn) listModel.getElementAt( jc ) )
                    .equals( colNames[ ic ] ) ) {
                    colIndices[ ic ] = jc;
                    found = true;
                }
            }
            foundAll = foundAll && found;
        }

        /* If not all the columns were found, return with no further action. */
        if ( ! foundAll ) {
            return false;
        }

        /* Update the visible column selection model. */
        visibleModel.setValueIsAdjusting( true );
        visibleModel.clearSelection();
        for ( int ic = 0; ic < colIndices.length; ic++ ) {
            int jc = colIndices[ ic ];
            visibleModel.addSelectionInterval( jc, jc );
        }
        visibleModel.setValueIsAdjusting( false );

        /* Reorder columns to match the request if necessary.  The declared 
         * semantics of moveColumn suggest that this might result in the
         * desired effect, but I think sensible implementations will work
         * as expected, and the worst that will happen is that the columns
         * end up out of order. */
        int ncol = getColumnCount();
        assert ncol == colNames.length;
        for ( int ito = 0; ito < ncol; ito++ ) {
            int ifrom = ito;
            for ( int jc = ito; jc < ncol; jc++ ) {
                if ( getColumnName( getColumn( jc ) )
                    .equals( colNames[ ito ] ) ) {
                    ifrom = jc;
                }
            }
            if ( ifrom != ito ) {
                moveColumn( ifrom, ito );
            }
        }

        assert Arrays.asList( colNames )
              .equals( Arrays.asList( getVisibleColumnsByName() ) );
        return true;
    }

    /**
     * This method is called when this object's visibleModel's selection
     * status changes.  It's not a direct implementation of the 
     * ListSelectionListener.valueChanged(ListSelectionEvent) method,
     * since TableColumnModel already implements ListSelectionListener
     * so it would get called for the wrong events.
     *
     * @param   evt  the event to handle
     */
    private void handleSelectionChange( ListSelectionEvent evt ) {

        /* Look at each item in the visible list which may have changed. */
        for ( int i = evt.getFirstIndex(); i <= evt.getLastIndex(); i++ ) {

            /* If a column has turned invisible, remove it from this
             * column model. */
            if ( ! visibleModel.isSelectedIndex( i ) && 
                 isVisible( i ) ) {
                TableColumn tcol = (TableColumn) listModel.get( i );
                removeColumn( tcol );
            }

            /* If a column has turned visible, add it to this 
             * column model. */
            else if ( visibleModel.isSelectedIndex( i ) && ! isVisible( i ) ) {

                /* Add the column in question. */
                TableColumn tcol = (TableColumn) listModel.get( i );
                addColumn( tcol );

                /* Try to place it in the order of the original
                 * column model, though if the columns have been
                 * moved around by the user it might end up somewhere
                 * funny. */
                int colPos = listModel.indexOf( tcol );
                int ipos = 0;
                while ( ipos < getColumnCount() && 
                        listModel.indexOf( getColumn( ipos ) ) < colPos ) {
                    ipos++;
                }
                int from = getColumnCount() - 1;
                if ( ipos < from && ipos >= 0 ) {
                    moveColumn( from, ipos );
                }
            }
        }
    }

    /**
     * Called when this object's associated TableModel changes - if an
     * invisible column starts to look interesting, expose it.
     *
     * @param   evt  the event to handle
     */
    private void handleTableChange( TableModelEvent evt ) {
        if ( evt.getType() == TableModelEvent.DELETE ) {
            return;
        }
        TableModel tableModel = (TableModel) evt.getSource();
        int first = Math.max( evt.getFirstRow(), 0 );
        int last = Math.min( evt.getLastRow(), tableModel.getRowCount() - 1 );
        for ( int ipos = 0; ipos < listModel.size(); ipos++ ) {
            if ( purgedColumns.get( ipos ) ) {
                for ( int irow = first; irow <= last; irow++ ) {
                    TableColumn tcol = (TableColumn) listModel.get( ipos );
                    int modelIndex = tcol.getModelIndex();
                    Object val = tableModel.getValueAt( irow, modelIndex );
                    if ( ! isVisible( ipos ) &&
                         ( val != null && 
                           val.toString().trim().length() > 0 ) ) {
                        visibleModel.addSelectionInterval( ipos, ipos );
                    }
                }
            }
        }
    }

    /**
     * Used internally by this class to provide the <em>name</em> of a column -
     * a string which identifies it.
     *
     * @return   stringified column identifier
     */
    public static String getColumnName( TableColumn tcol ) {
        return String.valueOf( tcol.getIdentifier() );
    }
}
