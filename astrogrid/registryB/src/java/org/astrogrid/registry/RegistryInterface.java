package org.astrogrid.registry;


import java.rmi.Remote ;
import java.rmi.RemoteException;
import java.util.Date;

public interface RegistryInterface extends Remote {
   
   public Boolean submitQuery(Object a) throws RemoteException;
   
   public Boolean updateQuery(Object a) throws RemoteException;
   
   public Boolean deleteQuery(Object a) throws RemoteException;
   
   public Boolean harvest(Date a) throws RemoteException;
   
   public Boolean replicate() throws RemoteException;
   
}