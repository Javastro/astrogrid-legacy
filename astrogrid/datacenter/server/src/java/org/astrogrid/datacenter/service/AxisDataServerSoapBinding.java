/*$Id: AxisDataServerSoapBinding.java,v 1.3 2004/01/13 00:33:14 nw Exp $
 * Created on 27-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.service;

import java.io.IOException;

import org.astrogrid.datacenter.axisdataserver.AxisDataServerSoapBindingSkeleton;

/** Soap binding stub that wraps our implementation class
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Nov-2003
 *
 */
public class AxisDataServerSoapBinding
    extends AxisDataServerSoapBindingSkeleton {

    /**
     *
     */
    public AxisDataServerSoapBinding() throws IOException {
        super(new AxisDataServer());
    }

    /**
     * @param impl
     */
    public AxisDataServerSoapBinding(org.astrogrid.datacenter.axisdataserver.AxisDataServer impl) {
        super(impl);
        // TODO Auto-generated constructor stub
    }

}


/*
$Log: AxisDataServerSoapBinding.java,v $
Revision 1.3  2004/01/13 00:33:14  nw
Merged in branch providing
* sql pass-through
* replace Certification by User
* Rename _query as Query

Revision 1.2.4.1  2004/01/08 09:43:40  nw
replaced adql front end with a generalized front end that accepts
a range of query languages (pass-thru sql at the moment)

Revision 1.2  2003/12/16 11:10:12  mch
Added IOException throws to constructor

Revision 1.1  2003/11/27 17:28:09  nw
finished plugin-refactoring
 
*/
