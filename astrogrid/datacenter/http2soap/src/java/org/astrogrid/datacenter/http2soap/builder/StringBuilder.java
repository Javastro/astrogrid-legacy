/*$Id: StringBuilder.java,v 1.1 2003/10/12 21:39:34 nw Exp $
 * Created on 30-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2soap.builder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ReadableByteChannel;

import org.astrogrid.datacenter.http2soap.ResultBuilder;

/** build a string from the response stream
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Sep-2003
 *
 */
public class StringBuilder extends AbstractResultBuilder implements ResultBuilder {

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.legacy.ResultBuilder#build(java.io.InputStream)
     */
    public Object build(ReadableByteChannel cin) throws IOException {
        ByteBuffer bb = createBuffer();
        cin.close();
        bb.flip();
        CharBuffer cb = bb.asCharBuffer();
        return cb.toString();

    }

}


/* 
$Log: StringBuilder.java,v $
Revision 1.1  2003/10/12 21:39:34  nw
first import
 
*/