/*$Id: BooleanBuilder.java,v 1.1 2003/10/12 21:39:34 nw Exp $
 * Created on 02-Oct-2003
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

import org.astrogrid.datacenter.http2soap.ResultBuilder;

/** Builder that returns a boolean value.
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Oct-2003
 *
 */
public class BooleanBuilder extends StringBuilder implements ResultBuilder {

    public Object build(ReadableByteChannel cin) throws IOException {
        return new Boolean((String)super.build(cin));

    }

}


/* 
$Log: BooleanBuilder.java,v $
Revision 1.1  2003/10/12 21:39:34  nw
first import
 
*/