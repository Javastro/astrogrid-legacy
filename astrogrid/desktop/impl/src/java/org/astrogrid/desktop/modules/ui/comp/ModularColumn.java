package org.astrogrid.desktop.modules.ui.comp;

import java.util.Comparator;

/**
 * Configuration object for a single column of a Glazed Lists table.
 * Used in configuration with {@link ModularTableFormat} to define the data 
 * and metadata for a single column of a table.
 * Implementations must implement {@link #getColumnValue} to provide the data.
 *
 * @author   Mark Taylor
 * @param <B> type of the base object
 * @param <C> type of the projected column value
 * @since    3 August 2007
 * @see   {@link ModularTableFormat}
 */
public abstract class ModularColumn<B,C> {

    private final String name;
    private final Class<C> clazz;
    private final Comparator<? super C> comparator;

    /**
     * Constructor.
     *
     * @param  name  column name
     * @param  clazz  most specific known superclass for values in this column
     * @param  comparator  default comparator to use for this column;
     *                     use null for a non-comparable column
     */
    public ModularColumn(final String name, final Class<C> clazz, final Comparator<? super C> comparator) {
        this.name = name;
        this.clazz = clazz;
        this.comparator = comparator;
    }

    /**
     * Returns the name of this column.
     *
     * @return  column name
     */
    public String getColumnName() {
        return name;
    }

    /**
     * Returns the value for this column of the given base object.
     *
     * @param  baseObj  the object EventList underlying the required table row
     * @return  object in this column corresponding to <code>baseObj</code>
     */
    public abstract C getColumnValue(B baseObj);

    /**
     * Returns the most specific known superclass for values in this column.
     *
     * @return   content class
     */
    public Class<C> getColumnClass() {
        return clazz;
    }

    /**
     * Returns the default comparator to use for this column.
     *
     * @return   comparator  comparator, or <code>null</code> for an incomparable column
     */
    public Comparator<? super C> getColumnComparator() {
        return comparator;
    }
}
