package org.astrogrid.registry.server;

import java.rmi.RemoteException; 
import java.util.Date;

import org.astrogrid.registry.query.request.RegistryQuery;
import org.astrogrid.registry.query.request.RegistryUpdate;
import org.astrogrid.registry.query.response.RegistryResponse;


public class RegistryService implements org.astrogrid.registry.RegistryInterface {

   public RegistryResponse submitQuery(RegistryQuery rq) throws RemoteException {
      return null;
   }
      
   public Boolean updateQuery(RegistryUpdate ru) throws RemoteException {
      return new Boolean(true);
   }
      
   public Boolean deleteQuery(RegistryUpdate ru) throws RemoteException {
      return new Boolean(true);
   }
      
   public Boolean harvest(Date a) throws RemoteException {
      return new Boolean(true);
   }
      
   public Boolean replicate() throws RemoteException {
      return new Boolean(true);
   }

}