package net.mchill.log.ui;

/***********************************************************************
/**
 * Status bar containing a Status label that listens to the
 *  event log, displaying message text, and an 'ack' button to clear the label.
 *
 *  @version          :
 *  @Created          :  (25/01/01 19:48:14)
 *  @Last Update      :
 *
 *  @author           :
 *  @author           :
 *  @author           :
 *
 * --------------------------------------------------------------------
 */
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Dimension;
import net.mchill.log.*;

public class StatusBar extends javax.swing.JPanel {

   private  JButton ackButton = null;
   protected StatusLabel statusLabel = null;

   /**
    * StatusBar constructor comment.
    */
   public StatusBar()
   {
      super();

      //set up status label
      statusLabel = new StatusLabel();
      statusLabel.setName("label");
//    statusLabel.setBorder(new javax.swing.border.EtchedBorder());

      //ack button
      ackButton = new JButton();
      ackButton.setName("AckButton");
      ackButton.setFont(new java.awt.Font("dialog.bold", 1, 10));
      ackButton.setText("Ack");
//    ackButton.setPreferredSize(new Dimension(60,40));
      ackButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent e) {
               statusLabel.clear();
         }
      });

      setName("StatusBar");
      setLayout(new BorderLayout());
      add(statusLabel, BorderLayout.CENTER);
      add(ackButton, BorderLayout.WEST);

   }

   public LogHandler getLogHandler()
   {
      return statusLabel;
   }
}
