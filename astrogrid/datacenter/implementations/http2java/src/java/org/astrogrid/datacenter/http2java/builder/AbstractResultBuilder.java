/*$Id: AbstractResultBuilder.java,v 1.1 2003/11/18 11:48:15 nw Exp $
 * Created on 06-Oct-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2java.builder;

import java.nio.ByteBuffer;


/** abstract result builder - provides buffer-size methods.
 * @author Noel Winstanley nw@jb.man.ac.uk 06-Oct-2003
 *
 */
public abstract class AbstractResultBuilder implements ResultBuilder {

    public void setBufferSize(int size) {
        this.bufferSize = size;
    }
    protected ByteBuffer createBuffer() {
        ByteBuffer bb = ByteBuffer.allocate(bufferSize);
        bb.clear();
        return bb;
    }
    protected int bufferSize = DEFAULT_BUFFER_SIZE;
    public static final int DEFAULT_BUFFER_SIZE = 1000;

}


/* 
$Log: AbstractResultBuilder.java,v $
Revision 1.1  2003/11/18 11:48:15  nw
mavenized http2java

Revision 1.2  2003/11/11 14:43:33  nw
added unit tests.
basic working version

Revision 1.1  2003/10/12 21:39:34  nw
first import
 
*/