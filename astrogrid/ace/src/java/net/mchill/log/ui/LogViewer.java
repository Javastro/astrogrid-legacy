/**
 * A Frame with a text area that is used to display messages going to the
 * current log.  Includes filter buttons.
 */
package net.mchill.log.ui;

import net.mchill.log.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class LogViewer extends Panel implements ItemListener
{
   Panel northControls = new Panel(new FlowLayout(FlowLayout.LEFT));
   LogViewPanel eventPanel = new LogViewPanel();
   SeverityFilter severityFilter = new SeverityFilter(Severity.TRACE);
   Choice severityCombo = new Choice();
   
   /**
    * Blank Constructor
    */
   public LogViewer()
   {
      setLayout(new BorderLayout());

      //severity filter button
      Enumeration i = Severity.getEnumeration();
      while (i.hasMoreElements())
         severityCombo.addItem(i.nextElement().toString());
      severityCombo.select(Severity.TRACE.toString());
      northControls.add(severityCombo);
      severityCombo.addItemListener(this);
      eventPanel.addFilter(severityFilter);
      
      //date range filter fields
      
      
      add(northControls, BorderLayout.NORTH);
      add(eventPanel, BorderLayout.CENTER);
   }

   /** Item listener - choice selected */
   public void itemStateChanged(ItemEvent anEvent)
   {
      severityFilter.setSeverity(Severity.getSeverity(severityCombo.getSelectedItem()));
      eventPanel.reFilter();
   }
   /**
    * Test harness
    * Creates a frame to put the above in
    */
   public static void main(String[] args)
   {
      // Create application frame.
      Frame frame = new Frame("Log Message Viewer");
      LogViewer viewer = new  LogViewer();
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
      
      Log.addHandler(viewer.eventPanel);
      Log.addHandler(new Log2Console());
      Log.logInfo("Monitor started...");
      Log.logWarning("Example warning...");
      Log.logAlarm("Example Alarm...");
      Log.logError("Example Error...");
      Log.trace("Example trace...");
      Log.logError("Example error with exception",new Exception("Wibble"));
      
      // Show frame.
      frame.pack();
      frame.setSize(400,300);
      frame.show();
   }
   
   
}
