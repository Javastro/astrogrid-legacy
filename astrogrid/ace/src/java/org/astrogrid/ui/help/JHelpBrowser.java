package org.astrogrid.ui.help;

import java.awt.*;
import javax.swing.*;
import java.net.URL;
import javax.swing.text.html.*;
import java.util.Vector;
import java.io.IOException;
import java.net.MalformedURLException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.astrogrid.log.Log;


/** JHelpBrowser
 *
 * A swing implementation of HelpBrowser, using HTMLDocument to view
 * the pages.
 *
 * @Created          : Feb 2001
 * @Last Update      :
 *
 * @author           : M Hill
 *
 */

public class JHelpBrowser extends JFrame implements HelpBrowser, ActionListener
{
   JEditorPane textPane = new JEditorPane();
   
   History history = new History();
   
   JButton doneButton;
   JButton backButton;
   JButton fwdButton;
   JButton helpButton;
   
   /**
    * Constructor
    */
   public JHelpBrowser()
   {
      super(Help.getBrowserTitle());
      
      textPane.setEditorKit(new HTMLEditorKit());
      JScrollPane scroller = new JScrollPane(textPane);
      
      JButton doneButton = newButton("Done", "Close Help");
      JButton backButton = newButton("<", "Return to previous page");
      JButton fwdButton = newButton(">", "Go to next visited page");
      JButton helpButton = newButton("Help", "Help on how to use Help");
      
      JPanel controlPanel = new JPanel();
      controlPanel.add(backButton);
      controlPanel.add(fwdButton);
      controlPanel.add(doneButton);
      
      getContentPane().setLayout(new BorderLayout());
      getContentPane().add(scroller, BorderLayout.CENTER);
      getContentPane().add(controlPanel, BorderLayout.SOUTH);
         
      new org.astrogrid.ui.EscEnterListener(this, doneButton, doneButton);
//    textPane.setBackground(Help.getBackground());
//    textPane.setForeground(Help.getForeground());
   }

   /**
    * Convenience routine that greates a standard button for this box
    */
   private JButton newButton(String label, String tooltip)
   {
      JButton b = null;
      try {
         String iconPath = label+".gif";
         ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(iconPath));
         if (icon != null)
            b = new JButton(icon);
      }
      catch (Exception e) {}
      if (b == null)
         b = new JButton(label);
      
      b.setToolTipText(tooltip);
      
      b.addActionListener(this);
      
      return b;
   }
   
   /**
    * Show a page at particular url
    */
   public void showHelp(URL helpUrl) throws HelpNotFoundException
   {
      String path = null;
      try
      {
         history.add(helpUrl);
         textPane.setText("Loading "+helpUrl+"...");
         textPane.setPage(helpUrl);
         show();
      }
      catch (IOException ioe)
      {
         throw new HelpNotFoundException("I/O Error for "+path,ioe);
      }
      
   }
   
   public void setEditable(boolean b)
   {
      textPane.setEditable(b);
   }
   
   public HelpBrowser createNewBrowser()
   {
      return new JHelpBrowser();
   }
   
   /**
    * Button has been pressed
    */
   public void actionPerformed(ActionEvent anEvent)
   {
      if (anEvent.getSource() == backButton)
      {
         try
         {
            showHelp( (URL) history.moveBack());
         }
         catch (HelpNotFoundException hnfe)
         {
            Log.logError("Failed to load help @"+history.getCurrentItem(),hnfe);
         }
      }
      else if (anEvent.getSource() == fwdButton)
      {
         try
         {
            showHelp( (URL) history.moveForward());
         }
         catch (HelpNotFoundException hnfe)
         {
            Log.logError("Failed to load help @"+history.getCurrentItem(),hnfe);
         }
      }
      else if (anEvent.getSource() == helpButton)
      {
         try
         {
            showHelp(Help.makePageUrl(HELP_ON_HELP,""));
         }
         catch (HelpNotFoundException hnfe)
         {
            Log.logError("Failed to load help no help",hnfe);
         }
         catch (MalformedURLException mfue)
         {
            Log.logError("Help on help url is bad ",mfue);
         }
      }
      else if (anEvent.getSource() == doneButton)
      {
         dispose();
      }
   }
   
   /**
    * Test harness
    */
   public static void main(String[] args)
   {
      Help.setMainBrowser(new JHelpBrowser());
      Help.setRoot("file:///temp//");
      Help.showHelp(Help.INDEX);
   }
      
}
