/*$Id: IOHelper.java,v 1.4 2004/12/06 20:03:03 clq2 Exp $
 * Created on 22-Nov-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.scripting;

import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibraryFactory;
import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.applications.parameter.protocol.InaccessibleExternalValueException;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.applications.parameter.protocol.UnrecognizedProtocolException;
import org.astrogrid.io.Piper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/** hellper methods for working with IO.
 * 
 * <p>
 * would have liked to just extend piper - but has no public constructor.
 * @see org.astrogrid.io.Piper
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Nov-2004
 *@script-summary help with input / output
 *@script-doc helper methods for working with streams and external values
 */
public class IOHelper {

    /** Construct a new IOHelper
     * 
     */
    public IOHelper() {
        super();
    }
   /**@script-doc pipe contents of <code>in</code> to <code>out</code> */
    public void bufferedPipe(InputStream in, OutputStream out) throws IOException {
        Piper.bufferedPipe(in,out);
    }
    /**@script-doc pipe contents of <code>in</code> to <code>out</code> */    
    public void bufferedPipe(Reader in,Writer out) throws IOException {
        Piper.bufferedPipe(in,out);
    }
    /**@script-doc pipe contents of <code>in</code> to <code>out</code> */
    public void pipe(InputStream in, OutputStream out) throws IOException {
        Piper.pipe(in,out);
    }
    /**@script-doc pipe contents of <code>in</code> to <code>out</code> */
    public void pipe(Reader in, Writer out) throws IOException {
        Piper.pipe(in,out);
    }
    /**@script-doc pipe contents of <code>in</code> to <code>out</code> */
    public void pipe(ExternalValue in, OutputStream out) throws InaccessibleExternalValueException, IOException {
        pipe(in.read(),out);
    }
    /**@script-doc pipe contents of <code>in</code> to <code>out</code> */
    public void pipe(InputStream in,ExternalValue out) throws InaccessibleExternalValueException, IOException {
        pipe(in,out.write());
    }
    /**@script-doc pipe contents of <code>in</code> to <code>out</code> */    
    public void pipe(ExternalValue in,ExternalValue out) throws InaccessibleExternalValueException, IOException {
        pipe(in.read(),out.write());
    }
    /***@script-doc returns a stream of the contents of a string */
    public InputStream streamFromString(String content) {
        return new ByteArrayInputStream(content.getBytes());
    }
    /**@script-doc returns a reader of the contents of a string */
    public Reader readFromString(String content) {
        return new StringReader(content);
    }
    /** @script-doc read contents of a stream */
    public String getContents(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        this.pipe(is,os);
        is.close();
        os.close();
        return os.toString();
    }
    /** @script-doc read contents of a stream */    
    public  String getContents(Reader r) throws IOException {
        StringWriter sw = new StringWriter();
        this.pipe(r,sw);
        r.close();
        sw.close();
        return sw.toString();             
    }
    /** @script-doc read contents of an external value */
    public String getContents(ExternalValue ext) throws InaccessibleExternalValueException, IOException {
        return getContents(ext.read());
    }
    /** @script-doc generate a date-stamp in format 'DD-MM-YY' */
    public String dateStamp() {
        return dateFormat.format(new Date());
    }
    
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
    /** @script-doc generate a time-stamp as a long number */
    public String timeStamp() {
        return String.valueOf(System.currentTimeMillis());
    }
    /** @script-doc generate a date/time stamp in format 'DD-MM-YY-HHMMSS' */
    public String dateTimeStamp() {
        return dateTimeFormat.format(new Date());
    }
    
    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yy-HHmmss");

    /** access library object that 'knows' about a variety of IO protocols, and can construct {@link ExternalValue} objects to 
     * read / write resources via these protocols.
     */
    public ProtocolLibrary getProtocolLibrary() {
        return protocolLib;
    }

    private final ProtocolLibrary protocolLib = (new DefaultProtocolLibraryFactory()).createLibrary();
    
    /** create an external value that points to a uri */
    public ExternalValue getExternalValue(String uri) throws InaccessibleExternalValueException, UnrecognizedProtocolException, URISyntaxException {
        return protocolLib.getExternalValue(uri);
    }

    /** create an external value that points to a uri */    
    public ExternalValue getExternalValue(URI uri) throws InaccessibleExternalValueException, UnrecognizedProtocolException {
        return protocolLib.getExternalValue(uri);
    }

    /** create an external value that points to a uri */    
    public ExternalValue getExternalValue(URL uri) throws InaccessibleExternalValueException, UnrecognizedProtocolException, URISyntaxException {
        return protocolLib.getExternalValue(uri.toString());
    }
    
}


/* 
$Log: IOHelper.java,v $
Revision 1.4  2004/12/06 20:03:03  clq2
nww_807a

Revision 1.3.2.1  2004/12/06 13:27:47  nw
fixes to improvide integration with external values and starTables.

Revision 1.3  2004/11/30 15:39:56  clq2
scripting-nww-777

Revision 1.1.2.1.2.1  2004/11/26 15:38:16  nw
improved some names, added some missing methods.

Revision 1.1.2.1  2004/11/22 15:54:51  nw
deprecated existing scripting interface (which includes service lists).
produced new scripting interface, with more helpler objects.
 
*/