package net.mchill.log.ui;

import javax.swing.*;
import java.awt.*;
import net.mchill.log.*;
import java.text.SimpleDateFormat;

/**
 * A log handler that displays a pop up dialog box to show
 * log events. Defaults to filtering only info
 * messages and above.
 *
 * @version
 * @Created       Dec 2001
 * @Last Update   Dec 2001
 *
 * @author       M Hill
 */

public class PopupDialog extends JFrame implements FilteredLogHandler
{
   //keep reference to even itself so we can display in different ways
   LogEvent event = null;

   FilteredHandlerAdaptor filterAdaptor = new FilteredHandlerAdaptor();

   JTextPane userMessageArea = new JTextPane();
   JLabel iconArea = new JLabel();
   JButton continueButton = new JButton("Continue");
   JButton detailButton = new JButton("Detail ->");
   JTextPane detailArea = new JTextPane();
   JComponent detailPanel;

   /**
    * StatusLabel constructor - assembles components
    */
   public PopupDialog()
   {
      super();

      //button panel
      JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      buttonPanel.add(detailButton);
      buttonPanel.add(continueButton);

      //area normally seen by user
      JPanel userPanel = new JPanel(new BorderLayout());
      userPanel.setLayout(new BorderLayout());
      userPanel.add(iconArea, BorderLayout.WEST);
      userPanel.add(userMessageArea, BorderLayout.CENTER);
      userPanel.add(buttonPanel, BorderLayout.SOUTH);

      //detail area for stack dumps etc
      JScrollPane detailPanel = new JScrollPane(detailArea);

      getContentPane().add(userPanel, BorderLayout.NORTH);
      getContentPane().add(detailPanel, BorderLayout.SOUTH);

      showDetails(false);

      addFilter(new SeverityFilter(Severity.INFO));

      new EscEnterListener(this, continueButton, continueButton);
   }

   /**
    * Show or hide details panel
    */
   private void showDetails(boolean b)
   {
      if (b)
      {
         detailPanel.show();
      }
      else
      {
         detailPanel.hide();
      }
   }

   /**
    * Message received
    */
   public void messageLogged(LogEvent anEvent)
   {
      if (!filterAdaptor.keepEvent(anEvent))
         return;

      event = anEvent;

      //set colours corresponding to severity
      iconArea.setBackground(event.getSeverity().getBackground());

      //set the date label based on thae timestamp
      SimpleDateFormat formatter = new SimpleDateFormat ("dd/MM/yy HH:mm:ss");
      String dateString = formatter.format( event.getTimestamp() );

//    dateLabel.setText(dateString+" ");

      //make up text
      String s = event.getText();
      if (event.getSeverity() == Severity.ERROR)
         s = "ERROR "+s;   //slightly different from alarm
      if (event.getException() != null)
         s = s + " <"+event.getException()+">";

      userMessageArea.setText(s);

      //adjust for any change of widths.
      validate(); //adjust for change of widths.

      //set tool tips to give more info
//    textLabel.setToolTipText(dateString+" "+event.getText());   //allows much wider display
//    dateLabel.setToolTipText(dateString+" "+event.getText());   //allows much wider display
//    dateLabel.setToolTipText( //set day of week

      show();
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
