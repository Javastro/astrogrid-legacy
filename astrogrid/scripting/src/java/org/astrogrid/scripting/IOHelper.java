/*$Id: IOHelper.java,v 1.3 2004/11/30 15:39:56 clq2 Exp $
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

import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.applications.parameter.protocol.InaccessibleExternalValueException;
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
import java.text.SimpleDateFormat;
import java.util.Date;

/** hellper methods for working with IO.
 * 
 * <p>
 * would have liked to just extend piper - but has no public constructor.
 * @see org.astrogrid.io.Piper
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Nov-2004
 *
 */
public class IOHelper {

    /** Construct a new IOHelper
     * 
     */
    public IOHelper() {
        super();
    }
    
    public void bufferedPipe(InputStream in, OutputStream out) throws IOException {
        Piper.bufferedPipe(in,out);
    }
    
    public void bufferedPipe(Reader in,Writer out) throws IOException {
        Piper.bufferedPipe(in,out);
    }
    public void pipe(InputStream in, OutputStream out) throws IOException {
        Piper.pipe(in,out);
    }
    
    public void pipe(Reader in, Writer out) throws IOException {
        Piper.pipe(in,out);
    }
    
    public void pipe(ExternalValue in, OutputStream out) throws InaccessibleExternalValueException, IOException {
        pipe(in.read(),out);
    }
    
    public void pipe(InputStream in,ExternalValue out) throws InaccessibleExternalValueException, IOException {
        pipe(in,out.write());
    }
    
    public void pipe(ExternalValue in,ExternalValue out) throws InaccessibleExternalValueException, IOException {
        pipe(in.read(),out.write());
    }
    
    public InputStream streamFromString(String content) {
        return new ByteArrayInputStream(content.getBytes());
    }
    
    public Reader readFromString(String content) {
        return new StringReader(content);
    }
    
    public String getContents(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        this.pipe(is,os);
        is.close();
        os.close();
        return os.toString();
    }
    
    public  String getContents(Reader r) throws IOException {
        StringWriter sw = new StringWriter();
        this.pipe(r,sw);
        r.close();
        sw.close();
        return sw.toString();             
    }
    
    public String getContents(ExternalValue ext) throws InaccessibleExternalValueException, IOException {
        return getContents(ext.read());
    }
    
    public String dateStamp() {
        return dateFormat.format(new Date());
    }
    
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
    
    public String timeStamp() {
        return String.valueOf(System.currentTimeMillis());
    }
    
    public String dateTimeStamp() {
        return dateTimeFormat.format(new Date());
    }
    
    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yy-HHmmss");

    
}


/* 
$Log: IOHelper.java,v $
Revision 1.3  2004/11/30 15:39:56  clq2
scripting-nww-777

Revision 1.1.2.1.2.1  2004/11/26 15:38:16  nw
improved some names, added some missing methods.

Revision 1.1.2.1  2004/11/22 15:54:51  nw
deprecated existing scripting interface (which includes service lists).
produced new scripting interface, with more helpler objects.
 
*/