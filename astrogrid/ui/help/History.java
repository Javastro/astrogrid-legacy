package org.astrogrid.ui.help;

import java.util.Vector;
import javax.swing.tree.*;
import java.awt.event.*;
import log.Log;

/**
 * History (from the navigator package)
 *
 * Provides a way of tracking a history, such as may be used by browsers.
 * This takes & returns Object types for each instance; it's up to the
 * using class to check for typesafety.
 *
 * Is an action listener, so can listen for (eg) button events - which
 * should have the commands given as final constants below
 *
 * Similarly, it generates History events, so that other components can tell if
 * it has changed
 *
 * @author M Hill
 */

public class History implements ActionListener
{
   private Vector history = new Vector();
   private int historyCursor = -1;
   
   private Vector listeners = new Vector();
   
   //command for back/fwd actions
   public static final String HISTORY_CMD_BACK = "BackHistory";
   public static final String HISTORY_CMD_FWD = "FwdHistory";
   public static final String HISTORY_CMD_CLEAR = "Clear History";
   
   /**
    * Add item to history at current index. ie, mmove forward,
    * ie trim at current index, add new item to end, and move
    * cursor forward to that new item
    */
   public void add(Object item)
   {
      trim();
      history.add(item);
      historyCursor++;
      fireHistoryChange();
   }
   
   /**
    * Chops history so it contains only all those before and
    * <b>including</b> the historyCursor
    */
   private void trim()
   {
      if (historyCursor>-1)
         while (history.size()>(historyCursor+1))
            history.removeElementAt(historyCursor+1);
      else
         history.clear();
      
   }

   /**
    * Gets current history item - returns null if no history
    */
   public Object getCurrentItem()
   {
      if (historyCursor<0)
         return null;
      else
         return history.elementAt(historyCursor);
   }
   
   public Object getPreviousItem()
   {
      if (historyCursor<1)
         return null;
      else
         return history.elementAt(historyCursor-1);
   }
   
   /**
    * Clears history - resets to empty
    */
   public void clear()
   {
      historyCursor = -1;
      trim();
   }
   
   /**
    * move to previous item in history and return it.
    * Returns null if no previous
    */
   public Object moveBack()
   {
      if (historyCursor >0)
      {
         historyCursor--;
         fireHistoryChange();
         return getCurrentItem();
      }
      return null;
   }
   
   /**
    * Move to next item in history and return it
    * Returns null if no next
    */
   public Object moveForward()
   {
      if (historyCursor+1<history.size())
      {
         historyCursor++;
         fireHistoryChange();
         return getCurrentItem();
      }
      return null;
   }
   

   /**
    * Register listener to listen to history cursor changes
    *
   public void addHistoryListener(HistoryListener newListener)
   {
      listeners.add(newListener);
   }
   
   /**
    * Let listeners know of change
    */
   private void fireHistoryChange()
   {
      dump();//debug
      
//      HistoryEvent event = new HistoryEvent(isFirst(), isLast());
//      for (int i=0;i<listeners.size();i++)
//      {
//         ((HistoryListener) listeners.get(i)).historyChanged(event);
//      }
   }

   /**
    * Returns true if cursor is on last history item
    */
   public boolean isLast()
   {
      return (historyCursor == history.size()-1);
   }
   
   /**
    * Returns true if cursor is on first history item
    */
   public boolean isFirst()
   {
      return (historyCursor == 0) || (historyCursor == -1);
   }
   
   /**
    * Returns true if there is a previous item
    */
   public boolean isPrevious()
   {
      return (historyCursor > 0);
   }
   
   /**
    * Returns true if there is a next item
    */
   public boolean isNext()
   {
      return (historyCursor < history.size()-1);
   }
   /**
    * For debug - dump history to Log
    */
   public void dump()
   {
      //dump
      String s = "History: ";
      for (int i=0;i<history.size();i++)
      {
         if (i == historyCursor)
            s = s + ">>"+history.elementAt(i)+"<<, ";
         else
            s = s + ""+history.elementAt(i)+", ";
      }
      Log.trace(s);
   }
   
   /**
    * Although the History class exposes its action methods
    * (moveBack(), etc), it can also be given instructions via
    * ActionEvents.  in other words, you can create a "Back" Button,
    * set its 'actionCommand'  to a History command (see HISTORY_CMD_*),
    * and register a History instance with it, and Robert is your
    * father's brother.
    */
   public void actionPerformed(ActionEvent anEvent)
   {
      if (anEvent.getActionCommand() == HISTORY_CMD_BACK)
         moveBack();
      else if (anEvent.getActionCommand() == HISTORY_CMD_FWD)
         moveForward();
      else if (anEvent.getActionCommand() == HISTORY_CMD_CLEAR)
         clear();
   }
   
}

