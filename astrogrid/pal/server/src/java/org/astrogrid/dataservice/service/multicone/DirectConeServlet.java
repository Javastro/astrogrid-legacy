package org.astrogrid.dataservice.service.multicone;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.cfg.ConfigReader;
import org.astrogrid.dataservice.service.ServletHelper;
import org.astrogrid.dataservice.service.Queues;
import org.astrogrid.dataservice.service.TokenQueue;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableWriter;
import uk.ac.starlink.ttools.cone.ConeSearcher;
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
public class DirectConeServlet extends HttpServlet {

    private static final double MAX_RADIUS = MulticoneServlet.getMaxRadius();

    /**
     * Determines action if limit of simultaneous queries has been reached.
     * If true, queries when busy will block until load has reduced.
     * If false, queries when busy will be rejected with an error.
     */
    public static boolean WAIT_WHEN_BUSY = true;

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {

        // Prepare the output stream, which will contain a VOTable in any case.
        // The cone search standard specifies a MIME type of "text/xml"
        // rather than the more sensible "text/x-votable+xml".
        OutputStream out = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("text/xml");

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
        double ra = ServletHelper.getRa(request);
        double dec = ServletHelper.getDec(request);
        double radius = ServletHelper.getRadius(request);
        if (radius > MAX_RADIUS) {
            throw new ServletException("SR value " + radius + " greater than "
                                      +"allowed maximum " + MAX_RADIUS);
        }

        // Do the search, using the throttling mechanism.
        TokenQueue.Token token = acquireToken();
        final ConeSearcher searcher = DirectConeSearcher
            .createConeSearcher(token, catalogName, tableName, false);
        final StarTable outTable =
            new ConeOutputTable( searcher.performSearch(ra, dec, radius) );
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
}
