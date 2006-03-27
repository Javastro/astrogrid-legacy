package org.eurovotech.quaestor;

import javax.servlet.ServletResponse;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * A lazy output handler.  The ServletResponse object can have its
 * <code>getOutputStream</code> or <code>getWriter</code> method
 * called at most once.  However two parts of a code (for example
 * including an error handler) may want to write to the Response, and
 * cannot know if the other has already opened the output.  If they
 * instead go via this class, then they do not have to care.
 *
 * <p>If the access to the output stream is mediated through this
 * class, then the problem disappears, both the {@link #getOutputStream}
 * and the {@link #getWriter} methods of this class may be called,
 * possibly multiple times.
 *
 * <p>Usage:
 * <pre>
 * ServletResponse resp = ...;
 * LazyOutputStream los = getLazyOutputStream(resp);
 * OutputStream os = los.getOutputStream();
 * ...
 * PrintWriter pw = los.getWriter(); // not an error
 * </pre>
 * @see javax.servlet.ServletResponse
 */
public class LazyOutputStream {
    private ServletResponse response;
    private OutputStream stream;
    private PrintWriter writer;

    private static java.util.Map streamMap;
    static {
        streamMap = new java.util.WeakHashMap();
    }

    /**
     * Constructor is private by design.
     */
    private LazyOutputStream(ServletResponse response) {
        assert response != null;
        this.response = response;
    }

    /**
     * Retrieve the LazyOutputStream associated with the given HTTP
     * response.  This will either be a new instance of this class, or
     * a previously created one, if this method has been called before
     * with the given response.
     * @param response a response object obtained from the Servlet container
     * @return the unique LazyOutputStream associated with this response
     * @throws IllegalArgumentException if the response is null
     */
    public static LazyOutputStream getLazyOutputStream
            (ServletResponse response) {
        assert streamMap != null;
        if (response == null)
            throw new IllegalArgumentException("null response object");
        synchronized (streamMap) {
            LazyOutputStream los = (LazyOutputStream)streamMap.get(response);
            if (los == null) {
                los = new LazyOutputStream(response);
                streamMap.put(response, los);
            }
            return los;
        }
    }

    /**
     * Returns the output stream associated with this object.
     */
    public synchronized OutputStream getOutputStream()
            throws java.io.IOException {
        if (stream == null) {
            assert writer == null; // writer is set after a call to this method
            stream = response.getOutputStream();
        }
        return stream;
    }

    /**
     * Returns the Writer associated with this object.
     */
    public synchronized PrintWriter getWriter() 
            throws java.io.IOException {
        if (writer == null)
            writer = new PrintWriter(getOutputStream());
        return writer;
    }
}
