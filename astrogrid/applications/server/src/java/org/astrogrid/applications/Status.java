package org.astrogrid.applications;

import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;

import java.util.HashMap;
import java.util.Map;

/**
 * The status values that the application can have. 
 * Follows a typesafe enum pattern: the only instances available to 
 * clients are the static, final objects declared in this class and the
 * constructor is private. This closes the set of stati and allows a reliable
 * mapping between this class and ExecutionPhase.
 *
 * @author Paul Harrison
 * @author Guy Rixon
 */
public class Status {

   private final String value;
   private static Map allmap = new HashMap();
   private static Map phaseMap = new HashMap();

   /**
    * Constructs a Status.
    * This is private in order to close the set of stati: clients can
    * only use the constant ones below.
    *
    * @param value The Text associated with the state.
    */
   private Status(String value) {
      this.value = value;
      allmap.put(value, this);
   }
   

  
   public String toString() {
     return value;
   }
   
   /** 
    * Converts from the cea enumeration type to the associated value 
    * in the JES enumeration type.
    * @return The equivalent execution phase.
    */
   public ExecutionPhase toExecutionPhase() {
       return (ExecutionPhase)phaseMap.get(this);
       
   }
   
   public boolean equals(Object o) {
       Status other = (Status)o;
       return this.value.equals(other.value);
   }
   
   /**
    * Returns a hash code for the object.
    *
    * @since 2007.2.02
    */
   public int hashCode() {
     if (this.equals(NEW)) {
       return 1;
     }
     else if (this.equals(INITIALIZED)) {
       return 2;
     }
     else if (this.equals(RUNNING)) {
       return 3;
     }
     else if (this.equals(COMPLETED)) {
       return 4;
     }
     else if (this.equals(WRITINGBACK)) {
       return 5;
     }
     else if (this.equals(ERROR)) {
       return 6;
     }
     else if (this.equals(QUEUED)) {
       return 7;
     }
     else {
       return 8; // Covers UNKNOWN and arbitrary codes.
     }
   }
   
   /** parse a string as a status 
    * @param val the strng status
    * @return equivalent enumeration object, or null*/
   public static Status valueOf(String val)
   {
      Status retval;
      retval = (Status)allmap.get(val);
      return retval;
   }
   
   /** applications are in this state when first constructed */
   public static final Status NEW = new Status("New");
   /** applications are in this state when held on a queue ready for execution. @since 2007.2.02 */
   public static final Status QUEUED = new Status("Queued");
   /** applications are in this state after the {@link org.astrogrid.applications.Application#createExecutionTask()} method has returned */
   public static final Status INITIALIZED = new Status("Initialized");
   /** applications are in this state while executing */
   public static final Status RUNNING = new Status("Running");
   /** the application has completed execution */
   public static final Status COMPLETED = new Status("Completed");
   /** the framework is writing back the results of the application execution */
   public static final Status WRITINGBACK = new Status("writing parameters back");
   /** somethings gone wrong */
   public static final Status ERROR = new Status("Error");
   /** something has gone really wrong */
   public static final Status UNKNOWN = new Status("Unknown");
   
   static {
       phaseMap.put(Status.NEW,         ExecutionPhase.INITIALIZING);
       phaseMap.put(Status.QUEUED,      ExecutionPhase.PENDING);
       phaseMap.put(Status.INITIALIZED, ExecutionPhase.PENDING);
       phaseMap.put(Status.RUNNING,     ExecutionPhase.RUNNING);
       phaseMap.put(Status.COMPLETED,   ExecutionPhase.COMPLETED);
       phaseMap.put(Status.WRITINGBACK, ExecutionPhase.RUNNING);
       phaseMap.put(Status.ERROR,       ExecutionPhase.ERROR);
       phaseMap.put(Status.UNKNOWN,     ExecutionPhase.UNKNOWN);
   }
}
