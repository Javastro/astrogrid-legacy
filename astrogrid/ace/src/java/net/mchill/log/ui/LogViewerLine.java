package net.mchill.log.ui;

/**
 * Description: A line that displays a particular event on a LogViewPanel.
 * Designed to be created around an existing LogEvent
 *
 * @version          :
 * @Created          : Oct 2000
 * @Last Update      : Aug 2001
 *
 * @author           : M Hill
 * @author           :
 *
 *
 *--------------------------------------------------------------------*/

import java.awt.*;
import net.mchill.log.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

import java.text.SimpleDateFormat;

public class LogViewerLine extends Panel
{
   JTextPane textLabel = new JTextPane();
   Label dateLabel = new Label();
   LogEvent event = null;
   boolean showStack = false;

   /**
    * StatusLabel constructor - assembles components, sets event
    */
   public LogViewerLine(LogEvent anEvent)
   {
      super();
      event = anEvent;
   
      StringWriter writer = new StringWriter();
      
      if (anEvent.getSeverity() == Severity.ERROR)
         writer.write("ERROR "); //slightly different from alarm
      writer.write(anEvent.getText());

      if (anEvent.getException() != null)
      {
         if (showStack)
         {
            writer.write("\n");
            writer.write(anEvent.getStackTrace());
         }
         else
         {
            writer.write(" <"+anEvent.getException()+">");
            textLabel.setToolTipText(anEvent.getStackTrace());
         }
      }
      
      textLabel.setText(writer.toString());
      
      //set colours corresponding to severity
      dateLabel.setBackground(anEvent.getSeverity().getBackground());
      textLabel.setBackground(anEvent.getSeverity().getBackground());
      dateLabel.setForeground(anEvent.getSeverity().getForeground());
      textLabel.setForeground(anEvent.getSeverity().getForeground());
      
      //set the date label based on thae timestamp
      SimpleDateFormat formatter = new SimpleDateFormat ("HH:mm:ss");
      String dateString = formatter.format( anEvent.getTimestamp() );
      
      dateLabel.setText(dateString+" ");

      //positioning, etc
      setLayout(new BorderLayout());
      Panel datePanel = new Panel(new BorderLayout());
      
      add(datePanel, BorderLayout.WEST);
      add(textLabel, BorderLayout.CENTER);
      
      datePanel.add(dateLabel, BorderLayout.NORTH);
      
   }
   
}
