package org.astrogrid.applications;


import java.util.HashMap;
import java.util.Map;

import org.astrogrid.applications.manager.ExecutionController;
import org.astrogrid.security.SecurityGuard;

import net.ivoa.uws.ExecutionPhase;

/**
 * The status values that the application can have. 
 * Follows a typesafe enum pattern: the only instances available to 
 * clients are the static, final objects declared in this class and the
 * constructor is private. This closes the set of states and allows a reliable
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
   
   @Override
public boolean equals(Object obj) {
    if (this == obj)
	return true;
    if (obj == null)
	return false;
    if (getClass() != obj.getClass())
	return false;
    final Status other = (Status) obj;
    if (value == null) {
	if (other.value != null)
	    return false;
    } else if (!value.equals(other.value))
	return false;
    return true;
}
   
   @Override
public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
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
   /** applications are in this state after the {@link ExecutionController#init(org.astrogrid.applications.description.base.Tool, String, SecurityGuard)} method has returned - implies that all of the parameters necessary are present */
   public static final Status INITIALIZED = new Status("Initialized");//IMPL does this have any useful meaning any more? parameter testing more difficult....
   /** applications are in this state while reading the parameters */
   public static final Status READINGPARAMETERS = new Status("Reading parameters");
   /** applications are in this state while executing */
   public static final Status RUNNING = new Status("Running");
   /** the application has completed execution */
   public static final Status COMPLETED = new Status("Completed");
   /** the framework is writing back the results of the application execution */
   public static final Status WRITINGBACK = new Status("writing parameters back");
   /** the application has been aborted */
   public static final Status ABORTED = new Status("Aborted");
   /** something's gone wrong */
   public static final Status ERROR = new Status("Error");
   /** something has gone really wrong */
   public static final Status UNKNOWN = new Status("Unknown");
   
   
   
   static {
       phaseMap.put(Status.NEW,         ExecutionPhase.PENDING);
       phaseMap.put(Status.QUEUED,      ExecutionPhase.PENDING);
       phaseMap.put(Status.INITIALIZED, ExecutionPhase.PENDING);
       phaseMap.put(Status.READINGPARAMETERS, ExecutionPhase.EXECUTING);
       phaseMap.put(Status.RUNNING,     ExecutionPhase.EXECUTING);
       phaseMap.put(Status.COMPLETED,   ExecutionPhase.COMPLETED);
       phaseMap.put(Status.WRITINGBACK, ExecutionPhase.EXECUTING);
       phaseMap.put(Status.ERROR,       ExecutionPhase.ERROR);
       phaseMap.put(Status.UNKNOWN,     ExecutionPhase.UNKNOWN);
       phaseMap.put(Status.ABORTED, ExecutionPhase.ABORTED);
   }
}
