package org.astrogrid.dataservice.service.multicone;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.cfg.ConfigReader;
import uk.ac.starlink.table.OnceRowPipe;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableWriter;
import uk.ac.starlink.table.TableBuilder;
import uk.ac.starlink.task.TaskException;
import uk.ac.starlink.ttools.cone.SkyConeMatch2Producer;
import uk.ac.starlink.ttools.task.TableProducer;
import uk.ac.starlink.votable.DataFormat;
import uk.ac.starlink.votable.VOTableBuilder;
import uk.ac.starlink.votable.VOTableWriter;

/**
 * Servlet which performs a multiple-cone-search type crossmatch bewtween
 * a POSTed VOTable and a table held on the server.
 *
 * @author   Mark Taylor
 * @since    4 Sep 2007
 */
public class MulticoneServlet extends HttpServlet {

    /**
     * Request parameter name for column (or expression) giving
     * Right Ascension in degrees.
     */
    public static final String RA_EXPR_PARAM = "RA";

    /**
     * Request parameter name for column (or expression) giving
     * Declination in degrees.
     */
    public static final String DEC_EXPR_PARAM = "DEC";

    /**
     * Request parameter name for column (or expression, for instance constant)
     * giving search radius in degrees. 
     */
    public static final String SR_EXPR_PARAM = "SR";

    /**
     * Request paramater name for flag indicating match type (optional).
     */
    public static final String FIND_MODE_PARAM = "FIND";

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

        // Get the stream containing the input VOTable.  Note this must be
        // done prior to any getParameter calls on request, since otherwise
        // the body seems to get treated like a POSTed form which may contain
        // parameter values.
        final InputStream in = request.getInputStream();

        // Currently only VOTable inputs supported, though it would be 
        // possible to support different streamable formats by using the
        // declared MIME type of the request.
        final TableBuilder inputHandler = new VOTableBuilder();

        // The input table producer can provide the input table for scanning
        // a row at a time.  Use of the OnceRowPipe here is only possible
        // because VOTable is a format that can be streamed.  Something like
        // a CSV table which needs to be interpreted in two passes would need
        // the whole table to be cached in a normal 
        // uk.ac.starlink.table.RowStore.
        TableProducer inProd = new TableProducer() {
            public StarTable getTable() throws IOException {
                OnceRowPipe rowStore = new OnceRowPipe();
                inputHandler.streamStarTable(in, rowStore, null);
                return rowStore.waitForStarTable();
            }
        };

        // Get RA and Dec column names.  If not specified explicitly in the
        // request, columns sporting the most likely UCDs are looked for.
        // Note that these values can be JEL/STILTS algebraic expressions
        // referencing the columns of the table - this complicated feature
        // is probably best not advertised.
        String ra = request.getParameter(RA_EXPR_PARAM);
        if (ra == null) {
            ra = "ucd$pos_eq_ra";
        }
        String dec = request.getParameter(DEC_EXPR_PARAM);
        if (dec == null) {
            dec = "ucd$pos_eq_dec";
        }

        // Get search radius.  This is most likely a constant value, but can
        // be a column name or expression.  There is no default.
        String sr = request.getParameter(SR_EXPR_PARAM);
        if (sr == null) {
            throw new ServletException("No " + SR_EXPR_PARAM
                                     + " parameter provided");
        }

        // Get search type flag.
        boolean bestOnly;
        String mode = request.getParameter(FIND_MODE_PARAM);
        if ("BEST".equalsIgnoreCase(mode)) {
            bestOnly = true;
        }
        else if ("ALL".equalsIgnoreCase(mode) || mode == null) {
            bestOnly = false;
        }
        else {
            throw new ServletException("Unknown value of parameter " +
                                       FIND_MODE_PARAM +
                                       " (should be BEST or ALL)");
        }

        // This prepares to do the work, by turning the input table into 
        // one consisting of its crossmatch with the specified table on the
        // server.
        TableProducer outProd = 
            new SkyConeMatch2Producer(new DsaConeSearcher(request), inProd,
                                      bestOnly, ra, dec, sr, "*");

        // Obtains the output table object.
        StarTable outTable;
        try {
            outTable = outProd.getTable();
        }
        catch (TaskException e) {
            throw new ServletException(e.getMessage(), e);
        }

        // Serializes the output table to the servlet response object.
        StarTableWriter outputHandler =
            new VOTableWriter(DataFormat.TABLEDATA, true);
        response.setContentType(outputHandler.getMimeType());
        OutputStream ostrm = response.getOutputStream();
        outputHandler.writeStarTable(outTable, ostrm);
        ostrm.flush();
        ostrm.close();
    }
}
