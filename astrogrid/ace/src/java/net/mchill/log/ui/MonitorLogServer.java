/**
 * A Frame with a text area that is used to display incoming data from a TCP/IP connection.
 * Used to monitor Log2Clients - in other words, this monitors the Log
 * Server - it is not a Server that monitors logs... errr
 */
package net.mchill.log.ui;

import net.mchill.log.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Frame class.
 */
public class MonitorLogServer extends Frame
{
   ClientLog clientLog = null;
   
   LogViewPanel logView = null;
   Checkbox connectToggle = null;
   TextField addressField = null;
   TextField portField = null;
   StatusLabel statusLine = null;

// String lastAddress = "";
// int lastPort = 0;
   
   /**
    * The constructor. This method is called when the frame is created.
    */
   public MonitorLogServer()
   {
      super("Monitoring Log Server (no connection)");
      
      // Create log view field
      logView = new LogViewPanel();
      
      clientLog = new ClientLog("",0);
      
      this.setLayout(new BorderLayout());
      this.add(logView, BorderLayout.CENTER);
      
      //make up connection panel
      Panel connPanel = new Panel();
      connPanel.setLayout(new BorderLayout());
      Panel connPanel2 = new Panel();
      connPanel2.setLayout(new BorderLayout());
      
      addressField = new TextField("localhost");
      portField = new TextField(""+Log2Clients.DEFAULT_PORT);
      connectToggle = new Checkbox("Monitor");

      connPanel2.add(addressField, BorderLayout.CENTER);
      connPanel2.add(portField, BorderLayout.EAST);
      
      connPanel.add(connPanel2, BorderLayout.CENTER);
      connPanel.add(connectToggle, BorderLayout.EAST);
      
      this.add(connPanel, BorderLayout.NORTH);
      
      //make up status panel
      Panel statusPanel = new Panel();
      statusPanel.setLayout(new BorderLayout());
      statusLine = new StatusLabel();
      statusPanel.add(statusLine, BorderLayout.CENTER);
      this.add(statusPanel, BorderLayout.SOUTH);

      // Add connection action
      connectToggle.addItemListener(
         new ItemListener()
         {
            /**
             * Called when window close button was pressed.
             */
            public void itemStateChanged(ItemEvent e)
            {
               boolean monitor = connectToggle.getState();
               if (monitor)
               {
                  try {
                     clientLog.setAddress(addressField.getText(), Integer.parseInt(portField.getText()));
                     clientLog.start();
                  }
                  catch (NumberFormatException nfe)
                  {
                     Log.logWarning("Port Number format incorrect");
                     monitor = false;
                  }
               }
               else
                  clientLog.stop();
               
               addressField.setEnabled(!monitor);
               portField.setEnabled(!monitor);
            }
         }
      );
      
      // Add window listener.
      this.addWindowListener(
         new WindowAdapter()
         {
            /**
             * Called when window close button was pressed.
             */
            public void windowClosing(WindowEvent e)
            {
               Log.logInfo("...closing Monitor Log Server at "+new Date());
               System.exit(0);
            }
         }
      );

   }

   
   /**
    * Main - so that this class can be run as a standalone application.
    * Creates a frame to put the above in
    */
   public static void main(String[] args)
   {
      MonitorLogServer frame = new  MonitorLogServer();
      
      Log.addHandler(frame.statusLine);
      Log.addHandler(frame.logView);
      
      
      frame.logView.addFilter(new ForSourceFilter(frame.clientLog.getSource()));
      frame.statusLine.addFilter(new NotSourceFilter(frame.clientLog.getSource()));
      
      Log.addHandler(new Log2Console());
      Log.addHandler(new Log2File("log.txt"));
      Log.logInfo("Monitor started...");
      
      // Show frame.
      frame.pack();
      frame.show();
   }
   
   
   
}
