package org.eurovotech.quaestor.helpers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Encapsulates the result of an HTTP transaction obtained from 
 * a QuaestorConnection.
 * @see org.eurovotech.quaestor.helpers.QuaestorConnection
 * @see <a href="http://www.ietf.org/rfc/rfc2045.txt" >RFC 2045: (MIME) Part One: Format of Internet Message Bodies</a>
 */
public class HttpResult {
    private int status;
    private String responseContentType;
    private String responseContentTypeParameters;
    private InputStream responseStream;
    private String responseContentString = null;

    private static java.util.regex.Pattern parameters;
    static {
        parameters = java.util.regex.Pattern.compile("([^;]*)(; *(.*))?");
    }

    /**
     * Protected constructor
     */
    HttpResult(int status, String contentType, InputStream content) {
        this.status = status;

        if (contentType == null) {
            this.responseContentType = "";
            this.responseContentTypeParameters = "";
        } else {
            java.util.regex.Matcher m = parameters.matcher(contentType);
            if (m.matches()) {
                this.responseContentType = m.group(1).toLowerCase();
                this.responseContentTypeParameters = 
                        (m.group(3) == null ? "" : m.group(3));
            } else {
                assert false;
            }
        }
        this.responseStream = content;
    }

    /** Obtains the HTTP status of the transaction */
    public int getStatus() {
        return status;
    }

    /**
     * Obtains the MIME type of the transaction response.  This is
     * guaranteed to be lower case.  Any content-type parameters are removed.
     *
     * @return the content type, or the empty string if no
     * content-type is available
     */
    public String getContentType() {
        return responseContentType;
    }

    /**
     * Obtains any parameters on the MIME content-type of the transaction
     * response.  MIME content-types can have parameters following the media
     * type, for example <code>Content-type: text/plain;
     * charset="us-ascii"</code>, and this method returns any text
     * after the semicolon in such a content-type header, with leading
     * whitespace stripped.  See RFC 2045 for definitions.
     *
     * @return any content-type parameters as a string, or an empty
     *   string if there are no parameters on the content-type
     */
    public String getContentTypeParameters() {
        return responseContentTypeParameters;
    }

    /**
     * Obtains the content of the HTTP transaction as a stream
     */
    public InputStream getContentStream() {
        return responseStream;
    }

    /** 
     * Obtains the content of the HTTP transaction response as a
     * string.  If there was no response stream for some reason,
     * then return null without error.  This method may be called
     * multiple times, returning the same content each time.
     *
     * @return content of the HTTP response, as a single string
     * @throws java.io.IOException if there is a problem reading from the stream
     */
    public String getContentAsString()
            throws java.io.IOException {
        if (responseContentString != null)
            return responseContentString;

        if (responseStream == null)
            return null;

        StringBuffer sb = new StringBuffer();
        BufferedReader r
                = new BufferedReader
                (new InputStreamReader(responseStream));

        char[] buffer = new char[4096];
        int nread;
        while ((nread = r.read(buffer, 0, buffer.length)) >= 0) {
            sb.append(buffer, 0, nread);
        }
        r.close();
        responseStream = null;
            
        if (sb.length() == 0)
            responseContentString = null;
        else
            responseContentString = sb.toString();
        return responseContentString;
    }

    /** 
     * Obtains the content of the HTTP transaction response as an array of
     * strings.  If there was no response stream for some reason,
     * then return null without error.  This method may be called
     * multiple times, returning the same content each time.
     *
     * @return content of the HTTP response, one line per array element
     * @throws java.io.IOException if there is a problem reading from the stream
     */
    public String[] getContentAsStringList()
            throws java.io.IOException {
        return getContentAsString().split("[\\n\\r]+");
    }

    /**
     * Provides a representation of an HttpResult object, primarily for debugging
     */
    public String toString() {
        try {
            return "Status=" + status
                    + ", content-type=" + responseContentType
                    + "(" + responseContentTypeParameters
                    //                + ")";
                    + "), content=" + getContentAsString();
        } catch (java.io.IOException e) {
            return "Status=" + status
                    + ", content-type=" + responseContentType
                    + "(" + responseContentTypeParameters + ")";
                   
        }
    }
}
