/*$Id: AbstractResultBuilder.java,v 1.1 2003/10/12 21:39:34 nw Exp $
 * Created on 06-Oct-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2soap.builder;

import java.nio.ByteBuffer;

import org.astrogrid.datacenter.http2soap.ResultBuilder;

/** abstract result builder - provides buffer-size methods.
 * @author Noel Winstanley nw@jb.man.ac.uk 06-Oct-2003
 *
 */
public abstract class AbstractResultBuilder implements ResultBuilder {

    public void setBufferSize(int size) {
        this.bufferSize = size;
    }
    protected ByteBuffer createBuffer() {
        return ByteBuffer.allocate(bufferSize);
    }
    protected int bufferSize = DEFAULT_BUFFER_SIZE;
    public static final int DEFAULT_BUFFER_SIZE = 1000;

}


/* 
$Log: AbstractResultBuilder.java,v $
Revision 1.1  2003/10/12 21:39:34  nw
first import
 
*/