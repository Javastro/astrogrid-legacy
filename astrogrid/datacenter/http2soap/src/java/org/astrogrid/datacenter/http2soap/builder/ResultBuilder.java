/*$Id: ResultBuilder.java,v 1.1 2003/11/11 14:43:33 nw Exp $
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
import java.nio.channels.ReadableByteChannel;

/** interface of class that builds result object from response from legacy web method.
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Sep-2003
 *
 */
public interface ResultBuilder  {
    /** build an object from stream. */
    public Object build(ReadableByteChannel in) throws ResultBuilderException, IOException;
    /** set maximum chunk to read from in channel to build object
     *  - i.e. size of internal buffer allocated
     * @author Noel Winstanley nw@jb.man.ac.uk 06-Oct-2003
     *
     */
    public void setBufferSize(int size);
}


/* 
$Log: ResultBuilder.java,v $
Revision 1.1  2003/11/11 14:43:33  nw
added unit tests.
basic working version

Revision 1.1  2003/10/12 21:39:34  nw
first import
 
*/