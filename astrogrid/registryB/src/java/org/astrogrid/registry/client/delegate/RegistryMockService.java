package org.astrogrid.registry.client.delegate;

import java.rmi.RemoteException;
import java.util.Calendar;

import org.astrogrid.registry.query.request.RegistryQuery;
import org.astrogrid.registry.query.request.RegistryUpdate;
import org.astrogrid.registry.query.response.RegistryResponse;


public class RegistryMockService implements org.astrogrid.registry.RegistryInterface {

   private org.astrogrid.registry.RegistryInterface ri = null;
   
   public RegistryMockService(org.astrogrid.registry.RegistryInterface ri) {
      this.ri = ri;
   }   
   
   public RegistryResponse submitQuery(RegistryQuery rq) throws RemoteException {
     return ri.submitQuery(rq);
   }
      
   public Boolean updateQuery(RegistryUpdate ru) throws RemoteException {
      return ri.updateQuery(ru);
   }
      
   public Boolean deleteQuery(RegistryUpdate ru) throws RemoteException {
      return ri.deleteQuery(ru);
   }
      
   public Boolean harvest(Calendar a) throws RemoteException {
      return ri.harvest(a);
   }
      
   public Boolean replicate() throws RemoteException {
      return ri.replicate();
   }
   
}