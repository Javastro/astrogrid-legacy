/*
   $Id: JSetFileButton.java,v 1.6 2004/04/15 16:34:53 mch Exp $

   (c) Copyright...
*/

package org.astrogrid.ui;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.filechooser.FileFilter;

/**
 * A ready made button that when pressed brings up a file selection dialog
 * box to pick a file for a text field.
 *
 * @version %I%
 * @author M Hill
 */

public class JSetFileButton extends JButton implements ActionListener
{
   private JComboBox comboBox = null;
   private JTextField textField = null;
   private JFileChooser fileChooser = new JFileChooser();

   public JSetFileButton()
   {
      super(IconFactory.getIcon("Open"));

      addActionListener(this);

      if (getIcon() == null)
      {
         setText("Set");
      }
      else
      {
         //make an effort to size the button to the icon
//      setPreferredSize(SIZE);
//      setMinimumSize(SIZE);
//      setMaximumSize(SIZE);
//      getBorder().getBorderInsets(this).top=0;
         setBorder(null);
//       setMargin(new Insets(0,0,0,0));
      }
      setToolTipText("Choose file from browser");
   }

   public JSetFileButton(JTextField textField)
   {
      this();
      setTextField(textField);
   }


   /**
    * Used if special file choosers are required
    */
   public void setChooser(JFileChooser specialChooser)
   {
      fileChooser = specialChooser;
   }

   /**
    * Used to access chooser properties (eg default dir)
    */
   public JFileChooser getChooser()
   {
      return fileChooser;
   }

   /**
    * Used if special file filter is required
    */
   public void addChoosableFilter(FileFilter specialFilter)
   {
      fileChooser.addChoosableFileFilter(specialFilter);
   }

   /**
    * Used if special file filter is required
    */
   public void setFilter(FileFilter specialFilter)
   {
      fileChooser.setFileFilter(specialFilter);
   }


   /**
    * Set the input text field that will show the path to the chosen file
    */
   public void setTextField(JTextField givenField)
   {
      textField = givenField;

      if (getIcon() != null)
      {
         setPreferredSize(new Dimension(
//                          this.getPreferredSize().width,
                             textField.getPreferredSize().height,
                             textField.getPreferredSize().height
         ));
      }
   }

   /**
    * Set an editable combo box that will show the path to the chosen file
    */
   public void setComboBox(JComboBox givenCombo)
   {
      comboBox = givenCombo;

      if (getIcon() != null)
      {
         setPreferredSize(new Dimension(
//                          this.getPreferredSize().width,
                             comboBox.getPreferredSize().height,
                             comboBox.getPreferredSize().height
         ));
      }

   };

   /**
    * Invoked when an action occurs - ie wehn the button is pressed. Displays
    * the file chooser and puts the chosen file into the text field
    */
   public void actionPerformed(ActionEvent e)
   {
      int returnVal = fileChooser.showDialog(this, "Set");

      if (returnVal == JFileChooser.APPROVE_OPTION)
      {
         fileChosen(getChosenFile());
      }
   }


   /** Sets the associated UI compronentso to the
    * given File
    */
   protected void fileChosen(File givenFile)
   {
      if (textField != null)
      {
         textField.setText(givenFile.getAbsolutePath());
      }

      if (comboBox != null)
      {
         comboBox.setSelectedItem(givenFile.getAbsolutePath());
      }
   }

   /**
    * Returns the file chosen
    */
   public File getChosenFile()
   {
      return fileChooser.getSelectedFile();
   }


   /**
    * "Test harness"
    */
/*   public static void main(String[] args)
   {
      StandardDialog sd = new StandardDialog((java.awt.Frame) null, "Test");

      JTextField textField = new JTextField("                  ");
      JSetFileButton button = new JSetFileButton("Set");

      button.setTextField(textField);

      sd.getContentPane().setLayout(new BorderLayout());
      sd.getContentPane().add(textField, BorderLayout.CENTER);
      sd.getContentPane().add(button, BorderLayout.EAST);

      sd.show();
   }
*/
}

/**
$Log: JSetFileButton.java,v $
Revision 1.6  2004/04/15 16:34:53  mch
Tidied up, introduced stuff from datacenter ui

Revision 1.1  2004/03/03 17:40:58  mch
Moved ui package

Revision 1.1  2004/02/17 16:04:06  mch
New Desktop GUI

Revision 1.1.1.1  2003/08/25 18:36:35  mch
Reimported to fit It02 source structure

Revision 1.6  2003/07/03 18:17:08  mch
Fixed double-line-spacing

 */

