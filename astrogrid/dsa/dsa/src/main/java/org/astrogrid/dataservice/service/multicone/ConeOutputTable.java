package org.astrogrid.dataservice.service.multicone;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.RowSequence;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.WrapperRowSequence;
import uk.ac.starlink.table.WrapperStarTable;

/**
 * Adapts a table recovered from a STIL JDBC query to a form suitable for
 * output as the result of a Cone Search.
 *
 * @author   Mark Taylor
 * @since    14 Dec 2009
 */
public class ConeOutputTable extends WrapperStarTable {

    private final ValueHandler[] valHandlers_;

    /**
     * Constructor.
     *
     * @param   baseTable  input table
     */
    public ConeOutputTable(StarTable baseTable) {
        super(baseTable);
        int ncol = baseTable.getColumnCount();
        valHandlers_ = new ValueHandler[ncol];
        for (int icol = 0; icol < ncol; icol++) {
            valHandlers_[icol] =
                createValueHandler(baseTable.getColumnInfo(icol));
        }
    }

    @Override
    public ColumnInfo getColumnInfo(int icol) {
        return valHandlers_[icol].getColumnInfo();
    }

    @Override
    public Object getCell(long irow, int icol) throws IOException {
        return valHandlers_[icol].getValue(super.getCell(irow, icol));
    }

    @Override
    public Object[] getRow(long irow) throws IOException {
        Object[] row = super.getRow(irow);
        for (int icol = 0; icol < row.length; icol++) {
            row[icol] = valHandlers_[icol].getValue(row[icol]);
        }
        return row;
    }

    @Override
    public RowSequence getRowSequence() throws IOException {
        return new WrapperRowSequence(super.getRowSequence()) {
            @Override
            public Object getCell(int icol) throws IOException {
                return valHandlers_[icol].getValue(super.getCell(icol));
            }
            @Override
            public Object[] getRow() throws IOException {
                Object[] row = super.getRow();
                for (int icol = 0; icol < row.length; icol++) {
                    row[icol] = valHandlers_[icol].getValue(row[icol]);
                }
                return row;
            }
        };
    }

    /**
     * Generates a handler suitable for a given column.
     *
     * @param   baseInfo  column metadata for the column whose values will
     *          be adjusted
     * @return  new value handler
     */
    private static ValueHandler createValueHandler(ColumnInfo baseInfo) {
        Class clazz = baseInfo.getContentClass();
        String ucd = baseInfo.getUCD();

        // Stringify JDBC types which can't be written to VOTable.
        if (Timestamp.class.isAssignableFrom(clazz) ||
            Time.class.isAssignableFrom(clazz) ||
            Date.class.isAssignableFrom(clazz)) {
            return new StringValueHandler(baseInfo);
        }

        // Cone Search standard mandates that the ID_MAIN column is of 
        // string type.
        else if ("ID_MAIN".equals(ucd)) {
            return new StringValueHandler(baseInfo);
        }

        // Pass anything else through unchanged.
        else {
            return new CopyValueHandler(baseInfo);
        }
    }

    /**
     * Interface for an object that can adjust values.
     */
    private static abstract class ValueHandler {

        /**
         * Returns the column metadata for this handler.
         *
         * @return   column metadata
         */
        abstract ColumnInfo getColumnInfo();

        /**
         * Returns an adjusted version of a given value.
         *
         * @param   baseValue  value to adjust
         * @return  adjusted value
         */
        abstract Object getValue(Object baseValue);
    }

    /**
     * ValueHandler implementation which passes data and metadata through
     * unchanged.
     */
    private static class CopyValueHandler extends ValueHandler {
        private final ColumnInfo info_;
        CopyValueHandler(ColumnInfo baseInfo) {
            info_ = baseInfo;
        }
        ColumnInfo getColumnInfo() {
            return info_;
        }
        Object getValue(Object baseValue) {
            return baseValue;
        }
    }

    /**
     * ValueHandler implementation which turns Object data into strings.
     */
    private static class StringValueHandler extends ValueHandler {
        private final ColumnInfo info_;
        StringValueHandler(ColumnInfo baseInfo) {
            info_ = new ColumnInfo(baseInfo);
            info_.setContentClass(String.class);
        }
        ColumnInfo getColumnInfo() {
            return info_;
        }
        Object getValue(Object baseValue) {
            return (String)
                   (baseValue == null ? null : baseValue.toString());
        }
    }
}
