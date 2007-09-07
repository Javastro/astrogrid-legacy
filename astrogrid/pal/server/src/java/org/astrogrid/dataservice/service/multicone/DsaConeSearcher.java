package org.astrogrid.dataservice.service.multicone;

import java.io.IOException;
import java.security.Principal;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.dataservice.service.ServletHelper;
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryException;
import org.astrogrid.query.returns.ReturnSpec; 
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.slinger.targets.TableSinkTarget;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
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
     * @param  request  the servlet request which provides the context within
     *         which the searcher will be used
     */
    public DsaConeSearcher(HttpServletRequest request) throws ServletException {
        try {
            this.catalogName = ServletHelper.getCatalogName(request);
            this.tableName = ServletHelper.getTableName(request);
            this.user = ServletHelper.getUser(request);
            this.source = request.getRemoteHost()
                        + " (" + request.getRemoteAddr()
                        + ") via MultiCone servlet";
        }
        catch (IllegalArgumentException e) {
            throw new ServletException(e.getMessage(), e);
        }

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
}
