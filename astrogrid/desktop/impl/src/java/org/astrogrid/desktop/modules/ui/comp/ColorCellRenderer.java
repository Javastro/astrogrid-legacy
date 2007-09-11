/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

/** cell renderer that just displays a block of colour */
public class ColorCellRenderer extends BasicComboBoxRenderer {
    private final static Dimension preferredSize = new Dimension(16, 16);
    public ColorCellRenderer() {
        setIcon(ico);
    }
    ColorSwatchIcon ico = new ColorSwatchIcon(Color.WHITE,preferredSize);

    public Component getListCellRendererComponent(JList list, Object value,
        int index, boolean isSelected, boolean cellHasFocus) {
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
           setForeground(list.getForeground());
        }
      if (value instanceof Color) {
          ico.setColor((Color)value);
      }
      return this;
    }
  }