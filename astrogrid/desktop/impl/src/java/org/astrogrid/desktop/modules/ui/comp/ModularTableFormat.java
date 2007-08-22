package org.astrogrid.desktop.modules.ui.comp;

import ca.odell.glazedlists.gui.AdvancedTableFormat;

import java.util.Collection;
import java.util.Comparator;

/**
 * <code>AdvancedTableFormat</code> implementation which can be built up 
 * from an array of {@link ModularColumn} objects.  
 * This is a convenience which makes it easier to assemble a multi-column table.
 *
 * @author   Mark Taylor
 * @since    3 August 2007
 */
public class ModularTableFormat implements AdvancedTableFormat {

    private ModularColumn[] columns = new ModularColumn[0];

    /**
     * Configures this object by providing an array of <code>ModularColumn</code>s.
     *
     * @param   columns   array of column configuration objects
     */
    public void setColumns(ModularColumn[] columns) {
        this.columns = columns;
    }
    
    public ModularColumn[] getColumns() {
        return this.columns;
    }

    /**
     * Returns the column used for a given column index.
     *
     * @param  icol  column index
     * @return   column description object
     */
    public ModularColumn getColumn(int icol) {
        return columns[icol];
    }

    public int getColumnCount() {
        return columns.length;
    }

    public String getColumnName(int icol) {
        return getColumn(icol).getColumnName();
    }

    public Object getColumnValue(Object baseObj, int icol) {
        return getColumn(icol).getColumnValue(baseObj);
    }

    public Class getColumnClass(int icol) {
        return getColumn(icol).getColumnClass();
    }

    public Comparator getColumnComparator(int icol) {
        return getColumn(icol).getColumnComparator();
    }
}
