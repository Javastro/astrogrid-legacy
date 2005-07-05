/**
 * WSTestSOAPBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.applications.testservice;

import java.rmi.RemoteException;

public class WSTestSOAPBindingImpl implements org.astrogrid.applications.testservice.WSTest{
    public float multiply(float r1, float r2) throws java.rmi.RemoteException {
        return r1*r2;
    }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.testservice.WSTest#add(int, int)
    */
   public int add(int i1, int i2) throws RemoteException {
      return i1+i2;
   }

}
