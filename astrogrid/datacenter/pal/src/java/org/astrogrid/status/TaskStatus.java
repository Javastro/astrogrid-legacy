/*
 * $Id: TaskStatus.java,v 1.1 2004/10/01 18:04:59 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.status;

import java.util.Date;
import org.astrogrid.community.Account;

/**
 * Defines the things that a task status must be able to return
 */

public interface TaskStatus
{
   public final static String INITIALISED = "Initialised";
   public final static String RUNNING = "Running";
   public final static String COMPLETE = "Complete";
   public final static String ERROR = "Error";
   public final static String ABORTED = "Aborted";

   /** Returns the ID of the task */
   public String getId();

   /** Returns the time the task started */
   public Date getStartTime();
   
   /** Returns who owns the task */
   public Account getOwner();
   
   /** Returns one of the above stage constants */
   public String getStage();
   
   /** Returns true if the task has finished, whether it has completed successfully or not */
   public boolean isFinished();
   
   /** Some kind of progress indication.  */
   public long getProgress();
   public long getProgressMax();
   public long getProgressMin();

   /** A user description of the state of the current task */
   public String getMessage();

   /** A list of messages or some kind of history of the task.  For example, for datacenters
    * this records what SQL was submitted, where the results are going to, etc that are concatinated
    * as they occur
    */
   public String[] getDetails();
   
}

/*
$Log: TaskStatus.java,v $
Revision 1.1  2004/10/01 18:04:59  mch
Some factoring out of status stuff, added monitor page

Revision 1.1  2004/10/01 09:42:56  mch
Began to factor out Status


 */
