/*$Id: AbstractRequest.java,v 1.1 2003/10/12 21:39:34 nw Exp $
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


import java.util.ArrayList;
import java.util.List;

import org.astrogrid.datacenter.http2soap.Parameter;
import org.astrogrid.datacenter.http2soap.RequestMapper;

/** Abstract  base class for request implementations
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Sep-2003
 *
 */
public abstract class AbstractRequest implements RequestMapper {

    public AbstractRequest() {
        parameterList = new ArrayList();
    }
    /** url endpoint for this method call */
    protected String endpoint;
    /** the list of parameters. */
    protected List parameterList;
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;

    }
    
    public String getEndpoint() {
        return this.endpoint; 
    }
    
    public List getParameters() {
        return parameterList;
    }
    public void addParameter(Parameter p) {
        parameterList.add(p);
    }

}


/* 
$Log: AbstractRequest.java,v $
Revision 1.1  2003/10/12 21:39:34  nw
first import
 
*/