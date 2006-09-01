package org.eurovotech.quaestor.helpers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Encapsulates the result of an HTTP transaction.
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
        
    HttpResult(int status, String contentType, InputStream content) {
        this.status = status;

        if (contentType == null) {
            this.responseContentType = null;
            this.responseContentTypeParameters = null;
        } else {
            java.util.regex.Matcher m = parameters.matcher(contentType);
            if (m.matches()) {
                this.responseContentType = m.group(1);
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

    /** Obtains the MIME type of the transaction response */
    public String getContentType() {
        return (responseContentType == null ? "" : responseContentType);
    }

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
     * or if this method has already been called, then return null
     * without error.
     *
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

        char[] buffer = new char[256];
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
