package org.astrogrid.registry;


import java.rmi.Remote ;
import java.rmi.RemoteException;
import java.util.Date;

import org.astrogrid.registry.query.request.RegistryQuery;
import org.astrogrid.registry.query.request.RegistryUpdate;
import org.astrogrid.registry.query.response.RegistryResponse;


public interface RegistryInterface extends Remote {
   
   public RegistryResponse submitQuery(RegistryQuery rq) throws RemoteException;
   
   public Boolean updateQuery(RegistryUpdate ru) throws RemoteException;
   
   public Boolean deleteQuery(RegistryUpdate ru) throws RemoteException;
   
   public Boolean harvest(Date a) throws RemoteException;
   
   public Boolean replicate() throws RemoteException;
   
}