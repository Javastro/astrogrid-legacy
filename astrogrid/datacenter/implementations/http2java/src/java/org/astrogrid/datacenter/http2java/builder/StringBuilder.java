/*$Id: StringBuilder.java,v 1.1 2003/11/18 11:48:15 nw Exp $
 * Created on 30-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2java.builder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;


/** build a string from the response stream
 * expects string to be in UTF-8 encoding by default.
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Sep-2003
 *
 */
public class StringBuilder extends AbstractResultBuilder implements ResultBuilder {

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.legacy.ResultBuilder#build(java.io.InputStream)
     */
    public Object build(ReadableByteChannel cin) throws IOException {
        ByteBuffer bb = createBuffer();
        for (int count = 0 ; count != -1; count = cin.read(bb)) {
            // keep reading until empty.
        }
        cin.close();
        bb.flip();
        CharBuffer cb = charset.decode(bb);
        return cb.toString();

    }
    
    public void setEncoding(String encoding) {
        this.charset = Charset.forName(encoding);
    }
    // the encoding charset.
    protected Charset charset = Charset.forName("UTF-8");

}


/* 
$Log: StringBuilder.java,v $
Revision 1.1  2003/11/18 11:48:15  nw
mavenized http2java

Revision 1.2  2003/11/11 14:43:33  nw
added unit tests.
basic working version

Revision 1.1  2003/10/12 21:39:34  nw
first import
 
*/