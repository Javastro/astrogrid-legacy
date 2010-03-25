package org.astrogrid.dataservice.service.multicone;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.cfg.ConfigReader;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.service.ServletHelper;
import org.astrogrid.dataservice.service.Queues;
import org.astrogrid.dataservice.service.TokenQueue;
import uk.ac.starlink.table.RowSequence;
import uk.ac.starlink.table.SelectorStarTable;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableWriter;
import uk.ac.starlink.table.ValueInfo;
import uk.ac.starlink.ttools.cone.ConeSearcher;
import uk.ac.starlink.ttools.func.Coords;
import uk.ac.starlink.util.XmlWriter;
import uk.ac.starlink.votable.DataFormat;
import uk.ac.starlink.votable.VOTableWriter;

/**
 * A servlet for processing Cone Search queries.
 * This is implemented to work using a direct connection to the database,
 * rather than going through the ADQL translation machinery.
 * It can be considerably faster than the alternative approach.
 *
 * @author   Mark Taylor
 * @since    4 Dec 2009
 */
public class DirectConeServlet extends SecuredServlet {

  private static Log log = LogFactory.getLog(DirectConeServlet.class);

  private static final double MAX_RADIUS = getMaxRadius();

    /**
     * Determines action if limit of simultaneous queries has been reached.
     * If true, queries when busy will block until load has reduced.
     * If false, queries when busy will be rejected with an error.
     */
    public static boolean WAIT_WHEN_BUSY = true;

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {

        try {
          checkAuthorization(request);
        }
        catch (Exception e) {
          response.sendError(response.SC_FORBIDDEN, e.getMessage());
          return;
        }

        // Prepare the output stream, which will contain a VOTable in any case.
        // The cone search standard specifies a MIME type of "text/xml"
        // rather than the more sensible "text/x-votable+xml".
        OutputStream out = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("text/xml");
        response.setCharacterEncoding("UTF-8");

        // Attempt the cone search based on the received request.
        boolean success;
        ConeResult result;
        Throwable error;
        try {
            result = coneSearch(request);
            error = null;
            success = true;
        }
        catch (Exception e) {
            result = null;
            error = e;
            success = false;
        }

        // Write either the result or an error document.
        if (success) {
            assert result != null;
            try {
                result.writeTable(new VOTableWriter(DataFormat.TABLEDATA, true),
                                  out);
            }
            finally {
                result.close();
            }
        }
        else {
            assert error != null;
            writeErrorTable(out, error);
        }

        // Tidy up.
        out.flush();
        out.close();
    }

    /**
     * Attempts a cone search based on a given request, and returns an
     * object which can output the data table in case of success.
     *
     * @param  request  servlet request
     * @return   cone result; the caller MUST close this to avoid resource
     *           leakage
     */
    private ConeResult coneSearch(HttpServletRequest request)
            throws ServletException, IOException {

        // Check service is not disabled.
        ConfigReader config = ConfigFactory.getCommonConfig();
        if (!config.getBoolean("datacenter.implements.conesearch", true)) {
            throw new ServletException("Cone search is disabled " +
                                       "in the config file");
        }

        // Extract the query parameters.
        String catalogName = ServletHelper.getCatalogName(request);
        String tableName = ServletHelper.getTableName(request);
        final double ra = ServletHelper.getRa(request);
        final double dec = ServletHelper.getDec(request);
        final double radius = ServletHelper.getRadius(request);
        if (radius > MAX_RADIUS) {
            throw new ServletException("SR value " + radius + " greater than "
                                      +"allowed maximum " + MAX_RADIUS);
        }

        // Do the SQL search, using the throttling mechanism.
        TokenQueue.Token token = acquireToken();
        final ConeSearcher searcher = DirectConeSearcher
            .createConeSearcher(token, catalogName, tableName, false);
        StarTable sqlResultTable = searcher.performSearch(ra, dec, radius);

        // The result of the SQL search may contain rows outside the 
        // requested cone (probably shouldn't do - may be fixed in later
        // versions of STILTS).  In case it does, filter here to only those
        // within the cone.
        final StarTable coneTable;
        final int ira = searcher.getRaIndex( sqlResultTable );
        final int idec = searcher.getDecIndex( sqlResultTable );
        if ( isDegrees( sqlResultTable.getColumnInfo( ira ) ) &&
             isDegrees( sqlResultTable.getColumnInfo( idec ) ) ) {
            coneTable = new SelectorStarTable( sqlResultTable ) {
                public boolean isIncluded(RowSequence rseq) throws IOException {
                    Object raCell = rseq.getCell( ira );
                    Object decCell = rseq.getCell( idec );
                    double rowRa = raCell instanceof Number
                                 ? ((Number) raCell).doubleValue()
                                 : Double.NaN;
                    double rowDec = decCell instanceof Number
                                  ? ((Number) decCell).doubleValue()
                                  : Double.NaN;
                    double dist =
                        Coords.skyDistanceDegrees( ra, dec, rowRa, rowDec );
                    return ! ( dist > radius );
                }
            };
        }
        else {
            // shouldn't happen: JdbcConeSearcher should always give RA and Dec
            // column indices with units of degrees.  But, if it does, all
            // that will happen is that the service will return some results
            // outside of the given radius; this is permitted by the standard.
            coneTable = sqlResultTable;
        }

        // Finally make some adjustments to the types of columns etc to
        // render it suitable for output from a cone search, and write the
        // result.
        final StarTable outTable = new ConeOutputTable( coneTable );
        return new ConeResult() {
            void writeTable(StarTableWriter outHandler, OutputStream out)
                    throws IOException {
                outHandler.writeStarTable(outTable, out);
            }
            void close() {
                searcher.close();
            }
        };
    }

    /**
     * Returns a token to use for throttling.
     *
     * @return   non-null token
     * @throws   ServletException  if not token can be acquired
     */
    private TokenQueue.Token acquireToken() throws ServletException {
        TokenQueue queue = Queues.getSyncConnectionQueue();
        if (WAIT_WHEN_BUSY) {
            try {
                return queue.waitForToken();
            }
            catch (InterruptedException e) {
                throw (ServletException)
                      new ServletException("Queue wait interrupted")
                     .initCause(e);
            }
        }
        else {
            return Queues.getSyncToken(queue);
        }
    }

    /**
     * Outputs a VOTable document representing an error, in the format
     * required by the Cone Search standard.
     *
     * @param  out  destination stream
     * @param  error   error to represent
     */
    private void writeErrorTable(OutputStream out, Throwable error)
            throws IOException {
        PrintStream pout = new PrintStream(out);
        pout.println("<?xml version='1.0'?>");
        pout.println("<VOTABLE version='1.1'"
                    +" xmlns='http://www.ivoa.net/xml/VOTable/v1.1'>");
        pout.println("<INFO"
                    + XmlWriter.formatAttribute("name", "Error")
                    + XmlWriter.formatAttribute("value", error.toString())
                    + ">");
        pout.println(XmlWriter.formatText(getStackTrace(error)));
        pout.println("</INFO>");
        pout.println("</VOTABLE>");
        pout.flush();
    }

    /**
     * Returns the stack trace of a throwable as a String.
     *
     * @param  error   throwable
     * @return   string containing stack trace
     */
    private static String getStackTrace(Throwable error) {
        try {
            ByteArrayOutputStream traceOut = new ByteArrayOutputStream();
            PrintStream pTraceOut = new PrintStream(traceOut);
            error.printStackTrace(pTraceOut);
            pTraceOut.close();
            return new String(traceOut.toByteArray(), "UTF-8");
        }
        catch (IOException e) {
            return "";
        }
    }

    /**
     * Determines whether a given column has units of degrees.
     *
     * @param   info  column metadata
     * @return   true if the column is known to have units of degrees
     */
    private static boolean isDegrees( ValueInfo info ) {
        String units = info.getUnitString();
        return units != null && units.trim().toLowerCase().startsWith( "deg" );
    }

    /**
     * Interface for an object that can write the result of a cone search
     * as a table.
     */
    private static abstract class ConeResult {

        /**
         * Outputs the cone search result.
         *
         * @param   outHandler  table output handler
         * @param   out  destination stream
         */
        abstract void writeTable(StarTableWriter outHandler, OutputStream out)
                throws IOException;

        /**
         * Tidies up.  MUST called to avoid resource leakage, even in
         * case of write failure.
         */
        abstract void close();
    }

    /**
     * Returns the maximum radius permitted for a cone search.
     * This value is obtained from the DSA properties (via ConfigFactory).
     * The return value is guaranteed in the range (0,180].
     *
     * @return   maximum permitted cone search radius, in the range 0<max<=180
     */
    private static double getMaxRadius() {
        final String LIMIT_KEY = "conesearch.radius.limit";
        final double MAX_LIMIT = 180.0;
        String maxRadStr = SimpleConfig.getProperty(LIMIT_KEY);
        if (maxRadStr == null || maxRadStr.trim().length() == 0) {
            return MAX_LIMIT;
        }
        else {
            double maxRad;
            try {
                maxRad = Double.parseDouble(maxRadStr);
                if (maxRad == 0) {
                    return MAX_LIMIT;
                }
                else if (maxRad > 0 && maxRad <= 180) {
                    return maxRad;
                }
                else {
                    log.warn("Invalid value \"" + maxRad + "\" for " +
                             LIMIT_KEY);
                    return MAX_LIMIT;
                }
            }
            catch (NumberFormatException e) {
                log.warn("Non-numeric value \"" + maxRadStr + "\" for " +
                         LIMIT_KEY);
                return 180.0;
            }
        }
    }

}
