/*$Id: ResultsListenerSoapBindingImpl.java,v 1.1 2004/07/01 21:15:00 nw Exp $
 * Created on 01-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.resultlistener;

import org.astrogrid.jes.component.ComponentManagerFactory;
import org.astrogrid.jes.service.v1.cearesults.ResultsListener;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.ResultListType;

import java.rmi.RemoteException;

/** soap skeleton - just delegates on to real method in container.
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Jul-2004
 *
 */
public class ResultsListenerSoapBindingImpl implements ResultsListener {
    /** Construct a new ResultsListenerSoapBindingImple
     * 
     */
    public ResultsListenerSoapBindingImpl() {
        listener = ComponentManagerFactory.getInstance().getResultsListener();
    }
    protected final ResultsListener listener;
    /**
     * @see org.astrogrid.jes.service.v1.cearesults.ResultsListener#putResults(org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType, org.astrogrid.jes.types.v1.cea.axis.ResultListType)
     */
    public void putResults(JobIdentifierType arg0, ResultListType arg1) throws RemoteException {
        listener.putResults(arg0,arg1);
    }
}


/* 
$Log: ResultsListenerSoapBindingImpl.java,v $
Revision 1.1  2004/07/01 21:15:00  nw
added results-listener interface to jes
 
*/