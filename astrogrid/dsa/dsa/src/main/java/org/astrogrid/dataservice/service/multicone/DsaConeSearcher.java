package org.astrogrid.dataservice.service.multicone;

import java.io.IOException;
import java.security.Principal;
import javax.servlet.ServletException;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryException;
import org.astrogrid.query.returns.ReturnSpec; 
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.slinger.targets.TableSinkTarget;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.dataservice.metadata.MetadataException;
import uk.ac.starlink.table.OnceRowPipe;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.ttools.cone.ConeSearcher;

/**
 * ConeSearcher implementation which uses DSA classes to do the cone search
 * step.
 *
 * @author   Mark Taylor
 * @since    4 Sep 2007
 */
public class DsaConeSearcher implements ConeSearcher {

    private final String catalogName;
    private final String tableName;
    private final Principal user;
    private final String source;
    private final String raColName;
    private final String decColName;
    private final String units;

    /**
     * Constructor.
     *
     * @param  catalogName  The name of the catalog (database) to be searched
     * @param  tableName  The name of the table in the catalog to be searched
     * @param  user  The identity of the user running the query
     *               (if known - may be null)
     * @param  source  The source of the query (if known - may be null)
     * KONA TOFIX WHAT SHOULD USER AND SOURCE BE IF NULL??
     */
    public DsaConeSearcher(String catalogName, String tableName, Principal user,
                           String source) throws ServletException {
        this.catalogName = catalogName;
        this.tableName = tableName;
        this.user = user;
        this.source = source;
        try {
            this.raColName = TableMetaDocInterpreter
                    .getConeRAColumnByName(catalogName, tableName);
            this.decColName = TableMetaDocInterpreter
                    .getConeDecColumnByName(catalogName, tableName);
            this.units = TableMetaDocInterpreter
                    .getConeUnitsByName(catalogName, tableName);
        }
        catch (Exception e) {
            throw new ServletException(e.getMessage(), e);
        }
    }

    public int getRaIndex(StarTable result) {
        if (raColName != null) {
            int ncol = result.getColumnCount();
            for (int icol = 0; icol < ncol; icol++) {
                if (raColName.equalsIgnoreCase(result.getColumnInfo(icol)
                                                     .getName())) {
                    return icol;
                }
            }
        }
        return -1;
    }

    public int getDecIndex(StarTable result) {
        if (decColName != null) {
            int ncol = result.getColumnCount();
            for (int icol = 0; icol < ncol; icol++) {
                if (decColName.equalsIgnoreCase(result.getColumnInfo(icol)
                                                      .getName())) {
                    return icol;
                }
            }
        }
        return -1;
    }

    public StarTable performSearch(double ra, double dec, double sr)
            throws IOException {

        // Define a target which will be able to provide a streamed StarTable.
        OnceRowPipe rowStore = new OnceRowPipe();
        TargetIdentifier target = new TableSinkTarget(rowStore);
        ReturnSpec retspec = new ReturnTable(target, ReturnTable.VOTABLE);

        // Invoke the query which will send the data to the target.
        Query query;
        try {
            query = new Query(catalogName, tableName, units,
                              raColName, decColName, ra, dec, sr, retspec);
        }
        catch (QueryException e) {
            throw (IOException) new IOException(e.getMessage())
                 .initCause(e);
        }
        DataServer server = new DataServer();
        try {
            server.askQuery(this.user, query, this.source);
        }
        catch (Throwable e) {
            throw (IOException) new IOException(e.getMessage())
                 .initCause(e);
        }

        // Return the query result as a streamed StarTable object.
        return rowStore.waitForStarTable();
    }

    public void close() {
    }
}
