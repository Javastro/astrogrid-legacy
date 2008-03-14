package org.astrogrid.dataservice.service.multicone;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.cfg.ConfigReader;
import org.astrogrid.cfg.PropertyNotFoundException;
import org.astrogrid.dataservice.service.Queues;
import org.astrogrid.dataservice.service.TokenQueue;
import org.astrogrid.dataservice.service.ServletHelper;
import uk.ac.starlink.table.JoinFixAction;
import uk.ac.starlink.table.OnceRowPipe;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableFactory;
import uk.ac.starlink.table.StarTableOutput;
import uk.ac.starlink.table.StarTableWriter;
import uk.ac.starlink.table.TableBuilder;
import uk.ac.starlink.table.TableFormatException;
import uk.ac.starlink.task.TaskException;
import uk.ac.starlink.ttools.cone.ConeMatcher;
import uk.ac.starlink.ttools.cone.ConeSearcher;
import uk.ac.starlink.ttools.task.TableProducer;
import uk.ac.starlink.votable.DataFormat;
import uk.ac.starlink.votable.VOTableBuilder;
import uk.ac.starlink.votable.VOTableWriter;

/**
 * Servlet which performs a multiple-cone-search type crossmatch between
 * an input VOTable and a table held on the server.
 *
 * <p>This servlet can be used in two modes.
 * <ol>
 * <li>Input table is the body of a POST request and other parameters are
 *     supplied as the query part of the URL in normal
 *     <code>application/x-www-form-urlencoded</code> form.
 *     This has the advantage that the input table can be streamed.
 *     </li>
 * <li>All parameters including the table are supplied within the body of
 *     a POST request in <code>multipart/form-data</code> form.
 *     This is so-called RFC1867 mode, and suitable for use from an
 *     HTML form.
 *     <li>
 * </ol>
 * Possibly only one of these modes should be permitted, but I'm not sure
 * which at time of writing (Sep 2007), so for now I'm sitting on the fence.
 * Future decisions about other DAL multiple input queries may throw more
 * light on this.
 *
 * <p>See public static final members for parameter names and semantics.
 *
 * @author   Mark Taylor
 * @since    4 Sep 2007
 */
public class MulticoneServlet extends HttpServlet {

    /**
     * Request parameter name for column giving Right Ascension in degrees
     * (if absent, looks for UCD).
     */
    public static final String RA_COL_PARAM = "RA";

    /**
     * Request parameter name for column giving Declination in degrees.
     * (if absent, looks for UCD).
     */
    public static final String DEC_COL_PARAM = "DEC";

    /**
     * Request parameter name for search radius value in degrees.
     */
    public static final String SR_PARAM = "SR";

    /**
     * Request paramater name for flag indicating match type
     * (if absent, defaults to BEST).
     */
    public static final String FIND_MODE_PARAM = "FIND";

    /**
     * Request parameter name identifying the text of the input VOTable.
     * Note this is only used in <code>multipart/form-data</code> mode,
     * not if the table is POSTed directly.
     */
    public static final String INPUT_TABLE_PARAM = "TABLE";

    /**
     * Request parameter name identifying the requested format of the results.
     * In the form of a STIL output format specifier.
     * If absent, defaults to VOTable.
     */
    public static final String FORMAT_PARAM = "Format";

    /**
     * Request parameter for determining multicone implementation.
     * May be "ADQL" or "DIRECT".  Defaults to "DIRECT".
     */
    public static final String IMPL_PARAM = "IMPL";

    /**
     * Handler for interpreting input tables.  Currently only VOTable input
     * is supported, though it would be possible to support different
     * streamable formats by using the declared MIME type of the request.
     */
    private final TableBuilder inputHandler = new VOTableBuilder();

    private final StarTableOutput sto = new StarTableOutput();

    private static final double MAX_RADIUS = getMaxRadius();

    private static final Log log = LogFactory.getLog(MulticoneServlet.class);

    /**
     * Called for an HTTP POST.  The input VOTable is the body of the request.
     */
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, IOException {

        // Check service is not disabled.
        ConfigReader config = ConfigFactory.getCommonConfig();
        if (!config.getBoolean("datacenter.implements.conesearch", true)) {
            throw new ServletException("Cone search is disabled " +
                                       "in the config file");
        }
        if (!config.getBoolean("datacenter.implements.multicone", true)) {
            throw new ServletException("Multiple cone search is disabled " +
                                       "in the config file");
        }

        // Get the parameters object from the request.
        final RequestParams params =
            request.getContentType().startsWith("multipart/form-data")
                ? getMultipartParams(request)
                : getPostTableParams(request);

        // Extract parameter values.
        HttpServletRequest eReq = new ExtrasRequest(request, params);
        String catalogName = ServletHelper.getCatalogName(eReq);
        String tableName = ServletHelper.getTableName(eReq);
        Principal user = ServletHelper.getUser(request);
        String source = request.getRemoteHost() + " (" + request.getRemoteAddr()
                      + ") via MultiCone servlet";
        TableProducer inProd = params.getTableProducer();
        String raCol = params.getParameter(RA_COL_PARAM);
        String decCol = params.getParameter(DEC_COL_PARAM);
        String srString = params.getParameter(SR_PARAM);
        if (srString == null || srString.trim().length() == 0) {
            throw new ServletException("No " + SR_PARAM + " parameter given");
        }
        double sr;
        try {
            sr = Double.parseDouble(srString.trim());
        }
        catch (NumberFormatException e) {
            throw new ServletException(SR_PARAM + " value \"" + srString +
                                       "\" is not a number");
        }
        if (sr > MAX_RADIUS) {
            throw new ServletException(SR_PARAM + " value " + sr +
                                       " greater than allowed maximum " + 
                                       MAX_RADIUS);
        }
        String findMode = params.getParameter(FIND_MODE_PARAM);
        boolean bestOnly;
        if (findMode == null) {
            bestOnly = true;
        }
        else if ("BEST".equalsIgnoreCase(findMode)) {
            bestOnly = true;
        }
        else if ("ALL".equalsIgnoreCase(findMode)) {
            bestOnly = false;
        }
        else {
            throw new ServletException("Unknown value of parameter " +
                                       FIND_MODE_PARAM +
                                       " (should be BEST or ALL)");
        }
        boolean direct = true;
        String impl = params.getParameter(IMPL_PARAM);
        if (impl == null) {
           // If impl is unset, check system config
           try {
              String forceadql = "false";
              forceadql =  ConfigFactory.getCommonConfig().getString(
                 "datacenter.multicone.forceadql","false");
              if ("true".equalsIgnoreCase(forceadql)) {
                 direct = false;
              }
           }
           catch (PropertyNotFoundException pnfe) {
              // Just ignore if property is unset
              direct = true;
           }
        }
        else if (impl.equalsIgnoreCase("DIRECT")) {
            direct = true;
        }
        else if (impl.equalsIgnoreCase("ADQL")) {
            direct = false;
        }
        else {
             throw new ServletException("Unknown value of parameter " +
                                        IMPL_PARAM +
                                        " (should be ADQL or DIRECT)");
        }

        // Construct a cone searcher object.
        ConeSearcher searcher;
        if (direct) {
            TokenQueue.Token token =
                Queues.getSyncToken(Queues.getSyncConnectionQueue());
            searcher = DirectConeSearcher
                .createConeSearcher(token, catalogName, tableName, bestOnly);
        }
        else {
            searcher = new DsaConeSearcher(catalogName, tableName, user,
                                           source);
        }

        // Prepare the object which will do the work.
        ConeMatcher matcher =
            new ConeMatcher(searcher, inProd,
                            new DsaQuerySequenceFactory(raCol, decCol, sr),
                            bestOnly);

        // It's OK to stream on output since we will be outputting a VOTable,
        // which can be done in a single pass.
        matcher.setStreamOutput(true);

        // Obtains the output table object.
        StarTable outTable;
        try {
            outTable = matcher.getTable();
        }
        catch (TaskException e) {
            throw new ServletException(e.getMessage(), e);
        }

        // Following the practice of the SubmitCone servlet we modify the
        // output according to any supplied Format parameter.
        // Note not all of these will work - formats which require two passes
        // (notably FITS, most of the time) will end up throwing an error.
        final StarTableWriter outputHandler;
        String ofmt = params.getParameter(FORMAT_PARAM);
        if (ofmt == null || ofmt.trim().length() == 0) {
            outputHandler = new VOTableWriter(DataFormat.TABLEDATA, true);
        }
        else {
            try {
                outputHandler = sto.getHandler(ofmt);
            }
            catch (TableFormatException e) {
                throw new ServletException(e.getMessage(), e);
            }
        }

        // Serializes the output table to the servlet response object.
        response.setContentType(outputHandler.getMimeType());
        OutputStream ostrm = response.getOutputStream();
        outputHandler.writeStarTable(outTable, ostrm);
        ostrm.flush();
        ostrm.close();
    }

    /**
     * Decodes parameter values from a request in which the input table
     * is the content of the POST stream and other parameters are supplied
     * as part of the URL in <code>application/x-www-form-urlencoded</code>.
     * 
     * @param  request  HTTP request
     * @return  multicone parameters
     */
    private RequestParams getPostTableParams(HttpServletRequest request)
            throws ServletException, IOException {

        // Get the stream consituting the POSTed part of the request.
        // Note this must be done prior to any getParameter calls on request,
        // since otherwise the body seems to get treated as a 
        // multipart/form-data parameter list.
        final InputStream tis = request.getInputStream();
        if (tis == null) {
            throw new ServletException("No POSTed table to operate on");
        }

        // The input table producer can provide the input table for scanning
        // a row at a time.  Use of the OnceRowPipe here is only possible
        // because VOTable is a format that can be streamed.  Something like
        // a CSV table which needs to be interpreted in two passes would need
        // the whole table to be cached in a normal 
        // uk.ac.starlink.table.RowStore.
        TableProducer inProd = new TableProducer() {
            public StarTable getTable() throws IOException {
                OnceRowPipe rowStore = new OnceRowPipe();
                inputHandler.streamStarTable(tis, rowStore, null);
                return rowStore.waitForStarTable();
            }
        };

        // Construct a map of the other parameter values.  They will have been
        // application/x-www-form-urlencoded as part of the URL.
        Map othersMap = new HashMap();
        for (Enumeration en = request.getParameterNames();
             en.hasMoreElements();) {
            String name = (String) en.nextElement();
            othersMap.put(name, request.getParameter(name));
        }

        // Return as parameters object.
        return new RequestParams(inProd, othersMap);
    }

    /**
     * Decodes parameter values from a Request made using
     * <code>multipart/form-data</code> mode.
     *
     * @param   request  HTTP request
     * @return  multicone parameters
     */
    private RequestParams getMultipartParams(HttpServletRequest request)
            throws ServletException, IOException {
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new ServletException("Request content is not multipart");
        }
        TableProducer tableIn = null;
        Map othersMap = new HashMap();

        // All parameters are multipart/form-data encoded in the POSTed
        // body of the message.
        try {
            for (FileItemIterator it =
                     new ServletFileUpload().getItemIterator(request);
                 it.hasNext();) {
                FileItemStream item = it.next();
                String name = item.getFieldName();
                InputStream istrm = item.openStream();
                if (item.isFormField()) {
                    othersMap.put(name, Streams.asString(istrm));
                }
                else {
                    if (INPUT_TABLE_PARAM.equals(name)) {
                        final StarTable table =
                            new StarTableFactory()
                           .makeStarTable(istrm, inputHandler);
                        tableIn = new TableProducer() {
                            public StarTable getTable() {
                                return table;
                            }
                        };
                    }
                }
            }
        }
        catch (FileUploadException e) {
            throw (IOException)
                  new IOException("Parameter parse exception " + e)
                 .initCause(e);
        }
        if (tableIn == null) {
            throw new ServletException("No " + INPUT_TABLE_PARAM +
                                       " parameter supplied");
        }
        return new RequestParams(tableIn, othersMap);
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
        String maxRadStr =
            ConfigFactory.getCommonConfig().getString(LIMIT_KEY);
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

    /**
     * Encapsulates the set of parameters required by a multicone operation.
     */
    private static class RequestParams {
        private final TableProducer tableIn;
        private final Map otherParams;

        /**
         * Constructor.
         *
         * @param  tableIn  source for input VOTable
         * @param  otherParams  String->String map for other parameter values
         */
        public RequestParams(TableProducer tableIn, Map otherParams)
                throws ServletException {
            if (tableIn == null) {
                throw new ServletException("No input table provided");
            }
            this.tableIn = tableIn;
            this.otherParams = otherParams;
        }

        /**
         * Returns an object which can provide the input table for this 
         * multicone request.
         *
         * @return  input table producer
         */
        public TableProducer getTableProducer() {
            return tableIn;
        }

        /**
         * Returns the string value of one of the non-table parameters.
         *
         * @param  name  parameter name
         * @return  parameter value, or null if not present
         */
        public String getParameter(String name) {
            return (String) otherParams.get(name);
        }
    }

    /**
     * Decorator for an HttpServletRequest which augments the parameters
     * which can be accessed using 
     * {@link javax.servlet.ServletRequest#getParameter}.
     * This is used to fake a servlet request with parameters which are
     * not normally available using getParameter (becuase they have been
     * decoded separately from a multipart/form-data body).
     *
     * <p>Note that this does not override getParameterNames() etc, so it's
     * not really a consistent ServletRequest object.  Use with care.
     */
    private static class ExtrasRequest extends HttpServletRequestWrapper {

        private final RequestParams params;

        /**
         * Constructor.
         *
         * @param  request  base request
         * @param  params  object containing additional request parameters
         */
        public ExtrasRequest(HttpServletRequest request, RequestParams params) {
            super(request);
            this.params = params;
        }

        /**
         * Returns a parameter which is got either from the underlying
         * implementation or from the supplied RequestParams object.
         */
        public String getParameter(String name) {
            String value = super.getParameter(name);
            return value == null ? params.getParameter(name)
                                 : value;
        }
    }
}
