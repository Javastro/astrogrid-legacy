package net.mchill.log.ui;

/**
 * Description: A very simple 'label' subclass that listens to the
 * messagelog and traps, and displays, all messages that are logged.
 * Made from awt components to allow extra portability.
 *
 * @version          :
 * @Created          : Oct 2000
 * @Last Update      : Aug 2001
 *
 * @author           : Chris Thomas
 * @author           : M Hill
 * @author           :
 *
 */
//--------------------------------------------------------------------

import javax.swing.*;
import java.awt.*;
import net.mchill.log.*;

import java.text.SimpleDateFormat;

public class StatusLabel extends JPanel implements FilteredHandler
{
   JLabel textLabel = new JLabel();
   JLabel dateLabel = new JLabel();

   //keep reference to even itself so we can display in different ways
   LogEvent event = null;

   FilteredHandlerAdaptor filterAdaptor = new FilteredHandlerAdaptor();

   /**
    * StatusLabel constructor - assembles components
    */
   public StatusLabel()
   {
      super();

      setLayout(new BorderLayout());

      add(dateLabel, BorderLayout.WEST);
      add(textLabel, BorderLayout.CENTER);

      // Swing feature
      dateLabel.setOpaque(false);
      textLabel.setOpaque(false);
      setOpaque(true);

      clear();
   }

   /**
    * Resets the status line by clearing the labels and setting the colour to light grey
    */
   public void clear()
   {
//    dateLabel.setText("                 ");   //room for dd/mm/yy hh:mm:ss
      dateLabel.setText("");
      textLabel.setText("");
      setBackground(Color.lightGray);
      textLabel.setToolTipText("");
      dateLabel.setToolTipText("");
   }

   /**
    * Update display with info from logged message
    */
   public void messageLogged(LogEvent anEvent)
   {
      if (!filterAdaptor.keepEvent(anEvent))
         return;

      event = anEvent;

      //set colours corresponding to severity
      dateLabel.setBackground(event.getSeverity().getBackground());
      textLabel.setBackground(event.getSeverity().getBackground());
      dateLabel.setForeground(event.getSeverity().getForeground());
      textLabel.setForeground(event.getSeverity().getForeground());

      //set the date label based on thae timestamp
      SimpleDateFormat formatter = new SimpleDateFormat ("dd/MM/yy HH:mm:ss");
      String dateString = formatter.format( event.getTimestamp() );

      dateLabel.setText(dateString+" ");

      //make up text
      String s = event.getText();
      if (event.getSeverity() == Severity.ERROR)
         s = "ERROR "+s;   //slightly different from alarm
      if (event.getException() != null)
         s = s + " <"+event.getException()+">";

      textLabel.setText(s);

      //adjust for any change of widths.
      validate(); //adjust for change of widths.

      //set tool tips to give more info
      textLabel.setToolTipText(dateString+" "+event.getText());   //allows much wider display
      dateLabel.setToolTipText(dateString+" "+event.getText());   //allows much wider display
//    dateLabel.setToolTipText( //set day of week
   }

   /**
    * Implementation of FilteredLogHandler, adds the given filter to allow/prevent certain
    * messages from appearing on the status line
    */
   public void addFilter(LogFilter aFilter)
   {
      filterAdaptor.addFilter(aFilter);
   }

   /**
    * Implementation of FilteredLogHandler, removes the given filter to allow/prevent certain
    * messages from appearing on the status line
    */
   public void removeFilter(LogFilter aFilter)
   {
      filterAdaptor.removeFilter(aFilter);
   }

}
