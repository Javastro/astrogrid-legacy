package org.astrogrid.desktop.modules.ui.comp;

import java.util.Comparator;

import ca.odell.glazedlists.gui.AdvancedTableFormat;

/**
 * {@code AdvancedTableFormat} implementation which can be built up 
 * from an array of {@link ModularColumn} objects.  
 * This is a convenience which makes it easier to assemble a multi-column table.
 *
 * @author   Mark Taylor
 * @param <R> The type of object representing one 'row' of the table
 * @since    3 August 2007
 */
public class ModularTableFormat<R> implements AdvancedTableFormat<R> {

    private ModularColumn<R,?>[] columns = new ModularColumn[0];

    /**
     * Configures this object by providing an array of <code>ModularColumn</code>s.
     *
     * @param   columns   array of column configuration objects
     */
    public void setColumns(final ModularColumn<R,?>[] columns) {
        this.columns = columns;
    }
    
    public ModularColumn<R,?>[] getColumns() {
        return this.columns;
    }

    /**
     * Returns the column used for a given column index.
     *
     * @param  icol  column index
     * @return   column description object
     */
    public ModularColumn<R,?> getColumn(final int icol) {
        return columns[icol];
    }

    public int getColumnCount() {
        return columns.length;
    }

    public String getColumnName(final int icol) {
        return getColumn(icol).getColumnName();
    }

    public Object getColumnValue(final R baseObj, final int icol) {
        return getColumn(icol).getColumnValue(baseObj);
    }

    public Class getColumnClass(final int icol) {
        return getColumn(icol).getColumnClass();
    }

    public Comparator getColumnComparator(final int icol) {
        return getColumn(icol).getColumnComparator();
    }
}
