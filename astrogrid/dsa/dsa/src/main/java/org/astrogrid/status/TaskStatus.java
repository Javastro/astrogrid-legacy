/*
 * $Id: TaskStatus.java,v 1.1.1.1 2009/05/13 13:20:42 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.status;

import java.security.Principal;
import java.util.Date;

/**
 * Defines the things that a task status must be able to return
 */

public interface TaskStatus
{
   public final static String INITIALISED = "Initialised";
   public final static String QUEUED = "Queued";
   public final static String RUNNING = "Running";
   public final static String COMPLETE = "Complete";
   public final static String ERROR = "Error";
   public final static String ABORTED = "Aborted";

   /** Returns the ID of the task, that might be used to act on the task */
   public String getId();

   /** Returns the time this status was reached */
   public Date getTimestamp();
   
   /** Returns who owns the task */
   public Principal getOwner();
   
   /** Returns one of the above stage constants */
   public String getStage();
   
   /** Returns some indication of the source of the task - undefined, but might
    * be a URI or the interface used, etc */
   public String getSource();
   
   /** Returns true if the task has finished, whether it has completed successfully or not */
   public boolean isFinished();
   
   /** Some kind of progress indication.  */
   public long getProgress();
   public long getProgressMax();
   /** to go with progress indicators - eg 'getting row ' */
   public String getProgressText();

   /** A user description of the state of the current task */
   public String getMessage();

   /** A list of messages or some kind of history of the task.  For example, for datacenters
    * this records what SQL was submitted, where the results are going to, etc that are concatinated
    * as they occur
    */
   public String[] getDetails();
   
   /** An implementation might use a 'chain' of statuses to record the history of
    * the task.  To get the previous status use this */
   public TaskStatus getPrevious();
   
   /** Implement a convenience routine to get to the first task */
   public TaskStatus getFirst();
   
}

/*
$Log: TaskStatus.java,v $
Revision 1.1.1.1  2009/05/13 13:20:42  gtr


Revision 1.1  2006/06/16 14:50:06  kea
Moving code from astrogrid/status project back into pal, and updating
dependencies.

Revision 1.1.1.1  2005/02/16 19:43:25  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 15:15:01  mch
Initial checkin

Revision 1.1.26.3  2004/11/26 18:17:21  mch
More status persisting, browsing and aborting

Revision 1.1.26.2  2004/11/25 18:33:43  mch
more status (incl persisting) more tablewriting lots of fixes

Revision 1.1.26.1  2004/11/22 00:57:16  mch
New interfaces for SIAP etc and new slinger package

Revision 1.1  2004/10/08 15:14:59  mch
Moved from server to client

Revision 1.2  2004/10/05 15:04:00  mch
Added Queued

Revision 1.1  2004/10/01 18:04:59  mch
Some factoring out of status stuff, added monitor page

Revision 1.1  2004/10/01 09:42:56  mch
Began to factor out Status


 */
