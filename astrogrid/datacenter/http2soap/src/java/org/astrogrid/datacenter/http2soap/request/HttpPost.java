/*$Id: HttpPost.java,v 1.1 2003/10/12 21:39:34 nw Exp $
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
import org.apache.commons.httpclient.methods.PostMethod;
import org.astrogrid.datacenter.http2soap.RequestMapper;
import org.astrogrid.datacenter.http2soap.RequestMapperException;

/** mapper that performs request via HTTP-POST
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Sep-2003
 *
 */
public class HttpPost extends AbstractHttpRequest implements RequestMapper {



    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.legacy.request.AbstractHttpRequest#createMethod()
     */
    protected HttpMethod createMethod() throws RequestMapperException {
        PostMethod pm = new PostMethod(this.getEndpoint());
        return pm;
    }

}


/* 
$Log: HttpPost.java,v $
Revision 1.1  2003/10/12 21:39:34  nw
first import
 
*/