package org.astrogrid.registry.delegate;

import java.rmi.RemoteException; 
import java.util.Date;

public class RegistryService implements org.astrogrid.registry.RegistryInterface {

   public Boolean submitQuery(Object a) throws RemoteException {
      return new Boolean(true);
   }
      
   public Boolean updateQuery(Object a) throws RemoteException {
      return new Boolean(true);
   }
      
   public Boolean deleteQuery(Object a) throws RemoteException {
      return new Boolean(true);
   }
      
   public Boolean harvest(Date a) throws RemoteException {
      return new Boolean(true);
   }
      
   public Boolean replicate() throws RemoteException {
      return new Boolean(true);
   }

}