package org.astrogrid.registry;

public class RegistryException extends Exception {

   public RegistryException(String msg) {
      //Need to log here.
      super(msg);
   }

   public RegistryException(Throwable cause) {
      //Need to log here.
      super(cause);
      cause.printStackTrace();      
   }

   public RegistryException(String msg, Throwable cause) {
      //Need to log here.
      super(msg, cause);
      cause.printStackTrace();      
   }  
}