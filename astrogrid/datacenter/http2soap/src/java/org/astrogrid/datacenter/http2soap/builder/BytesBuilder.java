/*$Id: BytesBuilder.java,v 1.2 2003/11/11 14:43:33 nw Exp $
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
import java.nio.channels.ReadableByteChannel;


/** Builder that returns result as an array of bytes.
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Sep-2003
 *
 */
public class BytesBuilder extends AbstractResultBuilder  implements ResultBuilder {

 
    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.legacy.ResultBuilder#build(java.io.InputStream)
     */
    public Object build(ReadableByteChannel ic) throws IOException {
        ByteBuffer bb = createBuffer();
        int sum = 0;
        for (int count = 0; count != -1; count = ic.read(bb)) {
            sum += count;
        }
        ic.close();
        byte[] result = new byte[sum];
        bb.flip();
        bb.get(result);
        return result;
    }
    


}


/* 
$Log: BytesBuilder.java,v $
Revision 1.2  2003/11/11 14:43:33  nw
added unit tests.
basic working version

Revision 1.1  2003/10/12 21:39:34  nw
first import
 
*/