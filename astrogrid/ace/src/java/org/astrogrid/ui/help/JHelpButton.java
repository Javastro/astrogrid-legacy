/*
   $Id: JHelpButton.java,v 1.1 2003/08/25 18:36:38 mch Exp $

   Date       Author      Changes
   12 Dec 02  M Hill      Created

   (c) Copyright...
*/

package org.astrogrid.ui.help;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JTextField;

import org.astrogrid.ui.IconFactory;

/**
 * A button that shows the given help page when pressed
 *
 * @author M Hill
 */


public class JHelpButton extends JButton
{
   String page;
   String ref;

   public JHelpButton(String helpPage, String aRef)
   {
      super(IconFactory.getIcon("Help"));
      
      if (getIcon()==null)
      {
         setText("Help");
      }

      this.page = helpPage;
      this.ref = aRef;
      this.setToolTipText("Displays help on "+helpPage);

      this.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            Help.showHelp(page, ref, null);
         }
      });
      
   }


}

