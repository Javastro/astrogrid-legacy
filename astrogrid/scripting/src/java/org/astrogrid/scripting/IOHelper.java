/*$Id: IOHelper.java,v 1.2 2004/11/22 18:26:54 clq2 Exp $
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
    
    public InputStream streamFromString(String content) {
        return new ByteArrayInputStream(content.getBytes());
    }
    
    public Reader readFromString(String content) {
        return new StringReader(content);
    }
    
    public String streamToString(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        this.pipe(is,os);
        is.close();
        os.close();
        return os.toString();
    }
    
    public  String writeToString(Reader r) throws IOException {
        StringWriter sw = new StringWriter();
        this.pipe(r,sw);
        r.close();
        sw.close();
        return sw.toString();             
    }

}


/* 
$Log: IOHelper.java,v $
Revision 1.2  2004/11/22 18:26:54  clq2
scripting-nww-715

Revision 1.1.2.1  2004/11/22 15:54:51  nw
deprecated existing scripting interface (which includes service lists).
produced new scripting interface, with more helpler objects.
 
*/