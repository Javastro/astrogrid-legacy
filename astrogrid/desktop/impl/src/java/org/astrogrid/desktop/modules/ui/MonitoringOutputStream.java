/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ProxyOutputStream;

/**
 * Output stream which monitors progress and reports it in increments to a worker reporter.
 * implementation based on commons.io CountingOutputStreams
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 27, 20071:17:07 PM
 */
public class MonitoringOutputStream extends ProxyOutputStream {
    public static final long ONE_KB = FileUtils.ONE_KB;
    public static final long ONE_MB = FileUtils.ONE_MB;
    public static final long ONE_GB = FileUtils.ONE_GB;
    
    
    private final long size;
    private final long factor;
    private final int factoredSize;
    private long factoredCount;
    private WorkerProgressReporter reporter;
    public MonitoringOutputStream(WorkerProgressReporter reporter,OutputStream os,long size,long factor) {
        super(os);
        this.reporter = reporter;
        this.size = size;
        this.factor = factor;
        this.factoredSize = (int)(size / factor);
        reporter.setProgress(0,factoredSize);
    }    
    /** access the size of the outputt, formatted in human-readable terms */
    public String getFormattedSize() {
        return FileUtils.byteCountToDisplaySize(size);
    }
    /** get the size of the output, in bytes */
    public long getSize() {
        return size;
    }

    /** The count of bytes that have passed. */
    private long count;


    //-----------------------------------------------------------------------
    /**
     * Writes the contents of the specified byte array to this output stream
     * keeping count of the number of bytes written.
     *
     * @param b  the bytes to write, not null
     * @throws IOException if an I/O error occurs
     * @see java.io.OutputStream#write(byte[])
     */
    public void write(byte[] b) throws IOException {
        count += b.length;
        super.write(b);
        long nu = count / factor;
        if (nu != this.factoredCount) {
            this.factoredCount = nu;
            reporter.setProgress((int)nu,factoredSize);
        }
    }

    /**
     * Writes a portion of the specified byte array to this output stream
     * keeping count of the number of bytes written.
     *
     * @param b  the bytes to write, not null
     * @param off  the start offset in the buffer
     * @param len  the maximum number of bytes to write
     * @throws IOException if an I/O error occurs
     * @see java.io.OutputStream#write(byte[], int, int)
     */
    public void write(byte[] b, int off, int len) throws IOException {
        count += len;
        super.write(b, off, len);
        long nu = count / factor;
        if (nu != this.factoredCount) {
            this.factoredCount = nu;
            reporter.setProgress((int)nu,factoredSize);
        }        
    }

    /**
     * Writes a single byte to the output stream adding to the count of the
     * number of bytes written.
     *
     * @param b  the byte to write
     * @throws IOException if an I/O error occurs
     * @see java.io.OutputStream#write(int)
     */
    public void write(int b) throws IOException {
        count++;
        super.write(b);
        long nu = count / factor;
        if (nu != this.factoredCount) {
            this.factoredCount = nu;
            reporter.setProgress((int)nu,factoredSize);
        }        
    }



    /**
     * The number of bytes that have passed through this stream.
     * <p>
     * NOTE: This method is an alternative for <code>getCount()</code>.
     * It was added because that method returns an integer which will
     * result in incorrect count for files over 2GB.
     *
     * @return the number of bytes accumulated
     * @since Commons IO 1.3
     */
    public synchronized long getByteCount() {
        return this.count;
    }

    /** 
     * Set the byte count back to 0. 
     * <p>
     * NOTE: This method is an alternative for <code>resetCount()</code>.
     * It was added because that method returns an integer which will
     * result in incorrect count for files over 2GB.
     *
     * @return the count previous to resetting
     * @since Commons IO 1.3
     */
    public synchronized long resetByteCount() {
        long tmp = this.count;
        this.count = 0;
        return tmp;
    }

}
