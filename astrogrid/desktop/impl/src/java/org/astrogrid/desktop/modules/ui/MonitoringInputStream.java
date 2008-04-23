/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.ProxyInputStream;
import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileObject;

/** Input stream which monitors progress and reports it in increments to a worker progress reporter.
 * implementation based on commons.io.CountingInputStream
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 27, 200712:45:39 PM
 * @TEST unit test this.
 */
public class MonitoringInputStream  extends ProxyInputStream {

    public static final long ONE_KB = FileUtils.ONE_KB;
    public static final long ONE_MB = FileUtils.ONE_MB;
    public static final long ONE_GB = FileUtils.ONE_GB;
    
    /** open a monitoring input stream
     * 
     * @param reporter to report progress to
     * @param url location to open
     * @param factor number of bytese to report progress at 
     * @return
     * @throws IOException
     */
    public static MonitoringInputStream create(WorkerProgressReporter reporter, URL url,long factor) throws IOException {
        URLConnection conn = url.openConnection();
        int size = conn.getContentLength();
        long lastModified = conn.getLastModified();
        return new MonitoringInputStream(reporter,conn.getInputStream(),lastModified,size,factor);
    }

    public static MonitoringInputStream create(WorkerProgressReporter reporter, FileObject fo,long factor) throws IOException {
       FileContent content = fo.getContent();
        long size = content.getSize();
        long lastModified = content.getLastModifiedTime();
        return new MonitoringInputStream(reporter,content.getInputStream(),lastModified,size,factor);
    }
    
    
    private final WorkerProgressReporter reporter;
    private final long size;
    private final long factor;
    private final int factoredSize;
    private long factoredCount;
    private final long lastModified;
    private MonitoringInputStream(WorkerProgressReporter reporter,InputStream is,long lastModified,long size,long factor) {
        super(is);
        this.reporter = reporter;
        this.lastModified = lastModified;
        this.size = size;
        
        this.factor = factor;
        this.factoredSize = (int)(size / factor);
        reporter.setProgress(0,factoredSize);
    }
    
    /** access the size of the input, formatted in human-readable terms */
    public String getFormattedSize() {
        return FileUtils.byteCountToDisplaySize(size);
    }
    /** get the size of the input, in bytes */
    public long getSize() {
        return size;
    }
    
    /** The count of bytes that have passed. */
    private long count;


    //-----------------------------------------------------------------------
    /**
     * Reads a number of bytes into the byte array, keeping count of the
     * number read.
     *
     * @param b  the buffer into which the data is read, not null
     * @return the total number of bytes read into the buffer, -1 if end of stream
     * @throws IOException if an I/O error occurs
     * @see java.io.InputStream#read(byte[]) 
     */
    public int read(byte[] b) throws IOException {
        int found = super.read(b);
        this.count += (found >= 0) ? found : 0;
        long nu = count/factor;
        if (nu != this.factoredCount) { // trigger a change
            this.factoredCount = nu;
            reporter.setProgress((int)factoredCount,factoredSize);
        }      
        return found;
    }

    /**
     * Reads a number of bytes into the byte array at a specific offset,
     * keeping count of the number read.
     *
     * @param b  the buffer into which the data is read, not null
     * @param off  the start offset in the buffer
     * @param len  the maximum number of bytes to read
     * @return the total number of bytes read into the buffer, -1 if end of stream
     * @throws IOException if an I/O error occurs
     * @see java.io.InputStream#read(byte[], int, int)
     */
    public int read(byte[] b, int off, int len) throws IOException {
        int found = super.read(b, off, len);
        this.count += (found >= 0) ? found : 0;
        long nu = count/factor;
        if (nu != this.factoredCount) { // trigger a change
            this.factoredCount = nu;
            reporter.setProgress((int)factoredCount,factoredSize);
        }         
        return found;
    }

    /**
     * Reads the next byte of data adding to the count of bytes received
     * if a byte is successfully read. 
     *
     * @return the byte read, -1 if end of stream
     * @throws IOException if an I/O error occurs
     * @see java.io.InputStream#read()
     */
    public int read() throws IOException {
        int found = super.read();
        this.count += (found >= 0) ? 1 : 0;
        long nu = count/factor;
        if (nu != this.factoredCount) { // trigger a change
            this.factoredCount = nu;
            reporter.setProgress((int)factoredCount,factoredSize);
        }        
        return found;
    }

    /**
     * Skips the stream over the specified number of bytes, adding the skipped
     * amount to the count.
     *
     * @param length  the number of bytes to skip
     * @return the actual number of bytes skipped
     * @throws IOException if an I/O error occurs
     * @see java.io.InputStream#skip(long)
     */
    public long skip(final long length) throws IOException {
        final long skip = super.skip(length);
        this.count += skip;
        long nu = count/factor;
        if (nu != this.factoredCount) { // trigger a change
            this.factoredCount = nu;
            reporter.setProgress((int)factoredCount,factoredSize);
        }       
        return skip;
    }


    /**
     * The number of bytes that have passed through this stream.
     * <p>
     * NOTE: This method is an alternative for <code>getCount()</code>
     * and was added because that method returns an integer which will
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
     * NOTE: This method is an alternative for <code>resetCount()</code>
     * and was added because that method returns an integer which will
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

    public final long getLastModified() {
        return this.lastModified;
    }

    
}
