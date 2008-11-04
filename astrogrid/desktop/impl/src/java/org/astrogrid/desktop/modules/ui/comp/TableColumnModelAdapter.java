package org.astrogrid.desktop.modules.ui.comp;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;

/**
 * Adapter class that implements <code>TableColumnModelListener</code>.
 * All methods do nothing.
 *
 * @author   Mark Taylor
 * @since    6 Aug 2007
 */
public class TableColumnModelAdapter implements TableColumnModelListener {
    public void columnAdded(final TableColumnModelEvent evt) {
    }
    public void columnRemoved(final TableColumnModelEvent evt) {
    }
    public void columnMoved(final TableColumnModelEvent evt) {
    }
    public void columnMarginChanged(final ChangeEvent evt) {
    }
    public void columnSelectionChanged(final ListSelectionEvent evt) {
    }
}
