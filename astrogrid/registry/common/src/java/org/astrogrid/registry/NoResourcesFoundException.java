package org.astrogrid.registry;

public class NoResourcesFoundException extends RegistryException {

   public NoResourcesFoundException(String msg) {
      //Need to log here.
      super(msg);
   }

   public NoResourcesFoundException(Throwable cause) {
      //Need to log here.
      super(cause);      
   }

   public NoResourcesFoundException(String msg, Throwable cause) {
      //Need to log here.
      super(msg, cause);      
   }  
}