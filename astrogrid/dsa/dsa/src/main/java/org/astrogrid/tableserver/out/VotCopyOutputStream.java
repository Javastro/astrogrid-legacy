// TOFIX USE STILTS CLASS WHEN AVAILABLE, RATHER THAN HAVING LOCAL COPY
// package uk.ac.starlink.ttools.copy;
package org.astrogrid.tableserver.out;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import uk.ac.starlink.ttools.task.VotCopy;
import uk.ac.starlink.ttools.copy.VotCopyHandler;
import uk.ac.starlink.util.PipeReaderThread;

/**
 * Filter output stream which turns a stream containing a VOTable into
 * one containing the same VOTable content but using a given (presumably
 * different) encoding.
 *
 * @author   Mark Taylor
 * @since    8 Feb 2007
 */
public class VotCopyOutputStream extends OutputStream {

    private final OutputStream baseOut_;
    private final OutputStream filterOut_;
    private final VotCopyHandler handler_;
    private boolean finished_;

    /**
     * Constructor.
     *
     * @param  out  output stream to which the re-encoded VOTable will
     *              be written
     * @param  handler  object which controls how the re-encoding is to be done
     */
    VotCopyOutputStream( OutputStream out, VotCopyHandler handler )
            throws IOException {
        baseOut_ = out;
        handler_ = handler;
        PipeReaderThread readerThread = new PipeReaderThread() {
            protected void doReading( InputStream dataIn ) throws IOException {
                processInput( dataIn );
            }
        };
        filterOut_ = readerThread.getOutputStream();
        handler_.setOutput( new OutputStreamWriter( baseOut_ ) );
        readerThread.start();
    }

    public void close() throws IOException {
        filterOut_.close();
        synchronized ( this ) {
            while ( ! finished_ ) {
                try {
                    wait();
                }
                catch ( InterruptedException e ) {
                    throw (IOException) new IOException( "Interrupted" )
                                       .initCause( e );
                }
            }
        }
        baseOut_.close();
    }

    public void flush() throws IOException {
        filterOut_.flush();
    }

    public void write( byte[] b ) throws IOException {
        filterOut_.write( b );
    }

    public void write( byte[] b, int off, int len ) throws IOException {
        filterOut_.write( b, off, len );
    }

    public void write( int b ) throws IOException {
        filterOut_.write( b );
    }

    private void processInput( InputStream in ) throws IOException {
        InputSource saxsrc = new InputSource( in );
        try {
            VotCopy.saxCopy( saxsrc, handler_ );
            filterOut_.flush();
        }
        catch ( SAXException e ) {
            finished_ = true;
            notifyAll();
            throw (IOException) new IOException( "SAX processing error: " 
                                               + e.getMessage() )
                               .initCause( e );
        }
        finally {
            finished_ = true;
            synchronized ( this ) {
                notifyAll();
            }
        }
    }

    /**
     * Filter command - takes a VOTable on standard input and writes it out
     * using a BINARY encoding on standard output.
     */
    public static void main( String[] args ) throws IOException {
        if ( args.length > 0 ) {
            throw new IllegalArgumentException( "no args" );
        }
        VotCopyHandler handler =
            new VotCopyHandler( true, uk.ac.starlink.votable.DataFormat.BINARY,
                                true, null, false, null );
        OutputStream out =
            new VotCopyOutputStream( new BufferedOutputStream( System.out ),
                                     handler );
        out = new java.io.BufferedOutputStream( out );
        InputStream in = new BufferedInputStream( System.in );
        byte[] buf = new byte[ 4069 ];
        for ( int n; ( n = in.read( buf ) ) >= 0; ) {
            out.write( buf, 0, n );
        }
        in.close();
        out.close();
    }
}
