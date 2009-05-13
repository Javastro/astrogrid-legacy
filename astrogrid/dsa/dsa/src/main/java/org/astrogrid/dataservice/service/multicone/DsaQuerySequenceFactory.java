package org.astrogrid.dataservice.service.multicone;

import java.io.IOException;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.Tables;
import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.ValueInfo;
import uk.ac.starlink.ttools.cone.ColumnQueryRowSequence;
import uk.ac.starlink.ttools.cone.ConeQueryRowSequence;
import uk.ac.starlink.ttools.cone.QuerySequenceFactory;

/**
 * QuerySequenceFactory implementation for use with public Multicone service.
 * The columns giving RA and Declination are named in the constructor.
 * If no names are supplied, then suitable columns are located by looking
 * for UCDs.  Some leniency is permitted: for RA any of
 * <ul>
 * <li>POS_EQ_RA</li>
 * <li>POS_EQ_RA_MAIN</li>
 * <li>pos.eq.ra</li>
 * <li>pos.eq.ra;meta.main</li>
 * </ul>
 * are permitted (matching is case-insensitive).
 *
 * @author   Mark Taylor
 * @since    24 Oct 2007
 */
public class DsaQuerySequenceFactory implements QuerySequenceFactory {

    private final String raName;
    private final String decName;
    private final double sr;

    /**
     * Constructor.
     *
     * @param  raName  name of RA column in degrees - 
     *         if null UCD is used to locate a suitable column
     * @param  decName name of Dec column in degrees -
     *         if null UCD is used to locate a suitable column
     * @param  sr   value of search radius in degrees
     */
    public DsaQuerySequenceFactory(String raName, String decName, double sr) {
        this.raName = raName;
        this.decName = decName;
        this.sr = sr;
    }

    public ConeQueryRowSequence createQuerySequence(StarTable table)
            throws IOException {
        ColumnInfo[] colInfos = Tables.getColumnInfos(table);
        int raIndex = locateColumn(colInfos, raName, "ra");
        int decIndex = locateColumn(colInfos, decName, "dec");
        return ColumnQueryRowSequence
              .createFixedRadiusSequence(table, raIndex, decIndex, sr);
    }

    /**
     * Locates a column corresponding to a required RA or Dec quantity.
     *
     * @param   infos  column metadata objects
     * @param   colName  name of column to use - if null, try UCDs instead
     * @param   ucdAtom  atom of pos.eq UCD specific to the reqired quantity
     * @return  index into <code>infos</code> of required quantity
     * @throws  IOException if no suitable column could be located
     */
    private static int locateColumn(ValueInfo[] infos, String colName, 
                                    String ucdAtom) throws IOException {

        // If name is supplied, seek a column with that name.
        if (colName != null && colName.trim().length() > 0) {
            for (int i = 0; i < infos.length; i++) {
                if (colName.equalsIgnoreCase(infos[i].getName())) {
                    return i;
                }
            }
            throw new IOException("No column named \"" + colName + "\"");
        }

        // Otherwise look for a column with a suitable UCD.
        else {

            // Prepare possible matching UCD1 and UCD1+ strings.
            String atom = ucdAtom.toLowerCase();
            final String ucd1Part = "pos_eq_" + atom;
            final String ucd1Full = ucd1Part + "_main";
            final String ucd1pPart = "pos.eq." + atom;
            final String ucd1pFull = ucd1pPart + ";meta.main";

            // Examine each column.
            int possibleIndex = -1;
            for (int i = 0; i < infos.length; i++) {
                String ucd = infos[i].getUCD();
                if (ucd != null && ucd.length() > 0) {
                    ucd = ucd.trim().toLowerCase();

                    // If we find a match to the full UCD, we're done.
                    if (ucd.equals(ucd1Full) || ucd.equals(ucd1pFull)) {
                        return i;
                    }

                    // If we find a match to the partial UCD, remember it.
                    else if (ucd.equals(ucd1Part) || ucd.equals(ucd1pPart)) {
                        possibleIndex = i;
                    }
                }
            }

            // If no full match has been found but a partial match has, then
            // use that.
            if (possibleIndex >= 0) {
                return possibleIndex;
            }

            // Otherwise, report failure.
            else {
                throw new IOException("No column with " + ucd1pFull + " UCD");
            }
        }
    }
}
