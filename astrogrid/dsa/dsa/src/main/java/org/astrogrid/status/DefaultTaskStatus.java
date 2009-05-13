/*
 * $Id: DefaultTaskStatus.java,v 1.1 2009/05/13 13:20:42 gtr Exp $
 */

package org.astrogrid.status;

import java.util.Vector;
import java.util.Date;
import java.security.Principal;

/**
 * Default implementation of task status.  Can also be used as a soapy bean
 */

public class DefaultTaskStatus implements TaskStatus
{
   
   /** The ID of the task this is the status for */
   String id = null;
   /** The owner of the task - who is running it */
   Principal owner = null;
   String stage = null;
   String source = null;
   String message = "";
   Date timestamp = new Date();
   
   long progress = -1;
   long progressMax = -1;
   String progressText = "";
   
   Vector details = new Vector();
   TaskStatus previous = null;
   
   /** Bean (empty) constructor for SOAP messages, etc */
   public DefaultTaskStatus() {
   }
   

   /** Constructs new task with given one as previous, and copies in owner, source, etc */
   public DefaultTaskStatus(TaskStatus existing, String newStage) {
      this.id = existing.getId();
      this.owner = existing.getOwner();
      this.stage = newStage;
      this.source = existing.getSource();

      this.previous = existing;
      
   }
   
   public void setId(String id)  {     this.id = id;  }
   
   public String getId()   {     return id;   }
   
   public void setPrevious(TaskStatus previous) {
      if (this == previous) {
         throw new IllegalArgumentException("Setting previous to self; circular link");
      }
      this.previous = previous;
   }
   
   public TaskStatus getPrevious()  {     return previous;  }
   
  
   public void setMessage(String message) {     this.message = message; }
   
   public String getMessage() {     return message;   }
   
   public void setSource(String source)   {     this.source = source;   }
   
   public String getSource()  {     return source; }
   
   public void setStage(String stage)  {     this.stage = stage.trim();  }
   
   public String getStage()   {     return stage;  }
   
   public void setOwner(Principal owner)  {     this.owner = owner;  }
   
   public Principal getOwner()   {     return owner;  }
   
   /** Returns true if the task has finished, whether it has completed successfully or not */
   public boolean isFinished()
   {
      return (stage.toUpperCase().equals(COMPLETE.toUpperCase()) ||
              stage.toUpperCase().equals(ABORTED.toUpperCase()) ||
              stage.toUpperCase().equals(ERROR.toUpperCase()));
   }
   public boolean isError()
   {
      return (stage.toUpperCase().equals(ERROR.toUpperCase())); 
   }
   
   public void setTimestamp(Date timestamp)  {     this.timestamp = timestamp;   }
   
   public Date getTimestamp() {     return timestamp; }
   
   public void setProgressMax(long progressMax) {     this.progressMax = progressMax;  }
   
   public long getProgressMax()  {     return progressMax;  }
   
   public void setProgress(long progress) {     this.progress = progress;  }
   
   public long getProgress()  {     return progress;  }
   
   public void setDetails(String[] newDetails) {
      for (int i = 0; i < newDetails.length; i++)
      {
         details.add(newDetails[i]);
      }
   }
   
   public void addDetail(String message) {
      details.add(message);
   }
   public String[] getDetails()  {
      return (String[]) details.toArray(new String[] {});
   }

   public void setProgressText(String progressText)   {     this.progressText = progressText;   }
   
   public String getProgressText()  {     return progressText; }
   
   /** Returns the progress as a human readable string */
   public String getProgressMsg() {
      if (getProgress()==-1) {
         return progressText;
      }
      else if (progressMax==-1) {
         return progressText+" "+progress;
      }
      else {
         int percent = (int) ( progress *100 / progressMax);
         return progressText+" "+progressText+" of "+progressMax+ "("+percent+"%)";
      }
   }

   /** Returns first status  */
   public TaskStatus getFirst() {

      TaskStatus first = this;
      while (first.getPrevious() != null) { first = first.getPrevious(); }
      return first;
   }

   /** Start time is the timestamp of the first task */
   public Date getStartTime() {
      return getFirst().getTimestamp();
   }
   
   public String toString() {
      return "Status ["+getId()+"] "+getStage()+" @"+getTimestamp()+" from "+getSource()+" by "+getOwner(); //+" (prev="+getPrevious()+")";
   }


   /** Cleans up fine-scale detail of status (to be used once query has 
    * completed to save memory).
    */
   public void clearHistory() {
      this.details = new Vector();
      this.previous = null;
      this.message = "";
      this.source = null;
      this.progressText = "";
   }
}
