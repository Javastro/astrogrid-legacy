/**
 * JVotBox.java
 *
 * Implements a simple JFC (Swing 1.1) dialog. To show this dialog use:
 *
 * JVotBox dialog = new JVotBox(frame);
 * dialog.pack();
 * dialog.show();
 *
 * where frame is an JFC frame which will be the owner of the dialog.
 *
 *
 * @author Created by Omnicore CodeGuide
 */

package org.astrogrid.tools.votable;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.astrogrid.ui.EscEnterListener;
import org.astrogrid.ui.IconFactory;


public class JVotBox extends JFrame
{
   JVot votView = null;
   JVotController votController = null;
   
   
   /**
    * Initializes the dialog.
    */
   public JVotBox(Frame owner)
   {
      super("VOTable Viewer");
      getContentPane().setLayout(new BorderLayout());

      // add vot table panel
      votView = new JVot();
      getContentPane().add(votView.newVotScrollPanel(), BorderLayout.CENTER);
      votController = new JVotController(votView);

      
      
      // Add window listener.
      this.addWindowListener
      (
         new WindowAdapter()
         {
            /**
             * Called when window close button was pressed.
             */
            public void windowClosing(WindowEvent e)
            {
               setVisible(false);
            }
         }
      );

      // add toolbar along the top
      JToolBar toolbar = new JToolBar();
      getContentPane().add(toolbar, BorderLayout.NORTH);

      JButton saveButton = new JButton(IconFactory.getIcon("SaveAs"));
      if (saveButton.getIcon() == null)
      {
         saveButton.setText("Save As");
      }
      saveButton.setToolTipText("Save VOTable to file");
      saveButton.setActionCommand(JVotController.SAVEAS_CMD);
      saveButton.addActionListener(votController);
      toolbar.add(saveButton);

      JButton loadButton = new JButton(IconFactory.getIcon("Open"));
      if (loadButton.getIcon() == null)
      {
         loadButton.setText("Open");
      }
      loadButton.setToolTipText("Load new VOTable from file");
      loadButton.setActionCommand(JVotController.LOAD_CMD);
      loadButton.addActionListener(votController);
      toolbar.add(loadButton);

      new EscEnterListener(this, null, null);

      validate();
   }

   /**
    * Expose the JVot component
    */
   public JVot getVotTable()
   {
      return votView;
   }

   /**
    * Expose the JVotController component
    */
   public JVotController getVotController()
   {
      return votController;
   }
   /**
    * Shows the frame.
    */
   public void show()
   {
      // You can also use: Dimension size = new Dimension(300,200);
      Dimension size = getPreferredSize();
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      setBounds
      (
         (screenSize.width - size.width) / 2,
         (screenSize.height - size.height) / 2,
         size.width,
         size.height
      );

      super.show();
   }

   /**
    * Test harness and standalone VOTable viewer
    */
   public static void main(String [] args)
   {
     net.mchill.log.Log.addHandler(new net.mchill.log.Log2Console());
     net.mchill.log.Log.addHandler(new net.mchill.log.ui.Log2Popup());

      JVotBox vb = new JVotBox(null);

      vb.show();
   }
    /**/


}

