/*$Id: RequestMapperType.java,v 1.1 2003/10/12 21:39:34 nw Exp $
 * Created on 02-Oct-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2soap;

import org.astrogrid.datacenter.http2soap.request.HttpGet;
import org.astrogrid.datacenter.http2soap.request.HttpPost;

/** enumeration of possible request mapper types.
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Oct-2003
 *
 */
public class RequestMapperType {

    /**
     * static class of enumeration of request types.
     */
    private RequestMapperType() {
    }
    
    public static final String GET = "get";
    public static final String POST = "post";

    public static RequestMapper createRequestMapper(String type) {
        if (RequestMapperType.POST.equalsIgnoreCase(type)) {
            return new HttpPost();
        }
        if (RequestMapperType.GET.equalsIgnoreCase(type)) {
            return new HttpGet();
        }           
        throw new IllegalArgumentException("Request Object: Unknown Type");
    }
}


/* 
$Log: RequestMapperType.java,v $
Revision 1.1  2003/10/12 21:39:34  nw
first import
 
*/