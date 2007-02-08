/**
 * Miscellaneous utilities
 */

package org.eurovotech.quaestor.helpers;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


/**
 * Various utility methods, used by either Quaestor or applications which use
 * <code>quaestorlib</code>.
 */
public class Util {

    /**
     * Copy the input stream to the output one.
     *
     * <p>This is closely based on the method
     * <code>org.apache.commons.io.IOUtils.copy()</code>, since that
     * isn't included by default in the Tomcat or Jena dependencies
     * (oddly, I think).  See there for discussion.  Those docs suggest
     * that the 4kB buffer size is efficient.
     *
     * <p>Copy bytes from an InputStream to an OutputStream.
     *
     * <p>This method buffers the input internally, so there is no need 
     * to use a BufferedInputStream. 
     *
     * @param i the source stream
     * @param o the destination stream
     * @return the number of bytes copied
     * @throws IOException passed on from either stream
     */
    public static long copy(InputStream i, OutputStream o)
            throws IOException {
        byte[] buf = new byte[4096];
        long len = 0;
        int nread = 0;

        while ((nread = i.read(buf)) >= 0) {
            o.write(buf, 0, nread);
            len += nread;
        }
        return len;
    }
}

            
