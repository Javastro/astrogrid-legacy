/**
 * Authorization.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.community.delegate.authorization;

public interface Authorization extends java.rmi.Remote {
    public boolean checkPermission(java.lang.String user, java.lang.String community, java.lang.String credentials, java.lang.String action, java.lang.String resource) throws java.rmi.RemoteException;
}
