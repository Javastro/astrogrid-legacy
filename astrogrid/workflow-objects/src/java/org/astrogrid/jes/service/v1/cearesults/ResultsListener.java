/**
 * ResultsListener.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.jes.service.v1.cearesults;

public interface ResultsListener extends java.rmi.Remote {

    // Return the results to the listener
    public void putResults(org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType jobIdentifier, org.astrogrid.jes.types.v1.cea.axis.ResultListType resultList) throws java.rmi.RemoteException;
}
