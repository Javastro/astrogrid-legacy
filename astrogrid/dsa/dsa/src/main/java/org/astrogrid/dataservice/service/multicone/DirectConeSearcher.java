package org.astrogrid.dataservice.service.multicone;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.dataservice.metadata.queryable.SearchField;
import org.astrogrid.dataservice.queriers.DatabaseAccessException;
import org.astrogrid.dataservice.service.TokenQueue;
import org.astrogrid.tableserver.jdbc.JdbcConnections;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.DefaultValueInfo;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.ValueInfo;
import uk.ac.starlink.table.WrapperStarTable;
import uk.ac.starlink.ttools.cone.AngleUnits;
import uk.ac.starlink.ttools.cone.ConeSearcher;
import uk.ac.starlink.ttools.cone.JdbcConeSearcher;
import uk.ac.starlink.ttools.cone.SkyTiling;

/**
 * Provides a ConeSearcher which works by making SQL queries over 
 * a direct JDBC connection to the database.
 */
public class DirectConeSearcher {

    /**
     * Private constructor prevents instantiation.
     */
    private DirectConeSearcher() {
    }

    /**
     * Constructs a direct cone searcher.
     * If it fails a ServletException is thrown and the token is released.
     *
     * @param  token        token reserving place in resource allocation queue
     *                      (released on failure)
     * @param  catalogName  table catalogue name
     * @param  tableName    table name
     * @param  bestOnly     true for best match only, false for all matches
     * @return  new cone searcher
     */
    public static ConeSearcher createConeSearcher(TokenQueue.Token token,
                                                  String catalogName,
                                                  String tableName,
                                                  boolean bestOnly)
            throws ServletException {
        try {
            return attemptCreateConeSearcher(token, catalogName, tableName,
                                             bestOnly);
        }
        catch (ServletException e) {
            token.release();
            throw e;
        }
        catch (Throwable e) {
            token.release();
            throw (ServletException) new ServletException(e.getMessage())
                                    .initCause(e);
        }
    }

    /**
     * Does the work for constructing a new cone searcher, throwing various
     * checked exceptions.
     * KEA comments:  The catalogName and tableName coming in here are 
     * "published names" (what appears in the registry) rather than "IDs"
     * (what appears in the database).  We need to translate the names to
     * their respective IDs to produce an sql query that the database will
     * understand.
     *
     * @param  token        token reserving place in resource allocation queue;
     *                      will be released on ConeSearcher close
     * @param  catalogName  table catalogue name
     * @param  tableName    table name
     * @param  bestOnly     true for best match only, false for all matches
     * @return  new cone searcher
     */
    private static ConeSearcher attemptCreateConeSearcher(
                final TokenQueue.Token token, String catalogName,
                String tableName, boolean bestOnly)
            throws ServletException, SQLException,
                   MetadataException, DatabaseAccessException {
        Connection connection = 
               JdbcConnections.makeFromConfig().getConnection();
        String catalogID = TableMetaDocInterpreter.getCatalogIDForName(
              catalogName); 
        String tableID = TableMetaDocInterpreter.getTableIDForName(
              catalogName,tableName); 
        String fullTableName =
            getQualifiedTableName(connection, catalogID, tableID);
        String raColName = TableMetaDocInterpreter
                      .getConeRAColumnByName(catalogName, tableName);
        String decColName = TableMetaDocInterpreter
                       .getConeDecColumnByName(catalogName, tableName);
        String unitName = TableMetaDocInterpreter
                         .getConeUnitsByName(catalogName, tableName);
        String raCol = TableMetaDocInterpreter.getColumnIDForName(
              catalogName, tableName, raColName);
        String decCol = TableMetaDocInterpreter.getColumnIDForName(
              catalogName, tableName, decColName);

        AngleUnits units;
        if ("deg".equals(unitName)) {
            units = AngleUnits.DEGREES;
        }
        else if ("rad".equals(unitName)) {
            units = AngleUnits.RADIANS;
        }
        else {
            // Shouldn't happen - ought to have been checked before.
            throw new ServletException("Unknown units \"" + unitName + "\"");
        }

        // Prepare a mapping from ResultSet column names to column metadata
        // objects obtained from the metadoc - actual names may be different
        // and there may be more metadata.  Use the type SearchField
        // in preference to ColumnInfo here to avoid confusion between
        // org.astrogrid.tableserver.metadata.ColumnInfo and
        // uk.ac.starlink.table.ColumnInfo.
        SearchField[] dbcols = TableMetaDocInterpreter
                              .getColumnsInfoByName(catalogName, tableName);
        final Map colMap = new HashMap();
        for (int i = 0; i < dbcols.length; i++) {
            SearchField dbcol = dbcols[i];
            String key = dbcol.getId();
            DefaultValueInfo starInfo = new DefaultValueInfo(dbcol.getName());
            starInfo.setUnitString(dbcol.getUnits().toString());
            starInfo.setDescription(dbcol.getDescription());
            String ucd = dbcol.getUcd("1");
            if (ucd == null || ucd.trim().length() == 0) {
                ucd = dbcol.getUcd("1+");
            }
            starInfo.setUCD(ucd);
            colMap.put(key, starInfo);
        }

        String tileCol = null;
        SkyTiling tiling = null;
        String cols = "*";
        String where = null;
        boolean prepareSql = false;
        return new JdbcConeSearcher(connection, fullTableName, raCol, decCol,
                                    units, tileCol, tiling, cols, where,
                                    bestOnly, prepareSql, true) {
            public StarTable performSearch(double ra, double dec, double sr)
                    throws IOException {

                // The returned table is the same as that generated by 
                // JdbcConeSearcher, which makes a table directly from the
                // SQL ResultSet, but with column metadata modified according
                // to the metadoc interrogation we performed earlier.
                return new WrapperStarTable(super.performSearch(ra, dec, sr)) {
                    public ColumnInfo getColumnInfo(int icol) {
                        return doctorColumnInfo(super.getColumnInfo(icol));
                    }
                };
            }

            public void close() {
                super.close();
                token.release();
            }

            /**
             * Change column metadata from headings which contain only the
             * names in the ResultSet (directly from the RDBMS) to 
             * ColumnInfo objects which may contain modified/additional 
             * metadata got from the DSA metadoc.
             *
             * @param  base column metadata
             * @return  modified column metadata using DSA metadoc
             */
            private ColumnInfo doctorColumnInfo(ColumnInfo base) {
                ValueInfo starInfo = (ValueInfo) colMap.get(base.getName());
                if (starInfo == null) {
                    return base;
                }
                else if (! isBlank(base.getDescription()) ||
                         ! isBlank(base.getUCD())) {
                    return base;
                }
                else {
                    ColumnInfo info = new ColumnInfo(base);
                    info.setName(starInfo.getName());
                    info.setDescription(starInfo.getDescription());
                    info.setUnitString(starInfo.getUnitString());
                    info.setUCD(starInfo.getUCD());
                    return info;
                }
            }

            /**
             * Checks if a string is empty.
             *
             * @param   txt  string to test
             * @return   true iff txt is either null or whitespace only
             */
            private boolean isBlank(String txt) {
                return txt == null || txt.trim().length() == 0;
            }
        };
    }

    /**
     * Returns the catalogue.table qualified table name in a form suitable
     * for use in SQL queries over a given connection.
     *
     * KEA Note:  When the DSA is configured in single-database mode,
     * the catalog (database) prefix is not required and if present
     * may break some JDBC drivers.  
     *
     * @param  connection   database connection
     * @param  catalogName  catalogue name
     * @param  tableName    table name
     * @return   qualified table name
     */
    private static String getQualifiedTableName(Connection connection,
                                                String catalogName,
                                                String tableName)
            throws SQLException {
        if (catalogName == null || catalogName.trim().length() == 0) {
            return tableName;
        }
        else {
           try {
               String plugin = ConfigFactory.getCommonConfig().getString(
                  "datacenter.querier.plugin");
               if (plugin.equals(
                      "org.astrogrid.tableserver.test.SampleStarsPlugin")) {
                  // No catalog names in HSQLDB - only one DB allowed
                  return tableName;
               }
            }
            catch (Exception e) {
               // Don't worry if this property isn't found, go on and
               // try to get separator from the connection metadata 
            }
            // Check to see if we are in single- or multiple- database 
            // mode
            String hideCat = "true";   //By default
            try {
               String[] catIDs = TableMetaDocInterpreter.getCatalogIDs();
               if (catIDs.length > 1) {
                  hideCat = "false";
                  try {
                     // Assume we don't want to hide prefix in multi-DB
                     // case, unless explicitly told to in the config
                     hideCat = ConfigFactory.getCommonConfig().getString(
                        "datacenter.plugin.jdbc.hidecatalog","false");
                  }
                  catch (Exception e) {
                    // Ignore if property not found
                  }
               }
            }
            catch (Exception e) {
               // Shouldn't really get here in working DSA installation
               // Just assume single-DB mode if in doubt, so do nothing
            }
            if ("true".equals(hideCat) || "TRUE".equals(hideCat)) {
               //Single-database mode - don't need a database prefix
               return tableName;
            }
            // Otherwise return fully-qualified name
            return catalogName
               + connection.getMetaData().getCatalogSeparator()
               + tableName;
        }
    }
}
