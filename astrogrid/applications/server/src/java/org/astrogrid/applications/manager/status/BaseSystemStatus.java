/*
 * $Id: BaseSystemStatus.java,v 1.2 2004/07/01 11:16:22 nw Exp $
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

package org.astrogrid.applications.manager.status;

import org.astrogrid.component.descriptor.ComponentDescriptor;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;

/**
 * A small utility class to represent the current state of the applicationController.
 * object is shared between components, as a global flag mechanism.
 * 
 * @author Paul Harrison (pah@jb.man.ac.uk) 17-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class BaseSystemStatus implements ComponentDescriptor, SystemStatus {
   
   private static State currentState;
   /** @todo - can't have the log messages stored in memory - otherwise application controller will slowly grind to a halt. not good! */
   private static List logs = new ArrayList();
   
   public BaseSystemStatus()
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
      public String toString(){
          return "Log: " + time + " " + message;
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

/**
 * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
 */
public String getName() {
    return "Default Controller Status";
}

/**
 * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
 */
public String getDescription() {
    StringBuffer logMessages = new StringBuffer();
    for (Iterator i = this.logs.iterator(); i.hasNext();) {
       logMessages.append(i.next().toString());
       logMessages.append("\n");
    }
    return "Current system status is:" + currentState.toString()
        + "\n Log Messages " + logMessages.toString();
}

/**
 * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
 * @todo - add a test that checks state is currenlty ok.
 */
public Test getInstallationTest() {
    return null;
}

}
