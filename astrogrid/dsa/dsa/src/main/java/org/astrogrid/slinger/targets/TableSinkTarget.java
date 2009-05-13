package org.astrogrid.slinger.targets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.astrogrid.slinger.targets.TargetIdentifier;
import uk.ac.starlink.table.TableBuilder;
import uk.ac.starlink.table.TableSink;
import uk.ac.starlink.util.PipeReaderThread;
import uk.ac.starlink.votable.VOTableBuilder;

/**
 * TargetIdentifier implementation which accepts a VOTable and streams it
 * to a given {@link uk.ac.starlink.table.TableSink} object.
 *
 * @author   Mark Taylor
 * @since    4 Sep 2007
 */
public class TableSinkTarget implements TargetIdentifier {

    private final TableSink sink;
    private TableBuilder tableBuilder;

    /**
     * Constructor.
     *
     * @param   sink  destination for the streamed table
     */
    public TableSinkTarget(TableSink sink) {
        this.sink = sink;
    }

    public OutputStream openOutputStream() throws IOException {
        final TableBuilder tbuild = this.tableBuilder;
        if (tbuild == null) {
            throw new IllegalStateException("MIME type not set");
        }

        // Start up a thread which will wait for the bytes written down 
        // the output stream provided by this object and stream them into
        // this object's TableSink as a table.
        PipeReaderThread reader = new PipeReaderThread() {
            protected void doReading(InputStream dataIn) throws IOException {
                tbuild.streamStarTable(dataIn, sink, "");
            }
        };
        reader.start();

        // Return the output stream which feeds bytes to the thread that we
        // have set up.
        return reader.getOutputStream();
    }

    public Writer openWriter() throws IOException {
        // is this correct??
        return new OutputStreamWriter(openOutputStream());
    }

    /**
     * Currently only VOTable is permitted.
     */
    public void setMimeType(String mimeType) throws IOException {
        if (mimeType.indexOf("x-votable+xml") >= 0) {
            this.tableBuilder = new VOTableBuilder();
        }
        else {
            // some others could be supported here (other streamable STIL
            // formats) but none are currently expected.
            throw new IOException("Unsupported MIME type " + mimeType);
        }
    }
}
