/**
 * A panel that listens to the log, adding a LogViewerLine for each
 * filtered logEvent.
 * If the filtering changes, it starts listening again (ie clears, and
 * relistens for new ones), as it does not keep track of events it has
 * filtered out.
 */
package net.mchill.log.ui;

import net.mchill.log.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class LogViewPanel extends ScrollPane implements FilteredHandler
{
   FilteredHandlerAdaptor adaptor = new FilteredHandlerAdaptor();
   Panel northCrammer = new Panel();   //keeps the events to the north
// Panel eventViewer = new Panel();
   Box eventViewer = new Box(BoxLayout.Y_AXIS);
   Vector events = new Vector();
   
   /** Constructor */
   public LogViewPanel()
   {
      northCrammer.setLayout(new BorderLayout());
//    eventViewer.setLayout(new BoxLayout(eventViewer,BoxLayout.Y_AXIS));
      northCrammer.add(eventViewer, BorderLayout.NORTH);
      add(northCrammer);
   }

   /** Implementation of FilteredLogHandler.messageLogged.
    * Adds a new LogViewerLine for each event - the line
    * must know whether to be visible or not depending ojn
    * filtering */
   public void messageLogged(LogEvent anEvent)
   {
      events.add(anEvent);
      if (keepEvent(anEvent))
      {
         LogViewerLine newLine = new LogViewerLine(anEvent);
         eventViewer.add(newLine);
         validate();
         setScrollPosition(newLine.getLocation());//and scroll to bottom
      }
//    if (keepEvent(anEvent))
//       append(""+anEvent+"\n");
   }
   
   /** Applies given filter and passes through to adaptor */
   public void addFilter(LogFilter aFilter)  {  clear(); adaptor.addFilter(aFilter); }
   
   /** Pass through to adaptor */
   public void removeFilter(LogFilter aFilter){ clear(); adaptor.removeFilter(aFilter); }

   /** Pass through to adaptor */
   private boolean keepEvent(LogEvent anEvent)
   {
      return adaptor.keepEvent(anEvent);
   }

   public void clear()
   {
      eventViewer.removeAll();
      events.clear();
      setScrollPosition(0,0);
      validate();
   }
   
   /** re-Applies filter to all lines */
   public void reFilter()
   {
      removeAll();
      
      Object[] eventArray = events.toArray();
      
      for (int i=0; i<eventArray.length; i++)
      {
         if (keepEvent((LogEvent) eventArray[i]))
            eventViewer.add(new LogViewerLine((LogEvent) eventArray[i]));
      }
      validate();
   }
   
   /**
    * Test harness
    * Creates a frame to put the above in
    */
   public static void main(String[] args)
   {
      // Create application frame.
      Frame frame = new Frame();
      LogViewPanel viewer = new  LogViewPanel();
      frame.add(viewer);

      // Add standard window close listener.
      frame.addWindowListener(
         new WindowAdapter()
         {
            public void windowClosing(WindowEvent e)
            {
               System.exit(0);
            }
         }
      );
      
      Log.addHandler(viewer);
      Log.addHandler(new Log2Console());
      Log.logInfo("Monitor started...");
      Log.logWarning("Example warning...");
      Log.logAlarm("Example Alarm...");
      Log.logError("Example Error...");
      Log.trace("Example trace...");
      Log.logError("Example error with exception",new Exception("Wibble"));
      
      // Show frame.
      frame.pack();
      frame.show();
   }
   
   
}
