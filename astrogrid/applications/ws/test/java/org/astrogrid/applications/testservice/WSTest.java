/**
 * WSTest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.applications.testservice;

public interface WSTest extends java.rmi.Remote {

    // multiply two numbers
    public float multiply(float r1, float r2) throws java.rmi.RemoteException;

    // add two numbers
    public int add(int i1, int i2) throws java.rmi.RemoteException;
}
