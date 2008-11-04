// Copyright (C) 2005 Mammoth Software LLC
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
//
// Contact the author at: info@mammothsoftware.com
package org.astrogrid.desktop.modules.ui.comp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.border.Border;

/**
 * A Drop Down Button.
 * 
 * @author m. bangham
 * Copyright 2005 Mammoth Software LLC
 * Edited nww - fixed some bugs, made it more open to extension
 */
public class DropDownButton extends JToolBar implements ActionListener {
	protected JPopupMenu popup = new JPopupMenu();
	protected JButton mainButton;
	protected JButton arrowButton;
	private final ActionListener mainButtonListener = new ActionListener() {
		public void actionPerformed(final ActionEvent e) {
			//NWW
			if (popup.getComponentCount() ==0) {
				return;
			}
			final Component component = popup.getComponent(0);
			if (component instanceof JMenuItem) {
				final JMenuItem item = (JMenuItem)component;
				item.doClick(0);
			}
		}
	};
	
	public void setEnabled(final boolean b) {
		super.setEnabled(b);
		mainButton.setEnabled(b);
		arrowButton.setEnabled(b);
	} 
	
	public DropDownButton(final String title,final Icon icon) {
	    this(new JButton(title,icon));
	}

	
	public DropDownButton(final JButton mainButton) {
		this(mainButton, new JButton(new DownArrow()));
	}
	
	public DropDownButton(final JButton mainButton, final JButton arrowButton) {
		this();
		this.mainButton = mainButton;
		this.arrowButton = arrowButton;
		init();
	}
	
	public JButton getMainButton() {
	    return mainButton;
	}
	
	public JButton getArrowButton() {
	    return arrowButton;
	}
	
	private DropDownButton() {
		super();
		setBorder(null);
	}
	
	public void updateUI() {
		super.updateUI();
		setBorder(null);
	}
	
	protected Border getRolloverBorder() {
		return BorderFactory.createRaisedBevelBorder();
	}
	

	
	private void init() {
		
      final Icon disDownArrow = new DisabledDownArrow();
      arrowButton.setDisabledIcon(disDownArrow);
      arrowButton.setMaximumSize(new Dimension(11,100));
      mainButton.addActionListener(this); 
      arrowButton.addActionListener(this);
      

      
      // Windows draws border around buttons, but not toolbar buttons
      // Using a toolbar keeps the look consistent.
      setBorder(null);
      setMargin(new Insets(0, 0, 0, 0));
      setFloatable(false);
      add(mainButton);
      add(arrowButton);
      
//      setFixedSize(mainButton, arrowButton);
  
	}
	/*
	 * Forces the width of this button to be the sum of the widths of the main
	 * button and the arrow button. The height is the max of the main button or
	 * the arrow button.
	 */
	private void setFixedSize(final JButton mainButton, final JButton arrowButton) {
      final int width = (int)(mainButton.getPreferredSize().getWidth() +
      					arrowButton.getPreferredSize().getWidth());
      final int height = (int)Math.max(mainButton.getPreferredSize().getHeight(),
      					arrowButton.getPreferredSize().getHeight());

      setMaximumSize(new Dimension(width, height));
      setMinimumSize(new Dimension(width, height));
      setPreferredSize(new Dimension(width, height));
	}
   
	/**
	 * Removes a component from the popup
	 * @param component
	 */
	public void removeComponent(final Component component) {
		popup.remove(component);
	}

	/**
	 * Adds a component to the popup
	 * @param component
	 */
	public Component addComponent(final Component component) {
		return popup.add(component);
	}
	
	/**
	 * Indicates that the first item in the menu should be executed
	 * when the main button is clicked
	 * @param isRunFirstItem True for on, false for off
	 */
	public void setRunFirstItem(final boolean isRunFirstItem) {
		mainButton.removeActionListener(this);
		if (!isRunFirstItem) {
			mainButton.addActionListener(this);
		} else {
            mainButton.addActionListener(mainButtonListener);
        }
	}
	
   /*------------------------------[ ActionListener ]---------------------------------------------------*/ 
	 
   public void actionPerformed(final ActionEvent ae){ 
        final JPopupMenu popup = getPopupMenu(); 
        popup.show(this, 0, this.getHeight()); 
    } 
   
   protected JPopupMenu getPopupMenu() { return popup; }
	
   /** an icon that draws a down arrow */
   private static class DownArrow implements Icon {
   	 
      Color arrowColor = Color.black;

      public void paintIcon(final Component c, final Graphics g, final int x, final int y) {
          g.setColor(arrowColor);
          g.drawLine(x, y, x+4, y);
          g.drawLine(x+1, y+1, x+3, y+1);
          g.drawLine(x+2, y+2, x+2, y+2);
      }

      public int getIconWidth() {
          return 6;
      }

      public int getIconHeight() {
          return 4;
      }

  }
   /** an icon that draws a disabled down arrow */
   private static class DisabledDownArrow extends DownArrow {
   	 
      public DisabledDownArrow() {
          arrowColor = new Color(140, 140, 140);
      }

      public void paintIcon(final Component c, final Graphics g, final int x, final int y) {
          super.paintIcon(c, g, x, y);
          g.setColor(Color.white);
          g.drawLine(x+3, y+2, x+4, y+1);
          g.drawLine(x+3, y+3, x+5, y+1);
      }
  }


}
