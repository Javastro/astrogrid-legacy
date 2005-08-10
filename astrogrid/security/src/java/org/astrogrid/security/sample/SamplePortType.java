package org.astrogrid.security.sample;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SamplePortType extends Remote {

  public String whoAmI () throws RemoteException;

}