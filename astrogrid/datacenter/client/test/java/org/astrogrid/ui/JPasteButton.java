/*
   $Id: JPasteButton.java,v 1.1 2004/02/17 16:04:06 mch Exp $

*/

package org.astrogrid.ui;

import java.awt.Dimension;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 * A button that pastes whatever is on the clipboard into the related
 * text field
 *
 * @author M Hill
 */


public class JPasteButton extends JButton implements ActionListener
{
   JTextField textField = null;
   JHistoryComboBox comboBox = null;

   private JPasteButton(int defHeight)
   {
      super(IconFactory.getIcon("Paste"));
      
      this.setToolTipText("Replaces text with clipboard contents");
      this.addActionListener(this);
      
      if (getIcon()==null)
      {
         setText("Paste");
      }
      else
      {
         setBorder(null);
         setPreferredSize(new Dimension(defHeight, defHeight));
      }
   }
   
   public JPasteButton(JTextField aField)
   {
      this(aField.getPreferredSize().height);
      
      this.textField = aField;
   }

   public JPasteButton(JHistoryComboBox aField)
   {
      this(aField.getPreferredSize().height);
      
      this.comboBox = aField;
   }

   public void actionPerformed(java.awt.event.ActionEvent e)
   {
      if (textField != null)
      {
         textField.selectAll();
         textField.paste();
      }
      
      if (comboBox != null)
      {
         //botch bit
         JTextField temp = new JTextField();
         temp.selectAll();
         temp.paste();
         
         comboBox.setItem(temp.getText());
      }
   }


}

/*
$Log: JPasteButton.java,v $
Revision 1.1  2004/02/17 16:04:06  mch
New Desktop GUI

Revision 1.2  2004/02/15 23:25:30  mch
Datacenter and MySpace desktop client GUIs

 */


