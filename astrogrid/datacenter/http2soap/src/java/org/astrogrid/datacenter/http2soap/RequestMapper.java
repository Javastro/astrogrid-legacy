/*$Id: RequestMapper.java,v 1.1 2003/10/12 21:39:34 nw Exp $
 * Created on 30-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2soap;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

/** interface of a class that maps from java method call to underlying legacy request
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Sep-2003
 *
 */
public interface RequestMapper {
    /**
     * Do the legacy request.
     * @param args the actual parameters for the request.
     * @return input stream containing the results of the call.
     * @throws RequestMapperException 
     * @throws IOException 
     */
    public ReadableByteChannel doRequest(Object[] argst) throws RequestMapperException, IOException;
    /** set the endpoint for the call
     * 
     * @param endpoint url to perform request to
     */
    public void setEndpoint(String endpoint);
    public String getEndpoint();
    /** add a formal parameter for this call */
    public void addParameter(Parameter p);
    /** access a list giving the name, type and order of the formal parameters for this request */
    public List getParameters();
}


/* 
$Log: RequestMapper.java,v $
Revision 1.1  2003/10/12 21:39:34  nw
first import
 
*/