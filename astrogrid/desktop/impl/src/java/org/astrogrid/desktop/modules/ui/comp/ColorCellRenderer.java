/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

/** cell renderer that  displays a block of colour */
public class ColorCellRenderer extends BasicComboBoxRenderer {
    private final static Dimension preferredSize = new Dimension(16, 16);
    public ColorCellRenderer() {
        setIcon(ico);
    }
    ColorSwatchIcon ico = new ColorSwatchIcon(Color.WHITE,preferredSize);

    public Component getListCellRendererComponent(final JList list, final Object value,
        final int index, final boolean isSelected, final boolean cellHasFocus) {
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