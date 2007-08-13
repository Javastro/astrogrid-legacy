package org.astrogrid.desktop.modules.ui.comp;

import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;

/**
 * Convenience class which implements <code>TableColumnModelListener</code>.
 * All methods do nothing.
 *
 * @author   Mark Taylor
 * @since    6 Aug 2007
 */
public class TableColumnModelAdapter implements TableColumnModelListener {
    public void columnAdded(TableColumnModelEvent evt) {
    }
    public void columnRemoved(TableColumnModelEvent evt) {
    }
    public void columnMoved(TableColumnModelEvent evt) {
    }
    public void columnMarginChanged(ChangeEvent evt) {
    }
    public void columnSelectionChanged(ListSelectionEvent evt) {
    }
}
