/*$Id: AxisDataServerSoapBinding.java,v 1.1 2003/11/27 17:28:09 nw Exp $
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
    public AxisDataServerSoapBinding() {
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
Revision 1.1  2003/11/27 17:28:09  nw
finished plugin-refactoring
 
*/