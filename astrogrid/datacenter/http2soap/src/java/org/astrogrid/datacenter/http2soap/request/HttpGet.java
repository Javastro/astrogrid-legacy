/*$Id: HttpGet.java,v 1.1 2003/10/12 21:39:34 nw Exp $
 * Created on 30-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2soap.request;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.astrogrid.datacenter.http2soap.RequestMapper;
import org.astrogrid.datacenter.http2soap.RequestMapperException;
/** mapper that performs request via HTTP-GET
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Sep-2003
 *
 */
public class HttpGet extends AbstractHttpRequest implements RequestMapper {



    protected HttpMethod createMethod() throws RequestMapperException {
        GetMethod gm = new GetMethod(this.getEndpoint());
        return gm;
    }
    
    

}


/* 
$Log: HttpGet.java,v $
Revision 1.1  2003/10/12 21:39:34  nw
first import
 
*/