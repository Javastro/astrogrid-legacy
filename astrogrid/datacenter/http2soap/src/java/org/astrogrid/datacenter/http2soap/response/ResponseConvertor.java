/*$Id: ResponseConvertor.java,v 1.1 2003/11/11 14:43:33 nw Exp $
 * Created on 30-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2soap.response;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/** interface of a class that maps the response from a legacy request
 * <p>
 * consumes the results of a request, producing another stream in turn
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Sep-2003
 *
 */
public interface ResponseConvertor {    
    public void convertResponse(ReadableByteChannel in, WritableByteChannel out) throws ResponseConvertorException, IOException;
 
}


/* 
$Log: ResponseConvertor.java,v $
Revision 1.1  2003/11/11 14:43:33  nw
added unit tests.
basic working version

Revision 1.1  2003/10/12 21:39:34  nw
first import
 
*/