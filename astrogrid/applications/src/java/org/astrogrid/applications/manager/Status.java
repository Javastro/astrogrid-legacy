/*
 * $Id: Status.java,v 1.2 2004/03/23 12:51:26 pah Exp $
 * 
 * Created on 17-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A small utility class to represent the current state of the applicationController.
 * @author Paul Harrison (pah@jb.man.ac.uk) 17-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class Status {
   
   private static State currentState;
   private static List logs = new ArrayList();
   
   public Status()
   {
      currentState = State.OK;
   }
   
   static class State
   {
      private String _state;
      private State(String s)
      {
         _state = s;
      }
      public static final State OK = new State("OK");
      public static final State FATAL = new State("Fatal Error");
      
      
      /* (non-Javadoc)
       * @see java.lang.Object#toString()
       */
      public String toString() {
        
        return _state;
      }
      
      public static final State normal()
      {
         return OK;
      }
      
      public static final boolean isNormal(State s)
      {
         return s == OK;
      }

   }
   
   static class LogEntry {
      private String message;
      private Date time;
      
      public LogEntry(String message)
      {
         this.message = message;
         time = new Date();
      }
      
   }
   
   public void addMessage(String message)
   {
    
     logs.add(new LogEntry(message));  
   }
   
   public void addError(String message)
   {
      addMessage(message);
      currentState = State.FATAL;
   }
   
   public boolean isNormal()
   {
      return State.isNormal(currentState);
   }

}
