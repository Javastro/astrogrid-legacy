/*
   $Id: JPasteButton.java,v 1.1 2002/11/27 16:20:06 mch Exp $

   Date       Author      Changes
   19 Oct 2002 M Hill      Created

   (c) Copyright...
*/

package org.astrogrid.ui;

import java.awt.event.ActionListener;
import javax.swing.JButton;
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

   public JPasteButton(JTextField aField)
   {
      super(IconFactory.getIcon("paste"));
      this.textField = aField;
      this.setToolTipText("Replaces text with clipboard contents");
      this.addActionListener(this);
   }


   public void actionPerformed(java.awt.event.ActionEvent e)
   {
      textField.selectAll();
      textField.paste();
   }


}

