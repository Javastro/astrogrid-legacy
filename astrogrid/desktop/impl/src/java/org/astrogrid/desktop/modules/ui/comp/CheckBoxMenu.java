package org.astrogrid.desktop.modules.ui.comp;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Action;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * A menu which contains only checkbox-type entries and has an associated
 * ListSelectionModel.
 *
 * @author   Mark Taylor (code lifted from uk.ac.starlink.topcat.CheckBoxMenu)
 */
public class CheckBoxMenu extends JMenu implements ListSelectionListener {

    private ListSelectionModel selModel;
    private int iBase;

    /**
     * Constructs a new CheckBoxMenu.
     */
    public CheckBoxMenu() {
        super();
        setSelectionModel( new DefaultListSelectionModel() );
    }

    /**
     * Constructs a new CheckBoxMenu with a given name.
     *
     * @param  name  the menu name
     */
    public CheckBoxMenu( final String name ) {
        this(); 
        setText( name );
    }

    /**
     * Returns the number of tickable entries in the menu.
     *
     * @return  number of entries
     */
    public int getEntryCount() {
        return getItemCount() - iBase;
    }

    public void addListSelectionListener( final ListSelectionListener listener ) {
        selModel.addListSelectionListener( listener );
    }

    public void removeListSelectionListener( final ListSelectionListener listener ) {
        selModel.removeListSelectionListener( listener );
    }

    /**
     * Inserts a menu item based on an action.
     * The new item is added at the head of the menu, in a region separated
     * from the main (checkboxes) part of the menu.
     *
     * @param  action  action to add
     */
    public void insertAction( final Action action ) {
        if ( iBase == 0 ) {
            insertSeparator( iBase++ );
        }
        insert( action, iBase++ - 1 );
    }

    /**
     * Adds an item to the menu.  The item will be represented as a 
     * checkbox menu item; ticking/unticking it will cause this object's
     * selection model to be updated (and vice versa).
     *
     * @param  text  the label for the next item on the menu
     */
    public void addMenuItem( final String text ) {
        final int index = getItemCount() - iBase;
        final JCheckBoxMenuItem item = 
            new JCheckBoxMenuItem( text, selModel.isSelectedIndex( index ) );
        item.addItemListener( new ItemListener() {
            public void itemStateChanged( final ItemEvent evt ) {
                if ( item.getState() ) {
                    selModel.addSelectionInterval( index, index );
                }
                else {
                    selModel.removeSelectionInterval( index, index );
                }
            }
        } );
        add( item );
    }

    /**
     * Returns the selection model used to keep track of the ticked/unticked
     * status of the checkboxes in this menu.
     *
     * @return   the selection model
     */
    public ListSelectionModel getSelectionModel() {
        return selModel;
    }

    /**
     * Sets the selection model used to keep track of the ticked/unticked
     * status of the checkboxes in this menu.  You can slot your own
     * model in here, any previous one is discarded by this object.
     * 
     * @param   selModel the new selection model
     */
    public void setSelectionModel( final ListSelectionModel selModel ) {
        if ( this.selModel != null ) {
            this.selModel.removeListSelectionListener( this );
        }
        this.selModel = selModel;
        for ( int i = iBase; i < getItemCount(); i++ ) {
            ((JCheckBoxMenuItem) getItem( i ))
           .setState( selModel.isSelectedIndex( i - iBase ) );
        }
        selModel.addListSelectionListener( this );
    }

    public void valueChanged( final ListSelectionEvent evt ) {
        final int first = evt.getFirstIndex();
        final int last = evt.getLastIndex();
        if (first  > last) {
            throw new IllegalArgumentException("first index cannod be larger than last");
        }
        for ( int i = first; i <= last; i++ ) {
            ((JCheckBoxMenuItem) getItem( i + iBase ))
           .setState( selModel.isSelectedIndex( i ) );
        }
    }
    
}
